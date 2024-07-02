package com.lvmc.service.impl;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvmc.entity.Account;
import com.lvmc.entity.LoginParam;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author 吕茂陈
 * @date 2022-08-10 11:18
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MessagingService {

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final RabbitTemplate rabbitTemplate;


    public void sendRegistrationMessage(Account account) throws IOException {
        send("topic_account", account);
    }

    public void sendLoginMessage(LoginParam msg) throws IOException {
        send("topic_login", msg);
    }

    private void send(String topic, Object msg) throws IOException {
        // 消息正文是序列化的json
        ProducerRecord<String, String> pr = new ProducerRecord<>(topic, objectMapper.writeValueAsString(msg));
        // 把消息类型作为Header添加到消息中，Header名称为type，值为class全名
        pr.headers().add("type", msg.getClass().getName().getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(pr);
    }


    public void sendRegistrationMessage2(Account msg) {
//        rabbitTemplate.convertAndSend(RabbitConfiguration.REGISTER_EXCHANGE, "", msg);
    }

    public void sendLoginMessage2(LoginParam msg) {
        String routingKey = msg.getSuccess() ? "" : "login_failed";
//        rabbitTemplate.convertAndSend(RabbitConfiguration.LOGIN_EXCHANGE, routingKey, msg);
    }


    /**
     * 可以用 ProducerRecord 包裹，也可以不用
     */
    public void sendMessage() {
        String jsonStr = JSONUtil.toJsonStr(Account.builder().id(1).username("myTopic").build());
        // 调用 send() 方法实际上返回的是 ListenableFuture 对象
        kafkaTemplate.send("myTopic", jsonStr);

        // 同步发送，可以拿到消息发送的结果（不推荐，没利用到Future对象的特性）
        try {
            String jsonStr2 = JSONUtil.toJsonStr(Account.builder().id(1).username("myTopic2").build());
            SendResult<String, String> result = kafkaTemplate.send("myTopic2", jsonStr2).get();
            if (result.getRecordMetadata() != null) {
                log.info("生产者成功发送消息到：{}",
                    result.getProducerRecord().topic() + "-> " + result.getProducerRecord().value());
            }
        } catch (Exception e) {
            log.error("生产者发送失败", e);
        }

        String jsonStr3 = JSONUtil.toJsonStr(Account.builder().id(1).username("myTopic3").build());
        // 优化
        ListenableFuture<SendResult<String, String>> myTopic3 = kafkaTemplate.send("myTopic3", jsonStr3);
        myTopic3.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onFailure(Throwable ex) {
                log.error("生产者发送消息失败", ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("生产者成功发送消息到：{}",
                    result.getProducerRecord().topic() + "-> " + result.getProducerRecord().value());
            }
        });

        String jsonStr4 = JSONUtil.toJsonStr(Account.builder().id(100).username("myTopic4").build());
        ProducerRecord<String, String> record = new ProducerRecord<>("myTopic4", jsonStr4);
        kafkaTemplate.send(record);


    }


}
