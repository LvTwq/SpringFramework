package com.lvmc.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 吕茂陈
 * @description
 * @date 2025/2/20 15:29
 */
@Configuration
public class QueueBeanConfig {

    /*
        以下是 简单的消息传递
    */

    @Bean
    public Queue simpleQueue() {
        return new Queue("simpleQueue");
    }


    /*
     以下是 Publish/Subscribe 即 Fanout（广播） 模型
     */
    @Bean
    public Queue publishQueue1() {
        return new Queue("publishQueue1");
    }

    @Bean
    public Queue publishQueue2() {
        return new Queue("publishQueue2");
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    public Binding publishBinding1() {
        return BindingBuilder.bind(publishQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding publishBinding2() {
        return BindingBuilder.bind(publishQueue2()).to(fanoutExchange());
    }


    /*
      以下是 路由（Routing），即 Direct（定向）模型
     */

    @Bean
    public Queue routingQueue1() {
        return new Queue("routingQueue1");
    }

    @Bean
    public Queue routingQueue2() {
        return new Queue("routingQueue2");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("directExchange");
    }

    @Bean
    public Binding routingBinding1() {
        return BindingBuilder.bind(routingQueue1()).to(directExchange()).with("routingKey1");
    }

    @Bean
    public Binding routingBinding2() {
        return BindingBuilder.bind(routingQueue2()).to(directExchange()).with("routingKey2");
    }


    /*
   以下是 主题 topic
  */
    @Bean
    public Queue topicQueue1() {
        return new Queue("topicQueue1");
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue("topicQueue2");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("first.#");
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("second.#");
    }


    /*
     header模式
     */

    @Bean
    public Queue headerFirstQueue() {
        return new Queue("header_first_queue");
    }

    @Bean
    public Queue headerSecondQueue() {
        return new Queue("header_second_queue");
    }

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange("header_exchange");
    }

    @Bean
    public Binding headerFirstQueueBindExchange() {
        Map<String, Object> headersMap = new HashMap<>(8);
        headersMap.put("matchAll", "YES");
        headersMap.put("hello", "world");

        return BindingBuilder.bind(headerFirstQueue()).to(headersExchange()).whereAll(headersMap).match();
    }

    @Bean
    public Binding headerSecondQueueBindExchange() {
        Map<String, Object> headersMap = new HashMap<>(8);
        headersMap.put("matchAll", "NO");
        headersMap.put("hello", "world");

        return BindingBuilder.bind(headerSecondQueue()).to(headersExchange()).whereAny(headersMap).match();
    }

    /*
      以下是死信队列
     */
    // 普通的交换机和队列
    public static final String NORMAL_EXCHANGE = "normal-exchange";
    public static final String NORMAL_QUEUE = "normal-queue";
    public static final String NORMAL_ROUTING_KEY = "normal.#";

    //死信队列和交换机
    public static final String DEAD_EXCHANGE = "dead-exchange";
    public static final String DEAD_QUEUE = "dead-queue";
    public static final String DEAD_ROUTING_KEY = "dead.#";


    /**
     * 创建普通的交换机
     *
     * @return
     */
    @Bean
    public Exchange normalExchange() {
        return ExchangeBuilder.topicExchange(NORMAL_EXCHANGE).build();
    }


    /**
     * 创建普通的队列，然后绑定死信队列和交换机
     *
     * @return
     */
    @Bean
    public Queue normalQueue() {
        //普通队列这里需要绑定死信交换机
        return QueueBuilder
                .durable(NORMAL_QUEUE)
                .deadLetterExchange(DEAD_EXCHANGE) //死信交换机
                .deadLetterRoutingKey("dead.hello") //绑定的routing_key
                .build();
    }

    /**
     * 绑定交换机和队列
     *
     * @return
     */
    @Bean
    public Binding normalBinding() {
        return BindingBuilder.bind(normalQueue()).to(normalExchange()).with(NORMAL_ROUTING_KEY).noargs();
    }

    /**
     * 创建普通的队列，然后绑定死信队列和交换机
     *
     * @return
     */
    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    /**
     * 创建死信交换机
     *
     * @return
     */
    @Bean
    public TopicExchange deadExchange() {
        return ExchangeBuilder.topicExchange(DEAD_EXCHANGE).build();
    }


    /**
     * 绑定交换机和队列
     *
     * @return
     */
    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_ROUTING_KEY);
    }


    public static final String DELAYED_EXCHANGE = "delayed-exchange";
    public static final String DELAYED_QUEUE = "delayed-queue";
    public static final String DELAYED_ROUTING_KEY = "delayed.#";

    /**
     * 创建一个延迟交换机（Exchange）并返回该交换机对象。
     *
     * @return
     */
    @Bean
    public Exchange delayedExchange() {
        //创建了一个HashMap对象，用于存储交换机的属性。然后，将一个名为x-delayed-type的属性和值为"topic"的键值对添加到HashMap中。
        HashMap<String, Object> map = new HashMap<>();
        map.put("x-delayed-type", "topic");

        /*
         使用CustomExchange类创建一个自定义交换机对象。CustomExchange是Spring AMQP库提供的一个类，用于创建自定义的交换机。构造方法的参数依次为交换机的名称、类型、是否持久化、是否自动删除和属性。
         交换机的名称为DELAYED_EXCHANGE，类型为"x-delayed-message"，持久化为true，自动删除为false，属性为之前创建的HashMap对象。
         */
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message", true, false, map);
    }

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE).build();
    }

    /**
     * 绑定交换机和队列
     *
     * @return
     */
    @Bean
    public Binding delayedBinding() {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with(DELAYED_ROUTING_KEY).noargs();
    }
}
