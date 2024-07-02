package com.lvmc.controller;

import com.lvmc.entity.RedisModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/3/31 17:30
 */
@Slf4j
@RestController
@RequestMapping("redis")
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


    @GetMapping("mq")
    public void redisMq() {
        Map<String, String> map = Map.of("name", "lvmc");

        MapRecord<String, String, String> record = StreamRecords.newRecord().ofMap(map).withStreamKey("mystream").withId(RecordId.autoGenerate());
        RecordId recordId = redisTemplate.opsForStream().add(record);
    }
}
