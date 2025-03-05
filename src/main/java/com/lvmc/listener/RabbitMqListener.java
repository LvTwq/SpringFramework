package com.lvmc.listener;

import com.lvmc.config.QueueBeanConfig;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.lvmc.config.QueueBeanConfig.DELAYED_QUEUE;

/**
 * @author 吕茂陈
 * @date 2022-08-10 15:38
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RabbitMqListener {

    /*
     以下是 简单模式，当有多个消费者时，默认使用轮训机制把消息分配给消费者
     */
    @RabbitListener(queues = "simpleQueue")
    public void simpleMessage(String message/*, Channel channel, Message msg*/) {
        log.info("simpleMessage 接收到消息：{}", message);
//        try {
//            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    @RabbitListener(queues = "simpleQueue")
    public void simple2Message(String message) {
        log.info("simple2Message 接收到消息：{}", message);
    }


     /*
     以下是 Publish/Subscribe 即 Fanout（广播） 模型，都能消费
     */

    @RabbitListener(queues = "publishQueue1")
    public void publishMessage1(String message) {
        log.info("publishMessage1接收到消息：{}", message);
    }

    @RabbitListener(queues = "publishQueue2")
    public void publishMessage2(String message) {
        log.info("publishMessage2接收到消息：{}", message);
    }

    
     /*
     以下是 路由（Routing），即 Direct（定向）模型
     */

    @RabbitListener(queues = "routingQueue1")
    public void routingMessage1(String message) {
        log.info("routingMessage1接收到消息：{}", message);
    }

    @RabbitListener(queues = "routingQueue2")
    public void routingMessage2(String message) {
        log.info("routingMessage2接收到消息：{}", message);
    }

    /*
      以下是 主题（Topic）
      在主题模式中，生产者发送消息到交换机，交换机根据 routing key 模式（支持通配符）将消息路由到不同的队列。
     */

    @RabbitListener(queues = "topicQueue1")
    public void topicMessage1(String message) {
        log.info("topicMessage1接收到消息：{}", message);
    }

    @RabbitListener(queues = "topicQueue2")
    public void topicMessage2(String message) {
        log.info("topicMessage2接收到消息：{}", message);
    }


    /**
     *
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "serverTopic2"),
            exchange = @Exchange(value = "topicExchange2", type = ExchangeTypes.TOPIC),
            key = "#", ignoreDeclarationExceptions = "true"),
            containerFactory = "rabbitListenerContainerFactory")
    public void receive(String message) {
        log.info("serverTopic 接收到消息：{}", message);
    }


    /*
     header模式
     */

    @RabbitListener(queues = "header_first_queue")
    public void headerFirstQueue(String context) {
        log.info("rabbit header queue first receiver: {}", context);
    }

    @RabbitListener(queues = "header_second_queue")
    public void headerSecondQueue(String context) {
        log.info("rabbit header queue second receiver: {}", context);
    }



    /*
     死信队列
     */


    @RabbitListener(queues = QueueBeanConfig.NORMAL_QUEUE)
    public void consume(String msg, Channel channel, Message message) {
        log.info("接收到normal-queue队列的消息：{}", msg);
        //消息被消费者拒绝（通过basic.reject 或者 back.nack），并且设置 requeue=false。
        try {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /**
         * channel.basicNack(deliveryTag, multiple, requeue) 是 RabbitMQ 的 Java 客户端中用于拒绝（Nack）一条或多条消息的方法。下面是对该方法的参数进行解释：
         *      deliveryTag：消息的交付标签（delivery tag），用于唯一标识一条消息。通过 message.getMessageProperties().getDeliveryTag() 获取消息的交付标签。
         *      multiple：是否拒绝多条消息。如果设置为 true，则表示拒绝交付标签小于或等于 deliveryTag 的所有消息；如果设置为 false，则只拒绝交付标签等于 deliveryTag 的消息。
         *      requeue：是否重新入队列。如果设置为 true，则被拒绝的消息会重新放回原始队列中等待重新投递；如果设置为 false，则被拒绝的消息会被丢弃。
         */
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
    }



    @RabbitListener(queues = DELAYED_QUEUE)
    public void delayedMessage(String msg) {
        log.info("接收到delayed-queue队列的消息：{}", msg);
    }
}
