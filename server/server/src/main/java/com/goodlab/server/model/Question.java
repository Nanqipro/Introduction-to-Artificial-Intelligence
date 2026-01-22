package com.goodlab.server.model;

import java.util.List;

public class Question {
    private Long id;
    private String chapterId;
    private String type; // choice, true-false, fill
    private String title;
    private String description;
    private List<String> options; // 选择题选项
    private Object correctAnswer; // 正确答案
    private Integer points; // 分值
    private String explanation; // 解释

    // 构造函数
    public Question() {
    }

    public Question(Long id, String chapterId, String type, String title, String description,
            List<String> options, Object correctAnswer, Integer points, String explanation) {
        this.id = id;
        this.chapterId = chapterId;
        this.type = type;
        this.title = title;
        this.description = description;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.points = points;
        this.explanation = explanation;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Object getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Object correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}