import axios from 'axios'

// 动态获取后端服务地址
const getBaseURL = () => {
  if (window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1') {
    // 花生壳外网访问时，使用代理路径
    return '/api'
  }
  return 'http://localhost:8082'
}

// 创建axios实例
const api = axios.create({
  baseURL: getBaseURL(),
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 定义公开API路径（不需要认证）
const PUBLIC_PATHS = [
  '/chapters',
  '/user/register',
  '/user/login',
  '/password-reset',
  '/email-verification'
]

// 检查是否为公开API
const isPublicAPI = (url) => {
  return PUBLIC_PATHS.some(path => {
    if (path === '/api/chapters') {
      return url.startsWith('/api/chapters')
    }
    return url.startsWith(path)
  })
}

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    const isPublic = isPublicAPI(config.url)
    
    // 调试日志
    console.log('🔍 API拦截器 - 请求详情:', {
      url: config.url,
      isPublic: isPublic,
      hasToken: !!token,
      tokenValue: token ? token.substring(0, 20) + '...' : 'null',
      tokenLength: token ? token.length : 0
    })
    
    // 为需要认证的API添加token
    if (!isPublic) {
      if (token && token.trim() !== '' && token !== 'null' && token !== 'undefined') {
        const authHeader = token.startsWith('Bearer ') ? token : `Bearer ${token}`
        config.headers.Authorization = authHeader
        console.log('✅ API拦截器 - 已添加Authorization头:', authHeader.substring(0, 30) + '...')
      } else {
        console.error('❌ API拦截器 - 强制拒绝无效token请求:', {
          url: config.url,
          token: token,
          tokenType: typeof token,
          headers: config.headers
        })
        // 强制抛出错误，阻止请求发送
        throw new Error('Token无效，请先登录')
      }
    }
    
    // 处理FormData
    if (config.data instanceof FormData) {
      delete config.headers['Content-Type']
    }
    
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    // 处理统一响应格式
    if (response.data?.code === 200) {
      return response.data
    }
    if (response.data?.code && response.data?.message) {
      throw new Error(response.data.message)
    }
    return response.data
  },
  error => {
    // 处理401未授权
    if (error.response?.status === 401) {
      const url = error.config?.url
      if (url?.includes('/user/login')) {
        return Promise.reject(new Error('用户名或密码错误'))
      }
      return Promise.reject(new Error('登录已过期，请重新登录'))
    }
    
    // 处理其他错误
    const message = error.response?.data?.message || 
                   (error.response ? '服务器错误' : '网络连接失败')
    return Promise.reject(new Error(message))
  }
)

// 章节相关API
export const chapterApi = {
  getChapterOverview: () => api.get('/chapters'),
  getAllChapters: () => api.get('/chapters/all'),
  getChapterById: (id) => api.get(`/chapters/${id}`),
  createChapter: (chapter) => api.post('/chapters', chapter),
  updateChapter: (id, chapter) => api.put(`/chapters/${id}`, chapter),
  deleteChapter: (id) => api.delete(`/chapters/${id}`),
  healthCheck: () => api.get('/chapters/health')
}

// 答题系统相关API
export const quizApi = {
  getQuestionsByChapter: (chapterId) => api.get(`/quiz/questions/${chapterId}`),
  saveQuizResult: (result) => api.post('/quiz/results', result),
  getUserHistory: () => api.get('/quiz/history'),
  getUserStats: () => api.get('/quiz/stats'),
  getLeaderboard: () => api.get('/quiz/leaderboard'),
  getQuestionStats: () => api.get('/quiz/question-stats'),
  // 第一章案例演示相关接口
  getChapter1GlobalStats: () => api.get('/api/chapter1-case-study/global-stats'),
  submitChapter1Answer: (data) => api.post('/api/chapter1-case-study/submit-answer', data),
  getUserChapter1Answers: () => api.get('/api/chapter1-case-study/user-answers')
}

// 用户相关API
export const userApi = {
  register: (userData) => {
    const formData = new URLSearchParams()
    formData.append('username', userData.username)
    formData.append('password', userData.password)
    return api.post('/user/register', formData, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
  },
  
  login: (userData) => {
    const formData = new URLSearchParams()
    formData.append('username', userData.username)
    formData.append('password', userData.password)
    return api.post('/user/login', formData, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
  },
  
  getUserInfo: () => {
    // 检查token是否存在
    const token = localStorage.getItem('token')
    console.log('🔍 userApi.getUserInfo - Token检查:', {
      hasToken: !!token,
      tokenValue: token ? token.substring(0, 20) + '...' : 'null',
      tokenLength: token ? token.length : 0
    })
    if (!token || token.trim() === '' || token === 'null') {
      console.log('🚫 userApi.getUserInfo - 没有有效token，拒绝请求')
      return Promise.reject(new Error('Token不存在或无效'))
    }
    return api.get('/user/userInfo')
  },
  updateUserInfo: (userInfo) => api.put('/user/update', userInfo),
  updateAvatar: (avatarUrl) => {
    const params = new URLSearchParams({ avatarUrl })
    return api.patch(`/user/updateAvatar?${params}`)
  },
  
  uploadAvatar: (formData) => {
    return api.post('/upload/avatar', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  updatePassword: (passwordData) => {
    // 转换参数名以匹配后端API期望的格式
    const requestData = {
      oldPwd: passwordData.currentPassword,
      newPwd: passwordData.newPassword,
      confirmPwd: passwordData.newPassword // 前端已经验证过确认密码，这里直接使用新密码
    }
    return api.patch('/user/updatePwd', requestData)
  },
  
  // 密码重置相关API
  sendPasswordResetEmail: (email) => {
    return api.post('/password-reset/send-email', { email })
  },
  
  validatePasswordResetToken: (token) => {
    return api.post('/password-reset/validate-token', { token })
  },
  
  resetPassword: (resetData) => {
    return api.post('/password-reset/reset', resetData)
  },
  
  // 邮箱验证相关API
  sendEmailVerificationCode: (email) => {
    return api.post('/email-verification/send-code', { email })
  },
  
  verifyEmailCode: (verificationData) => {
    return api.post('/email-verification/verify', verificationData)
  }
}

// 等级系统相关API
export const levelApi = {
  addExperience: (experienceData) => {
    return api.post('/level/addExperience', experienceData)
  },
  getUserStats: () => {
    // 检查token是否存在
    const token = localStorage.getItem('token')
    if (!token || token.trim() === '' || token === 'null') {
      console.log('🚫 levelApi.getUserStats - 没有有效token，拒绝请求')
      return Promise.reject(new Error('Token不存在或无效'))
    }
    return api.get('/level/stats')
  },
  getUserAchievements: () => {
    // 检查token是否存在
    const token = localStorage.getItem('token')
    if (!token || token.trim() === '' || token === 'null') {
      console.log('🚫 levelApi.getUserAchievements - 没有有效token，拒绝请求')
      return Promise.reject(new Error('Token不存在或无效'))
    }
    return api.get('/level/achievements')
  },
  getLearningRecords: () => {
    return api.get('/level/records')
  },
  getLeaderboard: (limit = 10) => api.get(`/level/leaderboard?limit=${limit}`),
  calculateLevel: (experience) => api.get(`/level/calculateLevel?experience=${experience}`),
  completeChapter: (chapterData) => {
    return api.post('/level/completeChapter', chapterData)
  }
}

// 管理员相关API
export const adminApi = {
  // 题目管理
  getAllQuestions: () => api.get('/admin/questions'),
  getQuestionById: (id) => api.get(`/admin/questions/${id}`),
  createQuestion: (question) => api.post('/admin/questions', question),
  updateQuestion: (id, question) => api.put(`/admin/questions/${id}`, question),
  deleteQuestion: (id) => api.delete(`/admin/questions/${id}`),
  getQuestionsByChapter: (chapterId) => api.get(`/admin/questions/chapter/${chapterId}`),
  
  // 文件导入
  importQuestions: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/admin/questions/import', formData)
  },
  
  // 统计信息
  getQuestionStats: () => api.get('/admin/questions/stats'),
  healthCheck: () => api.get('/admin/health')
}

export default api
