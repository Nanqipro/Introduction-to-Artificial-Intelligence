package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.pojo.Feedback;
import com.goodlab.server.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员反馈管理接口，路径受管理员拦截器保护
 */
@RestController
@RequestMapping("/api/admin/feedback")
public class AdminFeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 获取全部反馈列表
     */
    @GetMapping
    public ApiResponse<List<Feedback>> listAll() {
        try {
            List<Feedback> list = feedbackService.getAllFeedback();
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error("获取反馈列表失败: " + e.getMessage());
        }
    }

    /**
     * 更新反馈状态与管理员回复
     */
    @PatchMapping("/{id}")
    public ApiResponse<String> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        String status = request.getOrDefault("status", "");
        String adminReply = request.getOrDefault("adminReply", "");

        if (status.isEmpty()) {
            return ApiResponse.error("状态不能为空");
        }
        try {
            feedbackService.updateStatusAndReply(id, status, adminReply);
            return ApiResponse.success("更新成功");
        } catch (Exception e) {
            return ApiResponse.error("更新失败: " + e.getMessage());
        }
    }
}