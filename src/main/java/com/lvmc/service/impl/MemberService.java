package com.lvmc.service.impl;

import com.lvmc.entity.MqUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 吕茂陈
 * @date 2022-07-15 14:43
 */
@Slf4j
@Service
public class MemberService {

    /**
     * key:value = 存放那些发过短信的用户 ID：发送欢迎消息的状态
     */
    private Map<Long, Boolean> welcomeStatus = new ConcurrentHashMap<>();


    /**
     * 监听用户注册成功的消息，并发送欢迎短信
     *
     * @param user
     */
//    @RabbitListener(queues = RabbitConfiguration.QUEUE)
    public void listen(MqUser user) {
        log.info("接收 mq 用户：{}", user.getId());
        welcome(user);
    }

    /**
     * 发送欢迎消息
     *
     * @param user
     */
    public void welcome(MqUser user) {
        if (welcomeStatus.putIfAbsent(user.getId(), true) == null) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                log.error("", e);
                Thread.currentThread().interrupt();
            }
            log.info("MemberService: 欢迎新用户：{}", user.getId());
        }
    }
}
