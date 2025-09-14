<template>
  <div class="case-section image-segmentation-case">
    <div class="section-header">
      <h3 class="section-title">
        <span class="title-icon">🎨</span>
        <span class="title-text">图像分割技术</span>
        <span class="title-decoration"></span>
      </h3>
      <p class="section-subtitle">体验AI如何精确分割图像中的不同区域和对象</p>
    </div>
    
    <!-- 图像分割交互展示区 -->
    <div class="image-interaction-card">
      <!-- 卡片头部 -->
      <div class="card-header">
        <div class="header-content">
          <div class="vision-logo">
            <div class="logo-circle">
              <span class="logo-icon">🎨</span>
            </div>
            <div class="logo-text">
              <h4>图像分割实验室</h4>
              <span class="logo-subtitle">Image Segmentation Lab</span>
            </div>
          </div>
          <div class="interaction-status">
            <div class="status-indicator" :class="{ active: isSegmentationHovering }">
              <span class="status-dot"></span>
              <span class="status-text">{{ isSegmentationHovering ? '分割结果' : '原始图像' }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 图像展示区域 -->
      <div class="image-display-area">
        <div class="display-container">
          <!-- 指引文字 -->
          <div class="interaction-guide">
            <h5>🖌️ 交互体验</h5>
            <p>将鼠标悬停在下方图片上，查看AI图像分割结果</p>
          </div>
          
          <!-- 图像分割图片容器 -->
          <div class="image-container" 
               @mouseenter="handleSegmentationMouseEnter" 
               @mouseleave="handleSegmentationMouseLeave">
            <!-- 原始图像（默认显示） -->
            <img v-show="!isSegmentationHovering" 
                 :src="originalImage" 
                 alt="Original Image" 
                 class="view-image original-view" />
            
            <!-- 分割结果（悬停时显示） -->
            <img v-show="isSegmentationHovering" 
                 :src="segmentationImage" 
                 alt="Segmentation Result" 
                 class="view-image segmentation-view" />
            
            <!-- 悬停指示器 -->
            <div class="hover-indicator" :class="{ active: isSegmentationHovering }">
              <span class="indicator-icon">{{ isSegmentationHovering ? '🎨' : '📷' }}</span>
              <span class="indicator-text">{{ isSegmentationHovering ? 'Segmentation Result' : 'Original Image' }}</span>
            </div>
          </div>
          
          <!-- 视图说明 -->
          <div class="view-explanations">
            <div class="explanation-grid">
              <div class="explanation-item original" :class="{ active: !isSegmentationHovering }">
                <div class="explanation-icon">📷</div>
                <div class="explanation-content">
                  <h6>原始图像</h6>
                  <p>输入到AI系统的原始图片，包含需要分割的不同区域和对象</p>
                </div>
              </div>
              
              <div class="explanation-item segmentation" :class="{ active: isSegmentationHovering }">
                <div class="explanation-icon">🎨</div>
                <div class="explanation-content">
                  <h6>分割结果</h6>
                  <p>AI系统将图像分割成不同的语义区域，每个区域用不同颜色标识</p>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 技术说明 -->
          <div class="technical-info" v-if="isSegmentationHovering">
            <div class="info-header">
              <span class="info-icon">⚙️</span>
              <h6>图像分割技术说明</h6>
            </div>
            <div class="segmentation-explanation">
              <div class="segmentation-item">
                <span class="segmentation-label">语义分割 (Semantic Segmentation)</span>
                <span class="segmentation-desc">将图像中每个像素分类到特定语义类别</span>
              </div>
              <div class="segmentation-item">
                <span class="segmentation-label">实例分割 (Instance Segmentation)</span>
                <span class="segmentation-desc">区分同一类别中的不同实例对象</span>
              </div>
              <div class="segmentation-item">
                <span class="segmentation-label">颜色编码 (Color Coding)</span>
                <span class="segmentation-desc">不同颜色代表不同的对象类别或区域</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 统计信息 -->
      <div class="stats-section">
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-value">{{ segmentationInteractionCount }}</div>
            <div class="stat-label">交互次数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ Math.round(segmentationTotalHoverTime / 1000) }}s</div>
            <div class="stat-label">体验时长</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ segmentationCaseCompleted ? '✅' : '⏳' }}</div>
            <div class="stat-label">完成状态</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ImageSegmentationCase',
  props: {
    segmentationInteractionCount: {
      type: Number,
      default: 0
    },
    segmentationTotalHoverTime: {
      type: Number,
      default: 0
    }
  },
  emits: ['case-completed', 'interaction-update'],
  data() {
    return {
      isSegmentationHovering: false,
      segmentationCaseCompleted: false,
      originalImage: '/images/chapter3/图像分割1.PNG',
      segmentationImage: '/images/chapter3/图像分割2.PNG',
      hoverStartTime: null,
      localInteractionCount: 0,
      localTotalHoverTime: 0
    }
  },
  methods: {
    handleSegmentationMouseEnter() {
      this.isSegmentationHovering = true
      this.hoverStartTime = Date.now()
      this.localInteractionCount++
      
      // 发送交互更新事件
      this.$emit('interaction-update', {
        interactionCount: this.localInteractionCount,
        totalHoverTime: this.localTotalHoverTime
      })
    },
    
    handleSegmentationMouseLeave() {
      this.isSegmentationHovering = false
      
      if (this.hoverStartTime) {
        const hoverDuration = Date.now() - this.hoverStartTime
        this.localTotalHoverTime += hoverDuration
        this.hoverStartTime = null
      }
      
      // 检查是否完成案例（5次交互）
      if (this.localInteractionCount >= 5 && !this.segmentationCaseCompleted) {
        this.segmentationCaseCompleted = true
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
    }
  }
}
</script>

<style lang="scss" scoped>
.image-segmentation-case {
  margin-bottom: 3rem;
}

.section-header {
  text-align: center;
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: var(--chapter3-header-bg, linear-gradient(135deg, #8b5cf6 0%, #a855f7 100%));
  border-radius: 12px;
  color: white;
  box-shadow: 0 4px 20px rgba(139, 92, 246, 0.3);
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
  background: linear-gradient(135deg, #8b5cf6, #a855f7);
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
    background: var(--chapter3-status-active-bg, #f3e8ff);
    color: var(--chapter3-status-active-color, #7c3aed);
  }
}

.status-dot {
  width: 8px;
  height: 8px;
  background: var(--chapter3-status-dot, #64748b);
  border-radius: 50%;
  
  .status-indicator.active & {
    background: var(--chapter3-status-dot-active, #8b5cf6);
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
    background: rgba(139, 92, 246, 0.9);
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
    border-color: var(--chapter3-explanation-active-border, #8b5cf6);
    background: var(--chapter3-explanation-active-bg, #f3e8ff);
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

.segmentation-explanation {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.segmentation-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  background: var(--chapter3-segmentation-item-bg, #ffffff);
  border-radius: 8px;
  border-left: 4px solid var(--chapter3-segmentation-border, #8b5cf6);
}

.segmentation-label {
  font-weight: 600;
  color: #1f2937;
}

.segmentation-desc {
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
  color: var(--chapter3-stat-value-color, #8b5cf6);
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.9rem;
  color: #4b5563;
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