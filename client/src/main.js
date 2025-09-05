import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './styles/theme.css'
import { useAuth } from '@/composables/useAuth'

// 保留控制台输出以便调试
// (() => {
//   const noop = () => { }
//   const methods = ['log', 'info', 'warn', 'error', 'debug', 'trace', 'group', 'groupCollapsed', 'groupEnd']
//   for (const m of methods) {
//     try {
//       // eslint-disable-next-line no-console
//       console[m] = noop
//     } catch (_) {
//       /* ignore */
//     }
//   }
// })()

// 创建应用实例
const app = createApp(App)
app.use(router)
app.use(ElementPlus)

// 监控localStorage变化的调试功能
const originalSetItem = localStorage.setItem
const originalRemoveItem = localStorage.removeItem

localStorage.setItem = function (key, value) {
  if (key === 'token') {
    console.log('🔍 localStorage.setItem - token被设置:', value ? value.substring(0, 20) + '...' : 'null')
    console.trace('设置token的调用栈:')
  }
  return originalSetItem.call(this, key, value)
}

localStorage.removeItem = function (key) {
  if (key === 'token') {
    console.log('🔍 localStorage.removeItem - token被删除')
    console.trace('删除token的调用栈:')
  }
  return originalRemoveItem.call(this, key)
}

// 应用启动前的异步初始化
const initializeApp = async () => {
  console.log('🚀 开始初始化应用...')

  try {
    // 检查是否有token再初始化认证状态
    const token = localStorage.getItem('token')
    if (token) {
      const { forceRefreshAuth } = useAuth()
      console.log('🔐 有token，初始化认证状态...')
      
      // 只刷新本地状态，不发送网络请求
      await forceRefreshAuth()
      console.log('✅ 本地认证状态已刷新')
    } else {
      console.log('🔓 无token，跳过认证状态初始化')
    }

    // 立即挂载应用
    console.log('🎯 挂载Vue应用...')
    app.mount('#app')
    console.log('✅ 应用启动完成')

  } catch (error) {
    console.error('❌ 应用初始化失败:', error)
    // 确保应用能够启动
    app.mount('#app')
  }
}

// 执行初始化
initializeApp()
