package com.lxsc.goods.service;

import com.lxsc.commons.PageBean;
import com.lxsc.goods.model.Evaluate;

import java.util.List;
import java.util.Map;

public interface EvaluateService {

    /**
     * 获取商品评价
     *
     * @param goodsId       商品id
     * @param pageNo        页码
     * @param pageSize      单页大小
     * @param evaluateLevel 评价等级。A：好评，B：中评，C：差评，img：有图的评论
     * @return 封装的商品评价数据
     */
    PageBean<List<Evaluate>> getEvaluateListPage(Long goodsId, Long pageNo, Long pageSize, String evaluateLevel);

    /**
     * 获取商品的评价数量
     *
     * @param goodsId 商品id
     * @return 一个map集合，包含各个评价等级的数量
     */
    Map<String, Integer> countEvaluateNum(Long goodsId);

}
