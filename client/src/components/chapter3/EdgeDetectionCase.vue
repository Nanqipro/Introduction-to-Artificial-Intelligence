<template>
  <div class="case-section edge-case">
    <div class="section-header">
      <h3 class="section-title">
        <span class="title-icon">🔍</span>
        <span class="title-text">边缘特征提取</span>
        <span class="title-decoration"></span>
      </h3>
      <p class="section-subtitle">体验计算机视觉中的边缘检测算法，理解特征提取的基本原理</p>
    </div>
    
    <!-- 边缘检测实验室卡片 -->
    <div class="edge-lab-card">
      <!-- 卡片头部 -->
      <div class="card-header">
        <div class="header-content">
          <div class="lab-logo">
            <div class="logo-circle">
              <span class="logo-icon">⚡</span>
            </div>
            <div class="logo-text">
              <h4>边缘检测实验室</h4>
              <span class="logo-subtitle">Edge Detection Lab</span>
            </div>
          </div>
          <div class="detection-status">
            <div class="status-indicator" :class="{ active: isEdgeHovering }">
              <span class="status-dot"></span>
              <span class="status-text">{{ isEdgeHovering ? '边缘模式' : '原图模式' }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 图像展示区域 -->
      <div class="image-display-area">
        <div class="display-container">
          <!-- 指引文字 -->
          <div class="interaction-guide">
            <h5>🎯 边缘检测体验</h5>
            <p>将鼠标悬停在图片上，观察边缘检测算法的效果</p>
          </div>
          
          <!-- 边缘检测图片容器 -->
          <div class="image-container" 
               @mouseenter="handleEdgeMouseEnter" 
               @mouseleave="handleEdgeMouseLeave">
            <!-- 原始图像（默认显示） -->
            <img v-show="!isEdgeHovering" 
                 :src="originalImage" 
                 alt="Original Image" 
                 class="view-image original-view" />
            
            <!-- 边缘检测图像（悬停时显示） -->
            <img v-show="isEdgeHovering" 
                 :src="edgeDetectedImage" 
                 alt="Edge Detected Image" 
                 class="view-image edge-view" />
            
            <!-- 悬停指示器 -->
            <div class="hover-indicator" :class="{ active: isEdgeHovering }">
              <span class="indicator-icon">{{ isEdgeHovering ? '🔍' : '🖼️' }}</span>
              <span class="indicator-text">{{ isEdgeHovering ? 'Edge Detection' : 'Original Image' }}</span>
            </div>
            
            <!-- 算法标签 -->
            <div class="algorithm-badge" v-if="isEdgeHovering">
              <span class="badge-text">Sobel算法</span>
            </div>
          </div>
          
          <!-- 图片来源信息 -->
          <div class="image-source">
            <div class="source-info">
              <span class="source-label">图片来源：</span>
              <button class="source-button" @click="openImageSource">
                <span class="source-icon">🔗</span>
                <span class="source-text">查看原始图片</span>
              </button>
            </div>
          </div>
          
          <!-- 技术说明 -->
          <div class="technical-explanation">
            <div class="explanation-grid">
              <div class="explanation-item original" :class="{ active: !isEdgeHovering }">
                <div class="explanation-icon">🖼️</div>
                <div class="explanation-content">
                  <h6>原始图像</h6>
                  <p>包含完整的颜色、纹理和细节信息，是计算机视觉处理的输入数据</p>
                </div>
              </div>
              
              <div class="explanation-item edge" :class="{ active: isEdgeHovering }">
                <div class="explanation-icon">🔍</div>
                <div class="explanation-content">
                  <h6>边缘检测</h6>
                  <p>通过Sobel算子检测图像中的边缘特征，突出物体轮廓和结构信息</p>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 算法详细说明 -->
          <div class="algorithm-details" v-if="isEdgeHovering">
            <div class="details-header">
              <span class="details-icon">⚙️</span>
              <h6>Sobel边缘检测算法</h6>
            </div>
            <div class="algorithm-steps">
              <div class="step-item">
                <span class="step-number">1</span>
                <div class="step-content">
                  <h7>灰度转换</h7>
                  <p>将彩色图像转换为灰度图像</p>
                </div>
              </div>
              <div class="step-item">
                <span class="step-number">2</span>
                <div class="step-content">
                  <h7>梯度计算</h7>
                  <p>使用Sobel算子计算x和y方向的梯度</p>
                </div>
              </div>
              <div class="step-item">
                <span class="step-number">3</span>
                <div class="step-content">
                  <h7>边缘强度</h7>
                  <p>计算梯度幅值，确定边缘强度</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 边缘检测统计信息 -->
      <div class="detection-stats">
        <div class="stats-grid">
          <div class="stat-item">
            <span class="stat-value">{{ localInteractionCount }}</span>
            <span class="stat-label">检测次数</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ localTotalHoverTime.toFixed(1) }}s</span>
            <span class="stat-label">观察时长</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ isEdgeHovering ? 'Sobel算法' : '原始图像' }}</span>
            <span class="stat-label">当前状态</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ edgeCaseCompleted ? '已完成' : '进行中' }}</span>
            <span class="stat-label">案例状态</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EdgeDetectionCase',
  props: {
    edgeInteractionCount: {
      type: Number,
      default: 0
    },
    edgeTotalHoverTime: {
      type: Number,
      default: 0
    },
    edgeCaseCompleted: {
      type: Boolean,
      default: false
    }
  },
  emits: ['edge-mouse-enter', 'edge-mouse-leave', 'interaction-update', 'case-completed'],
  data() {
    return {
      isEdgeHovering: false,
      edgeHoverStartTime: null,
      originalImage: '/images/chapter3/neymar-original.jpg',
      edgeDetectedImage: '/images/chapter3/neymar-edges.jpg',
      localInteractionCount: 0,
      localTotalHoverTime: 0
    }
  },
  methods: {
    handleEdgeMouseEnter() {
      this.isEdgeHovering = true
      this.edgeHoverStartTime = Date.now()
      this.$emit('edge-mouse-enter')
    },
    
    handleEdgeMouseLeave() {
      this.isEdgeHovering = false
      
      // 计算悬停时间
      if (this.edgeHoverStartTime) {
        const hoverDuration = (Date.now() - this.edgeHoverStartTime) / 1000
        
        // 更新本地统计数据
        this.localInteractionCount += 1
        this.localTotalHoverTime += hoverDuration
        
        // 发出交互更新事件
        this.$emit('interaction-update', {
          interactionCount: this.localInteractionCount,
          totalHoverTime: this.localTotalHoverTime
        })
        
        // 检查是否完成案例（交互次数达到3次或总时长达到5秒）
        if (this.localInteractionCount >= 3 || this.localTotalHoverTime >= 5) {
          this.$emit('case-completed', {
            completed: true,
            interactionCount: this.localInteractionCount,
            totalHoverTime: this.localTotalHoverTime
          })
        }
        
        this.$emit('edge-mouse-leave', hoverDuration)
        this.edgeHoverStartTime = null
      }
    },
    
    openImageSource() {
      // 打开图片来源链接
      const sourceUrl = 'https://frenchfootballweekly.com/wp-content/uploads/2023/08/Neymar-the-astronomical-salary-that-awaits-him-in-Saudi-Arabia.jpg'
      window.open(sourceUrl, '_blank')
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
    background: linear-gradient(90deg, var(--secondary-accent-color, #10b981), transparent);
    border-radius: 2px;
  }
}

.section-subtitle {
  font-size: 1.1rem;
  color: var(--text-secondary-color);
  line-height: 1.6;
}

.edge-lab-card {
  background: var(--card-bg);
  border-radius: 16px;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--border-color);
  overflow: hidden;
}

.card-header {
  background: linear-gradient(135deg, var(--secondary-accent-color, #10b981) 0%, var(--secondary-accent-light-color, #34d399) 100%);
  padding: 1.5rem;
  color: white;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.lab-logo {
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

.detection-status {
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
    color: var(--secondary-accent-color, #10b981);
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
  max-width: 800px;
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
    background: rgba(16, 185, 129, 0.8);
  }
}

.algorithm-badge {
  position: absolute;
  bottom: 1rem;
  left: 1rem;
  background: rgba(16, 185, 129, 0.9);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
  backdrop-filter: blur(8px);
}

.technical-explanation {
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
    border-color: var(--secondary-accent-color, #10b981);
    background: rgba(16, 185, 129, 0.05);
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

.algorithm-details {
  background: rgba(16, 185, 129, 0.05);
  border: 1px solid rgba(16, 185, 129, 0.2);
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
}

.details-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  
  .details-icon {
    font-size: 1.2rem;
  }
  
  h6 {
    color: var(--text-color);
    margin: 0;
    font-size: 1rem;
    font-weight: 600;
  }
}

.algorithm-steps {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.step-item {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  
  .step-number {
    width: 2rem;
    height: 2rem;
    background: var(--secondary-accent-color, #10b981);
    color: white;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 0.9rem;
    font-weight: 600;
    flex-shrink: 0;
  }
  
  .step-content {
    h7 {
      color: var(--text-color);
      font-size: 0.95rem;
      font-weight: 600;
      margin-bottom: 0.25rem;
      display: block;
    }
    
    p {
      color: var(--text-secondary-color);
      font-size: 0.85rem;
      margin: 0;
      line-height: 1.4;
    }
  }
}

.detection-stats {
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
    color: var(--secondary-accent-color, #10b981);
    margin-bottom: 0.25rem;
  }
  
  .stat-label {
    color: var(--text-secondary-color);
    font-size: 0.9rem;
  }
}

/* 浅色主题适配 */
.light-theme {
  .edge-lab-card {
    background: var(--card-bg);
    border-color: var(--border-color);
  }
  
  .explanation-item {
    background: var(--card-bg);
    
    &.active {
      background: rgba(16, 185, 129, 0.05);
      border-color: rgba(16, 185, 129, 0.3);
    }
  }
  
  .algorithm-details {
    background: rgba(16, 185, 129, 0.05);
    border-color: rgba(16, 185, 129, 0.2);
  }
}

/* 图片来源样式 */
.image-source {
  margin-top: 1rem;
  padding: 0.75rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.source-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.source-label {
  color: var(--text-secondary-color);
  font-size: 0.9rem;
  font-weight: 500;
}

.source-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 0.85rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  
  &:hover {
    background: linear-gradient(135deg, #059669, #047857);
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
  }
  
  &:active {
    transform: translateY(0);
  }
}

.source-icon {
  font-size: 0.9rem;
}

.source-text {
  font-size: 0.85rem;
}

.detection-stats {
  background: var(--secondary-color);
}
</style>