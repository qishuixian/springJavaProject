package com.example.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 配置类
 * 配置 API 文档的基本信息和 JWT 认证方式
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // 定义 JWT 认证方案名称
        String securitySchemeName = "BearerAuth";

        return new OpenAPI()
                // API 文档基本信息
                .info(new Info()
                        .title("图书管理系统 API")
                        .description("提供用户认证、图书增删改查、分类管理、文件上传等接口")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("开发者")
                                .email("dev@example.com")))
                // 全局安全要求：所有接口默认需要 Bearer Token
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                // 定义安全方案：Bearer JWT
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("输入 JWT Token（不需要加 Bearer 前缀）")));
    }
}
