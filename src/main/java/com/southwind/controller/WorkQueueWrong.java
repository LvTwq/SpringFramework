package com.southwind.controller;

import cn.hutool.extra.spring.SpringUtil;
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
    private static final String QUEUE = "newuserQueue";


    @GetMapping
    public void sendMessage() {
        String name = UUID.randomUUID().toString();
        log.info("生成的name为：{}", name);
        // 发消息，设置routingKey为空
        rabbitTemplate.convertAndSend(EXCHANGE, "", name);
    }


    /**
     * 使用匿名（随机命名）队列作为消息队列
     */
    @Bean
    public Queue anonymousQueue() {
        return new AnonymousQueue();
    }


    /**
     * 声明DirectExchange交换器，绑定队列到交换器
     * 直接交换器根据routingKey对消息进行路由，由于程序每次启动都会创建匿名（随机命名）的队列
     * 所以如果起两个服务，两个服务都有对应独立的队列，以空routingKey绑定到直接交换器
     * 直接交换器发现两条队列都匹配，会发给两个
     *
     * @return
     */
    @Bean
    public Declarables declarables() {
        DirectExchange exchange = new DirectExchange(EXCHANGE);
        return new Declarables(anonymousQueue(), exchange, BindingBuilder.bind(anonymousQueue()).to(exchange).with(""));
    }


    /**
     * 监听队列，队列名称直接通过SpEL表达式引用Bean
     *
     * @param name
     */
    @RabbitListener(queues = "#{anonymousQueue.name}")
    public void memberService(String name) {
        log.info("memberService: 欢迎信息从{}发送给用户：{}", SpringUtil.getProperty("server.port"), name);
    }

}
