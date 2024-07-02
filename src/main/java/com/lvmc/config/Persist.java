package com.lvmc.config;

import com.lvmc.utils.JackJsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/11/23 10:11
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class Persist<E, S> {


    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * 持久化到内存map中
     *
     * @return
     */
    @Bean(name = "stateMachineMemPersister")
    public static StateMachinePersister getPersister() {
        return new DefaultStateMachinePersister(new StateMachinePersist() {
            @Override
            public void write(StateMachineContext context, Object contextObj) throws Exception {
                log.info("持久化状态机,context:{},contextObj:{}", JackJsonUtil.obj2String(context), JackJsonUtil.obj2String(contextObj));
                map.put(contextObj, context);
            }

            @Override
            public StateMachineContext read(Object contextObj) throws Exception {
                log.info("获取状态机,contextObj:{}", JackJsonUtil.obj2String(contextObj));
                StateMachineContext stateMachineContext = (StateMachineContext) map.get(contextObj);
                log.info("获取状态机结果,stateMachineContext:{}", JackJsonUtil.obj2String(stateMachineContext));
                return stateMachineContext;
            }

            private Map map = new HashMap();
        });
    }



    /**
     * 持久化到redis中，在分布式系统中使用
     *
     * @return
     */
    @Bean(name = "stateMachineRedisPersister")
    public RedisStateMachinePersister<E, S> getRedisPersister() {
        RedisStateMachineContextRepository<E, S> repository = new RedisStateMachineContextRepository<>(redisConnectionFactory);
        RepositoryStateMachinePersist p = new RepositoryStateMachinePersist<>(repository);
        return new RedisStateMachinePersister<>(p);
    }
}