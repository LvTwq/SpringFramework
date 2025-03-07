package com.lvmc.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class MetricData {

    /**
     * 指标类型 counter gauge
     */
    private String metricType;
    /**
     * 指标名称 指标名称 定义方式 分类代号.模块代号.{}.{}.指标 如
     * <p> license.expire node.cpu.usage <p/>
     */
    private String name;

    /**
     * 监控指标对象 服务器ip或者任务名称 ,可以为空
     */
    private String target;
    /**
     * 指标值
     */
    private Double value;
    /**
     * 扩展属性
     */
    private Map<String, String> extra;
    /**
     * 对象详细信息
     */
    private String annotations;


}
