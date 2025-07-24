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

<style lang="scss" scoped>
.chapter-detail {
  background: #23272e;
  min-height: 100vh;
  padding: 2rem 0;
}

.chapter-nav {
  background: rgba(41,44,51,0.92);
  border-bottom: 1px solid rgba(57,59,64,0.18);
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
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
  color: #8fa1b3;
  cursor: pointer;
  font-size: 0.95rem;
  padding: 0.5rem 0;
  transition: color 0.2s;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.nav-back:hover {
  color: #b0b3b8;
}

.nav-title {
  font-weight: 700;
  color: #f5f6fa;
  font-size: 1.05rem;
  letter-spacing: 1px;
}

.container {
  max-width: 900px;
  margin: 0 auto;
  background: #292c33;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(0,0,0,0.12);
  padding: 2.5rem 2rem;
  border: 1px solid rgba(57,59,64,0.18);
}

.chapter-content {
  color: #f5f6fa;
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
  background: linear-gradient(135deg, #18191a, #232526);
  color: #8fa1b3;
  padding: 0.5rem 1.2rem;
  border-radius: 20px;
  font-size: 1rem;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(143,161,179,0.08);
  letter-spacing: 1px;
}

.chapter-type {
  color: #8fa1b3;
  background: #23272e;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.95rem;
  font-weight: 600;
}

.chapter-title {
  font-size: 2rem;
  color: #f5f6fa;
  font-weight: 900;
  letter-spacing: 1px;
}

.chapter-summary {
  color: #b0b3b8;
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
  color: #8fa1b3;
}

.info-value {
  color: #f5f6fa;
  font-weight: 600;
}

.info-value.published {
  color: #8fa1b3;
}

.chapter-body {
  margin-bottom: 3rem;
}

.content-text {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #f5f6fa;
}

.content-text :deep(p) {
  margin-bottom: 1.5rem;
}

.chapter-pagination {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  padding-top: 2rem;
  border-top: 1px solid rgba(57,59,64,0.18);
}

.pagination-btn {
  background: #23272e;
  border: 1px solid rgba(57,59,64,0.18);
  border-radius: 12px;
  padding: 1rem;
  cursor: pointer;
  transition: box-shadow 0.18s, background 0.18s, border 0.18s;
  flex: 1;
  max-width: 300px;
  color: #8fa1b3;
  font-weight: 600;
  font-size: 1rem;
}

.pagination-btn:hover {
  background: #31343b;
  border-color: #8fa1b3;
  box-shadow: 0 4px 16px #8fa1b3;
  color: #f5f6fa;
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
  color: #8fa1b3;
  margin-bottom: 0.25rem;
}

.btn-title {
  display: block;
  font-weight: 700;
  color: #f5f6fa;
}

.loading-container, .error-container {
  text-align: center;
  padding: 4rem 2rem;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #393b40;
  border-top: 4px solid #8fa1b3;
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
  color: #f5f6fa;
  margin-bottom: 0.5rem;
}

.error-container p {
  color: #b0b3b8;
  margin-bottom: 1.5rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: #18191a;
  color: #f5f6fa;
  border: none;
}

.btn-primary:hover {
  background: #232526;
  color: #b0b3b8;
  border-color: #232526;
  transform: translateY(-1px);
}

.back-link {
  color: #8fa1b3;
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
