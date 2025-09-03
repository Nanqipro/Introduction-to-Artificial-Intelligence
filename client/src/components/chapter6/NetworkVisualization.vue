<template>
  <div class="network-visualization-container">
    <!-- 头部组件 -->
    <VisualizationHeader />

    <!-- 主控制面板 -->
    <MainControlPanel 
      :is-training="isTraining"
      :training-speed="trainingSpeed"
      :learning-rate="learningRate"
      :batch-size="batchSize"
      :visualization-mode="visualizationMode"
      :selected-dataset-index="selectedDatasetIndex"
      :available-datasets="availableDatasets"
      @start-training="startTraining"
      @pause-training="pauseTraining"
      @reset-training="resetTraining"
      @select-dataset="selectDataset"
      @show-network-structure="showNetworkStructureDialog = true"
      @update:training-speed="trainingSpeed = $event"
      @update:learning-rate="learningRate = $event"
      @update:batch-size="batchSize = $event"
      @update:visualization-mode="visualizationMode = $event"
    />

    <!-- 神经网络可视化区域 -->
    <NetworkVisualizationArea 
      :is-training="isTraining"
      :current-epoch="currentEpoch"
      :total-epochs="totalEpochs"
      :network-layers="networkLayers"
      :connections="connections"
      :data-particles="dataParticles"
      :visualization-mode="visualizationMode"
      :current-accuracy="currentAccuracy"
      :current-loss="currentLoss"
      :learning-rate="learningRate"
      :accuracy-history="accuracyHistory"
      :loss-history="lossHistory"
      @toggle-node="toggleNode"
      @highlight-node="highlightNode"
      @unhighlight-node="unhighlightNode"
      @network-click="handleNetworkClick"
    />

    <!-- 节点详情面板 -->
    <NodeDetailsPanel 
      v-if="selectedNode"
      :selected-node="selectedNode"
      @close="selectedNode = null"
      @toggle-node="toggleSelectedNode"
      @reset-weights="resetNodeWeights"
    />

    <!-- 训练日志 -->
    <TrainingLog 
      :training-logs="trainingLogs"
      @clear-log="clearLog"
    />

    <!-- 网络结构配置对话框 -->
    <NetworkStructureDialog 
      v-model="showNetworkStructureDialog"
      :layer-configs="layerConfigs"
      :total-neurons="totalNeurons"
      :estimated-parameters="estimatedParameters"
      @apply="applyNetworkStructure"
      @reset="resetToDefault"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import Chart from 'chart.js/auto'

// 导入子组件
import VisualizationHeader from './components/VisualizationHeader.vue'
import MainControlPanel from './components/MainControlPanel.vue'
import NetworkVisualizationArea from './components/NetworkVisualizationArea.vue'
import NodeDetailsPanel from './components/NodeDetailsPanel.vue'
import TrainingLog from './components/TrainingLog.vue'
import NetworkStructureDialog from './components/NetworkStructureDialog.vue'

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
.network-visualization-container {
  padding: 1.5rem;
  background: var(--body-bg);
  min-height: 100vh;
  color: var(--text-color);
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: 
      radial-gradient(circle at 20% 80%, var(--glow-color) 0%, transparent 50%),
      radial-gradient(circle at 80% 20%, var(--glow-light) 0%, transparent 50%),
      radial-gradient(circle at 40% 40%, rgba(102, 126, 234, 0.1) 0%, transparent 50%);
    pointer-events: none;
  }

  ::selection {
    background: var(--accent-color);
    color: #ffffff;
  }

  ::-moz-selection {
    background: var(--accent-color);
    color: #ffffff;
  }
}

/* 深色主题优化 - 完全重新设计 */
html.dark-theme .network-visualization-container {
  background: var(--body-bg);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: 
      radial-gradient(ellipse 800px 600px at 20% 30%, var(--bg-glow-blue) 0%, transparent 60%),
      radial-gradient(ellipse 600px 800px at 80% 70%, var(--bg-glow-green) 0%, transparent 60%),
      radial-gradient(ellipse 400px 400px at 50% 20%, var(--bg-glow-orange) 0%, transparent 50%),
      radial-gradient(ellipse 300px 300px at 10% 80%, var(--bg-glow-gold) 0%, transparent 40%);
    pointer-events: none;
    z-index: 0;
  }

  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: 
      linear-gradient(90deg, transparent 0%, var(--bg-glow-blue) 15%, transparent 30%),
      linear-gradient(180deg, transparent 0%, var(--bg-glow-green) 25%, transparent 40%),
      linear-gradient(45deg, transparent 0%, var(--bg-glow-orange) 35%, transparent 50%);
    animation: subtleFlow 20s ease-in-out infinite;
    pointer-events: none;
    z-index: 0;
  }

  @keyframes subtleFlow {
    0%, 100% { 
      opacity: 0.2; 
      transform: translateX(0) translateY(0);
    }
    25% { 
      opacity: 0.4; 
      transform: translateX(-10px) translateY(-5px);
    }
    50% { 
      opacity: 0.3; 
      transform: translateX(5px) translateY(-10px);
    }
    75% { 
      opacity: 0.5; 
      transform: translateX(-5px) translateY(5px);
    }
  }
}

/* 浅色主题适配 */
html.light-theme .network-visualization-container {
  background: #ffffff;
}

// 响应式设计
@media (max-width: 768px) {
  .network-visualization-container {
    padding: 1rem;
  }
}
</style>
