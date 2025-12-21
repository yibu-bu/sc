package com.lxsc.orders.service.remote;

import com.lxsc.commons.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "UserService")  // 有这个注解会自动将类注册到容器
public interface RemoteUserService {

    /*
    这里的@GetMapping不同于Controller中的意思。@RequestMapping等注解的本意是地址映射，把一个方法
    和地址关联起来，但是并不决定这个方法是发请求还是接收请求。

    本地服务想要调用远程服务就直接调用这个接口中相应的方法，Feign框架会帮助我们去调用远程服务，然后返回结果。
    Feign如何找到远程服务？接口上的注解就是远程服务名（远程服务在注册中心里的名字），然后@GetMapping后面的地址
    就是要访问的远程服务的地址。

    完整流程就是：当调用这个接口中的getUserId方法时，Feign框架会帮我们调用远程服务，即向UserService模块
    中的getUserId地址发请求
     */
    @GetMapping("/getUserId")
    JsonResult<Long> getUserId(@RequestParam(name = "token") String token);
    // @RequestParam：将参数标记为请求参数，还可以解决远程服务方法形参名不一致问题

}
