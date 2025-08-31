<template>
  <div class="network-training-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <el-button 
            @click="goBack" 
            type="primary" 
            :icon="ArrowLeft" 
            circle
            class="back-button"
          />
          <div class="title-section">
            <h1 class="page-title">
              <el-icon><Connection /></el-icon>
              交互式神经网络训练实验室
            </h1>
            <p class="page-subtitle">
              深度体验猫狗分类神经网络的完整训练过程
            </p>
          </div>
        </div>
        
        <div class="header-right">
          <el-tag type="success" size="large">
            <el-icon><Trophy /></el-icon>
            实验模式
          </el-tag>
        </div>
      </div>
    </div>

    <!-- 快速导航面板 -->
    <div class="quick-nav">
      <el-card class="nav-card">
        <div class="nav-content">
          <div class="nav-item">
            <el-icon><Document /></el-icon>
            <span>实验说明</span>
            <el-button @click="showInstructions = true" link size="small">
              查看
            </el-button>
          </div>
          
          <div class="nav-item">
            <el-icon><VideoPlay /></el-icon>
            <span>快速开始</span>
            <el-button @click="quickStart" type="primary" size="small">
              开始
            </el-button>
          </div>
          
          <div class="nav-item">
            <el-icon><Download /></el-icon>
            <span>导出结果</span>
            <el-button @click="exportResults" type="success" size="small">
              导出
            </el-button>
          </div>
          
          <div class="nav-item">
            <el-icon><Share /></el-icon>
            <span>分享实验</span>
            <el-button @click="shareExperiment" type="warning" size="small">
              分享
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <NetworkVisualization 
        ref="networkVisualizationRef"
        @progress-update="handleProgressUpdate"
        @training-complete="handleTrainingComplete"
      />
    </div>

    <!-- 实验说明对话框 -->
    <el-dialog
      v-model="showInstructions"
      title="实验说明"
      width="600px"
      :before-close="handleCloseInstructions"
    >
      <div class="instructions-content">
        <h3>🎯 实验目标</h3>
        <p>通过交互式操作，深入理解神经网络的训练过程和工作原理。</p>
        
        <h3>🚀 操作指南</h3>
        <ol>
          <li><strong>开始训练</strong>：点击"开始训练"按钮启动神经网络训练</li>
          <li><strong>选择输入</strong>：在输入数据面板选择不同的猫狗图片</li>
          <li><strong>交互节点</strong>：点击网络图中的任意节点来启用/禁用</li>
          <li><strong>观察指标</strong>：实时查看准确率和损失率的变化</li>
          <li><strong>调整参数</strong>：修改学习率、批次大小等训练参数</li>
        </ol>
        
        <h3>💡 学习要点</h3>
        <ul>
          <li>观察数据在神经网络中的流动过程</li>
          <li>理解不同节点对最终结果的影响</li>
          <li>体验超参数对训练效果的影响</li>
          <li>掌握神经网络的基本工作原理</li>
        </ul>
        
        <h3>⚠️ 注意事项</h3>
        <ul>
          <li>建议先观看完整的训练过程，再进行交互操作</li>
          <li>禁用过多节点可能导致网络性能下降</li>
          <li>可以随时重置训练重新开始实验</li>
        </ul>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showInstructions = false">关闭</el-button>
          <el-button type="primary" @click="startExperiment">开始实验</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 实验完成对话框 -->
    <el-dialog
      v-model="showCompletionDialog"
      title="🎉 实验完成"
      width="500px"
      :show-close="false"
    >
      <div class="completion-content">
        <div class="completion-stats">
          <div class="stat-item">
            <span class="stat-label">最终准确率</span>
            <span class="stat-value">{{ finalAccuracy }}%</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">训练轮数</span>
            <span class="stat-value">{{ totalEpochs }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">实验时长</span>
            <span class="stat-value">{{ experimentDuration }}</span>
          </div>
        </div>
        
        <div class="achievement-section">
          <h4>🏆 获得成就</h4>
          <div class="achievements">
            <el-tag 
              v-for="achievement in earnedAchievements" 
              :key="achievement"
              type="success"
              size="small"
            >
              {{ achievement }}
            </el-tag>
          </div>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="resetExperiment">重新实验</el-button>
          <el-button type="primary" @click="goToNextChapter">下一章节</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElNotification } from 'element-plus'
import {
  ArrowLeft, Connection, Trophy, Document, VideoPlay, Download, Share
} from '@element-plus/icons-vue'
import NetworkVisualization from '../components/chapter6/NetworkVisualization.vue'

// 路由
const router = useRouter()

// 响应式数据
const networkVisualizationRef = ref(null)
const showInstructions = ref(false)
const showCompletionDialog = ref(false)

// 实验数据
const finalAccuracy = ref(0)
const totalEpochs = ref(0)
const experimentDuration = ref('0分0秒')
const earnedAchievements = ref([])
const experimentStartTime = ref(null)

// 方法
const goBack = () => {
  router.go(-1)
}

const quickStart = () => {
  if (networkVisualizationRef.value) {
    networkVisualizationRef.value.startTraining()
    ElMessage.success('训练已开始！')
  }
}

const exportResults = () => {
  // 导出实验结果
  const results = {
    finalAccuracy: finalAccuracy.value,
    totalEpochs: totalEpochs.value,
    duration: experimentDuration.value,
    achievements: earnedAchievements.value,
    timestamp: new Date().toISOString()
  }
  
  const dataStr = JSON.stringify(results, null, 2)
  const dataBlob = new Blob([dataStr], { type: 'application/json' })
  const url = URL.createObjectURL(dataBlob)
  
  const link = document.createElement('a')
  link.href = url
  link.download = `neural_network_experiment_${Date.now()}.json`
  link.click()
  
  URL.revokeObjectURL(url)
  ElMessage.success('实验结果已导出！')
}

const shareExperiment = () => {
  // 分享实验
  const shareData = {
    title: '我的神经网络训练实验',
    text: `我刚完成了一个神经网络训练实验，最终准确率达到了${finalAccuracy.value}%！`,
    url: window.location.href
  }
  
  if (navigator.share) {
    navigator.share(shareData)
  } else {
    // 复制到剪贴板
    navigator.clipboard.writeText(shareData.url).then(() => {
      ElMessage.success('实验链接已复制到剪贴板！')
    })
  }
}

const handleCloseInstructions = () => {
  showInstructions.value = false
}

const startExperiment = () => {
  showInstructions.value = false
  experimentStartTime.value = Date.now()
  quickStart()
}

const handleProgressUpdate = (type, progress) => {
  // 处理进度更新
  console.log('Progress update:', type, progress)
}

const handleTrainingComplete = (results) => {
  // 处理训练完成
  finalAccuracy.value = Math.round(results.accuracy * 100)
  totalEpochs.value = results.epochs
  
  // 计算实验时长
  if (experimentStartTime.value) {
    const duration = Date.now() - experimentStartTime.value
    const minutes = Math.floor(duration / 60000)
    const seconds = Math.floor((duration % 60000) / 1000)
    experimentDuration.value = `${minutes}分${seconds}秒`
  }
  
  // 计算获得的成就
  earnedAchievements.value = []
  if (finalAccuracy.value >= 90) earnedAchievements.value.push('高精度训练师')
  if (totalEpochs.value <= 50) earnedAchievements.value.push('效率专家')
  if (results.nodeInteractions > 5) earnedAchievements.value.push('交互探索者')
  
  showCompletionDialog.value = true
  
  ElNotification({
    title: '实验完成',
    message: `恭喜！您的神经网络达到了${finalAccuracy.value}%的准确率`,
    type: 'success',
    duration: 5000
  })
}

const resetExperiment = () => {
  showCompletionDialog.value = false
  if (networkVisualizationRef.value) {
    networkVisualizationRef.value.resetTraining()
  }
  experimentStartTime.value = null
  ElMessage.info('实验已重置，可以重新开始')
}

const goToNextChapter = () => {
  showCompletionDialog.value = false
  router.push('/chapter/7')
}

// 生命周期
onMounted(() => {
  // 显示欢迎消息
  ElNotification({
    title: '欢迎来到神经网络训练实验室',
    message: '在这里您可以深度体验神经网络的训练过程',
    type: 'info',
    duration: 3000
  })
})

onUnmounted(() => {
  // 清理工作
})
</script>

<style lang="scss" scoped>


.network-training-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #0f0f23 0%, #1a1a2e 50%, #16213e 100%);
  position: relative;
  padding: 0;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: radial-gradient(circle at 20% 80%, rgba(120, 119, 198, 0.3) 0%, transparent 50%),
                radial-gradient(circle at 80% 20%, rgba(255, 119, 198, 0.15) 0%, transparent 50%),
                radial-gradient(circle at 40% 40%, rgba(120, 219, 226, 0.1) 0%, transparent 50%);
    pointer-events: none;
  }
}

.page-header {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 1.5rem 2rem;
  position: relative;
  z-index: 10;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(90deg, rgba(120, 119, 198, 0.1) 0%, rgba(255, 119, 198, 0.05) 100%);
    pointer-events: none;
  }
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1400px;
    margin: 0 auto;
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 1rem;
      
      .back-button {
          background: linear-gradient(135deg, #2c2c2c 0%, #1a1a1a 100%);
          border: 1px solid #444;
          color: white;
          position: relative;
          overflow: hidden;
          transition: all 0.3s ease;
          
          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
            transition: left 0.5s;
          }

          &:hover {
            background: linear-gradient(135deg, #3c3c3c 0%, #2a2a2a 100%);
            border-color: #666;
            transform: translateY(-2px) scale(1.05);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.7);
            
            &::before {
              left: 100%;
            }
          }
        }

      .title-section {
        .page-title {
          font-size: 2rem;
          font-weight: 600;
          color: #ffffff;
          margin: 0 0 0.5rem 0;
          display: flex;
          align-items: center;
          gap: 0.5rem;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);

          .el-icon {
            color: #ffffff;
            filter: drop-shadow(0 0 8px rgba(255, 255, 255, 0.3));
          }
        }

        .page-subtitle {
          color: rgba(255, 255, 255, 0.7);
          margin: 0;
          font-size: 1.1rem;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }
      }
    }
    
    .header-right {
      .el-tag {
        padding: 8px 16px;
        font-size: 1rem;
        
        .el-icon {
          margin-right: 5px;
        }
      }
    }
  }
}

.quick-nav {
  padding: 1rem 2rem;
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  z-index: 5;
  
  .nav-card {
    background: rgba(255, 255, 255, 0.08);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.15);
    border-radius: 20px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(135deg, rgba(120, 119, 198, 0.1) 0%, rgba(255, 119, 198, 0.05) 100%);
      pointer-events: none;
    }

    :deep(.el-card__body) {
      background: transparent;
      position: relative;
      z-index: 2;
    }

    .nav-content {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1rem;

      .nav-item {
        display: flex;
        align-items: center;
        gap: 0.75rem;
        padding: 1rem;
        border-radius: 12px;
        transition: all 0.3s ease;
        position: relative;
        
        &:hover {
          background: rgba(255, 255, 255, 0.1);
          transform: translateY(-2px);
          box-shadow: 0 4px 20px rgba(102, 126, 234, 0.2);
        }

        .el-icon {
          color: #ffffff;
          font-size: 1.3rem;
          filter: drop-shadow(0 0 6px rgba(255, 255, 255, 0.3));
        }

        span {
          flex: 1;
          color: rgba(255, 255, 255, 0.9);
          font-weight: 500;
          text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
        }
      }
    }
  }
}

.main-content {
  padding: 0 2rem 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.instructions-content {
  h3 {
    color: var(--text-color, #f5f6fa);
    margin: 1.5rem 0 1rem 0;
    display: flex;
    align-items: center;
    gap: 0.5rem;
  }

  p, li {
    color: var(--text-secondary-color, #b0b3b8);
    line-height: 1.6;
    margin-bottom: 0.5rem;
  }

  ol, ul {
    padding-left: 1.5rem;
  }

  strong {
    color: var(--text-color, #f5f6fa);
  }
}

.completion-content {
  text-align: center;

  .completion-stats {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 1rem;
    margin-bottom: 2rem;

    .stat-item {
      padding: 1rem;
      background: var(--secondary-color, #23272e);
      border: 1px solid var(--border-color, #393b40);
      border-radius: 8px;

      .stat-label {
        display: block;
        color: var(--text-secondary-color, #b0b3b8);
        font-size: 0.9rem;
        margin-bottom: 0.5rem;
      }

      .stat-value {
        display: block;
        color: var(--text-color, #f5f6fa);
        font-size: 1.5rem;
        font-weight: 600;
      }
    }
  }

  .achievement-section {
    h4 {
      color: var(--text-color, #f5f6fa);
      margin-bottom: 1rem;
    }
    
    .achievements {
      display: flex;
      justify-content: center;
      gap: 0.5rem;
      flex-wrap: wrap;
    }
  }
}

// 对话框样式覆盖
:deep(.el-dialog) {
  background: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.7);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.05) 0%, rgba(255, 255, 255, 0.02) 100%);
    pointer-events: none;
  }

  .el-dialog__header {
    background: linear-gradient(135deg, rgba(0, 0, 0, 0.6) 0%, rgba(0, 0, 0, 0.4) 100%);
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
    padding: 1.5rem 2rem;
    position: relative;
    z-index: 2;

    .el-dialog__title {
      color: #ffffff;
      font-weight: 600;
      font-size: 1.2rem;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
    }

    .el-dialog__headerbtn {
      .el-dialog__close {
        color: rgba(255, 255, 255, 0.7);
        transition: all 0.3s ease;

        &:hover {
          color: white;
          transform: scale(1.1);
        }
      }
    }
  }

  .el-dialog__body {
    background: transparent;
    color: rgba(255, 255, 255, 0.9);
    padding: 2rem;
    position: relative;
    z-index: 2;
  }

  .el-dialog__footer {
    background: transparent;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    padding: 1.5rem 2rem;
    position: relative;
    z-index: 2;
  }
}

// 按钮样式覆盖
:deep(.el-button) {
  &.el-button--primary {
    background: linear-gradient(135deg, #2c2c2c 0%, #1a1a1a 100%);
    border: 1px solid #444;
    color: #ffffff;
    border-radius: 12px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
    position: relative;
    overflow: hidden;
    transition: all 0.3s ease;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
      transition: left 0.5s;
    }

    &:hover {
      background: linear-gradient(135deg, #3c3c3c 0%, #2a2a2a 100%);
      border-color: #666;
      transform: translateY(-2px) scale(1.05);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.7);
      
      &::before {
        left: 100%;
      }
    }
  }

  &.el-button--success {
    background: linear-gradient(135deg, #404040 0%, #2a2a2a 100%);
    border: 1px solid #555;
    color: #ffffff;
    border-radius: 12px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.4);
    transition: all 0.3s ease;

    &:hover {
      background: linear-gradient(135deg, #505050 0%, #3a3a3a 100%);
      border-color: #777;
      transform: translateY(-2px) scale(1.05);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.6);
    }
  }

  &.el-button--warning {
    background: linear-gradient(135deg, #3a3a3a 0%, #252525 100%);
    border: 1px solid #666;
    color: #ffffff;
    border-radius: 12px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.4);
    transition: all 0.3s ease;

    &:hover {
      background: linear-gradient(135deg, #4a4a4a 0%, #353535 100%);
      border-color: #888;
      transform: translateY(-2px) scale(1.05);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.6);
    }
  }

  &.el-button--info {
    background: rgba(0, 0, 0, 0.3);
    border: 1px solid rgba(255, 255, 255, 0.2);
    color: rgba(255, 255, 255, 0.9);
    border-radius: 12px;
    backdrop-filter: blur(10px);
    transition: all 0.3s ease;

    &:hover {
      background: rgba(0, 0, 0, 0.5);
      border-color: rgba(255, 255, 255, 0.4);
      transform: translateY(-1px);
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
    }
  }
}

// 标签样式覆盖
:deep(.el-tag) {
  &.el-tag--success {
    background: linear-gradient(135deg, #3a3a3a 0%, #2a2a2a 100%);
    border: 1px solid #555;
    color: #ffffff;
    border-radius: 20px;
    padding: 8px 16px;
    font-weight: 500;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.4);
    transition: all 0.3s ease;
    
    &:hover {
      transform: scale(1.05);
      background: linear-gradient(135deg, #4a4a4a 0%, #3a3a3a 100%);
      border-color: #777;
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.6);
    }
    
    .el-icon {
      filter: drop-shadow(0 0 4px rgba(255, 255, 255, 0.3));
    }
  }
}

// 表单样式覆盖
:deep(.el-form) {
  .el-form-item__label {
    color: #ffffff;
    font-weight: 600;
    font-size: 1rem;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  }

  .el-input {
    .el-input__wrapper {
      background: rgba(0, 0, 0, 0.6);
      backdrop-filter: blur(10px);
      border: 1px solid rgba(255, 255, 255, 0.3);
      border-radius: 12px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
      transition: all 0.3s ease;
      position: relative;
      overflow: hidden;
      
      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.02) 0%, rgba(255, 255, 255, 0.01) 100%);
        pointer-events: none;
      }

      &:hover {
        border-color: rgba(255, 255, 255, 0.5);
        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.4);
        transform: translateY(-1px);
      }

      &.is-focus {
        border-color: rgba(255, 255, 255, 0.7);
        box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.2), 0 8px 25px rgba(0, 0, 0, 0.5);
        transform: translateY(-2px);
      }

      .el-input__inner {
        color: rgba(255, 255, 255, 0.9);
        background: transparent;
        position: relative;
        z-index: 2;

        &::placeholder {
          color: rgba(255, 255, 255, 0.4);
        }
      }
    }
  }

  .el-textarea {
    .el-textarea__inner {
      background: rgba(0, 0, 0, 0.6);
      backdrop-filter: blur(10px);
      border: 1px solid rgba(255, 255, 255, 0.3);
      border-radius: 12px;
      color: rgba(255, 255, 255, 0.9);
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
      transition: all 0.3s ease;
      position: relative;
      
      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.02) 0%, rgba(255, 255, 255, 0.01) 100%);
        pointer-events: none;
      }

      &:hover {
        border-color: rgba(255, 255, 255, 0.5);
        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.4);
        transform: translateY(-1px);
      }

      &:focus {
        border-color: rgba(255, 255, 255, 0.7);
        box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.2), 0 8px 25px rgba(0, 0, 0, 0.5);
        transform: translateY(-2px);
      }

      &::placeholder {
        color: rgba(255, 255, 255, 0.4);
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .page-header {
    padding: 1rem;
    
    .header-content {
      flex-direction: column;
      gap: 1rem;
      
      .header-left {
        flex-direction: column;
        text-align: center;
        
        .title-section .page-title {
          font-size: 1.5rem;
        }
      }
    }
  }
  
  .quick-nav {
    padding: 1rem;
    
    .nav-content {
      grid-template-columns: 1fr;
    }
  }
  
  .main-content {
    padding: 0 1rem 1rem;
  }
  
  .completion-stats {
    grid-template-columns: 1fr !important;
  }
}
</style>
