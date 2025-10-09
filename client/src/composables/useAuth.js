import { ref, computed } from 'vue'
import { userApi } from '@/services/api'
import { ElMessage } from 'element-plus'

// 防抖机制，避免短时间内重复调用fetchUserInfo
let fetchUserInfoTimer = null
let lastFetchUserInfoTime = 0
let isRequestingUserInfo = false // 添加请求状态标志

// 全局单例状态
let globalAuthState = null

export function useAuth() {
  // 如果已经有全局状态，直接返回
  if (globalAuthState) {
    return globalAuthState
  }

  // 创建新的实例 - 确保从localStorage正确读取
  const token = ref('')
  const userInfo = ref(null)

  // 立即同步localStorage状态
  if (typeof localStorage !== 'undefined') {
    const storedToken = localStorage.getItem('token')
    const storedUserInfo = localStorage.getItem('userInfo')

    if (storedToken) {
      token.value = storedToken
    }

    if (storedUserInfo) {
      try {
        userInfo.value = JSON.parse(storedUserInfo)
        // 从localStorage恢复用户信息
      } catch (e) {
        // 解析用户信息失败
        userInfo.value = null
      }
    }
  }

  // 初始化状态完成

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const currentUser = computed(() => userInfo.value)

  // 登录
  const login = async (loginData) => {
    try {
      const response = await userApi.login(loginData)
      // 登录响应处理

      if (response && response.code === 200) {
        const newToken = response.data
        // 登录成功，收到token

        // 同步更新响应式状态和localStorage
        token.value = newToken
        if (typeof localStorage !== 'undefined') {
          localStorage.setItem('token', newToken)
        }

        // Token已存储到localStorage和响应式状态

        // 登录成功，不在这里获取用户信息，让页面自己处理
        // 登录成功，token已设置

        ElMessage.success('登录成功')
        return { success: true }
      } else {
        const errorMsg = response?.message || '登录失败'
        ElMessage.error(errorMsg)
        return { success: false, message: errorMsg }
      }
    } catch (error) {
      // 登录错误
      const errorMsg = error.message || '登录失败'
      ElMessage.error(errorMsg)
      return { success: false, message: errorMsg }
    }
  }

  // 注册
  const register = async (registerData) => {
    try {
      const response = await userApi.register(registerData)
      // 注册响应处理

      if (response && response.code === 200) {
        ElMessage.success('注册成功，请登录')
        return { success: true }
      } else {
        const errorMsg = response?.message || '注册失败'
        ElMessage.error(errorMsg)
        return { success: false, message: errorMsg }
      }
    } catch (error) {
      // 注册错误
      const errorMsg = error.message || '注册失败'
      ElMessage.error(errorMsg)
      return { success: false, message: errorMsg }
    }
  }

  // 获取用户信息
  const fetchUserInfo = async (force = false) => {
    try {
      // 最优先检查：localStorage中必须有token
      const localToken = localStorage.getItem('token')
      if (!localToken) {
        // localStorage中无token，直接返回
        return { success: false, message: 'Token不存在' }
      }

      // 检查是否已有请求在进行中
      if (isRequestingUserInfo && !force) {
        return { success: false, message: '请求正在进行中' }
      }

      // 防抖机制：如果距离上次调用时间少于1秒，则跳过（除非强制）
      const now = Date.now()
      if (!force && now - lastFetchUserInfoTime < 1000) {
        return { success: false, message: '请求过于频繁，已跳过' }
      }
      lastFetchUserInfoTime = now

      // 设置请求状态
      isRequestingUserInfo = true
      // 开始获取用户信息

      // 检查token状态 - 更严格的检查
      const localStorageToken = localStorage.getItem('token')
      const reactiveToken = token.value
      
      // token检查
      
      // 如果localStorage中没有token，直接返回
      if (!localStorageToken) {
        // localStorage中没有token
        isRequestingUserInfo = false
        return { success: false, message: '没有找到认证token' }
      }
      
      // 如果响应式token为空，先同步再检查
      if (!reactiveToken) {
        // 响应式token为空，先同步
        token.value = localStorageToken
        // 再次检查同步后的token
        if (!token.value) {
          // token同步失败
          isRequestingUserInfo = false
          return { success: false, message: 'token同步失败' }
        }
      }
      
      const currentToken = token.value

      // 最终检查：确保token有效
      if (!currentToken) {
        // 最终token检查失败
        isRequestingUserInfo = false
        return { success: false, message: '认证token无效' }
      }

      // 使用当前token获取用户信息

      // 开始发送请求
      // 在调用API前再次确认token存在
      const finalToken = localStorage.getItem('token')
      if (!finalToken || finalToken.trim() === '' || finalToken === 'null') {
        // 发送请求前token检查失败
        isRequestingUserInfo = false
        return { success: false, message: '发送请求前token验证失败' }
      }
      
      const response = await userApi.getUserInfo()
      // 收到响应

      if (response && response.code === 200) {
        userInfo.value = response.data
        if (typeof localStorage !== 'undefined') {
          localStorage.setItem('userInfo', JSON.stringify(response.data))
        }
        // 用户信息设置成功
        return { success: true, data: response.data }
      } else {
        const errorMsg = response?.message || '获取用户信息失败'
        // 响应错误
        return { success: false, message: errorMsg }
      }
    } catch (error) {
      // 请求异常

      // 如果是token过期错误，清理认证状态
      if (error.message && error.message.includes('登录已过期')) {
        // Token已过期，清理认证状态
        logout()
      }

      const errorMsg = error.message || '获取用户信息失败'
      return { success: false, message: errorMsg }
    } finally {
      // 无论成功还是失败，都重置请求状态
      isRequestingUserInfo = false
      // 请求状态已重置
    }
  }

  // 更新用户信息
  const updateUserInfo = async (updateData) => {
    try {
      const response = await userApi.updateUserInfo(updateData)
      // 更新用户信息响应

      if (response.code === 200) {
        // 更新成功后重新获取用户信息
        await fetchUserInfo()
        ElMessage.success('更新成功')
        return { success: true }
      } else {
        const errorMsg = response.message || '更新失败'
        ElMessage.error(errorMsg)
        return { success: false, message: errorMsg }
      }
    } catch (error) {
      // 更新用户信息错误
      const errorMsg = error.response?.data?.message || error.message || '更新失败'
      ElMessage.error(errorMsg)
      return { success: false, message: errorMsg }
    }
  }

  // 更新头像
  const updateAvatar = async (avatarUrl) => {
    try {
      const response = await userApi.updateAvatar(avatarUrl)

      if (response.code === 200) {
        // 更新成功后重新获取用户信息
        await fetchUserInfo()
        ElMessage.success('头像更新成功')
        return { success: true }
      } else {
        ElMessage.error(response.message || '头像更新失败')
        return { success: false, message: response.message }
      }
    } catch (error) {
      // 更新头像错误
      ElMessage.error(error.message || '头像更新失败')
      return { success: false, message: error.message }
    }
  }

  // 更新密码
  const updatePassword = async (passwordData) => {
    try {
      const response = await userApi.updatePassword(passwordData)

      if (response.code === 200) {
        ElMessage.success('密码更新成功')
        return { success: true }
      } else {
        ElMessage.error(response.message || '密码更新失败')
        return { success: false, message: response.message }
      }
    } catch (error) {
      // 更新密码错误
      ElMessage.error(error.message || '密码更新失败')
      return { success: false, message: error.message }
    }
  }

  // 登出
  const logout = () => {
    token.value = ''
    userInfo.value = null
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    }
    ElMessage.success('已退出登录')
  }

  // 初始化时检查登录状态
  const checkAuthStatus = async () => {
    // 检查localStorage是否可用
    if (typeof localStorage === 'undefined') {
      return { success: false, message: 'localStorage不可用' }
    }

    // 从localStorage同步状态
    const localStorageToken = localStorage.getItem('token')
    const localStorageUserInfo = localStorage.getItem('userInfo')

    // 检查认证状态

    // 强制同步token状态
    if (localStorageToken && localStorageToken !== token.value) {
      // 强制同步token状态
      token.value = localStorageToken
    } else if (!localStorageToken) {
      token.value = ''
    }

    // 同步用户信息状态
    if (localStorageUserInfo) {
      try {
        userInfo.value = JSON.parse(localStorageUserInfo)
        // 用户信息已从localStorage恢复
      } catch (e) {
        // 解析用户信息失败
        userInfo.value = null
        localStorage.removeItem('userInfo')
      }
    } else {
      userInfo.value = null
    }

    // 检查认证状态
    if (!token.value) {
      // 没有token，用户未登录
      userInfo.value = null
      return { success: false, message: '用户未登录' }
    }

    if (!userInfo.value) {
      // 有token但无用户信息，尝试获取用户信息
      // 再次确认token确实存在且有效
      const currentToken = token.value || localStorage.getItem('token')
      if (!currentToken || currentToken.trim() === '' || currentToken === 'null') {
        // token为空或无效，跳过获取用户信息
        logout()
        return { success: false, message: 'Token无效' }
      }
      
      // 只有在确实有有效token的情况下才调用fetchUserInfo
      if (currentToken && currentToken.trim() !== '' && currentToken !== 'null') {
        const result = await fetchUserInfo(true)
        if (!result.success) {
          // 获取用户信息失败
          // 只有在明确的认证错误时才清除token，网络错误不清除
          if (result.message && (result.message.includes('登录已过期') || result.message.includes('token') || result.message.includes('认证'))) {
            // 认证失败，清除token
            logout()
            return { success: false, message: '认证状态已失效' }
          } else {
            // 网络错误，保留token状态
            return { success: false, message: '网络连接失败，但认证状态保留' }
          }
        }
      }
    }

    // 认证状态正常
    return { success: true, message: '认证状态正常' }
  }

  // 强制刷新认证状态
  const forceRefreshAuth = async () => {
    // 强制刷新认证状态

    // 检查localStorage是否可用
    if (typeof localStorage === 'undefined') {
      // localStorage不可用
      return { success: false, message: 'localStorage不可用' }
    }

    // 重新从localStorage读取token
    const localStorageToken = localStorage.getItem('token')
    // 强制刷新 - localStorage token检查

    // 更新响应式状态
    token.value = localStorageToken || ''

    // 重新从localStorage读取userInfo
    const localStorageUserInfo = localStorage.getItem('userInfo')
    // 强制刷新 - localStorage userInfo检查

    if (localStorageUserInfo) {
      try {
        userInfo.value = JSON.parse(localStorageUserInfo)
        // 强制刷新 - 用户信息已恢复
      } catch (e) {
        // 强制刷新 - 解析用户信息失败
        userInfo.value = null
      }
    } else {
      userInfo.value = null
    }

    // 强制触发响应式更新
    // 强制触发响应式更新
    await new Promise(resolve => setTimeout(resolve, 100))

    // 强制刷新后的状态检查完成

    // 如果有token但没有用户信息，尝试获取（添加额外检查避免循环调用）
    const localToken = localStorage.getItem('token')
    if (localToken && localToken.trim() !== '' && localToken !== 'null' && token.value && !userInfo.value && !isRequestingUserInfo) {
      // 强制刷新 - 有token无用户信息，尝试获取
      const result = await fetchUserInfo()
      if (result.success) {
        // 强制刷新 - 用户信息获取成功
      } else {
        // 强制刷新 - 用户信息获取失败
      }
    } else if (!localToken) {
       // 强制刷新 - localStorage中无token，跳过获取
     }

     // 强制刷新完成
     return { success: true }
  }

  // 创建实例对象
  const instance = {
    // 状态
    token,
    userInfo,
    isLoggedIn,
    currentUser,

    // 方法
    login,
    register,
    logout,
    fetchUserInfo,
    updateUserInfo,
    updateAvatar,
    updatePassword,
    checkAuthStatus,
    forceRefreshAuth
  }

  // 保存到全局状态
  globalAuthState = instance

  return instance
}
