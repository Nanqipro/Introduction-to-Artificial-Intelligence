import re
import hashlib

# 读取Excel文件内容
with open('分课程按上课班级打印成绩登记册.xls', 'r', encoding='utf-8') as file:
    content = file.read()

# 提取学生信息的正则表达式
# 匹配学号和姓名的模式
pattern = r'<td class="xl72" x:str>(\d{10})</td>\s*<td class="xl72" x:str>([^<]+)</td>'

# 查找所有匹配的学生信息
matches = re.findall(pattern, content)

# 生成SQL插入语句
sql_statements = []
for i, (student_id, name) in enumerate(matches, 1):
    # 使用学号@ncu2025作为密码，并进行MD5加密
    password_plain = f"{student_id}@ncu2025"
    password = hashlib.md5(password_plain.encode()).hexdigest()
    
    sql = f"INSERT INTO user (id, username, password, nickname, role) VALUES ({i + 1}, '{student_id}', '{password}', '{name}', 'student');"
    sql_statements.append(sql)

# 将SQL语句写入文件
with open('insert_students.sql', 'w', encoding='utf-8') as sql_file:
    sql_file.write("-- 学生账号批量导入SQL\n")
    sql_file.write("-- 密码为学号@ncu2025的MD5加密\n\n")
    for sql in sql_statements:
        sql_file.write(sql + '\n')

print(f"成功提取 {len(matches)} 个学生账号")
print("学生信息已保存到 insert_students.sql 文件")

# 显示前几个学生信息作为示例
print("\n前10个学生信息:")
for i, (student_id, name) in enumerate(matches[:10], 1):
    print(f"{i}. 学号: {student_id}, 姓名: {name}")