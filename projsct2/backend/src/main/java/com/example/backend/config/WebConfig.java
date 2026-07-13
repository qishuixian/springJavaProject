package com.example.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 * 将本地磁盘目录映射为静态资源 URL，使前端可以通过 URL 直接访问上传的图片
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 读取上传目录路径
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /uploads/covers/** 请求映射到本地磁盘目录
        registry.addResourceHandler("/uploads/covers/**")
                .addResourceLocations("file:" + uploadDir);
    }
}
