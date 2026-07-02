
//声明这个类属于 service包
package com.example.backend.service;
import com.example.backend.dto.BookRequest;
import com.example.backend.dto.BookResponse;
// 引入 Book 实体类
import com.example.backend.model.Book;
//引入 BookRepository 接口
import com.example.backend.repository.BookRepository;
//引入依赖注入注解
import org.springframework.beans.factory.annotation.Autowired;
//引入 Service 注解
import org.springframework.stereotype.Service;
//引入 List 集合
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// 标记这是一个业务服务类，Spring 会自动扫描并管理它的生命周期
@Service
public class BookService {
    //自动注入BookRepository 实例，不用手动 new
    @Autowired
    private BookRepository bookRepository;   // 声明一个 BookRepository 类型的成员变量
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
    public BookResponse findById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        // 如果找到了，转换成 Response；否则返回 null
        return book.map(this::convertToResponse).orElse(null);
    }

    // 新增书籍
    public BookResponse save(BookRequest request) {
        // 1. 把请求对象转换成实体对象
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        // 如果前端没传 status，默认设为 "可借阅"
        book.setStatus(request.getStatus() != null ? request.getStatus() : "可借阅");

        // 2. 保存到数据库
        Book saved = bookRepository.save(book);

        // 3. 把保存后的实体转换成响应对象返回
        return convertToResponse(saved);
    }

    // 更新书籍
    public BookResponse update(Long id, BookRequest request) {
        // 1. 查找要更新的书籍
        Optional<Book> optional = bookRepository.findById(id);
        if (optional.isPresent()) {
            Book existing = optional.get();
            // 2. 更新字段
            existing.setTitle(request.getTitle());
            existing.setAuthor(request.getAuthor());
            existing.setStatus(request.getStatus());
            // 3. 保存到数据库
            Book saved = bookRepository.save(existing);
            // 4. 转换成响应对象返回
            return convertToResponse(saved);
        }
        return null;
    }

    // 删除书籍
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    // 实体转 Response 的私有方法（核心转换逻辑）
    private BookResponse convertToResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getStatus()
        );
    }
}