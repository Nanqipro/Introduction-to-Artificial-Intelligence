package com.goodlab.server.service.impl;

import com.goodlab.server.mapper.QuestionMapper;
import com.goodlab.server.pojo.Question;
import com.goodlab.server.dto.QuestionDTO;
import com.goodlab.server.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 题目服务实现类
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public Question createQuestion(Question question) {
        // 设置默认值
        if (question.getIsActive() == null) {
            question.setIsActive(true);
        }
        if (question.getScore() == null) {
            question.setScore(10);
        }
        if (question.getCreateTime() == null) {
            question.setCreateTime(LocalDateTime.now());
        }
        if (question.getUpdateTime() == null) {
            question.setUpdateTime(LocalDateTime.now());
        }

        questionMapper.insert(question);
        return question;
    }

    @Override
    public Question updateQuestion(Question question) {
        question.setUpdateTime(LocalDateTime.now());
        questionMapper.updateById(question);
        return question;
    }

    @Override
    public boolean deleteQuestion(Long id) {
        // 软删除：设置is_active为false
        Question question = new Question();
        question.setId(id);
        question.setIsActive(false);
        question.setUpdateTime(LocalDateTime.now());

        return questionMapper.updateById(question) > 0;
    }

    @Override
    public Question getQuestionById(Long id) {
        return questionMapper.selectById(id);
    }

    @Override
    public List<QuestionDTO> getAllQuestions(int page, int size) {
        int offset = (page - 1) * size;
        return questionMapper.selectAll(offset, size);
    }

    @Override
    public List<QuestionDTO> getQuestionsByChapterId(String chapterId) {
        return questionMapper.selectByChapterId(chapterId);
    }

    @Override
    public List<QuestionDTO> getQuestionsByType(String type) {
        return questionMapper.selectByType(type);
    }

    @Override
    public List<QuestionDTO> getQuestionsByDifficulty(String difficulty) {
        return questionMapper.selectByDifficulty(difficulty);
    }

    @Override
    public List<QuestionDTO> searchQuestions(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllQuestions(1, 100); // 返回前100题
        }
        return questionMapper.searchQuestions(keyword.trim());
    }

    @Override
    public int getQuestionCount() {
        return questionMapper.countAll();
    }

    @Override
    public int getQuestionCountByChapter(String chapterId) {
        return questionMapper.countByChapterId(chapterId);
    }

    @Override
    public boolean importQuestions(List<Question> questions) {
        try {
            for (Question question : questions) {
                createQuestion(question);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<QuestionDTO> exportQuestions() {
        return questionMapper.selectAll(0, Integer.MAX_VALUE);
    }
}
