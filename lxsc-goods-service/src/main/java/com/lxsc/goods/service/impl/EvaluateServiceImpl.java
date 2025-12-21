package com.lxsc.goods.service.impl;

import com.lxsc.commons.PageBean;
import com.lxsc.goods.mapper.EvaluateMapper;
import com.lxsc.goods.model.Evaluate;
import com.lxsc.goods.service.EvaluateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Resource
    private EvaluateMapper evaluateMapper;

    /**
     * 获取商品评价
     *
     * @param goodsId       商品id
     * @param pageNo        页码
     * @param pageSize      单页大小
     * @param evaluateLevel 评价等级。A：好评，B：中评，C：差评，img：有图的评论
     * @return 用pageBean类封装的商品评价数据
     */
    @Override
    public PageBean<List<Evaluate>> getEvaluateListPage(Long goodsId, Long pageNo, Long pageSize, String evaluateLevel) {
        // 创建PageBean对象，利用构造器设置页码和单页大小
        PageBean<List<Evaluate>> pageBean = new PageBean<>(pageNo, pageSize);
        List<Evaluate> evaluateList;
        // 如果要查有图评论
        if (evaluateLevel.equals("img")) {
            // 查询商品有图评价总数量
            long totalNum = evaluateMapper.countEvaluateHaveImgByGoodsId(goodsId);
            // 用set方法设置pageBean对象的totalNum字段的值，同时会设置总页码的值（具体看set方法的实现）
            pageBean.setTotalNum(totalNum);
            // 查询要的数据
            evaluateList = evaluateMapper.selectEvaluateHaveImgListPage(goodsId, pageBean.getSkipNum(), pageBean.getPageSize());
            // 将查到的数据放入pageBean
            pageBean.setData(evaluateList);
        } else {
            // 查询商品总评价数量
            long totalNum = evaluateMapper.countEvaluateByGoodsId(goodsId, evaluateLevel);
            // 用set方法设置pageBean对象的totalNum字段的值，同时会设置总页码的值（具体看set方法的实现）
            pageBean.setTotalNum(totalNum);
            // 查询要的数据
            evaluateList = evaluateMapper.selectEvaluateListPage(goodsId, pageBean.getSkipNum(), pageBean.getPageSize(), evaluateLevel);
            // 将查到的数据放入pageBean
            pageBean.setData(evaluateList);
        }
        // 返回
        return pageBean;
    }

    /**
     * 获取商品的评价数量
     *
     * @param goodsId 商品id
     * @return 一个map集合，包含各个评价等级的数量
     */
    @Override
    public Map<String, Integer> countEvaluateNum(Long goodsId) {
        return evaluateMapper.countEvaluateNum(goodsId);
    }

}
