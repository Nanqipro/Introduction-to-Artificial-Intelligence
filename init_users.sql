-- 初始化用户数据
USE AI_platform;

-- 插入管理员用户
INSERT INTO user (username, password, nickname, email, level, experience, total_score, completed_chapters, quiz_count, correct_answers, create_time, update_time) 
VALUES ('admin', MD5('admin'), 'Administrator', 'admin@example.com', 1, 0, 0, 0, 0, 0, NOW(), NOW());

-- 插入测试用户
INSERT INTO user (username, password, nickname, email, level, experience, total_score, completed_chapters, quiz_count, correct_answers, create_time, update_time) 
VALUES ('test', MD5('test'), 'Test User', 'test@example.com', 1, 0, 0, 0, 0, 0, NOW(), NOW());

-- 验证插入结果
SELECT * FROM user;
