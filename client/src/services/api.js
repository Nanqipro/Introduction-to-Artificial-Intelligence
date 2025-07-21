import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    console.log('发送请求:', config.method?.toUpperCase(), config.url)
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
    if (response.data && response.data.code === 200) {
      return response.data.data
    }
    return response.data
  },
  error => {
    console.error('响应错误:', error.response?.status, error.response?.data || error.message)

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
    return api.get('/chapters')
  },
  
  // 获取所有章节
  getAllChapters() {
    return api.get('/chapters/all')
  },
  
  // 根据ID获取章节详情
  getChapterById(id) {
    return api.get(`/chapters/${id}`)
  },
  
  // 创建章节
  createChapter(chapter) {
    return api.post('/chapters', chapter)
  },
  
  // 更新章节
  updateChapter(id, chapter) {
    return api.put(`/chapters/${id}`, chapter)
  },
  
  // 删除章节
  deleteChapter(id) {
    return api.delete(`/chapters/${id}`)
  },
  
  // 健康检查
  healthCheck() {
    return api.get('/chapters/health')
  }
}

export default api
