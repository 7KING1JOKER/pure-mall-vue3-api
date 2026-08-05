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

    // refresh token 有效期（亮点3）：长于 access token，用于过期续期
    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    public String getSecret() {
        return secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public Long getRefreshExpiration() {
        return refreshExpiration;
    }
}