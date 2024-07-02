package com.lvmc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.google.common.collect.Lists;
import com.lvmc.aop.TrackCounter;
import com.lvmc.service.CompactDisc;
import com.lvmc.service.impl.BlankDisc;

/**
 * @author 吕茂陈
 * @date 2021/09/30 08:55
 */
@Configuration
@EnableAspectJAutoProxy
public class TrackCounterConfig {

    @Bean
    public CompactDisc sgtPeppers() {
        return BlankDisc.builder().artist("艺术家").title("标题").tracks(Lists.newArrayList("1", "2", "3")).build();

    }

    @Bean
    public TrackCounter trackCounter() {
        return new TrackCounter();
    }
}
