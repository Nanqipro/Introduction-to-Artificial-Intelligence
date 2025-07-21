<template>
  <div class="about">
    <div class="container">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1 class="page-title">关于本项目</h1>
        <p class="page-description">
          了解《人工智能概论与应用》数字化教材平台的开发背景、技术架构和团队信息
        </p>
      </div>

      <!-- 项目介绍 -->
      <section class="section">
        <h2 class="section-title">项目简介</h2>
        <div class="content-card">
          <p>
            本项目是基于《人工智能概论与应用》教材开发的数字化网页教材平台，
            通过现代化的Web技术将传统纸质教材转化为交互式的在线学习平台。
            项目采用前后端分离的架构，为学生提供更加直观、互动的学习方式，
            帮助学生更好地理解和掌握人工智能的核心概念和应用。
          </p>
        </div>
      </section>

      <!-- 技术架构 -->
      <section class="section">
        <h2 class="section-title">技术架构</h2>
        <div class="tech-grid">
          <div class="tech-card">
            <div class="tech-icon">🎨</div>
            <h3>前端技术</h3>
            <ul>
              <li>Vue.js 3.5.17</li>
              <li>Vite 7.0.4</li>
              <li>Vue Router 4</li>
              <li>Axios</li>
            </ul>
          </div>
          <div class="tech-card">
            <div class="tech-icon">⚙️</div>
            <h3>后端技术</h3>
            <ul>
              <li>Spring Boot 3.5.3</li>
              <li>Java 17</li>
              <li>Maven</li>
              <li>Lombok</li>
            </ul>
          </div>
          <div class="tech-card">
            <div class="tech-icon">🔧</div>
            <h3>开发工具</h3>
            <ul>
              <li>Git 版本控制</li>
              <li>RESTful API</li>
              <li>响应式设计</li>
              <li>跨域支持</li>
            </ul>
          </div>
        </div>
      </section>

      <!-- 功能特色 -->
      <section class="section">
        <h2 class="section-title">功能特色</h2>
        <div class="features-list">
          <div class="feature-item">
            <div class="feature-icon">📚</div>
            <div class="feature-content">
              <h3>权威内容</h3>
              <p>基于GoodLab团队编写的专业教材，内容权威可靠，涵盖人工智能完整知识体系</p>
            </div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">💡</div>
            <div class="feature-content">
              <h3>交互学习</h3>
              <p>比传统PDF更丰富的交互体验，支持章节导航、内容搜索等功能</p>
            </div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">📱</div>
            <div class="feature-content">
              <h3>多端适配</h3>
              <p>支持桌面端、平板和移动端访问，随时随地学习人工智能知识</p>
            </div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">🔄</div>
            <div class="feature-content">
              <h3>实时更新</h3>
              <p>内容可以实时更新和维护，始终保持最新的知识内容和技术发展</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 教材信息 -->
      <section class="section">
        <h2 class="section-title">原始教材</h2>
        <div class="content-card">
          <h3>《人工智能概论与应用》</h3>
          <div class="book-info">
            <div class="info-row">
              <span class="label">编写团队:</span>
              <span class="value">GoodLab</span>
            </div>
            <div class="info-row">
              <span class="label">出版年份:</span>
              <span class="value">2024年</span>
            </div>
            <div class="info-row">
              <span class="label">章节数量:</span>
              <span class="value">8章（含续章）</span>
            </div>
            <div class="info-row">
              <span class="label">内容涵盖:</span>
              <span class="value">人工智能基础理论、核心技术、实际应用、伦理思考</span>
            </div>
          </div>
          <p class="book-description">
            该教材采用LaTeX排版，内容涵盖人工智能的基本概念和发展历程、
            机器学习和深度学习的核心理论、自然语言处理技术及应用、
            计算机视觉技术及应用、人工智能的实际应用案例、
            以及人工智能的发展趋势和伦理挑战。
          </p>
        </div>
      </section>

      <!-- 开发团队 -->
      <section class="section">
        <h2 class="section-title">开发团队</h2>
        <div class="content-card">
          <div class="team-info">
            <div class="team-item">
              <h3>教材编写</h3>
              <p>GoodLab团队负责《人工智能概论与应用》教材的编写工作</p>
            </div>
            <div class="team-item">
              <h3>平台开发</h3>
              <p>数字化平台开发团队负责将教材内容转化为现代化的Web应用</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 联系信息 -->
      <section class="section">
        <h2 class="section-title">联系我们</h2>
        <div class="content-card">
          <div class="contact-info">
            <div class="contact-item">
              <div class="contact-icon">📧</div>
              <div>
                <h4>邮箱联系</h4>
                <p>contact@goodlab.com</p>
              </div>
            </div>
            <div class="contact-item">
              <div class="contact-icon">🐙</div>
              <div>
                <h4>GitHub</h4>
                <p>项目开源地址和问题反馈</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
export default {
  name: 'About'
}
</script>

<style scoped>
.about {
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

.section {
  margin-bottom: 3rem;
}

.section-title {
  font-size: 1.8rem;
  color: #333;
  margin-bottom: 1.5rem;
  text-align: center;
}

.content-card {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  line-height: 1.6;
}

.tech-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.tech-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  text-align: center;
}

.tech-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.tech-card h3 {
  color: #333;
  margin-bottom: 1rem;
}

.tech-card ul {
  list-style: none;
  padding: 0;
}

.tech-card li {
  color: #666;
  margin-bottom: 0.5rem;
}

.features-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.feature-item {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  display: flex;
  align-items: flex-start;
  gap: 1rem;
}

.feature-icon {
  font-size: 2rem;
  flex-shrink: 0;
}

.feature-content h3 {
  color: #333;
  margin-bottom: 0.5rem;
}

.feature-content p {
  color: #666;
  line-height: 1.6;
}

.book-info {
  margin: 1.5rem 0;
}

.info-row {
  display: flex;
  margin-bottom: 0.5rem;
}

.label {
  font-weight: 500;
  color: #333;
  min-width: 100px;
}

.value {
  color: #666;
}

.book-description {
  margin-top: 1.5rem;
  color: #666;
  line-height: 1.6;
}

.team-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.team-item h3 {
  color: #333;
  margin-bottom: 0.5rem;
}

.team-item p {
  color: #666;
  line-height: 1.6;
}

.contact-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.contact-item {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
}

.contact-icon {
  font-size: 1.5rem;
  flex-shrink: 0;
}

.contact-item h4 {
  color: #333;
  margin-bottom: 0.25rem;
}

.contact-item p {
  color: #666;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  
  .tech-grid {
    grid-template-columns: 1fr;
  }
  
  .feature-item {
    flex-direction: column;
    text-align: center;
  }
  
  .team-info, .contact-info {
    grid-template-columns: 1fr;
  }
  
  .info-row {
    flex-direction: column;
  }
  
  .label {
    min-width: auto;
    font-weight: 600;
  }
}
</style>
