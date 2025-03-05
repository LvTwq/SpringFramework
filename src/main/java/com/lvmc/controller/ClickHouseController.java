package com.lvmc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lvmc.po.UkPricePaidPO;
import com.lvmc.service.UkPricePaidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 吕茂陈
 * @description
 * @date 2024/11/18 14:25
 */
@Slf4j
@RestController
@RequestMapping("clickhouse")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ClickHouseController {


    private final UkPricePaidService pricePaidService;


    @GetMapping("list")
    public List<UkPricePaidPO> list() {
        LambdaQueryWrapper<UkPricePaidPO> wrapper = new LambdaQueryWrapper<UkPricePaidPO>()
                .eq(UkPricePaidPO::getPrice, "145000")
                .eq(UkPricePaidPO::getDate, "2008-11-19");
        return pricePaidService.list(wrapper);
    }
}
