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

    <!-- 使用指南区域 -->
    <div class="usage-guide">
      <el-card class="guide-card">
        <template #header>
          <div class="guide-header">
            <el-icon><Document /></el-icon>
            <span>神经网络训练实验室使用指南</span>
          </div>
        </template>
        
        <div class="guide-content">
          <div class="guide-section">
            <h4>🎯 实验简介</h4>
            <p>本实验室提供了一个交互式的神经网络训练环境，您将体验完整的猫狗图像分类模型训练过程。通过可视化的方式，深入理解神经网络的工作原理、数据流动过程以及各种参数对训练效果的影响。</p>
          </div>
          
          <div class="guide-section">
            <h4>🚀 快速开始</h4>
            <div class="steps-grid">
              <div class="step-item">
                <div class="step-number">1</div>
                <div class="step-content">
                  <h5>启动训练</h5>
                  <p>点击右侧控制面板的"开始训练"按钮，启动神经网络训练过程</p>
                </div>
              </div>
              <div class="step-item">
                <div class="step-number">2</div>
                <div class="step-content">
                  <h5>观察过程</h5>
                  <p>观察网络结构图中数据的流动，实时查看准确率和损失值的变化</p>
                </div>
              </div>
              <div class="step-item">
                <div class="step-number">3</div>
                <div class="step-content">
                  <h5>交互探索</h5>
                  <p>点击网络节点进行交互，调整训练参数，体验不同设置的效果</p>
                </div>
              </div>
              <div class="step-item">
                <div class="step-number">4</div>
                <div class="step-content">
                  <h5>分析结果</h5>
                  <p>查看训练结果和性能指标，理解神经网络的学习过程</p>
                </div>
              </div>
            </div>
          </div>
          
          <div class="guide-section">
            <h4>💡 核心功能</h4>
            <div class="features-grid">
              <div class="feature-item">
                <el-icon class="feature-icon"><Connection /></el-icon>
                <div class="feature-text">
                  <h5>网络可视化</h5>
                  <p>实时显示神经网络结构和数据流动过程</p>
                </div>
              </div>
              <div class="feature-item">
                <el-icon class="feature-icon"><VideoPlay /></el-icon>
                <div class="feature-text">
                  <h5>训练控制</h5>
                  <p>开始、暂停、重置训练，调整训练速度</p>
                </div>
              </div>
              <div class="feature-item">
                <el-icon class="feature-icon"><Trophy /></el-icon>
                <div class="feature-text">
                  <h5>性能监控</h5>
                  <p>实时监控准确率、损失值等关键指标</p>
                </div>
              </div>
              <div class="feature-item">
                <el-icon class="feature-icon"><Document /></el-icon>
                <div class="feature-text">
                  <h5>参数调节</h5>
                  <p>调整学习率、批次大小等超参数</p>
                </div>
              </div>
            </div>
          </div>
          
          <div class="guide-section">
            <h4>⚠️ 使用提示</h4>
            <div class="tips-list">
              <div class="tip-item">
                <el-icon class="tip-icon"><Trophy /></el-icon>
                <span>建议首次使用时先观看完整的训练过程，了解基本流程</span>
              </div>
              <div class="tip-item">
                <el-icon class="tip-icon"><Connection /></el-icon>
                <span>点击网络节点可以启用/禁用该节点，观察对训练效果的影响</span>
              </div>
              <div class="tip-item">
                <el-icon class="tip-icon"><VideoPlay /></el-icon>
                <span>可以随时调整训练速度，快速查看训练效果或仔细观察细节</span>
              </div>
              <div class="tip-item">
                <el-icon class="tip-icon"><Document /></el-icon>
                <span>尝试不同的参数组合，体验超参数对模型性能的影响</span>
              </div>
            </div>
          </div>
        </div>
      </el-card>
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
  if (networkVisualizationRef.value && typeof networkVisualizationRef.value.startTraining === 'function') {
    try {
      networkVisualizationRef.value.startTraining()
      ElMessage.success('训练已开始！')
    } catch (error) {
      // 启动训练失败
      ElMessage.error('启动训练失败，请稍后重试')
    }
  } else {
    ElMessage.warning('网络组件尚未准备就绪，请稍后重试')
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
  // Progress update
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
  
  // 自动导出结果
  exportResults()
  
  ElNotification({
    title: '实验完成',
    message: `恭喜！您的神经网络达到了${finalAccuracy.value}%的准确率，结果已自动导出`,
    type: 'success',
    duration: 5000
  })
}

const resetExperiment = () => {
  showCompletionDialog.value = false
  if (networkVisualizationRef.value && typeof networkVisualizationRef.value.resetTraining === 'function') {
    try {
      networkVisualizationRef.value.resetTraining()
      ElMessage.info('实验已重置，可以重新开始')
    } catch (error) {
      // 重置实验失败
      ElMessage.error('重置实验失败，请刷新页面重试')
    }
  } else {
    ElMessage.warning('网络组件尚未准备就绪')
  }
  experimentStartTime.value = null
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
  background: var(--body-bg);
  position: relative;
  padding: 0;
  
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
}

// 使用指南样式
.usage-guide {
  margin: 1.5rem 2rem;
  max-width: 1400px;
  margin-left: auto;
  margin-right: auto;
  position: relative;
  z-index: 5;
  
  .guide-card {
    background: var(--card-bg);
    backdrop-filter: blur(20px);
    border: 1px solid var(--border-light-color);
    border-radius: 20px;
    box-shadow: var(--box-shadow);
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(135deg, var(--glow-color) 0%, var(--glow-light) 100%);
      opacity: 0.05;
      pointer-events: none;
    }
    
    :deep(.el-card__header) {
      background: linear-gradient(135deg, var(--btn-primary-bg) 0%, var(--accent-color) 100%);
      border-radius: 20px 20px 0 0;
      padding: 1.5rem 2rem;
      border-bottom: 1px solid var(--border-light-color);
      position: relative;
      z-index: 2;
      
      .guide-header {
        display: flex;
        align-items: center;
        gap: 0.75rem;
        color: white;
        font-size: 1.2rem;
        font-weight: 600;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        
        .el-icon {
          font-size: 1.4rem;
          filter: drop-shadow(0 0 8px rgba(255, 255, 255, 0.3));
        }
      }
    }
    
    :deep(.el-card__body) {
      padding: 2rem;
      background: transparent;
      position: relative;
      z-index: 2;
    }
  }
  
  .guide-content {
    .guide-section {
      margin-bottom: 2.5rem;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      h4 {
        color: var(--text-color);
        font-size: 1.3rem;
        margin-bottom: 1.2rem;
        display: flex;
        align-items: center;
        gap: 0.5rem;
        font-weight: 600;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
      }
      
      p {
        color: var(--text-secondary-color);
        line-height: 1.7;
        margin-bottom: 1rem;
        font-size: 1rem;
      }
    }
    
    .steps-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: 1.5rem;
      margin-top: 1.5rem;
      
      .step-item {
        display: flex;
        align-items: flex-start;
        gap: 1.2rem;
        padding: 1.8rem;
        background: var(--secondary-color);
        border-radius: 16px;
        border: 1px solid var(--border-light-color);
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;
        
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
          transform: translateY(-4px);
          box-shadow: 0 12px 40px var(--glow-color);
          border-color: var(--accent-color);
          background: var(--card-hover-bg);
          
          &::before {
            left: 100%;
          }
        }
        
        .step-number {
          width: 40px;
          height: 40px;
          background: linear-gradient(135deg, var(--btn-primary-bg) 0%, var(--accent-color) 100%);
          color: white;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: 700;
          font-size: 1.1rem;
          flex-shrink: 0;
          box-shadow: 0 4px 15px var(--glow-color);
          text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
        }
        
        .step-content {
          h5 {
            color: var(--text-color);
            font-size: 1.1rem;
            margin-bottom: 0.7rem;
            font-weight: 600;
            text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
          }
          
          p {
            color: var(--text-secondary-color);
            font-size: 0.95rem;
            line-height: 1.6;
            margin: 0;
          }
        }
      }
    }
    
    .features-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
      gap: 1.2rem;
      margin-top: 1.5rem;
      
      .feature-item {
        display: flex;
        align-items: center;
        gap: 1rem;
        padding: 1.5rem;
        background: var(--secondary-color);
        border-radius: 12px;
        border: 1px solid var(--border-light-color);
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;
        
        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: -100%;
          width: 100%;
          height: 100%;
          background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.05), transparent);
          transition: left 0.5s;
        }
        
        &:hover {
          background: var(--card-hover-bg);
          border-color: var(--accent-color);
          transform: translateY(-2px);
          box-shadow: 0 8px 25px var(--glow-color);
          
          &::before {
            left: 100%;
          }
        }
        
        .feature-icon {
          font-size: 1.8rem;
          color: var(--accent-color);
          flex-shrink: 0;
          filter: drop-shadow(0 0 8px var(--glow-color));
        }
        
        .feature-text {
          h5 {
            color: var(--text-color);
            font-size: 1rem;
            margin-bottom: 0.4rem;
            font-weight: 600;
            text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
          }
          
          p {
            color: var(--text-secondary-color);
            font-size: 0.9rem;
            line-height: 1.5;
            margin: 0;
          }
        }
      }
    }
    
    .tips-list {
      margin-top: 1.5rem;
      
      .tip-item {
        display: flex;
        align-items: flex-start;
        gap: 1rem;
        padding: 1rem 0;
        border-bottom: 1px solid var(--border-light-color);
        transition: all 0.3s ease;
        
        &:last-child {
          border-bottom: none;
        }
        
        &:hover {
          padding-left: 0.5rem;
          
          .tip-icon {
            transform: scale(1.1);
            filter: drop-shadow(0 0 8px var(--glow-color));
          }
        }
        
        .tip-icon {
          font-size: 1.2rem;
          color: var(--accent-color);
          margin-top: 0.2rem;
          flex-shrink: 0;
          transition: all 0.3s ease;
        }
        
        span {
          color: var(--text-secondary-color);
          font-size: 0.95rem;
          line-height: 1.6;
        }
      }
    }
  }
}

.page-header {
  background: var(--nav-bg);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid var(--border-light-color);
  padding: 1.5rem 2rem;
  position: relative;
  z-index: 10;
  box-shadow: var(--box-shadow-light);
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(90deg, var(--glow-color) 0%, var(--glow-light) 100%);
    opacity: 0.1;
    pointer-events: none;
  }
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1400px;
    margin: 0 auto;
    position: relative;
    z-index: 2;
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 1rem;
      
      .back-button {
        background: var(--btn-primary-bg);
        border: none;
        color: white;
        box-shadow: var(--box-shadow-light);
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;

        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: -100%;
          width: 100%;
          height: 100%;
          background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
          transition: left 0.5s;
        }

        &:hover {
          background: var(--btn-hover-bg);
          transform: translateY(-2px);
          box-shadow: 0 8px 25px var(--glow-color);
          
          &::before {
            left: 100%;
          }
        }
      }

      .title-section {
        .page-title {
          font-size: 2rem;
          font-weight: 600;
          color: var(--text-color);
          margin: 0 0 0.5rem 0;
          display: flex;
          align-items: center;
          gap: 0.5rem;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);

          .el-icon {
            color: var(--accent-color);
            filter: drop-shadow(0 0 8px var(--glow-color));
          }
        }

        .page-subtitle {
          color: var(--text-secondary-color);
          margin: 0;
          font-size: 1.1rem;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }
      }
    }
    
    .header-right {
      .el-tag {
        background: var(--btn-primary-bg);
        color: white;
        border: none;
        box-shadow: var(--box-shadow-light);
        position: relative;
        overflow: hidden;

        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: -100%;
          width: 100%;
          height: 100%;
          background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
          transition: left 0.5s;
        }

        &:hover::before {
          left: 100%;
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
    background: var(--card-bg);
    border: 1px solid var(--border-light-color);
    border-radius: 20px;
    box-shadow: var(--box-shadow);
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(135deg, var(--glow-color) 0%, var(--glow-light) 100%);
      opacity: 0.05;
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
        background: var(--secondary-color);
        border: 1px solid var(--border-light-color);
        color: var(--text-color);
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;
        
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
          background: var(--btn-primary-bg);
          color: white;
          transform: translateY(-2px);
          box-shadow: 0 8px 25px var(--glow-color);
          border-color: var(--accent-color);
          
          &::before {
            left: 100%;
          }
        }

        span {
          color: inherit;
        }
      }
    }
  }
}

.main-content {
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
  background: transparent;
  position: relative;
  z-index: 1;
}

.completion-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-top: 2rem;

  .stat-card {
    background: var(--card-bg);
    border: 1px solid var(--border-light-color);
    border-radius: 16px;
    padding: 1.5rem;
    box-shadow: var(--box-shadow);
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
      background: linear-gradient(135deg, var(--glow-color) 0%, var(--glow-light) 100%);
      opacity: 0.05;
      pointer-events: none;
    }

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 12px 40px var(--glow-color);
      border-color: var(--accent-color);
    }

    .stat-content {
      text-align: center;
      position: relative;
      z-index: 2;

      .stat-value {
        font-size: 2.5rem;
        font-weight: 700;
        color: var(--accent-color);
        margin-bottom: 0.5rem;
        text-shadow: 0 0 10px var(--glow-color);
      }

      .stat-label {
        color: var(--text-secondary-color);
        font-size: 1rem;
      }
    }
  }
}

.completion-dialog {
  .dialog-content {
    background: var(--modal-bg);
    border: 1px solid var(--border-light-color);
    border-radius: 20px;
    box-shadow: var(--box-shadow);
    padding: 2rem;
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(135deg, var(--glow-color) 0%, var(--glow-light) 100%);
      opacity: 0.05;
      pointer-events: none;
    }

    .dialog-header {
      text-align: center;
      margin-bottom: 2rem;
      position: relative;
      z-index: 2;

      .dialog-title {
        font-size: 1.8rem;
        font-weight: 600;
        color: var(--text-color);
        margin-bottom: 0.5rem;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
      }

      .dialog-subtitle {
        color: var(--text-secondary-color);
        font-size: 1.1rem;
      }
    }

    .dialog-body {
      position: relative;
      z-index: 2;
      
      .metric-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 1rem;
        margin-bottom: 1rem;
        background: var(--secondary-color);
        border: 1px solid var(--border-light-color);
        border-radius: 12px;
        transition: all 0.3s ease;

        &:hover {
          background: var(--card-hover-bg);
          border-color: var(--accent-color);
          box-shadow: 0 4px 16px var(--glow-color);
        }

        .metric-label {
          color: var(--text-color);
          font-weight: 500;
        }

        .metric-value {
          color: var(--accent-color);
          font-weight: 600;
          text-shadow: 0 0 8px var(--glow-color);
        }
      }
    }
  }
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
.el-form {
  :deep(.el-form-item__label) {
    color: #ffffff;
    font-weight: 600;
    font-size: 1rem;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  }

  :deep(.el-input) {
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

  :deep(.el-textarea) {
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

/* 深色主题优化 - 完全重新设计 */
html.dark-theme .network-training-page {
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

  .page-header {
    background: rgba(26, 26, 46, 0.8);
    backdrop-filter: blur(30px);
    border-bottom: 1px solid rgba(0, 191, 255, 0.2);
    box-shadow: 
      0 4px 20px rgba(0, 0, 0, 0.4),
      0 0 0 1px rgba(0, 191, 255, 0.1);
    position: relative;
    z-index: 10;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(90deg, 
        rgba(0, 191, 255, 0.1) 0%, 
        rgba(0, 255, 127, 0.05) 50%, 
        rgba(255, 69, 0, 0.05) 100%);
      opacity: 0.6;
    }

    .header-content {
      position: relative;
      z-index: 1;

      .header-left {
        .back-button {
          background: linear-gradient(135deg, #00bfff 0%, #0080ff 100%);
          border: 1px solid rgba(0, 191, 255, 0.5);
          color: white;
          box-shadow: 
            0 4px 15px rgba(0, 191, 255, 0.4),
            0 0 0 1px rgba(0, 191, 255, 0.2);
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

          &:hover {
            background: linear-gradient(135deg, #00a6e6 0%, #00bfff 100%);
            border-color: rgba(0, 191, 255, 0.8);
            transform: translateY(-3px) scale(1.05);
            box-shadow: 
              0 8px 25px rgba(0, 191, 255, 0.5),
              0 0 0 2px rgba(0, 191, 255, 0.3);
          }

          &:active {
            transform: translateY(-1px) scale(1.02);
          }
        }

        .title-section {
          .page-title {
            background: linear-gradient(135deg, #ffffff 0%, #e0e0e0 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            text-shadow: none;
            font-weight: 700;
            letter-spacing: -0.5px;

            .el-icon {
              background: linear-gradient(135deg, #00bfff 0%, #00ff7f 100%);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
              background-clip: text;
            }
          }

          .page-subtitle {
            color: rgba(255, 255, 255, 0.7);
            font-weight: 400;
            text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
          }
        }
      }

      .header-right {
        .el-tag {
          background: linear-gradient(135deg, #00ff7f 0%, #00cc66 100%);
          color: white;
          border: none;
          box-shadow: 
            0 4px 15px rgba(0, 255, 127, 0.4),
            0 0 0 1px rgba(0, 255, 127, 0.2);
          font-weight: 600;
          letter-spacing: 0.5px;

          .el-icon {
            filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.3));
          }
        }
      }
    }
  }

  .quick-nav {
    position: relative;
    z-index: 10;

    .nav-card {
      background: rgba(26, 26, 46, 0.9);
      backdrop-filter: blur(30px);
      border: 1px solid rgba(0, 191, 255, 0.2);
      box-shadow: 
        0 8px 32px rgba(0, 0, 0, 0.4),
        0 0 0 1px rgba(0, 191, 255, 0.1);
      position: relative;

      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: linear-gradient(135deg, 
          rgba(0, 191, 255, 0.05) 0%, 
          rgba(0, 255, 127, 0.03) 50%, 
          rgba(255, 69, 0, 0.02) 100%);
        border-radius: inherit;
      }

      .nav-content {
        position: relative;
        z-index: 1;

        .nav-item {
          background: rgba(255, 255, 255, 0.03);
          border: 1px solid rgba(0, 191, 255, 0.15);
          color: rgba(255, 255, 255, 0.9);
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          position: relative;
          overflow: hidden;

          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, 
              transparent 0%, 
              rgba(0, 191, 255, 0.1) 50%, 
              transparent 100%);
            transition: left 0.5s ease;
          }

          &:hover {
            background: rgba(0, 191, 255, 0.1);
            border-color: rgba(0, 191, 255, 0.4);
            transform: translateY(-3px);
            box-shadow: 
              0 8px 25px rgba(0, 191, 255, 0.3),
              0 0 0 1px rgba(0, 191, 255, 0.2);

            &::before {
              left: 100%;
            }
          }

          span {
            color: rgba(255, 255, 255, 0.9);
            font-weight: 500;
          }

          .el-icon {
            color: rgba(0, 191, 255, 0.8);
            transition: all 0.3s ease;
          }

          &:hover .el-icon {
            color: #00bfff;
            transform: scale(1.1);
          }
        }
      }
    }
  }

  .main-content {
    background: transparent;
    position: relative;
    z-index: 5;
  }

  .completion-stats {
    .stat-card {
      background: rgba(255, 255, 255, 0.08);
      backdrop-filter: blur(20px);
      border: 1px solid rgba(255, 255, 255, 0.15);
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);

      .stat-content {
        .stat-value {
          color: #ffffff;
        }

        .stat-label {
          color: rgba(255, 255, 255, 0.8);
        }
      }
    }
  }

  .completion-dialog {
    .dialog-content {
      background: rgba(0, 0, 0, 0.8);
      backdrop-filter: blur(20px);
      border: 1px solid rgba(255, 255, 255, 0.2);
      box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5);

      .dialog-header {
        .dialog-title {
          color: #ffffff;
        }

        .dialog-subtitle {
          color: rgba(255, 255, 255, 0.8);
        }
      }

      .dialog-body {
        .metric-item {
          background: rgba(255, 255, 255, 0.05);
          border: 1px solid rgba(255, 255, 255, 0.1);

          .metric-label {
            color: rgba(255, 255, 255, 0.9);
          }

          .metric-value {
            color: rgba(255, 255, 255, 0.8);
          }
        }
      }
    }
  }
}

/* 浅色主题适配 */
html.light-theme .network-training-page {

  &::before {
    background: radial-gradient(circle at 20% 80%, rgba(59, 130, 246, 0.1) 0%, transparent 50%),
                radial-gradient(circle at 80% 20%, rgba(139, 92, 246, 0.05) 0%, transparent 50%),
                radial-gradient(circle at 40% 40%, rgba(34, 197, 94, 0.05) 0%, transparent 50%);
  }

  .page-header {
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(20px);
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    &::before {
      background: linear-gradient(90deg, rgba(59, 130, 246, 0.05) 0%, rgba(139, 92, 246, 0.02) 100%);
    }

    .header-content {
      .header-left {
        .back-button {
          background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
          border: 1px solid #3b82f6;
          color: white;

          &:hover {
            background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
            border-color: #2563eb;
          }
        }

        .title-section {
          .page-title {
            color: #212529;
          }

          .page-subtitle {
            color: #495057;
          }
        }
      }

      .header-right {
        .el-tag {
          background: linear-gradient(135deg, #10b981, #059669);
          color: white;
          border: none;
        }
      }
    }
  }

  .quick-nav {
    .nav-card {
      background: #ffffff;
      border: 1px solid rgba(0, 0, 0, 0.1);
      box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);

      .nav-content {
        .nav-item {
          background: #f8f9fa;
          border: 1px solid rgba(0, 0, 0, 0.1);
          color: #212529;

          &:hover {
            background: #e9ecef;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
          }

          span {
            color: #212529;
          }
        }
      }
    }
  }

  .main-content {
    background: transparent;
  }

  .completion-stats {
    .stat-card {
      background: #ffffff;
      border: 1px solid rgba(0, 0, 0, 0.1);
      box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);

      .stat-content {
        .stat-value {
          color: #212529;
        }

        .stat-label {
          color: #495057;
        }
      }
    }
  }

  .completion-dialog {
    .dialog-content {
      background: #ffffff;
      border: 1px solid rgba(0, 0, 0, 0.1);
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);

      .dialog-header {
        .dialog-title {
          color: #212529;
        }

        .dialog-subtitle {
          color: #495057;
        }
      }

      .dialog-body {
        .metric-item {
          background: #f8f9fa;
          border: 1px solid rgba(0, 0, 0, 0.1);

          .metric-label {
            color: #212529;
          }

          .metric-value {
            color: #495057;
          }
        }
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
