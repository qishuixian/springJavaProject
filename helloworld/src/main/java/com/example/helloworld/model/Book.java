package com.example.helloworld.model;
// 这个 Book类就是一个“数据容器”，用来存放一本书的信息（编号、书名、作者）。后面你会用 List 装好几个 Book 对象返回给前端。
public class Book {
    private Long id;
    private String title;
    private String author;
    private String status;

    // 构造方法
    public  Book(long id, String title, String author,String status) {
        this.id = id;
        this.title = title;
        this.status = status;

    }
    // Getter 方法（用于 Spring 自动转 JSON）
    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }
    public String getStatus() {
        return status;
    }
}
