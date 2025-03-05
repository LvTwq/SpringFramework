package com.lvmc.controller;

import com.lvmc.utils.JackJsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 吕茂陈
 * @description
 * @date 2025/2/26 15:54
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbController {

    @GetMapping("oracle")
    public void oracle() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl("");
        dataSource.setUsername("system");
        dataSource.setPassword("oracle");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("");
        log.info("{}", JackJsonUtil.obj2String(maps));
    }
}
