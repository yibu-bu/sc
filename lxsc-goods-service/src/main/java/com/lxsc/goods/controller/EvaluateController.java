package com.lxsc.goods.controller;

import com.lxsc.commons.Code;
import com.lxsc.commons.JsonResult;
import com.lxsc.commons.PageBean;
import com.lxsc.goods.model.Evaluate;
import com.lxsc.goods.service.EvaluateService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class EvaluateController {

    @Resource
    private EvaluateService evaluateService;

    /**
     * 获取商品分页评价
     *
     * @param goodsId       要获取的商品id
     * @param pageNo        要展示的页码
     * @param pageSize      单页大小
     * @param evaluateLevel 评价等级
     * @return 分页商品评价
     */
    @GetMapping("/getEvaluateListPage")
    public Object getEvaluateListPage(Long goodsId, Long pageNo, Long pageSize, String evaluateLevel) {
        PageBean<List<Evaluate>> evaluateListPage = evaluateService.getEvaluateListPage(goodsId, pageNo, pageSize, evaluateLevel);
        return new JsonResult<>(Code.OK, evaluateListPage);
    }

    /**
     * 获取商品的评价数量
     *
     * @param goodsId 商品id
     * @return JsonResult中携带一个map集合，包含各个评价等级的数量
     */
    @GetMapping("/countEvaluateNum")
    public Object countEvaluateNum(Long goodsId) {
        Map<String, Integer> map = evaluateService.countEvaluateNum(goodsId);
        return new JsonResult<>(Code.OK, map);
    }

}
