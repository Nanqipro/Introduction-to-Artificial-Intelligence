package com.goodlab.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 章节小节数据模型
 * 用于表示章节内的小节信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    
    /**
     * 小节ID
     */
    private String id;
    
    /**
     * 所属章节ID
     */
    private String chapterId;
    
    /**
     * 小节编号 (如: "1.1", "1.2", "2.1"等)
     */
    private String sectionNumber;
    
    /**
     * 小节标题
     */
    private String title;
    
    /**
     * 小节内容
     */
    private String content;
    
    /**
     * 小节类型 (section: 普通小节, subsection: 子小节)
     */
    private String type;
    
    /**
     * 排序顺序
     */
    private Integer orderIndex;
    
    /**
     * 父小节ID (用于子小节)
     */
    private String parentSectionId;
    
    /**
     * 创建时间
     */
    private String createdAt;
    
    /**
     * 更新时间
     */
    private String updatedAt;
}
