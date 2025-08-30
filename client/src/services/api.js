import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8082', // 指向后端服务地址
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    console.log('🚀 发送请求:', config.method?.toUpperCase(), config.url)

    // 添加JWT token到请求头
    const token = localStorage.getItem('token')
    console.log('📝 本地存储的token:', token ? token.substring(0, 20) + '...' : 'null')

    if (token) {
      // 确保token格式正确（不重复添加Bearer前缀）
      const formattedToken = token.startsWith('Bearer ') ? token : `Bearer ${token}`
      config.headers.Authorization = formattedToken
      console.log('✅ 已添加Authorization头部:', formattedToken.substring(0, 20) + '...')
    } else {
      console.warn('⚠️ 警告: 没有找到token，该请求可能会被拒绝')
      console.log('🔍 当前请求URL:', config.url)
      console.log('🔍 请求类型:', config.method)
    }

    // 添加详细的请求日志
    console.log('📋 完整请求头部:', {
      'Content-Type': config.headers['Content-Type'],
      'Authorization': config.headers.Authorization ? config.headers.Authorization.substring(0, 30) + '...' : 'none'
    })

    return config
  },
  error => {
    console.error('❌ 请求拦截器错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    console.log('收到响应:', response.status, response.data)
    // 处理后端统一响应格式
    if (response.data && typeof response.data === 'object') {
      // 后端成功响应的code是200
      if (response.data.code === 200) {
        return response.data
      }
      // 后端错误响应（code不是200时）
      if (response.data.code !== 200 && response.data.message) {
        console.log('后端返回错误:', response.data.code, response.data.message)
        throw new Error(response.data.message)
      }
    }
    return response.data
  },
  error => {
    console.error('响应错误:', error.response?.status, error.response?.data || error.message)

    // 处理401未授权错误
    if (error.response?.status === 401) {
      console.log('🔒 收到401未授权响应，检查是否需要清除token')

      // 只有在确实有token的情况下才清除，避免误清除
      const currentToken = localStorage.getItem('token')
      if (currentToken) {
        console.log('🔒 清除已失效的token')
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        // 不要强制跳转，让Vue Router处理
        console.log('🔒 token已清除，让Vue Router处理跳转')
        return Promise.reject(new Error('登录已过期，请重新登录'))
      } else {
        console.log('🔒 没有token，可能是首次请求，不进行跳转')
        return Promise.reject(new Error('需要登录'))
      }
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

  // 获取题目（兼容性方法）
  getQuestions(chapterId) {
    return this.getQuestionsByChapter(chapterId)
  },

  // 从数据库获取章节题目
  getQuestionsFromDB(chapterId) {
    // 调整为通用可用的后端接口，避免 404
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

  // 获取用户历史（兼容性方法）
  getUserHistory() {
    return this.getUserQuizHistory()
  },

  // 获取用户统计信息
  getUserStats() {
    return api.get('/api/quiz/stats')
  },

  // 获取排行榜
  getLeaderboard() {
    return api.get('/api/quiz/leaderboard')
  },

  // 获取题目统计信息
  getQuestionStats() {
    return api.get('/api/quiz/question-stats')
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

    return api.post('/api/user/register', formData, {
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

    return api.post('/api/user/login', formData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
  },

  // 获取用户信息
  getUserInfo() {
    return api.get('/api/user/userInfo')
  },

  // 获取当前用户（兼容性方法）
  getCurrentUser() {
    return this.getUserInfo()
  },

  // 更新用户基本信息
  updateUserInfo(userInfo) {
    return api.put('/api/user/update', userInfo)
  },

  // 更新用户头像
  updateAvatar(avatarUrl) {
    const params = new URLSearchParams()
    params.append('avatarUrl', avatarUrl)

    return api.patch(`/api/user/updateAvatar?${params.toString()}`)
  },

  // 更新用户密码
  updatePassword(passwordData) {
    return api.patch('/api/user/updatePwd', passwordData)
  }
}

// 等级系统相关API
export const levelApi = {
  // 添加经验值
  addExperience(experienceData) {
    return api.post('/api/level/addExperience', experienceData)
  },

  // 获取用户统计信息
  getUserStats() {
    return api.get('/api/level/stats')
  },

  // 获取用户成就
  getUserAchievements() {
    return api.get('/api/level/achievements')
  },

  // 获取学习记录
  getLearningRecords() {
    return api.get('/api/level/records')
  },

  // 获取排行榜
  getLeaderboard(limit = 10) {
    return api.get(`/api/level/leaderboard?limit=${limit}`)
  },

  // 计算等级
  calculateLevel(experience) {
    return api.get(`/api/level/calculateLevel?experience=${experience}`)
  }
}

// 题目管理相关API
export const questionApi = {
  // 创建题目
  createQuestion(question) {
    return api.post('/api/questions', question)
  },

  // 更新题目
  updateQuestion(id, question) {
    return api.put(`/api/questions/${id}`, question)
  },

  // 删除题目
  deleteQuestion(id) {
    return api.delete(`/api/questions/${id}`)
  },

  // 根据ID获取题目
  getQuestionById(id) {
    return api.get(`/api/questions/${id}`)
  },

  // 获取所有题目（分页）
  getAllQuestions(page = 1, size = 10) {
    return api.get(`/api/questions?page=${page}&size=${size}`)
  },

  // 根据章节ID获取题目
  getQuestionsByChapterId(chapterId) {
    return api.get(`/api/questions/chapter/${chapterId}`)
  },

  // 根据类型获取题目
  getQuestionsByType(type) {
    return api.get(`/api/questions/type/${type}`)
  },

  // 根据难度获取题目
  getQuestionsByDifficulty(difficulty) {
    return api.get(`/api/questions/difficulty/${difficulty}`)
  },

  // 搜索题目
  searchQuestions(keyword) {
    return api.get(`/api/questions/search?keyword=${encodeURIComponent(keyword)}`)
  },

  // 批量导入题目
  importQuestions(questions) {
    return api.post('/api/questions/import', questions)
  },

  // 导出题目
  exportQuestions() {
    return api.get('/api/questions/export')
  },

  // 获取题目统计信息
  getStats() {
    return api.get('/api/questions/stats')
  }
}

export default api
