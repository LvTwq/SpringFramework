package com.southwind.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 会员服务 监听 用户服务 发出的新用户注册消息
 *
 * @author 吕茂陈
 * @date 2022-07-15 16:32
 */
@Slf4j
@Configuration
@RestController
@RequestMapping("workqueuewrong")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WorkQueueWrong {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "newuserExchange";


    @GetMapping
    public void sendMessage() {
        rabbitTemplate.convertAndSend(EXCHANGE, "", UUID.randomUUID().toString());
    }


    /**
     * 使用匿名队列作为消息队列
     */
    @Bean
    public Queue queue() {
        return new AnonymousQueue();
    }


    /**
     * 声明DirectExchange交换器，绑定队列到交换器
     *
     * @return
     */
    @Bean
    public Declarables declarables() {
        DirectExchange exchange = new DirectExchange(EXCHANGE);
        return new Declarables(queue(), exchange, BindingBuilder.bind(queue()).to(exchange).with(""));
    }


    /**
     * 监听队列，队列名称直接通过SpEL表达式引用Bean
     *
     * @param name
     */
    @RabbitListener(queues = "#{queue.name}")
    public void memberService(String name) {
        log.info("memberService: 欢迎信息从{}发送给用户：{}", System.getProperty("server.port"), name);
    }
}
