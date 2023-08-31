package com.southwind.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/7/20 17:23
 */
@Slf4j
@Service
public class OneTimeTask {


    public void executeTask() {
        // 在此处放置你想要执行的逻辑代码
        log.info("执行一次性任务...");
    }
}
