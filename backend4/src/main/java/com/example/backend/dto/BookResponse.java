package com.example.backend.dto;

public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String status;
    private Long categoryId;    // 分类ID
    private String categoryName;  // 分类名称

    // 无参构造（必须）
    public BookResponse() {}

    // 全参构造（方便转换）
    public BookResponse(Long id, String title, String author, String status, Long categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}