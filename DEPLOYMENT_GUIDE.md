# AI教材平台部署指南

## 目标服务器信息
- IP地址：222.204.4.108
- 操作系统：建议使用 Ubuntu 20.04 或 CentOS 7+
- 所需端口：80（HTTP）、8082（后端服务）、3306（MySQL）

## 前置要求

### 1. 系统环境
- Node.js 16+
- Java 11+
- Maven 3.6+
- MySQL 5.7+
- Nginx 1.18+

### 2. 安装必要软件

#### Ubuntu/Debian:
```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装 Node.js
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
sudo apt install -y nodejs

# 安装 Java
sudo apt install -y openjdk-11-jdk

# 安装 Maven
sudo apt install -y maven

# 安装 MySQL
sudo apt install -y mysql-server

# 安装 Nginx
sudo apt install -y nginx

# 安装 Git
sudo apt install -y git
```

#### CentOS/RHEL:
```bash
# 更新系统
sudo yum update -y

# 安装 Node.js
curl -fsSL https://rpm.nodesource.com/setup_16.x | sudo bash -
sudo yum install -y nodejs

# 安装 Java
sudo yum install -y java-11-openjdk java-11-openjdk-devel

# 安装 Maven
sudo yum install -y maven

# 安装 MySQL
sudo yum install -y mysql-server

# 安装 Nginx
sudo yum install -y nginx

# 安装 Git
sudo yum install -y git
```

## 部署步骤

### 1. 克隆项目
```bash
cd /opt
sudo git clone https://github.com/your-repo/Introduction-to-Artificial-Intelligence.git
cd Introduction-to-Artificial-Intelligence
```

### 2. 配置数据库
```bash
# 启动 MySQL
sudo systemctl start mysql
sudo systemctl enable mysql

# 安全配置 MySQL
sudo mysql_secure_installation

# 创建数据库和用户
sudo mysql -u root -p

# 在 MySQL 中执行：
CREATE DATABASE AI_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'ai_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON AI_platform.* TO 'ai_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;

# 导入初始数据
mysql -u ai_user -p AI_platform < docs/AI-platform.sql
```

### 3. 修改配置文件

#### 修改后端生产环境配置
编辑 `server/src/main/resources/application-prod.properties`：
```properties
# 修改数据库连接信息
spring.datasource.username=ai_user
spring.datasource.password=your_secure_password

# 修改邮件配置（如果需要）
spring.mail.username=your-email@qq.com
spring.mail.password=your-app-password
```

### 4. 使用部署脚本
```bash
# 添加执行权限
chmod +x deploy.sh

# 运行部署脚本
sudo ./deploy.sh
```

### 5. 手动部署（如果不使用脚本）

#### 构建前端
```bash
cd client
npm install
npm run build

# 创建部署目录
sudo mkdir -p /var/www/ai-platform/dist
sudo cp -r dist/* /var/www/ai-platform/dist/
```

#### 构建后端
```bash
cd ../server
mvn clean package -DskipTests

# 创建部署目录
sudo mkdir -p /var/www/ai-platform/server
sudo cp target/server-0.0.1-SNAPSHOT.jar /var/www/ai-platform/server/
```

#### 配置 Nginx
```bash
# 复制配置文件
sudo cp nginx.conf /etc/nginx/nginx.conf

# 测试配置
sudo nginx -t

# 重启 Nginx
sudo systemctl restart nginx
sudo systemctl enable nginx
```

#### 创建后端服务
```bash
# 创建服务文件
sudo vim /etc/systemd/system/ai-platform-backend.service

# 添加以下内容：
[Unit]
Description=AI Platform Backend Service
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/var/www/ai-platform/server
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod server-0.0.1-SNAPSHOT.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target

# 启动服务
sudo systemctl daemon-reload
sudo systemctl enable ai-platform-backend
sudo systemctl start ai-platform-backend
```

### 6. 设置防火墙
```bash
# Ubuntu/Debian
sudo ufw allow 80/tcp
sudo ufw allow 22/tcp
sudo ufw enable

# CentOS/RHEL
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=ssh
sudo firewall-cmd --reload
```

### 7. 设置文件权限
```bash
# 设置正确的所有者和权限
sudo chown -R www-data:www-data /var/www/ai-platform
sudo chmod -R 755 /var/www/ai-platform
sudo chmod -R 777 /var/www/ai-platform/uploads
```

## 验证部署

### 1. 检查服务状态
```bash
# 检查后端服务
sudo systemctl status ai-platform-backend

# 检查 Nginx
sudo systemctl status nginx

# 检查 MySQL
sudo systemctl status mysql
```

### 2. 查看日志
```bash
# 后端日志
sudo journalctl -u ai-platform-backend -f

# Nginx 日志
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log
```

### 3. 测试访问
- 前端页面：http://222.204.4.108
- 后端健康检查：http://222.204.4.108/api/chapters/health

## 故障排查

### 1. 后端无法启动
- 检查 Java 版本：`java -version`
- 检查端口占用：`sudo lsof -i:8082`
- 查看详细日志：`sudo journalctl -u ai-platform-backend -n 100`

### 2. 前端无法访问
- 检查 Nginx 配置：`sudo nginx -t`
- 检查文件权限：`ls -la /var/www/ai-platform/dist`
- 查看 Nginx 错误日志：`sudo tail -f /var/log/nginx/error.log`

### 3. 数据库连接失败
- 检查 MySQL 服务：`sudo systemctl status mysql`
- 测试连接：`mysql -u ai_user -p AI_platform`
- 检查防火墙：`sudo iptables -L`

### 4. 文件上传失败
- 检查上传目录权限：`ls -la /var/www/ai-platform/uploads`
- 检查磁盘空间：`df -h`

## 性能优化建议

### 1. 启用 Gzip 压缩
已在 nginx.conf 中配置

### 2. 配置 SSL（HTTPS）
```bash
# 安装 Certbot
sudo apt install certbot python3-certbot-nginx

# 获取证书（需要域名）
sudo certbot --nginx -d your-domain.com
```

### 3. 数据库优化
```sql
-- 添加索引
ALTER TABLE quiz_results ADD INDEX idx_user_id (user_id);
ALTER TABLE quiz_results ADD INDEX idx_chapter_id (chapter_id);
ALTER TABLE learning_records ADD INDEX idx_user_created (user_id, created_time);
```

### 4. JVM 优化
修改服务文件中的启动命令：
```bash
ExecStart=/usr/bin/java -Xms512m -Xmx2g -jar -Dspring.profiles.active=prod server-0.0.1-SNAPSHOT.jar
```

## 备份策略

### 1. 数据库备份
```bash
# 创建备份脚本
sudo vim /opt/backup-ai-platform.sh

#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/opt/backups"
mkdir -p $BACKUP_DIR

# 备份数据库
mysqldump -u ai_user -p'your_password' AI_platform > $BACKUP_DIR/ai_platform_$DATE.sql

# 备份上传文件
tar -czf $BACKUP_DIR/uploads_$DATE.tar.gz /var/www/ai-platform/uploads

# 保留最近7天的备份
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
find $BACKUP_DIR -name "*.tar.gz" -mtime +7 -delete

# 添加到 crontab
# 0 2 * * * /opt/backup-ai-platform.sh
```

## 监控建议

### 1. 使用 PM2 管理后端进程（可选）
```bash
# 安装 PM2
sudo npm install -g pm2

# 使用 PM2 启动
pm2 start "java -jar -Dspring.profiles.active=prod server-0.0.1-SNAPSHOT.jar" --name ai-backend
pm2 save
pm2 startup
```

### 2. 安装监控工具
```bash
# 安装 htop
sudo apt install htop

# 安装 netdata（可选）
bash <(curl -Ss https://my-netdata.io/kickstart.sh)
```

## 安全建议

1. **修改默认密码**：确保修改所有默认密码
2. **限制数据库访问**：只允许本地连接
3. **配置防火墙**：只开放必要端口
4. **定期更新**：保持系统和依赖项更新
5. **启用日志审计**：监控异常访问

## 联系支持

如有问题，请查看：
- 项目文档：`/docs`
- 开发者指南：`DEVELOPER_GUIDE.md`
- 提交Issue：[GitHub Issues]
