package com.goodlab.server.pojo;

/**
 * 学生信息实体类
 * 用于存储从Excel文件中解析的学生基本信息
 */
public class StudentInfo {
    
    private String studentId;  // 学号
    private String name;       // 姓名
    private String gender;     // 性别
    private String sheetName;  // 所在工作表名称
    
    public StudentInfo() {}
    
    public StudentInfo(String studentId, String name, String gender, String sheetName) {
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.sheetName = sheetName;
    }
    
    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getSheetName() {
        return sheetName;
    }
    
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
    
    @Override
    public String toString() {
        return "StudentInfo{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", sheetName='" + sheetName + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        StudentInfo that = (StudentInfo) o;
        return studentId != null ? studentId.equals(that.studentId) : that.studentId == null;
    }
    
    @Override
    public int hashCode() {
        return studentId != null ? studentId.hashCode() : 0;
    }
}