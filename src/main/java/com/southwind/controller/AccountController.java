package com.southwind.controller;

import com.southwind.entity.MqUser;
import com.southwind.service.impl.MqUserService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 吕茂陈
 */
@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AccountController {

    private final MqUserService mqUserService;

//    private final RabbitTemplate rabbitTemplate;



    @GetMapping("register")
    @ResponseBody
    public void register(){
        //模拟10个用户注册
        IntStream.rangeClosed(1, 10).forEach(i -> {
            MqUser user = mqUserService.register();
            //模拟50%的消息可能发送失败
            if (ThreadLocalRandom.current().nextInt(10) % 2 == 0) {
                //通过RabbitMQ发送消息
//                rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE, RabbitConfiguration.ROUTING_KEY, user);
                log.info("发送 mq user {}", user.getId());
            }
        });
    }
}
