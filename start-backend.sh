#!/bin/bash
# 后端启动脚本

echo "========================================"
echo "AI教材平台 - 后端启动脚本"
echo "========================================"

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 检查 Docker MySQL 是否运行
echo -e "${YELLOW}检查 MySQL Docker 容器状态...${NC}"
if docker ps | grep -q ai_platform_mysql; then
    echo -e "${GREEN}✓ MySQL 容器正在运行${NC}"
else
    echo -e "${YELLOW}启动 MySQL 容器...${NC}"
    docker-compose up -d mysql
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ MySQL 容器启动成功${NC}"
        echo "等待数据库初始化..."
        sleep 10
    else
        echo -e "${RED}✗ MySQL 容器启动失败${NC}"
        exit 1
    fi
fi

# 测试数据库连接
echo -e "${YELLOW}测试数据库连接...${NC}"
mysql -h localhost -P 3307 -u root -proot123456 -e "SELECT 1" > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ 数据库连接成功${NC}"
else
    echo -e "${RED}✗ 数据库连接失败，请检查 MySQL 容器${NC}"
    exit 1
fi

# 进入后端目录
cd server

# 检查是否需要编译
if [ ! -f "target/server-0.0.1-SNAPSHOT.jar" ]; then
    echo -e "${YELLOW}未找到编译后的 JAR 文件，开始编译...${NC}"
    mvn clean package -DskipTests
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ 编译成功${NC}"
    else
        echo -e "${RED}✗ 编译失败${NC}"
        exit 1
    fi
fi

# 检查端口是否被占用
PID=$(lsof -ti:8082)
if [ ! -z "$PID" ]; then
    echo -e "${YELLOW}检测到端口 8082 被占用，正在终止进程...${NC}"
    kill -9 $PID
    sleep 2
fi

# 启动后端服务
echo -e "${YELLOW}启动后端服务...${NC}"
echo "API 访问地址："
echo "  - 本地访问: http://localhost:8082"
echo "  - 健康检查: http://localhost:8082/api/chapters/health"
echo ""
echo "按 Ctrl+C 停止服务器"
echo "========================================"

# 启动 Spring Boot 应用
java -jar target/server-0.0.1-SNAPSHOT.jar
