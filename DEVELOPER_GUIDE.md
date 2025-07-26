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

## ⚡ 快速启动命令 | Quick Start Commands

### 前端启动 | Frontend Startup
```bash
# 进入前端目录 | Enter frontend directory
cd client

# 安装依赖 | Install dependencies
npm install

# 启动开发服务器 | Start development server
npm run dev
# 访问 | Access: http://localhost:5173

# 构建生产版本 | Build for production
npm run build

# 预览生产版本 | Preview production build
npm run preview
```

### 后端启动 | Backend Startup

**方式一：使用 Maven Wrapper（推荐）| Method 1: Using Maven Wrapper (Recommended)**
```bash
# 进入后端目录 | Enter backend directory
cd server

# 启动应用 | Start application
./mvnw spring-boot:run
# 访问 | Access: http://localhost:8080

# 清理并编译 | Clean and compile
./mvnw clean compile

# 运行测试 | Run tests
./mvnw test

# 打包应用 | Package application
./mvnw clean package
```

**方式二：使用本地 Maven | Method 2: Using Local Maven**
```bash
# 进入后端目录 | Enter backend directory
cd server

# 启动应用 | Start application
mvn spring-boot:run

# 清理并编译 | Clean and compile
mvn clean compile

# 打包应用 | Package application
mvn clean package
```

**方式三：运行打包后的应用 | Method 3: Run Packaged Application**
```bash
# 打包 | Package
./mvnw clean package

# 运行 JAR 文件 | Run JAR file
java -jar target/server-0.0.1-SNAPSHOT.jar

# 指定端口运行 | Run with specific port
java -jar target/server-0.0.1-SNAPSHOT.jar --server.port=8081

# 指定配置文件 | Run with specific profile
java -jar target/server-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 同时启动前后端 | Start Both Frontend and Backend

**终端1 | Terminal 1:**
```bash
cd server
./mvnw spring-boot:run
```

**终端2 | Terminal 2:**
```bash
cd client
npm run dev
```

然后访问 | Then visit: http://localhost:5173

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

## 🐛 故障排除 | Troubleshooting

### 常见问题 | Common Issues

#### 前端问题 | Frontend Issues

**问题：npm install 失败**
```bash
# 清除缓存 | Clear cache
npm cache clean --force

# 删除 node_modules 重新安装 | Remove node_modules and reinstall
rm -rf node_modules package-lock.json
npm install

# 使用淘宝镜像 | Use Taobao registry
npm install --registry https://registry.npmmirror.com
```

**问题：端口被占用**
```bash
# 查看端口占用 | Check port usage
netstat -tulpn | grep :5173

# 杀死占用进程 | Kill process
kill -9 <PID>

# 或者修改端口 | Or change port in vite.config.js
export default {
  server: {
    port: 3000
  }
}
```

#### 后端问题 | Backend Issues

**问题：JAVA_HOME 未设置**
```bash
# 检查 Java 版本 | Check Java version
java -version

# 设置 JAVA_HOME | Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
echo $JAVA_HOME

# 永久设置 | Permanent setting (add to ~/.bashrc)
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk' >> ~/.bashrc
source ~/.bashrc
```

**问题：Maven 编译失败**
```bash
# 清理项目 | Clean project
./mvnw clean

# 跳过测试编译 | Compile skipping tests
./mvnw compile -DskipTests

# 强制更新依赖 | Force update dependencies
./mvnw clean compile -U
```

**问题：端口 8080 被占用**
```bash
# 查看端口占用 | Check port usage
netstat -tulpn | grep :8080

# 修改端口 | Change port in application.properties
server.port=8081

# 或者启动时指定端口 | Or specify port at startup
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### 环境检查命令 | Environment Check Commands

```bash
# 检查所有环境 | Check all environments
echo "Node.js version:" && node --version
echo "npm version:" && npm --version
echo "Java version:" && java -version
echo "Maven version:" && mvn --version

# 检查端口状态 | Check port status
netstat -tulpn | grep -E ":(5173|8080)"

# 检查进程 | Check processes
ps aux | grep -E "(node|java)" | grep -v grep
```

### 性能优化 | Performance Optimization

```bash
# 前端构建优化 | Frontend build optimization
npm run build -- --mode production

# 后端 JVM 优化 | Backend JVM optimization
java -Xms512m -Xmx1024m -jar target/server-0.0.1-SNAPSHOT.jar

# 开发模式热重载优化 | Development hot reload optimization
npm run dev -- --host 0.0.0.0 --port 5173
```

---

📝 本文档持续更新中，如有疑问请及时反馈。
💡 更多问题请查看项目 Issues 或联系开发团队。
