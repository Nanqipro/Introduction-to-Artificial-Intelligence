#!/bin/bash

# 项目状态检查脚本
# Project Status Check Script

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_message() {
    echo -e "${2}${1}${NC}"
}

# 检查服务状态
check_service_status() {
    print_message "🔍 检查服务运行状态..." $BLUE
    echo
    
    # 检查前端服务
    if curl -s http://localhost:5173 > /dev/null 2>&1; then
        print_message "✅ 前端服务: 运行中 (http://localhost:5173)" $GREEN
    else
        print_message "❌ 前端服务: 未运行" $RED
    fi
    
    # 检查后端服务
    if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
        print_message "✅ 后端服务: 运行中 (http://localhost:8080)" $GREEN
    elif curl -s http://localhost:8080 > /dev/null 2>&1; then
        print_message "✅ 后端服务: 运行中 (http://localhost:8080)" $GREEN
    else
        print_message "❌ 后端服务: 未运行" $RED
    fi
}

# 检查项目结构
check_project_structure() {
    print_message "📁 检查项目结构..." $BLUE
    echo
    
    # 检查主要目录
    directories=("client" "server" "AI-GOODLAB")
    for dir in "${directories[@]}"; do
        if [ -d "$dir" ]; then
            print_message "✅ $dir/ 目录存在" $GREEN
        else
            print_message "❌ $dir/ 目录缺失" $RED
        fi
    done
    
    # 检查重要文件
    files=("README.md" "DEVELOPER_GUIDE.md" "start.sh" "start.bat")
    for file in "${files[@]}"; do
        if [ -f "$file" ]; then
            print_message "✅ $file 文件存在" $GREEN
        else
            print_message "❌ $file 文件缺失" $RED
        fi
    done
    
    # 检查前端关键文件
    if [ -d "client" ]; then
        frontend_files=("client/package.json" "client/vite.config.js" "client/src/main.js")
        for file in "${frontend_files[@]}"; do
            if [ -f "$file" ]; then
                print_message "✅ $file 存在" $GREEN
            else
                print_message "❌ $file 缺失" $RED
            fi
        done
        
        # 检查 node_modules
        if [ -d "client/node_modules" ]; then
            print_message "✅ client/node_modules 已安装" $GREEN
        else
            print_message "⚠️  client/node_modules 未安装，需要运行 npm install" $YELLOW
        fi
    fi
    
    # 检查后端关键文件
    if [ -d "server" ]; then
        backend_files=("server/pom.xml" "server/mvnw" "server/src/main/java")
        for file in "${backend_files[@]}"; do
            if [ -e "$file" ]; then
                print_message "✅ $file 存在" $GREEN
            else
                print_message "❌ $file 缺失" $RED
            fi
        done
    fi
}

# 检查依赖和环境
check_dependencies() {
    print_message "🔧 检查开发环境..." $BLUE
    echo
    
    # 检查 Node.js
    if command -v node &> /dev/null; then
        NODE_VERSION=$(node --version)
        print_message "✅ Node.js: $NODE_VERSION" $GREEN
        
        # 检查版本是否符合要求
        NODE_MAJOR=$(echo $NODE_VERSION | cut -d'.' -f1 | sed 's/v//')
        if [ "$NODE_MAJOR" -ge 16 ]; then
            print_message "   版本符合要求 (>= 16.0.0)" $GREEN
        else
            print_message "   ⚠️  版本过低，建议升级到 >= 16.0.0" $YELLOW
        fi
    else
        print_message "❌ Node.js: 未安装" $RED
    fi
    
    # 检查 npm
    if command -v npm &> /dev/null; then
        NPM_VERSION=$(npm --version)
        print_message "✅ npm: $NPM_VERSION" $GREEN
    else
        print_message "❌ npm: 未安装" $RED
    fi
    
    # 检查 Java
    if command -v java &> /dev/null; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1)
        print_message "✅ Java: $JAVA_VERSION" $GREEN
        
        # 检查版本是否符合要求
        if java -version 2>&1 | grep -q "17\|18\|19\|20\|21"; then
            print_message "   版本符合要求 (>= 17)" $GREEN
        else
            print_message "   ⚠️  版本可能过低，建议使用 Java 17+" $YELLOW
        fi
    else
        print_message "❌ Java: 未安装" $RED
    fi
    
    # 检查 Maven
    if command -v mvn &> /dev/null; then
        MVN_VERSION=$(mvn --version | head -n 1)
        print_message "✅ Maven: $MVN_VERSION" $GREEN
    else
        print_message "ℹ️  Maven: 未安装 (可使用 Maven Wrapper)" $BLUE
    fi
}

# 检查端口占用
check_ports() {
    print_message "🔌 检查端口状态..." $BLUE
    echo
    
    ports=(5173 8080)
    for port in "${ports[@]}"; do
        if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
            PID=$(lsof -Pi :$port -sTCP:LISTEN -t)
            PROCESS=$(ps -p $PID -o comm= 2>/dev/null || echo "未知进程")
            print_message "🔴 端口 $port: 被占用 (PID: $PID, 进程: $PROCESS)" $RED
        else
            print_message "🟢 端口 $port: 可用" $GREEN
        fi
    done
}

# 显示快速启动命令
show_quick_commands() {
    print_message "🚀 快速启动命令..." $PURPLE
    echo
    print_message "启动前端:" $BLUE
    echo "  cd client && npm install && npm run dev"
    echo
    print_message "启动后端:" $BLUE
    echo "  cd server && ./mvnw spring-boot:run"
    echo
    print_message "同时启动:" $BLUE
    echo "  ./start.sh"
    echo
    print_message "检查环境:" $BLUE
    echo "  ./check-status.sh"
}

# 主函数
main() {
    print_message "🎓 AI Textbook Platform - 项目状态检查" $PURPLE
    print_message "============================================" $PURPLE
    echo
    
    check_project_structure
    echo
    check_dependencies
    echo
    check_ports
    echo
    check_service_status
    echo
    show_quick_commands
    echo
    print_message "✨ 检查完成！" $GREEN
}

# 执行主函数
main "$@"
