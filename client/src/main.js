import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { useAuth } from '@/composables/useAuth'

// 创建应用实例
const app = createApp(App)
app.use(router)
app.use(ElementPlus)

// 应用启动前的异步初始化
const initializeApp = async () => {
  console.log('🚀 开始初始化应用...')

  try {
    // 初始化认证状态
    const { checkAuthStatus, token, userInfo, isLoggedIn } = useAuth()
    console.log('🔐 初始化认证状态...')
    console.log('🔍 启动时检查:', {
      hasToken: !!token.value,
      hasUserInfo: !!userInfo.value,
      isLoggedIn: isLoggedIn.value
    })

    await checkAuthStatus()

    console.log('✅ 认证状态初始化完成:', {
      hasToken: !!token.value,
      hasUserInfo: !!userInfo.value,
      isLoggedIn: isLoggedIn.value
    })

    // 挂载应用
    console.log('🎯 挂载Vue应用...')
    app.mount('#app')
    console.log('✅ 应用启动完成')

  } catch (error) {
    console.error('❌ 应用初始化失败:', error)
    // 即使认证初始化失败，也要挂载应用
    app.mount('#app')
  }
}

// 执行初始化
initializeApp()
