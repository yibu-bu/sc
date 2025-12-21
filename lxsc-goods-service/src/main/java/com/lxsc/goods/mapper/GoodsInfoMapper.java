package com.lxsc.goods.mapper;

import com.lxsc.goods.model.GoodsInfo;

import java.math.BigDecimal;

public interface GoodsInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(GoodsInfo record);

    int insertSelective(GoodsInfo record);

    GoodsInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsInfo record);

    int updateByPrimaryKey(GoodsInfo record);

    /**
     * 减商品库存
     *
     * @param goodsId 商品id
     * @param buyNum  购买数量
     * @return 商品单价
     */
    int decrGoodsStore(Long goodsId, Integer buyNum);

    /**
     * 查询商品单价
     *
     * @param goodsId 商品id
     * @return 商品单价
     */
    BigDecimal selectPriceByGoodsId(Long goodsId);

    String selectGoodsDescribeByGoodsId(Long goodsId);

    void incrGoodsStore(Long goodsId, Integer buyNum);

}