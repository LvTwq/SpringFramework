package com.lvmc.controller;

import com.lvmc.mqtt.MqttClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @description
 * @date 2025/3/5 11:41
 */
@Slf4j
@RestController
@RequestMapping("mqtt")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MqttController {

    private final MqttClientWrapper mqttClientWrapper;

    @GetMapping("send")
    public String send() {
        mqttClientWrapper.publish("temp", "hello", 1, false);
        return "success";
    }
}
