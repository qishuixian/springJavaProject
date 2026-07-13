package com.example.backend.config;

import com.example.backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
// 标记为配置类
@Configuration
//启用 Spring Security 的 Web 安全支持
@EnableWebSecurity
// 启用方法安全（@PreAuthorize 等注解）,允许我们在 Controller 的方法上使用 @PreAuthorize、@PostAuthorize等注解进行细粒度权限控制
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;  // 注入 JWT 过滤器

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
                // ★ 新增：启用 CORS 支持，并指定配置来源
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                .userDetailsService(customUserDetailsService)
                // ★ 在 UsernamePasswordAuthenticationFilter 之前添加 JWT 过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    // ★ 新增：定义 CORS 规则
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许来自任何源的请求（开发阶段使用 *，生产环境建议指定具体域名）
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // 允许的 HTTP 方法（必须包含 OPTIONS，因为浏览器预检请求会发 OPTIONS）
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许的请求头
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 允许携带凭证（如 Cookie，虽然 JWT 不常用，但保持兼容）
        configuration.setAllowCredentials(true);
        // 预检请求的有效期（秒），避免频繁发送 OPTIONS
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径应用此 CORS 配置
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}