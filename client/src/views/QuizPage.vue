<template>
  <div class="quiz-page">
    <!-- 导航栏 -->
    <nav class="quiz-nav">
      <div class="nav-container">
        <button @click="goBack" class="nav-back">
          ← 返回章节
        </button>
        <div class="nav-title">
          {{ chapterTitle }} - 知识测验
        </div>
      </div>
    </nav>
    


    <!-- 答题系统组件 -->
    <QuizSystem 
      :chapterId="chapterId"
      :chapterTitle="chapterTitle"
    />
  </div>
</template>

<script>
import QuizSystem from '../components/QuizSystem/QuizSystem.vue'

export default {
  name: 'QuizPage',
  components: {
    QuizSystem
  },
  data() {
    return {
      chapterId: '',
      chapterTitle: ''
    }
  },
  async mounted() {
    // 从路由参数获取章节ID
    this.chapterId = this.$route.params.chapterId
    console.log('📚 QuizPage mounted, chapterId:', this.chapterId)
    await this.loadChapterInfo()
  },
  watch: {
    '$route.params.chapterId': {
      handler(newChapterId) {
        if (newChapterId && newChapterId !== this.chapterId) {
          this.chapterId = newChapterId
          console.log('🔄 章节ID变化:', newChapterId)
          this.loadChapterInfo()
        }
      },
      immediate: true
    }
  },
  methods: {
    async loadChapterInfo() {
      try {
        // 这里可以从路由参数或API获取章节标题
        const chapterTitles = {
          '1': '人工智能概述',
          '2': '机器学习基础',
          '3': '深度学习入门',
          '4': 'AI应用领域',
          '5': 'AI发展趋势',
          '6': '第一个人工智能项目',
          '7': 'AI伦理与未来'
        }
        this.chapterTitle = chapterTitles[this.chapterId] || `第${this.chapterId}章`
      } catch (error) {
        console.error('加载章节信息失败:', error)
        this.chapterTitle = `第${this.chapterId}章`
      }
    },
    goBack() {
      this.$router.push(`/chapters/${this.chapterId}`)
    }
  }
}
</script>

<style lang="scss" scoped>

.quiz-page {
  background: var(--secondary-color, #23272e);
  min-height: 100vh;
}

.quiz-nav {
  background: var(--nav-bg, rgba(41, 44, 51, 0.92));
  border-bottom: 1px solid var(--nav-border, rgba(57, 59, 64, 0.18));
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: var(--nav-shadow, 0 4px 24px rgba(24, 25, 26, 0.10));
  backdrop-filter: blur(4px);
}

.nav-container {
  max-width: var(--page-max-width, 1000px);
  margin: 0 auto;
  padding: 0 var(--page-padding, 1rem);
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-back {
  background: none;
  border: none;
  color: var(--accent-color, #b0b3b8);
  cursor: pointer;
  font-size: 0.95rem;
  padding: 0.5rem 0;
  transition: color 0.2s;
  font-weight: 600;
  letter-spacing: 0.5px;
  
  &:hover {
    color: var(--nav-back-hover, #ffffff);
  }
}

.nav-title {
  font-weight: 700;
  color: var(--text-color, #f5f6fa);
  font-size: 1.05rem;
  letter-spacing: 1px;
}

@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}
</style> 