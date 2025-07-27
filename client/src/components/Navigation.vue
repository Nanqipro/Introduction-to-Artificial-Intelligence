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
      <!-- 菜单 -->
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
        <el-menu-item index="/about">
          <el-icon><InfoFilled /></el-icon>
          关于
        </el-menu-item>
      </el-menu>
    </div>
  </el-header>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { House, Notebook, InfoFilled, TrendCharts, Connection } from '@element-plus/icons-vue'

const route = useRoute()
const activePath = computed(() => route.path)
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
  max-width: 95%;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
}
.brand-link {
  display: flex;
  align-items: center;
  gap: 1.2rem;
  text-decoration: none;
  color: inherit;
}
.brand-icon {
  font-size: 2.5rem;
  background: #fff;
  box-shadow: 0 2px 8px rgba(176,179,184,0.10);
  border-radius: 50%;
  padding: 0.2rem;
}
.brand-text {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.brand-title {
  font-size: 1.22rem;
  font-weight: 800;
  color: $accent-color;
  line-height: 1.1;
  letter-spacing: 1.2px;
}
.brand-subtitle {
  font-size: 0.9rem;
  color: $text-secondary-color;
  line-height: 1.1;
  font-weight: 500;
}
.nav-menu {
  flex: 1;
  justify-content: flex-end;
  background: transparent;
  border-bottom: none;
}
.el-menu--horizontal {
  background: transparent !important;
  border-bottom: none !important;
  box-shadow: none !important;
}
.el-menu--horizontal > .el-menu-item {
  font-size: 1.12rem;
  font-weight: 700;
  border-bottom: none !important;
  background: transparent !important;
  border-radius: $border-radius * 1.5;
  margin: 0 1.1rem;
  padding: 0.9rem 1.6rem;
  transition: background 0.18s, color 0.18s, box-shadow 0.18s;
  display: flex;
  align-items: center;
  gap: 0.7rem;
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

// 特殊菜单项样式
.special-menu-item {
  position: relative;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%) !important;
  border-radius: 8px !important;
  margin: 0 8px !important;
  transition: all 0.3s ease !important;

  &:hover {
    background: linear-gradient(135deg, rgba(102, 126, 234, 0.2) 0%, rgba(118, 75, 162, 0.2) 100%) !important;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3) !important;
  }

  .menu-text {
    margin-right: 8px;
  }

  .menu-tag {
    font-size: 10px;
    padding: 2px 6px;
    border-radius: 10px;
    background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%);
    border: none;
    color: white;
    font-weight: 600;
    animation: pulse 2s ease-in-out infinite alternate;
  }

  &.is-active {
    background: linear-gradient(135deg, rgba(102, 126, 234, 0.3) 0%, rgba(118, 75, 162, 0.3) 100%) !important;

    .menu-tag {
      background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
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
</style>