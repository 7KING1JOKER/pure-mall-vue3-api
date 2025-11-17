package com.puremall.entity;

import java.io.Serializable;

public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;

    public Response() {
    }

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应
    public static <T> Response<T> success(T data) {
        return new Response<>(200, "success", data);
    }

    // 成功响应，无数据
    public static <T> Response<T> success() {
        return new Response<>(200, "success", null);
    }

    // 失败响应
    public static <T> Response<T> error(int code, String message) {
        return new Response<>(code, message, null);
    }

    // 失败响应，默认错误码
    public static <T> Response<T> error(String message) {
        return new Response<>(500, message, null);
    }

    // getter and setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}