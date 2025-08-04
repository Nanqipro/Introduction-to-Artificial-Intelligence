import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8082',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    console.log('发送请求:', config.method?.toUpperCase(), config.url)

    // 添加JWT token到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = token
    }

    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    console.log('收到响应:', response.status, response.data)
    // 处理后端统一响应格式
    if (response.data && response.data.code === 0) {
      return response.data
    }
    return response.data
  },
  error => {
    console.error('响应错误:', error.response?.status, error.response?.data || error.message)

    // 处理401未授权错误
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      // 可以在这里触发登录页面跳转
      window.location.href = '/login'
      return Promise.reject(new Error('登录已过期，请重新登录'))
    }

    // 处理后端错误响应
    if (error.response?.data?.message) {
      const errorMessage = error.response.data.message
      console.error('后端错误信息:', errorMessage)
      return Promise.reject(new Error(errorMessage))
    }

    // 处理网络错误
    if (!error.response) {
      return Promise.reject(new Error('网络连接失败，请检查网络设置'))
    }

    return Promise.reject(error)
  }
)

// 章节相关API
export const chapterApi = {
  // 获取章节概览列表
  getChapterOverview() {
    return api.get('/api/chapters')
  },

  // 获取所有章节
  getAllChapters() {
    return api.get('/api/chapters/all')
  },

  // 根据ID获取章节详情
  getChapterById(id) {
    return api.get(`/api/chapters/${id}`)
  },

  // 创建章节
  createChapter(chapter) {
    return api.post('/api/chapters', chapter)
  },

  // 更新章节
  updateChapter(id, chapter) {
    return api.put(`/api/chapters/${id}`, chapter)
  },

  // 删除章节
  deleteChapter(id) {
    return api.delete(`/api/chapters/${id}`)
  },

  // 健康检查
  healthCheck() {
    return api.get('/api/chapters/health')
  }
}

// 答题系统相关API
export const quizApi = {
  // 根据章节获取题目
  getQuestionsByChapter(chapterId) {
    return api.get(`/api/quiz/questions/${chapterId}`)
  },

  // 保存答题结果
  saveQuizResult(result) {
    return api.post('/api/quiz/results', result)
  },

  // 获取用户答题历史
  getUserQuizHistory() {
    return api.get('/api/quiz/history')
  },

  // 获取用户统计信息
  getUserStats() {
    return api.get('/api/quiz/stats')
  },

  // 获取排行榜
  getLeaderboard() {
    return api.get('/api/quiz/leaderboard')
  }
}

// 管理员相关API
export const adminApi = {
  // 获取所有题目
  getAllQuestions() {
    return api.get('/api/admin/questions')
  },

  // 根据ID获取题目
  getQuestionById(id) {
    return api.get(`/api/admin/questions/${id}`)
  },

  // 创建题目
  createQuestion(question) {
    return api.post('/api/admin/questions', question)
  },

  // 更新题目
  updateQuestion(id, question) {
    return api.put(`/api/admin/questions/${id}`, question)
  },

  // 删除题目
  deleteQuestion(id) {
    return api.delete(`/api/admin/questions/${id}`)
  },

  // 导入Excel文件
  importQuestions(file) {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/admin/questions/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 获取题目统计
  getQuestionStats() {
    return api.get('/api/admin/questions/stats')
  },

  // 根据章节获取题目
  getQuestionsByChapter(chapterId) {
    return api.get(`/api/admin/questions/chapter/${chapterId}`)
  },

  // 健康检查
  healthCheck() {
    return api.get('/api/admin/health')
  }
}

// 用户相关API
export const userApi = {
  // 用户注册
  register(userData) {
    const formData = new URLSearchParams()
    formData.append('username', userData.username)
    formData.append('password', userData.password)

    return api.post('/user/register', formData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
  },

  // 用户登录
  login(userData) {
    const formData = new URLSearchParams()
    formData.append('username', userData.username)
    formData.append('password', userData.password)

    return api.post('/user/login', formData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
  },

  // 获取用户信息
  getUserInfo() {
    return api.get('/user/userInfo')
  },

  // 更新用户基本信息
  updateUserInfo(userInfo) {
    return api.put('/user/update', userInfo)
  },

  // 更新用户头像
  updateAvatar(avatarUrl) {
    const params = new URLSearchParams()
    params.append('avatarUrl', avatarUrl)

    return api.patch(`/user/updateAvatar?${params.toString()}`)
  },

  // 更新用户密码
  updatePassword(passwordData) {
    return api.patch('/user/updatePwd', passwordData)
  }
}

export default api
