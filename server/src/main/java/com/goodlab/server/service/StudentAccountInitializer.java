package com.goodlab.server.service;

import com.goodlab.server.mapper.UserMapper;
import com.goodlab.server.pojo.StudentInfo;
import com.goodlab.server.pojo.User;
import com.goodlab.server.utils.ExcelStudentReader;
import com.goodlab.server.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生账户初始化服务
 * 用于批量初始化学生账户
 */
@Service
public class StudentAccountInitializer {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ExcelStudentReader excelStudentReader;
    
    /**
     * 批量初始化学生账户
     * @return 初始化结果信息
     */
    public String initializeStudentAccounts() {
        List<StudentInfo> studentInfos = excelStudentReader.getAllStudentInfos();
        
        if (studentInfos.isEmpty()) {
            return "未找到有效的学生信息";
        }
        
        int successCount = 0;
        int skipCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (StudentInfo studentInfo : studentInfos) {
            try {
                // 检查用户是否已存在
                User existingUser = userMapper.findByUserName(studentInfo.getStudentId());
                if (existingUser != null) {
                    skipCount++;
                    continue;
                }
                
                // 创建新用户 - 使用正确的add方法
                String initialPassword = studentInfo.getStudentId() + "@ncu2025";
                String encodedPassword = Md5Util.getMD5String(initialPassword);
                
                // 插入用户
                userMapper.add(studentInfo.getStudentId(), encodedPassword);
                
                successCount++;
                
            } catch (Exception e) {
                errors.add("学号 " + studentInfo.getStudentId() + " 初始化失败: " + e.getMessage());
            }
        }
        
        StringBuilder result = new StringBuilder();
        result.append("学生账户初始化完成！\n");
        result.append("总计学生数: ").append(studentInfos.size()).append("\n");
        result.append("成功初始化: ").append(successCount).append("\n");
        result.append("跳过已存在: ").append(skipCount).append("\n");
        
        if (!errors.isEmpty()) {
            result.append("错误信息:\n");
            for (String error : errors) {
                result.append("- ").append(error).append("\n");
            }
        }
        
        return result.toString();
    }
    
    /**
     * 重置指定学生的密码为初始密码
     * @param studentId 学号
     * @return 重置结果
     */
    public String resetStudentPassword(String studentId) {
        try {
            // 验证学号是否有效
            StudentInfo studentInfo = excelStudentReader.getStudentInfo(studentId);
            if (studentInfo == null) {
                return "学号不存在或无效";
            }
            
            // 查找用户
             User user = userMapper.findByUserName(studentId);
            if (user == null) {
                return "用户不存在，请先初始化学生账户";
            }
            
            // 重置为初始密码
            String initialPassword = studentId + "@ncu2025";
            String encodedPassword = Md5Util.getMD5String(initialPassword);
            
            userMapper.updatePwd(encodedPassword, user.getId());
            
            return "密码重置成功，新密码为: " + initialPassword;
            
        } catch (Exception e) {
            return "密码重置失败: " + e.getMessage();
        }
    }
    
    /**
     * 检查给定密码是否为指定学生的初始密码
     * @param studentId 学号
     * @param password 密码
     * @return 是否为初始密码
     */
    public boolean isInitialPassword(String studentId, String password) {
        return excelStudentReader.isValidPasswordFormat(studentId, password);
    }

    /**
     * 获取学生信息统计
     * @return 学生信息统计
     */
    public Object getStudentStats() {
        List<StudentInfo> studentInfos = excelStudentReader.getAllStudentInfos();
        
        // 统计Excel中的学生数量
        int totalStudentsInExcel = studentInfos.size();
        
        // 统计已导入的学生数量
        int importedCount = 0;
        for (StudentInfo studentInfo : studentInfos) {
            User existingUser = userMapper.findByUserName(studentInfo.getStudentId());
            if (existingUser != null) {
                importedCount++;
            }
        }
        
        // 创建统计结果
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalStudentsInExcel", totalStudentsInExcel);
        stats.put("importedStudents", importedCount);
        stats.put("notImportedStudents", totalStudentsInExcel - importedCount);
        stats.put("importProgress", totalStudentsInExcel > 0 ? 
            String.format("%.2f%%", (double) importedCount / totalStudentsInExcel * 100) : "0%");
        
        return stats;
    }

    /**
     * 初始化结果统计类
     */
    public static class InitializationResult {
        private final int totalCount;
        private final int createdCount;
        private final int existingCount;
        private final int errorCount;

        public InitializationResult(int totalCount, int createdCount, int existingCount, int errorCount) {
            this.totalCount = totalCount;
            this.createdCount = createdCount;
            this.existingCount = existingCount;
            this.errorCount = errorCount;
        }

        public int getTotalCount() { return totalCount; }
        public int getCreatedCount() { return createdCount; }
        public int getExistingCount() { return existingCount; }
        public int getErrorCount() { return errorCount; }

        @Override
        public String toString() {
            return String.format("初始化完成 - 总计: %d, 新建: %d, 已存在: %d, 错误: %d", 
                totalCount, createdCount, existingCount, errorCount);
        }
    }
}