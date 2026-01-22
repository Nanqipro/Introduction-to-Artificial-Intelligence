package com.goodlab.server.dto;

import java.util.List;

public class QuestionImportDto {
    private String chapterId;
    private String type;
    private String title;
    private String description;
    private List<String> options;
    private String correctAnswer;
    private Integer points;
    private String explanation;

    public QuestionImportDto() {
    }

    public QuestionImportDto(String chapterId, String type, String title, String description,
            List<String> options, String correctAnswer, Integer points, String explanation) {
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

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
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