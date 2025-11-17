<template>
  <el-header class="navigation">
    <div class="nav-container">
      <!-- Logo和标题 -->
      <router-link to="/" class="brand-link">
        <el-avatar class="brand-icon" size="large" style="background: #fff;">
          <img src="../assets/goodlab.png" alt="GoodLab Logo"></img>
        </el-avatar>
        <div class="brand-text">
          <div class="brand-title">《人工智能概论》数字化教材平台</div>
          <div class="brand-subtitle">人工智能概论</div>
        </div>
      </router-link>
      
      <!-- 主要菜单 -->
      <el-menu
        :default-active="activePath"
        mode="horizontal"
        background-color="transparent"
        text-color="#b0b3b8"
        active-text-color="#b0b3b8"
        class="nav-menu"
        router
      >
        <el-menu-item index="/">
          <el-icon><House /></el-icon>
          首页
        </el-menu-item>
        <el-menu-item index="/chapters">
          <el-icon><Notebook /></el-icon>
          章节
        </el-menu-item>

        <el-menu-item index="/quiz">
          <el-icon><QuestionFilled /></el-icon>
          题目自测
        </el-menu-item>
        <el-menu-item index="/network-training" class="special-menu-item">
          <el-icon><Connection /></el-icon>
          <span class="menu-text">神经网络实验室</span>
          <el-tag size="small" type="success" class="menu-tag">NEW</el-tag>
        </el-menu-item>
      </el-menu>
      
      <!-- 用户菜单 -->
      <div class="user-section">
        <!-- 未登录状态 -->
        <div v-if="!isLoggedIn" class="auth-buttons">
          <el-button type="primary" @click="goToPage('/login')">
            登录
          </el-button>
        </div>

        <!-- 已登录状态 -->
        <el-dropdown v-else class="user-menu" trigger="click">
          <div class="user-info">
            <el-avatar :size="36" :src="currentUser?.userPic || ''">
              <el-icon><User /></el-icon>
            </el-avatar>
            <span class="username">{{ currentUser?.nickname || currentUser?.username || '用户' }}</span>
            <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleProfileClick">
                <el-icon style="margin-right: 12px;"><User /></el-icon>
                个人中心
              </el-dropdown-item>
              <div class="dropdown-divider"></div>
              <el-dropdown-item @click="handleLogout">
                <el-icon style="margin-right: 12px;"><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

      <!-- 更多菜单 -->
      <el-dropdown class="more-menu" trigger="click">
        <el-button class="more-btn" link>
          <el-icon><MoreFilled /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="goToPage('/stats')">
              <el-icon style="margin-right: 12px;"><DataAnalysis /></el-icon>
              统计
            </el-dropdown-item>
            <el-dropdown-item @click="goToPage('/feedback')">
              <el-icon style="margin-right: 12px;"><QuestionFilled /></el-icon>
              反馈
            </el-dropdown-item>
            <div class="dropdown-divider"></div>
            <!-- 只有管理员才显示管理菜单 -->
            <template v-if="currentUser?.role === 'admin'">
              <el-dropdown-item @click="goToPage('/admin')">
                <el-icon style="margin-right: 12px;"><Setting /></el-icon>
                管理
              </el-dropdown-item>
              <div class="dropdown-divider"></div>
            </template>
            <el-dropdown-item @click="goToPage('/about')">
              <el-icon style="margin-right: 12px;"><InfoFilled /></el-icon>
              关于
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-header>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { computed, onMounted, onUnmounted, watch } from 'vue'
import { House, Notebook, InfoFilled, TrendCharts, Connection, DataAnalysis, Setting, MoreFilled, User, ArrowDown, SwitchButton, QuestionFilled } from '@element-plus/icons-vue'
import { useAuth } from '@/composables/useAuth'

const route = useRoute()
const router = useRouter()
const { isLoggedIn, currentUser, logout, checkAuthStatus, forceRefreshAuth } = useAuth()

const activePath = computed(() => route.path)

const goToPage = async (path) => {
  try {
    await router.push(path)
  } catch (error) {
    // Navigation: 跳转失败
  }
}

const handleProfileClick = async () => {
  // 直接从localStorage获取token，不依赖useAuth的响应式状态
  const localToken = localStorage.getItem('token')
  
  if (!localToken) {
    logout()
    router.push('/login')
    return
  }
  
  // 直接跳转，让路由守卫和页面自己处理认证
  goToPage('/profile')
}

const handleLogout = () => {
  logout()
  router.push('/')
}

// 监听localStorage变化，确保状态同步
const handleStorageChange = async (event) => {
  // 只处理token相关的变化，避免过度响应
  if (event.key === 'token' || event.key === 'userInfo') {
    // 延迟一下，确保localStorage操作完成
    setTimeout(async () => {
      // 只有在有token时才刷新认证状态
      if (localStorage.getItem('token')) {
        await forceRefreshAuth()
      }
    }, 100)
  }
}

// 初始化时检查认证状态
onMounted(async () => {
  // 监听localStorage变化
  window.addEventListener('storage', handleStorageChange)
  
  // 延迟检查认证状态，避免与main.js的初始化冲突
  setTimeout(async () => {
    try {
      // 只有在有token的情况下才进行认证相关操作
      if (localStorage.getItem('token')) {
        // 先强制刷新认证状态
        await forceRefreshAuth()
        await checkAuthStatus()
      }
    } catch (error) {
      // 认证状态检查失败
    }
  }, 100) // 延迟100ms避免并发请求
  
  // 组件卸载时清理
  onUnmounted(() => {
    window.removeEventListener('storage', handleStorageChange)
  })
})
</script>

<style scoped lang="scss">

.navigation {
  height: 80px;
  background: linear-gradient(135deg, var(--primary-color, #18191a) 0%, var(--secondary-color, #23272e) 100%);
  backdrop-filter: blur(20px);
  /* remove global navbar shadow to avoid layered effect */
  box-shadow: none;
  position: sticky;
  top: 0;
  z-index: 1000;
  padding: 0;
  border-bottom: 1px solid var(--border-color, rgba(57, 59, 64, 0.2));
}

.nav-container {
  max-width: 1600px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
  padding: 0 2rem;
  gap: 1.5rem;
}

.brand-link {
  display: flex;
  align-items: center;
  gap: 1rem;
  text-decoration: none;
  color: inherit;
  min-width: 180px; // 进一步减少品牌区域宽度
  flex-shrink: 0; // 防止被压缩
}

.brand-icon {
  font-size: 2.5rem;
  background: #fff;
  box-shadow: 0 2px 8px rgba(var(--text-secondary-color-rgb, 176, 179, 184), 0.10);
  border-radius: 50%;
  padding: 0;
  flex-shrink: 0; // 防止图标被压缩
  overflow: hidden;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    object-position: center;
  }
}

.brand-text {
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 0; // 允许文本换行
}

.brand-title {
  font-size: 1.22rem;
  font-weight: 800;
  color: var(--text-color, #f5f6fa);
  line-height: 1.1;
  letter-spacing: 1.2px;
  white-space: nowrap;
  
  /* 深色主题下使用渐变效果 */
  .dark-theme & {
    background: linear-gradient(135deg, var(--accent-color, #3b82f6) 0%, #60a5fa 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
  
  /* 浅色主题下使用深色文字 */
  .light-theme & {
    color: var(--text-color, #1e293b);
    background: none;
    -webkit-background-clip: unset;
    -webkit-text-fill-color: unset;
    background-clip: unset;
  }
}

.brand-subtitle {
  font-size: 0.9rem;
  color: var(--text-color, #f5f6fa);
  line-height: 1.1;
  font-weight: 500;
  white-space: nowrap;
  opacity: 0.9;
  
  /* 浅色主题下调整颜色 */
  .light-theme & {
    color: var(--text-secondary-color, #475569);
    opacity: 0.8;
  }
}

.nav-menu {
  flex: 1;
  min-width: 0; // 允许菜单收缩
  justify-content: center; // 菜单居中
  background: transparent;
  border-bottom: none;
  margin: 0 1rem; // 减少菜单左右空间
  overflow: visible; // 确保菜单项不被隐藏
}
.el-menu--horizontal {
  background: transparent !important;
  border-bottom: none !important;
  box-shadow: none !important;
}
.el-menu--horizontal > .el-menu-item {
  font-size: 1rem;
  font-weight: 600;
  border-bottom: none !important;
  background: transparent !important;
  border-radius: 12px;
  margin: 0 0.5rem;
  padding: 0.8rem 1.2rem;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  white-space: nowrap;
  color: var(--text-color, #f5f6fa);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, var(--accent-color, #3b82f6) 0%, #60a5fa 100%);
    opacity: 0;
    transition: opacity 0.3s ease;
    border-radius: 12px;
  }
  
  &:hover::before {
    opacity: 0.1;
  }
}
.el-menu--horizontal > .el-menu-item .el-icon {
  font-size: 1.4rem;
  margin-right: 0.3rem;
  color: var(--accent-color, #3b82f6);
  transition: all 0.3s ease;
  z-index: 1;
  position: relative;
}
.el-menu--horizontal > .el-menu-item.is-active,
.el-menu--horizontal > .el-menu-item:hover {
  background: rgba(var(--accent-color-rgb, 59, 130, 246), 0.1) !important;
  color: var(--accent-color, #3b82f6) !important;
  box-shadow: 0 4px 16px rgba(var(--accent-color-rgb, 59, 130, 246), 0.15);
  transform: translateY(-1px);
  
  .el-icon {
    color: var(--accent-color, #3b82f6);
    transform: scale(1.1);
  }
}
.el-menu--horizontal > .el-menu-item.is-active {
  font-weight: 700;
  letter-spacing: 0.5px;
  background: linear-gradient(135deg, rgba(var(--accent-color-rgb, 59, 130, 246), 0.15) 0%, rgba(var(--accent-color-light-rgb, 96, 165, 250), 0.1) 100%) !important;
  border: 1px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.2);
  
  &::before {
    opacity: 0.15;
  }
}
.el-menu--horizontal > .el-menu-item:focus {
  outline: none !important;
  box-shadow: none !important;
}



// 下拉菜单样式 - 现代简洁设计
:deep(.el-dropdown-menu) {
  background: var(--dropdown-bg, rgba(26, 26, 46, 0.95)) !important;
  border: 1px solid var(--dropdown-border, rgba(0, 191, 255, 0.2)) !important;
  border-radius: 12px !important;
  box-shadow: var(--dropdown-shadow, 0 12px 32px rgba(0, 0, 0, 0.3), 0 4px 12px rgba(0, 0, 0, 0.1)) !important;
  padding: 0.75rem 0 !important;
  min-width: 140px !important;
  z-index: 2000 !important;
  backdrop-filter: blur(12px) !important;
  position: relative !important;
}

// 强制覆盖Element Plus默认样式
.el-dropdown-menu {
  background: var(--dropdown-bg, rgba(26, 26, 46, 0.95)) !important;
  border: 1px solid var(--dropdown-border, rgba(0, 191, 255, 0.2)) !important;
  border-radius: 12px !important;
  box-shadow: var(--dropdown-shadow, 0 12px 32px rgba(0, 0, 0, 0.3), 0 4px 12px rgba(0, 0, 0, 0.1)) !important;
  padding: 0.75rem 0 !important;
  min-width: 140px !important;
  z-index: 2000 !important;
  backdrop-filter: blur(12px) !important;
}

.el-dropdown-menu__item {
  color: var(--dropdown-item-color, #f5f6fa) !important;
  font-size: 0.9rem !important;
  font-weight: 500 !important;
  padding: 0.75rem 1.25rem !important;
  transition: all 0.2s ease !important;
  cursor: pointer !important;
  white-space: nowrap !important;
  margin: 0 0.5rem !important;
  border-radius: 8px !important;
  display: flex !important;
  align-items: center !important;
  
  &:hover {
    background: var(--dropdown-item-hover-bg, rgba(0, 191, 255, 0.1)) !important;
    color: var(--dropdown-item-hover-color, #00bfff) !important;
    transform: translateX(4px) !important;
  }
  
  &:active {
    transform: translateX(2px) !important;
    transition: all 0.1s ease !important;
  }
}

:deep(.el-dropdown-menu__item) {
  color: var(--dropdown-item-color, #f5f6fa) !important;
  font-size: 0.9rem !important;
  font-weight: 500 !important;
  padding: 0.75rem 1.25rem !important;
  transition: all 0.2s ease !important;
  cursor: pointer !important;
  white-space: nowrap !important;
  position: relative !important;
  margin: 0 0.5rem !important;
  border-radius: 8px !important;
  display: flex !important;
  align-items: center !important;
  
  &:hover {
    background: var(--dropdown-item-hover-bg, rgba(0, 191, 255, 0.1)) !important;
    color: var(--dropdown-item-hover-color, #00bfff) !important;
    transform: translateX(4px) !important;
    
    .el-icon {
      color: var(--dropdown-item-hover-color, #00bfff) !important;
      transform: scale(1.05) !important;
    }
  }
  
  &:active {
    transform: translateX(2px) !important;
    transition: all 0.1s ease !important;
  }
  
  .el-icon {
    transition: all 0.2s ease !important;
    margin-right: 10px !important;
    font-size: 1rem !important;
  }
}

// 分隔线样式
.dropdown-divider {
  height: 1px;
  background: var(--dropdown-divider-bg, rgba(0, 191, 255, 0.1));
  margin: 0.4rem 1rem;
  border-radius: 1px;
}

// 确保下拉菜单可见
:deep(.el-popper) {
  z-index: 2000 !important;
  animation: dropdownFadeIn 0.2s ease !important;
}

@keyframes dropdownFadeIn {
  from {
    opacity: 0;
    transform: translateY(-8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 用户区域样式
.user-section {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex-shrink: 0;
}

.auth-buttons {
  .el-button {
    font-weight: 600;
    border-radius: 8px;
    padding: 0.6rem 1.2rem;
  }
}

.user-menu {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: all 0.2s ease;

  &:hover {
    background: rgba(var(--accent-color, #b0b3b8), 0.1);
  }

  .username {
    color: var(--text-color, #f5f6fa);
    font-weight: 500;
    font-size: 0.9rem;
  }

  .dropdown-icon {
    font-size: 0.8rem;
    color: var(--text-secondary-color, #b0b3b8);
    transition: transform 0.2s ease;
  }

  &:hover .dropdown-icon {
    transform: rotate(180deg);
  }
}

// 更多菜单按钮样式优化
.more-menu {
  flex-shrink: 0; // 防止被压缩
  margin-left: 0.5rem; // 减少间距
}

.more-btn {
  background: transparent;
  border: 1px solid rgba(var(--accent-color, #b0b3b8), 0.2);
  color: var(--accent-color, #b0b3b8);
  font-size: 1.2rem;
  padding: 0.6rem;
  border-radius: 8px;
  transition: all 0.2s ease;
  min-width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  
  &:hover {
    background: rgba(var(--accent-color, #b0b3b8), 0.1);
    color: var(--accent-color, #00bfff);
    border-color: rgba(var(--accent-color, #b0b3b8), 0.3);
    transform: translateY(-1px);
  }
  
  &:active {
    transform: translateY(0);
  }
  
  &:focus {
    outline: none;
    box-shadow: 0 0 0 2px rgba(var(--accent-color, #b0b3b8), 0.2);
  }
}

// 特殊菜单项样式 - 优化显示
.special-menu-item {
  position: relative;
  background: rgba(var(--accent-color, #b0b3b8), 0.05) !important;
  border-radius: var(--border-radius, 10px) !important;
  margin: 0 0.3rem !important;
  transition: all 0.2s ease !important;
  min-width: 0 !important;
  flex-shrink: 0 !important;

  &:hover {
    background: rgba(var(--accent-color, #b0b3b8), 0.1) !important;
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(var(--accent-color, #b0b3b8), 0.2) !important;
  }

  .menu-text {
    margin-right: 6px;
    white-space: nowrap;
  }

  .menu-tag {
    font-size: 9px;
    padding: 1px 4px;
    border-radius: 6px;
    background: var(--success-color, #4caf50);
    border: none;
    color: white;
    font-weight: 600;
    animation: pulse 2s ease-in-out infinite alternate;
  }

  &.is-active {
    background: rgba(var(--accent-color, #b0b3b8), 0.15) !important;

    .menu-tag {
      background: var(--error-color, #f44336);
    }
  }
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(39, 174, 96, 0.7);
  }
  70% {
    box-shadow: 0 0 0 4px rgba(39, 174, 96, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(39, 174, 96, 0);
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .nav-container {
    max-width: 100%;
    padding: 0 1rem;
  }
  
  .brand-link {
    min-width: 280px;
  }
  
  .brand-title {
    font-size: 1.1rem;
  }
  
  .brand-subtitle {
    font-size: 0.85rem;
  }
}

@media (max-width: 768px) {
  .nav-container {
    padding: 0 0.5rem;
  }
  
  .brand-link {
    min-width: 200px;
    gap: 0.8rem;
  }
  
  .brand-title {
    font-size: 1rem;
  }
  
  .brand-subtitle {
    font-size: 0.8rem;
  }
  
  .nav-menu {
    margin: 0 1rem;
  }
  
  .el-menu--horizontal > .el-menu-item {
    font-size: 1rem;
    padding: 0.7rem 1.2rem;
    margin: 0 0.5rem;
  }
}
</style>