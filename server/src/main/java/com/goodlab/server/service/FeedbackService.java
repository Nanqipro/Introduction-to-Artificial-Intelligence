package com.goodlab.server.service;

import com.goodlab.server.pojo.Feedback;

import java.util.List;

public interface FeedbackService {
    void submitFeedback(Integer userId, String content, String category);
    List<Feedback> getMyFeedback(Integer userId);
    List<Feedback> getAllFeedback();
    void updateStatusAndReply(Long id, String status, String adminReply);
}