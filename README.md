# 《人工智能概论与应用》数字化教材平台

> 基于《人工智能概论与应用》教材的交互式数字化学习平台，旨在为学生提供更加生动、有趣的人工智能学习体验。

## ⚡ 快速启动

### 🚀 一键启动脚本（推荐）

**Linux/macOS:**
```bash
# 检查项目状态
./check-status.sh

# 启动前后端服务
./start.sh

# 只启动前端
./start.sh frontend

# 只启动后端
./start.sh backend
```

**Windows:**
```cmd
# 检查项目状态
check-status.bat

# 启动前后端服务
start.bat

# 只启动前端
start.bat frontend

# 只启动后端
start.bat backend
```

### 📋 手动启动命令

**前端启动:**
```bash
cd client
npm install
npm run dev
```
访问：http://localhost:5173

**后端启动:**
```bash
cd server
./mvnw spring-boot:run
```
访问：http://localhost:8080

### 🎯 核心功能
- 🧠 **神经网络训练实验室** - 交互式可视化训练过程
- 📊 **回归算法演示** - 机器学习算法可视化
- 📚 **数字化教材** - 现代化的在线学习体验
- 🎮 **交互式学习** - 点击、拖拽、实时反馈

## 📖 项目简介

本项目是为《人工智能概论与应用》教材开发的数字化网页教材平台，通过现代化的Web技术将传统纸质教材转化为交互式的在线学习平台。项目采用前后端分离的架构，为学生提供更加直观、互动的学习方式，帮助学生更好地理解和掌握人工智能的核心概念和应用。

### 🎯 项目目标

- **数字化转型**：将传统教材转化为现代化的数字学习平台
- **提升学习体验**：通过交互式界面和多媒体内容增强学习效果
- **激发学习兴趣**：运用现代Web技术让AI学习变得更加有趣
- **便捷访问**：随时随地通过网络访问学习内容

## 📚 教材内容概览

本数字化平台基于《人工智能概论与应用》教材，涵盖以下核心章节：

### 📋 章节结构

- **续章** - 生活中的人工智能概述
- **第1章** - 初识人工智能
  - 人工智能的定义
  - 智能的定义
  - 发展历史与现状
- **第2章** - 人工智能基础
  - 人工智能、机器学习、深度学习的关系
  - 基础理论与概念
- **第3章** - 机器学习进阶
- **第4章** - 深度学习应用
- **第5章** - 自然语言处理
- **第6章** - 计算机视觉
- **第7章** - 人工智能的未来与挑战

## 🏗️ 技术架构

### 前端技术栈
- **框架**: Vue.js 3.5.17
- **构建工具**: Vite 7.0.4
- **开发语言**: JavaScript/TypeScript
- **UI组件**: 现代化响应式设计

### 后端技术栈
- **框架**: Spring Boot 3.5.3
- **开发语言**: Java 17
- **构建工具**: Maven
- **依赖管理**: Lombok

### 项目结构
```
Introduction-to-Artificial-Intelligence/
├── AI-GOODLAB/                 # 原始LaTeX教材源码
│   ├── book.pdf               # 编译后的PDF教材
│   ├── chapter_*.tex          # 各章节LaTeX源文件
│   ├── figures/               # 图片资源
│   └── gallery/               # 展示图片
├── client/                    # Vue.js前端项目
│   ├── src/
│   │   ├── components/        # Vue组件
│   │   ├── assets/           # 静态资源
│   │   └── App.vue           # 主应用组件
│   ├── package.json          # 前端依赖配置
│   └── vite.config.js        # Vite配置文件
└── server/                    # Spring Boot后端项目
    ├── src/main/java/        # Java源码
    ├── src/main/resources/   # 配置文件和静态资源
    └── pom.xml              # Maven配置文件
```

## 🚀 快速开始

### 环境要求

- **Node.js**: >= 16.0.0 (推荐使用 LTS 版本)
- **Java**: >= 17 (OpenJDK 17 或 Oracle JDK 17)
- **Maven**: >= 3.6.0 (或使用项目自带的 Maven Wrapper)

### MySQL 数据库
- **数据库版本**: MySQL 8.0 或以上
```
-- =================================================================
-- AI Platform 数据库初始化脚本
-- 执行方式: mysql -u root -p --socket=/var/run/mysqld/mysqld.sock
-- =================================================================

-- 删除已存在的数据库（可选，用于全新创建）
DROP DATABASE IF EXISTS AI_platform;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS AI_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE AI_platform;

-- 打印当前操作信息
SELECT '数据库 AI_platform 创建成功，并切换到该数据库' AS '操作状态';

-- =================================================================
-- 创建数据表
-- =================================================================

-- 用户表
CREATE TABLE user (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    username VARCHAR(20) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(32) COMMENT '密码',
    nickname VARCHAR(10) DEFAULT '' COMMENT '昵称',
    email VARCHAR(128) DEFAULT '' COMMENT '邮箱',
    user_pic VARCHAR(128) DEFAULT '' COMMENT '头像',
    level INT DEFAULT 1 COMMENT '用户等级',
    experience INT DEFAULT 0 COMMENT '经验值',
    total_score INT DEFAULT 0 COMMENT '总分数',
    completed_chapters INT DEFAULT 0 COMMENT '已完成章节数',
    quiz_count INT DEFAULT 0 COMMENT '答题次数',
    correct_answers INT DEFAULT 0 COMMENT '正确答案数',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '修改时间'
) COMMENT '用户表';
SELECT '表 `user` 创建成功' AS '操作状态';

-- 用户成就表
CREATE TABLE user_achievement (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id INT UNSIGNED NOT NULL COMMENT '用户ID',
    achievement_type VARCHAR(50) NOT NULL COMMENT '成就类型',
    achievement_name VARCHAR(100) NOT NULL COMMENT '成就名称',
    achievement_desc VARCHAR(255) COMMENT '成就描述',
    earned_date DATETIME NOT NULL COMMENT '获得时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) COMMENT '用户成就表';
SELECT '表 `user_achievement` 创建成功' AS '操作状态';

-- 用户学习记录表
CREATE TABLE user_learning_record (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id INT UNSIGNED NOT NULL COMMENT '用户ID',
    chapter_id INT COMMENT '章节ID',
    activity_type VARCHAR(50) NOT NULL COMMENT '活动类型(quiz,chapter,experiment)',
    score INT DEFAULT 0 COMMENT '得分',
    experience_gained INT DEFAULT 0 COMMENT '获得经验值',
    completion_time DATETIME COMMENT '完成时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) COMMENT '用户学习记录表';
SELECT '表 `user_learning_record` 创建成功' AS '操作状态';

-- 章节表
CREATE TABLE chapters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    order_num INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '章节表';
SELECT '表 `chapters` 创建成功' AS '操作状态';

-- 问题表
CREATE TABLE questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chapter_id BIGINT,
    question_text TEXT NOT NULL,
    question_type VARCHAR(20) DEFAULT 'MULTIPLE_CHOICE',
    options JSON,
    correct_answer VARCHAR(255),
    explanation TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chapter_id) REFERENCES chapters(id)
) COMMENT '问题表';
SELECT '表 `questions` 创建成功' AS '操作状态';


-- =================================================================
-- 插入初始数据
-- =================================================================

-- 插入管理员用户
INSERT INTO user (username, password, nickname, email, level, experience, total_score, completed_chapters, quiz_count, correct_answers, create_time, update_time)
VALUES ('admin', MD5('admin'), 'Administrator', 'admin@example.com', 1, 0, 0, 0, 0, 0, NOW(), NOW());

-- 插入测试用户
INSERT INTO user (username, password, nickname, email, level, experience, total_score, completed_chapters, quiz_count, correct_answers, create_time, update_time)
VALUES ('test', MD5('test'), 'Test User', 'test@example.com', 1, 0, 0, 0, 0, 0, NOW(), NOW());
SELECT '初始用户数据插入成功' AS '操作状态';

-- 插入章节示例数据
INSERT INTO chapters (title, content, order_num) VALUES
('人工智能概述', '人工智能（Artificial Intelligence，AI）是计算机科学的一个分支...', 1),
('机器学习基础', '机器学习是人工智能的一个重要分支...', 2),
('深度学习入门', '深度学习是机器学习的一个子领域...', 3),
('计算机视觉', '计算机视觉是人工智能的一个重要应用领域...', 4),
('自然语言处理', '自然语言处理是人工智能的另一个重要应用领域...', 5),
('AI应用案例', '人工智能在各个领域的实际应用案例...', 6),
('实践项目', '动手实践项目，巩固所学知识...', 7);
SELECT '章节示例数据插入成功' AS '操作状态';

-- 插入示例问题
INSERT INTO questions (chapter_id, question_text, question_type, options, correct_answer, explanation) VALUES
(1, '人工智能的定义是什么？', 'MULTIPLE_CHOICE', '["让机器像人一样思考", "让机器具备智能行为", "让机器完全替代人类", "让机器拥有情感"]', '让机器具备智能行为', '人工智能是让机器具备智能行为的技术'),
(2, '机器学习的主要类型包括哪些？', 'MULTIPLE_CHOICE', '["监督学习", "无监督学习", "强化学习", "以上都是"]', '以上都是', '机器学习包括监督学习、无监督学习和强化学习三种主要类型');
SELECT '问题示例数据插入成功' AS '操作状态';


-- =================================================================
-- 检查数据表和结构
-- =================================================================
-- 检查所有数据库
SHOW DATABASES;
-- 切换到 AI_platform 数据库
USE AI_platform;
-- 检查所有数据表
SELECT '--- 检查数据库中的所有表 ---' AS '检查项目';
SHOW TABLES;

-- 检查每个表的结构
SELECT '--- 检查 `user` 表结构 ---' AS '检查项目';
DESCRIBE user;

SELECT '--- 检查 `user_achievement` 表结构 ---' AS '检查项目';
DESCRIBE user_achievement;

SELECT '--- 检查 `user_learning_record` 表结构 ---' AS '检查项目';
DESCRIBE user_learning_record;

SELECT '--- 检查 `chapters` 表结构 ---' AS '检查项目';
DESCRIBE chapters;

SELECT '--- 检查 `questions` 表结构 ---' AS '检查项目';
DESCRIBE questions;

-- 验证插入结果
SELECT '--- 验证 `user` 表的初始数据 ---' AS '检查项目';
SELECT * FROM user;

SELECT '脚本执行完毕' AS '最终状态';

```

### 安装与运行

#### 1. 克隆项目
```bash
git clone https://github.com/your-username/Introduction-to-Artificial-Intelligence.git
cd Introduction-to-Artificial-Intelligence
```

#### 2. 启动前端服务
```bash
# 进入前端目录
cd client

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```
前端服务将在 `http://localhost:5173` 启动（如果端口被占用，会自动使用下一个可用端口如 5174）

#### 3. 启动后端服务

**方式一：使用 Maven Wrapper（推荐）**
```bash
# 进入后端目录
cd server

# 使用 Maven Wrapper 启动（无需预安装 Maven）
./mvnw spring-boot:run
```

**方式二：使用本地 Maven**
```bash
# 进入后端目录
cd server

# 清理并编译
mvn clean compile

# 启动 Spring Boot 应用
mvn spring-boot:run
```

**方式三：打包后运行**
```bash
# 进入后端目录
cd server

# 打包应用
./mvnw clean package

# 运行打包后的 JAR 文件
java -jar target/server-0.0.1-SNAPSHOT.jar
```

后端服务将在 `http://localhost:8080` 启动

#### 4. 访问应用

- **主页**: http://localhost:5173
- **章节列表**: http://localhost:5173/chapters
- **神经网络实验室**: http://localhost:5173/network-training
- **回归演示**: http://localhost:5173/regression-demo
- **后端API**: http://localhost:8080/api

## � 启动脚本说明

项目提供了便捷的启动脚本，支持一键启动和状态检查：

### 启动脚本
- **`start.sh`** (Linux/macOS) - 项目启动脚本
- **`start.bat`** (Windows) - Windows 启动脚本

**支持的命令:**
```bash
./start.sh [选项]

选项:
  frontend, fe    只启动前端
  backend, be     只启动后端
  both, all       同时启动前后端 (默认)
  check           检查环境
  help, -h        显示帮助信息
```

### 状态检查脚本
- **`check-status.sh`** (Linux/macOS) - 项目状态检查
- **`check-status.bat`** (Windows) - Windows 状态检查

**检查内容:**
- ✅ 项目结构完整性
- ✅ 开发环境版本
- ✅ 端口占用状态
- ✅ 服务运行状态
- ✅ 依赖安装情况

### 使用示例
```bash
# 检查项目状态
./check-status.sh

# 启动所有服务
./start.sh

# 只启动前端开发服务器
./start.sh frontend

# 只启动后端API服务
./start.sh backend

# 查看帮助信息
./start.sh help
```

## �📱 功能特性

### 🎨 用户界面
- **响应式设计**: 支持桌面端、平板和移动端访问
- **现代化UI**: 基于 Element Plus 的简洁美观界面设计
- **交互式导航**: 便捷的章节导航和内容检索
- **渐变主题**: 现代化的渐变色彩设计
- **动画效果**: 流畅的过渡动画和交互反馈

### 📖 学习功能
- **章节浏览**: 按章节组织的结构化内容展示
- **多媒体支持**: 图片、图表、动画等多媒体内容展示
- **交互式演示**: 机器学习算法的可视化演示
- **学习进度**: 记录和跟踪学习进度
- **实时反馈**: 即时的学习效果反馈

### 🧠 AI 可视化功能
- **神经网络训练实验室**: 交互式神经网络训练可视化
  - 实时数据流动画
  - 可点击的神经元节点
  - 动态调整网络结构（1-10个神经元/层）
  - 实时训练指标监控
  - 猫狗分类演示
- **回归算法演示**: 线性回归和多项式回归的可视化
- **参数调节**: 实时调整学习率、批次大小等超参数
- **训练日志**: 详细的训练过程记录

### 🔧 技术特性
- **前后端分离**: Vue 3 + Spring Boot 的现代化架构
- **组件化开发**: 高度模块化的前端组件设计
- **热重载**: 开发环境下的实时更新
- **Chart.js 集成**: 专业的图表可视化
- **SVG 动画**: 高性能的网络结构可视化
- **响应式布局**: 适配各种屏幕尺寸

## 📸 项目截图

### 主页界面
*即将添加主页截图*

### 章节浏览
*即将添加章节浏览截图*

### 移动端适配
*即将添加移动端截图*

## 🎯 核心亮点

### 📚 教材特色
- **权威内容**: 基于专业教材，内容权威可靠
- **系统性强**: 从基础概念到实际应用的完整知识体系
- **案例丰富**: 结合生活实例，让抽象概念具体化
- **与时俱进**: 涵盖最新的AI发展趋势和技术

### 💡 技术创新
- **LaTeX转Web**: 将学术级LaTeX内容无缝转换为Web格式
- **交互式学习**: 比传统PDF更丰富的交互体验
- **实时更新**: 内容可以实时更新和维护
- **多端同步**: 学习进度在不同设备间同步

## 📖 原始教材

本项目基于GoodLab团队编写的《人工智能概论与应用》教材，该教材采用LaTeX排版，内容涵盖：

- 人工智能的基本概念和发展历程
- 机器学习和深度学习的核心理论
- 自然语言处理技术及应用
- 计算机视觉技术及应用
- 人工智能的实际应用案例
- 人工智能的发展趋势和挑战

## 🤝 贡献指南

我们欢迎任何形式的贡献！请遵循以下步骤：

1. Fork 本项目
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

## 📄 许可证

本项目采用 [MIT License](LICENSE) 许可证。

## 👥 开发团队

- **GoodLab** - 教材内容编写
- **开发团队** - 数字化平台开发

## 🎓 学习路径建议

### 初学者路径
1. **续章**: 了解生活中的人工智能应用
2. **第1章**: 掌握人工智能的基本概念
3. **第2章**: 理解AI、ML、DL的关系
4. **第3-4章**: 深入学习机器学习和深度学习
5. **第5-6章**: 探索具体应用领域
6. **第7章**: 展望未来发展趋势

### 进阶学习者路径
- 重点关注第3-6章的技术细节
- 结合实际项目进行实践
- 深入研究特定应用领域

## 🔧 开发指南

### Mysql数据库
```
mysql -u root -p --socket=/var/run/mysqld/mysqld.sock

# 数据源配置
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/AI_platform?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root123456
```

### 前端开发命令

```bash
# 进入前端目录
cd client

# 安装依赖
npm install

# 启动开发服务器（热重载）
npm run dev

# 构建生产版本
npm run build

# 预览生产版本
npm run preview

# 代码格式化
npm run lint

# 依赖更新
npm update
```

### 后端开发命令

```bash
# 进入后端目录
cd server

# 使用 Maven Wrapper（推荐）
./mvnw clean compile          # 清理并编译
./mvnw test                   # 运行测试
./mvnw spring-boot:run        # 启动应用
./mvnw clean package          # 打包应用
./mvnw clean install          # 安装到本地仓库

# 使用本地 Maven
mvn clean compile             # 清理并编译
mvn test                      # 运行测试
mvn spring-boot:run           # 启动应用
mvn clean package             # 打包应用
mvn clean install             # 安装到本地仓库

# 运行打包后的应用
java -jar target/server-0.0.1-SNAPSHOT.jar

# 指定配置文件运行
java -jar target/server-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

# 指定端口运行
java -jar target/server-0.0.1-SNAPSHOT.jar --server.port=8081
```

### 开发环境配置

#### 前端环境变量
在 `client/` 目录下创建 `.env.local` 文件：
```env
# API 基础地址
VITE_API_BASE_URL=http://localhost:8080/api

# 开发模式
VITE_NODE_ENV=development
```

#### 后端配置
在 `server/src/main/resources/` 目录下的 `application.properties`：
```properties
# 服务器端口
server.port=8080

# 数据库配置（如需要）
# spring.datasource.url=jdbc:mysql://localhost:3306/ai_textbook
# spring.datasource.username=root
# spring.datasource.password=password

# 日志级别
logging.level.com.goodlab.server=DEBUG
```

### 代码规范
- **前端**: 遵循 Vue.js 官方风格指南和 ESLint 规则
- **后端**: 遵循 Spring Boot 最佳实践和 Java 编码规范
- **提交信息**: 使用语义化提交规范 (Conventional Commits)
- **组件命名**: 使用 PascalCase 命名 Vue 组件
- **API 设计**: 遵循 RESTful API 设计原则

## 🐛 问题排查

### 常见问题

**Q: 前端启动失败？**
```bash
# 检查 Node.js 版本
node --version  # 应该 >= 16.0.0

# 清除缓存并重新安装
cd client
rm -rf node_modules package-lock.json
npm install
```

**Q: 后端编译错误？**
```bash
# 检查 Java 版本
java -version   # 应该 >= 17

# 检查 JAVA_HOME 环境变量
echo $JAVA_HOME

# 清理并重新编译
cd server
./mvnw clean compile
```

**Q: 端口冲突？**
```bash
# 前端端口修改（vite.config.js）
export default {
  server: {
    port: 3000  // 修改为其他端口
  }
}

# 后端端口修改（application.properties）
server.port=8081
```

**Q: 神经网络可视化无法加载？**
A: 确保 Chart.js 依赖已正确安装，检查浏览器控制台是否有 JavaScript 错误

**Q: 对话框无法关闭？**
A: 这是已知问题，已在最新版本中修复。请确保使用最新代码

**Q: 网络结构配置不生效？**
A: 确保在训练停止状态下修改网络结构，训练中无法修改

### 调试技巧

#### 前端调试
```bash
# 启用详细日志
npm run dev -- --debug

# 检查网络请求
# 打开浏览器开发者工具 -> Network 标签页

# Vue DevTools
# 安装 Vue DevTools 浏览器扩展进行组件调试
```

#### 后端调试
```bash
# 启用调试模式
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# 查看详细日志
./mvnw spring-boot:run -Dlogging.level.com.goodlab.server=DEBUG

# 检查应用健康状态
curl http://localhost:8080/actuator/health
```

#### 环境检查
```bash
# 检查所有环境
node --version
java -version
mvn --version

# 检查端口占用
netstat -tulpn | grep :5173  # 前端端口
netstat -tulpn | grep :8080  # 后端端口

# 检查进程
ps aux | grep node    # 前端进程
ps aux | grep java    # 后端进程
```

## 📈 项目状态

### 已完成功能 ✅
- ✅ 基础架构搭建完成
- ✅ 前端 Vue.js 3 + Vite 框架集成
- ✅ 后端 Spring Boot 3 框架集成
- ✅ Element Plus UI 组件库集成
- ✅ 响应式页面布局设计
- ✅ 章节导航和路由系统
- ✅ 神经网络训练可视化实验室
  - ✅ 实时数据流动画
  - ✅ 交互式节点控制
  - ✅ 动态网络结构配置
  - ✅ 训练指标监控
  - ✅ 猫狗分类演示
- ✅ 回归算法演示页面
- ✅ Chart.js 图表集成
- ✅ SVG 动画效果
- ✅ 移动端响应式适配

### 进行中功能 🚧
- 🚧 教材内容数字化转换
- 🚧 更多 AI 算法可视化
- 🚧 用户交互体验优化
- 🚧 性能优化和代码重构

### 待开发功能 ⏳
- ⏳ 用户注册和登录系统
- ⏳ 学习进度跟踪
- ⏳ 在线练习和测验
- ⏳ AI 助手功能
- ⏳ 社区讨论功能

## 🔮 未来规划

### 短期目标 (1-3个月)
- [ ] 完成所有章节内容的数字化
- [ ] 实现基础的用户交互功能
- [ ] 添加搜索和导航功能
- [ ] 优化移动端体验

### 中期目标 (3-6个月)
- [ ] 添加学习进度跟踪
- [ ] 实现用户注册和登录
- [ ] 添加练习题和测验功能
- [ ] 集成多媒体内容播放

### 长期目标 (6个月以上)
- [ ] 开发AI助手功能
- [ ] 添加社区讨论功能
- [ ] 实现个性化学习推荐
- [ ] 支持多语言版本

## 📞 联系我们

如有任何问题或建议，请通过以下方式联系我们：

- 项目Issues: [GitHub Issues](https://github.com/your-username/Introduction-to-Artificial-Intelligence/issues)
- 邮箱: contact@goodlab.com

## 🙏 致谢

感谢所有为本项目做出贡献的开发者和教育工作者，特别感谢：

- GoodLab团队提供的优质教材内容
- Vue.js和Spring Boot社区的技术支持
- 所有测试用户的宝贵反馈

---

⭐ 如果这个项目对您有帮助，请给我们一个星标！

📚 让我们一起推动人工智能教育的数字化转型！
