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
    component: Home
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
      requiresAuth: true
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
  // 直接从localStorage获取token，确保与useAuth中的状态同步
  const token = localStorage.getItem('token')

  console.log(`🛣️ 路由守卫: 导航到 ${to.path}, 来自 ${from.path}`)
  console.log(`🔑 当前token状态: ${token ? 'exists' : 'null'}`)
  if (token) {
    console.log(`🔑 Token内容: ${token.substring(0, 30)}...`)
  }

  // 如果路由需要认证但用户未登录，跳转到登录页
  if (to.meta.requiresAuth && !token) {
    console.log('🔒 路由守卫: 需要认证但未登录，跳转到登录页')
    next('/login')
    return
  }

  // 如果用户已登录且访问登录页，跳转到首页
  if (to.name === 'LoginPage' && token) {
    console.log('🏠 路由守卫: 已登录用户访问登录页，跳转到首页')
    next('/')
    return
  }

  // 如果访问需要认证的页面且有token，简化验证逻辑
  if (to.meta.requiresAuth && token) {
    console.log('🔍 路由守卫: 访问需要认证的页面，简化验证')
    console.log('🔍 目标页面:', to.path, '需要认证:', to.meta.requiresAuth)

    // 简化验证：只要有token就允许访问，让页面自己处理用户信息获取
    console.log('✅ 路由守卫: 有token，允许访问需要认证的页面')
  }

  console.log('✅ 路由守卫: 允许导航到', to.path)

  // 如果是导航到个人中心页面，增加额外的调试信息
  if (to.path === '/profile') {
    console.log('🔍 个人中心页面导航: 即将进入个人中心页面')
    console.log('🔍 个人中心页面导航: 当前认证状态', {
      hasToken: !!localStorage.getItem('token'),
      fromPath: from.path
    })
  }

  next()
})

export default router
