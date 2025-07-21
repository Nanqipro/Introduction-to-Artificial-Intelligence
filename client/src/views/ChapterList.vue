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
        const response = await chapterApi.getChapterOverview()
        if (response.code === 200) {
          this.chapters = response.data
        } else {
          this.error = response.message || '获取章节列表失败'
        }
      } catch (error) {
        console.error('加载章节失败:', error)
        this.error = '网络连接失败，请检查后端服务是否启动'
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

<style scoped>
.chapter-list {
  min-height: 100vh;
  background: #f8f9fa;
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
  color: #333;
  margin-bottom: 1rem;
}

.page-description {
  font-size: 1.1rem;
  color: #666;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

.chapters-container {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.chapter-item {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #e9ecef;
}

.chapter-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.15);
  border-color: #667eea;
}

.chapter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  font-size: 0.9rem;
  background: #f8f9fa;
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
}

.chapter-title {
  font-size: 1.5rem;
  color: #333;
  margin-bottom: 1rem;
  font-weight: 600;
}

.chapter-summary {
  color: #666;
  line-height: 1.6;
  margin-bottom: 1.5rem;
  font-size: 1rem;
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
  font-size: 0.9rem;
  color: #666;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #ccc;
}

.status-dot.published {
  background: #28a745;
}

.read-more {
  color: #667eea;
  font-weight: 500;
  font-size: 0.9rem;
}

.loading-container, .error-container, .empty-container {
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

.error-icon, .empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.error-container h3, .empty-container h3 {
  color: #333;
  margin-bottom: 0.5rem;
}

.error-container p, .empty-container p {
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
