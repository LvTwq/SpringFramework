package com.southwind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.southwind.aop.Audience;

/**
 *
 *
 * @author 吕茂陈
 * @date 2021/09/27 20:02
 */
@EnableAspectJAutoProxy
@Configuration
@ComponentScan
public class AopConfig {

    @Bean
    public Audience audience() {
        return new Audience();
    }

}
