package com.goodlab.server.controller;

import com.goodlab.server.pojo.Question;
import com.goodlab.server.dto.QuestionDTO;
import com.goodlab.server.service.QuestionService;
import com.goodlab.server.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 题目管理控制器
 */
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 创建题目
     */
    @PostMapping
    public Result<Question> createQuestion(@RequestBody Question question) {
        try {
            Question createdQuestion = questionService.createQuestion(question);
            return Result.success(createdQuestion, "题目创建成功");
        } catch (Exception e) {
            return Result.error("题目创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新题目
     */
    @PutMapping("/{id}")
    public Result<Question> updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        try {
            question.setId(id);
            Question updatedQuestion = questionService.updateQuestion(question);
            return Result.success(updatedQuestion, "题目更新成功");
        } catch (Exception e) {
            return Result.error("题目更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除题目
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteQuestion(@PathVariable Long id) {
        try {
            boolean success = questionService.deleteQuestion(id);
            if (success) {
                return Result.success(true, "题目删除成功");
            } else {
                return Result.error("题目删除失败");
            }
        } catch (Exception e) {
            return Result.error("题目删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取题目
     */
    @GetMapping("/{id}")
    public Result<Question> getQuestionById(@PathVariable Long id) {
        try {
            Question question = questionService.getQuestionById(id);
            if (question != null) {
                return Result.success(question, "获取题目成功");
            } else {
                return Result.error("题目不存在");
            }
        } catch (Exception e) {
            return Result.error("获取题目失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有题目（分页）
     */
    @GetMapping
    public Result<Map<String, Object>> getAllQuestions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<QuestionDTO> questions = questionService.getAllQuestions(page, size);
            int total = questionService.getQuestionCount();

            Map<String, Object> result = Map.of(
                    "questions", questions,
                    "total", total,
                    "page", page,
                    "size", size,
                    "totalPages", (int) Math.ceil((double) total / size));

            return Result.success(result, "获取题目列表成功");
        } catch (Exception e) {
            return Result.error("获取题目列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据章节ID获取题目
     */
    @GetMapping("/chapter/{chapterId}")
    public Result<List<QuestionDTO>> getQuestionsByChapterId(@PathVariable String chapterId) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByChapterId(chapterId);
            return Result.success(questions, "获取章节题目成功");
        } catch (Exception e) {
            return Result.error("获取章节题目失败：" + e.getMessage());
        }
    }

    /**
     * 根据类型获取题目
     */
    @GetMapping("/type/{type}")
    public Result<List<QuestionDTO>> getQuestionsByType(@PathVariable String type) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByType(type);
            return Result.success(questions, "获取类型题目成功");
        } catch (Exception e) {
            return Result.error("获取类型题目失败：" + e.getMessage());
        }
    }

    /**
     * 根据难度获取题目
     */
    @GetMapping("/difficulty/{difficulty}")
    public Result<List<QuestionDTO>> getQuestionsByDifficulty(@PathVariable String difficulty) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByDifficulty(difficulty);
            return Result.success(questions, "获取难度题目成功");
        } catch (Exception e) {
            return Result.error("获取难度题目失败：" + e.getMessage());
        }
    }

    /**
     * 搜索题目
     */
    @GetMapping("/search")
    public Result<List<QuestionDTO>> searchQuestions(@RequestParam String keyword) {
        try {
            List<QuestionDTO> questions = questionService.searchQuestions(keyword);
            return Result.success(questions, "搜索题目成功");
        } catch (Exception e) {
            return Result.error("搜索题目失败：" + e.getMessage());
        }
    }

    /**
     * 批量导入题目
     */
    @PostMapping("/import")
    public Result<Boolean> importQuestions(@RequestBody List<Question> questions) {
        try {
            boolean success = questionService.importQuestions(questions);
            if (success) {
                return Result.success(true, "题目导入成功");
            } else {
                return Result.error("题目导入失败");
            }
        } catch (Exception e) {
            return Result.error("题目导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出题目
     */
    @GetMapping("/export")
    public Result<List<QuestionDTO>> exportQuestions() {
        try {
            List<QuestionDTO> questions = questionService.exportQuestions();
            return Result.success(questions, "题目导出成功");
        } catch (Exception e) {
            return Result.error("题目导出失败：" + e.getMessage());
        }
    }

    /**
     * 获取题目统计信息
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getQuestionStats() {
        try {
            int totalQuestions = questionService.getQuestionCount();
            int chapter1Questions = questionService.getQuestionCountByChapter("1");
            int chapter2Questions = questionService.getQuestionCountByChapter("2");
            int chapter3Questions = questionService.getQuestionCountByChapter("3");
            int chapter4Questions = questionService.getQuestionCountByChapter("4");

            Map<String, Object> stats = Map.of(
                    "totalQuestions", totalQuestions,
                    "chapter1Questions", chapter1Questions,
                    "chapter2Questions", chapter2Questions,
                    "chapter3Questions", chapter3Questions,
                    "chapter4Questions", chapter4Questions);

            return Result.success(stats, "获取统计信息成功");
        } catch (Exception e) {
            return Result.error("获取统计信息失败：" + e.getMessage());
        }
    }
}
