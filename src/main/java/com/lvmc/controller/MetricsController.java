package com.lvmc.controller;

import com.lvmc.entity.Account;
import com.lvmc.service.impl.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 对 @RestController 自动进行监控
 *
 * @author 吕茂陈
 * @date 2022-07-07 17:15
 */
@Slf4j
@RestController
//@Metrics(logParameters = false, logReturn = false)
@RequestMapping("metricstest")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MetricsController {

    private final AccountService accountService;


    @GetMapping("transaction")
    public int transaction(@RequestParam("name") String name) {
        try {
            accountService.createAccount(Account.builder().id(UUID.randomUUID().hashCode()).username(name).build());
        } catch (Exception ex) {
            log.error("create user failed because，", ex);
        }
        return accountService.findByUsername(name);
    }
}


