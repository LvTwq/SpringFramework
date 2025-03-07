

package com.lvmc.controller;

import com.lvmc.constant.MetricType;
import com.lvmc.entity.MetricData;
import com.lvmc.service.MetricService;
import com.lvmc.service.promus.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/4/25 16:54
 */
@Slf4j
@RestController
@RequestMapping("promos")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PromosController {

    private final MetricService metricService;
    private final PaymentService paymentService;

    @GetMapping("/trigger-error")
    public String triggerError() {
        // 模拟错误发生，增加计数
        return "Error triggered!";
    }


    @Scheduled(fixedRate = 5000)
    public void reportMetrics() {

        MetricData metricData = new MetricData();
        metricData.setMetricType(MetricType.COUNTER.getValue());
        metricData.setName("lvmc.restful.requests");
        metricData.setTarget("lvmc");
        metricData.setValue(1.0);
        Map<String, String> extra = new HashMap<>(8);
        extra.put("status", "200");
        extra.put("method", "GET");
        metricData.setExtra(extra);
        // 发送
        metricService.metric(metricData);
    }

}
