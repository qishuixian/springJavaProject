package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.exception.BusinessException;
import com.example.backend.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "图书管理", description = "图书的增删改查、分页、搜索等接口")
public class BookController {

    @Autowired
    private BookService bookService;

    // GET /api/books?categoryId=1（不加 categoryId 则返回全部）
    @Operation(summary = "获取所有图书", description = "可选按分类ID筛选")
    @GetMapping("/books")
    //拥有 ROLE_USER或 ROLE_ADMIN的用户都能访问
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    @Operation(summary = "根据ID查询图书")
    @GetMapping("/books/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse book = bookService.findById(id);
        if (book == null) {
            throw new BusinessException(404, "书籍不存在，ID: " + id);
        }
        return Result.success(book);
    }


    // POST /api/books - 新增（仅管理员）
    @Operation(summary = "新增图书", description = "仅管理员可操作")
    @PostMapping("/books")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<BookResponse> addBook(@Validated(CreateGroup.class) @RequestBody BookRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException(400, "书名不能为空");
        }
        BookResponse saved = bookService.save(request);
        return Result.success(saved);
    }

    // PUT /api/books/{id} - 更新（仅管理员）
    @Operation(summary = "更新图书", description = "仅管理员可操作")
    @PutMapping("/books/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<BookResponse> updateBook(@PathVariable Long id, @Validated(UpdateGroup.class) @RequestBody BookRequest request) {
        BookResponse updated = bookService.update(id, request);
        if (updated == null) {
            throw new BusinessException(404, "书籍不存在，ID: " + id);
        }
        return Result.success(updated);
    }

    // DELETE /api/books/{id} - 删除（仅管理员）
    @Operation(summary = "删除图书", description = "仅管理员可操作")
    @DeleteMapping("/books/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/books/category")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<BookResponse>> getBooksByCategory(@RequestParam Long categoryId) {
        List<BookResponse> books = bookService.findByCategory(categoryId);
        return Result.success(books);
    }

    // GET /api/books/search?keyword=Spring
    @GetMapping("/books/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<BookResponse>> searchBooks(@RequestParam String keyword) {
        List<BookResponse> books = bookService.searchByTitle(keyword);
        return Result.success(books);
    }

    // GET /api/books/author?author=张三
    @GetMapping("/books/author")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<BookResponse>> getBooksByAuthor(@RequestParam String author) {
        List<BookResponse> books = bookService.findByAuthor(author);
        return Result.success(books);
    }

    // GET /api/books/filter?categoryName=计算机&status=可借阅
    @GetMapping("/books/filter")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<BookResponse>> filterBooks(
            @RequestParam String categoryName,
            @RequestParam String status) {
        List<BookResponse> books = bookService.findByCategoryNameAndStatus(categoryName, status);
        return Result.success(books);
    }

    // GET /api/books/borrowed
    @GetMapping("/books/borrowed")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<BookResponse>> getBorrowedBooks() {
        List<BookResponse> books = bookService.findBorrowedBooks();
        return Result.success(books);
    }

    // GET /api/books/sorted/id-asc
    @GetMapping("/books/sorted/id-asc")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<BookResponse>> getAllBooksSortedByTitleAsc() {
        return Result.success(bookService.findAllByOrderBIdAsc());
    }

    // GET /api/books/page?page=0&size=5&sortField=updatedAt&sortDirection=desc
    // 默认按更新时间倒序排列（最新的在最前面）
    @Operation(summary = "分页查询图书", description = "默认按更新时间倒序")
    @GetMapping("/books/page")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<PageResponse<BookResponse>> getAllBooksWithPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "updatedAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PageResponse<BookResponse> pageResult = bookService.findAllWithPage(page, size, sortField, sortDirection);
        return Result.success(pageResult);
    }

    // GET /api/books/page/status?status=可借阅&page=0&size=3
    @GetMapping("/books/page/status")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<PageResponse<BookResponse>> getBooksByStatusWithPage(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PageResponse<BookResponse> pageResult = bookService.findByStatusWithPage(status, page, size);
        return Result.success(pageResult);
    }

    // GET /api/books/page/category?categoryId=1&page=0&size=3
    @GetMapping("/books/page/category")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<PageResponse<BookResponse>> getBooksByCategoryWithPage(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PageResponse<BookResponse> pageResult = bookService.findByCategoryWithPage(categoryId, page, size);
        return Result.success(pageResult);
    }

    // GET /api/books/page/search?keyword=Spring&page=0&size=3
    @Operation(summary = "分页搜索图书", description = "按书名关键字搜索")
    @GetMapping("/books/page/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<PageResponse<BookResponse>> searchBooksWithPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PageResponse<BookResponse> pageResult = bookService.searchByTitleWithPage(keyword, page, size);
        return Result.success(pageResult);
    }

    // GET /api/books/titles-authors
    @GetMapping("/books/titles-authors")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<Map<String, Object>>> getTitlesAndAuthors() {
        return Result.success(bookService.findTitlesAndAuthorsOnly());
    }

    // GET /api/books/projection
    @GetMapping("/books/projection")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<BookTitleAuthorProjection>> getTitlesAndAuthorsProjection() {
        return Result.success(bookService.findTitlesAndAuthorsProjection());
    }

    // GET /api/books/simple
    @GetMapping("/books/simple")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<BookSimpleDTO>> getSimpleBooks() {
        return Result.success(bookService.findTitlesAndAuthorsAsDTO());
    }
    @GetMapping("/books/projection/by-category")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<BookTitleAuthorProjection>> getTitlesAndAuthorsByCategory(@RequestParam String categoryName) {
        return Result.success(bookService.findTitlesAndAuthorsByCategory(categoryName));
    }
    // GET /api/books/stats/count-all
    @GetMapping("/books/stats/count-all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<Long> countAllBooks() {
        return Result.success(bookService.countAllBooks());
    }

    // GET /api/books/stats/by-status
    @GetMapping("/books/stats/by-status")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<BookStatsDTO>> countBooksByStatus() {
        return Result.success(bookService.countBooksByStatus());
    }

    // GET /api/books/stats/by-category
    @GetMapping("/books/stats/by-category")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<List<BookStatsDTO>> countBooksByCategory() {
        return Result.success(bookService.countBooksByCategory());
    }

    // GET /api/books/stats/by-category-id?categoryId=1
    @GetMapping("/books/stats/by-category-id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<Long> countBooksByCategoryId(@RequestParam Long categoryId) {
        return Result.success(bookService.countBooksByCategoryId(categoryId));
    }

    // GET /api/books/stats/by-author?author=张三
    @GetMapping("/books/stats/by-author")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<Long> countBooksByAuthor(@RequestParam String author) {
        return Result.success(bookService.countBooksByAuthor(author));
    }

    // GET /api/books/stats/borrowed
    @GetMapping("/books/stats/borrowed")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<Long> countBorrowedBooks() {
        return Result.success(bookService.countBorrowedBooks());
    }

}