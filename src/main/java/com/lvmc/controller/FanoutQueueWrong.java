package com.lvmc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @date 2022-08-12 16:31
 */

@Slf4j
@Configuration
@RestController
@RequestMapping("fanoutwrong")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FanoutQueueWrong {
    private static final String QUEUE = "newuser";
    private static final String EXCHANGE = "newuser";
//    private final RabbitTemplate rabbitTemplate;

    @GetMapping
    public void sendMessage() {
//        rabbitTemplate.convertAndSend(EXCHANGE, "", UUID.randomUUID().toString());
    }


    /**
     * 声明广播交换器FanoutExchange，然后绑定到队列，FanoutExchange绑定队列的时候不需要routingKey
     */
//    @Bean
//    public Declarables declarables2() {
//        Queue queue = new Queue(QUEUE);
//        FanoutExchange exchange = new FanoutExchange(EXCHANGE);
//        return new Declarables(queue, exchange,
//                BindingBuilder.bind(queue).to(exchange));
//    }

    /**
     * 会员服务实例1
     *
     * @param userName
     */
//    @RabbitListener(queues = QUEUE)
    public void memberService1(String userName) {
        log.info("memberService1: welcome message sent to new user {}", userName);

    }

    /**
     * 会员服务实例2
     *
     * @param userName
     */
//    @RabbitListener(queues = QUEUE)
//    public void memberService2(String userName) {
//        log.info("memberService2: welcome message sent to new user {}", userName);
//
//    }

    /**
     * 营销服务实例1
     *
     * @param userName
     */
//    @RabbitListener(queues = QUEUE)
//    public void promotionService1(String userName) {
//        log.info("promotionService1: gift sent to new user {}", userName);
//    }

    /**
     * 营销服务实例2
     *
     * @param userName
     */
//    @RabbitListener(queues = QUEUE)
//    public void promotionService2(String userName) {
//        log.info("promotionService2: gift sent to new user {}", userName);
//    }

}