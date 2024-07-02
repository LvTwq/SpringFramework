package com.lvmc.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 吕茂陈
 * @date 2022-07-15 14:27
 */
@Data
public class MqUser implements Serializable {

    private static AtomicLong atomicLonng = new AtomicLong();
    private Long id = atomicLonng.incrementAndGet();
    private String name = UUID.randomUUID().toString();
    private LocalDateTime registerTime = LocalDateTime.now();
}
