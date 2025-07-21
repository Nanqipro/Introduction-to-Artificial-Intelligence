# 开发者指南 | Developer Guide

## 项目概述 | Project Overview

这是一个基于《人工智能概论与应用》教材的数字化学习平台，采用Vue.js + Spring Boot的前后端分离架构。

This is a digital learning platform based on the "Introduction to Artificial Intelligence and Applications" textbook, using Vue.js + Spring Boot frontend-backend separation architecture.

## 技术栈 | Tech Stack

### 前端 | Frontend
- **Vue.js 3.5.17** - 渐进式JavaScript框架
- **Vite 7.0.4** - 现代化构建工具
- **JavaScript/TypeScript** - 开发语言

### 后端 | Backend
- **Spring Boot 3.5.3** - Java企业级框架
- **Java 17** - 编程语言
- **Maven** - 项目管理工具
- **Lombok** - 代码简化工具

## 项目结构 | Project Structure

```
├── AI-GOODLAB/          # LaTeX教材源码
├── client/              # Vue.js前端
│   ├── src/
│   │   ├── components/  # 组件
│   │   ├── assets/      # 静态资源
│   │   └── App.vue      # 主组件
│   └── package.json     # 依赖配置
└── server/              # Spring Boot后端
    ├── src/main/java/   # Java源码
    └── pom.xml          # Maven配置
```

## 开发环境搭建 | Development Setup

### 环境要求 | Requirements
- Node.js >= 16.0.0
- Java >= 17
- Maven >= 3.6.0

### 安装步骤 | Installation Steps

1. **克隆项目 | Clone Repository**
```bash
git clone <repository-url>
cd Introduction-to-Artificial-Intelligence
```

2. **前端开发 | Frontend Development**
```bash
cd client
npm install          # 安装依赖
npm run dev          # 启动开发服务器
npm run build        # 构建生产版本
npm run preview      # 预览生产版本
```

3. **后端开发 | Backend Development**
```bash
cd server
mvn clean install    # 编译项目
mvn spring-boot:run   # 启动应用
mvn test             # 运行测试
```

## 开发规范 | Development Standards

### 代码风格 | Code Style
- **前端**: 遵循Vue.js官方风格指南
- **后端**: 遵循Spring Boot最佳实践
- **提交**: 使用语义化提交信息

### 分支管理 | Branch Management
- `main`: 主分支，稳定版本
- `develop`: 开发分支
- `feature/*`: 功能分支
- `hotfix/*`: 热修复分支

### 提交规范 | Commit Convention
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 代码重构
test: 测试相关
chore: 构建过程或辅助工具的变动
```

## API设计 | API Design

### RESTful接口规范 | RESTful API Standards
```
GET    /api/chapters          # 获取章节列表
GET    /api/chapters/{id}     # 获取特定章节
POST   /api/chapters          # 创建章节
PUT    /api/chapters/{id}     # 更新章节
DELETE /api/chapters/{id}     # 删除章节
```

### 响应格式 | Response Format
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## 数据库设计 | Database Design

### 主要实体 | Main Entities
- **Chapter**: 章节信息
- **Section**: 小节信息
- **User**: 用户信息
- **Progress**: 学习进度

### 表结构示例 | Table Structure Example
```sql
CREATE TABLE chapters (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    order_num INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## 部署指南 | Deployment Guide

### 开发环境 | Development Environment
```bash
# 前端
cd client && npm run dev

# 后端
cd server && mvn spring-boot:run
```

### 生产环境 | Production Environment
```bash
# 前端构建
cd client && npm run build

# 后端打包
cd server && mvn clean package

# 运行
java -jar server/target/server-0.0.1-SNAPSHOT.jar
```

### Docker部署 | Docker Deployment
```dockerfile
# Dockerfile示例
FROM openjdk:17-jre-slim
COPY target/server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 测试策略 | Testing Strategy

### 前端测试 | Frontend Testing
- 单元测试: Jest + Vue Test Utils
- 端到端测试: Cypress

### 后端测试 | Backend Testing
- 单元测试: JUnit 5
- 集成测试: Spring Boot Test
- API测试: MockMvc

## 性能优化 | Performance Optimization

### 前端优化 | Frontend Optimization
- 代码分割 (Code Splitting)
- 懒加载 (Lazy Loading)
- 缓存策略 (Caching Strategy)
- 图片优化 (Image Optimization)

### 后端优化 | Backend Optimization
- 数据库索引优化
- 缓存机制 (Redis)
- 连接池配置
- JVM参数调优

## 监控与日志 | Monitoring & Logging

### 日志配置 | Logging Configuration
```yaml
logging:
  level:
    com.goodlab: DEBUG
    org.springframework: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
```

### 监控指标 | Monitoring Metrics
- 应用性能监控 (APM)
- 数据库性能监控
- 系统资源监控
- 用户行为分析

## 常见问题 | FAQ

### Q: 如何添加新的章节？
A: 在后端创建对应的Controller和Service，前端添加相应的组件和路由。

### Q: 如何处理跨域问题？
A: 在Spring Boot中配置CORS，或使用代理服务器。

### Q: 如何优化首屏加载速度？
A: 使用代码分割、预加载关键资源、优化图片大小等方式。

## 贡献指南 | Contributing

1. Fork项目
2. 创建功能分支
3. 提交代码
4. 创建Pull Request
5. 代码审查
6. 合并代码

## 联系方式 | Contact

- 技术问题: 提交Issue
- 邮箱: dev@goodlab.com
- 文档: 查看项目Wiki

---

📝 本文档持续更新中，如有疑问请及时反馈。
