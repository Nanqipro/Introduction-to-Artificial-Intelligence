package com.goodlab.server.dao;

import com.goodlab.server.model.QuizResult;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class QuizResultDao {

    // 模拟数据库存储
    private final Map<Long, QuizResult> results = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public QuizResult save(QuizResult result) {
        if (result.getId() == null) {
            result.setId(idGenerator.getAndIncrement());
        }
        results.put(result.getId(), result);
        return result;
    }

    public List<QuizResult> getAllResults() {
        return new ArrayList<>(results.values());
    }

    public List<QuizResult> getResultsByChapter(String chapterId) {
        return results.values().stream()
                .filter(result -> chapterId.equals(result.getChapterId()))
                .sorted(Comparator.comparing(QuizResult::getCompletedAt).reversed())
                .toList();
    }

    public Optional<QuizResult> getResultById(Long id) {
        return Optional.ofNullable(results.get(id));
    }

    public void deleteResult(Long id) {
        results.remove(id);
    }

    public List<QuizResult> getTopResults(int limit) {
        return results.values().stream()
                .sorted(Comparator.comparing(QuizResult::getScore).reversed())
                .limit(limit)
                .toList();
    }

    public Map<String, Object> getUserStats() {
        if (results.isEmpty()) {
            return Map.of(
                    "totalQuizzes", 0,
                    "averageScore", 0.0,
                    "totalScore", 0,
                    "bestScore", 0,
                    "completedChapters", new HashSet<>());
        }

        double averageScore = results.values().stream()
                .mapToInt(QuizResult::getScore)
                .average()
                .orElse(0.0);

        int totalScore = results.values().stream()
                .mapToInt(QuizResult::getScore)
                .sum();

        int bestScore = results.values().stream()
                .mapToInt(QuizResult::getScore)
                .max()
                .orElse(0);

        Set<String> completedChapters = results.values().stream()
                .map(QuizResult::getChapterId)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        return Map.of(
                "totalQuizzes", results.size(),
                "averageScore", Math.round(averageScore * 100.0) / 100.0,
                "totalScore", totalScore,
                "bestScore", bestScore,
                "completedChapters", completedChapters);
    }
}