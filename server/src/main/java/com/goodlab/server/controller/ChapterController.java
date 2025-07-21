package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.model.Chapter;
import com.goodlab.server.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 章节控制器
 * 提供章节相关的RESTful API
 */
@RestController
@RequestMapping("/api/chapters")
@CrossOrigin(origins = "*") // 允许跨域访问
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
     * 创建新章节
     * POST /api/chapters
     */
    @PostMapping
    public ApiResponse<Chapter> createChapter(@RequestBody Chapter chapter) {
        try {
            Chapter createdChapter = chapterService.createChapter(chapter);
            return ApiResponse.success(createdChapter);
        } catch (Exception e) {
            return ApiResponse.error("创建章节失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新章节
     * PUT /api/chapters/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<Chapter> updateChapter(@PathVariable String id, @RequestBody Chapter chapter) {
        try {
            if (!chapterService.chapterExists(id)) {
                return ApiResponse.error(404, "章节不存在");
            }
            chapter.setId(id);
            Chapter updatedChapter = chapterService.updateChapter(chapter);
            return ApiResponse.success(updatedChapter);
        } catch (Exception e) {
            return ApiResponse.error("更新章节失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除章节
     * DELETE /api/chapters/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteChapter(@PathVariable String id) {
        try {
            if (!chapterService.chapterExists(id)) {
                return ApiResponse.error(404, "章节不存在");
            }
            boolean deleted = chapterService.deleteChapter(id);
            if (deleted) {
                return ApiResponse.success();
            } else {
                return ApiResponse.error("删除章节失败");
            }
        } catch (Exception e) {
            return ApiResponse.error("删除章节失败: " + e.getMessage());
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
