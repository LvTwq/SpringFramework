package com.lvmc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 吕茂陈
 * @date 2021/08/23 09:36
 */
@Slf4j
@Configuration
public class LoginConfig implements WebMvcConfigurer {

//    private final SsoHandler ssoHandler;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册拦截器
//        InterceptorRegistration registration = registry.addInterceptor(ssoHandler);
//        // 所有请求都被拦截
//        registration.addPathPatterns("/**");
//    }

//    @Bean
//    public MyInterceptor getMyInterceptor() {
//        log.info("注入了MyInterceptor！！！");
//        return new MyInterceptor();
//    }

    /**
     * 注册拦截器
     *
     * @param registry
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
////        registry.addInterceptor(new JwtLoginInterceptor());
//        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/**");
//    }
}
