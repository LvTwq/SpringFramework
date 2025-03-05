package com.lvmc.config;

import com.lvmc.interceptor.RestTemplateInterceptor;
import com.lvmc.listener.StreamMessageListener;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;

/**
 * @author 吕茂陈
 * @date 2021/10/02 10:53
 */
@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient getRedissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://redis.lmc.top:6378")
                .setPassword(password)
                .setDatabase(0);
        return Redisson.create(config);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateInterceptor()));
        return restTemplate;
    }

    private final StreamMessageListener streamMessageListener;


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }


    /**
     * 参考 https://mp.weixin.qq.com/s/9-j9B7O48jXYJ5hJ4GYgeg
     */
    @Bean
    public Subscription subscription(RedisConnectionFactory factory, RedisTemplate<String, Object> redisTemplate) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();
        // 创建 stream 名称
        Boolean hasKey = redisTemplate.hasKey("mystream");
        if (!hasKey) {
            redisTemplate.opsForStream().createGroup("mystream", "mygroup");
        }

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer = StreamMessageListenerContainer.create(factory, options);
        Subscription subscription = listenerContainer.receiveAutoAck(
                Consumer.from("mygroup", "courage-cache-demo"),
                StreamOffset.create("mystream", ReadOffset.lastConsumed()),
                streamMessageListener
        );
        listenerContainer.start();
        return subscription;
    }

}
