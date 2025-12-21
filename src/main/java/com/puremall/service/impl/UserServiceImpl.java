package com.puremall.service.impl;

/**
 * 用户服务实现类
 * 实现用户相关的业务逻辑操作
 */

import com.puremall.entity.User;
import com.puremall.mapper.UserMapper;
import com.puremall.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puremall.exception.BusinessException;
import com.puremall.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        if(userMapper.findByUsername(user.getUsername()) == null) {
            throw new BusinessException("用户名未注册");
        }

        // 根据用户名查询用户
        User existingUser = userMapper.findByUsername(user.getUsername());

        System.out.println("existingUser: " + existingUser.getPassword());
        System.out.println("user: " + user.getPassword());

        // 验证密码
        if (!PasswordUtils.matchesPassword(user.getPassword(), existingUser.getPassword())) {
            throw new BusinessException("密码错误");
        }
        // 隐藏密码
        existingUser.setPassword(null);
        return existingUser;
    }

    @Override
    public User getUserInfo(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 隐藏密码
        user.setPassword(null);
        return user;
    }

    @Override
    public User updateUserInfo(String username, User user) {
        User existingUser = userMapper.findByUsername(username);
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }
        // 更新用户信息
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getSex() != null) {
            existingUser.setSex(user.getSex());
        }
        if (user.getBirthday() != null) {
            existingUser.setBirthday(user.getBirthday());
        }
        
        userMapper.updateById(existingUser);
        // 隐藏密码
        existingUser.setPassword(null);
        return existingUser;
    }

    @Override
    public void logout(String username) {
        // JWT令牌过期处理由前端负责，后端无需特殊处理
        
    }
    
    @Override
    public Map<String, Object> changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 验证旧密码
        if (!PasswordUtils.matchesPassword(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        // 加密新密码并更新
        user.setPassword(PasswordUtils.encodePassword(newPassword));
        userMapper.updateById(user);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "密码修改成功");
        return result;
    }
    
    @Override
    public Map<String, String> uploadAvatar(Long userId, MultipartFile file) {
        // 简单的文件保存逻辑，实际项目中应考虑文件大小、类型限制、保存路径等
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        
        // 生成文件名，添加空指针检查
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new BusinessException("无效的文件名或文件格式");
        }
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String filename = UUID.randomUUID().toString() + fileExtension;
        
        // 这里应该有实际的文件保存逻辑，暂时返回一个模拟的URL
        String avatarUrl = "/uploads/avatars/" + filename;
        
        // 更新用户头像
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setAvatar(avatarUrl);
        userMapper.updateById(user);
        
        Map<String, String> result = new HashMap<>();
        result.put("avatarUrl", avatarUrl);
        result.put("message", "头像上传成功");
        return result;
    }
    
    @Override
    public Long getUserIdByUsername(String username) {
        return userMapper.getUserIdByUsername(username);
    }

    @Override
    public boolean checkUsernameAvailability(String username) {
        return userMapper.findByUsername(username) == null;
    }
    
    @Override
    public boolean checkEmailAvailability(String email) {
        return userMapper.findByEmail(email) == null;
    }
    
    @Override
    public boolean checkPhoneAvailability(String phone) {
        return userMapper.findByPhone(phone) == null;
    }
}