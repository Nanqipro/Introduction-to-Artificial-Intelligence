Param(
  [string]$ContainerName = "ai_platform_mysql",
  [string]$SqlFile = "scripts/rebuild_db.sql",
  [string]$StudentsSql = "insert_students.sql",
  [string]$RootPassword = "root123456"
)

Write-Host "[DB] 检查 Docker 容器状态..." -ForegroundColor Cyan
$container = docker ps --format "{{.Names}}" | Where-Object { $_ -eq $ContainerName }
if (-not $container) {
  Write-Host "[DB] 容器未运行，尝试启动 mysql 服务..." -ForegroundColor Yellow
  docker compose up -d mysql
  Start-Sleep -Seconds 5
}

if (-not (Test-Path $SqlFile)) {
  Write-Error "[DB] 找不到SQL文件: $SqlFile"
  exit 1
}

Write-Host "[DB] 开始重建数据库，执行: $SqlFile" -ForegroundColor Cyan

# 将本地SQL内容通过管道传入容器内的mysql客户端（使用 sh -c 以保证参数解析一致）
Get-Content -Path $SqlFile -Raw | docker exec -i $ContainerName sh -c "mysql -uroot -p$RootPassword --default-character-set=utf8mb4"

if ($LASTEXITCODE -ne 0) {
  Write-Error "[DB] SQL执行失败，退出码: $LASTEXITCODE"
  exit $LASTEXITCODE
}

# 可选：导入学生账号
if (Test-Path $StudentsSql) {
  Write-Host "[DB] 导入学生账号: $StudentsSql" -ForegroundColor Cyan
  $rawStudents = Get-Content -Path $StudentsSql -Raw
  # 去除显式 id 列并改为 INSERT IGNORE，避免与初始用户主键冲突以及重复导入
  $withoutId = $rawStudents -replace 'INSERT INTO user \(id, username, password, nickname, role\) VALUES \(\d+,', 'INSERT INTO user (username, password, nickname, role) VALUES ('
  $studentsSql = $withoutId -replace 'INSERT INTO user \(', 'INSERT IGNORE INTO user ('
  $studentsContent = "USE AI_platform;" + [Environment]::NewLine + $studentsSql
  $studentsContent | docker exec -i $ContainerName sh -c "mysql -uroot -p$RootPassword --default-character-set=utf8mb4"
  if ($LASTEXITCODE -ne 0) {
    Write-Error "[DB] 学生账号导入失败，退出码: $LASTEXITCODE"
    exit $LASTEXITCODE
  }
} else {
  Write-Host "[DB] 未找到学生导入文件（跳过）：$StudentsSql" -ForegroundColor Yellow
}

Write-Host "[DB] 数据库重建完成，验证用户与学生数量..." -ForegroundColor Green
docker exec $ContainerName sh -c "mysql -uroot -p$RootPassword -e \"USE AI_platform; SELECT COUNT(*) AS user_count FROM user; SELECT COUNT(*) AS student_count FROM user WHERE role='student';\""

Write-Host "[DB] 完成" -ForegroundColor Green