package com.lvmc.mqtt;

import cn.hutool.core.collection.CollUtil;
import com.lvmc.config.MqttConfig;
import com.lvmc.exception.MqttRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * 集成化的 MQTT 客户端工具类（支持单客户端同时发布和订阅）
 */
@Slf4j
@Configuration
public class MqttClientWrapper implements MqttCallbackExtended {

    private final String broker;
    private final String clientId;
    private final MqttConnectOptions options;
    private MqttClient client;
    private final List<MqttSubscriptTopic> mqttSubscriptTopic;
    private final ExecutorService publisherExecutor = Executors.newSingleThreadExecutor();
    private final Map<String, BiConsumer<String, String>> topicHandlers = new HashMap<>();


    public MqttClientWrapper(List<MqttSubscriptTopic> mqttSubscriptTopic, MqttConfig mqttConfig) {
        this.broker = mqttConfig.getHost();
        this.clientId = "java_123";
        this.options = buildDefaultOptions(mqttConfig);
        this.mqttSubscriptTopic = mqttSubscriptTopic;
        initialize();
    }

    private MqttConnectOptions buildDefaultOptions(MqttConfig mqttConfig) {
        MqttConnectOptions opts = new MqttConnectOptions();
        opts.setCleanSession(false);          // 保持持久会话，离线能接收 QoS 1/2 消息
        opts.setAutomaticReconnect(true);     // 自动重连
        opts.setMaxReconnectDelay(5000);      // 重连最大间隔
        opts.setConnectionTimeout(10);        // 连接超时时间
        //设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
        opts.setWill("willTopic", ("java_123与服务器断开连接").getBytes(), 0, false);
        //设置连接用户名
        opts.setUserName(mqttConfig.getUsername());
        //设置连接密码
        opts.setPassword(mqttConfig.getPassword().toCharArray());
        return opts;
    }

    // ================== 初始化连接 ==================
    private void initialize() {
        try {
            this.client = new MqttClient(broker, clientId, new MemoryPersistence());
            this.client.setCallback(this);  // 设置消息接收回调
            this.client.connect(options);
        } catch (MqttException e) {
            throw new MqttRuntimeException("MQTT 连接失败", e);
        }
    }

    // ================== 消息发送 ==================


    /**
     * @description: 同步发送消息（阻塞直到发送完成）
     * @author: 吕茂陈
     * @date: 2025/3/5 16:00
     * @param: topic
     * @param: payload
     * @param: qos 消息等级 0:消息最多传递一次，如果当时客户端不可用，则会丢失该消息。 1：包含了简单的重发机制，Sender 发送消息之后等待接收者的 ACK，如果没收到 ACK
     *      *                 则重新发送消息。这种模式能保证消息至少能到达一次，但无法保证消息重复。 2：保证消息到达对方并且严格只到达一次。
     * @param: retained 是否需要保留最新的消息到服务器上，重新连接MQTT服务时，需要接收该主题最新消息，设置retained为false;
     * @return: void
     */
    public void publish(String topic, String payload, int qos, boolean retained) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(qos);
            message.setRetained(retained);
            client.publish(topic, message);
        } catch (MqttException e) {
            throw new MqttRuntimeException("消息发送失败", e);
        }
    }

    /**
     * 异步发送消息（不阻塞主线程）
     */
    public void publishAsync(String topic, String payload, int qos, boolean retained) {
        publisherExecutor.execute(() -> publish(topic, payload, qos, retained));
    }

    // ================== 消息订阅 ==================

    /**
     * 订阅主题并绑定消息处理器（支持通配符）
     */
    public void subscribe(String topicFilter, int qos, BiConsumer<String, String> messageHandler) {
        try {
            client.subscribe(topicFilter, qos);
            topicHandlers.put(topicFilter, messageHandler);
        } catch (MqttException e) {
            throw new MqttRuntimeException("订阅失败: " + topicFilter, e);
        }
    }

    // ================== 回调处理 ==================
    @Override
    public void connectionLost(Throwable cause) {
        log.info("[MQTT] 连接丢失，尝试重连...", cause);
        try {
            this.client.connect(options);
            log.info("重连成功");
        } catch (MqttException e) {
            throw new MqttRuntimeException("MQTT 连接失败", e);
        }
    }

    /**
     * 接收到消息时触发（自动分发到对应的处理器）
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        // 查找匹配的通配符处理器（例如 "sensors/#" 匹配 "sensors/temp"）
/*        topicHandlers.entrySet().stream()
                .filter(entry -> matchTopic(topic, entry.getKey()))
                .findFirst()
                .ifPresent(entry -> entry.getValue().accept(topic, payload));*/

        mqttSubscriptTopic.forEach(method -> {
            if (MqttTopic.isMatched(method.realTopic(), topic)) {
                try {
                    method.messageArrived(topic, message);
                } catch (Exception e) {
                    log.error("消费失败,原因:", e);
                }

            }
        });
    }

    /**
     * MQTT 通配符匹配逻辑（支持 # 和 +）
     */
    private boolean matchTopic(String actualTopic, String subscribedFilter) {
        return actualTopic.matches(subscribedFilter
                .replace("+", "[^/]+")
                .replace("#", ".+"));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // QoS 1/2 的发送确认（可选记录日志）
    }

    // ================== 资源管理 ==================

    /**
     * 安全关闭客户端（释放所有资源）
     */
    public void close() {
        try {
            if (client != null) {
                client.disconnect();
                client.close();
            }
            publisherExecutor.shutdown();
        } catch (MqttException e) {
            // 记录日志
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        log.info("完成连接订阅队列");
        subscribe();
    }

    private void subscribe() {
        if (CollUtil.isNotEmpty(mqttSubscriptTopic)) {
            try {
                //订阅队列
                client.subscribe(mqttSubscriptTopic.stream().map(MqttSubscriptTopic::getTopic).toArray(String[]::new));
            } catch (Exception e) {
                log.error("订阅失败，原因", e);
            }
        }
    }


}

