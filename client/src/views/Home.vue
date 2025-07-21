<template>
  <div class="home">
    <!-- 头部横幅 -->
    <section class="hero">
      <div class="hero-content">
        <h1 class="hero-title">《人工智能概论与应用》</h1>
        <p class="hero-subtitle">数字化教材平台</p>
        <p class="hero-description">
          基于GoodLab团队编写的《人工智能概论与应用》教材，
          通过现代化的Web技术为学生提供更加生动、有趣的人工智能学习体验。
        </p>
        <div class="hero-actions">
          <router-link to="/chapters" class="btn btn-primary">开始学习</router-link>
          <router-link to="/about" class="btn btn-secondary">了解更多</router-link>
        </div>
      </div>
    </section>

    <!-- 特色介绍 -->
    <section class="features">
      <div class="container">
        <h2 class="section-title">平台特色</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">📚</div>
            <h3>权威内容</h3>
            <p>基于专业教材，内容权威可靠，涵盖人工智能完整知识体系</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">💡</div>
            <h3>交互学习</h3>
            <p>比传统PDF更丰富的交互体验，让学习变得更加生动有趣</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">📱</div>
            <h3>多端适配</h3>
            <p>支持桌面端、平板和移动端访问，随时随地学习</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🔄</div>
            <h3>实时更新</h3>
            <p>内容可以实时更新和维护，始终保持最新的知识内容</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 章节概览 -->
    <section class="chapters-preview">
      <div class="container">
        <h2 class="section-title">章节概览</h2>
        <div class="chapters-grid" v-if="chapters.length > 0">
          <div 
            v-for="chapter in chapters" 
            :key="chapter.id" 
            class="chapter-card"
            @click="goToChapter(chapter.id)"
          >
            <div class="chapter-number">{{ chapter.chapterNumber === '0' ? '续章' : `第${chapter.chapterNumber}章` }}</div>
            <h3 class="chapter-title">{{ chapter.title }}</h3>
            <p class="chapter-summary">{{ chapter.summary }}</p>
          </div>
        </div>
        <div v-else class="loading">
          <p>正在加载章节信息...</p>
        </div>
        <div class="text-center">
          <router-link to="/chapters" class="btn btn-outline">查看所有章节</router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { chapterApi } from '../services/api'

export default {
  name: 'Home',
  data() {
    return {
      chapters: []
    }
  },
  async mounted() {
    await this.loadChapters()
  },
  methods: {
    async loadChapters() {
      try {
        const response = await chapterApi.getChapterOverview()
        if (response.code === 200) {
          this.chapters = response.data
        } else {
          console.error('获取章节失败:', response.message)
        }
      } catch (error) {
        console.error('加载章节失败:', error)
      }
    },
    goToChapter(id) {
      this.$router.push(`/chapters/${id}`)
    }
  }
}
</script>

<style scoped>
.home {
  min-height: 100vh;
}

.hero {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 100px 20px;
  text-align: center;
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
}

.hero-title {
  font-size: 3rem;
  font-weight: bold;
  margin-bottom: 1rem;
}

.hero-subtitle {
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  opacity: 0.9;
}

.hero-description {
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 2rem;
  opacity: 0.8;
}

.hero-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

.btn {
  padding: 12px 24px;
  border-radius: 6px;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
  display: inline-block;
}

.btn-primary {
  background: white;
  color: #667eea;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
}

.btn-secondary {
  background: transparent;
  color: white;
  border: 2px solid white;
}

.btn-secondary:hover {
  background: white;
  color: #667eea;
}

.btn-outline {
  background: transparent;
  color: #667eea;
  border: 2px solid #667eea;
}

.btn-outline:hover {
  background: #667eea;
  color: white;
}

.features, .chapters-preview {
  padding: 80px 20px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
}

.section-title {
  text-align: center;
  font-size: 2.5rem;
  margin-bottom: 3rem;
  color: #333;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.feature-card {
  text-align: center;
  padding: 2rem;
  border-radius: 12px;
  background: white;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.chapters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.chapter-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: all 0.3s ease;
}

.chapter-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 30px rgba(0,0,0,0.15);
}

.chapter-number {
  color: #667eea;
  font-weight: bold;
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
}

.chapter-title {
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
  color: #333;
}

.chapter-summary {
  color: #666;
  line-height: 1.5;
  font-size: 0.95rem;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.text-center {
  text-align: center;
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 2rem;
  }
  
  .hero-subtitle {
    font-size: 1.2rem;
  }
  
  .hero-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .btn {
    width: 200px;
  }
}
</style>
