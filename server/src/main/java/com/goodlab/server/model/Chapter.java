package com.goodlab.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 章节数据模型
 * 用于表示《人工智能概论与应用》教材的章节信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {
    
    /**
     * 章节ID
     */
    private String id;
    
    /**
     * 章节编号 (如: "0", "1", "2"等)
     */
    private String chapterNumber;
    
    /**
     * 章节标题
     */
    private String title;
    
    /**
     * 章节简介/摘要
     */
    private String summary;
    
    /**
     * 章节完整内容
     */
    private String content;
    
    /**
     * 章节类型 (prologue: 续章, chapter: 正章, appendix: 附录)
     */
    private String type;
    
    /**
     * 排序顺序
     */
    private Integer orderIndex;
    
    /**
     * 是否已发布
     */
    private Boolean published;
    
    /**
     * 创建时间
     */
    private String createdAt;
    
    /**
     * 更新时间
     */
    private String updatedAt;
}
