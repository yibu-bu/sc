package com.lxsc.orders.listener;

import com.alibaba.fastjson.JSONObject;
import com.lxsc.orders.model.OrderInfo;
import com.lxsc.orders.model.Orders;
import com.lxsc.orders.service.OrdersService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitMQListener {

    @Resource
    private OrdersService ordersService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AmqpTemplate amqpTemplate;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue("ordersStatus"),
                    exchange = @Exchange(name = "ordersStatusExchange", type = "fanout"
                    ))
    })
    public void deleteOrdersFromRedis(String message) {
        // Set<String> set = stringRedisTemplate.opsForZSet().range("orders", 0, -1);
        // for (String s : set) {
        //     System.out.println("s = " + s);
        // }
        // 将订单备份数据从Redis中删除,这样定时任务就不会再继续发送消息,表示订单完成
        // 我们利用监听器从MQ中取出的数据积分或物流服务写入到MQ中的订单备份数据的Json数据
        System.out.println("待删除的json：" + message);
        JSONObject.parseObject(message, Orders.class);
        message = JSONObject.toJSONString(message);
        // todo 硬编码测试可以，形参接到的就不行，中间传输时可能有点空格什么的区别
        message = "{\"orderNo\":\"e141347f9a66489197bc721a20d27041\",\"id\":218,\"point\":0,\"addressId\":1}";
        Long res = stringRedisTemplate.opsForZSet().remove("orders", message);
        System.out.println("删除结果：" + res);
    }

    @RabbitListener(bindings = {
            @QueueBinding(key = {"orderDeadLetterKey"},
                    value = @Queue("orderDeadLetterQueue"),
                    exchange = @Exchange(name = "orderDeadLetterExchange", type = "direct")
            )
    })
    public void orderDeadLetterListener(String message) {
        System.out.println("死信队列监听到了消息");
        Map map = JSONObject.parseObject(message, HashMap.class);
        JSONObject orderJson = (JSONObject) map.get("orders");
        JSONObject orderInfoJson = (JSONObject) map.get("orderInfo");
        Orders orders = orderJson.toJavaObject(Orders.class);
        OrderInfo orderInfo = orderInfoJson.toJavaObject(OrderInfo.class);
        ordersService.cancelOrder(orders, orderInfo);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue("wuLiuQueue"),
                    exchange = @Exchange(name = "orderPayExchange", type = "fanout")
            )
    })
    public void wuLiuQueueListener(String message) {
        System.out.println("物流模块接收到了消息：" + message);
        Long num = stringRedisTemplate.opsForValue().increment("ordersStatus");
        amqpTemplate.convertAndSend("ordersStatusExchange", "", message);
        // if (num == 2) {
        //     amqpTemplate.convertAndSend("ordersStatusExchange", "", message);
        // }
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue("pointQueue"),
                    exchange = @Exchange(
                            name = "orderPayExchange",
                            type = "fanout")
            )
    })
    public void pointQueueListener(String message) {
        System.out.println("积分模块接收到了消息：" + message);
        Long num = stringRedisTemplate.opsForValue().increment("ordersStatus");
        amqpTemplate.convertAndSend("ordersStatusExchange", "", message);
        // if (num == 2) {
        //     amqpTemplate.convertAndSend("ordersStatusExchange", "", message);
        // }

    }

}
