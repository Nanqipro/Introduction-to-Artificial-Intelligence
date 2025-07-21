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
          <div class="content-text" v-html="formatContent(chapter.content)"></div>
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

export default {
  name: 'ChapterDetail',
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
        const chapter = await chapterApi.getChapterById(this.id)
        this.chapter = chapter
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
        const chapters = await chapterApi.getChapterOverview()
        this.allChapters = chapters || []
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

<style scoped>
.chapter-detail {
  min-height: 100vh;
  background: #fff;
}

.chapter-nav {
  background: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 100;
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
  color: #667eea;
  cursor: pointer;
  font-size: 0.9rem;
  padding: 0.5rem 0;
  transition: color 0.3s ease;
}

.nav-back:hover {
  color: #5a6fd8;
}

.nav-title {
  font-weight: 500;
  color: #333;
  font-size: 0.9rem;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 1rem;
}

.chapter-content {
  padding: 2rem 0;
}

.chapter-header {
  margin-bottom: 3rem;
  padding-bottom: 2rem;
  border-bottom: 1px solid #e9ecef;
}

.chapter-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.chapter-badge {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

.chapter-type {
  color: #666;
  background: #f8f9fa;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
}

.chapter-title {
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 1rem;
  font-weight: 600;
  line-height: 1.2;
}

.chapter-summary {
  font-size: 1.1rem;
  color: #666;
  line-height: 1.6;
  margin-bottom: 1.5rem;
}

.chapter-info {
  display: flex;
  gap: 2rem;
  font-size: 0.9rem;
}

.info-item {
  display: flex;
  gap: 0.5rem;
}

.info-label {
  color: #666;
}

.info-value {
  color: #333;
  font-weight: 500;
}

.info-value.published {
  color: #28a745;
}

.chapter-body {
  margin-bottom: 3rem;
}

.content-text {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #333;
}

.content-text :deep(p) {
  margin-bottom: 1.5rem;
}

.chapter-pagination {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  padding-top: 2rem;
  border-top: 1px solid #e9ecef;
}

.pagination-btn {
  background: white;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  flex: 1;
  max-width: 300px;
}

.pagination-btn:hover {
  border-color: #667eea;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
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
  color: #666;
  margin-bottom: 0.25rem;
}

.btn-title {
  display: block;
  font-weight: 500;
  color: #333;
}

.loading-container, .error-container {
  text-align: center;
  padding: 4rem 2rem;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.error-container h3 {
  color: #333;
  margin-bottom: 0.5rem;
}

.error-container p {
  color: #666;
  margin-bottom: 1.5rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: #667eea;
  color: white;
}

.btn-primary:hover {
  background: #5a6fd8;
  transform: translateY(-1px);
}

@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .chapter-title {
    font-size: 2rem;
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
