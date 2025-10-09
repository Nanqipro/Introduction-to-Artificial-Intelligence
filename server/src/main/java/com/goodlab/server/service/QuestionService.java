package com.goodlab.server.service;

import com.goodlab.server.pojo.Question;
import com.goodlab.server.dto.QuestionDTO;
import java.util.List;

/**
 * 题目服务接口
 */
public interface QuestionService {

    /**
     * 创建题目
     */
    Question createQuestion(Question question);

    /**
     * 更新题目
     */
    Question updateQuestion(Question question);

    /**
     * 删除题目
     */
    boolean deleteQuestion(Long id);

    /**
     * 根据ID获取题目
     */
    Question getQuestionById(Long id);

    /**
     * 获取所有题目（分页）
     */
    List<QuestionDTO> getAllQuestions(int page, int size);

    /**
     * 根据章节ID获取题目
     */
    List<QuestionDTO> getQuestionsByChapterId(String chapterId);

    /**
     * 根据类型获取题目
     */
    List<QuestionDTO> getQuestionsByType(String type);

    /**
     * 根据难度获取题目
     */
    List<QuestionDTO> getQuestionsByDifficulty(String difficulty);

    /**
     * 搜索题目
     */
    List<QuestionDTO> searchQuestions(String keyword);

    /**
     * 获取题目总数
     */
    int getQuestionCount();

    /**
     * 根据章节获取题目数量
     */
    int getQuestionCountByChapter(String chapterId);

    /**
     * 批量导入题目
     */
    boolean importQuestions(List<Question> questions);

    /**
     * 导出题目
     */
    List<QuestionDTO> exportQuestions();
}
