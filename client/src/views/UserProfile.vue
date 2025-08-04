<template>
  <div class="profile-container">
    <div class="profile-header">
      <h1>个人中心</h1>
      <p>管理您的个人信息和账户设置</p>
    </div>

    <div class="profile-content">
      <!-- 用户信息卡片 -->
      <el-card class="profile-card">
        <template #header>
          <div class="card-header">
            <span>基本信息</span>
            <el-button type="primary" @click="editMode = !editMode">
              {{ editMode ? '取消编辑' : '编辑信息' }}
            </el-button>
          </div>
        </template>

        <div class="user-info">
          <!-- 头像部分 -->
          <div class="avatar-section">
            <el-avatar :size="100" :src="userInfo?.userPic || ''" class="user-avatar">
              <el-icon><User /></el-icon>
            </el-avatar>
            <el-button type="text" @click="showAvatarDialog = true">
              更换头像
            </el-button>
          </div>

          <!-- 信息表单 -->
          <el-form
            ref="formRef"
            :model="formData"
            :rules="formRules"
            label-width="80px"
            class="info-form"
            :disabled="!editMode"
          >
            <el-form-item label="用户名" prop="username">
              <el-input v-model="formData.username" disabled />
            </el-form-item>

            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="formData.nickname" placeholder="请输入昵称" />
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-input v-model="formData.email" placeholder="请输入邮箱" />
            </el-form-item>

            <el-form-item label="创建时间">
              <el-input :value="formatDate(userInfo?.createTime)" disabled />
            </el-form-item>

            <el-form-item label="更新时间">
              <el-input :value="formatDate(userInfo?.updateTime)" disabled />
            </el-form-item>

            <el-form-item v-if="editMode">
              <el-button type="primary" @click="handleUpdateInfo" :loading="loading">
                保存修改
              </el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>

      <!-- 密码修改卡片 -->
      <el-card class="profile-card">
        <template #header>
          <span>修改密码</span>
        </template>

        <el-form
          ref="passwordFormRef"
          :model="passwordForm"
          :rules="passwordRules"
          label-width="100px"
          class="password-form"
        >
          <el-form-item label="当前密码" prop="old_pwd">
            <el-input
              v-model="passwordForm.old_pwd"
              type="password"
              placeholder="请输入当前密码"
              show-password
            />
          </el-form-item>

          <el-form-item label="新密码" prop="new_pwd">
            <el-input
              v-model="passwordForm.new_pwd"
              type="password"
              placeholder="请输入新密码"
              show-password
            />
          </el-form-item>

          <el-form-item label="确认密码" prop="re_pwd">
            <el-input
              v-model="passwordForm.re_pwd"
              type="password"
              placeholder="请确认新密码"
              show-password
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleUpdatePassword" :loading="passwordLoading">
              修改密码
            </el-button>
            <el-button @click="resetPasswordForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 头像上传对话框 -->
    <el-dialog v-model="showAvatarDialog" title="更换头像" width="400px">
      <div class="avatar-upload">
        <el-input
          v-model="avatarUrl"
          placeholder="请输入头像URL地址"
          clearable
        />
        <div class="avatar-preview" v-if="avatarUrl">
          <p>预览：</p>
          <el-avatar :size="80" :src="avatarUrl">
            <el-icon><User /></el-icon>
          </el-avatar>
        </div>
      </div>
      <template #footer>
        <el-button @click="showAvatarDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateAvatar" :loading="avatarLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useAuth } from '@/composables/useAuth'
import { User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const { userInfo, fetchUserInfo, updateUserInfo, updateAvatar, updatePassword } = useAuth()

// 表单状态
const editMode = ref(false)
const loading = ref(false)
const passwordLoading = ref(false)
const avatarLoading = ref(false)
const showAvatarDialog = ref(false)
const avatarUrl = ref('')

const formRef = ref()
const passwordFormRef = ref()

// 用户信息表单
const formData = reactive({
  id: '',
  username: '',
  nickname: '',
  email: ''
})

// 密码表单
const passwordForm = reactive({
  old_pwd: '',
  new_pwd: '',
  re_pwd: ''
})

// 表单验证规则
const formRules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 1, max: 10, message: '昵称长度为1-10位', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const passwordRules = {
  old_pwd: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  new_pwd: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 5, max: 16, message: '密码长度为5-16位', trigger: 'blur' }
  ],
  re_pwd: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.new_pwd) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 监听用户信息变化
watch(userInfo, (newUserInfo) => {
  if (newUserInfo) {
    formData.id = newUserInfo.id
    formData.username = newUserInfo.username
    formData.nickname = newUserInfo.nickname || ''
    formData.email = newUserInfo.email || ''
  }
}, { immediate: true })

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN')
}

// 重置表单
const resetForm = () => {
  if (userInfo.value) {
    formData.nickname = userInfo.value.nickname || ''
    formData.email = userInfo.value.email || ''
  }
}

// 重置密码表单
const resetPasswordForm = () => {
  passwordForm.old_pwd = ''
  passwordForm.new_pwd = ''
  passwordForm.re_pwd = ''
  passwordFormRef.value?.clearValidate()
}

// 更新用户信息
const handleUpdateInfo = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    const result = await updateUserInfo(formData)
    if (result.success) {
      editMode.value = false
    }
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    loading.value = false
  }
}

// 更新头像
const handleUpdateAvatar = async () => {
  if (!avatarUrl.value.trim()) {
    ElMessage.warning('请输入头像URL')
    return
  }

  avatarLoading.value = true
  const result = await updateAvatar(avatarUrl.value)
  
  if (result.success) {
    showAvatarDialog.value = false
    avatarUrl.value = ''
  }
  
  avatarLoading.value = false
}

// 更新密码
const handleUpdatePassword = async () => {
  if (!passwordFormRef.value) return

  try {
    await passwordFormRef.value.validate()
    passwordLoading.value = true

    const result = await updatePassword(passwordForm)
    if (result.success) {
      resetPasswordForm()
    }
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    passwordLoading.value = false
  }
}

// 初始化
onMounted(async () => {
  if (!userInfo.value) {
    await fetchUserInfo()
  }
})
</script>

<style scoped lang="scss">
.profile-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

.profile-header {
  text-align: center;
  margin-bottom: 2rem;

  h1 {
    font-size: 2rem;
    color: #333;
    margin-bottom: 0.5rem;
  }

  p {
    color: #666;
    font-size: 1rem;
  }
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.profile-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.user-info {
  display: flex;
  gap: 2rem;
  align-items: flex-start;

  .avatar-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    flex-shrink: 0;

    .user-avatar {
      border: 3px solid #f0f0f0;
    }
  }

  .info-form {
    flex: 1;
  }
}

.password-form {
  max-width: 400px;
}

.avatar-upload {
  .avatar-preview {
    margin-top: 1rem;
    text-align: center;

    p {
      margin-bottom: 0.5rem;
      color: #666;
    }
  }
}

@media (max-width: 768px) {
  .profile-container {
    padding: 1rem;
  }

  .user-info {
    flex-direction: column;
    align-items: center;
  }
}
</style>
