<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <el-avatar class="brand-icon" size="large">
          <img :src="logoUrl" alt="logo">
        </el-avatar>
        <h2 class="login-title">{{ isLogin ? '用户登录' : '用户注册' }}</h2>
        <p class="login-subtitle">人工智能概论与应用数字化教材平台</p>
      </div>

      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        class="login-form"
        @submit.prevent="handleSubmit"
      >
        <el-form-item prop="username">
          <el-input
            v-model="formData.username"
            placeholder="请输入用户名"
            size="large"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="formData.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            clearable
          />
        </el-form-item>

        <el-form-item v-if="!isLogin" prop="confirmPassword">
          <el-input
            v-model="formData.confirmPassword"
            type="password"
            placeholder="请确认密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            clearable
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            native-type="submit"
          >
            {{ isLogin ? '登录' : '注册' }}
          </el-button>
        </el-form-item>

        <div class="form-footer">
          <el-button type="text" @click="toggleMode">
            {{ isLogin ? '没有账号？立即注册' : '已有账号？立即登录' }}
          </el-button>
          
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, nextTick } from 'vue'
import logoPng from '@/assets/Nanchang_University_logo.png'
import { useRouter } from 'vue-router'
import { useAuth } from '@/composables/useAuth'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const { login, register } = useAuth()
const logoUrl = logoPng

// 表单状态
const isLogin = ref(true)
const loading = ref(false)
const formRef = ref()

// 表单数据
const formData = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

// 表单验证规则
const formRules = computed(() => ({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '用户名长度为4-16位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 4, max: 16, message: '密码长度为4-16位', trigger: 'blur' }
  ],
  confirmPassword: isLogin.value ? [] : [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== formData.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}))

// 切换登录/注册模式
const toggleMode = () => {
  isLogin.value = !isLogin.value
  // 清空表单
  formData.username = ''
  formData.password = ''
  formData.confirmPassword = ''
  // 清除验证状态
  nextTick(() => {
    formRef.value?.clearValidate()
    console.log('切换模式:', isLogin.value ? '登录' : '注册')
    console.log('当前验证规则:', formRules.value)
  })
}

// 

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    console.log('开始表单验证，当前模式:', isLogin.value ? '登录' : '注册')
    console.log('表单数据:', formData)
    console.log('验证规则:', formRules.value)
    
    await formRef.value.validate()
    loading.value = true

    if (isLogin.value) {
      // 登录
      console.log('执行登录逻辑')
      const result = await login({
        username: formData.username,
        password: formData.password
      })

      if (result.success) {
        console.log('✅ 登录成功，准备跳转到个人中心页面')
        // 登录成功后跳转到个人中心页面
        try {
          await router.push('/profile')
          console.log('✅ 成功跳转到个人中心页面')
        } catch (error) {
          console.error('❌ 跳转到个人中心页面失败:', error)
          // 如果跳转失败，尝试跳转到首页
          router.push('/')
        }
      } else {
        ElMessage.error(result.message || '登录失败，请稍后重试')
      }
    } else {
      // 注册
      console.log('执行注册逻辑')
      const result = await register({
        username: formData.username,
        password: formData.password
      })

      if (result.success) {
        // 注册成功后切换到登录模式
        isLogin.value = true
        formData.password = ''
        formData.confirmPassword = ''
        // 清除验证状态
        nextTick(() => {
          formRef.value?.clearValidate()
        })
      } else {
        ElMessage.error(result.message || '注册失败，请稍后重试')
      }
    }
  } catch (error) {
    console.error('表单验证失败:', error)
    console.error('验证错误详情:', error?.errors || error?.message)
    // 统一格式错误弹窗（中英文）
    const title = '格式错误 / Format Error'
    const message = `请检查输入格式：\n
• 用户名：4-16 位 / Username: 4-16 chars\n
• 密码：4-16 位 / Password: 4-16 chars\n
• 注册需两次密码一致 / Confirm password must match`
    try {
      await ElMessageBox.alert(message, title, { type: 'warning', confirmButtonText: '知道了 / OK' })
    } catch (_) {}
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">

.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--primary-color, #18191a), var(--primary-gradient-end, #232526));
  padding: var(--page-padding, 1rem);
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: radial-gradient(circle at 30% 20%, rgba(176, 179, 184, 0.1) 0%, transparent 50%),
                radial-gradient(circle at 70% 80%, rgba(74, 144, 226, 0.1) 0%, transparent 50%);
    pointer-events: none;
  }
}

.login-card {
  background: var(--card-bg, #292c33);
  border-radius: var(--card-radius, 10px);
  padding: 2rem;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3), 0 8px 32px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 360px;
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  backdrop-filter: blur(10px);
  position: relative;
  z-index: 1;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.05) 0%, rgba(255, 255, 255, 0.02) 100%);
    border-radius: var(--card-radius, 10px);
    pointer-events: none;
  }
}

.login-header {
  text-align: center;
  margin-bottom: var(--spacing-lg, 2rem);
  position: relative;
  z-index: 2;
}

.brand-icon {
  margin-bottom: var(--spacing-md, 1.5rem);
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  border: 2px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
  width: 100px;
  height: 100px;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.3);
    background: rgba(255, 255, 255, 0.95);
  }
  
  img {
    width: 75px;
    height: 75px;
    object-fit: contain;
  }
}

.login-title {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-color, #f5f6fa);
  margin-bottom: var(--spacing-xs, 0.5rem);
  background: linear-gradient(135deg, var(--text-color, #f5f6fa), #d1d3d8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.login-subtitle {
  color: var(--text-secondary-color, #b0b3b8);
  font-size: 0.95rem;
  font-weight: 400;
  opacity: 0.9;
}

.login-form {
  position: relative;
  z-index: 2;
  
  .el-form-item {
    margin-bottom: var(--spacing-md, 1.5rem);
    
    :deep(.el-input) {
      .el-input__wrapper {
        background: var(--form-bg, #23272e);
        border: 1px solid var(--form-border, rgba(57, 59, 64, 0.18));
        border-radius: var(--form-radius, 10px);
        box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;
        
        &:hover {
          border-color: var(--accent-color, #b0b3b8);
        }
        
        &.is-focus {
          border-color: var(--form-focus-border, #4a90e2);
          box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.2), inset 0 2px 4px rgba(0, 0, 0, 0.1);
        }
      }
      
      .el-input__inner {
        color: var(--text-color, #f5f6fa);
        
        &::placeholder {
          color: var(--text-secondary-color, #b0b3b8);
        }
      }
      
      .el-input__prefix-inner,
        .el-input__suffix-inner {
          color: #ffffff !important;
          opacity: 1;
          font-size: 20px;
          filter: drop-shadow(0 0 2px rgba(0, 0, 0, 0.5));
        }
    }
  }
}

.login-btn {
  width: 100%;
  height: 52px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: var(--btn-radius, 12px);
  background: var(--btn-primary-bg, linear-gradient(135deg, #4a90e2, #357abd));
  border: none;
  box-shadow: var(--btn-shadow, 0 4px 16px rgba(74, 144, 226, 0.3));
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    transition: left 0.5s ease;
  }
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(74, 144, 226, 0.4);
    
    &::before {
      left: 100%;
    }
  }
  
  &:active {
    transform: translateY(0);
  }
}

.form-footer {
  text-align: center;
  margin-top: var(--spacing-md, 1.5rem);
  position: relative;
  z-index: 2;
  
  .el-button {
    color: var(--link-color, #667eea);
    font-weight: 500;
    transition: all 0.3s ease;
    
    &:hover {
      color: var(--link-hover-color, #5a67d8);
      transform: translateY(-1px);
    }
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: var(--spacing-sm, 1rem);
  }
  
  .login-card {
    padding: 1.5rem;
    max-width: 100%;
  }
  
  .login-title {
    font-size: 1.6rem;
  }
  
  .login-subtitle {
    font-size: 0.85rem;
  }
}
</style>
