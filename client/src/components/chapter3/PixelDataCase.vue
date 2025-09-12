<template>
  <div class="case-section pixel-case">
    <div class="section-header">
      <h3 class="section-title">
        <span class="title-icon">🖼️</span>
        <span class="title-text">人眼vs计算机视觉</span>
        <span class="title-decoration"></span>
      </h3>
      <p class="section-subtitle">通过鼠标悬停体验人类视觉与机器视觉的根本差异</p>
    </div>
    
    <!-- 像素数据图像交互展示区 -->
    <div class="image-interaction-card">
      <!-- 卡片头部 -->
      <div class="card-header">
        <div class="header-content">
          <div class="vision-logo">
            <div class="logo-circle">
              <span class="logo-icon">👀</span>
            </div>
            <div class="logo-text">
              <h4>AI视觉实验室</h4>
              <span class="logo-subtitle">Computer Vision Lab</span>
            </div>
          </div>
          <div class="interaction-status">
            <div class="status-indicator" :class="{ active: isPixelHovering }">
              <span class="status-dot"></span>
              <span class="status-text">{{ isPixelHovering ? '数据视图' : '人眼视图' }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 图像展示区域 -->
      <div class="image-display-area">
        <div class="display-container">
          <!-- 指引文字 -->
          <div class="interaction-guide">
            <h5>🔍 交互体验</h5>
            <p>将鼠标悬停在下方图片上，体验视觉转换</p>
          </div>
          
          <!-- 像素数据图片容器 -->
          <div class="image-container" 
               @mouseenter="handlePixelMouseEnter" 
               @mouseleave="handlePixelMouseLeave">
            <!-- 人眼视图（默认显示） -->
            <img v-show="!isPixelHovering" 
                 :src="humanViewImage" 
                 alt="Human Vision View" 
                 class="view-image human-view" />
            
            <!-- 计算机视图（悬停时显示） -->
            <img v-show="isPixelHovering" 
                 :src="computerViewImage" 
                 alt="Computer Vision View" 
                 class="view-image computer-view" />
            
            <!-- 悬停指示器 -->
            <div class="hover-indicator" :class="{ active: isPixelHovering }">
              <span class="indicator-icon">{{ isPixelHovering ? '🤖' : '👁️' }}</span>
              <span class="indicator-text">{{ isPixelHovering ? 'Computer View' : 'Human View' }}</span>
            </div>
          </div>
          
          <!-- 视图说明 -->
          <div class="view-explanations">
            <div class="explanation-grid">
              <div class="explanation-item human" :class="{ active: !isPixelHovering }">
                <div class="explanation-icon">👁️</div>
                <div class="explanation-content">
                  <h6>人眼视图</h6>
                  <p>我们看到的是完整、连贯的图像，能够直接识别出物体的形状、特征和含义</p>
                </div>
              </div>
              
              <div class="explanation-item computer" :class="{ active: isPixelHovering }">
                <div class="explanation-icon">🤖</div>
                <div class="explanation-content">
                  <h6>计算机视图</h6>
                  <p>计算机看到的是像素数据，每个颜色块都有对应的RGB数值表示</p>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 数据技术说明 -->
          <div class="technical-info" v-if="isPixelHovering">
            <div class="info-header">
              <span class="info-icon">📊</span>
              <h6>RGB数据说明</h6>
            </div>
            <div class="rgb-explanation">
              <div class="rgb-item">
                <span class="rgb-label">R (Red)</span>
                <span class="rgb-desc">红色通道值 (0-255)</span>
              </div>
              <div class="rgb-item">
                <span class="rgb-label">G (Green)</span>
                <span class="rgb-desc">绿色通道值 (0-255)</span>
              </div>
              <div class="rgb-item">
                <span class="rgb-label">B (Blue)</span>
                <span class="rgb-desc">蓝色通道值 (0-255)</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 像素案例统计信息 -->
      <div class="interaction-stats">
        <div class="stats-grid">
          <div class="stat-item">
            <span class="stat-value">{{ localInteractionCount }}</span>
            <span class="stat-label">交互次数</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ localTotalHoverTime.toFixed(1) }}s</span>
            <span class="stat-label">总体验时长</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ isPixelHovering ? '数据模式' : '视觉模式' }}</span>
            <span class="stat-label">当前视图</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PixelDataCase',
  props: {
    pixelInteractionCount: {
      type: Number,
      default: 0
    },
    pixelTotalHoverTime: {
      type: Number,
      default: 0
    },
    pixelCaseCompleted: {
      type: Boolean,
      default: false
    }
  },
  emits: ['pixel-mouse-enter', 'pixel-mouse-leave', 'interaction-update', 'case-completed'],
  data() {
    return {
      isPixelHovering: false,
      pixelHoverStartTime: null,
      humanViewImage: '/images/chapter3/human-view.png',
      computerViewImage: '/images/chapter3/computer-view.png',
      localInteractionCount: 0,
      localTotalHoverTime: 0
    }
  },
  methods: {
    handlePixelMouseEnter() {
      this.isPixelHovering = true
      this.pixelHoverStartTime = Date.now()
      this.$emit('pixel-mouse-enter')
    },
    
    handlePixelMouseLeave() {
      this.isPixelHovering = false
      
      // 计算悬停时间
      if (this.pixelHoverStartTime) {
        const hoverDuration = (Date.now() - this.pixelHoverStartTime) / 1000
        
        // 更新本地统计数据
        this.localInteractionCount += 1
        this.localTotalHoverTime += hoverDuration
        
        // 发送交互更新事件
        this.$emit('interaction-update', {
          interactionCount: this.localInteractionCount,
          totalHoverTime: this.localTotalHoverTime
        })
        
        this.$emit('pixel-mouse-leave', hoverDuration)
        this.pixelHoverStartTime = null
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.case-section {
  margin-bottom: 4rem;
}

.section-header {
  text-align: center;
  margin-bottom: 2rem;
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-color);
  margin-bottom: 1rem;
  
  .title-icon {
    font-size: 2.5rem;
  }
  
  .title-decoration {
    width: 4rem;
    height: 4px;
    background: linear-gradient(90deg, var(--accent-color), transparent);
    border-radius: 2px;
  }
}

.section-subtitle {
  font-size: 1.1rem;
  color: var(--text-secondary-color);
  line-height: 1.6;
}

.image-interaction-card {
  background: var(--card-bg);
  border-radius: 16px;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--border-color);
  overflow: hidden;
}

.card-header {
  background: linear-gradient(135deg, var(--accent-color) 0%, var(--accent-light-color) 100%);
  padding: 1.5rem;
  color: white;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.vision-logo {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.logo-circle {
  width: 3rem;
  height: 3rem;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

.logo-text {
  h4 {
    margin: 0;
    font-size: 1.2rem;
    font-weight: 600;
  }
  
  .logo-subtitle {
    font-size: 0.9rem;
    opacity: 0.8;
  }
}

.interaction-status {
  .status-indicator {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.5rem 1rem;
    background: rgba(255, 255, 255, 0.15);
    border-radius: 20px;
    backdrop-filter: blur(8px);
    
    &.active {
      background: rgba(255, 255, 255, 0.25);
    }
  }
  
  .status-dot {
    width: 8px;
    height: 8px;
    background: #4caf50;
    border-radius: 50%;
    animation: pulse 2s infinite;
  }
  
  .status-text {
    font-size: 0.9rem;
    font-weight: 500;
  }
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.image-display-area {
  padding: 2rem;
}

.interaction-guide {
  text-align: center;
  margin-bottom: 2rem;
  
  h5 {
    color: var(--accent-color);
    font-size: 1.1rem;
    margin-bottom: 0.5rem;
  }
  
  p {
    color: var(--text-secondary-color);
    font-size: 0.95rem;
  }
}

.image-container {
  position: relative;
  max-width: 600px;
  margin: 0 auto 2rem;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.view-image {
  width: 100%;
  height: auto;
  display: block;
}

.hover-indicator {
  position: absolute;
  top: 1rem;
  right: 1rem;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  backdrop-filter: blur(8px);
  
  &.active {
    background: rgba(var(--accent-color-rgb, 59, 130, 246), 0.8);
  }
}

.view-explanations {
  margin-bottom: 2rem;
}

.explanation-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
  
  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
}

.explanation-item {
  background: var(--secondary-color);
  border: 2px solid var(--border-color);
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  opacity: 0.6;
  
  &.active {
    opacity: 1;
    border-color: var(--accent-color);
    background: var(--accent-color, #3b82f6);
    background: rgba(var(--accent-color-rgb, 59, 130, 246), 0.05);
  }
}

.explanation-icon {
  font-size: 2rem;
  min-width: 2.5rem;
}

.explanation-content {
  h6 {
    color: var(--text-color);
    margin: 0 0 0.5rem 0;
    font-size: 1rem;
    font-weight: 600;
  }
  
  p {
    color: var(--text-secondary-color);
    margin: 0;
    font-size: 0.9rem;
    line-height: 1.5;
  }
}

.technical-info {
  background: var(--accent-color, #3b82f6);
  background: rgba(var(--accent-color-rgb, 59, 130, 246), 0.05);
  border: 1px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.2);
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
}

.info-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 1rem;
  
  .info-icon {
    font-size: 1.2rem;
  }
  
  h6 {
    color: var(--text-color);
    margin: 0;
    font-size: 1rem;
    font-weight: 600;
  }
}

.rgb-explanation {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.rgb-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  
  .rgb-label {
    color: var(--accent-color);
    font-weight: 600;
    font-size: 0.9rem;
  }
  
  .rgb-desc {
    color: var(--text-secondary-color);
    font-size: 0.85rem;
  }
}

.interaction-stats {
  background: var(--secondary-color);
  padding: 1.5rem;
  border-top: 1px solid var(--border-color);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1.5rem;
}

.stat-item {
  text-align: center;
  
  .stat-value {
    display: block;
    font-size: 1.5rem;
    font-weight: 700;
    color: var(--accent-color);
    margin-bottom: 0.25rem;
  }
  
  .stat-label {
    color: var(--text-secondary-color);
    font-size: 0.9rem;
  }
}

/* 浅色主题适配 */
.light-theme {
  .image-interaction-card {
    background: var(--card-bg);
    border-color: var(--border-color);
  }
  
  .explanation-item {
    background: var(--card-bg);
    
    &.active {
      background: rgba(59, 130, 246, 0.05);
      border-color: rgba(59, 130, 246, 0.3);
    }
  }
  
  .technical-info {
    background: rgba(59, 130, 246, 0.05);
    border-color: rgba(59, 130, 246, 0.2);
  }
  
  .interaction-stats {
    background: var(--secondary-color);
  }
}
</style>