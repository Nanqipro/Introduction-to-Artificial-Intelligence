package com.goodlab.server.service;

import com.goodlab.server.dao.QuestionDao;
import com.goodlab.server.dao.QuizResultDao;
import com.goodlab.server.pojo.Question;
import com.goodlab.server.model.QuizResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private QuizResultDao quizResultDao;

    public List<Question> getQuestionsByChapter(String chapterId) {
        return questionDao.getQuestionsByChapter(chapterId);
    }

    public QuizResult saveQuizResult(Map<String, Object> resultData) {
        QuizResult result = new QuizResult();
        result.setChapterId((String) resultData.get("chapterId"));
        result.setScore((Integer) resultData.get("score"));

        // 安全地转换accuracy字段，支持Integer和Double
        Object accuracyObj = resultData.get("accuracy");
        if (accuracyObj instanceof Integer) {
            result.setAccuracy(((Integer) accuracyObj).doubleValue());
        } else if (accuracyObj instanceof Double) {
            result.setAccuracy((Double) accuracyObj);
        } else if (accuracyObj instanceof Number) {
            result.setAccuracy(((Number) accuracyObj).doubleValue());
        } else {
            result.setAccuracy(0.0); // 默认值
        }

        result.setCorrectCount((Integer) resultData.get("correctCount"));
        result.setTotalQuestions((Integer) resultData.get("totalQuestions"));
        result.setAnswers((List<Map<String, Object>>) resultData.get("answers"));
        result.setRewards((List<Map<String, Object>>) resultData.get("rewards"));

        return quizResultDao.save(result);
    }

    public List<QuizResult> getUserQuizHistory() {
        return quizResultDao.getAllResults();
    }

    public Map<String, Object> getUserStats() {
        return quizResultDao.getUserStats();
    }

    public List<QuizResult> getLeaderboard() {
        return quizResultDao.getTopResults(10);
    }

    public List<QuizResult> getResultsByChapter(String chapterId) {
        return quizResultDao.getResultsByChapter(chapterId);
    }
}