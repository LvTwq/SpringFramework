package com.southwind.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.southwind.config.RabbitConfiguration;
import com.southwind.entity.Account;
import com.southwind.entity.LoginParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
    private final RabbitTemplate rabbitTemplate;


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
        rabbitTemplate.convertAndSend(RabbitConfiguration.REGISTER_EXCHANGE, "", msg);
    }

    public void sendLoginMessage2(LoginParam msg) {
        String routingKey = msg.getSuccess() ? "" : "login_failed";
        rabbitTemplate.convertAndSend(RabbitConfiguration.LOGIN_EXCHANGE, routingKey, msg);
    }

}
