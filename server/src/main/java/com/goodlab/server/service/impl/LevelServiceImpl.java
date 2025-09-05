package com.goodlab.server.service.impl;

import com.goodlab.server.dao.UserAchievementDao;
import com.goodlab.server.dao.UserLearningRecordDao;
import com.goodlab.server.mapper.UserMapper;
import com.goodlab.server.pojo.User;
import com.goodlab.server.pojo.UserAchievement;
import com.goodlab.server.pojo.UserLearningRecord;
import com.goodlab.server.service.LevelService;
import com.goodlab.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class LevelServiceImpl implements LevelService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserAchievementDao userAchievementDao;
    
    @Autowired
    private UserLearningRecordDao userLearningRecordDao;
    
    // 移除内存存储，使用数据库操作
    
    // 等级经验值配置
    private static final int[] LEVEL_EXPERIENCE = {
        0, 100, 250, 450, 700, 1000, 1350, 1750, 2200, 2700, 3250,
        3850, 4500, 5200, 5950, 6750, 7600, 8500, 9450, 10450, 11500
    };
    
    // 等级称号配置
    private static final String[] LEVEL_TITLES = {
        "新手学习者", "初级学习者", "进阶学习者", "中级学习者", "高级学习者",
        "专业学习者", "资深学习者", "专家学习者", "大师学习者", "传奇学习者",
        "AI新手", "AI学徒", "AI专家", "AI大师", "AI传奇",
        "机器学习专家", "深度学习专家", "人工智能专家", "AI科学家", "AI先驱", "AI宗师"
    };
    
    @Override
    public int calculateLevel(int experience) {
        for (int i = LEVEL_EXPERIENCE.length - 1; i >= 0; i--) {
            if (experience >= LEVEL_EXPERIENCE[i]) {
                return i + 1;
            }
        }
        return 1;
    }
    
    @Override
    public int getExperienceForLevel(int level) {
        if (level <= 0 || level > LEVEL_EXPERIENCE.length) {
            return LEVEL_EXPERIENCE[LEVEL_EXPERIENCE.length - 1];
        }
        return LEVEL_EXPERIENCE[level - 1];
    }
    
    @Override
    public String getLevelTitle(int level) {
        if (level <= 0) {
            return LEVEL_TITLES[0];
        }
        if (level > LEVEL_TITLES.length) {
            return LEVEL_TITLES[LEVEL_TITLES.length - 1];
        }
        return LEVEL_TITLES[level - 1];
    }
    
    @Override
    public Map<String, Object> addExperience(Integer userId, int experience, String activityType, Integer chapterId, Integer score) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        int oldLevel = user.getLevel();
        int oldExperience = user.getExperience();
        
        // 更新经验值和相关统计
        user.setExperience(oldExperience + experience);
        if (score != null) {
            user.setTotalScore(user.getTotalScore() + score);
        }
        
        // 更新活动统计
        switch (activityType) {
            case "quiz":
            case "quiz_completion":
                user.setQuizCount(user.getQuizCount() + 1);
                if (score != null && score > 0) {
                    user.setCorrectAnswers(user.getCorrectAnswers() + 1);
                }
                break;
            case "chapter":
                user.setCompletedChapters(user.getCompletedChapters() + 1);
                break;
        }
        
        // 计算新等级
        int newLevel = calculateLevel(user.getExperience());
        user.setLevel(newLevel);
        
        // 保存用户信息
        userService.update(user);
        
        // 记录学习活动
        userLearningRecordDao.addLearningRecord(userId, chapterId, activityType, score != null ? score : 0, experience, LocalDateTime.now(), LocalDateTime.now());
        
        // 检查升级
        boolean levelUp = newLevel > oldLevel;
        
        // 检查并授予成就
        checkAndGrantAchievements(userId, user, chapterId, activityType);
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("levelUp", levelUp);
        result.put("oldLevel", oldLevel);
        result.put("newLevel", newLevel);
        result.put("experience", user.getExperience());
        result.put("experienceGained", experience);
        result.put("title", getLevelTitle(newLevel));
        
        if (levelUp) {
            result.put("levelUpMessage", String.format("恭喜升级！从 %s 升级到 %s！", 
                getLevelTitle(oldLevel), getLevelTitle(newLevel)));
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getUserStats(Integer userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("level", user.getLevel());
        stats.put("experience", user.getExperience());
        stats.put("title", getLevelTitle(user.getLevel()));
        stats.put("totalScore", user.getTotalScore());
        stats.put("completedChapters", user.getCompletedChapters());
        stats.put("quizCount", user.getQuizCount());
        stats.put("correctAnswers", user.getCorrectAnswers());
        
        // 计算准确率
        double accuracy = user.getQuizCount() > 0 ? 
            (double) user.getCorrectAnswers() / user.getQuizCount() * 100 : 0;
        stats.put("accuracy", Math.round(accuracy * 100.0) / 100.0);
        
        // 计算升级进度
        int currentLevelExp = getExperienceForLevel(user.getLevel());
        int nextLevelExp = getExperienceForLevel(user.getLevel() + 1);
        int expToNext = nextLevelExp - user.getExperience();
        double progress = nextLevelExp > currentLevelExp ? 
            (double) (user.getExperience() - currentLevelExp) / (nextLevelExp - currentLevelExp) * 100 : 100;
        
        stats.put("expToNextLevel", Math.max(0, expToNext));
        stats.put("levelProgress", Math.min(100, Math.max(0, Math.round(progress * 100.0) / 100.0)));
        
        // 获取成就数量
        List<UserAchievement> achievements = getUserAchievements(userId);
        stats.put("achievementCount", achievements.size());
        
        // 添加前端期望的其他字段
        stats.put("studyTime", 0); // 暂时设为0，后续可根据实际需求计算
        stats.put("networkProgress", 0); // 暂时设为0，后续可根据实际需求计算
        stats.put("protocolProgress", 0); // 暂时设为0，后续可根据实际需求计算
        stats.put("practiceProgress", 0); // 暂时设为0，后续可根据实际需求计算
        
        return stats;
    }
    
    @Override
    public List<UserAchievement> getUserAchievements(Integer userId) {
        return userAchievementDao.getUserAchievements(userId);
    }
    
    @Override
    public List<UserLearningRecord> getUserLearningRecords(Integer userId) {
        return userLearningRecordDao.getUserLearningRecords(userId, 50); // 限制返回最近50条记录
    }
    
    @Override
    public List<Map<String, Object>> getLeaderboard(int limit) {
        return userMapper.findAllUsers().stream()
            .sorted((a, b) -> {
                // 先按等级排序，再按经验值排序
                int levelCompare = Integer.compare(b.getLevel(), a.getLevel());
                if (levelCompare != 0) return levelCompare;
                return Integer.compare(b.getExperience(), a.getExperience());
            })
            .limit(limit)
            .map(user -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", user.getId());
                item.put("username", user.getUsername());
                item.put("nickname", user.getNickname());
                item.put("level", user.getLevel());
                item.put("experience", user.getExperience());
                item.put("title", getLevelTitle(user.getLevel()));
                item.put("totalScore", user.getTotalScore());
                item.put("completedChapters", user.getCompletedChapters());
                return item;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public void checkAndGrantAchievements(Integer userId, User user) {
        checkAndGrantAchievements(userId, user, null, null);
    }
    
    public void checkAndGrantAchievements(Integer userId, User user, Integer chapterId, String activityType) {
        List<UserAchievement> currentAchievements = getUserAchievements(userId);
        Set<String> earnedTypes = currentAchievements.stream()
            .map(UserAchievement::getAchievementType)
            .collect(Collectors.toSet());
        
        // 检查各种成就条件
        checkLevelAchievements(userId, user, earnedTypes);
        checkQuizAchievements(userId, user, earnedTypes);
        checkChapterAchievements(userId, user, earnedTypes, chapterId, activityType);
        checkScoreAchievements(userId, user, earnedTypes);
    }
    
    private void checkLevelAchievements(Integer userId, User user, Set<String> earnedTypes) {
        int level = user.getLevel();
        
        if (level >= 5 && !earnedTypes.contains("level_5")) {
            grantAchievement(userId, "level_5", "初级大师", "达到5级");
        }
        if (level >= 10 && !earnedTypes.contains("level_10")) {
            grantAchievement(userId, "level_10", "中级大师", "达到10级");
        }
        if (level >= 15 && !earnedTypes.contains("level_15")) {
            grantAchievement(userId, "level_15", "高级大师", "达到15级");
        }
        if (level >= 20 && !earnedTypes.contains("level_20")) {
            grantAchievement(userId, "level_20", "传奇大师", "达到20级");
        }
    }
    
    private void checkQuizAchievements(Integer userId, User user, Set<String> earnedTypes) {
        int quizCount = user.getQuizCount();
        int correctAnswers = user.getCorrectAnswers();
        
        if (quizCount >= 10 && !earnedTypes.contains("quiz_10")) {
            grantAchievement(userId, "quiz_10", "答题新手", "完成10次答题");
        }
        if (quizCount >= 50 && !earnedTypes.contains("quiz_50")) {
            grantAchievement(userId, "quiz_50", "答题达人", "完成50次答题");
        }
        if (quizCount >= 100 && !earnedTypes.contains("quiz_100")) {
            grantAchievement(userId, "quiz_100", "答题专家", "完成100次答题");
        }
        
        // 准确率成就
        if (quizCount >= 10) {
            double accuracy = (double) correctAnswers / quizCount;
            if (accuracy >= 0.9 && !earnedTypes.contains("accuracy_90")) {
                grantAchievement(userId, "accuracy_90", "精准射手", "答题准确率达到90%");
            }
            if (accuracy >= 0.95 && !earnedTypes.contains("accuracy_95")) {
                grantAchievement(userId, "accuracy_95", "完美主义者", "答题准确率达到95%");
            }
        }
    }
    
    private void checkChapterAchievements(Integer userId, User user, Set<String> earnedTypes) {
        checkChapterAchievements(userId, user, earnedTypes, null, null);
    }
    
    private void checkChapterAchievements(Integer userId, User user, Set<String> earnedTypes, Integer chapterId, String activityType) {
        // 如果是章节完成活动，根据具体章节ID触发对应成就
        if ("chapter".equals(activityType) && chapterId != null) {
            String chapterAchievementType = "chapter_" + chapterId;
            if (!earnedTypes.contains(chapterAchievementType)) {
                String achievementName = getChapterAchievementName(chapterId);
                String achievementDesc = getChapterAchievementDesc(chapterId);
                grantAchievement(userId, chapterAchievementType, achievementName, achievementDesc);
            }
        }
        
        // 检查基于总完成章节数的成就
        int completedChapters = user.getCompletedChapters();
        if (completedChapters >= 5 && !earnedTypes.contains("chapters_5")) {
            grantAchievement(userId, "chapters_5", "勤奋学习者", "完成5个章节");
        }
        if (completedChapters >= 10 && !earnedTypes.contains("chapters_10")) {
            grantAchievement(userId, "chapters_10", "知识探索者", "完成10个章节");
        }
    }
    
    private String getChapterAchievementName(Integer chapterId) {
        switch (chapterId) {
            case 1: return "学习起步";
            case 2: return "深入探索";
            case 3: return "知识进阶";
            case 4: return "技能提升";
            case 5: return "专业发展";
            case 6: return "实践应用";
            case 7: return "综合运用";
            default: return "章节完成";
        }
    }
    
    private String getChapterAchievementDesc(Integer chapterId) {
        switch (chapterId) {
            case 1: return "完成第一章节";
            case 2: return "完成第二章节";
            case 3: return "完成第三章节";
            case 4: return "完成第四章节";
            case 5: return "完成第五章节";
            case 6: return "完成第六章节";
            case 7: return "完成第七章节";
            default: return "完成第" + chapterId + "章节";
        }
    }
    
    private void checkScoreAchievements(Integer userId, User user, Set<String> earnedTypes) {
        int totalScore = user.getTotalScore();
        
        if (totalScore >= 1000 && !earnedTypes.contains("score_1000")) {
            grantAchievement(userId, "score_1000", "千分达人", "总分达到1000分");
        }
        if (totalScore >= 5000 && !earnedTypes.contains("score_5000")) {
            grantAchievement(userId, "score_5000", "五千分王者", "总分达到5000分");
        }
        if (totalScore >= 10000 && !earnedTypes.contains("score_10000")) {
            grantAchievement(userId, "score_10000", "万分传奇", "总分达到10000分");
        }
    }
    
    private void grantAchievement(Integer userId, String type, String name, String description) {
        userAchievementDao.addAchievement(userId, type, name, description, LocalDateTime.now());
    }
}