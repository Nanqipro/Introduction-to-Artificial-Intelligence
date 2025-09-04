<template>
  <div class="chapter-detail">
    <nav class="chapter-nav">
      <div class="nav-container">
        <button @click="goBack" class="nav-back">
          ← 返回章节列表
        </button>
        <div class="nav-title" v-if="chapter">
          {{ chapter.chapterNumber === '0' ? '序章' : ('第' + chapter.chapterNumber + '章') }} - {{ chapter.title }}
        </div>
      </div>
    </nav>

    <div class="container">
      <ChapterDetailContent 
        v-if="chapter && !loading" 
        :chapter="chapter" 
        :id="id" 
      />

      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>正在加载章节内容...</p>
      </div>

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
import ChapterDetailContent from '../components/ChapterDetailContent.vue'

export default {
  name: 'ChapterDetail',
  components: {
    ChapterDetailContent
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
      loading: true,
      error: null
    }
  },
  async mounted() {
    await this.loadChapter()
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
    goBack() {
      this.$router.push('/chapters')
    }
  }
}
</script>

<style scoped>
.chapter-detail {
  background: var(--body-bg);
  min-height: 100vh;
  padding: 0;
}

.chapter-nav {
  background: transparent;
  border-bottom: none;
  padding: 0.5rem 0 0 0;
}

.nav-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.nav-back {
  background: linear-gradient(135deg, var(--accent-color, #3b82f6), #60a5fa);
  border: none;
  color: white;
  cursor: pointer;
  font-size: 0.95rem;
  padding: 0.75rem 1.5rem;
  transition: all 0.3s ease;
  font-weight: 600;
  letter-spacing: 0.5px;
  border-radius: 25px;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}
  
.nav-back:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}

.nav-title {
  font-weight: 700;
  color: var(--text-color, #f5f6fa);
  font-size: 1.1rem;
  letter-spacing: 1px;
  background: linear-gradient(135deg, var(--accent-color, #3b82f6), #60a5fa);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  background: transparent;
  border-radius: 0;
  box-shadow: none;
  padding: 0.75rem 1rem 2rem 1rem;
  border: none;
  backdrop-filter: none;
}

.loading-container, .error-container {
  text-align: center;
  padding: 4rem 2rem;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid var(--loading-border, #393b40);
  border-top: 4px solid var(--loading-spinner, #8fa1b3);
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
  color: var(--text-color, #f5f6fa);
  margin-bottom: 0.5rem;
}

.error-container p {
  color: var(--text-secondary-color, #b0b3b8);
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
  background: var(--btn-primary-bg);
  color: white;
  border: none;
}
  
.btn-primary:hover {
    background: var(--btn-hover-bg);
    color: white;
    transform: translateY(-1px);
}

/* 浅色主题优化 */
html.light-theme .chapter-detail {
  .nav-back {
    background: var(--btn-primary-bg);
    box-shadow: var(--box-shadow-light);
    
    &:hover {
      background: var(--btn-hover-bg);
      box-shadow: var(--box-shadow);
    }
  }

  .nav-title {
    background: var(--btn-primary-bg);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }

  .btn-primary {
    background: var(--accent-color);
    color: white;
    
    &:hover {
      background: var(--accent-light-color);
      color: white;
    }
  }

  .loading-spinner {
    border-color: var(--border-color);
    border-top-color: var(--accent-color);
  }

  .error-container h3 {
    color: var(--text-color);
  }

  .error-container p {
    color: var(--text-secondary-color);
  }
}

@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .container {
    padding: 1.2rem 0.5rem;
  }
}
</style>
