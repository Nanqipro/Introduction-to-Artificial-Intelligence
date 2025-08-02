package com.goodlab.server.service;

import com.goodlab.server.dto.QuestionImportDto;
import com.goodlab.server.model.Question;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ExcelService {

    public List<QuestionImportDto> parseExcelFile(MultipartFile file) throws IOException {
        List<QuestionImportDto> questions = new ArrayList<>();

        try (InputStream is = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            // 跳过标题行，从第二行开始读取
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                QuestionImportDto question = parseRow(row);
                if (question != null && question.getTitle() != null && !question.getTitle().trim().isEmpty()) {
                    questions.add(question);
                }
            }
        }

        return questions;
    }

    private QuestionImportDto parseRow(Row row) {
        try {
            String chapterId = getCellValueAsString(row.getCell(0));
            String type = getCellValueAsString(row.getCell(1));
            String title = getCellValueAsString(row.getCell(2));
            String description = getCellValueAsString(row.getCell(3));
            String optionsStr = getCellValueAsString(row.getCell(4));
            String correctAnswer = getCellValueAsString(row.getCell(5));
            String pointsStr = getCellValueAsString(row.getCell(6));
            String explanation = getCellValueAsString(row.getCell(7));

            // 解析选项
            List<String> options = null;
            if (optionsStr != null && !optionsStr.trim().isEmpty()) {
                options = Arrays.asList(optionsStr.split("\\|"));
            }

            // 解析分值
            Integer points = null;
            if (pointsStr != null && !pointsStr.trim().isEmpty()) {
                try {
                    points = Integer.parseInt(pointsStr.trim());
                } catch (NumberFormatException e) {
                    points = 20; // 默认分值
                }
            } else {
                points = 20; // 默认分值
            }

            // 处理正确答案
            if ("true-false".equals(type)) {
                // 判断题：true/false
                if ("true".equalsIgnoreCase(correctAnswer) || "正确".equals(correctAnswer)) {
                    correctAnswer = "true";
                } else if ("false".equalsIgnoreCase(correctAnswer) || "错误".equals(correctAnswer)) {
                    correctAnswer = "false";
                }
            } else if ("choice".equals(type)) {
                // 选择题：A=0, B=1, C=2, D=3
                if ("A".equalsIgnoreCase(correctAnswer)) {
                    correctAnswer = "0";
                } else if ("B".equalsIgnoreCase(correctAnswer)) {
                    correctAnswer = "1";
                } else if ("C".equalsIgnoreCase(correctAnswer)) {
                    correctAnswer = "2";
                } else if ("D".equalsIgnoreCase(correctAnswer)) {
                    correctAnswer = "3";
                }
            }

            return new QuestionImportDto(chapterId, type, title, description, options, correctAnswer, points,
                    explanation);

        } catch (Exception e) {
            System.err.println("解析行数据失败: " + e.getMessage());
            return null;
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    public Question convertToQuestion(QuestionImportDto dto, Long id) {
        Question question = new Question();
        question.setId(id);
        question.setChapterId(dto.getChapterId());
        question.setType(dto.getType());
        question.setTitle(dto.getTitle());
        question.setDescription(dto.getDescription());
        question.setOptions(dto.getOptions());

        // 处理正确答案
        if ("true-false".equals(dto.getType())) {
            question.setCorrectAnswer(Boolean.parseBoolean(dto.getCorrectAnswer()));
        } else if ("choice".equals(dto.getType())) {
            question.setCorrectAnswer(Integer.parseInt(dto.getCorrectAnswer()));
        } else {
            question.setCorrectAnswer(dto.getCorrectAnswer());
        }

        question.setPoints(dto.getPoints());
        question.setExplanation(dto.getExplanation());

        return question;
    }
}