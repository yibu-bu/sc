package com.lxsc.user.mapper;

import com.lxsc.user.model.User;

public interface UserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    /**
     * 选择性插入，将传入的对象中各个字段做判断，如果是null就跳过这个字段
     *
     * @param record 要插入的实体类对象
     * @return 受响应行数
     */
    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 通过手机号查询用户
     *
     * @param phone 用户的手机号
     * @return 用户的实体类
     */
    User selectByPhone(String phone);

}