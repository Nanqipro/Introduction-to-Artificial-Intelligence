package com.goodlab.server.dao;

import com.goodlab.server.model.Chapter;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 章节数据访问层
 * 目前使用内存存储，后续可扩展为数据库存储
 */
@Repository
public class ChapterDao {
    
    private final Map<String, Chapter> chapters = new HashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public ChapterDao() {
        initializeChapters();
    }
    
    /**
     * 初始化章节数据
     */
    private void initializeChapters() {
        String currentTime = LocalDateTime.now().format(formatter);
        
        // 续章
        chapters.put("0", new Chapter(
            "0", "0", "续章", 
            "生活中的人工智能概述，从日常应用场景引入人工智能概念",
            "在日常生活中，当提及人工智能，许多人脑海中可能会浮现出科幻电影中的机器人场景。然而，实际上，人工智能的形态和应用远比这些想象更加丰富多样且贴近人们的生活...",
            "prologue", 0, true, currentTime, currentTime
        ));
        
        // 第1章
        chapters.put("1", new Chapter(
            "1", "1", "初识人工智能",
            "介绍人工智能的定义、发展历史、分类方式和现状",
            "在当今科技飞速发展的时代，一场震撼世界的变革正汹涌来袭——人工智能蓬勃兴起。本章将全面探讨人工智能的含义、发展历程和分类方式...",
            "chapter", 1, true, currentTime, currentTime
        ));
        
        // 第2章
        chapters.put("2", new Chapter(
            "2", "2", "人工智能基础",
            "深入了解人工智能、机器学习和深度学习的关系及基础概念",
            "人工智能是一门旨在赋予机器模仿和实现人类智能行为的技术，涵盖从基础的逻辑推理到高度复杂的学习过程...",
            "chapter", 2, true, currentTime, currentTime
        ));
        
        // 第3章
        chapters.put("3", new Chapter(
            "3", "3", "人工智能核心技术",
            "探讨计算机视觉、自然语言处理和生成式人工智能等核心技术",
            "在第三章中，将深入探讨支撑人工智能的核心技术，这些技术是将'人工智能'从理论推向实际应用的关键...",
            "chapter", 3, true, currentTime, currentTime
        ));
        
        // 第4章
        chapters.put("4", new Chapter(
            "4", "4", "人工智能的应用",
            "展示人工智能在智慧生活、智慧交通、智慧医疗等领域的应用",
            "在当今快速发展的科技时代，人工智能技术的深度融合正在深刻改变人们的生活方式、出行模式以及医疗健康服务...",
            "chapter", 4, true, currentTime, currentTime
        ));
        
        // 第5章
        chapters.put("5", new Chapter(
            "5", "5", "人工智能的提示工程",
            "介绍提示工程的概念、技巧和实际应用",
            "在人工智能蓬勃发展的当下，提示工程这一概念应运而生，它如同一把精巧的钥匙，开启了人工智能应用的全新大门...",
            "chapter", 5, true, currentTime, currentTime
        ));
        
        // 第6章
        chapters.put("6", new Chapter(
            "6", "6", "第一个人工智能项目",
            "通过实际项目学习Python编程和人工智能开发",
            "人工智能的学习不仅涉及理论的掌握，更注重通过实践将知识转化为解决实际问题的能力...",
            "chapter", 6, true, currentTime, currentTime
        ));
        
        // 第7章
        chapters.put("7", new Chapter(
            "7", "7", "人工智能的思考",
            "探讨人工智能的伦理问题、算法歧视和未来发展",
            "人工智能技术是一种极具颠覆传统、改变人类未来的技术。随着人工智能的迅猛发展，其在社会各个领域中的应用日益广泛...",
            "chapter", 7, true, currentTime, currentTime
        ));
    }
    
    /**
     * 获取所有章节
     */
    public List<Chapter> findAll() {
        return chapters.values().stream()
                .sorted(Comparator.comparing(Chapter::getOrderIndex))
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取章节
     */
    public Optional<Chapter> findById(String id) {
        return Optional.ofNullable(chapters.get(id));
    }
    
    /**
     * 获取已发布的章节
     */
    public List<Chapter> findPublished() {
        return chapters.values().stream()
                .filter(Chapter::getPublished)
                .sorted(Comparator.comparing(Chapter::getOrderIndex))
                .collect(Collectors.toList());
    }
    
    /**
     * 保存章节
     */
    public Chapter save(Chapter chapter) {
        if (chapter.getId() == null) {
            chapter.setId(UUID.randomUUID().toString());
        }
        chapter.setUpdatedAt(LocalDateTime.now().format(formatter));
        chapters.put(chapter.getId(), chapter);
        return chapter;
    }
    
    /**
     * 删除章节
     */
    public boolean deleteById(String id) {
        return chapters.remove(id) != null;
    }
}
