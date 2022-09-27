package com.southwind.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * @author 吕茂陈
 * @date 2022-07-15 14:40
 */
//@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RabbitConfiguration {

//    private final RabbitTemplate rabbitTemplate;

    public static final String QUEUE = "newuserQueueCompensation";
    public static final String EXCHANGE = "newuserExchangeCompensation";
    public static final String ROUTING_KEY = "newuserRoutingCompensation";

    /**
     * 创建队列
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


    /**
     * MessageConverter用于将Java对象转换为RabbitMQ的消息。
     * 默认情况下，Spring Boot使用SimpleMessageConverter，只能发送String和byte[]类型的消息，不太方便。
     * 使用Jackson2JsonMessageConverter，我们就可以发送JavaBean对象，由Spring Boot自动序列化为JSON并以文本消息传递
     *
     * @return
     */
    @Bean
    public MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    public static final String Q_APP = "q_app";
    public static final String Q_MAIL = "q_mail";
    public static final String Q_SMS = "q_sms";
    public static final String REGISTER_EXCHANGE = "registration";
    public static final String LOGIN_EXCHANGE = "login";

    @Bean
    public Queue sendAppQueue() {
        // 创建Queue时注意到可配置为持久化（Durable）和非持久化（Transient），当Consumer不在线时，持久化的Queue会暂存消息，非持久化的Queue会丢弃消息
        return new Queue(Q_APP);
    }

    @Bean
    public Queue sendAppMail() {
        return new Queue(Q_MAIL);
    }

    @Bean
    public Queue sendAppSms() {
        return new Queue(Q_SMS);
    }

    /**
     * 在mq控制台配置Binding规则：
     * 凡是发送到registration这个Exchange的消息，均被发送到q_mail和q_sms这两个Queue
     *
     * @return
     */
    @Bean
    public Exchange registerExchange() {
        return ExchangeBuilder.directExchange(REGISTER_EXCHANGE).durable(true).build();
    }


    /**
     * 当发送消息给login这个Exchange时，如果消息没有指定Routing Key，则被投递到q_app和q_mail，
     * 如果消息指定了Routing Key="login_failed"，那么消息被投递到q_sms
     *
     * @return
     */
    @Bean
    public Exchange loginExchange() {
        return ExchangeBuilder.directExchange(LOGIN_EXCHANGE).durable(true).build();
    }


    public static final String QUEUE_TEST = "test";
    public static final String EXCHANGE_TEST = "test";
    public static final String ROUTING_KEY_TEST = "test";

    public static final String DEAD_EXCHANGE = "deadtest";
    public static final String DEAD_QUEUE = "deadtest";
    public static final String DEAD_ROUTING_KEY = "deadtest";

    @Bean
    public Declarables declarablesTest() {
        Queue queue = new Queue(QUEUE_TEST);
        DirectExchange directExchange = new DirectExchange(EXCHANGE_TEST);
        //快速声明一组对象，包含队列、交换器，以及队列到交换器的绑定
        return new Declarables(queue, directExchange,
                BindingBuilder.bind(queue).to(directExchange).with(ROUTING_KEY_TEST));
    }


//    @Bean
//    public Declarables declarablesForDead() {
//        Queue queue = new Queue(DEAD_QUEUE);
//        DirectExchange directExchange = new DirectExchange(DEAD_EXCHANGE);
//        return new Declarables(queue, directExchange,
//                BindingBuilder.bind(queue).to(directExchange).with(DEAD_ROUTING_KEY));
//    }
//
//    @Bean
//    public RetryOperationsInterceptor interceptor() {
//        return RetryInterceptorBuilder.stateless()
//                .maxAttempts(5)
//                .backOffOptions(1000, 2.0, 10000)
//                .recoverer(new RepublishMessageRecoverer(rabbitTemplate, DEAD_EXCHANGE, DEAD_ROUTING_KEY))
//                .build();
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setAdviceChain(interceptor());
//        factory.setConcurrentConsumers(10);
//        return factory;
//    }
}