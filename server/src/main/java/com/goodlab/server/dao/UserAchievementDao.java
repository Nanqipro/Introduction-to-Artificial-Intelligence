package com.goodlab.server.dao;

import com.goodlab.server.pojo.UserAchievement;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserAchievementDao {
    
    /**
     * 添加用户成就
     */
    @Insert("INSERT INTO user_achievement (user_id, achievement_type, achievement_name, achievement_description, achieved_at) " +
            "VALUES (#{userId}, #{achievementType}, #{achievementName}, #{achievementDescription}, #{achievedAt})")
    void addAchievement(@Param("userId") Integer userId, 
                       @Param("achievementType") String achievementType,
                       @Param("achievementName") String achievementName,
                       @Param("achievementDescription") String achievementDescription,
                       @Param("achievedAt") LocalDateTime achievedAt);
    
    /**
     * 获取用户成就列表
     */
    @Select("SELECT * FROM user_achievement WHERE user_id = #{userId} ORDER BY achieved_at DESC")
    List<UserAchievement> getUserAchievements(@Param("userId") Integer userId);
    
    /**
     * 检查用户是否已获得某个成就
     */
    @Select("SELECT COUNT(*) FROM user_achievement WHERE user_id = #{userId} AND achievement_type = #{achievementType}")
    int hasAchievement(@Param("userId") Integer userId, @Param("achievementType") String achievementType);
    
    /**
     * 获取用户成就数量
     */
    @Select("SELECT COUNT(*) FROM user_achievement WHERE user_id = #{userId}")
    int getAchievementCount(@Param("userId") Integer userId);
}