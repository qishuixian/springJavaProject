
//声明这个类属于 service包
package com.example.backend.service;
import com.example.backend.dto.*;
// 引入 Book 实体类
import com.example.backend.model.Book;
import com.example.backend.model.Category;
//引入 BookRepository 接口
import com.example.backend.repository.BookRepository;
import com.example.backend.repository.CategoryRepository;
//引入依赖注入注解
import org.springframework.beans.factory.annotation.Autowired;
//引入 Service 注解
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
//引入 List 集合
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

// 标记这是一个业务服务类，Spring 会自动扫描并管理它的生命周期
@Service
public class BookService {
    //自动注入BookRepository 实例，不用手动 new
    @Autowired
    private BookRepository bookRepository;   // 声明一个 BookRepository 类型的成员变量

    @Autowired
    private CategoryRepository categoryRepository;
    // 查询所有书籍
    public List<BookResponse> findAll() {
        // 1. 从数据库查出所有 Book 实体
        List<Book> books = bookRepository.findAll(); // 查询所有 Book 实体，然后用 stream().map()逐个转换成 BookResponse
        // 2. 把每个 Book 实体转换成 BookResponse
        return books.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 根据 ID 查询
    //查询单个实体，用 map()转换成 BookResponse，找不到返回 null
    public BookResponse findById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        // 如果找到了，转换成 Response；否则返回 null
        return book.map(this::convertToResponse).orElse(null);
    }

    // 新增书籍
    //把 BookRequest 转成 Book 实体，保存到数据库，再把保存后的实体转成 BookResponse 返回
    public BookResponse save(BookRequest request) {
        // 1. 把请求对象转换成实体对象
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        // 如果前端没传 status，默认设为 "可借阅"
        book.setStatus(request.getStatus() != null ? request.getStatus() : "可借阅");
        // 设置分类
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            book.setCategory(category);
        }

        // 2. 保存到数据库
        Book saved = bookRepository.save(book);

        // 3. 把保存后的实体转换成响应对象返回
        return convertToResponse(saved);
    }

    // 更新书籍
    //先查找，找到后更新字段，再保存，最后转成 BookResponse 返回
    public BookResponse update(Long id, BookRequest request) {
        // 1. 查找要更新的书籍
        Optional<Book> optional = bookRepository.findById(id);
        if (optional.isPresent()) {
            Book existing = optional.get();
            // 2. 更新字段
            existing.setTitle(request.getTitle());
            existing.setAuthor(request.getAuthor());
            existing.setStatus(request.getStatus());

            // 更新分类
            if (request.getCategoryId() != null) {
                Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
                existing.setCategory(category);
            }

            // 3. 保存到数据库
            Book saved = bookRepository.save(existing);
            // 4. 转换成响应对象返回
            return convertToResponse(saved);
        }
        return null;
    }

    // 删除书籍
    //直接删除，不需要转换
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    // 按分类查询
    public List<BookResponse> findByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 按书名模糊搜索
    public List<BookResponse> searchByTitle(String keyword) {
        return bookRepository.searchByTitle(keyword)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 按作者查询
    public List<BookResponse> findByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 按分类名称和状态联合查询
    public List<BookResponse> findByCategoryNameAndStatus(String categoryName, String status) {
        return bookRepository.findByCategoryNameAndStatus(categoryName, status)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 查询所有已借出的书籍（原生 SQL）
    public List<BookResponse> findBorrowedBooks() {
        return bookRepository.findBorrowedBooksNative()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 按书名升序
    public List<BookResponse> findAllByOrderBIdAsc() {
        return bookRepository.findAllByOrderBIdAsc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 分页查询所有书籍（支持排序）
    public PageResponse<BookResponse> findAllWithPage(int page, int size, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Book> bookPage = bookRepository.findAll(pageable);
        return convertToPageResponse(bookPage);
    }

    // 按状态分页查询
    public PageResponse<BookResponse> findByStatusWithPage(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByStatus(status, pageable);
        return convertToPageResponse(bookPage);
    }

    // 按分类 ID 分页查询
    public PageResponse<BookResponse> findByCategoryWithPage(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByCategoryId(categoryId, pageable);
        return convertToPageResponse(bookPage);
    }

    // 按书名模糊搜索分页
    public PageResponse<BookResponse> searchByTitleWithPage(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.searchByTitleWithPage(keyword, pageable);
        return convertToPageResponse(bookPage);
    }

    // 将 Page<Book> 转换为 PageResponse<BookResponse>
    private PageResponse<BookResponse> convertToPageResponse(Page<Book> bookPage) {
        List<BookResponse> content = bookPage.getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(
                content,
                bookPage.getNumber(),      // 当前页码
                bookPage.getSize(),        // 每页大小
                bookPage.getTotalElements(), // 总记录数
                bookPage.getTotalPages()   // 总页数
        );
    }
    // 查询书名和作者
    public List<Map<String, Object>> findTitlesAndAuthorsOnly() {
        List<Object[]> results = bookRepository.findTitlesAndAuthorsOnly();
        return results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("title", row[0]);
            map.put("author", row[1]);
            return map;
        }).collect(Collectors.toList());
    }
    // 查询书名和作者（投影方式）
    public List<BookTitleAuthorProjection> findTitlesAndAuthorsProjection() {
        return bookRepository.findTitlesAndAuthorsProjection();
    }

    // 查询书名和作者（DTO 方式）
    public List<BookSimpleDTO> findTitlesAndAuthorsAsDTO() {
        return bookRepository.findTitlesAndAuthorsAsDTO();
    }
    // 带条件的部分字段查询
    public List<BookTitleAuthorProjection> findTitlesAndAuthorsByCategory(String categoryName) {
        return bookRepository.findTitlesAndAuthorsByCategory(categoryName);
    }
    // 实体转 Response 的私有方法（核心转换逻辑）核心转换方法，把 Book 实体的字段赋值给 BookResponse
    private BookResponse convertToResponse(Book book) {
        String categoryName = (book.getCategory() != null) ? book.getCategory().getName() : null;
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getStatus(),
                categoryName
        );
    }
    // 统计所有书籍总数
    public Long countAllBooks() {
        return bookRepository.countAllBooks();
    }

    // 按状态统计书籍数量
    public List<BookStatsDTO> countBooksByStatus() {
        List<Object[]> results = bookRepository.countBooksByStatus();
        return results.stream()
                .map(row -> new BookStatsDTO((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }

    // 按分类统计书籍数量
    public List<BookStatsDTO> countBooksByCategory() {
        List<Object[]> results = bookRepository.countBooksByCategory();
        return results.stream()
                .map(row -> new BookStatsDTO((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }

    // 统计某分类下的书籍数量
    public Long countBooksByCategoryId(Long categoryId) {
        return bookRepository.countBooksByCategoryId(categoryId);
    }

    // 统计某作者的书籍数量
    public Long countBooksByAuthor(String author) {
        return bookRepository.countBooksByAuthor(author);
    }

    // 统计已借出书籍数量
    public Long countBorrowedBooks() {
        return bookRepository.countByStatus("已借出");
    }

}