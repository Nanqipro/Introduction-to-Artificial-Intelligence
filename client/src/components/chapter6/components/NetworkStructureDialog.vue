<template>
  <el-dialog
    v-model="dialogVisible"
    width="700px"
    :close-on-click-modal="true"
    :close-on-press-escape="true"
    :destroy-on-close="true"
    :modal="true"
    :lock-scroll="true"
    :append-to-body="true"
    :show-close="false"
  >
    <template #header>
      <div class="dialog-header">
        <span class="dialog-title">自定义神经网络结构</span>
        <el-button
          @click="closeDialog"
          type="danger"
          size="small"
          :icon="Close"
          circle
          title="关闭"
          style="margin-left: auto;"
        />
      </div>
    </template>
    
    <div class="structure-config-content">
      <div class="config-header">
        <el-alert
          title="配置说明"
          description="您可以自定义每层神经元的数量（1-10个），输入层固定为1个，输出层固定为2个（猫、狗分类）"
          type="info"
          :closable="false"
          show-icon
        />
      </div>

      <div class="layer-configs">
        <div
          v-for="(layer, index) in layerConfigs"
          :key="layer.id"
          class="layer-config-item"
          :class="{ disabled: layer.fixed }"
        >
          <div class="layer-info">
            <div class="layer-icon">
              <el-icon>
                <component :is="layer.icon" />
              </el-icon>
            </div>
            <div class="layer-details">
              <h4>{{ layer.name }}</h4>
              <p>{{ layer.description }}</p>
            </div>
          </div>

          <div class="layer-control">
            <el-input-number
              v-model="layer.nodeCount"
              :min="1"
              :max="10"
              :disabled="layer.fixed"
              @change="validateNodeCount(layer, $event)"
              size="small"
              style="width: 120px;"
            />
            <span class="unit-text">个神经元</span>
          </div>
        </div>
      </div>

      <div class="structure-preview">
        <h4>网络结构预览</h4>
        <div class="preview-network">
          <div
            v-for="(layer, index) in layerConfigs"
            :key="layer.id"
            class="preview-layer"
          >
            <div class="layer-name">{{ layer.name }}</div>
            <div class="layer-nodes">
              <div
                v-for="i in layer.nodeCount"
                :key="i"
                class="preview-node"
                :class="{ fixed: layer.fixed }"
              ></div>
            </div>
            <div class="node-count">{{ layer.nodeCount }}个</div>

            <!-- 连接线 -->
            <div
              v-if="index < layerConfigs.length - 1"
              class="layer-connection"
            ></div>
          </div>
        </div>

        <div class="total-stats">
          <div class="stat-item">
            <span class="stat-label">总层数：</span>
            <span class="stat-value">{{ layerConfigs.length }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">总神经元：</span>
            <span class="stat-value">{{ totalNeurons }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">预估参数：</span>
            <span class="stat-value">{{ estimatedParameters }}</span>
          </div>
        </div>
      </div>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="$emit('reset')">恢复默认</el-button>
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="applyConfig">应用配置</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { Close } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  layerConfigs: {
    type: Array,
    default: () => []
  },
  totalNeurons: {
    type: Number,
    default: 0
  },
  estimatedParameters: {
    type: String,
    default: '0'
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'apply', 'reset'])

// 本地响应式数据
const dialogVisible = ref(props.modelValue)

// 监听props变化
watch(() => props.modelValue, (newVal) => {
  dialogVisible.value = newVal
})

watch(dialogVisible, (newVal) => {
  emit('update:modelValue', newVal)
})

// 方法
const closeDialog = () => {
  dialogVisible.value = false
}

const validateNodeCount = (layer, newValue) => {
  if (newValue > 10) {
    ElMessage.warning({
      message: `每层神经元数量不能超过10个！当前设置：${newValue}个`,
      duration: 3000,
      showClose: true
    })
    layer.nodeCount = 10
  } else if (newValue < 1) {
    ElMessage.warning({
      message: '每层至少需要1个神经元！',
      duration: 3000,
      showClose: true
    })
    layer.nodeCount = 1
  }
}

const applyConfig = () => {
  emit('apply')
  closeDialog()
}
</script>

<style lang="scss" scoped>
.structure-config-content {
  .config-header {
    margin-bottom: 2rem;
  }

  .layer-configs {
    margin-bottom: 2rem;

    .layer-config-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 1rem;
      margin-bottom: 1rem;
      border: 1px solid var(--border-light-color);
      border-radius: 8px;
      transition: all 0.3s ease;

      &:hover {
        border-color: var(--accent-color);
        background: var(--card-hover-bg);
      }

      &.disabled {
        background: var(--secondary-color);
        opacity: 0.7;

        .layer-control {
          opacity: 0.5;
        }
      }

      .layer-info {
        display: flex;
        align-items: center;
        flex: 1;

        .layer-icon {
          margin-right: 1rem;

          .el-icon {
            font-size: 1.5rem;
            color: var(--accent-color);
          }
        }

        .layer-details {
          h4 {
            margin: 0 0 0.3rem 0;
            color: var(--text-color);
            font-size: 1rem;
          }

          p {
            margin: 0;
            color: var(--text-secondary-color);
            font-size: 0.9rem;
          }
        }
      }

      .layer-control {
        display: flex;
        align-items: center;
        gap: 0.5rem;

        .unit-text {
          color: var(--text-secondary-color);
          font-size: 0.9rem;
        }
      }
    }
  }

  .structure-preview {
    background: var(--secondary-color);
    padding: 1.5rem;
    border-radius: 8px;
    border: 1px solid var(--border-light-color);

    h4 {
      margin: 0 0 1rem 0;
      color: var(--text-color);
    }

    .preview-network {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 1.5rem;
      padding: 1rem;
      background: var(--card-bg);
      border-radius: 8px;
      overflow-x: auto;
      border: 1px solid var(--border-light-color);

      .preview-layer {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin: 0 1rem;
        position: relative;

        .layer-name {
          font-size: 0.8rem;
          color: var(--text-secondary-color);
          margin-bottom: 0.5rem;
          text-align: center;
        }

        .layer-nodes {
          display: flex;
          flex-direction: column;
          gap: 4px;
          margin-bottom: 0.5rem;

          .preview-node {
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background: var(--accent-color);
            border: 2px solid var(--accent-dark-color);

            &.fixed {
              background: var(--error-color);
              border-color: var(--error-dark-color);
            }
          }
        }

        .node-count {
          font-size: 0.7rem;
          color: var(--text-secondary-color);
        }

        .layer-connection {
          position: absolute;
          right: -1rem;
          top: 50%;
          width: 2rem;
          height: 2px;
          background: var(--border-light-color);
          transform: translateY(-50%);

          &::after {
            content: '';
            position: absolute;
            right: -4px;
            top: -2px;
            width: 0;
            height: 0;
            border-left: 6px solid var(--border-light-color);
            border-top: 3px solid transparent;
            border-bottom: 3px solid transparent;
          }
        }
      }
    }

    .total-stats {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 1rem;

      .stat-item {
        text-align: center;
        padding: 0.8rem;
        background: var(--card-bg);
        border-radius: 6px;
        border: 1px solid var(--border-light-color);

        .stat-label {
          display: block;
          font-size: 0.8rem;
          color: var(--text-secondary-color);
          margin-bottom: 0.3rem;
        }

        .stat-value {
          display: block;
          font-size: 1.2rem;
          font-weight: 600;
          color: var(--text-color);
        }
      }
    }
  }
}

// 对话框头部样式
.dialog-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;

  .dialog-title {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-color);
  }
}

/* 深色主题优化 */
html.dark-theme .structure-config-content {
  .layer-configs {
    .layer-config-item {
      background: rgba(255, 255, 255, 0.03);
      border: 1px solid rgba(255, 255, 255, 0.1);

      &:hover {
        background: rgba(255, 255, 255, 0.05);
        border-color: rgba(0, 191, 255, 0.3);
      }

      &.disabled {
        background: rgba(255, 255, 255, 0.02);
      }
    }
  }

  .structure-preview {
    background: rgba(255, 255, 255, 0.03);
    border: 1px solid rgba(255, 255, 255, 0.1);

    .preview-network {
      background: rgba(255, 255, 255, 0.02);
      border: 1px solid rgba(255, 255, 255, 0.1);
    }

    .total-stats {
      .stat-item {
        background: rgba(255, 255, 255, 0.02);
        border: 1px solid rgba(255, 255, 255, 0.1);
      }
    }
  }
}

/* 浅色主题适配 */
html.light-theme .structure-config-content {
  .layer-configs {
    .layer-config-item {
      background: #f8f9fa;
      border: 1px solid rgba(0, 0, 0, 0.1);

      &:hover {
        background: #e9ecef;
        border-color: #3b82f6;
      }

      &.disabled {
        background: #f5f5f5;
      }
    }
  }

  .structure-preview {
    background: #f8f9fa;
    border: 1px solid rgba(0, 0, 0, 0.1);

    .preview-network {
      background: #ffffff;
      border: 1px solid rgba(0, 0, 0, 0.1);
    }

    .total-stats {
      .stat-item {
        background: #ffffff;
        border: 1px solid rgba(0, 0, 0, 0.1);
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .structure-config-content {
    .layer-configs {
      .layer-config-item {
        flex-direction: column;
        align-items: flex-start;
        gap: 1rem;
      }
    }

    .structure-preview {
      .total-stats {
        grid-template-columns: 1fr;
      }
    }
  }
}
</style>
