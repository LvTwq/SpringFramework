package com.lvmc.aop;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvmc.annotation.BuriedPoint;
import com.lvmc.service.IBuriedPointService;

import lombok.RequiredArgsConstructor;

/**
 * @author 吕茂陈
 * @date 2021/09/26 20:18
 */
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BuriedPointAspect {

    private final Map<String, IBuriedPointService> buriedPointServiceMap;


    @AfterReturning(value = "@annotation(buriedPoint)", returning = "returnValue")
    public void afterReturning(JoinPoint jp, Object returnValue, BuriedPoint buriedPoint) {
        if ("stu".equals(buriedPoint.value())) {
            buriedPointServiceMap.get("jaBuriedPoint").afterReturning(jp, returnValue);
        }
    }
}
