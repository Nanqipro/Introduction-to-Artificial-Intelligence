#!/bin/bash

# 《人工智能概论与应用》数字化教材平台启动脚本
# AI Textbook Platform Startup Script

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_message() {
    echo -e "${2}${1}${NC}"
}

# 检查命令是否存在
check_command() {
    if ! command -v $1 &> /dev/null; then
        print_message "错误: $1 未安装或不在 PATH 中" $RED
        exit 1
    fi
}

# 检查端口是否被占用
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null 2>&1; then
        print_message "警告: 端口 $1 已被占用" $YELLOW
        return 1
    fi
    return 0
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  frontend, fe    只启动前端"
    echo "  backend, be     只启动后端"
    echo "  both, all       同时启动前后端 (默认)"
    echo "  check           检查环境"
    echo "  help, -h        显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0              # 启动前后端"
    echo "  $0 frontend     # 只启动前端"
    echo "  $0 backend      # 只启动后端"
    echo "  $0 check        # 检查环境"
}

# 检查环境
check_environment() {
    print_message "🔍 检查开发环境..." $BLUE
    
    # 检查 Node.js
    if check_command "node"; then
        NODE_VERSION=$(node --version)
        print_message "✅ Node.js: $NODE_VERSION" $GREEN
    fi
    
    # 检查 npm
    if check_command "npm"; then
        NPM_VERSION=$(npm --version)
        print_message "✅ npm: $NPM_VERSION" $GREEN
    fi
    
    # 检查 Java
    if check_command "java"; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1)
        print_message "✅ Java: $JAVA_VERSION" $GREEN
    fi
    
    # 检查 Maven
    if command -v mvn &> /dev/null; then
        MVN_VERSION=$(mvn --version | head -n 1)
        print_message "✅ Maven: $MVN_VERSION" $GREEN
    else
        print_message "ℹ️  Maven: 未安装 (将使用 Maven Wrapper)" $YELLOW
    fi
    
    # 检查端口
    print_message "\n🔍 检查端口状态..." $BLUE
    if check_port 5173; then
        print_message "✅ 前端端口 5173 可用" $GREEN
    else
        print_message "⚠️  前端端口 5173 被占用" $YELLOW
    fi
    
    if check_port 8080; then
        print_message "✅ 后端端口 8080 可用" $GREEN
    else
        print_message "⚠️  后端端口 8080 被占用" $YELLOW
    fi
}

# 启动前端
start_frontend() {
    print_message "🚀 启动前端服务..." $BLUE
    
    if [ ! -d "client" ]; then
        print_message "错误: client 目录不存在" $RED
        exit 1
    fi
    
    cd client
    
    # 检查是否已安装依赖
    if [ ! -d "node_modules" ]; then
        print_message "📦 安装前端依赖..." $YELLOW
        npm install
    fi
    
    print_message "✅ 前端服务启动中..." $GREEN
    print_message "📱 访问地址: http://localhost:5173" $BLUE
    npm run dev
}

# 启动后端
start_backend() {
    print_message "🚀 启动后端服务..." $BLUE
    
    if [ ! -d "server" ]; then
        print_message "错误: server 目录不存在" $RED
        exit 1
    fi
    
    cd server
    
    print_message "✅ 后端服务启动中..." $GREEN
    print_message "🔗 API 地址: http://localhost:8080" $BLUE
    
    # 使用 Maven Wrapper
    if [ -f "./mvnw" ]; then
        ./mvnw spring-boot:run
    else
        mvn spring-boot:run
    fi
}

# 同时启动前后端
start_both() {
    print_message "🚀 同时启动前后端服务..." $BLUE
    
    # 检查环境
    check_environment
    
    print_message "\n📋 启动说明:" $YELLOW
    print_message "1. 后端将在后台启动" $YELLOW
    print_message "2. 前端将在前台启动" $YELLOW
    print_message "3. 按 Ctrl+C 停止前端，然后手动停止后端" $YELLOW
    
    # 启动后端（后台）
    print_message "\n🔧 启动后端服务..." $BLUE
    cd server
    if [ -f "./mvnw" ]; then
        nohup ./mvnw spring-boot:run > ../backend.log 2>&1 &
    else
        nohup mvn spring-boot:run > ../backend.log 2>&1 &
    fi
    BACKEND_PID=$!
    echo $BACKEND_PID > ../backend.pid
    cd ..
    
    # 等待后端启动
    print_message "⏳ 等待后端启动..." $YELLOW
    sleep 10
    
    # 检查后端是否启动成功
    if check_port 8080; then
        print_message "❌ 后端启动失败，请检查日志: backend.log" $RED
        exit 1
    else
        print_message "✅ 后端启动成功" $GREEN
    fi
    
    # 启动前端
    print_message "\n🎨 启动前端服务..." $BLUE
    start_frontend
}

# 主函数
main() {
    print_message "🎓 《人工智能概论与应用》数字化教材平台" $BLUE
    print_message "================================================" $BLUE
    
    case "${1:-both}" in
        "frontend"|"fe")
            start_frontend
            ;;
        "backend"|"be")
            start_backend
            ;;
        "both"|"all"|"")
            start_both
            ;;
        "check")
            check_environment
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            print_message "错误: 未知选项 '$1'" $RED
            show_help
            exit 1
            ;;
    esac
}

# 捕获 Ctrl+C 信号
cleanup() {
    print_message "\n🛑 正在停止服务..." $YELLOW
    if [ -f "backend.pid" ]; then
        BACKEND_PID=$(cat backend.pid)
        kill $BACKEND_PID 2>/dev/null || true
        rm -f backend.pid
        print_message "✅ 后端服务已停止" $GREEN
    fi
    exit 0
}

trap cleanup SIGINT SIGTERM

# 执行主函数
main "$@"
