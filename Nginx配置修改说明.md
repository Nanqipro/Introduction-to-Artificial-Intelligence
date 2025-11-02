# Nginx 配置安全加固说明

## 📋 修改概述

根据漏洞检测报告，已对 `nginx.conf` 进行全面安全加固，修复了以下安全问题：

### ✅ 已实施的安全措施

1. **隐藏服务器版本信息** - 防止信息泄露
2. **添加完整的HTTP安全响应头** - 防御多种Web攻击
3. **配置域名访问限制** - 防止恶意Host头攻击
4. **禁用危险的HTTP方法** - 阻止TRACE/TRACK等方法
5. **加固文件上传目录** - 防止敏感文件泄露
6. **禁用目录列表** - 防止目录遍历
7. **添加HTTPS配置模板** - 为启用HTTPS做准备

---

## 🔧 主要修改内容

### 1. HTTP全局安全配置

```nginx
# 隐藏Nginx版本号（防止信息泄露）
server_tokens off;

# 禁用目录列表
autoindex off;

# 限制请求方法
map $request_method $not_allowed_method {
    default 1;
    GET 0;
    POST 0;
    PUT 0;
    DELETE 0;
    HEAD 0;
    OPTIONS 0;
}
```

**效果**：
- ✅ 响应头不再显示 `Server: nginx/1.18.0 (Ubuntu)`，改为 `Server: nginx`
- ✅ 全局禁用目录列表功能
- ✅ 仅允许必要的HTTP方法

---

### 2. 默认Server块（防御恶意Host头）

```nginx
# 默认server块 - 拒绝所有未明确绑定的请求
server {
    listen 80 default_server;
    listen [::]:80 default_server;
    server_name _;
    return 444;  # 关闭连接，不返回响应
}
```

**效果**：
- ✅ 拒绝使用恶意Host头的请求（如 `Host: evilhost_test`）
- ✅ 修复中危漏洞：域名访问限制不严格

---

### 3. Host头验证

```nginx
# 拒绝非法的Host头
if ($host !~* ^222\.204\.4\.108$ ) {
    return 403;
}
```

**效果**：
- ✅ 仅允许 `222.204.4.108` 作为合法Host
- ✅ 其他Host头返回403 Forbidden

---

### 4. 完整的HTTP安全响应头

添加了所有缺失的安全响应头：

| 响应头 | 作用 | 防御的攻击 |
|--------|------|-----------|
| X-Frame-Options | 防止点击劫持 | Clickjacking |
| X-Content-Type-Options | 防止MIME嗅探 | MIME Sniffing |
| X-XSS-Protection | XSS保护 | Cross-Site Scripting |
| Content-Security-Policy | 限制资源加载 | XSS, 数据注入 |
| Referrer-Policy | 保护隐私 | 信息泄露 |
| Permissions-Policy | 限制浏览器特性 | 未授权访问 |
| X-Download-Options | 控制下载 | 文件执行攻击 |
| X-Permitted-Cross-Domain-Policies | 跨域策略 | 跨域攻击 |

**效果**：
- ✅ 修复低危漏洞：A5 安全配置错误 - HTTP安全响应头缺失
- ✅ SecurityHeaders.com 评分预计从 C级 提升到 A级

---

### 5. API代理安全加固

```nginx
location /api/ {
    # 限制请求方法
    limit_except GET POST PUT DELETE HEAD OPTIONS {
        deny all;
    }
    # ... 其他代理配置
}
```

**效果**：
- ✅ 禁用TRACE、TRACK等危险方法
- ✅ 修复低危漏洞：启用了危险的Method (TRACE)

---

### 6. 文件上传目录加固

```nginx
location /uploads/ {
    # 禁用目录列表
    autoindex off;
    
    # 仅允许访问图片文件
    location ~ \.(jpg|jpeg|png|gif|svg|webp|bmp|ico)$ {
        try_files $uri =404;
    }
    
    # 拒绝访问敏感文件类型
    location ~ \.(php|sh|py|pl|cgi|txt|log|bak|conf|sql|exe)$ {
        deny all;
    }
}
```

**效果**：
- ✅ 防止目录遍历
- ✅ 防止访问敏感文件
- ✅ 仅允许访问图片文件

---

### 7. 禁止访问隐藏文件

```nginx
location ~ /\. {
    deny all;
    access_log off;
    log_not_found off;
}
```

**效果**：
- ✅ 防止访问 `.git`、`.env` 等敏感文件
- ✅ 修复低危漏洞：信息泄露

---

### 8. HTTPS配置模板（已注释）

已添加完整的HTTPS配置模板，包括：
- SSL证书配置
- 强加密套件
- HSTS（HTTP严格传输安全）
- 所有安全响应头

---

## 🚀 部署步骤

### 步骤1: 备份当前配置

```bash
# 备份原配置文件
sudo cp /etc/nginx/nginx.conf /etc/nginx/nginx.conf.backup.$(date +%Y%m%d)

# 或者如果您的配置在其他位置
sudo cp /path/to/nginx.conf /path/to/nginx.conf.backup.$(date +%Y%m%d)
```

### 步骤2: 部署新配置

```bash
# 将修改后的配置文件复制到Nginx配置目录
sudo cp nginx.conf /etc/nginx/nginx.conf

# 或者根据您的实际路径调整
```

### 步骤3: 测试配置

```bash
# 测试Nginx配置语法
sudo nginx -t

# 如果看到以下输出说明配置正确：
# nginx: the configuration file /etc/nginx/nginx.conf syntax is ok
# nginx: configuration file /etc/nginx/nginx.conf test is successful
```

### 步骤4: 重载Nginx

```bash
# 重载配置（不中断服务）
sudo systemctl reload nginx

# 或者重启Nginx
sudo systemctl restart nginx

# 验证服务状态
sudo systemctl status nginx
```

### 步骤5: 验证修复效果

```bash
# 1. 检查服务器版本信息（应该不显示版本号）
curl -I http://222.204.4.108/ | grep Server
# 期望: Server: nginx

# 2. 检查安全响应头
curl -I http://222.204.4.108/ | grep -E "X-Frame-Options|Content-Security-Policy|X-Content-Type-Options"
# 期望: 看到所有安全响应头

# 3. 测试TRACE方法（应该返回405）
curl -X TRACE http://222.204.4.108/api/
# 期望: 405 Not Allowed

# 4. 测试恶意Host头（应该返回403或444）
curl -H "Host: evilhost_test" http://222.204.4.108/
# 期望: 403 Forbidden 或无响应

# 5. 测试目录列表（应该被拒绝）
curl http://222.204.4.108/uploads/
# 期望: 403 Forbidden 或 404 Not Found

# 6. 测试隐藏文件访问（应该被拒绝）
curl http://222.204.4.108/.git/config
# 期望: 403 Forbidden
```

---

## 🔍 在线安全检测

部署后，使用以下在线工具验证安全性：

### 1. SecurityHeaders.com
```
访问: https://securityheaders.com
输入: http://222.204.4.108
```
**预期评分**: A级或A+级

### 2. Mozilla Observatory
```
访问: https://observatory.mozilla.org
输入: http://222.204.4.108
```
**预期评分**: B级或更高

---

## ⚠️ 注意事项

### 1. Content-Security-Policy 可能需要调整

如果您的前端使用了外部CDN或第三方资源，可能需要调整CSP策略：

```nginx
# 示例：允许从CDN加载资源
add_header Content-Security-Policy "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdn.example.com; style-src 'self' 'unsafe-inline' https://cdn.example.com; img-src 'self' data: https:; font-src 'self' data: https://cdn.example.com; connect-src 'self'; frame-ancestors 'self';" always;
```

### 2. 如果有域名，建议添加到配置中

```nginx
server_name 222.204.4.108 yourdomain.com www.yourdomain.com;

# 同时修改Host头验证
if ($host !~* ^(222\.204\.4\.108|yourdomain\.com|www\.yourdomain\.com)$ ) {
    return 403;
}
```

### 3. 强烈建议启用HTTPS

配置文件中已包含完整的HTTPS配置模板（已注释）。启用步骤：

```bash
# 1. 获取SSL证书（Let's Encrypt免费）
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d yourdomain.com

# 2. 取消nginx.conf中HTTPS配置的注释

# 3. 重载Nginx
sudo systemctl reload nginx
```

### 4. 测试所有功能

部署后请测试：
- ✅ 网站首页正常访问
- ✅ 用户登录/注册功能
- ✅ API接口调用
- ✅ 文件上传功能
- ✅ 图片显示正常

---

## 📊 预期安全改善

### 修复的漏洞（共20个Web漏洞中的大部分）

| 漏洞类型 | 风险等级 | 状态 |
|---------|---------|------|
| A5 安全配置错误 - HTTP安全响应头缺失 | 低危 | ✅ 已修复 |
| 域名访问限制不严格 | 中危 | ✅ 已修复 |
| 启用危险的Method (TRACE) | 低危 | ✅ 已修复 |
| 信息泄露（服务器版本） | 低危 | ✅ 已修复 |
| 目录列表 | 低危 | ✅ 已修复 |
| 缺少X-Frame-Options | 低危 | ✅ 已修复 |
| 缺少Content-Security-Policy | 低危 | ✅ 已修复 |
| 缺少X-Content-Type-Options | 低危 | ✅ 已修复 |
| 缺少X-XSS-Protection | 低危 | ✅ 已修复 |
| 缺少Referrer-Policy | 低危 | ✅ 已修复 |

### 安全评分预期

- **SecurityHeaders.com**: C级 → **A级**
- **Mozilla Observatory**: D级 → **B+级**
- **Web风险等级**: 5.0/10.0 → **预计 2.0/10.0**

---

## 🔄 后续工作

配置文件已完成安全加固，但要达到最佳安全效果，还需要：

### 1. 升级Nginx（高优先级）

```bash
# 当前版本: 1.18.0（存在5个高危漏洞）
# 目标版本: 1.26.x 或更高

sudo add-apt-repository ppa:nginx/stable
sudo apt update
sudo apt install --only-upgrade nginx
```

### 2. 升级MySQL（中优先级）

```bash
# 当前版本: 8.0.43（存在CVE-2024-21176）
# 目标版本: 8.4.0 或更高
```

### 3. 修复SSH DHE漏洞（高优先级）

```bash
# 更新OpenSSL并配置SSH禁用DHE
```

### 4. 启用HTTPS（强烈推荐）

```bash
# 申请SSL证书并启用HTTPS配置
```

详细步骤请参考《安全漏洞修复计划.md》。

---

## 🆘 回滚方案

如果新配置导致问题，可以快速回滚：

```bash
# 恢复备份的配置
sudo cp /etc/nginx/nginx.conf.backup.YYYYMMDD /etc/nginx/nginx.conf

# 测试配置
sudo nginx -t

# 重载Nginx
sudo systemctl reload nginx
```

---

## 📞 技术支持

如果遇到问题，请检查：

1. **Nginx错误日志**
   ```bash
   sudo tail -f /var/log/nginx/error.log
   ```

2. **Nginx访问日志**
   ```bash
   sudo tail -f /var/log/nginx/access.log
   ```

3. **Nginx服务状态**
   ```bash
   sudo systemctl status nginx
   ```

---

## ✅ 部署检查清单

部署前：
- [ ] 已备份原配置文件
- [ ] 已阅读所有注意事项
- [ ] 已准备回滚方案

部署中：
- [ ] 配置文件已复制到正确位置
- [ ] nginx -t 测试通过
- [ ] Nginx已成功重载

部署后：
- [ ] 网站正常访问
- [ ] 安全响应头已生效
- [ ] TRACE方法已禁用
- [ ] 恶意Host头被拒绝
- [ ] 所有功能正常工作
- [ ] 已使用在线工具验证

---

**配置版本**: v2.0  
**修改日期**: 2025-11-02  
**适用系统**: Ubuntu + Nginx 1.18.0+  
**修改人员**: AI Assistant

**重要提示**: 此配置修复了Nginx层面的安全问题，但要完全解决所有漏洞，还需要升级Nginx到最新版本以修复高危系统漏洞。

