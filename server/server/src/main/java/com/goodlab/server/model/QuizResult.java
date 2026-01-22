package com.goodlab.server.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class QuizResult {
    private Long id;
    private String chapterId;
    private Integer score;
    private Double accuracy;
    private Integer correctCount;
    private Integer totalQuestions;
    private List<Map<String, Object>> answers; // 用户答案
    private List<Map<String, Object>> rewards; // 获得的奖励
    private LocalDateTime completedAt;

    // 构造函数
    public QuizResult() {
    }

    public QuizResult(Long id, String chapterId, Integer score, Double accuracy,
            Integer correctCount, Integer totalQuestions,
            List<Map<String, Object>> answers, List<Map<String, Object>> rewards) {
        this.id = id;
        this.chapterId = chapterId;
        this.score = score;
        this.accuracy = accuracy;
        this.correctCount = correctCount;
        this.totalQuestions = totalQuestions;
        this.answers = answers;
        this.rewards = rewards;
        this.completedAt = LocalDateTime.now();
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public List<Map<String, Object>> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Map<String, Object>> answers) {
        this.answers = answers;
    }

    public List<Map<String, Object>> getRewards() {
        return rewards;
    }

    public void setRewards(List<Map<String, Object>> rewards) {
        this.rewards = rewards;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}