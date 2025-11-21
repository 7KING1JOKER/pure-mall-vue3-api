package com.puremall;

/**
 * 应用程序入口类
 * 启动Spring Boot应用并配置MyBatis扫描路径
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.puremall.mapper")
public class PureMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(PureMallApplication.class, args);
    }
}