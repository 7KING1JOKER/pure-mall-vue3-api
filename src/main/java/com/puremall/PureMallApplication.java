package com.puremall;

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