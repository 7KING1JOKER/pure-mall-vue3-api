package com.puremall.controller;

/**
 * 鉴权控制器
 * 提供 access token 无感刷新接口（亮点3）
 */

import com.puremall.response.Response;
import com.puremall.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "鉴权", description = "Token 刷新接口")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/refresh")
    @Operation(summary = "刷新 access token", description = "依据 refresh token 换发新的 access token 与 refresh token")
    public ResponseEntity<Response<Map<String, Object>>> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        // refresh token 缺失 / 无效 / 过期 -> 401，前端拦截器据此登出（死循环防护）
        if (refreshToken == null
                || !jwtUtils.validateToken(refreshToken)
                || !"refresh".equals(jwtUtils.getTokenType(refreshToken))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.fail(401, "refresh token 无效或已过期"));
        }

        Long userId = jwtUtils.getUserIdFromToken(refreshToken);
        String username = jwtUtils.getUsernameFromToken(refreshToken);

        // 续期：下发新的 access token 与 refresh token（refresh token 滚动续期）
        Map<String, Object> data = new HashMap<>();
        data.put("token", jwtUtils.generateToken(userId, username));
        data.put("refreshToken", jwtUtils.generateRefreshToken(userId, username));

        return ResponseEntity.ok(Response.success(data));
    }
}
