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
      <div class="profile-hero">
        <div class="hero-content">
          <div class="avatar-container">
            <div class="avatar-wrapper">
              <el-avatar :size="120" :src="userInfo.avatar" class="user-avatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <div class="avatar-overlay" @click="showAvatarDialog = true">
                <el-icon class="camera-icon"><Camera /></el-icon>
              </div>
            </div>
            <div class="status-indicator"></div>
          </div>
          
          <div class="user-details">
            <h1 class="username">{{ userInfo.username || '用户名' }}</h1>
            <p class="user-role">{{ userInfo.role || '学生' }}</p>
            
            <div class="user-stats">
              <div class="stat-item level-stat">
                <span class="stat-number">{{ userStats.level || 1 }}</span>
                <span class="stat-label">当前等级</span>
                <div class="level-title">{{ getLevelTitle(userStats.level || 1) }}</div>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ userStats.experience || 0 }}</span>
                <span class="stat-label">经验值</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ userStats.completedChapters || 0 }}</span>
                <span class="stat-label">已完成章节</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ userAchievements.length || 0 }}</span>
                <span class="stat-label">获得成就</span>
              </div>
            </div>
            
            <!-- 等级进度条 -->
            <div class="level-progress">
              <div class="progress-header">
                <span>等级进度</span>
                <span>{{ getExpToNextLevel() }} 经验值升级</span>
              </div>
              <el-progress 
                :percentage="getLevelProgress()" 
                :stroke-width="8"
                :show-text="true"
                status="success"
              />
            </div>
          </div>
        </div>
      </div>
      
      <!-- 主要内容区域 -->
      <div class="profile-content">
        <!-- 学习统计 -->
        <div class="stats-card">
          <div class="card-header">
            <div class="header-left">
              <div class="card-icon stats-icon">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="header-text">
                <h3>学习统计</h3>
                <p>查看您的学习进度和成就</p>
              </div>
            </div>
          </div>
          
          <div class="card-content">
            <div class="stats-grid">
              <div class="stat-card">
                <div class="stat-icon">
                  <el-icon><Document /></el-icon>
                </div>
                <div class="stat-info">
                  <h4>{{ userStats.completedChapters }}</h4>
                  <p>已完成章节</p>
                </div>
              </div>
              
              <div class="stat-card">
                <div class="stat-icon">
                  <el-icon><Trophy /></el-icon>
                </div>
                <div class="stat-info">
                  <h4>{{ userStats.totalScore }}</h4>
                  <p>总得分</p>
                </div>
              </div>
              
              <div class="stat-card">
                <div class="stat-icon">
                  <el-icon><Timer /></el-icon>
                </div>
                <div class="stat-info">
                  <h4>{{ userStats.studyTime }}h</h4>
                  <p>学习时长</p>
                </div>
              </div>
              
              <div class="stat-card">
                <div class="stat-icon">
                  <el-icon><Medal /></el-icon>
                </div>
                <div class="stat-info">
                  <h4>{{ userStats.achievements }}</h4>
                  <p>获得成就</p>
                </div>
              </div>
            </div>
            
            <div class="progress-section">
              <h4>学习进度</h4>
              <div class="progress-item">
                <div class="progress-label">
                  <span>网络基础</span>
                  <span>{{ userStats.networkProgress }}%</span>
                </div>
                <el-progress :percentage="userStats.networkProgress" :show-text="false" />
              </div>
              
              <div class="progress-item">
                <div class="progress-label">
                  <span>协议学习</span>
                  <span>{{ userStats.protocolProgress }}%</span>
                </div>
                <el-progress :percentage="userStats.protocolProgress" :show-text="false" />
              </div>
              
              <div class="progress-item">
                <div class="progress-label">
                  <span>实践训练</span>
                  <span>{{ userStats.practiceProgress }}%</span>
                </div>
                <el-progress :percentage="userStats.practiceProgress" :show-text="false" />
              </div>
            </div>
          </div>
        </div>
        
        <!-- 个人信息 -->
        <div class="info-card">
          <div class="card-header">
            <div class="header-left">
              <div class="card-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="header-text">
                <h3>个人信息</h3>
                <p>管理您的个人资料</p>
              </div>
            </div>
            <div class="edit-switch">
              <el-switch
                v-model="editMode"
                active-text="编辑"
                inactive-text="查看"
              />
            </div>
          </div>
          
          <div class="card-content">
            <el-form
              ref="formRef"
              :model="formData"
              :rules="formRules"
              label-position="top"
              class="info-form"
            >
              <div class="form-grid">
                <el-form-item label="用户名" prop="username">
                  <el-input
                    v-model="formData.username"
                    :disabled="!editMode"
                    placeholder="请输入用户名"
                  >
                    <template #prefix>
                      <el-icon><User /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>
                
                <el-form-item label="昵称" prop="nickname">
                  <el-input
                    v-model="formData.nickname"
                    :disabled="!editMode"
                    placeholder="请输入昵称"
                  >
                    <template #prefix>
                      <el-icon><Edit /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>
                
                <el-form-item label="邮箱" prop="email">
                  <el-input
                    v-model="formData.email"
                    :disabled="!editMode"
                    placeholder="请输入邮箱地址"
                  >
                    <template #prefix>
                      <el-icon><Message /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>
                
                <el-form-item label="手机号码" prop="phone">
                  <el-input
                    v-model="formData.phone"
                    :disabled="!editMode"
                    placeholder="请输入手机号码"
                  >
                    <template #prefix>
                      <el-icon><Phone /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>
                
                <el-form-item label="性别" prop="gender">
                  <el-select
                    v-model="formData.gender"
                    :disabled="!editMode"
                    placeholder="请选择性别"
                    style="width: 100%"
                  >
                    <el-option label="男" value="male" />
                    <el-option label="女" value="female" />
                    <el-option label="保密" value="secret" />
                  </el-select>
                </el-form-item>
                
                <el-form-item label="生日" prop="birthday">
                  <el-date-picker
                    v-model="formData.birthday"
                    :disabled="!editMode"
                    type="date"
                    placeholder="请选择生日"
                    style="width: 100%"
                  />
                </el-form-item>
              </div>
              
              <el-form-item label="所在地区" prop="location">
                <el-input
                  v-model="formData.location"
                  :disabled="!editMode"
                  placeholder="请输入所在地区"
                >
                  <template #prefix>
                    <el-icon><Location /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
              
              <el-form-item label="个人简介" prop="bio">
                <el-input
                  v-model="formData.bio"
                  :disabled="!editMode"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入个人简介"
                />
              </el-form-item>
              
              <div v-if="editMode" class="form-actions">
                <el-button @click="resetForm" class="cancel-btn">
                  <el-icon><RefreshLeft /></el-icon>
                  重置
                </el-button>
                <el-button type="primary" @click="handleUpdateInfo" :loading="loading" class="save-btn">
                  <el-icon><Check /></el-icon>
                  保存
                </el-button>
              </div>
            </el-form>
          </div>
        </div>
        
        <!-- 通知设置 -->
        <div class="notification-card">
          <div class="card-header">
            <div class="header-left">
              <div class="card-icon notification-icon">
                <el-icon><Bell /></el-icon>
              </div>
              <div class="header-text">
                <h3>通知设置</h3>
                <p>管理您的通知偏好</p>
              </div>
            </div>
          </div>
          
          <div class="card-content">
            <div class="notification-settings">
              <div class="setting-group">
                <h4>学习提醒</h4>
                <div class="setting-item">
                  <div class="setting-info">
                    <span class="setting-label">每日学习提醒</span>
                    <span class="setting-desc">每天定时提醒您进行学习</span>
                  </div>
                  <el-switch v-model="notificationSettings.dailyReminder" />
                </div>
                
                <div class="setting-item">
                  <div class="setting-info">
                    <span class="setting-label">作业截止提醒</span>
                    <span class="setting-desc">作业截止前24小时提醒</span>
                  </div>
                  <el-switch v-model="notificationSettings.assignmentReminder" />
                </div>
                
                <div class="setting-item">
                  <div class="setting-info">
                    <span class="setting-label">学习进度提醒</span>
                    <span class="setting-desc">每周发送学习进度报告</span>
                  </div>
                  <el-switch v-model="notificationSettings.progressReminder" />
                </div>
              </div>
              
              <div class="setting-group">
                <h4>系统通知</h4>
                <div class="setting-item">
                  <div class="setting-info">
                    <span class="setting-label">系统更新通知</span>
                    <span class="setting-desc">接收系统更新和维护通知</span>
                  </div>
                  <el-switch v-model="notificationSettings.systemUpdate" />
                </div>
                
                <div class="setting-item">
                  <div class="setting-info">
                    <span class="setting-label">新功能介绍</span>
                    <span class="setting-desc">接收新功能和改进介绍</span>
                  </div>
                  <el-switch v-model="notificationSettings.featureIntro" />
                </div>
              </div>
              
              <div class="setting-group">
                <h4>邮件通知</h4>
                <div class="setting-item">
                  <div class="setting-info">
                    <span class="setting-label">邮件提醒</span>
                    <span class="setting-desc">通过邮件接收重要通知</span>
                  </div>
                  <el-switch v-model="notificationSettings.emailNotification" />
                </div>
                
                <div class="setting-item" v-if="notificationSettings.emailNotification">
                  <div class="setting-info">
                    <span class="setting-label">提醒时间</span>
                    <span class="setting-desc">设置每日提醒的时间</span>
                  </div>
                  <el-time-picker
                    v-model="notificationSettings.reminderTime"
                     format="HH:mm"
                     value-format="HH:mm"
                    placeholder="选择时间"
                    style="width: 120px;"
                  />
                </div>
              </div>
              
              <div class="notification-actions">
                <el-button type="primary" @click="saveNotificationSettings" :loading="notificationLoading">
                  <el-icon><Check /></el-icon>
                  保存设置
                </el-button>
                <el-button @click="resetNotificationSettings">
                  <el-icon><RefreshLeft /></el-icon>
                  重置默认
                </el-button>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 安全设置 -->
        <div class="security-card">
          <div class="card-header">
            <div class="header-left">
              <div class="card-icon security-icon">
                <el-icon><Lock /></el-icon>
              </div>
              <div class="header-text">
                <h3>安全设置</h3>
                <p>修改密码和安全选项</p>
              </div>
            </div>
          </div>
          
          <div class="card-content">
            <el-form
              ref="passwordFormRef"
              :model="passwordForm"
              :rules="passwordRules"
              label-position="top"
              class="password-form"
            >
              <el-form-item label="当前密码" prop="currentPassword">
                <el-input
                  v-model="passwordForm.currentPassword"
                  type="password"
                  placeholder="请输入当前密码"
                  show-password
                >
                  <template #prefix>
                    <el-icon><Lock /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
              
              <el-form-item label="新密码" prop="newPassword">
                <el-input
                  v-model="passwordForm.newPassword"
                  type="password"
                  placeholder="请输入新密码"
                  show-password
                >
                  <template #prefix>
                    <el-icon><Key /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
              
              <el-form-item label="确认新密码" prop="confirmPassword">
                <el-input
                  v-model="passwordForm.confirmPassword"
                  type="password"
                  placeholder="请再次输入新密码"
                  show-password
                >
                  <template #prefix>
                    <el-icon><Key /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
              
              <div class="form-actions">
                <el-button @click="resetPasswordForm" class="reset-btn">
                  <el-icon><RefreshLeft /></el-icon>
                  重置
                </el-button>
                <el-button type="primary" @click="handleUpdatePassword" :loading="passwordLoading" class="update-btn">
                  <el-icon><Check /></el-icon>
                  更新密码
                </el-button>
              </div>
            </el-form>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 头像上传对话框 -->
    <el-dialog
      v-model="showAvatarDialog"
      title="更换头像"
      width="500px"
      class="avatar-dialog"
    >
      <div class="avatar-upload">
        <el-tabs v-model="uploadTab" class="upload-tabs">
          <el-tab-pane label="URL上传" name="url">
            <div class="upload-area">
              <el-input
                v-model="avatarUrl"
                placeholder="请输入头像URL"
                clearable
                class="avatar-input"
              >
                <template #prepend>
                  <el-icon><Link /></el-icon>
                </template>
              </el-input>
              
              <div class="upload-tips">
                <p>支持 JPG、PNG 格式，建议尺寸 200x200 像素</p>
              </div>
            </div>
          </el-tab-pane>
          
          <el-tab-pane label="文件上传" name="file">
            <div class="upload-area">
              <el-upload
                ref="uploadRef"
                class="avatar-uploader"
                :show-file-list="false"
                :before-upload="beforeAvatarUpload"
                :on-success="handleAvatarSuccess"
                :on-error="handleAvatarError"
                action="#"
                :http-request="customUpload"
              >
                <div class="upload-dragger">
                  <el-icon class="upload-icon"><Upload /></el-icon>
                  <div class="upload-text">
                    <p>点击或拖拽文件到此区域上传</p>
                    <p class="upload-hint">支持 JPG、PNG 格式，文件大小不超过 2MB</p>
                  </div>
                </div>
              </el-upload>
            </div>
          </el-tab-pane>
        </el-tabs>
        
        <div class="avatar-preview" v-if="avatarUrl || uploadedFile">
          <h4>预览效果</h4>
          <div class="preview-container">
            <el-avatar :size="100" :src="avatarUrl || uploadedFile" class="preview-avatar">
              <el-icon><User /></el-icon>
            </el-avatar>
          </div>
        </div>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showAvatarDialog = false" class="cancel-btn">
            取消
          </el-button>
          <el-button type="primary" @click="handleUpdateAvatar" :loading="avatarLoading" class="confirm-btn">
            <el-icon><Check /></el-icon>
            确认更换
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { userApi, levelApi } from '@/services/api'
import { useAuth } from '@/composables/useAuth'
import { 
  User, 
  Edit, 
  Lock, 
  Check, 
  Link, 
  Camera, 
  Upload, 
  Bell,
  Message,
  Phone,
  Location,
  Key,
  RefreshLeft,
  TrendCharts,
  Document,
  Trophy,
  Timer,
  Medal
} from '@element-plus/icons-vue'

// 路由
const router = useRouter()

// 使用共享的认证状态
const { userInfo: authUserInfo, token, isLoggedIn, logout } = useAuth()

// 用户信息（从认证状态获取或使用默认值）
const userInfo = computed(() => authUserInfo.value || {
  username: '',
  nickname: '',
  email: '',
  phone: '',
  gender: '',
  birthday: '',
  location: '',
  bio: '',
  avatar: '',
  role: '学生'
})

// 表单数据
const formData = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  gender: '',
  birthday: '',
  location: '',
  bio: ''
})

// 密码表单
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 通知设置
const notificationSettings = reactive({
  dailyReminder: true,
  assignmentReminder: true,
  progressReminder: false,
  systemUpdate: true,
  featureIntro: false,
  emailNotification: true,
  reminderTime: '09:00'
})

// 用户统计数据 - 从用户信息动态获取
const userStats = computed(() => {
  const user = authUserInfo.value
  if (!user) return {
    level: 1,
    experience: 0,
    completedChapters: 0,
    totalScore: 0,
    studyTime: 0,
    achievements: 0,
    networkProgress: 0,
    protocolProgress: 0,
    practiceProgress: 0
  }
  
  return {
    level: user.level || 1,
    experience: user.experience || 0,
    completedChapters: user.completedChapters || 0,
    totalScore: user.totalScore || 0,
    studyTime: Math.floor((user.experience || 0) / 10), // 根据经验值计算学习时长
    achievements: userAchievements.value.length,
    networkProgress: Math.min(100, Math.floor(((user.completedChapters || 0) / 20) * 100)),
    protocolProgress: Math.min(100, Math.floor(((user.experience || 0) / 1000) * 100)),
    practiceProgress: Math.min(100, Math.floor(((user.quizCount || 0) / 50) * 100))
  }
})

// 用户成就数据
const userAchievements = ref([
  { id: 1, name: '初学者', description: '完成第一个章节', icon: 'trophy' },
  { id: 2, name: '勤奋学习者', description: '连续学习7天', icon: 'medal' },
  { id: 3, name: '知识探索者', description: '完成10个章节', icon: 'star' }
])

// 状态管理
const editMode = ref(false)
const loading = ref(false)
const passwordLoading = ref(false)
const notificationLoading = ref(false)
const showAvatarDialog = ref(false)
const avatarUrl = ref('')
const avatarLoading = ref(false)
const uploadTab = ref('url')
const uploadedFile = ref('')
const uploadRef = ref()
const formRef = ref()
const passwordFormRef = ref()

// 表单验证规则
const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 密码验证规则
const passwordRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 重置表单
const resetForm = () => {
  const user = userInfo.value
  if (user) {
    formData.username = user.username || ''
    formData.nickname = user.nickname || ''
    formData.email = user.email || ''
    formData.phone = user.phone || ''
    formData.gender = user.gender || ''
    formData.birthday = user.birthday || ''
    formData.location = user.location || ''
    formData.bio = user.bio || ''
  }
}

// 重置密码表单
const resetPasswordForm = () => {
  passwordForm.currentPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordFormRef.value?.clearValidate()
}

// 更新用户信息
const handleUpdateInfo = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 更新用户信息
    Object.assign(userInfo.value, formData)
    
    ElMessage.success('个人信息更新成功')
    editMode.value = false
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    loading.value = false
  }
}

// 文件上传相关方法
const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2
  
  if (!isJPG) {
    ElMessage.error('头像图片只能是 JPG/PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

const customUpload = (options) => {
  const file = options.file
  const reader = new FileReader()
  reader.onload = (e) => {
    uploadedFile.value = e.target.result
    avatarUrl.value = '' // 清空URL输入
    ElMessage.success('文件上传成功')
  }
  reader.readAsDataURL(file)
}

const handleAvatarSuccess = (response, file) => {
  uploadedFile.value = URL.createObjectURL(file.raw)
  ElMessage.success('上传成功')
}

const handleAvatarError = () => {
  ElMessage.error('上传失败，请重试')
}

// 更新头像
const handleUpdateAvatar = async () => {
  const finalAvatarUrl = avatarUrl.value || uploadedFile.value
  if (!finalAvatarUrl) {
    ElMessage.warning('请输入头像URL或上传图片')
    return
  }
  
  avatarLoading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    userInfo.value.avatar = finalAvatarUrl
    ElMessage.success('头像更新成功')
    showAvatarDialog.value = false
    // 重置上传状态
    avatarUrl.value = ''
    uploadedFile.value = ''
    uploadTab.value = 'url'
  } catch (error) {
    ElMessage.error('头像更新失败')
  } finally {
    avatarLoading.value = false
  }
}

// 更新密码
const handleUpdatePassword = async () => {
  if (!passwordFormRef.value) return

  try {
    await passwordFormRef.value.validate()
    passwordLoading.value = true

    // 调用真正的后端API
    const passwordData = {
      oldPwd: passwordForm.currentPassword,
      newPwd: passwordForm.newPassword,
      confirmPwd: passwordForm.confirmPassword
    }
    
    await userApi.updatePassword(passwordData)
    
    ElMessage.success('密码更新成功')
    resetPasswordForm()
  } catch (error) {
    console.error('密码更新失败:', error)
    ElMessage.error(error.message || '密码更新失败，请重试')
  } finally {
    passwordLoading.value = false
  }
}

// 保存通知设置
const saveNotificationSettings = async () => {
  notificationLoading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('通知设置已保存')
  } catch (error) {
    ElMessage.error('保存失败，请重试')
  } finally {
    notificationLoading.value = false
  }
}

// 重置通知设置
const resetNotificationSettings = () => {
  notificationSettings.dailyReminder = true
  notificationSettings.assignmentReminder = true
  notificationSettings.progressReminder = false
  notificationSettings.systemUpdate = true
  notificationSettings.featureIntro = false
  notificationSettings.emailNotification = true
  notificationSettings.reminderTime = '09:00'
  ElMessage.success('已重置为默认设置')
}

// 等级系统相关方法
const getLevelTitle = (level) => {
  const titles = {
    1: '新手',
    2: '学徒',
    3: '熟练者',
    4: '专家',
    5: '大师',
    6: '宗师'
  }
  return titles[level] || '未知'
}

const getLevelProgress = () => {
  const currentLevel = userStats.value.level || 1
  const currentExp = userStats.value.experience || 0
  const expForCurrentLevel = (currentLevel - 1) * 500
  const expForNextLevel = currentLevel * 500
  const progressExp = currentExp - expForCurrentLevel
  const levelExpRange = expForNextLevel - expForCurrentLevel
  return Math.round((progressExp / levelExpRange) * 100)
}

const getExpToNextLevel = () => {
  const currentLevel = userStats.value.level || 1
  const currentExp = userStats.value.experience || 0
  const expForNextLevel = currentLevel * 500
  return expForNextLevel - currentExp
}

// 获取用户等级数据
const fetchUserLevelData = async () => {
  // 确保用户已登录且有token
  if (!isLoggedIn.value || !token.value) {
    console.log('用户未登录，跳过获取等级数据')
    return
  }
  
  try {
    console.log('开始获取用户等级数据...')
    // 由于后端可能没有这些API，我们直接使用用户信息中的统计数据
    // 用户信息已经通过fetchUserInfo获取，包含了level、experience等字段
    
    if (authUserInfo.value) {
      console.log('用户信息已获取，包含统计数据:', {
        level: authUserInfo.value.level,
        experience: authUserInfo.value.experience,
        completedChapters: authUserInfo.value.completedChapters,
        totalScore: authUserInfo.value.totalScore
      })
    }
  } catch (error) {
    console.error('获取用户等级数据失败:', error)
  }
}

// 初始化用户数据
const initializeUserData = async () => {
  console.log('UserProfile: 开始初始化用户数据...')
  console.log('UserProfile: 当前登录状态:', isLoggedIn.value)
  console.log('UserProfile: 当前token状态:', token.value ? 'exists' : 'null')
  console.log('UserProfile: 当前用户信息状态:', authUserInfo.value ? 'exists' : 'null')
  
  // 如果没有token，说明用户未登录，跳转到登录页
  if (!token.value) {
    console.log('UserProfile: 没有token，跳转到登录页')
    router.push('/login')
    return
  }
  
  // 如果有token但没有用户信息，先获取用户信息
  if (token.value && !authUserInfo.value) {
    console.log('UserProfile: 有token但无用户信息，先获取用户信息')
    
    // 先测试token是否有效（使用fetch，避免Axios的问题）
    console.log('UserProfile: 测试token有效性（使用fetch）...')
    try {
      const testResponse = await fetch('/api/user/userInfo', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token.value}`,
          'Content-Type': 'application/json'
        }
      })
      console.log('UserProfile: Fetch Token测试结果:', testResponse.status, testResponse.statusText)
      
      if (testResponse.status === 200) {
        const userData = await testResponse.json()
        console.log('UserProfile: Token有效，用户信息:', userData)
        // 直接设置用户信息，避免重复调用
        if (userData.code === 200 && userData.data) {
          authUserInfo.value = userData.data
          return
        } else {
          console.error('UserProfile: 用户信息格式错误:', userData)
          logout()
          router.push('/login')
          return
        }
      } else if (testResponse.status === 401) {
        console.error('UserProfile: Token无效，需要重新登录')
        logout()
        router.push('/login')
        return
      } else {
        console.error('UserProfile: 请求失败，状态码:', testResponse.status)
        logout()
        router.push('/login')
        return
      }
    } catch (error) {
      console.error('UserProfile: Fetch Token测试失败:', error)
      logout()
      router.push('/login')
      return
    }
    
    // 已经通过Axios测试获取了用户信息，不需要再调用fetchUserInfo
  }
  
  // 确保用户信息存在后再进行后续操作
  if (!authUserInfo.value) {
    console.error('UserProfile: 用户信息仍然为空，无法初始化')
    router.push('/login')
    return
  }
  
  console.log('UserProfile: 用户信息验证通过:', {
    username: authUserInfo.value.username,
    nickname: authUserInfo.value.nickname,
    email: authUserInfo.value.email
  })
  
  // 重置表单
  resetForm()
  
  // 获取等级数据
  await fetchUserLevelData()
  
  console.log('UserProfile: 用户数据初始化完成')
}

// 初始化 - 只在组件挂载时执行一次
onMounted(async () => {
  await initializeUserData()
})

// 监听认证状态变化，但不自动初始化（避免重复调用）
watch([isLoggedIn, authUserInfo], async ([newIsLoggedIn, newUserInfo]) => {
  console.log('UserProfile: 认证状态变化', { isLoggedIn: newIsLoggedIn, hasUserInfo: !!newUserInfo })
  if (newIsLoggedIn && newUserInfo) {
    console.log('UserProfile: 用户信息变化，更新表单数据')
    resetForm()
    // 不调用fetchUserLevelData，避免重复请求
  }
})
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;

.profile-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1a1a, #2d2d2d);
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
  opacity: 0.3;
}

.floating-shapes {
  position: relative;
  width: 100%;
  height: 100%;
  
  .shape {
    position: absolute;
    border-radius: 50%;
    background: rgba(0, 0, 0, 0.2);
    
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

.profile-hero {
  background: rgba(42, 42, 42, 0.95);
  border-radius: 20px;
  padding: 3rem;
  margin-bottom: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.1);
  
  .hero-content {
    display: flex;
    align-items: center;
    gap: 3rem;
  }
  
  .avatar-container {
    position: relative;
    
    .avatar-wrapper {
      position: relative;
      display: inline-block;
      
      .user-avatar {
        border: 4px solid #fff;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
      }
      
      .avatar-overlay {
        position: absolute;
        bottom: 0;
        right: 0;
        width: 36px;
        height: 36px;
        background: linear-gradient(135deg, #333333, #555555);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        border: 3px solid #fff;
        
        .camera-icon {
          color: white;
          font-size: 16px;
        }
      }
    }
    
    .status-indicator {
      position: absolute;
      top: 10px;
      right: 10px;
      width: 16px;
      height: 16px;
      background: #52c41a;
      border-radius: 50%;
      border: 3px solid #fff;
    }
  }
  
  .user-details {
    flex: 1;
    
    .username {
      font-size: 2.5rem;
      font-weight: 700;
      color: #ffffff;
      margin-bottom: 0.5rem;
    }
    
    .user-role {
      font-size: 1.2rem;
      color: #b0b3b8;
      margin-bottom: 2rem;
    }
    
    .user-stats {
      display: flex;
      gap: 2rem;
      
      .stat-item {
        text-align: center;
        
        .stat-number {
          display: block;
          font-size: 2rem;
          font-weight: 700;
          color: #ffffff;
          margin-bottom: 0.25rem;
        }
        
        .stat-label {
          font-size: 0.9rem;
          color: #8e9297;
          text-transform: uppercase;
          letter-spacing: 0.5px;
        }
        
        &.level-stat {
          .level-title {
            font-size: 0.8rem;
            color: #67c23a;
            font-weight: bold;
            margin-top: 0.25rem;
            text-transform: none;
            letter-spacing: normal;
          }
        }
      }
    }
    
    .level-progress {
      margin-top: 1.5rem;
      padding: 1rem;
      background: rgba(255, 255, 255, 0.1);
      border-radius: 8px;
      border: 1px solid rgba(255, 255, 255, 0.2);
      
      .progress-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 0.75rem;
        font-size: 0.9rem;
        color: #b0b3b8;
        
        span:last-child {
          color: #67c23a;
          font-weight: bold;
        }
      }
    }
  }
}

.profile-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
}

.stats-card {
  grid-column: 1 / -1;
  background: rgba(42, 42, 42, 0.9);
  border-radius: 16px;
  padding: 0;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.4);
  border: 1px solid rgba(255, 255, 255, 0.1);
  overflow: hidden;
  
  .card-header {
    background: $header-background;
    padding: 1.5rem 2rem;
    border-bottom: 1px solid $border-color;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 1rem;
      
      .card-icon {
        width: 48px;
        height: 48px;
        background: linear-gradient(135deg, #4f46e5, #7c3aed);
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 20px;
        
        &.stats-icon {
          background: linear-gradient(135deg, #059669, #10b981);
        }
      }
      
      .header-text {
        h3 {
          font-size: 1.25rem;
          font-weight: 600;
          color: $text-primary;
          margin: 0 0 0.25rem 0;
        }
        
        p {
          font-size: 0.9rem;
          color: $text-secondary;
          margin: 0;
        }
      }
    }
  }
  
  .card-content {
    padding: 2rem;
    
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1.5rem;
      margin-bottom: 2rem;
      
      .stat-card {
        background: rgba(60, 60, 60, 0.6);
        border-radius: 12px;
        padding: 1.5rem;
        display: flex;
        align-items: center;
        gap: 1rem;
        border: 1px solid rgba(255, 255, 255, 0.1);
        
        .stat-icon {
          width: 48px;
          height: 48px;
          background: linear-gradient(135deg, #333333, #555555);
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-size: 20px;
        }
        
        .stat-info {
          h4 {
            font-size: 1.75rem;
            font-weight: 700;
            color: $text-primary;
            margin: 0 0 0.25rem 0;
          }
          
          p {
            font-size: 0.9rem;
            color: $text-secondary;
            margin: 0;
          }
        }
      }
    }
    
    .progress-section {
      h4 {
        font-size: 1.1rem;
        font-weight: 600;
        color: $text-primary;
        margin: 0 0 1.5rem 0;
      }
      
      .progress-item {
        margin-bottom: 1.5rem;
        
        &:last-child {
          margin-bottom: 0;
        }
        
        .progress-label {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 0.5rem;
          
          span {
            font-size: 0.9rem;
            color: $text-primary;
            font-weight: 500;
            
            &:last-child {
              color: $primary-color;
              font-weight: 600;
            }
          }
        }
        
        :deep(.el-progress) {
          .el-progress-bar__outer {
            background-color: rgba(255, 255, 255, 0.1);
            border-radius: 4px;
          }
          
          .el-progress-bar__inner {
            background: linear-gradient(90deg, #4f46e5, #7c3aed);
            border-radius: 4px;
          }
        }
      }
    }
  }
}

.info-card,
.notification-card,
.security-card {
  background: rgba(42, 42, 42, 0.9);
  border-radius: 16px;
  padding: 0;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.4);
  border: 1px solid rgba(255, 255, 255, 0.1);
  overflow: hidden;
  
  .card-header {
    background: $header-background;
    padding: 1.5rem 2rem;
    border-bottom: 1px solid $border-color;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 1rem;
      
      .card-icon {
        width: 48px;
        height: 48px;
        background: linear-gradient(135deg, #333333, #555555);
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 20px;
        
        &.notification-icon {
          background: linear-gradient(135deg, #3b82f6, #1d4ed8);
        }
        
        &.security-icon {
          background: linear-gradient(135deg, #f56565, #e53e3e);
        }
      }
      
      .header-text {
        h3 {
          font-size: 1.25rem;
          font-weight: 600;
          color: $text-primary;
          margin: 0 0 0.25rem 0;
        }
        
        p {
          font-size: 0.9rem;
          color: $text-secondary;
          margin: 0;
        }
      }
    }
    
    .edit-switch {
      :deep(.el-switch__label) {
        color: $text-secondary;
        font-weight: 500;
      }
    }
  }
  
  .card-content {
    padding: 2rem;
  }
}

.notification-settings {
  .setting-group {
    margin-bottom: 2rem;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    h4 {
      font-size: 1.1rem;
      font-weight: 600;
      color: $text-primary;
      margin: 0 0 1rem 0;
      padding-bottom: 0.5rem;
      border-bottom: 1px solid $border-color;
    }
    
    .setting-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 1rem 0;
      border-bottom: 1px solid rgba(255, 255, 255, 0.05);
      
      &:last-child {
        border-bottom: none;
      }
      
      .setting-info {
        flex: 1;
        
        .setting-label {
          display: block;
          font-size: 0.95rem;
          font-weight: 500;
          color: $text-primary;
          margin-bottom: 0.25rem;
        }
        
        .setting-desc {
          font-size: 0.85rem;
          color: $text-secondary;
          line-height: 1.4;
        }
      }
      
      :deep(.el-switch) {
        .el-switch__core {
          background-color: rgba(255, 255, 255, 0.2);
          border-color: transparent;
          
          &.is-checked {
            background-color: $primary-color;
          }
        }
      }
      
      :deep(.el-time-picker) {
        .el-input__wrapper {
          background: $input-background;
          border: 2px solid $border-color;
          border-radius: 6px;
          
          &:hover {
            border-color: $primary-color;
          }
          
          &.is-focus {
            border-color: $primary-color;
          }
        }
        
        .el-input__inner {
          color: $text-primary;
          font-size: 0.9rem;
        }
      }
    }
  }
  
  .notification-actions {
    display: flex;
    gap: 1rem;
    justify-content: flex-end;
    padding-top: 1.5rem;
    border-top: 1px solid $border-color;
    margin-top: 2rem;
    
    .el-button {
      padding: 0.75rem 2rem;
      border-radius: 8px;
      font-weight: 600;
      
      &[type="primary"] {
        background: $primary-color;
        border: none;
        color: white;
        
        &:hover {
          background: $primary-hover;
        }
      }
      
      &:not([type]) {
        background: $button-secondary;
        border: 2px solid $border-color;
        color: $text-secondary;
        
        &:hover {
          background: $button-secondary-hover;
          border-color: $border-hover;
          color: $text-primary;
        }
      }
    }
  }
}

.info-form {
  .form-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1.5rem;
    margin-bottom: 2rem;
  }
  
  :deep(.el-form-item) {
    margin-bottom: 1.5rem;
    
    .el-form-item__label {
      color: $text-primary;
      font-weight: 600;
      font-size: 0.9rem;
      margin-bottom: 0.5rem;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
    
    .el-input {
      .el-input__wrapper {
        background: $input-background;
        border: 2px solid $border-color;
        border-radius: 8px;
        padding: 0.75rem 1rem;
        box-shadow: none;
        
        &:hover {
          border-color: $primary-color;
          background: $card-background;
        }
        
        &.is-focus {
          border-color: $primary-color;
          background: $card-background;
        }
        
        &.is-disabled {
          background: $disabled-background;
          border-color: $disabled-border;
        }
      }
      
      .el-input__inner {
        color: $text-primary;
        font-weight: 500;
        
        &::placeholder {
          color: $text-placeholder;
        }
        
        &:disabled {
          color: $text-disabled;
        }
      }
      
      .el-input__prefix {
        color: $primary-color;
      }
    }
    
    .el-select {
      .el-input__wrapper {
        background: $input-background;
        border: 2px solid $border-color;
        border-radius: 8px;
        
        &:hover {
          border-color: $primary-color;
        }
        
        &.is-focus {
          border-color: $primary-color;
        }
      }
    }
    
    .el-date-editor {
      .el-input__wrapper {
        background: $input-background;
        border: 2px solid $border-color;
        border-radius: 8px;
        
        &:hover {
          border-color: $primary-color;
        }
        
        &.is-focus {
          border-color: $primary-color;
        }
      }
    }
    
    .el-textarea {
      .el-textarea__inner {
        background: $input-background;
        border: 2px solid $border-color;
        border-radius: 8px;
        color: $text-primary;
        
        &:hover {
          border-color: $primary-color;
        }
        
        &:focus {
          border-color: $primary-color;
        }
        
        &::placeholder {
          color: $text-placeholder;
        }
      }
    }
  }
  
  .form-actions {
    display: flex;
    gap: 1rem;
    justify-content: flex-end;
    padding-top: 1rem;
    border-top: 1px solid $border-color;
    
    .el-button {
      padding: 0.75rem 2rem;
      border-radius: 8px;
      font-weight: 600;
      
      &.save-btn {
        background: $primary-color;
        border: none;
        color: white;
        
        &:hover {
          background: $primary-hover;
        }
      }
      
      &.cancel-btn {
        background: $button-secondary;
        border: 2px solid $border-color;
        color: $text-secondary;
        
        &:hover {
          background: $button-secondary-hover;
          border-color: $border-hover;
          color: $text-primary;
        }
      }
    }
  }
}

.password-form {
  :deep(.el-form-item) {
    margin-bottom: 1.5rem;
    
    .el-form-item__label {
      color: $text-primary;
      font-weight: 600;
      font-size: 0.9rem;
      margin-bottom: 0.5rem;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
    
    .el-input {
      .el-input__wrapper {
        background: $input-background;
        border: 2px solid $border-color;
        border-radius: 8px;
        padding: 0.75rem 1rem;
        box-shadow: none;
        
        &:hover {
          border-color: $danger-color;
          background: $card-background;
        }
        
        &.is-focus {
          border-color: $danger-color;
          background: $card-background;
        }
      }
      
      .el-input__inner {
        color: $text-primary;
        font-weight: 500;
        
        &::placeholder {
          color: $text-placeholder;
        }
      }
      
      .el-input__prefix {
        color: $danger-color;
      }
    }
  }
  
  .form-actions {
    display: flex;
    gap: 1rem;
    justify-content: flex-end;
    padding-top: 1rem;
    border-top: 1px solid $border-color;
    
    .el-button {
      padding: 0.75rem 2rem;
      border-radius: 8px;
      font-weight: 600;
      
      &.update-btn {
        background: $danger-color;
        border: none;
        color: white;
        
        &:hover {
          background: $danger-hover;
        }
      }
      
      &.reset-btn {
        background: $button-secondary;
        border: 2px solid $border-color;
        color: $text-secondary;
        
        &:hover {
          background: $button-secondary-hover;
          border-color: $border-hover;
          color: $text-primary;
        }
      }
    }
  }
}

.avatar-upload {
  .upload-tabs {
    margin-bottom: 1.5rem;
    
    :deep(.el-tabs__header) {
      margin: 0 0 1rem 0;
    }
    
    :deep(.el-tabs__nav-wrap::after) {
      background-color: $border-color;
    }
    
    :deep(.el-tabs__active-bar) {
      background-color: $primary-color;
    }
    
    :deep(.el-tabs__item) {
      color: $text-secondary;
      
      &.is-active {
        color: $primary-color;
      }
    }
  }
  
  .upload-area {
    margin-bottom: 1.5rem;
    
    .avatar-input {
      :deep(.el-input-group__prepend) {
        background: $primary-color;
        border: none;
        color: white;
        border-radius: 8px 0 0 8px;
      }
      
      :deep(.el-input__wrapper) {
        background: $input-background;
        border: 2px solid $border-color;
        border-left: none;
        border-radius: 0 8px 8px 0;
        
        &:hover {
          border-color: $primary-color;
          background: $card-background;
        }
        
        &.is-focus {
          border-color: $primary-color;
          background: $card-background;
        }
      }
      
      :deep(.el-input__inner) {
        color: $text-primary;
        font-weight: 500;
        
        &::placeholder {
          color: $text-placeholder;
        }
      }
    }
    
    .avatar-uploader {
      width: 100%;
      
      :deep(.el-upload) {
        width: 100%;
      }
      
      .upload-dragger {
        width: 100%;
        height: 120px;
        border: 2px dashed $border-color;
        border-radius: 8px;
        background: $input-background;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: all 0.3s ease;
        
        &:hover {
          border-color: $primary-color;
          background: $card-background;
        }
        
        .upload-icon {
          font-size: 32px;
          color: $text-placeholder;
          margin-bottom: 8px;
        }
        
        .upload-text {
          text-align: center;
          
          p {
            margin: 0;
            color: $text-primary;
            
            &.upload-hint {
              font-size: 0.75rem;
              color: $text-placeholder;
              margin-top: 4px;
            }
          }
        }
      }
    }
    
    .upload-tips {
      margin-top: 0.75rem;
      
      p {
        font-size: 0.85rem;
        color: $text-secondary;
        margin: 0;
        text-align: center;
      }
    }
  }
  
  .avatar-preview {
    text-align: center;
    padding: 1.5rem;
    background: rgba(60, 60, 60, 0.6);
    border-radius: 8px;
    border: 2px dashed rgba(255, 255, 255, 0.3);
    
    h4 {
      font-size: 1rem;
      font-weight: 600;
      color: $text-primary;
      margin: 0 0 1rem 0;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
    
    .preview-container {
      .preview-avatar {
        border: 4px solid #fff;
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
      }
    }
  }
}

.avatar-dialog {
  :deep(.el-dialog) {
    background: rgba(42, 42, 42, 0.95);
    border-radius: 16px;
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.5);
    
    .el-dialog__header {
      background: $header-background;
      border-bottom: 1px solid $border-color;
      border-radius: 16px 16px 0 0;
      padding: 1.5rem 2rem;
      
      .el-dialog__title {
        color: $text-primary;
        font-weight: 700;
        font-size: 1.25rem;
      }
    }
    
    .el-dialog__body {
      padding: 2rem;
      color: $text-primary;
    }
    
    .el-dialog__footer {
      border-top: 1px solid $border-color;
      padding: 1.5rem 2rem;
      
      .dialog-footer {
        display: flex;
        gap: 1rem;
        justify-content: flex-end;
        
        .el-button {
          padding: 0.75rem 2rem;
          border-radius: 8px;
          font-weight: 600;
          
          &.confirm-btn {
            background: $primary-color;
            border: none;
            color: white;
            
            &:hover {
              background: $primary-hover;
            }
          }
          
          &.cancel-btn {
            background: $button-secondary;
            border: 2px solid $border-color;
            color: $text-secondary;
            
            &:hover {
              background: $button-secondary-hover;
              border-color: $border-hover;
              color: $text-primary;
            }
          }
        }
      }
    }
  }
}

@media (max-width: 1024px) {
  .profile-content {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  
  .stats-card .card-content .stats-grid {
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  }
}

@media (max-width: 768px) {
  .profile-wrapper {
    padding: 1rem;
  }
  
  .profile-hero {
    padding: 2rem;
    
    .hero-content {
      flex-direction: column;
      text-align: center;
      gap: 2rem;
    }
    
    .user-details {
      .username {
        font-size: 2rem;
      }
      
      .user-stats {
        justify-content: center;
      }
    }
  }
  
  .info-form .form-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .card-header {
    padding: 1rem 1.5rem !important;
    
    .header-left {
      .card-icon {
        width: 40px;
        height: 40px;
        font-size: 18px;
      }
      
      .header-text h3 {
        font-size: 1.1rem;
      }
    }
  }
  
  .card-content {
    padding: 1.5rem !important;
  }
  
  .form-actions,
  .notification-actions {
    flex-direction: column;
    gap: 0.75rem;
    
    .el-button {
      width: 100%;
    }
  }
  
  .notification-settings .setting-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
    
    .setting-info {
      margin-bottom: 0;
    }
  }
  
  .stats-card .card-content .stats-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .profile-hero {
    padding: 1.5rem;
    
    .user-details {
      .username {
        font-size: 1.75rem;
      }
      
      .user-stats {
        flex-direction: column;
        gap: 1rem;
        
        .stat-item {
          .stat-number {
            font-size: 1.5rem;
          }
        }
      }
    }
  }
  
  .avatar-dialog :deep(.el-dialog) {
    width: 90% !important;
    margin: 5vh auto;
  }
}
</style>
