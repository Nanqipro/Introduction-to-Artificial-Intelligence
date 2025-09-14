<template>
  <div class="case-section object-tracking-case">
    <div class="section-header">
      <h3 class="section-title">
        <span class="title-icon">🎬</span>
        <span class="title-text">目标跟踪技术</span>
        <span class="title-decoration"></span>
      </h3>
      <p class="section-subtitle">体验AI如何在视频序列中持续跟踪移动目标</p>
    </div>
    
    <!-- 目标跟踪视频交互展示区 -->
    <div class="video-interaction-card">
      <!-- 卡片头部 -->
      <div class="card-header">
        <div class="header-content">
          <div class="vision-logo">
            <div class="logo-circle">
              <span class="logo-icon">🎬</span>
            </div>
            <div class="logo-text">
              <h4>目标跟踪实验室</h4>
              <span class="logo-subtitle">Object Tracking Lab</span>
            </div>
          </div>
          <div class="interaction-status">
            <div class="status-indicator" :class="{ active: currentVideoMode === 'tracking' }">
              <span class="status-dot"></span>
              <span class="status-text">{{ currentVideoMode === 'tracking' ? '跟踪结果' : '原始视频' }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 视频展示区域 -->
      <div class="video-display-area">
        <div class="display-container">
          <!-- 指引文字 -->
          <div class="interaction-guide">
            <h5>🎥 交互体验</h5>
            <p>点击下方按钮切换视频模式，体验AI目标跟踪技术</p>
          </div>
          
          <!-- 视频控制按钮 -->
          <div class="video-controls">
            <button 
              class="control-btn" 
              :class="{ active: currentVideoMode === 'original' }"
              @click="switchVideoMode('original')">
              <span class="btn-icon">📹</span>
              <span class="btn-text">原始视频</span>
            </button>
            <button 
              class="control-btn" 
              :class="{ active: currentVideoMode === 'tracking' }"
              @click="switchVideoMode('tracking')">
              <span class="btn-icon">🎯</span>
              <span class="btn-text">跟踪结果</span>
            </button>
          </div>
          
          <!-- 视频容器 -->
          <div class="video-container">
            <!-- 原始视频 -->
            <video 
              v-show="currentVideoMode === 'original'"
              ref="originalVideo"
              :src="originalVideoSrc"
              class="tracking-video original-video"
              controls
              loop
              muted
              @play="handleVideoPlay('original')"
              @pause="handleVideoPause('original')"
              @ended="handleVideoEnd('original')">
              您的浏览器不支持视频播放。
            </video>
            
            <!-- 跟踪结果视频 -->
            <video 
              v-show="currentVideoMode === 'tracking'"
              ref="trackingVideo"
              :src="trackingVideoSrc"
              class="tracking-video tracking-result-video"
              controls
              loop
              muted
              @play="handleVideoPlay('tracking')"
              @pause="handleVideoPause('tracking')"
              @ended="handleVideoEnd('tracking')">
              您的浏览器不支持视频播放。
            </video>
            
            <!-- 视频状态指示器 -->
            <div class="video-indicator" :class="{ active: isVideoPlaying }">
              <span class="indicator-icon">{{ isVideoPlaying ? '▶️' : '⏸️' }}</span>
              <span class="indicator-text">{{ isVideoPlaying ? 'Playing' : 'Paused' }}</span>
            </div>
          </div>
          
          <!-- 视频说明 -->
          <div class="video-explanations">
            <div class="explanation-grid">
              <div class="explanation-item original" :class="{ active: currentVideoMode === 'original' }">
                <div class="explanation-icon">📹</div>
                <div class="explanation-content">
                  <h6>原始视频</h6>
                  <p>输入到AI系统的原始视频序列，包含多个移动的目标对象</p>
                </div>
              </div>
              
              <div class="explanation-item tracking" :class="{ active: currentVideoMode === 'tracking' }">
                <div class="explanation-icon">🎯</div>
                <div class="explanation-content">
                  <h6>跟踪结果</h6>
                  <p>AI系统实时跟踪目标对象，用彩色边界框标注并分配唯一ID</p>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 技术说明 -->
          <div class="technical-info" v-if="currentVideoMode === 'tracking'">
            <div class="info-header">
              <span class="info-icon">⚙️</span>
              <h6>目标跟踪技术说明</h6>
            </div>
            <div class="tracking-explanation">
              <div class="tracking-item">
                <span class="tracking-label">多目标跟踪 (MOT)</span>
                <span class="tracking-desc">同时跟踪视频中的多个移动目标</span>
              </div>
              <div class="tracking-item">
                <span class="tracking-label">目标ID (Object ID)</span>
                <span class="tracking-desc">为每个目标分配唯一标识符保持一致性</span>
              </div>
              <div class="tracking-item">
                <span class="tracking-label">轨迹预测 (Trajectory)</span>
                <span class="tracking-desc">预测目标未来位置处理遮挡情况</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 统计信息 -->
      <div class="stats-section">
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-value">{{ trackingInteractionCount }}</div>
            <div class="stat-label">切换次数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ Math.round(trackingTotalWatchTime / 1000) }}s</div>
            <div class="stat-label">观看时长</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ trackingCaseCompleted ? '✅' : '⏳' }}</div>
            <div class="stat-label">完成状态</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ObjectTrackingCase',
  props: {
    trackingInteractionCount: {
      type: Number,
      default: 0
    },
    trackingTotalWatchTime: {
      type: Number,
      default: 0
    }
  },
  emits: ['case-completed', 'interaction-update'],
  data() {
    return {
      currentVideoMode: 'original', // 'original' or 'tracking'
      isVideoPlaying: false,
      trackingCaseCompleted: false,
      originalVideoSrc: '/videos/chapter3/MOT16-11-raw.webm',
      trackingVideoSrc: '/videos/chapter3/MOT16-11-gt.webm',
      playStartTime: null,
      localInteractionCount: 0,
      localTotalWatchTime: 0
    }
  },
  methods: {
    switchVideoMode(mode) {
      if (this.currentVideoMode !== mode) {
        // 暂停当前播放的视频
        this.pauseCurrentVideo()
        
        this.currentVideoMode = mode
        this.localInteractionCount++
        
        // 发送交互更新事件
        this.$emit('interaction-update', {
          interactionCount: this.localInteractionCount,
          totalWatchTime: this.localTotalWatchTime
        })
        
        // 检查是否完成案例（5次切换）
        if (this.localInteractionCount >= 5 && !this.trackingCaseCompleted) {
          this.trackingCaseCompleted = true
          this.$emit('case-completed', {
            completed: true,
            interactionCount: this.localInteractionCount,
            totalWatchTime: this.localTotalWatchTime
          })
        }
      }
    },
    
    pauseCurrentVideo() {
      if (this.$refs.originalVideo && !this.$refs.originalVideo.paused) {
        this.$refs.originalVideo.pause()
      }
      if (this.$refs.trackingVideo && !this.$refs.trackingVideo.paused) {
        this.$refs.trackingVideo.pause()
      }
      this.isVideoPlaying = false
    },
    
    handleVideoPlay(mode) {
      this.isVideoPlaying = true
      this.playStartTime = Date.now()
      
      // 暂停另一个视频
      if (mode === 'original' && this.$refs.trackingVideo) {
        this.$refs.trackingVideo.pause()
      } else if (mode === 'tracking' && this.$refs.originalVideo) {
        this.$refs.originalVideo.pause()
      }
    },
    
    handleVideoPause(mode) {
      this.isVideoPlaying = false
      
      if (this.playStartTime) {
        const watchDuration = Date.now() - this.playStartTime
        this.localTotalWatchTime += watchDuration
        this.playStartTime = null
        
        // 发送交互更新事件
        this.$emit('interaction-update', {
          interactionCount: this.localInteractionCount,
          totalWatchTime: this.localTotalWatchTime
        })
      }
    },
    
    handleVideoEnd(mode) {
      this.isVideoPlaying = false
      
      if (this.playStartTime) {
        const watchDuration = Date.now() - this.playStartTime
        this.localTotalWatchTime += watchDuration
        this.playStartTime = null
        
        // 发送交互更新事件
        this.$emit('interaction-update', {
          interactionCount: this.localInteractionCount,
          totalWatchTime: this.localTotalWatchTime
        })
      }
    }
  },
  
  beforeUnmount() {
    // 清理定时器
    if (this.playStartTime) {
      const watchDuration = Date.now() - this.playStartTime
      this.localTotalWatchTime += watchDuration
    }
  }
}
</script>

<style lang="scss" scoped>
.object-tracking-case {
  margin-bottom: 3rem;
}

.section-header {
  text-align: center;
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: var(--chapter3-header-bg, linear-gradient(135deg, #06b6d4 0%, #0891b2 100%));
  border-radius: 12px;
  color: white;
  box-shadow: 0 4px 20px rgba(6, 182, 212, 0.3);
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

.video-interaction-card {
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
  background: linear-gradient(135deg, #06b6d4, #0891b2);
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
  color: #1f2937;
}

.logo-subtitle {
  font-size: 0.9rem;
  color: #4b5563;
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
    background: var(--chapter3-status-active-bg, #cffafe);
    color: var(--chapter3-status-active-color, #0e7490);
  }
}

.status-dot {
  width: 8px;
  height: 8px;
  background: var(--chapter3-status-dot, #64748b);
  border-radius: 50%;
  
  .status-indicator.active & {
    background: var(--chapter3-status-dot-active, #06b6d4);
  }
}

.video-display-area {
  padding: 2rem;
}

.interaction-guide {
  text-align: center;
  margin-bottom: 2rem;
  
  h5 {
    font-size: 1.2rem;
    margin-bottom: 0.5rem;
    color: #1f2937;
  }
  
  p {
    color: #4b5563;
    font-size: 1rem;
  }
}

.video-controls {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 2rem;
}

.control-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  background: var(--chapter3-btn-bg, #f1f5f9);
  border: 2px solid transparent;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
  font-weight: 500;
  
  &:hover {
    background: var(--chapter3-btn-hover-bg, #e2e8f0);
    transform: translateY(-2px);
  }
  
  &.active {
    background: var(--chapter3-btn-active-bg, #06b6d4);
    color: white;
    border-color: var(--chapter3-btn-active-border, #0891b2);
  }
}

.btn-icon {
  font-size: 1.2rem;
}

.video-container {
  position: relative;
  max-width: 800px;
  margin: 0 auto 2rem;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.tracking-video {
  width: 100%;
  height: auto;
  display: block;
}

.video-indicator {
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
    background: rgba(6, 182, 212, 0.9);
  }
}

.video-explanations {
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
    border-color: var(--chapter3-explanation-active-border, #06b6d4);
    background: var(--chapter3-explanation-active-bg, #cffafe);
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

.tracking-explanation {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.tracking-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  background: var(--chapter3-tracking-item-bg, #ffffff);
  border-radius: 8px;
  border-left: 4px solid var(--chapter3-tracking-border, #06b6d4);
}

.tracking-label {
  font-weight: 600;
  color: #1f2937;
}

.tracking-desc {
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
  color: var(--chapter3-stat-value-color, #06b6d4);
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
  
  .video-controls {
    flex-direction: column;
    align-items: center;
  }
}
</style>