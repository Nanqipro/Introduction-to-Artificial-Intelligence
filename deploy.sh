#!/bin/bash
# AI教材平台部署脚本

echo "========================================="
echo "AI教材平台部署脚本"
echo "目标服务器: 222.204.4.108"
echo "========================================="

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查必要的环境
check_requirements() {
    echo -e "${YELLOW}检查系统环境...${NC}"
    
    # 检查 Node.js
    if ! command -v node &> /dev/null; then
        echo -e "${RED}错误: Node.js 未安装${NC}"
        exit 1
    fi
    echo -e "${GREEN}✓ Node.js 已安装: $(node -v)${NC}"
    
    # 检查 Java
    if ! command -v java &> /dev/null; then
        echo -e "${RED}错误: Java 未安装${NC}"
        exit 1
    fi
    echo -e "${GREEN}✓ Java 已安装: $(java -version 2>&1 | head -1)${NC}"
    
    # 检查 Maven
    if ! command -v mvn &> /dev/null; then
        echo -e "${RED}错误: Maven 未安装${NC}"
        exit 1
    fi
    echo -e "${GREEN}✓ Maven 已安装: $(mvn -v | head -1)${NC}"
    
    # 检查 MySQL
    if ! command -v mysql &> /dev/null; then
        echo -e "${RED}错误: MySQL 客户端未安装${NC}"
        exit 1
    fi
    echo -e "${GREEN}✓ MySQL 客户端已安装${NC}"
    
    # 检查 Nginx
    if ! command -v nginx &> /dev/null; then
        echo -e "${RED}错误: Nginx 未安装${NC}"
        exit 1
    fi
    echo -e "${GREEN}✓ Nginx 已安装: $(nginx -v 2>&1)${NC}"
}

# 创建必要的目录
create_directories() {
    echo -e "${YELLOW}创建必要的目录...${NC}"
    
    sudo mkdir -p /var/www/ai-platform/dist
    sudo mkdir -p /var/www/ai-platform/uploads/avatars
    sudo mkdir -p /var/log/ai-platform
    
    echo -e "${GREEN}✓ 目录创建完成${NC}"
}

# 构建前端
build_frontend() {
    echo -e "${YELLOW}构建前端项目...${NC}"
    
    cd client
    npm install
    npm run build
    
    # 复制构建结果到部署目录
    sudo cp -r dist/* /var/www/ai-platform/dist/
    
    echo -e "${GREEN}✓ 前端构建完成${NC}"
    cd ..
}

# 构建后端
build_backend() {
    echo -e "${YELLOW}构建后端项目...${NC}"
    
    cd server
    mvn clean package -DskipTests
    
    echo -e "${GREEN}✓ 后端构建完成${NC}"
    cd ..
}

# 配置数据库
setup_database() {
    echo -e "${YELLOW}配置数据库...${NC}"
    
    read -p "请输入MySQL root密码: " -s mysql_password
    echo
    
    # 创建数据库（如果不存在）
    mysql -u root -p$mysql_password <<EOF
CREATE DATABASE IF NOT EXISTS AI_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EOF
    
    # 导入初始数据
    if [ -f "docs/AI-platform.sql" ]; then
        mysql -u root -p$mysql_password AI_platform < docs/AI-platform.sql
        echo -e "${GREEN}✓ 数据库初始化完成${NC}"
    else
        echo -e "${YELLOW}警告: 未找到初始化SQL文件${NC}"
    fi
}

# 配置 Nginx
setup_nginx() {
    echo -e "${YELLOW}配置 Nginx...${NC}"
    
    # 备份原有配置
    if [ -f "/etc/nginx/nginx.conf" ]; then
        sudo cp /etc/nginx/nginx.conf /etc/nginx/nginx.conf.bak
    fi
    
    # 复制新配置
    sudo cp nginx.conf /etc/nginx/nginx.conf
    
    # 测试配置
    sudo nginx -t
    
    if [ $? -eq 0 ]; then
        sudo systemctl restart nginx
        echo -e "${GREEN}✓ Nginx 配置完成${NC}"
    else
        echo -e "${RED}错误: Nginx 配置有误${NC}"
        exit 1
    fi
}

# 创建系统服务
create_service() {
    echo -e "${YELLOW}创建系统服务...${NC}"
    
    # 创建后端服务文件
    sudo tee /etc/systemd/system/ai-platform-backend.service > /dev/null <<EOF
[Unit]
Description=AI Platform Backend Service
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/var/www/ai-platform/server
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod target/server-0.0.1-SNAPSHOT.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF
    
    # 复制后端JAR文件
    sudo mkdir -p /var/www/ai-platform/server/target
    sudo cp server/target/server-0.0.1-SNAPSHOT.jar /var/www/ai-platform/server/target/
    
    # 重载systemd并启动服务
    sudo systemctl daemon-reload
    sudo systemctl enable ai-platform-backend.service
    sudo systemctl start ai-platform-backend.service
    
    echo -e "${GREEN}✓ 系统服务创建完成${NC}"
}

# 设置权限
set_permissions() {
    echo -e "${YELLOW}设置文件权限...${NC}"
    
    sudo chown -R www-data:www-data /var/www/ai-platform
    sudo chmod -R 755 /var/www/ai-platform
    sudo chmod -R 777 /var/www/ai-platform/uploads
    
    echo -e "${GREEN}✓ 权限设置完成${NC}"
}

# 检查服务状态
check_services() {
    echo -e "${YELLOW}检查服务状态...${NC}"
    
    # 检查后端服务
    if systemctl is-active --quiet ai-platform-backend.service; then
        echo -e "${GREEN}✓ 后端服务运行正常${NC}"
    else
        echo -e "${RED}✗ 后端服务未运行${NC}"
    fi
    
    # 检查Nginx
    if systemctl is-active --quiet nginx; then
        echo -e "${GREEN}✓ Nginx 运行正常${NC}"
    else
        echo -e "${RED}✗ Nginx 未运行${NC}"
    fi
    
    # 检查MySQL
    if systemctl is-active --quiet mysql; then
        echo -e "${GREEN}✓ MySQL 运行正常${NC}"
    else
        echo -e "${RED}✗ MySQL 未运行${NC}"
    fi
}

# 主函数
main() {
    echo "开始部署..."
    
    check_requirements
    create_directories
    build_frontend
    build_backend
    setup_database
    setup_nginx
    create_service
    set_permissions
    check_services
    
    echo -e "${GREEN}"
    echo "========================================="
    echo "部署完成！"
    echo "访问地址: http://222.204.4.108"
    echo "========================================="
    echo -e "${NC}"
}

# 执行主函数
main
