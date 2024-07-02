package com.lvmc.service.impl;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import com.lvmc.service.IBuriedPointService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 吕茂陈
 * @date 2021/09/26 20:23
 */
@Slf4j
@Service("jaBuriedPoint")
public class BuriedPointServiceImpl implements IBuriedPointService {


    @Override
    public void afterReturning(JoinPoint jp, Object returnValue) {
        Object[] args = jp.getArgs();
        String s1 = (String) args[0];
        String s2 = (String) args[1];
        log.info("s1" + s1);
        log.info("s2" + s2);
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Class<?> returnType = method.getReturnType();

    }
}
