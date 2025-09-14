<template>
  <div class="chapter-content">
    <!-- 章节头部 -->
    <header class="chapter-header">
      <div class="chapter-meta">
        <span class="chapter-badge">
          {{ getChapterDisplayNumber(chapter.chapterNumber) }}
        </span>
        <span class="chapter-type">{{ getChapterType(chapter.type) }}</span>
      </div>
      <h1 class="chapter-title">{{ chapter.title }}</h1>
      <p class="chapter-summary">{{ chapter.summary }}</p>
      <div class="chapter-info">
        <span class="info-item">
          <span class="info-label">更新时间:</span>
          <span class="info-value">{{ formatDate(chapter.updatedAt) }}</span>
        </span>
        <span class="info-item">
          <span class="info-label">状态:</span>
          <span class="info-value" :class="{ published: chapter.published }">
            {{ chapter.published ? '已发布' : '未发布' }}
          </span>
        </span>
      </div>
    </header>

    <!-- 章节正文 -->
    <div class="chapter-body">
      <!-- 第一章特殊处理：案例演示 -->
      <div v-if="isChapter1" class="chapter1-special-layout">
        <!-- 第一章案例演示 -->
        <div class="case-study-section">
          <Chapter1CaseStudy 
            :chapter-id="id" 
            @case-completed="onCaseCompleted"
            @all-cases-completed="onAllCasesCompleted"
          />
        </div>
      </div>

      <!-- 序章案例演示：播放预热视频 -->
      <div v-if="isPrologue" class="case-study-section">
        <PrologueCase poster-url="/images/cover_prologue.jpg" />
      </div>

      <!-- 第六章特殊处理：显示交互式内容 -->
      <div v-if="isChapter6" class="interactive-chapter">
        <Chapter6Interactive />
      </div>

      <!-- 其他章节显示普通内容 -->
      <div v-else-if="!isChapter1 && !isPrologue && !isChapter6" class="content-text" v-html="formatContent(chapter.content)"></div>

      <!-- 第二章案例学习 -->
      <div v-if="isChapter2" class="case-study-section">
        <Chapter2CaseStudy 
          :chapter-id="id" 
          @case-completed="onCaseCompleted"
          @all-cases-completed="onAllCasesCompleted"
        />
      </div>

      <!-- 第三章案例学习 -->
      <div v-if="isChapter3" class="case-study-section">
        <Chapter3CaseStudy 
          :chapter-id="id" 
          @case-completed="onCaseCompleted"
          @all-cases-completed="onAllCasesCompleted"
        />
      </div>

      <!-- 第四章案例学习 -->
      <div v-if="isChapter4" class="case-study-section">
        <Chapter4CaseStudy 
          :chapter-id="id" 
          @case-completed="onCaseCompleted"
          @all-cases-completed="onAllCasesCompleted"
        />
      </div>

      <!-- 第七章案例学习 -->
      <div v-if="isChapter7" class="case-study-section">
        <Chapter7CaseStudy 
          :chapter-id="id" 
          @case-completed="onCaseCompleted"
          @all-cases-completed="onAllCasesCompleted"
        />
      </div>

      <!-- 答题系统入口 (序章除外) -->
      <div v-if="!isPrologue" class="quiz-section">
        <div class="quiz-header">
          <h3 class="quiz-title">📚 知识测验</h3>
          <p class="quiz-description">
            参加知识测验来检验学习成果，测试你对本章节内容的理解程度
          </p>
        </div>
        <div class="quiz-actions">
          <button 
            @click="startQuiz" 
            class="btn btn-quiz"
          >
            <span class="btn-icon">🎯</span>
            <span class="btn-text">开始测验</span>
          </button>
          <div class="quiz-info">
            <span class="info-item">
              <span class="info-icon">⏱️</span>
              <span class="info-text">预计用时: 10-15分钟</span>
            </span>
            <span class="info-item">
              <span class="info-icon">🏆</span>
              <span class="info-text">可获得奖励和成就</span>
            </span>
          </div>
        </div>
      </div>

      <!-- 章节导航 -->
      <nav class="chapter-pagination">
        <button 
          v-if="prevChapter" 
          @click="goToChapter(prevChapter.id)"
          class="pagination-btn prev"
        >
          <span class="btn-label">上一章</span>
          <span class="btn-title">{{ prevChapter.title }}</span>
        </button>
        <button 
          v-if="nextChapter" 
          @click="goToChapter(nextChapter.id)"
          class="pagination-btn next"
        >
          <span class="btn-label">下一章</span>
          <span class="btn-title">{{ nextChapter.title }}</span>
        </button>
      </nav>
    </div>
  </div>
</template>

<script>
import PrologueCase from './chapter0/PrologueCase.vue'
import Chapter1CaseStudy from './chapter1/Chapter1CaseStudy.vue'
import Chapter6Interactive from './chapter6/Chapter6Interactive.vue'
import Chapter2CaseStudy from './chapter2/Chapter2CaseStudy.vue'
import Chapter3CaseStudy from './chapter3/Chapter3CaseStudy.vue'
import Chapter4CaseStudy from './chapter4/Chapter4CaseStudy.vue'
import Chapter7CaseStudy from './chapter7/Chapter7CaseStudy.vue'

export default {
  name: 'ChapterDetailContent',
  components: {
    PrologueCase,
    Chapter1CaseStudy,
    Chapter6Interactive,
    Chapter2CaseStudy,
    Chapter3CaseStudy,
    Chapter4CaseStudy,
    Chapter7CaseStudy
  },
  props: {
    chapter: {
      type: Object,
      required: true
    },
    id: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      allChapters: [],
      completedCasesCount: 0,
      allCasesCompleted: false,
      hasScrolledToBottom: false
    }
  },
  computed: {
    isChapter6() {
      return this.chapter && (
        this.chapter.chapterNumber === '6' ||
        this.chapter.title.includes('第一个人工智能项目') ||
        this.id === '6'
      )
    },
    isChapter2() {
      return this.chapter && (
        this.chapter.chapterNumber === '2' ||
        this.chapter.title.includes('机器学习基础') ||
        this.id === '2'
      )
    },
    isChapter1() {
      return this.chapter && (
        this.chapter.chapterNumber === '1' ||
        this.chapter.title.includes('人工智能概论') ||
        this.id === '1'
      )
    },
    isChapter3() {
      return this.chapter && (
        this.chapter.chapterNumber === '3' ||
        this.chapter.title.includes('图像识别') ||
        this.chapter.title.includes('计算机视觉') ||
        this.id === '3'
      )
    },
    isPrologue() {
      if (!this.chapter) return false
      const chapterNumberStr = String(this.chapter.chapterNumber)
      return (
        chapterNumberStr === '0' ||
        this.chapter.type === 'prologue' ||
        this.id === '0'
      )
    },
    isChapter4() {
      return this.chapter && (
        this.chapter.chapterNumber === '4' ||
        this.chapter.title.includes('人工智能应用') ||
        this.chapter.title.includes('智慧生活') ||
        this.chapter.title.includes('智慧驾驶') ||
        this.chapter.title.includes('智慧医疗') ||
        this.id === '4'
      )
    },
    isChapter7() {
      return this.chapter && (
        this.chapter.chapterNumber === '7' ||
        this.chapter.title.includes('人工智能的思考') ||
        this.chapter.title.includes('伦理') ||
        this.chapter.title.includes('算法歧视') ||
        this.id === '7'
      )
    },
    prevChapter() {
      if (!this.chapter || !this.allChapters.length) return null
      const currentIndex = this.allChapters.findIndex(ch => ch.id === this.chapter.id)
      return currentIndex > 0 ? this.allChapters[currentIndex - 1] : null
    },
    nextChapter() {
      if (!this.chapter || !this.allChapters.length) return null
      // 如果是第7章，不显示下一章按钮
      if (this.isChapter7) return null
      const currentIndex = this.allChapters.findIndex(ch => ch.id === this.chapter.id)
      return currentIndex < this.allChapters.length - 1 ? this.allChapters[currentIndex + 1] : null
    }
  },
  async mounted() {
    await this.loadAllChapters()
    // 为第三章添加滚动监听
    if (this.isChapter3) {
      this.addScrollListener()
    }
  },
  beforeUnmount() {
    // 清理滚动监听器
    if (this.isChapter3) {
      this.removeScrollListener()
    }
  },
  methods: {
    async loadAllChapters() {
      try {
        const { chapterApi } = await import('../services/api')
        const response = await chapterApi.getChapterOverview()
        this.allChapters = response.data || []
      } catch (error) {
        console.error('加载章节列表失败:', error)
      }
    },
    goToChapter(id) {
      this.$router.push(`/chapters/${id}`).then(() => {
        // 跳转后滚动到页面顶部
        this.$nextTick(() => {
          window.scrollTo({ top: 0, behavior: 'smooth' })
        })
      })
    },
    startQuiz() {
      // 直接跳转到答题页面，移除所有限制条件
      this.$router.push(`/quiz/${this.id}`)
    },
    addScrollListener() {
      this.handleScroll = () => {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop
        const windowHeight = window.innerHeight
        const documentHeight = document.documentElement.scrollHeight
        
        // 检查是否滚动到底部（允许50px的误差）
        if (scrollTop + windowHeight >= documentHeight - 50) {
          this.hasScrolledToBottom = true
          this.$message({
             message: '✅ 已查看完整内容，现在可以开始测验了！',
             type: 'success',
             duration: 1000
           })
          // 移除监听器，避免重复提示
          this.removeScrollListener()
        }
      }
      window.addEventListener('scroll', this.handleScroll)
    },
    removeScrollListener() {
      if (this.handleScroll) {
        window.removeEventListener('scroll', this.handleScroll)
        this.handleScroll = null
      }
    },
    onCaseCompleted(caseId) {
      this.completedCasesCount++
      // 跳过 deepLearning 案例的完成消息
      if (caseId !== 'deepLearning') {
        this.$message({
          message: `案例 ${caseId} 完成！`,
          type: 'success',
          duration: 2000
        })
      }
    },
    async onAllCasesCompleted() {
      this.allCasesCompleted = true
      this.$message({
        message: '🎉 恭喜！所有案例学习已完成，现在可以开始测验了！',
        type: 'success',
        duration: 3000
      })
      
      // 章节完成后添加经验值
      await this.addChapterExperience()
    },
    getChapterType(type) {
      const typeMap = {
        'prologue': '序章',
        'chapter': '正章',
        'appendix': '附录'
      }
      return typeMap[type] || '章节'
    },
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleDateString('zh-CN')
    },
    formatContent(content) {
      if (!content) return ''
      // 简单的文本格式化，将换行转换为段落
      return content
        .split('\n\n')
        .map(paragraph => `<p>${paragraph.trim()}</p>`)
        .join('')
    },
    async addChapterExperience() {
      // 检查用户是否登录
      const token = localStorage.getItem('token')
      if (!token) {
        console.log('⚠️ 用户未登录，跳过经验值添加')
        return
      }
      
      try {
        const { levelApi } = await import('../services/api')
        
        // 根据章节ID计算经验值奖励
        const experienceGained = 50 // 章节完成基础经验值
        
        const response = await levelApi.addExperience({
          experience: experienceGained,
          activityType: 'chapter',
          chapterId: parseInt(this.id),
          score: 100 // 章节完成满分
        })
        
        if (response && response.code === 200) {
          const result = response.data
          console.log('✅ 章节完成经验值添加成功:', result)
          
          // 显示经验值获得提示
          this.$message.success(`章节完成！获得 ${experienceGained} 经验值！`)
          
          // 发送全局事件通知经验值更新
          window.dispatchEvent(new CustomEvent('experienceUpdated', {
            detail: {
              experienceGained: experienceGained,
              newExperience: result.experience,
              newLevel: result.newLevel,
              leveledUp: result.levelUp,
              activityType: 'chapter',
              chapterId: this.id
            }
          }))
          
          // 检查是否升级
          if (result.levelUp) {
            this.$notify({
              title: '🎉 恭喜升级！',
              message: result.levelUpMessage || `恭喜升级到 ${result.newLevel} 级！`,
              type: 'success',
              duration: 5000
            })
          }
        }
      } catch (error) {
        console.error('添加章节经验值失败:', error)
        this.$message.error('添加经验值失败，请稍后重试')
      }
    },
    getChapterDisplayNumber(chapterNumber) {
      if (chapterNumber === '0') {
        return '序章'
      } else if (chapterNumber === 'appendix') {
        return '附录'
      } else {
        return `第${chapterNumber}章`
      }
    }
  }
}
</script>

<style scoped>
.chapter-content {
  color: var(--text-color, #f5f6fa);
  font-size: 1.08rem;
  line-height: 1.8;
  margin-bottom: 2.5rem;
}

.chapter-header {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  margin-bottom: 3rem;
  padding: 2rem;
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.1), rgba(96, 165, 250, 0.05));
  border-radius: 16px;
  border: 1px solid rgba(59, 130, 246, 0.2);
}

.chapter-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
  flex-wrap: wrap;
}

.chapter-badge {
  background: linear-gradient(135deg, var(--accent-color, #3b82f6), #60a5fa);
  color: white;
  padding: 0.75rem 1.5rem;
  border-radius: 25px;
  font-size: 1rem;
  font-weight: 600;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
  letter-spacing: 1px;
  border: none;
}

.chapter-type {
  color: var(--text-color, #f5f6fa);
  background: var(--secondary-color, #23272e);
  padding: 0.75rem 1.5rem;
  border-radius: 25px;
  font-size: 0.95rem;
  font-weight: 600;
  border: 1px solid var(--border-color, rgba(57, 59, 64, 0.18));
}

.chapter-title {
  font-size: 2.5rem;
  color: var(--text-color, #f5f6fa);
  font-weight: 900;
  letter-spacing: 1px;
  line-height: 1.2;
  margin-bottom: 1rem;
}

.chapter-summary {
  color: var(--text-secondary-color, #b0b3b8);
  font-size: 1.2rem;
  margin-bottom: 2rem;
  line-height: 1.7;
  padding: 1.5rem;
  background: var(--secondary-color, #23272e);
  border-radius: 12px;
  border-left: 4px solid var(--accent-color, #3b82f6);
}

.chapter-info {
  display: flex;
  gap: 2rem;
  font-size: 0.95rem;
  flex-wrap: wrap;
}

.info-item {
  display: flex;
  gap: 0.5rem;
  align-items: center;
  padding: 0.75rem 1rem;
  background: var(--secondary-color, #23272e);
  border-radius: 12px;
  border: 1px solid var(--border-color, rgba(57, 59, 64, 0.18));
}

.info-label {
  color: var(--accent-color, #3b82f6);
  font-weight: 600;
}

.info-value {
  color: var(--text-color, #f5f6fa);
  font-weight: 600;
}

.info-value.published {
  color: var(--success-color, #4caf50);
}

.chapter-body {
  margin-bottom: 3rem;
  padding: 2rem;
  background: var(--secondary-color, #23272e);
  border-radius: 16px;
  border: 1px solid var(--border-color, rgba(57, 59, 64, 0.18));
}

.content-text {
  font-size: 1.15rem;
  line-height: 1.8;
  color: var(--text-color, #f5f6fa);
}

.content-text :deep(p) {
  margin-bottom: 1.8rem;
  text-align: justify;
}

.content-text :deep(h2) {
  font-size: 1.8rem;
  color: var(--accent-color, #3b82f6);
  margin: 2.5rem 0 1.5rem 0;
  font-weight: 700;
  border-bottom: 2px solid var(--accent-color, #3b82f6);
  padding-bottom: 0.5rem;
}

.content-text :deep(h3) {
  font-size: 1.5rem;
  color: var(--text-color, #f5f6fa);
  margin: 2rem 0 1rem 0;
  font-weight: 600;
}

.content-text :deep(ul), .content-text :deep(ol) {
  margin: 1.5rem 0;
  padding-left: 2rem;
}

.content-text :deep(li) {
  margin-bottom: 0.8rem;
}

.content-text :deep(blockquote) {
  border-left: 4px solid var(--accent-color, #3b82f6);
  padding: 1rem 1.5rem;
  margin: 2rem 0;
  background: rgba(59, 130, 246, 0.1);
  border-radius: 0 12px 12px 0;
  font-style: italic;
}

.case-study-section {
  margin: 2rem 0;
}

.interactive-chapter {
  margin: 2rem 0;
}

.chapter-pagination {
  display: flex;
  justify-content: space-between;
  gap: 1.5rem;
  padding: 2rem;
  background: var(--secondary-color, #23272e);
  border-radius: 16px;
  border: 1px solid var(--border-color, rgba(57, 59, 64, 0.18));
  margin-top: 2rem;
}

.quiz-section {
  background: linear-gradient(135deg, var(--secondary-color, #23272e), rgba(59, 130, 246, 0.1));
  border-radius: 20px;
  padding: 2.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  border: 1px solid rgba(59, 130, 246, 0.2);
  backdrop-filter: blur(10px);
}

.quiz-header {
  margin-bottom: 2rem;
  text-align: center;
}

.quiz-title {
  font-size: 1.8rem;
  color: var(--text-color, #f5f6fa);
  margin-bottom: 1rem;
  font-weight: 700;
  background: linear-gradient(135deg, var(--accent-color, #3b82f6), #60a5fa);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.quiz-description {
  color: var(--text-secondary-color, #b0b3b8);
  font-size: 1.1rem;
  line-height: 1.6;
  max-width: 600px;
  margin: 0 auto;
}

.quiz-actions {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 2rem;
  flex-wrap: wrap;
}

.btn-quiz {
  background: var(--btn-primary-bg);
  color: white;
  border: none;
  border-radius: 12px;
  padding: 1rem 2rem;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-quiz:hover:not(.disabled) {
  background: var(--btn-hover-bg);
  transform: translateY(-2px);
  box-shadow: var(--box-shadow);
}

.btn-quiz.disabled {
  background: var(--btn-secondary-bg);
  color: var(--text-secondary-color);
  cursor: not-allowed;
  opacity: 0.6;
}

.btn-icon {
  font-size: 1.2rem;
}

.btn-text {
  font-weight: 600;
}

.quiz-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.quiz-info .info-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--accent-color, #b0b3b8);
  font-size: 0.9rem;
}

.info-icon {
  font-size: 1rem;
}

.pagination-btn {
  background: var(--pagination-btn-bg);
  border: 1px solid var(--pagination-btn-border);
  border-radius: 12px;
  padding: 1rem;
  cursor: pointer;
  transition: box-shadow 0.18s, background 0.18s, border 0.18s;
  flex: 1;
  max-width: 300px;
  color: var(--text-color);
  font-weight: 600;
  font-size: 1rem;
}

.pagination-btn:hover {
  background: var(--pagination-btn-hover-bg);
  border-color: var(--accent-color);
  box-shadow: var(--box-shadow-light);
  color: var(--text-color);
}

.pagination-btn.prev {
  text-align: left;
}

.pagination-btn.next {
  text-align: right;
  margin-left: auto;
}

.btn-label {
  display: block;
  font-size: 0.8rem;
  color: #b0b3b8;
  margin-bottom: 0.25rem;
}

.btn-title {
  display: block;
  font-weight: 700;
  color: #f5f6fa;
}

/* 浅色主题优化 */
html.light-theme .chapter-content .chapter-header {
  background: var(--chapter-header-bg);
  border: 1px solid var(--chapter-header-border);
}

html.light-theme .chapter-content .chapter-badge {
  background: var(--chapter-badge-bg);
  box-shadow: var(--chapter-badge-shadow);
}

html.light-theme .chapter-content .chapter-type {
  background: var(--chapter-type-bg);
  color: var(--accent-color);
  border: 1px solid var(--chapter-header-border);
}

html.light-theme .chapter-content .chapter-summary {
  background: var(--chapter-summary-bg);
  border-left: 4px solid var(--chapter-summary-border);
}

html.light-theme .chapter-content .info-item {
  background: var(--info-item-bg);
  border: 1px solid var(--info-item-border);
}

html.light-theme .chapter-content .info-label {
  color: var(--accent-color);
}

html.light-theme .chapter-content .quiz-section {
  background: var(--quiz-section-bg);
  border: 1px solid var(--quiz-section-border);
}

html.light-theme .chapter-content .quiz-header {
  background: var(--quiz-header-bg);
}

html.light-theme .chapter-content .btn-quiz {
  background: var(--btn-primary-bg);
  color: white;
}

html.light-theme .chapter-content .btn-quiz:hover:not(:disabled) {
  background: var(--btn-hover-bg);
  transform: translateY(-2px);
  box-shadow: var(--box-shadow);
}

html.light-theme .chapter-content .pagination-btn {
  background: var(--pagination-btn-bg);
  border: 1px solid var(--pagination-btn-border);
}

html.light-theme .chapter-content .pagination-btn:hover {
  background: var(--pagination-btn-hover-bg);
  border-color: var(--accent-color);
}

/* 提升"上一章/下一章"文本可读性 */
html.light-theme .chapter-content .btn-label {
  color: var(--text-secondary-color);
}

html.light-theme .chapter-content .btn-title {
  color: var(--text-color);
}

@media (max-width: 768px) {
  .chapter-title {
    font-size: 1.3rem;
  }
  
  .chapter-info {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .chapter-pagination {
    flex-direction: column;
  }
  
  .pagination-btn {
    max-width: none;
    text-align: center;
  }
}
</style>
