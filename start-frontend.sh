#!/bin/bash
# 前端启动脚本

echo "========================================"
echo "AI教材平台 - 前端启动脚本"
echo "========================================"

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 进入前端目录
cd client

# 检查 node_modules 是否存在
if [ ! -d "node_modules" ]; then
    echo -e "${YELLOW}检测到 node_modules 不存在，正在安装依赖...${NC}"
    npm install
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ 依赖安装成功${NC}"
    else
        echo -e "${RED}✗ 依赖安装失败${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}✓ 依赖已安装${NC}"
fi

# 检查是否有 npm 进程在运行
PID=$(lsof -ti:5173)
if [ ! -z "$PID" ]; then
    echo -e "${YELLOW}检测到端口 5173 被占用，正在终止进程...${NC}"
    kill -9 $PID
    sleep 2
fi

# 启动开发服务器
echo -e "${YELLOW}启动前端开发服务器...${NC}"
echo "访问地址："
echo "  - 本地访问: http://localhost:5173"
echo "  - 局域网访问: http://$(hostname -I | awk '{print $1}'):5173"
echo ""
echo "按 Ctrl+C 停止服务器"
echo "========================================"

# 启动服务
npm run dev -- --host
