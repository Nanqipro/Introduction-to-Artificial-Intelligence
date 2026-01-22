-- 创建题目表
CREATE TABLE IF NOT EXISTS `questions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '题目ID',
  `title` varchar(500) NOT NULL COMMENT '题目标题',
  `description` text COMMENT '题目描述',
  `type` varchar(20) NOT NULL DEFAULT 'choice' COMMENT '题目类型：choice(选择题), tf(判断题), fill(填空题)',
  `difficulty` varchar(20) NOT NULL DEFAULT 'medium' COMMENT '难度：easy(简单), medium(中等), hard(困难)',
  `chapter_id` varchar(20) NOT NULL COMMENT '所属章节ID',
  `option_a` text COMMENT '选项A',
  `option_b` text COMMENT '选项B',
  `option_c` text COMMENT '选项C',
  `option_d` text COMMENT '选项D',
  `correct_answer` varchar(100) NOT NULL COMMENT '正确答案',
  `explanation` text COMMENT '答案解释',
  `image_url` varchar(500) COMMENT '图片URL',
  `audio_url` varchar(500) COMMENT '音频URL',
  `video_url` varchar(500) COMMENT '视频URL',
  `score` int(11) NOT NULL DEFAULT 10 COMMENT '题目分值',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用：1启用，0禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(100) COMMENT '创建人',
  `updated_by` varchar(100) COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_chapter_id` (`chapter_id`),
  KEY `idx_type` (`type`),
  KEY `idx_difficulty` (`difficulty`),
  KEY `idx_is_active` (`is_active`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目表';

-- 插入示例题目数据
INSERT INTO `questions` (`title`, `description`, `type`, `difficulty`, `chapter_id`, `option_a`, `option_b`, `option_c`, `option_d`, `correct_answer`, `explanation`, `image_url`, `score`, `created_by`) VALUES
('雪纺裙图片识别', '从两张图片中判断哪一张是真实拍摄，哪一张是AI生成。', 'choice', 'medium', '1', '真实图片', 'AI生成图片', NULL, NULL, 'A', 'A选项是真实拍摄的雪纺裙照片，B选项是AI生成的图片。', '/images/chapter1/A_真实照片.png', 10, 'admin'),
('音频对比识别', '请听两段音频，判断哪段是人声翻唱，哪段是AI翻唱《告白气球》。', 'choice', 'medium', '1', '人声翻唱', 'AI翻唱', NULL, NULL, 'A', 'A选项是人声翻唱，B选项是AI翻唱。', NULL, 10, 'admin'),
('城市街景识别', '从两张图片中判断哪一张是真实拍摄，哪一张是AI生成。', 'choice', 'medium', '1', '真实图片', 'AI生成图片', NULL, NULL, 'A', 'A选项是真实拍摄的阿姆斯特丹街景，B选项是AI生成的图片。', '/images/chapter1/B_真实图片.png', 10, 'admin'),
('论文语句识别', '请阅读两段文字，判断哪段是AI生成，哪段是人类优秀论文。', 'choice', 'hard', '1', 'AI生成', '人类优秀论文', NULL, NULL, 'B', 'A选项是AI生成的论文语句，B选项是人类优秀论文的真实语句。', NULL, 15, 'admin'),
('猫咪视频识别', '请观看两段视频，判断哪段是真实拍摄，哪段是AI生成。', 'choice', 'medium', '1', '真实视频', 'AI生成视频', NULL, NULL, 'A', 'A选项是真实拍摄的猫咪玩耍视频，B选项是AI生成的视频。', NULL, 10, 'admin'),
('麻辣烫店铺识别', '从两张图片中判断哪一张是真实拍摄，哪一张是AI生成。', 'choice', 'easy', '1', '真实图片', 'AI生成图片', NULL, NULL, 'A', 'A选项是真实拍摄的麻辣烫店铺，B选项是AI生成的图片。', NULL, 10, 'admin'),
('沸腾火锅识别', '从两张图片中判断哪一张是真实拍摄，哪一张是AI生成。', 'choice', 'easy', '1', '真实图片', 'AI生成图片', NULL, NULL, 'A', 'A选项是真实拍摄的沸腾火锅，B选项是AI生成的图片。', NULL, 10, 'admin'),
('哲学语句识别', '请阅读两段文字，判断哪段是AI生成，哪段是人类哲学思考。', 'choice', 'hard', '1', 'AI生成', '人类哲学思考', NULL, NULL, 'B', 'A选项是AI生成的哲学语句，B选项是人类真实的哲学思考。', NULL, 15, 'admin'),
('古典画作识别', '从两张图片中判断哪一张是真实古典画作，哪一张是AI生成。', 'choice', 'hard', '1', '真实古典画作', 'AI生成画作', NULL, NULL, 'A', 'A选项是真实的古典画作，B选项是AI生成的画作。', NULL, 15, 'admin'),
('广告视频识别', '请观看两段视频，判断哪段是真实拍摄，哪段是AI生成。', 'choice', 'medium', '1', '真实视频', 'AI生成视频', NULL, NULL, 'A', 'A选项是真实拍摄的广告视频，B选项是AI生成的视频。', NULL, 10, 'admin');
