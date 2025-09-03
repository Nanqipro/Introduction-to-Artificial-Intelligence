<template>
  <div class="visualization-header">
    <h2>
      <el-icon><Connection /></el-icon>
      交互式神经网络训练可视化
    </h2>
    <p>实时观察数据在神经网络中的流动过程，体验猫狗分类的完整训练流程</p>
  </div>
</template>

<script setup>
import { Connection } from '@element-plus/icons-vue'
</script>

<style lang="scss" scoped>
.visualization-header {
  text-align: center;
  margin-bottom: 2rem;
  position: relative;
  z-index: 2;

  h2 {
    color: var(--text-color);
    margin-bottom: 0.5rem;
    font-size: 2rem;
    font-weight: 600;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);

    .el-icon {
      margin-right: 0.5rem;
      color: var(--accent-color);
      filter: drop-shadow(0 0 8px var(--glow-color));
    }
  }

  p {
    color: var(--text-secondary-color);
    font-size: 1.1rem;
    max-width: 600px;
    margin: 0 auto;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  }
}

/* 深色主题优化 */
html.dark-theme .visualization-header {
  position: relative;
  z-index: 10;

  h2 {
    background: linear-gradient(135deg, #ffffff 0%, #e0e0e0 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    text-shadow: none;
    font-weight: 700;
    letter-spacing: -0.5px;
  }

  p {
    color: rgba(255, 255, 255, 0.7);
    font-weight: 400;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  }
}

/* 浅色主题适配 */
html.light-theme .visualization-header {
  h2 {
    color: #212529;
  }

  p {
    color: #495057;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .visualization-header {
    h2 {
      font-size: 1.5rem;
    }

    p {
      font-size: 1rem;
    }
  }
}
</style>
