package com.southwind.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.southwind.annotation.BuriedPoint;
import com.southwind.service.Performance;

import lombok.RequiredArgsConstructor;

/**
 * @author 吕茂陈
 * @date 2021/09/27 19:54
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PerformanceImpl implements Performance {


    @Override
    public String perform() {
        return "开始测试aop！！！";
    }

    @Override
    @BuriedPoint("stu")
    public String stutest(String s1, String s2) {
        return "开始测试注解aop！！！";
    }
}
