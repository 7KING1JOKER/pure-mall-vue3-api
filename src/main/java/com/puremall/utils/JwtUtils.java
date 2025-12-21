package com.puremall.utils;

/**
 * JWT工具类
 * 用于生成、解析和验证JWT令牌
 */

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.puremall.config.JwtConfig;

import java.util.Date;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtils {

    @Autowired
    private JwtConfig jwtConfig;

    // 生成JWT令牌
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtConfig.getExpiration());
        
        // 创建密钥对象
        SecretKeySpec secretKeySpec = new SecretKeySpec(jwtConfig.getSecret().getBytes(), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(secretKeySpec)
                .compact();
    }

    // 从JWT令牌中获取用户名
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    // 从JWT令牌中获取用户ID
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    // 验证JWT令牌是否有效
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expirationDate = claims.getExpiration();
            return expirationDate.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 解析JWT令牌，获取Claims
    private Claims getClaimsFromToken(String token) {
        // 创建密钥对象
        SecretKeySpec secretKeySpec = new SecretKeySpec(jwtConfig.getSecret().getBytes(), SignatureAlgorithm.HS256.getJcaName());
        
        return Jwts.parserBuilder()
                .setSigningKey(secretKeySpec)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}