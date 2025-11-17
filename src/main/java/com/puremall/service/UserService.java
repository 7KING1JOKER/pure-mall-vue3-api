package com.puremall.service;

import com.puremall.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    User register(User user);
    User login(User user);
    User getUserInfo(Long userId);
    User updateUserInfo(Long userId, User user);
    void logout(Long userId);
}