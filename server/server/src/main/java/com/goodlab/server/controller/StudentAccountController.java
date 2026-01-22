package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.service.StudentAccountInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 学号账户管理控制器
 * 提供学号账户的初始化和管理功能
 */
@RestController
@RequestMapping("/api/admin/student-accounts")
public class StudentAccountController {

    @Autowired
    private StudentAccountInitializer studentAccountInitializer;

    /**
     * 批量初始化学号账户
     * @return 初始化结果
     */
    @PostMapping("/initialize")
    public ApiResponse<String> initializeAccounts() {
        try {
            String result = 
                studentAccountInitializer.initializeStudentAccounts();
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("账户初始化失败: " + e.getMessage());
        }
    }

    /**
     * 重置指定学号的密码为初始密码
     * @param studentId 学号
     * @return 重置结果
     */
    @PostMapping("/reset-password/{studentId}")
    public ApiResponse<String> resetPassword(@PathVariable String studentId) {
        try {
            String result = studentAccountInitializer.resetStudentPassword(studentId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("密码重置失败: " + e.getMessage());
        }
    }

    /**
     * 检查是否为初始密码
     * @param studentId 学号
     * @param password 密码
     * @return 检查结果
     */
    @PostMapping("/check-initial-password")
    public ApiResponse<Boolean> checkInitialPassword(@RequestParam String studentId, 
                                                   @RequestParam String password) {
        try {
            boolean isInitial = studentAccountInitializer.isInitialPassword(studentId, password);
            return ApiResponse.success(isInitial);
        } catch (Exception e) {
            return ApiResponse.error("检查失败: " + e.getMessage());
        }
    }

    /**
     * 获取学生信息统计
     * @return 学生信息统计结果
     */
    @GetMapping("/stats")
    public ApiResponse<Object> getStudentStats() {
        try {
            Object stats = studentAccountInitializer.getStudentStats();
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error("获取统计信息失败: " + e.getMessage());
        }
    }
}