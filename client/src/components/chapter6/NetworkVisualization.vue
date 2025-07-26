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
              <el-button
                type="primary"
                @click="startTraining"
                :disabled="isTraining"
                size="large"
                style="width: 100%; margin-bottom: 10px;"
              >
                <el-icon><VideoPlay /></el-icon>
                {{ isTraining ? '训练中...' : '开始训练' }}
              </el-button>

              <el-button
                type="warning"
                @click="pauseTraining"
                :disabled="!isTraining"
                size="large"
                style="width: 100%; margin-bottom: 10px;"
              >
                <el-icon><VideoPause /></el-icon>
                暂停训练
              </el-button>

              <el-button
                type="info"
                @click="resetTraining"
                size="large"
                style="width: 100%;"
              >
                <el-icon><Refresh /></el-icon>
                重置训练
              </el-button>

              <div class="speed-control" style="margin-top: 15px;">
                <span>训练速度：</span>
                <el-slider
                  v-model="trainingSpeed"
                  :min="0.5"
                  :max="3"
                  :step="0.5"
                  show-stops
                />
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 输入数据选择 -->
        <el-col :span="8">
          <el-card class="control-card">
            <template #header>
              <span><el-icon><Picture /></el-icon> 输入数据</span>
            </template>
            <div class="input-selection">
              <div class="sample-images">
                <div
                  v-for="(sample, index) in sampleImages"
                  :key="index"
                  class="sample-item"
                  :class="{ active: selectedSample === index }"
                  @click="selectSample(index)"
                >
                  <img :src="sample.url" :alt="sample.label" />
                  <span class="label">{{ sample.label }}</span>
                </div>
              </div>

              <div class="upload-area" @click="uploadImage">
                <el-icon><Upload /></el-icon>
                <span>上传自定义图片</span>
              </div>
              <input
                ref="fileInput"
                type="file"
                accept="image/*"
                @change="handleImageUpload"
                style="display: none;"
              />
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
                  <el-radio label="dataflow">数据流</el-radio>
                  <el-radio label="activation">激活值</el-radio>
                </el-radio-group>
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

              <!-- 输入图像显示 -->
              <div class="input-image-display">
                <img
                  v-if="currentInputImage"
                  :src="currentInputImage.url"
                  :alt="currentInputImage.label"
                  class="input-image"
                />
                <div class="image-info">
                  <span>输入: {{ currentInputImage?.label || '无' }}</span>
                </div>
              </div>

              <!-- 输出预测显示 -->
              <div class="output-prediction">
                <div class="prediction-item"
                     v-for="(prob, label) in currentPrediction"
                     :key="label"
                     :class="{ winner: prob === Math.max(...Object.values(currentPrediction)) }"
                >
                  <span class="label">{{ label }}</span>
                  <div class="probability-bar">
                    <div
                      class="probability-fill"
                      :style="{ width: `${prob * 100}%` }"
                    ></div>
                  </div>
                  <span class="probability">{{ (prob * 100).toFixed(1) }}%</span>
                </div>
              </div>
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
            <el-button @click="selectedNode = null" type="text" size="small">
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
            <el-button @click="clearLog" type="text" size="small">清空日志</el-button>
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'
import {
  Connection, Refresh, VideoPlay, VideoPause, Loading, Picture, Upload, Setting,
  TrendCharts, Close, Document
} from '@element-plus/icons-vue'
import Chart from 'chart.js/auto'

// 定义事件
const emit = defineEmits(['progress-update', 'training-complete'])

// 响应式数据
const networkContainer = ref(null)
const networkSvg = ref(null)
const chartContainer = ref(null)
const metricsChart = ref(null)
const fileInput = ref(null)

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

// 当前选中的样本和节点
const selectedSample = ref(0)
const selectedNode = ref(null)

// 当前输入和预测
const currentInputImage = ref(null)
const currentPrediction = ref({ cat: 0.5, dog: 0.5 })

// 数据流粒子
const dataParticles = ref([])
const connections = ref([])

// 训练日志
const trainingLogs = ref([])

// Chart.js 实例
let chartInstance = null
let animationId = null
let nodeInteractionCount = 0

// 样本图像数据
const sampleImages = ref([
  {
    url: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjQiIGhlaWdodD0iNjQiIHZpZXdCb3g9IjAgMCA2NCA2NCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjY0IiBoZWlnaHQ9IjY0IiBmaWxsPSIjRjNGNEY2Ii8+CjxwYXRoIGQ9Ik0zMiAyMEM0MC44MzY2IDIwIDQ4IDI3LjE2MzQgNDggMzZDNDggNDQuODM2NiA0MC44MzY2IDUyIDMyIDUyQzIzLjE2MzQgNTIgMTYgNDQuODM2NiAxNiAzNkMxNiAyNy4xNjM0IDIzLjE2MzQgMjAgMzIgMjBaIiBmaWxsPSIjRkY2QjM1Ii8+CjxwYXRoIGQ9Ik0yOCAzMkMzMC4yMDkxIDMyIDMyIDMwLjIwOTEgMzIgMjhDMzIgMjUuNzkwOSAzMC4yMDkxIDI0IDI4IDI0QzI1Ljc5MDkgMjQgMjQgMjUuNzkwOSAyNCAyOEMyNCAzMC4yMDkxIDI1Ljc5MDkgMzIgMjggMzJaIiBmaWxsPSIjMjEyMTIxIi8+CjxwYXRoIGQ9Ik0zNiAzMkMzOC4yMDkxIDMyIDQwIDMwLjIwOTEgNDAgMjhDNDAgMjUuNzkwOSAzOC4yMDkxIDI0IDM2IDI0QzMzLjc5MDkgMjQgMzIgMjUuNzkwOSAzMiAyOEMzMiAzMC4yMDkxIDMzLjc5MDkgMzIgMzYgMzJaIiBmaWxsPSIjMjEyMTIxIi8+CjxwYXRoIGQ9Ik0yNCA0MEMzMiA0NCA0MCA0MCA0MCA0MEMzNiA0NCAyOCA0NCAyNCA0MFoiIGZpbGw9IiMyMTIxMjEiLz4KPC9zdmc+',
    label: 'cat'
  },
  {
    url: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjQiIGhlaWdodD0iNjQiIHZpZXdCb3g9IjAgMCA2NCA2NCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjY0IiBoZWlnaHQ9IjY0IiBmaWxsPSIjRjNGNEY2Ii8+CjxwYXRoIGQ9Ik0zMiAyMEM0MC44MzY2IDIwIDQ4IDI3LjE2MzQgNDggMzZDNDggNDQuODM2NiA0MC44MzY2IDUyIDMyIDUyQzIzLjE2MzQgNTIgMTYgNDQuODM2NiAxNiAzNkMxNiAyNy4xNjM0IDIzLjE2MzQgMjAgMzIgMjBaIiBmaWxsPSIjOEI0NTEzIi8+CjxwYXRoIGQ9Ik0yOCAzMkMzMC4yMDkxIDMyIDMyIDMwLjIwOTEgMzIgMjhDMzIgMjUuNzkwOSAzMC4yMDkxIDI0IDI4IDI0QzI1Ljc5MDkgMjQgMjQgMjUuNzkwOSAyNCAyOEMyNCAzMC4yMDkxIDI1Ljc5MDkgMzIgMjggMzJaIiBmaWxsPSIjMjEyMTIxIi8+CjxwYXRoIGQ9Ik0zNiAzMkMzOC4yMDkxIDMyIDQwIDMwLjIwOTEgNDAgMjhDNDAgMjUuNzkwOSAzOC4yMDkxIDI0IDM2IDI0QzMzLjc5MDkgMjQgMzIgMjUuNzkwOSAzMiAyOEMzMiAzMC4yMDkxIDMzLjc5MDkgMzIgMzYgMzJaIiBmaWxsPSIjMjEyMTIxIi8+CjxwYXRoIGQ9Ik0yNCA0MEMzMiA0NCA0MCA0MCA0MCA0MEMzNiA0NCAyOCA0NCAyNCA0MFoiIGZpbGw9IiMyMTIxMjEiLz4KPC9zdmc+',
    label: 'dog'
  },
  {
    url: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjQiIGhlaWdodD0iNjQiIHZpZXdCb3g9IjAgMCA2NCA2NCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjY0IiBoZWlnaHQ9IjY0IiBmaWxsPSIjRjNGNEY2Ii8+CjxwYXRoIGQ9Ik0zMiAyMEM0MC44MzY2IDIwIDQ4IDI3LjE2MzQgNDggMzZDNDggNDQuODM2NiA0MC44MzY2IDUyIDMyIDUyQzIzLjE2MzQgNTIgMTYgNDQuODM2NiAxNiAzNkMxNiAyNy4xNjM0IDIzLjE2MzQgMjAgMzIgMjBaIiBmaWxsPSIjRkZEQjAwIi8+CjxwYXRoIGQ9Ik0yOCAzMkMzMC4yMDkxIDMyIDMyIDMwLjIwOTEgMzIgMjhDMzIgMjUuNzkwOSAzMC4yMDkxIDI0IDI4IDI0QzI1Ljc5MDkgMjQgMjQgMjUuNzkwOSAyNCAyOEMyNCAzMC4yMDkxIDI1Ljc5MDkgMzIgMjggMzJaIiBmaWxsPSIjMjEyMTIxIi8+CjxwYXRoIGQ9Ik0zNiAzMkMzOC4yMDkxIDMyIDQwIDMwLjIwOTEgNDAgMjhDNDAgMjUuNzkwOSAzOC4yMDkxIDI0IDM2IDI0QzMzLjc5MDkgMjQgMzIgMjUuNzkwOSAzMiAyOEMzMiAzMC4yMDkxIDMzLjc5MDkgMzIgMzYgMzJaIiBmaWxsPSIjMjEyMTIxIi8+CjxwYXRoIGQ9Ik0yNCA0MEMzMiA0NCA0MCA0MCA0MCA0MEMzNiA0NCAyOCA0NCAyNCA0MFoiIGZpbGw9IiMyMTIxMjEiLz4KPC9zdmc+',
    label: 'cat'
  }
])

// 网络层定义
const networkLayers = ref([
  {
    id: 'input',
    name: '输入层',
    nodes: [],
    labelX: 100,
    labelY: 30
  },
  {
    id: 'conv1',
    name: '卷积层1',
    nodes: [],
    labelX: 200,
    labelY: 30
  },
  {
    id: 'pool1',
    name: '池化层1',
    nodes: [],
    labelX: 300,
    labelY: 30
  },
  {
    id: 'conv2',
    name: '卷积层2',
    nodes: [],
    labelX: 400,
    labelY: 30
  },
  {
    id: 'pool2',
    name: '池化层2',
    nodes: [],
    labelX: 500,
    labelY: 30
  },
  {
    id: 'fc1',
    name: '全连接层1',
    nodes: [],
    labelX: 600,
    labelY: 30
  },
  {
    id: 'output',
    name: '输出层',
    nodes: [],
    labelX: 700,
    labelY: 30
  }
])

// 计算属性
const trainingProgress = computed(() => {
  return Math.round((currentEpoch.value / totalEpochs.value) * 100)
})

// 方法
const initializeNetwork = () => {
  // 初始化网络结构
  const layerSizes = [1, 6, 3, 16, 8, 120, 2] // 每层节点数
  const layerPositions = [100, 200, 300, 400, 500, 600, 700] // X坐标

  networkLayers.value.forEach((layer, layerIndex) => {
    const nodeCount = layerSizes[layerIndex]
    const x = layerPositions[layerIndex]
    const startY = (networkHeight.value - (nodeCount - 1) * 40) / 2

    layer.nodes = []
    for (let i = 0; i < nodeCount; i++) {
      layer.nodes.push({
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
  })

  // 初始化连接线
  initializeConnections()

  // 设置当前输入图像
  currentInputImage.value = sampleImages.value[selectedSample.value]
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

const selectSample = (index) => {
  selectedSample.value = index
  currentInputImage.value = sampleImages.value[index]

  // 重新运行前向传播
  if (isTraining.value) {
    forwardPass()
  }

  addLog('info', `选择了新的输入样本: ${currentInputImage.value.label}`)
}

const uploadImage = () => {
  fileInput.value?.click()
}

const handleImageUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    const reader = new FileReader()
    reader.onload = (e) => {
      const newImage = {
        url: e.target.result,
        label: 'custom'
      }
      sampleImages.value.push(newImage)
      selectSample(sampleImages.value.length - 1)
    }
    reader.readAsDataURL(file)
  }
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

// 监听器
watch(visualizationMode, (newMode) => {
  if (newMode === 'dataflow' && isTraining.value) {
    updateDataFlow()
  }
})

watch(selectedSample, (newIndex) => {
  selectSample(newIndex)
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
})

onUnmounted(() => {
  if (animationId) {
    cancelAnimationFrame(animationId)
  }
  if (chartInstance) {
    chartInstance.destroy()
  }
  isTraining.value = false
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.network-visualization-container {
  padding: 1.5rem;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
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
    color: #2c3e50;
    margin-bottom: 0.5rem;
    font-size: 2rem;
    font-weight: 600;

    .el-icon {
      margin-right: 0.5rem;
      color: #3498db;
    }
  }

  p {
    color: #7f8c8d;
    font-size: 1.1rem;
    max-width: 600px;
    margin: 0 auto;
  }
}

.main-control-panel {
  margin-bottom: 2rem;

  .control-card {
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border: none;

    :deep(.el-card__header) {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
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

    .training-controls, .input-selection, .network-config {
      padding: 1rem;
    }

    .speed-control {
      span {
        color: #7f8c8d;
        font-size: 0.9rem;
        margin-bottom: 8px;
        display: block;
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
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border: none;

    :deep(.el-card__header) {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
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
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
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

      .input-image-display {
        position: absolute;
        top: 20px;
        left: 20px;
        background: white;
        border-radius: 8px;
        padding: 10px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

        .input-image {
          width: 60px;
          height: 60px;
          border-radius: 4px;
          display: block;
        }

        .image-info {
          margin-top: 5px;
          text-align: center;

          span {
            font-size: 0.8rem;
            color: #7f8c8d;
          }
        }
      }

      .output-prediction {
        position: absolute;
        top: 20px;
        right: 20px;
        background: white;
        border-radius: 8px;
        padding: 15px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        min-width: 150px;

        .prediction-item {
          display: flex;
          align-items: center;
          margin-bottom: 10px;
          gap: 10px;

          &:last-child {
            margin-bottom: 0;
          }

          &.winner {
            .label {
              color: #e74c3c;
              font-weight: 600;
            }

            .probability {
              color: #e74c3c;
              font-weight: 600;
            }
          }

          .label {
            font-size: 0.9rem;
            color: #7f8c8d;
            min-width: 30px;
          }

          .probability-bar {
            flex: 1;
            height: 8px;
            background: #ecf0f1;
            border-radius: 4px;
            overflow: hidden;

            .probability-fill {
              height: 100%;
              background: linear-gradient(90deg, #3498db, #e74c3c);
              transition: width 0.3s ease;
            }
          }

          .probability {
            font-size: 0.8rem;
            color: #7f8c8d;
            min-width: 40px;
            text-align: right;
          }
        }
      }
    }
  }
}

.metrics-card {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border: none;

  :deep(.el-card__header) {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
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
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
    border: none;

    :deep(.el-card__header) {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
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
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border: none;

    :deep(.el-card__header) {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
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

    .input-image-display,
    .output-prediction {
      position: relative !important;
      top: auto !important;
      left: auto !important;
      right: auto !important;
      margin: 10px;
      display: inline-block;
    }
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
