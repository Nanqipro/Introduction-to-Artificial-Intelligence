import re
import hashlib

# 读取Excel文件内容
with open('分课程按上课班级打印成绩登记册.xls', 'r', encoding='utf-8') as file:
    content = file.read()

# 提取学生信息（学号、姓名）
pattern = r'<td class="xl72" x:str>(\d{10})</td>\s*<td class="xl72" x:str>([^<]+)</td>'
matches = re.findall(pattern, content)

# 管理员账号（用户名、昵称、明文密码）
admins = [
    ("TCH_qiuruiyun", "qiuruiyun", "qiuruiyun@Ncu2025"),
    ("TCH_xuzichen", "xuzichen", "xuzichen@Ncu2025"),
    ("TCH_zouhuaxing", "zouhuaxing", "zouhuaxing@Ncu2025"),
    ("TCH_hujiahui", "hujiahui", "hujiahui@Ncu2025"),
    ("TCH_duanwenying", "duanwenying", "duanwenying@Ncu2025"),
    ("TCH_yihanxiao", "yihanxiao", "yihanxiao@Ncu2025"),
]

sql_statements = []

# 生成学生插入语句（不写入id，避免与现有自增冲突）
for student_id, name in matches:
    password_plain = f"{student_id}@ncu2025"
    password_md5 = hashlib.md5(password_plain.encode()).hexdigest()
    sql = (
        "INSERT INTO user (username, password, nickname, role, is_first_login, create_time, update_time) "
        f"VALUES ('{student_id}', '{password_md5}', '{name}', 'student', 1, NOW(), NOW()) "
        "ON DUPLICATE KEY UPDATE password=VALUES(password), nickname=VALUES(nickname), role='student', update_time=NOW();"
    )
    sql_statements.append(sql)

# 生成管理员插入语句
for username, nickname, plain_pwd in admins:
    password_md5 = hashlib.md5(plain_pwd.encode()).hexdigest()
    sql = (
        "INSERT INTO user (username, password, nickname, role, is_first_login, create_time, update_time) "
        f"VALUES ('{username}', '{password_md5}', '{nickname}', 'admin', 1, NOW(), NOW()) "
        "ON DUPLICATE KEY UPDATE role='admin', password=VALUES(password), nickname=VALUES(nickname), update_time=NOW();"
    )
    sql_statements.append(sql)

# 写入SQL文件
with open('insert_students.sql', 'w', encoding='utf-8') as sql_file:
    sql_file.write("USE AI_platform;\n")
    sql_file.write("SET NAMES utf8mb4;\n\n")
    sql_file.write("-- 学生与管理员账号批量导入SQL\n")
    sql_file.write("-- 学生密码: 学号@ncu2025；管理员密码: 名字@Ncu2025\n\n")
    for sql in sql_statements:
        sql_file.write(sql + "\n")

print(f"成功提取 {len(matches)} 个学生账号，并合并 {len(admins)} 个管理员账号")
print("账号导入SQL已写入 insert_students.sql 文件")

print("\n示例前5个学生:")
for i, (student_id, name) in enumerate(matches[:5], 1):
    print(f"{i}. 学号: {student_id}, 姓名: {name}")