package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.pojo.Feedback;
import com.goodlab.server.service.FeedbackService;
import com.goodlab.server.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 学生提交反馈
     */
    @PostMapping
    public ApiResponse<String> submitFeedback(@RequestBody Map<String, String> request) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        if (userId == null) {
            return ApiResponse.error("用户未登录");
        }

        String content = request.get("content");
        String category = request.getOrDefault("category", "");
        if (!StringUtils.hasText(content)) {
            return ApiResponse.error("反馈内容不能为空");
        }

        try {
            feedbackService.submitFeedback(userId, content.trim(), category.trim());
            return ApiResponse.success("反馈已提交");
        } catch (Exception e) {
            return ApiResponse.error("提交反馈失败: " + e.getMessage());
        }
    }

    /**
     * 查询当前用户的反馈记录
     */
    @GetMapping("/my")
    public ApiResponse<List<Feedback>> getMyFeedback() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        if (userId == null) {
            return ApiResponse.error("用户未登录");
        }
        try {
            List<Feedback> list = feedbackService.getMyFeedback(userId);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error("获取反馈失败: " + e.getMessage());
        }
    }

    /**
     * 查询全部用户反馈（登录用户均可查看）
     */
    @GetMapping("/all")
    public ApiResponse<List<Feedback>> getAllFeedback() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = claims != null ? (Integer) claims.get("id") : null;
        if (userId == null) {
            return ApiResponse.error("用户未登录");
        }
        try {
            List<Feedback> list = feedbackService.getAllFeedback();
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error("获取全部反馈失败: " + e.getMessage());
        }
    }
}