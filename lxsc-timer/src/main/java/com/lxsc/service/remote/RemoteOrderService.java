package com.lxsc.service.remote;

import com.lxsc.commons.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "OrdersService")
public interface RemoteOrderService {

    @RequestMapping("/checkOrderPay")
    JsonResult checkOrderPay(@RequestParam String orderJson);

}
