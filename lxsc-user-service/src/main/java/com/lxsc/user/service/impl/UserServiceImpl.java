package com.lxsc.user.service.impl;

import com.lxsc.user.mapper.AddressMapper;
import com.lxsc.user.mapper.UserMapper;
import com.lxsc.user.model.Address;
import com.lxsc.user.model.User;
import com.lxsc.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AddressMapper addressMapper;

    /**
     * 用户登录功能
     *
     * @param user 用户相关信息
     * @return 返回0表示登录成功，返回1表示用户名错误，返回2表示密码错误
     */
    @Override
    public int login(User user) {
        User dbUser = userMapper.selectByPhone(user.getPhone());
        if (dbUser == null) {
            return 1;
        }
        if (!user.getPassword().equals(dbUser.getPassword())) {
            return 2;
        }
        // 把dbUser拷贝给user，虽是值传递，但是user仍然指向函数调用处的那个对象，是可以修改那个对象的值的。
        BeanUtils.copyProperties(dbUser, user);
        return 0;
    }

    /**
     * 用户注册功能
     *
     * @param user 用户提交的注册信息
     * @return 返回0表示注册成功，返回1表示手机号为空，返回2表示密码为空，返回3表示手机号已存在
     */
    @Override
    public int reg(User user) {
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            return 1;
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return 2;
        }
        try {
            // 这里可能抛出DuplicateKeyException异常，表示违反了数据库的唯一性索引，说明该手机号已经用过了
            userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            return 3;
        }
        return 0;
    }

    @Override
    public List<Address> getUserAddress(Long userId) {
        return addressMapper.selectByUserId(userId);
    }

}
