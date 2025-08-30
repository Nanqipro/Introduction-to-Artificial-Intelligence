package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.pojo.Question;
import com.goodlab.server.dto.QuestionDTO;
import com.goodlab.server.model.QuizResult;
import com.goodlab.server.service.QuizService;
import com.goodlab.server.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173" })
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/questions/{chapterId}")
    public ApiResponse<List<QuestionDTO>> getQuestionsByChapter(@PathVariable String chapterId) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByChapterId(chapterId);
            return ApiResponse.success(questions);
        } catch (Exception e) {
            return ApiResponse.error("获取题目失败: " + e.getMessage());
        }
    }

    @PostMapping("/results")
    public ApiResponse<QuizResult> saveQuizResult(@RequestBody Map<String, Object> resultData) {
        try {
            QuizResult result = quizService.saveQuizResult(resultData);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("保存答题结果失败: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ApiResponse<List<QuizResult>> getUserQuizHistory() {
        try {
            List<QuizResult> history = quizService.getUserQuizHistory();
            return ApiResponse.success(history);
        } catch (Exception e) {
            return ApiResponse.error("获取答题历史失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getUserStats() {
        try {
            Map<String, Object> stats = quizService.getUserStats();
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error("获取用户统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/leaderboard")
    public ApiResponse<List<QuizResult>> getLeaderboard() {
        try {
            List<QuizResult> leaderboard = quizService.getLeaderboard();
            return ApiResponse.success(leaderboard);
        } catch (Exception e) {
            return ApiResponse.error("获取排行榜失败: " + e.getMessage());
        }
    }

    @GetMapping("/results/{chapterId}")
    public ApiResponse<List<QuizResult>> getResultsByChapter(@PathVariable String chapterId) {
        try {
            List<QuizResult> results = quizService.getResultsByChapter(chapterId);
            return ApiResponse.success(results);
        } catch (Exception e) {
            return ApiResponse.error("获取章节答题结果失败: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("答题系统运行正常");
    }

    /**
     * 从数据库获取章节题目
     */
    @GetMapping("/db-questions/{chapterId}")
    public ApiResponse<List<QuestionDTO>> getQuestionsFromDB(@PathVariable String chapterId) {
        try {
            List<QuestionDTO> questions = questionService.getQuestionsByChapterId(chapterId);
            return ApiResponse.success(questions);
        } catch (Exception e) {
            return ApiResponse.error("从数据库获取题目失败: " + e.getMessage());
        }
    }

    /**
     * 获取题目统计信息
     */
    @GetMapping("/question-stats")
    public ApiResponse<Map<String, Object>> getQuestionStats() {
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

            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error("获取题目统计失败: " + e.getMessage());
        }
    }
}