import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import ChapterList from '../views/ChapterList.vue'
import ChapterDetail from '../views/ChapterDetail.vue'
import About from '../views/About.vue'
import QuizPage from '../views/QuizPage.vue'
import QuizSelection from '../views/QuizSelection.vue'
import StatsPage from '../views/StatsPage.vue'
import AdminPage from '../views/AdminPage.vue'
import LoginPage from '../views/LoginPage.vue'
import UserProfile from '../views/UserProfile.vue'
import SystemTestPage from '../views/SystemTestPage.vue'



const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: '首页',
      description: '人工智能概论与应用数字化教材平台首页'
    }
  },
  {
    path: '/chapters',
    name: 'ChapterList',
    component: ChapterList
  },
  {
    path: '/chapters/:id',
    name: 'ChapterDetail',
    component: ChapterDetail,
    props: true
  },
  {
    path: '/about',
    name: 'About',
    component: About
  },

  {
    path: '/network-training',
    name: 'NetworkTraining',
    component: () => import('@/views/NetworkTraining.vue'),
    meta: {
      title: '神经网络训练实验室',
      description: '交互式神经网络训练可视化'
    }
  },
  {
    path: '/regression-demo',
    name: 'RegressionDemo',
    component: () => import('@/views/RegressionDemo.vue'),
    meta: {
      title: '回归演示实验室',
      description: '线性回归与逻辑回归交互式演示'
    }
  },
  {
    path: '/quiz',
    name: 'QuizSelection',
    component: QuizSelection,
    meta: {
      title: '题目自测',
      description: '选择章节进行知识测验'
    }
  },
  {
    path: '/quiz/:chapterId',
    name: 'QuizPage',
    component: QuizPage,
    props: true,
    meta: {
      title: '知识测验',
      description: '章节知识测验系统'
    }
  },
  {
    path: '/stats',
    name: 'StatsPage',
    component: StatsPage,
    meta: {
      title: '学习统计',
      description: '用户学习统计和排行榜'
    }
  },
  {
    path: '/admin',
    name: 'AdminPage',
    component: AdminPage,
    meta: {
      title: '管理员控制台',
      description: '题目管理和Excel导入功能',
      requiresAuth: true,
      requiresAdmin: true
    }
  },
  {
    path: '/login',
    name: 'LoginPage',
    component: LoginPage,
    meta: {
      title: '用户登录',
      description: '用户登录注册页面'
    }
  },
  {
    path: '/profile',
    name: 'UserProfile',
    component: UserProfile,
    meta: {
      title: '个人中心',
      description: '用户个人信息管理',
      requiresAuth: true
    }
  },
  {
    path: '/system-test',
    name: 'SystemTestPage',
    component: SystemTestPage,
    meta: {
      title: '系统测试',
      description: '系统功能测试和诊断工具'
    }
  },


]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = localStorage.getItem('userInfo')
  let currentUser = null
  
  try {
    currentUser = userInfo ? JSON.parse(userInfo) : null
  } catch (e) {
    console.error('解析用户信息失败:', e)
  }

  console.log(`🛣️ 路由守卫: ${from.path} -> ${to.path}`)
  console.log(`🔑 Token状态: ${token ? 'exists' : 'null'}`)
  console.log(`👤 用户角色: ${currentUser?.role || 'unknown'}`)

  // 如果用户已登录且访问登录页，跳转到首页
  if (to.name === 'LoginPage' && token) {
    console.log('🏠 已登录用户访问登录页，跳转到首页')
    next('/')
    return
  }

  // 如果用户未登录且访问首页，跳转到登录页
  if (to.name === 'Home' && !token) {
    console.log('🔒 未登录用户访问首页，跳转到登录页')
    next('/login')
    return
  }

  // 如果路由需要认证但用户未登录，跳转到登录页
  if (to.meta.requiresAuth && !token) {
    console.log(`🔒 需要认证但未登录，跳转到登录页: ${to.path}`)
    next('/login')
    return
  }
  
  // 如果路由需要管理员权限但用户不是管理员，显示权限不足
  if (to.meta.requiresAdmin && currentUser?.role !== 'admin') {
    console.log(`🚫 需要管理员权限但用户角色不符: ${currentUser?.role || 'unknown'}`)
    // 可以跳转到一个权限不足的页面，或者显示提示
    alert('访问被拒绝：只有管理员才能访问此功能\n\n管理员账号：goodlabAdmin\n管理员密码：goodlabPwd')
    next('/')
    return
  }

  console.log(`✅ 允许导航到: ${to.path}`)
  next()
})

// 路由导航完成后的处理
router.afterEach((to, from) => {
  console.log(`✅ 路由导航完成: ${to.path}`)
})

export default router
