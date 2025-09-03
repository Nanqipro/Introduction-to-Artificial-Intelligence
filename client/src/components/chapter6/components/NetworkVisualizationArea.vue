<template>
  <div class="network-visualization-area">
    <el-row :gutter="20">
      <!-- 网络结构图 -->
      <el-col :span="16">
        <el-card class="network-card">
          <template #header>
            <div class="network-header">
              <span>神经网络结构 - 实时数据流</span>
              <div class="network-status">
                <el-tag :type="isTraining ? 'success' : 'info'" size="small">
                  {{ isTraining ? '训练中' : '待机' }}
                </el-tag>
                <span class="epoch-info">Epoch: {{ currentEpoch }}/{{ totalEpochs }}</span>
              </div>
            </div>
          </template>

          <div ref="networkContainer" class="network-container">
            <!-- 网络层渲染区域 -->
            <svg
              ref="networkSvg"
              class="network-svg"
              :width="networkWidth"
              :height="networkHeight"
              @click="handleNetworkClick"
            >
              <!-- 连接线 -->
              <g class="connections">
                <line
                  v-for="connection in connections"
                  :key="connection.id"
                  :x1="connection.x1"
                  :y1="connection.y1"
                  :x2="connection.x2"
                  :y2="connection.y2"
                  :stroke="connection.color"
                  :stroke-width="connection.width"
                  :opacity="connection.opacity"
                  class="connection-line"
                />
              </g>

              <!-- 数据流动粒子 -->
              <g class="data-particles">
                <circle
                  v-for="particle in dataParticles"
                  :key="particle.id"
                  :cx="particle.x"
                  :cy="particle.y"
                  :r="particle.radius"
                  :fill="particle.color"
                  :opacity="particle.opacity"
                  class="data-particle"
                />
              </g>

              <!-- 网络节点 -->
              <g class="network-nodes">
                <g
                  v-for="layer in networkLayers"
                  :key="layer.id"
                  class="layer-group"
                >
                  <circle
                    v-for="(node, nodeIndex) in layer.nodes"
                    :key="`${layer.id}-${nodeIndex}`"
                    :cx="node.x"
                    :cy="node.y"
                    :r="node.radius"
                    :fill="node.color"
                    :stroke="node.strokeColor"
                    :stroke-width="node.strokeWidth"
                    :opacity="node.opacity"
                    class="network-node"
                    :class="{
                      active: node.active,
                      disabled: node.disabled,
                      highlighted: node.highlighted
                    }"
                    @click="$emit('toggle-node', layer.id, nodeIndex)"
                    @mouseenter="$emit('highlight-node', layer.id, nodeIndex)"
                    @mouseleave="$emit('unhighlight-node', layer.id, nodeIndex)"
                  />

                  <!-- 节点激活值显示 -->
                  <text
                    v-for="(node, nodeIndex) in layer.nodes"
                    v-if="visualizationMode === 'activation' && node.activation !== undefined"
                    :key="`text-${layer.id}-${nodeIndex}`"
                    :x="node.x"
                    :y="node.y + 25"
                    text-anchor="middle"
                    class="activation-text"
                    :fill="node.activation > 0.5 ? '#e74c3c' : '#3498db'"
                  >
                    {{ node.activation.toFixed(2) }}
                  </text>
                </g>
              </g>

              <!-- 层标签 -->
              <g class="layer-labels">
                <text
                  v-for="layer in networkLayers"
                  :key="`label-${layer.id}`"
                  :x="layer.labelX"
                  :y="layer.labelY"
                  text-anchor="middle"
                  class="layer-label"
                >
                  {{ layer.name }}
                </text>
              </g>
            </svg>
          </div>
        </el-card>
      </el-col>

      <!-- 训练指标面板 -->
      <el-col :span="8">
        <el-card class="metrics-card">
          <template #header>
            <span><el-icon><TrendCharts /></el-icon> 训练指标</span>
          </template>

          <div class="metrics-content">
            <!-- 实时指标 -->
            <div class="current-metrics">
              <div class="metric-item">
                <span class="metric-label">准确率</span>
                <div class="metric-value accuracy">
                  {{ (currentAccuracy * 100).toFixed(2) }}%
                </div>
                <el-progress
                  :percentage="currentAccuracy * 100"
                  :stroke-width="8"
                  :show-text="false"
                  status="success"
                />
              </div>

              <div class="metric-item">
                <span class="metric-label">损失率</span>
                <div class="metric-value loss">
                  {{ currentLoss.toFixed(4) }}
                </div>
                <el-progress
                  :percentage="Math.max(0, 100 - currentLoss * 100)"
                  :stroke-width="8"
                  :show-text="false"
                  status="warning"
                />
              </div>

              <div class="metric-item">
                <span class="metric-label">学习率</span>
                <div class="metric-value learning-rate">
                  {{ learningRate.toFixed(4) }}
                </div>
              </div>
            </div>

            <!-- 训练曲线图 -->
            <div ref="chartContainer" class="chart-container">
              <canvas ref="metricsChart" class="metrics-chart"></canvas>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { TrendCharts } from '@element-plus/icons-vue'
import Chart from 'chart.js/auto'

// Props
const props = defineProps({
  isTraining: {
    type: Boolean,
    default: false
  },
  currentEpoch: {
    type: Number,
    default: 0
  },
  totalEpochs: {
    type: Number,
    default: 100
  },
  networkLayers: {
    type: Array,
    default: () => []
  },
  connections: {
    type: Array,
    default: () => []
  },
  dataParticles: {
    type: Array,
    default: () => []
  },
  visualizationMode: {
    type: String,
    default: 'dataflow'
  },
  currentAccuracy: {
    type: Number,
    default: 0
  },
  currentLoss: {
    type: Number,
    default: 1
  },
  learningRate: {
    type: Number,
    default: 0.001
  },
  accuracyHistory: {
    type: Array,
    default: () => []
  },
  lossHistory: {
    type: Array,
    default: () => []
  }
})

// Emits
const emit = defineEmits([
  'toggle-node',
  'highlight-node',
  'unhighlight-node',
  'network-click'
])

// 响应式数据
const networkContainer = ref(null)
const networkSvg = ref(null)
const chartContainer = ref(null)
const metricsChart = ref(null)

// 网络尺寸
const networkWidth = ref(800)
const networkHeight = ref(400)

// Chart.js 实例
let chartInstance = null

// 方法
const handleNetworkClick = (event) => {
  // 处理网络区域点击事件
  const rect = networkSvg.value.getBoundingClientRect()
  const x = event.clientX - rect.left
  const y = event.clientY - rect.top

  // 检查是否点击了节点
  props.networkLayers.forEach((layer, layerIndex) => {
    layer.nodes.forEach((node, nodeIndex) => {
      const distance = Math.sqrt((x - node.x) ** 2 + (y - node.y) ** 2)
      if (distance <= node.radius) {
        emit('network-click', { layerId: layer.id, nodeIndex, node })
      }
    })
  })
}

const initChart = () => {
  if (!metricsChart.value) return

  const ctx = metricsChart.value.getContext('2d')

  chartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: [],
      datasets: [
        {
          label: '准确率',
          data: [],
          borderColor: '#27ae60',
          backgroundColor: 'rgba(39, 174, 96, 0.1)',
          tension: 0.4,
          fill: true
        },
        {
          label: '损失率',
          data: [],
          borderColor: '#e74c3c',
          backgroundColor: 'rgba(231, 76, 60, 0.1)',
          tension: 0.4,
          fill: true,
          yAxisID: 'y1'
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        y: {
          type: 'linear',
          display: true,
          position: 'left',
          min: 0,
          max: 1,
          title: {
            display: true,
            text: '准确率'
          }
        },
        y1: {
          type: 'linear',
          display: true,
          position: 'right',
          min: 0,
          max: 1,
          title: {
            display: true,
            text: '损失率'
          },
          grid: {
            drawOnChartArea: false,
          },
        }
      },
      plugins: {
        legend: {
          display: true,
          position: 'top'
        }
      },
      animation: {
        duration: 0
      }
    }
  })
}

const updateChart = () => {
  if (!chartInstance) return

  const maxPoints = 50
  const labels = props.accuracyHistory.map((_, index) => index + 1)

  chartInstance.data.labels = labels.slice(-maxPoints)
  chartInstance.data.datasets[0].data = props.accuracyHistory.slice(-maxPoints)
  chartInstance.data.datasets[1].data = props.lossHistory.slice(-maxPoints)

  chartInstance.update('none')
}

// 监听器
watch(() => props.accuracyHistory, () => {
  updateChart()
}, { deep: true })

watch(() => props.lossHistory, () => {
  updateChart()
}, { deep: true })

// 生命周期
onMounted(async () => {
  await nextTick()
  initChart()
})

onUnmounted(() => {
  if (chartInstance) {
    chartInstance.destroy()
  }
})
</script>

<style lang="scss" scoped>
.network-visualization-area {
  position: relative;
  z-index: 2;
  
  .network-card {
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
    }

    :deep(.el-card__body) {
      background: var(--card-bg);
      color: var(--text-color);
      position: relative;
      z-index: 2;
    }
  }

  .network-container {
    background: var(--secondary-color);
    border-radius: 12px;
    padding: 1rem;
    min-height: 400px;
    border: 1px solid var(--border-light-color);
  }

  .network-svg {
    background: var(--secondary-color);
    border-radius: 8px;

    .connection-line {
      transition: all 0.3s ease;
    }

    .data-particle {
      animation: pulse 1s ease-in-out infinite alternate;
    }

    .network-node {
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        stroke-width: 4;
        filter: drop-shadow(0 0 8px rgba(52, 152, 219, 0.6));
      }

      &.active {
        filter: drop-shadow(0 0 6px rgba(39, 174, 96, 0.8));
      }

      &.disabled {
        opacity: 0.3;
        filter: grayscale(100%);
      }

      &.highlighted {
        stroke-width: 6;
        filter: drop-shadow(0 0 12px rgba(231, 76, 60, 0.8));
      }
    }

    .activation-text {
      font-size: 10px;
      font-weight: 600;
      pointer-events: none;
    }

    .layer-label {
      fill: var(--text-color);
      font-size: 14px;
      font-weight: 500;
    }
  }
}

.metrics-card {
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
  }

  :deep(.el-card__body) {
    background: var(--card-bg);
    color: var(--text-color);
    position: relative;
    z-index: 2;
  }

  .metrics-content {
    padding: 1rem;

    .current-metrics {
      margin-bottom: 20px;

      .metric-item {
        margin-bottom: 20px;

        .metric-label {
          display: block;
          font-size: 0.9rem;
          color: var(--text-secondary-color);
          margin-bottom: 5px;
        }

        .metric-value {
          font-size: 1.5rem;
          font-weight: 600;
          margin-bottom: 8px;

          &.accuracy {
            color: #27ae60;
          }

          &.loss {
            color: #e74c3c;
          }

          &.learning-rate {
            color: #3498db;
          }
        }
      }
    }

    .chart-container {
      height: 200px;
      position: relative;

      .metrics-chart {
        width: 100%;
        height: 100%;
      }
    }
  }
}

/* 深色主题优化 */
html.dark-theme .network-visualization-area {
  .network-card {
    background: rgba(255, 255, 255, 0.08);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.15);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  }

  .network-container {
    background: linear-gradient(135deg, #0f0f23 0%, #1a1a2e 50%, #16213e 100%);
  }

  .network-svg {
    background: linear-gradient(135deg, #0f0f23 0%, #1a1a2e 50%, #16213e 100%);
  }

  .layer-label {
    fill: #ffffff;
  }
}

html.dark-theme .metrics-card {
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

/* 浅色主题适配 */
html.light-theme .network-visualization-area {
  .network-card {
    background: #ffffff;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
  }

  .network-container {
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  }

  .network-svg {
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  }

  .layer-label {
    fill: #212529;
  }
}

html.light-theme .metrics-card {
  background: #ffffff;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
}

// 动画效果
@keyframes pulse {
  0% {
    opacity: 0.6;
    transform: scale(1);
  }
  100% {
    opacity: 1;
    transform: scale(1.2);
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .network-visualization-area {
    .el-row {
      flex-direction: column;
    }
  }
}

@media (max-width: 768px) {
  .network-container {
    height: 400px !important;
  }
}
</style>
