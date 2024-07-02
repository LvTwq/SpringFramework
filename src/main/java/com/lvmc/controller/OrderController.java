package com.lvmc.controller;

import com.lvmc.entity.TbOrderPO;
import com.lvmc.service.TbOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/11/23 10:12
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {


    private final TbOrderService orderService;


    /**
     * 根据id查询订单
     *
     * @return
     */
    @RequestMapping("/getById")
    public TbOrderPO getById(@RequestParam("id") Long id) {
        //根据id查询订单
        return orderService.getById(id);
    }

    /**
     * 创建订单
     *
     * @return
     */
    @RequestMapping("/create")
    public String create(@RequestBody TbOrderPO order) {
        //创建订单
        orderService.create(order);
        return "sucess";
    }

    /**
     * 对订单进行支付
     *
     * @param id
     * @return
     */
    @RequestMapping("/pay")
    public String pay(@RequestParam("id") Long id) {
        //对订单进行支付
        orderService.pay(id);
        return "success";
    }

    /**
     * 对订单进行发货
     *
     * @param id
     * @return
     */
    @RequestMapping("/deliver")
    public String deliver(@RequestParam("id") Long id) {
        //对订单进行确认收货
        orderService.deliver(id);
        return "success";
    }

    /**
     * 对订单进行确认收货
     *
     * @param id
     * @return
     */
    @RequestMapping("/receive")
    public String receive(@RequestParam("id") Long id) {
        //对订单进行确认收货
        orderService.receive(id);
        return "success";
    }
}
