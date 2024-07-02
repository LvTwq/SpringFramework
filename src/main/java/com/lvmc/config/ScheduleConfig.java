package com.lvmc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author 吕茂陈
 * @description
 * @date 2024/3/8 14:23
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    private volatile ScheduledTaskRegistrar taskRegistrar;


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        taskRegistrar.setScheduler(scheduledExecutorService);
        this.taskRegistrar = taskRegistrar;
    }
}
