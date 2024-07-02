package com.lvmc.service.impl;

import com.lvmc.service.Encoreable;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 吕茂陈
 * @date 2021/10/08 08:59
 */
@Slf4j
public class DefaultEncoreable implements Encoreable {

    @Override
    public void performEncore() {
        log.info("返场表演！！！");
    }
}
