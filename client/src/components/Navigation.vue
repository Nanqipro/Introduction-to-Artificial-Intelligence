<template>
  <nav class="navigation">
    <div class="nav-container">
      <!-- Logo和标题 -->
      <div class="nav-brand">
        <router-link to="/" class="brand-link">
          <div class="brand-icon">🤖</div>
          <div class="brand-text">
            <div class="brand-title">AI教材平台</div>
            <div class="brand-subtitle">人工智能概论与应用</div>
          </div>
        </router-link>
      </div>

      <!-- 导航菜单 -->
      <div class="nav-menu" :class="{ active: menuOpen }">
        <router-link to="/" class="nav-link" @click="closeMenu">
          <span class="nav-icon">🏠</span>
          <span class="nav-text">首页</span>
        </router-link>
        <router-link to="/chapters" class="nav-link" @click="closeMenu">
          <span class="nav-icon">📚</span>
          <span class="nav-text">章节</span>
        </router-link>
        <router-link to="/about" class="nav-link" @click="closeMenu">
          <span class="nav-icon">ℹ️</span>
          <span class="nav-text">关于</span>
        </router-link>
      </div>

      <!-- 移动端菜单按钮 -->
      <button class="menu-toggle" @click="toggleMenu">
        <span class="hamburger" :class="{ active: menuOpen }"></span>
      </button>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Navigation',
  data() {
    return {
      menuOpen: false
    }
  },
  methods: {
    toggleMenu() {
      this.menuOpen = !this.menuOpen
    },
    closeMenu() {
      this.menuOpen = false
    }
  },
  mounted() {
    // 点击外部关闭菜单
    document.addEventListener('click', (e) => {
      if (!this.$el.contains(e.target)) {
        this.menuOpen = false
      }
    })
  }
}
</script>

<style scoped>
.navigation {
  background: white;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70px;
}

.nav-brand {
  flex-shrink: 0;
}

.brand-link {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  text-decoration: none;
  color: inherit;
}

.brand-icon {
  font-size: 2rem;
}

.brand-text {
  display: flex;
  flex-direction: column;
}

.brand-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  line-height: 1;
}

.brand-subtitle {
  font-size: 0.8rem;
  color: #666;
  line-height: 1;
}

.nav-menu {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  color: #666;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link:hover {
  color: #667eea;
  background: #f8f9ff;
}

.nav-link.router-link-active {
  color: #667eea;
  background: #f8f9ff;
}

.nav-link.router-link-active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 2px;
  background: #667eea;
  border-radius: 1px;
}

.nav-icon {
  font-size: 1.1rem;
}

.nav-text {
  font-size: 0.95rem;
}

.menu-toggle {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 4px;
  transition: background 0.3s ease;
}

.menu-toggle:hover {
  background: #f8f9fa;
}

.hamburger {
  display: block;
  width: 24px;
  height: 2px;
  background: #333;
  position: relative;
  transition: all 0.3s ease;
}

.hamburger::before,
.hamburger::after {
  content: '';
  position: absolute;
  width: 24px;
  height: 2px;
  background: #333;
  transition: all 0.3s ease;
}

.hamburger::before {
  top: -8px;
}

.hamburger::after {
  top: 8px;
}

.hamburger.active {
  background: transparent;
}

.hamburger.active::before {
  top: 0;
  transform: rotate(45deg);
}

.hamburger.active::after {
  top: 0;
  transform: rotate(-45deg);
}

/* 移动端样式 */
@media (max-width: 768px) {
  .nav-menu {
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    background: white;
    flex-direction: column;
    gap: 0;
    padding: 1rem;
    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    transform: translateY(-10px);
    opacity: 0;
    visibility: hidden;
    transition: all 0.3s ease;
  }

  .nav-menu.active {
    transform: translateY(0);
    opacity: 1;
    visibility: visible;
  }

  .nav-link {
    width: 100%;
    justify-content: flex-start;
    padding: 1rem;
    border-radius: 8px;
    margin-bottom: 0.5rem;
  }

  .nav-link.router-link-active::after {
    display: none;
  }

  .menu-toggle {
    display: block;
  }

  .brand-text {
    display: none;
  }
}

@media (max-width: 480px) {
  .nav-container {
    padding: 0 0.5rem;
  }
  
  .brand-link {
    gap: 0.5rem;
  }
  
  .brand-icon {
    font-size: 1.5rem;
  }
}
</style>
