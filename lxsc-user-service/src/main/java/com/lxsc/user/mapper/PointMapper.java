package com.lxsc.user.mapper;

import com.lxsc.user.model.Point;

public interface PointMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Point record);

    int insertSelective(Point record);

    Point selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Point record);

    int updateByPrimaryKey(Point record);

}