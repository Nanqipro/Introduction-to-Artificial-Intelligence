<template>
    <el-main class="about">
      <el-row justify="center">
        <el-col :xs="24" :sm="20" :md="16">
          <!-- 页面头部 -->
          <div class="page-header">
            <h1 class="page-title">关于本平台</h1>
            <p class="page-description">
              了解《人工智能概论》数字化教材平台的开发背景、技术架构和团队信息
            </p>
          </div>
  
          <!-- 项目简介 -->
          <el-card class="mb-4">
            <template #header>
              <el-icon><InfoFilled /></el-icon>
              <span class="ml-1"> 项目简介</span>
            </template>
            <p>
              本项目是基于《人工智能概论》教材开发的数字化网页教材平台，
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
            <el-descriptions title="《人工智能概论》" :column="1" border>
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
            <div class="book-purchase-section">
              <el-divider />
              <div class="purchase-info">
                <h4>📚 获取纸质版教材</h4>
                <p>如果您希望获得更好的阅读体验，可以购买纸质版教材进行深入学习。</p>
                <el-button 
                  type="primary" 
                  size="large" 
                  class="purchase-btn"
                  @click="openPurchaseLink"
                >
                  <el-icon><ShoppingCart /></el-icon>
                  立即购买纸质版
                </el-button>
              </div>
            </div>
          </el-card>
  
          <!-- 开发团队 -->
          <el-card class="mb-4">
            <template #header>
              <el-icon><UserFilled /></el-icon>
              <span class="ml-1"> 开发团队</span>
            </template>
            
            <!-- 教材编写团队 -->
            <div class="team-section">
              <h3 class="team-title">📚 教材编写团队</h3>
              <p class="team-description">GoodLab团队负责《人工智能概论与应用》教材的编写工作</p>
            </div>
            
            <el-divider />
            
            <!-- 平台开发团队 -->
            <div class="team-section">
              <h3 class="team-title">💻 平台开发团队</h3>
              <p class="team-description">数字化平台开发团队负责将教材内容转化为现代化的Web应用</p>
              
              <el-row :gutter="20" class="team-members">
                <!-- 总主编和设计师 -->
                <el-col :xs="24" :sm="12" :md="8">
                  <div class="member-card">
                    <div class="member-role">👑 总主编 & 设计师</div>
                    <div class="member-name">易寒箫</div>
                  </div>
                </el-col>
                
                <!-- 开发工程师 -->
                <el-col :xs="24" :sm="12" :md="8">
                  <div class="member-card">
                    <div class="member-role">⚙️ 开发工程师</div>
                    <div class="member-names">
                      <div class="member-name">吴琳鑫</div>
                      <div class="member-name">赵劲</div>
                      <div class="member-name">钟咏涛</div>
                      <div class="member-name">程子乾</div>
                      <div class="member-name">郑浩男</div>
                    </div>
                  </div>
                </el-col>
                
                <!-- 运维人员 -->
                <el-col :xs="24" :sm="12" :md="8">
                  <div class="member-card">
                    <div class="member-role">🔧 运维人员</div>
                    <div class="member-names">
                      <div class="member-name">周子祺</div>
                      <div class="member-name">易炜晗</div>
                      <div class="member-name">袁跃辉</div>
                      <div class="member-name">戴康钰</div>
                      <div class="member-name">邓入玮</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
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
  import { InfoFilled, Notebook, UserFilled, Message, Cpu, ShoppingCart } from '@element-plus/icons-vue'
  
  // 打开购买链接
  const openPurchaseLink = () => {
    window.open('https://pdc.capub.cn/search.html#/detail?id=igsp3vb7bwrzbuim6zarjxxzdmtsnfayec44vlahrmyqk6iub5vsn3ffgzh3ldffnfaorgyzpzdhe&from=1&type=cip', '_blank')
  }
  </script>
  
  <style lang="scss">
  
  .mb-4 { margin-bottom: 2rem; }
  .page-header { text-align: center; margin-bottom: 3rem; }
  .page-title { 
    font-size: 2.5rem; 
    color: var(--about-page-title-color); 
    margin-bottom: 1rem; 
    font-weight: 900; 
    letter-spacing: 1.5px; 
  }
  .page-description { 
    font-size: 1.1rem; 
    color: var(--about-page-description-color); 
    max-width: 600px; 
    margin: 0 auto; 
    line-height: 1.6; 
  }
  .book-description { 
    margin-top: 1.5rem; 
    color: var(--about-page-description-color); 
    line-height: 1.6; 
  }
  .contact-icon { 
    font-size: 2rem; 
    margin-bottom: 0.5rem; 
    color: var(--about-contact-icon-color); 
  }
  
  /* Element Plus 卡片和描述组件主题适配 */
  .el-card {
    background: var(--about-card-bg) !important;
    color: var(--text-color) !important;
    border: 1px solid var(--about-card-border) !important;
    box-shadow: var(--about-card-shadow);
    border-radius: var(--about-card-radius);
  }
  .el-card__header {
    background: transparent !important;
    color: var(--text-color) !important;
    font-weight: 700;
    font-size: 1.1rem;
    border-bottom: 1px solid var(--about-card-header-border);
  }
  .el-descriptions__title {
    color: var(--text-color) !important;
    font-weight: 700;
  }
  .el-descriptions__label {
    color: var(--accent-color) !important;
    font-weight: 600;
  }
  .el-descriptions__cell {
    color: var(--text-color) !important;
  }
  .el-tag {
    background: var(--about-tag-bg) !important;
    color: var(--about-tag-color) !important;
    border: none;
    font-weight: 600;
    border-radius: 8px;
  }
  .el-divider {
    background: var(--about-divider-bg) !important;
  }
  .el-card h3, .el-card h4 {
    color: var(--text-color);
    font-weight: 700;
  }
  .el-card p {
    color: var(--text-secondary-color);
  }
  .about {
    min-height: 100vh;
    background: var(--about-page-bg);
    padding: 2rem 0;
  }
  .about .el-descriptions,
  .about .el-descriptions__body,
  .about .el-descriptions__table {
    background: var(--about-descriptions-bg) !important;
  }
  .about .el-descriptions__cell,
  .about .el-descriptions__label {
    background: var(--about-descriptions-cell-bg) !important;
    color: var(--text-color) !important;
    border-color: var(--about-descriptions-border) !important;
  }
  .about .el-descriptions__label {
    color: var(--accent-color) !important;
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
    color: var(--about-link-color) !important;
    text-decoration: none;
    transition: color 0.3s ease;
    
    &:hover {
      color: var(--about-link-hover-color) !important;
      text-decoration: underline;
    }
  }
  
  /* 购买部分样式 */
  .book-purchase-section {
    margin-top: 1.5rem;
  }
  
  .purchase-info {
    text-align: center;
    padding: 1.5rem;
    background: var(--about-purchase-bg);
    border-radius: 12px;
    border: 1px solid var(--about-purchase-border);
    
    h4 {
      color: var(--text-color) !important;
      font-size: 1.2rem;
      margin-bottom: 0.8rem;
      font-weight: 700;
    }
    
    p {
      color: var(--text-secondary-color) !important;
      margin-bottom: 1.5rem;
      line-height: 1.6;
    }
  }
  
  .purchase-btn {
    background: var(--about-purchase-btn-bg) !important;
    border: none !important;
    border-radius: 25px !important;
    padding: 12px 30px !important;
    font-size: 1rem !important;
    font-weight: 600 !important;
    box-shadow: var(--about-purchase-btn-shadow) !important;
    transition: all 0.3s ease !important;
    
    &:hover {
      transform: translateY(-2px) !important;
      box-shadow: var(--about-purchase-btn-hover-shadow) !important;
      background: var(--about-purchase-btn-hover-bg) !important;
    }
    
    &:active {
       transform: translateY(0) !important;
     }
   }
   
   /* 团队成员样式 */
   .team-section {
     margin-bottom: 2rem;
   }
   
   .team-title {
     color: var(--text-color) !important;
     font-size: 1.3rem;
     font-weight: 700;
     margin-bottom: 0.8rem;
     display: flex;
     align-items: center;
     gap: 0.5rem;
   }
   
   .team-description {
     color: var(--text-secondary-color) !important;
     line-height: 1.6;
     margin-bottom: 1.5rem;
   }
   
   .team-members {
     margin-top: 1.5rem;
   }
   
   .member-card {
     background: var(--about-member-card-bg);
     border: 1px solid var(--about-member-card-border);
     border-radius: 12px;
     padding: 1.5rem;
     text-align: center;
     transition: all 0.3s ease;
     height: 100%;
     
     &:hover {
       transform: translateY(-3px);
       box-shadow: var(--about-member-card-hover-shadow);
       border-color: var(--about-member-card-hover-border);
     }
   }
   
   .member-role {
     color: var(--accent-color);
     font-weight: 700;
     font-size: 1rem;
     margin-bottom: 1rem;
     padding-bottom: 0.5rem;
     border-bottom: 2px solid var(--about-member-role-border);
   }
   
   .member-name {
     color: var(--text-color);
     font-size: 0.95rem;
     font-weight: 600;
     margin-bottom: 0.3rem;
     
     &:last-child {
       margin-bottom: 0;
     }
   }
   
   .member-names {
     display: flex;
     flex-direction: column;
     gap: 0.3rem;
   }
   
   /* 响应式设计 */
   @media (max-width: 768px) {
     .team-members {
       .member-card {
         margin-bottom: 1rem;
       }
     }
   }
   </style>