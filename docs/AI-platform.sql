-- 创建数据库
CREATE DATABASE IF NOT EXISTS AI_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE AI_platform;

-- 用户表
create table user (
                      id int unsigned primary key auto_increment comment 'ID',
                      username varchar(20) not null unique comment '用户名',
                      password varchar(32)  comment '密码',
                      nickname varchar(10)  default '' comment '昵称',
                      email varchar(128) default '' comment '邮箱',
                      user_pic varchar(128) default '' comment '头像',
                      role varchar(20) default 'user' comment '用户角色(admin/user)',
                      level int default 1 comment '用户等级',
                      experience int default 0 comment '经验值',
                      total_score int default 0 comment '总分数',
                      completed_chapters int default 0 comment '已完成章节数',
                      quiz_count int default 0 comment '答题次数',
                      correct_answers int default 0 comment '正确答案数',
                      create_time datetime not null comment '创建时间',
                      update_time datetime not null comment '修改时间'
) comment '用户表';

-- 用户成就表
create table user_achievement (
                                   id int unsigned primary key auto_increment comment 'ID',
                                   user_id int unsigned not null comment '用户ID',
                                   achievement_type varchar(50) not null comment '成就类型',
                                   achievement_name varchar(100) not null comment '成就名称',
                                   achievement_desc varchar(255) comment '成就描述',
                                   earned_date datetime not null comment '获得时间',
                                   foreign key (user_id) references user(id) on delete cascade
) comment '用户成就表';

-- 用户学习记录表
create table user_learning_record (
                                       id int unsigned primary key auto_increment comment 'ID',
                                       user_id int unsigned not null comment '用户ID',
                                       chapter_id int comment '章节ID',
                                       activity_type varchar(50) not null comment '活动类型(quiz,chapter,experiment)',
                                       score int default 0 comment '得分',
                                       experience_gained int default 0 comment '获得经验值',
                                       completion_time datetime comment '完成时间',
                                       create_time datetime not null comment '创建时间',
                                       foreign key (user_id) references user(id) on delete cascade
) comment '用户学习记录表';

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

-- 插入管理员用户
INSERT INTO user (username, password, nickname, email, role, level, experience, total_score, completed_chapters, quiz_count, correct_answers, create_time, update_time) 
VALUES ('goodlabAdmin', MD5('goodlabPwd'), 'GoodLab管理员', 'admin@goodlab.com', 'admin', 1, 0, 0, 0, 0, 0, NOW(), NOW());

-- 插入测试用户
INSERT INTO user (username, password, nickname, email, role, level, experience, total_score, completed_chapters, quiz_count, correct_answers, create_time, update_time) 
VALUES ('test', MD5('test'), 'TestUser', 'test@example.com', 'user', 1, 0, 0, 0, 0, 0, NOW(), NOW());

-- 为现有用户添加角色字段（如果表已存在）
ALTER TABLE user ADD COLUMN IF NOT EXISTS role varchar(20) default 'user' comment '用户角色(admin/user)' AFTER user_pic;

-- 更新现有管理员账号
UPDATE user SET role = 'admin' WHERE username = 'goodlabAdmin';
UPDATE user SET username = 'goodlabAdmin', password = MD5('goodlabPwd'), nickname = 'GoodLab管理员', role = 'admin' WHERE username = 'admin';

-- 插入章节示例数据
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

-- 第一章案例演示答题记录表
CREATE TABLE IF NOT EXISTS chapter1_case_study_answers (
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
CREATE TABLE IF NOT EXISTS chapter1_global_stats (
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

-- 初始化全局统计数据
INSERT INTO chapter1_global_stats (total_users, total_attempts, correct_answers, accuracy_rate, completion_rate, chapter_type) 
VALUES (0, 0, 0, 0.00, 0.00, 'chapter1_case_study');

-- 验证插入结果
SELECT * FROM user;
