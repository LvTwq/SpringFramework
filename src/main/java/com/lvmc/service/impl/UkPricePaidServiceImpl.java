package com.lvmc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import  com.lvmc.service.UkPricePaidService;
import com.lvmc.mapper.clickhouse.UkPricePaidMapper;
import com.lvmc.po.UkPricePaidPO;
import org.springframework.stereotype.Service;

/**
 * (UkPricePaid)表服务实现类
 *
 * @author 吕茂陈
 * @since 2024-11-18 14:22:47
 */
@Service
public class UkPricePaidServiceImpl extends ServiceImpl<UkPricePaidMapper, UkPricePaidPO> implements UkPricePaidService {
}
