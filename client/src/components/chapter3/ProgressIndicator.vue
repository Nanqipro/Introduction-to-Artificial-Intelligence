<template>
  <div class="progress-indicator">
    <div class="progress-item" :class="{ completed: pixelCaseCompleted }">
      <div class="progress-icon">🖼️</div>
      <div class="progress-info">
        <h4>像素数据理解案例</h4>
        <p>体验次数: {{ pixelInteractionCount }}/5 次</p>
        <div class="progress-status">
          <span v-if="pixelCaseCompleted" class="status-completed">✅ 已完成</span>
          <span v-else class="status-pending">⏳ 进行中</span>
        </div>
      </div>
    </div>
    
    <div class="progress-item" :class="{ completed: edgeCaseCompleted }">
      <div class="progress-icon">🔍</div>
      <div class="progress-info">
        <h4>边缘特征提取案例</h4>
        <p>体验次数: {{ edgeInteractionCount }}/5 次</p>
        <div class="progress-status">
          <span v-if="edgeCaseCompleted" class="status-completed">✅ 已完成</span>
          <span v-else class="status-pending">⏳ 进行中</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProgressIndicator',
  props: {
    pixelCaseCompleted: {
      type: Boolean,
      default: false
    },
    pixelInteractionCount: {
      type: Number,
      default: 0
    },
    edgeCaseCompleted: {
      type: Boolean,
      default: false
    },
    edgeInteractionCount: {
      type: Number,
      default: 0
    }
  }
}
</script>

<style lang="scss" scoped>
.progress-indicator {
  display: flex;
  justify-content: center;
  gap: 2rem;
  margin-top: 2rem;
  flex-wrap: wrap;
}

.progress-item {
  background: rgba(255, 255, 255, 0.15);
  border: 2px solid rgba(255, 255, 255, 0.25);
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  min-width: 300px;
  backdrop-filter: blur(8px);
  
  &.completed {
    background: rgba(76, 175, 80, 0.25);
    border-color: rgba(76, 175, 80, 0.6);
    transform: scale(1.02);
    box-shadow: 0 4px 16px rgba(76, 175, 80, 0.3);
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
    color: white;
    margin: 0 0 0.5rem 0;
    font-size: 1.1rem;
    font-weight: 600;
  }
  
  p {
    color: rgba(255, 255, 255, 0.8);
    margin: 0 0 0.5rem 0;
    font-size: 0.9rem;
  }
}

.progress-status {
  .status-completed {
    color: #4caf50;
    font-weight: 600;
    font-size: 0.9rem;
  }
  
  .status-pending {
    color: rgba(255, 255, 255, 0.7);
    font-size: 0.9rem;
  }
}

/* 浅色主题适配 */
.light-theme {
  .progress-item {
    background: rgba(59, 130, 246, 0.08);
    border-color: rgba(59, 130, 246, 0.2);
    backdrop-filter: none;
    
    &.completed {
      background: rgba(34, 197, 94, 0.1);
      border-color: rgba(34, 197, 94, 0.3);
      box-shadow: 0 4px 16px rgba(34, 197, 94, 0.15);
    }
    
    h4 {
      color: var(--text-color);
    }
    
    p {
      color: var(--text-secondary-color);
    }
    
    .status-pending {
      color: var(--text-muted-color);
    }
  }
}
</style>