<template>
  <div class="theme-switcher">
    <button 
      @click="toggleTheme" 
      class="theme-toggle-btn"
      :class="{ 'light': isLightTheme }"
      :title="isLightTheme ? '切换到深色主题' : '切换到浅色主题'"
    >
      <div class="theme-icon">
        <span v-if="isLightTheme" class="moon-icon">🌙</span>
        <span v-else class="sun-icon">☀️</span>
      </div>
      <span class="theme-label">{{ isLightTheme ? '深色' : '浅色' }}</span>
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const isLightTheme = ref(false)

// 从localStorage获取保存的主题
const getSavedTheme = () => {
  const saved = localStorage.getItem('theme')
  return saved === 'light' ? 'light' : 'dark'
}

// 应用主题
const applyTheme = (theme) => {
  const root = document.documentElement
  isLightTheme.value = theme === 'light'
  
  if (theme === 'light') {
    root.classList.add('light-theme')
    root.classList.remove('dark-theme')
  } else {
    root.classList.add('dark-theme')
    root.classList.remove('light-theme')
  }
  
  localStorage.setItem('theme', theme)
}

// 切换主题
const toggleTheme = () => {
  const newTheme = isLightTheme.value ? 'dark' : 'light'
  applyTheme(newTheme)
}

// 初始化主题
onMounted(() => {
  const savedTheme = getSavedTheme()
  applyTheme(savedTheme)
})
</script>

<style scoped>
.theme-switcher {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
}

.theme-toggle-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  border: none;
  border-radius: 25px;
  background: var(--card-bg, #292c33);
  color: var(--text-color, #f5f6fa);
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(var(--shadow-color-rgb, 0, 0, 0), 0.15);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(var(--card-border-rgb, 57, 59, 64), 0.18);
  font-size: 14px;
  font-weight: 500;
}

.theme-toggle-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(var(--shadow-color-rgb, 0, 0, 0), 0.2);
}

.theme-toggle-btn.light {
  background: var(--card-bg, #ffffff);
  color: var(--text-color, #212529);
  border-color: rgba(var(--card-border-rgb, 0, 0, 0), 0.125);
}

.theme-icon {
  font-size: 18px;
  transition: transform 0.3s ease;
}

.theme-toggle-btn:hover .theme-icon {
  transform: rotate(15deg);
}

.theme-label {
  font-weight: 500;
}

@media (max-width: 768px) {
  .theme-switcher {
    bottom: 15px;
    right: 15px;
  }
  
  .theme-toggle-btn {
    padding: 10px 12px;
    font-size: 12px;
  }
  
  .theme-label {
    display: none;
  }
}
</style>
