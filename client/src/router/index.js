import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import ChapterList from '../views/ChapterList.vue'
import ChapterDetail from '../views/ChapterDetail.vue'
import About from '../views/About.vue'
import QuizPage from '../views/QuizPage.vue'
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
    path: '/regression-demo',
    name: 'RegressionDemo',
    component: () => import('@/views/RegressionDemo.vue')
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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  // 如果路由需要认证但用户未登录，跳转到登录页
  if (to.meta.requiresAuth && !token) {
    next('/login')
    return
  }

  // 如果用户已登录且访问登录页，跳转到首页
  if (to.name === 'LoginPage' && token) {
    next('/')
    return
  }

  next()
})

export default router
