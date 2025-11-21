package com.puremall.utils;

/**
 * 密码工具类
 * 用于密码的加密和验证
 */

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // 密码加密
    public static String encodePassword(String password) {
        return encoder.encode(password);
    }

    // 密码验证
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}