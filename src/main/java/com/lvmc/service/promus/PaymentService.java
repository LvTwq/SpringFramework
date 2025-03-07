package com.lvmc.service.promus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 吕茂陈
 * @description
 * @date 2025/3/7 11:26
 */
@Component
public class PaymentService {

    // 场景：统计支付失败次数
    private final Counter paymentFailureCounter;
    private final MeterRegistry registry;

    public PaymentService(MeterRegistry registry) {
        // 定义 Counter：payment_failures_total，带标签 method 和 type
        paymentFailureCounter = Counter.builder("payment_failures_total")
                .description("Total number of payment failures")
                .tag("method", "credit_card")  // 固定标签
                .tag("type", "external")       // 动态标签需通过方法参数传递
                .register(registry);
        this.registry = registry;
    }

    public void processPayment() {
        try {
            // 业务逻辑
        } catch (Exception e) {
            paymentFailureCounter.increment();  // +1
            throw e;
        }
    }

    // 原子操作递增方法
    @Scheduled(fixedRate = 5000)
    public void increment() {
        Counter counter = Counter.builder("http_requests_total")
                .tags("status", "200", "path", "/api/payment")  // 单个标签示例：("status", "500")
                .register(registry);
        counter.increment();
    }
}
