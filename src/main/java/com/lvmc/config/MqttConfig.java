package com.lvmc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 吕茂陈
 * @description
 * @date 2025/3/5 10:33
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttConfig {

    private String host;
    private String username;
    private String password;
}
