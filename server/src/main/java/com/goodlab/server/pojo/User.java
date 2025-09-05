package com.goodlab.server.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NonNull;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;// 主键ID
    private String username;// 用户名
    @JsonIgnore
    private String password;// 密码
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "用户名格式错误")
    private String nickname;// 昵称
    @NotEmpty
    @Email
    private String email;// 邮箱
    private String userPic;// 用户头像地址
    private Integer level = 1;// 用户等级
    private Integer experience = 0;// 经验值
    @Column(name = "total_score")
    private Integer totalScore = 0;// 总分数
    @Column(name = "completed_chapters")
    private Integer completedChapters = 0;// 已完成章节数
    @Column(name = "quiz_count")
    private Integer quizCount = 0;// 答题次数
    @Column(name = "correct_answers")
    private Integer correctAnswers = 0;// 正确答案数

    // 新增字段
    private String phone;// 手机号码
    private String gender;// 性别
    private LocalDate birthday;// 生日
    private String location;// 所在地区
    private String bio;// 个人简介
    private String school;// 学校
    private String major;// 专业
    private String role = "学生";// 用户角色

    @Column(name = "create_time")
    private LocalDateTime createTime;// 创建时间
    @Column(name = "update_time")
    private LocalDateTime updateTime;// 更新时间
}
