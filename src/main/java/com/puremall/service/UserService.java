package com.puremall.service;

/**
 * 用户服务接口
 * 提供用户相关的业务逻辑操作
 */

import com.puremall.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface UserService extends IService<User> {
    User register(User user);
    User login(User user);
    User getUserInfo(Long userId);
    User updateUserInfo(Long userId, User user);
    void logout(Long userId);
    Map<String, Object> changePassword(Long userId, String oldPassword, String newPassword);
    Map<String, String> uploadAvatar(Long userId, MultipartFile file);
    boolean checkUsernameAvailability(String username);
    boolean checkEmailAvailability(String email);
    boolean checkPhoneAvailability(String phone);
}