
// 声明这个接口属于 repository包
package com.example.backend.repository;
// 引入 Book 实体类
import com.example.backend.dto.BookSimpleDTO;
import com.example.backend.dto.BookTitleAuthorProjection;
import com.example.backend.model.Book;
// 引入 JPA 提供的核心接口
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
// 引入 Spring 的 Repository 注解
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 标记这是一个数据访问层组件，Spring 会自动扫描并管理它
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    //继承 JPA 接口，泛型第一个参数是实体类（Book），第二个是主键类型（Long）
    // 根据分类 ID 查询所有书籍  findByCategoryId→ 翻译成 WHERE category_id = ?
    List<Book> findByCategoryId(Long categoryId);
    // WHERE title = ?
    // List<Book> findByTitle(String title);

    //WHERE author LIKE %?%
    // List<Book> findByAuthorContaining(String keyword);

    // WHERE status = ? AND category_id = ?
    // List<Book> findByStatusAndCategoryId(String status, Long categoryId);


    //关联查询：WHERE category.name = ?
    // List<Book> findByCategory_Name(String categoryName);

    // 2. JPQL：根据书名模糊查询（使用 LIKE）
    // @Query("...")  指定要执行的 JPQL 或 SQL 语句
    // :keyword     命名参数，用 @Param("keyword")绑定方法参数
    //%:keyword%   LIKE 模糊查询，%是通配符
    // JOIN b.category c    JPQL 中的关联查询，b.category直接导航到关联实体
    // nativeQuery = true  表示使用原生 SQL 而不是 JPQL
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword%")
    List<Book> searchByTitle(@Param("keyword") String keyword);

    // 3. JPQL：根据作者精确查询
    @Query("SELECT b FROM Book b WHERE b.author = :author")
    List<Book> findByAuthor(@Param("author") String author);

    // 4. JPQL：根据分类名称和状态联合查询
    @Query("SELECT b FROM Book b JOIN b.category c WHERE c.name = :categoryName AND b.status = :status")
    List<Book> findByCategoryNameAndStatus(@Param("categoryName") String categoryName, @Param("status") String status);

    // 5. 原生 SQL 查询：查询所有已借出的书籍（使用 nativeQuery = true）
    @Query(value = "SELECT * FROM books WHERE status = '已借出'", nativeQuery = true)
    List<Book> findBorrowedBooksNative();

    // 6. JPQL：查询某个分类下的所有书籍（通过分类 ID）
    @Query("SELECT b FROM Book b WHERE b.category.id = :categoryId")
    List<Book> findByCategoryIdWithJPQL(@Param("categoryId") Long categoryId);
   // 6.1 排序
   @Query("SELECT b FROM Book b ORDER BY b.id ASC")
   List<Book> findAllByOrderBIdAsc();
    // 6.2.1. 分页查询所有书籍
    Page<Book> findAll(Pageable pageable);

    // 6.2.2. 按状态分页查询
    Page<Book> findByStatus(String status, Pageable pageable);

    // 6.2.3. 按分类 ID 分页查询
    Page<Book> findByCategoryId(Long categoryId, Pageable pageable);

    // 6.2.4. JPQL 分页查询（按书名模糊搜索）
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword%")
    Page<Book> searchByTitleWithPage(@Param("keyword") String keyword, Pageable pageable);
    // 6.3.1 只查询部分字段   只查询书名和作者，返回 Object[] 数组
    // 优点:简单，无需额外类    缺点：类型不安全，需要手动转换
    @Query("SELECT b.title, b.author FROM Book b")
    List<Object[]> findTitlesAndAuthorsOnly();
    // 6.3.2使用接口投影，只查询书名和作者
    // 优点:类型安全，无需额外类（接口即可）    缺点：只能通过 Getter 方法映射
    @Query("SELECT b.title AS title, b.author AS author FROM Book b")
    List<BookTitleAuthorProjection> findTitlesAndAuthorsProjection();

    // 6.3.3使用 JPQL NEW 关键字直接构造 DTO
    // 优点:最灵活，可以包含计算字段    缺点：需要额外创建 DTO 类
    @Query("SELECT new com.example.backend.dto.BookSimpleDTO(b.title, b.author) FROM Book b")
    List<BookSimpleDTO> findTitlesAndAuthorsAsDTO();
   // 6.3.4 带条件的部分字段查询
   @Query("SELECT b.title AS title, b.author AS author FROM Book b WHERE b.category.name = :categoryName")
   List<BookTitleAuthorProjection> findTitlesAndAuthorsByCategory(@Param("categoryName") String categoryName);
     //  6.4 聚合查询
    // 6.4.1. 统计所有书籍总数
    @Query("SELECT COUNT(b) FROM Book b")
    Long countAllBooks();

    // 6.4.2. 按状态统计书籍数量（返回 Object[]，第一个是状态，第二个是数量）
    @Query("SELECT b.status, COUNT(b) FROM Book b GROUP BY b.status")
    List<Object[]> countBooksByStatus();

    // 6.4.3. 按分类名称统计书籍数量（使用 JOIN）
    @Query("SELECT c.name, COUNT(b) FROM Book b RIGHT JOIN b.category c GROUP BY c.name")
    List<Object[]> countBooksByCategory();

    // 6.4.4. 统计某分类下的书籍数量
    @Query("SELECT COUNT(b) FROM Book b WHERE b.category.id = :categoryId")
    Long countBooksByCategoryId(@Param("categoryId") Long categoryId);

    // 6.4.5. 统计某作者的书籍数量
    @Query("SELECT COUNT(b) FROM Book b WHERE b.author = :author")
    Long countBooksByAuthor(@Param("author") String author);

    // 6.4.6. 统计所有已借出书籍的数量
    Long countByStatus(String status);
}