package com.lvmc.listener;

import com.lvmc.event.BaseEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author 吕茂陈
 * @date 2022-07-07 11:40
 */
public interface IEventListener<T extends BaseEvent> extends ApplicationListener<T> {

    @Override
    default void onApplicationEvent(T event) {
        try {
            if (support(event)) {
                handler(event);
            }
        } catch (Throwable e) {
            handleException(e);
        }
    }

    /**
     * 默认异常不处理
     *
     * @param e
     */
    default void handleException(Throwable e) {

    }

    /**
     * 真正实现业务逻辑的接口，由子类实现
     *
     * @param event
     */
    void handler(T event);

    /**
     * 只对部分特殊用户执行监听逻辑，让实现类去覆盖此方法，决定是否需要执行
     *
     * @param event
     * @return
     */
    default boolean support(T event) {
        return true;
    }
}
