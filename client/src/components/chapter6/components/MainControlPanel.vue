<template>
  <div class="main-control-panel">
    <el-row :gutter="20">
      <!-- 训练控制 -->
      <el-col :span="8">
        <el-card class="control-card">
          <template #header>
            <span><el-icon><VideoPlay /></el-icon> 训练控制</span>
          </template>
          <div class="training-controls">
            <div class="control-buttons">
              <el-button
                type="primary"
                @click="$emit('start-training')"
                :disabled="isTraining"
                size="large"
                class="control-button"
              >
                <el-icon><VideoPlay /></el-icon>
                {{ isTraining ? '训练中...' : '开始训练' }}
              </el-button>

              <el-button
                type="warning"
                @click="$emit('pause-training')"
                :disabled="!isTraining"
                size="large"
                class="control-button"
              >
                <el-icon><VideoPause /></el-icon>
                暂停训练
              </el-button>

              <el-button
                type="info"
                @click="$emit('reset-training')"
                size="large"
                class="control-button"
              >
                <el-icon><Refresh /></el-icon>
                重置训练
              </el-button>
            </div>

            <div class="speed-control">
              <div class="speed-label">训练速度</div>
              <el-slider
                v-model="localTrainingSpeed"
                :min="0.5"
                :max="3"
                :step="0.5"
                show-stops
                class="speed-slider"
                @change="$emit('update:training-speed', localTrainingSpeed)"
              />
              <div class="speed-value">{{ localTrainingSpeed }}x</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 训练数据集选择 -->
      <el-col :span="8">
        <el-card class="control-card">
          <template #header>
            <span><el-icon><Document /></el-icon> 训练数据集</span>
          </template>
          <div class="dataset-selection">
            <div class="dataset-info">
              <div class="current-dataset">
                <span class="dataset-name">{{ selectedDataset.name }}</span>
                <span class="dataset-size">{{ selectedDataset.size }} 张图片</span>
              </div>
              <div class="dataset-stats">
                <div class="stat-item">
                  <span class="stat-label">猫:</span>
                  <span class="stat-value">{{ selectedDataset.catCount }}</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">狗:</span>
                  <span class="stat-value">{{ selectedDataset.dogCount }}</span>
                </div>
              </div>
            </div>

            <div class="dataset-options">
              <div
                v-for="(dataset, index) in availableDatasets"
                :key="index"
                class="dataset-item"
                :class="{ active: selectedDatasetIndex === index }"
                @click="$emit('select-dataset', index)"
              >
                <div class="dataset-icon">
                  <el-icon><Document /></el-icon>
                </div>
                <div class="dataset-details">
                  <div class="dataset-title">{{ dataset.name }}</div>
                  <div class="dataset-description">{{ dataset.description }}</div>
                  <div class="dataset-meta">
                    <span>{{ dataset.size }} 张</span>
                    <span>{{ dataset.difficulty }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 网络配置 -->
      <el-col :span="8">
        <el-card class="control-card">
          <template #header>
            <span><el-icon><Setting /></el-icon> 网络配置</span>
          </template>
          <div class="network-config">
            <div class="config-item">
              <span>学习率：</span>
              <el-input-number
                v-model="localLearningRate"
                :min="0.0001"
                :max="0.1"
                :step="0.0001"
                :precision="4"
                size="small"
                @change="$emit('update:learning-rate', localLearningRate)"
              />
            </div>

            <div class="config-item">
              <span>批次大小：</span>
              <el-select v-model="localBatchSize" size="small" @change="$emit('update:batch-size', localBatchSize)">
                <el-option label="16" :value="16" />
                <el-option label="32" :value="32" />
                <el-option label="64" :value="64" />
              </el-select>
            </div>

            <div class="config-item">
              <span>显示模式：</span>
              <el-radio-group v-model="localVisualizationMode" size="small" @change="$emit('update:visualization-mode', localVisualizationMode)">
                <el-radio value="dataflow">数据流</el-radio>
                <el-radio value="activation">激活值</el-radio>
              </el-radio-group>
            </div>

            <div class="config-item">
              <el-button
                @click="$emit('show-network-structure')"
                type="primary"
                size="small"
                style="width: 100%;"
              >
                <el-icon><Edit /></el-icon>
                自定义网络结构
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import {
  VideoPlay, VideoPause, Refresh, Document, Setting, Edit
} from '@element-plus/icons-vue'

// Props
const props = defineProps({
  isTraining: {
    type: Boolean,
    default: false
  },
  trainingSpeed: {
    type: Number,
    default: 1
  },
  learningRate: {
    type: Number,
    default: 0.001
  },
  batchSize: {
    type: Number,
    default: 32
  },
  visualizationMode: {
    type: String,
    default: 'dataflow'
  },
  selectedDatasetIndex: {
    type: Number,
    default: 0
  },
  availableDatasets: {
    type: Array,
    default: () => []
  }
})

// Emits
const emit = defineEmits([
  'start-training',
  'pause-training', 
  'reset-training',
  'select-dataset',
  'show-network-structure',
  'update:training-speed',
  'update:learning-rate',
  'update:batch-size',
  'update:visualization-mode'
])

// 本地响应式数据
const localTrainingSpeed = ref(props.trainingSpeed)
const localLearningRate = ref(props.learningRate)
const localBatchSize = ref(props.batchSize)
const localVisualizationMode = ref(props.visualizationMode)

// 计算属性
const selectedDataset = computed(() => props.availableDatasets[props.selectedDatasetIndex] || {})

// 监听props变化
watch(() => props.trainingSpeed, (newVal) => {
  localTrainingSpeed.value = newVal
})

watch(() => props.learningRate, (newVal) => {
  localLearningRate.value = newVal
})

watch(() => props.batchSize, (newVal) => {
  localBatchSize.value = newVal
})

watch(() => props.visualizationMode, (newVal) => {
  localVisualizationMode.value = newVal
})
</script>

<style lang="scss" scoped>
.main-control-panel {
  margin-bottom: 2rem;
  position: relative;
  z-index: 2;

  .control-card {
    border-radius: 12px;
    background: var(--card-bg);
    border: 1px solid var(--border-light-color);
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

      span {
        font-weight: 600;
        display: flex;
        align-items: center;

        .el-icon {
          margin-right: 8px;
          color: var(--accent-color);
        }
      }
    }

    :deep(.el-card__body) {
      background: var(--card-bg);
      color: var(--text-color);
      position: relative;
      z-index: 2;
    }

    .training-controls, .dataset-selection, .network-config {
      padding: 1rem;
    }

    // 训练控制样式
    .training-controls {
      .control-buttons {
        display: flex;
        flex-direction: column;
        gap: 0.75rem;
        margin-bottom: 1.5rem;

        .control-button {
          width: 100%;
          height: 48px;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 0.5rem;
          font-weight: 500;
          border-radius: 8px;
          transition: all 0.3s ease;
          background: var(--btn-primary-bg);
          border: none;
          color: white;
          position: relative;
          overflow: hidden;

          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
            transition: left 0.5s;
          }

          .el-icon {
            font-size: 1.1rem;
          }

          &:hover:not(:disabled) {
            background: var(--btn-hover-bg);
            transform: translateY(-2px);
            box-shadow: 0 8px 25px var(--glow-color);
            
            &::before {
              left: 100%;
            }
          }
        }
      }

      .speed-control {
        .speed-label {
          color: var(--text-color);
          font-weight: 500;
          margin-bottom: 0.5rem;
        }

        .speed-value {
          color: var(--text-secondary-color);
          text-align: center;
          margin-top: 0.5rem;
        }
      }
    }

    // 数据集选择样式
    .dataset-selection {
      .dataset-info {
        margin-bottom: 1rem;

        .current-dataset {
          margin-bottom: 1rem;

          .dataset-name {
            color: var(--text-color);
            font-weight: 600;
            font-size: 1.1rem;
          }

          .dataset-size {
            color: var(--text-secondary-color);
            font-size: 0.9rem;
          }
        }

        .dataset-stats {
          display: flex;
          gap: 1rem;

          .stat-item {
            .stat-label {
              color: var(--text-secondary-color);
              font-size: 0.9rem;
            }

            .stat-value {
              color: var(--accent-color);
              font-weight: 600;
              text-shadow: 0 0 8px var(--glow-color);
            }
          }
        }
      }

      .dataset-options {
        display: flex;
        flex-direction: column;
        gap: 0.75rem;

        .dataset-item {
          padding: 1rem;
          border-radius: 12px;
          background: var(--secondary-color);
          border: 1px solid var(--border-light-color);
          color: var(--text-color);
          cursor: pointer;
          transition: all 0.3s ease;
          position: relative;
          overflow: hidden;
          
          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
            transition: left 0.5s;
          }

          &:hover {
            background: var(--btn-primary-bg);
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px var(--glow-color);
            border-color: var(--accent-color);
            
            &::before {
              left: 100%;
            }
          }

          &.active {
            background: var(--btn-primary-bg);
            color: white;
            border-color: var(--accent-color);
            box-shadow: 0 4px 16px var(--glow-color);
          }

          .dataset-icon {
            color: inherit;
          }

          .dataset-details {
            .dataset-title {
              font-weight: 600;
              margin-bottom: 0.25rem;
            }

            .dataset-description {
              font-size: 0.9rem;
              opacity: 0.8;
              margin-bottom: 0.5rem;
            }

            .dataset-meta {
              display: flex;
              gap: 1rem;
              font-size: 0.8rem;
              opacity: 0.7;
            }
          }
        }
      }
    }

    // 网络配置样式
    .network-config {
      .config-item {
        display: flex;
        align-items: center;
        gap: 1rem;
        margin-bottom: 1rem;

        span {
          color: var(--text-color);
          font-weight: 500;
          min-width: 80px;
        }
      }
    }
  }
}

/* 深色主题优化 */
html.dark-theme .main-control-panel {
  position: relative;
  z-index: 10;

  .control-card {
    background: rgba(26, 26, 46, 0.9);
    backdrop-filter: blur(30px);
    border: 1px solid rgba(0, 191, 255, 0.2);
    box-shadow: 
      0 8px 32px rgba(0, 0, 0, 0.4),
      0 0 0 1px rgba(0, 191, 255, 0.1);
    position: relative;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(135deg, 
        rgba(0, 191, 255, 0.05) 0%, 
        rgba(0, 255, 127, 0.03) 50%, 
        rgba(255, 69, 0, 0.02) 100%);
      border-radius: inherit;
    }
  }

  .training-controls {
    position: relative;
    z-index: 1;

    .control-buttons {
      .control-button {
        background: linear-gradient(135deg, #00bfff 0%, #0080ff 100%);
        color: #ffffff;
        border: 1px solid rgba(0, 191, 255, 0.5);
        box-shadow: 
          0 4px 15px rgba(0, 191, 255, 0.4),
          0 0 0 1px rgba(0, 191, 255, 0.2);
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        font-weight: 600;

        &:hover {
          background: linear-gradient(135deg, #00a6e6 0%, #00bfff 100%);
          border-color: rgba(0, 191, 255, 0.8);
          transform: translateY(-3px);
          box-shadow: 
            0 8px 25px rgba(0, 191, 255, 0.5),
            0 0 0 2px rgba(0, 191, 255, 0.3);
        }

        &:active {
          transform: translateY(-1px);
        }
      }
    }

    .speed-control {
      .speed-label {
        color: rgba(255, 255, 255, 0.9);
        font-weight: 500;
      }

      .speed-value {
        background: linear-gradient(135deg, #00bfff 0%, #00ff7f 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        font-weight: 700;
      }
    }
  }

  .dataset-selection {
    position: relative;
    z-index: 1;

    .dataset-info {
      .current-dataset {
        .dataset-name {
          background: linear-gradient(135deg, #ffffff 0%, #e0e0e0 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
          font-weight: 700;
        }

        .dataset-size {
          color: rgba(255, 255, 255, 0.7);
          font-weight: 400;
        }
      }

      .dataset-stats {
        .stat-item {
          .stat-label {
            color: rgba(255, 255, 255, 0.9);
            font-weight: 500;
          }

          .stat-value {
            background: linear-gradient(135deg, #00bfff 0%, #00ff7f 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            font-weight: 700;
          }
        }
      }
    }

    .dataset-options {
      .dataset-item {
        background: rgba(255, 255, 255, 0.03);
        border: 1px solid rgba(0, 191, 255, 0.15);
        color: rgba(255, 255, 255, 0.9);
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        position: relative;
        overflow: hidden;

        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: -100%;
          width: 100%;
          height: 100%;
          background: linear-gradient(90deg, 
            transparent 0%, 
            rgba(0, 191, 255, 0.1) 50%, 
            transparent 100%);
          transition: left 0.5s ease;
        }

        &:hover {
          background: rgba(0, 191, 255, 0.1);
          border-color: rgba(0, 191, 255, 0.4);
          transform: translateY(-3px);
          box-shadow: 
            0 8px 25px rgba(0, 191, 255, 0.3),
            0 0 0 1px rgba(0, 191, 255, 0.2);

          &::before {
            left: 100%;
          }
        }

        &.active {
          background: linear-gradient(135deg, #00bfff 0%, #0080ff 100%);
          color: #ffffff;
          border-color: rgba(0, 191, 255, 0.8);
          box-shadow: 
            0 4px 15px rgba(0, 191, 255, 0.4),
            0 0 0 1px rgba(0, 191, 255, 0.3);
        }
      }
    }
  }

  .network-config {
    position: relative;
    z-index: 1;

    .config-item {
      span {
        color: rgba(255, 255, 255, 0.9);
      }
    }
  }
}

/* 浅色主题适配 */
html.light-theme .main-control-panel {
  .control-card {
    background: #ffffff;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
  }

  .training-controls {
    .control-buttons {
      .control-button {
        background: linear-gradient(135deg, #3b82f6, #2563eb);
        color: #ffffff;
        border: none;
        box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);

        &:hover {
          background: linear-gradient(135deg, #2563eb, #1d4ed8);
          transform: translateY(-1px);
          box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
        }
      }
    }

    .speed-control {
      .speed-label {
        color: #212529;
      }

      .speed-value {
        color: #495057;
      }
    }
  }

  .dataset-selection {
    .dataset-info {
      .current-dataset {
        .dataset-name {
          color: #212529;
        }

        .dataset-size {
          color: #495057;
        }
      }

      .dataset-stats {
        .stat-item {
          .stat-label {
            color: #212529;
          }

          .stat-value {
            color: #495057;
          }
        }
      }
    }

    .dataset-options {
      .dataset-item {
        background: #f8f9fa;
        border: 1px solid rgba(0, 0, 0, 0.1);
        color: #212529;

        &:hover {
          background: #e9ecef;
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        &.active {
          background: #3b82f6;
          color: #ffffff;
          border-color: #3b82f6;
        }
      }
    }
  }

  .network-config {
    .config-item {
      span {
        color: #212529;
      }
    }
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .main-control-panel {
    .el-row {
      flex-direction: column;
    }
  }
}

@media (max-width: 480px) {
  .config-item {
    flex-direction: column !important;
    align-items: flex-start !important;
    gap: 8px;
  }
}
</style>
