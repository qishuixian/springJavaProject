package com.example.backend.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity   // 标记这个类是一个数据库实体
//指定对应的数据库表名为 books
@Table(name = "books", indexes = {
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_author", columnList = "author"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_category_status", columnList = "category_id, status")
})
// 这个 Book类就是一个“数据容器”，用来存放一本书的信息（编号、书名、作者）。后面你会用 List 装好几个 Book 对象返回给前端。
public class Book {
    @Id   //标记 id字段是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // 主键自增长
    private Long id;

    @Column(nullable = false)   // 字段不能为空
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)  // ManyToOne:多对一关系（多本书属于一个分类）,FetchType.LAZY:懒加载，查询 Book 时不立即加载 Category，需要时才查（性能优化）
    @JoinColumn(name = "category_id")   // 指定外键字段名为 category_id
    private Category category;

    // 封面图片路径
    @Column
    private String coverImage;

    // 创建时间
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 更新时间
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 保存前自动填充：创建时间和更新时间都设为当前时间
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // 更新前自动填充：只更新 updatedAt
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 无参构造方法（Spring 反序列化时需要）
    public Book() {
    }

    // 构造方法    全参构造方法  方便在初始化时一次性设置所有字段
    public  Book(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;

    }
    // 全参构造方法
    public Book(Long id, String title, String author, String status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
    }

    // Getter 和 Setter 方法
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
