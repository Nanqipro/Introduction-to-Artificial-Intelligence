#!/bin/bash
# 一键启动所有服务

echo "========================================"
echo "AI教材平台 - 一键启动脚本"
echo "========================================"

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 检查必要的工具
check_requirements() {
    echo -e "${YELLOW}检查系统环境...${NC}"
    
    local missing=0
    
    # 检查 Docker
    if ! command -v docker &> /dev/null; then
        echo -e "${RED}✗ Docker 未安装${NC}"
        missing=1
    else
        echo -e "${GREEN}✓ Docker 已安装${NC}"
    fi
    
    # 检查 Node.js
    if ! command -v node &> /dev/null; then
        echo -e "${RED}✗ Node.js 未安装${NC}"
        missing=1
    else
        echo -e "${GREEN}✓ Node.js 已安装: $(node -v)${NC}"
    fi
    
    # 检查 Java
    if ! command -v java &> /dev/null; then
        echo -e "${RED}✗ Java 未安装${NC}"
        missing=1
    else
        echo -e "${GREEN}✓ Java 已安装${NC}"
    fi
    
    # 检查 Maven
    if ! command -v mvn &> /dev/null; then
        echo -e "${RED}✗ Maven 未安装${NC}"
        missing=1
    else
        echo -e "${GREEN}✓ Maven 已安装${NC}"
    fi
    
    if [ $missing -eq 1 ]; then
        echo -e "${RED}请先安装缺失的依赖${NC}"
        exit 1
    fi
}

# 启动 MySQL
start_mysql() {
    echo -e "\n${BLUE}[1/3] 启动 MySQL 数据库...${NC}"
    
    if docker ps | grep -q ai_platform_mysql; then
        echo -e "${GREEN}✓ MySQL 已在运行${NC}"
    else
        docker-compose up -d mysql
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ MySQL 启动成功${NC}"
            echo "等待数据库初始化..."
            sleep 10
        else
            echo -e "${RED}✗ MySQL 启动失败${NC}"
            exit 1
        fi
    fi
    
    # 测试连接
    mysql -h localhost -P 3307 -u root -proot123456 -e "SELECT 1" > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ 数据库连接正常${NC}"
    else
        echo -e "${RED}✗ 数据库连接失败${NC}"
        exit 1
    fi
}

# 启动后端
start_backend() {
    echo -e "\n${BLUE}[2/3] 启动后端服务...${NC}"
    
    # 检查端口
    PID=$(lsof -ti:8082)
    if [ ! -z "$PID" ]; then
        echo -e "${YELLOW}终止占用 8082 端口的进程...${NC}"
        kill -9 $PID
        sleep 2
    fi
    
    cd server
    
    # 编译项目
    if [ ! -f "target/server-0.0.1-SNAPSHOT.jar" ] || [ "$1" == "--rebuild" ]; then
        echo -e "${YELLOW}编译后端项目...${NC}"
        mvn clean package -DskipTests
        if [ $? -ne 0 ]; then
            echo -e "${RED}✗ 后端编译失败${NC}"
            exit 1
        fi
    fi
    
    # 后台启动
    echo -e "${YELLOW}启动后端服务...${NC}"
    nohup java -jar target/server-0.0.1-SNAPSHOT.jar > ../logs/backend.log 2>&1 &
    BACKEND_PID=$!
    echo $BACKEND_PID > ../backend.pid
    
    cd ..
    sleep 5
    
    # 检查是否启动成功
    if curl -s http://localhost:8082/api/chapters/health > /dev/null; then
        echo -e "${GREEN}✓ 后端服务启动成功 (PID: $BACKEND_PID)${NC}"
    else
        echo -e "${RED}✗ 后端服务启动失败${NC}"
        exit 1
    fi
}

# 启动前端
start_frontend() {
    echo -e "\n${BLUE}[3/3] 启动前端服务...${NC}"
    
    # 检查端口
    PID=$(lsof -ti:5173)
    if [ ! -z "$PID" ]; then
        echo -e "${YELLOW}终止占用 5173 端口的进程...${NC}"
        kill -9 $PID
        sleep 2
    fi
    
    cd client
    
    # 安装依赖
    if [ ! -d "node_modules" ]; then
        echo -e "${YELLOW}安装前端依赖...${NC}"
        npm install
        if [ $? -ne 0 ]; then
            echo -e "${RED}✗ 依赖安装失败${NC}"
            exit 1
        fi
    fi
    
    # 后台启动
    echo -e "${YELLOW}启动前端服务...${NC}"
    nohup npm run dev -- --host > ../logs/frontend.log 2>&1 &
    FRONTEND_PID=$!
    echo $FRONTEND_PID > ../frontend.pid
    
    cd ..
    sleep 5
    
    echo -e "${GREEN}✓ 前端服务启动成功 (PID: $FRONTEND_PID)${NC}"
}

# 显示访问信息
show_info() {
    echo -e "\n${GREEN}========================================"
    echo "所有服务启动成功！"
    echo "========================================"
    echo -e "${NC}"
    
    LOCAL_IP=$(hostname -I | awk '{print $1}')
    
    echo "访问地址："
    echo -e "${BLUE}前端页面:${NC}"
    echo "  - 本地: http://localhost:5173"
    echo "  - 局域网: http://$LOCAL_IP:5173"
    echo "  - 外网: http://222.204.4.108 (需要配置端口转发)"
    echo ""
    echo -e "${BLUE}后端API:${NC}"
    echo "  - 健康检查: http://localhost:8082/api/chapters/health"
    echo ""
    echo -e "${BLUE}日志文件:${NC}"
    echo "  - 前端日志: logs/frontend.log"
    echo "  - 后端日志: logs/backend.log"
    echo ""
    echo -e "${YELLOW}停止服务请运行: ./stop-all.sh${NC}"
}

# 主函数
main() {
    # 创建日志目录
    mkdir -p logs
    
    # 检查环境
    check_requirements
    
    # 启动服务
    start_mysql
    start_backend $1
    start_frontend
    
    # 显示信息
    show_info
}

# 捕获退出信号
trap 'echo -e "\n${RED}启动被中断${NC}"; exit 1' INT

# 执行主函数
main $@
