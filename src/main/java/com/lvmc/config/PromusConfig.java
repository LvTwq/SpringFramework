

package com.lvmc.config;

import com.lvmc.component.YourCustomCollector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/4/25 16:52
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PromusConfig {


    private final CollectorRegistry collectorRegistry;


    @Bean
    public Counter counter() {
        return Counter.build()
            .name("http_requests_total")
            .labelNames("path", "method", "code")
            .help("http请求总计数").register(collectorRegistry);
    }


    @Bean
    @Primary
    public YourCustomCollector yourCustomCollector(){
        return new YourCustomCollector().register(collectorRegistry);
    }

}
