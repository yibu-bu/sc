package com.lxsc.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.lxsc.commons.Code;
import com.lxsc.commons.JsonResult;
import com.lxsc.user.model.Address;
import com.lxsc.user.model.User;
import com.lxsc.user.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;

@RestController
@CrossOrigin  // 允许Ajax跨域
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private String getUserJson(String token) {
        String userJson = stringRedisTemplate.opsForValue().get("userToken:" + token);
        if (userJson == null) {
            return null;
        }
        // 刷新登录有效时长
        // Duration timeout = Duration.ofSeconds(60 * 30);
        // stringRedisTemplate.expire("userToken:" + token, timeout);
        return userJson;
    }

    /**
     * 获取用户地址
     *
     * @param token 用户token
     * @return 如果token无效返回null，否则以List形式返回用户的所有地址，且默认地址是第一个
     */
    @RequestMapping("/confirmUserAddress")
    public Object confirmUserAddress(String token) {
        String userJson = getUserJson(token);
        // 如果没有获取到userToken，说明用户没有登录
        if (userJson == null) {
            return new JsonResult<>(Code.NO_LOGIN, null);
        }
        Long userId = JSONObject.parseObject(userJson).getBigInteger("id").longValue();
        List<Address> list = userService.getUserAddress(userId);
        return new JsonResult<>(Code.OK, list);
    }

    /**
     * 获取用户id
     *
     * @param token 用户token
     * @return 用户id，如果token无效返回null（未登录）
     */
    @RequestMapping("/getUserId")
    public Object getUserId(String token) {
        String userJson = getUserJson(token);
        // 如果没有获取到userToken，说明用户没有登录
        if (userJson == null) {
            return new JsonResult<>(Code.NO_LOGIN, null);
        }
        Long userId = JSONObject.parseObject(userJson).getBigInteger("id").longValue();
        return new JsonResult<>(Code.OK, userId);
    }

    @PostMapping("/reg")
    public Object reg(User user) {
        int result = userService.reg(user);
        // 如果注册失败
        switch (result) {
            case 1:
                return new JsonResult<>(Code.ERROR, "手机号不能为空", "");
            case 2:
                return new JsonResult<>(Code.ERROR, "密码不能为空", "");
            case 3:
                return new JsonResult<>(Code.ERROR, "手机号已存在，请更换或直接登录", "");
        }
        // 运行到这里说明注册成功，前端部分写的是注册成功后就直接登录，所以接下来的部分和login方法中的相同
        // 生成token，这个是用户成功登录后的身份令牌
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 把token存到Redis中，有效时间为30分钟
        Duration timeout = Duration.ofSeconds(60 * 30); // 设置有效时间
        stringRedisTemplate.opsForValue().set("userToken:" + token, JSONObject.toJSONString(user), timeout);
        // 将用户数据返回给前端
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("token", token);
        resultData.put("id", user.getId());
        resultData.put("phone", user.getPhone());
        resultData.put("nickName", user.getNickName());
        return new JsonResult<>(Code.OK, "注册成功", resultData);
    }

    @PostMapping("/login")
    public Object login(User user) {
        int result = userService.login(user);  // 如果登录成功，这个user对象中的字段会同步为数据库中的值
        String loginErrorNumStr = stringRedisTemplate.opsForValue().get("loginErrorNum:" + user.getPhone());
        // 如果登录失败次数达到5次，就冻结账号
        if (loginErrorNumStr != null && Long.parseLong(loginErrorNumStr) >= 5) {
            return new JsonResult<>(Code.ERROR, "你的账号已冻结，明天再来", "");
        }
        // 如果登录失败
        if (result != 0) {
            // 递增用户登陆失败次数increment方法用于递增一个key，如果没有就创建
            Long i = stringRedisTemplate.opsForValue().increment("loginErrorNum:" + user.getPhone());
            // 设置错误登录计数器的时效，i = 1表示第一次登录失败，只用设置一次就够了
            if (i == 1) {
                setLoginErrorNumDuration(user.getPhone());
            }
            // 返回时不提示具体错误信息，以防用户猜密码
            return new JsonResult<>(Code.ERROR, "用户名或密码错误", "");
        }
        // 运行到这里就说明登录成功了，删除之前的登陆失败计数器（不删除也行，看业务需求）
        stringRedisTemplate.delete("loginErrorNum:" + user.getPhone());
        // 生成token，这个是用户成功登录后的身份令牌（唯一、随机）
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 把token存到Redis中，有效时间为30分钟
        Duration timeout = Duration.ofSeconds(60 * 30); // 设置有效时间
        stringRedisTemplate.opsForValue().set("userToken:" + token, JSONObject.toJSONString(user), timeout);
        // 将用户数据返回给前端
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("token", token);
        resultData.put("id", user.getId());
        resultData.put("phone", user.getPhone());
        resultData.put("nickName", user.getNickName());
        return new JsonResult<>(Code.OK, "登录成功", resultData);
    }

    /**
     * 设置登录错误计数器的时效，有效期至第二天的0时0分0秒
     *
     * @param phone 用户的手机号
     */
    private void setLoginErrorNumDuration(String phone) {
        // 创建日历对象，默认初始化为它被创建的时间
        Calendar calendar = Calendar.getInstance();
        // 设置日历对象的时分秒为0时0分0秒
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        // 在日历当前时间上加一天
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        // 计算有效期（单位：秒）
        Duration timeout = Duration.ofSeconds((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000);
        // 设置时效，stringRedisTemplate.expire方法用于设置一个键的时效，如果键不存在返回false
        stringRedisTemplate.expire("loginErrorNum:" + phone, timeout);
    }

}
