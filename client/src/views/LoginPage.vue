<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <el-avatar class="brand-icon" size="large">
          <img :src="logoUrl" alt="logo">
        </el-avatar>
        <h2 class="login-title">学生登录</h2>
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
            placeholder="请输入学号"
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

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            native-type="submit"
          >
            登录
          </el-button>
        </el-form-item>
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
const { login } = useAuth()
const logoUrl = logoPng

// 表单状态
const loading = ref(false)
const formRef = ref()

// 表单数据
const formData = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const formRules = computed(() => ({
  username: [
    { required: true, message: '请输入学号或管理员账号', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (!value) {
          callback()
          return
        }
        // 允许管理员账号或10-12位数字学号
        const isAdmin = value === 'goodlabAdmin'
        const isStudentId = /^\d{10,12}$/.test(value)
        
        if (!isAdmin && !isStudentId) {
          callback(new Error('请输入10-12位数字学号或管理员账号'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (!value) {
          callback()
          return
        }
        
        // 获取当前用户名
        const username = formData.username
        
        // 如果是管理员账号，只需要最少4位密码
        if (username === 'goodlabAdmin') {
          if (value.length < 4) {
            callback(new Error('密码长度不能少于4位'))
          } else {
            callback()
          }
          return
        }
        
        // 对于普通用户，执行强密码验证
        if (value.length < 8) {
          callback(new Error('密码长度不能少于8位'))
          return
        }
        
        // 强密码验证：至少包含数字、字母和特殊字符中的两种
        const hasNumber = /\d/.test(value)
        const hasLetter = /[a-zA-Z]/.test(value)
        const hasSpecial = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?@]/.test(value)
        
        const typeCount = [hasNumber, hasLetter, hasSpecial].filter(Boolean).length
        
        if (typeCount < 2) {
          callback(new Error('密码必须包含数字、字母、特殊字符中的至少两种'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ]
}))

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    // 开始表单验证
    await formRef.value.validate()
    loading.value = true

    // 执行登录逻辑
    const result = await login({
      username: formData.username,
      password: formData.password
    })

    if (result.success) {
      // 登录成功，等待一下确保token设置完成
      await nextTick()
      
      // 尝试获取用户信息
      const { fetchUserInfo } = useAuth()
      try {
        await fetchUserInfo()
      } catch (error) {
        console.log('登录后获取用户信息失败，但不影响登录流程')
      }
      
      // 登录成功，准备跳转到个人中心页面
      try {
        await router.push('/profile')
        // 成功跳转到个人中心页面
      } catch (error) {
        // 跳转到个人中心页面失败
        // 如果跳转失败，尝试跳转到首页
        router.push('/')
      }
    } else {
      ElMessage.error(result.message || '登录失败，请检查学号和密码')
    }
  } catch (error) {
    // 表单验证失败
    const title = '输入格式错误'
    const message = `请检查输入格式：\n• 学号：10-12位数字\n• 密码：至少8位，包含数字、字母、特殊字符中的至少两种`
    try {
      await ElMessageBox.alert(message, title, { type: 'warning', confirmButtonText: '知道了' })
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
  
  .login-hint {
    color: var(--text-secondary-color, #b0b3b8);
    font-size: 0.85rem;
    line-height: 1.4;
    opacity: 0.8;
    
    .password-requirement {
      color: var(--primary-color, #4a90e2);
      font-weight: 500;
      margin-top: 0.5rem;
      opacity: 1;
    }
    
    .admin-hint {
      color: #e74c3c;
      font-weight: 600;
      margin-top: 0.5rem;
      opacity: 1;
      background: rgba(231, 76, 60, 0.1);
      padding: 0.3rem 0.8rem;
      border-radius: 4px;
      display: inline-block;
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
