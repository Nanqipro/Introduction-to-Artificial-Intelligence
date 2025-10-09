<template>
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
      <div class="edit-actions">
        <el-button
          v-if="!editMode"
          type="primary"
          @click="$emit('update:editMode', true)"
        >
          <el-icon><Edit /></el-icon>
          编辑资料
        </el-button>
        <el-button
          v-else
          type="info"
          @click="$emit('update:editMode', false)"
        >
          <el-icon><View /></el-icon>
          取消编辑
        </el-button>
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
              disabled
              placeholder="用户名是唯一标识，不可修改"
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
                <el-icon><UserFilled /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="邮箱" prop="email">
            <el-input
              v-model="formData.email"
              :disabled="!editMode"
              placeholder="请输入邮箱"
            >
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="手机号" prop="phone">
            <el-input
              v-model="formData.phone"
              :disabled="!editMode"
              placeholder="请输入手机号"
            >
              <template #prefix>
                <el-icon><Phone /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="学校" prop="school">
            <el-input
              v-model="formData.school"
              :disabled="!editMode"
              placeholder="请输入学校"
            >
              <template #prefix>
                <el-icon><School /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="专业" prop="major">
            <el-input
              v-model="formData.major"
              :disabled="!editMode"
              placeholder="请输入专业"
            >
              <template #prefix>
                <el-icon><Reading /></el-icon>
              </template>
            </el-input>
          </el-form-item>
        </div>
        
        <!-- 密码修改区域 -->
        <div v-if="editMode" class="password-section">
          <div class="section-header">
            <h4>修改密码</h4>
            <p>如需修改密码，请填写以下信息</p>
          </div>
          
          <div class="password-grid">
            <el-form-item label="当前密码" prop="currentPassword">
              <el-input
                v-model="passwordData.currentPassword"
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
                v-model="passwordData.newPassword"
                type="password"
                placeholder="请输入新密码"
                show-password
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item label="确认新密码" prop="confirmPassword">
              <el-input
                v-model="passwordData.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
                show-password
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </div>
        </div>
        
        <div class="form-actions" v-if="editMode">
          <el-button type="primary" @click="handleSave">
            <el-icon><Check /></el-icon>
            保存修改
          </el-button>
          <el-button @click="handleCancel">
            <el-icon><Close /></el-icon>
            取消
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { User, UserFilled, Message, Phone, School, Reading, Check, Close, Lock, Edit, View } from '@element-plus/icons-vue'
import { ref, reactive } from 'vue'

const props = defineProps({
  formData: {
    type: Object,
    default: () => ({})
  },
  editMode: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:editMode', 'save', 'cancel'])

const formRef = ref()

// 密码修改数据
const passwordData = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 重置密码数据
const resetPasswordData = () => {
  passwordData.currentPassword = ''
  passwordData.newPassword = ''
  passwordData.confirmPassword = ''
}

const formRules = {
  // 用户名是唯一标识，不允许修改，因此移除验证规则
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  currentPassword: [
    { 
      validator: (rule, value, callback) => {
        if (passwordData.newPassword && !value) {
          callback(new Error('请输入当前密码'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ],
  newPassword: [
    { 
      validator: (rule, value, callback) => {
        if (passwordData.currentPassword && !value) {
          callback(new Error('请输入新密码'))
        } else if (value && value.length < 6) {
          callback(new Error('新密码长度不能少于6位'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ],
  confirmPassword: [
    { 
      validator: (rule, value, callback) => {
        if (passwordData.newPassword && !value) {
          callback(new Error('请确认新密码'))
        } else if (value && value !== passwordData.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ]
}

const handleSave = async () => {
  try {
    await formRef.value.validate()
    
    // 准备保存数据
    const saveData = {
      userInfo: { ...props.formData },
      passwordChange: null
    }
    
    // 如果有密码修改
    if (passwordData.currentPassword || passwordData.newPassword || passwordData.confirmPassword) {
      if (passwordData.currentPassword && passwordData.newPassword && passwordData.confirmPassword) {
        saveData.passwordChange = {
          currentPassword: passwordData.currentPassword,
          newPassword: passwordData.newPassword,
          confirmPassword: passwordData.confirmPassword // 添加确认密码字段
        }
      }
    }
    
    emit('save', saveData)
  } catch (error) {
    // 表单验证失败
  }
}

const handleCancel = () => {
  resetPasswordData()
  emit('cancel')
}
</script>

<style scoped lang="scss">
.info-card {
  background: var(--card-bg);
  border-radius: 16px;
  padding: 0;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--border-color);
  overflow: hidden;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--box-shadow-light);
  }
  
  .card-header {
    background: var(--card-header-bg);
    padding: 1.5rem 2rem;
    border-bottom: 1px solid var(--card-header-border);
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
        background: var(--card-icon-bg);
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: var(--card-icon-color);
        font-size: 20px;
        backdrop-filter: blur(10px);
      }
      
      .header-text {
        h3 {
          font-size: 1.25rem;
          font-weight: 600;
          color: var(--text-color);
          margin: 0 0 0.25rem 0;
        }
        
        p {
          font-size: 0.9rem;
          color: var(--text-secondary-color);
          margin: 0;
        }
      }
    }
    
    .edit-actions {
      display: flex;
      align-items: center;
      
      .el-button {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        font-size: 0.9rem;
      }
    }
  }
  
  .card-content {
    padding: 2rem;
    background: var(--card-content-bg);
    
    .info-form {
      .form-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 1.5rem;
        margin-bottom: 2rem;
      }
      
      .password-section {
        margin-top: 2rem;
        padding-top: 2rem;
        border-top: 1px solid var(--border-color);
        
        .section-header {
          margin-bottom: 1.5rem;
          
          h4 {
            font-size: 1.1rem;
            font-weight: 600;
            color: var(--text-color);
            margin: 0 0 0.5rem 0;
            display: flex;
            align-items: center;
            gap: 0.5rem;
            
            &::before {
              content: '';
              width: 4px;
              height: 16px;
              background: linear-gradient(135deg, #00bfff, #00ff7f);
              border-radius: 2px;
            }
          }
          
          p {
            font-size: 0.9rem;
            color: var(--text-secondary-color);
            margin: 0;
          }
        }
        
        .password-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
          gap: 1.5rem;
        }
      }
      
      .form-actions {
        display: flex;
        gap: 1rem;
        justify-content: flex-end;
        margin-top: 2rem;
        
        .el-button {
          display: flex;
          align-items: center;
          gap: 0.5rem;
        }
      }
    }
  }
}

/* 深色主题优化 */
html.dark-theme .profile-container {
  .info-card {
    background: rgba(26, 26, 46, 0.8);
    border: 1px solid rgba(0, 191, 255, 0.2);
    box-shadow: 0 8px 32px rgba(0, 191, 255, 0.1);
    backdrop-filter: blur(20px);
    
    &:hover {
      box-shadow: 0 12px 40px rgba(0, 191, 255, 0.15);
    }
    
    .card-header {
      background: linear-gradient(135deg, rgba(0, 191, 255, 0.1), rgba(0, 255, 127, 0.05));
      border-bottom: 1px solid rgba(0, 191, 255, 0.2);
      
      .header-text h3 {
        color: #ffffff;
        text-shadow: 0 2px 8px rgba(0, 191, 255, 0.3);
      }
      
      .header-text p {
        color: rgba(255, 255, 255, 0.7);
      }
      
      .card-icon {
        background: rgba(0, 191, 255, 0.15);
        color: #00bfff;
        box-shadow: 0 4px 16px rgba(0, 191, 255, 0.2);
      }
      
      .edit-actions .el-button {
        border-color: rgba(0, 191, 255, 0.3);
        
        &:hover {
          border-color: rgba(0, 191, 255, 0.6);
          background: rgba(0, 191, 255, 0.1);
        }
      }
    }
    
    .card-content {
      background: rgba(26, 26, 46, 0.8);
      
      .info-form {
        :deep(.el-form-item__label) {
          color: rgba(255, 255, 255, 0.8);
        }
        
        :deep(.el-input__wrapper) {
          background: rgba(0, 191, 255, 0.05);
          border: 1px solid rgba(0, 191, 255, 0.2);
          backdrop-filter: blur(10px);
          
          &:hover {
            border-color: rgba(0, 191, 255, 0.4);
            background: rgba(0, 191, 255, 0.08);
          }
          
          &.is-focus {
            border-color: #00bfff;
            box-shadow: 0 0 0 3px rgba(0, 191, 255, 0.1);
          }
        }
        
        :deep(.el-input__inner) {
          color: #ffffff;
          
          &::placeholder {
            color: rgba(255, 255, 255, 0.5);
          }
        }
        
        :deep(.el-input__prefix) {
          color: #00bfff;
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .info-card .card-content .info-form .form-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .info-card .card-content .info-form .form-actions {
    flex-direction: column;
    gap: 0.75rem;
    
    .el-button {
      width: 100%;
    }
  }
}
</style>
