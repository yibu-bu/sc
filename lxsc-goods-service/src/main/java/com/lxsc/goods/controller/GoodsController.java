package com.lxsc.goods.controller;

import com.lxsc.commons.Code;
import com.lxsc.commons.JsonResult;
import com.lxsc.goods.service.GoodsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@CrossOrigin
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    /**
     * 减库存
     *
     * @param goodsId 商品id
     * @param buyNum  购买数量
     * @return Json格式数据，请求成功就返回商品单价
     */
    @GetMapping("/decrGoodsStore")
    public JsonResult<BigDecimal> decrGoodsStore(Long goodsId, Integer buyNum) {
        BigDecimal price = goodsService.decrGoodsStore(goodsId, buyNum);
        if (price == null) {
            return new JsonResult<>(Code.NO_GOODS_STORE, null);
        }
        return new JsonResult<>(Code.OK, price);
    }

    @GetMapping("/incrGoodsStore")
    public void incrGoodsStore(Long goodsId, Integer buyNum) {
        goodsService.incrGoodsStore(goodsId, buyNum);
    }

    @GetMapping("/getGoodsDescribeByGoodsId")
    public Object getGoodsDescribeByGoodsId(Long goodsId) {
        String describe = goodsService.getGoodsDescribeByGoodsId(goodsId);
        if (describe == null) {
            return new JsonResult<>(Code.ERROR, "没有该商品的描述", null);
        }
        return new JsonResult<>(Code.OK, describe);
    }

}
