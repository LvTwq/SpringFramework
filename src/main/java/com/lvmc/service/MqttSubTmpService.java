package com.lvmc.service;

import com.lvmc.mqtt.MqttSubscriptTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 吕茂陈
 * @description
 * @date 2025/3/3 17:03
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MqttSubTmpService implements MqttSubscriptTopic {

    @Override
    public String getTopic() {
        // $share 表示共享订阅
        return "$share/zz/temp";
    }

    @Override
    public String realTopic() {
        return "temp";
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String s = new String(message.getPayload());
        log.info("收到消息: {}", s);
    }
}
