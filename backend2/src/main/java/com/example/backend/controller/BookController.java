package com.example.backend.controller;

import com.example.backend.dto.BookRequest;
import com.example.backend.dto.BookResponse;
import com.example.backend.dto.Result;
import com.example.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookService bookService;

    // GET /api/books - 查询所有
    @GetMapping("/books")
    public Result<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.findAll();
        return Result.success(books);
    }

    // GET /api/books/{id} - 根据 ID 查询
    @GetMapping("/books/{id}")
    public Result<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse book = bookService.findById(id);
        if (book == null) {
            return Result.error(404, "书籍不存在");
        }
        return Result.success(book);
    }

    // POST /api/books - 新增
    @PostMapping("/books")
    public Result<BookResponse> addBook(@RequestBody BookRequest request) {
        BookResponse saved = bookService.save(request);
        return Result.success(saved);
    }

    // PUT /api/books/{id} - 更新
    @PutMapping("/books/{id}")
    public Result<BookResponse> updateBook(@PathVariable Long id, @RequestBody BookRequest request) {
        BookResponse updated = bookService.update(id, request);
        if (updated == null) {
            return Result.error(404, "书籍不存在");
        }
        return Result.success(updated);
    }

    // DELETE /api/books/{id} - 删除
    @DeleteMapping("/books/{id}")
    public Result<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return Result.success();
    }
}