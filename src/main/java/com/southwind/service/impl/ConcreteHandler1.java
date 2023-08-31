package com.southwind.service.impl;

import com.southwind.entity.Request;
import com.southwind.service.Handler;
import org.springframework.stereotype.Component;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/8/2 14:13
 */
@Component
public class ConcreteHandler1 implements Handler {
    private Handler nextHandler;

    @Override
    public void handleRequest(Request request) {
        // 根据业务逻辑判断是否处理该请求
        if (request.isNeedsHandlingByHandler1()) {
            // 处理请求
        } else {
            // 将请求传递给下一个处理器
            passToNextHandler(request);
        }
    }

    @Override
    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    private void passToNextHandler(Request request) {
        if (nextHandler != null) {
            nextHandler.handleRequest(request);
        }
    }
}
