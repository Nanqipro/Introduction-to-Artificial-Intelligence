<template>
  <div v-if="hasError" class="error-boundary">
    <div class="error-container">
      <div class="error-icon">⚠️</div>
      <h3 class="error-title">出现了一些问题</h3>
      <p class="error-message">{{ errorMessage }}</p>
      <div class="error-actions">
        <button @click="retry" class="btn btn-primary">
          🔄 重试
        </button>
        <button @click="goHome" class="btn btn-secondary">
          🏠 返回首页
        </button>
        <button @click="reportError" class="btn btn-outline">
          📧 报告问题
        </button>
      </div>
      <div class="error-details" v-if="showDetails">
        <details>
          <summary>错误详情</summary>
          <pre class="error-stack">{{ errorStack }}</pre>
        </details>
      </div>
    </div>
  </div>
  <slot v-else />
</template>

<script>
export default {
  name: 'ErrorBoundary',
  data() {
    return {
      hasError: false,
      errorMessage: '',
      errorStack: '',
      showDetails: false
    }
  },
  errorCaptured(err, vm, info) {
    console.error('ErrorBoundary 捕获到错误:', err, vm, info)
    
    this.hasError = true
    this.errorMessage = err.message || '组件渲染时发生未知错误'
    this.errorStack = err.stack || info || '无堆栈信息'
    
    // 记录错误到控制台
    console.group('🚨 错误详情')
    console.error('错误信息:', err.message)
    console.error('错误堆栈:', err.stack)
    console.error('组件信息:', info)
    console.error('Vue实例:', vm)
    console.groupEnd()
    
    return false // 阻止错误继续传播
  },
  methods: {
    retry() {
      this.hasError = false
      this.errorMessage = ''
      this.errorStack = ''
      this.showDetails = false
      
      // 触发组件重新渲染
      this.$forceUpdate()
    },
    
    goHome() {
      this.$router.push('/')
    },
    
    reportError() {
      // 这里可以集成错误报告服务
      const errorReport = {
        message: this.errorMessage,
        stack: this.errorStack,
        url: window.location.href,
        timestamp: new Date().toISOString(),
        userAgent: navigator.userAgent
      }
      
      console.log('错误报告:', errorReport)
      
      // 显示成功提示
      this.$message({
        message: '错误报告已生成，请查看控制台',
        type: 'success',
        duration: 2000
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@use '../styles/variables.scss' as *;

.error-boundary {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: $secondary-color;
  padding: $spacing-lg;
}

.error-container {
  max-width: 600px;
  text-align: center;
  background: $card-bg;
  border-radius: $card-radius;
  padding: $spacing-2xl;
  box-shadow: $card-shadow;
  border: 1px solid $card-border;
}

.error-icon {
  font-size: 4rem;
  margin-bottom: $spacing-lg;
  animation: bounce 2s infinite;
}

.error-title {
  font-size: $font-size-2xl;
  color: $text-color;
  margin-bottom: $spacing-md;
  font-weight: 700;
}

.error-message {
  color: $text-secondary-color;
  margin-bottom: $spacing-xl;
  line-height: 1.6;
  font-size: $font-size-lg;
}

.error-actions {
  display: flex;
  gap: $spacing-md;
  justify-content: center;
  flex-wrap: wrap;
  margin-bottom: $spacing-xl;
}

.btn {
  padding: $spacing-md $spacing-lg;
  border: none;
  border-radius: $btn-radius;
  font-weight: 600;
  cursor: pointer;
  transition: all $transition-normal ease;
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: $btn-shadow;
  }
}

.btn-primary {
  background: $primary-color;
  color: white;
  
  &:hover {
    background: $primary-hover-color;
  }
}

.btn-secondary {
  background: $secondary-color-light;
  color: $text-color;
  
  &:hover {
    background: $card-bg;
  }
}

.btn-outline {
  background: transparent;
  color: $accent-color;
  border: 2px solid $accent-color;
  
  &:hover {
    background: $accent-color;
    color: $text-color;
  }
}

.error-details {
  margin-top: $spacing-lg;
  text-align: left;
  
  details {
    color: $text-secondary-color;
    
    summary {
      cursor: pointer;
      padding: $spacing-sm;
      border-radius: $form-radius;
      background: $form-bg;
      margin-bottom: $spacing-sm;
      
      &:hover {
        background: $card-bg;
      }
    }
  }
  
  .error-stack {
    background: $form-bg;
    padding: $spacing-md;
    border-radius: $form-radius;
    overflow-x: auto;
    font-size: $font-size-sm;
    line-height: 1.4;
    color: $text-secondary-color;
    border: 1px solid $form-border;
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

@media (max-width: $breakpoint-md) {
  .error-container {
    padding: $spacing-xl;
  }
  
  .error-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .btn {
    width: 100%;
    max-width: 300px;
    justify-content: center;
  }
}
</style> 