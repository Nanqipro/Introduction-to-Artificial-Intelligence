# 部署检查清单

## 部署前检查

### 环境准备
- [ ] 服务器操作系统已更新到最新版本
- [ ] 已安装 Node.js 16+
- [ ] 已安装 Java 11+
- [ ] 已安装 Maven 3.6+
- [ ] 已安装 MySQL 5.7+
- [ ] 已安装 Nginx 1.18+
- [ ] 已配置服务器防火墙规则（开放80端口）
- [ ] 已创建数据库和用户
- [ ] 已导入初始数据

### 配置文件修改
- [ ] `client/src/services/api.js` - 已添加服务器IP判断
- [ ] `server/src/main/java/com/goodlab/server/config/WebConfig.java` - 已添加服务器IP到CORS配置
- [ ] `server/src/main/resources/application-prod.properties` - 已创建并配置
- [ ] `nginx.conf` - 已配置并放置到正确位置

## 部署过程检查

### 前端部署
- [ ] 前端项目已成功构建 (`npm run build`)
- [ ] 构建产物已复制到 `/var/www/ai-platform/dist`
- [ ] 静态文件权限设置正确

### 后端部署
- [ ] 后端项目已成功构建 (`mvn clean package`)
- [ ] JAR文件已复制到部署目录
- [ ] 系统服务已创建并启动
- [ ] 后端服务日志无错误

### Nginx配置
- [ ] Nginx配置文件语法检查通过 (`nginx -t`)
- [ ] Nginx服务已重启并运行正常
- [ ] 代理规则工作正常

## 功能测试检查

### 基础功能
- [ ] 首页可以正常访问：http://222.204.4.108
- [ ] 静态资源加载正常（CSS、JS、图片）
- [ ] 页面路由切换正常

### 用户功能
- [ ] 用户注册功能正常
- [ ] 用户登录功能正常
- [ ] 用户信息显示正常
- [ ] 修改密码功能正常
- [ ] 头像上传功能正常

### 核心功能
- [ ] 章节列表显示正常
- [ ] 章节内容加载正常
- [ ] 视频播放功能正常
- [ ] 答题系统功能正常
- [ ] 排行榜功能正常
- [ ] 成就系统功能正常

### API测试
- [ ] 健康检查接口：`curl http://222.204.4.108/api/chapters/health`
- [ ] 章节列表接口：`curl http://222.204.4.108/api/chapters`
- [ ] 跨域请求正常工作

### 文件上传
- [ ] 头像上传目录权限正确
- [ ] 上传的文件可以正常访问
- [ ] 上传大小限制生效

## 性能和安全检查

### 性能
- [ ] 页面加载时间合理（< 3秒）
- [ ] API响应时间正常（< 500ms）
- [ ] 静态资源已启用缓存
- [ ] Gzip压缩已启用

### 安全
- [ ] 所有默认密码已修改
- [ ] 数据库只允许本地连接
- [ ] 敏感配置文件权限设置正确
- [ ] 错误页面不暴露敏感信息

## 日志和监控

### 日志检查
- [ ] Nginx访问日志记录正常
- [ ] Nginx错误日志无异常
- [ ] 后端应用日志记录正常
- [ ] 日志轮转配置正确

### 系统资源
- [ ] CPU使用率正常（< 80%）
- [ ] 内存使用率正常（< 80%）
- [ ] 磁盘空间充足（> 20%）
- [ ] 网络连接数正常

## 备份和恢复

- [ ] 数据库备份脚本已配置
- [ ] 文件备份策略已制定
- [ ] 备份恢复流程已测试
- [ ] 定时备份任务已设置

## 问题排查命令

### 服务状态检查
```bash
# 检查所有服务状态
sudo systemctl status ai-platform-backend
sudo systemctl status nginx
sudo systemctl status mysql

# 查看端口占用
sudo netstat -tlnp | grep -E '80|8082|3306'
```

### 日志查看
```bash
# 查看后端日志
sudo journalctl -u ai-platform-backend -f

# 查看Nginx日志
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log

# 查看MySQL日志
sudo tail -f /var/log/mysql/error.log
```

### 资源监控
```bash
# 实时资源监控
htop

# 磁盘使用情况
df -h

# 内存使用情况
free -h

# 网络连接情况
ss -tunlp
```

## 回滚方案

如果部署出现问题，执行以下回滚步骤：

1. 停止服务
```bash
sudo systemctl stop ai-platform-backend
sudo systemctl stop nginx
```

2. 恢复备份
```bash
# 恢复数据库
mysql -u ai_user -p AI_platform < /opt/backups/ai_platform_backup.sql

# 恢复文件
tar -xzf /opt/backups/uploads_backup.tar.gz -C /
```

3. 使用之前的版本
```bash
# 切换到之前的代码版本
git checkout <previous-commit-hash>

# 重新构建和部署
```

## 联系信息

部署遇到问题时的处理流程：
1. 检查本清单中的所有项目
2. 查看相关日志文件
3. 参考 DEPLOYMENT_GUIDE.md
4. 查看项目 Issues
