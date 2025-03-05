package com.lvmc.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;

/**
 * @author 吕茂陈
 * @description
 * @date 2025/3/3 16:56
 */
public interface MqttSubscriptTopic extends IMqttMessageListener {

    /**
     * 订阅的topic
     *
     * @return
     */
    String getTopic();

    /**
     * 监听的topic
     *
     * @return
     */
    default String realTopic() {
        return getTopic();
    }
}
