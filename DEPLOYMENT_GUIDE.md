# 服务器部署指南

## 部署环境信息
- **服务器IP**: 222.204.4.108
- **前端端口**: 5173
- **后端端口**: 8082
- **外网访问地址**: http://222.204.4.108/

## 部署步骤

### 1. 服务器环境准备

#### 1.1 安装必要软件
```bash
# 更新系统包
sudo apt update && sudo apt upgrade -y

# 安装 Node.js (版本 >= 16)
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# 安装 Java 17
sudo apt install openjdk-17-jdk -y

# 安装 Maven
sudo apt install maven -y

# 安装 Nginx
sudo apt install nginx -y

# 安装 MySQL
sudo apt install mysql-server -y
```

#### 1.2 验证安装
```bash
node --version    # 应该 >= 16.0.0
java -version     # 应该是 Java 17
mvn --version     # Maven 版本
nginx -v          # Nginx 版本
mysql --version   # MySQL 版本
```

### 2. 数据库配置

#### 2.1 配置 MySQL
```bash
# 启动 MySQL 服务
sudo systemctl start mysql
sudo systemctl enable mysql

# 安全配置
sudo mysql_secure_installation

# 登录 MySQL
sudo mysql -u root -p
```

#### 2.2 创建数据库和用户
```sql
-- 创建数据库
CREATE DATABASE AI_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户（可选，也可以使用 root）
CREATE USER 'ai_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON AI_platform.* TO 'ai_user'@'localhost';
FLUSH PRIVILEGES;

-- 退出
EXIT;
```

#### 2.3 导入初始数据（如果有）
```bash
# 如果有 SQL 文件，导入数据
mysql -u root -p AI_platform < /path/to/your/init.sql
```

### 3. 项目部署

#### 3.1 上传项目文件
```bash
# 在服务器上创建项目目录
sudo mkdir -p /var/www/ai-platform
sudo chown $USER:$USER /var/www/ai-platform

# 将项目文件上传到服务器（使用 scp、rsync 或 git clone）
# 方法1: 使用 git clone
cd /var/www/ai-platform
git clone <your-repository-url> .

# 方法2: 使用 scp 上传
# scp -r /local/path/to/project user@222.204.4.108:/var/www/ai-platform/
```

#### 3.2 后端配置和启动

```bash
# 进入后端目录
cd /var/www/ai-platform/server

# 修改数据库配置（如果需要）
nano src/main/resources/application.properties
```

更新 `application.properties` 中的数据库配置：
```properties
# 数据源配置
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/AI_platform?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=your_mysql_password

# 服务器配置
server.port=8082
```

```bash
# 编译和启动后端
./mvnw clean package -DskipTests
nohup java -jar target/server-0.0.1-SNAPSHOT.jar > backend.log 2>&1 &

# 检查后端是否启动成功
curl http://localhost:8082/api/chapters
```

#### 3.3 前端配置和启动

```bash
# 进入前端目录
cd /var/www/ai-platform/client

# 安装依赖
npm install

# 启动前端开发服务器
nohup npm run dev > frontend.log 2>&1 &

# 检查前端是否启动成功
curl http://localhost:5173
```

### 4. Nginx 配置

#### 4.1 复制 Nginx 配置文件
```bash
# 复制项目中的 nginx.conf 到 Nginx 配置目录
sudo cp /var/www/ai-platform/nginx.conf /etc/nginx/sites-available/ai-platform

# 创建软链接启用站点
sudo ln -s /etc/nginx/sites-available/ai-platform /etc/nginx/sites-enabled/

# 删除默认站点（可选）
sudo rm /etc/nginx/sites-enabled/default
```

#### 4.2 测试和重启 Nginx
```bash
# 测试 Nginx 配置
sudo nginx -t

# 重启 Nginx
sudo systemctl restart nginx
sudo systemctl enable nginx

# 检查 Nginx 状态
sudo systemctl status nginx
```

### 5. 防火墙配置

```bash
# 开放必要端口
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 22/tcp
sudo ufw allow 5173/tcp  # 前端端口（可选，通过 nginx 代理）
sudo ufw allow 8082/tcp  # 后端端口（可选，通过 nginx 代理）

# 启用防火墙
sudo ufw enable

# 查看防火墙状态
sudo ufw status
```

### 6. 系统服务配置（推荐）

#### 6.1 创建后端系统服务
```bash
sudo nano /etc/systemd/system/ai-platform-backend.service
```

添加以下内容：
```ini
[Unit]
Description=AI Platform Backend Service
After=network.target mysql.service

[Service]
Type=simple
User=www-data
WorkingDirectory=/var/www/ai-platform/server
ExecStart=/usr/bin/java -jar target/server-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=ai-platform-backend

[Install]
WantedBy=multi-user.target
```

#### 6.2 创建前端系统服务
```bash
sudo nano /etc/systemd/system/ai-platform-frontend.service
```

添加以下内容：
```ini
[Unit]
Description=AI Platform Frontend Service
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/var/www/ai-platform/client
ExecStart=/usr/bin/npm run dev
Restart=always
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=ai-platform-frontend

[Install]
WantedBy=multi-user.target
```

#### 6.3 启用和启动服务
```bash
# 重新加载 systemd
sudo systemctl daemon-reload

# 启用服务
sudo systemctl enable ai-platform-backend
sudo systemctl enable ai-platform-frontend

# 启动服务
sudo systemctl start ai-platform-backend
sudo systemctl start ai-platform-frontend

# 检查服务状态
sudo systemctl status ai-platform-backend
sudo systemctl status ai-platform-frontend
```

### 7. 验证部署

#### 7.1 检查服务状态
```bash
# 检查端口监听
sudo netstat -tulpn | grep :80
sudo netstat -tulpn | grep :5173
sudo netstat -tulpn | grep :8082

# 检查进程
ps aux | grep java
ps aux | grep node
ps aux | grep nginx
```

#### 7.2 功能测试
```bash
# 测试前端访问
curl -I http://222.204.4.108/

# 测试后端 API
curl http://222.204.4.108/api/chapters

# 测试文件上传路径
curl -I http://222.204.4.108/uploads/
```

#### 7.3 浏览器测试
1. 访问 `http://222.204.4.108/` 查看首页
2. 测试用户注册和登录功能
3. 测试章节浏览和测验功能
4. 检查浏览器控制台是否有错误

### 8. 日志监控

#### 8.1 查看应用日志
```bash
# 查看后端日志
sudo journalctl -u ai-platform-backend -f

# 查看前端日志
sudo journalctl -u ai-platform-frontend -f

# 查看 Nginx 日志
sudo tail -f /var/log/nginx/ai_platform_access.log
sudo tail -f /var/log/nginx/ai_platform_error.log
```

#### 8.2 日志轮转配置
```bash
# 创建日志轮转配置
sudo nano /etc/logrotate.d/ai-platform
```

添加以下内容：
```
/var/log/nginx/ai_platform_*.log {
    daily
    missingok
    rotate 52
    compress
    delaycompress
    notifempty
    create 644 www-data www-data
    postrotate
        systemctl reload nginx
    endscript
}
```

### 9. 备份策略

#### 9.1 数据库备份
```bash
# 创建备份脚本
sudo nano /usr/local/bin/backup-ai-platform.sh
```

添加以下内容：
```bash
#!/bin/bash
BACKUP_DIR="/var/backups/ai-platform"
DATE=$(date +%Y%m%d_%H%M%S)

# 创建备份目录
mkdir -p $BACKUP_DIR

# 备份数据库
mysqldump -u root -p AI_platform > $BACKUP_DIR/ai_platform_$DATE.sql

# 备份上传文件
tar -czf $BACKUP_DIR/uploads_$DATE.tar.gz /var/www/ai-platform/server/uploads/

# 删除7天前的备份
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
find $BACKUP_DIR -name "*.tar.gz" -mtime +7 -delete

echo "Backup completed: $DATE"
```

```bash
# 设置执行权限
sudo chmod +x /usr/local/bin/backup-ai-platform.sh

# 添加到定时任务
sudo crontab -e
# 添加以下行（每天凌晨2点备份）
0 2 * * * /usr/local/bin/backup-ai-platform.sh
```

### 10. 故障排除

#### 10.1 常见问题

**问题1: 无法访问网站**
```bash
# 检查 Nginx 状态
sudo systemctl status nginx

# 检查端口监听
sudo netstat -tulpn | grep :80

# 检查防火墙
sudo ufw status
```

**问题2: API 请求失败**
```bash
# 检查后端服务
sudo systemctl status ai-platform-backend

# 查看后端日志
sudo journalctl -u ai-platform-backend -n 50

# 测试后端连接
curl http://localhost:8082/api/chapters
```

**问题3: 前端页面空白**
```bash
# 检查前端服务
sudo systemctl status ai-platform-frontend

# 查看前端日志
sudo journalctl -u ai-platform-frontend -n 50

# 检查前端构建
cd /var/www/ai-platform/client
npm run build
```

**问题4: 数据库连接失败**
```bash
# 检查 MySQL 状态
sudo systemctl status mysql

# 测试数据库连接
mysql -u root -p -e "SHOW DATABASES;"

# 检查数据库配置
cat /var/www/ai-platform/server/src/main/resources/application.properties
```

#### 10.2 性能优化

**前端优化**
```bash
# 构建生产版本
cd /var/www/ai-platform/client
npm run build

# 使用 Nginx 直接服务静态文件
# 修改 nginx.conf 中的前端配置
```

**后端优化**
```bash
# 调整 JVM 参数
# 修改 systemd 服务文件中的 ExecStart
ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar target/server-0.0.1-SNAPSHOT.jar
```

### 11. 安全建议

1. **定期更新系统和软件包**
2. **使用 HTTPS**（配置 SSL 证书）
3. **设置强密码策略**
4. **限制数据库访问权限**
5. **配置防火墙规则**
6. **定期备份数据**
7. **监控系统日志**

### 12. 维护命令

```bash
# 重启所有服务
sudo systemctl restart ai-platform-backend
sudo systemctl restart ai-platform-frontend
sudo systemctl restart nginx

# 查看系统资源使用
htop
df -h
free -h

# 清理日志
sudo journalctl --vacuum-time=7d

# 更新项目代码
cd /var/www/ai-platform
git pull origin main
# 然后重新构建和重启服务
```

## 部署完成检查清单

- [ ] 服务器环境配置完成
- [ ] 数据库安装和配置完成
- [ ] 项目代码上传完成
- [ ] 后端服务启动正常
- [ ] 前端服务启动正常
- [ ] Nginx 配置和启动完成
- [ ] 防火墙配置完成
- [ ] 系统服务配置完成
- [ ] 外网访问测试通过
- [ ] 功能测试通过
- [ ] 日志监控配置完成
- [ ] 备份策略配置完成

部署完成后，您可以通过 `http://222.204.4.108/` 访问您的AI教材平台！