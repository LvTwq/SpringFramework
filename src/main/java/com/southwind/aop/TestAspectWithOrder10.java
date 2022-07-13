package com.southwind.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author 吕茂陈
 * @date 2022-07-08 10:23
 */
@Aspect
@Component
@Order(10)
@Slf4j
public class TestAspectWithOrder10 {


    /**
     * 入操作（Around 的连接点执行前、Before）：切面优先级越高，越先执行。
     * 一个切面的入操作执行完，才轮到下一切面，所有切面操作执行完，才开始执行连接点（方法）
     * @param joinPoint
     * @throws Throwable
     */
    @Before("execution(* com.southwind.controller.HelloController.*(..))")
    public void before(JoinPoint joinPoint) throws Throwable {
        log.info("TestAspectWithOrder10 @Before");
    }


    /**
     * 出操作（Around 的连接点执行后、After、AfterReturning、AfterThrowing）：切面优先级越低，越先执行。
     * 一个切面的出操作执行完，才轮到下一切面，直到返回到调用点
     * @param joinPoint
     * @throws Throwable
     */
    @After("execution(* com.southwind.controller.HelloController.*(..))")
    public void after(JoinPoint joinPoint) throws Throwable {
        log.info("TestAspectWithOrder10 @After");
    }


    /**
     * 同一切面的 Around 比 After、Before 先执行
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.southwind.controller.HelloController.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("TestAspectWithOrder10 @Around before");
        Object o = pjp.proceed();
        log.info("TestAspectWithOrder10 @Around after");
        return o;
    }

}
