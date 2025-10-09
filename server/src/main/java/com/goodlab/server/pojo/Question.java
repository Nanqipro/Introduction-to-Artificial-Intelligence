package com.goodlab.server.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 题目实体类
 */
@Data
public class Question {
    private Long id;
    private String title; // 题目标题
    private String description; // 题目描述
    private String type; // 题目类型：choice(选择题), tf(判断题), fill(填空题)
    private String difficulty; // 难度：easy(简单), medium(中等), hard(困难)
    private String chapterId; // 所属章节ID
    private String optionA; // 选项A
    private String optionB; // 选项B
    private String optionC; // 选项C（可选）
    private String optionD; // 选项D（可选）
    private String correctAnswer; // 正确答案
    private String explanation; // 答案解释
    private String imageUrl; // 图片URL（可选）
    private String audioUrl; // 音频URL（可选）
    private String videoUrl; // 视频URL（可选）
    private Integer score; // 题目分值
    private Boolean isActive; // 是否启用
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private String createdBy; // 创建人
    private String updatedBy; // 更新人
}
