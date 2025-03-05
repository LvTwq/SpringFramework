package com.lvmc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvmc.po.TbOrderPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单表(TbOrder)表数据库访问层
 *
 * @author 吕茂陈
 * @since 2023-11-23 10:14:51
 */
@Mapper
public interface TbOrderMapper extends BaseMapper<TbOrderPO> {
}
