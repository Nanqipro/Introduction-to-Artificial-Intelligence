import { ref, computed } from 'vue'
import { userApi } from '@/services/api'
import { ElMessage } from 'element-plus'

// 全局状态 - 使用单例模式
let authInstance = null

// 防抖机制，避免短时间内重复调用fetchUserInfo
let fetchUserInfoTimer = null
let lastFetchUserInfoTime = 0

export function useAuth() {
  // 如果已经存在实例，直接返回
  if (authInstance) {
    return authInstance
  }

  // 创建新的实例
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))
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

        // 更新全局状态和本地存储
        token.value = newToken
        localStorage.setItem('token', newToken)

        console.log('Token已存储到localStorage和全局状态')
        console.log('当前token状态:', token.value ? 'exists' : 'null')

        // 立即验证token是否正确存储
        const storedToken = localStorage.getItem('token')
        console.log('立即验证localStorage中的token:', storedToken ? storedToken.substring(0, 20) + '...' : 'null')

        if (storedToken !== newToken) {
          console.error('❌ Token存储验证失败！')
          return { success: false, message: 'Token存储失败' }
        }

        // 等待一下确保token设置完成
        console.log('⏳ 等待token设置完成...')
        await new Promise(resolve => setTimeout(resolve, 200))

        // 再次检查token状态
        const finalToken = localStorage.getItem('token')
        console.log('🔍 延迟后检查token状态:', finalToken ? finalToken.substring(0, 20) + '...' : 'null')

        if (!finalToken) {
          console.error('❌ 延迟后仍然没有token，登录状态异常')
          return { success: false, message: '登录状态异常' }
        }

        // 登录成功后获取用户信息
        console.log('🔍 开始获取用户信息...')
        const userInfoResult = await fetchUserInfo()
        console.log('🔍 获取用户信息结果:', userInfoResult)

        if (userInfoResult.success) {
          console.log('✅ 用户信息获取成功，当前状态:', {
            hasToken: !!token.value,
            hasUserInfo: !!userInfo.value,
            isLoggedIn: isLoggedIn.value
          })
        } else {
          console.error('❌ 用户信息获取失败:', userInfoResult.message)
        }

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
  const fetchUserInfo = async () => {
    try {
      // 防抖机制：如果距离上次调用时间少于2秒，则跳过
      const now = Date.now()
      if (now - lastFetchUserInfoTime < 2000) {
        console.log('⏸️ fetchUserInfo - 防抖跳过，距离上次调用:', now - lastFetchUserInfoTime, 'ms')
        return { success: false, message: '请求过于频繁，已跳过' }
      }
      lastFetchUserInfoTime = now

      // 添加调用栈信息，帮助调试
      console.log('🔍 fetchUserInfo - 被调用，调用栈:', new Error().stack)

      // 在发送请求前检查token状态
      const currentToken = localStorage.getItem('token')
      console.log('🔍 fetchUserInfo - 当前localStorage token:', currentToken ? currentToken.substring(0, 20) + '...' : 'null')
      console.log('🔍 fetchUserInfo - 当前响应式token:', token.value ? token.value.substring(0, 20) + '...' : 'null')

      if (!currentToken) {
        console.error('❌ fetchUserInfo - 没有找到token，无法获取用户信息')
        return { success: false, message: '没有找到认证token' }
      }

      // 确保响应式状态与localStorage同步
      if (currentToken !== token.value) {
        console.log('🔄 fetchUserInfo - 同步token状态')
        token.value = currentToken
      }

      console.log('🚀 fetchUserInfo - 开始发送请求...')
      const response = await userApi.getUserInfo()
      console.log('📥 fetchUserInfo - 收到响应:', response)

      if (response && response.code === 200) {
        userInfo.value = response.data
        localStorage.setItem('userInfo', JSON.stringify(response.data))
        console.log('✅ fetchUserInfo - 用户信息设置成功:', response.data)
        return { success: true, data: response.data }
      } else {
        const errorMsg = response?.message || '获取用户信息失败'
        console.error('❌ fetchUserInfo - 响应错误:', errorMsg)
        return { success: false, message: errorMsg }
      }
    } catch (error) {
      console.error('❌ fetchUserInfo - 请求异常:', error)

      // 如果是401错误（token无效），清理认证状态
      if (error.message && error.message.includes('登录已过期')) {
        console.log('Token已过期，清理认证状态')
        logout()
      }

      const errorMsg = error.message || '获取用户信息失败'
      return { success: false, message: errorMsg }
    }
  }

  // 更新用户信息
  const updateUserInfo = async (updateData) => {
    try {
      const response = await userApi.updateUserInfo(updateData)

      if (response.code === 200) {
        // 更新成功后重新获取用户信息
        await fetchUserInfo()
        ElMessage.success('更新成功')
        return { success: true }
      } else {
        ElMessage.error(response.message || '更新失败')
        return { success: false, message: response.message }
      }
    } catch (error) {
      console.error('更新用户信息错误:', error)
      ElMessage.error(error.message || '更新失败')
      return { success: false, message: error.message }
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
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    ElMessage.success('已退出登录')
  }

  // 初始化时检查登录状态
  const checkAuthStatus = async () => {
    // 直接从localStorage获取token，确保与路由守卫使用相同的源
    const localStorageToken = localStorage.getItem('token')

    console.log('🔍 检查认证状态 - localStorage token:', localStorageToken ? localStorageToken.substring(0, 20) + '...' : 'null')
    console.log('🔍 检查认证状态 - 响应式token:', token.value ? token.value.substring(0, 20) + '...' : 'null')
    console.log('🔍 检查认证状态 - 当前userInfo:', userInfo.value ? 'exists' : 'null')
    console.log('🔍 检查认证状态 - isLoggedIn计算值:', isLoggedIn.value)

    // 如果localStorage中有token但响应式状态中没有，同步一下
    if (localStorageToken && !token.value) {
      console.log('🔄 同步localStorage中的token到响应式状态')
      token.value = localStorageToken
    }

    if (token.value && !userInfo.value) {
      console.log('📥 有token但无用户信息，重新获取用户信息')
      const result = await fetchUserInfo()
      if (!result.success) {
        console.log('❌ 获取用户信息失败，可能token已失效')
        // 如果获取用户信息失败，说明token可能已失效，清理状态
        logout()
        return { success: false, message: '认证状态已失效' }
      } else {
        console.log('✅ 用户信息获取成功，认证状态恢复')
        console.log('✅ 恢复后的用户信息:', userInfo.value)
        return { success: true, message: '认证状态已恢复' }
      }
    } else if (!token.value) {
      console.log('🚫 没有token，用户未登录')
      // 确保清理任何残留的用户信息
      userInfo.value = null
      localStorage.removeItem('userInfo')
      return { success: false, message: '用户未登录' }
    } else {
      console.log('✅ token和用户信息都存在，认证状态正常')
      return { success: true, message: '认证状态正常' }
    }
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
    checkAuthStatus
  }

  // 保存实例并返回
  authInstance = instance
  return instance
}
