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
              <span>模型结构</span>
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
        <el-tab-pane label="模型结构" name="model-structure">
          <template #label>
            <span class="tab-label">
              <el-icon><Connection /></el-icon>
              模型结构
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

      <!-- 模型结构3D可视化 -->
      <div v-show="activeTab === 'model-structure'" class="content-section">
        <NetworkVisualization @progress-update="updateProgress" />
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
import { 
  Cpu, Document, Edit, DataLine, Connection, 
  Trophy, ChatDotRound 
} from '@element-plus/icons-vue'

// 导入组件
import PythonBasics from './PythonBasics.vue'
import CodeEditor from './CodeEditor.vue'
import DataFlowVisualization from './DataFlowVisualization.vue'
import NetworkVisualization from './NetworkVisualization.vue'
import GameifiedLearning from './GameifiedLearning.vue'
import AIAssistant from './AIAssistant.vue'

// 响应式数据
const activeTab = ref('python-basics')
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
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.chapter6-interactive {
  // 全局选中样式
  ::selection {
    background: $accent-color;
    color: #ffffff;
  }
  
  ::-moz-selection {
    background: $accent-color;
    color: #ffffff;
  }
}

.interactive-intro {
  margin-bottom: 2rem;
  
  .intro-card {
    background: $primary-color;
    border: 1px solid $border-color;
    
    :deep(.el-card__body) {
      background: $primary-color;
    }
    
    .intro-content {
      text-align: center;
      
      h2 {
        color: $text-color;
        margin-bottom: 1rem;
        font-size: 2rem;
        
        .el-icon {
          margin-right: 0.5rem;
          color: $accent-color;
        }
      }
      
      p {
        color: $text-secondary-color;
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
          background: $secondary-color;
          border-radius: 8px;
          border: 1px solid $border-color;
          transition: all 0.3s ease;
          
          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(176, 179, 184, 0.2);
          }
          
          .el-icon {
            font-size: 2rem;
            color: $accent-color;
            margin-bottom: 0.5rem;
          }
          
          span {
            color: $text-color;
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
    background: $primary-color;
    border: 1px solid $border-color;
    
    :deep(.el-card__header) {
      background: $secondary-color;
      border-bottom: 1px solid $border-color;
      color: $text-color;
      font-weight: 600;
    }
    
    :deep(.el-card__body) {
      background: $primary-color;
    }
    
    .progress-content {
      text-align: center;
      
      .progress-text {
        margin-top: 1rem;
        color: $text-secondary-color;
        font-size: 1.1rem;
      }
    }
  }
}

.chapter-navigation {
  background: rgba(35, 39, 46, 0.95);
  backdrop-filter: blur(10px);
  padding: 0 2rem;
  border-radius: 8px;
  margin-bottom: 2rem;
  
  .chapter-tabs {
    :deep(.el-tabs__header) {
      margin: 0;
      border-bottom: 2px solid $border-color;
    }
    
    :deep(.el-tabs__item) {
      font-size: 1rem;
      font-weight: 600;
      padding: 1rem 1.5rem;
      color: $text-secondary-color;
      
      &.is-active {
        color: $accent-color;
      }
      
      &:hover {
        color: $accent-color-light;
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
