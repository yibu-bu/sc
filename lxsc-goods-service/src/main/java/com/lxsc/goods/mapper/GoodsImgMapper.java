package com.lxsc.goods.mapper;

import com.lxsc.goods.model.GoodsImg;

public interface GoodsImgMapper {

    int deleteByPrimaryKey(Long id);

    int insert(GoodsImg record);

    int insertSelective(GoodsImg record);

    GoodsImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsImg record);

    int updateByPrimaryKey(GoodsImg record);

}