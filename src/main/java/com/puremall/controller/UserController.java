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

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户登录、注册、信息管理接口")
public class UserController {

    @Autowired
    private UserService userService;

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

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public Response<User> getUserInfo(@RequestParam Long userId) {
        User user = userService.getUserInfo(userId);
        return Response.success(user);
    }

    @PutMapping("/info")
    @Operation(summary = "更新用户信息")
    public Response<User> updateUserInfo(@RequestParam Long userId, @RequestBody User user) {
        User updatedUser = userService.updateUserInfo(userId, user);
        return Response.success(updatedUser);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public Response<Void> logout(@RequestParam Long userId) {
        userService.logout(userId);
        return Response.success(null);
    }
}