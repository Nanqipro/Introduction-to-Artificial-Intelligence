<template>
  <div class="section quiz">
    <div class="section-header">
      <h3 class="section-title">
        <span class="title-icon">🧠</span>
        <span class="title-text">知识测验</span>
      </h3>
      <p class="section-subtitle">测试您对AI应用案例的理解程度</p>
    </div>

    <div class="quiz-container">
      <!-- 测验开始界面 -->
      <div v-if="!quizStarted && !showResults" class="quiz-intro">
        <div class="intro-content">
          <div class="intro-icon">📝</div>
          <h4>AI应用案例知识测验</h4>
          <p class="intro-description">
            本测验包含{{ quizQuestions.length }}道题目，涵盖智慧生活、智慧驾驶、智慧娱乐和精准医疗等AI应用领域。
            通过测验可以检验您对这些AI技术应用的理解程度。
          </p>
          <div class="quiz-stats">
            <div class="stat-item">
              <span class="stat-number">{{ quizQuestions.length }}</span>
              <span class="stat-label">题目数量</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">{{ estimatedTime }}</span>
              <span class="stat-label">预计用时</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">{{ passingScore }}%</span>
              <span class="stat-label">及格分数</span>
            </div>
          </div>
          <button class="start-quiz-btn" @click="startQuiz">
            <span class="btn-icon">🚀</span>
            开始测验
          </button>
        </div>
      </div>

      <!-- 测验进行中 -->
      <div v-if="quizStarted && !showResults" class="quiz-active">
        <!-- 进度条 -->
        <div class="quiz-progress">
          <div class="progress-bar">
            <div 
              class="progress-fill" 
              :style="{ width: `${(currentQuestionIndex / quizQuestions.length) * 100}%` }"
            ></div>
          </div>
          <div class="progress-text">
            {{ currentQuestionIndex + 1 }} / {{ quizQuestions.length }}
          </div>
        </div>

        <!-- 当前题目 -->
        <div class="question-card">
          <div class="question-header">
            <span class="question-number">第 {{ currentQuestionIndex + 1 }} 题</span>
            <span class="question-category">{{ currentQuestion.category }}</span>
          </div>
          <h4 class="question-text">{{ currentQuestion.question }}</h4>
          
          <div class="options-list">
            <div 
              v-for="(option, index) in currentQuestion.options" 
              :key="index"
              class="option-item"
              :class="{ 
                'selected': selectedAnswer === index,
                'correct': showAnswer && index === currentQuestion.correct,
                'incorrect': showAnswer && selectedAnswer === index && index !== currentQuestion.correct
              }"
              @click="selectAnswer(index)"
            >
              <div class="option-marker">{{ String.fromCharCode(65 + index) }}</div>
              <div class="option-text">{{ option }}</div>
              <div v-if="showAnswer" class="option-indicator">
                <span v-if="index === currentQuestion.correct" class="correct-icon">✓</span>
                <span v-else-if="selectedAnswer === index" class="incorrect-icon">✗</span>
              </div>
            </div>
          </div>

          <!-- 题目解释 -->
          <div v-if="showAnswer" class="question-explanation">
            <h5>解释说明</h5>
            <p>{{ currentQuestion.explanation }}</p>
          </div>

          <!-- 操作按钮 -->
          <div class="question-actions">
            <button 
              v-if="!showAnswer" 
              class="submit-answer-btn" 
              :disabled="selectedAnswer === null"
              @click="submitAnswer"
            >
              提交答案
            </button>
            <button 
              v-if="showAnswer && currentQuestionIndex < quizQuestions.length - 1" 
              class="next-question-btn"
              @click="nextQuestion"
            >
              下一题
            </button>
            <button 
              v-if="showAnswer && currentQuestionIndex === quizQuestions.length - 1" 
              class="finish-quiz-btn"
              @click="finishQuiz"
            >
              完成测验
            </button>
          </div>
        </div>
      </div>

      <!-- 测验结果 -->
      <div v-if="showResults" class="quiz-results">
        <div class="results-content">
          <div class="results-header">
            <div class="results-icon" :class="scoreClass">
              {{ scoreIcon }}
            </div>
            <h4>测验完成！</h4>
            <div class="final-score">
              <span class="score-number">{{ finalScore }}</span>
              <span class="score-total">/ {{ quizQuestions.length }}</span>
            </div>
            <div class="score-percentage" :class="scoreClass">
              {{ Math.round((finalScore / quizQuestions.length) * 100) }}%
            </div>
          </div>

          <div class="results-analysis">
            <div class="analysis-item">
              <span class="analysis-label">正确答题:</span>
              <span class="analysis-value correct">{{ finalScore }} 题</span>
            </div>
            <div class="analysis-item">
              <span class="analysis-label">错误答题:</span>
              <span class="analysis-value incorrect">{{ quizQuestions.length - finalScore }} 题</span>
            </div>
            <div class="analysis-item">
              <span class="analysis-label">正确率:</span>
              <span class="analysis-value" :class="scoreClass">
                {{ Math.round((finalScore / quizQuestions.length) * 100) }}%
              </span>
            </div>
            <div class="analysis-item">
              <span class="analysis-label">评价等级:</span>
              <span class="analysis-value" :class="scoreClass">{{ getScoreLevel() }}</span>
            </div>
          </div>

          <div class="results-feedback">
            <h5>学习建议</h5>
            <p>{{ getFeedbackMessage() }}</p>
          </div>

          <div class="results-actions">
            <button class="review-answers-btn" @click="reviewAnswers">
              <span class="btn-icon">📋</span>
              查看答案解析
            </button>
            <button class="retry-quiz-btn" @click="retryQuiz">
              <span class="btn-icon">🔄</span>
              重新测验
            </button>
          </div>
        </div>
      </div>

      <!-- 答案回顾 -->
      <div v-if="showReview" class="quiz-review">
        <div class="review-header">
          <h4>答案解析</h4>
          <button class="close-review-btn" @click="closeReview">×</button>
        </div>
        
        <div class="review-content">
          <div 
            v-for="(question, index) in quizQuestions" 
            :key="index"
            class="review-item"
          >
            <div class="review-question">
              <div class="review-question-header">
                <span class="review-question-number">第 {{ index + 1 }} 题</span>
                <span class="review-question-category">{{ question.category }}</span>
                <span 
                  class="review-result"
                  :class="userAnswers[index] === question.correct ? 'correct' : 'incorrect'"
                >
                  {{ userAnswers[index] === question.correct ? '✓ 正确' : '✗ 错误' }}
                </span>
              </div>
              <p class="review-question-text">{{ question.question }}</p>
            </div>
            
            <div class="review-options">
              <div 
                v-for="(option, optionIndex) in question.options" 
                :key="optionIndex"
                class="review-option"
                :class="{
                  'user-answer': userAnswers[index] === optionIndex,
                  'correct-answer': optionIndex === question.correct,
                  'wrong-user-answer': userAnswers[index] === optionIndex && optionIndex !== question.correct
                }"
              >
                <span class="review-option-marker">{{ String.fromCharCode(65 + optionIndex) }}</span>
                <span class="review-option-text">{{ option }}</span>
                <span v-if="optionIndex === question.correct" class="review-option-indicator correct">✓</span>
                <span v-else-if="userAnswers[index] === optionIndex" class="review-option-indicator incorrect">✗</span>
              </div>
            </div>
            
            <div class="review-explanation">
              <h6>解释说明</h6>
              <p>{{ question.explanation }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, defineEmits } from 'vue'

// Props
const props = defineProps({
  chapterId: {
    type: String,
    default: '4'
  }
})

// Emits
const emit = defineEmits(['quiz-completed', 'interaction'])

// 响应式数据
const quizStarted = ref(false)
const showResults = ref(false)
const showReview = ref(false)
const showAnswer = ref(false)
const currentQuestionIndex = ref(0)
const selectedAnswer = ref(null)
const userAnswers = ref([])
const finalScore = ref(0)
const estimatedTime = ref('10-15分钟')
const passingScore = ref(70)

// 测验题目数据
const quizQuestions = ref([
  {
    id: 1,
    category: '智慧生活',
    question: '以下哪个设备最能体现AI在智慧生活中的应用？',
    options: [
      '传统的机械开关',
      '具有语音识别和学习能力的智能音箱',
      '普通的LED灯泡',
      '手动调节的空调'
    ],
    correct: 1,
    explanation: '智能音箱集成了语音识别、自然语言处理、机器学习等多种AI技术，能够理解用户指令并不断学习用户习惯，是智慧生活的典型代表。'
  },
  {
    id: 2,
    category: '智慧驾驶',
    question: '自动驾驶技术中，哪种传感器主要用于精确测距和3D环境感知？',
    options: [
      '摄像头',
      '雷达',
      '激光雷达(LiDAR)',
      'GPS定位系统'
    ],
    correct: 2,
    explanation: '激光雷达(LiDAR)通过发射激光束并测量反射时间来精确计算距离，能够生成高精度的3D点云图，是自动驾驶车辆环境感知的核心传感器。'
  },
  {
    id: 3,
    category: '智慧娱乐',
    question: '流媒体平台如Netflix使用AI技术主要是为了？',
    options: [
      '提高视频画质',
      '个性化内容推荐',
      '减少网络延迟',
      '增加存储容量'
    ],
    correct: 1,
    explanation: 'Netflix等流媒体平台主要使用协同过滤、深度学习等AI算法分析用户观看历史和偏好，为每个用户提供个性化的内容推荐，提升用户体验和平台粘性。'
  },
  {
    id: 4,
    category: '精准医疗',
    question: '在精准医疗中，AI技术最主要的应用是？',
    options: [
      '替代医生进行手术',
      '分析基因数据和医学影像辅助诊断',
      '生产药物',
      '管理医院财务'
    ],
    correct: 1,
    explanation: 'AI在精准医疗中的核心应用是通过深度学习算法分析大量的基因组数据、医学影像和临床数据，辅助医生进行更准确的诊断和个性化治疗方案制定。'
  },
  {
    id: 5,
    category: '智慧生活',
    question: '智能家居系统中的"学习能力"主要指什么？',
    options: [
      '系统能够自动更新软件',
      '系统能够学习用户习惯并自动调整设备设置',
      '系统能够连接更多设备',
      '系统能够节省电力'
    ],
    correct: 1,
    explanation: '智能家居的学习能力是指通过机器学习算法分析用户的行为模式、作息时间、环境偏好等数据，自动调整温度、照明、安防等设备设置，实现真正的智能化体验。'
  },
  {
    id: 6,
    category: '智慧驾驶',
    question: '自动驾驶的L5级别代表什么？',
    options: [
      '需要驾驶员时刻监控',
      '在特定条件下完全自动驾驶',
      '任何条件下都能完全自动驾驶',
      '只能在高速公路上自动驾驶'
    ],
    correct: 2,
    explanation: 'L5级自动驾驶是最高级别的自动驾驶，代表在任何道路条件、天气条件下都能完全自动驾驶，无需人类驾驶员干预，车辆甚至可以没有方向盘和踏板。'
  },
  {
    id: 7,
    category: '智慧娱乐',
    question: 'AI在游戏中的应用不包括以下哪项？',
    options: [
      '智能NPC行为',
      '程序化内容生成',
      '玩家行为分析',
      '游戏硬件制造'
    ],
    correct: 3,
    explanation: 'AI在游戏中主要应用于软件层面，包括创建智能NPC、生成游戏内容、分析玩家行为等。游戏硬件制造属于硬件工程领域，不是AI技术的直接应用范畴。'
  },
  {
    id: 8,
    category: '精准医疗',
    question: '基因检测中AI技术的主要作用是？',
    options: [
      '提取DNA样本',
      '分析基因变异并预测疾病风险',
      '合成新的基因',
      '储存基因数据'
    ],
    correct: 1,
    explanation: 'AI在基因检测中主要用于分析复杂的基因组数据，识别基因变异模式，预测疾病风险，并为个性化医疗提供科学依据。机器学习算法能够处理海量基因数据并发现人类难以识别的模式。'
  }
])

// 计算属性
const currentQuestion = computed(() => {
  return quizQuestions.value[currentQuestionIndex.value]
})

const scoreClass = computed(() => {
  const percentage = (finalScore.value / quizQuestions.value.length) * 100
  if (percentage >= 90) return 'excellent'
  if (percentage >= 80) return 'good'
  if (percentage >= 70) return 'pass'
  return 'fail'
})

const scoreIcon = computed(() => {
  const percentage = (finalScore.value / quizQuestions.value.length) * 100
  if (percentage >= 90) return '🏆'
  if (percentage >= 80) return '🎉'
  if (percentage >= 70) return '👍'
  return '📚'
})

// 方法
const startQuiz = () => {
  quizStarted.value = true
  currentQuestionIndex.value = 0
  selectedAnswer.value = null
  userAnswers.value = []
  finalScore.value = 0
  showAnswer.value = false
  emit('interaction')
  console.log('开始测验')
}

const selectAnswer = (index) => {
  if (!showAnswer.value) {
    selectedAnswer.value = index
    emit('interaction')
  }
}

const submitAnswer = () => {
  if (selectedAnswer.value !== null) {
    showAnswer.value = true
    userAnswers.value[currentQuestionIndex.value] = selectedAnswer.value
    
    if (selectedAnswer.value === currentQuestion.value.correct) {
      finalScore.value++
    }
    
    emit('interaction')
    console.log(`提交答案: ${selectedAnswer.value}, 正确答案: ${currentQuestion.value.correct}`)
  }
}

const nextQuestion = () => {
  currentQuestionIndex.value++
  selectedAnswer.value = null
  showAnswer.value = false
  emit('interaction')
}

const finishQuiz = () => {
  showResults.value = true
  quizStarted.value = false
  emit('quiz-completed', {
    score: finalScore.value,
    total: quizQuestions.value.length,
    percentage: Math.round((finalScore.value / quizQuestions.value.length) * 100),
    answers: userAnswers.value
  })
  emit('interaction')
  console.log(`测验完成，得分: ${finalScore.value}/${quizQuestions.value.length}`)
}

const retryQuiz = () => {
  quizStarted.value = false
  showResults.value = false
  showReview.value = false
  currentQuestionIndex.value = 0
  selectedAnswer.value = null
  userAnswers.value = []
  finalScore.value = 0
  showAnswer.value = false
  emit('interaction')
  console.log('重新开始测验')
}

const reviewAnswers = () => {
  showReview.value = true
  emit('interaction')
  console.log('查看答案解析')
}

const closeReview = () => {
  showReview.value = false
  emit('interaction')
}

const getScoreLevel = () => {
  const percentage = (finalScore.value / quizQuestions.value.length) * 100
  if (percentage >= 90) return '优秀'
  if (percentage >= 80) return '良好'
  if (percentage >= 70) return '及格'
  return '需要加强'
}

const getFeedbackMessage = () => {
  const percentage = (finalScore.value / quizQuestions.value.length) * 100
  if (percentage >= 90) {
    return '恭喜您！您对AI应用案例有着深入的理解，建议继续关注AI技术的最新发展动态。'
  } else if (percentage >= 80) {
    return '很好！您对AI应用有较好的掌握，建议深入学习某个特定领域的AI应用。'
  } else if (percentage >= 70) {
    return '不错！您已经掌握了AI应用的基础知识，建议多关注实际案例来加深理解。'
  } else {
    return '建议您重新学习相关内容，多了解AI在各个领域的具体应用案例，加强基础知识的掌握。'
  }
}
</script>

<style scoped>
/* 知识测验板块样式 */
.section {
  padding: 2rem;
  margin-bottom: 2rem;
}

.section-header {
  text-align: center;
  margin-bottom: 3rem;
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-color);
  margin-bottom: 1rem;
}

.title-icon {
  font-size: 3rem;
}

.section-subtitle {
  font-size: 1.2rem;
  color: var(--text-secondary-color);
  margin: 0;
}

.quiz-container {
  max-width: 800px;
  margin: 0 auto;
}

/* 测验介绍样式 */
.quiz-intro {
  text-align: center;
}

.intro-content {
  background: var(--card-bg);
  padding: 3rem;
  border-radius: 20px;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--border-color);
}

.intro-icon {
  font-size: 4rem;
  margin-bottom: 1.5rem;
}

.intro-content h4 {
  color: var(--text-color);
  font-size: 1.8rem;
  margin-bottom: 1rem;
}

.intro-description {
  color: var(--text-secondary-color);
  line-height: 1.6;
  margin-bottom: 2rem;
  font-size: 1.1rem;
}

.quiz-stats {
  display: flex;
  justify-content: center;
  gap: 2rem;
  margin-bottom: 2rem;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 2rem;
  font-weight: 700;
  color: var(--accent-color);
  margin-bottom: 0.5rem;
}

.stat-label {
  color: var(--text-secondary-color);
  font-size: 0.9rem;
}

.start-quiz-btn {
  background: linear-gradient(135deg, var(--accent-color), var(--accent-light-color));
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 50px;
  font-size: 1.2rem;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-normal);
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0 auto;
}

.start-quiz-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 191, 255, 0.3);
}

.btn-icon {
  font-size: 1.3rem;
}

/* 测验进行中样式 */
.quiz-active {
  background: var(--card-bg);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--border-color);
}

.quiz-progress {
  padding: 1.5rem;
  background: var(--secondary-color);
  display: flex;
  align-items: center;
  gap: 1rem;
}

.progress-bar {
  flex: 1;
  height: 8px;
  background: var(--border-color);
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--accent-color), var(--accent-light-color));
  transition: width var(--transition-normal);
}

.progress-text {
  color: var(--text-color);
  font-weight: 600;
  min-width: 60px;
}

.question-card {
  padding: 2rem;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.question-number {
  background: var(--accent-color);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-weight: 600;
}

.question-category {
  background: var(--success-color);
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 15px;
  font-size: 0.9rem;
}

.question-text {
  color: var(--text-color);
  font-size: 1.3rem;
  line-height: 1.5;
  margin-bottom: 2rem;
}

.options-list {
  margin-bottom: 2rem;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  margin-bottom: 0.5rem;
  background: var(--secondary-color);
  border-radius: 12px;
  cursor: pointer;
  transition: all var(--transition-fast);
  border: 2px solid transparent;
}

.option-item:hover {
  background: var(--border-color);
  transform: translateX(5px);
}

.option-item.selected {
  background: rgba(0, 191, 255, 0.1);
  border-color: var(--accent-color);
}

.option-item.correct {
  background: rgba(40, 167, 69, 0.1);
  border-color: var(--success-color);
}

.option-item.incorrect {
  background: rgba(220, 53, 69, 0.1);
  border-color: var(--error-color);
}

.option-marker {
  width: 30px;
  height: 30px;
  background: var(--accent-color);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  flex-shrink: 0;
}

.option-item.correct .option-marker {
  background: var(--success-color);
}

.option-item.incorrect .option-marker {
  background: var(--error-color);
}

.option-text {
  flex: 1;
  color: var(--text-color);
  line-height: 1.4;
}

.option-indicator {
  font-size: 1.2rem;
  font-weight: bold;
}

.correct-icon {
  color: var(--success-color);
}

.incorrect-icon {
  color: var(--error-color);
}

.question-explanation {
  background: var(--secondary-color);
  padding: 1.5rem;
  border-radius: 12px;
  margin-bottom: 2rem;
  border-left: 4px solid var(--accent-color);
}

.question-explanation h5 {
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.question-explanation p {
  color: var(--text-secondary-color);
  line-height: 1.6;
  margin: 0;
}

.question-actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.submit-answer-btn,
.next-question-btn,
.finish-quiz-btn {
  padding: 0.8rem 2rem;
  border: none;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-normal);
}

.submit-answer-btn {
  background: var(--accent-color);
  color: white;
}

.submit-answer-btn:disabled {
  background: var(--border-color);
  color: var(--text-secondary-color);
  cursor: not-allowed;
}

.next-question-btn {
  background: var(--success-color);
  color: white;
}

.finish-quiz-btn {
  background: var(--warning-color);
  color: white;
}

.submit-answer-btn:not(:disabled):hover,
.next-question-btn:hover,
.finish-quiz-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

/* 测验结果样式 */
.quiz-results {
  background: var(--card-bg);
  border-radius: 20px;
  padding: 3rem;
  text-align: center;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--border-color);
}

.results-header {
  margin-bottom: 2rem;
}

.results-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.results-icon.excellent {
  color: #FFD700;
}

.results-icon.good {
  color: var(--success-color);
}

.results-icon.pass {
  color: var(--warning-color);
}

.results-icon.fail {
  color: var(--error-color);
}

.results-header h4 {
  color: var(--text-color);
  font-size: 1.8rem;
  margin-bottom: 1rem;
}

.final-score {
  font-size: 3rem;
  font-weight: 700;
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.score-number {
  color: var(--accent-color);
}

.score-total {
  color: var(--text-secondary-color);
}

.score-percentage {
  font-size: 1.5rem;
  font-weight: 600;
  padding: 0.5rem 1rem;
  border-radius: 25px;
  display: inline-block;
}

.score-percentage.excellent {
  background: rgba(255, 215, 0, 0.2);
  color: #B8860B;
}

.score-percentage.good {
  background: rgba(40, 167, 69, 0.2);
  color: var(--success-color);
}

.score-percentage.pass {
  background: rgba(255, 193, 7, 0.2);
  color: var(--warning-color);
}

.score-percentage.fail {
  background: rgba(220, 53, 69, 0.2);
  color: var(--error-color);
}

.results-analysis {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: var(--secondary-color);
  border-radius: 12px;
}

.analysis-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.analysis-label {
  color: var(--text-secondary-color);
  font-weight: 500;
}

.analysis-value {
  font-weight: 600;
}

.analysis-value.correct {
  color: var(--success-color);
}

.analysis-value.incorrect {
  color: var(--error-color);
}

.analysis-value.excellent {
  color: #B8860B;
}

.analysis-value.good {
  color: var(--success-color);
}

.analysis-value.pass {
  color: var(--warning-color);
}

.analysis-value.fail {
  color: var(--error-color);
}

.results-feedback {
  margin-bottom: 2rem;
  text-align: left;
  background: var(--secondary-color);
  padding: 1.5rem;
  border-radius: 12px;
}

.results-feedback h5 {
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.results-feedback p {
  color: var(--text-secondary-color);
  line-height: 1.6;
  margin: 0;
}

.results-actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.review-answers-btn,
.retry-quiz-btn {
  padding: 1rem 2rem;
  border: none;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-normal);
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.review-answers-btn {
  background: var(--accent-color);
  color: white;
}

.retry-quiz-btn {
  background: var(--success-color);
  color: white;
}

.review-answers-btn:hover,
.retry-quiz-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

/* 答案回顾样式 */
.quiz-review {
  background: var(--card-bg);
  border-radius: 20px;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--border-color);
  max-height: 80vh;
  overflow-y: auto;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
  position: sticky;
  top: 0;
  background: var(--card-bg);
  z-index: 10;
}

.review-header h4 {
  color: var(--text-color);
  margin: 0;
}

.close-review-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: var(--text-secondary-color);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 50%;
  transition: all var(--transition-fast);
}

.close-review-btn:hover {
  background: var(--border-color);
  color: var(--text-color);
}

.review-content {
  padding: 1.5rem;
}

.review-item {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: var(--secondary-color);
  border-radius: 12px;
}

.review-item:last-child {
  margin-bottom: 0;
}

.review-question-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.review-question-number {
  background: var(--accent-color);
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 15px;
  font-size: 0.9rem;
  font-weight: 600;
}

.review-question-category {
  background: var(--success-color);
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 15px;
  font-size: 0.9rem;
}

.review-result {
  padding: 0.3rem 0.8rem;
  border-radius: 15px;
  font-size: 0.9rem;
  font-weight: 600;
}

.review-result.correct {
  background: rgba(40, 167, 69, 0.2);
  color: var(--success-color);
}

.review-result.incorrect {
  background: rgba(220, 53, 69, 0.2);
  color: var(--error-color);
}

.review-question-text {
  color: var(--text-color);
  font-weight: 600;
  margin-bottom: 1rem;
}

.review-options {
  margin-bottom: 1rem;
}

.review-option {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.8rem;
  margin-bottom: 0.5rem;
  background: var(--card-bg);
  border-radius: 8px;
  border: 2px solid transparent;
}

.review-option.correct-answer {
  background: rgba(40, 167, 69, 0.1);
  border-color: var(--success-color);
}

.review-option.wrong-user-answer {
  background: rgba(220, 53, 69, 0.1);
  border-color: var(--error-color);
}

.review-option-marker {
  width: 25px;
  height: 25px;
  background: var(--border-color);
  color: var(--text-color);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.9rem;
  flex-shrink: 0;
}

.review-option.correct-answer .review-option-marker {
  background: var(--success-color);
  color: white;
}

.review-option.wrong-user-answer .review-option-marker {
  background: var(--error-color);
  color: white;
}

.review-option-text {
  flex: 1;
  color: var(--text-color);
}

.review-option-indicator {
  font-size: 1.1rem;
  font-weight: bold;
}

.review-option-indicator.correct {
  color: var(--success-color);
}

.review-option-indicator.incorrect {
  color: var(--error-color);
}

.review-explanation {
  background: var(--card-bg);
  padding: 1rem;
  border-radius: 8px;
  border-left: 4px solid var(--accent-color);
}

.review-explanation h6 {
  color: var(--text-color);
  margin-bottom: 0.5rem;
  font-size: 1rem;
}

.review-explanation p {
  color: var(--text-secondary-color);
  line-height: 1.5;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .section {
    padding: 1rem;
  }
  
  .section-title {
    font-size: 2rem;
  }
  
  .title-icon {
    font-size: 2.5rem;
  }
  
  .intro-content {
    padding: 2rem;
  }
  
  .quiz-stats {
    flex-direction: column;
    gap: 1rem;
  }
  
  .question-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .question-text {
    font-size: 1.1rem;
  }
  
  .results-analysis {
    grid-template-columns: 1fr;
  }
  
  .results-actions {
    flex-direction: column;
  }
  
  .review-question-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}

/* 浅色主题样式 */
.light-theme .section-title {
  color: var(--text-color);
}

.light-theme .section-subtitle {
  color: var(--text-secondary-color);
}

.light-theme .intro-content {
  background: var(--card-bg);
  border-color: var(--border-color);
}

.light-theme .intro-content h4 {
  color: var(--text-color);
}

.light-theme .intro-description {
  color: var(--text-secondary-color);
}

.light-theme .stat-label {
  color: var(--text-secondary-color);
}

.light-theme .quiz-active {
  background: var(--card-bg);
  border-color: var(--border-color);
}

.light-theme .quiz-progress {
  background: var(--secondary-color);
}

.light-theme .progress-text {
  color: var(--text-color);
}

.light-theme .question-text {
  color: var(--text-color);
}

.light-theme .option-item {
  background: var(--secondary-color);
}

.light-theme .option-item:hover {
  background: var(--border-color);
}

.light-theme .option-text {
  color: var(--text-color);
}

.light-theme .question-explanation {
  background: var(--secondary-color);
}

.light-theme .question-explanation h5 {
  color: var(--text-color);
}

.light-theme .question-explanation p {
  color: var(--text-secondary-color);
}

.light-theme .quiz-results {
  background: var(--card-bg);
  border-color: var(--border-color);
}

.light-theme .results-header h4 {
  color: var(--text-color);
}

.light-theme .final-score {
  color: var(--text-color);
}

.light-theme .score-total {
  color: var(--text-secondary-color);
}

.light-theme .results-analysis {
  background: var(--secondary-color);
}

.light-theme .analysis-label {
  color: var(--text-secondary-color);
}

.light-theme .results-feedback {
  background: var(--secondary-color);
}

.light-theme .results-feedback h5 {
  color: var(--text-color);
}

.light-theme .results-feedback p {
  color: var(--text-secondary-color);
}

.light-theme .quiz-review {
  background: var(--card-bg);
  border-color: var(--border-color);
}

.light-theme .review-header {
  background: var(--card-bg);
  border-bottom-color: var(--border-color);
}

.light-theme .review-header h4 {
  color: var(--text-color);
}

.light-theme .close-review-btn {
  color: var(--text-secondary-color);
}

.light-theme .close-review-btn:hover {
  background: var(--border-color);
  color: var(--text-color);
}

.light-theme .review-item {
  background: var(--secondary-color);
}

.light-theme .review-question-text {
  color: var(--text-color);
}

.light-theme .review-option {
  background: var(--card-bg);
}

.light-theme .review-option-text {
  color: var(--text-color);
}

.light-theme .review-explanation {
  background: var(--card-bg);
}

.light-theme .review-explanation h6 {
  color: var(--text-color);
}

.light-theme .review-explanation p {
  color: var(--text-secondary-color);
}
</style>