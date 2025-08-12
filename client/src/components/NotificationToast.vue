<template>
  <transition name="toast" appear>
    <div 
      v-if="visible" 
      class="notification-toast"
      :class="[`toast-${type}`, { 'toast-closable': closable }]"
      @click="handleClick"
    >
      <div class="toast-content">
        <!-- 图标 -->
        <div class="toast-icon">
          <span v-if="type === 'success'">✅</span>
          <span v-else-if="type === 'warning'">⚠️</span>
          <span v-else-if="type === 'error'">❌</span>
          <span v-else-if="type === 'info'">ℹ️</span>
          <span v-else>📢</span>
        </div>
        
        <!-- 内容 -->
        <div class="toast-body">
          <h4 v-if="title" class="toast-title">{{ title }}</h4>
          <p class="toast-message">{{ message }}</p>
        </div>
        
        <!-- 关闭按钮 -->
        <button 
          v-if="closable" 
          @click.stop="close"
          class="toast-close"
          :aria-label="'关闭通知'"
        >
          ×
        </button>
      </div>
      
      <!-- 进度条 -->
      <div 
        v-if="autoClose && duration > 0" 
        class="toast-progress"
        :style="{ animationDuration: `${duration}ms` }"
      ></div>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'NotificationToast',
  props: {
    type: {
      type: String,
      default: 'info',
      validator: value => ['success', 'warning', 'error', 'info'].includes(value)
    },
    title: {
      type: String,
      default: ''
    },
    message: {
      type: String,
      required: true
    },
    duration: {
      type: Number,
      default: 5000
    },
    autoClose: {
      type: Boolean,
      default: true
    },
    closable: {
      type: Boolean,
      default: true
    },
    persistent: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      visible: false,
      timer: null
    }
  },
  mounted() {
    this.show()
  },
  beforeUnmount() {
    this.clearTimer()
  },
  methods: {
    show() {
      this.visible = true
      this.$emit('show')
      
      if (this.autoClose && this.duration > 0 && !this.persistent) {
        this.startTimer()
      }
    },
    
    close() {
      this.visible = false
      this.clearTimer()
      this.$emit('close')
      this.$emit('update:visible', false)
    },
    
    startTimer() {
      this.clearTimer()
      this.timer = setTimeout(() => {
        this.close()
      }, this.duration)
    },
    
    clearTimer() {
      if (this.timer) {
        clearTimeout(this.timer)
        this.timer = null
      }
    },
    
    handleClick() {
      if (!this.closable) return
      this.close()
    }
  }
}
</script>

<style lang="scss" scoped>
@use '../styles/variables.scss' as *;

.notification-toast {
  position: fixed;
  top: 20px;
  right: 20px;
  max-width: 400px;
  background: $card-bg;
  border-radius: $card-radius;
  box-shadow: $shadow-lg;
  border: 1px solid $card-border;
  z-index: 10000;
  overflow: hidden;
  cursor: pointer;
  transition: all $transition-normal ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: $shadow-lg, 0 8px 32px rgba(0, 0, 0, 0.3);
  }
  
  &.toast-closable:hover .toast-close {
    opacity: 1;
  }
}

.toast-content {
  display: flex;
  align-items: flex-start;
  gap: $spacing-md;
  padding: $spacing-lg;
}

.toast-icon {
  font-size: $font-size-xl;
  flex-shrink: 0;
  margin-top: 2px;
}

.toast-body {
  flex: 1;
  min-width: 0;
}

.toast-title {
  margin: 0 0 $spacing-sm 0;
  font-size: $font-size-lg;
  font-weight: 600;
  color: $text-color;
  line-height: 1.3;
}

.toast-message {
  margin: 0;
  font-size: $font-size-base;
  color: $text-secondary-color;
  line-height: 1.5;
}

.toast-close {
  background: none;
  border: none;
  color: $text-secondary-color;
  font-size: $font-size-xl;
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all $transition-fast ease;
  opacity: 0.6;
  flex-shrink: 0;
  
  &:hover {
    background: rgba($accent-color, 0.1);
    color: $accent-color;
    opacity: 1;
  }
  
  &:active {
    transform: scale(0.95);
  }
}

.toast-progress {
  height: 3px;
  background: linear-gradient(90deg, $primary-color, $accent-color);
  animation: progress-shrink linear forwards;
}

@keyframes progress-shrink {
  from { width: 100%; }
  to { width: 0%; }
}

// 类型样式
.toast-success {
  border-left: 4px solid $success-color;
  
  .toast-icon {
    color: $success-color;
  }
}

.toast-warning {
  border-left: 4px solid $warning-color;
  
  .toast-icon {
    color: $warning-color;
  }
}

.toast-error {
  border-left: 4px solid $error-color;
  
  .toast-icon {
    color: $error-color;
  }
}

.toast-info {
  border-left: 4px solid $info-color;
  
  .toast-icon {
    color: $info-color;
  }
}

// 动画
.toast-enter-active,
.toast-leave-active {
  transition: all $transition-normal ease;
}

.toast-enter-from {
  opacity: 0;
  transform: translateX(100%) translateY(-20px);
}

.toast-leave-to {
  opacity: 0;
  transform: translateX(100%) translateY(-20px);
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .notification-toast {
    top: 10px;
    right: 10px;
    left: 10px;
    max-width: none;
  }
  
  .toast-content {
    padding: $spacing-md;
  }
  
  .toast-title {
    font-size: $font-size-base;
  }
  
  .toast-message {
    font-size: $font-size-sm;
  }
}

// 深色主题适配
@media (prefers-color-scheme: dark) {
  .notification-toast {
    background: rgba($primary-color, 0.95);
    backdrop-filter: blur(8px);
  }
}

// 减少动画（用户偏好）
@media (prefers-reduced-motion: reduce) {
  .notification-toast {
    transition: none;
    
    &:hover {
      transform: none;
    }
  }
  
  .toast-enter-active,
  .toast-leave-active {
    transition: none;
  }
  
  .toast-progress {
    animation: none;
  }
}
</style> 