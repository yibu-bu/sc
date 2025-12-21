package com.lxsc.orders.service.remote;

import com.lxsc.commons.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "GoodsService")
public interface RemoteGoodsService {

    @GetMapping("/decrGoodsStore")
    JsonResult<BigDecimal> decrGoodsStore(@RequestParam Long goodsId, @RequestParam Integer buyNum);

    @GetMapping("/getGoodsDescribeByGoodsId")
    JsonResult<String> getGoodsDescribeByGoodsId(@RequestParam Long goodsId);

    @GetMapping("/incrGoodsStore")
    void incrGoodsStore(@RequestParam Long goodsId, @RequestParam Integer buyNum);

}
