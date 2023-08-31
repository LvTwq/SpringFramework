package com.southwind.controller;

import com.southwind.entity.Account;
import com.southwind.entity.LoginParam;
import com.southwind.service.impl.MessagingService;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @date 2022-08-10 11:17
 */
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MqController {

    private final MessagingService messagingService;
//    private final RabbitTemplate rabbitTemplate;


    @GetMapping("kafka")
    public void kafka() throws IOException {
        messagingService.sendRegistrationMessage(Account.builder().id(1).username("topic_account").build());
        messagingService.sendLoginMessage(LoginParam.builder().username("topic_login").password("123456").build());

        messagingService.sendMessage();
    }

    @GetMapping("rabbit")
    public void rabbit() throws IOException {
        //会在两个Queue收到消息
        messagingService.sendRegistrationMessage2(Account.builder().id(1).username("lmc").build());
        //登录失败时，发送LoginParam并设定Routing Key为login_failed，此时，只有q_sms会收到消息
        messagingService.sendLoginMessage2(LoginParam.builder().username("lmc").password("123456").success(false).build());
        //登录成功后，发送LoginParam，此时，q_mail和q_app将收到消息
        messagingService.sendLoginMessage2(LoginParam.builder().username("lmc").password("123456").success(true).build());
    }


    AtomicLong atomicLong = new AtomicLong();


    @GetMapping("sendMessage")
    public void sendMessage() {
        String msg = "msg" + atomicLong.incrementAndGet();
        log.info("send message:{}", msg);
//        rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_TEST, msg);
    }
}
