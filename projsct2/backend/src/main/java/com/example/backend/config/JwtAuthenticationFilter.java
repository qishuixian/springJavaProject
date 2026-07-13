package com.example.backend.config;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component  // 告诉 Spring：这是一个组件，请管理它
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;  // 注入 JWT 工具类

    @Autowired
    private UserRepository userRepository;  // 注入用户 Repository

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 从请求头中获取 Authorization 字段
        String authHeader = request.getHeader("Authorization");

        // 2. 检查 Authorization 是否存在且以 "Bearer " 开头
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 没有 Token，直接放行（后续会被 SecurityConfig 拦截）
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 提取 Token（去掉 "Bearer " 前缀）
        String token = authHeader.substring(7);

        // 4. 从 Token 中提取用户名
        String username = jwtUtil.getUsernameFromToken(token);

        // 5. 检查用户名不为空，且当前还没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. 验证 Token 是否有效
            if (jwtUtil.validateToken(token)) {

                // 7. 从数据库查询用户信息
                User user = userRepository.findByUsername(username).orElse(null);

                if (user != null) {
                    // 8. 创建认证令牌（包含用户信息和权限）
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,           // 用户对象
                                    null,           // 凭证（密码），这里不需要
                                    user.getAuthorities()  // 权限列表（角色）
                            );

                    // 9. 设置请求详情（IP、Session ID 等）
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 10. 将认证信息设置到安全上下文中
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // 11. 放行请求，继续执行后续的过滤器或 Controller
        filterChain.doFilter(request, response);
    }
}