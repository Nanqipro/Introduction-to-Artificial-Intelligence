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
@import './styles/variables.scss';

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
  color: $body-color;
  background: $body-bg;
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
  background: $footer-bg;
  color: $footer-color;
  padding: $spacing-lg 0;
  margin-top: auto;
}

.footer-content {
  max-width: $footer-max-width;
  margin: 0 auto;
  padding: 0 $spacing-sm;
  text-align: center;
}

.footer-content p {
  margin-bottom: $spacing-xs;
}

.footer-tech {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: $scrollbar-width;
}

::-webkit-scrollbar-track {
  background: $scrollbar-track;
}

::-webkit-scrollbar-thumb {
  background: $scrollbar-thumb;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: $scrollbar-thumb-hover;
}

/* 响应式图片 */
img {
  max-width: 100%;
  height: auto;
}

/* 链接样式 */
a {
  color: $link-color;
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
  }
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
  outline: 2px solid $focus-color;
  outline-offset: 2px;
}

/* 选择文本样式 */
::selection {
  background: $selection-bg;
  color: $selection-color;
}

::-moz-selection {
  background: $selection-bg;
  color: $selection-color;
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

.mb-1 { margin-bottom: $spacing-xs; }
.mb-2 { margin-bottom: $spacing-sm; }
.mb-3 { margin-bottom: $spacing-md; }
.mb-4 { margin-bottom: $spacing-lg; }

.mt-1 { margin-top: $spacing-xs; }
.mt-2 { margin-top: $spacing-sm; }
.mt-3 { margin-top: $spacing-md; }
.mt-4 { margin-top: $spacing-lg; }

.p-1 { padding: $spacing-xs; }
.p-2 { padding: $spacing-sm; }
.p-3 { padding: $spacing-md; }
.p-4 { padding: $spacing-lg; }

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
