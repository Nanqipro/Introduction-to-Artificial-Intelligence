-- 添加学习时长字段到用户表
ALTER TABLE user ADD COLUMN study_time INT DEFAULT 0 COMMENT '学习时长(分钟)';