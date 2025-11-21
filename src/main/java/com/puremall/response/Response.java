package com.puremall.response;

/**
 * 统一响应实体类
 * 用于封装API响应结果
 */

import lombok.Data;

@Data
public class Response<T> {
    private int code;
    private String message;
    private T data;

    private Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应
    public static <T> Response<T> success(T data) {
        return new Response<>(200, "success", data);
    }

    // 失败响应
    public static <T> Response<T> fail(int code, String message) {
        return new Response<>(code, message, null);
    }

    // 失败响应（默认错误码）
    public static <T> Response<T> fail(String message) {
        return new Response<>(500, message, null);
    }
}