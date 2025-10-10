<template>
  <div class="first-login-overlay">
    <div class="first-login-modal">
      <div class="modal-header">
        <h2>首次登录密码修改</h2>
        <p class="subtitle">为了您的账户安全，请修改您的初始密码</p>
      </div>
      
      <div class="modal-body">
        <el-form 
          ref="passwordForm" 
          :model="form" 
          :rules="rules" 
          label-width="120px"
          @submit.prevent="handleSubmit"
        >
          <el-form-item label="新密码" prop="newPassword">
            <el-input
              v-model="form.newPassword"
              type="password"
              placeholder="请输入新密码"
              show-password
              autocomplete="new-password"
            />
          </el-form-item>
          
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input
              v-model="form.confirmPassword"
              type="password"
              placeholder="请再次输入新密码"
              show-password
              autocomplete="new-password"
            />
          </el-form-item>
          
          <div class="password-requirements">
            <p class="requirements-title">密码要求：</p>
            <ul>
              <li>至少8个字符</li>
              <li>至少包含数字、字母、特殊字符中的两种</li>
            </ul>
          </div>
          
          <div class="form-actions">
            <el-button 
              type="primary" 
              :loading="loading"
              @click="handleSubmit"
              size="large"
              class="submit-btn"
            >
              {{ loading ? '修改中...' : '确认修改' }}
            </el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuth } from '@/composables/useAuth'

const { firstLoginPasswordChange } = useAuth()

const passwordForm = ref(null)
const loading = ref(false)

const form = reactive({
  newPassword: '',
  confirmPassword: ''
})

// 密码验证规则
const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入新密码'))
    return
  }
  
  if (value.length < 8) {
    callback(new Error('密码长度至少8个字符'))
    return
  }
  
  // 检查是否包含至少两种类型的字符
  const hasNumber = /\d/.test(value)
  const hasLetter = /[a-zA-Z]/.test(value)
  const hasSpecial = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(value)
  
  const typeCount = [hasNumber, hasLetter, hasSpecial].filter(Boolean).length
  
  if (typeCount < 2) {
    callback(new Error('密码必须包含数字、字母、特殊字符中的至少两种'))
    return
  }
  
  callback()
}

const validateConfirmPassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请确认密码'))
    return
  }
  
  if (value !== form.newPassword) {
    callback(new Error('两次输入的密码不一致'))
    return
  }
  
  callback()
}

const rules = {
  newPassword: [
    { validator: validatePassword, trigger: 'blur' }
  ],
  confirmPassword: [
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!passwordForm.value) return
  
  try {
    const valid = await passwordForm.value.validate()
    if (!valid) return
    
    loading.value = true
    
    const result = await firstLoginPasswordChange(form.newPassword)
    
    if (result.success) {
      // 成功后会自动跳转到登录页面
    }
  } catch (error) {
    console.error('密码修改失败:', error)
    ElMessage.error('密码修改失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.first-login-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: var(--modal-overlay-bg, rgba(0, 0, 0, 0.7));
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.first-login-modal {
  background: var(--modal-bg, #ffffff);
  border-radius: var(--modal-radius, 12px);
  padding: 0;
  width: 480px;
  max-width: 90vw;
  box-shadow: var(--box-shadow, 0 20px 40px rgba(0, 0, 0, 0.3));
  animation: modalSlideIn 0.3s ease-out;
  border: 1px solid var(--border-color, rgba(57, 59, 64, 0.18));
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-50px) scale(0.9);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.modal-header {
  background: var(--btn-primary-bg, linear-gradient(135deg, #667eea 0%, #764ba2 100%));
  color: var(--text-color, #ffffff);
  padding: 24px;
  border-radius: var(--modal-radius, 12px) var(--modal-radius, 12px) 0 0;
  text-align: center;
}

.modal-header h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #ffffff;
}

.subtitle {
  margin: 0;
  opacity: 0.9;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.9);
}

.modal-body {
  padding: 32px;
  background: var(--modal-bg, #ffffff);
}

.password-requirements {
  background: var(--form-bg, #f8f9fa);
  border: 1px solid var(--border-color, #e9ecef);
  border-radius: 8px;
  padding: 16px;
  margin: 20px 0;
}

.requirements-title {
  margin: 0 0 8px 0;
  font-weight: 600;
  color: var(--text-color, #495057);
  font-size: 14px;
}

.password-requirements ul {
  margin: 0;
  padding-left: 20px;
  color: var(--text-secondary-color, #6c757d);
  font-size: 13px;
}

.password-requirements li {
  margin-bottom: 4px;
}

.form-actions {
  margin-top: 24px;
  text-align: center;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
}

:deep(.el-form-item__label) {
  font-weight: 600;
  color: var(--text-color, #495057) !important;
}

:deep(.el-input__wrapper) {
  background-color: var(--input-bg, #ffffff);
  border-color: var(--input-border, rgba(0, 191, 255, 0.2));
  box-shadow: none;
}

:deep(.el-input__wrapper.is-focus) {
  border-color: var(--input-focus-border, #00bfff);
  box-shadow: 0 0 0 2px var(--input-focus-shadow, rgba(0, 191, 255, 0.1));
}

:deep(.el-input__inner) {
  height: 44px;
  border-radius: 8px;
  color: var(--text-color, #333333);
  background-color: transparent;
}

:deep(.el-input__inner::placeholder) {
  color: var(--text-muted-color, #9ca3af);
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-button--primary) {
  background: var(--btn-primary-bg, linear-gradient(135deg, #00bfff, #0080ff));
  border-color: var(--accent-color, #00bfff);
}

:deep(.el-button--primary:hover) {
  background: var(--btn-hover-bg, linear-gradient(135deg, #00a6e6, #00bfff));
}

/* 浅色主题特定样式 */
html.light-theme .first-login-modal {
  --modal-overlay-bg: rgba(0, 0, 0, 0.5);
  --modal-bg: #ffffff;
  --form-bg: #f8f9fa;
  --border-color: #dee2e6;
  --text-color: #1a1a1a;
  --text-secondary-color: #6c757d;
  --text-muted-color: #9ca3af;
  --input-bg: #ffffff;
  --input-border: rgba(0, 191, 255, 0.2);
  --input-focus-border: #00bfff;
  --input-focus-shadow: rgba(0, 191, 255, 0.1);
  --btn-primary-bg: linear-gradient(135deg, #00bfff, #0080ff);
  --btn-hover-bg: linear-gradient(135deg, #00a6e6, #00bfff);
  --accent-color: #00bfff;
  --box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

/* 深色主题特定样式 */
html.dark-theme .first-login-modal,
:root .first-login-modal {
  --modal-overlay-bg: rgba(0, 0, 0, 0.8);
  --modal-bg: #1f2937;
  --form-bg: #374151;
  --border-color: #4b5563;
  --text-color: #f8f9fa;
  --text-secondary-color: #d1d5db;
  --text-muted-color: #9ca3af;
  --input-bg: rgba(31, 41, 55, 0.8);
  --input-border: rgba(59, 130, 246, 0.3);
  --input-focus-border: #3b82f6;
  --input-focus-shadow: rgba(59, 130, 246, 0.25);
  --btn-primary-bg: linear-gradient(135deg, #3b82f6, #1d4ed8);
  --btn-hover-bg: linear-gradient(135deg, #60a5fa, #3b82f6);
  --accent-color: #3b82f6;
  --box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
}
</style>