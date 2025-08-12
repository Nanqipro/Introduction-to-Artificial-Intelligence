<template>
  <div class="loading-spinner" :class="{ 'fullscreen': fullscreen, 'overlay': overlay }">
    <div class="spinner-container">
      <!-- 主加载动画 -->
      <div class="spinner-main">
        <div class="spinner-ring"></div>
        <div class="spinner-ring"></div>
        <div class="spinner-ring"></div>
      </div>
      
      <!-- 加载文本 -->
      <div class="loading-text" v-if="text">
        <span class="text-content">{{ text }}</span>
        <span class="text-dots">
          <span class="dot" v-for="i in 3" :key="i" :style="{ animationDelay: `${i * 0.2}s` }"></span>
        </span>
      </div>
      
      <!-- 进度条（如果提供） -->
      <div class="progress-bar" v-if="showProgress && progress !== null">
        <div class="progress-track">
          <div class="progress-fill" :style="{ width: `${progress}%` }"></div>
        </div>
        <span class="progress-text">{{ Math.round(progress) }}%</span>
      </div>
      
      <!-- 取消按钮（如果可取消） -->
      <button 
        v-if="cancellable" 
        @click="cancel" 
        class="cancel-btn"
      >
        取消
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoadingSpinner',
  props: {
    text: {
      type: String,
      default: '加载中'
    },
    fullscreen: {
      type: Boolean,
      default: false
    },
    overlay: {
      type: Boolean,
      default: false
    },
    progress: {
      type: Number,
      default: null
    },
    showProgress: {
      type: Boolean,
      default: false
    },
    cancellable: {
      type: Boolean,
      default: false
    }
  },
  emits: ['cancel'],
  methods: {
    cancel() {
      this.$emit('cancel')
    }
  }
}
</script>

<style lang="scss" scoped>
@use '../styles/variables.scss' as *;

.loading-spinner {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: $spacing-xl;
  
  &.fullscreen {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba($secondary-color, 0.95);
    backdrop-filter: blur(8px);
    z-index: 9999;
  }
  
  &.overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba($secondary-color, 0.8);
    backdrop-filter: blur(4px);
    z-index: 100;
  }
}

.spinner-container {
  text-align: center;
  max-width: 300px;
}

.spinner-main {
  position: relative;
  width: 80px;
  height: 80px;
  margin: 0 auto $spacing-lg;
}

.spinner-ring {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: 3px solid transparent;
  border-top: 3px solid $accent-color;
  border-radius: 50%;
  animation: spin 1.2s linear infinite;
  
  &:nth-child(2) {
    width: 60px;
    height: 60px;
    top: 10px;
    left: 10px;
    border-top-color: $primary-color;
    animation-duration: 1s;
    animation-direction: reverse;
  }
  
  &:nth-child(3) {
    width: 40px;
    height: 40px;
    top: 20px;
    left: 20px;
    border-top-color: $accent-color-light;
    animation-duration: 0.8s;
  }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  margin-bottom: $spacing-lg;
  color: $text-color;
  font-size: $font-size-lg;
  font-weight: 500;
  
  .text-content {
    margin-right: $spacing-sm;
  }
  
  .text-dots {
    display: inline-flex;
    gap: 2px;
    
    .dot {
      width: 4px;
      height: 4px;
      background: $accent-color;
      border-radius: 50%;
      animation: pulse 1.4s ease-in-out infinite both;
    }
  }
}

@keyframes pulse {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.progress-bar {
  margin-bottom: $spacing-lg;
  
  .progress-track {
    width: 100%;
    height: 6px;
    background: rgba($accent-color, 0.2);
    border-radius: 3px;
    overflow: hidden;
    margin-bottom: $spacing-sm;
  }
  
  .progress-fill {
    height: 100%;
    background: linear-gradient(90deg, $primary-color, $accent-color);
    border-radius: 3px;
    transition: width 0.3s ease;
    position: relative;
    
    &::after {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
      animation: shimmer 2s infinite;
    }
  }
  
  .progress-text {
    color: $text-secondary-color;
    font-size: $font-size-sm;
    font-weight: 500;
  }
}

@keyframes shimmer {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.cancel-btn {
  background: transparent;
  border: 2px solid $accent-color;
  color: $accent-color;
  padding: $spacing-sm $spacing-lg;
  border-radius: $btn-radius;
  cursor: pointer;
  font-weight: 500;
  transition: all $transition-normal ease;
  
  &:hover {
    background: $accent-color;
    color: $text-color;
    transform: translateY(-1px);
  }
  
  &:active {
    transform: translateY(0);
  }
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .spinner-main {
    width: 60px;
    height: 60px;
    
    .spinner-ring:nth-child(2) {
      width: 45px;
      height: 45px;
      top: 7.5px;
      left: 7.5px;
    }
    
    .spinner-ring:nth-child(3) {
      width: 30px;
      height: 30px;
      top: 15px;
      left: 15px;
    }
  }
  
  .loading-text {
    font-size: $font-size-base;
  }
}

// 深色主题适配
@media (prefers-color-scheme: dark) {
  .loading-spinner {
    &.fullscreen,
    &.overlay {
      background: rgba($primary-color, 0.95);
    }
  }
}

// 减少动画（用户偏好）
@media (prefers-reduced-motion: reduce) {
  .spinner-ring {
    animation-duration: 2s !important;
  }
  
  .text-dots .dot {
    animation: none;
  }
  
  .progress-fill::after {
    animation: none;
  }
}
</style> 