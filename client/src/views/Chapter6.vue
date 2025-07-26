<template>
  <div class="chapter6-container">
    <!-- 章节头部 -->
    <div class="chapter-header">
      <div class="header-content">
        <h1 class="chapter-title">
          <el-icon><Cpu /></el-icon>
          第六章：第一个人工智能项目
        </h1>
        <p class="chapter-subtitle">
          通过猫狗识别项目，体验完整的AI开发流程
        </p>
        <div class="progress-bar">
          <el-progress 
            :percentage="overallProgress" 
            :stroke-width="8"
            :show-text="true"
            status="success"
          />
          <span class="progress-text">学习进度: {{ overallProgress }}%</span>
        </div>
      </div>
    </div>

    <!-- 导航标签页 -->
    <div class="chapter-navigation">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="chapter-tabs">
        <el-tab-pane label="Python基础" name="python-basics">
          <el-icon><Document /></el-icon>
          Python基础
        </el-tab-pane>
        <el-tab-pane label="代码实践" name="code-practice">
          <el-icon><Edit /></el-icon>
          代码实践
        </el-tab-pane>
        <el-tab-pane label="数据流程" name="data-flow">
          <el-icon><DataLine /></el-icon>
          数据流程
        </el-tab-pane>
        <el-tab-pane label="模型结构" name="model-structure">
          <el-icon><Connection /></el-icon>
          模型结构
        </el-tab-pane>
        <el-tab-pane label="游戏化学习" name="gamified-learning">
          <el-icon><Trophy /></el-icon>
          游戏化学习
        </el-tab-pane>
        <el-tab-pane label="AI助手" name="ai-assistant">
          <el-icon><ChatDotRound /></el-icon>
          AI助手
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 内容区域 -->
    <div class="chapter-content">
      <!-- Python基础知识 -->
      <div v-show="activeTab === 'python-basics'" class="content-section">
        <PythonBasics @progress-update="updateProgress" />
      </div>

      <!-- 代码实践 -->
      <div v-show="activeTab === 'code-practice'" class="content-section">
        <CodeEditor @progress-update="updateProgress" />
      </div>

      <!-- 数据流程可视化 -->
      <div v-show="activeTab === 'data-flow'" class="content-section">
        <DataFlowVisualization @progress-update="updateProgress" />
      </div>

      <!-- 神经网络训练实验室入口 -->
      <div v-show="activeTab === 'model-structure'" class="content-section">
        <div class="network-training-entrance">
          <el-card class="entrance-card">
            <div class="entrance-content">
              <div class="entrance-header">
                <div class="icon-section">
                  <el-icon class="main-icon"><Connection /></el-icon>
                </div>
                <div class="title-section">
                  <h2>神经网络训练实验室</h2>
                  <p class="subtitle">交互式深度学习体验平台</p>
                </div>
              </div>

              <div class="features-grid">
                <div class="feature-item">
                  <el-icon><VideoPlay /></el-icon>
                  <h4>实时训练可视化</h4>
                  <p>观察数据在神经网络中的流动过程</p>
                </div>

                <div class="feature-item">
                  <el-icon><Mouse /></el-icon>
                  <h4>交互式节点控制</h4>
                  <p>点击节点启用/禁用，影响训练结果</p>
                </div>

                <div class="feature-item">
                  <el-icon><TrendCharts /></el-icon>
                  <h4>动态指标监控</h4>
                  <p>实时查看准确率和损失率变化</p>
                </div>

                <div class="feature-item">
                  <el-icon><Picture /></el-icon>
                  <h4>猫狗分类演示</h4>
                  <p>体验完整的图像分类训练流程</p>
                </div>
              </div>

              <div class="entrance-actions">
                <el-button
                  type="primary"
                  size="large"
                  @click="goToNetworkTraining"
                  class="launch-button"
                >
                  <el-icon><Rocket /></el-icon>
                  启动实验室
                </el-button>

                <el-button
                  type="info"
                  size="large"
                  @click="showPreview = true"
                  class="preview-button"
                >
                  <el-icon><View /></el-icon>
                  预览功能
                </el-button>
              </div>

              <div class="stats-section">
                <div class="stat-item">
                  <span class="stat-number">7</span>
                  <span class="stat-label">网络层数</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number">120+</span>
                  <span class="stat-label">神经元节点</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number">∞</span>
                  <span class="stat-label">交互可能</span>
                </div>
              </div>
            </div>
          </el-card>
        </div>

        <!-- 功能预览对话框 -->
        <el-dialog
          v-model="showPreview"
          title="神经网络训练实验室 - 功能预览"
          width="800px"
        >
          <div class="preview-content">
            <div class="preview-section">
              <h3>🎯 核心功能</h3>
              <ul>
                <li><strong>实时数据流可视化</strong>：观察数据在各个神经网络层之间的流动</li>
                <li><strong>交互式节点控制</strong>：点击任意节点来启用/禁用，实时影响训练</li>
                <li><strong>动态训练指标</strong>：准确率、损失率的实时监控和历史曲线</li>
                <li><strong>猫狗分类演示</strong>：完整的图像分类训练流程体验</li>
              </ul>
            </div>

            <div class="preview-section">
              <h3>🚀 学习收获</h3>
              <ul>
                <li>深入理解神经网络的前向传播和反向传播过程</li>
                <li>体验超参数对训练效果的实际影响</li>
                <li>掌握神经网络结构与性能的关系</li>
                <li>培养对深度学习的直观认知</li>
              </ul>
            </div>

            <div class="preview-section">
              <h3>⚡ 技术特色</h3>
              <ul>
                <li>基于Vue 3和SVG的高性能可视化</li>
                <li>Chart.js驱动的专业图表展示</li>
                <li>响应式设计，支持多设备访问</li>
                <li>实时动画效果，流畅的用户体验</li>
              </ul>
            </div>
          </div>

          <template #footer>
            <span class="dialog-footer">
              <el-button @click="showPreview = false">关闭预览</el-button>
              <el-button type="primary" @click="goToNetworkTraining">立即体验</el-button>
            </span>
          </template>
        </el-dialog>
      </div>

      <!-- 游戏化学习 -->
      <div v-show="activeTab === 'gamified-learning'" class="content-section">
        <GameifiedLearning @progress-update="updateProgress" />
      </div>

      <!-- AI助手 -->
      <div v-show="activeTab === 'ai-assistant'" class="content-section">
        <AIAssistant @progress-update="updateProgress" />
      </div>
    </div>

    <!-- 浮动的成就通知 -->
    <div v-if="showAchievement" class="achievement-notification">
      <el-card class="achievement-card">
        <div class="achievement-content">
          <el-icon class="achievement-icon"><Trophy /></el-icon>
          <div class="achievement-text">
            <h3>{{ achievementTitle }}</h3>
            <p>{{ achievementDescription }}</p>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Cpu, Document, Edit, DataLine, Connection,
  Trophy, ChatDotRound, VideoPlay, Mouse, TrendCharts,
  Picture, Rocket, View
} from '@element-plus/icons-vue'

// 导入组件
import PythonBasics from '../components/chapter6/PythonBasics.vue'
import CodeEditor from '../components/chapter6/CodeEditor.vue'
import DataFlowVisualization from '../components/chapter6/DataFlowVisualization.vue'
import NetworkVisualization from '../components/chapter6/NetworkVisualization.vue'
import GameifiedLearning from '../components/chapter6/GameifiedLearning.vue'
import AIAssistant from '../components/chapter6/AIAssistant.vue'

// 路由
const router = useRouter()

// 响应式数据
const activeTab = ref('python-basics')
const showPreview = ref(false)
const progressData = ref({
  'python-basics': 0,
  'code-practice': 0,
  'data-flow': 0,
  'model-structure': 0,
  'gamified-learning': 0,
  'ai-assistant': 0
})

// 成就系统
const showAchievement = ref(false)
const achievementTitle = ref('')
const achievementDescription = ref('')

// 计算总体进度
const overallProgress = computed(() => {
  const values = Object.values(progressData.value)
  const total = values.reduce((sum, val) => sum + val, 0)
  return Math.round(total / values.length)
})

// 方法
const handleTabClick = (tab) => {
  console.log('切换到标签页:', tab.name)
}

const goToNetworkTraining = () => {
  router.push('/network-training')
  ElMessage.success('正在启动神经网络训练实验室...')
}

const updateProgress = (tabName, progress) => {
  progressData.value[tabName] = progress
  
  // 检查是否达成成就
  if (progress === 100) {
    showAchievement.value = true
    achievementTitle.value = '模块完成！'
    achievementDescription.value = `恭喜完成${getTabLabel(tabName)}模块！`
    
    setTimeout(() => {
      showAchievement.value = false
    }, 3000)
  }
}

const getTabLabel = (tabName) => {
  const labels = {
    'python-basics': 'Python基础',
    'code-practice': '代码实践',
    'data-flow': '数据流程',
    'model-structure': '模型结构',
    'gamified-learning': '游戏化学习',
    'ai-assistant': 'AI助手'
  }
  return labels[tabName] || tabName
}

onMounted(() => {
  console.log('第六章页面已加载')
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.chapter6-container {
  min-height: 100vh;
  background: linear-gradient(135deg, $primary-color 0%, $secondary-color 100%);
  padding: 0;

  // 全局选中样式
  ::selection {
    background: $accent-color;
    color: #ffffff;
  }

  ::-moz-selection {
    background: $accent-color;
    color: #ffffff;
  }

  // Element Plus 组件样式覆盖
  :deep(.el-input__inner) {
    background: $secondary-color !important;
    border-color: $border-color !important;
    color: $text-color !important;

    &::placeholder {
      color: $text-secondary-color !important;
    }

    &::selection {
      background: $accent-color !important;
      color: #ffffff !important;
    }

    &::-moz-selection {
      background: $accent-color !important;
      color: #ffffff !important;
    }
  }

  :deep(.el-textarea__inner) {
    background: $secondary-color !important;
    border-color: $border-color !important;
    color: $text-color !important;

    &::placeholder {
      color: $text-secondary-color !important;
    }

    &::selection {
      background: $accent-color !important;
      color: #ffffff !important;
    }

    &::-moz-selection {
      background: $accent-color !important;
      color: #ffffff !important;
    }
  }

  :deep(.el-select .el-input__inner) {
    background: $secondary-color !important;
    border-color: $border-color !important;
    color: $text-color !important;
  }
}

.chapter-header {
  background: rgba(35, 39, 46, 0.8);
  backdrop-filter: blur(10px);
  padding: 2rem 0;
  text-align: center;
  border-bottom: 1px solid $border-color;

  .header-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 2rem;
  }

  .chapter-title {
    font-size: 3rem;
    color: $text-color;
    margin-bottom: 1rem;
    font-weight: 700;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);

    .el-icon {
      margin-right: 1rem;
      color: $accent-color;
    }
  }

  .chapter-subtitle {
    font-size: 1.2rem;
    color: $text-secondary-color;
    margin-bottom: 2rem;
  }

  .progress-bar {
    max-width: 400px;
    margin: 0 auto;

    .progress-text {
      color: $text-secondary-color;
      font-size: 0.9rem;
      margin-top: 0.5rem;
      display: block;
    }
  }
}

.chapter-navigation {
  background: rgba(35, 39, 46, 0.95);
  backdrop-filter: blur(10px);
  padding: 0 2rem;

  .chapter-tabs {
    max-width: 1200px;
    margin: 0 auto;

    :deep(.el-tabs__header) {
      margin: 0;
      border-bottom: 2px solid $border-color;
    }

    :deep(.el-tabs__item) {
      font-size: 1.1rem;
      font-weight: 600;
      padding: 1rem 2rem;
      color: $text-secondary-color;

      &.is-active {
        color: $accent-color;
      }

      &:hover {
        color: $accent-color-light;
      }

      .el-icon {
        margin-right: 0.5rem;
      }
    }
  }
}

.chapter-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;

  .content-section {
    background: $secondary-color;
    border-radius: 12px;
    box-shadow: $box-shadow;
    overflow: hidden;
    border: 1px solid $border-color;
  }
}

.achievement-notification {
  position: fixed;
  top: 100px;
  right: 20px;
  z-index: 1000;
  animation: slideInRight 0.5s ease-out;

  .achievement-card {
    background: linear-gradient(135deg, $accent-color, $accent-color-light);
    border: none;
    box-shadow: 0 8px 32px rgba(176, 179, 184, 0.3);

    .achievement-content {
      display: flex;
      align-items: center;

      .achievement-icon {
        font-size: 2rem;
        color: $primary-color;
        margin-right: 1rem;
      }

      .achievement-text {
        h3 {
          margin: 0 0 0.5rem 0;
          color: $primary-color;
          font-weight: 700;
        }

        p {
          margin: 0;
          color: $primary-color-light;
        }
      }
    }
  }
}

@keyframes slideInRight {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .chapter-header {
    .chapter-title {
      font-size: 2rem;
    }
    
    .chapter-subtitle {
      font-size: 1rem;
    }
  }
  
  .chapter-navigation {
    padding: 0 1rem;
    
    .chapter-tabs :deep(.el-tabs__item) {
      font-size: 0.9rem;
      padding: 0.8rem 1rem;
    }
  }
  
  .chapter-content {
    padding: 1rem;
  }
}

// 神经网络训练实验室入口样式
.network-training-entrance {
  .entrance-card {
    border-radius: 16px;
    border: none;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);

    .entrance-content {
      padding: 2rem;

      .entrance-header {
        display: flex;
        align-items: center;
        margin-bottom: 2rem;

        .icon-section {
          margin-right: 1.5rem;

          .main-icon {
            font-size: 4rem;
            color: #667eea;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
          }
        }

        .title-section {
          h2 {
            font-size: 2.5rem;
            font-weight: 600;
            color: #2c3e50;
            margin: 0 0 0.5rem 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
          }

          .subtitle {
            font-size: 1.2rem;
            color: #7f8c8d;
            margin: 0;
          }
        }
      }

      .features-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 1.5rem;
        margin-bottom: 2rem;

        .feature-item {
          text-align: center;
          padding: 1.5rem;
          background: rgba(102, 126, 234, 0.05);
          border-radius: 12px;
          border: 1px solid rgba(102, 126, 234, 0.1);
          transition: all 0.3s ease;

          &:hover {
            transform: translateY(-4px);
            box-shadow: 0 8px 24px rgba(102, 126, 234, 0.15);
            border-color: rgba(102, 126, 234, 0.3);
          }

          .el-icon {
            font-size: 2.5rem;
            color: #667eea;
            margin-bottom: 1rem;
          }

          h4 {
            font-size: 1.1rem;
            font-weight: 600;
            color: #2c3e50;
            margin: 0 0 0.5rem 0;
          }

          p {
            color: #7f8c8d;
            margin: 0;
            line-height: 1.5;
          }
        }
      }

      .entrance-actions {
        display: flex;
        justify-content: center;
        gap: 1rem;
        margin-bottom: 2rem;

        .launch-button {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          border: none;
          padding: 12px 32px;
          font-size: 1.1rem;
          font-weight: 600;
          border-radius: 8px;
          transition: all 0.3s ease;

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
          }
        }

        .preview-button {
          padding: 12px 32px;
          font-size: 1.1rem;
          border-radius: 8px;
          transition: all 0.3s ease;

          &:hover {
            transform: translateY(-2px);
          }
        }
      }

      .stats-section {
        display: flex;
        justify-content: center;
        gap: 3rem;

        .stat-item {
          text-align: center;

          .stat-number {
            display: block;
            font-size: 2rem;
            font-weight: 700;
            color: #667eea;
            margin-bottom: 0.5rem;
          }

          .stat-label {
            color: #7f8c8d;
            font-size: 0.9rem;
          }
        }
      }
    }
  }
}

.preview-content {
  .preview-section {
    margin-bottom: 2rem;

    h3 {
      color: #2c3e50;
      margin-bottom: 1rem;
      font-size: 1.2rem;
    }

    ul {
      list-style: none;
      padding: 0;

      li {
        padding: 0.5rem 0;
        color: #7f8c8d;
        line-height: 1.6;
        border-bottom: 1px solid #ecf0f1;

        &:last-child {
          border-bottom: none;
        }

        strong {
          color: #2c3e50;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .network-training-entrance {
    .entrance-content {
      padding: 1rem;

      .entrance-header {
        flex-direction: column;
        text-align: center;

        .icon-section {
          margin-right: 0;
          margin-bottom: 1rem;
        }

        .title-section h2 {
          font-size: 2rem;
        }
      }

      .features-grid {
        grid-template-columns: 1fr;
      }

      .entrance-actions {
        flex-direction: column;
        align-items: center;

        .launch-button,
        .preview-button {
          width: 100%;
          max-width: 300px;
        }
      }

      .stats-section {
        gap: 1.5rem;
      }
    }
  }
}
</style>
