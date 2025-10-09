package com.goodlab.server.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 第一章案例演示答题记录实体类
 */
@Data
public class Chapter1CaseStudyAnswer {
    private Long id; // 主键ID
    private Integer userId; // 用户ID
    private String questionId; // 题目ID
    private String userAnswer; // 用户答案
    private String correctAnswer; // 正确答案
    private Boolean isCorrect; // 是否正确
    private String chapterType; // 章节类型，固定为"chapter1_case_study"
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}