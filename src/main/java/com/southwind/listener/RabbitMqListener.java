package com.southwind.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 吕茂陈
 * @date 2022-08-10 15:38
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RabbitMqListener {


    /**
     * Binding的规则：
     * 凡是发送到registration这个Exchange的消息，均被发送到q_mail和q_sms这两个Queue
     *
     * @param msg
     */
//    @RabbitListener(queues = RabbitConfiguration.Q_MAIL)
//    public void onRegistrationMessageFromMailQueue(Account msg) {
//        log.info("queue {} received registration message: {}", RabbitConfiguration.Q_MAIL, msg);
//    }

//    @RabbitListener(queues = RabbitConfiguration.Q_SMS)
//    public void onRegistrationMessageFromSmsQueue(Account message) throws Exception {
//        log.info("queue {} received registration message: {}", RabbitConfiguration.Q_SMS, message);
//    }
//
//    @RabbitListener(queues = RabbitConfiguration.Q_MAIL)
//    public void onLoginMessageFromMailQueue(LoginParam message) throws Exception {
//        log.info("queue {} received message: {}", RabbitConfiguration.Q_MAIL, message);
//    }
//
//
//    @RabbitListener(queues = RabbitConfiguration.Q_SMS)
//    public void onLoginMessageFromSmsQueue(LoginParam message) throws Exception {
//        log.info("queue {} received message: {}", RabbitConfiguration.Q_SMS, message);
//    }
//
//    @RabbitListener(queues = RabbitConfiguration.Q_APP)
//    public void onLoginMessageFromAppQueue(LoginParam message) throws Exception {
//        log.info("queue {} received message: {}", RabbitConfiguration.Q_APP, message);
//    }
//
//
//    @RabbitListener(queues = RabbitConfiguration.QUEUE_TEST)
//    public void handler(String data) {
//        log.info("got message {}", data);
//        // 模拟处理出错的情况
//        throw new NullPointerException("error");
//        //throw new AmqpRejectAndDontRequeueException("error");
//    }
//
//    @RabbitListener(queues = RabbitConfiguration.DEAD_QUEUE)
//    public void deadHandler(String data) {
//        log.error("got dead message {}", data);
//    }

}
