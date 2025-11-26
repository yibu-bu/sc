package com.lxsc.orders.service;

import com.lxsc.orders.model.OrderInfo;
import com.lxsc.orders.model.Orders;

import java.util.List;
import java.util.Map;

public interface OrdersService {

    /**
     * 添加订单
     *
     * @param goodsId 商品id
     * @param buyNum  购买数量
     * @param userId  用户id
     * @return 该订单对象
     */
    Orders addOrders(Long goodsId, Integer buyNum, Long userId);

    /**
     * 获取订单详情。一个订单可以有多个详情，表示一个订单可以同时买多个商品
     *
     * @param orderId 订单id
     * @return List集合，其中每一项数据都是Map，每个map中包含的信息有：商品描述、单价、购买数量
     */
    List<Map<Object, Object>> getOrderInfoByOrderId(Long orderId);

    /**
     * 支付订单成功的后续业务，修改订单信息、通知物流系统发货等
     *
     * @param orders 订单信息
     */
    void orderPay(Orders orders);

    void checkOrderPay(String payStatus, Orders orders);

    void cancelOrder(Orders orders, OrderInfo orderInfo);

}
