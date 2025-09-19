# 快速部署指南 - AI教材平台

## 服务器: 222.204.4.108

### 1. 快速部署命令（适用于 Ubuntu 20.04）

```bash
# 1. 安装依赖（一次性执行）
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
sudo apt update
sudo apt install -y nodejs openjdk-11-jdk maven mysql-server nginx git

# 2. 克隆项目
cd /opt
sudo git clone <your-repository-url> ai-platform
cd ai-platform

# 3. 设置数据库
sudo mysql -e "CREATE DATABASE AI_platform CHARACTER SET utf8mb4;"
sudo mysql -e "CREATE USER 'ai_user'@'localhost' IDENTIFIED BY 'AI@2024Platform';"
sudo mysql -e "GRANT ALL ON AI_platform.* TO 'ai_user'@'localhost';"
sudo mysql AI_platform < docs/AI-platform.sql

# 4. 快速部署
chmod +x deploy.sh
sudo ./deploy.sh
```

### 2. 必要的配置修改

在部署前，需要修改以下文件中的密码和配置：

#### `server/src/main/resources/application-prod.properties`
```properties
spring.datasource.password=AI@2024Platform  # 修改为实际密码
spring.mail.username=your-email@qq.com      # 修改为实际邮箱
spring.mail.password=your-app-password       # 修改为邮箱授权码
```

### 3. 验证部署

```bash
# 检查服务
systemctl status ai-platform-backend
systemctl status nginx

# 测试访问
curl http://222.204.4.108
curl http://222.204.4.108/api/chapters/health
```

### 4. 常用命令

```bash
# 重启后端
sudo systemctl restart ai-platform-backend

# 查看后端日志
sudo journalctl -u ai-platform-backend -f

# 重启Nginx
sudo systemctl restart nginx

# 查看Nginx日志
sudo tail -f /var/log/nginx/error.log
```

### 5. 故障快速排查

如果无法访问，按顺序检查：

1. **检查服务是否运行**
   ```bash
   ps aux | grep java
   ps aux | grep nginx
   ```

2. **检查端口**
   ```bash
   sudo netstat -tlnp | grep -E '80|8082'
   ```

3. **检查防火墙**
   ```bash
   sudo ufw status
   # 如果需要开放端口
   sudo ufw allow 80/tcp
   ```

4. **查看错误日志**
   ```bash
   # 后端错误
   sudo journalctl -u ai-platform-backend -n 50
   
   # Nginx错误
   sudo tail -20 /var/log/nginx/error.log
   ```

### 6. 紧急回滚

如果需要回滚：
```bash
# 停止服务
sudo systemctl stop ai-platform-backend
sudo systemctl stop nginx

# 恢复之前的版本
cd /opt/ai-platform
git checkout <previous-version>

# 重新部署
sudo ./deploy.sh
```
