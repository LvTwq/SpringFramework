package com.lvmc.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/8/17 11:45
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RedisDistributedLock {

    private final StringRedisTemplate redisTemplate;


    public boolean acquireLock(String lockKey, String clientId, long expirationTime) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Boolean lockAcquired = valueOperations.setIfAbsent(lockKey, clientId, expirationTime, TimeUnit.MILLISECONDS);
        return lockAcquired != null && lockAcquired;
    }

    public void releaseLock(String lockKey, String clientId) {
        String storedClientId = redisTemplate.opsForValue().get(lockKey);
        if (clientId.equals(storedClientId)) {
            redisTemplate.delete(lockKey);
        }
    }


    private final RedissonClient redissonClient;

    /**
     * 通过 redisson 实现分布式锁
     */
    public void testRedissonLock() {
        RLock rLock = redissonClient.getLock("order");
        try {
            rLock.lock(30, TimeUnit.SECONDS);
            log.info("我获得了锁！！！");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error("获取锁失败", e);
        } finally {
            log.info("我释放了锁！！");
            rLock.unlock();
        }
    }

}
