-- AI Platform 数据库重建脚本（MySQL 8）
-- 作用：清空并重建数据库结构，初始化管理员与示例数据

DROP DATABASE IF EXISTS AI_platform;
CREATE DATABASE IF NOT EXISTS AI_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE AI_platform;

-- 用户表（合并最新字段）
CREATE TABLE user (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
  username VARCHAR(20) NOT NULL UNIQUE COMMENT '用户名/学号',
  password VARCHAR(32) COMMENT 'MD5密码',
  nickname VARCHAR(50) DEFAULT '' COMMENT '昵称',
  email VARCHAR(128) DEFAULT '' COMMENT '邮箱',
  user_pic VARCHAR(128) DEFAULT '' COMMENT '头像',
  role VARCHAR(20) DEFAULT 'user' COMMENT '用户角色(admin/user/student)',
  level INT DEFAULT 1 COMMENT '用户等级',
  experience INT DEFAULT 0 COMMENT '经验值',
  total_score INT DEFAULT 0 COMMENT '总分数',
  completed_chapters INT DEFAULT 0 COMMENT '已完成章节数',
  quiz_count INT DEFAULT 0 COMMENT '答题次数',
  correct_answers INT DEFAULT 0 COMMENT '正确答案数',
  school VARCHAR(255) DEFAULT '' COMMENT '学校',
  major VARCHAR(255) DEFAULT '' COMMENT '专业',
  study_time INT DEFAULT 0 COMMENT '学习时长(分钟)',
  reset_token VARCHAR(255) COMMENT '密码重置令牌',
  reset_token_expiry DATETIME COMMENT '重置令牌过期时间',
  email_verified BOOLEAN DEFAULT FALSE COMMENT '邮箱是否已验证',
  is_first_login BOOLEAN DEFAULT TRUE COMMENT '是否首次登录',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) COMMENT '用户表';

-- 用户成就表
CREATE TABLE user_achievement (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
  user_id INT UNSIGNED NOT NULL COMMENT '用户ID',
  achievement_type VARCHAR(50) NOT NULL COMMENT '成就类型',
  achievement_name VARCHAR(100) NOT NULL COMMENT '成就名称',
  achievement_desc VARCHAR(255) COMMENT '成就描述',
  earned_date DATETIME NOT NULL COMMENT '获得时间',
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) COMMENT '用户成就表';

-- 用户学习记录表
CREATE TABLE user_learning_record (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
  user_id INT UNSIGNED NOT NULL COMMENT '用户ID',
  chapter_id INT COMMENT '章节ID',
  activity_type VARCHAR(50) NOT NULL COMMENT '活动类型(quiz,chapter,experiment)',
  score INT DEFAULT 0 COMMENT '得分',
  experience_gained INT DEFAULT 0 COMMENT '获得经验值',
  completion_time DATETIME COMMENT '完成时间',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) COMMENT '用户学习记录表';

-- 章节表
CREATE TABLE chapters (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  content TEXT,
  order_num INT DEFAULT 0,
  status VARCHAR(20) DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '章节表';

-- 题目表
CREATE TABLE questions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  chapter_id BIGINT,
  question_text TEXT NOT NULL,
  question_type VARCHAR(20) DEFAULT 'MULTIPLE_CHOICE',
  options JSON,
  correct_answer VARCHAR(255),
  explanation TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (chapter_id) REFERENCES chapters(id)
) COMMENT '题目表';

-- 第一章案例演示答题记录表
CREATE TABLE chapter1_case_study_answers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
  user_id INT UNSIGNED COMMENT '用户ID（可为空，支持匿名答题）',
  question_id VARCHAR(50) NOT NULL COMMENT '题目ID',
  selected_option VARCHAR(10) NOT NULL COMMENT '选择的选项',
  is_correct BOOLEAN NOT NULL COMMENT '是否正确',
  answer_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '答题时间',
  ip_address VARCHAR(45) COMMENT '用户IP地址',
  user_agent TEXT COMMENT '用户代理信息',
  session_id VARCHAR(100) COMMENT '会话ID',
  INDEX idx_question_id (question_id),
  INDEX idx_user_id (user_id),
  INDEX idx_answer_time (answer_time),
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL
) COMMENT '第一章案例演示答题记录表';

-- 第一章案例演示全局统计表
CREATE TABLE chapter1_global_stats (
  id INT AUTO_INCREMENT PRIMARY KEY COMMENT '统计ID',
  total_users INT DEFAULT 0 COMMENT '参与用户总数',
  total_attempts INT DEFAULT 0 COMMENT '总答题次数',
  correct_answers INT DEFAULT 0 COMMENT '正确答案总数',
  accuracy_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT '全局准确率',
  completion_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT '完成率',
  chapter_type VARCHAR(50) DEFAULT 'chapter1_case_study' COMMENT '章节类型',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) COMMENT '第一章案例演示全局统计表';

-- 初始化管理员与测试用户
INSERT INTO user (username, password, nickname, email, role, level, experience, total_score, completed_chapters, quiz_count, correct_answers, school, major, study_time, email_verified, is_first_login, create_time, update_time)
VALUES ('goodlabAdmin', MD5('goodlabPwd'), 'GoodLab管理员', 'admin@goodlab.com', 'admin', 1, 0, 0, 0, 0, 0, '', '', 0, TRUE, FALSE, NOW(), NOW());

INSERT INTO user (username, password, nickname, email, role, level, experience, total_score, completed_chapters, quiz_count, correct_answers, create_time, update_time)
VALUES ('testuser', MD5('123456'), '测试用户', 'test@example.com', 'user', 1, 0, 0, 0, 0, 0, NOW(), NOW());

-- 示例章节与题目
INSERT INTO chapters (title, content, order_num) VALUES 
('人工智能概述', '人工智能（Artificial Intelligence，AI）是计算机科学的一个分支...', 1),
('机器学习基础', '机器学习是人工智能的一个重要分支...', 2),
('深度学习入门', '深度学习是机器学习的一个子领域...', 3);

INSERT INTO questions (chapter_id, question_text, question_type, options, correct_answer, explanation) VALUES
(1, '人工智能的定义是什么？', 'MULTIPLE_CHOICE', JSON_ARRAY('让机器像人一样思考', '让机器具备智能行为', '让机器完全替代人类', '让机器拥有情感'), '让机器具备智能行为', '人工智能是让机器具备智能行为的技术');

-- 可选：验证插入结果
-- SELECT COUNT(*) AS user_count FROM user;
-- SELECT * FROM user LIMIT 5;