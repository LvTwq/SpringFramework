package com.lvmc.controller;

import com.lvmc.config.QueueBeanConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 吕茂陈
 * @description
 * @date 2025/2/20 16:17
 */
@Slf4j
@RequestMapping("rabbitmq")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RabbitMqController {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 简单消息传递（Point-to-Point）
     * 在简单的消息传递模式下，消息从生产者发送到消息队列，消费者从队列中获取消息。
     */
    @GetMapping("simple/{message}")
    public void sendMessage(@PathVariable String message) {
        rabbitTemplate.convertAndSend("simpleQueue", message);
    }


    /**
     * 发布/订阅（Publish/Subscribe）
     * 在发布/订阅模式中，生产者发送消息到交换机，多个消费者订阅该交换机来接收消息
     */
    @GetMapping("publish/{message}")
    public void sendMessage2(@PathVariable String message) {
        // FanoutExchange 不需要 routingKey
        rabbitTemplate.convertAndSend("fanoutExchange", "", message);
    }

    /**
     * 路由（Routing）
     * 在路由模式中，生产者发送消息到交换机，交换机根据 routing key 将消息路由到不同的队列。
     */
    @GetMapping("routing/{message}/{routingKey}")
    public void sendMessage3(@PathVariable String message, @PathVariable String routingKey) {
        rabbitTemplate.convertAndSend("directExchange", routingKey, message);
    }


    /**
     * 主题（Topic）
     * 在主题模式中，生产者发送消息到交换机，交换机根据 routing key 模式（支持通配符）将消息路由到不同的队列。
     */
    @GetMapping("topic/{message}/{routingKey}")
    public void sendMessage4(@PathVariable String message, @PathVariable String routingKey) {
        rabbitTemplate.convertAndSend("topicExchange", routingKey, message);
    }

    /**
     * header模式
     */
    @GetMapping("header/{message}")
    public void sendMessage5(@PathVariable String message) {
        // 这条消息应该能被两个队列都接收到，第一个队列 all 匹配成功，第二个队列 hello-value any匹配成功
        rabbitTemplate.convertAndSend("header_exchange", "", message, properties -> {
            properties.getMessageProperties().setHeaders(Map.of("matchAll", "YES", "hello", "world"));
            return properties;
        });

        // 这条消息应该只被第二个队列接受，第一个队列 all 匹配失败，第二个队列 matchAll-NO any匹配成功
        rabbitTemplate.convertAndSend("header_exchange", "", message, properties -> {
            properties.getMessageProperties().setHeaders(Map.of("matchAll", "NO"));
            return properties;
        });
    }


    @GetMapping("dead")
    public void publishDead() {
        rabbitTemplate.convertAndSend(QueueBeanConfig.NORMAL_EXCHANGE, "normal.topic", "123456789", message -> {
            //设置消息的生存时间 单位是ms
            message.getMessageProperties().setExpiration("5000");
            return message;
        });
    }


    @GetMapping("delayed")
    public void publish() {
        rabbitTemplate.convertAndSend(QueueBeanConfig.DELAYED_EXCHANGE, "delayed.hello", "99999", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置消息的延迟时间，单位为毫秒。
                message.getMessageProperties().setDelay(5000);
                return message;
            }
        });
        log.info("发送到delayed-queue队列的消息");
    }
}
