<template>
  <div class="progress-indicator">
    <div
      class="progress-item"
      :class="{ completed: regressionCaseCompleted }"
    >
      <div class="progress-icon">📊</div>
      <div class="progress-info">
        <h4>回归分析案例</h4>
        <p>需要生成数据 {{ interactionCounts.regressionGenerate }}/3 次</p>
        <div class="progress-status">
          <span v-if="regressionCaseCompleted" class="status-completed"
            >✅ 已完成</span
          >
          <span v-else class="status-pending">⏳ 进行中</span>
        </div>
      </div>
    </div>

    <div
      class="progress-item"
      :class="{ completed: deepLearningCaseCompleted }"
    >
      <div class="progress-icon">🧠</div>
      <div class="progress-info">
        <h4>深度学习核心层</h4>
        <p>观看动画演示，理解核心概念</p>
        <div class="progress-status">
          <span v-if="deepLearningCaseCompleted" class="status-completed"
            >✅ 已完成</span
          >
          <span v-else class="status-pending">⏳ 进行中</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProgressIndicator",
  props: {
    interactionCounts: {
      type: Object,
      required: true
    },
    regressionCaseCompleted: {
      type: Boolean,
      default: false
    },
    deepLearningCaseCompleted: {
      type: Boolean,
      default: false
    }
  }
};
</script>

<style lang="scss" scoped>
.progress-indicator {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
  margin: 2rem 0;
  padding: 2rem;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  border: 2px solid rgba(255, 255, 255, 0.2);
}

.progress-item {
  background: rgba(255, 255, 255, 0.1);
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;

  &.completed {
    background: rgba(76, 175, 80, 0.2);
    border-color: rgba(76, 175, 80, 0.5);
  }
}

.progress-icon {
  font-size: 2rem;
  min-width: 3rem;
  text-align: center;
}

.progress-info {
  flex: 1;

  h4 {
    color: var(--text-color);
    margin: 0 0 0.5rem 0;
    font-size: 1.1rem;
    font-weight: 600;
  }

  p {
    color: var(--text-secondary-color);
    margin: 0 0 0.5rem 0;
    font-size: 0.9rem;
  }
}

.progress-status {
  .status-completed {
    color: var(--success-color);
    font-weight: 600;
  }

  .status-pending {
    color: var(--warning-color);
    font-weight: 600;
  }
}

/* 浅色主题样式已移至统一的theme.css管理 */

/* 响应式设计 */
@media (max-width: 768px) {
  .progress-indicator {
    grid-template-columns: 1fr;
    gap: 1rem;
    padding: 1rem;
  }
}
</style>