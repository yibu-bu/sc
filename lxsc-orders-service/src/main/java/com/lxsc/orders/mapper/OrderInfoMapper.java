package com.lxsc.orders.mapper;

import com.lxsc.orders.model.OrderInfo;

import java.util.List;

public interface OrderInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    /**
     * 通过订单 id查询其所有订单详情
     *
     * @return List集合，所有订单详情
     */
    List<OrderInfo> selectOrderInfoByOrderId(Long orderId);

    void deleteByOrderId(Long ordersId);

}