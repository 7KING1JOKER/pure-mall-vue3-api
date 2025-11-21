package com.puremall.config;

/**
 * JWT配置类
 * 管理JWT相关的配置参数，如密钥和过期时间
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getSecret() {
        return secret;
    }

    public Long getExpiration() {
        return expiration;
    }
}