package com.goodlab.server.service;

import com.goodlab.server.pojo.User;
import com.goodlab.server.pojo.UserAchievement;
import com.goodlab.server.pojo.UserLearningRecord;

import java.util.List;
import java.util.Map;

public interface LevelService {
    
    /**
     * 计算用户等级
     * @param experience 经验值
     * @return 等级
     */
    int calculateLevel(int experience);
    
    /**
     * 计算升级所需经验值
     * @param level 当前等级
     * @return 升级所需经验值
     */
    int getExperienceForLevel(int level);
    
    /**
     * 获取等级称号
     * @param level 等级
     * @return 称号
     */
    String getLevelTitle(int level);
    
    /**
     * 添加经验值并检查升级
     * @param userId 用户ID
     * @param experience 新增经验值
     * @param activityType 活动类型
     * @param chapterId 章节ID（可选）
     * @param score 得分（可选）
     * @return 升级信息
     */
    Map<String, Object> addExperience(Integer userId, int experience, String activityType, Integer chapterId, Integer score);
    
    /**
     * 获取用户统计信息
     * @param userId 用户ID
     * @return 统计信息
     */
    Map<String, Object> getUserStats(Integer userId);
    
    /**
     * 获取用户成就列表
     * @param userId 用户ID
     * @return 成就列表
     */
    List<UserAchievement> getUserAchievements(Integer userId);
    
    /**
     * 获取用户学习记录
     * @param userId 用户ID
     * @return 学习记录
     */
    List<UserLearningRecord> getUserLearningRecords(Integer userId);
    
    /**
     * 获取排行榜
     * @param limit 限制数量
     * @return 排行榜
     */
    List<Map<String, Object>> getLeaderboard(int limit);
    
    /**
     * 检查并授予成就
     * @param userId 用户ID
     * @param user 用户信息
     */
    void checkAndGrantAchievements(Integer userId, User user);
}