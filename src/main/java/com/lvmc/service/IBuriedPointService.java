package com.lvmc.service;

import org.aspectj.lang.JoinPoint;

/**
 * @author 吕茂陈
 * @date 2021/09/26 20:23
 */
public interface IBuriedPointService {

    /**
     * 埋点实现
     * @param jp 方法信息
     */
    void afterReturning(JoinPoint jp, Object returnValue);
}
