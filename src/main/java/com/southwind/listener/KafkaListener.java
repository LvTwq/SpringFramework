package com.southwind.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.southwind.entity.Account;
import com.southwind.entity.LoginParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author 吕茂陈
 * @date 2022-08-10 11:21
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class KafkaListener {

    private final ObjectMapper objectMapper;


    /**
     * @param message @Payload 传入的是消息正文
     * @param type    发送消息时指定的Class全名
     * @throws JsonProcessingException
     */
    @org.springframework.kafka.annotation.KafkaListener(topics = "topic_account", groupId = "group1")
    public void onAccountMessage(@Payload String message, @Header("type") String type) throws JsonProcessingException {
        Account msg = objectMapper.readValue(message, getType(type));
        log.info("接收 topic_account：{}", msg);
    }


    /**
     * 假设Producer发送的消息流是A、B、C、D，Group ID不同表示这是两个不同的Consumer，
     * 它们将分别收取完整的消息流，即各自均收到A、B、C、D
     * <p>
     * Group ID相同的多个Consumer实际上被视作一个Consumer，
     * 即如果有两个Group ID相同的Consumer，那么它们各自收到的很可能是A、C和B、D
     * <p>
     * 因为Group ID不同，同一个消息被两个Consumer分别独立接收。
     * 如果把Group ID改为相同，那么同一个消息只会被两者之一接收
     *
     * @param message
     * @param type
     * @throws JsonProcessingException
     */
    @org.springframework.kafka.annotation.KafkaListener(topics = "topic_login", groupId = "group1")
    public void onLoginMessage(@Payload String message, @Header("type") String type) throws JsonProcessingException {
        LoginParam msg = objectMapper.readValue(message, getType(type));
        log.info("接收 topic_login，{}", msg);
    }


    @org.springframework.kafka.annotation.KafkaListener(topics = "topic_login", groupId = "group2")
    public void processLoginMessage(@Payload String message, @Header("type") String type) throws JsonProcessingException {
        LoginParam msg = objectMapper.readValue(message, getType(type));
        log.info("处理 topic_login，{}", msg);
    }

    private static <T> Class<T> getType(String type) {
        // TODO: use cache:
        try {
            return (Class<T>) Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
