<template>
  <el-header class="navigation">
    <div class="nav-container">
      <!-- Logo和标题 -->
      <router-link to="/" class="brand-link">
        <el-avatar class="brand-icon" size="large" style="background: #fff;">
          <img src="../assets/Nanchang_University_logo.png" alt="logo"></img>
        </el-avatar>
        <div class="brand-text">
          <div class="brand-title">《人工智能概论与应用》数字化教材平台</div>
          <div class="brand-subtitle">人工智能概论与应用</div>
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
        <el-menu-item index="/regression-demo">
          <el-icon><TrendCharts /></el-icon>
          回归演示
        </el-menu-item>
        <el-menu-item index="/network-training" class="special-menu-item">
          <el-icon><Connection /></el-icon>
          <span class="menu-text">神经网络实验室</span>
          <el-tag size="small" type="success" class="menu-tag">NEW</el-tag>
        </el-menu-item>
      </el-menu>
      
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
            <div class="dropdown-divider"></div>
            <el-dropdown-item @click="goToPage('/admin')">
              <el-icon style="margin-right: 12px;"><Setting /></el-icon>
              管理
            </el-dropdown-item>
            <div class="dropdown-divider"></div>
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
import { computed } from 'vue'
import { House, Notebook, InfoFilled, TrendCharts, Connection, DataAnalysis, Setting, MoreFilled } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const activePath = computed(() => route.path)

const goToPage = (path) => {
  router.push(path)
}
</script>

<style scoped lang="scss">

.navigation {
  height: 80px;
  background: rgba(51, 51, 51, 0.92); // 更通透的深灰
  box-shadow: $box-shadow;
  position: sticky;
  top: 0;
  z-index: 1000;
  padding: 0;
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
  box-shadow: 0 2px 8px rgba(176,179,184,0.10);
  border-radius: 50%;
  padding: 0.2rem;
  flex-shrink: 0; // 防止图标被压缩
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
  color: $accent-color;
  line-height: 1.1;
  letter-spacing: 1.2px;
  white-space: nowrap; // 防止标题换行
}

.brand-subtitle {
  font-size: 0.9rem;
  color: $text-secondary-color;
  line-height: 1.1;
  font-weight: 500;
  white-space: nowrap; // 防止副标题换行
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
  font-weight: 700;
  border-bottom: none !important;
  background: transparent !important;
  border-radius: $border-radius;
  margin: 0 0.5rem; // 进一步减少菜单项间距
  padding: 0.7rem 1rem; // 进一步减少内边距
  transition: background 0.18s, color 0.18s, box-shadow 0.18s;
  display: flex;
  align-items: center;
  gap: 0.4rem; // 减少图标和文字间距
  white-space: nowrap; // 防止文字换行
}
.el-menu--horizontal > .el-menu-item .el-icon {
  font-size: 1.45rem;
  margin-right: 0.3rem;
  color: $accent-color;
}
.el-menu--horizontal > .el-menu-item.is-active,
.el-menu--horizontal > .el-menu-item:hover {
  background: rgba(176,179,184,0.15) !important;
  color: $accent-color !important;
  box-shadow: 0 2px 12px rgba(176,179,184,0.10);
}
.el-menu--horizontal > .el-menu-item.is-active {
  font-weight: 900;
  letter-spacing: 1.2px;
}
.el-menu--horizontal > .el-menu-item:focus {
  outline: none !important;
  box-shadow: none !important;
}



// 下拉菜单样式 - 现代简洁设计
:deep(.el-dropdown-menu) {
  background: $card-bg !important;
  border: 1px solid $card-border !important;
  border-radius: 12px !important;
  box-shadow: 
    0 12px 32px rgba(0, 0, 0, 0.3),
    0 4px 12px rgba(0, 0, 0, 0.1) !important;
  padding: 0.75rem 0 !important;
  min-width: 140px !important;
  z-index: 2000 !important;
  backdrop-filter: blur(12px) !important;
  position: relative !important;
}

:deep(.el-dropdown-menu__item) {
  color: $text-color !important;
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
    background: rgba($accent-color, 0.1) !important;
    color: $accent-color-light !important;
    transform: translateX(4px) !important;
    
    .el-icon {
      color: $accent-color-light !important;
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
  background: $divider-bg;
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

// 更多菜单按钮样式优化
.more-menu {
  flex-shrink: 0; // 防止被压缩
  margin-left: 0.5rem; // 减少间距
}

.more-btn {
  background: transparent;
  border: 1px solid rgba($accent-color, 0.2);
  color: $accent-color;
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
    background: rgba($accent-color, 0.1);
    color: $accent-color-light;
    border-color: rgba($accent-color, 0.3);
    transform: translateY(-1px);
  }
  
  &:active {
    transform: translateY(0);
  }
  
  &:focus {
    outline: none;
    box-shadow: 0 0 0 2px rgba($accent-color, 0.2);
  }
}

// 特殊菜单项样式 - 优化显示
.special-menu-item {
  position: relative;
  background: rgba($accent-color, 0.05) !important;
  border-radius: $border-radius !important;
  margin: 0 0.3rem !important;
  transition: all 0.2s ease !important;
  min-width: 0 !important;
  flex-shrink: 0 !important;

  &:hover {
    background: rgba($accent-color, 0.1) !important;
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba($accent-color, 0.2) !important;
  }

  .menu-text {
    margin-right: 6px;
    white-space: nowrap;
  }

  .menu-tag {
    font-size: 9px;
    padding: 1px 4px;
    border-radius: 6px;
    background: $success-color;
    border: none;
    color: white;
    font-weight: 600;
    animation: pulse 2s ease-in-out infinite alternate;
  }

  &.is-active {
    background: rgba($accent-color, 0.15) !important;

    .menu-tag {
      background: $error-color;
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