package com.lvmc.vo;

import lombok.Data;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/12/6 15:35
 */
@Data
public class JobVo {

    private String beanName;
    private String methodName;
    private String cronExpression;

    public JobVo(String beanClassName, String name, String cron) {
        this.beanName = beanClassName;
        this.methodName = name;
        this.cronExpression = cron;
    }
}
