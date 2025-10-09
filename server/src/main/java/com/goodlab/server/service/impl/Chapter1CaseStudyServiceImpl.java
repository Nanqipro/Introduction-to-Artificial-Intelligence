package com.goodlab.server.service.impl;

import com.goodlab.server.mapper.Chapter1CaseStudyMapper;
import com.goodlab.server.pojo.Chapter1CaseStudyAnswer;
import com.goodlab.server.pojo.Chapter1GlobalStats;
import com.goodlab.server.service.Chapter1CaseStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Chapter1CaseStudyServiceImpl implements Chapter1CaseStudyService {

    @Autowired
    private Chapter1CaseStudyMapper chapter1CaseStudyMapper;

    @Override
    @Transactional
    public Map<String, Object> submitAnswer(Integer userId, String questionId, String userAnswer, String correctAnswer, String chapterType) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 创建答题记录
            Chapter1CaseStudyAnswer answer = new Chapter1CaseStudyAnswer();
            answer.setUserId(userId);
            answer.setQuestionId(questionId);
            answer.setUserAnswer(userAnswer);
            answer.setCorrectAnswer(correctAnswer);
            answer.setIsCorrect(userAnswer.equals(correctAnswer));
            answer.setChapterType(chapterType);
            
            // 插入答题记录
            chapter1CaseStudyMapper.insertAnswer(answer);
            
            // 更新全局统计
            updateGlobalStats();
            
            result.put("success", true);
            result.put("message", "答题提交成功");
            result.put("isCorrect", answer.getIsCorrect());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "答题提交失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getGlobalStats() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 先尝试从数据库获取统计数据
            Chapter1GlobalStats stats = chapter1CaseStudyMapper.findGlobalStats();
            System.out.println("查询到的统计数据: " + stats);
            
            if (stats == null) {
                // 如果没有统计数据，创建初始数据
                stats = new Chapter1GlobalStats();
                stats.setTotalUsers(0);
                stats.setTotalAttempts(0);
                stats.setCorrectAnswers(0);
                stats.setAccuracyRate(0.0);
                stats.setCompletionRate(0.0);
                stats.setChapterType("chapter1_case_study");
                
                chapter1CaseStudyMapper.insertGlobalStats(stats);
            }
            
            result.put("success", true);
            result.put("data", Map.of(
                "totalUsers", stats.getTotalUsers(),
                "totalAttempts", stats.getTotalAttempts(),
                "correctAnswers", stats.getCorrectAnswers(),
                "accuracyRate", stats.getAccuracyRate(),
                "completionRate", stats.getCompletionRate()
            ));
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取统计数据失败: " + e.getMessage());
            // 返回默认数据
            result.put("data", Map.of(
                "totalUsers", 0,
                "totalAttempts", 0,
                "correctAnswers", 0,
                "accuracyRate", 0.0
            ));
        }
        
        return result;
    }

    @Override
    public List<Chapter1CaseStudyAnswer> getUserAnswers(Integer userId) {
        return chapter1CaseStudyMapper.findAnswersByUserId(userId);
    }

    @Override
    @Transactional
    public void updateGlobalStats() {
        try {
            // 重新计算统计数据
            Integer totalUsers = chapter1CaseStudyMapper.countTotalParticipants();
            Integer totalAttempts = chapter1CaseStudyMapper.countTotalAnswers();
            Integer correctAnswers = chapter1CaseStudyMapper.countCorrectAnswers();
            
            Double accuracyRate = 0.0;
            if (totalAttempts > 0) {
                accuracyRate = (double) correctAnswers / totalAttempts * 100;
                // 保留两位小数
                accuracyRate = Math.round(accuracyRate * 100.0) / 100.0;
            }
            
            // 检查是否已存在统计记录
            Chapter1GlobalStats existingStats = chapter1CaseStudyMapper.findGlobalStats();
            
            if (existingStats != null) {
                // 更新现有记录
                existingStats.setTotalUsers(totalUsers);
                existingStats.setTotalAttempts(totalAttempts);
                existingStats.setCorrectAnswers(correctAnswers);
                existingStats.setAccuracyRate(accuracyRate);
                
                chapter1CaseStudyMapper.updateGlobalStats(existingStats);
            } else {
                // 创建新记录
                Chapter1GlobalStats newStats = new Chapter1GlobalStats();
                newStats.setTotalUsers(totalUsers);
                newStats.setTotalAttempts(totalAttempts);
                newStats.setCorrectAnswers(correctAnswers);
                newStats.setAccuracyRate(accuracyRate);
                newStats.setChapterType("chapter1_case_study");
                
                chapter1CaseStudyMapper.insertGlobalStats(newStats);
            }
            
        } catch (Exception e) {
            System.err.println("更新全局统计数据失败: " + e.getMessage());
        }
    }
}