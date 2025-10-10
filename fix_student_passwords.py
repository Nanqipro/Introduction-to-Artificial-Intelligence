import hashlib
import mysql.connector
import subprocess
import json

def get_all_students():
    """获取所有学生账号信息"""
    cmd = [
        'docker', 'exec', '-i', 'ai_platform_mysql', 
        'mysql', '-u', 'root', '-proot123456', 'AI_platform',
        '-e', "SELECT username FROM user WHERE role='student';"
    ]
    
    result = subprocess.run(cmd, capture_output=True, text=True, encoding='utf-8')
    
    if result.returncode != 0:
        print(f"错误: {result.stderr}")
        return []
    
    # 解析输出，跳过表头
    lines = result.stdout.strip().split('\n')
    students = []
    for line in lines[1:]:  # 跳过表头
        if line.strip() and not line.startswith('+') and not line.startswith('|'):
            username = line.strip()
            if username and username != 'username':
                students.append(username)
    
    return students

def generate_password_update_sql():
    """生成批量更新密码的SQL语句"""
    students = get_all_students()
    
    if not students:
        print("未找到学生账号")
        return
    
    print(f"找到 {len(students)} 个学生账号")
    
    # 生成SQL更新语句
    sql_statements = []
    for username in students:
        # 新密码格式: 学号@ncu2025
        new_password = f"{username}@ncu2025"
        # MD5加密
        password_hash = hashlib.md5(new_password.encode()).hexdigest()
        
        sql = f"UPDATE user SET password = '{password_hash}' WHERE username = '{username}' AND role = 'student';"
        sql_statements.append(sql)
    
    # 写入SQL文件
    with open('update_student_passwords.sql', 'w', encoding='utf-8') as f:
        f.write("-- 批量更新学生密码为 学号@ncu2025 格式\n")
        f.write("-- 所有密码都经过MD5加密\n\n")
        for sql in sql_statements:
            f.write(sql + '\n')
    
    print(f"已生成 {len(sql_statements)} 条更新语句")
    print("SQL文件已保存为: update_student_passwords.sql")
    
    # 显示前几个示例
    print("\n前5个密码更新示例:")
    for i, username in enumerate(students[:5]):
        new_password = f"{username}@ncu2025"
        password_hash = hashlib.md5(new_password.encode()).hexdigest()
        print(f"{i+1}. 学号: {username}, 新密码: {new_password}, MD5: {password_hash}")

if __name__ == "__main__":
    generate_password_update_sql()