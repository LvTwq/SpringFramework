package com.southwind.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import javax.annotation.PostConstruct;

/**
 * @author 吕茂陈
 * @date 2021/09/16 09:01
 */
@Aspect
@Slf4j
public class Audience {

    /**
     * execution() 是指示器
     */
    @Pointcut("execution(public * com.southwind.service.Performance.perform())")
    public void performance() {
        log.info("public String com.southwind.service.Performance.perform(..) 是切入点！！");
    }


    @Before("performance()")
    public void silenceCellPhones() {
        log.info("silencing cell phones");
    }

    @AfterReturning("performance()")
    public void taskSeats() {
        log.info("clap clap clap!!!");
    }

    /**
     *
     * @param jp 使用环绕通知必须要有，通过它调用被通知的方法，通知方法中可以做任何事情，当要把控制权交给被通知的方法时，需要调用proceed()方法
     *           如果不调用，你的通知实际上会阻塞对被通知方法的调用
     */
    @Around("performance()")
    public void watchPerformance(ProceedingJoinPoint jp) {
        try {
            log.info("关电话！！！！");
            log.info("调整位置！！！！");
            jp.proceed();
            log.info("鼓掌！！！！");
        } catch (Throwable throwable) {
            log.info("退钱！！！！");
        }
    }


    @PostConstruct
    public void init() {
        log.info("Audience 初始化！！！！");
    }

}
