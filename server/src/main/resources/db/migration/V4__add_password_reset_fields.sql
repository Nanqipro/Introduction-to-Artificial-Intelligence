-- 添加密码重置相关字段
ALTER TABLE user ADD COLUMN reset_token VARCHAR(255) COMMENT '密码重置令牌';
ALTER TABLE user ADD COLUMN reset_token_expiry DATETIME COMMENT '重置令牌过期时间';
ALTER TABLE user ADD COLUMN email_verified BOOLEAN DEFAULT FALSE COMMENT '邮箱是否已验证';