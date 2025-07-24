<template>
  <div class="network-visualization-container">
    <div class="visualization-header">
      <h2>
        <el-icon><Connection /></el-icon>
        LeNet神经网络3D可视化
      </h2>
      <p>立体展示LeNet网络结构，理解深度学习模型架构</p>
    </div>

    <!-- 控制面板 -->
    <div class="control-panel">
      <el-card class="control-card">
        <div class="control-content">
          <div class="view-controls">
            <h4>视图控制</h4>
            <div class="control-buttons">
              <el-button-group>
                <el-button 
                  @click="resetView"
                  type="primary"
                  size="small"
                >
                  <el-icon><Refresh /></el-icon>
                  重置视图
                </el-button>
                <el-button 
                  @click="toggleAnimation"
                  :type="isAnimating ? 'warning' : 'success'"
                  size="small"
                >
                  <el-icon><VideoPlay v-if="!isAnimating" /><VideoPause v-else /></el-icon>
                  {{ isAnimating ? '暂停' : '播放' }}
                </el-button>
              </el-button-group>
            </div>
          </div>
          
          <div class="layer-selector">
            <h4>网络层选择</h4>
            <el-select v-model="selectedLayer" @change="highlightLayer" placeholder="选择网络层">
              <el-option
                v-for="layer in networkLayers"
                :key="layer.id"
                :label="layer.name"
                :value="layer.id"
              />
            </el-select>
          </div>
          
          <div class="visualization-mode">
            <h4>可视化模式</h4>
            <el-radio-group v-model="visualizationMode" @change="updateVisualization">
              <el-radio label="structure">结构视图</el-radio>
              <el-radio label="dataflow">数据流</el-radio>
              <el-radio label="activation">激活图</el-radio>
            </el-radio-group>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 3D可视化区域 -->
    <div class="visualization-area">
      <el-card class="visualization-card">
        <div ref="threeContainer" class="three-container"></div>
        
        <!-- 加载状态 -->
        <div v-if="isLoading" class="loading-overlay">
          <el-icon class="loading-icon"><Loading /></el-icon>
          <p>正在加载3D模型...</p>
        </div>
      </el-card>
    </div>

    <!-- 网络信息面板 -->
    <div class="info-panel">
      <el-row :gutter="20">
        <!-- 网络结构信息 -->
        <el-col :span="12">
          <el-card class="info-card">
            <template #header>
              <span>网络结构信息</span>
            </template>
            
            <div class="network-info">
              <div class="layer-list">
                <div 
                  v-for="layer in networkLayers" 
                  :key="layer.id"
                  class="layer-item"
                  :class="{ active: selectedLayer === layer.id }"
                  @click="selectLayer(layer.id)"
                >
                  <div class="layer-icon">
                    <el-icon>
                      <component :is="layer.icon" />
                    </el-icon>
                  </div>
                  <div class="layer-details">
                    <h4>{{ layer.name }}</h4>
                    <p>{{ layer.description }}</p>
                    <div class="layer-params">
                      <el-tag size="small">{{ layer.params }}</el-tag>
                      <el-tag size="small" type="info">{{ layer.output }}</el-tag>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <!-- 当前层详细信息 -->
        <el-col :span="12">
          <el-card class="info-card">
            <template #header>
              <span>{{ getCurrentLayerInfo().name }} 详细信息</span>
            </template>
            
            <div class="layer-details-info">
              <div class="detail-section">
                <h4>功能说明</h4>
                <p>{{ getCurrentLayerInfo().function }}</p>
              </div>
              
              <div class="detail-section">
                <h4>参数配置</h4>
                <ul>
                  <li v-for="param in getCurrentLayerInfo().parameters" :key="param">
                    {{ param }}
                  </li>
                </ul>
              </div>
              
              <div class="detail-section">
                <h4>数学原理</h4>
                <div class="math-formula" v-html="getCurrentLayerInfo().formula"></div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 学习进度 -->
    <div class="learning-progress">
      <el-card class="progress-card">
        <div class="progress-content">
          <h3>学习进度</h3>
          <el-progress 
            :percentage="learningProgress" 
            :stroke-width="12"
            :show-text="true"
            status="success"
          />
          <p class="progress-text">
            已探索 {{ exploredLayers.length }} / {{ networkLayers.length }} 个网络层
          </p>
          
          <div class="achievements">
            <h4>🏆 成就解锁</h4>
            <div class="achievement-list">
              <el-tag 
                v-for="achievement in unlockedAchievements" 
                :key="achievement"
                type="success"
                size="small"
              >
                {{ achievement }}
              </el-tag>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { 
  Connection, Refresh, VideoPlay, VideoPause, Loading 
} from '@element-plus/icons-vue'
import * as THREE from 'three'

// 定义事件
const emit = defineEmits(['progress-update'])

// 响应式数据
const threeContainer = ref(null)
const selectedLayer = ref('conv1')
const visualizationMode = ref('structure')
const isLoading = ref(true)
const isAnimating = ref(false)
const exploredLayers = ref(['conv1'])

// Three.js 相关变量
let scene, camera, renderer, animationId
const networkObjects = ref([])

// 网络层定义
const networkLayers = ref([
  {
    id: 'input',
    name: '输入层',
    description: '224×224×3 RGB图像',
    params: '150,528参数',
    output: '224×224×3',
    icon: 'Picture',
    function: '接收原始图像数据，每个像素包含RGB三个通道的值',
    parameters: [
      '输入尺寸: 224×224像素',
      '通道数: 3 (RGB)',
      '数据类型: float32',
      '数值范围: [0, 1]'
    ],
    formula: '<code>Input: x ∈ ℝ^(224×224×3)</code>'
  },
  {
    id: 'conv1',
    name: '卷积层1',
    description: '第一个卷积层',
    params: '456参数',
    output: '220×220×6',
    icon: 'Grid',
    function: '使用6个5×5卷积核提取图像的基本特征，如边缘、线条等',
    parameters: [
      '卷积核数量: 6',
      '卷积核大小: 5×5',
      '步长: 1',
      '激活函数: ReLU'
    ],
    formula: '<code>y = ReLU(W * x + b)</code><br>其中 W 是卷积核权重，* 表示卷积操作'
  },
  {
    id: 'pool1',
    name: '池化层1',
    description: '最大池化层',
    params: '0参数',
    output: '110×110×6',
    icon: 'Crop',
    function: '通过最大池化操作减少特征图尺寸，保留重要特征，降低计算复杂度',
    parameters: [
      '池化类型: 最大池化',
      '池化窗口: 2×2',
      '步长: 2',
      '填充: 无'
    ],
    formula: '<code>y(i,j) = max(x(2i:2i+1, 2j:2j+1))</code>'
  },
  {
    id: 'conv2',
    name: '卷积层2',
    description: '第二个卷积层',
    params: '2,416参数',
    output: '106×106×16',
    icon: 'Grid',
    function: '使用16个5×5卷积核进一步提取更复杂的特征组合',
    parameters: [
      '卷积核数量: 16',
      '卷积核大小: 5×5',
      '步长: 1',
      '激活函数: ReLU'
    ],
    formula: '<code>y = ReLU(W * x + b)</code><br>输入通道数: 6，输出通道数: 16'
  },
  {
    id: 'pool2',
    name: '池化层2',
    description: '第二个最大池化层',
    params: '0参数',
    output: '53×53×16',
    icon: 'Crop',
    function: '进一步减少特征图尺寸，为全连接层做准备',
    parameters: [
      '池化类型: 最大池化',
      '池化窗口: 2×2',
      '步长: 2',
      '填充: 无'
    ],
    formula: '<code>y(i,j) = max(x(2i:2i+1, 2j:2j+1))</code>'
  },
  {
    id: 'fc1',
    name: '全连接层1',
    description: '第一个全连接层',
    params: '5,393,400参数',
    output: '120',
    icon: 'Connection',
    function: '将卷积层提取的特征映射到120维向量空间',
    parameters: [
      '输入维度: 44,944',
      '输出维度: 120',
      '激活函数: ReLU',
      '权重初始化: Xavier'
    ],
    formula: '<code>y = ReLU(Wx + b)</code><br>其中 W ∈ ℝ^(120×44944)'
  },
  {
    id: 'fc2',
    name: '全连接层2',
    description: '第二个全连接层',
    params: '10,164参数',
    output: '84',
    icon: 'Connection',
    function: '进一步压缩特征表示，为最终分类做准备',
    parameters: [
      '输入维度: 120',
      '输出维度: 84',
      '激活函数: ReLU',
      '正则化: Dropout'
    ],
    formula: '<code>y = ReLU(Wx + b)</code><br>其中 W ∈ ℝ^(84×120)'
  },
  {
    id: 'fc3',
    name: '输出层',
    description: '分类输出层',
    params: '170参数',
    output: '2',
    icon: 'Flag',
    function: '输出最终的分类结果，2个神经元分别对应猫和狗',
    parameters: [
      '输入维度: 84',
      '输出维度: 2',
      '激活函数: Softmax',
      '损失函数: 交叉熵'
    ],
    formula: '<code>y = Softmax(Wx + b)</code><br><code>P(class_i) = exp(y_i) / Σexp(y_j)</code>'
  }
])

// 计算属性
const learningProgress = computed(() => {
  return Math.round((exploredLayers.value.length / networkLayers.value.length) * 100)
})

const unlockedAchievements = computed(() => {
  const achievements = []
  if (exploredLayers.value.length >= 3) achievements.push('网络探索者')
  if (exploredLayers.value.length >= 6) achievements.push('结构分析师')
  if (exploredLayers.value.length === networkLayers.value.length) achievements.push('网络大师')
  return achievements
})

// 方法
const initThreeJS = () => {
  if (!threeContainer.value) return

  // 创建场景
  scene = new THREE.Scene()
  scene.background = new THREE.Color(0xf0f0f0)

  // 创建相机
  camera = new THREE.PerspectiveCamera(
    75,
    threeContainer.value.clientWidth / threeContainer.value.clientHeight,
    0.1,
    1000
  )
  camera.position.set(0, 5, 10)

  // 创建渲染器
  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(threeContainer.value.clientWidth, threeContainer.value.clientHeight)
  renderer.shadowMap.enabled = true
  renderer.shadowMap.type = THREE.PCFSoftShadowMap
  threeContainer.value.appendChild(renderer.domElement)

  // 添加光源
  const ambientLight = new THREE.AmbientLight(0x404040, 0.6)
  scene.add(ambientLight)

  const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8)
  directionalLight.position.set(10, 10, 5)
  directionalLight.castShadow = true
  scene.add(directionalLight)

  // 创建网络结构
  createNetworkStructure()

  // 添加控制器
  addControls()

  // 开始渲染循环
  animate()

  isLoading.value = false
}

const createNetworkStructure = () => {
  const layerPositions = [
    { x: -8, y: 0, z: 0 },   // input
    { x: -6, y: 0, z: 0 },   // conv1
    { x: -4, y: 0, z: 0 },   // pool1
    { x: -2, y: 0, z: 0 },   // conv2
    { x: 0, y: 0, z: 0 },    // pool2
    { x: 2, y: 0, z: 0 },    // fc1
    { x: 4, y: 0, z: 0 },    // fc2
    { x: 6, y: 0, z: 0 }     // fc3
  ]

  networkLayers.value.forEach((layer, index) => {
    const position = layerPositions[index]
    const layerObject = createLayerObject(layer, position)
    scene.add(layerObject)
    networkObjects.value.push({ id: layer.id, object: layerObject })
  })

  // 创建连接线
  createConnections(layerPositions)
}

const createLayerObject = (layer, position) => {
  const group = new THREE.Group()
  
  // 根据层类型创建不同的几何体
  let geometry, material
  
  if (layer.id.includes('conv')) {
    // 卷积层 - 立方体
    geometry = new THREE.BoxGeometry(1, 1, 1)
    material = new THREE.MeshLambertMaterial({ color: 0x3498db })
  } else if (layer.id.includes('pool')) {
    // 池化层 - 球体
    geometry = new THREE.SphereGeometry(0.5, 16, 16)
    material = new THREE.MeshLambertMaterial({ color: 0xe74c3c })
  } else if (layer.id.includes('fc')) {
    // 全连接层 - 圆柱体
    geometry = new THREE.CylinderGeometry(0.5, 0.5, 1, 16)
    material = new THREE.MeshLambertMaterial({ color: 0x27ae60 })
  } else {
    // 输入/输出层 - 平面
    geometry = new THREE.PlaneGeometry(1, 1)
    material = new THREE.MeshLambertMaterial({ color: 0xf39c12 })
  }
  
  const mesh = new THREE.Mesh(geometry, material)
  mesh.position.set(position.x, position.y, position.z)
  mesh.castShadow = true
  mesh.receiveShadow = true
  
  group.add(mesh)
  
  return group
}

const createConnections = (positions) => {
  for (let i = 0; i < positions.length - 1; i++) {
    const start = positions[i]
    const end = positions[i + 1]
    
    const geometry = new THREE.BufferGeometry().setFromPoints([
      new THREE.Vector3(start.x + 0.5, start.y, start.z),
      new THREE.Vector3(end.x - 0.5, end.y, end.z)
    ])
    
    const material = new THREE.LineBasicMaterial({ color: 0x95a5a6 })
    const line = new THREE.Line(geometry, material)
    scene.add(line)
  }
}

const addControls = () => {
  // 简单的鼠标控制
  let mouseDown = false
  let mouseX = 0
  let mouseY = 0

  const onMouseDown = (event) => {
    mouseDown = true
    mouseX = event.clientX
    mouseY = event.clientY
  }

  const onMouseUp = () => {
    mouseDown = false
  }

  const onMouseMove = (event) => {
    if (!mouseDown) return

    const deltaX = event.clientX - mouseX
    const deltaY = event.clientY - mouseY

    camera.position.x += deltaX * 0.01
    camera.position.y -= deltaY * 0.01

    mouseX = event.clientX
    mouseY = event.clientY
  }

  renderer.domElement.addEventListener('mousedown', onMouseDown)
  renderer.domElement.addEventListener('mouseup', onMouseUp)
  renderer.domElement.addEventListener('mousemove', onMouseMove)
}

const animate = () => {
  animationId = requestAnimationFrame(animate)
  
  if (isAnimating.value) {
    // 旋转网络结构
    networkObjects.value.forEach((obj, index) => {
      obj.object.rotation.y += 0.01 * (index + 1)
    })
  }
  
  camera.lookAt(0, 0, 0)
  renderer.render(scene, camera)
}

const selectLayer = (layerId) => {
  selectedLayer.value = layerId
  if (!exploredLayers.value.includes(layerId)) {
    exploredLayers.value.push(layerId)
  }
  highlightLayer()
}

const highlightLayer = () => {
  // 重置所有层的材质
  networkObjects.value.forEach(obj => {
    const mesh = obj.object.children[0]
    if (mesh && mesh.material) {
      mesh.material.emissive.setHex(0x000000)
    }
  })
  
  // 高亮选中的层
  const selectedObj = networkObjects.value.find(obj => obj.id === selectedLayer.value)
  if (selectedObj) {
    const mesh = selectedObj.object.children[0]
    if (mesh && mesh.material) {
      mesh.material.emissive.setHex(0x444444)
    }
  }
}

const getCurrentLayerInfo = () => {
  return networkLayers.value.find(layer => layer.id === selectedLayer.value) || networkLayers.value[0]
}

const resetView = () => {
  camera.position.set(0, 5, 10)
  camera.lookAt(0, 0, 0)
}

const toggleAnimation = () => {
  isAnimating.value = !isAnimating.value
}

const updateVisualization = () => {
  // 根据可视化模式更新显示
  console.log('更新可视化模式:', visualizationMode.value)
}

// 生命周期
onMounted(async () => {
  await nextTick()
  initThreeJS()
  
  // 监听进度变化
  emit('progress-update', 'model-structure', learningProgress.value)
})

onUnmounted(() => {
  if (animationId) {
    cancelAnimationFrame(animationId)
  }
  if (renderer) {
    renderer.dispose()
  }
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.network-visualization-container {
  padding: 2rem;
  background: $secondary-color;
  min-height: 100vh;

  // 全局选中样式
  ::selection {
    background: $accent-color;
    color: $primary-color;
  }

  ::-moz-selection {
    background: $accent-color;
    color: $primary-color;
  }
}

.visualization-header {
  text-align: center;
  margin-bottom: 2rem;

  h2 {
    color: $text-color;
    margin-bottom: 0.5rem;

    .el-icon {
      margin-right: 0.5rem;
      color: $accent-color;
    }
  }

  p {
    color: $text-secondary-color;
    font-size: 1.1rem;
  }
}

.control-panel {
  margin-bottom: 2rem;

  .control-card {
    background: $primary-color;
    border: 1px solid $border-color;

    :deep(.el-card__body) {
      background: $primary-color;
    }

    .control-content {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 2rem;

      h4 {
        color: $text-color;
        margin-bottom: 1rem;
        font-size: 1rem;
      }

      .control-buttons {
        display: flex;
        gap: 0.5rem;
      }
    }
  }
}

.visualization-area {
  margin-bottom: 2rem;
  
  .visualization-card {
    position: relative;
    
    .three-container {
      width: 100%;
      height: 500px;
      border-radius: 8px;
      overflow: hidden;
    }
    
    .loading-overlay {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(255, 255, 255, 0.9);
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      
      .loading-icon {
        font-size: 3rem;
        color: #3498db;
        animation: spin 1s linear infinite;
        margin-bottom: 1rem;
      }
      
      p {
        color: #7f8c8d;
        font-size: 1.1rem;
      }
    }
  }
}

.info-panel {
  margin-bottom: 2rem;
  
  .info-card {
    height: 400px;
    
    .network-info {
      .layer-list {
        .layer-item {
          display: flex;
          align-items: center;
          padding: 1rem;
          border-radius: 8px;
          margin-bottom: 0.5rem;
          cursor: pointer;
          transition: all 0.3s ease;
          border: 2px solid transparent;
          
          &:hover {
            background: #f8f9fa;
          }
          
          &.active {
            background: #e3f2fd;
            border-color: #3498db;
          }
          
          .layer-icon {
            margin-right: 1rem;
            font-size: 1.5rem;
            color: #3498db;
          }
          
          .layer-details {
            flex: 1;
            
            h4 {
              margin: 0 0 0.3rem 0;
              color: #2c3e50;
              font-size: 1rem;
            }
            
            p {
              margin: 0 0 0.5rem 0;
              color: #7f8c8d;
              font-size: 0.9rem;
            }
            
            .layer-params {
              display: flex;
              gap: 0.3rem;
            }
          }
        }
      }
    }
    
    .layer-details-info {
      .detail-section {
        margin-bottom: 1.5rem;
        
        h4 {
          color: #2c3e50;
          margin-bottom: 0.5rem;
          font-size: 1rem;
        }
        
        p {
          color: #7f8c8d;
          line-height: 1.6;
          margin-bottom: 1rem;
        }
        
        ul {
          list-style: none;
          padding: 0;
          
          li {
            padding: 0.3rem 0;
            color: #7f8c8d;
            border-bottom: 1px solid #ecf0f1;
            
            &:last-child {
              border-bottom: none;
            }
            
            &:before {
              content: "•";
              color: #3498db;
              margin-right: 0.5rem;
            }
          }
        }
        
        .math-formula {
          background: #f8f9fa;
          padding: 1rem;
          border-radius: 4px;
          border-left: 4px solid #3498db;
          
          :deep(code) {
            font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
            font-size: 0.9rem;
            color: #2c3e50;
          }
        }
      }
    }
  }
}

.learning-progress {
  .progress-card {
    .progress-content {
      text-align: center;
      
      h3 {
        color: #2c3e50;
        margin-bottom: 1rem;
      }
      
      .progress-text {
        margin: 1rem 0;
        color: #7f8c8d;
      }
      
      .achievements {
        margin-top: 2rem;
        
        h4 {
          color: #f39c12;
          margin-bottom: 1rem;
        }
        
        .achievement-list {
          display: flex;
          justify-content: center;
          gap: 0.5rem;
          flex-wrap: wrap;
        }
      }
    }
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .control-content {
    grid-template-columns: 1fr !important;
  }
  
  .three-container {
    height: 300px !important;
  }
  
  .info-panel .el-row {
    flex-direction: column;
  }
}
</style>
