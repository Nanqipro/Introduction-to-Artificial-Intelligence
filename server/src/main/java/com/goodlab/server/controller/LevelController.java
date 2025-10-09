package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.pojo.UserAchievement;
import com.goodlab.server.pojo.UserLearningRecord;
import com.goodlab.server.service.LevelService;
import com.goodlab.server.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/level")
public class LevelController {
    
    @Autowired
    private LevelService levelService;
    
    /**
     * 安全地将Object转换为Integer
     */
    private int convertToInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        throw new IllegalArgumentException("无法将 " + value + " 转换为Integer");
    }
    
    /**
     * 安全地将Object转换为Integer，允许null
     */
    private Integer convertToIntegerOrNull(Object value) {
        if (value == null) {
            return null;
        }
        return convertToInteger(value);
    }
    
    /**
     * 添加经验值
     */
    @PostMapping("/addExperience")
    public ApiResponse<Map<String, Object>> addExperience(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer userId = (Integer) claims.get("id");
            
            // 安全地转换参数类型
            int experience = convertToInteger(params.get("experience"));
            String activityType = (String) params.get("activityType");
            Integer chapterId = convertToIntegerOrNull(params.get("chapterId"));
            Integer score = convertToIntegerOrNull(params.get("score"));
            
            Map<String, Object> result = levelService.addExperience(userId, experience, activityType, chapterId, score);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("添加经验值失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户统计信息
     */
    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getUserStats() {
        try {
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer userId = (Integer) claims.get("id");
            
            Map<String, Object> stats = levelService.getUserStats(userId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error("获取用户统计失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户成就
     */
    @GetMapping("/achievements")
    public ApiResponse<List<UserAchievement>> getUserAchievements() {
        try {
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer userId = (Integer) claims.get("id");
            
            List<UserAchievement> achievements = levelService.getUserAchievements(userId);
            return ApiResponse.success(achievements);
        } catch (Exception e) {
            return ApiResponse.error("获取用户成就失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取学习记录
     */
    @GetMapping("/records")
    public ApiResponse<List<UserLearningRecord>> getLearningRecords() {
        try {
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer userId = (Integer) claims.get("id");
            
            List<UserLearningRecord> records = levelService.getUserLearningRecords(userId);
            return ApiResponse.success(records);
        } catch (Exception e) {
            return ApiResponse.error("获取学习记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 标记章节完成
     */
    @PostMapping("/completeChapter")
    public ApiResponse<Map<String, Object>> completeChapter(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer userId = (Integer) claims.get("id");
            
            Integer chapterId = convertToIntegerOrNull(params.get("chapterId"));
            String completionType = (String) params.get("completionType"); // "video" 或 "quiz"
            Integer score = convertToIntegerOrNull(params.get("score"));
            
            if (chapterId == null) {
                return ApiResponse.error("章节ID不能为空");
            }
            
            // 计算经验值奖励
            int experienceGained = 50; // 基础章节完成奖励
            if ("quiz".equals(completionType) && score != null) {
                experienceGained += score; // 测验分数额外奖励
            }
            
            Map<String, Object> result = levelService.addExperience(userId, experienceGained, "chapter", chapterId, score);
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("标记章节完成失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取排行榜
     */
    @GetMapping("/leaderboard")
    public ApiResponse<List<Map<String, Object>>> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Map<String, Object>> leaderboard = levelService.getLeaderboard(limit);
            return ApiResponse.success(leaderboard);
        } catch (Exception e) {
            return ApiResponse.error("获取排行榜失败: " + e.getMessage());
        }
    }
    
    /**
     * 计算等级
     */
    @GetMapping("/calculateLevel")
    public ApiResponse<Map<String, Object>> calculateLevel(@RequestParam int experience) {
        try {
            int level = levelService.calculateLevel(experience);
            String title = levelService.getLevelTitle(level);
            
            Map<String, Object> result = Map.of(
                "level", level,
                "title", title,
                "experience", experience
            );
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("计算等级失败: " + e.getMessage());
        }
    }
}