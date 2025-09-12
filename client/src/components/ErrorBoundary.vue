<template>
  <div v-if="hasError" class="error-boundary">
    <div class="error-container">
      <div class="error-icon">⚠️</div>
      <h3 class="error-title">出现了一些问题</h3>
      <p class="error-message">{{ errorMessage }}</p>
      <div class="error-actions">
        <el-button @click="retry" type="primary" :icon="RefreshIcon">
          重试
        </el-button>
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
import { ElButton } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'

export default {
  name: 'ErrorBoundary',
  components: {
    ElButton
  },
  data() {
    return {
      RefreshIcon: Refresh,
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

.error-boundary {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--body-bg, #18191a);
  padding: var(--spacing-lg, 2rem);
}

.error-container {
  max-width: 600px;
  text-align: center;
  background: var(--card-bg, #292c33);
  border-radius: var(--card-radius, 10px);
  padding: var(--spacing-2xl, 3rem);
  box-shadow: var(--card-shadow, 0 4px 24px rgba(24, 25, 26, 0.10));
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
}

.error-icon {
  font-size: 4rem;
  margin-bottom: var(--spacing-lg, 2rem);
  animation: bounce 2s infinite;
}

.error-title {
  font-size: var(--font-size-2xl, 1.5rem);
  color: var(--text-color, #f5f6fa);
  margin-bottom: var(--spacing-md, 1.5rem);
  font-weight: 700;
}

.error-message {
  color: var(--text-secondary-color, #b0b3b8);
  margin-bottom: var(--spacing-xl, 2.5rem);
  line-height: 1.6;
  font-size: var(--font-size-lg, 1.125rem);
}

.error-actions {
  display: flex;
  gap: var(--spacing-md, 1.5rem);
  justify-content: center;
  flex-wrap: wrap;
  margin-bottom: var(--spacing-xl, 2.5rem);
}

.btn {
  padding: var(--spacing-md, 1.5rem) var(--spacing-lg, 2rem);
  border: none;
  border-radius: var(--btn-radius, 12px);
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-normal, 0.3s) ease;
  display: flex;
  align-items: center;
  gap: var(--spacing-sm, 1rem);
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--btn-shadow, 0 4px 16px rgba(74, 144, 226, 0.3));
  }
}

.btn-primary {
  background: var(--btn-primary-bg, linear-gradient(135deg, #3b82f6, #2563eb));
  color: var(--text-color, #f5f6fa);
  
  &:hover {
    filter: brightness(1.05);
  }
}

.btn-secondary {
  background: var(--btn-secondary-bg, #393b40);
  color: var(--text-color, #f5f6fa);
  
  &:hover {
    filter: brightness(1.05);
  }
}

.btn-outline {
  background: transparent;
  color: var(--accent-color, #b0b3b8);
  border: 2px solid var(--accent-color, #b0b3b8);
  
  &:hover {
    background: var(--accent-color, #b0b3b8);
    color: var(--text-color, #f5f6fa);
  }
}

.error-details {
  margin-top: var(--spacing-lg, 2rem);
  text-align: left;
  
  details {
    color: var(--text-secondary-color, #b0b3b8);
    
    summary {
      cursor: pointer;
      padding: var(--spacing-sm, 1rem);
      border-radius: var(--form-radius, 10px);
      background: var(--form-bg, #23272e);
      margin-bottom: var(--spacing-sm, 1rem);
      
      &:hover {
        background: var(--card-bg, #292c33);
      }
    }
  }
  
  .error-stack {
    background: var(--form-bg, #23272e);
    padding: var(--spacing-md, 1.5rem);
    border-radius: var(--form-radius, 10px);
    overflow-x: auto;
    font-size: var(--font-size-sm, 0.875rem);
    line-height: 1.4;
    color: var(--text-secondary-color, #b0b3b8);
    border: 1px solid var(--form-border, rgba(57, 59, 64, 0.18));
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

@media (max-width: var(--breakpoint-md, 768px)) {
  .error-container {
    padding: var(--spacing-xl, 2.5rem);
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