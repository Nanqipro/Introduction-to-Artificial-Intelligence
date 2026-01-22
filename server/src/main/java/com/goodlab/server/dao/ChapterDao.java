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
        
        // 续章：生活中的人工智能
        chapters.put("0", new Chapter(
            "0", "0", "生活中的人工智能",
            "从日常生活场景引入人工智能概念，展示AI在生活中的实际应用",
            "在日常生活中，当提及人工智能，许多人脑海中可能会浮现出科幻电影中的机器人场景。然而，实际上，人工智能的形态和应用远比这些想象更加丰富多样且贴近人们的生活。从清晨的闹铃响起，到音乐播放器的智能推荐，再到出行时的导航系统和智能交通信号灯，人工智能已经悄然融入我们生活的方方面面...",
            "prologue", 0, true, currentTime, currentTime
        ));

        // 第1章：初识人工智能
        chapters.put("1", new Chapter(
            "1", "1", "初识人工智能",
            "全面介绍人工智能的定义、智能概念、发展历史、分类方式和未来展望",
            "在当今科技飞速发展的时代，一场震撼世界的变革正汹涌来袭——人工智能蓬勃兴起。本章将从人工智能的定义开始，探讨智能在不同领域的含义，包括通识领域、心理学领域和人工智能研究领域的定义。通过图灵测试和中文屋思想实验来理解机器智能的本质。回顾人工智能的三次发展热潮：20世纪50-70年代的第一次热潮、80-90年代的第二次热潮，以及90年代末至今的第三次热潮。最后介绍人工智能的多种分类方式：基于能力的分类（弱人工智能、强人工智能、超人工智能）、基于学派的分类（符号学派、联结学派、贝叶斯学派、类推学派）和基于关键技术的分类（计算机视觉、语音识别与自然语言处理、生成式人工智能）...",
            "chapter", 1, true, currentTime, currentTime
        ));

        // 第2章：人工智能基础
        chapters.put("2", new Chapter(
            "2", "2", "人工智能基础",
            "深入了解人工智能、机器学习、深度学习的关系及机器学习的基础概念",
            "本章将深入探讨人工智能、机器学习和深度学习三者之间的关系，帮助读者建立清晰的概念框架。详细介绍机器学习的定义、基本原理、主要分类方法（监督学习、无监督学习、强化学习）以及评估方法。通过具体案例和实例，让读者理解机器学习如何作为人工智能的核心技术，以及深度学习作为机器学习的重要分支如何推动AI技术的发展...",
            "chapter", 2, true, currentTime, currentTime
        ));

        // 第3章：人工智能核心技术
        chapters.put("3", new Chapter(
            "3", "3", "人工智能核心技术",
            "探讨计算机视觉、自然语言智能和生成式人工智能等核心技术领域",
            "本章深入探讨支撑人工智能发展的三大核心技术领域。首先介绍计算机视觉技术，包括其奠基者的贡献和主要应用；然后探讨自然语言智能，涵盖语音识别、自然语言处理等技术；最后重点介绍当前热门的生成式人工智能技术，包括其原理、应用和发展趋势。这些核心技术是将人工智能从理论推向实际应用的关键驱动力...",
            "chapter", 3, true, currentTime, currentTime
        ));

        // 第4章：人工智能的应用
        chapters.put("4", new Chapter(
            "4", "4", "人工智能的应用",
            "展示人工智能在智慧生活、智能家居等领域的实际应用案例",
            "在快速发展的科技时代，人工智能技术正在深刻改变人们的生活方式。本章重点介绍人工智能在智慧生活领域的应用，特别是智能家居系统的发展和应用。详细解析家庭自动化系统的工作原理，包括传感器网络、控制系统和用户界面的协同工作。通过具体案例展示AI如何提升生活质量，创造更加便捷、舒适和安全的居住环境...",
            "chapter", 4, true, currentTime, currentTime
        ));

        // 第5章：人工智能的提示工程
        chapters.put("5", new Chapter(
            "5", "5", "人工智能的提示工程",
            "介绍提示工程的定义、发展历程、核心技巧和实际应用方法",
            "在人工智能蓬勃发展的当下，提示工程这一概念应运而生，它如同一把精巧的钥匙，开启了人工智能应用的全新大门。本章将详细介绍提示工程的定义与内涵，回顾其发展历程，深入探讨各种提示工程技巧，包括如何设计有效的提示词、优化交互方式等。同时通过丰富的实际案例展示提示工程在不同领域的应用，帮助读者掌握与AI系统高效交互的方法...",
            "chapter", 5, true, currentTime, currentTime
        ));

        // 第6章：第一个人工智能项目
        chapters.put("6", new Chapter(
            "6", "6", "第一个人工智能项目",
            "通过Python编程语言学习和实际项目实践，掌握人工智能开发技能",
            "人工智能的学习不仅涉及理论的掌握，更注重通过实践将知识转化为解决实际问题的能力。本章首先介绍Python作为人工智能编程语言的特点和优势，包括其简洁的语法、丰富的库生态和较低的学习曲线。然后通过一个完整的人工智能项目实践，让读者亲手体验从问题分析、数据处理、模型构建到结果评估的完整开发流程，为后续的深入学习奠定坚实基础...",
            "chapter", 6, true, currentTime, currentTime
        ));

        // 第7章：人工智能的思考
        chapters.put("7", new Chapter(
            "7", "7", "人工智能的思考",
            "深入探讨人工智能的伦理问题、算法歧视现象和社会影响",
            "人工智能技术是一种极具颠覆性的技术，正在深刻改变人类的未来。随着AI在社会各个领域中的广泛应用，我们必须认真思考其带来的伦理和社会问题。本章重点探讨算法歧视问题，包括其概念和具体表现形式，分析有损群体包容性的偏见、有损群体公平性的偏见以及有损个体利益的偏见等不同类型。通过深入分析这些问题，帮助读者建立对AI技术的全面认识，促进AI技术的健康发展...",
            "chapter", 7, true, currentTime, currentTime
        ));

        // 附录：专业名词解释
        chapters.put("appendix", new Chapter(
            "appendix", "附录", "专业名词解释",
            "提供人工智能领域的专业术语解释和参考资料",
            "本附录收录了书中涉及的重要专业术语和概念的详细解释，包括技术术语、算法与模型相关术语、数据处理与评估术语等，为读者提供便捷的查阅参考...",
            "appendix", 8, true, currentTime, currentTime
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
