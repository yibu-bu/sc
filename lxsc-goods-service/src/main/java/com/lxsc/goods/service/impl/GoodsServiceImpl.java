package com.lxsc.goods.service.impl;

import com.lxsc.goods.mapper.GoodsInfoMapper;
import com.lxsc.goods.service.GoodsService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsInfoMapper goodsInfoMapper;

    @Override
    @GlobalTransactional  // 开启全局事务
    public BigDecimal decrGoodsStore(Long goodsId, Integer buyNum) {
        System.out.println("商品id：" + goodsId);
        int rows = goodsInfoMapper.decrGoodsStore(goodsId, buyNum);
        // 如果受响应行数为0，表示没有成功
        if (rows == 0) {
            return null;
        }
        // 如果成功就返回该商品的单价
        return goodsInfoMapper.selectPriceByGoodsId(goodsId);
    }

    @Override
    public void incrGoodsStore(Long goodsId, Integer buyNum) {
        goodsInfoMapper.incrGoodsStore(goodsId, buyNum);
    }

    @Override
    public String getGoodsDescribeByGoodsId(Long goodsId) {
        return goodsInfoMapper.selectGoodsDescribeByGoodsId(goodsId);
    }

}
