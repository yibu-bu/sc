package com.lxsc.goods.mapper;

import com.lxsc.goods.model.Evaluate;

import java.util.List;
import java.util.Map;

public interface EvaluateMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Evaluate record);

    int insertSelective(Evaluate record);

    Evaluate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Evaluate record);

    int updateByPrimaryKeyWithBLOBs(Evaluate record);

    int updateByPrimaryKey(Evaluate record);

    /**
     * 通过商品主键查询该商品的评价数量
     *
     * @param goodsId       商品id
     * @param evaluateLevel 商品评价等级
     * @return 该商品的评价数量
     */
    long countEvaluateByGoodsId(Long goodsId, String evaluateLevel);

    /**
     * 通过商品主键查询该商品有图评价的数量
     *
     * @param goodsId 商品id
     * @return 该商品的有图评价数量
     */
    long countEvaluateHaveImgByGoodsId(Long goodsId);

    /**
     * 分页查询商品的评价
     *
     * @param goodsId       要查询的商品主键
     * @param skipNum       跳过的数据
     * @param pageSize      单页大小
     * @param evaluateLevel 要查询的评价等级
     * @return 单页数据
     */
    List<Evaluate> selectEvaluateListPage(Long goodsId, Long skipNum, Long pageSize, String evaluateLevel);

    /**
     * 分页查询有图商品的评价
     *
     * @param goodsId  商品id
     * @param skipNum  跳过的数据
     * @param pageSize 单页大小
     * @return 单页数据
     */
    List<Evaluate> selectEvaluateHaveImgListPage(Long goodsId, Long skipNum, Long pageSize);

    /**
     * 获取商品的评价数量
     *
     * @param goodsId 商品id
     * @return 一个map集合，包含各个评价等级的数量
     */
    Map<String, Integer> countEvaluateNum(Long goodsId);

}