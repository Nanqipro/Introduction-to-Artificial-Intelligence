-- 添加首次登录标识字段
ALTER TABLE user ADD COLUMN is_first_login BOOLEAN DEFAULT TRUE COMMENT '是否首次登录';