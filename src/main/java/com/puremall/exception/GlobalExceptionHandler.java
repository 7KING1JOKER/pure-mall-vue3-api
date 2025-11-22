package com.puremall.exception;

/**
 * 全局异常处理器
 * 用于统一处理系统中的各种异常
 */

import com.puremall.response.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Response<?> handleBusinessException(BusinessException e) {
        return Response.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<?> handleException(Exception e) {
        e.printStackTrace();
        return Response.fail(500, "服务器内部错误");
    }
}