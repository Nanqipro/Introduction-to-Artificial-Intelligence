<template>
  <div class="case-section object-detection-case">
    <div class="section-header">
      <h3 class="section-title">
        <span class="title-icon">🎯</span>
        <span class="title-text">目标检测技术</span>
        <span class="title-decoration"></span>
      </h3>
      <p class="section-subtitle">体验AI如何识别和定位图像中的多个目标对象</p>
    </div>
    
    <!-- 目标检测图像交互展示区 -->
    <div class="image-interaction-card">
      <!-- 卡片头部 -->
      <div class="card-header">
        <div class="header-content">
          <div class="vision-logo">
            <div class="logo-circle">
              <span class="logo-icon">🎯</span>
            </div>
            <div class="logo-text">
              <h4>目标检测实验室</h4>
              <span class="logo-subtitle">Object Detection Lab</span>
            </div>
          </div>
          <div class="interaction-status">
            <div class="status-indicator" :class="{ active: isDetectionHovering }">
              <span class="status-dot"></span>
              <span class="status-text">{{ isDetectionHovering ? '检测结果' : '原始图像' }}</span>
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
            <p>将鼠标悬停在下方图片上，查看AI目标检测结果</p>
          </div>
          
          <!-- 目标检测图片容器 -->
          <div class="image-container" 
               @mouseenter="handleDetectionMouseEnter" 
               @mouseleave="handleDetectionMouseLeave">
            <!-- 原始图像（默认显示） -->
            <img v-show="!isDetectionHovering" 
                 :src="originalImage" 
                 alt="Original Image" 
                 class="view-image original-view" />
            
            <!-- 检测结果（悬停时显示） -->
            <img v-show="isDetectionHovering" 
                 :src="detectionImage" 
                 alt="Detection Result" 
                 class="view-image detection-view" />
            
            <!-- 悬停指示器 -->
            <div class="hover-indicator" :class="{ active: isDetectionHovering }">
              <span class="indicator-icon">{{ isDetectionHovering ? '🎯' : '📷' }}</span>
              <span class="indicator-text">{{ isDetectionHovering ? 'Detection Result' : 'Original Image' }}</span>
            </div>
          </div>
          
          <!-- 图片来源信息 -->
          <div class="image-source">
            <div class="source-info">
              <span class="source-label">技术来源：</span>
              <button class="source-button" @click="openTechSource">
                <span class="source-icon">🔗</span>
                <span class="source-text">Ultralytics YOLO</span>
              </button>
            </div>
          </div>
          
          <!-- 视图说明 -->
          <div class="view-explanations">
            <div class="explanation-grid">
              <div class="explanation-item original" :class="{ active: !isDetectionHovering }">
                <div class="explanation-icon">📷</div>
                <div class="explanation-content">
                  <h6>原始图像</h6>
                  <p>输入到AI系统的原始图片，包含多个待识别的目标对象</p>
                </div>
              </div>
              
              <div class="explanation-item detection" :class="{ active: isDetectionHovering }">
                <div class="explanation-icon">🎯</div>
                <div class="explanation-content">
                  <h6>检测结果</h6>
                  <p>AI系统识别出的目标对象，用边界框标注并显示置信度分数</p>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 技术说明 -->
          <div class="technical-info" v-if="isDetectionHovering">
            <div class="info-header">
              <span class="info-icon">⚙️</span>
              <h6>目标检测技术说明</h6>
            </div>
            <div class="detection-explanation">
              <div class="detection-item">
                <span class="detection-label">边界框 (Bounding Box)</span>
                <span class="detection-desc">标记目标对象位置的矩形框</span>
              </div>
              <div class="detection-item">
                <span class="detection-label">置信度 (Confidence)</span>
                <span class="detection-desc">AI对检测结果的确信程度 (0-1)</span>
              </div>
              <div class="detection-item">
                <span class="detection-label">类别标签 (Class Label)</span>
                <span class="detection-desc">识别出的目标对象类型</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 统计信息 -->
      <div class="stats-section">
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-value">{{ detectionInteractionCount }}</div>
            <div class="stat-label">交互次数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ Math.round(detectionTotalHoverTime / 1000) }}s</div>
            <div class="stat-label">体验时长</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ detectionCaseCompleted ? '✅' : '⏳' }}</div>
            <div class="stat-label">完成状态</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ObjectDetectionCase',
  props: {
    detectionInteractionCount: {
      type: Number,
      default: 0
    },
    detectionTotalHoverTime: {
      type: Number,
      default: 0
    }
  },
  emits: ['case-completed', 'interaction-update'],
  data() {
    return {
      isDetectionHovering: false,
      detectionCaseCompleted: false,
      originalImage: '/images/chapter3/目标检测1.PNG',
      detectionImage: '/images/chapter3/目标检测2.PNG',
      hoverStartTime: null,
      localInteractionCount: 0,
      localTotalHoverTime: 0
    }
  },
  methods: {
    handleDetectionMouseEnter() {
      this.isDetectionHovering = true
      this.hoverStartTime = Date.now()
      this.localInteractionCount++
      
      // 发送交互更新事件
      this.$emit('interaction-update', {
        interactionCount: this.localInteractionCount,
        totalHoverTime: this.localTotalHoverTime
      })
    },
    
    handleDetectionMouseLeave() {
      this.isDetectionHovering = false
      
      if (this.hoverStartTime) {
        const hoverDuration = Date.now() - this.hoverStartTime
        this.localTotalHoverTime += hoverDuration
        this.hoverStartTime = null
      }
      
      // 检查是否完成案例（5次交互）
      if (this.localInteractionCount >= 5 && !this.detectionCaseCompleted) {
        this.detectionCaseCompleted = true
        this.$emit('case-completed', {
          completed: true,
          interactionCount: this.localInteractionCount,
          totalHoverTime: this.localTotalHoverTime
        })
      }
      
      // 发送交互更新事件
      this.$emit('interaction-update', {
        interactionCount: this.localInteractionCount,
        totalHoverTime: this.localTotalHoverTime
      })
    },
    
    openTechSource() {
      // 打开Ultralytics YOLO链接
      const sourceUrl = 'https://www.ultralytics.com/yolo'
      window.open(sourceUrl, '_blank')
    }
  }
}
</script>

<style lang="scss" scoped>
.object-detection-case {
  margin-bottom: 3rem;
}

.section-header {
  text-align: center;
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: var(--chapter3-header-bg, linear-gradient(135deg, #f59e0b 0%, #f97316 100%));
  border-radius: 12px;
  color: white;
  box-shadow: 0 4px 20px rgba(245, 158, 11, 0.3);
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}

.title-icon {
  font-size: 2rem;
}

.section-subtitle {
  font-size: 1.1rem;
  opacity: 0.9;
  line-height: 1.5;
}

.image-interaction-card {
  background: var(--chapter3-card-bg, #ffffff);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid var(--chapter3-card-border, rgba(0, 0, 0, 0.1));
  overflow: hidden;
}

.card-header {
  background: var(--chapter3-card-header-bg, #f8fafc);
  padding: 1.5rem;
  border-bottom: 1px solid var(--chapter3-card-border, rgba(0, 0, 0, 0.1));
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
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #f59e0b, #f97316);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

.logo-text h4 {
  margin: 0;
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--text-color);
}

.logo-subtitle {
  font-size: 0.9rem;
  color: var(--text-secondary-color);
}

.interaction-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: var(--chapter3-status-bg, #f1f5f9);
  border-radius: 20px;
  transition: all 0.3s ease;
  
  &.active {
    background: var(--chapter3-status-active-bg, #fef3c7);
    color: var(--chapter3-status-active-color, #92400e);
  }
}

.status-dot {
  width: 8px;
  height: 8px;
  background: var(--chapter3-status-dot, #64748b);
  border-radius: 50%;
  
  .status-indicator.active & {
    background: var(--chapter3-status-dot-active, #f59e0b);
  }
}

.image-display-area {
  padding: 2rem;
}

.interaction-guide {
  text-align: center;
  margin-bottom: 2rem;
  
  h5 {
    font-size: 1.2rem;
    margin-bottom: 0.5rem;
    color: var(--text-color);
  }
  
  p {
    color: var(--text-secondary-color);
    font-size: 1rem;
  }
}

.image-container {
  position: relative;
  max-width: 600px;
  margin: 0 auto 2rem;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  cursor: pointer;
  transition: transform 0.3s ease;
  
  &:hover {
    transform: translateY(-4px);
  }
}

.view-image {
  width: 100%;
  height: auto;
  display: block;
  transition: opacity 0.3s ease;
}

.hover-indicator {
  position: absolute;
  top: 1rem;
  right: 1rem;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  
  &.active {
    background: rgba(245, 158, 11, 0.9);
  }
}

.view-explanations {
  margin-bottom: 2rem;
}

.explanation-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.explanation-item {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  padding: 1.5rem;
  background: var(--chapter3-explanation-bg, #f8fafc);
  border-radius: 12px;
  border: 2px solid transparent;
  transition: all 0.3s ease;
  
  &.active {
    border-color: var(--chapter3-explanation-active-border, #f59e0b);
    background: var(--chapter3-explanation-active-bg, #fef3c7);
  }
}

.explanation-icon {
  font-size: 2rem;
  flex-shrink: 0;
}

.explanation-content h6 {
  margin: 0 0 0.5rem 0;
  font-size: 1.1rem;
  font-weight: 600;
  color: #1f2937;
}

.explanation-content p {
  margin: 0;
  color: #4b5563;
  line-height: 1.5;
}

.technical-info {
  background: var(--chapter3-tech-info-bg, #f1f5f9);
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
}

.info-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 1rem;
  
  h6 {
    margin: 0;
    font-size: 1.1rem;
    font-weight: 600;
    color: #1f2937;
  }
}

.detection-explanation {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.detection-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  background: var(--chapter3-detection-item-bg, #ffffff);
  border-radius: 8px;
  border-left: 4px solid var(--chapter3-detection-border, #f59e0b);
}

.detection-label {
  font-weight: 600;
  color: #1f2937;
}

.detection-desc {
  color: #4b5563;
  font-size: 0.9rem;
}

.stats-section {
  background: var(--chapter3-stats-bg, #f8fafc);
  padding: 1.5rem;
  border-top: 1px solid var(--chapter3-card-border, rgba(0, 0, 0, 0.1));
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
}

.stat-item {
  text-align: center;
  padding: 1rem;
  background: var(--chapter3-stat-item-bg, #ffffff);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--chapter3-stat-value-color, #f59e0b);
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.9rem;
  color: #4b5563;
}

.dataset-source {
  text-align: center;
  margin-bottom: 1.5rem;
  padding: 1rem;
  background: var(--chapter3-source-bg, #f8fafc);
  border-radius: 8px;
  border: 1px solid var(--chapter3-source-border, rgba(0, 0, 0, 0.1));
}

.source-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
}

.source-label {
  font-size: 0.9rem;
  color: var(--text-secondary-color);
  font-weight: 500;
}

.source-button {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: var(--chapter3-source-btn-bg, #ffffff);
  border: 1px solid var(--chapter3-source-btn-border, #d1d5db);
  border-radius: 6px;
  color: var(--chapter3-source-btn-color, #374151);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    background: var(--chapter3-source-btn-hover-bg, #f3f4f6);
    border-color: var(--chapter3-source-btn-hover-border, #9ca3af);
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
  
  &:active {
    transform: translateY(0);
  }
}

.source-icon {
  font-size: 0.9rem;
}

.source-text {
  font-weight: 500;
}

// 响应式设计
@media (max-width: 768px) {
  .explanation-grid {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .header-content {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
}
</style>