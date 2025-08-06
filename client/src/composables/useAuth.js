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
      
      if (response.code === 200) {
        token.value = response.data
        localStorage.setItem('token', response.data)
        
        // 登录成功后获取用户信息
        await fetchUserInfo()
        
        ElMessage.success('登录成功')
        return { success: true }
      } else {
        ElMessage.error(response.message || '登录失败')
        return { success: false, message: response.message }
      }
    } catch (error) {
      console.error('登录错误:', error)
      ElMessage.error(error.message || '登录失败')
      return { success: false, message: error.message }
    }
  }

  // 注册
  const register = async (registerData) => {
    try {
      const response = await userApi.register(registerData)
      
      if (response.code === 200) {
        ElMessage.success('注册成功，请登录')
        return { success: true }
      } else {
        ElMessage.error(response.message || '注册失败')
        return { success: false, message: response.message }
      }
    } catch (error) {
      console.error('注册错误:', error)
      ElMessage.error(error.message || '注册失败')
      return { success: false, message: error.message }
    }
  }

  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      const response = await userApi.getUserInfo()
      
      if (response.code === 200) {
        userInfo.value = response.data
        localStorage.setItem('userInfo', JSON.stringify(response.data))
        return { success: true, data: response.data }
      } else {
        return { success: false, message: response.message }
      }
    } catch (error) {
      console.error('获取用户信息错误:', error)
      return { success: false, message: error.message }
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
    if (token.value && !userInfo.value) {
      await fetchUserInfo()
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
