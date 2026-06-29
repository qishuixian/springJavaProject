package com.example.helloworld.controller;

import com.example.helloworld.model.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    private List<Book> books = new ArrayList<>();

    public BookController() {
        books.add(new Book(1L, "Spring Boot 实战", "张三", "可借阅"));
        books.add(new Book(2L, "Java 核心技术", "李四", "已借出"));
        books.add(new Book(3L, "微服务架构设计", "王五", "可借阅"));
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return books;
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book newBook) {
        newBook.setId((long) (books.size() + 1));
        books.add(newBook);
        return newBook;
    }

    @PutMapping("/books/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                return book;
            }
        }
        return null;
    }
}