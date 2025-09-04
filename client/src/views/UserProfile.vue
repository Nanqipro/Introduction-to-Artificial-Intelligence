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
        :user-stats="userStats"
        :user-achievements="userAchievements"
        @show-avatar-dialog="showAvatarDialog = true"
      />
      
      <!-- 主要内容区域 -->
      <div class="profile-content">
        <!-- 学习统计 -->
        <StudyStats :user-stats="userStats" />
        
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
          action="/api/upload/avatar"
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
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import ProfileHero from '@/components/profile/ProfileHero.vue'
import StudyStats from '@/components/profile/StudyStats.vue'
import UserInfo from '@/components/profile/UserInfo.vue'
import UserAchievements from '@/components/profile/UserAchievements.vue'
import { useAuth } from '@/composables/useAuth'

// 使用认证状态
const { currentUser, fetchUserInfo, updateUserInfo, updateAvatar } = useAuth()

// 响应式数据
const showAvatarDialog = ref(false)
const editMode = ref(false)
const imageUrl = ref('')

// 用户信息 - 从认证状态获取
const userInfo = reactive({
  username: '',
  role: '学生',
  avatar: ''
})

// 用户统计数据
const userStats = reactive({
  level: 3,
  experience: 2500,
  completedChapters: 8,
  totalScore: 1250,
  studyTime: 45,
  achievements: 5,
  networkProgress: 75,
  protocolProgress: 60,
  practiceProgress: 40
})

// 用户成就
const userAchievements = ref([
  {
    id: 1,
    title: '初学者',
    description: '完成第一章学习',
    unlocked: true,
    unlockedAt: '2024-01-15',
    progress: 100
  },
  {
    id: 2,
    title: '网络探索者',
    description: '完成网络基础章节',
    unlocked: true,
    unlockedAt: '2024-01-20',
    progress: 100
  },
  {
    id: 3,
    title: '协议专家',
    description: '完成协议学习章节',
    unlocked: false,
    progress: 60
  }
])

// 表单数据 - 从认证状态获取
const formData = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  school: '',
  major: ''
})

// 同步用户信息到本地状态
const syncUserInfo = () => {
  if (currentUser.value) {
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
  }
}

// 监听用户信息变化
watch(currentUser, () => {
  syncUserInfo()
}, { immediate: true, deep: true })

// 方法
const handleSaveInfo = async (data) => {
  try {
    const result = await updateUserInfo(data)
    if (result.success) {
      ElMessage.success('个人信息保存成功')
      editMode.value = false
      // 重新同步用户信息
      syncUserInfo()
    } else {
      // 处理具体的错误信息
      ElMessage.error(result.message || '保存失败，请重试')
    }
  } catch (error) {
    console.error('保存个人信息失败:', error)
    ElMessage.error('保存失败，请重试')
  }
}

const handleCancelEdit = () => {
  editMode.value = false
  ElMessage.info('已取消编辑')
}

const handleAvatarSuccess = async (response, file) => {
  try {
    // 这里应该是上传到服务器后返回的图片URL
    const avatarUrl = URL.createObjectURL(file.raw) // 临时使用本地URL，实际应该是服务器返回的URL
    imageUrl.value = avatarUrl
    
    // 调用API更新头像
    const result = await updateAvatar(avatarUrl)
    if (result.success) {
      ElMessage.success('头像上传成功')
      showAvatarDialog.value = false
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
  console.log('个人中心页面加载完成')
  
  // 如果没有用户信息，尝试获取
  if (!currentUser.value) {
    try {
      await fetchUserInfo()
      console.log('用户信息获取成功')
    } catch (error) {
      console.error('获取用户信息失败:', error)
      ElMessage.error('获取用户信息失败')
    }
  } else {
    // 如果已有用户信息，直接同步
    syncUserInfo()
  }
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
