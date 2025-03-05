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
        String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=oracle.lvmc.top)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=ORCLPDB)))";
        log.info("南通公安数据导入，数据库连接信息，{}", url);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl(url);
        dataSource.setUsername("system");
        dataSource.setPassword("oracle");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from HJ_GW.TLPT_ORG where ORG_CODE = '320684660100'");
        log.info("{}", JackJsonUtil.obj2String(maps));
    }
}
