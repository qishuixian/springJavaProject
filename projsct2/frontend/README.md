# Frontend - Vue3 + Vite + Element Plus

## 项目说明

这是一个基于 Vue 3 + Vite + Element Plus 的前端项目，用于图书管理系统。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 下一代前端构建工具
- **Element Plus** - 基于 Vue 3 的组件库
- **Vue Router** - Vue.js 官方路由管理器
- **Axios** - 基于 Promise 的 HTTP 客户端

## 项目结构

```
frontend/
├── src/
│   ├── api/                # API 接口封装
│   │   ├── index.js        # 业务 API
│   │   └── request.js      # axios 封装
│   ├── views/              # 页面组件
│   │   ├── Login.vue       # 登录/注册页面
│   │   └── Books.vue       # 图书列表页面
│   ├── components/         # 公共组件
│   ├── router/             # 路由配置
│   │   └── index.js        # 路由定义
│   ├── store/              # 状态管理（预留）
│   ├── utils/              # 工具函数
│   │   └── index.js        # 工具方法
│   ├── App.vue             # 根组件
│   ├── main.js             # 入口文件
│   └── style.css           # 全局样式
├── public/                 # 静态资源
├── index.html              # HTML 模板
├── vite.config.js          # Vite 配置
├── package.json            # 项目依赖
└── README.md               # 项目说明
```

## 快速开始

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问地址：http://localhost:5173

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 功能说明

### 已实现功能

1. **用户认证**
   - 用户登录
   - 用户注册
   - JWT Token 管理

2. **图书管理**
   - 图书列表展示
   - 新增图书
   - 编辑图书
   - 删除图书

3. **路由守卫**
   - 自动拦截未登录用户
   - 已登录用户自动跳转

### API 配置

后端 API 地址配置在 `vite.config.js` 中的 proxy 设置：

```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',  // 后端服务地址
    changeOrigin: true
  }
}
```

### 环境要求

- Node.js >= 16.0.0
- npm >= 7.0.0

## 注意事项

1. 确保后端服务已启动（默认端口 8080）
2. Token 存储在 localStorage 中
3. 请求失败时会自动处理 401、403、404、500 等错误
