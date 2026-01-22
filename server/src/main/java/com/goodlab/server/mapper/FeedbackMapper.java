package com.goodlab.server.mapper;

import com.goodlab.server.pojo.Feedback;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FeedbackMapper {

    @Insert("INSERT INTO feedback(user_id, content, category, status, created_at, updated_at) VALUES(#{userId}, #{content}, #{category}, 'NEW', NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Feedback feedback);

    @Select("SELECT f.id, f.user_id AS userId, u.nickname AS nickname, f.content, f.category, f.status, f.admin_reply AS adminReply, f.created_at AS createdAt, f.updated_at AS updatedAt FROM feedback f LEFT JOIN user u ON f.user_id = u.id WHERE f.user_id = #{userId} ORDER BY f.created_at DESC")
    List<Feedback> findByUserId(Integer userId);

    @Select("SELECT f.id, f.user_id AS userId, u.nickname AS nickname, f.content, f.category, f.status, f.admin_reply AS adminReply, f.created_at AS createdAt, f.updated_at AS updatedAt FROM feedback f LEFT JOIN user u ON f.user_id = u.id ORDER BY f.created_at DESC")
    List<Feedback> findAll();

    @Update("UPDATE feedback SET status = #{status}, admin_reply = #{adminReply}, updated_at = NOW() WHERE id = #{id}")
    void updateStatusAndReply(@Param("id") Long id, @Param("status") String status, @Param("adminReply") String adminReply);
}