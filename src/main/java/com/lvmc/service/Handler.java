package com.lvmc.service;

import com.lvmc.entity.Request;

/**
 * @author 吕茂陈
 * @description 责任链模式适用于存在多个处理步骤，每个处理步骤具有独立逻辑或条件、需要灵活组合扩展的场景
 * @date 2023/8/2 14:12
 */
public interface Handler {

    void handleRequest(Request request);

    void setNextHandler(Handler nextHandler);
}
