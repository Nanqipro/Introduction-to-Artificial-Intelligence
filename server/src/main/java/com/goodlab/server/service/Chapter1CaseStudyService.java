package com.goodlab.server.service;

import com.goodlab.server.pojo.Chapter1CaseStudyAnswer;
import com.goodlab.server.pojo.Chapter1GlobalStats;

import java.util.List;
import java.util.Map;

/**
 * 第一章案例演示服务接口
 */
public interface Chapter1CaseStudyService {

    /**
     * 提交答题数据
     * @param userId 用户ID
     * @param questionId 题目ID
     * @param userAnswer 用户答案
     * @param correctAnswer 正确答案
     * @param chapterType 章节类型
     * @return 提交结果
     */
    Map<String, Object> submitAnswer(Integer userId, String questionId, String userAnswer, String correctAnswer, String chapterType);

    /**
     * 获取全局统计数据
     * @return 全局统计数据
     */
    Map<String, Object> getGlobalStats();

    /**
     * 获取用户答题记录
     * @param userId 用户ID
     * @return 用户答题记录列表
     */
    List<Chapter1CaseStudyAnswer> getUserAnswers(Integer userId);

    /**
     * 更新全局统计数据
     */
    void updateGlobalStats();
}