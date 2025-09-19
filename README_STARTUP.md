# AI教材平台 - 快速启动指南

## 🚀 一键启动

```bash
# 1. 设置脚本权限
chmod +x setup-scripts.sh
./setup-scripts.sh

# 2. 启动所有服务
./start-all.sh
```

## 📋 服务说明

### 启动的服务
- **MySQL 数据库**: 端口 3307 (Docker 容器)
- **后端服务**: 端口 8082 (Spring Boot)
- **前端服务**: 端口 5173 (Vite 开发服务器)

### 访问地址
- **前端页面**: http://localhost:5173
- **后端API**: http://localhost:8082
- **健康检查**: http://localhost:8082/api/chapters/health

## 🔧 单独启动服务

```bash
# 启动前端
./start-frontend.sh

# 启动后端
./start-backend.sh

# 停止所有服务
./stop-all.sh
```

## 🌐 外网访问配置

### 端口转发
在路由器或云服务器上配置端口转发：
- 外部端口 5173 → 内网 222.204.4.108:5173
- 外部端口 8082 → 内网 222.204.4.108:8082

### 外网访问地址
- **前端**: http://222.204.4.108:5173
- **后端**: http://222.204.4.108:8082

## ✅ 功能验证

### 基础功能测试
1. 访问前端页面
2. 用户注册/登录
3. 章节浏览
4. 答题功能

### API 测试
```bash
# 测试章节列表
curl http://localhost:8082/api/chapters

# 测试注册
curl -X POST http://localhost:8082/api/user/register \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=test&password=test123"
```

## 📁 项目结构

```
├── client/                 # 前端代码
├── server/                 # 后端代码
├── docker-compose.yml      # Docker 配置
├── start-all.sh           # 一键启动脚本
├── start-frontend.sh      # 前端启动脚本
├── start-backend.sh       # 后端启动脚本
├── stop-all.sh           # 停止所有服务
├── nginx.conf            # Nginx 配置
└── logs/                 # 日志目录
```

## 🛠️ 故障排查

### 查看日志
```bash
# 前端日志
tail -f logs/frontend.log

# 后端日志
tail -f logs/backend.log

# 数据库日志
docker logs ai_platform_mysql
```

### 检查服务状态
```bash
# 检查端口占用
sudo lsof -i:5173  # 前端
sudo lsof -i:8082  # 后端
sudo lsof -i:3307  # 数据库

# 检查进程
ps aux | grep java
ps aux | grep node
```

### 重启服务
```bash
# 停止所有服务
./stop-all.sh

# 重新启动
./start-all.sh
```

## 📚 详细文档

- [部署指南](DEPLOYMENT_GUIDE.md)
- [外网访问配置](EXTERNAL_ACCESS_GUIDE.md)
- [快速部署](QUICK_START.md)
- [部署检查清单](deployment-checklist.md)

## 🔐 默认账号

- **管理员账号**: goodlabAdmin
- **管理员密码**: goodlabPwd

## 📞 技术支持

如遇问题，请：
1. 查看日志文件
2. 检查网络连接
3. 参考故障排查部分
4. 查看详细文档
