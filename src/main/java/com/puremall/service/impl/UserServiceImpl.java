package com.puremall.service.impl;

import com.puremall.entity.User;
import com.puremall.mapper.UserMapper;
import com.puremall.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puremall.exception.BusinessException;
import com.puremall.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;



    @Override
    public User register(User user) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        // 检查邮箱是否已存在
        if (userMapper.findByEmail(user.getEmail()) != null) {
            throw new BusinessException("邮箱已存在");
        }
        // 检查手机号是否已存在
        if (userMapper.findByPhone(user.getPhone()) != null) {
            throw new BusinessException("手机号已存在");
        }
        // 加密密码
        user.setPassword(PasswordUtils.encodePassword(user.getPassword()));
        // 保存用户
        userMapper.insert(user);
        // 隐藏密码
        user.setPassword(null);
        return user;
    }

    @Override
    public User login(User user) {
        // 根据用户名查询用户
        User existingUser = userMapper.findByUsername(user.getUsername());
        if (existingUser == null) {
            throw new BusinessException("用户名或密码错误");
        }
        // 验证密码
        if (!PasswordUtils.matchesPassword(user.getPassword(), existingUser.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        // 隐藏密码
        existingUser.setPassword(null);
        return existingUser;
    }

    @Override
    public User getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 隐藏密码
        user.setPassword(null);
        return user;
    }

    @Override
    public User updateUserInfo(Long userId, User user) {
        User existingUser = userMapper.selectById(userId);
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }
        // 更新用户信息
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setAvatar(user.getAvatar());
        userMapper.updateById(existingUser);
        // 隐藏密码
        existingUser.setPassword(null);
        return existingUser;
    }

    @Override
    public void logout(Long userId) {
        // JWT令牌过期处理由前端负责，后端无需特殊处理
    }
}