package com.lvmc.task;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.system.HostInfo;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author 吕茂陈
 * @date 2021/11/11 18:19
 */
@Slf4j
@Component
public class TimeTask {

//    @Scheduled(fixedRate = 50000)
    public void push() {
        log.info("每50秒执行定时任务！！！");
    }

//    @Scheduled(fixedRate = 50000)
    public void env() {
        HostInfo hostInfo = SystemUtil.getHostInfo();
        String ip = hostInfo.getAddress();

        Environment env = SpringUtil.getBean(Environment.class);
        String port = env.getProperty("server.port");
        log.info("ip:{}, port:{}", ip, port);
    }
}
