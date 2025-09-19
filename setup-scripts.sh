#!/bin/bash
# 设置启动脚本权限

echo "设置启动脚本权限..."

chmod +x start-frontend.sh
chmod +x start-backend.sh
chmod +x start-all.sh
chmod +x stop-all.sh

echo "所有脚本权限设置完成！"
echo ""
echo "使用方法："
echo "  启动前端: ./start-frontend.sh"
echo "  启动后端: ./start-backend.sh"
echo "  启动所有: ./start-all.sh"
echo "  停止所有: ./stop-all.sh"
