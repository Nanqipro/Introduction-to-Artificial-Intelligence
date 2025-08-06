-- 创建数据库
create database AI_platform;

-- 使用数据库
use AI_platform;

-- 用户表
create table user (
                      id int unsigned primary key auto_increment comment 'ID',
                      username varchar(20) not null unique comment '用户名',
                      password varchar(32)  comment '密码',
                      nickname varchar(10)  default '' comment '昵称',
                      email varchar(128) default '' comment '邮箱',
                      user_pic varchar(128) default '' comment '头像',
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
