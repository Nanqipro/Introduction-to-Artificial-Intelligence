<template>
  <div class="network-visualization-container">
    <div class="visualization-header">
      <h2>
        <el-icon><Connection /></el-icon>
        交互式神经网络训练可视化
      </h2>
      <p>实时观察数据在神经网络中的流动过程，体验猫狗分类的完整训练流程</p>
    </div>

    <!-- 主控制面板 -->
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
                  @click="startTraining"
                  :disabled="isTraining"
                  size="large"
                  class="control-button"
                >
                  <el-icon><VideoPlay /></el-icon>
                  {{ isTraining ? '训练中...' : '开始训练' }}
                </el-button>

                <el-button
                  type="warning"
                  @click="pauseTraining"
                  :disabled="!isTraining"
                  size="large"
                  class="control-button"
                >
                  <el-icon><VideoPause /></el-icon>
                  暂停训练
                </el-button>

                <el-button
                  type="info"
                  @click="resetTraining"
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
                  v-model="trainingSpeed"
                  :min="0.5"
                  :max="3"
                  :step="0.5"
                  show-stops
                  class="speed-slider"
                />
                <div class="speed-value">{{ trainingSpeed }}x</div>
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
                  @click="selectDataset(index)"
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
                  v-model="learningRate"
                  :min="0.0001"
                  :max="0.1"
                  :step="0.0001"
                  :precision="4"
                  size="small"
                />
              </div>

              <div class="config-item">
                <span>批次大小：</span>
                <el-select v-model="batchSize" size="small">
                  <el-option label="16" :value="16" />
                  <el-option label="32" :value="32" />
                  <el-option label="64" :value="64" />
                </el-select>
              </div>

              <div class="config-item">
                <span>显示模式：</span>
                <el-radio-group v-model="visualizationMode" size="small">
                  <el-radio value="dataflow">数据流</el-radio>
                  <el-radio value="activation">激活值</el-radio>
                </el-radio-group>
              </div>

              <div class="config-item">
                <el-button
                  @click="showNetworkStructureDialog = true"
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

    <!-- 神经网络可视化区域 -->
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
                      @click="toggleNode(layer.id, nodeIndex)"
                      @mouseenter="highlightNode(layer.id, nodeIndex)"
                      @mouseleave="unhighlightNode(layer.id, nodeIndex)"
                    />

                    <!-- 节点激活值显示 -->
                    <text
                      v-if="visualizationMode === 'activation' && node.activation !== undefined"
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

    <!-- 节点详情面板 -->
    <div v-if="selectedNode" class="node-details-panel">
      <el-card class="node-details-card">
        <template #header>
          <div class="node-header">
            <span>节点详情 - {{ selectedNode.layerName }} 第{{ selectedNode.nodeIndex + 1 }}个节点</span>
            <el-button @click="selectedNode = null" link size="small">
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
              @click="toggleSelectedNode"
              size="small"
            >
              {{ selectedNode.active ? '禁用节点' : '启用节点' }}
            </el-button>

            <el-button
              type="primary"
              @click="resetNodeWeights"
              size="small"
            >
              重置权重
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 训练日志 -->
    <div class="training-log">
      <el-card class="log-card">
        <template #header>
          <div class="log-header">
            <span><el-icon><Document /></el-icon> 训练日志</span>
            <el-button @click="clearLog" link size="small">清空日志</el-button>
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

    <!-- 网络结构配置对话框 -->
    <el-dialog
      v-model="showNetworkStructureDialog"
      width="700px"
      :before-close="handleCloseStructureDialog"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      :destroy-on-close="true"
      :modal="true"
      :lock-scroll="true"
      :append-to-body="true"
    >
      <template #header>
        <div class="dialog-header">
          <span class="dialog-title">自定义神经网络结构</span>
          <el-button
            @click="forceCloseStructureDialog"
            type="danger"
            size="small"
            :icon="Close"
            circle
            title="强制关闭"
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
          <el-button @click="resetToDefault">恢复默认</el-button>
          <el-button @click="closeStructureDialog">取消</el-button>
          <el-button type="primary" @click="applyNetworkStructure">应用配置</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'
import {
  Connection, Refresh, VideoPlay, VideoPause, Setting,
  TrendCharts, Close, Document, Edit
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import Chart from 'chart.js/auto'

// 定义事件
const emit = defineEmits(['progress-update', 'training-complete'])

// 响应式数据
const networkContainer = ref(null)
const networkSvg = ref(null)
const chartContainer = ref(null)
const metricsChart = ref(null)

// 训练状态
const isTraining = ref(false)
const currentEpoch = ref(0)
const totalEpochs = ref(100)
const trainingSpeed = ref(1)
const learningRate = ref(0.001)
const batchSize = ref(32)

// 可视化模式
const visualizationMode = ref('dataflow')

// 网络尺寸
const networkWidth = ref(800)
const networkHeight = ref(400)

// 训练指标
const currentAccuracy = ref(0)
const currentLoss = ref(1)
const accuracyHistory = ref([])
const lossHistory = ref([])

// 当前选中的数据集和节点
const selectedDatasetIndex = ref(0)
const selectedNode = ref(null)

// 当前数据集和预测
const selectedDataset = computed(() => availableDatasets.value[selectedDatasetIndex.value])
const currentPrediction = ref({ cat: 0.5, dog: 0.5 })

// 数据流粒子
const dataParticles = ref([])
const connections = ref([])

// 训练日志
const trainingLogs = ref([])

// 网络结构配置
const showNetworkStructureDialog = ref(false)
const layerConfigs = ref([
  {
    id: 'input',
    name: '输入层',
    description: '接收图像数据',
    icon: 'Picture',
    nodeCount: 1,
    fixed: true
  },
  {
    id: 'conv1',
    name: '卷积层1',
    description: '特征提取',
    icon: 'Grid',
    nodeCount: 6,
    fixed: false
  },
  {
    id: 'pool1',
    name: '池化层1',
    description: '降维处理',
    icon: 'Crop',
    nodeCount: 3,
    fixed: false
  },
  {
    id: 'conv2',
    name: '卷积层2',
    description: '深层特征',
    icon: 'Grid',
    nodeCount: 8,
    fixed: false
  },
  {
    id: 'pool2',
    name: '池化层2',
    description: '进一步降维',
    icon: 'Crop',
    nodeCount: 4,
    fixed: false
  },
  {
    id: 'fc1',
    name: '全连接层',
    description: '特征整合',
    icon: 'Connection',
    nodeCount: 6,
    fixed: false
  },
  {
    id: 'output',
    name: '输出层',
    description: '分类结果（猫/狗）',
    icon: 'Flag',
    nodeCount: 2,
    fixed: true
  }
])

// Chart.js 实例
let chartInstance = null
let animationId = null
let nodeInteractionCount = 0

// 可用数据集
const availableDatasets = ref([
  {
    name: 'CIFAR-10 猫狗子集',
    description: '经典的小图像数据集，32x32像素',
    size: 12000,
    catCount: 6000,
    dogCount: 6000,
    difficulty: '简单',
    accuracy: 0.85,
    trainingTime: '2-3分钟'
  },
  {
    name: 'Kaggle 猫狗大战',
    description: '高质量的猫狗图像，适合深度学习',
    size: 25000,
    catCount: 12500,
    dogCount: 12500,
    difficulty: '中等',
    accuracy: 0.92,
    trainingTime: '5-8分钟'
  },
  {
    name: 'Oxford-IIIT Pet',
    description: '包含多种猫狗品种的精细数据集',
    size: 7349,
    catCount: 3686,
    dogCount: 3663,
    difficulty: '困难',
    accuracy: 0.88,
    trainingTime: '3-5分钟'
  },
  {
    name: '自定义小数据集',
    description: '快速验证模型的小型数据集',
    size: 1000,
    catCount: 500,
    dogCount: 500,
    difficulty: '简单',
    accuracy: 0.78,
    trainingTime: '1分钟'
  }
])

// 网络层定义（动态生成）
const networkLayers = ref([])

// 计算属性
const trainingProgress = computed(() => {
  return Math.round((currentEpoch.value / totalEpochs.value) * 100)
})

const totalNeurons = computed(() => {
  return layerConfigs.value.reduce((total, layer) => total + layer.nodeCount, 0)
})

const estimatedParameters = computed(() => {
  let params = 0
  for (let i = 0; i < layerConfigs.value.length - 1; i++) {
    const currentLayer = layerConfigs.value[i]
    const nextLayer = layerConfigs.value[i + 1]
    params += currentLayer.nodeCount * nextLayer.nodeCount
  }
  return params.toLocaleString()
})

// 方法
const initializeNetwork = () => {
  // 根据layerConfigs动态初始化网络结构
  const layerPositions = []
  const totalLayers = layerConfigs.value.length
  const spacing = (networkWidth.value - 200) / (totalLayers - 1)

  for (let i = 0; i < totalLayers; i++) {
    layerPositions.push(100 + i * spacing)
  }

  // 更新networkLayers以匹配layerConfigs
  networkLayers.value = layerConfigs.value.map((config, layerIndex) => {
    const nodeCount = config.nodeCount
    const x = layerPositions[layerIndex]
    const startY = (networkHeight.value - (nodeCount - 1) * 40) / 2

    const nodes = []
    for (let i = 0; i < nodeCount; i++) {
      nodes.push({
        x: x,
        y: startY + i * 40,
        radius: 15,
        color: '#3498db',
        strokeColor: '#2980b9',
        strokeWidth: 2,
        opacity: 1,
        active: true,
        disabled: false,
        highlighted: false,
        activation: Math.random(),
        weight: Math.random() * 2 - 1,
        bias: Math.random() * 0.2 - 0.1
      })
    }

    return {
      id: config.id,
      name: config.name,
      nodes: nodes,
      labelX: x,
      labelY: 30
    }
  })

  // 初始化连接线
  initializeConnections()

  // 初始化完成，当前数据集通过计算属性自动设置
}

const initializeConnections = () => {
  connections.value = []

  for (let i = 0; i < networkLayers.value.length - 1; i++) {
    const currentLayer = networkLayers.value[i]
    const nextLayer = networkLayers.value[i + 1]

    currentLayer.nodes.forEach((currentNode, currentIndex) => {
      nextLayer.nodes.forEach((nextNode, nextIndex) => {
        connections.value.push({
          id: `${currentLayer.id}-${currentIndex}-${nextLayer.id}-${nextIndex}`,
          x1: currentNode.x + currentNode.radius,
          y1: currentNode.y,
          x2: nextNode.x - nextNode.radius,
          y2: nextNode.y,
          color: '#bdc3c7',
          width: 1,
          opacity: 0.6,
          active: true
        })
      })
    })
  }
}

const startTraining = () => {
  if (isTraining.value) return

  isTraining.value = true
  currentEpoch.value = 0

  addLog('info', '开始训练神经网络...')
  addLog('info', `学习率: ${learningRate.value}, 批次大小: ${batchSize.value}`)

  // 开始训练循环
  trainingLoop()
}

const pauseTraining = () => {
  isTraining.value = false
  addLog('warning', '训练已暂停')
}

const resetTraining = () => {
  isTraining.value = false
  currentEpoch.value = 0
  currentAccuracy.value = 0
  currentLoss.value = 1
  accuracyHistory.value = []
  lossHistory.value = []
  dataParticles.value = []

  // 重置网络状态
  initializeNetwork()
  updateChart()

  addLog('info', '训练已重置')
}

const trainingLoop = () => {
  if (!isTraining.value) return

  // 模拟训练步骤
  simulateTrainingStep()

  // 更新可视化
  updateDataFlow()
  updateMetrics()
  updateChart()

  currentEpoch.value++

  if (currentEpoch.value < totalEpochs.value) {
    setTimeout(() => {
      trainingLoop()
    }, 1000 / trainingSpeed.value)
  } else {
    isTraining.value = false
    addLog('success', '训练完成！')

    // 发射训练完成事件
    emit('training-complete', {
      accuracy: currentAccuracy.value,
      loss: currentLoss.value,
      epochs: currentEpoch.value,
      nodeInteractions: getNodeInteractionCount()
    })
  }
}

const simulateTrainingStep = () => {
  // 模拟前向传播
  forwardPass()

  // 模拟反向传播
  backwardPass()

  // 更新指标
  const progress = currentEpoch.value / totalEpochs.value
  currentAccuracy.value = Math.min(0.95, 0.5 + progress * 0.45 + (Math.random() - 0.5) * 0.1)
  currentLoss.value = Math.max(0.05, 1 - progress * 0.95 + (Math.random() - 0.5) * 0.1)

  // 记录历史
  accuracyHistory.value.push(currentAccuracy.value)
  lossHistory.value.push(currentLoss.value)

  // 限制历史长度
  if (accuracyHistory.value.length > 100) {
    accuracyHistory.value.shift()
    lossHistory.value.shift()
  }

  addLog('info', `Epoch ${currentEpoch.value}: 准确率 ${(currentAccuracy.value * 100).toFixed(2)}%, 损失 ${currentLoss.value.toFixed(4)}`)
}

const forwardPass = () => {
  // 模拟前向传播过程
  networkLayers.value.forEach((layer, layerIndex) => {
    layer.nodes.forEach((node, nodeIndex) => {
      if (layerIndex === 0) {
        // 输入层
        node.activation = Math.random()
      } else {
        // 其他层：计算激活值
        const prevLayer = networkLayers.value[layerIndex - 1]
        let sum = 0
        prevLayer.nodes.forEach(prevNode => {
          if (prevNode.active) {
            sum += prevNode.activation * node.weight
          }
        })
        sum += node.bias

        // ReLU激活函数
        node.activation = Math.max(0, sum)

        // 对于输出层，使用softmax
        if (layerIndex === networkLayers.value.length - 1) {
          node.activation = Math.exp(sum)
        }
      }

      // 更新节点颜色基于激活值
      const intensity = Math.min(1, node.activation)
      node.color = `hsl(${200 + intensity * 60}, 70%, ${50 + intensity * 30}%)`
    })
  })

  // 对输出层进行softmax归一化
  const outputLayer = networkLayers.value[networkLayers.value.length - 1]
  const sum = outputLayer.nodes.reduce((acc, node) => acc + node.activation, 0)
  outputLayer.nodes.forEach((node, index) => {
    node.activation = node.activation / sum
    if (index === 0) {
      currentPrediction.value.cat = node.activation
    } else {
      currentPrediction.value.dog = node.activation
    }
  })
}

const backwardPass = () => {
  // 模拟反向传播过程
  networkLayers.value.forEach((layer, layerIndex) => {
    layer.nodes.forEach(node => {
      // 简单的权重更新模拟
      node.weight += (Math.random() - 0.5) * learningRate.value * 0.1
      node.bias += (Math.random() - 0.5) * learningRate.value * 0.01

      // 限制权重范围
      node.weight = Math.max(-2, Math.min(2, node.weight))
      node.bias = Math.max(-0.5, Math.min(0.5, node.bias))
    })
  })
}

const updateDataFlow = () => {
  if (visualizationMode.value !== 'dataflow') return

  // 清除旧的粒子
  dataParticles.value = []

  // 创建新的数据流粒子
  connections.value.forEach((connection, index) => {
    if (Math.random() < 0.3) { // 30%的连接显示数据流
      const particle = {
        id: `particle-${index}-${Date.now()}`,
        x: connection.x1,
        y: connection.y1,
        targetX: connection.x2,
        targetY: connection.y2,
        radius: 3,
        color: '#e74c3c',
        opacity: 0.8,
        progress: 0
      }
      dataParticles.value.push(particle)
    }
  })

  // 动画粒子移动
  animateParticles()
}

const animateParticles = () => {
  dataParticles.value.forEach((particle, index) => {
    particle.progress += 0.02 * trainingSpeed.value

    if (particle.progress >= 1) {
      // 粒子到达目标，移除
      dataParticles.value.splice(index, 1)
    } else {
      // 更新粒子位置
      particle.x = particle.x + (particle.targetX - particle.x) * particle.progress
      particle.y = particle.y + (particle.targetY - particle.y) * particle.progress
      particle.opacity = 1 - particle.progress * 0.5
    }
  })

  if (isTraining.value) {
    requestAnimationFrame(animateParticles)
  }
}

const updateMetrics = () => {
  // 更新连接线的可视化
  connections.value.forEach(connection => {
    const strength = Math.random()
    connection.width = 1 + strength * 3
    connection.opacity = 0.3 + strength * 0.7
    connection.color = `hsl(${200 + strength * 60}, 70%, 50%)`
  })
}

const selectDataset = (index) => {
  selectedDatasetIndex.value = index

  // 重新运行前向传播
  if (isTraining.value) {
    forwardPass()
  }

  const dataset = availableDatasets.value[index]
  addLog('info', `选择了新的训练数据集: ${dataset.name}`)
  addLog('info', `数据集包含 ${dataset.size} 张图片 (猫: ${dataset.catCount}, 狗: ${dataset.dogCount})`)
}



const toggleNode = (layerId, nodeIndex) => {
  const layer = networkLayers.value.find(l => l.id === layerId)
  if (layer && layer.nodes[nodeIndex]) {
    const node = layer.nodes[nodeIndex]
    node.active = !node.active
    node.disabled = !node.active

    // 增加交互计数
    nodeInteractionCount++

    // 更新节点外观
    if (node.active) {
      node.strokeColor = '#2980b9'
      node.opacity = 1
    } else {
      node.strokeColor = '#e74c3c'
      node.opacity = 0.5
    }

    // 更新相关连接
    updateNodeConnections(layerId, nodeIndex, node.active)

    addLog('info', `${node.active ? '启用' : '禁用'}了 ${layer.name} 第${nodeIndex + 1}个节点`)
  }
}

const updateNodeConnections = (layerId, nodeIndex, active) => {
  connections.value.forEach(connection => {
    if (connection.id.includes(`${layerId}-${nodeIndex}`)) {
      connection.opacity = active ? 0.6 : 0.2
      connection.color = active ? '#bdc3c7' : '#ecf0f1'
    }
  })
}

const highlightNode = (layerId, nodeIndex) => {
  const layer = networkLayers.value.find(l => l.id === layerId)
  if (layer && layer.nodes[nodeIndex]) {
    layer.nodes[nodeIndex].highlighted = true
    layer.nodes[nodeIndex].strokeWidth = 4
  }
}

const unhighlightNode = (layerId, nodeIndex) => {
  const layer = networkLayers.value.find(l => l.id === layerId)
  if (layer && layer.nodes[nodeIndex]) {
    layer.nodes[nodeIndex].highlighted = false
    layer.nodes[nodeIndex].strokeWidth = 2
  }
}

const handleNetworkClick = (event) => {
  // 处理网络区域点击事件
  const rect = networkSvg.value.getBoundingClientRect()
  const x = event.clientX - rect.left
  const y = event.clientY - rect.top

  // 检查是否点击了节点
  networkLayers.value.forEach((layer, layerIndex) => {
    layer.nodes.forEach((node, nodeIndex) => {
      const distance = Math.sqrt((x - node.x) ** 2 + (y - node.y) ** 2)
      if (distance <= node.radius) {
        selectedNode.value = {
          layerId: layer.id,
          layerName: layer.name,
          nodeIndex: nodeIndex,
          ...node
        }
      }
    })
  })
}

const toggleSelectedNode = () => {
  if (selectedNode.value) {
    toggleNode(selectedNode.value.layerId, selectedNode.value.nodeIndex)
    // 更新选中节点的状态
    const layer = networkLayers.value.find(l => l.id === selectedNode.value.layerId)
    if (layer) {
      selectedNode.value.active = layer.nodes[selectedNode.value.nodeIndex].active
    }
  }
}

const resetNodeWeights = () => {
  if (selectedNode.value) {
    const layer = networkLayers.value.find(l => l.id === selectedNode.value.layerId)
    if (layer) {
      const node = layer.nodes[selectedNode.value.nodeIndex]
      node.weight = Math.random() * 2 - 1
      node.bias = Math.random() * 0.2 - 0.1

      selectedNode.value.weight = node.weight
      selectedNode.value.bias = node.bias

      addLog('info', `重置了 ${selectedNode.value.layerName} 第${selectedNode.value.nodeIndex + 1}个节点的权重`)
    }
  }
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
  const labels = accuracyHistory.value.map((_, index) => index + 1)

  chartInstance.data.labels = labels.slice(-maxPoints)
  chartInstance.data.datasets[0].data = accuracyHistory.value.slice(-maxPoints)
  chartInstance.data.datasets[1].data = lossHistory.value.slice(-maxPoints)

  chartInstance.update('none')
}

const addLog = (type, message) => {
  const now = new Date()
  const time = now.toLocaleTimeString()

  trainingLogs.value.push({
    type,
    time,
    message
  })

  // 限制日志数量
  if (trainingLogs.value.length > 100) {
    trainingLogs.value.shift()
  }
}

const clearLog = () => {
  trainingLogs.value = []
}

const getNodeInteractionCount = () => {
  return nodeInteractionCount
}

// 网络结构配置方法
const validateNodeCount = (layer, newValue) => {
  if (newValue > 10) {
    ElMessage.warning({
      message: `每层神经元数量不能超过10个！当前设置：${newValue}个`,
      duration: 3000,
      showClose: true
    })
    // 重置为10
    layer.nodeCount = 10
  } else if (newValue < 1) {
    ElMessage.warning({
      message: '每层至少需要1个神经元！',
      duration: 3000,
      showClose: true
    })
    // 重置为1
    layer.nodeCount = 1
  }
}

const handleCloseStructureDialog = (done) => {
  try {
    // Element Plus 的 before-close 回调需要调用 done() 来关闭对话框
    console.log('Closing structure dialog, done function:', typeof done)

    // 先更新状态
    showNetworkStructureDialog.value = false

    // 然后调用 done() 让 Element Plus 处理关闭逻辑
    if (done && typeof done === 'function') {
      done()
    }
  } catch (error) {
    console.error('Error closing structure dialog:', error)
    // 即使出错也要确保对话框能关闭
    showNetworkStructureDialog.value = false
    if (done && typeof done === 'function') {
      done()
    }
  }
}

const closeStructureDialog = () => {
  console.log('Manually closing structure dialog')
  showNetworkStructureDialog.value = false
}

// 强制关闭弹框的方法，用于处理特殊情况
const forceCloseStructureDialog = () => {
  console.log('Force closing structure dialog')
  showNetworkStructureDialog.value = false
  // 使用 nextTick 确保 DOM 更新
  nextTick(() => {
    showNetworkStructureDialog.value = false
  })
}

const resetToDefault = () => {
  ElMessageBox.confirm(
    '确定要恢复到默认的网络结构吗？这将重置所有自定义配置。',
    '确认重置',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    // 恢复默认配置
    layerConfigs.value = [
      {
        id: 'input',
        name: '输入层',
        description: '接收图像数据',
        icon: 'Picture',
        nodeCount: 1,
        fixed: true
      },
      {
        id: 'conv1',
        name: '卷积层1',
        description: '特征提取',
        icon: 'Grid',
        nodeCount: 6,
        fixed: false
      },
      {
        id: 'pool1',
        name: '池化层1',
        description: '降维处理',
        icon: 'Crop',
        nodeCount: 3,
        fixed: false
      },
      {
        id: 'conv2',
        name: '卷积层2',
        description: '深层特征',
        icon: 'Grid',
        nodeCount: 8,
        fixed: false
      },
      {
        id: 'pool2',
        name: '池化层2',
        description: '进一步降维',
        icon: 'Crop',
        nodeCount: 4,
        fixed: false
      },
      {
        id: 'fc1',
        name: '全连接层',
        description: '特征整合',
        icon: 'Connection',
        nodeCount: 6,
        fixed: false
      },
      {
        id: 'output',
        name: '输出层',
        description: '分类结果（猫/狗）',
        icon: 'Flag',
        nodeCount: 2,
        fixed: true
      }
    ]

    ElMessage.success('已恢复默认网络结构')
  }).catch(() => {
    // 用户取消
  })
}

const applyNetworkStructure = () => {
  // 检查是否有训练正在进行
  if (isTraining.value) {
    ElMessage.warning('请先停止当前训练再修改网络结构')
    return
  }

  // 应用新的网络结构
  initializeNetwork()
  showNetworkStructureDialog.value = false

  ElMessage.success({
    message: `网络结构已更新！总共${totalNeurons.value}个神经元，预估${estimatedParameters.value}个参数`,
    duration: 4000,
    showClose: true
  })

  addLog('info', `网络结构已更新：${layerConfigs.value.map(l => `${l.name}(${l.nodeCount})`).join(' → ')}`)
}

// 监听器
watch(visualizationMode, (newMode) => {
  if (newMode === 'dataflow' && isTraining.value) {
    updateDataFlow()
  }
})

watch(selectedDatasetIndex, (newIndex) => {
  selectDataset(newIndex)
})

watch(learningRate, (newRate) => {
  addLog('info', `学习率更新为: ${newRate}`)
})

watch(batchSize, (newSize) => {
  addLog('info', `批次大小更新为: ${newSize}`)
})

// 生命周期
onMounted(async () => {
  await nextTick()

  // 初始化网络
  initializeNetwork()

  // 初始化图表
  initChart()

  // 添加初始日志
  addLog('info', '神经网络可视化系统已初始化')
  addLog('info', '点击节点可以启用/禁用该节点参与计算')

  // 监听进度变化
  emit('progress-update', 'network-training', trainingProgress.value)

  // 添加键盘事件监听器，处理弹框关闭
  const handleKeydown = (event) => {
    if (event.key === 'Escape' && showNetworkStructureDialog.value) {
      console.log('ESC key pressed, force closing dialog')
      forceCloseStructureDialog()
    }
  }
  document.addEventListener('keydown', handleKeydown)

  // 保存事件监听器引用以便清理
  window.dialogKeydownHandler = handleKeydown
})

onUnmounted(() => {
  if (animationId) {
    cancelAnimationFrame(animationId)
  }
  if (chartInstance) {
    chartInstance.destroy()
  }
  isTraining.value = false

  // 清理键盘事件监听器
  if (window.dialogKeydownHandler) {
    document.removeEventListener('keydown', window.dialogKeydownHandler)
    delete window.dialogKeydownHandler
  }
})
</script>

<style lang="scss" scoped>


.network-visualization-container {
  padding: 1.5rem;
  background: linear-gradient(135deg, $primary-color 0%, $primary-gradient-end 100%);
  min-height: 100vh;

  ::selection {
    background: $accent-color;
    color: #ffffff;
  }

  ::-moz-selection {
    background: $accent-color;
    color: #ffffff;
  }
}

.visualization-header {
  text-align: center;
  margin-bottom: 2rem;

  h2 {
    color: $text-color;
    margin-bottom: 0.5rem;
    font-size: 2rem;
    font-weight: 600;

    .el-icon {
      margin-right: 0.5rem;
      color: $accent-color;
    }
  }

  p {
    color: $text-secondary-color;
    font-size: 1.1rem;
    max-width: 600px;
    margin: 0 auto;
  }
}

.main-control-panel {
  margin-bottom: 2rem;

  .control-card {
    border-radius: 12px;
    background: $secondary-color;
    border: 1px solid $border-color;
    box-shadow: $box-shadow;

    :deep(.el-card__header) {
      background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
      color: $primary-color;
      border-radius: 12px 12px 0 0;

      span {
        font-weight: 600;
        display: flex;
        align-items: center;

        .el-icon {
          margin-right: 8px;
        }
      }
    }

    :deep(.el-card__body) {
      background: $secondary-color;
      color: $text-color;
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

          .el-icon {
            font-size: 1.1rem;
          }

          &:hover:not(:disabled) {
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
          }

          &:disabled {
            opacity: 0.6;
            cursor: not-allowed;
          }
        }
      }

      .speed-control {
        .speed-label {
          color: $text-secondary-color;
          font-size: 0.9rem;
          font-weight: 500;
          margin-bottom: 0.75rem;
          display: block;
        }

        .speed-slider {
          margin-bottom: 0.5rem;
        }

        .speed-value {
          text-align: center;
          color: $accent-color;
          font-size: 0.85rem;
          font-weight: 600;
          padding: 0.25rem 0.5rem;
          background: $primary-color;
          border: 1px solid $border-color;
          border-radius: 4px;
          display: inline-block;
          min-width: 40px;
        }
      }
    }

    // 数据集选择样式
    .dataset-selection {
      .dataset-info {
        margin-bottom: 1.5rem;
        padding: 1rem;
        background: $primary-color;
        border: 1px solid $border-color;
        border-radius: 8px;

        .current-dataset {
          margin-bottom: 0.5rem;

          .dataset-name {
            display: block;
            font-weight: 600;
            color: $text-color;
            font-size: 1.1rem;
          }

          .dataset-size {
            color: $text-secondary-color;
            font-size: 0.9rem;
          }
        }

        .dataset-stats {
          display: flex;
          gap: 1rem;

          .stat-item {
            display: flex;
            align-items: center;
            gap: 0.25rem;

            .stat-label {
              color: $text-secondary-color;
              font-size: 0.9rem;
            }

            .stat-value {
              color: $accent-color;
              font-weight: 600;
            }
          }
        }
      }

      .dataset-options {
        display: flex;
        flex-direction: column;
        gap: 0.75rem;

        .dataset-item {
          display: flex;
          align-items: center;
          gap: 0.75rem;
          padding: 0.75rem;
          background: $secondary-color;
          border: 1px solid $border-color;
          border-radius: 8px;
          cursor: pointer;
          transition: all 0.3s ease;

          &:hover {
            background: $primary-hover-color;
            border-color: $accent-color;
            transform: translateY(-1px);
          }

          &.active {
            background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
            border-color: $accent-color;
            color: $primary-color;

            .dataset-icon {
              color: $primary-color;
            }

            .dataset-details {
              .dataset-title {
                color: $primary-color;
              }

              .dataset-description {
                color: rgba(24, 25, 26, 0.8);
              }

              .dataset-meta {
                span {
                  color: rgba(24, 25, 26, 0.7);
                }
              }
            }
          }

          .dataset-icon {
            font-size: 1.5rem;
            color: $accent-color;
            flex-shrink: 0;
          }

          .dataset-details {
            flex: 1;

            .dataset-title {
              font-weight: 600;
              color: $text-color;
              margin-bottom: 0.25rem;
              font-size: 0.95rem;
            }

            .dataset-description {
              color: $text-secondary-color;
              font-size: 0.85rem;
              margin-bottom: 0.25rem;
              line-height: 1.3;
            }

            .dataset-meta {
              display: flex;
              gap: 0.75rem;

              span {
                color: $text-secondary-color;
                font-size: 0.8rem;
                padding: 0.125rem 0.375rem;
                background: $primary-color;
                border-radius: 4px;
              }
            }
          }
        }
      }
    }

    .sample-images {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 10px;
      margin-bottom: 15px;

      .sample-item {
        border: 2px solid #ecf0f1;
        border-radius: 8px;
        padding: 8px;
        cursor: pointer;
        transition: all 0.3s ease;
        text-align: center;

        &:hover {
          border-color: #3498db;
          transform: translateY(-2px);
        }

        &.active {
          border-color: #e74c3c;
          background: #fdf2f2;
        }

        img {
          width: 40px;
          height: 40px;
          border-radius: 4px;
        }

        .label {
          display: block;
          margin-top: 5px;
          font-size: 0.8rem;
          color: #7f8c8d;
        }
      }
    }

    .upload-area {
      border: 2px dashed #bdc3c7;
      border-radius: 8px;
      padding: 20px;
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        border-color: #3498db;
        background: #f8f9fa;
      }

      .el-icon {
        font-size: 1.5rem;
        color: #7f8c8d;
        margin-bottom: 5px;
        display: block;
      }

      span {
        color: #7f8c8d;
        font-size: 0.9rem;
      }
    }

    .config-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;

      span {
        color: #7f8c8d;
        font-size: 0.9rem;
      }
    }
  }
}

.network-visualization-area {
  margin-bottom: 2rem;

  .network-card {
    border-radius: 12px;
    background: $secondary-color;
    border: 1px solid $border-color;
    box-shadow: $box-shadow;

    :deep(.el-card__header) {
      background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
      color: $primary-color;
      border-radius: 12px 12px 0 0;

      .network-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .network-status {
          display: flex;
          align-items: center;
          gap: 15px;

          .epoch-info {
            font-size: 0.9rem;
            opacity: 0.9;
          }
        }
      }
    }

    .network-container {
      position: relative;
      height: 500px;
      overflow: hidden;

      .network-svg {
        width: 100%;
        height: 100%;
        background: linear-gradient(135deg, $primary-color 0%, $primary-gradient-end 100%);
        border-radius: 0 0 12px 12px;

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
          font-size: 12px;
          font-weight: 600;
          fill: #2c3e50;
          pointer-events: none;
        }
      }




    }
  }
}

.metrics-card {
  border-radius: 12px;
  background: $secondary-color;
  border: 1px solid $border-color;
  box-shadow: $box-shadow;

  :deep(.el-card__header) {
    background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
    color: $primary-color;
    border-radius: 12px 12px 0 0;

    span {
      font-weight: 600;
      display: flex;
      align-items: center;

      .el-icon {
        margin-right: 8px;
      }
    }
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
          color: #7f8c8d;
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

.node-details-panel {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  min-width: 400px;

  .node-details-card {
    border-radius: 12px;
    background: $secondary-color;
    border: 1px solid $border-color;
    box-shadow: 0 8px 24px rgba(24, 25, 26, 0.3);

    :deep(.el-card__header) {
      background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
      color: $primary-color;
      border-radius: 12px 12px 0 0;

      .node-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
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
          border-bottom: 1px solid #ecf0f1;

          &:last-child {
            border-bottom: none;
          }

          span:first-child {
            color: #7f8c8d;
            font-weight: 500;
          }

          .activation-value, .weight-value, .bias-value {
            font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
            font-weight: 600;
            color: #2c3e50;
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

.training-log {
  .log-card {
    border-radius: 12px;
    background: $secondary-color;
    border: 1px solid $border-color;
    box-shadow: $box-shadow;

    :deep(.el-card__header) {
      background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
      color: $primary-color;
      border-radius: 12px 12px 0 0;

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

// 网络结构配置对话框样式
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
      border: 1px solid #ecf0f1;
      border-radius: 8px;
      transition: all 0.3s ease;

      &:hover {
        border-color: #3498db;
        background: #f8f9fa;
      }

      &.disabled {
        background: #f5f5f5;
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
            color: #3498db;
          }
        }

        .layer-details {
          h4 {
            margin: 0 0 0.3rem 0;
            color: #2c3e50;
            font-size: 1rem;
          }

          p {
            margin: 0;
            color: #7f8c8d;
            font-size: 0.9rem;
          }
        }
      }

      .layer-control {
        display: flex;
        align-items: center;
        gap: 0.5rem;

        .unit-text {
          color: #7f8c8d;
          font-size: 0.9rem;
        }
      }
    }
  }

  .structure-preview {
    background: #f8f9fa;
    padding: 1.5rem;
    border-radius: 8px;

    h4 {
      margin: 0 0 1rem 0;
      color: #2c3e50;
    }

    .preview-network {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 1.5rem;
      padding: 1rem;
      background: white;
      border-radius: 8px;
      overflow-x: auto;

      .preview-layer {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin: 0 1rem;
        position: relative;

        .layer-name {
          font-size: 0.8rem;
          color: #7f8c8d;
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
            background: #3498db;
            border: 2px solid #2980b9;

            &.fixed {
              background: #e74c3c;
              border-color: #c0392b;
            }
          }
        }

        .node-count {
          font-size: 0.7rem;
          color: #7f8c8d;
        }

        .layer-connection {
          position: absolute;
          right: -1rem;
          top: 50%;
          width: 2rem;
          height: 2px;
          background: #bdc3c7;
          transform: translateY(-50%);

          &::after {
            content: '';
            position: absolute;
            right: -4px;
            top: -2px;
            width: 0;
            height: 0;
            border-left: 6px solid #bdc3c7;
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
        background: white;
        border-radius: 6px;

        .stat-label {
          display: block;
          font-size: 0.8rem;
          color: #7f8c8d;
          margin-bottom: 0.3rem;
        }

        .stat-value {
          display: block;
          font-size: 1.2rem;
          font-weight: 600;
          color: #2c3e50;
        }
      }
    }
  }
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

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideIn {
  from {
    transform: translateX(-100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@keyframes glow {
  0%, 100% {
    box-shadow: 0 0 5px rgba(52, 152, 219, 0.5);
  }
  50% {
    box-shadow: 0 0 20px rgba(52, 152, 219, 0.8);
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .main-control-panel {
    .el-row {
      flex-direction: column;
    }
  }

  .network-visualization-area {
    .el-row {
      flex-direction: column;
    }
  }
}

@media (max-width: 768px) {
  .network-visualization-container {
    padding: 1rem;
  }

  .visualization-header {
    h2 {
      font-size: 1.5rem;
    }

    p {
      font-size: 1rem;
    }
  }

  .network-container {
    height: 400px !important;
  }

  .node-details-panel {
    min-width: 90vw;
    max-width: 90vw;
  }

  .sample-images {
    grid-template-columns: repeat(2, 1fr) !important;
  }
}

@media (max-width: 480px) {
  .sample-images {
    grid-template-columns: 1fr !important;
  }

  .config-item {
    flex-direction: column !important;
    align-items: flex-start !important;
    gap: 8px;
  }
}

// Element Plus 组件样式覆盖
:deep(.el-card__body) {
  background: $secondary-color;
  color: $text-color;
}

:deep(.el-button) {
  &.el-button--primary {
    background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
    border: none;
    color: $primary-color;

    &:hover {
      background: linear-gradient(135deg, $accent-color-light 0%, $accent-color 100%);
      transform: translateY(-1px);
    }
  }

  &.el-button--default {
    background: $secondary-color;
    border: 1px solid $border-color;
    color: $text-color;

    &:hover {
      background: $primary-hover-color;
      border-color: $accent-color;
    }
  }
}

:deep(.el-slider) {
  .el-slider__runway {
    background: $border-color;
  }

  .el-slider__bar {
    background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
  }

  .el-slider__button {
    border: 2px solid $accent-color;
    background: $text-color;
  }
}

:deep(.el-select) {
  .el-input__inner {
    background: $secondary-color;
    border: 1px solid $border-color;
    color: $text-color;

    &:focus {
      border-color: $accent-color;
    }
  }
}

:deep(.el-input) {
  .el-input__inner {
    background: $secondary-color;
    border: 1px solid $border-color;
    color: $text-color;

    &:focus {
      border-color: $accent-color;
    }
  }
}

:deep(.el-progress) {
  .el-progress-bar__outer {
    background: $border-color;
  }

  .el-progress-bar__inner {
    background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
  }
}

:deep(.el-tag) {
  background: $secondary-color;
  border: 1px solid $border-color;
  color: $text-color;

  &.el-tag--success {
    background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
    border: none;
    color: $primary-color;
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
    color: $primary-color;
  }
}

// 深色主题支持
@media (prefers-color-scheme: dark) {
  .network-visualization-container {
    background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);

    .visualization-header {
      h2 {
        color: #ecf0f1;
      }

      p {
        color: #bdc3c7;
      }
    }

    .network-svg {
      background: linear-gradient(135deg, #34495e 0%, #2c3e50 100%) !important;
    }

    .layer-label {
      fill: #ecf0f1 !important;
    }
  }
}

// 打印样式
@media print {
  .network-visualization-container {
    background: white !important;

    .main-control-panel,
    .node-details-panel,
    .training-log {
      display: none !important;
    }

    .network-card {
      box-shadow: none !important;
      border: 1px solid #ddd !important;
    }
  }
}
</style>
