package com.lvmc.exception;

/**
 * @author 吕茂陈
 * @description
 * @date 2025/3/5 11:13
 */
public class MqttRuntimeException extends RuntimeException {
    public MqttRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
