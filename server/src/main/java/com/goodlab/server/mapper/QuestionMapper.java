package com.goodlab.server.mapper;

import com.goodlab.server.pojo.Question;
import com.goodlab.server.dto.QuestionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 题目数据访问接口
 */
@Mapper
public interface QuestionMapper {

    /**
     * 插入新题目
     */
    int insert(Question question);

    /**
     * 根据ID更新题目
     */
    int updateById(Question question);

    /**
     * 根据ID删除题目
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据ID查询题目
     */
    Question selectById(@Param("id") Long id);

    /**
     * 查询所有题目（分页）
     */
    List<QuestionDTO> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据章节ID查询题目
     */
    List<QuestionDTO> selectByChapterId(@Param("chapterId") String chapterId);

    /**
     * 根据类型查询题目
     */
    List<QuestionDTO> selectByType(@Param("type") String type);

    /**
     * 根据难度查询题目
     */
    List<QuestionDTO> selectByDifficulty(@Param("difficulty") String difficulty);

    /**
     * 搜索题目
     */
    List<QuestionDTO> searchQuestions(@Param("keyword") String keyword);

    /**
     * 统计题目总数
     */
    int countAll();

    /**
     * 根据章节统计题目数量
     */
    int countByChapterId(@Param("chapterId") String chapterId);
}
