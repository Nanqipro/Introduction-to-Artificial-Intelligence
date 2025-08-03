package com.goodlab.server.service;

import com.goodlab.server.dao.QuestionDao;
import com.goodlab.server.dto.QuestionImportDto;
import com.goodlab.server.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class AdminService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private ExcelService excelService;

    public List<Question> getAllQuestions() {
        return questionDao.getAllQuestions();
    }

    public Question getQuestionById(Long id) {
        return questionDao.getQuestionById(id);
    }

    public Question saveQuestion(Question question) {
        return questionDao.saveQuestion(question);
    }

    public boolean deleteQuestion(Long id) {
        return questionDao.deleteQuestion(id);
    }

    public Map<String, Object> importQuestionsFromExcel(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 解析Excel文件
            List<QuestionImportDto> questionDtos = excelService.parseExcelFile(file);

            if (questionDtos.isEmpty()) {
                result.put("success", false);
                result.put("message", "Excel文件中没有找到有效的题目数据");
                return result;
            }

            // 转换为Question对象并保存
            List<Question> questions = new ArrayList<>();
            for (QuestionImportDto dto : questionDtos) {
                Question question = excelService.convertToQuestion(dto, null);
                if (question != null) {
                    questions.add(question);
                }
            }

            // 批量导入
            questionDao.importQuestions(questions);

            result.put("success", true);
            result.put("message", "成功导入 " + questions.size() + " 道题目");
            result.put("importedCount", questions.size());
            result.put("totalCount", questionDtos.size());

        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "文件读取失败: " + e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "导入失败: " + e.getMessage());
        }

        return result;
    }

    public Map<String, Object> getQuestionStats() {
        Map<String, Object> stats = new HashMap<>();
        List<Question> allQuestions = questionDao.getAllQuestions();

        stats.put("totalQuestions", allQuestions.size());

        // 按章节统计
        Map<String, Integer> chapterStats = new HashMap<>();
        for (Question question : allQuestions) {
            String chapterId = question.getChapterId();
            chapterStats.put(chapterId, chapterStats.getOrDefault(chapterId, 0) + 1);
        }
        stats.put("chapterStats", chapterStats);

        // 按题型统计
        Map<String, Integer> typeStats = new HashMap<>();
        for (Question question : allQuestions) {
            String type = question.getType();
            typeStats.put(type, typeStats.getOrDefault(type, 0) + 1);
        }
        stats.put("typeStats", typeStats);

        return stats;
    }

    public List<Question> getQuestionsByChapter(String chapterId) {
        return questionDao.getQuestionsByChapter(chapterId);
    }
}