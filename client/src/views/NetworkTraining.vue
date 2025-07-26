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
            <el-button @click="showInstructions = true" type="text" size="small">
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
@import '@/styles/variables.scss';

.network-training-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0;
}

.page-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  padding: 1.5rem 2rem;
  
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
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;
        
        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
      }
      
      .title-section {
        .page-title {
          font-size: 2rem;
          font-weight: 600;
          color: #2c3e50;
          margin: 0 0 0.5rem 0;
          display: flex;
          align-items: center;
          gap: 0.5rem;
          
          .el-icon {
            color: #667eea;
          }
        }
        
        .page-subtitle {
          color: #7f8c8d;
          margin: 0;
          font-size: 1.1rem;
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
  
  .nav-card {
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(10px);
    border: none;
    border-radius: 12px;
    
    .nav-content {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1rem;
      
      .nav-item {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        padding: 0.5rem;
        
        .el-icon {
          color: #667eea;
          font-size: 1.2rem;
        }
        
        span {
          flex: 1;
          color: #2c3e50;
          font-weight: 500;
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
    color: #2c3e50;
    margin: 1.5rem 0 1rem 0;
    display: flex;
    align-items: center;
    gap: 0.5rem;
  }
  
  p, li {
    color: #7f8c8d;
    line-height: 1.6;
    margin-bottom: 0.5rem;
  }
  
  ol, ul {
    padding-left: 1.5rem;
  }
  
  strong {
    color: #2c3e50;
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
      background: #f8f9fa;
      border-radius: 8px;
      
      .stat-label {
        display: block;
        color: #7f8c8d;
        font-size: 0.9rem;
        margin-bottom: 0.5rem;
      }
      
      .stat-value {
        display: block;
        color: #2c3e50;
        font-size: 1.5rem;
        font-weight: 600;
      }
    }
  }
  
  .achievement-section {
    h4 {
      color: #2c3e50;
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
