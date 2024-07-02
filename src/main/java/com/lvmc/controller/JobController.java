package com.lvmc.controller;

import com.lvmc.config.ScheduledTasksConfig;
import com.lvmc.vo.JobVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/12/6 15:05
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("job")
public class JobController {

    private final ScheduledTasksConfig scheduledTasksConfig;

    @GetMapping("list")
    public List<JobVo> listJobs() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents("com.lvmc.task");

        List<JobVo> jobVoList = new ArrayList<>();
        beanDefinitions.forEach(bean -> {
            try {
                Class<?> targetClass = Class.forName(bean.getBeanClassName());
                String beanClassName = targetClass.getName();

                Method[] methods = targetClass.getDeclaredMethods();
                for (Method method : methods) {
                    Scheduled annotation = method.getAnnotation(Scheduled.class);
                    if (null != annotation) {
                        jobVoList.add(new JobVo(beanClassName, method.getName(), annotation.cron()));
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return jobVoList;
    }


    @GetMapping("exec/{taskName}")
    public void execTask(@PathVariable String taskName) {
        scheduledTasksConfig.executeTask(taskName);
    }

}
