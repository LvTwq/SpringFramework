package com.southwind.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

/**
 * 此注解会创建一个切面并触发Spring缓存注解的切点，根据所使用的注解以及缓存的状态，
 * 这个切面会从缓存中获取数据、将数据添加到缓存中、从缓存中移除某个值
 *
 * @author 吕茂陈
 * @date 2021/10/02 09:32
 */
//@EnableCaching
//@Configuration
public class CachingConfig {

    /**
     * 声明缓存管理器➡Spring抽象的核心，能够与多个缓存实现进行集成
     *
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

    /**
     * EhCache 直接在jvm虚拟机中缓存，速度快，效率高；但是共享缓存麻烦，集群分布式应用不方便
     * @param cm Ehcache
     * @return springframework
     */
    @Bean
    public EhCacheCacheManager cacheManager(net.sf.ehcache.CacheManager cm) {
        return new EhCacheCacheManager(cm);
    }

    /**
     * 上个方法需要使用Ehcache的CacheManager来进行注入，所以必须声明一个CacheManager Bean
     * 注入到Spring应用上下文的并不是EhCacheManagerFactoryBean的实例，而是CacheManager的一个实例
     *
     * @return 工厂bean，实现了Spring的FactoryBean接口
     */
    @Bean
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(
                new ClassPathResource("ehcache.xml")
        );
        return ehCacheManagerFactoryBean;
    }


}
