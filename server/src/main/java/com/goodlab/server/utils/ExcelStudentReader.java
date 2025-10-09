package com.goodlab.server.utils;

import com.goodlab.server.pojo.StudentInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Excel学号读取工具类
 * 用于从Excel文件中读取学号数据并进行验证
 */
@Component
public class ExcelStudentReader {
    
    // 学号格式验证正则表达式（10-12位数字）
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^\\d{10,12}$");
    
    // Excel文件路径
    private static final String EXCEL_FILE_PATH = "/Users/wulinxin/Desktop/其他项目/电子教材/Introduction-to-Artificial-Intelligence-master/分课程按上课班级打印成绩登记册.xls";
    
    // 测试CSV文件路径
    private static final String TEST_CSV_PATH = "/Users/wulinxin/Desktop/其他项目/电子教材/Introduction-to-Artificial-Intelligence-master/test_students.csv";
    
    // 缓存学号集合，避免重复读取文件
    private static Set<String> cachedStudentIds = null;
    
    // 缓存学生详细信息，避免重复读取文件
    private static List<StudentInfo> cachedStudentInfos = null;
    
    /**
     * 获取所有学生详细信息
     * @return 学生信息列表
     */
    public List<StudentInfo> getAllStudentInfos() {
        if (cachedStudentInfos != null) {
            return new ArrayList<>(cachedStudentInfos);
        }
        
        cachedStudentInfos = readStudentInfosFromExcel();
        return new ArrayList<>(cachedStudentInfos);
    }
    
    /**
     * 根据学号获取学生信息
     * @param studentId 学号
     * @return 学生信息，如果不存在返回null
     */
    public StudentInfo getStudentInfo(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        
        List<StudentInfo> studentInfos = getAllStudentInfos();
        return studentInfos.stream()
                .filter(info -> studentId.equals(info.getStudentId()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 获取所有有效学号
     * @return 学号集合
     */
    public Set<String> getAllStudentIds() {
        if (cachedStudentIds != null) {
            return new HashSet<>(cachedStudentIds);
        }
        
        // 从学生信息中提取学号
        List<StudentInfo> studentInfos = getAllStudentInfos();
        cachedStudentIds = studentInfos.stream()
                .map(StudentInfo::getStudentId)
                .collect(java.util.stream.Collectors.toSet());
        
        return new HashSet<>(cachedStudentIds);
    }
    
    /**
     * 验证学号是否存在于Excel文件中
     * @param studentId 学号
     * @return 是否存在
     */
    public boolean isValidStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        
        // 首先验证格式
        if (!STUDENT_ID_PATTERN.matcher(studentId).matches()) {
            return false;
        }
        
        // 然后验证是否在Excel文件中
        Set<String> studentIds = getAllStudentIds();
        return studentIds.contains(studentId);
    }
    
    /**
     * 从Excel文件中读取学生详细信息
     * @return 学生信息列表
     */
    private List<StudentInfo> readStudentInfosFromExcel() {
        List<StudentInfo> studentInfos = new ArrayList<>();
        
        // 先尝试读取CSV测试文件
        try {
            return readStudentInfosFromCSV();
        } catch (Exception e) {
            System.out.println("读取CSV文件失败，尝试读取Excel文件: " + e.getMessage());
        }
        
        // 如果CSV文件读取失败，尝试读取HTML格式的Excel文件
        try {
            return readStudentInfosFromHTML();
        } catch (Exception e) {
            System.out.println("读取HTML格式Excel文件失败: " + e.getMessage());
        }
        
        return studentInfos;
    }
    
    /**
     * 从CSV文件读取学生信息
     */
    private List<StudentInfo> readStudentInfosFromCSV() throws IOException {
        List<StudentInfo> studentInfos = new ArrayList<>();
        
        System.out.println("开始读取CSV文件: " + TEST_CSV_PATH);
        
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader(TEST_CSV_PATH, java.nio.charset.StandardCharsets.UTF_8))) {
            
            String line = reader.readLine(); // 跳过标题行
            System.out.println("CSV标题行: " + line);
            
            while ((line = reader.readLine()) != null) {
                System.out.println("读取CSV行: " + line);
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String studentId = parts[0].trim();
                    String name = parts[1].trim();
                    String gender = parts[2].trim();
                    
                    System.out.println("解析学号: " + studentId + ", 姓名: " + name + ", 性别: " + gender);
                    
                    if (STUDENT_ID_PATTERN.matcher(studentId).matches()) {
                        StudentInfo info = new StudentInfo(studentId, name, gender, "CSV测试数据");
                        studentInfos.add(info);
                        System.out.println("添加学生信息: " + studentId);
                    } else {
                        System.out.println("学号格式不匹配: " + studentId);
                    }
                }
            }
        }
        
        System.out.println("CSV文件读取完成，共读取 " + studentInfos.size() + " 条学生信息");
        return studentInfos;
    }
    
    /**
     * 从HTML格式的Excel文件读取学生信息
     */
    private List<StudentInfo> readStudentInfosFromHTML() throws IOException {
        List<StudentInfo> studentInfos = new ArrayList<>();
        
        System.out.println("开始读取HTML格式Excel文件: " + EXCEL_FILE_PATH);
        
        // 使用正则表达式从HTML中提取学号
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader(EXCEL_FILE_PATH, java.nio.charset.StandardCharsets.UTF_8))) {
            
            String line;
            String currentStudentId = null;
            String currentName = null;
            String currentGender = null;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                // 查找包含学号的行（10-12位数字）
                java.util.regex.Matcher studentIdMatcher = java.util.regex.Pattern.compile("x:str>([0-9]{10,12})</td>").matcher(line);
                if (studentIdMatcher.find()) {
                    String studentId = studentIdMatcher.group(1);
                    if (STUDENT_ID_PATTERN.matcher(studentId).matches()) {
                        currentStudentId = studentId;
                        System.out.println("找到学号: " + studentId + " (行号: " + lineNumber + ")");
                    }
                }
                
                // 查找包含姓名的行（中文字符）
                java.util.regex.Matcher nameMatcher = java.util.regex.Pattern.compile("x:str>([\\u4e00-\\u9fa5]{2,4})</td>").matcher(line);
                if (nameMatcher.find() && currentStudentId != null && currentName == null) {
                    currentName = nameMatcher.group(1);
                    System.out.println("找到姓名: " + currentName + " (学号: " + currentStudentId + ")");
                }
                
                // 查找包含性别的行
                java.util.regex.Matcher genderMatcher = java.util.regex.Pattern.compile("x:str>([男女])</td>").matcher(line);
                if (genderMatcher.find() && currentStudentId != null && currentGender == null) {
                    currentGender = genderMatcher.group(1);
                    System.out.println("找到性别: " + currentGender + " (学号: " + currentStudentId + ")");
                }
                
                // 当找到完整的学生信息时，创建StudentInfo对象
                if (currentStudentId != null && currentName != null && currentGender != null) {
                    StudentInfo info = new StudentInfo(currentStudentId, currentName, currentGender, "Excel数据");
                    studentInfos.add(info);
                    System.out.println("添加学生信息: " + currentStudentId + " - " + currentName + " - " + currentGender);
                    
                    // 重置变量，准备读取下一个学生
                    currentStudentId = null;
                    currentName = null;
                    currentGender = null;
                }
            }
        }
        
        System.out.println("HTML文件读取完成，共读取 " + studentInfos.size() + " 条学生信息");
        return studentInfos;
    }
    
    /**
     * 查找表头行
     * @param sheet 工作表
     * @return 表头行索引，如果没有找到返回-1
     */
    private int findHeaderRow(Sheet sheet) {
        for (int rowIndex = 0; rowIndex <= Math.min(5, sheet.getLastRowNum()); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;
            
            boolean hasStudentId = false, hasName = false, hasGender = false;
            
            for (Cell cell : row) {
                String cellValue = getCellValueAsString(cell);
                if (cellValue != null) {
                    if (cellValue.contains("学号")) hasStudentId = true;
                    if (cellValue.contains("姓名")) hasName = true;
                    if (cellValue.contains("性别")) hasGender = true;
                }
            }
            
            if (hasStudentId && hasName && hasGender) {
                return rowIndex;
            }
        }
        return -1;
    }
    
    /**
     * 查找指定列名的列索引
     * @param headerRow 表头行
     * @param columnName 列名
     * @return 列索引，如果没有找到返回-1
     */
    private int findColumnIndex(Row headerRow, String columnName) {
        if (headerRow == null) return -1;
        
        for (Cell cell : headerRow) {
            String cellValue = getCellValueAsString(cell);
            if (cellValue != null && cellValue.contains(columnName)) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }

    /**
     * 从Excel文件中读取学号
     * @return 学号集合
     */
    private Set<String> readStudentIdsFromExcel() {
        Set<String> studentIds = new HashSet<>();
        
        try (FileInputStream fis = new FileInputStream(EXCEL_FILE_PATH)) {
            Workbook workbook;
            
            // 根据文件扩展名选择合适的工作簿类型
            if (EXCEL_FILE_PATH.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else {
                workbook = new HSSFWorkbook(fis);
            }
            
            // 遍历所有工作表
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                
                // 遍历所有行
                for (Row row : sheet) {
                    if (row == null) continue;
                    
                    // 遍历所有单元格
                    for (Cell cell : row) {
                        if (cell == null) continue;
                        
                        String cellValue = getCellValueAsString(cell);
                        if (cellValue != null && STUDENT_ID_PATTERN.matcher(cellValue).matches()) {
                            studentIds.add(cellValue);
                        }
                    }
                }
            }
            
            workbook.close();
            
        } catch (IOException e) {
            System.err.println("读取Excel文件失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return studentIds;
    }
    
    /**
     * 获取单元格值作为字符串
     * @param cell 单元格
     * @return 字符串值
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // 对于数字类型，转换为整数字符串（去除小数点）
                double numericValue = cell.getNumericCellValue();
                if (numericValue == (long) numericValue) {
                    return String.valueOf((long) numericValue);
                } else {
                    return String.valueOf(numericValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    // 尝试获取公式的计算结果
                    return getCellValueAsString(cell);
                } catch (Exception e) {
                    return null;
                }
            default:
                return null;
        }
    }
    
    /**
     * 清除缓存，强制重新读取Excel文件
     */
    public void clearCache() {
        System.out.println("清除学生信息缓存");
        cachedStudentIds = null;
        cachedStudentInfos = null;
    }
    
    /**
     * 获取学号总数
     * @return 学号数量
     */
    public int getStudentCount() {
        return getAllStudentIds().size();
    }
    
    /**
     * 验证密码格式是否正确
     * @param studentId 学号
     * @param password 密码
     * @return 是否匹配预期格式
     */
    public boolean isValidPasswordFormat(String studentId, String password) {
        if (studentId == null || password == null) {
            return false;
        }
        
        String expectedPassword = studentId + "@ncu2025";
        return expectedPassword.equals(password);
    }
}