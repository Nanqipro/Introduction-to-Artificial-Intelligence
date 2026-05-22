package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.service.Chapter1CaseStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/chapter1")
public class AdminChapter1Controller {

    @Autowired
    private Chapter1CaseStudyService chapter1CaseStudyService;

    @PostMapping("/update-stats")
    public ApiResponse<String> updateGlobalStats() {
        try {
            chapter1CaseStudyService.updateGlobalStats();
            return ApiResponse.success("全局统计数据更新成功");
        } catch (Exception e) {
            return ApiResponse.error("更新全局统计数据失败: " + e.getMessage());
        }
    }
}
