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
      
      <!-- 主题切换器 -->
      <ThemeSwitcher />
      
      <!-- 通知管理器 -->
      <NotificationManager ref="notificationManager" />
      
      <!-- 首次登录密码修改弹窗 -->
      <FirstLoginPasswordChange v-if="showFirstLoginModal" />
    </ErrorBoundary>
  </div>
</template>

<script>
import { onMounted, provide, ref, computed, watch } from 'vue'
import Navigation from './components/Navigation.vue'
import ErrorBoundary from './components/ErrorBoundary.vue'
import NotificationManager from './components/NotificationManager.vue'
import ThemeSwitcher from './components/ThemeSwitcher.vue'
import FirstLoginPasswordChange from './components/FirstLoginPasswordChange.vue'
import { useAuth } from '@/composables/useAuth'

export default {
  name: 'App',
  components: {
    Navigation,
    ErrorBoundary,
    NotificationManager,
    ThemeSwitcher,
    FirstLoginPasswordChange
  },
  setup() {
    const { checkAuthStatus, isLoggedIn, currentUser, fetchUserInfo } = useAuth()
    const notificationManager = ref(null)

    // 检查是否需要显示首次登录密码修改弹窗
    const showFirstLoginModal = computed(() => {
      if (!isLoggedIn.value || !currentUser.value) {
        console.log('首次登录检查: 未登录或无用户信息', { isLoggedIn: isLoggedIn.value, currentUser: currentUser.value })
        return false
      }
      
      // 检查用户是否为首次登录且不是管理员或教师
      const userInfo = currentUser.value
      const isFirstLogin = userInfo.isFirstLogin === true || userInfo.isFirstLogin === 'true'
      const isAdmin = userInfo.role === '管理员' || userInfo.role === 'admin'
      const isTeacher = userInfo.role === 'teacher' || (userInfo.username && userInfo.username.startsWith('TCH_'))
      
      console.log('首次登录检查:', {
        userInfo,
        isFirstLogin,
        isAdmin,
        isTeacher,
        shouldShow: isFirstLogin && !isAdmin && !isTeacher
      })
      
      return isFirstLogin && !isAdmin && !isTeacher
    })

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
    onMounted(async () => {
      // 只有在有token的情况下才检查认证状态
      const token = localStorage.getItem('token')
      if (token) {
        await checkAuthStatus()
        // 如果有token但没有用户信息，尝试获取用户信息
        if (!currentUser.value) {
          await fetchUserInfo()
        }
      }
    })

    return {
      notificationManager,
      showFirstLoginModal
    }
  }
}
</script>

<style lang="scss">

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
  color: var(--body-color, #333);
  background: var(--body-bg, #f8f9fa);
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: var(--scrollbar-width, 8px);
}

::-webkit-scrollbar-track {
  background: var(--scrollbar-track, #f1f1f1);
}

::-webkit-scrollbar-thumb {
  background: var(--scrollbar-thumb, #c1c1c1);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: var(--scrollbar-thumb, #c1c1c1)-hover;
}

/* 响应式图片 */
img {
  max-width: 100%;
  height: auto;
}

/* 链接样式 */
a {
  color: var(--link-color, #667eea);
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
  outline: 2px solid var(--focus-color, #667eea);
  outline-offset: 2px;
}

/* 选择文本样式 */
::selection {
  background: var(--selection-bg, #b0b3b8);
  color: var(--selection-color, #ffffff);
}

::-moz-selection {
  background: var(--selection-bg, #b0b3b8);
  color: var(--selection-color, #ffffff);
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

.mb-1 { margin-bottom: var(--spacing-xs, 0.5rem); }
.mb-2 { margin-bottom: var(--spacing-sm, 1rem); }
.mb-3 { margin-bottom: var(--spacing-md, 1.5rem); }
.mb-4 { margin-bottom: var(--spacing-lg, 2rem); }

.mt-1 { margin-top: var(--spacing-xs, 0.5rem); }
.mt-2 { margin-top: var(--spacing-sm, 1rem); }
.mt-3 { margin-top: var(--spacing-md, 1.5rem); }
.mt-4 { margin-top: var(--spacing-lg, 2rem); }

.p-1 { padding: var(--spacing-xs, 0.5rem); }
.p-2 { padding: var(--spacing-sm, 1rem); }
.p-3 { padding: var(--spacing-md, 1.5rem); }
.p-4 { padding: var(--spacing-lg, 2rem); }

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

/* Footer样式 */
.app-footer {
  background: var(--footer-bg, #f8f9fa);
  color: var(--footer-color, #212529);
  padding: 2rem 0 1rem 0;
  margin-top: auto;
  border-top: 1px solid var(--footer-border, #dee2e6);
  backdrop-filter: blur(20px);
}

.footer-content {
  max-width: var(--footer-max-width, 1200px);
  margin: 0 auto;
  padding: 0 1rem;
  text-align: center;
}

.footer-content p {
  margin: 0.5rem 0;
  font-size: 0.9rem;
  color: var(--footer-text-primary, #6c757d);
}

.footer-tech {
  font-size: 0.8rem !important;
  color: var(--footer-text-secondary, #6c757d) !important;
  opacity: 0.8;
}

/* 确保主内容区域占满剩余空间 */
.main-content {
  min-height: calc(100vh - 200px);
  flex: 1;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
</style>
