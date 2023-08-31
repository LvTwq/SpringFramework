package com.southwind.controller;

import com.southwind.utils.SpringContextUtil;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @date 2022-08-09 11:35
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AsyncController {

    private final ApplicationContext applicationContext;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @GetMapping("task")
    public void threadPoolTaskExecutor() {
        log.info("====================");
        // 看线程名，线程是否复用
        threadPoolTaskExecutor.submit(() -> log.info("spring 自带的线程池！"));
        log.info("====================");
    }

    @GetMapping("task2")
    public void completeFuture() {
        log.info("====================");
        // 看线程名，线程是否复用
        CompletableFuture.runAsync(() -> log.info("completableFuture 的线程池！"));
        log.info("====================");
    }


    @GetMapping("async")
    public void async1() throws InterruptedException {
        log.info("开始睡一秒");
        TimeUnit.SECONDS.sleep(1);
        method1();
        // controller 是代理对象
//        AsyncController controller = applicationContext.getBean(AsyncController.class);
        AsyncController controller = SpringContextUtil.getBean(AsyncController.class);
        controller.method2();
    }


    @Async
    public void method1() {
        log.info("method1 不会异步，执行线程为tomcat的线程");
    }

    /**
     * @Async 会先创建代理，然后提供一个异步处理执行器
     */
    @Async
    public void method2() {
        log.info("method2 会异步，执行线程为SimpleAsyncTaskExecutor");
    }

}
