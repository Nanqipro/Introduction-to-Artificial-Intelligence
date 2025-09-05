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
    console.log('🔄 useAuth - 返回现有实例')
    return globalAuthState
  }

  console.log('🆕 useAuth - 创建新的全局实例')

  // 创建新的实例 - 确保从localStorage正确读取
  const token = ref('')
  const userInfo = ref(null)

  // 立即同步localStorage状态
  if (typeof localStorage !== 'undefined') {
    const storedToken = localStorage.getItem('token')
    const storedUserInfo = localStorage.getItem('userInfo')

    if (storedToken) {
      token.value = storedToken
      console.log('🔄 useAuth - 从localStorage恢复token:', storedToken.substring(0, 20) + '...')
    }

    if (storedUserInfo) {
      try {
        userInfo.value = JSON.parse(storedUserInfo)
        console.log('🔄 useAuth - 从localStorage恢复用户信息:', userInfo.value?.username || 'unknown')
      } catch (e) {
        console.error('❌ useAuth - 解析用户信息失败:', e)
        userInfo.value = null
      }
    }
  }

  console.log('🔍 useAuth - 初始化状态:', {
    token: token.value ? token.value.substring(0, 20) + '...' : 'null',
    userInfo: userInfo.value ? 'exists' : 'null',
    isLoggedIn: !!token.value
  })

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const currentUser = computed(() => userInfo.value)

  // 登录
  const login = async (loginData) => {
    try {
      const response = await userApi.login(loginData)
      console.log('登录响应:', response)

      if (response && response.code === 200) {
        const newToken = response.data
        console.log('登录成功，收到token:', newToken.substring(0, 30) + '...')

        // 同步更新响应式状态和localStorage
        token.value = newToken
        if (typeof localStorage !== 'undefined') {
          localStorage.setItem('token', newToken)
        }

        console.log('✅ Token已存储到localStorage和响应式状态')
        console.log('🔍 当前token状态验证:', {
          tokenValue: token.value ? 'exists' : 'null',
          localStorageToken: localStorage.getItem('token') ? 'exists' : 'null'
        })

        // 登录成功，不在这里获取用户信息，让页面自己处理
        console.log('✅ 登录成功，token已设置，等待页面处理用户信息获取')

        ElMessage.success('登录成功')
        return { success: true }
      } else {
        const errorMsg = response?.message || '登录失败'
        ElMessage.error(errorMsg)
        return { success: false, message: errorMsg }
      }
    } catch (error) {
      console.error('登录错误:', error)
      const errorMsg = error.message || '登录失败'
      ElMessage.error(errorMsg)
      return { success: false, message: errorMsg }
    }
  }

  // 注册
  const register = async (registerData) => {
    try {
      const response = await userApi.register(registerData)
      console.log('注册响应:', response)

      if (response && response.code === 200) {
        ElMessage.success('注册成功，请登录')
        return { success: true }
      } else {
        const errorMsg = response?.message || '注册失败'
        ElMessage.error(errorMsg)
        return { success: false, message: errorMsg }
      }
    } catch (error) {
      console.error('注册错误:', error)
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
        console.log('🚫 fetchUserInfo - localStorage中无token，直接返回')
        return { success: false, message: 'Token不存在' }
      }

      // 检查是否已有请求在进行中
      if (isRequestingUserInfo && !force) {
        console.log('⏸️ fetchUserInfo - 已有请求进行中，跳过')
        return { success: false, message: '请求正在进行中' }
      }

      // 防抖机制：如果距离上次调用时间少于1秒，则跳过（除非强制）
      const now = Date.now()
      if (!force && now - lastFetchUserInfoTime < 1000) {
        console.log('⏸️ fetchUserInfo - 防抖跳过，距离上次调用:', now - lastFetchUserInfoTime, 'ms')
        return { success: false, message: '请求过于频繁，已跳过' }
      }
      lastFetchUserInfoTime = now

      // 设置请求状态
      isRequestingUserInfo = true
      console.log('🔍 fetchUserInfo - 开始获取用户信息', force ? '(强制)' : '')
      console.log('📍 fetchUserInfo - 调用栈:', new Error().stack)

      // 检查token状态 - 更严格的检查
      const localStorageToken = localStorage.getItem('token')
      const reactiveToken = token.value
      
      console.log('🔍 fetchUserInfo - token检查:', {
        localStorage: localStorageToken ? 'exists' : 'null',
        reactive: reactiveToken ? 'exists' : 'null'
      })
      
      // 如果localStorage中没有token，直接返回
      if (!localStorageToken) {
        console.error('❌ fetchUserInfo - localStorage中没有token')
        isRequestingUserInfo = false
        return { success: false, message: '没有找到认证token' }
      }
      
      // 如果响应式token为空，先同步再检查
      if (!reactiveToken) {
        console.log('🔄 fetchUserInfo - 响应式token为空，先同步')
        token.value = localStorageToken
        // 再次检查同步后的token
        if (!token.value) {
          console.error('❌ fetchUserInfo - token同步失败')
          isRequestingUserInfo = false
          return { success: false, message: 'token同步失败' }
        }
      }
      
      const currentToken = token.value

      // 最终检查：确保token有效
      if (!currentToken) {
        console.error('❌ fetchUserInfo - 最终token检查失败')
        isRequestingUserInfo = false
        return { success: false, message: '认证token无效' }
      }

      console.log('🔍 fetchUserInfo - 使用当前token获取用户信息')

      console.log('🚀 fetchUserInfo - 开始发送请求...')
      // 在调用API前再次确认token存在
      const finalToken = localStorage.getItem('token')
      if (!finalToken || finalToken.trim() === '' || finalToken === 'null') {
        console.error('❌ fetchUserInfo - 发送请求前token检查失败')
        isRequestingUserInfo = false
        return { success: false, message: '发送请求前token验证失败' }
      }
      
      const response = await userApi.getUserInfo()
      console.log('📥 fetchUserInfo - 收到响应:', response)

      if (response && response.code === 200) {
        userInfo.value = response.data
        if (typeof localStorage !== 'undefined') {
          localStorage.setItem('userInfo', JSON.stringify(response.data))
        }
        console.log('✅ fetchUserInfo - 用户信息设置成功:', response.data)
        return { success: true, data: response.data }
      } else {
        const errorMsg = response?.message || '获取用户信息失败'
        console.error('❌ fetchUserInfo - 响应错误:', errorMsg)
        return { success: false, message: errorMsg }
      }
    } catch (error) {
      console.error('❌ fetchUserInfo - 请求异常:', error)

      // 如果是token过期错误，清理认证状态
      if (error.message && error.message.includes('登录已过期')) {
        console.log('🔒 Token已过期，清理认证状态')
        logout()
      }

      const errorMsg = error.message || '获取用户信息失败'
      return { success: false, message: errorMsg }
    } finally {
      // 无论成功还是失败，都重置请求状态
      isRequestingUserInfo = false
      console.log('🔄 fetchUserInfo - 请求状态已重置')
    }
  }

  // 更新用户信息
  const updateUserInfo = async (updateData) => {
    try {
      const response = await userApi.updateUserInfo(updateData)
      console.log('更新用户信息响应:', response)

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
      console.error('更新用户信息错误:', error)
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
      console.error('更新头像错误:', error)
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
      console.error('更新密码错误:', error)
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
      console.log('🚫 localStorage不可用，无法检查认证状态')
      return { success: false, message: 'localStorage不可用' }
    }

    // 从localStorage同步状态
    const localStorageToken = localStorage.getItem('token')
    const localStorageUserInfo = localStorage.getItem('userInfo')

    console.log('🔍 检查认证状态 - localStorage token:', localStorageToken ? 'exists' : 'null')
    console.log('🔍 检查认证状态 - localStorage userInfo:', localStorageUserInfo ? 'exists' : 'null')
    console.log('🔍 检查认证状态 - 当前响应式token:', token.value ? 'exists' : 'null')

    // 强制同步token状态
    if (localStorageToken && localStorageToken !== token.value) {
      console.log('🔄 强制同步token状态')
      token.value = localStorageToken
    } else if (!localStorageToken) {
      token.value = ''
    }

    // 同步用户信息状态
    if (localStorageUserInfo) {
      try {
        userInfo.value = JSON.parse(localStorageUserInfo)
        console.log('✅ 用户信息已从localStorage恢复')
      } catch (e) {
        console.error('❌ 解析用户信息失败:', e)
        userInfo.value = null
        localStorage.removeItem('userInfo')
      }
    } else {
      userInfo.value = null
    }

    // 检查认证状态
    if (!token.value) {
      console.log('🚫 没有token，用户未登录')
      userInfo.value = null
      return { success: false, message: '用户未登录' }
    }

    if (!userInfo.value) {
      console.log('📥 有token但无用户信息，尝试获取用户信息')
      // 再次确认token确实存在且有效
      const currentToken = token.value || localStorage.getItem('token')
      if (!currentToken || currentToken.trim() === '' || currentToken === 'null') {
        console.log('❌ token为空或无效，跳过获取用户信息')
        logout()
        return { success: false, message: 'Token无效' }
      }
      
      // 只有在确实有有效token的情况下才调用fetchUserInfo
      if (currentToken && currentToken.trim() !== '' && currentToken !== 'null') {
        const result = await fetchUserInfo(true)
        if (!result.success) {
          console.log('❌ 获取用户信息失败:', result.message)
          // 只有在明确的认证错误时才清除token，网络错误不清除
          if (result.message && (result.message.includes('登录已过期') || result.message.includes('token') || result.message.includes('认证'))) {
            console.log('🔒 认证失败，清除token')
            logout()
            return { success: false, message: '认证状态已失效' }
          } else {
            console.log('🌐 网络错误，保留token状态')
            return { success: false, message: '网络连接失败，但认证状态保留' }
          }
        }
      }
    }

    console.log('✅ 认证状态正常')
    return { success: true, message: '认证状态正常' }
  }

  // 强制刷新认证状态
  const forceRefreshAuth = async () => {
    console.log('🔄 强制刷新认证状态')

    // 检查localStorage是否可用
    if (typeof localStorage === 'undefined') {
      console.error('❌ localStorage不可用')
      return { success: false, message: 'localStorage不可用' }
    }

    // 重新从localStorage读取token
    const localStorageToken = localStorage.getItem('token')
    console.log('🔍 强制刷新 - localStorage token:', localStorageToken ? localStorageToken.substring(0, 20) + '...' : 'null')

    // 更新响应式状态
    token.value = localStorageToken || ''

    // 重新从localStorage读取userInfo
    const localStorageUserInfo = localStorage.getItem('userInfo')
    console.log('🔍 强制刷新 - localStorage userInfo:', localStorageUserInfo ? 'exists' : 'null')

    if (localStorageUserInfo) {
      try {
        userInfo.value = JSON.parse(localStorageUserInfo)
        console.log('✅ 强制刷新 - 用户信息已恢复:', userInfo.value)
      } catch (e) {
        console.error('❌ 强制刷新 - 解析用户信息失败:', e)
        userInfo.value = null
      }
    } else {
      userInfo.value = null
    }

    // 强制触发响应式更新
    console.log('🔄 强制触发响应式更新...')
    await new Promise(resolve => setTimeout(resolve, 100))

    console.log('🔍 强制刷新后的状态:', {
      token: token.value ? 'exists' : 'null',
      userInfo: userInfo.value ? 'exists' : 'null',
      isLoggedIn: isLoggedIn.value,
      currentUser: currentUser.value ? 'exists' : 'null'
    })

    // 如果有token但没有用户信息，尝试获取（添加额外检查避免循环调用）
    const localToken = localStorage.getItem('token')
    if (localToken && localToken.trim() !== '' && localToken !== 'null' && token.value && !userInfo.value && !isRequestingUserInfo) {
      console.log('📥 强制刷新 - 有token无用户信息，尝试获取')
      const result = await fetchUserInfo()
      if (result.success) {
        console.log('✅ 强制刷新 - 用户信息获取成功')
      } else {
        console.log('❌ 强制刷新 - 用户信息获取失败')
      }
    } else if (!localToken) {
      console.log('⚠️ 强制刷新 - localStorage中无token，跳过获取')
    }

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
