<template>
  <div class="chapter3-case-study">
    <div class="case-header">
      <h2 class="case-title">🎯 第三章案例学习</h2>
      <p class="case-description">
        通过两个交互式案例深入理解计算机视觉的基础概念
      </p>
      
      <!-- 学习进度指示器 -->
      <div class="progress-indicator">
        <div class="progress-item" :class="{ completed: pixelCaseCompleted }">
          <div class="progress-icon">🖼️</div>
          <div class="progress-info">
            <h4>像素数据理解案例</h4>
            <p>体验次数: {{ pixelInteractionCount }}/5 次</p>
            <div class="progress-status">
              <span v-if="pixelCaseCompleted" class="status-completed">✅ 已完成</span>
              <span v-else class="status-pending">⏳ 进行中</span>
            </div>
          </div>
        </div>
        
        <div class="progress-item" :class="{ completed: edgeCaseCompleted }">
          <div class="progress-icon">🔍</div>
          <div class="progress-info">
            <h4>边缘特征提取案例</h4>
            <p>体验次数: {{ edgeInteractionCount }}/5 次</p>
            <div class="progress-status">
              <span v-if="edgeCaseCompleted" class="status-completed">✅ 已完成</span>
              <span v-else class="status-pending">⏳ 进行中</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 案例导航 -->
    <div class="case-navigation">
      <div class="nav-buttons">
        <button 
          @click="activeCase = 'pixel'" 
          :class="{ active: activeCase === 'pixel' }"
          class="nav-btn">
          <span class="nav-icon">🖼️</span>
          <span class="nav-text">像素数据案例</span>
        </button>
        <button 
          @click="activeCase = 'edge'" 
          :class="{ active: activeCase === 'edge' }"
          class="nav-btn">
          <span class="nav-icon">🔍</span>
          <span class="nav-text">边缘特征案例</span>
        </button>
      </div>
    </div>

    <!-- 像素数据案例 -->
    <div v-if="activeCase === 'pixel'" class="case-section pixel-case">
      <div class="section-header">
        <h3 class="section-title">
          <span class="title-icon">🖼️</span>
          <span class="title-text">人眼vs计算机视觉</span>
          <span class="title-decoration"></span>
        </h3>
        <p class="section-subtitle">通过鼠标悬停体验人类视觉与机器视觉的根本差异</p>
      </div>
      
      <!-- 像素数据图像交互展示区 -->
      <div class="image-interaction-card">
        <!-- 卡片头部 -->
        <div class="card-header">
          <div class="header-content">
            <div class="vision-logo">
              <div class="logo-circle">
                <span class="logo-icon">👀</span>
              </div>
              <div class="logo-text">
                <h4>AI视觉实验室</h4>
                <span class="logo-subtitle">Computer Vision Lab</span>
              </div>
            </div>
            <div class="interaction-status">
              <div class="status-indicator" :class="{ active: isPixelHovering }">
                <span class="status-dot"></span>
                <span class="status-text">{{ isPixelHovering ? '数据视图' : '人眼视图' }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 图像展示区域 -->
        <div class="image-display-area">
          <div class="display-container">
            <!-- 指引文字 -->
            <div class="interaction-guide">
              <h5>🔍 交互体验</h5>
              <p>将鼠标悬停在下方图片上，体验视觉转换</p>
            </div>
            
            <!-- 像素数据图片容器 -->
            <div class="image-container" 
                 @mouseenter="handlePixelMouseEnter" 
                 @mouseleave="handlePixelMouseLeave">
              <!-- 人眼视图（默认显示） -->
              <img v-show="!isPixelHovering" 
                   :src="humanViewImage" 
                   alt="Human Vision View" 
                   class="view-image human-view" />
              
              <!-- 计算机视图（悬停时显示） -->
              <img v-show="isPixelHovering" 
                   :src="computerViewImage" 
                   alt="Computer Vision View" 
                   class="view-image computer-view" />
              
              <!-- 悬停指示器 -->
              <div class="hover-indicator" :class="{ active: isPixelHovering }">
                <span class="indicator-icon">{{ isPixelHovering ? '🤖' : '👁️' }}</span>
                <span class="indicator-text">{{ isPixelHovering ? 'Computer View' : 'Human View' }}</span>
              </div>
            </div>
            
            <!-- 视图说明 -->
            <div class="view-explanations">
              <div class="explanation-grid">
                <div class="explanation-item human" :class="{ active: !isPixelHovering }">
                  <div class="explanation-icon">👁️</div>
                  <div class="explanation-content">
                    <h6>人眼视图</h6>
                    <p>我们看到的是完整、连贯的图像，能够直接识别出物体的形状、特征和含义</p>
                  </div>
                </div>
                
                <div class="explanation-item computer" :class="{ active: isPixelHovering }">
                  <div class="explanation-icon">🤖</div>
                  <div class="explanation-content">
                    <h6>计算机视图</h6>
                    <p>计算机看到的是像素数据，每个颜色块都有对应的RGB数值表示</p>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 数据技术说明 -->
            <div class="technical-info" v-if="isPixelHovering">
              <div class="info-header">
                <span class="info-icon">📊</span>
                <h6>RGB数据说明</h6>
              </div>
              <div class="rgb-explanation">
                <div class="rgb-item">
                  <span class="rgb-label">R (Red)</span>
                  <span class="rgb-desc">红色通道值 (0-255)</span>
                </div>
                <div class="rgb-item">
                  <span class="rgb-label">G (Green)</span>
                  <span class="rgb-desc">绿色通道值 (0-255)</span>
                </div>
                <div class="rgb-item">
                  <span class="rgb-label">B (Blue)</span>
                  <span class="rgb-desc">蓝色通道值 (0-255)</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 像素案例统计信息 -->
        <div class="interaction-stats">
          <div class="stats-grid">
            <div class="stat-item">
              <span class="stat-value">{{ pixelInteractionCount }}</span>
              <span class="stat-label">交互次数</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ pixelTotalHoverTime.toFixed(1) }}s</span>
              <span class="stat-label">总体验时长</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ isPixelHovering ? '数据模式' : '视觉模式' }}</span>
              <span class="stat-label">当前视图</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 边缘特征提取案例 -->
    <div v-if="activeCase === 'edge'" class="case-section edge-case">
      <div class="section-header">
        <h3 class="section-title">
          <span class="title-icon">🔍</span>
          <span class="title-text">计算机视觉基础：从图像到特征</span>
          <span class="title-decoration"></span>
        </h3>
        <p class="section-subtitle">体验计算机如何将复杂图像简化为基础边缘特征</p>
      </div>
      
      <!-- 边缘检测图像交互展示区 -->
      <div class="image-interaction-card">
        <!-- 卡片头部 -->
        <div class="card-header">
          <div class="header-content">
            <div class="vision-logo">
              <div class="logo-circle">
                <span class="logo-icon">⚽</span>
              </div>
              <div class="logo-text">
                <h4>边缘检测实验室</h4>
                <span class="logo-subtitle">Edge Detection Lab</span>
              </div>
            </div>
            <div class="interaction-status">
              <div class="status-indicator" :class="{ active: isEdgeHovering }">
                <span class="status-dot"></span>
                <span class="status-text">{{ isEdgeHovering ? '边缘特征' : '原始图像' }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 图像展示区域 -->
        <div class="image-display-area">
          <div class="display-container">
            <!-- 指引文字 -->
            <div class="interaction-guide">
              <h5>🔍 特征提取体验</h5>
              <p>将鼠标悬停在下方图片上，观察边缘检测效果</p>
            </div>
            
            <!-- 边缘检测图片容器 -->
            <div class="image-container" 
                 @mouseenter="handleEdgeMouseEnter" 
                 @mouseleave="handleEdgeMouseLeave">
              <!-- 原始图像（默认显示） -->
              <img v-show="!isEdgeHovering" 
                   :src="neymarOriginalImage" 
                   alt="Original Image" 
                   class="view-image original-view" />
              
              <!-- 边缘检测图像（悬停时显示） -->
              <img v-show="isEdgeHovering" 
                   :src="neymarEdgesImage" 
                   alt="Edge Detection View" 
                   class="view-image edges-view" />
              
              <!-- 悬停指示器 -->
              <div class="hover-indicator" :class="{ active: isEdgeHovering }">
                <span class="indicator-icon">{{ isEdgeHovering ? '🔍' : '⚽' }}</span>
                <span class="indicator-text">{{ isEdgeHovering ? 'Edge Features' : 'Original Image' }}</span>
              </div>
            </div>
            
            <!-- 视图说明 -->
            <div class="view-explanations">
              <div class="explanation-grid">
                <div class="explanation-item human" :class="{ active: !isEdgeHovering }">
                  <div class="explanation-icon">⚽</div>
                  <div class="explanation-content">
                    <h6>人眼所见</h6>
                    <p>完整、丰富的彩色图像，包含所有细节、颜色和纹理信息</p>
                  </div>
                </div>
                
                <div class="explanation-item computer" :class="{ active: isEdgeHovering }">
                  <div class="explanation-icon">🔍</div>
                  <div class="explanation-content">
                    <h6>计算机初步处理</h6>
                    <p>通过边缘检测算法提取的黑白轮廓图，突出物体的结构特征</p>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 技术说明 -->
            <div class="technical-info" v-if="isEdgeHovering">
              <div class="info-header">
                <span class="info-icon">🔬</span>
                <h6>边缘检测技术说明</h6>
              </div>
              <div class="edge-explanation">
                <div class="edge-item">
                  <span class="edge-label">Sobel算子</span>
                  <span class="edge-desc">检测图像中的边缘和轮廓</span>
                </div>
                <div class="edge-item">
                  <span class="edge-label">梯度计算</span>
                  <span class="edge-desc">计算像素强度变化</span>
                </div>
                <div class="edge-item">
                  <span class="edge-label">特征提取</span>
                  <span class="edge-desc">为后续识别提供结构信息</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 边缘案例统计信息 -->
        <div class="interaction-stats">
          <div class="stats-grid">
            <div class="stat-item">
              <span class="stat-value">{{ edgeInteractionCount }}</span>
              <span class="stat-label">交互次数</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ edgeTotalHoverTime.toFixed(1) }}s</span>
              <span class="stat-label">总体验时长</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ isEdgeHovering ? '特征模式' : '原图模式' }}</span>
              <span class="stat-label">当前视图</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 案例总结 -->
    <div class="case-summary">
      <h3 class="summary-title">💡 案例总结</h3>
      <div class="summary-content">
        <div class="summary-item">
          <h4>计算机视觉的两个基础概念</h4>
          <ul>
            <li><strong>像素数据理解:</strong> 计算机将图像看作RGB数值矩阵，每个像素都有精确的数字表示</li>
            <li><strong>特征提取:</strong> 通过边缘检测等算法，从复杂图像中提取关键的结构特征</li>
            <li><strong>处理流程:</strong> 从原始图像 → 数据化 → 特征提取 → 模式识别</li>
            <li><strong>AI的挑战:</strong> 如何让计算机从数字数据和特征中"理解"图像的真实含义</li>
          </ul>
        </div>
      </div>
    </div>

    <!-- 下一步提示 -->
    <div class="next-step" :class="{ 'completed': allCasesCompleted }">
      <div class="next-step-content">
        <div v-if="allCasesCompleted" class="completion-celebration">
          <h3>🎉 恭喜！所有案例学习已完成</h3>
          <p>你已经深入理解了计算机视觉的基础概念：像素数据处理和特征提取。现在可以参加知识测验来检验学习成果！</p>
          <button @click="startQuiz" class="btn btn-quiz btn-enabled">
            🚀 开始答题
          </button>
        </div>
        
        <div v-else class="completion-guide">
          <h3>🔄 继续完成案例学习</h3>
          <p>请完成两个案例的交互体验，深入理解计算机视觉的基础概念。</p>
          <div class="remaining-tasks">
            <div v-if="!pixelCaseCompleted" class="task-item">
              <span class="task-icon">🖼️</span>
              <span class="task-text">像素数据案例：还需要 {{ Math.max(0, 5 - pixelInteractionCount) }} 次交互</span>
            </div>
            <div v-if="!edgeCaseCompleted" class="task-item">
              <span class="task-icon">🔍</span>
              <span class="task-text">边缘特征案例：还需要 {{ Math.max(0, 5 - edgeInteractionCount) }} 次交互</span>
            </div>
          </div>
          <button @click="scrollToTop" class="btn btn-guide">
            📖 返回案例学习
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Chapter3CaseStudy',
  props: {
    chapterId: {
      type: String,
      default: '3'
    }
  },
  emits: ['case-completed', 'all-cases-completed'],
  data() {
    return {
      // 当前激活的案例
      activeCase: 'pixel',
      
      // 像素案例相关数据
      isPixelHovering: false,
      pixelInteractionCount: 0,
      pixelTotalHoverTime: 0,
      pixelHoverStartTime: null,
      pixelCaseCompleted: false,
      
      // 边缘检测案例相关数据
      isEdgeHovering: false,
      edgeInteractionCount: 0,
      edgeTotalHoverTime: 0,
      edgeHoverStartTime: null,
      edgeCaseCompleted: false,
      
      // 图片路径
      humanViewImage: '/images/chapter3/human-view.png',
      computerViewImage: '/images/chapter3/computer-view.png',
      neymarOriginalImage: '/images/chapter3/neymar-original.jpg',
      neymarEdgesImage: '/images/chapter3/neymar-edges.jpg'
    }
  },
  computed: {
    allCasesCompleted() {
      return this.pixelCaseCompleted && this.edgeCaseCompleted
    }
  },
  methods: {
    // 像素案例交互方法
    handlePixelMouseEnter() {
      this.isPixelHovering = true
      this.pixelHoverStartTime = Date.now()
      
      // 增加交互次数
      this.pixelInteractionCount++
      
      // 检查是否完成案例
      this.checkPixelCaseCompletion()
    },
    
    handlePixelMouseLeave() {
      this.isPixelHovering = false
      
      // 计算悬停时间
      if (this.pixelHoverStartTime) {
        const hoverDuration = (Date.now() - this.pixelHoverStartTime) / 1000
        this.pixelTotalHoverTime += hoverDuration
        this.pixelHoverStartTime = null
      }
    },
    
    // 边缘检测案例交互方法
    handleEdgeMouseEnter() {
      this.isEdgeHovering = true
      this.edgeHoverStartTime = Date.now()
      
      // 增加交互次数
      this.edgeInteractionCount++
      
      // 检查是否完成案例
      this.checkEdgeCaseCompletion()
    },
    
    handleEdgeMouseLeave() {
      this.isEdgeHovering = false
      
      // 计算悬停时间
      if (this.edgeHoverStartTime) {
        const hoverDuration = (Date.now() - this.edgeHoverStartTime) / 1000
        this.edgeTotalHoverTime += hoverDuration
        this.edgeHoverStartTime = null
      }
    },
    
    checkPixelCaseCompletion() {
      if (this.pixelInteractionCount >= 5 && !this.pixelCaseCompleted) {
        this.pixelCaseCompleted = true
        this.$emit('case-completed', 'pixel-data')
        
        // 显示完成提示
        this.$message({
          message: '🎉 恭喜！你已经完成了像素数据理解案例！',
          type: 'success',
          duration: 3000
        })
        
        // 检查是否所有案例都完成
        this.checkAllCasesCompletion()
      }
    },
    
    checkEdgeCaseCompletion() {
      if (this.edgeInteractionCount >= 5 && !this.edgeCaseCompleted) {
        this.edgeCaseCompleted = true
        this.$emit('case-completed', 'edge-detection')
        
        // 显示完成提示
        this.$message({
          message: '🎉 恭喜！你已经完成了边缘特征提取案例！',
          type: 'success',
          duration: 3000
        })
        
        // 检查是否所有案例都完成
        this.checkAllCasesCompletion()
      }
    },
    
    checkAllCasesCompletion() {
      if (this.allCasesCompleted) {
        this.$emit('all-cases-completed')
        
        // 显示完成提示
        this.$message({
          message: '🎉 恭喜！你已经完成了第三章的所有案例学习！',
          type: 'success',
          duration: 3000
        })
      }
    },
    
    startQuiz() {
      if (!this.allCasesCompleted) {
        this.$message({
          message: '请先完成所有案例学习后再开始测验',
          type: 'warning',
          duration: 3000
        })
        return
      }
      this.$router.push(`/quiz/${this.chapterId}`)
    },
    
    // 案例切换方法
    switchToCase(caseType) {
      this.activeCase = caseType
      this.scrollToTop()
    },
    
    scrollToTop() {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@use 'sass:color';

.chapter3-case-study {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.case-header {
  text-align: center;
  margin-bottom: 3rem;
  padding: 2rem;
  background: linear-gradient(135deg, var(--accent-color, #3b82f6) 0%, var(--accent-color-light, #60a5fa) 100%);
  border-radius: 16px;
  color: white;
  box-shadow: 0 8px 32px rgba(var(--accent-color-rgb, 59, 130, 246), 0.3);
  border: 1px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.2);
}

.case-title {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  font-weight: 900;
}

.case-description {
  font-size: 1.2rem;
  opacity: 0.9;
  line-height: 1.6;
  margin-bottom: 2rem;
}

.progress-indicator {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

.progress-item {
  background: rgba(255, 255, 255, 0.15);
  border: 2px solid rgba(255, 255, 255, 0.25);
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: all 0.3s ease;
  min-width: 300px;
  backdrop-filter: blur(8px);
  
  &.completed {
    background: rgba(var(--success-color-rgb, 76, 175, 80), 0.25);
    border-color: rgba(var(--success-color-rgb, 76, 175, 80), 0.6);
    transform: scale(1.02);
    box-shadow: 0 4px 16px rgba(var(--success-color-rgb, 76, 175, 80), 0.3);
  }
}

.progress-icon {
  font-size: 2rem;
  min-width: 3rem;
  text-align: center;
}

.progress-info {
  flex: 1;
  
  h4 {
    color: white;
    margin: 0 0 0.5rem 0;
    font-size: 1.1rem;
    font-weight: 600;
  }
  
  p {
    color: rgba(255, 255, 255, 0.8);
    margin: 0 0 0.5rem 0;
    font-size: 0.9rem;
  }
}

.progress-status {
  .status-completed {
    color: var(--success-color, #4caf50);
    font-weight: 600;
    text-shadow: 0 1px 2px rgba(var(--success-color-rgb, 76, 175, 80), 0.3);
  }
  
  .status-pending {
    color: var(--warning-color, #ff9800);
    font-weight: 600;
    text-shadow: 0 1px 2px rgba(var(--warning-color-rgb, 255, 152, 0), 0.3);
  }
}

.case-section {
  background: var(--card-bg, #292c33);
  border-radius: var(--card-radius, 10px);
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: var(--card-shadow, 0 4px 24px rgba(24, 25, 26, 0.10));
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
}

.section-header {
  text-align: center;
  margin-bottom: 32px;

  .section-title {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    margin-bottom: 12px;
    position: relative;

    .title-icon {
      font-size: 2rem;
      filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
    }

    .title-text {
      background: linear-gradient(135deg, var(--primary-color, #18191a), var(--accent-color, #b0b3b8));
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      font-weight: 800;
      font-size: 2rem;
    }

    .title-decoration {
      position: absolute;
      bottom: -8px;
      left: 50%;
      transform: translateX(-50%);
      width: 60px;
      height: 3px;
      background: linear-gradient(90deg, var(--primary-color, #18191a), var(--accent-color, #b0b3b8));
      border-radius: 2px;
    }
  }

  .section-subtitle {
    color: var(--text-secondary-color, #b0b3b8);
    font-size: 1.1rem;
    font-weight: 500;
    margin: 0;
    opacity: 0.8;
  }
}

.image-interaction-card {
  background: linear-gradient(145deg, rgba(var(--card-bg-rgb, 41, 44, 51), 0.95) 0%, rgba(var(--card-bg-rgb, 41, 44, 51), 0.9) 50%, rgba(var(--card-bg-rgb, 41, 44, 51), 0.95) 100%);
  border: 2px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.2);
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 
    0 20px 25px -5px rgba(var(--shadow-color-rgb, 0, 0, 0), 0.2),
    0 10px 10px -5px rgba(var(--shadow-color-rgb, 0, 0, 0), 0.1),
    inset 0 1px 0 rgba(var(--highlight-color-rgb, 255, 255, 255), 0.1);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  margin: 24px 0;
  backdrop-filter: blur(12px);

  &:hover {
    transform: translateY(-2px);
    box-shadow: 
      0 32px 64px -12px rgba(var(--shadow-color-rgb, 0, 0, 0), 0.3),
      0 20px 25px -5px rgba(var(--shadow-color-rgb, 0, 0, 0), 0.2),
      inset 0 1px 0 rgba(var(--highlight-color-rgb, 255, 255, 255), 0.15);
  }

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, var(--primary-color, #18191a), var(--accent-color, #b0b3b8), var(--info-color, #4a90e2));
    background-size: 200% 100%;
    animation: shimmer 3s ease-in-out infinite;
  }

  @keyframes shimmer {
    0%, 100% { background-position: 200% 0; }
    50% { background-position: -200% 0; }
  }
}

.card-header {
  padding: 32px 32px 24px;
  background: linear-gradient(135deg, rgba(var(--card-bg-rgb, 41, 44, 51), 0.8) 0%, rgba(var(--card-bg-rgb, 41, 44, 51), 0.6) 100%);
  border-bottom: 2px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.15);

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;

    @media (max-width: 768px) {
      flex-direction: column;
      gap: 16px;
      text-align: center;
    }
  }

  .vision-logo {
    display: flex;
    align-items: center;
    gap: 16px;

    .logo-circle {
      width: 64px;
      height: 64px;
      background: linear-gradient(135deg, var(--primary-color, #18191a), var(--accent-color, #b0b3b8));
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
      position: relative;
      overflow: hidden;

      &::before {
        content: '';
        position: absolute;
        top: -50%;
        left: -50%;
        width: 200%;
        height: 200%;
        background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.3), transparent);
        animation: rotate 3s linear infinite;
      }

      .logo-icon {
        font-size: 1.8rem;
        z-index: 1;
        filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
      }
    }

    .logo-text {
      h4 {
        margin: 0 0 4px 0;
        font-size: 1.5rem;
        font-weight: 700;
        color: var(--text-color, #f5f6fa);
      }

      .logo-subtitle {
        color: var(--text-secondary-color, #b0b3b8);
        font-size: 0.9rem;
        font-weight: 500;
        opacity: 0.8;
      }
    }
  }

  .interaction-status {
    .status-indicator {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 16px;
      background: rgba(100, 116, 139, 0.1);
      border: 1px solid rgba(100, 116, 139, 0.2);
      border-radius: 20px;
      transition: all 0.3s ease;

      &.active {
        background: rgba(34, 197, 94, 0.1);
        border-color: rgba(34, 197, 94, 0.3);
        
        .status-dot {
          background: #22c55e;
        }
        
        .status-text {
          color: #16a34a;
        }
      }

      .status-dot {
        width: 8px;
        height: 8px;
        background: #64748b;
        border-radius: 50%;
        animation: pulse 2s infinite;
      }

      .status-text {
        color: #475569;
        font-weight: 600;
        font-size: 0.875rem;
      }
    }
  }

  @keyframes rotate {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
  }

  @keyframes pulse {
    0%, 100% { opacity: 1; }
    50% { opacity: 0.5; }
  }
}

.image-display-area {
  padding: 32px;
  background: linear-gradient(135deg, rgba(var(--card-bg-rgb, 41, 44, 51), 0.7) 0%, rgba(var(--card-bg-rgb, 41, 44, 51), 0.5) 100%);
  backdrop-filter: blur(8px);
}

.display-container {
  max-width: 800px;
  margin: 0 auto;
}

.interaction-guide {
  text-align: center;
  margin-bottom: 32px;

  h5 {
    font-size: 1.4rem;
    font-weight: 700;
    color: var(--text-color, #f5f6fa);
    margin-bottom: 8px;
  }

  p {
    color: var(--text-secondary-color, #b0b3b8);
    font-size: 1rem;
    margin: 0;
    opacity: 0.8;
  }
}

.image-container {
  position: relative;
  max-width: 500px;
  margin: 0 auto;
  cursor: pointer;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 25px -5px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

  &:hover {
    transform: scale(1.02);
    box-shadow: 0 20px 40px -5px rgba(0, 0, 0, 0.15);
  }

  .view-image {
    width: 100%;
    height: auto;
    display: block;
    transition: opacity 0.3s ease;
  }

  .hover-indicator {
    position: absolute;
    top: 16px;
    right: 16px;
    background: rgba(0, 0, 0, 0.8);
    color: white;
    padding: 8px 16px;
    border-radius: 20px;
    font-size: 0.9rem;
    font-weight: 600;
    display: flex;
    align-items: center;
    gap: 8px;
    transition: all 0.3s ease;
    opacity: 0.7;

    &.active {
      background: rgba(34, 197, 94, 0.9);
      opacity: 1;
      transform: scale(1.05);
    }

    .indicator-icon {
      font-size: 1.1rem;
    }
  }
}

.view-explanations {
  margin: 32px 0;
}

.explanation-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
    gap: 16px;
  }
}

.explanation-item {
  padding: 24px;
  background: linear-gradient(135deg, rgba(var(--card-bg-rgb, 41, 44, 51), 0.8) 0%, rgba(var(--card-bg-rgb, 41, 44, 51), 0.6) 100%);
  border-radius: 16px;
  border: 2px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.2);
  transition: all 0.3s ease;
  opacity: 0.6;
  backdrop-filter: blur(8px);

  &.active {
    opacity: 1;
    transform: translateY(-2px);
    box-shadow: 0 8px 25px -5px rgba(var(--shadow-color-rgb, 0, 0, 0), 0.2);
  }

  &.human.active {
    border-color: var(--info-color, #4a90e2);
    background: linear-gradient(135deg, rgba(var(--info-color-rgb, 74, 144, 226), 0.15) 0%, rgba(var(--info-color-rgb, 74, 144, 226), 0.08) 100%);
  }

  &.computer.active {
    border-color: var(--success-color, #4caf50);
    background: linear-gradient(135deg, rgba(var(--success-color-rgb, 76, 175, 80), 0.15) 0%, rgba(var(--success-color-rgb, 76, 175, 80), 0.08) 100%);
  }

  .explanation-icon {
    font-size: 2rem;
    margin-bottom: 12px;
  }

  .explanation-content {
    h6 {
      font-size: 1.2rem;
      font-weight: 600;
      color: var(--text-color, #f5f6fa);
      margin: 0 0 8px 0;
    }

    p {
      font-size: 0.95rem;
      color: var(--text-secondary-color, #b0b3b8);
      margin: 0;
      line-height: 1.5;
    }
  }
}

.technical-info {
  background: linear-gradient(135deg, rgba(34, 197, 94, 0.1) 0%, rgba(34, 197, 94, 0.05) 100%);
  border: 1px solid rgba(34, 197, 94, 0.2);
  border-radius: 12px;
  padding: 20px;
  margin-top: 24px;
  animation: fadeIn 0.3s ease;

  .info-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 16px;

    .info-icon {
      font-size: 1.2rem;
    }

    h6 {
      font-size: 1.1rem;
      font-weight: 600;
      color: var(--text-color, #f5f6fa);
      margin: 0;
    }
  }

  .rgb-explanation {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;

    @media (max-width: 768px) {
      grid-template-columns: 1fr;
      gap: 12px;
    }
  }

  .rgb-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding: 12px;
    background: white;
    border-radius: 8px;
    border: 1px solid rgba(34, 197, 94, 0.2);

    .rgb-label {
      font-weight: 700;
      color: var(--text-color, #f5f6fa);
      font-size: 0.9rem;
      margin-bottom: 4px;
    }

    .rgb-desc {
      font-size: 0.8rem;
      color: var(--text-secondary-color, #b0b3b8);
    }
  }
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.interaction-stats {
  padding: 24px 32px;
  background: linear-gradient(135deg, rgba(var(--card-bg-rgb, 41, 44, 51), 0.8) 0%, rgba(var(--card-bg-rgb, 41, 44, 51), 0.6) 100%);
  border-top: 2px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.15);
  backdrop-filter: blur(8px);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  max-width: 600px;
  margin: 0 auto;

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
    gap: 16px;
  }
}

.stat-item {
  text-align: center;
  padding: 16px;
  background: linear-gradient(135deg, rgba(var(--card-bg-rgb, 41, 44, 51), 0.9) 0%, rgba(var(--card-bg-rgb, 41, 44, 51), 0.7) 100%);
  border-radius: 12px;
  border: 2px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.2);
  transition: all 0.3s ease;
  backdrop-filter: blur(8px);

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(var(--accent-color-rgb, 59, 130, 246), 0.2);
    border-color: rgba(var(--accent-color-rgb, 59, 130, 246), 0.4);
  }

  .stat-value {
    display: block;
    font-size: 1.4rem;
    font-weight: 700;
    color: var(--text-color, #f5f6fa);
    margin-bottom: 4px;
  }

  .stat-label {
    font-size: 0.9rem;
    color: var(--text-secondary-color, #b0b3b8);
    font-weight: 500;
  }
}

.case-summary {
  background: linear-gradient(135deg, var(--success-color, #4caf50) 0%, var(--success-color-light, #66bb6a) 100%);
  border-radius: 16px;
  padding: 2rem;
  color: white;
  margin-bottom: 2rem;
  box-shadow: 0 8px 32px rgba(var(--success-color-rgb, 76, 175, 80), 0.3);
  border: 2px solid rgba(var(--success-color-rgb, 76, 175, 80), 0.2);
}

.summary-title {
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
  font-weight: 700;
  text-align: center;
}

.summary-content {
  display: flex;
  justify-content: center;
}

.summary-item {
  max-width: 800px;
  
  h4 {
    margin-bottom: 1rem;
    font-weight: 700;
    color: rgba(255, 255, 255, 0.9);
  }
  
  ul {
    list-style: none;
    padding: 0;
    
    li {
      margin-bottom: 0.5rem;
      padding-left: 1.5rem;
      position: relative;
      line-height: 1.6;
      
      &:before {
        content: '✓';
        position: absolute;
        left: 0;
        color: #FFD700;
        font-weight: bold;
      }
    }
  }
}

.next-step {
  border-radius: 16px;
  padding: 2rem;
  text-align: center;
  color: white;
  transition: all 0.3s ease;
  
  &:not(.completed) {
    background: linear-gradient(135deg, var(--warning-color, #ff9800) 0%, var(--warning-color-light, #ffb74d) 100%);
    box-shadow: 0 8px 32px rgba(var(--warning-color-rgb, 255, 152, 0), 0.3);
    border: 2px solid rgba(var(--warning-color-rgb, 255, 152, 0), 0.2);
  }
  
  &.completed {
    background: linear-gradient(135deg, var(--success-color, #4caf50) 0%, var(--success-color-light, #66bb6a) 100%);
    box-shadow: 0 8px 32px rgba(var(--success-color-rgb, 76, 175, 80), 0.3);
    border: 2px solid rgba(var(--success-color-rgb, 76, 175, 80), 0.2);
  }
}

.completion-celebration {
  h3 {
    font-size: 2rem;
    margin-bottom: 1rem;
    animation: bounce 1s ease-in-out;
  }
  
  p {
    font-size: 1.1rem;
    margin-bottom: 2rem;
    line-height: 1.6;
  }
}

.completion-guide {
  h3 {
    font-size: 1.8rem;
    margin-bottom: 1rem;
  }
  
  p {
    font-size: 1.1rem;
    margin-bottom: 2rem;
    line-height: 1.6;
  }
}

.remaining-tasks {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 1.5rem;
  margin: 2rem 0;
  text-align: left;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}

.task-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  justify-content: center;
}

.task-icon {
  font-size: 1.5rem;
}

.task-text {
  font-size: 1rem;
  line-height: 1.5;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
}

.btn-quiz {
  &.btn-enabled {
    background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
    color: white;
    box-shadow: 0 4px 20px rgba(249, 115, 22, 0.25);
    
    &:hover {
      background: linear-gradient(135deg, #ea580c 0%, #dc2626 100%);
      transform: translateY(-3px);
      box-shadow: 0 12px 35px rgba(249, 115, 22, 0.4);
    }
  }
}

.btn-guide {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.3);
  padding: 1rem 2rem;
  border-radius: 12px;
  font-size: 1.1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    background: rgba(255, 255, 255, 0.3);
    transform: translateY(-2px);
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

// 案例导航样式
.case-navigation {
  margin: 2rem 0;
  padding: 1.5rem;
  background: linear-gradient(135deg, rgba(var(--card-bg-rgb, 41, 44, 51), 0.95) 0%, rgba(var(--card-bg-rgb, 41, 44, 51), 0.9) 100%);
  border-radius: 16px;
  border: 2px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.15);
  box-shadow: 0 8px 32px rgba(var(--shadow-color-rgb, 0, 0, 0), 0.15);
  backdrop-filter: blur(12px);
}

.nav-buttons {
  display: flex;
  gap: 1.5rem;
  justify-content: center;
  align-items: center;
}

.nav-btn {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2rem;
  border: 2px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.2);
  border-radius: 12px;
  background: linear-gradient(135deg, rgba(var(--card-bg-rgb, 41, 44, 51), 0.8) 0%, rgba(var(--card-bg-rgb, 41, 44, 51), 0.6) 100%);
  color: var(--text-color, #f5f6fa);
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px rgba(var(--shadow-color-rgb, 0, 0, 0), 0.1);
  min-width: 200px;
  justify-content: center;
  backdrop-filter: blur(8px);
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(var(--accent-color-rgb, 59, 130, 246), 0.2);
    border-color: rgba(var(--accent-color-rgb, 59, 130, 246), 0.4);
    background: linear-gradient(135deg, rgba(var(--accent-color-rgb, 59, 130, 246), 0.1) 0%, rgba(var(--accent-color-rgb, 59, 130, 246), 0.05) 100%);
  }
  
  &.active {
    background: linear-gradient(135deg, var(--accent-color, #3b82f6) 0%, var(--accent-color-light, #60a5fa) 100%);
    color: white;
    border-color: var(--accent-color, #3b82f6);
    transform: translateY(-1px);
    box-shadow: 0 8px 25px rgba(var(--accent-color-rgb, 59, 130, 246), 0.3);
    
    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 12px 35px rgba(var(--accent-color-rgb, 59, 130, 246), 0.4);
    }
  }
}

.nav-icon {
  font-size: 1.5rem;
  display: flex;
  align-items: center;
}

.nav-text {
  font-weight: 700;
  letter-spacing: 0.025em;
}

@media (max-width: 768px) {
  .chapter3-case-study {
    padding: 1rem;
  }
  
  .case-title {
    font-size: 2rem;
  }
  
  .image-container {
    max-width: 100%;
  }
  
  .card-header {
    padding: 24px 16px 16px;
  }
  
  .image-display-area {
    padding: 24px 16px;
  }
  
  .interaction-stats {
    padding: 16px;
  }
  
  .nav-buttons {
    flex-direction: column;
    gap: 1rem;
  }
  
  .nav-btn {
    min-width: 100%;
    padding: 1.25rem 1.5rem;
    font-size: 1rem;
  }
}
</style>
