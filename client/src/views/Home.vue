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
        this.chapters = response.data || []
        console.log('Home页面加载章节成功:', this.chapters)
      } catch (error) {
        console.error('加载章节失败:', error)
        this.chapters = []
      }
    },
    goToChapter(id) {
      this.$router.push(`/chapters/${id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.home {
  min-height: 100vh;
}

.hero {
  background: linear-gradient(135deg, var(--primary-color, #18191a) 0%, var(--primary-gradient-end, #232526) 100%);
  color: var(--text-color, #f5f6fa);
  padding: 100px 20px 60px 20px;
  text-align: center;
  position: relative;
  overflow: hidden;
  /* Light theme: restore blue hero background */
  .light-theme & {
    background: linear-gradient(
      135deg,
      #0d6efd 0%,
      #1d74ff 35%,
      #3b82f6 65%,
      #60a5fa 100%
    );
    color: var(--text-primary, #ffffff);
  }
}
.hero::after {
  content: '';
  position: absolute;
  left: 50%; top: 100%;
  transform: translate(-50%, -50%);
  width: 120vw; height: 400px;
  background: radial-gradient(ellipse at center, var(--accent-color, #3b82f6) 0%, transparent 80%);
  opacity: 0.18;
  z-index: 0;
}
.hero-content {
  position: relative;
  z-index: 1;
  max-width: 800px;
  margin: 0 auto;
}
.hero-title {
  font-size: 3.2rem;
  font-weight: 800;
  margin-bottom: 1.2rem;
  letter-spacing: 2px;
}
.hero-subtitle {
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  color: var(--accent-color, #3b82f6);
  font-weight: 700;
  letter-spacing: 1px;
}
.hero-description {
  font-size: 1.15rem;
  line-height: 1.7;
  margin-bottom: 2.2rem;
  color: var(--text-secondary-color, #475569);
  opacity: 0.92;
  /* Light theme: make this sentence white for better contrast */
  .light-theme & {
    color: #ffffff;
    opacity: 1;
  }
}
.hero-actions {
  display: flex;
  gap: 1.2rem;
  justify-content: center;
  flex-wrap: wrap;
}
.btn-primary {
  background: var(--accent-color, #3b82f6);
  color: var(--card-bg, #ffffff);
  border: none;
  border-radius: 12px;
  font-weight: 700;
  font-size: 1.1rem;
  padding: 14px 36px;
  box-shadow: 0 4px 24px rgba(59, 130, 246, 0.18);
  transition: all 0.25s;
}
.btn-primary:hover {
  background: var(--primary-hover, #2563eb);
  color: var(--card-bg, #ffffff);
  box-shadow: 0 8px 32px rgba(59, 130, 246, 0.3);
  transform: translateY(-2px) scale(1.05);
}
.btn-secondary {
  background: transparent;
  color: var(--accent-color, #3b82f6);
  border: 2px solid var(--accent-color, #3b82f6);
  border-radius: 12px;
  font-weight: 700;
  font-size: 1.1rem;
  padding: 14px 36px;
  transition: all 0.25s;
}
.btn-secondary:hover {
  background: var(--accent-color, #3b82f6);
  color: var(--card-bg, #ffffff);
}

.features {
  padding: 90px 20px 50px 20px;
  background: var(--secondary-color, #f8fafc);
}
.section-title {
  text-align: center;
  font-size: 2.6rem;
  margin-bottom: 3.5rem;
  color: var(--text-color, #1e293b);
  font-weight: 800;
  letter-spacing: 2px;
}
.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 2.8rem;
}
.feature-card {
  background: var(--card-bg, #ffffff);
  border-radius: 15px;
  box-shadow: var(--shadow-md, 0 4px 16px rgba(0, 0, 0, 0.1));
  padding: 2.8rem 2rem 2.2rem 2rem;
  text-align: left;
  border: 1.5px solid var(--border-color, #e2e8f0);
  position: relative;
  transition: box-shadow 0.18s, background 0.18s, border 0.18s;
  color: var(--text-secondary-color, #475569);
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  backdrop-filter: blur(2px);

  .feature-icon {
    width: 64px;
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 2.5rem;
    background: var(--accent-color, #3b82f6);
    color: var(--card-bg, #ffffff);
    border-radius: 50%;
    margin-bottom: 1.5rem;
    box-shadow: 0 2px 12px rgba(59, 130, 246, 0.2);
  }
  h3 {
    color: var(--text-color, #1e293b);
    font-size: 1.18rem;
    font-weight: 700;
    margin-bottom: 0.6rem;
    letter-spacing: 1px;
  }
  p {
    color: var(--text-secondary-color, #475569);
    font-size: 1.05rem;
    line-height: 1.8;
    margin-bottom: 0;
  }
}
.feature-card:hover {
  box-shadow: var(--shadow-lg, 0 8px 32px rgba(0, 0, 0, 0.15));
  background: var(--card-bg, #ffffff);
  border: 1.5px solid var(--accent-color, #3b82f6);
}

.chapters-preview {
  padding: 90px 20px 50px 20px;
  background: var(--secondary-color, #f8fafc);
}
.chapters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}
.chapter-card {
  background: var(--card-bg, #ffffff);
  border-radius: 15px;
  padding: 1.8rem;
  box-shadow: var(--shadow-md, 0 4px 16px rgba(0, 0, 0, 0.1));
  cursor: pointer;
  transition: box-shadow 0.18s, background 0.18s, border 0.18s;
  border: 1.5px solid var(--border-color, #e2e8f0);
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.chapter-card:hover {
  box-shadow: var(--shadow-lg, 0 8px 32px rgba(0, 0, 0, 0.15));
  background: var(--card-bg, #ffffff);
  border: 1.5px solid var(--accent-color, #3b82f6);
}
.chapter-number {
  color: var(--accent-color, #3b82f6);
  font-weight: bold;
  font-size: 1rem;
  margin-bottom: 0.5rem;
}
.chapter-title {
  font-size: 1.25rem;
  margin-bottom: 0.5rem;
  color: var(--text-color, #1e293b);
  font-weight: 700;
}
.chapter-summary {
  color: var(--text-secondary-color, #475569);
  line-height: 1.6;
  font-size: 1.02rem;
}
.loading {
  text-align: center;
  padding: 2rem;
  color: var(--text-secondary-color, #475569);
}
.text-center {
  text-align: center;
}

@media (max-width: 900px) {
  .hero-title { font-size: 2rem; }
  .hero-content { padding: 0 0.5rem; }
  .features-grid, .chapters-grid { grid-template-columns: 1fr; }
}
@media (max-width: 600px) {
  .hero { padding: 60px 8px 30px 8px; border-bottom-left-radius: 20px; border-bottom-right-radius: 20px; }
  .features, .chapters-preview { padding: 40px 8px 20px 8px; }
  .section-title { font-size: 1.5rem; margin-bottom: 1.5rem; }
  .feature-card, .chapter-card { padding: 1.2rem; border-radius: var(--border-radius, 10px); }
}
@media (hover: none) {
  .feature-card:hover, .chapter-card:hover {
    box-shadow: var(--shadow-md, 0 4px 16px rgba(0, 0, 0, 0.1));
    background: var(--card-bg, #ffffff);
    border: 1.5px solid var(--border-color, #e2e8f0);
  }
}
</style>
