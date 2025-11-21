package com.puremall.mapper;

/**
 * 用户Mapper接口
 * 用于用户数据的数据库操作
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puremall.entity.User;

public interface UserMapper extends BaseMapper<User> {
    // 自定义查询方法
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
}