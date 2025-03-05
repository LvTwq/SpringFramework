package com.lvmc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvmc.po.TbOrderPO;

/**
 * 订单表(TbOrder)表服务接口
 *
 * @author 吕茂陈
 * @since 2023-11-23 10:14:51
 */
public interface TbOrderService extends IService<TbOrderPO> {


    TbOrderPO receive(Long id);

    TbOrderPO deliver(Long id);

    TbOrderPO pay(Long id);


    TbOrderPO create(TbOrderPO order);
}
