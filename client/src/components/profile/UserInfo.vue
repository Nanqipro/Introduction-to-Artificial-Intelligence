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
      <div class="edit-switch">
        <el-switch
          :model-value="editMode"
          active-text="编辑"
          inactive-text="查看"
          @update:model-value="$emit('update:editMode', $event)"
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
import { User, UserFilled, Message, Phone, School, Reading, Check, Close } from '@element-plus/icons-vue'
import { ref } from 'vue'

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

const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ]
}

const handleSave = async () => {
  try {
    await formRef.value.validate()
    emit('save', props.formData)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const handleCancel = () => {
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
    
    .edit-switch {
      :deep(.el-switch__label) {
        color: var(--text-secondary-color);
        font-weight: 500;
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
      
      .form-actions {
        display: flex;
        gap: 1rem;
        justify-content: flex-end;
        
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
      
      .edit-switch :deep(.el-switch__label) {
        color: rgba(255, 255, 255, 0.7);
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
