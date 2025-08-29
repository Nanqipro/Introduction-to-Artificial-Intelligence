package com.goodlab.server.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 题目数据传输对象
 */
@Data
public class QuestionDTO {
    private Long id;
    private String title; // 题目标题
    private String description; // 题目描述
    private String type; // 题目类型
    private String difficulty; // 难度
    private String chapterId; // 所属章节ID
    private String optionA; // 选项A
    private String optionB; // 选项B
    private String optionC; // 选项C
    private String optionD; // 选项D
    private String correctAnswer; // 正确答案
    private String explanation; // 答案解释
    private String imageUrl; // 图片URL
    private String audioUrl; // 音频URL
    private String videoUrl; // 视频URL
    private Integer score; // 题目分值
    private Boolean isActive; // 是否启用
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private String createdBy; // 创建人
    private String updatedBy; // 更新人

    // 扩展字段
    private String chapterTitle; // 章节标题
    private String typeName; // 类型名称
    private String difficultyName; // 难度名称
}
