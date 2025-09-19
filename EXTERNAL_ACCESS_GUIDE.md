# 外网访问配置指南

## 当前配置状态

### ✅ 已完成的配置
1. **前端路由配置** - 已修复语法错误
2. **后端跨域配置** - 已统一在 WebConfig 中管理
3. **API 路径配置** - 已支持服务器 IP 访问
4. **启动脚本** - 已创建完整的启动/停止脚本

### 🔧 启动服务

#### 方法一：一键启动（推荐）
```bash
# 设置脚本权限
chmod +x setup-scripts.sh
./setup-scripts.sh

# 启动所有服务
./start-all.sh
```

#### 方法二：分别启动
```bash
# 启动前端
./start-frontend.sh

# 启动后端（另一个终端）
./start-backend.sh
```

## 外网访问配置

### 1. 端口转发配置

#### 路由器端口转发
需要在路由器上配置端口转发：
```
外部端口 5173 -> 内网 IP 222.204.4.108:5173 (前端)
外部端口 8082 -> 内网 IP 222.204.4.108:8082 (后端)
```

#### 云服务器配置
如果是云服务器，需要在安全组中开放端口：
- 端口 5173 (前端)
- 端口 8082 (后端)

### 2. 域名配置（可选）

#### 使用 Nginx 反向代理
```bash
# 安装 Nginx
sudo apt install nginx

# 使用项目中的 nginx.conf 配置
sudo cp nginx.conf /etc/nginx/nginx.conf

# 重启 Nginx
sudo systemctl restart nginx
```

#### 配置域名解析
如果有域名，将域名解析到服务器 IP：
```
your-domain.com -> 222.204.4.108
```

### 3. SSL 证书配置（推荐）

#### 使用 Let's Encrypt
```bash
# 安装 Certbot
sudo apt install certbot python3-certbot-nginx

# 获取证书
sudo certbot --nginx -d your-domain.com
```

## 访问地址

### 开发环境
- **前端**: http://localhost:5173
- **后端API**: http://localhost:8082
- **健康检查**: http://localhost:8082/api/chapters/health

### 生产环境（外网访问）
- **前端**: http://222.204.4.108:5173
- **后端API**: http://222.204.4.108:8082
- **健康检查**: http://222.204.4.108:8082/api/chapters/health

### 使用 Nginx 代理后
- **前端**: http://222.204.4.108 (或你的域名)
- **后端API**: http://222.204.4.108/api/*

## 功能测试检查清单

### ✅ 基础功能测试
- [ ] 前端页面可以正常访问
- [ ] 静态资源（CSS、JS、图片）加载正常
- [ ] 页面路由切换正常

### ✅ 用户功能测试
- [ ] 用户注册功能正常
- [ ] 用户登录功能正常
- [ ] 用户信息显示正常
- [ ] 修改密码功能正常
- [ ] 头像上传功能正常

### ✅ API 功能测试
```bash
# 测试章节列表
curl http://222.204.4.108:8082/api/chapters

# 测试健康检查
curl http://222.204.4.108:8082/api/chapters/health

# 测试注册接口
curl -X POST http://222.204.4.108:8082/api/user/register \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=testuser&password=test123"
```

### ✅ 跨域测试
- [ ] 前端可以正常调用后端 API
- [ ] 没有 CORS 错误
- [ ] 所有 API 请求正常

## 故障排查

### 1. 前端无法访问
```bash
# 检查端口是否被占用
sudo lsof -i:5173

# 检查防火墙
sudo ufw status

# 查看前端日志
tail -f logs/frontend.log
```

### 2. 后端 API 无法访问
```bash
# 检查后端服务状态
sudo systemctl status ai-platform-backend

# 检查端口
sudo lsof -i:8082

# 查看后端日志
tail -f logs/backend.log
```

### 3. 数据库连接问题
```bash
# 检查 MySQL 容器
docker ps | grep mysql

# 测试数据库连接
mysql -h localhost -P 3307 -u root -proot123456 -e "SELECT 1"

# 查看数据库日志
docker logs ai_platform_mysql
```

### 4. 跨域问题
如果出现跨域错误，检查：
- 后端 WebConfig.java 中的 CORS 配置
- 前端 API 配置中的 baseURL
- 服务器防火墙设置

## 性能优化建议

### 1. 启用 Gzip 压缩
在 Nginx 配置中已包含 Gzip 压缩设置

### 2. 静态资源缓存
```nginx
location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
    expires 1y;
    add_header Cache-Control "public, immutable";
}
```

### 3. 数据库优化
```sql
-- 添加索引
ALTER TABLE quiz_results ADD INDEX idx_user_id (user_id);
ALTER TABLE learning_records ADD INDEX idx_user_created (user_id, created_time);
```

## 监控和维护

### 1. 日志监控
```bash
# 实时查看所有日志
tail -f logs/frontend.log logs/backend.log

# 查看系统资源
htop
```

### 2. 定期备份
```bash
# 数据库备份
mysqldump -u root -proot123456 AI_platform > backup_$(date +%Y%m%d).sql

# 文件备份
tar -czf uploads_backup_$(date +%Y%m%d).tar.gz uploads/
```

### 3. 更新部署
```bash
# 拉取最新代码
git pull

# 重新构建和部署
./start-all.sh --rebuild
```

## 安全建议

1. **修改默认密码**
   - 数据库密码
   - 管理员账号密码

2. **配置防火墙**
   ```bash
   sudo ufw allow 22/tcp    # SSH
   sudo ufw allow 80/tcp    # HTTP
   sudo ufw allow 443/tcp   # HTTPS
   sudo ufw enable
   ```

3. **定期更新**
   - 系统更新
   - 依赖包更新
   - 安全补丁

## 联系支持

如果遇到问题：
1. 查看日志文件
2. 检查网络连接
3. 验证配置文件
4. 参考本文档的故障排查部分
