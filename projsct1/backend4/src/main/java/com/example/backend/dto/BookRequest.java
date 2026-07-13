package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public class BookRequest {
    @Null(groups = CreateGroup.class, message = "新增时ID必须为空")
    @NotNull(groups = UpdateGroup.class, message = "更新时ID不能为空")
    private Long id;
    // @NotBlank：不允许 null、空字符串、纯空格
    @NotBlank(message = "书名不能为空")
    // @Size(min, max)：限制字符串长度
    // message：校验失败时的提示信息，会传递给全局异常处理器
    @Size(min = 1, max = 100, message = "书名长度必须在1-100之间")
    private String title;
    @NotBlank(message = "作者不能为空")
    @Size(min = 1, max = 50, message = "作者长度必须在1-50之间")
    private String author;
    private String status;
    private Long categoryId;
     // 注意:请求对象中没有 id字段，因为新增时 ID 由数据库自动生成，前端不需要传。
    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCategoryId() { return categoryId; }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}