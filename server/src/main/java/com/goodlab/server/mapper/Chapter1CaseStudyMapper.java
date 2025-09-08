package com.goodlab.server.mapper;

import com.goodlab.server.pojo.Chapter1CaseStudyAnswer;
import com.goodlab.server.pojo.Chapter1GlobalStats;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface Chapter1CaseStudyMapper {

    /**
     * 插入答题记录
     */
    @Insert("INSERT INTO chapter1_case_study_answers (user_id, question_id, user_answer, correct_answer, is_correct, chapter_type, create_time, update_time) " +
            "VALUES (#{userId}, #{questionId}, #{userAnswer}, #{correctAnswer}, #{isCorrect}, #{chapterType}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertAnswer(Chapter1CaseStudyAnswer answer);

    /**
     * 根据用户ID查询答题记录
     */
    @Select("SELECT * FROM chapter1_case_study_answers WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Chapter1CaseStudyAnswer> findAnswersByUserId(Integer userId);

    /**
     * 查询全局统计数据
     */
    @Select("SELECT id, total_users as totalUsers, total_attempts as totalAttempts, " +
            "correct_answers as correctAnswers, accuracy_rate as accuracyRate, " +
            "completion_rate as completionRate, chapter_type as chapterType, " +
            "create_time as createTime, update_time as updateTime " +
            "FROM chapter1_global_stats WHERE chapter_type = 'chapter1_case_study' ORDER BY update_time DESC LIMIT 1")
    Chapter1GlobalStats findGlobalStats();

    /**
     * 更新全局统计数据
     */
    @Update("UPDATE chapter1_global_stats SET total_users = #{totalUsers}, " +
            "total_attempts = #{totalAttempts}, correct_answers = #{correctAnswers}, " +
            "accuracy_rate = #{accuracyRate}, completion_rate = #{completionRate}, update_time = now() " +
            "WHERE chapter_type = #{chapterType}")
    void updateGlobalStats(Chapter1GlobalStats stats);

    /**
     * 插入全局统计数据（如果不存在）
     */
    @Insert("INSERT INTO chapter1_global_stats (total_users, total_attempts, correct_answers, accuracy_rate, completion_rate, chapter_type, create_time, update_time) " +
            "VALUES (#{totalUsers}, #{totalAttempts}, #{correctAnswers}, #{accuracyRate}, #{completionRate}, #{chapterType}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertGlobalStats(Chapter1GlobalStats stats);

    /**
     * 统计总参与人数
     */
    @Select("SELECT COUNT(DISTINCT user_id) FROM chapter1_case_study_answers WHERE chapter_type = 'chapter1_case_study'")
    Integer countTotalParticipants();

    /**
     * 统计总答题数
     */
    @Select("SELECT COUNT(*) FROM chapter1_case_study_answers WHERE chapter_type = 'chapter1_case_study'")
    Integer countTotalAnswers();

    /**
     * 统计正确答案数
     */
    @Select("SELECT COUNT(*) FROM chapter1_case_study_answers WHERE chapter_type = 'chapter1_case_study' AND is_correct = true")
    Integer countCorrectAnswers();
}