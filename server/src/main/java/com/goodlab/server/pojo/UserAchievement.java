package com.goodlab.server.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_achievement")
public class UserAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键ID
    
    @Column(name = "user_id")
    private Integer userId;//用户ID
    
    @Column(name = "achievement_type")
    private String achievementType;//成就类型
    
    @Column(name = "achievement_name")
    private String achievementName;//成就名称
    
    @Column(name = "achievement_desc")
    private String achievementDesc;//成就描述
    
    @Column(name = "earned_date")
    private LocalDateTime earnedDate;//获得时间
}