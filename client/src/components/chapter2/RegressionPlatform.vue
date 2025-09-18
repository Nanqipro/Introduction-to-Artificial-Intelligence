<template>
  <div class="case-section">
    <div class="section-header">
      <h3 class="section-title">
        <span class="title-icon">📊</span>
        <span class="title-text">回归演示学习平台</span>
        <span class="title-decoration"></span>
      </h3>
      <p class="section-subtitle">探索机器学习中最基础且重要的回归算法</p>
    </div>

    <!-- 现代化回归演示平台 -->
    <div class="modern-platform-card">
      <!-- 平台头部 -->
      <div class="platform-header">
        <div class="header-content">
          <div class="platform-logo">
            <div class="logo-circle">
              <span class="logo-icon">🎯</span>
            </div>
            <div class="logo-text">
              <h4>AI回归实验室</h4>
              <span class="logo-subtitle">Interactive Regression Lab</span>
            </div>
          </div>
          <div class="platform-status">
            <div class="status-indicator active">
              <span class="status-dot"></span>
              <span class="status-text">平台在线</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 算法展示区 -->
      <div class="algorithms-showcase">
        <div class="showcase-header">
          <h5>🧮 核心算法模块</h5>
          <p>两大经典回归算法，从理论到实践的完整学习体验</p>
        </div>

        <div class="algorithm-grid">
          <!-- 线性回归卡片 -->
          <div class="algorithm-card linear-regression">
            <div class="card-background">
              <div class="bg-pattern"></div>
            </div>
            <div class="card-content">
              <div class="algorithm-icon">
                <span class="icon-symbol">📈</span>
                <div class="icon-glow"></div>
              </div>
              <div class="algorithm-info">
                <h6 class="algorithm-title">线性回归</h6>
                <p class="algorithm-desc">Linear Regression</p>
                <div class="algorithm-tags">
                  <span class="tag">连续预测</span>
                  <span class="tag">最小二乘</span>
                </div>
              </div>
            </div>
            <div class="card-features">
              <div class="feature-list">
                <div class="feature-item">
                  <span class="feature-dot"></span>
                  <span>梯度下降优化</span>
                </div>
                <div class="feature-item">
                  <span class="feature-dot"></span>
                  <span>R²性能评估</span>
                </div>
                <div class="feature-item">
                  <span class="feature-dot"></span>
                  <span>正则化技术</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 逻辑回归卡片 -->
          <div class="algorithm-card logistic-regression">
            <div class="card-background">
              <div class="bg-pattern"></div>
            </div>
            <div class="card-content">
              <div class="algorithm-icon">
                <span class="icon-symbol">📊</span>
                <div class="icon-glow"></div>
              </div>
              <div class="algorithm-info">
                <h6 class="algorithm-title">逻辑回归</h6>
                <p class="algorithm-desc">Logistic Regression</p>
                <div class="algorithm-tags">
                  <span class="tag">分类问题</span>
                  <span class="tag">Sigmoid</span>
                </div>
              </div>
            </div>
            <div class="card-features">
              <div class="feature-list">
                <div class="feature-item">
                  <span class="feature-dot"></span>
                  <span>最大似然估计</span>
                </div>
                <div class="feature-item">
                  <span class="feature-dot"></span>
                  <span>ROC曲线分析</span>
                </div>
                <div class="feature-item">
                  <span class="feature-dot"></span>
                  <span>决策边界可视化</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 平台特性展示 -->
      <div class="platform-highlights">
        <div class="highlights-grid">
          <div class="highlight-item">
            <div class="highlight-icon">
              <span>🎮</span>
            </div>
            <div class="highlight-content">
              <h6>交互式学习</h6>
              <p>实时调参，即时反馈</p>
            </div>
          </div>
          <div class="highlight-item">
            <div class="highlight-icon">
              <span>📊</span>
            </div>
            <div class="highlight-content">
              <h6>数据可视化</h6>
              <p>直观图表，深度理解</p>
            </div>
          </div>
          <div class="highlight-item">
            <div class="highlight-icon">
              <span>🧪</span>
            </div>
            <div class="highlight-content">
              <h6>算法实验</h6>
              <p>参数对比，效果分析</p>
            </div>
          </div>
          <div class="highlight-item">
            <div class="highlight-icon">
              <span>⚡</span>
            </div>
            <div class="highlight-content">
              <h6>实时计算</h6>
              <p>毫秒级响应，流畅体验</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 启动按钮区域 -->
      <div class="platform-launch">
        <div class="launch-content">
          <div class="launch-info">
            <h5>🚀 开始您的回归学习之旅</h5>
            <p>线性回归与逻辑回归交互式演示实验室已准备就绪，包含数据生成、模型训练和可视化</p>
          </div>
          <button @click="redirectToRegressionPlatform" class="launch-button">
            <div class="button-background">
              <div class="bg-gradient"></div>
              <div class="bg-shine"></div>
            </div>
            <div class="button-content">
              <span class="button-icon">🎯</span>
              <span class="button-text">进入回归实验室</span>
              <span class="button-arrow">→</span>
            </div>
          </button>
        </div>
        <div class="launch-note">
          <div class="note-icon">💡</div>
          <p>在回归实验室中，您可以生成数据、训练模型、观察拟合效果，为后续测验做准备</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "RegressionPlatform",
  methods: {
    // 跳转到回归演示实验室
    redirectToRegressionPlatform() {
      this.$router.push("/regression-demo");
    }
  }
};
</script>

<style lang="scss" scoped>
.case-section {
  margin: 3rem 0;
  padding: 2rem;
  background: var(--card-bg);
  border-radius: 16px;
  border: 1px solid var(--border-color);
}

.section-header {
  text-align: center;
  margin-bottom: 3rem;
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin: 0 0 1rem 0;
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-color);
  position: relative;
}

.title-icon {
  font-size: 2.5rem;
  filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.3));
}

.title-text {
  background: var(--btn-primary-bg);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.title-decoration {
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: var(--btn-primary-bg);
  border-radius: 2px;
}

.section-subtitle {
  color: var(--text-secondary-color);
  font-size: 1.1rem;
  margin: 0;
  font-weight: 400;
}

.modern-platform-card {
  background: var(--card-bg);
  border-radius: 20px;
  border: 1px solid var(--border-color);
  backdrop-filter: blur(10px);
  overflow: hidden;
  box-shadow: var(--box-shadow);
}

.platform-header {
  background: var(--card-header-bg);
  padding: 2rem;
  border-bottom: 1px solid var(--border-color);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.platform-logo {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.logo-circle {
  width: 60px;
  height: 60px;
  background: var(--btn-primary-bg);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: var(--box-shadow-light);
}

.logo-icon {
  font-size: 1.8rem;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.logo-text h4 {
  margin: 0;
  color: var(--text-color);
  font-size: 1.4rem;
  font-weight: 700;
}

.logo-subtitle {
  color: var(--text-secondary-color);
  font-size: 0.9rem;
  font-style: italic;
}

.platform-status {
  display: flex;
  align-items: center;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: rgba(76, 175, 80, 0.2);
  border: 1px solid var(--success-color);
  border-radius: 20px;
}

.status-dot {
  width: 8px;
  height: 8px;
  background: var(--success-color);
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { opacity: 1; }
  50% { opacity: 0.5; }
  100% { opacity: 1; }
}

.status-text {
  color: var(--success-color);
  font-size: 0.9rem;
  font-weight: 600;
}

.algorithms-showcase {
  padding: 2rem;
}

.showcase-header {
  text-align: center;
  margin-bottom: 2rem;
}

.showcase-header h5 {
  margin: 0 0 0.5rem 0;
  color: var(--text-color);
  font-size: 1.3rem;
  font-weight: 600;
}

.showcase-header p {
  margin: 0;
  color: var(--text-secondary-color);
  font-size: 1rem;
}

.algorithm-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
}

.algorithm-card {
  position: relative;
  background: var(--card-bg);
  border-radius: 16px;
  border: 1px solid var(--border-color);
  overflow: hidden;
}

.card-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  opacity: 0.1;
}

.bg-pattern {
  width: 100%;
  height: 100%;
  background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.1) 50%, transparent 70%);
  background-size: 20px 20px;
}

.card-content {
  position: relative;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.algorithm-icon {
  position: relative;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  flex-shrink: 0;
}

.icon-symbol {
  font-size: 1.8rem;
  z-index: 2;
}

.icon-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 40px;
  height: 40px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.3) 0%, transparent 70%);
  border-radius: 50%;
  animation: glow 3s ease-in-out infinite alternate;
}

@keyframes glow {
  from { opacity: 0.5; transform: translate(-50%, -50%) scale(0.8); }
  to { opacity: 1; transform: translate(-50%, -50%) scale(1.2); }
}

.algorithm-info {
  flex: 1;
}

.algorithm-title {
  margin: 0 0 0.3rem 0;
  color: #ffffff;
  font-size: 1.2rem;
  font-weight: 600;
}

.algorithm-desc {
  margin: 0 0 0.8rem 0;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
  font-style: italic;
}

.algorithm-tags {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.tag {
  padding: 0.2rem 0.6rem;
  background: rgba(0, 191, 255, 0.3);
  border: 1px solid rgba(0, 191, 255, 0.5);
  border-radius: 12px;
  color: rgba(255, 255, 255, 0.9);
  font-size: 0.75rem;
  font-weight: 500;
}

.card-features {
  padding: 0 1.5rem 1.5rem;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.85rem;
}

.feature-dot {
  width: 4px;
  height: 4px;
  background: rgba(0, 191, 255, 0.8);
  border-radius: 50%;
  flex-shrink: 0;
}

.platform-highlights {
  padding: 2rem;
  background: rgba(255, 255, 255, 0.02);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.highlights-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
}

.highlight-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.highlight-icon {
  font-size: 1.5rem;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  flex-shrink: 0;
}

.highlight-content h6 {
  margin: 0 0 0.2rem 0;
  color: #ffffff;
  font-size: 0.95rem;
  font-weight: 600;
}

.highlight-content p {
  margin: 0;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.8rem;
}

.platform-launch {
  padding: 2rem;
  background: linear-gradient(135deg, rgba(0, 191, 255, 0.1) 0%, rgba(0, 166, 230, 0.1) 100%);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.launch-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 2rem;
  margin-bottom: 1rem;
}

.launch-info {
  flex: 1;
}

.launch-info h5 {
  margin: 0 0 0.5rem 0;
  color: #ffffff;
  font-size: 1.3rem;
  font-weight: 600;
}

.launch-info p {
  margin: 0;
  color: rgba(255, 255, 255, 0.8);
  font-size: 1rem;
  line-height: 1.5;
}

.launch-button {
  position: relative;
  padding: 0;
  border: none;
  background: none;
  cursor: pointer;
  border-radius: 12px;
  overflow: hidden;
}

.button-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.bg-gradient {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.bg-shine {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.5s ease;
}



.button-content {
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.8rem;
  padding: 1rem 2rem;
  color: #ffffff;
  font-size: 1rem;
  font-weight: 600;
  z-index: 2;
}

.button-icon {
  font-size: 1.2rem;
}

.button-arrow {
  margin-left: 0.5rem;
}

.launch-note {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  border-left: 3px solid rgba(0, 191, 255, 0.6);
}

.note-icon {
  font-size: 1.2rem;
  flex-shrink: 0;
}

.launch-note p {
  margin: 0;
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9rem;
  line-height: 1.4;
}



/* 浅色主题适配 */
// 移除重复的.light-theme样式，使用统一的theme.css管理

.light-theme .logo-subtitle {
  color: var(--chapter2-header-secondary);
}

.light-theme .showcase-header h5 {
  color: var(--chapter2-header-text);
}

.light-theme .showcase-header p {
  color: var(--chapter2-header-secondary);
}

.light-theme .algorithm-card {
  background: var(--chapter2-matrix-bg);
  border-color: var(--chapter2-matrix-cell-border);
}

.light-theme .algorithm-title {
  color: var(--chapter2-header-text);
}

.light-theme .algorithm-desc {
  color: var(--chapter2-text-secondary);
}

.light-theme .tag {
  color: var(--chapter2-header-text);
  background: var(--chapter2-progress-bg);
  border-color: var(--chapter2-progress-border);
}

.light-theme .feature-item {
  color: var(--chapter2-text-secondary);
}

.light-theme .highlight-item {
  background: var(--chapter2-layer-demo-bg);
  border-color: var(--chapter2-layer-demo-border);
}

.light-theme .highlight-content h6 {
  color: var(--chapter2-header-text);
}

.light-theme .highlight-content p {
  color: var(--chapter2-text-secondary);
}

.light-theme .launch-info h5 {
  color: var(--chapter2-header-text);
}

.light-theme .launch-info p {
  color: var(--chapter2-text-secondary);
}

.light-theme .launch-note p {
  color: var(--chapter2-text-secondary);
}

.light-theme .button-content {
  color: var(--chapter2-header-text);
}

.light-theme .algorithm-title,
.light-theme .highlight-content h6,
.light-theme .launch-info h5,
.light-theme .launch-button-content {
  color: var(--chapter2-header-text) !important;
}

.light-theme .algorithm-desc,
.light-theme .feature-item,
.light-theme .highlight-content p,
.light-theme .launch-info p,
.light-theme .launch-note p {
  color: var(--chapter2-text-secondary) !important;
}

.light-theme .tag {
  color: var(--chapter2-header-text) !important;
  background: var(--chapter2-progress-bg) !important;
  border-color: var(--chapter2-progress-border) !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .case-section {
    padding: 1.5rem;
  }
  
  .section-title {
    font-size: 1.5rem;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .title-icon {
    font-size: 2rem;
  }
  
  .platform-header {
    padding: 1.5rem;
  }
  
  .header-content {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .algorithms-showcase {
    padding: 1.5rem;
  }
  
  .algorithm-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  
  .highlights-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .launch-content {
    flex-direction: column;
    text-align: center;
    gap: 1.5rem;
  }
  
  .launch-note {
    flex-direction: column;
    text-align: center;
    gap: 0.5rem;
  }
}

@media (max-width: 480px) {
  .case-section {
    padding: 1rem;
  }
  
  .platform-header {
    padding: 1rem;
  }
  
  .algorithms-showcase {
    padding: 1rem;
  }
  
  .platform-highlights {
    padding: 1rem;
  }
  
  .platform-launch {
    padding: 1rem;
  }
  
  .button-content {
    padding: 0.8rem 1.5rem;
    font-size: 0.9rem;
  }
}
</style>