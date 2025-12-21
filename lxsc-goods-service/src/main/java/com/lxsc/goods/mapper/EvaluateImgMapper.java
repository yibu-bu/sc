package com.lxsc.goods.mapper;

import com.lxsc.goods.model.EvaluateImg;

public interface EvaluateImgMapper {

    int deleteByPrimaryKey(Long id);

    int insert(EvaluateImg record);

    int insertSelective(EvaluateImg record);

    EvaluateImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EvaluateImg record);

    int updateByPrimaryKey(EvaluateImg record);

}