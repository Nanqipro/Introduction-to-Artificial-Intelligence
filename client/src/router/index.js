import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import ChapterList from '../views/ChapterList.vue'
import ChapterDetail from '../views/ChapterDetail.vue'
import About from '../views/About.vue'

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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
