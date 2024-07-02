package com.lvmc.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author 吕茂陈
 * @description
 * @date 2024/4/23 19:44
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StreamMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        log.info("message id:{}, stream:{}, body:{}", entries.getId(), entries.getStream(), entries.getValue());
    }
}
