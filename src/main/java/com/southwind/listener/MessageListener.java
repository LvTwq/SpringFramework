package com.southwind.listener;

import com.southwind.event.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 观察者
 *
 * @author 吕茂陈
 * @date 2022-07-07 10:52
 */
@Slf4j
@Component
public class MessageListener implements IEventListener<MessageEvent> {


//    @Override
//    public void onApplicationEvent(MessageEvent messageEvent) {
//        log.info("用户注册成功，执行监听事件{}", messageEvent.getSource());
//    }

    @Override
    public void handler(MessageEvent event) {
        // 注意看执行的线程：SimpleAsyncTaskExecutor-xx
        log.info("用户注册成功，执行监听事件{}", event.getMsgId());
    }
}
