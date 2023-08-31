package com.southwind.controller;

import com.southwind.entity.Request;
import com.southwind.service.Handler;
import com.southwind.service.impl.ConcreteHandler1;
import com.southwind.service.impl.ConcreteHandler2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/8/2 14:15
 */
@RestController
public class ChainRequestController {


    private final Handler firstHandler;

    @Autowired
    public ChainRequestController(ConcreteHandler1 firstHandler, ConcreteHandler2 secondHandler) {
        this.firstHandler = firstHandler;
        firstHandler.setNextHandler(secondHandler);
    }

    @PostMapping("/handleRequest")
    public void handleRequest() {
        // 发起请求
        Request request = new Request();
        firstHandler.handleRequest(request);
    }
}
