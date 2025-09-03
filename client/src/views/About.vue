<template>
    <el-main class="about">
      <el-row justify="center">
        <el-col :xs="24" :sm="20" :md="16">
          <!-- 页面头部 -->
          <div class="page-header">
            <h1 class="page-title">关于本平台</h1>
            <p class="page-description">
              了解《人工智能概论与应用》数字化教材平台的开发背景、技术架构和团队信息
            </p>
          </div>
  
          <!-- 项目简介 -->
          <el-card class="mb-4">
            <template #header>
              <el-icon><InfoFilled /></el-icon>
              <span class="ml-1"> 项目简介</span>
            </template>
            <p>
              本项目是基于《人工智能概论与应用》教材开发的数字化网页教材平台，
              通过现代化的Web技术将传统纸质教材转化为交互式的在线学习平台。
              项目采用前后端分离的架构，为学生提供更加直观、互动的学习方式，
              帮助学生更好地理解和掌握人工智能的核心概念和应用。
            </p>
          </el-card>
  
          <!-- 技术架构 -->
          <!-- <el-card class="mb-4">
            <template #header>
              <el-icon><Cpu /></el-icon>
              <span class="ml-1"> 技术架构</span>
            </template>
            <el-row :gutter="20">
              <el-col :xs="24" :sm="8">
                <el-card shadow="hover">
                  <div class="tech-icon">🎨</div>
                  <h3>前端技术</h3>
                  <div class="tech-tags">
                    <el-tag>Vue.js 3</el-tag>
                    <el-tag>Vite</el-tag>
                    <el-tag>Vue Router</el-tag>
                    <el-tag>Axios</el-tag>
                  </div>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="8">
                <el-card shadow="hover">
                  <div class="tech-icon">⚙️</div>
                  <h3>后端技术</h3>
                  <div class="tech-tags">
                    <el-tag>Spring Boot</el-tag>
                    <el-tag>Java 17</el-tag>
                    <el-tag>Maven</el-tag>
                    <el-tag>Lombok</el-tag>
                  </div>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="8">
                <el-card shadow="hover">
                  <div class="tech-icon">🔧</div>
                  <h3>开发工具</h3>
                  <div class="tech-tags">
                    <el-tag>Git</el-tag>
                    <el-tag>RESTful API</el-tag>
                    <el-tag>响应式设计</el-tag>
                    <el-tag>跨域支持</el-tag>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-card> -->
  
          <!-- 教材信息 -->
          <el-card class="mb-4">
            <template #header>
              <el-icon><Notebook /></el-icon>
              <span class="ml-1"> 原始教材</span>
            </template>
            <el-descriptions title="《人工智能概论与应用》" :column="1" border>
              <el-descriptions-item label="编写团队">GoodLab</el-descriptions-item>
              <el-descriptions-item label="出版年份">2024年</el-descriptions-item>
              <el-descriptions-item label="章节数量">8章（含续章）</el-descriptions-item>
              <el-descriptions-item label="内容涵盖">
                人工智能基础理论、核心技术、实际应用、伦理思考
              </el-descriptions-item>
            </el-descriptions>
            <el-divider />
            <p class="book-description">
              该教材采用LaTeX排版，内容涵盖人工智能的基本概念和发展历程、
              机器学习和深度学习的核心理论、自然语言处理技术及应用、
              计算机视觉技术及应用、人工智能的实际应用案例、
              以及人工智能的发展趋势和伦理挑战。
            </p>
          </el-card>
  
          <!-- 开发团队 -->
          <el-card class="mb-4">
            <template #header>
              <el-icon><UserFilled /></el-icon>
              <span class="ml-1"> 开发团队</span>
            </template>
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12">
                <el-card shadow="never">
                  <h3>教材编写</h3>
                  <p>GoodLab团队负责《人工智能概论与应用》教材的编写工作</p>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-card shadow="never">
                  <h3>平台开发</h3>
                  <p>数字化平台开发团队负责将教材内容转化为现代化的Web应用</p>
                </el-card>
              </el-col>
            </el-row>
          </el-card>
  
          <!-- 联系信息 -->
          <el-card>
            <template #header>
              <el-icon><Message /></el-icon>
              <span class="ml-1"> 联系我们</span>
            </template>
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12">
                <el-card shadow="never">
                  <div class="contact-icon">📧</div>
                  <h4>邮箱联系</h4>
                  <p>xuz@ncu.edu.cn</p>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-card shadow="never">
                  <div class="contact-icon">🐙</div>
                  <h4>GitHub</h4>
                  <p>
                    <a href="https://github.com/Nanqipro/Introduction-to-Artificial-Intelligence/tree/zj"
                       target="_blank"
                       rel="noopener noreferrer"
                       class="github-link">
                      项目开源地址和问题反馈
                    </a>
                  </p>
                </el-card>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </template>
  
  <script setup>
  import { InfoFilled, Notebook, UserFilled, Message, Cpu } from '@element-plus/icons-vue'
  </script>
  
  <style lang="scss">
  
  .mb-4 { margin-bottom: 2rem; }
  .page-header { text-align: center; margin-bottom: 3rem; }
  .page-title { 
    font-size: 2.5rem; 
    color: var(--text-color, #f5f6fa); 
    margin-bottom: 1rem; 
    font-weight: 900; 
    letter-spacing: 1.5px; 
  }
  .page-description { 
    font-size: 1.1rem; 
    color: var(--text-secondary-color, #b0b3b8); 
    max-width: 600px; 
    margin: 0 auto; 
    line-height: 1.6; 
  }
  .book-description { 
    margin-top: 1.5rem; 
    color: var(--text-secondary-color, #b0b3b8); 
    line-height: 1.6; 
  }
  .contact-icon { 
    font-size: 2rem; 
    margin-bottom: 0.5rem; 
    color: var(--accent-color, #b0b3b8); 
  }
  
  /* Element Plus 卡片和描述组件深色主题适配 */
  .el-card {
    background: var(--card-bg, #292c33) !important;
    color: var(--text-color, #f5f6fa) !important;
    border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18)) !important;
    box-shadow: var(--card-shadow, 0 4px 24px rgba(24, 25, 26, 0.10));
    border-radius: var(--card-radius, 10px);
  }
  .el-card__header {
    background: transparent !important;
    color: var(--text-color, #f5f6fa) !important;
    font-weight: 700;
    font-size: 1.1rem;
    border-bottom: 1px solid var(--card-header-border, rgba(57, 59, 64, 0.18));
  }
  .el-descriptions__title {
    color: var(--text-color, #f5f6fa) !important;
    font-weight: 700;
  }
  .el-descriptions__label {
    color: var(--accent-color, #b0b3b8) !important;
    font-weight: 600;
  }
  .el-descriptions__cell {
    color: var(--text-color, #f5f6fa) !important;
  }
  .el-tag {
    background: var(--tag-bg, #4a90e2) !important;
    color: var(--tag-color, #ffffff) !important;
    border: none;
    font-weight: 600;
    border-radius: 8px;
  }
  .el-divider {
    background: var(--divider-bg, rgba(57, 59, 64, 0.18)) !important;
  }
  .el-card h3, .el-card h4 {
    color: var(--text-color, #f5f6fa);
    font-weight: 700;
  }
  .el-card p {
    color: var(--text-secondary-color, #b0b3b8);
  }
  .about {
    min-height: 100vh;
    background: var(--secondary-color, #23272e);
    padding: 2rem 0;
  }
  .about .el-descriptions,
  .about .el-descriptions__body,
  .about .el-descriptions__table {
    background: var(--descriptions-bg, #292c33) !important;
  }
  .about .el-descriptions__cell,
  .about .el-descriptions__label {
    background: var(--descriptions-cell-bg, #23272e) !important;
    color: var(--text-color, #f5f6fa) !important;
    border-color: var(--descriptions-border, rgba(57, 59, 64, 0.18)) !important;
  }
  .about .el-descriptions__label {
    color: var(--accent-color, #b0b3b8) !important;
    font-weight: 600;
  }
  .tech-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 0.3rem 0.3rem;
    margin-top: 1rem;
    margin-bottom: 0.5rem;
  }
  .github-link {
    color: var(--link-color, #667eea) !important;
    text-decoration: none;
    transition: color 0.3s ease;
    
    &:hover {
      color: var(--link-hover-color, #5a67d8) !important;
      text-decoration: underline;
    }
  }
  </style>