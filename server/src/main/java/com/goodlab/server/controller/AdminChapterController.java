package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.model.Chapter;
import com.goodlab.server.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/chapters")
public class AdminChapterController {

    @Autowired
    private ChapterService chapterService;

    @PostMapping
    public ApiResponse<Chapter> createChapter(@RequestBody Chapter chapter) {
        try {
            Chapter createdChapter = chapterService.createChapter(chapter);
            return ApiResponse.success(createdChapter);
        } catch (Exception e) {
            return ApiResponse.error("创建章节失败: " + e.getMessage());
        }
    }

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
}
