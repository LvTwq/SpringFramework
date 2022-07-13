package com.southwind.controller;

import com.southwind.event.MessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @date 2022-07-07 10:54
 */
@Slf4j
@RestController
@RequestMapping("springListenRegister")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ObserverController {

    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("sync")
    public String publishSync() {
        log.info("被观察者执行了一些动作，同步阻塞");
        // 执行完了，发布事件
        applicationEventPublisher.publishEvent(new MessageEvent("同步"));
        return "success";
    }

    @GetMapping("async")
    public String publishAsync() {
        log.info("被观察者执行了一些动作，异步非阻塞");
        // 执行完了，发布事件
        applicationEventPublisher.publishEvent(new MessageEvent("异步"));
        return "success";
    }


}
