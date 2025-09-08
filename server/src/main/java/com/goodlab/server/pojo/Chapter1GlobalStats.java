package com.goodlab.server.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 第一章案例演示全局统计实体类
 */
@Data
public class Chapter1GlobalStats {
    private Long id; // 主键ID
    private Integer totalUsers; // 总参与人数
    private Integer totalAttempts; // 总答题数
    private Integer correctAnswers; // 正确答案数
    private Double accuracyRate; // 准确率
    private Double completionRate; // 完成率
    private String chapterType; // 章节类型，固定为"chapter1_case_study"
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}