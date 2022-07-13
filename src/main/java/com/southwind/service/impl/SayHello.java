package com.southwind.service.impl;

import com.southwind.service.SayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * @author 吕茂陈
 * @date 2022-07-07 15:14
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class SayHello extends SayService {
    @Override
    public void say() {
        super.say();
        log.info("hello");
    }
}
