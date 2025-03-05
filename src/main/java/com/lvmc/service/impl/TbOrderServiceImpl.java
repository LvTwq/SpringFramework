package com.lvmc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvmc.constant.OrderStatus;
import com.lvmc.constant.OrderStatusChangeEvent;
import com.lvmc.po.TbOrderPO;
import com.lvmc.mapper.TbOrderMapper;
import com.lvmc.service.TbOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

/**
 * 订单表(TbOrder)表服务实现类
 *
 * @author 吕茂陈
 * @since 2023-11-23 10:14:51
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TbOrderServiceImpl extends ServiceImpl<TbOrderMapper, TbOrderPO> implements TbOrderService {


    private final StateMachine<OrderStatus, OrderStatusChangeEvent> orderStateMachine;
    private final StateMachinePersister<OrderStatus, OrderStatusChangeEvent, String> stateMachineMemPersister;
    private final StateMachinePersister<OrderStatus, OrderStatusChangeEvent, String> stateMachineRedisPersister;
    private final TbOrderMapper orderMapper;


    /**
     * 创建订单
     *
     * @param order
     * @return
     */
    @Override
    public TbOrderPO create(TbOrderPO order) {
        order.setStatus(OrderStatus.WAIT_PAYMENT.getKey());
        orderMapper.insert(order);
        return order;
    }

    /**
     * 对订单进行支付
     *
     * @param id
     * @return
     */
    @Override
    public TbOrderPO pay(Long id) {
        TbOrderPO order = orderMapper.selectById(id);
        log.info("线程名称：{},尝试支付，订单号：{}", Thread.currentThread().getName(), id);
        if (!sendEvent(OrderStatusChangeEvent.PAYED, order)) {
            log.error("线程名称：{},支付失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("支付失败, 订单状态异常");
        }
        return order;
    }

    /**
     * 对订单进行发货
     *
     * @param id
     * @return
     */
    @Override
    public TbOrderPO deliver(Long id) {
        TbOrderPO order = orderMapper.selectById(id);
        log.info("线程名称：{},尝试发货，订单号：{}", Thread.currentThread().getName(), id);
        if (!sendEvent(OrderStatusChangeEvent.DELIVERY, order)) {
            log.error("线程名称：{},发货失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("发货失败, 订单状态异常");
        }
        return order;
    }

    /**
     * 对订单进行确认收货
     *
     * @param id
     * @return
     */
    @Override
    public TbOrderPO receive(Long id) {
        TbOrderPO order = orderMapper.selectById(id);
        log.info("线程名称：{},尝试收货，订单号：{}", Thread.currentThread().getName(), id);
        if (!sendEvent(OrderStatusChangeEvent.RECEIVED, order)) {
            log.error("线程名称：{},收货失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("收货失败, 订单状态异常");
        }
        return order;
    }

    /**
     * 发送订单状态转换事件
     * synchronized修饰保证这个方法是线程安全的
     *
     * @param changeEvent
     * @param order
     * @return
     */
    private synchronized boolean sendEvent(OrderStatusChangeEvent changeEvent, TbOrderPO order) {
        boolean result = false;
        try {
            //启动状态机
            orderStateMachine.start();
            //尝试恢复状态机状态
            stateMachineMemPersister.restore(orderStateMachine, String.valueOf(order.getId()));
            Message message = MessageBuilder.withPayload(changeEvent).setHeader("order", order).build();
            result = orderStateMachine.sendEvent(message);
            //持久化状态机状态
            stateMachineMemPersister.persist(orderStateMachine, String.valueOf(order.getId()));
        } catch (Exception e) {
            log.error("订单操作失败:{}", e);
        } finally {
            orderStateMachine.stop();
        }
        return result;
    }
}
