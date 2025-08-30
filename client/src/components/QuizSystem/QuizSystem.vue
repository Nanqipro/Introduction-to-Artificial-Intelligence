<template>
  <div class="quiz-system">
    <!-- 卡通人物指导 -->
    <QuizGuide
      ref="quizGuide"
      :current-question="currentQuestion"
      :question-index="currentQuestionIndex"
      :total-questions="questions.length"
      :user-answer="selectedAnswer || fillAnswer"
      :is-correct="isAnswerCorrect"
      :show-hint="showHint"
      :show-answer="showAnswer"
    />
    <!-- 答题系统头部 -->
    <div class="quiz-header">
      <div class="quiz-info">
        <h2 class="quiz-title">{{ chapterTitle }} - 知识测验</h2>
        <p class="quiz-description">通过答题检验学习成果，获得奖励和成就</p>
      </div>
      
      <!-- 用户进度和奖励信息 -->
      <div class="user-progress">
        <div class="progress-item">
          <div class="progress-icon">🏆</div>
          <div class="progress-info">
            <span class="progress-label">总分</span>
            <span class="progress-value">{{ totalScore }}</span>
          </div>
        </div>
        <div class="progress-item">
          <div class="progress-icon">⭐</div>
          <div class="progress-info">
            <span class="progress-label">等级</span>
            <span class="progress-value">{{ userLevel }}</span>
          </div>
        </div>
        <div class="progress-item">
          <div class="progress-icon">🎯</div>
          <div class="progress-info">
            <span class="progress-label">正确率</span>
            <span class="progress-value">{{ accuracyRate }}%</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 答题设置面板 -->
    <div class="quiz-settings" v-if="currentQuestionIndex === 0 && !showAnswer">
      <div class="settings-panel">
        <h3 class="settings-title">答题设置</h3>
        
        <!-- 难度选择 -->
        <div class="setting-group">
          <label class="setting-label">选择难度</label>
          <div class="difficulty-options">
            <button 
              v-for="diff in ['easy', 'normal', 'hard']" 
              :key="diff"
              @click="setDifficulty(diff)"
              class="difficulty-btn"
              :class="{ active: difficulty === diff }"
            >
              <span class="diff-icon">{{ getDifficultyIcon(diff) }}</span>
              <span class="diff-text">{{ getDifficultyText(diff) }}</span>
              <span class="diff-desc">{{ getDifficultyDesc(diff) }}</span>
            </button>
          </div>
        </div>
        
        <!-- 模式选择 -->
        <div class="setting-group">
          <label class="setting-label">答题模式</label>
          <div class="mode-options">
            <button 
              @click="setQuizMode('normal')"
              class="mode-btn"
              :class="{ active: quizMode === 'normal' }"
            >
              <span class="mode-icon">🎯</span>
              <span class="mode-text">标准模式</span>
              <span class="mode-desc">有时间限制，计分排名</span>
            </button>
            <button 
              @click="setQuizMode('practice')"
              class="mode-btn"
              :class="{ active: quizMode === 'practice' }"
            >
              <span class="mode-icon">📚</span>
              <span class="mode-text">练习模式</span>
              <span class="mode-desc">无时间限制，可重复练习</span>
            </button>
            <button 
              @click="setQuizMode('exam')"
              class="mode-btn"
              :class="{ active: quizMode === 'exam' }"
            >
              <span class="mode-icon">📝</span>
              <span class="mode-text">考试模式</span>
              <span class="mode-desc">严格计时，不可返回</span>
            </button>
          </div>
        </div>
        
        <button @click="startQuiz" class="start-quiz-btn">
          <span class="btn-icon">🚀</span>
          开始答题
        </button>
      </div>
    </div>

    <!-- 答题区域 -->
    <div class="quiz-container" v-if="!quizCompleted && (currentQuestionIndex > 0 || showAnswer)">
      <!-- 进度条和计时器 -->
      <div class="quiz-progress">
        <div class="progress-section">
          <div class="progress-bar">
            <div 
              class="progress-fill" 
              :style="{ width: `${(currentQuestionIndex / questions.length) * 100}%` }"
            ></div>
          </div>
          <span class="progress-text">{{ currentQuestionIndex + 1 }} / {{ questions.length }}</span>
        </div>
        
        <!-- 计时器 -->
        <div class="timer-section" v-if="!practiceMode">
          <div class="timer-display" :class="{ warning: timeRemaining <= 10, critical: timeRemaining <= 5 }">
            <div class="timer-icon">⏱️</div>
            <div class="timer-text">
              <span class="timer-value">{{ formatTime(timeRemaining) }}</span>
              <span class="timer-label">剩余时间</span>
            </div>
          </div>
          <div class="timer-progress">
            <div 
              class="timer-fill" 
              :style="{ width: `${(timeRemaining / timeLimit) * 100}%` }"
              :class="{ warning: timeRemaining <= 10, critical: timeRemaining <= 5 }"
            ></div>
          </div>
        </div>
        
        <!-- 答题统计 -->
        <div class="quiz-stats">
          <div class="stat-item">
            <span class="stat-icon">🔥</span>
            <span class="stat-value">{{ streakCount }}</span>
            <span class="stat-label">连击</span>
          </div>
          <div class="stat-item">
            <span class="stat-icon">💡</span>
            <span class="stat-value">{{ maxHints - hintsUsed }}</span>
            <span class="stat-label">提示</span>
          </div>
        </div>
      </div>

      <!-- 当前题目 -->
      <div class="question-container" v-if="currentQuestion">
        <div class="question-header">
          <div class="question-type-badge" :class="getQuestionTypeClass(currentQuestion.type)">
            {{ getQuestionTypeText(currentQuestion.type) }}
          </div>
          <div class="question-points">
            <span class="points-icon">💎</span>
            <span class="points-value">{{ currentQuestion.points }} 分</span>
          </div>
        </div>

        <div class="question-content">
          <h3 class="question-title">{{ currentQuestion.title }}</h3>
          <div class="question-description" v-if="currentQuestion.description">
            {{ currentQuestion.description }}
          </div>
          
          <!-- 多媒体内容显示 -->
          <div class="question-media" v-if="currentQuestion.imageUrl || currentQuestion.audioUrl || currentQuestion.videoUrl">
            <!-- 图片显示 -->
            <div v-if="currentQuestion.imageUrl" class="media-item">
              <img :src="currentQuestion.imageUrl" :alt="currentQuestion.title" class="question-image" />
            </div>
            
            <!-- 音频显示 -->
            <div v-if="currentQuestion.audioUrl" class="media-item">
              <audio controls class="question-audio">
                <source :src="currentQuestion.audioUrl" type="audio/mpeg">
                <source :src="currentQuestion.audioUrl" type="audio/ogg">
                您的浏览器不支持音频播放
              </audio>
            </div>
            
            <!-- 视频显示 -->
            <div v-if="currentQuestion.videoUrl" class="media-item">
              <video controls class="question-video">
                <source :src="currentQuestion.videoUrl" type="video/mp4">
                <source :src="currentQuestion.videoUrl" type="video/webm">
                您的浏览器不支持视频播放
              </video>
            </div>
          </div>
        </div>

        <!-- 选择题选项 -->
        <div class="options-container" v-if="currentQuestion.type === 'choice'">
          <div 
            v-for="(option, index) in currentQuestion.options" 
            :key="index"
            class="option-item"
            :class="{ 
              selected: selectedAnswer === index,
              correct: showAnswer && index === currentQuestion.correctAnswer,
              incorrect: showAnswer && selectedAnswer === index && selectedAnswer !== currentQuestion.correctAnswer
            }"
            @click="selectAnswer(index)"
          >
            <div class="option-marker">{{ String.fromCharCode(65 + index) }}</div>
            <div class="option-text">{{ option }}</div>
            <div class="option-status" v-if="showAnswer">
              <span v-if="index === currentQuestion.correctAnswer" class="status-correct">✓</span>
              <span v-else-if="selectedAnswer === index" class="status-incorrect">✗</span>
            </div>
          </div>
        </div>

        <!-- 填空题输入 -->
        <div class="fill-blank-container" v-if="currentQuestion.type === 'fill'">
          <div class="input-group">
            <input 
              v-model="fillAnswer"
              type="text" 
              class="fill-input"
              placeholder="请输入答案..."
              :disabled="showAnswer"
            />
          </div>
        </div>

        <!-- 判断题选项 -->
        <div class="true-false-container" v-if="currentQuestion.type === 'true-false'">
          <div 
            class="tf-option"
            :class="{ selected: selectedAnswer === true }"
            @click="selectAnswer(true)"
          >
            <div class="tf-marker">✓</div>
            <div class="tf-text">正确</div>
          </div>
          <div 
            class="tf-option"
            :class="{ selected: selectedAnswer === false }"
            @click="selectAnswer(false)"
          >
            <div class="tf-marker">✗</div>
            <div class="tf-text">错误</div>
          </div>
        </div>

        <!-- 提示区域 -->
        <div class="hint-section" v-if="showHint && !showAnswer">
          <div class="hint-content">
            <div class="hint-icon">💡</div>
            <div class="hint-text">
              <h4>学习提示</h4>
              <p>{{ getHintForQuestion(currentQuestion) }}</p>
            </div>
          </div>
        </div>

        <!-- 答题按钮 -->
        <div class="question-actions">
          <button 
            v-if="!showAnswer"
            @click="showHint = !showHint"
            class="btn btn-hint"
            :class="{ 'active': showHint }"
          >
            {{ showHint ? '隐藏提示' : '获取提示' }}
          </button>
          <button 
            v-if="!showAnswer"
            @click="submitAnswer"
            class="btn btn-primary"
            :disabled="!canSubmit"
          >
            提交答案
          </button>
          <button 
            v-else
            @click="nextQuestion"
            class="btn btn-next"
          >
            {{ isLastQuestion ? '查看结果' : '下一题' }}
          </button>
        </div>

        <!-- 答案反馈 -->
        <div class="answer-feedback" v-if="showAnswer">
          <div class="feedback-content" :class="{ correct: isAnswerCorrect, incorrect: !isAnswerCorrect }">
            <div class="feedback-icon">
              {{ isAnswerCorrect ? '🎉' : '💡' }}
            </div>
            <div class="feedback-text">
              <h4>{{ isAnswerCorrect ? '回答正确！' : '回答错误' }}</h4>
              <p v-if="currentQuestion.explanation">{{ currentQuestion.explanation }}</p>
              <div class="score-info">
                <span class="score-label">获得分数:</span>
                <span class="score-value">{{ earnedPoints }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 答题完成结果 -->
    <div class="quiz-result" v-if="quizCompleted">
      <div class="result-header">
        <div class="result-icon">🎊</div>
        <h2 class="result-title">测验完成！</h2>
        <p class="result-subtitle">恭喜你完成了本次知识测验</p>
      </div>

      <!-- 成绩统计 -->
      <div class="result-stats">
        <div class="stat-item">
          <div class="stat-icon">📊</div>
          <div class="stat-info">
            <span class="stat-value">{{ finalScore }}</span>
            <span class="stat-label">总分</span>
          </div>
        </div>
        <div class="stat-item">
          <div class="stat-icon">🎯</div>
          <div class="stat-info">
            <span class="stat-value">{{ correctCount }}/{{ questions.length }}</span>
            <span class="stat-label">正确题数</span>
          </div>
        </div>
        <div class="stat-item">
          <div class="stat-icon">📈</div>
          <div class="stat-info">
            <span class="stat-value">{{ finalAccuracy }}%</span>
            <span class="stat-label">正确率</span>
          </div>
        </div>
      </div>

      <!-- 奖励展示 -->
      <div class="rewards-section">
        <h3 class="rewards-title">🎁 获得奖励</h3>
        <div class="rewards-list">
          <div class="reward-item" v-for="reward in earnedRewards" :key="reward.type">
            <div class="reward-icon">{{ reward.icon }}</div>
            <div class="reward-info">
              <span class="reward-name">{{ reward.name }}</span>
              <span class="reward-desc">{{ reward.description }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 等级提升 -->
      <div class="level-up" v-if="levelUp">
        <div class="level-up-content">
          <div class="level-up-icon">🚀</div>
          <h3>等级提升！</h3>
          <p>恭喜你从 {{ oldLevel }} 升级到 {{ newLevel }}</p>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="result-actions">
        <button @click="restartQuiz" class="btn btn-secondary">重新开始</button>
        <button @click="goToChapter" class="btn btn-primary">返回章节</button>
        <button @click="shareResult" class="btn btn-share">分享成绩</button>
      </div>
    </div>
  </div>
</template>

<script>
import { quizApi } from '../../services/api'
import QuizGuide from './QuizGuide.vue'

export default {
  components: {
    QuizGuide
  },
  name: 'QuizSystem',
  props: {
    chapterId: {
      type: String,
      required: true
    },
    chapterTitle: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      questions: [],
      currentQuestionIndex: 0,
      selectedAnswer: null,
      fillAnswer: '',
      showAnswer: false,
      quizCompleted: false,
      userAnswers: [],
      totalScore: 0,
      correctCount: 0,
      earnedRewards: [],
      levelUp: false,
      oldLevel: '',
      newLevel: '',
      showHint: false,
      
      // 新增功能数据
      timeLimit: 60, // 每题时间限制（秒）
      timeRemaining: 60,
      timerInterval: null,
      isTimerActive: false,
      
      // 答题历史和统计
      quizHistory: [],
      wrongQuestions: [], // 错题本
      streakCount: 0, // 连续答对题数
      maxStreak: 0, // 最大连续答对数
      
      // 难度设置
      difficulty: 'normal', // easy, normal, hard
      difficultyMultiplier: 1,
      
      // 提示系统
      hintsUsed: 0,
      maxHints: 3,
      hintPenalty: 0.1, // 使用提示的分数惩罚
      
      // 答题模式
      quizMode: 'normal', // normal, practice, exam
      practiceMode: false,
      examMode: false
    }
  },
  computed: {
    currentQuestion() {
      return this.questions[this.currentQuestionIndex] || null
    },
    isLastQuestion() {
      return this.currentQuestionIndex === this.questions.length - 1
    },
    canSubmit() {
      if (this.currentQuestion.type === 'choice' || this.currentQuestion.type === 'true-false') {
        return this.selectedAnswer !== null
      } else if (this.currentQuestion.type === 'fill') {
        return this.fillAnswer.trim() !== ''
      }
      return false
    },
    isAnswerCorrect() {
      if (!this.currentQuestion) return false
      
      if (this.currentQuestion.type === 'choice' || this.currentQuestion.type === 'true-false') {
        return this.selectedAnswer === this.currentQuestion.correctAnswer
      } else if (this.currentQuestion.type === 'fill') {
        return this.fillAnswer.trim().toLowerCase() === this.currentQuestion.correctAnswer.toLowerCase()
      }
      return false
    },
    earnedPoints() {
      if (this.isAnswerCorrect) {
        return this.currentQuestion.points
      }
      return 0
    },
    userLevel() {
      if (this.totalScore < 100) return '初学者'
      if (this.totalScore < 300) return '进阶者'
      if (this.totalScore < 600) return '专家'
      if (this.totalScore < 1000) return '大师'
      return '传奇'
    },
    accuracyRate() {
      if (this.userAnswers.length === 0) return 0
      const correct = this.userAnswers.filter(answer => answer.correct).length
      return Math.round((correct / this.userAnswers.length) * 100)
    },
    finalScore() {
      return this.totalScore
    },
    finalAccuracy() {
      return this.accuracyRate
    }
  },
  async mounted() {
    await this.loadQuestions()
    this.loadWrongQuestions()
  },
  
  beforeDestroy() {
    // 清理计时器
    this.stopTimer()
  },
  methods: {
    async loadQuestions() {
      this.loading = true
      try {
        // 首先尝试从数据库加载题目
        console.log('🔄 尝试从数据库加载题目...')
        const dbResponse = await quizApi.getQuestionsFromDB(this.chapterId)
        
        if (dbResponse && dbResponse.data && dbResponse.data.length > 0) {
          // 转换数据库题目格式为答题系统格式
          this.questions = this.convertDBQuestionsToQuizFormat(dbResponse.data)
          console.log('✅ 从数据库加载题目成功:', this.questions.length, '道题目')
        } else {
          // 如果数据库没有题目，尝试从原有API加载
          console.log('🔄 数据库无题目，尝试从原有API加载...')
          const questions = await quizApi.getQuestionsByChapter(this.chapterId)
          this.questions = questions || []
          console.log('✅ 从原有API加载题目成功:', this.questions.length, '道题目')
        }
        
        // 如果都没有题目，使用默认题目
        if (!this.questions || this.questions.length === 0) {
          console.log('⚠️ 没有找到题目，使用默认题目')
          this.questions = this.getDefaultQuestions()
        }
        
        console.log('📚 最终题目数量:', this.questions.length)
      } catch (error) {
        console.error('❌ 加载题目失败:', error)
        // 如果API失败，使用默认题目
        this.questions = this.getDefaultQuestions()
      } finally {
        this.loading = false
      }
    },
    /**
     * 转换数据库题目格式为答题系统格式
     */
    convertDBQuestionsToQuizFormat(dbQuestions) {
      return dbQuestions.map(dbQuestion => {
        const quizQuestion = {
          id: dbQuestion.id,
          type: this.mapDBTypeToQuizType(dbQuestion.type),
          title: dbQuestion.title,
          description: dbQuestion.description || '',
          points: dbQuestion.score || 10,
          explanation: dbQuestion.explanation || '',
          imageUrl: dbQuestion.imageUrl,
          audioUrl: dbQuestion.audioUrl,
          videoUrl: dbQuestion.videoUrl,
          difficulty: dbQuestion.difficulty || 'medium'
        }
        
        // 根据题目类型设置选项和答案
        if (dbQuestion.type === 'choice') {
          quizQuestion.options = []
          if (dbQuestion.optionA) quizQuestion.options.push(dbQuestion.optionA)
          if (dbQuestion.optionB) quizQuestion.options.push(dbQuestion.optionB)
          if (dbQuestion.optionC) quizQuestion.options.push(dbQuestion.optionC)
          if (dbQuestion.optionD) quizQuestion.options.push(dbQuestion.optionD)
          
          // 转换正确答案格式
          quizQuestion.correctAnswer = this.convertCorrectAnswer(dbQuestion.correctAnswer, quizQuestion.options.length)
        } else if (dbQuestion.type === 'tf') {
          quizQuestion.correctAnswer = dbQuestion.correctAnswer.toLowerCase() === 'true' || 
                                     dbQuestion.correctAnswer.toLowerCase() === 't' ||
                                     dbQuestion.correctAnswer === '1'
        } else if (dbQuestion.type === 'fill') {
          quizQuestion.correctAnswer = dbQuestion.correctAnswer
        }
        
        return quizQuestion
      })
    },
    
    /**
     * 映射数据库题目类型到答题系统类型
     */
    mapDBTypeToQuizType(dbType) {
      const typeMap = {
        'choice': 'choice',
        'tf': 'true-false',
        'fill': 'fill'
      }
      return typeMap[dbType] || 'choice'
    },
    
    /**
     * 转换正确答案格式
     */
    convertCorrectAnswer(correctAnswer, optionsCount) {
      if (typeof correctAnswer === 'string') {
        const upperAnswer = correctAnswer.toUpperCase()
        if (upperAnswer === 'A' || upperAnswer === '1') return 0
        if (upperAnswer === 'B' || upperAnswer === '2') return 1
        if (upperAnswer === 'C' || upperAnswer === '3') return 2
        if (upperAnswer === 'D' || upperAnswer === '4') return 3
      }
      // 如果是数字，确保在有效范围内
      const numAnswer = parseInt(correctAnswer)
      if (!isNaN(numAnswer) && numAnswer >= 0 && numAnswer < optionsCount) {
        return numAnswer
      }
      return 0 // 默认返回第一个选项
    },
    
    getDefaultQuestions() {
      // 根据章节ID返回默认题目
      const defaultQuestions = {
        '1': [
          {
            id: 1,
            type: 'choice',
            title: '人工智能的定义是什么？',
            description: '选择最准确的人工智能定义',
            options: [
              '能够执行特定任务的计算机程序',
              '模拟人类智能的计算机系统',
              '能够学习的算法',
              '自动化的机器'
            ],
            correctAnswer: 1,
            points: 20,
            explanation: '人工智能是模拟人类智能的计算机系统，能够执行通常需要人类智能的任务。'
          },
          {
            id: 2,
            type: 'true-false',
            title: '图灵测试是判断机器是否具有智能的标准方法。',
            description: '',
            correctAnswer: true,
            points: 15,
            explanation: '图灵测试由艾伦·图灵提出，通过判断机器能否在对话中让人类无法区分其是否为人类来测试智能。'
          },
          {
            id: 3,
            type: 'fill',
            title: '人工智能之父是谁？',
            description: '请输入人名',
            correctAnswer: '约翰·麦卡锡',
            points: 25,
            explanation: '约翰·麦卡锡在1956年的达特茅斯会议上首次提出"人工智能"这一术语。'
          }
        ],
        '2': [
          {
            id: 4,
            type: 'choice',
            title: '机器学习的核心是什么？',
            description: '选择机器学习最核心的概念',
            options: [
              '数据收集',
              '模式识别',
              '算法优化',
              '硬件升级'
            ],
            correctAnswer: 1,
            points: 20,
            explanation: '机器学习的核心是通过数据识别模式，让计算机能够从经验中学习。'
          },
          {
            id: 5,
            type: 'true-false',
            title: '无监督学习不需要标记数据。',
            description: '',
            correctAnswer: true,
            points: 15,
            explanation: '无监督学习确实不需要标记数据，它通过发现数据中的隐藏模式来学习。'
          },
          {
            id: 6,
            type: 'fill',
            title: '线性回归用于预测什么类型的值？',
            description: '请输入答案',
            correctAnswer: '连续值',
            points: 25,
            explanation: '线性回归用于预测连续值，如房价、温度等。'
          }
        ],
        '3': [
          {
            id: 7,
            type: 'choice',
            title: '深度学习的核心是什么？',
            description: '选择深度学习的核心概念',
            options: [
              '多层神经网络',
              '大数据处理',
              'GPU加速',
              '算法优化'
            ],
            correctAnswer: 0,
            points: 20,
            explanation: '深度学习的核心是多层神经网络，通过多层结构学习复杂的特征表示。'
          },
          {
            id: 8,
            type: 'true-false',
            title: '卷积神经网络主要用于图像处理。',
            description: '',
            correctAnswer: true,
            points: 15,
            explanation: 'CNN确实主要用于图像处理，能够有效提取图像的空间特征。'
          },
          {
            id: 9,
            type: 'fill',
            title: '反向传播算法用于什么？',
            description: '请输入答案',
            correctAnswer: '更新权重',
            points: 25,
            explanation: '反向传播算法用于计算梯度并更新神经网络权重。'
          }
        ],
        '4': [
          {
            id: 10,
            type: 'choice',
            title: 'AI在医疗领域的主要应用不包括？',
            description: '选择不属于AI医疗应用的选项',
            options: [
              '医学影像诊断',
              '药物发现',
              '患者护理',
              '硬件制造'
            ],
            correctAnswer: 3,
            points: 20,
            explanation: '硬件制造不属于AI医疗应用，其他都是AI在医疗领域的重要应用。'
          },
          {
            id: 11,
            type: 'true-false',
            title: '自动驾驶汽车使用多种AI技术。',
            description: '',
            correctAnswer: true,
            points: 15,
            explanation: '自动驾驶汽车确实使用计算机视觉、机器学习等多种AI技术。'
          },
          {
            id: 12,
            type: 'fill',
            title: '智能语音助手主要使用什么技术？',
            description: '请输入答案',
            correctAnswer: '自然语言处理',
            points: 25,
            explanation: '智能语音助手主要使用自然语言处理技术来理解和生成人类语言。'
          }
        ]
      }
      return defaultQuestions[this.chapterId] || []
    },
    selectAnswer(answer) {
      if (this.showAnswer) return
      this.selectedAnswer = answer
    },
    submitAnswer() {
      if (!this.canSubmit) return
      
      this.stopTimer() // 停止计时器
      this.showAnswer = true
      
      const userAnswer = this.currentQuestion.type === 'fill' ? this.fillAnswer : this.selectedAnswer
      const isCorrect = this.isAnswerCorrect
      
      // 计算得分（考虑难度和提示惩罚）
      let points = this.earnedPoints * this.difficultyMultiplier
      if (this.showHint) {
        points = Math.floor(points * (1 - this.hintPenalty))
      }
      
      // 记录答案
      this.userAnswers.push({
        questionId: this.currentQuestion.id,
        answer: userAnswer,
        correct: isCorrect,
        points: points,
        timeSpent: this.timeLimit - this.timeRemaining,
        hintsUsed: this.showHint ? 1 : 0
      })
      
      // 更新分数和统计
      this.totalScore += points
      if (isCorrect) {
        this.correctCount++
      } else {
        // 添加到错题本
        this.addToWrongQuestions(this.currentQuestion, userAnswer)
      }
      
      // 更新连击数
      this.updateStreak(isCorrect)
      
      // 如果使用了提示，增加计数
      if (this.showHint) {
        this.hintsUsed++
      }
    },
    nextQuestion() {
      if (this.isLastQuestion) {
        this.completeQuiz()
      } else {
        this.currentQuestionIndex++
        this.resetQuestion()
        // 开始下一题的计时器
        if (!this.practiceMode) {
          this.startTimer()
        }
      }
    },
    resetQuestion() {
      this.selectedAnswer = null
      this.fillAnswer = ''
      this.showAnswer = false
      this.showHint = false
    },
    completeQuiz() {
      this.quizCompleted = true
      this.calculateRewards()
      this.checkLevelUp()
      this.saveQuizResult()
      
      // 显示答题总结
      // 注意：QuizGuide组件没有showQuizSummary方法，暂时注释掉
      // this.$nextTick(() => {
      //   if (this.$refs.quizGuide) {
      //     setTimeout(() => {
      //       this.$refs.quizGuide.showQuizSummary()
      //     }, 1000)
      //   }
      // })
    },
    calculateRewards() {
      this.earnedRewards = []
      
      // 根据分数给予奖励
      if (this.finalScore >= 80) {
        this.earnedRewards.push({
          type: 'high_score',
          icon: '🏆',
          name: '高分成就',
          description: '获得80分以上的优异成绩'
        })
      }
      
      if (this.finalAccuracy >= 90) {
        this.earnedRewards.push({
          type: 'high_accuracy',
          icon: '🎯',
          name: '精准答题',
          description: '正确率达到90%以上'
        })
      }
      
      if (this.correctCount === this.questions.length) {
        this.earnedRewards.push({
          type: 'perfect',
          icon: '💎',
          name: '完美答题',
          description: '全部题目回答正确'
        })
      }
    },
    checkLevelUp() {
      const oldLevel = this.userLevel
      const newLevel = this.userLevel
      
      if (oldLevel !== newLevel) {
        this.levelUp = true
        this.oldLevel = oldLevel
        this.newLevel = newLevel
      }
    },
    async saveQuizResult() {
      try {
        await quizApi.saveQuizResult({
          chapterId: this.chapterId,
          score: this.finalScore,
          accuracy: this.finalAccuracy,
          correctCount: this.correctCount,
          totalQuestions: this.questions.length,
          answers: this.userAnswers,
          rewards: this.earnedRewards
        })
      } catch (error) {
        console.error('保存答题结果失败:', error)
      }
    },
    restartQuiz() {
      this.currentQuestionIndex = 0
      this.selectedAnswer = null
      this.fillAnswer = ''
      this.showAnswer = false
      this.quizCompleted = false
      this.userAnswers = []
      this.totalScore = 0
      this.correctCount = 0
      this.earnedRewards = []
      this.levelUp = false
      this.showHint = false
      
      // 重置卡通人物的统计数据
      // 注意：QuizGuide组件没有resetStats方法，暂时注释掉
      // this.$nextTick(() => {
      //   if (this.$refs.quizGuide) {
      //     this.$refs.quizGuide.resetStats()
      //   }
      // })
    },
    goToChapter() {
      this.$router.push(`/chapters/${this.chapterId}`)
    },
    shareResult() {
      const shareText = `我在《${this.chapterTitle}》测验中获得了${this.finalScore}分，正确率${this.finalAccuracy}%！`
      if (navigator.share) {
        navigator.share({
          title: '我的测验成绩',
          text: shareText
        })
      } else {
        // 复制到剪贴板
        navigator.clipboard.writeText(shareText).then(() => {
          alert('成绩已复制到剪贴板！')
        })
      }
    },
    getQuestionTypeClass(type) {
      const classMap = {
        'choice': 'type-choice',
        'true-false': 'type-tf',
        'fill': 'type-fill'
      }
      return classMap[type] || ''
    },
    getQuestionTypeText(type) {
      const textMap = {
        'choice': '选择题',
        'true-false': '判断题',
        'fill': '填空题'
      }
      return textMap[type] || '题目'
    },
    
    getHintForQuestion(question) {
      if (!question) return '请仔细阅读题目内容'
      
      const hints = {
        'choice': '仔细分析每个选项，排除明显错误的答案，选择最符合题意的选项。',
        'true-false': '仔细阅读题目陈述，判断其是否符合事实或逻辑。',
        'fill': '根据题目上下文和关键词，填写最合适的答案。'
      }
      
      return hints[question.type] || '仔细阅读题目，注意关键词和细节。'
    },
    
    // 新增方法
    formatTime(seconds) {
      const mins = Math.floor(seconds / 60)
      const secs = seconds % 60
      return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
    },
    
    startTimer() {
      if (this.practiceMode) return
      
      this.isTimerActive = true
      this.timeRemaining = this.timeLimit
      
      this.timerInterval = setInterval(() => {
        this.timeRemaining--
        
        if (this.timeRemaining <= 0) {
          this.timeUp()
        }
      }, 1000)
    },
    
    stopTimer() {
      if (this.timerInterval) {
        clearInterval(this.timerInterval)
        this.timerInterval = null
      }
      this.isTimerActive = false
    },
    
    timeUp() {
      this.stopTimer()
      // 自动提交当前答案（如果有的话）
      if (this.canSubmit) {
        this.submitAnswer()
      } else {
        // 没有答案则记录为错误
        this.userAnswers.push({
          questionId: this.currentQuestion.id,
          answer: null,
          correct: false,
          points: 0,
          timeUp: true
        })
        this.showAnswer = true
        this.streakCount = 0 // 重置连击
      }
    },
    
    setDifficulty(diff) {
      this.difficulty = diff
      const multipliers = {
        'easy': 0.8,
        'normal': 1.0,
        'hard': 1.3
      }
      this.difficultyMultiplier = multipliers[diff]
      
      const timeLimits = {
        'easy': 90,
        'normal': 60,
        'hard': 45
      }
      this.timeLimit = timeLimits[diff]
    },
    
    setQuizMode(mode) {
      this.quizMode = mode
      this.practiceMode = mode === 'practice'
      this.examMode = mode === 'exam'
      
      if (mode === 'practice') {
        this.maxHints = 5
        this.hintPenalty = 0
      } else if (mode === 'exam') {
        this.maxHints = 1
        this.hintPenalty = 0.2
        this.timeLimit = Math.floor(this.timeLimit * 0.8) // 考试模式时间更紧
      }
    },
    
    startQuiz() {
      // 检查是否选择了难度和模式
      if (!this.difficulty) {
        this.$message.warning('请先选择难度')
        return
      }
      if (!this.quizMode) {
        this.$message.warning('请先选择答题模式')
        return
      }
      
      // 开始答题，设置第一个题目
      this.currentQuestionIndex = 1
      this.showAnswer = false
      this.selectedAnswer = null
      this.fillAnswer = ''
      this.timeRemaining = this.timeLimit
      this.startTimer()
      
      // 根据难度调整时间限制
      if (this.difficulty === 'easy') {
        this.timeLimit = 120 // 简单模式：2分钟
      } else if (this.difficulty === 'normal') {
        this.timeLimit = 90  // 普通模式：1.5分钟
      } else {
        this.timeLimit = 60  // 困难模式：1分钟
      }
      
      this.timeRemaining = this.timeLimit
    },
    
    getDifficultyIcon(diff) {
      const icons = {
        'easy': '🌱',
        'normal': '⚡',
        'hard': '🔥'
      }
      return icons[diff]
    },
    
    getDifficultyText(diff) {
      const texts = {
        'easy': '简单',
        'normal': '普通',
        'hard': '困难'
      }
      return texts[diff]
    },
    
    getDifficultyDesc(diff) {
      const descs = {
        'easy': '更多时间，基础题目',
        'normal': '标准难度，适中时间',
        'hard': '高难度，时间紧张'
      }
      return descs[diff]
    },
    
    addToWrongQuestions(question, userAnswer) {
      const wrongQuestion = {
        ...question,
        userAnswer,
        timestamp: new Date().toISOString(),
        reviewCount: 0
      }
      
      // 避免重复添加
      const exists = this.wrongQuestions.find(q => q.id === question.id)
      if (!exists) {
        this.wrongQuestions.push(wrongQuestion)
        // 保存到本地存储
        localStorage.setItem('wrongQuestions', JSON.stringify(this.wrongQuestions))
      }
    },
    
    loadWrongQuestions() {
      const saved = localStorage.getItem('wrongQuestions')
      if (saved) {
        this.wrongQuestions = JSON.parse(saved)
      }
    },
    
    updateStreak(isCorrect) {
      if (isCorrect) {
        this.streakCount++
        this.maxStreak = Math.max(this.maxStreak, this.streakCount)
      } else {
        this.streakCount = 0
      }
    }
  }
}
</script>

<style lang="scss" scoped>


.quiz-system {
  background: linear-gradient(135deg, $primary-color 0%, $secondary-color 100%);
  min-height: 100vh;
  padding: 2rem 0;
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: 
      radial-gradient(circle at 20% 80%, rgba($accent-color, 0.03) 0%, transparent 50%),
      radial-gradient(circle at 80% 20%, rgba($accent-color, 0.02) 0%, transparent 50%);
    pointer-events: none;
  }
}

.quiz-header {
  max-width: 1000px;
  margin: 0 auto 2rem;
  padding: 0 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(145deg, $card-bg 0%, rgba($card-bg, 0.95) 100%);
  border-radius: $card-radius;
  padding: 2.5rem;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.3),
    0 8px 16px rgba($accent-color, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.05);
  border: 1px solid $card-border;
  backdrop-filter: blur(20px);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, transparent, rgba($accent-color, 0.3), transparent);
  }
}

.quiz-info {
  flex: 1;
}

.quiz-title {
  font-size: 2rem;
  color: $text-color;
  margin-bottom: 0.8rem;
  font-weight: 800;
  letter-spacing: 0.5px;
  background: linear-gradient(135deg, $text-color 0%, $accent-color-light 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.quiz-description {
  color: $text-secondary-color;
  font-size: 1.1rem;
  line-height: 1.6;
  font-weight: 400;
}

.user-progress {
  display: flex;
  gap: 1.5rem;
}

.progress-item {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  background: linear-gradient(135deg, rgba($accent-color, 0.05) 0%, rgba($accent-color-light, 0.05) 100%);
  padding: 1rem 1.2rem;
  border-radius: $btn-radius;
  border: 1px solid rgba($accent-color, 0.1);
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
    background: linear-gradient(90deg, transparent, rgba($accent-color, 0.1), transparent);
    transition: left 0.6s ease;
  }
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba($accent-color, 0.2);
    border-color: rgba($accent-color, 0.2);
    
    &::before {
      left: 100%;
    }
  }
}

.progress-icon {
  font-size: 1.4rem;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.progress-info {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.progress-label {
  font-size: 0.85rem;
  color: $text-secondary-color;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.progress-value {
  font-size: 1.3rem;
  color: $text-color;
  font-weight: 800;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.quiz-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 1rem;
  position: relative;
  z-index: 1;
}

// 答题设置样式
.quiz-settings {
  max-width: 800px;
  margin: 0 auto 2rem;
  padding: 0 1rem;
}

.settings-panel {
  background: linear-gradient(145deg, $card-bg 0%, rgba($card-bg, 0.95) 100%);
  border-radius: $card-radius;
  padding: 2.5rem;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.3),
    0 8px 16px rgba($accent-color, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.05);
  border: 1px solid $card-border;
  backdrop-filter: blur(20px);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, transparent, rgba($accent-color, 0.3), transparent);
  }
}

.settings-title {
  color: $text-color;
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 2rem;
  text-align: center;
  background: linear-gradient(135deg, $text-color 0%, $accent-color-light 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.setting-group {
  margin-bottom: 2rem;
}

.setting-label {
  display: block;
  color: $text-color;
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 1rem;
  text-align: center;
}

.difficulty-options, .mode-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1rem;
}

.difficulty-btn, .mode-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.8rem;
  padding: 1.5rem;
  background: linear-gradient(135deg, rgba($accent-color, 0.05) 0%, rgba($accent-color-light, 0.05) 100%);
  border: 2px solid rgba($accent-color, 0.1);
  border-radius: $btn-radius;
  cursor: pointer;
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
    background: linear-gradient(90deg, transparent, rgba($accent-color, 0.1), transparent);
    transition: left 0.6s ease;
  }
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 24px rgba($accent-color, 0.2);
    border-color: rgba($accent-color, 0.3);
    
    &::before {
      left: 100%;
    }
  }
  
  &.active {
    background: linear-gradient(135deg, rgba($accent-color, 0.15) 0%, rgba($accent-color-light, 0.15) 100%);
    border-color: $accent-color;
    box-shadow: 0 8px 24px rgba($accent-color, 0.3);
  }
}

.diff-icon, .mode-icon {
  font-size: 2rem;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
}

.diff-text, .mode-text {
  color: $text-color;
  font-size: 1.1rem;
  font-weight: 700;
  text-align: center;
}

.diff-desc, .mode-desc {
  color: $text-secondary-color;
  font-size: 0.9rem;
  text-align: center;
  line-height: 1.4;
}

.start-quiz-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.8rem;
  width: 100%;
  max-width: 300px;
  margin: 2rem auto 0;
  padding: 1.2rem 2rem;
  background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
  border: none;
  border-radius: $btn-radius;
  color: white;
  font-size: 1.2rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 8px 24px rgba($accent-color, 0.3);
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 12px 32px rgba($accent-color, 0.4);
  }
  
  &:active {
    transform: translateY(-1px);
  }
}

.quiz-progress {
  background: linear-gradient(145deg, $card-bg 0%, rgba($card-bg, 0.95) 100%);
  border-radius: $card-radius;
  padding: 2rem;
  margin-bottom: 2rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 
    0 16px 32px rgba(0, 0, 0, 0.2),
    0 4px 8px rgba($accent-color, 0.1);
  border: 1px solid $card-border;
  backdrop-filter: blur(12px);
  gap: 2rem;
  flex-wrap: wrap;
}

.progress-section {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex: 1;
  min-width: 200px;
}

.timer-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  min-width: 120px;
}

.timer-display {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, rgba($info-color, 0.1) 0%, rgba($info-color-light, 0.1) 100%);
  border-radius: $btn-radius;
  border: 1px solid rgba($info-color, 0.2);
  transition: all 0.3s ease;
  
  &.warning {
    background: linear-gradient(135deg, rgba($warning-color, 0.1) 0%, rgba($warning-color-light, 0.1) 100%);
    border-color: rgba($warning-color, 0.3);
    animation: pulse 1s ease-in-out infinite;
  }
  
  &.critical {
    background: linear-gradient(135deg, rgba($error-color, 0.1) 0%, rgba($error-color-light, 0.1) 100%);
    border-color: rgba($error-color, 0.3);
    animation: shake 0.5s ease-in-out infinite;
  }
}

.timer-icon {
  font-size: 1.2rem;
}

.timer-text {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.timer-value {
  font-size: 1.1rem;
  font-weight: 700;
  color: $text-color;
}

.timer-label {
  font-size: 0.75rem;
  color: $text-secondary-color;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.timer-progress {
  width: 100px;
  height: 4px;
  background: rgba($text-secondary-color, 0.2);
  border-radius: 2px;
  overflow: hidden;
}

.timer-fill {
  height: 100%;
  background: linear-gradient(90deg, $info-color 0%, $info-color-light 100%);
  transition: all 0.3s ease;
  
  &.warning {
    background: linear-gradient(90deg, $warning-color 0%, $warning-color-light 100%);
  }
  
  &.critical {
    background: linear-gradient(90deg, $error-color 0%, $error-color-light 100%);
  }
}

.quiz-stats {
  display: flex;
  gap: 1rem;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.2rem;
  padding: 0.5rem;
  background: linear-gradient(135deg, rgba($accent-color, 0.05) 0%, rgba($accent-color-light, 0.05) 100%);
  border-radius: $btn-radius;
  border: 1px solid rgba($accent-color, 0.1);
  min-width: 60px;
}

.stat-icon {
  font-size: 1.2rem;
}

.stat-value {
  font-size: 1.1rem;
  font-weight: 700;
  color: $text-color;
}

.stat-label {
  font-size: 0.7rem;
  color: $text-secondary-color;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.progress-bar {
  flex: 1;
  height: 12px;
  background: linear-gradient(135deg, rgba($accent-color, 0.1) 0%, rgba($accent-color-light, 0.1) 100%);
  border-radius: 6px;
  overflow: hidden;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba($accent-color, 0.1);
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, $accent-color 0%, $accent-color-light 100%);
  transition: width 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  box-shadow: 0 2px 8px rgba($accent-color, 0.4);
  position: relative;
  
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
    animation: shimmer 2s infinite;
  }
}

.progress-text {
  color: $text-color;
  font-weight: 700;
  font-size: 1rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  min-width: 60px;
  text-align: center;
}

.question-container {
  background: linear-gradient(145deg, $card-bg 0%, rgba($card-bg, 0.95) 100%);
  border-radius: $card-radius;
  padding: 2.5rem;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.3),
    0 8px 16px rgba($accent-color, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.05);
  border: 1px solid $card-border;
  backdrop-filter: blur(20px);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, transparent, rgba($accent-color, 0.3), transparent);
  }
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.question-type-badge {
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 600;
  color: #f5f6fa;
  
  &.type-choice {
    background: linear-gradient(135deg, #667eea, #764ba2);
  }
  
  &.type-tf {
    background: linear-gradient(135deg, #f093fb, #f5576c);
  }
  
  &.type-fill {
    background: linear-gradient(135deg, #4facfe, #00f2fe);
  }
}

.question-points {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  background: #23272e;
  padding: 0.5rem 0.8rem;
  border-radius: 12px;
  border: 1px solid rgba(57,59,64,0.18);
}

.points-icon {
  font-size: 1rem;
}

.points-value {
  color: #8fa1b3;
  font-weight: 600;
  font-size: 0.9rem;
}

.question-content {
  margin-bottom: 2rem;
}

.question-title {
  font-size: 1.5rem;
  color: $text-color;
  margin-bottom: 1.2rem;
  font-weight: 700;
  line-height: 1.6;
  letter-spacing: 0.3px;
}

.question-description {
  color: $text-secondary-color;
  font-size: 1.1rem;
  line-height: 1.7;
  font-weight: 400;
  background: rgba($accent-color, 0.05);
  padding: 1rem;
  border-radius: 8px;
  border-left: 4px solid $accent-color;
  margin-bottom: 1rem;
}

.question-media {
  margin-bottom: 1.5rem;
  
  .media-item {
    margin-bottom: 1rem;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .question-image {
    max-width: 100%;
    height: auto;
    border-radius: 8px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    border: 1px solid rgba($accent-color, 0.2);
  }
  
  .question-audio,
  .question-video {
    width: 100%;
    border-radius: 8px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    border: 1px solid rgba($accent-color, 0.2);
  }
  
  .question-audio {
    height: 60px;
  }
  
  .question-video {
    height: auto;
    max-height: 400px;
  }
}

.options-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 1.2rem;
  padding: 1.2rem 1.5rem;
  background: linear-gradient(135deg, rgba($accent-color, 0.03) 0%, rgba($accent-color-light, 0.03) 100%);
  border: 2px solid rgba($accent-color, 0.1);
  border-radius: $btn-radius;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba($accent-color, 0.1), transparent);
    transition: left 0.6s ease;
  }
  
  &:hover {
    border-color: rgba($accent-color, 0.3);
    background: linear-gradient(135deg, rgba($accent-color, 0.08) 0%, rgba($accent-color-light, 0.08) 100%);
    transform: translateX(4px);
    box-shadow: 0 4px 12px rgba($accent-color, 0.2);
    
    &::before {
      left: 100%;
    }
  }
  
  &.selected {
    border-color: $info-color;
    background: linear-gradient(135deg, rgba($info-color, 0.15) 0%, rgba($info-color, 0.1) 100%);
    box-shadow: 0 4px 16px rgba($info-color, 0.3);
  }
  
  &.correct {
    border-color: #4caf50;
    background: rgba(76, 175, 80, 0.1);
  }
  
  &.incorrect {
    border-color: #f44336;
    background: rgba(244, 67, 54, 0.1);
  }
}

.option-marker {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba($accent-color, 0.1) 0%, rgba($accent-color-light, 0.1) 100%);
  color: $accent-color;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1rem;
  border: 2px solid rgba($accent-color, 0.2);
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba($accent-color, 0.1);
}

.option-text {
  flex: 1;
  color: $text-color;
  font-size: 1.1rem;
  font-weight: 500;
  line-height: 1.5;
}

.option-status {
  font-size: 1.2rem;
  font-weight: bold;
  
  .status-correct {
    color: #4caf50;
  }
  
  .status-incorrect {
    color: #f44336;
  }
}

.fill-blank-container {
  margin-bottom: 2rem;
}

.input-group {
  display: flex;
  gap: 1rem;
}

.fill-input {
  flex: 1;
  padding: 1.2rem 1.5rem;
  background: linear-gradient(135deg, rgba($accent-color, 0.03) 0%, rgba($accent-color-light, 0.03) 100%);
  border: 2px solid rgba($accent-color, 0.1);
  border-radius: $btn-radius;
  color: $text-color;
  font-size: 1.1rem;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba($accent-color, 0.05);
  
  &:focus {
    outline: none;
    border-color: $accent-color;
    box-shadow: 0 4px 16px rgba($accent-color, 0.2);
    transform: translateY(-1px);
  }
  
  &::placeholder {
    color: $text-secondary-color;
    font-weight: 400;
  }
}

.true-false-container {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}

.tf-option {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.2rem;
  background: #23272e;
  border: 2px solid rgba(57,59,64,0.18);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover {
    border-color: #8fa1b3;
    background: #2a2d34;
  }
  
  &.selected {
    border-color: #4a90e2;
    background: rgba(74, 144, 226, 0.1);
  }
}

.tf-marker {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #393b40;
  color: #8fa1b3;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  font-weight: bold;
}

.tf-text {
  color: #f5f6fa;
  font-size: 1.1rem;
  font-weight: 600;
}

.hint-section {
  margin-bottom: 2rem;
  animation: slideInUp 0.3s ease-out;
}

.hint-content {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  background: linear-gradient(135deg, rgba($warning-color, 0.1) 0%, rgba($warning-color-light, 0.1) 100%);
  border: 1px solid rgba($warning-color, 0.3);
  border-radius: $card-radius;
  padding: 1.5rem;
  box-shadow: 0 8px 24px rgba($warning-color, 0.2);
  backdrop-filter: blur(12px);
}

.hint-icon {
  font-size: 2rem;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
  animation: glow 2s ease-in-out infinite;
}

.hint-text {
  flex: 1;
  
  h4 {
    color: $text-color;
    margin: 0 0 0.5rem 0;
    font-size: 1.1rem;
    font-weight: 700;
  }
  
  p {
    color: $text-secondary-color;
    margin: 0;
    font-size: 1rem;
    line-height: 1.6;
  }
}

.question-actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.btn {
  padding: 1.2rem 2.5rem;
  border: none;
  border-radius: $btn-radius;
  font-weight: 700;
  font-size: 1.1rem;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  overflow: hidden;
  letter-spacing: 0.5px;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    transition: left 0.6s ease;
  }
  
  &:hover::before {
    left: 100%;
  }
  
  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
    transform: none !important;
  }
}

.btn-primary {
  background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
  color: $text-color;
  box-shadow: 0 4px 16px rgba($accent-color, 0.3);
  
  &:hover:not(:disabled) {
    transform: translateY(-3px);
    box-shadow: 0 8px 24px rgba($accent-color, 0.4);
  }
  
  &:active {
    transform: translateY(-1px);
  }
}

.btn-next {
  background: linear-gradient(135deg, $success-color 0%, $success-color-light 100%);
  color: $text-color;
  box-shadow: 0 4px 16px rgba($success-color, 0.3);
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 24px rgba($success-color, 0.4);
  }
}

.answer-feedback {
  margin-top: 2rem;
  animation: slideInUp 0.5s ease-out;
}

.feedback-content {
  display: flex;
  align-items: flex-start;
  gap: 1.5rem;
  padding: 2rem;
  border-radius: $card-radius;
  backdrop-filter: blur(12px);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba($accent-color, 0.05) 0%, rgba($accent-color-light, 0.05) 100%);
    z-index: -1;
  }
  
  &.correct {
    background: linear-gradient(135deg, rgba($success-color, 0.1) 0%, rgba($success-color-light, 0.1) 100%);
    border: 1px solid rgba($success-color, 0.3);
    box-shadow: 0 8px 24px rgba($success-color, 0.2);
  }
  
  &.incorrect {
    background: linear-gradient(135deg, rgba($error-color, 0.1) 0%, rgba($error-color-light, 0.1) 100%);
    border: 1px solid rgba($error-color, 0.3);
    box-shadow: 0 8px 24px rgba($error-color, 0.2);
  }
}

.feedback-icon {
  font-size: 2.5rem;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
  animation: bounce 0.6s ease-out;
}

.feedback-text h4 {
  color: $text-color;
  margin-bottom: 0.8rem;
  font-weight: 700;
  font-size: 1.3rem;
  letter-spacing: 0.5px;
}

.feedback-text p {
  color: $text-secondary-color;
  margin-bottom: 1.2rem;
  line-height: 1.7;
  font-size: 1.1rem;
}

.score-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.score-label {
  color: #8fa1b3;
  font-size: 0.9rem;
}

.score-value {
  color: #4caf50;
  font-weight: 700;
  font-size: 1.1rem;
}

.quiz-result {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 1rem;
  animation: fadeInUp 0.8s ease-out;
}

.result-header {
  text-align: center;
  margin-bottom: 3rem;
  background: linear-gradient(145deg, $card-bg 0%, rgba($card-bg, 0.95) 100%);
  border-radius: $card-radius;
  padding: 3rem 2rem;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.3),
    0 8px 16px rgba($accent-color, 0.1);
  border: 1px solid $card-border;
  backdrop-filter: blur(20px);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, transparent, rgba($accent-color, 0.3), transparent);
  }
}

.result-icon {
  font-size: 5rem;
  margin-bottom: 1.5rem;
  filter: drop-shadow(0 8px 16px rgba(0, 0, 0, 0.3));
  animation: celebration 1s ease-out;
}

.result-title {
  font-size: 3rem;
  color: $text-color;
  margin-bottom: 1rem;
  font-weight: 800;
  letter-spacing: 1px;
  background: linear-gradient(135deg, $text-color 0%, $accent-color-light 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.result-subtitle {
  color: $text-secondary-color;
  font-size: 1.3rem;
  font-weight: 500;
  line-height: 1.6;
}

.result-stats {
  display: flex;
  justify-content: space-around;
  margin-bottom: 3rem;
  background: linear-gradient(145deg, $card-bg 0%, rgba($card-bg, 0.95) 100%);
  border-radius: $card-radius;
  padding: 2.5rem;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.3),
    0 8px 16px rgba($accent-color, 0.1);
  border: 1px solid $card-border;
  backdrop-filter: blur(20px);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, transparent, rgba($accent-color, 0.3), transparent);
  }
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 1.2rem;
  padding: 1rem;
  background: linear-gradient(135deg, rgba($accent-color, 0.05) 0%, rgba($accent-color-light, 0.05) 100%);
  border-radius: $btn-radius;
  border: 1px solid rgba($accent-color, 0.1);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba($accent-color, 0.2);
    border-color: rgba($accent-color, 0.2);
  }
}

.stat-icon {
  font-size: 2.5rem;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
}

.stat-info {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.stat-value {
  font-size: 1.8rem;
  color: $text-color;
  font-weight: 800;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.stat-label {
  color: $text-secondary-color;
  font-size: 1rem;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.rewards-section {
  background: linear-gradient(145deg, $card-bg 0%, rgba($card-bg, 0.95) 100%);
  border-radius: $card-radius;
  padding: 2.5rem;
  margin-bottom: 2rem;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.3),
    0 8px 16px rgba($accent-color, 0.1);
  border: 1px solid $card-border;
  backdrop-filter: blur(20px);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, transparent, rgba($accent-color, 0.3), transparent);
  }
}

.rewards-title {
  color: $text-color;
  margin-bottom: 2rem;
  font-size: 1.5rem;
  font-weight: 700;
  letter-spacing: 0.5px;
  text-align: center;
}

.rewards-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.reward-item {
  display: flex;
  align-items: center;
  gap: 1.2rem;
  padding: 1.5rem;
  background: linear-gradient(135deg, rgba($accent-color, 0.05) 0%, rgba($accent-color-light, 0.05) 100%);
  border-radius: $btn-radius;
  border: 1px solid rgba($accent-color, 0.1);
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
    background: linear-gradient(90deg, transparent, rgba($accent-color, 0.1), transparent);
    transition: left 0.6s ease;
  }
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba($accent-color, 0.2);
    border-color: rgba($accent-color, 0.2);
    
    &::before {
      left: 100%;
    }
  }
}

.reward-icon {
  font-size: 2rem;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
}

.reward-info {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.reward-name {
  color: $text-color;
  font-weight: 700;
  font-size: 1.1rem;
  letter-spacing: 0.3px;
}

.reward-desc {
  color: $text-secondary-color;
  font-size: 1rem;
  line-height: 1.5;
}

.level-up {
  background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
  border-radius: $card-radius;
  padding: 2.5rem;
  margin-bottom: 2rem;
  text-align: center;
  box-shadow: 
    0 20px 40px rgba($accent-color, 0.4),
    0 8px 16px rgba($accent-color, 0.2);
  border: 1px solid rgba($accent-color, 0.3);
  backdrop-filter: blur(20px);
  position: relative;
  overflow: hidden;
  animation: levelUpPulse 2s ease-in-out infinite;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.1) 50%, transparent 70%);
    animation: shimmer 3s infinite;
  }
}

.level-up-content h3 {
  color: $text-color;
  margin-bottom: 1rem;
  font-size: 1.8rem;
  font-weight: 800;
  letter-spacing: 0.5px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.level-up-content p {
  color: $text-color;
  font-size: 1.2rem;
  font-weight: 600;
  line-height: 1.6;
}

.level-up-icon {
  font-size: 4rem;
  margin-bottom: 1.5rem;
  filter: drop-shadow(0 8px 16px rgba(0, 0, 0, 0.3));
  animation: rocketLaunch 1s ease-out;
}

.result-actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
  flex-wrap: wrap;
}

.btn-secondary {
  background: linear-gradient(135deg, $secondary-color 0%, $secondary-color-light 100%);
  color: $text-color;
  box-shadow: 0 4px 16px rgba($secondary-color, 0.3);
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 24px rgba($secondary-color, 0.4);
  }
}

.btn-hint {
  background: linear-gradient(135deg, $warning-color 0%, $warning-color-light 100%);
  color: $text-color;
  box-shadow: 0 4px 16px rgba($warning-color, 0.3);
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 24px rgba($warning-color, 0.4);
  }
  
  &.active {
    background: linear-gradient(135deg, $success-color 0%, $success-color-light 100%);
    box-shadow: 0 4px 16px rgba($success-color, 0.3);
  }
}

.btn-share {
  background: linear-gradient(135deg, $warning-color 0%, $warning-color-light 100%);
  color: $text-color;
  box-shadow: 0 4px 16px rgba($warning-color, 0.3);
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 24px rgba($warning-color, 0.4);
  }
}

// 动画效果
@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bounce {
  0%, 20%, 53%, 80%, 100% {
    transform: translate3d(0, 0, 0);
  }
  40%, 43% {
    transform: translate3d(0, -8px, 0);
  }
  70% {
    transform: translate3d(0, -4px, 0);
  }
  90% {
    transform: translate3d(0, -2px, 0);
  }
}

@keyframes celebration {
  0% {
    transform: scale(0.5) rotate(0deg);
    opacity: 0;
  }
  50% {
    transform: scale(1.2) rotate(180deg);
    opacity: 1;
  }
  100% {
    transform: scale(1) rotate(360deg);
    opacity: 1;
  }
}

@keyframes shimmer {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100%);
  }
}

@keyframes levelUpPulse {
  0%, 100% {
    transform: scale(1);
    box-shadow: 
      0 20px 40px rgba($accent-color, 0.4),
      0 8px 16px rgba($accent-color, 0.2);
  }
  50% {
    transform: scale(1.02);
    box-shadow: 
      0 25px 50px rgba($accent-color, 0.5),
      0 12px 24px rgba($accent-color, 0.3);
  }
}

@keyframes rocketLaunch {
  0% {
    transform: translateY(20px) scale(0.8);
    opacity: 0;
  }
  50% {
    transform: translateY(-10px) scale(1.1);
    opacity: 1;
  }
  100% {
    transform: translateY(0) scale(1);
    opacity: 1;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .quiz-header {
    flex-direction: column;
    gap: 1.5rem;
    text-align: center;
  }
  
  .user-progress {
    justify-content: center;
  }
  
  .result-stats {
    flex-direction: column;
    gap: 1.5rem;
  }
  
  .result-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .btn {
    width: 100%;
    max-width: 300px;
  }
  
  .true-false-container {
    flex-direction: column;
  }
}
</style>