package com.goodlab.server.service.impl;

import com.goodlab.server.mapper.FeedbackMapper;
import com.goodlab.server.pojo.Feedback;
import com.goodlab.server.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public void submitFeedback(Integer userId, String content, String category) {
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setContent(content);
        feedback.setCategory(category);
        feedbackMapper.insert(feedback);
    }

    @Override
    public List<Feedback> getMyFeedback(Integer userId) {
        return feedbackMapper.findByUserId(userId);
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackMapper.findAll();
    }

    @Override
    public void updateStatusAndReply(Long id, String status, String adminReply) {
        feedbackMapper.updateStatusAndReply(id, status, adminReply);
    }
}