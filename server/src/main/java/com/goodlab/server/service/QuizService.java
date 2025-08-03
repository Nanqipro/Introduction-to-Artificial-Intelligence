package com.goodlab.server.service;

import com.goodlab.server.dao.QuestionDao;
import com.goodlab.server.dao.QuizResultDao;
import com.goodlab.server.model.Question;
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
        result.setAccuracy((Double) resultData.get("accuracy"));
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