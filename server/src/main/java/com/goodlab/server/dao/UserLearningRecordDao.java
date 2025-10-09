package com.goodlab.server.dao;

import com.goodlab.server.pojo.UserLearningRecord;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserLearningRecordDao {
    
    /**
     * 添加学习记录
     */
    @Insert("INSERT INTO user_learning_record (user_id, chapter_id, activity_type, score, experience_gained, completion_time, create_time) " +
            "VALUES (#{userId}, #{chapterId}, #{activityType}, #{score}, #{experienceGained}, #{completedAt}, #{createdAt})")
    void addLearningRecord(@Param("userId") Integer userId,
                          @Param("chapterId") Integer chapterId,
                          @Param("activityType") String activityType,
                          @Param("score") Integer score,
                          @Param("experienceGained") Integer experienceGained,
                          @Param("completedAt") LocalDateTime completedAt,
                          @Param("createdAt") LocalDateTime createdAt);
    
    /**
     * 获取用户学习记录
     */
    @Select("SELECT * FROM user_learning_record WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{limit}")
    List<UserLearningRecord> getUserLearningRecords(@Param("userId") Integer userId, @Param("limit") int limit);
    
    /**
     * 获取用户在某个章节的学习记录
     */
    @Select("SELECT * FROM user_learning_record WHERE user_id = #{userId} AND chapter_id = #{chapterId} ORDER BY create_time DESC")
    List<UserLearningRecord> getUserChapterRecords(@Param("userId") Integer userId, @Param("chapterId") Integer chapterId);
    
    /**
     * 获取用户总学习次数
     */
    @Select("SELECT COUNT(*) FROM user_learning_record WHERE user_id = #{userId}")
    int getTotalLearningCount(@Param("userId") Integer userId);
    
    /**
     * 获取用户某种活动类型的次数
     */
    @Select("SELECT COUNT(*) FROM user_learning_record WHERE user_id = #{userId} AND activity_type = #{activityType}")
    int getActivityCount(@Param("userId") Integer userId, @Param("activityType") String activityType);
    
    /**
     * 获取用户已完成的不同章节数
     */
    @Select("SELECT COUNT(DISTINCT chapter_id) FROM user_learning_record WHERE user_id = #{userId}")
    int getCompletedChapterCount(@Param("userId") Integer userId);
}