package com.example.backend.config;

import com.example.backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// 标记为配置类
@Configuration
//启用 Spring Security 的 Web 安全支持
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    //密码编码器，用于加密和验证密码
    @Bean
    public PasswordEncoder passwordEncoder() {
        //Spring Security 推荐的密码哈希算法
        return new BCryptPasswordEncoder();
    }

    // 认证管理器（用于后续登录接口）
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    //定义安全过滤链，配置请求授权规则
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF（前后端分离项目不需要）
                .csrf(csrf -> csrf.disable())

                // 设置会话管理为无状态（使用 JWT，不需要 Session）
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        // 1. 放行注册和登录接口
                        .requestMatchers("/api/auth/**").permitAll()
                        // 2. 【新增】放行书籍相关的业务接口
//                        .requestMatchers("/api/books/**").permitAll()
                        // 3. 其他所有请求都需要认证
                        .anyRequest().authenticated()
                )

                // 设置自定义 UserDetailsService
                .userDetailsService(customUserDetailsService);

        return http.build();
    }
}