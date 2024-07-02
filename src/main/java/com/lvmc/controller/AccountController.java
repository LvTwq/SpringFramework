package com.lvmc.controller;

import cn.hutool.core.util.RandomUtil;
import com.lvmc.entity.Account;
import com.lvmc.entity.MqUser;
import com.lvmc.mapper.AccountMapper;
import com.lvmc.service.AccountService;
import com.lvmc.service.impl.MqUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * @author 吕茂陈
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AccountController {

    private final MqUserService mqUserService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;

//    private final RabbitTemplate rabbitTemplate;



    @PostMapping("account")
    public void insertAccount() {
        String password = RandomUtil.randomNumbers(6);
        Account account = Account.builder().id(Integer.parseInt(password)).username("lmc").perms("").role("").password(password).build();
        accountService.save(account);
    }

    @GetMapping("account")
    public List<Account> queryAccount() {
        return accountService.list();
    }


    @GetMapping("register")
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
