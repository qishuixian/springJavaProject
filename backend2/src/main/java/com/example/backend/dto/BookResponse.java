package com.example.backend.dto;

public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String status;

    // 无参构造（必须）
    public BookResponse() {}

    // 全参构造（方便转换）
    public BookResponse(Long id, String title, String author, String status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
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
}