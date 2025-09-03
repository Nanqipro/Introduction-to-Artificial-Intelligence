<template>
  <div class="chapter6-interactive">
    <!-- 交互式学习介绍 -->
    <div class="interactive-intro">
      <el-card class="intro-card">
        <div class="intro-content">
          <h2>
            <el-icon><Cpu /></el-icon>
            交互式学习体验
          </h2>
          <p>通过多种创新的教学方式，体验完整的AI项目开发流程</p>
          <div class="features-grid">
            <div class="feature-item">
              <el-icon><Document /></el-icon>
              <span>Python基础</span>
            </div>
            <div class="feature-item">
              <el-icon><Edit /></el-icon>
              <span>代码实践</span>
            </div>
            <div class="feature-item">
              <el-icon><DataLine /></el-icon>
              <span>数据流程</span>
            </div>
            <div class="feature-item">
              <el-icon><Connection /></el-icon>
              <span>神经网络实验室</span>
            </div>
            <div class="feature-item">
              <el-icon><Trophy /></el-icon>
              <span>游戏化学习</span>
            </div>
            <div class="feature-item">
              <el-icon><ChatDotRound /></el-icon>
              <span>AI助手</span>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 进度概览 -->
    <div class="progress-overview">
      <el-card class="progress-card">
        <template #header>
          <span>学习进度概览</span>
        </template>
        <div class="progress-content">
          <el-progress 
            :percentage="overallProgress" 
            :stroke-width="12"
            :show-text="true"
            status="success"
          />
          <p class="progress-text">总体完成度: {{ overallProgress }}%</p>
        </div>
      </el-card>
    </div>

    <!-- 导航标签页 -->
    <div class="chapter-navigation">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="chapter-tabs">
        <el-tab-pane label="Python基础" name="python-basics">
          <template #label>
            <span class="tab-label">
              <el-icon><Document /></el-icon>
              Python基础
            </span>
          </template>
        </el-tab-pane>
        <el-tab-pane label="代码实践" name="code-practice">
          <template #label>
            <span class="tab-label">
              <el-icon><Edit /></el-icon>
              代码实践
            </span>
          </template>
        </el-tab-pane>
        <el-tab-pane label="数据流程" name="data-flow">
          <template #label>
            <span class="tab-label">
              <el-icon><DataLine /></el-icon>
              数据流程
            </span>
          </template>
        </el-tab-pane>
        <el-tab-pane label="神经网络实验室" name="network-training">
          <template #label>
            <span class="tab-label">
              <el-icon><Connection /></el-icon>
              神经网络实验室
            </span>
          </template>
        </el-tab-pane>
        <el-tab-pane label="游戏化学习" name="gamified-learning">
          <template #label>
            <span class="tab-label">
              <el-icon><Trophy /></el-icon>
              游戏化学习
            </span>
          </template>
        </el-tab-pane>
        <el-tab-pane label="AI助手" name="ai-assistant">
          <template #label>
            <span class="tab-label">
              <el-icon><ChatDotRound /></el-icon>
              AI助手
            </span>
          </template>
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

      <!-- 神经网络实验室 - 直接跳转 -->
      <div v-show="activeTab === 'network-training'" class="content-section">
        <div class="network-training-redirect">
          <el-card class="redirect-card">
            <div class="redirect-content">
              <el-icon class="redirect-icon"><Connection /></el-icon>
              <h3>正在跳转到神经网络实验室...</h3>
              <p>请稍候，系统正在为您启动交互式训练环境</p>
            </div>
          </el-card>
        </div>
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
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Cpu, Document, Edit, DataLine, Connection,
  Trophy, ChatDotRound
} from '@element-plus/icons-vue'

// 导入组件
import PythonBasics from './PythonBasics.vue'
import CodeEditor from './CodeEditor.vue'
import DataFlowVisualization from './DataFlowVisualization.vue'
import GameifiedLearning from './GameifiedLearning.vue'
import AIAssistant from './AIAssistant.vue'

// 路由
const router = useRouter()

// 响应式数据
const activeTab = ref('python-basics')
const progressData = ref({
  'python-basics': 0,
  'code-practice': 0,
  'data-flow': 0,
  'network-training': 0,
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

  // 如果点击的是神经网络实验室标签页，直接跳转到独立页面
  if (tab.name === 'network-training') {
    goToNetworkTraining()
    // 重置回之前的标签页，避免显示空白内容
    setTimeout(() => {
      activeTab.value = 'data-flow'
    }, 100)
  }
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
    'network-training': '神经网络实验室',
    'gamified-learning': '游戏化学习',
    'ai-assistant': 'AI助手'
  }
  return labels[tabName] || tabName
}
</script>

<style lang="scss" scoped>


.chapter6-interactive {
  // 全局选中样式
  ::selection {
    background: var(--accent-color);
    color: #ffffff;
  }
  
  ::-moz-selection {
    background: var(--accent-color);
    color: #ffffff;
  }
}

.interactive-intro {
  margin-bottom: 2rem;
  
  .intro-card {
    background: var(--intro-card-bg);
    border: 1px solid var(--intro-card-border);
    
    :deep(.el-card__body) {
      background: var(--intro-card-bg);
    }
    
    .intro-content {
      text-align: center;
      
      h2 {
        color: var(--text-color);
        margin-bottom: 1rem;
        font-size: 2rem;
        
        .el-icon {
          margin-right: 0.5rem;
          color: var(--accent-color);
        }
      }
      
      p {
        color: var(--text-secondary-color);
        font-size: 1.1rem;
        margin-bottom: 2rem;
      }
      
      .features-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
        gap: 1rem;
        
        .feature-item {
          display: flex;
          flex-direction: column;
          align-items: center;
          padding: 1rem;
          background: var(--feature-item-bg);
          border-radius: 8px;
          border: 1px solid var(--feature-item-border);
          transition: all 0.3s ease;
          
          &:hover {
            background: var(--feature-item-hover-bg);
            transform: translateY(-2px);
            box-shadow: var(--feature-item-hover-shadow);
          }
          
          .el-icon {
            font-size: 2rem;
            color: var(--accent-color);
            margin-bottom: 0.5rem;
          }
          
          span {
            color: var(--text-color);
            font-weight: 600;
          }
        }
      }
    }
  }
}

.progress-overview {
  margin-bottom: 2rem;
  
  .progress-card {
    background: var(--progress-card-bg);
    border: 1px solid var(--progress-card-border);
    
    :deep(.el-card__header) {
      background: var(--progress-card-header-bg);
      border-bottom: 1px solid var(--progress-card-border);
      color: var(--text-color);
      font-weight: 600;
    }
    
    :deep(.el-card__body) {
      background: var(--progress-card-bg);
    }
    
    .progress-content {
      text-align: center;
      
      .progress-text {
        margin-top: 1rem;
        color: var(--text-secondary-color);
        font-size: 1.1rem;
      }
    }
  }
}

.chapter-navigation {
  background: var(--chapter-nav-bg);
  backdrop-filter: blur(10px);
  padding: 0 2rem;
  border-radius: 8px;
  margin-bottom: 2rem;
  
  .chapter-tabs {
    :deep(.el-tabs__header) {
      margin: 0;
      border-bottom: 2px solid var(--chapter-nav-border);
    }
    
    :deep(.el-tabs__item) {
      font-size: 1rem;
      font-weight: 600;
      padding: 1rem 1.5rem;
      color: var(--tab-item-color);
      
      &.is-active {
        color: var(--tab-item-active-color);
      }
      
      &:hover {
        color: var(--tab-item-hover-color);
      }
    }
    
    .tab-label {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }
  }
}

.chapter-content {
  .content-section {
    background: var(--content-section-bg);
    border-radius: 12px;
    box-shadow: var(--box-shadow);
    overflow: hidden;
    border: 1px solid var(--content-section-border);
  }
}

.achievement-notification {
  position: fixed;
  top: 100px;
  right: 20px;
  z-index: 1000;
  animation: slideInRight 0.5s ease-out;
  
  .achievement-card {
    background: var(--achievement-card-bg);
    border: none;
    box-shadow: var(--achievement-card-shadow);
    
    .achievement-content {
      display: flex;
      align-items: center;
      
      .achievement-icon {
        font-size: 2rem;
        color: var(--primary-color);
        margin-right: 1rem;
      }
      
      .achievement-text {
        h3 {
          margin: 0 0 0.5rem 0;
          color: var(--primary-color);
          font-weight: 700;
        }
        
        p {
          margin: 0;
          color: var(--secondary-color);
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

// 神经网络实验室重定向样式
.network-training-redirect {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;

  .redirect-card {
    background: var(--redirect-card-bg);
    border: none;
    border-radius: 20px;
    box-shadow: var(--redirect-card-shadow);

    :deep(.el-card__body) {
      background: transparent;
      text-align: center;
      padding: 3rem;
    }

    .redirect-content {
      color: white;

      .redirect-icon {
        font-size: 4rem;
        margin-bottom: 1.5rem;
        animation: pulse 2s infinite;
      }

      h3 {
        font-size: 1.8rem;
        margin-bottom: 1rem;
        font-weight: 600;
      }

      p {
        font-size: 1.1rem;
        opacity: 0.9;
        margin: 0;
      }
    }
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
}

/* 浅色主题适配 */
html.light-theme .chapter6-interactive {
  .interactive-intro {
    .intro-card {
      background: var(--intro-card-bg);
      border: 1px solid var(--intro-card-border);
      box-shadow: var(--box-shadow);
    }

    .intro-content {
      h2 {
        color: var(--text-color);
      }

      p {
        color: var(--text-secondary-color);
      }

      .features-grid {
        .feature-item {
          background: var(--feature-item-bg);
          border: 1px solid var(--feature-item-border);
          color: var(--text-color);

          &:hover {
            background: var(--feature-item-hover-bg);
            transform: translateY(-2px);
            box-shadow: var(--feature-item-hover-shadow);
          }
        }
      }
    }
  }

  .progress-overview {
    .progress-card {
      background: var(--progress-card-bg);
      border: 1px solid var(--progress-card-border);
      box-shadow: var(--box-shadow);
    }

    .progress-content {
      .progress-text {
        color: var(--text-secondary-color);
      }
    }
  }

  .chapter-navigation {
    .chapter-tabs {
      :deep(.el-tabs__header) {
        background: var(--intro-card-bg);
        border-bottom: 1px solid var(--intro-card-border);
      }

      :deep(.el-tabs__item) {
        color: var(--tab-item-color);
        background: var(--feature-item-bg);
        border: 1px solid var(--feature-item-border);

        &:hover {
          color: var(--accent-color);
          background: var(--feature-item-hover-bg);
        }

        &.is-active {
          color: var(--accent-color);
          background: var(--intro-card-bg);
          border-bottom: 2px solid var(--accent-color);
        }
      }
    }
  }

  .chapter-content {
    .content-section {
      background: var(--content-section-bg);
      border: 1px solid var(--content-section-border);
      box-shadow: var(--box-shadow);
    }
  }

  .achievement-notification {
    .achievement-card {
      background: var(--btn-primary-bg);
      color: white;
      box-shadow: var(--box-shadow);

      .achievement-content {
        .achievement-icon {
          color: white;
        }

        .achievement-text {
          h3 {
            color: white;
          }

          p {
            color: rgba(255, 255, 255, 0.9);
          }
        }
      }
    }
  }

  .network-training-redirect {
    .redirect-card {
      background: var(--redirect-card-bg);
      color: white;
      box-shadow: var(--redirect-card-shadow);

      .redirect-content {
        color: white;

        h3 {
          color: white;
        }

        p {
          color: rgba(255, 255, 255, 0.9);
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .features-grid {
    grid-template-columns: repeat(2, 1fr) !important;
  }
  
  .chapter-navigation {
    padding: 0 1rem;
    
    .chapter-tabs :deep(.el-tabs__item) {
      font-size: 0.9rem;
      padding: 0.8rem 1rem;
    }
  }
}
</style>
