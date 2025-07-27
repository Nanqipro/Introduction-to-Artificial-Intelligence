@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM 项目状态检查脚本 (Windows)
REM Project Status Check Script for Windows

title AI Textbook Platform - Status Check

echo.
echo 🎓 AI Textbook Platform - 项目状态检查
echo ============================================
echo.

REM 检查项目结构
echo 📁 检查项目结构...
echo.

set "directories=client server AI-GOODLAB"
for %%d in (%directories%) do (
    if exist "%%d" (
        echo ✅ %%d/ 目录存在
    ) else (
        echo ❌ %%d/ 目录缺失
    )
)

set "files=README.md DEVELOPER_GUIDE.md start.sh start.bat"
for %%f in (%files%) do (
    if exist "%%f" (
        echo ✅ %%f 文件存在
    ) else (
        echo ❌ %%f 文件缺失
    )
)

REM 检查前端文件
if exist "client" (
    set "frontend_files=client\package.json client\vite.config.js client\src\main.js"
    for %%f in (!frontend_files!) do (
        if exist "%%f" (
            echo ✅ %%f 存在
        ) else (
            echo ❌ %%f 缺失
        )
    )
    
    if exist "client\node_modules" (
        echo ✅ client\node_modules 已安装
    ) else (
        echo ⚠️  client\node_modules 未安装，需要运行 npm install
    )
)

REM 检查后端文件
if exist "server" (
    if exist "server\pom.xml" (
        echo ✅ server\pom.xml 存在
    ) else (
        echo ❌ server\pom.xml 缺失
    )
    
    if exist "server\mvnw.cmd" (
        echo ✅ server\mvnw.cmd 存在
    ) else (
        echo ❌ server\mvnw.cmd 缺失
    )
    
    if exist "server\src\main\java" (
        echo ✅ server\src\main\java 存在
    ) else (
        echo ❌ server\src\main\java 缺失
    )
)

echo.
echo 🔧 检查开发环境...
echo.

REM 检查 Node.js
node --version >nul 2>&1
if !errorlevel! equ 0 (
    for /f "tokens=*" %%i in ('node --version') do set "node_version=%%i"
    echo ✅ Node.js: !node_version!
    
    REM 检查版本
    for /f "tokens=1 delims=." %%a in ("!node_version:v=!") do set "node_major=%%a"
    if !node_major! geq 16 (
        echo    版本符合要求 ^(^>= 16.0.0^)
    ) else (
        echo    ⚠️  版本过低，建议升级到 ^>= 16.0.0
    )
) else (
    echo ❌ Node.js: 未安装
)

REM 检查 npm
npm --version >nul 2>&1
if !errorlevel! equ 0 (
    for /f "tokens=*" %%i in ('npm --version') do set "npm_version=%%i"
    echo ✅ npm: !npm_version!
) else (
    echo ❌ npm: 未安装
)

REM 检查 Java
java -version >nul 2>&1
if !errorlevel! equ 0 (
    for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr "version"') do set "java_version=%%i"
    echo ✅ Java: !java_version!
    
    REM 检查版本
    echo !java_version! | findstr /C:"17" /C:"18" /C:"19" /C:"20" /C:"21" >nul
    if !errorlevel! equ 0 (
        echo    版本符合要求 ^(^>= 17^)
    ) else (
        echo    ⚠️  版本可能过低，建议使用 Java 17+
    )
) else (
    echo ❌ Java: 未安装
)

REM 检查 Maven
mvn --version >nul 2>&1
if !errorlevel! equ 0 (
    for /f "tokens=3" %%i in ('mvn --version 2^>^&1 ^| findstr "Apache Maven"') do set "mvn_version=%%i"
    echo ✅ Maven: !mvn_version!
) else (
    echo ℹ️  Maven: 未安装 ^(可使用 Maven Wrapper^)
)

echo.
echo 🔌 检查端口状态...
echo.

REM 检查端口 5173
netstat -an | findstr ":5173" >nul 2>&1
if !errorlevel! equ 0 (
    echo 🔴 端口 5173: 被占用
) else (
    echo 🟢 端口 5173: 可用
)

REM 检查端口 8080
netstat -an | findstr ":8080" >nul 2>&1
if !errorlevel! equ 0 (
    echo 🔴 端口 8080: 被占用
) else (
    echo 🟢 端口 8080: 可用
)

echo.
echo 🔍 检查服务运行状态...
echo.

REM 检查前端服务
curl -s http://localhost:5173 >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ 前端服务: 运行中 ^(http://localhost:5173^)
) else (
    echo ❌ 前端服务: 未运行
)

REM 检查后端服务
curl -s http://localhost:8080 >nul 2>&1
if !errorlevel! equ 0 (
    echo ✅ 后端服务: 运行中 ^(http://localhost:8080^)
) else (
    echo ❌ 后端服务: 未运行
)

echo.
echo 🚀 快速启动命令...
echo.
echo 启动前端:
echo   cd client ^&^& npm install ^&^& npm run dev
echo.
echo 启动后端:
echo   cd server ^&^& mvnw.cmd spring-boot:run
echo.
echo 同时启动:
echo   start.bat
echo.
echo 检查环境:
echo   check-status.bat
echo.
echo ✨ 检查完成！
echo.
pause
exit /b 0
