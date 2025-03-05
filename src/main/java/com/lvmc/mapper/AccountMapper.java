package com.lvmc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvmc.po.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Account)表数据库访问层
 *
 * @author 吕茂陈
 * @since 2023-10-19 20:13:09
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
