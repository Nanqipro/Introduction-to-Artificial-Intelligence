import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './styles/theme.css'
import { useAuth } from '@/composables/useAuth'

// 全局关闭控制台输出（防止在F12控制台暴露信息）
(() => {
  const noop = () => {}
  const methods = ['log', 'info', 'warn', 'error', 'debug', 'trace', 'group', 'groupCollapsed', 'groupEnd']
  for (const m of methods) {
    try {
      console[m] = noop
    } catch (_) {
      /* ignore */
    }
  }
  // 关闭Vue生产环境devtools标志（进一步减少调试信息）
  try { window.__VUE_PROD_DEVTOOLS__ = false } catch (_) {}
})()

// 创建应用实例
const app = createApp(App)
app.use(router)
app.use(ElementPlus)

// localStorage监听（生产环境已禁用调试输出）

// 应用启动前的异步初始化
const initializeApp = async () => {
  // 开始初始化应用

  try {
    // 检查是否有token再初始化认证状态
    const token = localStorage.getItem('token')
    if (token) {
      const { forceRefreshAuth } = useAuth()
      // 有token，初始化认证状态
      
      // 只刷新本地状态，不发送网络请求
      await forceRefreshAuth()
      // 本地认证状态已刷新
    } else {
      // 无token，跳过认证状态初始化
    }

    // 立即挂载应用
    // 挂载Vue应用
    app.mount('#app')
    // 应用启动完成

  } catch (error) {
    // 应用初始化失败
    // 确保应用能够启动
    app.mount('#app')
  }
}

// 执行初始化
initializeApp()
