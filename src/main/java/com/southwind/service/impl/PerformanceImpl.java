package com.southwind.service.impl;

import com.southwind.annotation.BuriedPoint;
import com.southwind.service.Performance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 吕茂陈
 * @date 2021/09/27 19:54
 */
@Slf4j
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

    @Override
    public void method() {
        log.info("*******************");
    }
}
