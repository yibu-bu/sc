package com.lxsc.user.mapper;

import com.lxsc.user.model.Address;

import java.util.List;

public interface AddressMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

    /**
     * 根据用户id查找其地址信息
     *
     * @param userId 用户id
     * @return 所有的地址组成的List集合
     */
    List<Address> selectByUserId(Long userId);

}