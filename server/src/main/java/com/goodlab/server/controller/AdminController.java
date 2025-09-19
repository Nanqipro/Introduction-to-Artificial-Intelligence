package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.pojo.Question;
import com.goodlab.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/questions")
    public ApiResponse<List<Question>> getAllQuestions() {
        try {
            List<Question> questions = adminService.getAllQuestions();
            return ApiResponse.success(questions);
        } catch (Exception e) {
            return ApiResponse.error("获取题目列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/questions/{id}")
    public ApiResponse<Question> getQuestionById(@PathVariable Long id) {
        try {
            Question question = adminService.getQuestionById(id);
            if (question == null) {
                return ApiResponse.error("题目不存在");
            }
            return ApiResponse.success(question);
        } catch (Exception e) {
            return ApiResponse.error("获取题目失败: " + e.getMessage());
        }
    }

    @PostMapping("/questions")
    public ApiResponse<Question> createQuestion(@RequestBody Question question) {
        try {
            Question savedQuestion = adminService.saveQuestion(question);
            return ApiResponse.success(savedQuestion);
        } catch (Exception e) {
            return ApiResponse.error("创建题目失败: " + e.getMessage());
        }
    }

    @PutMapping("/questions/{id}")
    public ApiResponse<Question> updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        try {
            question.setId(id);
            Question updatedQuestion = adminService.saveQuestion(question);
            return ApiResponse.success(updatedQuestion);
        } catch (Exception e) {
            return ApiResponse.error("更新题目失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/questions/{id}")
    public ApiResponse<String> deleteQuestion(@PathVariable Long id) {
        try {
            boolean deleted = adminService.deleteQuestion(id);
            if (deleted) {
                return ApiResponse.success("题目删除成功");
            } else {
                return ApiResponse.error("题目不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error("删除题目失败: " + e.getMessage());
        }
    }

    @PostMapping("/questions/import")
    public ApiResponse<Map<String, Object>> importQuestions(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ApiResponse.error("请选择要上传的Excel文件");
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return ApiResponse.error("请上传Excel文件(.xlsx或.xls格式)");
            }

            Map<String, Object> result = adminService.importQuestionsFromExcel(file);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("导入题目失败: " + e.getMessage());
        }
    }

    @GetMapping("/questions/stats")
    public ApiResponse<Map<String, Object>> getQuestionStats() {
        try {
            Map<String, Object> stats = adminService.getQuestionStats();
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error("获取统计信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/questions/chapter/{chapterId}")
    public ApiResponse<List<Question>> getQuestionsByChapter(@PathVariable String chapterId) {
        try {
            List<Question> questions = adminService.getQuestionsByChapter(chapterId);
            return ApiResponse.success(questions);
        } catch (Exception e) {
            return ApiResponse.error("获取章节题目失败: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("管理员系统运行正常");
    }
}