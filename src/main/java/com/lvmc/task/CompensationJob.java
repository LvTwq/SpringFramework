package com.lvmc.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lvmc.service.impl.MemberService;
import com.lvmc.service.impl.MqUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 吕茂陈
 * @date 2022-07-15 15:00
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompensationJob {

    /**
     * 补偿Job异步处理线程池
     */
    private static ThreadPoolExecutor compensationThreadPool = new ThreadPoolExecutor(
            10, 10,
            1, TimeUnit.HOURS,
            new ArrayBlockingQueue<>(1000),
            new ThreadFactoryBuilder().setNameFormat("compensation-thread-pool-%d").build()
    );

    private final MqUserService mqUserService;

    private final MemberService memberService;

    /**
     * 目前补偿到哪个用户id
     */
    private long offset = 0;

    /**
     * 10秒后开始补偿，5秒补偿一次
     */
//    @Scheduled(initialDelay = 10_000, fixedRate = 5_000)
    public void compensationJob() {
        log.info("开始从用户ID {} 补偿", offset);
        mqUserService.getUsersAfterIdWithLimit(offset, 5).forEach(user -> {
            compensationThreadPool.execute(() -> memberService.welcome(user));
            offset = user.getId();
        });
    }
}
