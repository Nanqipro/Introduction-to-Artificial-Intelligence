<template>
  <div class="node-details-panel">
    <el-card class="node-details-card">
      <template #header>
        <div class="node-header">
          <span>节点详情 - {{ selectedNode.layerName }} 第{{ selectedNode.nodeIndex + 1 }}个节点</span>
          <el-button @click="$emit('close')" link size="small">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
      </template>

      <div class="node-content">
        <div class="node-info">
          <div class="info-item">
            <span>状态:</span>
            <el-tag :type="selectedNode.active ? 'success' : 'danger'">
              {{ selectedNode.active ? '激活' : '禁用' }}
            </el-tag>
          </div>

          <div class="info-item">
            <span>激活值:</span>
            <span class="activation-value">{{ selectedNode.activation?.toFixed(4) || '0.0000' }}</span>
          </div>

          <div class="info-item">
            <span>权重:</span>
            <span class="weight-value">{{ selectedNode.weight?.toFixed(4) || '0.0000' }}</span>
          </div>

          <div class="info-item">
            <span>偏置:</span>
            <span class="bias-value">{{ selectedNode.bias?.toFixed(4) || '0.0000' }}</span>
          </div>
        </div>

        <div class="node-controls">
          <el-button
            :type="selectedNode.active ? 'danger' : 'success'"
            @click="$emit('toggle-node')"
            size="small"
          >
            {{ selectedNode.active ? '禁用节点' : '启用节点' }}
          </el-button>

          <el-button
            type="primary"
            @click="$emit('reset-weights')"
            size="small"
          >
            重置权重
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Close } from '@element-plus/icons-vue'

// Props
const props = defineProps({
  selectedNode: {
    type: Object,
    required: true
  }
})

// Emits
const emit = defineEmits(['close', 'toggle-node', 'reset-weights'])
</script>

<style lang="scss" scoped>
.node-details-panel {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  min-width: 400px;

  .node-details-card {
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
      border-radius: 12px 12px 0 0;
      border-bottom: 1px solid var(--border-light-color);
      position: relative;
      z-index: 2;

      .node-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
    }

    :deep(.el-card__body) {
      background: var(--card-bg);
      color: var(--text-color);
      position: relative;
      z-index: 2;
    }

    .node-content {
      padding: 1.5rem;

      .node-info {
        margin-bottom: 20px;

        .info-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 10px;
          padding: 8px 0;
          border-bottom: 1px solid var(--border-light-color);

          &:last-child {
            border-bottom: none;
          }

          span:first-child {
            color: var(--text-secondary-color);
            font-weight: 500;
          }

          .activation-value, .weight-value, .bias-value {
            font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
            font-weight: 600;
            color: var(--text-color);
          }
        }
      }

      .node-controls {
        display: flex;
        gap: 10px;
      }
    }
  }
}

/* 深色主题优化 */
html.dark-theme .node-details-panel {
  .node-details-card {
    background: rgba(255, 255, 255, 0.08);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.15);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  }
}

/* 浅色主题适配 */
html.light-theme .node-details-panel {
  .node-details-card {
    background: #ffffff;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .node-details-panel {
    min-width: 90vw;
    max-width: 90vw;
  }
}
</style>
