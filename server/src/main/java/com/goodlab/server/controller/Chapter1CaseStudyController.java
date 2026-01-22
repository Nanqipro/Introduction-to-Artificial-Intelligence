package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.pojo.Chapter1CaseStudyAnswer;
import com.goodlab.server.service.Chapter1CaseStudyService;
import com.goodlab.server.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 第一章案例演示控制器
 */
@RestController
@RequestMapping("/api/chapter1-case-study")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174", "http://localhost:3000", "http://127.0.0.1:5173", "http://127.0.0.1:5174" })
public class Chapter1CaseStudyController {

    @Autowired
    private Chapter1CaseStudyService chapter1CaseStudyService;

    /**
     * 获取全局统计数据
     * GET /api/chapter1-case-study/global-stats
     */
    @GetMapping("/global-stats")
    public ApiResponse<Map<String, Object>> getGlobalStats() {
        try {
            System.out.println("控制器: 开始获取全局统计数据");
            Map<String, Object> result = chapter1CaseStudyService.getGlobalStats();
            System.out.println("控制器: 服务层返回结果 = " + result);
            
            if ((Boolean) result.get("success")) {
                return ApiResponse.success((Map<String, Object>) result.get("data"));
            } else {
                return ApiResponse.error((String) result.get("message"));
            }
            
        } catch (Exception e) {
            return ApiResponse.error("获取全局统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 提交答题数据
     * POST /api/chapter1-case-study/submit-answer
     */
    @PostMapping("/submit-answer")
    public ApiResponse<Map<String, Object>> submitAnswer(@RequestBody Map<String, Object> requestData) {
        try {
            // 尝试从ThreadLocal获取当前用户ID，如果没有则为匿名用户
            Integer userId = null;
            try {
                Map<String, Object> claims = ThreadLocalUtil.get();
                if (claims != null) {
                    userId = (Integer) claims.get("id");
                }
            } catch (Exception e) {
                // 忽略获取用户信息的异常，允许匿名访问
            }
            
            String questionId = (String) requestData.get("questionId");
            String userAnswer = (String) requestData.get("userAnswer");
            String correctAnswer = (String) requestData.get("correctAnswer");
            String chapterType = (String) requestData.get("chapterType");
            
            if (questionId == null || userAnswer == null || correctAnswer == null) {
                return ApiResponse.error("请求参数不完整");
            }
            
            // 如果没有提供chapterType，使用默认值
            if (chapterType == null) {
                chapterType = "chapter1_case_study";
            }
            
            Map<String, Object> result = chapter1CaseStudyService.submitAnswer(userId, questionId, userAnswer, correctAnswer, chapterType);
            
            if ((Boolean) result.get("success")) {
                return ApiResponse.success(result);
            } else {
                return ApiResponse.error((String) result.get("message"));
            }
            
        } catch (Exception e) {
            return ApiResponse.error("提交答题数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户答题记录
     * GET /api/chapter1-case-study/user-answers
     */
    @GetMapping("/user-answers")
    public ApiResponse<List<Chapter1CaseStudyAnswer>> getUserAnswers() {
        try {
            // 从ThreadLocal获取当前用户ID
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer userId = (Integer) claims.get("id");
            
            if (userId == null) {
                return ApiResponse.error("用户未登录");
            }
            
            List<Chapter1CaseStudyAnswer> answers = chapter1CaseStudyService.getUserAnswers(userId);
            return ApiResponse.success(answers);
            
        } catch (Exception e) {
            return ApiResponse.error("获取用户答题记录失败: " + e.getMessage());
        }
    }

    /**
     * 手动更新全局统计数据（管理员接口）
     * POST /api/chapter1-case-study/update-stats
     */
    @PostMapping("/update-stats")
    public ApiResponse<String> updateGlobalStats() {
        try {
            chapter1CaseStudyService.updateGlobalStats();
            return ApiResponse.success("全局统计数据更新成功");
        } catch (Exception e) {
            return ApiResponse.error("更新全局统计数据失败: " + e.getMessage());
        }
    }
}