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

.notification-toast {
  position: fixed;
  top: 20px;
  right: 20px;
  max-width: 400px;
  background: var(--card-bg, #292c33);
  border-radius: var(--card-radius, 10px);
  box-shadow: var(--shadow-lg, 0 8px 32px rgba(0, 0, 0, 0.2));
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  z-index: 10000;
  overflow: hidden;
  cursor: pointer;
  transition: all var(--transition-normal, 0.3s) ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--shadow-lg, 0 8px 32px rgba(0, 0, 0, 0.2)), 0 8px 32px rgba(0, 0, 0, 0.3);
  }
  
  &.toast-closable:hover .toast-close {
    opacity: 1;
  }
}

.toast-content {
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-md, 1.5rem);
  padding: var(--spacing-lg, 2rem);
}

.toast-icon {
  font-size: var(--font-size-xl, 1.25rem);
  flex-shrink: 0;
  margin-top: 2px;
}

.toast-body {
  flex: 1;
  min-width: 0;
}

.toast-title {
  margin: 0 0 var(--spacing-sm, 1rem) 0;
  font-size: var(--font-size-lg, 1.125rem);
  font-weight: 600;
  color: var(--text-color, #f5f6fa);
  line-height: 1.3;
}

.toast-message {
  margin: 0;
  font-size: var(--font-size-base, 1rem);
  color: var(--text-secondary-color, #b0b3b8);
  line-height: 1.5;
}

.toast-close {
  background: none;
  border: none;
  color: var(--text-secondary-color, #b0b3b8);
  font-size: var(--font-size-xl, 1.25rem);
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all var(--transition-fast, 0.15s) ease;
  opacity: 0.6;
  flex-shrink: 0;
  
  &:hover {
    background: rgba(var(--accent-color, #b0b3b8), 0.1);
    color: var(--accent-color, #b0b3b8);
    opacity: 1;
  }
  
  &:active {
    transform: scale(0.95);
  }
}

.toast-progress {
  height: 3px;
  background: linear-gradient(90deg, var(--primary-color, #18191a), var(--accent-color, #b0b3b8));
  animation: progress-shrink linear forwards;
}

@keyframes progress-shrink {
  from { width: 100%; }
  to { width: 0%; }
}

// 类型样式
.toast-success {
  border-left: 4px solid var(--success-color, #4caf50);
  
  .toast-icon {
    color: var(--success-color, #4caf50);
  }
}

.toast-warning {
  border-left: 4px solid var(--warning-color, #ff9800);
  
  .toast-icon {
    color: var(--warning-color, #ff9800);
  }
}

.toast-error {
  border-left: 4px solid var(--error-color, #f44336);
  
  .toast-icon {
    color: var(--error-color, #f44336);
  }
}

.toast-info {
  border-left: 4px solid var(--info-color, #4a90e2);
  
  .toast-icon {
    color: var(--info-color, #4a90e2);
  }
}

// 动画
.toast-enter-active,
.toast-leave-active {
  transition: all var(--transition-normal, 0.3s) ease;
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
@media (max-width: var(--breakpoint-md, 768px)) {
  .notification-toast {
    top: 10px;
    right: 10px;
    left: 10px;
    max-width: none;
  }
  
  .toast-content {
    padding: var(--spacing-md, 1.5rem);
  }
  
  .toast-title {
    font-size: var(--font-size-base, 1rem);
  }
  
  .toast-message {
    font-size: var(--font-size-sm, 0.875rem);
  }
}

// 深色主题适配
@media (prefers-color-scheme: dark) {
  .notification-toast {
    background: rgba(var(--primary-color, #18191a), 0.95);
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