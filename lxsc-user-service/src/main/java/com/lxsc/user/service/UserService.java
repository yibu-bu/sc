package com.lxsc.user.service;

import com.lxsc.user.model.Address;
import com.lxsc.user.model.User;

import java.util.List;

public interface UserService {

    /**
     * 用户登录
     *
     * @param user 用户相关信息
     * @return 不同的数字表示不同的登录结果
     */
    int login(User user);

    /**
     * 用户注册
     *
     * @param user 用户提交的注册信息
     * @return 不同的数字表示不同的注册结果
     */
    int reg(User user);

    /**
     * 获取用户所有地址，且默认地址是第一个
     *
     * @param userId 用户id
     * @return 该用户所有的地址组成的List集合
     */
    List<Address> getUserAddress(Long userId);

}
