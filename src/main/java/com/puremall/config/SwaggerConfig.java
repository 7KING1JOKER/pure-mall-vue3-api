package com.puremall.config;

/**
 * Swagger配置类
 * 配置API文档生成的相关参数和信息
 */

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI pureMallOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Pure Mall API")
                        .description("Pure Mall 电商平台API文档")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Pure Mall Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }
    // Swagger UI默认路径: http://localhost:8080/swagger-ui.html
}