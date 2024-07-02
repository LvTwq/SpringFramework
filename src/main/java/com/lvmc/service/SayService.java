package com.lvmc.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 这个类是有状态的
 *
 * @author 吕茂陈
 * @date 2022-07-07 15:03
 */
@Slf4j
public abstract class SayService {

    List<String> data = new ArrayList<>();

    /**
     * 用来保存中间数据
     */
    public void say() {
        data.add(IntStream.rangeClosed(1, 1000000)
                .mapToObj(new IntFunction<String>() {
                    @Override
                    public String apply(int i) {
                        return "a";
                    }
                })
                .collect(Collectors.joining("")) + UUID.randomUUID());
        log.info("I'm {} size:{}", this, data.size());
    }
}
