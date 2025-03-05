package com.lvmc.controller;

import com.lvmc.vo.DnsCommandVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @description
 * @date 2024/10/14 15:09
 */
@Slf4j
@RestController
@RequestMapping
public class DnsCommandController {


    @PostMapping(value = "/xml-request")
    public void handleXmlRequest(@RequestBody DnsCommandVo dnsCommandVo) {
        // 处理请求
        log.info("{}", dnsCommandVo);
    }
}
