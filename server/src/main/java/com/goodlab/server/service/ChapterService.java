package com.goodlab.server.service;

import com.goodlab.server.dao.ChapterDao;
import com.goodlab.server.model.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 章节服务层
 */
@Service
public class ChapterService {
    
    @Autowired
    private ChapterDao chapterDao;
    
    /**
     * 获取所有已发布的章节
     */
    public List<Chapter> getAllPublishedChapters() {
        return chapterDao.findPublished();
    }
    
    /**
     * 获取所有章节（包括未发布）
     */
    public List<Chapter> getAllChapters() {
        return chapterDao.findAll();
    }
    
    /**
     * 根据ID获取章节
     */
    public Optional<Chapter> getChapterById(String id) {
        return chapterDao.findById(id);
    }
    
    /**
     * 获取章节概览（不包含完整内容）
     */
    public List<Chapter> getChapterOverview() {
        List<Chapter> chapters = chapterDao.findPublished();
        // 移除完整内容，只保留概览信息
        chapters.forEach(chapter -> chapter.setContent(null));
        return chapters;
    }
    
    /**
     * 创建新章节
     */
    public Chapter createChapter(Chapter chapter) {
        return chapterDao.save(chapter);
    }
    
    /**
     * 更新章节
     */
    public Chapter updateChapter(Chapter chapter) {
        return chapterDao.save(chapter);
    }
    
    /**
     * 删除章节
     */
    public boolean deleteChapter(String id) {
        return chapterDao.deleteById(id);
    }
    
    /**
     * 检查章节是否存在
     */
    public boolean chapterExists(String id) {
        return chapterDao.findById(id).isPresent();
    }
}
