package com.lxsc.goods.service;

import java.math.BigDecimal;

public interface GoodsService {

    /**
     * 减商品库存
     *
     * @param goodsId 商品id
     * @param buyNum  购买数量
     * @return 该商品的单价，如果失败返回null
     */
    BigDecimal decrGoodsStore(Long goodsId, Integer buyNum);

    /**
     * 库存回退
     *
     * @param goodsId 商品id
     * @param buyNum  回退数量
     */
    void incrGoodsStore(Long goodsId, Integer buyNum);

    /**
     * 根据商品id获取商品描述
     *
     * @param goodsId 商品id
     * @return 该商品描述
     */
    String getGoodsDescribeByGoodsId(Long goodsId);

}
