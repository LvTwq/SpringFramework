package com.lvmc.service;

import com.google.common.util.concurrent.AtomicDouble;
import com.lvmc.constant.MetricType;
import com.lvmc.entity.MetricData;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class MetricService {

    private static final String TARGET = "target";
    private static final String ANNOTATIONS = "annotations";

    private final MeterRegistry meterRegistry;
    private final HashMap<String, AtomicDouble> gaugeHashMap = new HashMap<>();


    public void metric(MetricData metricData) {
        String metricType = metricData.getMetricType();
        MetricType metricTypeEnum = MetricType.valueOfName(metricType);
        List<Tag> tagList = new ArrayList<>();
        if (StringUtils.isNotEmpty(metricData.getTarget())) {
            tagList.add(Tag.of(TARGET, metricData.getTarget()));
        }
        if (StringUtils.isNotEmpty(metricData.getAnnotations())) {
            tagList.add(Tag.of(ANNOTATIONS, metricData.getAnnotations()));
        }
        Map<String, String> extra = metricData.getExtra();
        if (extra != null) {
            extra.keySet().forEach(key -> {
                tagList.add(Tag.of(key, extra.get(key)));
            });
        }
        metric(metricTypeEnum, metricData.getName(), tagList, metricData.getValue());
    }



    public void metric(List<MetricData> metricDataList) {
        metricDataList.forEach(i -> metric(i));
    }


    public void metric(MetricType type, String name, List<Tag> tags, Double value) {
        switch (type) {
            case COUNTER:
                counter(name, tags, value);
                break;
            case GAUGE:
                gauge(name, tags, value);
                break;
            default:
                break;
        }
    }



    public void counter(String name, List<Tag> tags, double value) {
        meterRegistry.counter(name, tags).increment(value);
    }



    public void gauge(String name, List<Tag> tags, double value) {
        String target = tags.stream().filter(tag -> StringUtils.equals(tag.getKey(), TARGET))
                .map(Tag::getValue).findAny().orElse("");
        String annotation = tags.stream().filter(tag -> StringUtils.equals(tag.getKey(), ANNOTATIONS))
                .map(Tag::getValue).findAny().orElse("");
        String key = name + "_" + target + "_" + annotation;
        AtomicDouble gauge = gaugeHashMap.get(key);
        if (gauge == null) {
            gauge = meterRegistry.gauge(name, Tags.of(tags), new AtomicDouble(value));
            gaugeHashMap.put(key, gauge);
        } else {
            gauge.set(value);
        }
    }


    public void removeGaugeByName(String name) {
        try {
            for (Meter meter : meterRegistry.get(name).meters()) {
                meterRegistry.remove(meter);
            }
            List<String> targetList = gaugeHashMap.keySet().stream().filter(x -> x.startsWith(name + "_")).collect(Collectors.toList());
            targetList.forEach(gaugeHashMap::remove);
        } catch (Exception e) {
            log.info("{} not found，{}", name, e.getMessage());
        }
    }
}
