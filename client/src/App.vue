<template>
  <div id="app">
    <Navigation />
    <main class="main-content">
      <router-view />
    </main>
    <footer class="app-footer">
      <div class="footer-content">
        <p>&copy; 2024 GoodLab. 《人工智能概论与应用》数字化教材平台</p>
        <p class="footer-tech">由GOODLAB开发团队构建</p>
      </div>
    </footer>
  </div>
</template>

<script>
import Navigation from './components/Navigation.vue'

export default {
  name: 'App',
  components: {
    Navigation
  }
}
</script>

<style lang="scss">
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  line-height: 1.6;
  color: #333;
  background: #f8f9fa;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
}

.app-footer {
  background: $primary-color-light;
  color: white;
  padding: 2rem 0;
  margin-top: auto;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
  text-align: center;
}

.footer-content p {
  margin-bottom: 0.5rem;
}

.footer-tech {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式图片 */
img {
  max-width: 100%;
  height: auto;
}

/* 链接样式 */
a {
  color: #667eea;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

/* 按钮基础样式 */
button {
  font-family: inherit;
}

/* 表单元素样式 */
input, textarea, select {
  font-family: inherit;
}

/* 焦点样式 */
:focus {
  outline: 2px solid #667eea;
  outline-offset: 2px;
}

/* 选择文本样式 */
::selection {
  background: #b0b3b8;
  color: #18191a;
}

::-moz-selection {
  background: #b0b3b8;
  color: #18191a;
}

/* 工具类 */
.text-center {
  text-align: center;
}

.text-left {
  text-align: left;
}

.text-right {
  text-align: right;
}

.mb-1 { margin-bottom: 0.5rem; }
.mb-2 { margin-bottom: 1rem; }
.mb-3 { margin-bottom: 1.5rem; }
.mb-4 { margin-bottom: 2rem; }

.mt-1 { margin-top: 0.5rem; }
.mt-2 { margin-top: 1rem; }
.mt-3 { margin-top: 1.5rem; }
.mt-4 { margin-top: 2rem; }

.p-1 { padding: 0.5rem; }
.p-2 { padding: 1rem; }
.p-3 { padding: 1.5rem; }
.p-4 { padding: 2rem; }

/* 响应式工具类 */
@media (max-width: 768px) {
  .hide-mobile {
    display: none;
  }
}

@media (min-width: 769px) {
  .hide-desktop {
    display: none;
  }
}
</style>
