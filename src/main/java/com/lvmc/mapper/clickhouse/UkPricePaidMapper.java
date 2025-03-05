package com.lvmc.mapper.clickhouse;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvmc.po.UkPricePaidPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * (UkPricePaid)表数据库访问层
 *
 * @author 吕茂陈
 * @since 2024-11-18 14:22:47
 */
@Mapper
@DS("clickhouse")
public interface UkPricePaidMapper extends BaseMapper<UkPricePaidPO> {
}
