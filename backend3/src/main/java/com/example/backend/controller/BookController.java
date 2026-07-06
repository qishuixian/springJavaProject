package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.exception.BusinessException;
import com.example.backend.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookService bookService;

    // GET /api/books?categoryId=1（不加 categoryId 则返回全部）
    @GetMapping("/books")
    public Result<List<BookResponse>> getAllBooks(
            @RequestParam(required = false) Long categoryId) {
        // 	RequestParam  从 URL 查询参数中取值（如 ?categoryId=1）
        List<BookResponse> books;
        if (categoryId != null) {
            books = bookService.findByCategory(categoryId);
        } else {
            books = bookService.findAll();
        }
        return Result.success(books);
    }

    // GET /api/books/{id} - 根据 ID 查询
    @GetMapping("/books/{id}")
    public Result<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse book = bookService.findById(id);
        if (book == null) {
            throw new BusinessException(404, "书籍不存在，ID: " + id);
        }
        return Result.success(book);
    }


    // POST /api/books - 新增
    @PostMapping("/books")
    public Result<BookResponse> addBook(@Validated(CreateGroup.class) @RequestBody BookRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException(400, "书名不能为空");
        }
        BookResponse saved = bookService.save(request);
        return Result.success(saved);
    }

    // PUT /api/books/{id} - 更新
    @PutMapping("/books/{id}")
    public Result<BookResponse> updateBook(@PathVariable Long id, @Validated(UpdateGroup.class) @RequestBody BookRequest request) {
        BookResponse updated = bookService.update(id, request);
        if (updated == null) {
            throw new BusinessException(404, "书籍不存在，ID: " + id);
        }
        return Result.success(updated);
    }

    // DELETE /api/books/{id} - 删除
    @DeleteMapping("/books/{id}")
    public Result<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/books/category")
    public Result<List<BookResponse>> getBooksByCategory(@RequestParam Long categoryId) {
        List<BookResponse> books = bookService.findByCategory(categoryId);
        return Result.success(books);
    }

    // GET /api/books/search?keyword=Spring
    @GetMapping("/books/search")
    public Result<List<BookResponse>> searchBooks(@RequestParam String keyword) {
        List<BookResponse> books = bookService.searchByTitle(keyword);
        return Result.success(books);
    }

    // GET /api/books/author?author=张三
    @GetMapping("/books/author")
    public Result<List<BookResponse>> getBooksByAuthor(@RequestParam String author) {
        List<BookResponse> books = bookService.findByAuthor(author);
        return Result.success(books);
    }

    // GET /api/books/filter?categoryName=计算机&status=可借阅
    @GetMapping("/books/filter")
    public Result<List<BookResponse>> filterBooks(
            @RequestParam String categoryName,
            @RequestParam String status) {
        List<BookResponse> books = bookService.findByCategoryNameAndStatus(categoryName, status);
        return Result.success(books);
    }

    // GET /api/books/borrowed
    @GetMapping("/books/borrowed")
    public Result<List<BookResponse>> getBorrowedBooks() {
        List<BookResponse> books = bookService.findBorrowedBooks();
        return Result.success(books);
    }

    // GET /api/books/sorted/id-asc
    @GetMapping("/books/sorted/id-asc")
    public Result<List<BookResponse>> getAllBooksSortedByTitleAsc() {
        return Result.success(bookService.findAllByOrderBIdAsc());
    }

    // GET /api/books/page?page=0&size=5&sortField=title&sortDirection=asc
    @GetMapping("/books/page")
    public Result<PageResponse<BookResponse>> getAllBooksWithPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        PageResponse<BookResponse> pageResult = bookService.findAllWithPage(page, size, sortField, sortDirection);
        return Result.success(pageResult);
    }

    // GET /api/books/page/status?status=可借阅&page=0&size=3
    @GetMapping("/books/page/status")
    public Result<PageResponse<BookResponse>> getBooksByStatusWithPage(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PageResponse<BookResponse> pageResult = bookService.findByStatusWithPage(status, page, size);
        return Result.success(pageResult);
    }

    // GET /api/books/page/category?categoryId=1&page=0&size=3
    @GetMapping("/books/page/category")
    public Result<PageResponse<BookResponse>> getBooksByCategoryWithPage(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PageResponse<BookResponse> pageResult = bookService.findByCategoryWithPage(categoryId, page, size);
        return Result.success(pageResult);
    }

    // GET /api/books/page/search?keyword=Spring&page=0&size=3
    @GetMapping("/books/page/search")
    public Result<PageResponse<BookResponse>> searchBooksWithPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PageResponse<BookResponse> pageResult = bookService.searchByTitleWithPage(keyword, page, size);
        return Result.success(pageResult);
    }

    // GET /api/books/titles-authors
    @GetMapping("/books/titles-authors")
    public Result<List<Map<String, Object>>> getTitlesAndAuthors() {
        return Result.success(bookService.findTitlesAndAuthorsOnly());
    }

    // GET /api/books/projection
    @GetMapping("/books/projection")
    public Result<List<BookTitleAuthorProjection>> getTitlesAndAuthorsProjection() {
        return Result.success(bookService.findTitlesAndAuthorsProjection());
    }

    // GET /api/books/simple
    @GetMapping("/books/simple")
    public Result<List<BookSimpleDTO>> getSimpleBooks() {
        return Result.success(bookService.findTitlesAndAuthorsAsDTO());
    }
    @GetMapping("/books/projection/by-category")
    public Result<List<BookTitleAuthorProjection>> getTitlesAndAuthorsByCategory(@RequestParam String categoryName) {
        return Result.success(bookService.findTitlesAndAuthorsByCategory(categoryName));
    }
    // GET /api/books/stats/count-all
    @GetMapping("/books/stats/count-all")
    public Result<Long> countAllBooks() {
        return Result.success(bookService.countAllBooks());
    }

    // GET /api/books/stats/by-status
    @GetMapping("/books/stats/by-status")
    public Result<List<BookStatsDTO>> countBooksByStatus() {
        return Result.success(bookService.countBooksByStatus());
    }

    // GET /api/books/stats/by-category
    @GetMapping("/books/stats/by-category")
    public Result<List<BookStatsDTO>> countBooksByCategory() {
        return Result.success(bookService.countBooksByCategory());
    }

    // GET /api/books/stats/by-category-id?categoryId=1
    @GetMapping("/books/stats/by-category-id")
    public Result<Long> countBooksByCategoryId(@RequestParam Long categoryId) {
        return Result.success(bookService.countBooksByCategoryId(categoryId));
    }

    // GET /api/books/stats/by-author?author=张三
    @GetMapping("/books/stats/by-author")
    public Result<Long> countBooksByAuthor(@RequestParam String author) {
        return Result.success(bookService.countBooksByAuthor(author));
    }

    // GET /api/books/stats/borrowed
    @GetMapping("/books/stats/borrowed")
    public Result<Long> countBorrowedBooks() {
        return Result.success(bookService.countBorrowedBooks());
    }

}