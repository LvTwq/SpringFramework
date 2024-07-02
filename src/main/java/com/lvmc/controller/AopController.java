package com.lvmc.controller;

import com.lvmc.service.CompactDisc;
import com.lvmc.service.Performance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @date 2021/09/27 19:53
 */
@Slf4j
@RestController
@RequestMapping("aop")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AopController {

    private final Performance performance;

    private final CompactDisc compactDisc;

    @GetMapping("v1")
    public String aop01() {
        String perform = performance.perform();
        String stutest = performance.stutest("aaa", "bbb");
        return perform + stutest;
    }

    @GetMapping("v2/{id}")
    public int aop02(@PathVariable("id") int id) {
        return compactDisc.playTrack(id);
    }


    @GetMapping("/aop")
    public String aop() {
        long begin = System.currentTimeMillis();
        performance.method();
        long cost = System.currentTimeMillis() - begin;
        String cls = performance.getClass().toString();
        return cls + " | " + cost;
    }

}
