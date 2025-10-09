package com.goodlab.server.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_learning_record")
public class UserLearningRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键ID
    
    @Column(name = "user_id")
    private Integer userId;//用户ID
    
    @Column(name = "chapter_id")
    private Integer chapterId;//章节ID
    
    @Column(name = "activity_type")
    private String activityType;//活动类型(quiz,chapter,experiment)
    
    private Integer score = 0;//得分
    
    @Column(name = "experience_gained")
    private Integer experienceGained = 0;//获得经验值
    
    @Column(name = "completion_time")
    private LocalDateTime completionTime;//完成时间
    
    @Column(name = "create_time")
    private LocalDateTime createTime;//创建时间
}