package com.example.backend.controller;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.Result;
import com.example.backend.exception.BusinessException;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtil;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户注册、登录、获取当前用户信息")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // 注册接口
    @Operation(summary = "用户注册", description = "注册普通用户，返回 Token")
    @PostMapping("/register")
    public Result<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        // 1. 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 2. 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 加密密码
        user.setRole(Role.USER); // 默认角色为普通用户

        // 3. 保存到数据库
        userRepository.save(user);

        // 4. 生成 Token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        // 5. 返回响应
        AuthResponse response = new AuthResponse(token, user.getUsername(), user.getRole().name());
        return Result.success(response);
    }

    // 登录接口
    @Operation(summary = "用户登录", description = "用户名密码登录，返回 JWT Token")
    @PostMapping("/login")
    public Result<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            // 1. 使用 AuthenticationManager 验证用户名和密码
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // 2. 认证成功，获取用户信息
            User user = (User) authentication.getPrincipal();

            // 3. 生成 Token
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

            // 4. 返回响应
            AuthResponse response = new AuthResponse(token, user.getUsername(), user.getRole().name());
            return Result.success(response);

        } catch (Exception e) {
            // 认证失败（用户名或密码错误）
            throw new BusinessException(401, "用户名或密码错误");
        }
    }
    // 测试用：注册管理员（生产环境不应开放）
    @Operation(summary = "注册管理员", description = "测试用，注册管理员角色账号")
    @PostMapping("/register-admin")
    public Result<AuthResponse> registerAdmin(@Valid @RequestBody AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(400, "用户名已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);  // 设置为管理员
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        AuthResponse response = new AuthResponse(token, user.getUsername(), user.getRole().name());
        return Result.success(response);
    }

    // 获取当前登录用户信息
    @Operation(summary = "获取当前用户信息", description = "需要 Token 认证")
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<AuthResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        AuthResponse response = new AuthResponse(null, user.getUsername(), user.getRole().name());
        return Result.success(response);
    }
}