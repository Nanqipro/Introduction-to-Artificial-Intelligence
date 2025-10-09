import axios from 'axios'

// 动态获取后端服务地址
const getBaseURL = () => {
  // 生产环境 - 服务器部署
  if (window.location.hostname === '222.204.4.108') {
    return ''  // 使用nginx代理，直接使用相对路径
  }
  // 花生壳外网访问
  if (window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1') {
    return ''
  }
  // 本地开发
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
  '/api/chapters',
  '/api/user/register',
  '/api/user/login',
  '/password-reset',
  '/email-verification'
]

// 检查是否为公开API
const isPublicAPI = (url) => {
  // 检查公开API
  const isPublic = PUBLIC_PATHS.some(path => {
    if (path === '/api/chapters') {
      return url.startsWith('/api/chapters') || url.startsWith('/chapters')
    }
    return url.startsWith(path)
  })
  // 公开API检查结果
  return isPublic
}

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    const isPublic = isPublicAPI(config.url)
    
    // 调试日志
    
    // 为需要认证的API添加token
    if (!isPublic) {
      if (token && token.trim() !== '' && token !== 'null' && token !== 'undefined') {
        const authHeader = token.startsWith('Bearer ') ? token : `Bearer ${token}`
        config.headers.Authorization = authHeader
        // 已添加Authorization头
      } else {
        // 强制拒绝无效token请求
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
      if (url?.includes('/api/user/login')) {
        return Promise.reject(new Error('用户名或密码错误'))
      }
      if (url?.includes('/api/user/register')) {
        return Promise.reject(new Error('注册失败：' + (error.response?.data?.message || '用户名已存在或服务器错误')))
      }
      
      // Token过期处理：清理本地存储并跳转登录页
      // Token已过期，清理认证状态并跳转登录页
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('user')
      
      // 延迟跳转，避免在某些情况下立即跳转导致的问题
      setTimeout(() => {
        if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
          window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname)
        }
      }, 100)
      
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
  getChapterOverview: () => api.get('/api/chapters'),
  getAllChapters: () => api.get('/api/chapters/all'),
  getChapterById: (id) => api.get(`/api/chapters/${id}`),
  createChapter: (chapter) => api.post('/api/chapters', chapter),
  updateChapter: (id, chapter) => api.put(`/api/chapters/${id}`, chapter),
  deleteChapter: (id) => api.delete(`/api/chapters/${id}`),
  healthCheck: () => api.get('/api/chapters/health')
}

// 答题系统相关API
export const quizApi = {
  getQuestionsByChapter: (chapterId) => api.get(`/api/quiz/questions/${chapterId}`),
  saveQuizResult: (result) => api.post('/api/quiz/results', result),
  getUserHistory: () => api.get('/api/quiz/history'),
  getUserStats: () => api.get('/api/quiz/stats'),
  getLeaderboard: () => api.get('/api/quiz/leaderboard'),
  getQuestionStats: () => api.get('/api/quiz/question-stats'),
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
    return api.post('/api/user/register', formData, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
  },
  
  login: (userData) => {
    const formData = new URLSearchParams()
    formData.append('username', userData.username)
    formData.append('password', userData.password)
    return api.post('/api/user/login', formData, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
  },
  
  getUserInfo: () => {
    // 检查token是否存在
    const token = localStorage.getItem('token')
    // userApi.getUserInfo - Token检查
    if (!token || token.trim() === '' || token === 'null') {
      // 没有有效token，拒绝请求
      return Promise.reject(new Error('Token不存在或无效'))
    }
    return api.get('/api/user/userInfo')
  },
  updateUserInfo: (userInfo) => api.put('/api/user/update', userInfo),
  updateAvatar: (avatarUrl) => {
    const params = new URLSearchParams({ avatarUrl })
    return api.patch(`/api/user/updateAvatar?${params}`)
  },
  
  uploadAvatar: (formData) => {
    return api.post('/api/upload/avatar', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  updatePassword: (passwordData) => {
    // 转换参数名以匹配后端API期望的格式
    const requestData = {
      oldPwd: passwordData.currentPassword,
      newPwd: passwordData.newPassword,
      confirmPwd: passwordData.confirmPassword // 修复：使用实际的确认密码而不是新密码
    }
    return api.patch('/api/user/updatePwd', requestData)
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
    return api.post('/api/level/addExperience', experienceData)
  },
  getUserStats: () => {
    // 检查token是否存在
    const token = localStorage.getItem('token')
    if (!token || token.trim() === '' || token === 'null') {
      // 没有有效token，拒绝请求
      return Promise.reject(new Error('Token不存在或无效'))
    }
    return api.get('/api/level/stats')
  },
  getUserAchievements: () => {
    // 检查token是否存在
    const token = localStorage.getItem('token')
    if (!token || token.trim() === '' || token === 'null') {
      // 没有有效token，拒绝请求
      return Promise.reject(new Error('Token不存在或无效'))
    }
    return api.get('/api/level/achievements')
  },
  getLearningRecords: () => {
    return api.get('/api/level/records')
  },
  getLeaderboard: (limit = 10) => api.get(`/api/level/leaderboard?limit=${limit}`),
  calculateLevel: (experience) => api.get(`/api/level/calculateLevel?experience=${experience}`),
  completeChapter: (chapterData) => {
    return api.post('/api/level/completeChapter', chapterData)
  }
}

// 管理员相关API
export const adminApi = {
  // 题目管理
  getAllQuestions: () => api.get('/api/admin/questions'),
  getQuestionById: (id) => api.get(`/api/admin/questions/${id}`),
  createQuestion: (question) => api.post('/api/admin/questions', question),
  updateQuestion: (id, question) => api.put(`/api/admin/questions/${id}`, question),
  deleteQuestion: (id) => api.delete(`/api/admin/questions/${id}`),
  getQuestionsByChapter: (chapterId) => api.get(`/api/admin/questions/chapter/${chapterId}`),
  
  // 文件导入
  importQuestions: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/admin/questions/import', formData)
  },
  
  // 统计信息
  getQuestionStats: () => api.get('/api/admin/questions/stats'),
  healthCheck: () => api.get('/api/admin/health')
}

export default api
