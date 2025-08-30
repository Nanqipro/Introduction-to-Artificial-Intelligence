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
    
    <!-- 导航提示 -->
    <div style="background: #ff6b6b; color: white; padding: 15px; margin: 20px; border-radius: 8px; text-align: center; font-size: 16px;">
      🎯 <strong>提示：</strong>要查看第一章的案例演示，请点击左上角的"← 返回章节"按钮
      <br>
      📍 或者直接访问：<code style="background: rgba(255,255,255,0.2); padding: 2px 6px; border-radius: 4px;">/chapters/1</code>
    </div>

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
  props: {
    chapterId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      chapterTitle: ''
    }
  },
  async mounted() {
    await this.loadChapterInfo()
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
  background: $secondary-color;
  min-height: 100vh;
}

.quiz-nav {
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
  max-width: $page-max-width;
  margin: 0 auto;
  padding: 0 $page-padding;
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

@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}
</style> 