

package com.southwind.controller;

import io.prometheus.client.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/4/25 16:54
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PromusController {


//    @Qualifier("requestTotalCountCollector")
    private final Counter counter;

    @GetMapping("ddd")
    public Object deoi() {
        counter.labels("path1", "method1", "code1").inc();
        return "ok";
    }

}
