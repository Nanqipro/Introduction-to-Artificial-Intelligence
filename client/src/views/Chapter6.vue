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
import { ref, computed, onMounted } from 'vue'
import { 
  Cpu, Document, Edit, DataLine, Connection, 
  Trophy, ChatDotRound 
} from '@element-plus/icons-vue'

// 导入组件
import PythonBasics from '../components/chapter6/PythonBasics.vue'
import CodeEditor from '../components/chapter6/CodeEditor.vue'
import DataFlowVisualization from '../components/chapter6/DataFlowVisualization.vue'
import NetworkVisualization from '../components/chapter6/NetworkVisualization.vue'
import GameifiedLearning from '../components/chapter6/GameifiedLearning.vue'
import AIAssistant from '../components/chapter6/AIAssistant.vue'

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
</style>
