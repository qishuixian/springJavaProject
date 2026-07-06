package com.example.backend.dto;

public class BookRequest {
    private String title;
    private String author;
    private String status;
    private  Long  categoryId;
     // 注意:请求对象中没有 id字段，因为新增时 ID 由数据库自动生成，前端不需要传。
    // Getter 和 Setter
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