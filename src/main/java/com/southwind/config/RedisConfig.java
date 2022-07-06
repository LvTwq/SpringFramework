package com.southwind.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 吕茂陈
 * @date 2021/10/02 10:53
 */
//@Configuration
//@EnableCaching
public class RedisConfig {

    /**
     * Redis 连接工厂bean
     *
     * @return
     */
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }


    /**
     * RedisTemplate bean
     *
     * @param redisCF
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisCF) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisCF);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    /**
     * Redis 缓存管理器bean
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager cacheManager(/*RedisTemplate<String, String> redisTemplate, */RedisConnectionFactory redisCF) {
        return RedisCacheManager.create(redisCF);
//        return new RedisCacheManager(redisTemplate);
    }

//    @Bean
//    public CacheManager cacheManager(net.sf.ehcache.CacheManager cm, CacheManager jcm, RedisConnectionFactory redisCF) {
//        CompositeCacheManager cacheManager = new CompositeCacheManager();
//        List<CacheManager> managers = Lists.newArrayList(new JCacheCacheManager(jcm),
//                new EhCacheCacheManager(cm), cacheManager(redisCF));
//        cacheManager.setCacheManagers(managers);
//        return cacheManager;
//    }


}
