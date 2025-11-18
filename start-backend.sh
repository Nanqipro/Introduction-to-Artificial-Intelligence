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

# 基本数据库参数（Docker MySQL）
DB_HOST=localhost
DB_PORT=3307
DB_USER=root
DB_PASS=root123456
DB_NAME=AI_platform

# 可选：是否重置并重建反馈表（默认 false）。
# 运行时可通过环境变量开启：RESET_FEEDBACK_TABLE=true ./start-backend.sh
RESET_FEEDBACK_TABLE=${RESET_FEEDBACK_TABLE:-false}

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

# 测试数据库连接（Docker 模式）
echo -e "${YELLOW}测试数据库连接(使用 docker exec)...${NC}"
docker exec -i ai_platform_mysql mysql -uroot -proot123456 -e "SELECT 1" > /dev/null 2>&1
if [ $? -eq 0 ]; then
  echo -e "${GREEN}✓ 数据库连接成功${NC}"
else
  echo -e "${RED}✗ 数据库连接失败，请检查 MySQL 容器${NC}"
  exit 1
fi

# 封装执行命令（Docker 模式，指定数据库）
mysql_exec() {
  docker exec -i ai_platform_mysql mysql --default-character-set=utf8mb4 -uroot -proot123456 -D "$DB_NAME" -e "$1"
}

mysql_import_file() {
  # 通过STDIN导入，容器内mysql读取主机文件内容
  docker exec -i ai_platform_mysql mysql --default-character-set=utf8mb4 -uroot -proot123456 -D "$DB_NAME" < "$1"
}

# 修复必要列（幂等，兼容 MySQL 5.7/8.0）
echo -e "${YELLOW}检查并修复 user 表必需列...${NC}"
# 逐列添加，若已存在则忽略错误继续
mysql_exec "ALTER TABLE user ADD COLUMN is_first_login TINYINT(1) DEFAULT 1;" \
  && echo -e "${GREEN}✓ 已添加 is_first_login 列${NC}" \
  || echo -e "${YELLOW}is_first_login 已存在，跳过${NC}"
mysql_exec "ALTER TABLE user ADD COLUMN last_login_time DATETIME NULL;" \
  && echo -e "${GREEN}✓ 已添加 last_login_time 列${NC}" \
  || echo -e "${YELLOW}last_login_time 已存在，跳过${NC}"

# 统一库与表字符集为 utf8mb4，防止中文昵称乱码
echo -e "${YELLOW}统一库/表字符集为 utf8mb4...${NC}"
mysql_exec "ALTER DATABASE $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" || true
mysql_exec "ALTER TABLE user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" || true

# 清空并导入用户相关数据
echo -e "${YELLOW}清空用户相关表并重新导入数据...${NC}"
mysql_exec "SET FOREIGN_KEY_CHECKS=0; TRUNCATE TABLE user_achievement; TRUNCATE TABLE user_learning_record; TRUNCATE TABLE user; SET FOREIGN_KEY_CHECKS=1;" && echo -e "${GREEN}✓ 已清空用户相关表${NC}"

echo -e "${YELLOW}生成导入SQL（学生+管理员）...${NC}"
python3 extract_students.py
if [ $? -ne 0 ]; then
  echo -e "${RED}✗ 生成导入SQL失败${NC}"
  exit 1
fi

echo -e "${YELLOW}导入 insert_students.sql 到 ${DB_NAME}...${NC}"
mysql_import_file insert_students.sql
if [ $? -eq 0 ]; then
  echo -e "${GREEN}✓ 导入成功${NC}"
else
  echo -e "${RED}✗ 导入失败${NC}"
  exit 1
fi

echo -e "${YELLOW}导入后统计...${NC}"
mysql_exec "SELECT COUNT(*) AS total_users, SUM(role='admin') AS admin_count, SUM(role='student') AS student_count FROM user;" || true

# 去重并添加唯一索引（避免重复用户名导致登录异常）
echo -e "${YELLOW}去重重复用户名并添加唯一索引...${NC}"
mysql_exec "DELETE u1 FROM user u1 INNER JOIN user u2 ON u1.username=u2.username AND u1.id < u2.id;" || true
mysql_exec "ALTER TABLE user ADD UNIQUE INDEX uniq_username (username);" || true
mysql_exec "SELECT COUNT(*) AS total_users, (SELECT COUNT(*) FROM user u JOIN (SELECT username FROM user GROUP BY username HAVING COUNT(*)>1) d ON u.username=d.username) AS dup_users FROM user;" || true

# 创建/重建反馈表
if [ "$RESET_FEEDBACK_TABLE" = "true" ]; then
  echo -e "${YELLOW}已启用重置反馈表：删除并按新结构重建 feedback...${NC}"
  mysql_exec "SET FOREIGN_KEY_CHECKS=0; DROP TABLE IF EXISTS feedback; SET FOREIGN_KEY_CHECKS=1;" \
    && echo -e "${GREEN}✓ 已删除旧 feedback 表${NC}" \
    || echo -e "${RED}✗ 删除旧 feedback 表失败${NC}"
fi

echo -e "${YELLOW}确保 feedback 反馈表存在(如不存在则创建)...${NC}"
mysql_exec "CREATE TABLE IF NOT EXISTS feedback (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  content TEXT NOT NULL,
  category VARCHAR(50) DEFAULT '',
  status VARCHAR(20) DEFAULT 'NEW',
  admin_reply TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_user_id (user_id),
  INDEX idx_status (status),
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);" && echo -e "${GREEN}✓ feedback 表已就绪${NC}" || echo -e "${RED}✗ 创建 feedback 表失败${NC}"

# 确保备份管理员账号存在（幂等）
mysql_exec "INSERT INTO user (username,password,nickname,email,role,level,experience,total_score,completed_chapters,quiz_count,correct_answers,create_time,update_time)
SELECT 'goodlabAdmin', MD5('goodlabPwd'), 'GoodLab管理员', 'admin@goodlab.com', 'admin', 1, 0, 0, 0, 0, 0, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM user WHERE username='goodlabAdmin');" || true

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
