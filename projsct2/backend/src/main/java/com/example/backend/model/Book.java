package com.example.backend.model;
import jakarta.persistence.*;

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
}
