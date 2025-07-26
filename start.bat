@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM 《人工智能概论与应用》数字化教材平台启动脚本 (Windows)
REM AI Textbook Platform Startup Script for Windows

title AI Textbook Platform

echo.
echo 🎓 《人工智能概论与应用》数字化教材平台
echo ================================================
echo.

REM 检查参数
set "action=%~1"
if "%action%"=="" set "action=both"

if "%action%"=="help" goto :show_help
if "%action%"=="-h" goto :show_help
if "%action%"=="--help" goto :show_help
if "%action%"=="check" goto :check_environment
if "%action%"=="frontend" goto :start_frontend
if "%action%"=="fe" goto :start_frontend
if "%action%"=="backend" goto :start_backend
if "%action%"=="be" goto :start_backend
if "%action%"=="both" goto :start_both
if "%action%"=="all" goto :start_both

echo ❌ 错误: 未知选项 '%action%'
goto :show_help

:show_help
echo 用法: %~nx0 [选项]
echo.
echo 选项:
echo   frontend, fe    只启动前端
echo   backend, be     只启动后端
echo   both, all       同时启动前后端 (默认)
echo   check           检查环境
echo   help, -h        显示此帮助信息
echo.
echo 示例:
echo   %~nx0              # 启动前后端
echo   %~nx0 frontend     # 只启动前端
echo   %~nx0 backend      # 只启动后端
echo   %~nx0 check        # 检查环境
echo.
pause
exit /b 0

:check_environment
echo 🔍 检查开发环境...
echo.

REM 检查 Node.js
node --version >nul 2>&1
if !errorlevel! equ 0 (
    for /f "tokens=*" %%i in ('node --version') do set "node_version=%%i"
    echo ✅ Node.js: !node_version!
) else (
    echo ❌ Node.js: 未安装或不在 PATH 中
)

REM 检查 npm
npm --version >nul 2>&1
if !errorlevel! equ 0 (
    for /f "tokens=*" %%i in ('npm --version') do set "npm_version=%%i"
    echo ✅ npm: !npm_version!
) else (
    echo ❌ npm: 未安装或不在 PATH 中
)

REM 检查 Java
java -version >nul 2>&1
if !errorlevel! equ 0 (
    for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr "version"') do set "java_version=%%i"
    echo ✅ Java: !java_version!
) else (
    echo ❌ Java: 未安装或不在 PATH 中
)

REM 检查 Maven
mvn --version >nul 2>&1
if !errorlevel! equ 0 (
    for /f "tokens=3" %%i in ('mvn --version 2^>^&1 ^| findstr "Apache Maven"') do set "mvn_version=%%i"
    echo ✅ Maven: !mvn_version!
) else (
    echo ℹ️  Maven: 未安装 (将使用 Maven Wrapper)
)

echo.
echo 🔍 检查端口状态...

REM 检查端口 5173
netstat -an | findstr ":5173" >nul 2>&1
if !errorlevel! equ 0 (
    echo ⚠️  前端端口 5173 被占用
) else (
    echo ✅ 前端端口 5173 可用
)

REM 检查端口 8080
netstat -an | findstr ":8080" >nul 2>&1
if !errorlevel! equ 0 (
    echo ⚠️  后端端口 8080 被占用
) else (
    echo ✅ 后端端口 8080 可用
)

echo.
pause
exit /b 0

:start_frontend
echo 🚀 启动前端服务...
echo.

if not exist "client" (
    echo ❌ 错误: client 目录不存在
    pause
    exit /b 1
)

cd client

if not exist "node_modules" (
    echo 📦 安装前端依赖...
    npm install
    if !errorlevel! neq 0 (
        echo ❌ 前端依赖安装失败
        pause
        exit /b 1
    )
)

echo ✅ 前端服务启动中...
echo 📱 访问地址: http://localhost:5173
echo.
echo 按 Ctrl+C 停止服务
npm run dev
exit /b 0

:start_backend
echo 🚀 启动后端服务...
echo.

if not exist "server" (
    echo ❌ 错误: server 目录不存在
    pause
    exit /b 1
)

cd server

echo ✅ 后端服务启动中...
echo 🔗 API 地址: http://localhost:8080
echo.
echo 按 Ctrl+C 停止服务

if exist "mvnw.cmd" (
    mvnw.cmd spring-boot:run
) else (
    mvn spring-boot:run
)
exit /b 0

:start_both
echo 🚀 同时启动前后端服务...
echo.

call :check_environment

echo.
echo 📋 启动说明:
echo 1. 后端将在新窗口中启动
echo 2. 前端将在当前窗口启动
echo 3. 关闭窗口即可停止对应服务
echo.

REM 启动后端（新窗口）
echo 🔧 启动后端服务...
if exist "server" (
    if exist "server\mvnw.cmd" (
        start "AI Textbook Backend" cmd /k "cd server && mvnw.cmd spring-boot:run"
    ) else (
        start "AI Textbook Backend" cmd /k "cd server && mvn spring-boot:run"
    )
) else (
    echo ❌ 错误: server 目录不存在
    pause
    exit /b 1
)

REM 等待后端启动
echo ⏳ 等待后端启动...
timeout /t 10 /nobreak >nul

REM 启动前端
echo.
echo 🎨 启动前端服务...
goto :start_frontend

:end
echo.
echo 👋 感谢使用 AI Textbook Platform!
pause
exit /b 0
