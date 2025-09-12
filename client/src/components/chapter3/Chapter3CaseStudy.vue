<template>
  <div class="chapter3-case-study">
    <div class="case-header">
      <h2 class="case-title">🎯 第三章案例学习</h2>
      <p class="case-description">
        通过两个交互式案例深入理解计算机视觉的基础概念
      </p>
      
      <!-- 学习进度指示器 -->
      <ProgressIndicator 
        :pixel-case-completed="pixelCaseCompleted"
        :edge-case-completed="edgeCaseCompleted"
        :pixel-interaction-count="pixelInteractionCount"
        :edge-interaction-count="edgeInteractionCount"
      />
    </div>

    <!-- 案例导航 -->
    <div class="case-navigation">
      <div class="nav-tabs">
        <button 
          v-for="caseItem in caseList" 
          :key="caseItem.id"
          class="nav-tab"
          :class="{ active: activeCase === caseItem.id }"
          @click="switchCase(caseItem.id)"
        >
          <span class="tab-icon">{{ caseItem.icon }}</span>
          <span class="tab-title">{{ caseItem.title }}</span>
        </button>
      </div>
    </div>

    <!-- 像素数据案例 -->
    <PixelDataCase 
      v-if="activeCase === 'pixel'"
      :pixel-interaction-count="pixelInteractionCount"
      :pixel-total-hover-time="pixelTotalHoverTime"
      @case-completed="handlePixelCaseCompleted"
      @interaction-update="handlePixelInteractionUpdate"
    />

    <!-- 边缘特征提取案例 -->
    <EdgeDetectionCase 
      v-if="activeCase === 'edge'"
      :edge-interaction-count="edgeInteractionCount"
      :edge-total-hover-time="edgeTotalHoverTime"
      @case-completed="handleEdgeCaseCompleted"
      @interaction-update="handleEdgeInteractionUpdate"
    />

    <!-- 案例总结 -->
    <CaseSummary 
      :all-cases-completed="allCasesCompleted"
      :pixel-case-completed="pixelCaseCompleted"
      :edge-case-completed="edgeCaseCompleted"
      :pixel-interaction-count="pixelInteractionCount"
      :edge-interaction-count="edgeInteractionCount"
      :pixel-total-hover-time="pixelTotalHoverTime"
      :edge-total-hover-time="edgeTotalHoverTime"
      @start-quiz="startQuiz"
      @scroll-to-top="scrollToTop"
    />
  </div>
</template>

<script>
import ProgressIndicator from './ProgressIndicator.vue'
import PixelDataCase from './PixelDataCase.vue'
import EdgeDetectionCase from './EdgeDetectionCase.vue'
import CaseSummary from './CaseSummary.vue'

export default {
  name: 'Chapter3CaseStudy',
  components: {
    ProgressIndicator,
    PixelDataCase,
    EdgeDetectionCase,
    CaseSummary
  },
  props: {
    chapterId: {
      type: String,
      default: '3'
    }
  },
  emits: ['case-completed', 'all-cases-completed', 'start-quiz'],
  data() {
    return {
      // 当前激活的案例
      activeCase: 'pixel',
      
      // 像素数据案例相关数据
      pixelInteractionCount: 0,
      pixelTotalHoverTime: 0,
      pixelCaseCompleted: false,
      
      // 边缘检测案例相关数据
      edgeInteractionCount: 0,
      edgeTotalHoverTime: 0,
      edgeCaseCompleted: false,
      
      // 案例导航列表
      caseList: [
        { id: 'pixel', icon: '🖼️', title: '像素数据案例' },
        { id: 'edge', icon: '🔍', title: '边缘特征案例' }
      ]
    }
  },
  computed: {
    // 计算所有案例是否完成
    allCasesCompleted() {
      return this.pixelCaseCompleted && this.edgeCaseCompleted
    }
  },
  methods: {
    // 案例切换方法
    switchCase(caseId) {
      this.activeCase = caseId
    },
    
    // 处理像素案例完成
    handlePixelCaseCompleted(data) {
      this.pixelInteractionCount = data.interactionCount
      this.pixelCaseCompleted = data.completed
      if (data.completed) {
        this.$emit('case-completed', { caseId: 'pixel', data })
      }
    },
    
    // 处理边缘检测案例完成
    handleEdgeCaseCompleted(data) {
      this.edgeInteractionCount = data.interactionCount
      this.edgeCaseCompleted = data.completed
      if (data.completed) {
        this.$emit('case-completed', { caseId: 'edge', data })
      }
    },

    // 处理边缘检测交互更新
    handleEdgeInteractionUpdate(data) {
      this.edgeInteractionCount = data.interactionCount
      this.edgeTotalHoverTime = data.totalHoverTime
    },

    // 处理像素案例交互更新
    handlePixelInteractionUpdate(data) {
      this.pixelInteractionCount = data.interactionCount
      this.pixelTotalHoverTime = data.totalHoverTime
    },
    
    // 启动测验
    startQuiz() {
      this.$emit('start-quiz')
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
.chapter3-case-study {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.case-header {
  text-align: center;
  margin-bottom: 3rem;
  padding: 2rem;
  background: var(--chapter3-header-bg, linear-gradient(135deg, #3b82f6 0%, #60a5fa 100%));
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

// 案例导航样式
.case-navigation {
  margin-bottom: 3rem;
}

.nav-tabs {
  display: flex;
  justify-content: center;
  gap: 1rem;
  background: var(--chapter3-card-bg, rgba(31, 41, 55, 0.95));
  padding: 1rem;
  border-radius: 16px;
  border: 2px solid var(--chapter3-card-border, rgba(59, 130, 246, 0.2));
}

.nav-tab {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2rem;
  background: transparent;
  border: 2px solid transparent;
  border-radius: 12px;
  color: var(--text-color, #ffffff);
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 200px;
  justify-content: center;
  
  &:hover {
    background: var(--chapter3-explanation-bg, rgba(31, 41, 55, 0.8));
    border-color: var(--chapter3-explanation-border, rgba(59, 130, 246, 0.2));
    transform: translateY(-2px);
  }
  
  &.active {
    background: var(--chapter3-human-active-bg, rgba(6, 182, 212, 0.15));
    border-color: var(--chapter3-human-active-border, #06b6d4);
    color: var(--chapter3-human-active-border, #06b6d4);
    box-shadow: 0 4px 16px rgba(6, 182, 212, 0.2);
  }
}

.tab-icon {
  font-size: 1.25rem;
}

.tab-title {
  font-weight: 600;
}

// 响应式设计
@media (max-width: 768px) {
  .chapter3-case-study {
    padding: 1rem;
  }
  
  .case-header {
    padding: 1.5rem;
    margin-bottom: 2rem;
  }
  
  .case-title {
    font-size: 2rem;
  }
  
  .case-description {
    font-size: 1rem;
  }
  
  .nav-tabs {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .nav-tab {
    min-width: auto;
    width: 100%;
  }
}

@media (max-width: 480px) {
  .case-header {
    padding: 1rem;
  }
  
  .case-title {
    font-size: 1.75rem;
  }
}
</style>
