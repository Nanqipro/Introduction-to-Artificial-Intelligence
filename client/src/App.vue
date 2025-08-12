<template>
  <div id="app">
    <ErrorBoundary>
      <Navigation />
      <main class="main-content">
        <router-view />
      </main>
      <footer class="app-footer">
        <div class="footer-content">
          <p>&copy; 2024 GoodLab. 《人工智能概论与应用》数字化教材平台</p>
          <p class="footer-tech">由GOODLAB开发团队构建</p>
        </div>
      </footer>
      
      <!-- 通知管理器 -->
      <NotificationManager ref="notificationManager" />
    </ErrorBoundary>
  </div>
</template>

<script>
import { onMounted, provide, ref } from 'vue'
import Navigation from './components/Navigation.vue'
import ErrorBoundary from './components/ErrorBoundary.vue'
import NotificationManager from './components/NotificationManager.vue'
import { useAuth } from '@/composables/useAuth'

export default {
  name: 'App',
  components: {
    Navigation,
    ErrorBoundary,
    NotificationManager
  },
  setup() {
    const { checkAuthStatus } = useAuth()
    const notificationManager = ref(null)

    // 提供全局通知方法
    const showNotification = (options) => {
      if (notificationManager.value) {
        notificationManager.value.showNotification(options)
      }
    }

    const showSuccess = (message, options = {}) => {
      showNotification({ type: 'success', message, ...options })
    }

    const showWarning = (message, options = {}) => {
      showNotification({ type: 'warning', message, ...options })
    }

    const showError = (message, options = {}) => {
      showNotification({ type: 'error', message, ...options })
    }

    const showInfo = (message, options = {}) => {
      showNotification({ type: 'info', message, ...options })
    }

    // 提供全局通知方法
    provide('notifications', {
      showNotification,
      showSuccess,
      showWarning,
      showError,
      showInfo
    })

    // 应用启动时检查认证状态
    onMounted(() => {
      checkAuthStatus()
    })

    return {
      notificationManager
    }
  }
}
</script>

<style lang="scss">
@use './styles/variables.scss' as *;

/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  line-height: 1.6;
  color: $body-color;
  background: $body-bg;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
}

.app-footer {
  background: $footer-bg;
  color: $footer-color;
  padding: $spacing-lg 0;
  margin-top: auto;
}

.footer-content {
  max-width: $footer-max-width;
  margin: 0 auto;
  padding: 0 $spacing-sm;
  text-align: center;
}

.footer-content p {
  margin-bottom: $spacing-xs;
}

.footer-tech {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: $scrollbar-width;
}

::-webkit-scrollbar-track {
  background: $scrollbar-track;
}

::-webkit-scrollbar-thumb {
  background: $scrollbar-thumb;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: $scrollbar-thumb-hover;
}

/* 响应式图片 */
img {
  max-width: 100%;
  height: auto;
}

/* 链接样式 */
a {
  color: $link-color;
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
  }
}

/* 按钮基础样式 */
button {
  font-family: inherit;
}

/* 表单元素样式 */
input, textarea, select {
  font-family: inherit;
}

/* 焦点样式 */
:focus {
  outline: 2px solid $focus-color;
  outline-offset: 2px;
}

/* 选择文本样式 */
::selection {
  background: $selection-bg;
  color: $selection-color;
}

::-moz-selection {
  background: $selection-bg;
  color: $selection-color;
}

/* 工具类 */
.text-center {
  text-align: center;
}

.text-left {
  text-align: left;
}

.text-right {
  text-align: right;
}

.mb-1 { margin-bottom: $spacing-xs; }
.mb-2 { margin-bottom: $spacing-sm; }
.mb-3 { margin-bottom: $spacing-md; }
.mb-4 { margin-bottom: $spacing-lg; }

.mt-1 { margin-top: $spacing-xs; }
.mt-2 { margin-top: $spacing-sm; }
.mt-3 { margin-top: $spacing-md; }
.mt-4 { margin-top: $spacing-lg; }

.p-1 { padding: $spacing-xs; }
.p-2 { padding: $spacing-sm; }
.p-3 { padding: $spacing-md; }
.p-4 { padding: $spacing-lg; }

/* 响应式工具类 */
@media (max-width: 768px) {
  .hide-mobile {
    display: none;
  }
}

@media (min-width: 769px) {
  .hide-desktop {
    display: none;
  }
}
</style>
