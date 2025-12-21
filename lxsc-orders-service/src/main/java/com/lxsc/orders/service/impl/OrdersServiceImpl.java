package com.lxsc.orders.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lxsc.commons.Code;
import com.lxsc.commons.JsonResult;
import com.lxsc.orders.mapper.OrderInfoMapper;
import com.lxsc.orders.mapper.OrdersMapper;
import com.lxsc.orders.model.OrderInfo;
import com.lxsc.orders.model.Orders;
import com.lxsc.orders.service.OrdersService;
import com.lxsc.orders.service.remote.RemoteGoodsService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private RemoteGoodsService remoteGoodsService;

    @Resource
    private AmqpTemplate amqpTemplate;


    /**
     * 添加订单，基本步骤为：
     * 1.减库存
     * 2.判断减库存是否成功，如果失败直接返回
     * 3.创建订单对象和订单详情对象，设置相关参数，并添加到数据库
     *
     * @param goodsId 商品id
     * @param buyNum  购买数量
     * @param userId  用户id
     * @return 该订单对象
     */
    @Override
    @Transactional  // 开启本地事务
    @GlobalTransactional  // 开启全局事务（该注解由Seata提供）
    public Orders addOrders(Long goodsId, Integer buyNum, Long userId) {
        // 减少商品库存
        JsonResult<BigDecimal> jsonResult = remoteGoodsService.decrGoodsStore(goodsId, buyNum);
        // 如果减库存失败
        if (jsonResult.getCode().equals(Code.NO_GOODS_STORE.getCode())) {
            return null;
        }
        Orders order = new Orders();
        // 设置订单价格
        BigDecimal goodsPrice = jsonResult.getResult();
        order.setOrdersMoney(goodsPrice.multiply(new BigDecimal(buyNum)));
        // 设置订单状态，0表示待支付，详见数据库表的设计
        order.setStatus(0);
        // 设置订单编号（32位且唯一，用UUID类生成）
        order.setOrderNo(UUID.randomUUID().toString().replaceAll("-", ""));
        // 设置订单创建时间
        order.setCreateTime(System.currentTimeMillis());
        // 设置用户积分，默认0
        order.setPoint(0);
        // 设置用户id
        order.setUserId(userId);
        // 添加到数据库
        ordersMapper.insertSelective(order);
        OrderInfo orderInfo = new OrderInfo();
        // 设置详情对应的订单id
        orderInfo.setOrdersId(order.getId());
        // 设置购买数量
        orderInfo.setAmount(buyNum);
        // 设置商品id
        orderInfo.setGoodsId(goodsId);
        // 设置商品单价
        orderInfo.setPrice(goodsPrice);
        // 添加到数据库
        orderInfoMapper.insert(orderInfo);

        // 发消息给交换机
        Map messageMap = new HashMap();
        messageMap.put("orders", order);
        messageMap.put("orderInfo", orderInfo);
        String messageJson = JSONObject.toJSONString(messageMap);
        MessageProperties properties = new MessageProperties();
        // 设置消息过期的时间单位为毫秒，如果超时以后将存入死信队列。这里是100秒
        // 也就是说用户付款的时间不能超过100秒，之后死信队列的监听器会监听到这条消息，调用cancelOrder方法
        properties.setExpiration("300000");
        Message message = new Message(messageJson.getBytes(), properties);
        amqpTemplate.send("orderExchange", "", message);

        return order;
    }

    /**
     * 获取订单详情
     * 1.查询所有的订单详情
     * 2.遍历每一个订单详情，将其封装成一个Map
     *
     * @param orderId 订单id
     * @return List集合，其中每一项数据都是Map。每个map中包含的信息有：商品描述、单价、购买数量
     */
    @Override
    public List<Map<Object, Object>> getOrderInfoByOrderId(Long orderId) {
        // 查询所有订单详情
        List<OrderInfo> orderInfoList = orderInfoMapper.selectOrderInfoByOrderId(orderId);
        // 遍历所有订单详情，把每一个订单详情都封装成一个Map，并添加到最后要返回的List
        ArrayList<Map<Object, Object>> maps = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfoList) {
            HashMap<Object, Object> map = new HashMap<>();
            JsonResult<String> jsonResult = remoteGoodsService.getGoodsDescribeByGoodsId(orderInfo.getGoodsId());
            map.put("goods_describe", jsonResult.getResult());
            map.put("price", orderInfo.getPrice());
            map.put("amount", orderInfo.getAmount());
            maps.add(map);
        }
        return maps;
    }

    /**
     * 支付订单成功的后续业务
     *
     * @param orders 订单信息
     */
    @Override
    public void orderPay(Orders orders) {
        // 更新数据库的订单信息
        ordersMapper.updateByPrimaryKeySelective(orders);
        // 通知物流系统发货，通知积分系统添加积分。由于使用的是fanout类型交换机，所以会全部通知到
        // 为什么适合用mq？因为mq是异步请求，不影响主业务。发货、添加积分都属于附属业务。
        amqpTemplate.convertAndSend("orderPayExchange", "", JSONObject.toJSONString(orders));
    }

    @Override
    public void checkOrderPay(String payStatus, Orders orders) {
        if (payStatus.equals("TRADE_SUCCESS")) {
            ordersMapper.updateByPrimaryKeySelective(orders);
            amqpTemplate.convertAndSend("orderPayExchange", "", JSONObject.toJSONString(orders));
        }
    }

    /**
     * 调用该方法不是一定会取消订单，该方法有一层判断，订单状态是“未支付”的订单才会被取消
     */
    @Transactional
    @GlobalTransactional // 开启Seata全局事务（分布式事务）
    public void cancelOrder(Orders orders, OrderInfo orderInfo) {
        // 根据订单Id获取订单
        Orders order = ordersMapper.selectByPrimaryKey(orders.getId());
        // 进入if表示当前订单没有支付需要回退库，删除订单
        if (order.getStatus() == 0) {
            orderInfoMapper.deleteByOrderId(orderInfo.getOrdersId());
            ordersMapper.deleteByPrimaryKey(orders.getId());
            remoteGoodsService.incrGoodsStore(orderInfo.getGoodsId(), orderInfo.getAmount());
        }
    }

}
