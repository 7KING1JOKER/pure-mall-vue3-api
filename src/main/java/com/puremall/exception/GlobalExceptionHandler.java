package com.puremall.exception;

import com.puremall.entity.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Response<?> handleBusinessException(BusinessException e) {
        return Response.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<?> handleException(Exception e) {
        e.printStackTrace();
        return Response.error(500, "服务器内部错误");
    }
}