package com.lvmc.service.impl;

import com.lvmc.service.SayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 吕茂陈
 * @date 2022-07-07 15:14
 */
@Service
@Slf4j
public class SayBye extends SayService {
    @Override
    public void say() {
        super.say();
        log.info("bye");
    }
}
