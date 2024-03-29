package com.southwind.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @date 2022-08-12 16:47
 */
@Slf4j
@Configuration
@RestController
@RequestMapping("fanoutright")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FanoutQueueRight {
    private static final String MEMBER_QUEUE = "newusermember";
    private static final String PROMOTION_QUEUE = "newuserpromotion";
    private static final String EXCHANGE = "newuser";
//    private final RabbitTemplate rabbitTemplate;


    @GetMapping
    public void sendMessage() {
//        rabbitTemplate.convertAndSend(EXCHANGE, "", UUID.randomUUID().toString());
    }


    /**
     * 把队列进行拆分，会员和营销两组服务分别使用一条独立队列绑定到广播交换器
     *
     * @return
     */
//    @Bean
//    public Declarables declarables3() {
//        //会员服务队列
//        Queue memberQueue = new Queue(MEMBER_QUEUE);
//        //营销服务队列
//        Queue promotionQueue = new Queue(PROMOTION_QUEUE);
//        //广播交换器
//        FanoutExchange exchange = new FanoutExchange(EXCHANGE);
//        //两个队列绑定到同一个交换器
//        return new Declarables(memberQueue, promotionQueue, exchange,
//                BindingBuilder.bind(memberQueue).to(exchange),
//                BindingBuilder.bind(promotionQueue).to(exchange));
//    }
//
//    @RabbitListener(queues = MEMBER_QUEUE)
//    public void memberService1(String userName) {
//        log.info("memberService1: welcome message sent to new user {}", userName);
//    }
//
//    @RabbitListener(queues = MEMBER_QUEUE)
//    public void memberService2(String userName) {
//        log.info("memberService2: welcome message sent to new user {}", userName);
//    }
//
//    @RabbitListener(queues = PROMOTION_QUEUE)
//    public void promotionService1(String userName) {
//        log.info("promotionService1: gift sent to new user {}", userName);
//    }
//
//    @RabbitListener(queues = PROMOTION_QUEUE)
//    public void promotionService2(String userName) {
//        log.info("promotionService2: gift sent to new user {}", userName);
//    }
}
