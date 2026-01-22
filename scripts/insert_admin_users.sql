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

-- TCH_001 / TCH_001@Ncu2026
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_001', MD5('TCH_001@Ncu2026'), 'TCH_001', 'TCH_001@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_002 / TCH_002@Ncu2026
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_002', MD5('TCH_002@Ncu2026'), 'TCH_002', 'TCH_002@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_003 / TCH_003@Ncu2026
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_003', MD5('TCH_003@Ncu2026'), 'TCH_003', 'TCH_003@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_004 / TCH_004@Ncu2026
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_004', MD5('TCH_004@Ncu2026'), 'TCH_004', 'TCH_004@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_005 / TCH_005@Ncu2026
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_005', MD5('TCH_005@Ncu2026'), 'TCH_005', 'TCH_005@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_006 / TCH_006@Ncu2026
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_006', MD5('TCH_006@Ncu2026'), 'TCH_006', 'TCH_006@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_007 / TCH_007@Ncu2026
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_007', MD5('TCH_007@Ncu2026'), 'TCH_007', 'TCH_007@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_008 / TCH_008@Ncu2026
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_008', MD5('TCH_008@Ncu2026'), 'TCH_008', 'TCH_008@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_009 / TCH_009@Ncu2026
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_009', MD5('TCH_009@Ncu2026'), 'TCH_009', 'TCH_009@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- TCH_0010 / TCH_0010@Ncu2026
INSERT INTO user (username, password, nickname, email, role, is_first_login, create_time, update_time)
VALUES ('TCH_0010', MD5('TCH_0010@Ncu2026'), 'TCH_0010', 'TCH_0010@example.com', 'admin', 1, @now, @now)
ON DUPLICATE KEY UPDATE role=VALUES(role), password=VALUES(password), nickname=VALUES(nickname), email=VALUES(email), update_time=@now;

-- Optional: ensure admin role label stored as 'admin' for guards
UPDATE user SET role='admin' WHERE username IN ('TCH_qiuruiyun','TCH_xuzichen','TCH_zouhuaxing','TCH_hujiahui','TCH_duanwenying','TCH_yihanxiao','TCH_001','TCH_002','TCH_003','TCH_004','TCH_005','TCH_006','TCH_007','TCH_008','TCH_009','TCH_0010');
