<template>
  <div class="chapter-detail">
    <!-- 导航栏 -->
    <nav class="chapter-nav">
      <div class="nav-container">
        <button @click="goBack" class="nav-back">
          ← 返回章节列表
        </button>
        <div class="nav-title" v-if="chapter">
          {{ chapter.chapterNumber === '0' ? '续章' : `第${chapter.chapterNumber}章` }} - {{ chapter.title }}
        </div>
      </div>
    </nav>

    <div class="container">
      <!-- 章节内容 -->
      <article class="chapter-content" v-if="chapter && !loading">
        <!-- 章节头部 -->
        <header class="chapter-header">
          <div class="chapter-meta">
            <span class="chapter-badge">
              {{ chapter.chapterNumber === '0' ? '续章' : `第${chapter.chapterNumber}章` }}
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
          <!-- 第六章特殊处理：显示交互式内容 -->
          <div v-if="isChapter6" class="interactive-chapter">
            <Chapter6Interactive />
          </div>
          <!-- 其他章节显示普通内容 -->
          <div v-else class="content-text" v-html="formatContent(chapter.content)"></div>
        </div>

        <!-- 答题系统入口 -->
        <div class="quiz-section">
          <div class="quiz-header">
            <h3 class="quiz-title">📚 知识测验</h3>
            <p class="quiz-description">完成本章节的学习后，可以参加知识测验来检验学习成果</p>
          </div>
          <div class="quiz-actions">
            <button @click="startQuiz" class="btn btn-quiz">
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
      </article>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>正在加载章节内容...</p>
      </div>

      <!-- 错误状态 -->
      <div v-if="error" class="error-container">
        <div class="error-icon">⚠️</div>
        <h3>加载失败</h3>
        <p>{{ error }}</p>
        <button @click="loadChapter" class="btn btn-primary">重试</button>
      </div>
    </div>
  </div>
</template>

<script>
import { chapterApi } from '../services/api'
import Chapter6Interactive from '../components/chapter6/Chapter6Interactive.vue'

export default {
  name: 'ChapterDetail',
  components: {
    Chapter6Interactive
  },
  props: {
    id: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      chapter: null,
      allChapters: [],
      loading: true,
      error: null
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
    prevChapter() {
      if (!this.chapter || !this.allChapters.length) return null
      const currentIndex = this.allChapters.findIndex(ch => ch.id === this.chapter.id)
      return currentIndex > 0 ? this.allChapters[currentIndex - 1] : null
    },
    nextChapter() {
      if (!this.chapter || !this.allChapters.length) return null
      const currentIndex = this.allChapters.findIndex(ch => ch.id === this.chapter.id)
      return currentIndex < this.allChapters.length - 1 ? this.allChapters[currentIndex + 1] : null
    }
  },
  async mounted() {
    await this.loadChapter()
    await this.loadAllChapters()
  },
  watch: {
    id: {
      handler() {
        this.loadChapter()
      }
    }
  },
  methods: {
    async loadChapter() {
      this.loading = true
      this.error = null

      try {
        const response = await chapterApi.getChapterById(this.id)
        this.chapter = response.data
        console.log('加载章节详情成功:', this.chapter)
      } catch (error) {
        console.error('加载章节详情失败:', error)
        if (error.message && error.message.includes('章节不存在')) {
          this.error = '章节不存在'
        } else {
          this.error = error.message || '网络连接失败，请检查后端服务是否启动'
        }
      } finally {
        this.loading = false
      }
    },
    async loadAllChapters() {
      try {
        const response = await chapterApi.getChapterOverview()
        this.allChapters = response.data || []
      } catch (error) {
        console.error('加载章节列表失败:', error)
      }
    },
    goBack() {
      this.$router.push('/chapters')
    },
    goToChapter(id) {
      this.$router.push(`/chapters/${id}`)
    },
    startQuiz() {
      this.$router.push(`/quiz/${this.id}`)
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
    }
  }
}
</script>

<style lang="scss" scoped>
@use '../styles/variables.scss' as *;

.chapter-detail {
  background: $secondary-color;
  min-height: 100vh;
  padding: 2rem 0;
}

.chapter-nav {
  background: $nav-bg;
  border-bottom: 1px solid $nav-border;
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: $nav-shadow;
  backdrop-filter: blur(4px);
}

.nav-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-back {
  background: none;
  border: none;
  color: $accent-color;
  cursor: pointer;
  font-size: 0.95rem;
  padding: 0.5rem 0;
  transition: color 0.2s;
  font-weight: 600;
  letter-spacing: 0.5px;
  
  &:hover {
    color: $nav-back-hover;
  }
}

.nav-title {
  font-weight: 700;
  color: $text-color;
  font-size: 1.05rem;
  letter-spacing: 1px;
}

.container {
  max-width: 900px;
  margin: 0 auto;
  background: $card-bg;
  border-radius: $card-radius;
  box-shadow: $card-shadow;
  padding: 2.5rem 2rem;
  border: 1px solid $card-border;
}

.chapter-content {
  color: $text-color;
  font-size: 1.08rem;
  line-height: 1.8;
  margin-bottom: 2.5rem;
}

.chapter-header {
  display: flex;
  align-items: center;
  gap: 1.2rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.chapter-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.chapter-badge {
  background: $chapter-badge-bg;
  color: $accent-color;
  padding: 0.5rem 1.2rem;
  border-radius: 20px;
  font-size: 1rem;
  font-weight: 600;
  box-shadow: $chapter-badge-shadow;
  letter-spacing: 1px;
}

.chapter-type {
  color: $accent-color;
  background: $chapter-type-bg;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.95rem;
  font-weight: 600;
}

.chapter-title {
  font-size: 2rem;
  color: $text-color;
  font-weight: 900;
  letter-spacing: 1px;
}

.chapter-summary {
  color: $text-secondary-color;
  font-size: 1.1rem;
  margin-bottom: 2rem;
  line-height: 1.7;
}

.chapter-info {
  display: flex;
  gap: 2rem;
  font-size: 0.95rem;
}

.info-item {
  display: flex;
  gap: 0.5rem;
}

.info-label {
  color: $accent-color;
}

.info-value {
  color: $text-color;
  font-weight: 600;
  
  &.published {
    color: $accent-color;
  }
}

.chapter-body {
  margin-bottom: 3rem;
}

.content-text {
  font-size: 1.1rem;
  line-height: 1.8;
  color: $text-color;
  
  :deep(p) {
    margin-bottom: 1.5rem;
  }
}

.chapter-pagination {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  padding-top: 2rem;
  border-top: 1px solid $card-header-border;
}

.quiz-section {
  background: $secondary-color;
  border-radius: $card-radius;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: $card-shadow;
  border: 1px solid $card-border;
}

.quiz-header {
  margin-bottom: 1.5rem;
}

.quiz-title {
  font-size: 1.4rem;
  color: $text-color;
  margin-bottom: 0.5rem;
  font-weight: 700;
}

.quiz-description {
  color: $text-secondary-color;
  font-size: 1rem;
  line-height: 1.6;
}

.quiz-actions {
  display: flex;
  align-items: center;
  gap: 2rem;
  flex-wrap: wrap;
}

.btn-quiz {
  background: $btn-primary-bg;
  color: $text-color;
  border: none;
  border-radius: $btn-radius;
  padding: 1rem 2rem;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: $btn-shadow;
  }
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
  color: $accent-color;
  font-size: 0.9rem;
}

.info-icon {
  font-size: 1rem;
}

.pagination-btn {
  background: $chapter-pagination-bg;
  border: 1px solid $card-border;
  border-radius: $btn-radius;
  padding: 1rem;
  cursor: pointer;
  transition: box-shadow 0.18s, background 0.18s, border 0.18s;
  flex: 1;
  max-width: 300px;
  color: $accent-color;
  font-weight: 600;
  font-size: 1rem;
  
  &:hover {
    background: $chapter-pagination-hover;
    border-color: $chapter-pagination-hover-border;
    box-shadow: $chapter-pagination-hover-shadow;
    color: $text-color;
  }
  
  &.prev {
    text-align: left;
  }
  
  &.next {
    text-align: right;
    margin-left: auto;
  }
}

.btn-label {
  display: block;
  font-size: 0.8rem;
  color: $accent-color;
  margin-bottom: 0.25rem;
}

.btn-title {
  display: block;
  font-weight: 700;
  color: $text-color;
}

.loading-container, .error-container {
  text-align: center;
  padding: 4rem 2rem;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid $loading-border;
  border-top: 4px solid $loading-spinner;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-icon {
  font-size: $error-icon-size;
  margin-bottom: 1rem;
}

.error-container h3 {
  color: $text-color;
  margin-bottom: 0.5rem;
}

.error-container p {
  color: $text-secondary-color;
  margin-bottom: 1.5rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: $btn-radius;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: $primary-color;
  color: $text-color;
  border: none;
  
  &:hover {
    background: $primary-hover-color;
    color: $text-secondary-color;
    border-color: $primary-hover-color;
    transform: translateY(-1px);
  }
}

.back-link {
  color: $accent-color;
  font-size: 1rem;
  text-decoration: underline;
  cursor: pointer;
  margin-right: 1.5rem;
}

@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
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
  
  .container {
    padding: 1.2rem 0.5rem;
  }
}
</style>
