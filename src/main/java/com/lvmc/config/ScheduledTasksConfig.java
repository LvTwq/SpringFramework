package com.lvmc.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import java.util.Date;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/12/6 16:50
 */
@Configuration
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduledTasksConfig {

    private final TaskScheduler taskScheduler;
    private final ScheduledAnnotationBeanPostProcessor postProcessor;


    public void executeTask(String taskName) {
        for (ScheduledTask scheduledTask : postProcessor.getScheduledTasks()) {
            Runnable runnable = scheduledTask.getTask().getRunnable();
            if (runnable instanceof ScheduledMethodRunnable) {
                ScheduledMethodRunnable scheduledMethodRunnable = (ScheduledMethodRunnable) runnable;
                if (StringUtils.equals(scheduledMethodRunnable.toString(), taskName)) {
                    taskScheduler.schedule(runnable, new Date());
                    log.info("Manually triggered task: {}", taskName);
                    return;
                }
            }
        }
        log.warn("Task not found: {}", taskName);
    }

}
