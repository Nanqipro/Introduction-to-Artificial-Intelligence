-- Insert or update administrator accounts
USE AI_platform;

-- Helper: now timestamps
SET @now := NOW();

-- Admin accounts (username, plain password shown for reference)
-- TCH_qiuruiyun / qiuruiyun@Ncu2025
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_qiuruiyun', MD5('qiuruiyun@Ncu2025'), 'qiuruiyun', 'TCH_qiuruiyun@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_xuzichen / xuzichen@Ncu2025
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_xuzichen', MD5('xuzichen@Ncu2025'), 'xuzichen', 'TCH_xuzichen@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_zouhuaxing / zouhuaxing@Ncu2025
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_zouhuaxing', MD5('zouhuaxing@Ncu2025'), 'zouhuaxing', 'TCH_zouhuaxing@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_hujiahui / hujiahui@Ncu2025
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_hujiahui', MD5('hujiahui@Ncu2025'), 'hujiahui', 'TCH_hujiahui@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_duanwenying / duanwenying@Ncu2025
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_duanwenying', MD5('duanwenying@Ncu2025'), 'duanwenying', 'TCH_duanwenying@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_yihanxiao / yihanxiao@Ncu2025
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_yihanxiao', MD5('yihanxiao@Ncu2025'), 'yihanxiao', 'TCH_yihanxiao@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- Optional: ensure admin role label stored as 'admin' for guards
UPDATE user SET role='admin' WHERE username IN ('TCH_qiuruiyun','TCH_xuzichen','TCH_zouhuaxing','TCH_hujiahui','TCH_duanwenying','TCH_yihanxiao');