package com.goodlab.server.model;

/**
 * 章节数据模型
 * 用于表示《人工智能概论与应用》教材的章节信息
 */
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

    // 无参构造函数
    public Chapter() {
    }

    // 全参构造函数
    public Chapter(String id, String chapterNumber, String title, String summary, String content, 
                   String type, Integer orderIndex, Boolean published, String createdAt, String updatedAt) {
        this.id = id;
        this.chapterNumber = chapterNumber;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.type = type;
        this.orderIndex = orderIndex;
        this.published = published;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter和Setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
