package com.southwind.controller;

import com.southwind.entity.RedisModel;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/3/31 17:30
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RedisController {


    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    public void lock(String key, int timeOutSecond) {
        for (; ; ) {
            boolean exist = Boolean.TRUE.equals(
                redisTemplate.opsForValue().setIfAbsent(key, "", timeOutSecond, TimeUnit.SECONDS));
            if (exist) {
                break;
            }
        }
    }

    public void unlock(String key) {
        redisTemplate.delete(key);
    }

    @GetMapping("redis/{key}/{timeOutSecond}")
    public void redis01(@PathVariable String key, @PathVariable int timeOutSecond) {
        redisTemplate.opsForValue().setIfAbsent(key, "", timeOutSecond, TimeUnit.SECONDS);
    }


    @PostMapping("redis02")
    public void redis02(@RequestBody RedisModel model) {
        redisTemplate.opsForStream().add(model.getKey(), Map.of(model.getField(), model.getValue()));
    }
}
