package com.lvmc.config;

import com.lvmc.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/12/21 16:19
 */
@Configuration
public class WebConfig {


    @Bean
    public AuthenticationInterceptor authenticationFilter() {
        return new AuthenticationInterceptor();
    }
}
