import { ref, computed } from 'vue'
import { userApi } from '@/services/api'
import { ElMessage } from 'element-plus'

// 全局状态
const token = ref(localStorage.getItem('token') || '')
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))

export function useAuth() {
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
        
        // 登录成功后获取用户信息
        await fetchUserInfo()
        
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
      const response = await userApi.getUserInfo()
      console.log('获取用户信息响应:', response)
      
      if (response && response.code === 200) {
        userInfo.value = response.data
        localStorage.setItem('userInfo', JSON.stringify(response.data))
        return { success: true, data: response.data }
      } else {
        const errorMsg = response?.message || '获取用户信息失败'
        return { success: false, message: errorMsg }
      }
    } catch (error) {
      console.error('获取用户信息错误:', error)
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
    console.log('检查认证状态 - 当前token:', token.value ? token.value.substring(0, 20) + '...' : 'null')
    console.log('检查认证状态 - 当前userInfo:', userInfo.value ? 'exists' : 'null')
    
    if (token.value && !userInfo.value) {
      console.log('有token但无用户信息，重新获取用户信息')
      await fetchUserInfo()
    } else if (!token.value) {
      console.log('没有token，用户未登录')
    } else {
      console.log('token和用户信息都存在，认证状态正常')
    }
  }

  return {
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
}
