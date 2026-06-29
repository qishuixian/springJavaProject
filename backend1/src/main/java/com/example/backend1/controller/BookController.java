package com.example.backend1.controller;

import com.example.backend1.model.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class BookController {

    // 静态列表，模拟数据库
    private List<Book> books = new ArrayList<>();

    public BookController() {
        books.add(new Book(1L, "Spring Boot 实战", "张三", "可借阅"));
        books.add(new Book(2L, "Java 核心技术", "李四", "已借出"));
        books.add(new Book(3L, "微服务架构设计", "王五", "可借阅"));
    }

    // GET 请求：获取所有书籍
    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return books;
    }


    // POST 请求：添加一本新书
    @PostMapping("/addbooks")
    public Book addBook(@RequestBody Book newBook) {
        // 为新书生成一个 ID
        newBook.setId((long) (books.size() + 1));

        if (newBook.getStatus() == null) {
            newBook.setStatus("可借阅");  // 默认状态
        }

        // 添加到列表
        books.add(newBook);

        // 返回添加后的新书（包含生成的 ID）
        return newBook;
    }

    // PUT 请求：更新一本书的信息
    @RequestMapping(value = "/books/{id}", method = RequestMethod.PUT)
    public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        // 遍历列表，找到对应 ID 的书并更新
        for (Book book : books) {
            if (book.getId().equals(id)) {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                return book;
            }
        }
        // 如果没找到，返回 null（实际项目会抛出异常）
        return null;
    }
}
