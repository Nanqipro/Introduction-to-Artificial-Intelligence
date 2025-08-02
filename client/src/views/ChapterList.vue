<template>
  <div class="chapter-list">
    <div class="container">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1 class="page-title">教材章节</h1>
        <p class="page-description">
          《人工智能概论与应用》完整章节列表，点击章节卡片查看详细内容
        </p>
      </div>

      <!-- 章节列表 -->
      <div class="chapters-container" v-if="!loading">
        <div 
          v-for="chapter in chapters" 
          :key="chapter.id" 
          class="chapter-item"
          @click="goToChapter(chapter.id)"
        >
          <div class="chapter-header">
            <div class="chapter-badge">
              {{ chapter.chapterNumber === '0' ? '续章' : `第${chapter.chapterNumber}章` }}
            </div>
            <div class="chapter-type">{{ getChapterType(chapter.type) }}</div>
          </div>
          
          <h2 class="chapter-title">{{ chapter.title }}</h2>
          
          <p class="chapter-summary">{{ chapter.summary }}</p>
          
          <div class="chapter-footer">
            <span class="chapter-status">
              <span class="status-dot" :class="{ published: chapter.published }"></span>
              {{ chapter.published ? '已发布' : '未发布' }}
            </span>
            <span class="read-more">阅读详情 →</span>
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>正在加载章节信息...</p>
      </div>

      <!-- 错误状态 -->
      <div v-if="error" class="error-container">
        <div class="error-icon">⚠️</div>
        <h3>加载失败</h3>
        <p>{{ error }}</p>
        <button @click="loadChapters" class="btn btn-primary">重试</button>
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && !error && chapters.length === 0" class="empty-container">
        <div class="empty-icon">📚</div>
        <h3>暂无章节</h3>
        <p>目前还没有可用的章节内容</p>
      </div>
    </div>
  </div>
</template>

<script>
import { chapterApi } from '../services/api'

export default {
  name: 'ChapterList',
  data() {
    return {
      chapters: [],
      loading: true,
      error: null
    }
  },
  async mounted() {
    await this.loadChapters()
  },
  methods: {
    async loadChapters() {
      this.loading = true
      this.error = null

      try {
        const chapters = await chapterApi.getChapterOverview()
        this.chapters = chapters || []
        console.log('加载章节列表成功:', this.chapters)
      } catch (error) {
        console.error('加载章节失败:', error)
        this.error = error.message || '网络连接失败，请检查后端服务是否启动'
      } finally {
        this.loading = false
      }
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
    }
  }
}
</script>

<style lang="scss" scoped>

.chapter-list {
  min-height: 100vh;
  background: $secondary-color;
  padding: 2rem 0;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 1rem;
}

.page-header {
  text-align: center;
  margin-bottom: 3rem;
}

.page-title {
  font-size: 2.5rem;
  color: $text-color;
  margin-bottom: 1rem;
  font-weight: 900;
  letter-spacing: 1.5px;
}

.page-description {
  font-size: 1.1rem;
  color: $text-secondary-color;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

.chapters-container {
  display: flex;
  flex-direction: column;
  gap: $list-item-gap;
}

.chapter-item {
  background: $card-bg;
  border-radius: $card-radius;
  padding: 2rem;
  box-shadow: $card-shadow;
  cursor: pointer;
  transition: box-shadow 0.18s, background 0.18s, border 0.18s;
  border: 1px solid $card-border;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  
  &:hover {
    background: $list-item-hover-bg;
    box-shadow: $list-item-hover-shadow;
    border-color: $list-item-hover-border;
  }
}

.chapter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.chapter-badge {
  background: $chapter-badge-bg;
  color: $accent-color;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.95rem;
  font-weight: 600;
  box-shadow: $chapter-badge-shadow;
  letter-spacing: 1px;
}

.chapter-type {
  color: $accent-color;
  font-size: 0.95rem;
  background: $chapter-type-bg;
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-weight: 600;
}

.chapter-title {
  font-size: 1.5rem;
  color: $text-color;
  margin-bottom: 1rem;
  font-weight: 700;
  letter-spacing: 1px;
}

.chapter-summary {
  color: $text-secondary-color;
  line-height: 1.6;
  margin-bottom: 1.5rem;
  font-size: 1.05rem;
}

.chapter-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chapter-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.95rem;
  color: $accent-color;
  font-weight: 600;
}

.status-dot {
  width: $status-dot-size;
  height: $status-dot-size;
  border-radius: 50%;
  background: $status-dot-unpublished;
  
  &.published {
    background: $status-dot-published;
  }
}

.read-more {
  color: $accent-color;
  font-weight: 600;
  font-size: 0.95rem;
  letter-spacing: 0.5px;
}

.loading-container, .error-container, .empty-container {
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

.error-icon, .empty-icon {
  font-size: $empty-icon-size;
  margin-bottom: 1rem;
}

.error-container h3, .empty-container h3 {
  color: $text-color;
  margin-bottom: 0.5rem;
}

.error-container p, .empty-container p {
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
    border-color: $primary-hover-color;
    transform: translateY(-1px);
  }
}

@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  
  .chapter-item {
    padding: 1.5rem;
  }
  
  .chapter-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .chapter-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}
</style>
