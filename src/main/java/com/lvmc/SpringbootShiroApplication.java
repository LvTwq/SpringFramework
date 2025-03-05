package com.lvmc;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 吕茂陈
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
@MapperScan("com.lvmc.mapper")
public class SpringbootShiroApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringbootShiroApplication.class, args);
        String[] names = applicationContext.getBeanDefinitionNames();
        /*
        proxyBeanMethods 是默认开启，表示代理对象调用方法，这时输出 com.lvmc.config.AopConfig$$EnhancerBySpringCGLIB$$f6b2367e@5fcfca62
        => 这不是一个普通对象，而是被SpringCGLIB增强了的代理对象
        而关闭时，输出 com.lvmc.config.AopConfig@acdcf71
         */
//        AopConfig aopConfig = applicationContext.getBean(AopConfig.class);
//        log.info(aopConfig);

    }





}
