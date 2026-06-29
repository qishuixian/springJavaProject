
// 声明这个接口属于 repository包
package com.example.backend.repository;
// 引入 Book 实体类
import com.example.backend.model.Book;
// 引入 JPA 提供的核心接口
import org.springframework.data.jpa.repository.JpaRepository;
// 引入 Spring 的 Repository 注解
import org.springframework.stereotype.Repository;
// 标记这是一个数据访问层组件，Spring 会自动扫描并管理它
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    //继承 JPA 接口，泛型第一个参数是实体类（Book），第二个是主键类型（Long）

}