<template>
  <div class="training-log">
    <el-card class="log-card">
      <template #header>
        <div class="log-header">
          <span><el-icon><Document /></el-icon> 训练日志</span>
          <el-button @click="$emit('clear-log')" link size="small">清空日志</el-button>
        </div>
      </template>

      <div class="log-content">
        <div
          v-for="(log, index) in trainingLogs"
          :key="index"
          class="log-entry"
          :class="log.type"
        >
          <span class="log-time">{{ log.time }}</span>
          <span class="log-message">{{ log.message }}</span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Document } from '@element-plus/icons-vue'

// Props
const props = defineProps({
  trainingLogs: {
    type: Array,
    default: () => []
  }
})

// Emits
const emit = defineEmits(['clear-log'])
</script>

<style lang="scss" scoped>
.training-log {
  .log-card {
    background: var(--card-bg);
    border: 1px solid var(--border-light-color);
    border-radius: 12px;
    box-shadow: var(--box-shadow);
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(135deg, var(--glow-color) 0%, var(--glow-light) 100%);
      opacity: 0.05;
      pointer-events: none;
    }

    :deep(.el-card__header) {
      background: var(--secondary-color);
      color: var(--text-color);
      border-bottom: 1px solid var(--border-light-color);
      position: relative;
      z-index: 2;

      .log-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        span {
          font-weight: 600;
          display: flex;
          align-items: center;

          .el-icon {
            margin-right: 8px;
          }
        }
      }
    }

    :deep(.el-card__body) {
      background: var(--card-bg);
      color: var(--text-color);
      position: relative;
      z-index: 2;
    }

    .log-content {
      max-height: 300px;
      overflow-y: auto;
      padding: 1rem;

      .log-entry {
        display: flex;
        align-items: center;
        padding: 8px 12px;
        margin-bottom: 5px;
        border-radius: 6px;
        font-size: 0.9rem;

        &.info {
          background: #e8f4fd;
          color: #2980b9;
        }

        &.success {
          background: #d5f4e6;
          color: #27ae60;
        }

        &.warning {
          background: #fef9e7;
          color: #f39c12;
        }

        &.error {
          background: #fdf2f2;
          color: #e74c3c;
        }

        .log-time {
          font-weight: 600;
          margin-right: 10px;
          min-width: 80px;
        }

        .log-message {
          flex: 1;
        }
      }
    }
  }
}

/* 深色主题优化 */
html.dark-theme .training-log {
  .log-card {
    background: rgba(255, 255, 255, 0.08);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.15);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  }
}

/* 浅色主题适配 */
html.light-theme .training-log {
  .log-card {
    background: #ffffff;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
  }
}
</style>
