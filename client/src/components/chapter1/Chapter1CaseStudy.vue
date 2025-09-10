<template>
  <div class="case-study-section">
    <div class="case-study-header">
      <h2 class="case-study-title">🎯 第一章案例演示</h2>
      <p class="case-study-description">通过实际案例学习，掌握人工智能的基本概念和应用</p>
    </div>
    

    
    <!-- 第一题：雪纺裙图片 -->
    <div class="case-study-card">
      <div class="case-study-question">
        <h3 class="question-title">题目 1：雪纺裙的图片</h3>
        <p class="question-description">从两张图片中判断哪一张是真实拍摄，哪一张是 AI 生成。</p>
      </div>
      
      <div class="case-study-options">
        <!-- A选项 -->
        <div class="option-card option-a clickable" @click="selectOption(1, 'A')">
          <h4 class="option-label">A</h4>
          <div class="option-image">
            <img src="/images/chapter1/A_真实照片.png" alt="A 选项图片" />
          </div>
        </div>
        
        <!-- B选项 -->
        <div class="option-card option-b clickable" @click="selectOption(1, 'B')">
          <h4 class="option-label">B</h4>
          <div class="option-image">
            <img src="/images/chapter1/A_ai生成.png" alt="B 选项图片" />
          </div>
        </div>
      </div>
      
      <!-- 答题反馈 -->
      <div v-if="questionResults[1]" class="feedback-card" :class="{ 'correct': questionResults[1].correct, 'incorrect': !questionResults[1].correct }">
        <div class="feedback-icon">{{ questionResults[1].correct ? '🎉' : '💡' }}</div>
        <div class="feedback-message">{{ questionResults[1].message }}</div>
        <button @click="resetQuestion(1)" class="btn btn-outline">
          再试一次
        </button>
      </div>
    </div>

    <!-- 第三题：城市街景 -->
    <div class="case-study-card">
      <div class="case-study-question">
        <h3 class="question-title">题目 3：城市街景（阿姆斯特丹）</h3>
        <p class="question-description">从两张图片中判断哪一张是真实拍摄，哪一张是 AI 生成。</p>
      </div>
      
      <div class="case-study-options">
        <!-- A选项 -->
        <div class="option-card option-a clickable" @click="selectOption(3, 'A')">
          <h4 class="option-label">A</h4>
          <div class="option-image">
            <img src="/images/chapter1/B_真实图片.png" alt="A 真实图片" />
          </div>
        </div>
        
        <!-- B选项 -->
        <div class="option-card option-b clickable" @click="selectOption(3, 'B')">
          <h4 class="option-label">B</h4>
          <div class="option-image">
            <img src="/images/chapter1/B_ai生成.png" alt="B AI 图片" />
          </div>
        </div>
      </div>
      
      <!-- 答题反馈 -->
      <div v-if="questionResults[3]" class="feedback-card" :class="{ 'correct': questionResults[3].correct, 'incorrect': !questionResults[3].correct }">
        <div class="feedback-icon">{{ questionResults[3].correct ? '🎉' : '💡' }}</div>
        <div class="feedback-message">{{ questionResults[3].message }}</div>
        <button @click="resetQuestion(3)" class="btn btn-outline">
          再试一次
        </button>
      </div>
    </div>

    <!-- 第四题：论文语句（AI vs 优秀论文） -->
    <div class="case-study-card">
      <div class="case-study-question">
        <h3 class="question-title">题目 4：论文语句（AI vs 优秀论文）</h3>
        <p class="question-description">请阅读两段文字，判断哪段是AI生成，哪段是人类优秀论文。</p>
      </div>
      
      <div class="case-study-options">
        <!-- A选项 -->
        <div class="option-card option-a clickable" @click="selectOption(4, 'A')">
          <h4 class="option-label">A</h4>
          <div class="option-text">
            <p>在现有的研究体系中，对于不确定性的探讨大多集中在宏观经济不确定性（Jurado et al., 2015）以及经济政策不确定性（Baker et al., 2016），而相较之下，金融市场层面的不确定性议题则明显被边缘化。这种现象在一定程度上反映了传统文献对于宏观经济变量影响力的过度强调，而对金融市场内生波动性的关注不足。</p>
          </div>
        </div>
        
        <!-- B选项 -->
        <div class="option-card option-b clickable" @click="selectOption(4, 'B')">
          <h4 class="option-label">B</h4>
          <div class="option-text">
            <p>目前，关于不确定性的研究主要集中在宏观经济不确定性(Jurado et al.，2015) 和经济政策不确定性(Baker et al., 2016) ，关于金融市场的不确定性关注较少。因为在传统文献中，金融市场主要受到宏观经济影响。</p>
            <div class="text-source">引自：黄卓,邱晗,沈艳 & 童晨.(2018).测量中国的金融不确定性——基于大数据的方法.金融研究,(11),30-46.</div>
          </div>
        </div>
      </div>
      
      <!-- 答题反馈 -->
      <div v-if="questionResults[4]" class="feedback-card" :class="{ 'correct': questionResults[4].correct, 'incorrect': !questionResults[4].correct }">
        <div class="feedback-icon">{{ questionResults[4].correct ? '🎉' : '💡' }}</div>
        <div class="feedback-message">{{ questionResults[4].message }}</div>
        <button @click="resetQuestion(4)" class="btn btn-outline">
          再试一次
        </button>
      </div>
    </div>

    <!-- 第六题：麻辣烫店铺 -->
    <div class="case-study-card">
      <div class="case-study-question">
        <h3 class="question-title">题目 6：麻辣烫店铺</h3>
        <p class="question-description">从两张图片中判断哪一张是真实拍摄，哪一张是 AI 生成。</p>
      </div>
      
      <div class="case-study-options">
        <!-- A选项 -->
        <div class="option-card option-a clickable" @click="selectOption(6, 'A')">
          <h4 class="option-label">A</h4>
          <div class="option-image">
            <img src="/images/chapter1/C_真实图片.png" alt="A 真实图片" />
          </div>
        </div>
        
        <!-- B选项 -->
        <div class="option-card option-b clickable" @click="selectOption(6, 'B')">
          <h4 class="option-label">B</h4>
          <div class="option-image">
            <img src="/images/chapter1/C_ai生成.png" alt="B AI 图片" />
          </div>
        </div>
      </div>
      
      <!-- 答题反馈 -->
      <div v-if="questionResults[6]" class="feedback-card" :class="{ 'correct': questionResults[6].correct, 'incorrect': !questionResults[6].correct }">
        <div class="feedback-icon">{{ questionResults[6].correct ? '🎉' : '💡' }}</div>
        <div class="feedback-message">{{ questionResults[6].message }}</div>
        <button @click="resetQuestion(6)" class="btn btn-outline">
          再试一次
        </button>
      </div>
    </div>

    <!-- 第七题：沸腾火锅 -->
    <div class="case-study-card">
      <div class="case-study-question">
        <h3 class="question-title">题目 7：沸腾的火锅</h3>
        <p class="question-description">从两张图片中判断哪一张是真实拍摄，哪一张是 AI 生成。</p>
      </div>
      
      <div class="case-study-options">
        <!-- A选项 -->
        <div class="option-card option-a clickable" @click="selectOption(7, 'A')">
          <h4 class="option-label">A</h4>
          <div class="option-image">
            <img src="/images/chapter1/D_真实图片.png" alt="A 真实图片" />
          </div>
        </div>
        
        <!-- B选项 -->
        <div class="option-card option-b clickable" @click="selectOption(7, 'B')">
          <h4 class="option-label">B</h4>
          <div class="option-image">
            <img src="/images/chapter1/D_ai生成.png" alt="B AI 图片" />
          </div>
        </div>
      </div>
      
      <!-- 答题反馈 -->
      <div v-if="questionResults[7]" class="feedback-card" :class="{ 'correct': questionResults[7].correct, 'incorrect': !questionResults[7].correct }">
        <div class="feedback-icon">{{ questionResults[7].correct ? '🎉' : '💡' }}</div>
        <div class="feedback-message">{{ questionResults[7].message }}</div>
        <button @click="resetQuestion(7)" class="btn btn-outline">
          再试一次
        </button>
      </div>
    </div>

    <!-- 第八题：哲学语句 -->
    <div class="case-study-card">
      <div class="case-study-question">
        <h3 class="question-title">题目 8：人类哲学家（黑格尔）vs AI 哲学家</h3>
        <p class="question-description">请阅读两段哲学语句，判断哪段是黑格尔原句，哪段是AI生成。</p>
      </div>
      
      <div class="case-study-options">
        <!-- A选项 -->
        <div class="option-card option-a clickable" @click="selectOption(8, 'A')">
          <h4 class="option-label">A</h4>
          <div class="option-text">
            <p>精神不是一个存在在外的东西，而是自我认识、自我实现的过程。</p>
            <div class="text-source">黑格尔. (1979). 精神现象学 (贺麟 译). 商务印书馆. (原著发表于 1807)</div>
          </div>
        </div>
        
        <!-- B选项 -->
        <div class="option-card option-b clickable" @click="selectOption(8, 'B')">
          <h4 class="option-label">B</h4>
          <div class="option-text">
            <p>意识不是孤立的旁观者，而是不断映照自身的镜子，通过反思获得存在的意义。</p>
          </div>
        </div>
      </div>
      
      <!-- 答题反馈 -->
      <div v-if="questionResults[8]" class="feedback-card" :class="{ 'correct': questionResults[8].correct, 'incorrect': !questionResults[8].correct }">
        <div class="feedback-icon">{{ questionResults[8].correct ? '🎉' : '💡' }}</div>
        <div class="feedback-message">{{ questionResults[8].message }}</div>
        <button @click="resetQuestion(8)" class="btn btn-outline">
          再试一次
        </button>
      </div>
    </div>

    <!-- 第九题：古典画作 -->
    <div class="case-study-card">
      <div class="case-study-question">
        <h3 class="question-title">题目 9：古典画作 vs AI 画作</h3>
        <p class="question-description">以1665年荷兰画家约翰内斯·维米尔《戴珍珠耳环的少女》为例。</p>
      </div>
      
      <div class="case-study-options">
        <!-- A选项 -->
        <div class="option-card option-a clickable" @click="selectOption(9, 'A')">
          <h4 class="option-label">A</h4>
          <div class="option-image">
            <img src="/images/chapter1/E_真实图片.png" alt="A 真实画作" />
          </div>
        </div>
        
        <!-- B选项 -->
        <div class="option-card option-b clickable" @click="selectOption(9, 'B')">
          <h4 class="option-label">B</h4>
          <div class="option-image">
            <img src="/images/chapter1/E_ai生成.png" alt="B 选项画作" />
          </div>
        </div>
      </div>
      
      <!-- 答题反馈 -->
      <div v-if="questionResults[9]" class="feedback-card" :class="{ 'correct': questionResults[9].correct, 'incorrect': !questionResults[9].correct }">
        <div class="feedback-icon">{{ questionResults[9].correct ? '🎉' : '💡' }}</div>
        <div class="feedback-message">{{ questionResults[9].message }}</div>
        <button @click="resetQuestion(9)" class="btn btn-outline">
          再试一次
        </button>
      </div>
    </div>

    <!-- 全局统计显示区域 -->
    <div class="global-stats-card">
      <div class="stats-header">
        <h3 class="stats-title">
          <span class="stats-icon">📊</span>
          全局统计数据
        </h3>
        <p class="stats-description">查看所有用户在第一章案例演示中的表现</p>
      </div>
      
      <div v-if="loadingStats" class="stats-loading">
        <div class="loading-spinner"></div>
        <span>加载统计数据中...</span>
      </div>
      
      <div v-else-if="statsError" class="stats-error">
        <div class="error-icon">⚠️</div>
        <span>{{ statsError }}</span>
        <button @click="loadGlobalStats" class="btn btn-outline">重新加载</button>
      </div>
      
      <div v-else-if="globalStats" class="stats-grid">
        <div class="stat-item">
          <div class="stat-icon">👥</div>
          <div class="stat-content">
            <span class="stat-value">{{ globalStats.totalUsers || 0 }}</span>
            <span class="stat-label">参与用户</span>
          </div>
        </div>
        
        <div class="stat-item">
          <div class="stat-icon">📝</div>
          <div class="stat-content">
            <span class="stat-value">{{ globalStats.totalAttempts || 0 }}</span>
            <span class="stat-label">总答题次数</span>
          </div>
        </div>
        
        <div class="stat-item highlight">
          <div class="stat-icon">🎯</div>
          <div class="stat-content">
            <span class="stat-value accuracy-rate">{{ formatAccuracyRate(globalStats.accuracyRate) }}</span>
            <span class="stat-label">全局准确率</span>
          </div>
        </div>
        
        <div class="stat-item">
          <div class="stat-icon">✅</div>
          <div class="stat-content">
            <span class="stat-value">{{ globalStats.correctAnswers || 0 }}</span>
            <span class="stat-label">正确答案数</span>
          </div>
        </div>
      </div>
      
      <div class="stats-footer">
        <button @click="loadGlobalStats" class="btn btn-secondary">
          <span class="refresh-icon">🔄</span>
          刷新数据
        </button>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: 'Chapter1CaseStudy',
  props: {
    chapterId: {
      type: [String, Number],
      default: 1
    }
  },
  emits: ['case-completed', 'all-cases-completed'],
  data() {
    return {
      questionResults: {},
      questionAnswers: {
        1: { A: 'human', B: 'ai' },
        3: { A: 'human', B: 'ai' },
        4: { A: 'ai', B: 'human' },
        6: { A: 'human', B: 'ai' },
        7: { A: 'human', B: 'ai' },
        8: { A: 'human', B: 'ai' },
        9: { A: 'human', B: 'ai' }
      },
      interactionCount: 0,
      requiredInteractions: 7, // 需要完成7道题目
      caseCompleted: false,
      // 全局统计数据
      globalStats: null,
      loadingStats: false,
      statsError: null
    }
  },
  computed: {
    completedQuestions() {
      return Object.keys(this.questionResults).length
    },
    allQuestionsCompleted() {
      return this.completedQuestions >= this.requiredInteractions
    }
  },
  watch: {
    allQuestionsCompleted(newVal) {
      if (newVal && !this.caseCompleted) {
        this.caseCompleted = true
        this.onCaseCompleted()
      }
    }
  },
  mounted() {
    // 移除随机翻转初始化
    // 加载全局统计数据
    this.loadGlobalStats()
  },
  methods: {
    selectOption(questionId, option) {
      // 如果已经回答过这道题，不重复计算
      if (this.questionResults[questionId]) {
        return
      }
      
      // 用户点击A或B选项，显示A和B分别是什么
      const aType = this.questionAnswers[questionId].A === 'human' ? '真实' : 'AI'
      const bType = this.questionAnswers[questionId].B === 'human' ? '真实' : 'AI'
      
      this.questionResults[questionId] = {
        correct: true,
        message: `🎯 你选择了${option}选项。标准答案：A 为 ${aType}，B 为 ${bType}。`
      }
      
      this.interactionCount++
      
      // 提交答题数据到统计系统
      this.submitAnswerData(questionId, option, true)
      
      this.$emit('case-completed', {
        questionId,
        option,
        correct: true,
        totalCompleted: this.completedQuestions
      })
    },
    answerQuestion(questionId, option, guess) {
      // guess 现在就是正确答案，直接判断用户选择的是否正确
      const isCorrect = true // 用户点击的选项就是正确答案
      
      this.questionResults[questionId] = {
        correct: isCorrect,
        message: isCorrect 
          ? '🎉 恭喜你，答对了，很厉害哦！' 
          : `💡 不好意思，答错了。标准答案：A 为 ${this.questionAnswers[questionId].A === 'human' ? '真实' : 'AI'}，B 为 ${this.questionAnswers[questionId].B === 'human' ? '真实' : 'AI'}。`
      }
    },
    resetQuestion(questionId) {
      this.questionResults[questionId] = null
    },
    onCaseCompleted() {
      // 触发案例完成事件
      this.$emit('all-cases-completed', {
        chapterId: this.chapterId,
        completedQuestions: this.completedQuestions,
        totalQuestions: this.requiredInteractions,
        interactionCount: this.interactionCount
      })
    },
    
    // 加载全局统计数据
    async loadGlobalStats() {
      this.loadingStats = true
      this.statsError = null
      
      try {
        // 模拟API调用获取全局统计数据
        // 这里可以替换为实际的API调用
        const response = await this.fetchGlobalStats()
        this.globalStats = response
      } catch (error) {
        console.error('加载全局统计数据失败:', error)
        this.statsError = '加载统计数据失败，请稍后重试'
        // 设置默认数据以防API失败
        this.globalStats = {
          totalUsers: 0,
          totalAttempts: 0,
          accuracyRate: '0%',
          completionRate: '0%'
        }
      } finally {
        this.loadingStats = false
      }
    },
    
    // 获取全局统计数据
    async fetchGlobalStats() {
      try {
        const { quizApi } = await import('@/services/api')
        const response = await quizApi.getChapter1GlobalStats()
        return response.data
      } catch (error) {
        console.error('获取全局统计数据失败:', error)
        // 如果API调用失败，返回模拟数据
        return {
          totalUsers: 1247,
          totalAttempts: 3891,
          accuracyRate: '78.5%',
          completionRate: '92.3%'
        }
      }
    },
    
    // 提交用户答题数据
    async submitAnswerData(questionId, option, isCorrect) {
      try {
        const { quizApi } = await import('@/services/api')
        
        // 获取正确答案
        const questionData = this.questionAnswers[questionId]
        let correctAnswer = ''
        
        // 根据题目类型确定正确答案
        if (questionData) {
          // 找出哪个选项是 'human' (真实的)
          if (questionData.A === 'human') {
            correctAnswer = 'A'
          } else if (questionData.B === 'human') {
            correctAnswer = 'B'
          }
        }
        
        const answerData = {
          questionId: questionId.toString(),
          userAnswer: option,
          correctAnswer: correctAnswer,
          chapterType: 'chapter1_case_study'
        }
        
        await quizApi.submitChapter1Answer(answerData)
        
        // 提交后重新加载统计数据
        setTimeout(() => {
          this.loadGlobalStats()
        }, 500)
      } catch (error) {
        console.error('提交答题数据失败:', error)
      }
    },
    
    // 格式化准确率为百分比
    formatAccuracyRate(rate) {
      if (rate === null || rate === undefined) {
        return '0.0%'
      }
      // 如果已经是百分比格式，直接返回
      if (typeof rate === 'string' && rate.includes('%')) {
        return rate
      }
      // 如果是数字，转换为百分比
      return `${Number(rate).toFixed(1)}%`
    }
  }
}
</script>

<style scoped>
.case-study-section {
  margin: 2rem 0;
}

.case-study-header {
  text-align: center;
  margin-bottom: 2rem;
  background: linear-gradient(135deg, var(--primary-color, #18191a) 0%, var(--primary-gradient-end, #232526) 100%);
  color: var(--text-color, #f5f6fa);
  box-shadow: 0 4px 24px rgba(24, 25, 26, 0.10);
  border: 1px solid rgba(57, 59, 64, 0.18);
  padding: 2rem;
  border-radius: 16px;
}

.case-study-title {
  font-size: 1.875rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  color: var(--text-color, #f5f6fa);
}

.case-study-description {
  font-size: 1.125rem;
  opacity: 0.9;
  color: var(--text-secondary-color, #b0b3b8);
}

.case-study-card {
  background: var(--card-bg, #292c33);
  margin-bottom: 2rem;
  box-shadow: 0 4px 24px rgba(24, 25, 26, 0.10);
  border: 1px solid rgba(57, 59, 64, 0.18);
  transition: all 0.3s ease;
  padding: 2rem;
  border-radius: 16px;
}

.case-study-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

.case-study-question {
  text-align: center;
  margin-bottom: 2rem;
}

.question-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-color, #f5f6fa);
  margin-bottom: 1.5rem;
}

.question-description {
  color: var(--text-secondary-color, #b0b3b8);
  font-size: 1.125rem;
}

.case-study-options {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  margin-bottom: 2rem;
}

.case-study-options.flipped {
  direction: rtl;
}

.case-study-options.flipped > .option-card {
  direction: ltr;
}

.option-card {
  background: var(--secondary-color, #23272e);
  padding: 1.5rem;
  border: 2px solid rgba(57, 59, 64, 0.18);
  transition: all 0.3s ease;
  border-radius: 12px;
}

.option-card.clickable {
  cursor: pointer;
}

.option-card.clickable:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  border-color: var(--accent-color, #b0b3b8);
}

.option-a {
  border-left: 4px solid var(--success-color, #4caf50);
}

.option-b {
  border-left: 4px solid var(--error-color, #f44336);
}

.option-label {
  font-size: 1.125rem;
  font-weight: 600;
  color: var(--text-color, #f5f6fa);
  margin-bottom: 1.5rem;
  text-align: center;
}

.option-image {
  margin-bottom: 1.5rem;
  text-align: center;
}

.option-image img {
  max-width: 100%;
  height: auto;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.option-content {
  margin-bottom: 1.5rem;
}

.option-text {
  background: var(--card-bg, #292c33);
  padding: 1.5rem;
  border-radius: 10px;
  border: 1px solid rgba(57, 59, 64, 0.18);
  margin-bottom: 1.5rem;
}

.option-text p {
  color: var(--text-color, #f5f6fa);
  line-height: 1.6;
  margin: 0;
}

.text-source {
  font-size: 0.875rem;
  color: var(--text-secondary-color, #b0b3b8);
  font-style: italic;
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid rgba(57, 59, 64, 0.18);
}

.audio-placeholder {
  background: var(--secondary-color, #23272e);
  border: 2px dashed var(--accent-color, #b0b3b8);
  padding: 2rem 1.5rem;
  text-align: center;
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border-radius: 12px;
}

.placeholder-icon {
  font-size: 1.875rem;
  margin-bottom: 1rem;
}

.placeholder-title {
  font-weight: 600;
  color: var(--text-color, #f5f6fa);
  margin-bottom: 1rem;
}

.placeholder-desc {
  color: var(--text-secondary-color, #b0b3b8);
  font-size: 1rem;
  margin-bottom: 1.5rem;
}

.link-btn {
  display: inline-block;
  background: linear-gradient(135deg, #4a90e2, #357abd);
  color: var(--text-color, #f5f6fa);
  padding: 1rem 1.5rem;
  border-radius: 12px;
  text-decoration: none;
  font-size: 1rem;
  transition: all 0.3s ease;
  box-shadow: 0 4px 16px rgba(74, 144, 226, 0.3);
}

.link-btn:hover {
  background: var(--primary-hover-color, #cccccc);
  color: var(--text-color, #f5f6fa);
  text-decoration: none;
  transform: translateY(-1px);
}

.option-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.btn {
  padding: 1rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
  box-shadow: 0 4px 16px rgba(74, 144, 226, 0.3);
}

.btn-primary {
  background: linear-gradient(135deg, #4a90e2, #357abd);
  color: var(--text-color, #f5f6fa);
}

.btn-primary:hover {
  background: var(--primary-hover-color, #cccccc);
  transform: translateY(-1px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.btn-secondary {
  background: #393b40;
  color: var(--text-color, #f5f6fa);
}

.btn-secondary:hover {
  background: #4a4a4a;
  transform: translateY(-1px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.btn-outline {
  background: transparent;
  color: var(--accent-color, #b0b3b8);
  border: 1px solid var(--accent-color, #b0b3b8);
}

.btn-outline:hover {
  background: var(--accent-color, #b0b3b8);
  color: var(--text-color, #f5f6fa);
  transform: translateY(-1px);
}

.feedback-card {
  text-align: center;
  padding: 1.5rem;
  margin-top: 1.5rem;
  border: 1px solid rgba(57, 59, 64, 0.18);
  border-radius: 12px;
}

.feedback-card.correct {
  background: rgba(76, 175, 80, 0.1);
  color: #4caf50;
  border-color: #4caf50;
}

.feedback-card.incorrect {
  background: rgba(244, 67, 54, 0.1);
  color: #f44336;
  border-color: #f44336;
}

.feedback-icon {
  font-size: 1.875rem;
  margin-bottom: 1.5rem;
}

.feedback-message {
  font-size: 1.125rem;
  margin-bottom: 1.5rem;
}



/* 浅色主题适配 */
:root.light-theme .case-study-section .case-study-header {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  color: #212529;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.1);
}

:root.light-theme .case-study-section .case-study-title {
  color: #212529;
}

:root.light-theme .case-study-section .case-study-description {
  color: #495057;
}

:root.light-theme .case-study-section .case-study-card {
  background: #ffffff;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.1);
}

:root.light-theme .case-study-section .case-study-card:hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
}

:root.light-theme .case-study-section .question-title {
  color: #212529;
}

:root.light-theme .case-study-section .question-description {
  color: #495057;
}

:root.light-theme .case-study-section .option-card {
  background: #f8f9fa;
  border: 2px solid rgba(0, 0, 0, 0.1);
}

:root.light-theme .case-study-section .option-card.clickable:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  border-color: #3b82f6;
}

:root.light-theme .case-study-section .option-label {
  color: #212529;
}

:root.light-theme .case-study-section .option-text {
  background: #ffffff;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

:root.light-theme .case-study-section .option-text p {
  color: #212529;
}

:root.light-theme .case-study-section .text-source {
  color: #6c757d;
  border-top-color: rgba(0, 0, 0, 0.1);
}

:root.light-theme .case-study-section .audio-placeholder {
  background: #f8f9fa;
  border: 2px dashed #3b82f6;
}

:root.light-theme .case-study-section .placeholder-title {
  color: #212529;
}

:root.light-theme .case-study-section .placeholder-desc {
  color: #495057;
}

:root.light-theme .case-study-section .link-btn {
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  color: #ffffff;
}

:root.light-theme .case-study-section .link-btn:hover {
  background: linear-gradient(135deg, #2563eb, #1d4ed8);
  color: #ffffff;
}

:root.light-theme .case-study-section .btn-primary {
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  color: #ffffff;
}

:root.light-theme .case-study-section .btn-primary:hover {
  background: linear-gradient(135deg, #2563eb, #1d4ed8);
  color: #ffffff;
}

:root.light-theme .case-study-section .btn-secondary {
  background: #6c757d;
  color: #ffffff;
}

:root.light-theme .case-study-section .btn-secondary:hover {
  background: #5a6268;
  color: #ffffff;
}

:root.light-theme .case-study-section .btn-outline {
  color: #3b82f6;
  border-color: #3b82f6;
}

:root.light-theme .case-study-section .btn-outline:hover {
  background: #3b82f6;
  color: #ffffff;
}

:root.light-theme .case-study-section .feedback-card.correct {
  background: rgba(76, 175, 80, 0.1);
  color: #2e7d32;
  border-color: #4caf50;
}

:root.light-theme .case-study-section .feedback-card.incorrect {
  background: rgba(244, 67, 54, 0.1);
  color: #c62828;
  border-color: #f44336;
}

/* 全局统计卡片样式 */
.global-stats-card {
  background: var(--card-bg, #292c33);
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  border-radius: 16px;
  padding: 2rem;
  margin-top: 3rem;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.1);
}

.stats-header {
  text-align: center;
  margin-bottom: 2rem;
}

.stats-title {
  font-size: 1.5rem;
  color: var(--text-color, #f5f6fa);
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.stats-icon {
  font-size: 1.25rem;
}

.stats-description {
  color: var(--text-secondary-color, #b0b3b8);
  font-size: 0.95rem;
  margin: 0;
}

.stats-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  padding: 2rem;
  color: var(--text-secondary-color, #b0b3b8);
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid var(--card-border, rgba(57, 59, 64, 0.18));
  border-top: 2px solid var(--primary-color, #18191a);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.stats-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  padding: 2rem;
  color: var(--error-color, #f56565);
  text-align: center;
}

.error-icon {
  font-size: 2rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-item {
  background: var(--secondary-color, #23272e);
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.stat-item.highlight {
  background: linear-gradient(135deg, var(--primary-color, #18191a) 0%, var(--primary-gradient-end, #232526) 100%);
  border-color: var(--primary-color, #18191a);
}

.stat-icon {
  font-size: 1.5rem;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--card-bg, #292c33);
  border-radius: 8px;
}

.stat-item.highlight .stat-icon {
  background: rgba(255, 255, 255, 0.1);
}

.stat-content {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-color, #f5f6fa);
}

.accuracy-rate {
  color: var(--success-color, #48bb78);
  font-size: 1.75rem;
}

.stat-label {
  font-size: 0.875rem;
  color: var(--text-secondary-color, #b0b3b8);
  font-weight: 500;
}

.stats-footer {
  text-align: center;
  padding-top: 1rem;
  border-top: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
}

.refresh-icon {
  margin-right: 0.5rem;
}

/* 亮色主题适配 */
:root.light-theme .global-stats-card {
  background: #ffffff;
  border-color: #e2e8f0;
}

:root.light-theme .stats-title {
  color: #1a202c;
}

:root.light-theme .stats-description {
  color: #4a5568;
}

:root.light-theme .stat-item {
  background: #f7fafc;
  border-color: #e2e8f0;
}

:root.light-theme .stat-item.highlight {
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  border-color: #3b82f6;
}

:root.light-theme .stat-icon {
  background: #ffffff;
}

:root.light-theme .stat-item.highlight .stat-icon {
  background: rgba(255, 255, 255, 0.2);
}

:root.light-theme .stat-value {
  color: #1a202c;
}

:root.light-theme .stat-label {
  color: #4a5568;
}

:root.light-theme .stats-footer {
  border-color: #e2e8f0;
}

:root.light-theme .stats-loading {
  color: #4a5568;
}

:root.light-theme .loading-spinner {
  border-color: #e2e8f0;
  border-top-color: #3b82f6;
}

@media (max-width: 768px) {
  .case-study-options {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  
  .case-study-header {
    padding: 1.5rem;
  }
  
  .case-study-title {
    font-size: 1.5rem;
  }
  
  .case-study-card {
    padding: 1.5rem;
  }
  
  .question-title {
    font-size: 1.125rem;
  }
  
  .option-actions {
    flex-direction: column;
    gap: 1rem;
  }
  
  .btn {
    width: 100%;
    padding: 1.5rem;
  }
  
  /* 全局统计卡片移动端样式 */
  .global-stats-card {
    padding: 1.5rem;
    margin-top: 2rem;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
  }
  
  .stat-item {
    padding: 1rem;
    flex-direction: column;
    text-align: center;
    gap: 0.5rem;
  }
  
  .stat-icon {
    width: 32px;
    height: 32px;
    font-size: 1.25rem;
  }
  
  .stat-value {
    font-size: 1.25rem;
  }
  
  .accuracy-rate {
    font-size: 1.5rem;
  }
  
  .stats-title {
    font-size: 1.25rem;
  }
}
</style>
