package com.lvmc.component;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/7/24 15:40
 */
@Configuration
public class ConfigValue {


    public static String endMomentConfig = "23:59:59";


    @PostConstruct
    public void init() {
        endMomentConfig = "00";
    }
}
