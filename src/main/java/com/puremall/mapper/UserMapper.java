package com.puremall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.User;

public interface UserMapper extends BaseMapper<User> {
    // 自定义查询方法
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
}