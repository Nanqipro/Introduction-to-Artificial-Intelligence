<template>
  <div class="data-flow-container">
    <div class="flow-header">
      <h2>
        <el-icon><DataLine /></el-icon>
        数据处理流程可视化
      </h2>
      <p>直观了解从原始数据到模型训练的完整流程</p>
    </div>

    <!-- 流程控制面板 -->
    <div class="control-panel">
      <el-card class="control-card">
        <div class="control-content">
          <div class="control-buttons">
            <el-button 
              type="primary" 
              @click="startAnimation"
              :disabled="isAnimating"
              size="large"
            >
              <el-icon><VideoPlay /></el-icon>
              开始演示
            </el-button>
            
            <el-button 
              type="warning" 
              @click="pauseAnimation"
              :disabled="!isAnimating"
              size="large"
            >
              <el-icon><VideoPause /></el-icon>
              暂停
            </el-button>
            
            <el-button 
              type="info" 
              @click="resetAnimation"
              size="large"
            >
              <el-icon><RefreshRight /></el-icon>
              重置
            </el-button>
          </div>
          
          <div class="speed-control">
            <span>动画速度：</span>
            <el-slider
              v-model="animationSpeed"
              :min="0.5"
              :max="3"
              :step="0.5"
              :show-tooltip="false"
              style="width: 150px; margin-left: 10px;"
            />
            <span>{{ animationSpeed }}x</span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 数据流程图 -->
    <div class="flow-diagram">
      <el-card class="diagram-card">
        <div class="flow-steps">
          <!-- 步骤1: 原始数据 -->
          <div 
            class="flow-step" 
            :class="{ active: currentStep >= 0, completed: currentStep > 0 }"
          >
            <div class="step-icon">
              <el-icon><Folder /></el-icon>
            </div>
            <div class="step-content">
              <h3>原始数据</h3>
              <p>猫狗图片数据集</p>
              <div class="step-details">
                <el-tag size="small">2819张训练图片</el-tag>
                <el-tag size="small" type="success">581张验证图片</el-tag>
              </div>
            </div>
            <div class="data-preview" v-if="currentStep >= 0">
              <div class="sample-images">
                <div class="image-placeholder cat">🐱</div>
                <div class="image-placeholder dog">🐶</div>
              </div>
            </div>
          </div>

          <!-- 连接线 -->
          <div class="flow-arrow" :class="{ active: currentStep >= 1 }">
            <el-icon><ArrowRight /></el-icon>
          </div>

          <!-- 步骤2: 数据预处理 -->
          <div 
            class="flow-step" 
            :class="{ active: currentStep >= 1, completed: currentStep > 1 }"
          >
            <div class="step-icon">
              <el-icon><Setting /></el-icon>
            </div>
            <div class="step-content">
              <h3>数据预处理</h3>
              <p>图像处理与增强</p>
              <div class="step-details">
                <el-tag size="small">尺寸调整: 224×224</el-tag>
                <el-tag size="small" type="warning">数据增强</el-tag>
                <el-tag size="small" type="info">标准化</el-tag>
              </div>
            </div>
            <div class="processing-animation" v-if="currentStep >= 1">
              <div class="processing-steps">
                <div class="process-item" :class="{ active: processingStep >= 0 }">
                  <span>调整大小</span>
                </div>
                <div class="process-item" :class="{ active: processingStep >= 1 }">
                  <span>随机旋转</span>
                </div>
                <div class="process-item" :class="{ active: processingStep >= 2 }">
                  <span>标准化</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 连接线 -->
          <div class="flow-arrow" :class="{ active: currentStep >= 2 }">
            <el-icon><ArrowRight /></el-icon>
          </div>

          <!-- 步骤3: 数据加载 -->
          <div 
            class="flow-step" 
            :class="{ active: currentStep >= 2, completed: currentStep > 2 }"
          >
            <div class="step-icon">
              <el-icon><Upload /></el-icon>
            </div>
            <div class="step-content">
              <h3>数据加载</h3>
              <p>批次处理</p>
              <div class="step-details">
                <el-tag size="small">批次大小: 4</el-tag>
                <el-tag size="small" type="success">随机打乱</el-tag>
              </div>
            </div>
            <div class="batch-visualization" v-if="currentStep >= 2">
              <div class="batch-container">
                <div class="batch-item" v-for="i in 4" :key="i">
                  <div class="mini-image">{{ i }}</div>
                </div>
              </div>
            </div>
          </div>

          <!-- 连接线 -->
          <div class="flow-arrow" :class="{ active: currentStep >= 3 }">
            <el-icon><ArrowRight /></el-icon>
          </div>

          <!-- 步骤4: 模型训练 -->
          <div 
            class="flow-step" 
            :class="{ active: currentStep >= 3, completed: currentStep > 3 }"
          >
            <div class="step-icon">
              <el-icon><Cpu /></el-icon>
            </div>
            <div class="step-content">
              <h3>模型训练</h3>
              <p>LeNet神经网络</p>
              <div class="step-details">
                <el-tag size="small">20个训练轮次</el-tag>
                <el-tag size="small" type="warning">SGD优化器</el-tag>
              </div>
            </div>
            <div class="training-progress" v-if="currentStep >= 3">
              <el-progress 
                :percentage="trainingProgress" 
                :stroke-width="8"
                :show-text="true"
                status="success"
              />
              <div class="training-metrics">
                <span>准确率: {{ (trainingProgress * 0.8).toFixed(1) }}%</span>
              </div>
            </div>
          </div>

          <!-- 连接线 -->
          <div class="flow-arrow" :class="{ active: currentStep >= 4 }">
            <el-icon><ArrowRight /></el-icon>
          </div>

          <!-- 步骤5: 模型评估 -->
          <div 
            class="flow-step" 
            :class="{ active: currentStep >= 4, completed: currentStep > 4 }"
          >
            <div class="step-icon">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="step-content">
              <h3>模型评估</h3>
              <p>性能分析</p>
              <div class="step-details">
                <el-tag size="small" type="success">准确率: 85%</el-tag>
                <el-tag size="small" type="info">混淆矩阵</el-tag>
              </div>
            </div>
            <div class="evaluation-results" v-if="currentStep >= 4">
              <div class="metrics-grid">
                <div class="metric-item">
                  <div class="metric-value">85%</div>
                  <div class="metric-label">准确率</div>
                </div>
                <div class="metric-item">
                  <div class="metric-value">0.12</div>
                  <div class="metric-label">损失值</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 详细信息面板 -->
    <div class="info-panel">
      <el-card class="info-card">
        <template #header>
          <span>当前步骤详情</span>
        </template>
        
        <div class="step-info">
          <h3>{{ getCurrentStepInfo().title }}</h3>
          <p>{{ getCurrentStepInfo().description }}</p>
          
          <div class="technical-details">
            <h4>技术细节：</h4>
            <ul>
              <li v-for="detail in getCurrentStepInfo().details" :key="detail">
                {{ detail }}
              </li>
            </ul>
          </div>
        </div>
      </el-card>
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
            已完成 {{ Math.floor(learningProgress / 20) }} / 5 个流程步骤
          </p>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { 
  DataLine, VideoPlay, VideoPause, RefreshRight,
  Folder, Setting, Upload, Cpu, TrendCharts,
  ArrowRight
} from '@element-plus/icons-vue'

// 定义事件
const emit = defineEmits(['progress-update'])

// 响应式数据
const currentStep = ref(-1)
const processingStep = ref(-1)
const trainingProgress = ref(0)
const isAnimating = ref(false)
const animationSpeed = ref(1)
const animationTimer = ref(null)

// 步骤信息
const stepInfos = ref([
  {
    title: '原始数据收集',
    description: '收集和整理猫狗图片数据集，为模型训练做准备。',
    details: [
      '数据集包含3400张高质量图片',
      '按照8:2比例划分训练集和验证集',
      '图片格式统一为JPEG',
      '每个类别数据量基本平衡'
    ]
  },
  {
    title: '数据预处理',
    description: '对原始图片进行标准化处理，提高模型训练效果。',
    details: [
      '将图片尺寸统一调整为224×224像素',
      '应用随机旋转和透视变换进行数据增强',
      '使用ImageNet标准进行像素值归一化',
      '转换为PyTorch张量格式'
    ]
  },
  {
    title: '数据加载器',
    description: '创建数据加载器，实现批次化训练。',
    details: [
      '设置批次大小为4，平衡内存使用和训练效率',
      '训练集数据随机打乱，提高模型泛化能力',
      '验证集保持固定顺序，确保评估一致性',
      '使用多线程加速数据加载过程'
    ]
  },
  {
    title: '模型训练',
    description: '使用LeNet网络架构训练猫狗分类模型。',
    details: [
      '采用LeNet-5卷积神经网络架构',
      '使用SGD优化器，学习率设为0.001',
      '训练20个epoch，监控损失和准确率',
      '应用交叉熵损失函数进行分类'
    ]
  },
  {
    title: '模型评估',
    description: '评估训练好的模型性能，分析结果。',
    details: [
      '在验证集上测试模型准确率',
      '生成混淆矩阵分析分类效果',
      '绘制训练过程中的损失和准确率曲线',
      '计算精确率、召回率等详细指标'
    ]
  }
])

// 计算属性
const learningProgress = computed(() => {
  return Math.min(100, (currentStep.value + 1) * 20)
})

// 方法
const startAnimation = () => {
  if (isAnimating.value) return
  
  isAnimating.value = true
  currentStep.value = -1
  processingStep.value = -1
  trainingProgress.value = 0
  
  animateSteps()
}

const pauseAnimation = () => {
  isAnimating.value = false
  if (animationTimer.value) {
    clearTimeout(animationTimer.value)
  }
}

const resetAnimation = () => {
  pauseAnimation()
  currentStep.value = -1
  processingStep.value = -1
  trainingProgress.value = 0
}

const animateSteps = () => {
  const stepDuration = 2000 / animationSpeed.value
  
  // 步骤0: 原始数据
  animationTimer.value = setTimeout(() => {
    if (!isAnimating.value) return
    currentStep.value = 0
    
    // 步骤1: 数据预处理
    animationTimer.value = setTimeout(() => {
      if (!isAnimating.value) return
      currentStep.value = 1
      animateProcessing()
      
      // 步骤2: 数据加载
      animationTimer.value = setTimeout(() => {
        if (!isAnimating.value) return
        currentStep.value = 2
        
        // 步骤3: 模型训练
        animationTimer.value = setTimeout(() => {
          if (!isAnimating.value) return
          currentStep.value = 3
          animateTraining()
          
          // 步骤4: 模型评估
          animationTimer.value = setTimeout(() => {
            if (!isAnimating.value) return
            currentStep.value = 4
            
            // 动画完成
            setTimeout(() => {
              isAnimating.value = false
              emit('progress-update', 'data-flow', 100)
            }, stepDuration)
            
          }, stepDuration)
        }, stepDuration)
      }, stepDuration)
    }, stepDuration)
  }, stepDuration)
}

const animateProcessing = () => {
  const processingDuration = 500 / animationSpeed.value
  
  processingStep.value = 0
  setTimeout(() => {
    if (!isAnimating.value) return
    processingStep.value = 1
    setTimeout(() => {
      if (!isAnimating.value) return
      processingStep.value = 2
    }, processingDuration)
  }, processingDuration)
}

const animateTraining = () => {
  const trainingDuration = 2000 / animationSpeed.value
  const steps = 20
  const stepInterval = trainingDuration / steps
  
  let progress = 0
  const updateProgress = () => {
    if (!isAnimating.value) return
    progress += 5
    trainingProgress.value = Math.min(100, progress)
    
    if (progress < 100) {
      setTimeout(updateProgress, stepInterval)
    }
  }
  
  updateProgress()
}

const getCurrentStepInfo = () => {
  const index = Math.max(0, currentStep.value)
  return stepInfos.value[index] || stepInfos.value[0]
}

// 生命周期
onMounted(() => {
  // 初始化
})

onUnmounted(() => {
  if (animationTimer.value) {
    clearTimeout(animationTimer.value)
  }
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.data-flow-container {
  padding: 2rem;
  background: $secondary-color;
  min-height: 100vh;
}

.flow-header {
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
      display: flex;
      justify-content: space-between;
      align-items: center;

      .control-buttons {
        display: flex;
        gap: 1rem;
      }

      .speed-control {
        display: flex;
        align-items: center;
        color: $text-secondary-color;
      }
    }
  }
}

.flow-diagram {
  margin-bottom: 2rem;

  .diagram-card {
    background: $primary-color;
    border: 1px solid $border-color;

    :deep(.el-card__body) {
      background: $primary-color;
    }

    .flow-steps {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 2rem;
      overflow-x: auto;

      .flow-step {
        flex: 1;
        min-width: 200px;
        text-align: center;
        padding: 1.5rem;
        border-radius: 12px;
        border: 2px solid $border-color;
        background: $secondary-color;
        transition: all 0.3s ease;

        &.active {
          border-color: $accent-color;
          background: rgba(176, 179, 184, 0.1);
          transform: scale(1.05);
        }

        &.completed {
          border-color: $accent-color-light;
          background: rgba(209, 211, 216, 0.1);
        }
        
        .step-icon {
          font-size: 2rem;
          color: #7f8c8d;
          margin-bottom: 1rem;
          
          .el-icon {
            padding: 1rem;
            background: white;
            border-radius: 50%;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
          }
        }
        
        .step-content {
          h3 {
            color: #2c3e50;
            margin-bottom: 0.5rem;
            font-size: 1.1rem;
          }
          
          p {
            color: #7f8c8d;
            margin-bottom: 1rem;
            font-size: 0.9rem;
          }
          
          .step-details {
            display: flex;
            flex-direction: column;
            gap: 0.3rem;
          }
        }
        
        .data-preview {
          margin-top: 1rem;
          
          .sample-images {
            display: flex;
            justify-content: center;
            gap: 0.5rem;
            
            .image-placeholder {
              width: 40px;
              height: 40px;
              border-radius: 8px;
              display: flex;
              align-items: center;
              justify-content: center;
              font-size: 1.5rem;
              
              &.cat {
                background: #ffe4e1;
              }
              
              &.dog {
                background: #e1f5fe;
              }
            }
          }
        }
        
        .processing-animation {
          margin-top: 1rem;
          
          .processing-steps {
            display: flex;
            flex-direction: column;
            gap: 0.3rem;
            
            .process-item {
              padding: 0.3rem 0.6rem;
              background: #f0f0f0;
              border-radius: 4px;
              font-size: 0.8rem;
              transition: all 0.3s ease;
              
              &.active {
                background: #3498db;
                color: white;
              }
            }
          }
        }
        
        .batch-visualization {
          margin-top: 1rem;
          
          .batch-container {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 0.3rem;
            
            .batch-item {
              .mini-image {
                width: 30px;
                height: 30px;
                background: #3498db;
                color: white;
                border-radius: 4px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 0.8rem;
                margin: 0 auto;
              }
            }
          }
        }
        
        .training-progress {
          margin-top: 1rem;
          
          .training-metrics {
            margin-top: 0.5rem;
            font-size: 0.8rem;
            color: #7f8c8d;
          }
        }
        
        .evaluation-results {
          margin-top: 1rem;
          
          .metrics-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 0.5rem;
            
            .metric-item {
              text-align: center;
              
              .metric-value {
                font-size: 1.2rem;
                font-weight: bold;
                color: #27ae60;
              }
              
              .metric-label {
                font-size: 0.8rem;
                color: #7f8c8d;
              }
            }
          }
        }
      }
      
      .flow-arrow {
        flex: 0 0 auto;
        margin: 0 1rem;
        font-size: 1.5rem;
        color: #bdc3c7;
        transition: color 0.3s ease;
        
        &.active {
          color: #3498db;
          animation: pulse 1s infinite;
        }
      }
    }
  }
}

.info-panel {
  margin-bottom: 2rem;
  
  .info-card {
    .step-info {
      h3 {
        color: #2c3e50;
        margin-bottom: 1rem;
      }
      
      p {
        color: #7f8c8d;
        line-height: 1.6;
        margin-bottom: 1.5rem;
      }
      
      .technical-details {
        h4 {
          color: #34495e;
          margin-bottom: 0.5rem;
        }
        
        ul {
          list-style: none;
          padding: 0;
          
          li {
            padding: 0.5rem 0;
            border-bottom: 1px solid #ecf0f1;
            color: #7f8c8d;
            
            &:last-child {
              border-bottom: none;
            }
            
            &:before {
              content: "▸";
              color: #3498db;
              margin-right: 0.5rem;
            }
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
        margin-top: 1rem;
        color: #7f8c8d;
      }
    }
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .flow-steps {
    flex-direction: column !important;
    
    .flow-arrow {
      transform: rotate(90deg);
      margin: 1rem 0 !important;
    }
  }
  
  .control-content {
    flex-direction: column !important;
    gap: 1rem;
  }
}
</style>
