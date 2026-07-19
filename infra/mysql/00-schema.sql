SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS user (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(32) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(50) DEFAULT '',
  nickname VARCHAR(50) DEFAULT '',
  email VARCHAR(128) DEFAULT '',
  phone VARCHAR(32) DEFAULT '',
  gender VARCHAR(20) DEFAULT '',
  role VARCHAR(20) NOT NULL DEFAULT 'student',
  birthday DATE NULL,
  location VARCHAR(255) DEFAULT '',
  bio VARCHAR(500) DEFAULT '',
  student_id VARCHAR(32) DEFAULT '',
  class_name VARCHAR(100) DEFAULT '',
  major VARCHAR(255) DEFAULT '',
  school VARCHAR(255) DEFAULT '',
  college VARCHAR(255) DEFAULT '',
  grade INT NULL,
  user_pic VARCHAR(500) DEFAULT '',
  avatar VARCHAR(500) DEFAULT '',
  is_active BOOLEAN DEFAULT TRUE,
  level INT DEFAULT 1,
  experience INT DEFAULT 0,
  total_score INT DEFAULT 0,
  completed_chapters INT DEFAULT 0,
  quiz_count INT DEFAULT 0,
  correct_answers INT DEFAULT 0,
  study_time INT DEFAULT 0,
  reset_token VARCHAR(255) NULL,
  reset_token_expiry DATETIME NULL,
  email_verified BOOLEAN DEFAULT FALSE,
  is_first_login BOOLEAN DEFAULT TRUE,
  last_login_time DATETIME NULL,
  created_by VARCHAR(100) DEFAULT '',
  updated_by VARCHAR(100) DEFAULT '',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_user_role (role),
  INDEX idx_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS feedback (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id INT UNSIGNED NOT NULL,
  content TEXT NOT NULL,
  category VARCHAR(50) DEFAULT '',
  status VARCHAR(20) DEFAULT 'NEW',
  admin_reply TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_feedback_user_id (user_id),
  INDEX idx_feedback_status (status),
  CONSTRAINT fk_feedback_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS user_achievement (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  user_id INT UNSIGNED NOT NULL,
  achievement_type VARCHAR(50) NOT NULL,
  achievement_name VARCHAR(100) NOT NULL,
  achievement_desc VARCHAR(255),
  earned_date DATETIME NOT NULL,
  CONSTRAINT fk_achievement_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS user_learning_record (
  id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  user_id INT UNSIGNED NOT NULL,
  chapter_id INT,
  activity_type VARCHAR(50) NOT NULL,
  score INT DEFAULT 0,
  experience_gained INT DEFAULT 0,
  completion_time DATETIME,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_learning_record_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS chapters (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  content TEXT,
  order_num INT DEFAULT 0,
  status VARCHAR(20) DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS chapter1_case_study_answers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id INT UNSIGNED NULL,
  question_id VARCHAR(50) NOT NULL,
  selected_option VARCHAR(10) NOT NULL,
  is_correct BOOLEAN NOT NULL,
  answer_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ip_address VARCHAR(45),
  user_agent TEXT,
  session_id VARCHAR(100),
  INDEX idx_case_question_id (question_id),
  INDEX idx_case_user_id (user_id),
  INDEX idx_case_answer_time (answer_time),
  CONSTRAINT fk_case_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS chapter1_global_stats (
  id INT AUTO_INCREMENT PRIMARY KEY,
  total_users INT DEFAULT 0,
  total_attempts INT DEFAULT 0,
  correct_answers INT DEFAULT 0,
  accuracy_rate DECIMAL(5,2) DEFAULT 0.00,
  completion_rate DECIMAL(5,2) DEFAULT 0.00,
  chapter_type VARCHAR(50) DEFAULT 'chapter1_case_study',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO chapter1_global_stats (chapter_type)
SELECT 'chapter1_case_study'
WHERE NOT EXISTS (
  SELECT 1 FROM chapter1_global_stats WHERE chapter_type = 'chapter1_case_study'
);
