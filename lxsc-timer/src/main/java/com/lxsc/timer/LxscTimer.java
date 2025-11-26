package com.lxsc.timer;

import com.lxsc.commons.JsonResult;
import com.lxsc.service.remote.RemoteOrderService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 定时任务类
 */
@Component
public class LxscTimer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RemoteOrderService remoteOrderService;

    /**
     * 该方法会在后台不断执行，执行频率由cron表达式决定，这里设定的是每秒执行一次
     * 该方法会不断从Redis中获取存放超过5分钟的订单信息，这些订单已经过了5分钟还没有被删除，说明可能出现了掉单现象
     * 对于这些订单，这里的处理是调用远程订单模块的checkOrderPay方法去检查这些疑似调单的订单
     * checkOrderPay如果返回成功说明经过检查没有掉单，就直接删掉Redis中的数据
     */
    @Scheduled(cron = "* * * * * *") // sron表达式有六位组成，分别是：秒、分、时、日、月、周，*表示通配
    public void checkOrderPay() {
        // 获取5分钟之前的时间戳
        long maxScore = System.currentTimeMillis() - 1000 * 60 * 5;
        // 从Zset中获取分数从0到当前时间5分钟之前的所有消息，也就是存在于Redis中超过5分钟的消息。这些消息可能掉单了
        Set<String> ordersSet = stringRedisTemplate.opsForZSet().rangeByScore("orders", 0, maxScore);
        if (ordersSet != null) {
            for (String orderJson : ordersSet) {
                JsonResult jsonResult = remoteOrderService.checkOrderPay(orderJson);
                System.out.println("code = " + jsonResult.getCode() + ", orderJson = " + orderJson);
                if (jsonResult.getCode().equals("10000")) {
                    stringRedisTemplate.opsForZSet().remove("orders", orderJson);
                }
            }
        }
    }

}
