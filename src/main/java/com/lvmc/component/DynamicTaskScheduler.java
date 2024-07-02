package com.lvmc.component;

import com.lvmc.service.impl.OneTimeTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/7/20 17:09
 */
@Component
@Slf4j
public class DynamicTaskScheduler {

    private final TaskScheduler taskScheduler;
    private final OneTimeTask oneTimeTask;

    private final ScheduledExecutorService executorService;

    /**
     * 任务的名字：任务
     */
    private Map<String, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();


    public DynamicTaskScheduler(TaskScheduler taskScheduler, OneTimeTask oneTimeTask) {
        this.oneTimeTask = oneTimeTask;
        this.taskScheduler = taskScheduler;
        this.executorService = Executors.newScheduledThreadPool(5);
    }

    public void scheduleTask(String cronExpression) {
        Runnable task = () -> {
            // 在此处放置你想要执行的逻辑代码
            log.info("执行逻辑...");
        };

        ScheduledFuture<?> schedule = taskScheduler.schedule(task, new CronTrigger(cronExpression));
        scheduledFutureMap.put(cronExpression, schedule);
    }

    public void stopTask(String key) {
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(key);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }


    public void scheduleOneTimeTask(String timeString, Runnable command) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeToExecute = LocalDateTime.parse(timeString, formatter);

        long initialDelay = Duration.between(LocalDateTime.now(), timeToExecute).getSeconds();

        ScheduledFuture<?> schedule = executorService.schedule(command, initialDelay, TimeUnit.SECONDS);
        scheduledFutureMap.put(timeString, schedule);
    }

    public <T> ScheduledFuture<T> scheduleOneTimeTask(String timeString, Callable<T> callable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeToExecute = LocalDateTime.parse(timeString, formatter);

        long initialDelay = Duration.between(LocalDateTime.now(), timeToExecute).getSeconds();

        ScheduledFuture<T> schedule = executorService.schedule(callable, initialDelay, TimeUnit.SECONDS);
        scheduledFutureMap.put(timeString, schedule);
        return schedule;
    }

    private void onceTask() {
        log.info("执行一次性任务...");
    }


//    @PostConstruct
    public void init() throws ExecutionException, InterruptedException {
        scheduleOneTimeTask("2023-07-20 17:26:00", this::onceTask);
        scheduleOneTimeTask("2023-07-20 17:26:00", oneTimeTask::executeTask);


        Callable<String> callable = () -> {
            log.info("进入 Callable 的 call 方法");
            return "来自 Callable 的 hello";
        };
        ScheduledFuture<String> stringScheduledFuture = scheduleOneTimeTask("2023-07-20 17:26:00", callable);
        log.info("结果：{}", stringScheduledFuture.get());
    }


}
