package com.lvmc.listener;

import com.lvmc.constant.OrderStatus;
import com.lvmc.constant.OrderStatusChangeEvent;
import com.lvmc.entity.TbOrderPO;
import com.lvmc.mapper.TbOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/11/23 10:20
 */
@Slf4j
@Component
@WithStateMachine(name = "orderStateMachine")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderStateListener {

    private final TbOrderMapper orderMapper;

    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
    public void payTransition(Message<OrderStatusChangeEvent> message) {
        TbOrderPO order = (TbOrderPO) message.getHeaders().get("order");
        log.info("支付，状态机反馈信息：{}", message.getHeaders());
        //更新订单
        order.setStatus(OrderStatus.WAIT_DELIVER.getKey());
        orderMapper.updateById(order);
        //TODO 其他业务
    }

    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    public void deliverTransition(Message<OrderStatusChangeEvent> message) {
        TbOrderPO order = (TbOrderPO) message.getHeaders().get("order");
        log.info("发货，状态机反馈信息：{}", message.getHeaders());
        //更新订单
        order.setStatus(OrderStatus.WAIT_RECEIVE.getKey());
        orderMapper.updateById(order);
        //TODO 其他业务
    }

    @OnTransition(source = "WAIT_RECEIVE", target = "FINISH")
    public void receiveTransition(Message<OrderStatusChangeEvent> message) {
        TbOrderPO order = (TbOrderPO) message.getHeaders().get("order");
        log.info("确认收货，状态机反馈信息：{}", message.getHeaders().toString());
        //更新订单
        order.setStatus(OrderStatus.FINISH.getKey());
        orderMapper.updateById(order);
        //TODO 其他业务
    }
}
