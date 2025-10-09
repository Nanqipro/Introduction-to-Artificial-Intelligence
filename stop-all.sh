#!/bin/bash
# 停止所有服务

echo "========================================"
echo "AI教材平台 - 停止服务脚本"
echo "========================================"

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 停止前端
if [ -f "frontend.pid" ]; then
    FRONTEND_PID=$(cat frontend.pid)
    if ps -p $FRONTEND_PID > /dev/null 2>&1; then
        echo -e "${YELLOW}停止前端服务 (PID: $FRONTEND_PID)...${NC}"
        kill $FRONTEND_PID
        rm frontend.pid
        echo -e "${GREEN}✓ 前端服务已停止${NC}"
    else
        echo -e "${YELLOW}前端服务未运行${NC}"
        rm frontend.pid
    fi
else
    # 尝试通过端口查找
    PID=$(lsof -ti:5173)
    if [ ! -z "$PID" ]; then
        echo -e "${YELLOW}停止前端服务 (PID: $PID)...${NC}"
        kill $PID
        echo -e "${GREEN}✓ 前端服务已停止${NC}"
    else
        echo -e "${YELLOW}前端服务未运行${NC}"
    fi
fi

# 停止后端
if [ -f "backend.pid" ]; then
    BACKEND_PID=$(cat backend.pid)
    if ps -p $BACKEND_PID > /dev/null 2>&1; then
        echo -e "${YELLOW}停止后端服务 (PID: $BACKEND_PID)...${NC}"
        kill $BACKEND_PID
        rm backend.pid
        echo -e "${GREEN}✓ 后端服务已停止${NC}"
    else
        echo -e "${YELLOW}后端服务未运行${NC}"
        rm backend.pid
    fi
else
    # 尝试通过端口查找
    PID=$(lsof -ti:8082)
    if [ ! -z "$PID" ]; then
        echo -e "${YELLOW}停止后端服务 (PID: $PID)...${NC}"
        kill $PID
        echo -e "${GREEN}✓ 后端服务已停止${NC}"
    else
        echo -e "${YELLOW}后端服务未运行${NC}"
    fi
fi

# 询问是否停止数据库
read -p "是否停止 MySQL 数据库？(y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}停止 MySQL 容器...${NC}"
    docker-compose down
    echo -e "${GREEN}✓ MySQL 容器已停止${NC}"
fi

echo -e "\n${GREEN}所有服务已停止${NC}"
