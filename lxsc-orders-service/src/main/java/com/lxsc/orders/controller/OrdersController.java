package com.lxsc.orders.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.lxsc.commons.Code;
import com.lxsc.commons.JsonResult;
import com.lxsc.orders.config.AlipayConfig;
import com.lxsc.orders.model.Orders;
import com.lxsc.orders.service.OrdersService;
import com.lxsc.orders.service.remote.RemoteUserService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class OrdersController {

    @Resource
    private RemoteUserService remoteUserService;

    @Resource
    private OrdersService ordersService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/addOrder")
    public Object addOrder(Long goodsId, Integer buyNum, String token) {
        JsonResult<Long> jsonResult = remoteUserService.getUserId(token);
        // 判断是否没有登录
        if (jsonResult.getCode().equals(Code.NO_LOGIN.getCode())) {
            return new JsonResult<>(Code.NO_LOGIN, null);
        }
        Long userId = jsonResult.getResult();
        // 执行添加订单的动作
        Orders order = ordersService.addOrders(goodsId, buyNum, userId);
        // 如果返回null表示减库存失败了
        if (order == null) {
            return new JsonResult<>(Code.NO_GOODS_STORE, null);
        }
        HashMap<Object, Object> orderMap = new HashMap<>();
        orderMap.put("ordersMoney", order.getOrdersMoney());
        orderMap.put("orderId", order.getId());
        orderMap.put("orderNo", order.getOrderNo());
        return new JsonResult<>(Code.OK, orderMap);  // 返回该订单id给前端
    }

    @RequestMapping("/confirmOrderInfo")
    public Object confirmOrderInfo(Long orderId) {
        List<Map<Object, Object>> orderInfoList = ordersService.getOrderInfoByOrderId(orderId);
        return new JsonResult<>(Code.OK, orderInfoList);
    }

    // todo 有待调试
    @RequestMapping("/checkOrderPay")
    public Object checkOrderPay(String orderJson) throws AlipayApiException {
        System.out.println(orderJson);
        Orders order = JSONObject.parseObject(orderJson, Orders.class);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        System.out.println("orderNo = " + order.getOrderNo());
        bizContent.put("out_trade_no", order.getOrderNo());
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("alipay_trade_query_response");
            String payStatus = jsonObject1.getString("trade_status");
            ordersService.checkOrderPay(payStatus, order);
        } else {
            System.out.println("调用失败");
            System.out.println(response.getBody());
        }
        return new JsonResult(Code.OK, order);
    }

    /**
     * 处理前端发来的支付请求，参数是订单的相关参数
     */
    @RequestMapping("/pay")
    public Object pay(Long orderId, String orderNo, Integer point, BigDecimal actualMoney, Long addressId, String token, String payType) throws AlipayApiException {
        // 判断用户是否登录
        JsonResult<Long> jsonResult = remoteUserService.getUserId(token);
        if (jsonResult.getCode().equals(Code.NO_LOGIN.getCode())) {
            return new JsonResult<>(Code.NO_LOGIN, null);
        }
        // 将待支付订单存入Redis，且设置时效，超时后自动删除
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", orderId);
        map.put("addressId", addressId);
        map.put("point", point);
        map.put("orderNo", orderNo);
        String orderJson = JSONObject.toJSONString(map);
        stringRedisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                // 开启事务
                redisOperations.multi();
                // 将待支付的订单存入redis，并设置超时时间。这个是给支付成功的回调函数取订单数据用的
                redisOperations.opsForValue().setIfAbsent((K) ("orderPay" + orderNo), (V) orderJson, Duration.ofSeconds(60 * 45));
                // 需要通过定时任务不断扫描这个数据，防止掉单。这个是防止掉单用的，只有确定完成订单后才可删除这个数据
                redisOperations.opsForZSet().add((K) "orders", (V) orderJson, System.currentTimeMillis());
                // 提交事务
                return redisOperations.exec();
            }
        });
        if (payType.equals("zfb")) {
            return zfbPay(orderId, orderNo, point, actualMoney, addressId, token, payType);
        }
        return "准备支付";
    }

    /**
     * 使用支付宝支付
     */
    private String zfbPay(Long orderId, String orderNo, Integer point, BigDecimal actualMoney, Long addressId, String token, String payType) throws AlipayApiException {
        // 创建支付宝支付的客户端对象,指定支付宝支付时的公共参数
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        // 创建支付宝请求对象
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        // 设置异步和同步响应地址
        request.setNotifyUrl(AlipayConfig.notify_url);
        request.setReturnUrl(AlipayConfig.return_url);
        // 定义业务参数
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderNo);                  // 订单编号
        bizContent.put("total_amount", actualMoney);              // 支付金额
        bizContent.put("subject", "商城订单支付");               // 订单标题
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY"); // 产品类型
        // 设置请求参数
        request.setBizContent(bizContent.toString());
        // 发送请求,获取用户支付页面
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
            String str = response.getBody();
            System.out.println(str);
            return str;
        } else {
            System.out.println("调用失败");
        }
        return "";
    }

    /**
     * 支付成功后做的回调操作：修改订单信息，设置订单状态为“待发货”
     *
     * @param out_trade_no 支付宝支付订单
     * @param total_amount 实付金额，用于转化成用户的积分
     */
    @RequestMapping("/paySuccess")
    public Object paySuccess(String out_trade_no, BigDecimal total_amount) {
        // 从Redis中获取待支付订单信息
        String orderJson = stringRedisTemplate.opsForValue().get("orderPay" + out_trade_no);
        // 把JSON格式数据转成对象
        Orders orders = JSONObject.parseObject(orderJson, Orders.class);
        orders.setStatus(1);  // 1表示待发货
        orders.setActualMoney(total_amount);
        ordersService.orderPay(orders);
        return "支付成功";
    }

}
