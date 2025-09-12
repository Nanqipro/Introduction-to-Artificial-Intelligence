<template>
  <div class="profile-container">
    <div class="bg-decoration">
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
        <div class="shape shape-4"></div>
      </div>
    </div>
    
    <div class="profile-wrapper">

      
      <!-- 用户头像和基本信息 -->
      <ProfileHero 
        :user-info="userInfo"
        
        :user-achievements="userAchievements"
        @show-avatar-dialog="showAvatarDialog = true"
      />
      
      <!-- 主要内容区域 -->
      <div class="profile-content">

        
        <!-- 个人信息 -->
        <UserInfo 
          :form-data="formData"
          :edit-mode="editMode"
          @update:edit-mode="editMode = $event"
          @save="handleSaveInfo"
          @cancel="handleCancelEdit"
        />
        
        <!-- 成就展示 -->
        <UserAchievements :user-achievements="userAchievements" />
      </div>
    </div>
    
    <!-- 头像上传对话框 -->
    <el-dialog
      v-model="showAvatarDialog"
      title="更换头像"
      width="400px"
      class="avatar-dialog"
    >
      <div class="avatar-upload">
        <el-upload
          class="avatar-uploader"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :http-request="customUploadRequest"
        >
          <img v-if="imageUrl" :src="imageUrl" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <div class="upload-tips">
          <p>支持 JPG、PNG 格式，文件大小不超过 2MB</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import ProfileHero from '@/components/profile/ProfileHero.vue'

import UserInfo from '@/components/profile/UserInfo.vue'
import UserAchievements from '@/components/profile/UserAchievements.vue'
import { useAuth } from '@/composables/useAuth'
import { levelApi, userApi } from '@/services/api'

// 使用路由和认证状态
const router = useRouter()
const { currentUser, fetchUserInfo, updateUserInfo, updateAvatar, updatePassword, forceRefreshAuth, token, checkAuthStatus } = useAuth()

// 响应式数据
const showAvatarDialog = ref(false)
const editMode = ref(false)
const imageUrl = ref('')
const isInitialized = ref(false) // 添加初始化标志位

// 用户信息 - 从认证状态获取
const userInfo = reactive({
  username: '',
  role: '学生',
  avatar: ''
})

// 自定义上传请求方法
const customUploadRequest = async (options) => {
  const formData = new FormData()
  formData.append('file', options.file)
  
  try {
    const response = await userApi.uploadAvatar(formData)
    options.onSuccess(response)
    return response
  } catch (error) {
    console.error('上传失败:', error)
    options.onError(error)
    throw error
  }
}



// 用户成就（从后端拉取）
const userAchievements = ref([])

// 表单数据 - 从认证状态获取
const formData = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  school: '',
  major: ''
})



// 刷新用户成就数据的方法
const refreshUserAchievements = async () => {
  console.log('🔄 手动刷新用户成就数据...')
  await fetchUserAchievementsWithRetry()
}



// 带重试机制的获取用户成就
const fetchUserAchievementsWithRetry = async (maxRetries = 3) => {
  // 检查登录状态和token
  const token = localStorage.getItem('token')
  if (!token || token.trim() === '' || token === 'null') {
    console.log('🚫 UserProfile - 未登录，跳过用户成就获取')
    // 设置默认成就数据
    userAchievements.value = []
    console.log('🏆 已设置默认用户成就数据（未登录）')
    return
  }
  
  console.log('🏆 开始拉取用户成就...')
  
  for (let attempt = 1; attempt <= maxRetries; attempt++) {
    try {
      console.log(`🏆 尝试获取用户成就 (${attempt}/${maxRetries})`)
      const achResp = await levelApi.getUserAchievements()
      console.log('🏆 用户成就响应:', achResp)
      
      if (achResp && achResp.code === 200 && Array.isArray(achResp.data)) {
        userAchievements.value = achResp.data
        console.log('✅ 用户成就同步完成:', userAchievements.value)
        return // 成功获取，退出重试循环
      }
    } catch (error) {
      console.error(`❌ 获取用户成就失败 (尝试 ${attempt}/${maxRetries}):`, error)
      
      // 检查是否是认证错误
      if (error.message && error.message.includes('需要登录')) {
        console.log('🚫 认证失败，设置默认数据')
        userAchievements.value = []
        console.log('🏆 已设置默认用户成就数据（认证失败）')
        return
      }
      
      if (attempt === maxRetries) {
        console.log('⚠️ 用户成就获取失败，使用默认数据')
        // 设置默认成就数据
        userAchievements.value = []
        console.log('🏆 已设置默认用户成就数据')
      } else {
        // 等待一段时间后重试
        await new Promise(resolve => setTimeout(resolve, 1000 * attempt))
      }
    }
  }
}

// 同步用户信息到本地状态
const syncUserInfo = () => {
  console.log('🔄 syncUserInfo 被调用')
  console.log('🔍 currentUser.value:', currentUser.value)
  
  if (currentUser.value) {
    console.log('✅ 开始同步用户信息')
    // 更新用户基本信息
    userInfo.username = currentUser.value.username || ''
    userInfo.role = currentUser.value.role || '学生'
    userInfo.avatar = currentUser.value.userPic || ''
    
    // 更新表单数据
    formData.username = currentUser.value.username || ''
    formData.nickname = currentUser.value.nickname || ''
    formData.email = currentUser.value.email || ''
    formData.phone = currentUser.value.phone || ''
    formData.school = currentUser.value.school || ''
    formData.major = currentUser.value.major || ''
    
    console.log('✅ 用户信息同步完成:', {
      username: userInfo.username,
      role: userInfo.role,
      nickname: formData.nickname,
      email: formData.email
    })
  } else {
    console.log('❌ currentUser.value 为空，无法同步用户信息')
  }
}

// 监听用户信息变化
watch(currentUser, () => {
  syncUserInfo()
}, { deep: true })

// 监听页面可见性变化，当页面重新可见时刷新数据
const handleVisibilityChange = () => {
  if (!document.hidden && isInitialized.value) {
    console.log('📱 页面重新可见，刷新用户数据...')
    refreshUserAchievements()
  }
}

// 监听窗口焦点变化
const handleFocus = () => {
    if (isInitialized.value) {
      console.log('🔍 窗口获得焦点，刷新用户数据')
      refreshUserAchievements()
    }
  }

  // 监听经验值更新事件
  const handleExperienceUpdate = (event) => {
    console.log('🎯 收到经验值更新事件:', event.detail)
    // 延迟一点时间再刷新，确保后端数据已更新
    setTimeout(() => {
      refreshUserAchievements()
    }, 500)
  }

// 方法
const handleSaveInfo = async (data) => {
  try {
    let updateSuccess = false
    let passwordSuccess = false
    
    // 更新用户基本信息
    if (data.userInfo) {
      const result = await updateUserInfo(data.userInfo)
      if (result.success) {
        updateSuccess = true
        // 重新从服务器获取最新用户信息
        await fetchUserInfo()
        // 同步到本地状态
        syncUserInfo()
      } else {
        ElMessage.error(result.message || '用户信息保存失败')
        return
      }
    }
    
    // 如果有密码修改
    if (data.passwordChange) {
      const passwordResult = await updatePassword(data.passwordChange)
      if (passwordResult.success) {
        passwordSuccess = true
        ElMessage.success('密码修改成功')
      } else {
        ElMessage.error(passwordResult.message || '密码修改失败')
        return
      }
    }
    
    // 根据操作结果显示消息
    if (updateSuccess && passwordSuccess) {
      ElMessage.success('个人信息和密码修改成功')
    } else if (updateSuccess) {
      ElMessage.success('个人信息保存成功')
    }
    
    editMode.value = false
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败，请重试')
  }
}

const handleCancelEdit = () => {
  editMode.value = false
  ElMessage.info('已取消编辑')
}

const handleAvatarSuccess = async (response, file) => {
  try {
    console.log('上传响应:', response)
    
    // 检查响应格式
    if (response && response.code === 200 && response.data && response.data.avatarUrl) {
      const avatarUrl = response.data.avatarUrl
      imageUrl.value = avatarUrl
      
      ElMessage.success('头像上传成功')
      showAvatarDialog.value = false
      
      // 刷新用户信息以获取最新的头像
      await fetchUserInfo()
      // 同步到本地状态
      syncUserInfo()
    } else {
      console.error('上传响应格式错误:', response)
      ElMessage.error('头像上传失败，服务器响应格式错误')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error('头像上传失败，请重试')
  }
}

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg'
  const isPNG = file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG && !isPNG) {
    ElMessage.error('头像只能是 JPG 或 PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!')
    return false
  }
  return true
}



// 生命周期
onMounted(async () => {
  // 防止重复初始化
  if (isInitialized.value) {
    console.log('🚫 页面已经初始化过，跳过重复初始化')
    return
  }
  
  console.log('🚀 个人中心页面加载完成')
  console.log('🔍 初始 currentUser.value:', currentUser.value)
  
  // 先检查localStorage中的token
  const storedToken = localStorage.getItem('token')
  console.log('🔍 页面加载时检查token:', storedToken ? 'exists' : 'null')
  
  if (!storedToken) {
    console.log('🚫 没有找到token，跳转到登录页')
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  // 有token时才同步认证状态
  console.log('🔄 开始同步认证状态...')
  const authResult = await checkAuthStatus()
  console.log('🔍 同步后的响应式token状态:', token.value ? 'exists' : 'null')
  
  // 如果认证状态检查失败且是认证问题（不是网络问题），才跳转登录页
  if (!authResult.success && authResult.message.includes('认证状态已失效')) {
    console.log('🚫 认证状态失效，跳转到登录页')
    ElMessage.warning('登录状态已失效，请重新登录')
    router.push('/login')
    return
  }
  
  // 如果已有用户信息，直接同步
  if (currentUser.value) {
    console.log('✅ 已有用户信息，直接同步')
    syncUserInfo()
  } else {
    // 获取用户信息
    console.log('📥 获取用户信息...')
    try {
      // 等待一下确保token完全设置
      await new Promise(resolve => setTimeout(resolve, 500))
      
      const result = await fetchUserInfo(true)
      if (result.success) {
        console.log('✅ 用户信息获取成功')
        syncUserInfo()
      } else {
        console.error('❌ 用户信息获取失败:', result.message)
        // 如果是网络错误，不立即跳转，给用户提示
        if (result.message.includes('网络') || result.message.includes('连接')) {
          ElMessage.warning('网络连接不稳定，请稍后刷新页面')
        } else {
          ElMessage.error('获取用户信息失败，请重新登录')
          router.push('/login')
          return
        }
      }
    } catch (error) {
      console.error('❌ 获取用户信息异常:', error)
      ElMessage.warning('网络连接不稳定，请稍后刷新页面')
    }
  }

  // 等待用户信息同步完成后再拉取统计数据
  await new Promise(resolve => setTimeout(resolve, 500))

  // 再次检查认证状态，确保有有效的用户信息后才拉取统计数据
  if (currentUser.value && token.value) {
    console.log('✅ 认证状态有效，开始拉取统计数据')
    // 拉取用户成就（带重试机制和降级处理）
    try {
      await Promise.allSettled([
        fetchUserAchievementsWithRetry()
      ])
      console.log('✅ 数据获取完成（部分可能失败但不影响基本功能）')
    } catch (error) {
      console.log('⚠️ 数据获取过程中出现错误，但基本功能仍可使用:', error)
    }
  } else {
    console.log('🚫 认证状态无效，跳过数据获取')
    // 设置默认数据
    userAchievements.value = []
  }
  
  // 标记已初始化
  isInitialized.value = true
  console.log('✅ 个人中心页面初始化完成')
  
  // 添加事件监听器
    document.addEventListener('visibilitychange', handleVisibilityChange)
    window.addEventListener('focus', handleFocus)
    window.addEventListener('experienceUpdated', handleExperienceUpdate)
})

// 组件卸载时移除事件监听器
onUnmounted(() => {
  document.removeEventListener('visibilitychange', handleVisibilityChange)
  window.removeEventListener('focus', handleFocus)
  window.removeEventListener('experienceUpdated', handleExperienceUpdate)
})
</script>

<style scoped lang="scss">
.profile-container {
  min-height: 100vh;
  background: var(--profile-container-bg);
  position: relative;
}

.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
  opacity: 0.1;
}

.floating-shapes {
  position: relative;
  width: 100%;
  height: 100%;
  
  .shape {
    position: absolute;
    border-radius: 50%;
    background: var(--floating-shape-bg);
    
    &.shape-1 {
      width: 60px;
      height: 60px;
      top: 15%;
      left: 10%;
    }
    
    &.shape-2 {
      width: 80px;
      height: 80px;
      top: 25%;
      right: 15%;
    }
    
    &.shape-3 {
      width: 40px;
      height: 40px;
      bottom: 35%;
      left: 20%;
    }
    
    &.shape-4 {
      width: 70px;
      height: 70px;
      bottom: 25%;
      right: 10%;
    }
  }
}

.profile-wrapper {
  position: relative;
  z-index: 2;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.profile-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
}

/* 头像上传对话框样式 */
.avatar-dialog {
  :deep(.el-dialog) {
    border-radius: 16px;
    overflow: hidden;
  }
  
  .avatar-upload {
    text-align: center;
    
    .avatar-uploader {
      .avatar {
        width: 120px;
        height: 120px;
        border-radius: 50%;
        object-fit: cover;
      }
      
      .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 120px;
        height: 120px;
        line-height: 120px;
        text-align: center;
        border: 1px dashed #d9d9d9;
        border-radius: 50%;
      }
    }
    
    .upload-tips {
      margin-top: 1rem;
      
      p {
        color: var(--text-secondary-color);
        font-size: 0.9rem;
        margin: 0;
      }
    }
  }
}

/* 深色主题优化 */
html.dark-theme .profile-container {
  .profile-wrapper {
    .profile-hero {
      background: linear-gradient(135deg, rgba(0, 191, 255, 0.08), rgba(0, 255, 127, 0.05));
      border: 1px solid rgba(0, 191, 255, 0.2);
      box-shadow: 0 8px 32px rgba(0, 191, 255, 0.1);
      backdrop-filter: blur(20px);
      
      .avatar-container .avatar-wrapper {
        .user-avatar {
          border: 4px solid rgba(0, 191, 255, 0.3);
          box-shadow: 0 8px 32px rgba(0, 191, 255, 0.2);
        }
        
        .avatar-overlay {
          background: rgba(0, 191, 255, 0.9);
          border: 3px solid rgba(0, 191, 255, 0.3);
          box-shadow: 0 4px 16px rgba(0, 191, 255, 0.3);
        }
      }
      
      .user-details {
        .username {
          color: #ffffff;
          text-shadow: 0 2px 8px rgba(0, 191, 255, 0.3);
        }
        
        .user-role {
          color: rgba(255, 255, 255, 0.7);
        }
        
        .user-stats .stat-item {
          background: rgba(0, 191, 255, 0.05);
          border: 1px solid rgba(0, 191, 255, 0.2);
          border-radius: 12px;
          padding: 1rem;
          backdrop-filter: blur(10px);
          
          .stat-number {
            color: #00bfff;
            text-shadow: 0 2px 8px rgba(0, 191, 255, 0.3);
          }
          
          .stat-label {
            color: rgba(255, 255, 255, 0.6);
          }
          
          .level-title {
            color: #00ff7f;
            text-shadow: 0 2px 8px rgba(0, 255, 127, 0.3);
          }
        }
        
        .level-progress {
          background: rgba(0, 191, 255, 0.05);
          border: 1px solid rgba(0, 191, 255, 0.2);
          backdrop-filter: blur(10px);
          
          .progress-header {
            color: rgba(255, 255, 255, 0.8);
            
            span:last-child {
              color: #00ff7f;
            }
          }
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .profile-wrapper {
    padding: 1rem;
  }
  
  .profile-content {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .avatar-dialog :deep(.el-dialog) {
    width: 90% !important;
    margin: 5vh auto;
  }
}
</style>
