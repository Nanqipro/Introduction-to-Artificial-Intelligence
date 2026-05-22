package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.model.Chapter;
import com.goodlab.server.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * 章节控制器
 * 提供章节相关的RESTful API
 */
@RestController
@RequestMapping("/api/chapters")
public class ChapterController {
    
    @Autowired
    private ChapterService chapterService;
    
    /**
     * 获取章节概览列表
     * GET /api/chapters
     */
    @GetMapping
    public ApiResponse<List<Chapter>> getChapterOverview() {
        try {
            List<Chapter> chapters = chapterService.getChapterOverview();
            return ApiResponse.success(chapters);
        } catch (Exception e) {
            return ApiResponse.error("获取章节列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有章节（包括完整内容）
     * GET /api/chapters/all
     */
    @GetMapping("/all")
    public ApiResponse<List<Chapter>> getAllChapters() {
        try {
            List<Chapter> chapters = chapterService.getAllPublishedChapters();
            return ApiResponse.success(chapters);
        } catch (Exception e) {
            return ApiResponse.error("获取所有章节失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取特定章节
     * GET /api/chapters/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<Chapter> getChapterById(@PathVariable String id) {
        try {
            Optional<Chapter> chapter = chapterService.getChapterById(id);
            if (chapter.isPresent()) {
                return ApiResponse.success(chapter.get());
            } else {
                return ApiResponse.error(404, "章节不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error("获取章节详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 健康检查
     * GET /api/chapters/health
     */
    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("Chapter service is running");
    }
}
