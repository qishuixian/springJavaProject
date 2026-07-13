package com.example.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component  // 告诉 Spring：这是一个组件，请管理它的生命周期
public class JwtUtil {

    // @Value 从 application.yml 中读取配置
    @Value("${jwt.secret}")      // 读取 jwt.secret 的值
    private String secret;  // 存储签名密钥。

    @Value("${jwt.expiration}")  // 读取 jwt.expiration 的值
    private Long expiration;  // 存储 Token 过期时间
    // 把字符串密钥转换成 JWT 需要的密钥对象
    private SecretKey getSigningKey() {
        // secret.getBytes(StandardCharsets.UTF_8) 把字符串转成字节数组
        // Keys.hmacShaKeyFor(...)：用字节数组生成 HMAC-SHA 算法的密钥对象
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    // 生成 Token
    public String generateToken(String username, String role) {
        // 1. 创建一个 Map，存放要放入 Token 的自定义数据
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);  // 把角色信息放进去

        // 2. 构建 Token
        return Jwts.builder()
                .claims(claims)                 // 放入自定义数据（角色）
                .subject(username)              // 设置主题（用户名）
                .issuedAt(new Date())           // 设置签发时间（现在）
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 设置过期时间（现在 + 24小时）
                .signWith(getSigningKey())      // 用密钥签名（防篡改）
                .compact();                     // 生成最终的 Token 字符串
    }
    // 从 Token 中解析出所有数据
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())    // 用同样的密钥验证签名
                .build()                         // 构建解析器
                .parseSignedClaims(token)        // 解析 Token
                .getPayload();                   // 取出 Payload 中的数据
    }
    // 从 Token 中提取用户名
    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }
    // 从 Token 中提取角色
    public String getRoleFromToken(String token) {
        return parseToken(token).get("role", String.class);
    }
    // 验证 Token 是否有效（检查是否过期）
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);          // 尝试解析 Token
            return !claims.getExpiration().before(new Date()); // 检查是否过期
        } catch (Exception e) {
            return false; // 解析失败（签名错误、格式错误、已过期等），视为无效
        }
    }

}