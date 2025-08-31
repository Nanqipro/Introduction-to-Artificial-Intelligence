<template>
  <div class="quiz-selection">
    <div class="selection-container">
      <div class="selection-header">
        <h1 class="selection-title">📝 题目自测</h1>
        <p class="selection-description">选择您想要测试的章节，检验学习成果</p>
      </div>

      <div class="chapters-grid">
        <div 
          v-for="chapter in chapters" 
          :key="chapter.id"
          class="chapter-card"
          @click="startQuiz(chapter.id)"
        >
          <div class="chapter-icon">{{ chapter.icon }}</div>
          <div class="chapter-info">
            <h3 class="chapter-title">{{ chapter.title }}</h3>
            <p class="chapter-description">{{ chapter.description }}</p>
            <div class="chapter-stats">
              <span class="stat-item">
                <span class="stat-icon">📊</span>
                <span class="stat-text">{{ chapter.questionCount }}题</span>
              </span>
              <span class="stat-item">
                <span class="stat-icon">⏱️</span>
                <span class="stat-text">{{ chapter.estimatedTime }}分钟</span>
              </span>
            </div>
          </div>
          <div class="chapter-arrow">→</div>
        </div>
      </div>

      <div class="selection-footer">
        <p class="footer-text">💡 提示：建议按章节顺序进行测试，以获得最佳学习效果</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'QuizSelection',
  data() {
    return {
      chapters: [
        {
          id: '1',
          title: '人工智能概述',
          description: '人工智能的基本概念、发展历史和核心理论',
          icon: '🤖',
          questionCount: 10,
          estimatedTime: 15
        },
        {
          id: '2',
          title: '机器学习基础',
          description: '机器学习的基本原理、算法分类和线性回归',
          icon: '🧠',
          questionCount: 12,
          estimatedTime: 20
        },
        {
          id: '3',
          title: '深度学习入门',
          description: '神经网络、反向传播和卷积神经网络',
          icon: '🔬',
          questionCount: 15,
          estimatedTime: 25
        },
        {
          id: '4',
          title: 'AI应用领域',
          description: '智慧生活、智慧驾驶、智慧医疗等应用场景',
          icon: '🌍',
          questionCount: 10,
          estimatedTime: 15
        },
        {
          id: '5',
          title: 'AI发展趋势',
          description: '人工智能的未来发展方向和前沿技术',
          icon: '🚀',
          questionCount: 8,
          estimatedTime: 12
        },
        {
          id: '6',
          title: '第一个人工智能项目',
          description: '实践项目：图像分类和手写数字识别',
          icon: '💻',
          questionCount: 10,
          estimatedTime: 18
        },
        {
          id: '7',
          title: 'AI伦理与未来',
          description: '人工智能的伦理问题和社会影响',
          icon: '⚖️',
          questionCount: 8,
          estimatedTime: 12
        }
      ]
    }
  },
  methods: {
    startQuiz(chapterId) {
      this.$router.push(`/quiz/${chapterId}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.quiz-selection {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--primary-color, #18191a) 0%, var(--secondary-color, #23272e) 100%);
  color: var(--text-color, #f5f6fa);
  padding: 40px 20px;
  
  /* 浅色主题样式 */
  .light-theme & {
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
    color: var(--text-color, #1e293b);
  }
}

.selection-container {
  max-width: 1200px;
  margin: 0 auto;
}

.selection-header {
  text-align: center;
  margin-bottom: 50px;
}

.selection-title {
  font-size: 3rem;
  margin-bottom: 15px;
  background: linear-gradient(135deg, var(--accent-color, #3b82f6) 0%, #0dcaf0 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  
  /* 浅色主题样式 */
  .light-theme & {
    background: linear-gradient(135deg, var(--accent-color, #0d6efd) 0%, #0dcaf0 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
}

.selection-description {
  font-size: 1.2rem;
  color: var(--text-secondary-color, #a0a0a0);
  margin: 0;
  
  /* 浅色主题样式 */
  .light-theme & {
    color: var(--text-secondary-color, #64748b);
  }
}

.chapters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

.chapter-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  gap: 20px;
  
  /* 浅色主题样式 */
  .light-theme & {
    background: rgba(255, 255, 255, 0.9);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(203, 213, 225, 0.3);
  }
}

.chapter-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  
  /* 浅色主题样式 */
  .light-theme & {
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  }
}

.chapter-icon {
  font-size: 3rem;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--accent-color, #3b82f6) 0%, #0dcaf0 100%);
  border-radius: 20px;
  color: white;
  
  /* 浅色主题样式 */
  .light-theme & {
    background: linear-gradient(135deg, var(--accent-color, #0d6efd) 0%, #0dcaf0 100%);
  }
}

.chapter-info {
  flex: 1;
}

.chapter-title {
  font-size: 1.4rem;
  margin: 0 0 10px 0;
  color: var(--text-color, #f5f6fa);
  
  /* 浅色主题样式 */
  .light-theme & {
    color: var(--text-color, #1e293b);
  }
}

.chapter-description {
  color: var(--text-secondary-color, #a0a0a0);
  margin: 0 0 15px 0;
  line-height: 1.5;
  
  /* 浅色主题样式 */
  .light-theme & {
    color: var(--text-secondary-color, #64748b);
  }
}

.chapter-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.9rem;
  color: var(--text-secondary-color, #a0a0a0);
  
  /* 浅色主题样式 */
  .light-theme & {
    color: var(--text-secondary-color, #64748b);
  }
}

.stat-icon {
  font-size: 1rem;
}

.chapter-arrow {
  font-size: 1.5rem;
  color: var(--accent-color, #3b82f6);
  opacity: 0.7;
  transition: all 0.3s ease;
  
  /* 浅色主题样式 */
  .light-theme & {
    color: var(--accent-color, #0d6efd);
  }
}

.chapter-card:hover .chapter-arrow {
  opacity: 1;
  transform: translateX(5px);
}

.selection-footer {
  text-align: center;
  padding: 30px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  
  /* 浅色主题样式 */
  .light-theme & {
    background: rgba(255, 255, 255, 0.9);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(203, 213, 225, 0.3);
  }
}

.footer-text {
  margin: 0;
  color: var(--text-secondary-color, #a0a0a0);
  font-size: 1rem;
  
  /* 浅色主题样式 */
  .light-theme & {
    color: var(--text-secondary-color, #64748b);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .selection-title {
    font-size: 2rem;
  }
  
  .chapters-grid {
    grid-template-columns: 1fr;
  }
  
  .chapter-card {
    flex-direction: column;
    text-align: center;
  }
  
  .chapter-stats {
    justify-content: center;
  }
}
</style>
