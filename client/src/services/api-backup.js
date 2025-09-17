import axios from 'axios'

// 备用API配置 - 直接访问后端端口
// 如果花生壳不支持反向代理，可以使用此配置

// 动态获取后端服务地址
const getBaseURL = () => {
  // 如果是生产环境或外网访问，直接访问后端端口
  if (window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1') {
    // 方案1: 使用不同端口
    return `${window.location.protocol}//${window.location.hostname}:8082/api`
    
    // 方案2: 如果花生壳提供了专门的API域名
    // return 'https://api-102qldp675617.vicp.fun/api'
    
    // 方案3: 如果花生壳支持子路径
    // return `${window.location.origin}/api`
  }
  // 本地开发环境使用localhost
  return 'http://localhost:8082/api'
}

// 创建axios实例
const api = axios.create({
  baseURL: getBaseURL(), // 动态指向后端服务地址
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 发送请求

    // 添加JWT token到请求头
    const token = localStorage.getItem('token')
    // 本地存储的token

    if (token) {
      // 确保token格式正确（不重复添加Bearer前缀）
      const formattedToken = token.startsWith('Bearer ') ? token : `Bearer ${token}`
      config.headers.Authorization = formattedToken
      // 已添加Authorization头部
    } else {
      // 警告: 没有找到token，该请求可能会被拒绝
    }

    // 完整请求头部

    return config
  },
  error => {
    // 请求拦截器错误
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    // 收到响应
    // 处理后端统一响应格式
    if (response.data && typeof response.data === 'object') {
      if (response.data.code !== undefined) {
        // 后端返回的统一格式 {code, message, data}
        if (response.data.code === 0 || response.data.code === 200) {
          return response.data.data || response.data
        } else {
          throw new Error(response.data.message || '请求失败')
        }
      }
    }
    return response.data
  },
  error => {
    // 响应错误
    
    if (error.response) {
      // 服务器返回了错误状态码
      const status = error.response.status
      const data = error.response.data
      
      if (status === 401) {
        // token过期或无效，清除本地存储并跳转到登录页
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        window.location.href = '/login'
        return Promise.reject(new Error('登录已过期，请重新登录'))
      } else if (status === 403) {
        return Promise.reject(new Error('权限不足'))
      } else if (data && data.message) {
        return Promise.reject(new Error(data.message))
      }
    } else if (error.request) {
      // 网络错误
      // 网络错误
      return Promise.reject(new Error('网络连接失败，请检查网络设置'))
    }
    
    return Promise.reject(error)
  }
)

export default api

// 导出所有API模块（与原文件相同）
export const chapterApi = {
  // ... 所有章节相关API
}

export const quizApi = {
  // ... 所有测验相关API  
}

export const adminApi = {
  // ... 所有管理员相关API
}

export const userApi = {
  // ... 所有用户相关API
}

export const levelApi = {
  // ... 所有等级相关API
}

export const questionApi = {
  // ... 所有问题相关API
}