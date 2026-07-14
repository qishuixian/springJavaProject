package com.example.backend.controller;

import com.example.backend.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传控制器
 * 负责接收前端上传的图片文件，保存到服务器指定目录，并返回可访问的 URL
 */
@RestController
@RequestMapping("/api/files")
@Tag(name = "文件管理", description = "文件上传接口")
public class FileController {

    // 从配置文件中读取上传目录路径
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 上传封面图片
     * @param file 前端上传的文件（表单字段名为 file）
     * @return 返回可访问的图片 URL
     */
    @Operation(summary = "上传封面图片", description = "仅管理员可操作，支持 jpg/png，最大 5MB")
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        // 1. 校验文件是否为空
        if (file.isEmpty()) {
            return Result.error(400, "上传文件不能为空");
        }

        // 2. 校验文件类型（只允许图片）
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error(400, "只允许上传图片文件");
        }

        // 3. 生成唯一文件名，防止重名覆盖
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFilename = UUID.randomUUID().toString() + extension;

        // 4. 确保上传目录存在
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 5. 保存文件到磁盘
        try {
            File dest = new File(dir, newFilename);
            file.transferTo(dest);
        } catch (IOException e) {
            return Result.error(500, "文件上传失败：" + e.getMessage());
        }

        // 6. 返回可访问的 URL 路径
        String url = "/uploads/covers/" + newFilename;
        return Result.success(url);
    }
}
