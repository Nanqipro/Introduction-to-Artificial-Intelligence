-- 创建数据库
CREATE DATABASE IF NOT EXISTS AI_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE AI_platform;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建章节表
CREATE TABLE IF NOT EXISTS chapters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    order_num INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建问题表
CREATE TABLE IF NOT EXISTS questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chapter_id BIGINT,
    question_text TEXT NOT NULL,
    question_type VARCHAR(20) DEFAULT 'MULTIPLE_CHOICE',
    options JSON,
    correct_answer VARCHAR(255),
    explanation TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chapter_id) REFERENCES chapters(id)
);

-- 创建用户学习记录表
CREATE TABLE IF NOT EXISTS user_learning_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    chapter_id BIGINT,
    progress INT DEFAULT 0,
    completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (chapter_id) REFERENCES chapters(id)
);

-- 创建用户成就表
CREATE TABLE IF NOT EXISTS user_achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    achievement_type VARCHAR(50),
    achievement_name VARCHAR(100),
    description TEXT,
    earned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 插入一些示例数据
INSERT INTO chapters (title, content, order_num) VALUES 
('人工智能概述', '人工智能（Artificial Intelligence，AI）是计算机科学的一个分支...', 1),
('机器学习基础', '机器学习是人工智能的一个重要分支...', 2),
('深度学习入门', '深度学习是机器学习的一个子领域...', 3),
('计算机视觉', '计算机视觉是人工智能的一个重要应用领域...', 4),
('自然语言处理', '自然语言处理是人工智能的另一个重要应用领域...', 5),
('AI应用案例', '人工智能在各个领域的实际应用案例...', 6),
('实践项目', '动手实践项目，巩固所学知识...', 7);

-- 插入示例问题
INSERT INTO questions (chapter_id, question_text, question_type, options, correct_answer, explanation) VALUES
(1, '人工智能的定义是什么？', 'MULTIPLE_CHOICE', '["让机器像人一样思考", "让机器具备智能行为", "让机器完全替代人类", "让机器拥有情感"]', '让机器具备智能行为', '人工智能是让机器具备智能行为的技术'),
(2, '机器学习的主要类型包括哪些？', 'MULTIPLE_CHOICE', '["监督学习", "无监督学习", "强化学习", "以上都是"]', '以上都是', '机器学习包括监督学习、无监督学习和强化学习三种主要类型');
