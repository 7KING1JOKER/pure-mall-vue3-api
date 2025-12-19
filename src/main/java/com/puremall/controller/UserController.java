package com.puremall.controller;

/**
 * 用户管理控制器
 * 处理用户的登录、注册、信息管理等操作
 */

import com.puremall.entity.User;
import com.puremall.service.UserService;
import com.puremall.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户登录、注册、信息管理接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserId")
    @Operation(summary = "根据用户名获取用户ID")
    public Response<Map<String, Long>> getUserIdByUsername(@RequestParam String username) {
        Long userId = userService.getUserIdByUsername(username);
        Map<String, Long> response = new HashMap<>();
        response.put("userId", userId);
        return Response.success(response);
    }
    
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Response<User> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        return Response.success(registeredUser);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Response<User> login(@RequestBody User user) {
        User loggedInUser = userService.login(user);
        return Response.success(loggedInUser);
    }

    @GetMapping("/getInfo")
    @Operation(summary = "获取用户信息")
    public Response<User> getUserInfo(@RequestParam String username) {
        User user = userService.getUserInfo(username);
        return Response.success(user);
    }

    @PutMapping("/updateInfo")
    @Operation(summary = "更新用户信息")
    public Response<User> updateUserInfo(@RequestParam String username, @RequestBody User user) {
        User updatedUser = userService.updateUserInfo(username, user);
        return Response.success(updatedUser);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public Response<Void> logout(@RequestParam String username) {
        userService.logout(username);
        return Response.success(null);
    }
    
    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Response<Map<String, Object>> changePassword(
            @RequestParam Long userId,
            @RequestBody Map<String, String> passwordData) {
        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");
        return Response.success(userService.changePassword(userId, oldPassword, newPassword));
    }
    
    @PostMapping("/avatar")
    @Operation(summary = "上传用户头像")
    public Response<Map<String, String>> uploadAvatar(
            @RequestParam Long userId,
            @RequestParam("file") MultipartFile file) {
        Map<String, String> result = userService.uploadAvatar(userId, file);
        return Response.success(result);
    }
    
    @PostMapping("/check-username")
    @Operation(summary = "检查用户名是否可用")
    public Response<Map<String, Boolean>> checkUsername(@RequestBody Map<String, String> usernameData) {
        String username = usernameData.get("username");
        boolean isAvailable = userService.checkUsernameAvailability(username);
        Map<String, Boolean> result = new HashMap<>();
        result.put("available", isAvailable);
        return Response.success(result);
    }
    
    @PostMapping("/check-email")
    @Operation(summary = "检查邮箱是否可用")
    public Response<Map<String, Boolean>> checkEmail(@RequestBody Map<String, String> emailData) {
        String email = emailData.get("email");
        boolean isAvailable = userService.checkEmailAvailability(email);
        Map<String, Boolean> result = new HashMap<>();
        result.put("available", isAvailable);
        return Response.success(result);
    }
    
    @PostMapping("/check-phone")
    @Operation(summary = "检查手机号是否可用")
    public Response<Map<String, Boolean>> checkPhone(@RequestBody Map<String, String> phoneData) {
        String phone = phoneData.get("phone");
        boolean isAvailable = userService.checkPhoneAvailability(phone);
        Map<String, Boolean> result = new HashMap<>();
        result.put("available", isAvailable);
        return Response.success(result);
    }
}