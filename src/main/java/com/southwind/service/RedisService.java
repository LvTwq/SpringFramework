package com.southwind.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/7/5 15:16
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RedisService {


    private final RedisTemplate<String, String> redisTemplate;


    public void delPattern(String pattern) {
        List<String> keys = new ArrayList<>();
        scan(pattern, item -> {
            keys.add(new String(item, StandardCharsets.UTF_8));
        });
        redisTemplate.delete(keys);
    }

    public List<String> getKeyByPattern(String pattern) {
        List<String> keys = new ArrayList<>();
        scan(pattern, item -> {
            keys.add(new String(item, StandardCharsets.UTF_8));
        });
        return keys;
    }


/*    public List<String> hashGet(String key, List<String> hashKey) {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        RedisSerializer<String> valueSerializer = redisTemplate.getStringSerializer();
        return redisTemplate.executePipelined(
                (RedisCallback<String>) connection -> {
                    connection.openPipeline();
                    hashKey.stream().forEach(i ->
                            connection.hGet(serializer.serialize(key), serializer.serialize(i))
                    );
                    connection.closePipeline();
                    //must return null
                    return null;
                }, valueSerializer
        );
    }*/


/*    public List<Set<String>> patternSetMembers(String keyPattern) {
        List<String> keyList = getKeyByPattern(keyPattern);
        if (CollectionUtils.isEmpty(keyList)) {
            return new ArrayList<>();
        }
        return redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                SetOperations<K, V> hashOperations = operations.opsForSet();
                for (String key : keyList) {
                    hashOperations.members(key);
                }
                return null;
            }
        }, redisTemplate.getStringSerializer());
    }*/


    public void mSet(Map<String, String> map, long time) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.multiSet(map);
    }

    public List<String> mGet(List<String> keys) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.multiGet(keys);
    }



    private void scan(String pattern, Consumer<byte[]> consumer) {
        redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE)
                    .match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            }
        });
    }
}
