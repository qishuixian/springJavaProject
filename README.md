# 🚀 Spring Boot 全栈实战 6 周推进计划

## 📋 项目概述

本项目是一个从零开始的 Spring Boot + Vue3 全栈开发实战计划，适合前端开发者转型全栈或 Java 初学者系统学习。通过 6 周时间，完成一个完整的图书管理系统，涵盖后端 API 开发、数据库设计、用户鉴权、前后端联调到最终部署上线的全流程。

## 🎯 学习目标

- 掌握 Spring Boot 核心概念和最佳实践
- 理解 RESTful API 设计规范
- 掌握 JPA/MyBatis 数据库操作
- 实现 JWT 用户认证与授权
- 完成前后端分离项目联调
- 具备独立开发全栈项目的能力

## 🛠️ 技术栈

### 后端
- Java 21
- Spring Boot 3.x
- Spring Data JPA
- MySQL 8.0
- JWT (JSON Web Token)
- Lombok
- Maven

### 前端
- Vue 3
- Vite
- Axios
- Element Plus / Ant Design Vue

## 📅 6 周详细推进计划

### Week 1：环境搭建 + 第一个 REST API

**目标**：完成开发环境配置，创建第一个 Spring Boot 项目并实现基础 REST API

| Day | 任务 | 产出 |
|-----|------|------|
| Day 1 | 安装 JDK 17 + IntelliJ IDEA Community + Maven | 控制台运行 Hello World |
| Day 2 | 使用 Spring Initializr 生成项目（Spring Web + Lombok） | 项目启动成功，访问 http://localhost:8085 |
| Day 3 | 编写第一个 Controller：`GET /api/books` 返回静态列表 | 接口返回 JSON 数据 |
| Day 4 | 理解核心注解：`@RestController`、`@GetMapping`、`@RequestMapping` | 注解使用文档 |
| Day 5 | 创建 Book 实体类（id, title, author, status） | Book.java 实体类 |
| Day 6 | 配置 MySQL 数据库（application.yml），创建 book 表 | 数据库连接成功 |
| Day 7 | 复习总结 | 博客：[《Spring Boot 第一周：从0到第一个REST API》](https://blog.csdn.net/qishuixian/article/details/162412146) |

**核心知识点**：
- Spring Boot 项目结构
- Maven 依赖管理
- REST API 基础
- MySQL 基础配置

---

### Week 2：完整 CRUD + 分层架构

**目标**：实现标准的三层架构，完成图书管理的增删改查功能

| Day | 任务 | 产出 |
|-----|------|------|
| Day 1 | 创建 BookRepository 接口（继承 JpaRepository） | Repository 层完成 |
| Day 2 | 创建 BookService，实现 findAll() 方法 | Service 层初始化 |
| Day 3 | 实现完整 CRUD（findAll, findById, save, update, delete） | 5 个核心接口 |
| Day 4 | 理解 `@Service`、`@Repository`、依赖注入（DI） | 架构理解文档 |
| Day 5 | 引入 DTO：BookRequest, BookResponse | DTO 层完成 |
| Day 6 | 统一返回格式 `Result<T>`（code, message, data） | 统一响应结构 |
| Day 7 | 复习总结 | 博客：《Spring Boot CRUD 与三层架构实战》 |

**核心知识点**：
- 三层架构（Controller-Service-Repository）
- JPA 基础操作
- DTO 设计模式
- RESTful API 设计规范

---

### Week 3：数据库进阶 + 异常处理

**目标**：掌握数据库关联查询、索引优化和全局异常处理

| Day | 任务 | 产出 |
|-----|------|------|
| Day 1 | 添加 Category 表，Book 与 Category 多对一关联 | 实体关联完成 |
| Day 2 | 实现按分类查询图书接口 | `GET /api/books?categoryId=1` |
| Day 3 | 手写 JPQL / `@Query` 实现复杂查询 | 自定义查询方法 |
| Day 4 | 学习 MySQL 索引，给常用字段添加索引 | 索引优化文档 |
| Day 5 | 全局异常处理 `@ControllerAdvice` | 统一异常处理器 |
| Day 6 | 参数校验 `@Valid` + 自定义校验注解 | 参数校验完成 |
| Day 7 | 复习总结 | 博客：《Spring Boot 数据库关联与异常处理实践》 |

**核心知识点**：
- JPA 关联关系（@ManyToOne, @OneToMany）
- JPQL 查询语言
- MySQL 索引原理
- 全局异常处理
- JSR-303 参数校验

---

### Week 4：JWT 鉴权 + 前端联调准备

**目标**：实现用户注册登录和 JWT 鉴权机制，准备前端项目

| Day | 任务 | 产出 |
|-----|------|------|
| Day 1 | 添加 User 实体 + user 表 | User 实体类 |
| Day 2 | 实现注册接口（BCrypt 密码加密） | `POST /api/auth/register` |
| Day 3 | 实现登录接口，返回 JWT Token | `POST /api/auth/login` |
| Day 4 | 编写 JWT 工具类（生成 + 解析） | JwtUtil.java |
| Day 5 | 编写 JWT 过滤器，保护需要登录的接口 | JwtAuthenticationFilter |
| Day 6 | 前端 Vue3 + Vite 项目初始化，安装 axios | 前端项目结构 |
| Day 7 | 复习总结 | 博客：《Spring Boot + JWT 鉴权实战》 |

**核心知识点**：
- Spring Security 基础
- JWT 工作原理
- BCrypt 密码加密
- Filter 过滤器
- Vue3 项目搭建

---

### Week 5：前后端联调 + 完整功能闭环

**目标**：完成前后端联调，实现完整的用户交互流程

| Day | 任务 | 产出 |
|-----|------|------|
| Day 1 | 前端登录页面 → 调用后端登录接口 | 登录功能完成 |
| Day 2 | 前端图书列表页 → 调用 `GET /api/books` | 列表展示 |
| Day 3 | 前端新增/编辑图书表单 | 表单提交功能 |
| Day 4 | 前端删除图书功能 | 删除确认对话框 |
| Day 5 | 处理 CORS 跨域问题 | 跨域配置完成 |
| Day 6 | 修复联调 bug（字段名、数据类型等） | bug 修复清单 |
| Day 7 | 复习总结 | 博客：《Vue3 + Spring Boot 前后端联调实录》 |

**核心知识点**：
- CORS 跨域解决方案
- Axios 请求封装
- Token 存储与携带
- 前后端接口联调技巧
- 常见 bug 排查

---

### Week 6：打磨 + 部署 + 展示

**目标**：完善项目文档，部署上线，总结输出

| Day | 任务 | 产出 |
|-----|------|------|
| Day 1 | 编写完整 README.md（项目介绍、技术栈、启动方式） | README.md |
| Day 2 | 推送到 GitHub 公开仓库 | GitHub 仓库链接 |
| Day 3 | 部署到云服务器或 Railway/Render.com | 在线访问地址 |
| Day 4 | 编写完整技术博客，记录架构、难点、收获 | 技术博客文章 |
| Day 5 | 整理面试话术：如何用该项目证明全栈能力 | 面试准备文档 |
| Day 6 | 复盘：可优化的地方（分页、搜索、缓存、日志） | 优化清单 |
| Day 7 | 休息 + 庆祝 🎉 | 完成证书 |

**核心知识点**：
- 项目文档编写
- Git 版本管理
- 云服务器部署
- 技术博客写作
- 面试项目介绍技巧

---

## 📦 项目结构

```
book-management-system/
├── backend/                    # Spring Boot 后端
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/book/
│   │   │   │   ├── controller/     # 控制器层
│   │   │   │   ├── service/        # 服务层
│   │   │   │   ├── repository/     # 数据访问层
│   │   │   │   ├── entity/         # 实体类
│   │   │   │   ├── dto/            # 数据传输对象
│   │   │   │   ├── config/         # 配置类
│   │   │   │   ├── filter/         # 过滤器
│   │   │   │   └── util/           # 工具类
│   │   │   └── resources/
│   │   │       └── application.yml # 配置文件
│   │   └── test/                   # 测试代码
│   └── pom.xml                     # Maven 依赖
│
└── frontend/                   # Vue3 前端
    ├── src/
    │   ├── api/                # API 接口封装
    │   ├── views/              # 页面组件
    │   ├── components/         # 公共组件
    │   ├── router/             # 路由配置
    │   ├── store/              # 状态管理
    │   └── utils/              # 工具函数
    ├── package.json            # npm 依赖
    └── vite.config.js          # Vite 配置
```

---

## 🚀 快速开始

### 后端启动

```bash
# 1. 克隆项目
git clone https://github.com/yourusername/book-management-system.git

# 2. 进入后端目录
cd backend

# 3. 配置数据库
# 修改 src/main/resources/application.yml 中的数据库连接信息

# 4. 创建数据库
mysql -u root -p
CREATE DATABASE book_management;

# 5. 启动项目（Maven）
mvn spring-boot:run

# 或在 IDEA 中直接运行 BookManagementApplication
```

后端服务运行在：http://localhost:8080

### 前端启动

```bash
# 1. 进入前端目录
cd frontend

# 2. 安装依赖
npm install

# 3. 启动开发服务器
npm run dev
```

前端服务运行在：http://localhost:5173

---

## 📝 API 接口文档

### 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/register | 用户注册 |
| POST | /api/auth/login | 用户登录 |

### 图书接口

| 方法 | 路径 | 说明 | 是否需要登录 |
|------|------|------|-------------|
| GET | /api/books | 获取图书列表 | ✅ |
| GET | /api/books/{id} | 获取图书详情 | ✅ |
| POST | /api/books | 新增图书 | ✅ |
| PUT | /api/books/{id} | 更新图书 | ✅ |
| DELETE | /api/books/{id} | 删除图书 | ✅ |

---

## 🎓 学习资源

### 官方文档
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring Data JPA 文档](https://spring.io/projects/spring-data-jpa)
- [Vue3 官方文档](https://cn.vuejs.org/)

### 推荐教程
- [Spring Boot 快速入门](https://spring.io/quickstart)
- [RESTful API 设计指南](https://restfulapi.net/)
- [JWT 完全指南](https://jwt.io/introduction)

---

## 🤝 贡献指南

欢迎提交 Issue 或 Pull Request！

---

## 📄 许可证

MIT License

---

## 👨‍💻 作者

[@your-github-username](https://github.com/your-github-username)

---

## 🌟 Star History

如果这个项目对你有帮助，请给一个 ⭐️ 支持一下！

---

**最后更新时间**：2024-06-24
