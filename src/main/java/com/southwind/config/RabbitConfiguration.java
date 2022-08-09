package com.southwind.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

/**
 * @author 吕茂陈
 * @date 2022-07-15 14:40
 */
//@Configuration
public class RabbitConfiguration {

    public static final String QUEUE = "newuserQueueCompensation";
    public static final String EXCHANGE = "newuserExchangeCompensation";
    public static final String ROUTING_KEY = "newuserRoutingCompensation";

    /**
     * 队列
     *
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    /**
     * 交换器
     *
     * @return
     */
    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.directExchange(EXCHANGE).durable(true).build();
    }

    /**
     * 绑定
     *
     * @return
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY).noargs();
    }
}