package com.southwind.controller;

import com.southwind.component.DynamicTaskScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/7/20 17:10
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DynamicTaskController {

    private final DynamicTaskScheduler dynamicTaskScheduler;


    @PostMapping("/schedule")
    public String scheduleTask(@RequestBody Map<String, String> cronExpressionMap) {
        // "cron": "0 0/1 * * * ?"
        dynamicTaskScheduler.scheduleTask(cronExpressionMap.get("cron"));
        return "Task scheduled with cron expression: " + cronExpressionMap;
    }

    @PostMapping("/stop/{key}")
    public String stopTask(@PathVariable String key) {
        dynamicTaskScheduler.stopTask(key);
        return "Task stopped";
    }


}
