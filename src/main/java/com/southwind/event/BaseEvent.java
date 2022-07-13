package com.southwind.event;

import org.springframework.context.ApplicationEvent;

/**
 * 基础事件
 *
 * @author 吕茂陈
 * @date 2022-07-07 11:37
 */
public class BaseEvent extends ApplicationEvent {

    public BaseEvent(Object source) {
        super(source);
    }

    public BaseEvent() {
        this("");
    }
}
