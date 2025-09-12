<template>
  <div class="case-summary-section">
    <div class="summary-header">
      <h3 class="summary-title">
        <span class="title-icon">📚</span>
        <span class="title-text">案例总结</span>
        <span class="title-decoration"></span>
      </h3>
      <p class="summary-subtitle">通过实践理解计算机视觉的基础概念</p>
    </div>
    
    <div class="summary-content">
      <!-- 核心概念总结 -->
      <div class="concepts-summary">
        <h4 class="concepts-title">
          <span class="concepts-icon">💡</span>
          核心概念回顾
        </h4>
        
        <div class="concepts-grid">
          <div class="concept-item" v-for="concept in coreConcepts" :key="concept.id">
            <div class="concept-icon">{{ concept.icon }}</div>
            <div class="concept-content">
              <h5>{{ concept.title }}</h5>
              <p>{{ concept.description }}</p>
              <div class="concept-tags">
                <span v-for="tag in concept.tags" :key="tag" class="concept-tag">{{ tag }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 学习成果展示 -->
      <div class="achievements-summary">
        <h4 class="achievements-title">
          <span class="achievements-icon">🎯</span>
          学习成果
        </h4>
        
        <div class="achievements-grid">
          <div class="achievement-item" v-for="achievement in learningAchievements" :key="achievement.id">
            <div class="achievement-badge">
              <span class="badge-icon">{{ achievement.icon }}</span>
            </div>
            <div class="achievement-content">
              <h6>{{ achievement.title }}</h6>
              <p>{{ achievement.description }}</p>
              <div class="achievement-progress">
                <div class="progress-bar">
                  <div class="progress-fill" :style="{ width: achievement.progress + '%' }"></div>
                </div>
                <span class="progress-text">{{ achievement.progress }}%</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 关键洞察 -->
      <div class="insights-summary">
        <h4 class="insights-title">
          <span class="insights-icon">🔍</span>
          关键洞察
        </h4>
        
        <div class="insights-list">
          <div class="insight-item" v-for="insight in keyInsights" :key="insight.id">
            <div class="insight-marker"></div>
            <div class="insight-content">
              <h6>{{ insight.title }}</h6>
              <p>{{ insight.description }}</p>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 下一步提示 -->
      <div class="next-steps">
        <div class="next-steps-header">
          <h4 class="next-steps-title">
            <span class="next-steps-icon">🚀</span>
            下一步学习
          </h4>
          <p class="next-steps-subtitle">继续深入探索人工智能的奥秘</p>
        </div>
        
        <div class="completion-status" :class="{ completed: allCasesCompleted }">
          <div class="status-icon">
            <span v-if="allCasesCompleted">✅</span>
            <span v-else>⏳</span>
          </div>
          <div class="status-content">
            <h5>{{ allCasesCompleted ? '恭喜！案例学习完成' : '继续完成交互任务' }}</h5>
            <p v-if="allCasesCompleted">
              您已成功完成第三章的所有案例学习，对计算机视觉有了基础认识
            </p>
            <p v-else>
              请完成上述所有交互任务，体验人工智能的视觉处理能力
            </p>
          </div>
        </div>
        
        <div class="action-buttons">
          <button class="action-btn primary" @click="startQuiz" :disabled="!allCasesCompleted">
            <span class="btn-icon">📝</span>
            <span class="btn-text">开始测验</span>
          </button>
          <button class="action-btn secondary" @click="reviewCases">
            <span class="btn-icon">🔄</span>
            <span class="btn-text">重新体验</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CaseSummary',
  props: {
    allCasesCompleted: {
      type: Boolean,
      default: false
    },
    pixelCaseCompleted: {
      type: Boolean,
      default: false
    },
    edgeCaseCompleted: {
      type: Boolean,
      default: false
    }
  },
  emits: ['start-quiz', 'review-cases'],
  data() {
    return {
      coreConcepts: [
        {
          id: 1,
          icon: '👁️',
          title: '人眼vs机器视觉',
          description: '理解人类视觉系统与计算机视觉系统在信息处理方式上的根本差异',
          tags: ['视觉感知', '数据表示', '认知差异']
        },
        {
          id: 2,
          icon: '🎨',
          title: '像素数据表示',
          description: '掌握图像在计算机中的数字化表示方法，理解RGB颜色模型',
          tags: ['像素', 'RGB', '数字图像']
        },
        {
          id: 3,
          icon: '🔍',
          title: '边缘检测算法',
          description: '了解Sobel算子等边缘检测算法的基本原理和应用场景',
          tags: ['特征提取', 'Sobel算子', '图像处理']
        },
        {
          id: 4,
          icon: '⚡',
          title: '特征提取',
          description: '认识计算机视觉中特征提取的重要性和基本方法',
          tags: ['特征工程', '模式识别', '算法应用']
        }
      ],
      learningAchievements: [
        {
          id: 1,
          icon: '🎯',
          title: '视觉差异理解',
          description: '成功体验并理解人眼与计算机视觉的差异',
          progress: this.pixelCaseCompleted ? 100 : 60
        },
        {
          id: 2,
          icon: '🔬',
          title: '算法实践体验',
          description: '通过交互体验了解边缘检测算法的效果',
          progress: this.edgeCaseCompleted ? 100 : 40
        },
        {
          id: 3,
          icon: '📊',
          title: '数据处理认知',
          description: '建立对图像数据处理流程的基本认识',
          progress: this.allCasesCompleted ? 100 : 70
        }
      ],
      keyInsights: [
        {
          id: 1,
          title: '数据是AI的基础',
          description: '计算机视觉的所有处理都基于数字化的像素数据，这是AI理解世界的起点'
        },
        {
          id: 2,
          title: '算法决定能力边界',
          description: '不同的算法能够提取不同类型的特征，算法的选择直接影响AI的感知能力'
        },
        {
          id: 3,
          title: '特征提取是关键步骤',
          description: '从原始数据中提取有意义的特征是计算机视觉成功的关键环节'
        },
        {
          id: 4,
          title: '人机协作的价值',
          description: '理解人类视觉与机器视觉的差异有助于更好地设计人机协作系统'
        }
      ]
    }
  },
  methods: {
    startQuiz() {
      this.$emit('start-quiz')
    },
    
    reviewCases() {
      this.$emit('review-cases')
    }
  }
}
</script>

<style lang="scss" scoped>
.case-summary-section {
  margin-top: 3rem;
  padding: 2rem;
  background: var(--card-bg);
  border-radius: 16px;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--border-color);
}

.summary-header {
  text-align: center;
  margin-bottom: 3rem;
}

.summary-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-color);
  margin-bottom: 1rem;
  
  .title-icon {
    font-size: 2.5rem;
  }
  
  .title-decoration {
    width: 4rem;
    height: 4px;
    background: linear-gradient(90deg, var(--accent-color), transparent);
    border-radius: 2px;
  }
}

.summary-subtitle {
  font-size: 1.1rem;
  color: var(--text-secondary-color);
  line-height: 1.6;
}

.summary-content {
  display: flex;
  flex-direction: column;
  gap: 3rem;
}

/* 核心概念总结 */
.concepts-summary {
  .concepts-title {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    font-size: 1.3rem;
    font-weight: 600;
    color: var(--text-color);
    margin-bottom: 1.5rem;
    
    .concepts-icon {
      font-size: 1.5rem;
    }
  }
}

.concepts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.concept-item {
  background: var(--secondary-color);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  gap: 1rem;
  
  .concept-icon {
    font-size: 2rem;
    min-width: 2.5rem;
    text-align: center;
  }
  
  .concept-content {
    flex: 1;
    
    h5 {
      color: var(--text-color);
      margin: 0 0 0.5rem 0;
      font-size: 1rem;
      font-weight: 600;
    }
    
    p {
      color: var(--text-secondary-color);
      margin: 0 0 1rem 0;
      font-size: 0.9rem;
      line-height: 1.5;
    }
  }
}

.concept-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.concept-tag {
  background: var(--accent-color);
  background: rgba(var(--accent-color-rgb, 59, 130, 246), 0.1);
  color: var(--accent-color);
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 500;
}

/* 学习成果展示 */
.achievements-summary {
  .achievements-title {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    font-size: 1.3rem;
    font-weight: 600;
    color: var(--text-color);
    margin-bottom: 1.5rem;
    
    .achievements-icon {
      font-size: 1.5rem;
    }
  }
}

.achievements-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.5rem;
}

.achievement-item {
  background: var(--secondary-color);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  gap: 1rem;
  align-items: flex-start;
}

.achievement-badge {
  width: 3rem;
  height: 3rem;
  background: var(--accent-color);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  
  .badge-icon {
    font-size: 1.5rem;
    color: white;
  }
}

.achievement-content {
  flex: 1;
  
  h6 {
    color: var(--text-color);
    margin: 0 0 0.5rem 0;
    font-size: 1rem;
    font-weight: 600;
  }
  
  p {
    color: var(--text-secondary-color);
    margin: 0 0 1rem 0;
    font-size: 0.9rem;
    line-height: 1.5;
  }
}

.achievement-progress {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.progress-bar {
  flex: 1;
  height: 6px;
  background: var(--border-color);
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: var(--accent-color);
  border-radius: 3px;
  transition: width 0.3s ease;
}

.progress-text {
  color: var(--accent-color);
  font-size: 0.85rem;
  font-weight: 600;
  min-width: 3rem;
}

/* 关键洞察 */
.insights-summary {
  .insights-title {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    font-size: 1.3rem;
    font-weight: 600;
    color: var(--text-color);
    margin-bottom: 1.5rem;
    
    .insights-icon {
      font-size: 1.5rem;
    }
  }
}

.insights-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.insight-item {
  display: flex;
  gap: 1rem;
  align-items: flex-start;
  padding: 1rem;
  background: var(--secondary-color);
  border-radius: 8px;
  border-left: 4px solid var(--accent-color);
}

.insight-marker {
  width: 8px;
  height: 8px;
  background: var(--accent-color);
  border-radius: 50%;
  margin-top: 0.5rem;
  flex-shrink: 0;
}

.insight-content {
  h6 {
    color: var(--text-color);
    margin: 0 0 0.5rem 0;
    font-size: 1rem;
    font-weight: 600;
  }
  
  p {
    color: var(--text-secondary-color);
    margin: 0;
    font-size: 0.9rem;
    line-height: 1.5;
  }
}

/* 下一步提示 */
.next-steps {
  background: linear-gradient(135deg, var(--accent-color, #3b82f6) 0%, var(--accent-light-color, #60a5fa) 100%);
  background: rgba(var(--accent-color-rgb, 59, 130, 246), 0.05);
  border: 1px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.2);
  border-radius: 16px;
  padding: 2rem;
}

.next-steps-header {
  text-align: center;
  margin-bottom: 2rem;
  
  .next-steps-title {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
    font-size: 1.3rem;
    font-weight: 600;
    color: var(--text-color);
    margin-bottom: 0.5rem;
    
    .next-steps-icon {
      font-size: 1.5rem;
    }
  }
  
  .next-steps-subtitle {
    color: var(--text-secondary-color);
    font-size: 1rem;
  }
}

.completion-status {
  display: flex;
  gap: 1rem;
  align-items: center;
  padding: 1.5rem;
  background: var(--card-bg);
  border-radius: 12px;
  margin-bottom: 2rem;
  border: 2px solid var(--border-color);
  
  &.completed {
    border-color: var(--success-color, #10b981);
    background: rgba(16, 185, 129, 0.05);
  }
}

.status-icon {
  font-size: 2rem;
  min-width: 2.5rem;
  text-align: center;
}

.status-content {
  h5 {
    color: var(--text-color);
    margin: 0 0 0.5rem 0;
    font-size: 1.1rem;
    font-weight: 600;
  }
  
  p {
    color: var(--text-secondary-color);
    margin: 0;
    font-size: 0.95rem;
    line-height: 1.5;
  }
}

.action-buttons {
  display: flex;
  gap: 1rem;
  justify-content: center;
  
  @media (max-width: 768px) {
    flex-direction: column;
  }
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  border: none;
  
  &.primary {
    background: var(--accent-color);
    color: white;
    
    &:hover:not(:disabled) {
      background: var(--accent-dark-color, #2563eb);
    }
    
    &:disabled {
      background: var(--border-color);
      color: var(--text-secondary-color);
      cursor: not-allowed;
    }
  }
  
  &.secondary {
    background: var(--secondary-color);
    color: var(--text-color);
    border: 1px solid var(--border-color);
    
    &:hover {
      background: var(--border-color);
    }
  }
  
  .btn-icon {
    font-size: 1rem;
  }
}

/* 浅色主题适配 */
.light-theme {
  .case-summary-section {
    background: var(--card-bg);
    border-color: var(--border-color);
  }
  
  .concept-item,
  .achievement-item,
  .insight-item {
    background: var(--card-bg);
    border-color: var(--border-color);
  }
  
  .next-steps {
    background: rgba(59, 130, 246, 0.05);
    border-color: rgba(59, 130, 246, 0.2);
  }
  
  .completion-status {
    background: var(--card-bg);
    
    &.completed {
      background: rgba(16, 185, 129, 0.05);
      border-color: rgba(16, 185, 129, 0.3);
    }
  }
}
</style>