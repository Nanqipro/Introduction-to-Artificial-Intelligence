<template>
  <div class="quiz-guide" :class="{ 'guide-active': isActive, 'guide-hidden': isHidden }">
    <!-- 现代可爱卡通人物容器 -->
    <div class="guide-character" @click="toggleGuide">
      <div class="character-container">
        <!-- 现代可爱动漫角色 -->
        <div class="character-main">
          <!-- 头部区域 -->
          <div class="character-head">
            <!-- 动态头发 -->
            <div class="hair-layer">
              <div class="hair-main"></div>
              <div class="hair-highlight"></div>
              <div class="hair-strand left"></div>
              <div class="hair-strand right"></div>
              <div class="hair-accessory"></div>
            </div>
            
            <!-- 表情丰富的脸部 -->
            <div class="face">
              <!-- 大眼睛设计 -->
              <div class="eyes-container">
                <div class="eye left-eye" :class="{ 'blink': isBlinking, 'happy': mouthExpression === 'happy', 'excited': mouthExpression === 'excited', 'sad': mouthExpression === 'sad' }">
                  <div class="eye-white"></div>
                  <div class="eye-iris"></div>
                  <div class="eye-pupil"></div>
                  <div class="eye-shine primary"></div>
                  <div class="eye-shine secondary"></div>
                  <div class="eyelash"></div>
                </div>
                <div class="eye right-eye" :class="{ 'blink': isBlinking, 'happy': mouthExpression === 'happy', 'excited': mouthExpression === 'excited', 'sad': mouthExpression === 'sad' }">
                  <div class="eye-white"></div>
                  <div class="eye-iris"></div>
                  <div class="eye-pupil"></div>
                  <div class="eye-shine primary"></div>
                  <div class="eye-shine secondary"></div>
                  <div class="eyelash"></div>
                </div>
              </div>
              
              <!-- 眉毛 -->
              <div class="eyebrows">
                <div class="eyebrow left-eyebrow" :class="mouthExpression"></div>
                <div class="eyebrow right-eyebrow" :class="mouthExpression"></div>
              </div>
              
              <!-- 可爱的脸颊 -->
              <div class="cheeks" :class="{ 'show': mouthExpression === 'happy' || mouthExpression === 'excited' }">
                <div class="cheek left-cheek"></div>
                <div class="cheek right-cheek"></div>
              </div>
              
              <!-- 表情嘴巴 -->
              <div class="mouth-container">
                <div class="mouth" :class="mouthExpression"></div>
                <div class="mouth-highlight" v-if="mouthExpression === 'happy' || mouthExpression === 'excited'"></div>
              </div>
              
              <!-- 鼻子 -->
              <div class="nose"></div>
            </div>
          </div>
          
          <!-- 身体部分 -->
          <div class="character-body">
            <div class="body-main"></div>
            <div class="body-pattern"></div>
            <div class="body-shadow"></div>
            
            <!-- 动态手臂 -->
            <div class="arms-container">
              <div class="arm left-arm" :class="{ 'wave': isWaving, 'point': isPointing, 'thinking': mouthExpression === 'worried' }">
                <div class="arm-upper"></div>
                <div class="arm-lower"></div>
                <div class="hand">
                  <div class="fingers"></div>
                  <div class="thumb"></div>
                </div>
              </div>
              <div class="arm right-arm" :class="{ 'wave': isWaving, 'point': isPointing, 'celebrate': mouthExpression === 'excited' }">
                <div class="arm-upper"></div>
                <div class="arm-lower"></div>
                <div class="hand">
                  <div class="fingers"></div>
                  <div class="thumb"></div>
                </div>
              </div>
            </div>
            
            <!-- 腿部 -->
            <div class="legs-container">
              <div class="leg left-leg">
                <div class="leg-upper"></div>
                <div class="leg-lower"></div>
                <div class="foot"></div>
              </div>
              <div class="leg right-leg">
                <div class="leg-upper"></div>
                <div class="leg-lower"></div>
                <div class="foot"></div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 装饰元素 -->
        <div class="character-effects">
          <div class="sparkle" v-if="mouthExpression === 'excited'"></div>
          <div class="hearts" v-if="mouthExpression === 'happy'">
            <div class="heart"></div>
            <div class="heart"></div>
          </div>
        </div>
      </div>
      
      <!-- 状态指示器 -->
      <div class="status-indicator" v-if="showStatus">
        <div class="status-dot" :class="statusType"></div>
        <div class="status-ring"></div>
      </div>
    </div>

    <!-- 指导对话框 -->
    <div class="guide-dialog" v-if="showDialog" :class="dialogType">
      <div class="dialog-content">
        <div class="dialog-icon">{{ dialogIcon }}</div>
        <div class="dialog-text">
          <h4>{{ dialogTitle }}</h4>
          <p>{{ dialogMessage }}</p>
        </div>
      </div>
      <div class="dialog-arrow"></div>
    </div>

    <!-- 浮动提示 -->
    <div class="floating-tip" v-if="showFloatingTip" :class="tipType">
      <div class="tip-content">
        <span class="tip-icon">{{ tipIcon }}</span>
        <span class="tip-text">{{ floatingTip }}</span>
      </div>
    </div>
    
    <!-- 测试按钮（开发时使用） -->
    <div class="test-buttons" v-if="false">
      <button @click="testExpression('normal')">正常</button>
      <button @click="testExpression('happy')">开心</button>
      <button @click="testExpression('excited')">兴奋</button>
      <button @click="testExpression('sad')">难过</button>
      <button @click="testExpression('worried')">担心</button>
      <button @click="testAccuracy()">测试正确率</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'QuizGuide',
  props: {
    currentQuestion: {
      type: Object,
      default: null
    },
    questionIndex: {
      type: Number,
      default: 0
    },
    totalQuestions: {
      type: Number,
      default: 0
    },
    userAnswer: {
      type: [String, Number, Boolean],
      default: null
    },
    isCorrect: {
      type: Boolean,
      default: null
    },
    showHint: {
      type: Boolean,
      default: false
    },
    showAnswer: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      isActive: false,
      isHidden: false,
      isBlinking: false,
      isWaving: false,
      isPointing: false,
      showDialog: false,
      showFloatingTip: false,
      dialogType: 'info',
      tipType: 'info',
      currentTip: '',
      tipIcon: '⚡',
      dialogIcon: '⚡',
      dialogTitle: '佐助导师',
      dialogMessage: '我是你的忍术导师，让我来指导你的学习！',
      floatingTip: '',
      mouthExpression: 'normal',
      // 智能反应相关数据
      consecutiveCorrect: 0,
      consecutiveWrong: 0,
      totalCorrect: 0,
      totalWrong: 0,
      questionHistory: [],
      userMood: 'neutral', // neutral, happy, sad, excited, worried
      lastReactionTime: 0
    }
  },
  computed: {
    statusType() {
      if (this.isCorrect === true) return 'success'
      if (this.isCorrect === false) return 'error'
      if (this.showHint) return 'hint'
      return 'info'
    },
    
    showStatus() {
      return this.isCorrect !== null || this.showHint
    }
  },
  watch: {
    currentQuestion: {
      handler(newQuestion) {
        if (newQuestion) {
          this.showWelcomeMessage()
        }
      },
      immediate: true
    },
    questionIndex: {
      handler(newIndex) {
        this.updateProgressMessage(newIndex)
      }
    },
    showAnswer: {
      handler(newShowAnswer) {
        if (newShowAnswer && this.isCorrect !== null) {
          // 只有在显示答案且知道正确性时才反应
          this.showResultReaction(this.isCorrect)
        }
      }
    },
    userAnswer: {
      handler(newAnswer) {
        if (newAnswer !== null && newAnswer !== '' && !this.showAnswer) {
          // 用户选择了答案，但不显示结果，只是轻微反应
          this.showFloatingTip = true
          this.tipType = 'answer'
          this.tipIcon = '⚡'
          this.floatingTip = '答案已选择，点击提交查看结果'
          
          setTimeout(() => {
            this.showFloatingTip = false
          }, 2000)
        }
      }
    },
    isCorrect: {
      handler(newCorrect) {
        // 移除这里的自动反应，改为在showAnswer变化时处理
      }
    },
    showHint: {
      handler(newShowHint) {
        if (newShowHint) {
          this.showHintMessage()
        }
      }
    }
  },
  mounted() {
    this.startBlinking()
    this.showWelcomeMessage()
  },
  methods: {
    toggleGuide() {
      this.isActive = !this.isActive
      if (this.isActive) {
        this.showGeneralTips()
      } else {
        this.hideDialog()
      }
    },
    
    // 测试表情的方法（开发时使用）
    testExpression(expression) {
      this.mouthExpression = expression
      console.log('测试表情:', expression)
    },
    
    // 测试正确率计算
    testAccuracy() {
      console.log('=== 正确率测试 ===')
      console.log('问题历史:', this.questionHistory)
      console.log('总答对数:', this.totalCorrect)
      console.log('总答错数:', this.totalWrong)
      console.log('连续答对:', this.consecutiveCorrect)
      console.log('连续答错:', this.consecutiveWrong)
      console.log('最近5题正确率:', this.getRecentAccuracy())
      console.log('整体正确率:', this.getOverallAccuracy())
    },
    
    startBlinking() {
      setInterval(() => {
        this.isBlinking = true
        setTimeout(() => {
          this.isBlinking = false
        }, 200)
      }, 3000)
    },
    
    showWelcomeMessage() {
      this.dialogType = 'welcome'
      this.dialogIcon = '⚡'
      this.dialogTitle = '欢迎来到忍术训练！'
      this.dialogMessage = `我是佐助，你的忍术导师。我会指导你完成这${this.totalQuestions}道训练题目。准备好了吗？`
      this.showDialog = true
      this.isWaving = true
      
      setTimeout(() => {
        this.isWaving = false
        this.hideDialog()
      }, 4000)
    },
    
    updateProgressMessage(index) {
      const progress = ((index + 1) / this.totalQuestions * 100).toFixed(0)
      const overallAccuracy = this.getOverallAccuracy()
      
      this.showFloatingTip = true
      this.tipType = 'progress'
      
      if (progress >= 80 && overallAccuracy >= 0.8) {
        this.tipIcon = '⚡'
        this.floatingTip = `优秀！进度${progress}%，正确率${(overallAccuracy * 100).toFixed(0)}%`
      } else if (progress >= 60 && overallAccuracy >= 0.6) {
        this.tipIcon = '⚡'
        this.floatingTip = `很好！进度${progress}%，正确率${(overallAccuracy * 100).toFixed(0)}%`
      } else if (progress >= 40) {
        this.tipIcon = '⚡'
        this.floatingTip = `进度：${progress}% (${index + 1}/${this.totalQuestions})`
      } else {
        this.tipIcon = '⚡'
        this.floatingTip = `开始忍术训练！进度${progress}%`
      }
      
      setTimeout(() => {
        this.showFloatingTip = false
      }, 2500)
    },
    
    // 移除reactToAnswer方法，因为用户选择答案时不应该立即反应
    
    showResultReaction(isCorrect) {
      // 防止重复反应
      const now = Date.now()
      if (now - this.lastReactionTime < 1000) return
      this.lastReactionTime = now
      
      // 先更新统计数据
      this.updateStats(isCorrect)
      
      // 然后根据更新后的数据计算正确率
      if (isCorrect) {
        this.handleCorrectAnswer()
      } else {
        this.handleWrongAnswer()
      }
      
      this.showDialog = true
      
      setTimeout(() => {
        this.isWaving = false
        this.isPointing = false
        // 根据用户心情保持表情，而不是强制重置为normal
        if (this.userMood === 'excited') {
          this.mouthExpression = 'excited'
          console.log('保持表情: excited')
        } else if (this.userMood === 'happy') {
          this.mouthExpression = 'happy'
          console.log('保持表情: happy')
        } else if (this.userMood === 'sad') {
          this.mouthExpression = 'sad'
          console.log('保持表情: sad')
        } else if (this.userMood === 'worried') {
          this.mouthExpression = 'sad'
          console.log('保持表情: sad (worried)')
        } else {
          this.mouthExpression = 'normal'
          console.log('保持表情: normal')
        }
        this.hideDialog()
      }, 4000)
    },
    
    updateStats(isCorrect) {
      if (isCorrect) {
        this.consecutiveCorrect++
        this.consecutiveWrong = 0
        this.totalCorrect++
      } else {
        this.consecutiveWrong++
        this.consecutiveCorrect = 0
        this.totalWrong++
      }
      
      // 记录答题历史
      this.questionHistory.push({
        correct: isCorrect,
        timestamp: Date.now()
      })
      
      // 保持历史记录在合理范围内
      if (this.questionHistory.length > 10) {
        this.questionHistory.shift()
      }
    },
    
    handleCorrectAnswer() {
      const overallAccuracy = this.getOverallAccuracy()
      
      if (this.consecutiveCorrect >= 5) {
        // 连续答对5题以上
        this.dialogType = 'success'
        this.dialogIcon = '⚡'
        this.dialogTitle = '太厉害了！'
        this.dialogMessage = `连续答对${this.consecutiveCorrect}题！你简直就是忍术天才！继续保持这个状态！`
        this.mouthExpression = 'excited'
        this.userMood = 'excited'
        this.isWaving = true
        console.log('设置表情: excited')
      } else if (this.consecutiveCorrect >= 3) {
        // 连续答对3-4题
        this.dialogType = 'success'
        this.dialogIcon = '⚡'
        this.dialogTitle = '太棒了！'
        this.dialogMessage = `连续答对${this.consecutiveCorrect}题！你的忍术修炼状态很棒，继续保持！`
        this.mouthExpression = 'happy'
        this.userMood = 'happy'
        this.isWaving = true
        console.log('设置表情: happy')
      } else if (overallAccuracy >= 0.8) {
        // 整体正确率很高
        this.dialogType = 'success'
        this.dialogIcon = '⚡'
        this.dialogTitle = '回答正确！'
        this.dialogMessage = '答对了！你的正确率很高，说明你掌握得很好！'
        this.mouthExpression = 'happy'
        this.userMood = 'happy'
        console.log('设置表情: happy')
      } else {
        // 普通答对
        this.dialogType = 'success'
        this.dialogIcon = '⚡'
        this.dialogTitle = '答对了！'
        this.dialogMessage = this.getRandomCorrectMessage()
        this.mouthExpression = 'happy'
        this.userMood = 'happy'
        console.log('设置表情: happy')
      }
    },
    
    handleWrongAnswer() {
      const overallAccuracy = this.getOverallAccuracy()
      
      if (this.consecutiveWrong >= 3) {
        // 连续答错3题以上
        this.dialogType = 'error'
        this.dialogIcon = '⚡'
        this.dialogTitle = '别灰心！'
        this.dialogMessage = `连续答错${this.consecutiveWrong}题了，没关系！让我来帮你分析一下，我们一起找出问题所在。`
        this.mouthExpression = 'sad'
        this.userMood = 'worried'
        this.isPointing = true
        console.log('设置表情: sad (连续答错)')
      } else if (overallAccuracy < 0.5) {
        // 整体正确率较低
        this.dialogType = 'error'
        this.dialogIcon = '⚡'
        this.dialogTitle = '加油！'
        this.dialogMessage = '答错了，但不要紧！忍术修炼是一个过程，每个错误都是变强的机会。我们一起努力！'
        this.mouthExpression = 'sad'
        this.userMood = 'worried'
        this.isPointing = true
        console.log('设置表情: sad (正确率低)')
      } else {
        // 普通答错
        this.dialogType = 'error'
        this.dialogIcon = '⚡'
        this.dialogTitle = '没关系！'
        this.dialogMessage = this.getRandomWrongMessage()
        this.mouthExpression = 'sad'
        this.userMood = 'sad'
        console.log('设置表情: sad (普通答错)')
      }
    },
    
    getRecentAccuracy() {
      if (this.questionHistory.length === 0) return 0
      const recent = this.questionHistory.slice(-5) // 最近5题
      const correct = recent.filter(q => q.correct).length
      const accuracy = correct / recent.length
      console.log('正确率计算:', {
        totalHistory: this.questionHistory.length,
        recentQuestions: recent.length,
        correctCount: correct,
        accuracy: accuracy
      })
      return accuracy
    },
    
    getOverallAccuracy() {
      if (this.questionHistory.length === 0) return 0
      const totalCorrect = this.questionHistory.filter(q => q.correct).length
      const accuracy = totalCorrect / this.questionHistory.length
      console.log('整体正确率:', {
        totalQuestions: this.questionHistory.length,
        totalCorrect: totalCorrect,
        accuracy: accuracy
      })
      return accuracy
    },
    
    getRandomCorrectMessage() {
      const messages = [
        '答对了！你的忍术天赋不错！',
        '回答正确！继续保持这种状态！',
        '答对了！你的理解很到位！',
        '回答正确！忍术修炼有进步！',
        '答对了！你掌握得很好！',
        '回答正确！不愧是我的学生！',
        '答对了！你的思路很清晰！',
        '回答正确！继续保持这个状态！',
        '答对了！你的实力在增长！',
        '回答正确！这就是忍者的觉悟！'
      ]
      return messages[Math.floor(Math.random() * messages.length)]
    },
    
    getRandomWrongMessage() {
      const messages = [
        '答错了，但没关系！每个错误都是变强的机会。',
        '答错了，不要紧！我们一起找出正确答案。',
        '答错了，但你已经很努力了！继续修炼！',
        '答错了，没关系！忍术修炼就是在错误中进步的。',
        '答错了，但不要灰心！你一定能行的！',
        '答错了，没关系！我们一起修炼，一起变强！',
        '答错了，但你已经很棒了！继续努力！',
        '答错了，不要紧！错误是成功路上的垫脚石。',
        '答错了，但你的意志很坚强！继续加油！',
        '答错了，没关系！这就是忍者的修行！'
      ]
      return messages[Math.floor(Math.random() * messages.length)]
    },
    
    showHintMessage() {
      const overallAccuracy = this.getOverallAccuracy()
      const consecutiveWrong = this.consecutiveWrong
      
      this.dialogType = 'hint'
      this.dialogIcon = '⚡'
      this.dialogTitle = '忍术提示'
      
      if (consecutiveWrong >= 3) {
        this.dialogMessage = '看起来你遇到了一些困难。让我们一起来分析：1. 仔细阅读题目 2. 找出关键词 3. 排除明显错误的选项 4. 相信你的直觉！'
        this.userMood = 'worried'
        this.mouthExpression = 'sad'
      } else if (overallAccuracy < 0.6) {
        this.dialogMessage = '你的忍术修炼还有提升空间。建议：1. 放慢答题速度 2. 仔细思考每个选项 3. 回顾相关知识点 4. 不要着急，慢慢来！'
        this.userMood = 'worried'
        this.mouthExpression = 'sad'
      } else {
        this.dialogMessage = this.getRandomHintMessage()
        this.userMood = 'neutral'
        this.mouthExpression = 'normal'
      }
      
      this.showDialog = true
      this.isPointing = true
      
      setTimeout(() => {
        this.isPointing = false
        this.hideDialog()
      }, 4000)
    },
    
    getRandomHintMessage() {
      const hints = [
        '仔细阅读题目，注意关键词，相信你一定能找到正确答案！',
        '回想一下相关的知识点，答案就在你的脑海中！',
        '不要被表面现象迷惑，深入思考题目的本质！',
        '仔细分析每个选项，排除法是个好方法！',
        '相信你的直觉，但也要仔细验证！',
        '题目中的每个词都很重要，不要忽略细节！',
        '把题目读两遍，确保理解完全正确！',
        '冷静思考，答案就在眼前！',
        '专注你的查克拉，答案会自然显现！',
        '用写轮眼仔细观察每个选项！'
      ]
      return hints[Math.floor(Math.random() * hints.length)]
    },
    
        showGeneralTips() {
      const overallAccuracy = this.getOverallAccuracy()
      const totalAnswered = this.questionHistory.length
      
      this.dialogType = 'info'
      this.dialogIcon = '⚡'
      this.dialogTitle = '忍术指导'
      
      if (totalAnswered === 0) {
        this.dialogMessage = '准备好开始忍术训练了吗？记住要专注，相信你的实力！'
      } else if (overallAccuracy >= 0.9) {
        this.dialogMessage = '太棒了！你的忍术天赋很高！继续保持这个状态，你简直就是天才忍者！'
      } else if (overallAccuracy >= 0.7) {
        this.dialogMessage = '你的忍术修炼状态很好！继续保持，相信你还能变得更强！'
      } else if (overallAccuracy >= 0.5) {
        this.dialogMessage = '你的表现不错！多练习一下，忍术水平会更高的！'
      } else if (this.consecutiveWrong >= 2) {
        this.dialogMessage = '看起来你遇到了一些困难。不要着急，慢慢来，我们一起找出问题所在！'
      } else {
        this.dialogMessage = this.getRandomEncouragement()
      }
      
      this.showDialog = true
    },
    
    getRandomEncouragement() {
      const encouragements = [
        '记住要专注，不要分心！',
        '不确定的时候可以多思考',
        '相信自己，你有忍者的潜力！',
        '忍术修炼是一个渐进的过程',
        '每个错误都是变强的机会',
        '加油！你一定能成为强大的忍者！',
        '慢慢来，不要着急～',
        '你已经做得很好了！',
        '修炼需要耐心，你已经很棒了！',
        '每一次训练都是进步！',
        '不要害怕失败，失败是成功的一部分！',
        '你的努力一定会让你变得更强！'
      ]
      return encouragements[Math.floor(Math.random() * encouragements.length)]
    },
    
    hideDialog() {
      this.showDialog = false
    },
    
    resetStats() {
      this.consecutiveCorrect = 0
      this.consecutiveWrong = 0
      this.totalCorrect = 0
      this.totalWrong = 0
      this.questionHistory = []
      this.userMood = 'neutral'
      this.lastReactionTime = 0
    },
    
    showQuizSummary() {
      const totalAnswered = this.questionHistory.length
      const overallAccuracy = this.getOverallAccuracy()
      const accuracy = (overallAccuracy * 100).toFixed(1)
      
      this.dialogType = 'info'
      this.dialogIcon = '⚡'
      this.dialogTitle = '训练总结'
      
      if (accuracy >= 90) {
        this.dialogMessage = `太棒了！你的正确率是${accuracy}%，表现非常优秀！你简直就是忍术天才！`
        this.mouthExpression = 'excited'
      } else if (accuracy >= 80) {
        this.dialogMessage = `很好！你的正确率是${accuracy}%，表现很棒！继续保持这个状态！`
        this.mouthExpression = 'happy'
      } else if (accuracy >= 60) {
        this.dialogMessage = `不错！你的正确率是${accuracy}%，还有提升空间，继续加油！`
        this.mouthExpression = 'happy'
      } else {
        this.dialogMessage = `你的正确率是${accuracy}%，不要灰心！忍术修炼是一个过程，多练习一定会进步的！`
        this.mouthExpression = 'sad'
      }
      
      this.showDialog = true
      this.isWaving = true
      
      setTimeout(() => {
        this.isWaving = false
        this.hideDialog()
      }, 5000)
    }
  }
}
</script>

<style lang="scss" scoped>


.quiz-guide {
  position: fixed;
  left: 2rem;
  bottom: 2rem;
  z-index: 1000;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  
  &.guide-hidden {
    transform: translateX(-120%) scale(0.8);
    opacity: 0;
  }
  
  &.guide-active {
    .guide-character {
      transform: scale(1.1);
      
      .character-container {
        animation: bounce 0.6s ease;
      }
    }
  }
}

.guide-character {
  position: relative;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  filter: drop-shadow(0 8px 32px rgba(0, 0, 0, 0.15));
  
  &:hover {
    transform: scale(1.05) translateY(-2px);
    filter: drop-shadow(0 12px 40px rgba(0, 0, 0, 0.2));
  }
}

.character-container {
  width: 90px;
  height: 120px;
  position: relative;
}

.character-main {
  width: 100%;
  height: 100%;
  position: relative;
  transform-origin: center bottom;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  filter: drop-shadow(0 8px 24px rgba(0, 0, 0, 0.15));
}

// 现代头部设计
.character-head {
  width: 80px;
  height: 80px;
  position: relative;
  margin: 0 auto;
  z-index: 10;
}

// 全新可爱头发设计
.hair-layer {
  position: absolute;
  top: -12px;
  left: -15px;
  width: 110px;
  height: 75px;
  z-index: 1;
}

.hair-main {
  width: 100%;
  height: 60px;
  background: linear-gradient(135deg, #ff9ff3 0%, #f368e0 25%, #ff6b9d 50%, #feca57 75%, #ff9ff3 100%);
  border-radius: 70px 70px 30px 30px;
  position: relative;
  box-shadow: 
    inset 0 -8px 16px rgba(255, 159, 243, 0.3),
    0 8px 24px rgba(255, 107, 157, 0.4),
    0 0 0 3px rgba(255, 255, 255, 0.6);
  animation: hairFloat 3s ease-in-out infinite;
  
  &::before {
    content: '';
    position: absolute;
    top: 5px;
    left: 15px;
    width: 80px;
    height: 45px;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.4) 0%, rgba(255, 159, 243, 0.6) 50%, transparent 100%);
    border-radius: 60px 60px 25px 25px;
    filter: blur(1px);
  }
  
  &::after {
    content: '';
    position: absolute;
    top: 8px;
    right: 20px;
    width: 25px;
    height: 25px;
    background: linear-gradient(135deg, #feca57 0%, #ff9ff3 100%);
    border-radius: 50%;
    opacity: 0.7;
    animation: hairSparkle 2s ease-in-out infinite;
  }
}

.hair-highlight {
  position: absolute;
  top: 15px;
  left: 25px;
  width: 50px;
  height: 25px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.8) 0%, rgba(255, 255, 255, 0.4) 50%, transparent 100%);
  border-radius: 50px 50px 20px 20px;
  filter: blur(0.5px);
  animation: shimmer 2.5s ease-in-out infinite;
  
  &::before {
    content: '✨';
    position: absolute;
    top: -5px;
    right: 5px;
    font-size: 8px;
    opacity: 0.8;
    animation: twinkle 1.5s ease-in-out infinite;
  }
}

.hair-strand {
  position: absolute;
  width: 18px;
  height: 30px;
  background: linear-gradient(135deg, #ff6b9d 0%, #f368e0 50%, #ff9ff3 100%);
  border-radius: 0 0 20px 20px;
  box-shadow: 
    0 3px 8px rgba(255, 107, 157, 0.4),
    inset 0 2px 4px rgba(255, 255, 255, 0.3);
  
  &.left {
    top: 35px;
    left: -8px;
    transform: rotate(-25deg);
    animation: strandSway 3s ease-in-out infinite;
    
    &::before {
      content: '';
      position: absolute;
      top: 5px;
      left: 3px;
      width: 8px;
      height: 15px;
      background: linear-gradient(135deg, rgba(255, 255, 255, 0.5) 0%, transparent 100%);
      border-radius: 0 0 10px 10px;
    }
  }
  
  &.right {
    top: 35px;
    right: -8px;
    transform: rotate(25deg);
    animation: strandSway 3s ease-in-out infinite reverse;
    
    &::before {
      content: '';
      position: absolute;
      top: 5px;
      right: 3px;
      width: 8px;
      height: 15px;
      background: linear-gradient(135deg, rgba(255, 255, 255, 0.5) 0%, transparent 100%);
      border-radius: 0 0 10px 10px;
    }
  }
}

.hair-accessory {
  position: absolute;
  top: 20px;
  right: 18px;
  width: 16px;
  height: 16px;
  background: linear-gradient(135deg, #feca57 0%, #ff9ff3 50%, #f368e0 100%);
  border-radius: 50%;
  box-shadow: 
    0 0 0 3px rgba(255, 255, 255, 0.9),
    0 3px 12px rgba(254, 202, 87, 0.5),
    0 0 20px rgba(255, 159, 243, 0.3);
  animation: accessoryTwinkle 2s ease-in-out infinite;
  
  &::before {
    content: '🌟';
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 10px;
    animation: starRotate 3s linear infinite;
  }
  
  &::after {
    content: '';
    position: absolute;
    top: -3px;
    left: -3px;
    width: 22px;
    height: 22px;
    border: 2px solid rgba(255, 159, 243, 0.6);
    border-radius: 50%;
    animation: ringPulse 2s ease-in-out infinite;
  }
}

// 全新可爱脸部设计
.face {
  width: 75px;
  height: 75px;
  background: linear-gradient(135deg, #ffe4e1 0%, #ffb3ba 30%, #ffdfba 60%, #ffffba 100%);
  border-radius: 50%;
  position: relative;
  top: 18px;
  left: 2px;
  border: 4px solid rgba(255, 255, 255, 0.95);
  box-shadow: 
    0 8px 32px rgba(255, 179, 186, 0.3),
    inset 0 4px 8px rgba(255, 255, 255, 0.4),
    0 0 0 2px rgba(255, 182, 193, 0.2);
  z-index: 5;
  
  &::before {
    content: '';
    position: absolute;
    top: 12px;
    left: 15px;
    width: 45px;
    height: 35px;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, rgba(255, 255, 255, 0.2) 50%, transparent 100%);
    border-radius: 50%;
    filter: blur(1px);
  }
  
  &::after {
    content: '';
    position: absolute;
    top: 50px;
    left: 50%;
    transform: translateX(-50%);
    width: 20px;
    height: 8px;
    background: linear-gradient(135deg, rgba(255, 182, 193, 0.4) 0%, transparent 100%);
    border-radius: 50%;
    filter: blur(2px);
  }
}

// 全新可爱眼睛容器
.eyes-container {
  position: absolute;
  top: 25px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 18px;
  z-index: 10;
}

.eye {
  width: 24px;
  height: 24px;
  position: relative;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  filter: drop-shadow(0 2px 8px rgba(255, 182, 193, 0.3));
  
  &.blink {
    .eye-white {
      height: 4px;
      border-radius: 10px;
      transform: scaleY(0.2);
    }
    
    .eye-iris,
    .eye-pupil,
    .eye-shine {
      opacity: 0;
    }
    
    .eyelash {
      transform: translateX(-50%) scaleY(0.3);
    }
  }
  
  &.happy {
    transform: scale(1.2) rotate(-2deg);
    
    .eye-white {
      border-radius: 50% 50% 80% 80%;
      background: linear-gradient(135deg, #ffffff 0%, #fff5f5 100%);
    }
    
    .eye-iris {
      background: linear-gradient(135deg, #ff9ff3 0%, #f368e0 50%, #ff6b9d 100%);
      transform: translate(-50%, -50%) scale(1.15);
      box-shadow: 0 0 12px rgba(255, 159, 243, 0.4);
    }
    
    .eye-shine.primary {
      animation: happyShine 1s ease-in-out infinite;
      background: radial-gradient(circle, rgba(255, 255, 255, 0.95) 0%, rgba(255, 255, 255, 0.7) 100%);
    }
  }
  
  &.excited {
    transform: scale(1.3);
    
    .eye-iris {
      background: linear-gradient(135deg, #feca57 0%, #ff9ff3 50%, #f368e0 100%);
      animation: excitedPulse 0.8s ease-in-out infinite;
      box-shadow: 0 0 16px rgba(254, 202, 87, 0.5);
    }
    
    .eye-shine {
      animation: excitedSparkle 0.6s ease-in-out infinite;
      background: radial-gradient(circle, rgba(255, 255, 255, 1) 0%, rgba(254, 202, 87, 0.3) 100%);
    }
  }
  
  &.sad {
    transform: scale(0.95) rotate(5deg);
    
    .eye-iris {
      background: linear-gradient(135deg, #a8e6cf 0%, #88d8c0 50%, #74b9ff 100%);
      transform: translate(-50%, -35%);
      box-shadow: 0 0 8px rgba(168, 230, 207, 0.4);
    }
    
    .eye-white {
      border-radius: 50% 50% 65% 65%;
      background: linear-gradient(135deg, #ffffff 0%, #f0f8ff 100%);
    }
  }
}

.eye-white {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #ffffff 0%, #fffafa 50%, #fff0f5 100%);
  border-radius: 50%;
  border: 2px solid rgba(255, 182, 193, 0.3);
  position: relative;
  transition: all 0.4s ease;
  overflow: hidden;
  box-shadow: 
    inset 0 3px 6px rgba(255, 255, 255, 0.8),
    inset 0 -2px 4px rgba(255, 182, 193, 0.1),
    0 2px 8px rgba(255, 182, 193, 0.2);
}

.eye-iris {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 14px;
  height: 14px;
  background: linear-gradient(135deg, #ff9ff3 0%, #f368e0 30%, #ff6b9d 70%, #feca57 100%);
  border-radius: 50%;
  transition: all 0.4s ease;
  box-shadow: 
    0 0 12px rgba(255, 159, 243, 0.4),
    inset 0 2px 4px rgba(255, 255, 255, 0.3),
    inset 0 -2px 4px rgba(243, 104, 224, 0.3);
  
  &::before {
    content: '';
    position: absolute;
    top: 2px;
    left: 2px;
    width: 6px;
    height: 6px;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, transparent 100%);
    border-radius: 50%;
    filter: blur(0.5px);
  }
}

.eye-pupil {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 6px;
  height: 6px;
  background: radial-gradient(circle, #2d3436 0%, #636e72 70%, #74b9ff 100%);
  border-radius: 50%;
  transition: all 0.3s ease;
  box-shadow: 0 0 4px rgba(45, 52, 54, 0.5);
  
  &::before {
    content: '';
    position: absolute;
    top: 1px;
    left: 1px;
    width: 2px;
    height: 2px;
    background: rgba(255, 255, 255, 0.8);
    border-radius: 50%;
  }
}

.eye-shine {
  position: absolute;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.95) 0%, rgba(255, 255, 255, 0.7) 100%);
  transition: all 0.3s ease;
  box-shadow: 0 0 6px rgba(255, 255, 255, 0.8);
  
  &.primary {
    top: 2px;
    left: 2px;
    width: 5px;
    height: 5px;
    animation: eyeShine 2s ease-in-out infinite;
  }
  
  &.secondary {
    top: 8px;
    right: 2px;
    width: 3px;
    height: 3px;
    opacity: 0.7;
    animation: eyeShine 2s ease-in-out infinite 0.5s;
    background: radial-gradient(circle, rgba(255, 255, 255, 0.8) 0%, rgba(255, 182, 193, 0.3) 100%);
  }
}

.eyelash {
  position: absolute;
  top: -4px;
  left: 50%;
  transform: translateX(-50%);
  width: 16px;
  height: 6px;
  transition: all 0.3s ease;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 1px;
    width: 4px;
    height: 6px;
    background: linear-gradient(135deg, #ff6b9d 0%, #f368e0 100%);
    border-radius: 50% 50% 0 0;
    transform: rotate(-30deg);
    box-shadow: 0 1px 3px rgba(255, 107, 157, 0.3);
  }
  
  &::after {
    content: '';
    position: absolute;
    top: 0;
    right: 1px;
    width: 4px;
    height: 6px;
    background: linear-gradient(135deg, #ff6b9d 0%, #f368e0 100%);
    border-radius: 50% 50% 0 0;
    transform: rotate(30deg);
    box-shadow: 0 1px 3px rgba(255, 107, 157, 0.3);
  }
}

// 全新可爱眉毛设计
.eyebrows {
  position: absolute;
  top: 8px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 10px;
  display: flex;
  justify-content: space-between;
  z-index: 8;
}

.eyebrow {
  width: 20px;
  height: 5px;
  background: linear-gradient(135deg, #ff6b9d 0%, #f368e0 50%, #ff9ff3 100%);
  border-radius: 50px;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  box-shadow: 0 2px 6px rgba(255, 107, 157, 0.3);
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    top: 1px;
    left: 2px;
    width: 12px;
    height: 2px;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, transparent 100%);
    border-radius: 50px;
    filter: blur(0.5px);
  }
  
  &.happy {
    transform: rotate(-10deg) translateY(3px) scale(1.05);
    background: linear-gradient(135deg, #feca57 0%, #ff9ff3 50%, #f368e0 100%);
  }
  
  &.excited {
    transform: rotate(-15deg) translateY(4px) scale(1.15);
    background: linear-gradient(135deg, #feca57 0%, #ff9ff3 30%, #f368e0 70%, #ff6b9d 100%);
    animation: eyebrowBounce 0.8s ease-in-out infinite;
  }
  
  &.sad {
    transform: rotate(15deg) translateY(-2px) scale(0.9);
    background: linear-gradient(135deg, #a8e6cf 0%, #88d8c0 50%, #74b9ff 100%);
  }
  
  &.worried {
    transform: rotate(20deg) translateY(-3px) scale(0.95);
    background: linear-gradient(135deg, #fdcb6e 0%, #e17055 50%, #d63031 100%);
    animation: eyebrowWorry 1.5s ease-in-out infinite;
  }
  
  &.right-eyebrow {
    &.happy {
      transform: rotate(10deg) translateY(3px) scale(1.05);
    }
    
    &.excited {
      transform: rotate(15deg) translateY(4px) scale(1.15);
    }
    
    &.sad {
      transform: rotate(-15deg) translateY(-2px) scale(0.9);
    }
    
    &.worried {
      transform: rotate(-20deg) translateY(-3px) scale(0.95);
    }
  }
}

// 全新可爱鼻子设计
.nose {
  position: absolute;
  top: 38px;
  left: 50%;
  transform: translateX(-50%);
  width: 4px;
  height: 5px;
  background: linear-gradient(135deg, rgba(255, 182, 193, 0.6) 0%, rgba(255, 107, 157, 0.4) 50%, rgba(243, 104, 224, 0.3) 100%);
  border-radius: 50% 50% 80% 80%;
  z-index: 6;
  box-shadow: 
    0 1px 3px rgba(255, 182, 193, 0.3),
    inset 0 1px 2px rgba(255, 255, 255, 0.5);
  
  &::before {
    content: '';
    position: absolute;
    top: 1px;
    left: 50%;
    transform: translateX(-50%);
    width: 2px;
    height: 2px;
    background: rgba(255, 255, 255, 0.7);
    border-radius: 50%;
    filter: blur(0.5px);
  }
}

// 全新可爱脸颊红晕
.cheeks {
  position: absolute;
  top: 35px;
  left: 0;
  right: 0;
  opacity: 0;
  transition: all 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  z-index: 4;
  
  &.show {
    opacity: 1;
    animation: cheekGlow 2.5s ease-in-out infinite;
  }
}

.cheek {
  width: 12px;
  height: 10px;
  background: linear-gradient(135deg, #ff9ff3 0%, #f368e0 30%, #ff6b9d 70%, #feca57 100%);
  border-radius: 50%;
  position: absolute;
  box-shadow: 
    0 0 12px rgba(255, 159, 243, 0.5),
    inset 0 2px 4px rgba(255, 255, 255, 0.3);
  filter: blur(1px);
  
  &::before {
    content: '';
    position: absolute;
    top: 2px;
    left: 2px;
    width: 6px;
    height: 4px;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, transparent 100%);
    border-radius: 50%;
    filter: blur(0.5px);
  }
  
  &.left-cheek {
    left: 4px;
    animation: cheekPulse 2s ease-in-out infinite;
  }
  
  &.right-cheek {
    right: 4px;
    animation: cheekPulse 2s ease-in-out infinite 0.3s;
  }
}

// 全新可爱嘴巴容器
.mouth-container {
  position: absolute;
  bottom: 15px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 10;
}

.mouth {
  width: 18px;
  height: 7px;
  border: 3px solid rgba(255, 107, 157, 0.8);
  border-top: none;
  border-radius: 0 0 18px 18px;
  transition: all 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  background: linear-gradient(135deg, #ff9ff3 0%, #f368e0 50%, #ff6b9d 100%);
  box-shadow: 
    0 3px 8px rgba(255, 159, 243, 0.4),
    inset 0 2px 4px rgba(255, 255, 255, 0.3);
  
  &::before {
    content: '';
    position: absolute;
    top: 1px;
    left: 2px;
    right: 2px;
    height: 3px;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, transparent 100%);
    border-radius: 0 0 14px 14px;
    filter: blur(0.5px);
  }
  
  &.normal {
    width: 16px;
    height: 6px;
    border-radius: 0 0 16px 16px;
    background: linear-gradient(135deg, #ffb3ba 0%, #ff9ff3 50%, #f368e0 100%);
  }
  
  &.happy {
    border-radius: 25px 25px 0 0;
    border-bottom: none;
    border-top: 3px solid rgba(254, 202, 87, 0.8);
    height: 15px;
    width: 25px;
    transform: scale(1.15);
    background: linear-gradient(135deg, #feca57 0%, #ff9ff3 50%, #f368e0 100%);
    box-shadow: 
      0 4px 12px rgba(254, 202, 87, 0.5),
      inset 0 3px 6px rgba(255, 255, 255, 0.4);
    
    &::before {
      content: '';
      position: absolute;
      top: 3px;
      left: 3px;
      right: 3px;
      height: 8px;
      background: linear-gradient(135deg, #fff3cd 0%, #feca57 50%, #ff9ff3 100%);
      border-radius: 20px 20px 0 0;
      filter: none;
    }
    
    &::after {
      content: '😊';
      position: absolute;
      top: -20px;
      left: 50%;
      transform: translateX(-50%);
      font-size: 8px;
      opacity: 0.8;
      animation: floatUp 2s ease-in-out infinite;
    }
  }
  
  &.excited {
    border-radius: 30px 30px 0 0;
    border-bottom: none;
    border-top: 3px solid rgba(254, 202, 87, 0.8);
    height: 20px;
    width: 30px;
    transform: scale(1.25);
    background: linear-gradient(135deg, #feca57 0%, #ff9ff3 30%, #f368e0 70%, #ff6b9d 100%);
    box-shadow: 
      0 6px 16px rgba(254, 202, 87, 0.6),
      inset 0 4px 8px rgba(255, 255, 255, 0.5);
    animation: mouthExcited 0.8s ease-in-out infinite;
    
    &::before {
      content: '';
      position: absolute;
      top: 4px;
      left: 4px;
      right: 4px;
      height: 12px;
      background: linear-gradient(135deg, #fff3cd 0%, #feca57 30%, #ff9ff3 70%, #f368e0 100%);
      border-radius: 25px 25px 0 0;
      filter: none;
    }
    
    &::after {
      content: '🎉';
      position: absolute;
      top: -25px;
      left: 50%;
      transform: translateX(-50%);
      font-size: 10px;
      animation: celebrateFloat 1s ease-in-out infinite;
    }
  }
  
  &.sad {
    border-radius: 0 0 20px 20px;
    border-top: none;
    border-bottom: 3px solid rgba(168, 230, 207, 0.8);
    height: 10px;
    width: 18px;
    transform: scale(0.95) rotate(180deg);
    background: linear-gradient(135deg, #a8e6cf 0%, #88d8c0 50%, #74b9ff 100%);
    box-shadow: 
      0 3px 8px rgba(168, 230, 207, 0.4),
      inset 0 2px 4px rgba(255, 255, 255, 0.3);
    
    &::after {
      content: '💧';
      position: absolute;
      bottom: -25px;
      left: 50%;
      transform: translateX(-50%) rotate(180deg);
      font-size: 8px;
      opacity: 0.7;
      animation: tearDrop 2s ease-in-out infinite;
    }
  }
  
  &.worried {
    border-radius: 0 0 16px 16px;
    border-top: none;
    height: 6px;
    width: 14px;
    background: linear-gradient(135deg, #fdcb6e 0%, #e17055 50%, #d63031 100%);
    border-color: rgba(253, 203, 110, 0.8);
    transform: scale(0.9);
    
    &::after {
      content: '😰';
      position: absolute;
      top: -18px;
      left: 50%;
      transform: translateX(-50%);
      font-size: 6px;
      opacity: 0.6;
      animation: worry 1.5s ease-in-out infinite;
    }
  }
}

// 现代身体设计
.character-body {
  width: 70px;
  height: 85px;
  position: relative;
  top: -8px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 3;
}

.body-main {
  width: 100%;
  height: 65px;
  background: linear-gradient(135deg, #6c5ce7 0%, #a29bfe 50%, #74b9ff 100%);
  border-radius: 35px 35px 25px 25px;
  position: relative;
  border: 3px solid rgba(255, 255, 255, 0.9);
  box-shadow: 
    0 10px 30px rgba(108, 92, 231, 0.3),
    inset 0 3px 6px rgba(255, 255, 255, 0.3);
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 5px;
    left: 10px;
    width: 50px;
    height: 40px;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.2) 0%, transparent 70%);
    border-radius: 30px 30px 20px 20px;
    filter: blur(2px);
  }
}

.body-pattern {
  position: absolute;
  top: 18px;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 25px;
  background: linear-gradient(135deg, #fd79a8 0%, #fdcb6e 100%);
  border-radius: 20px;
  border: 2px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 4px 12px rgba(253, 121, 168, 0.3);
  
  &::before {
    content: '⭐';
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 12px;
    animation: patternTwinkle 2s ease-in-out infinite;
  }
}

.body-shadow {
  position: absolute;
  bottom: -5px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 8px;
  background: radial-gradient(ellipse, rgba(0, 0, 0, 0.2) 0%, transparent 70%);
  border-radius: 50%;
  filter: blur(3px);
}

// 现代手臂容器
.arms-container {
  position: absolute;
  top: 18px;
  left: -12px;
  right: -12px;
  height: 50px;
  display: flex;
  justify-content: space-between;
  z-index: 2;
}

.arm {
  position: relative;
  transform-origin: top center;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  
  &.wave {
    animation: armWave 1.2s ease-in-out infinite;
  }
  
  &.point {
    transform: rotate(-35deg);
    
    &.right-arm {
      transform: rotate(35deg);
    }
  }
  
  &.thinking {
    transform: rotate(-20deg) translateY(-5px);
    
    .hand {
      transform: rotate(45deg);
    }
  }
  
  &.celebrate {
    animation: celebrate 0.8s ease-in-out infinite;
    
    &.right-arm {
      animation: celebrate 0.8s ease-in-out infinite 0.2s;
    }
  }
}

.arm-upper {
  width: 12px;
  height: 22px;
  background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%);
  border-radius: 10px;
  border: 2px solid rgba(255, 255, 255, 0.8);
  position: relative;
  box-shadow: 0 2px 6px rgba(253, 203, 110, 0.3);
}

.arm-lower {
  width: 10px;
  height: 18px;
  background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%);
  border-radius: 8px;
  border: 2px solid rgba(255, 255, 255, 0.8);
  position: relative;
  top: -3px;
  left: 1px;
  box-shadow: 0 2px 6px rgba(253, 203, 110, 0.3);
}

.hand {
  width: 14px;
  height: 12px;
  background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%);
  border-radius: 10px;
  border: 2px solid rgba(255, 255, 255, 0.8);
  position: relative;
  top: -5px;
  left: -1px;
  box-shadow: 0 2px 6px rgba(253, 203, 110, 0.3);
  transition: all 0.3s ease;
}

.fingers {
  position: absolute;
  top: -2px;
  left: 2px;
  width: 8px;
  height: 4px;
  background: linear-gradient(135deg, #fdcb6e 0%, #e17055 100%);
  border-radius: 4px 4px 0 0;
  opacity: 0.7;
}

.thumb {
  position: absolute;
  top: 2px;
  right: -1px;
  width: 3px;
  height: 6px;
  background: linear-gradient(135deg, #fdcb6e 0%, #e17055 100%);
  border-radius: 50%;
  opacity: 0.7;
}

// 角色效果
.character-effects {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 15;
}

.sparkle {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 20px;
  height: 20px;
  
  &::before {
    content: '✨';
    position: absolute;
    font-size: 16px;
    animation: sparkle 1s ease-in-out infinite;
  }
}

.hearts {
  position: absolute;
  top: 5px;
  right: 5px;
  
  .heart {
    position: absolute;
    width: 12px;
    height: 12px;
    
    &::before {
      content: '💖';
      font-size: 12px;
      animation: float 2s ease-in-out infinite;
    }
    
    &:nth-child(1) {
      animation-delay: 0s;
    }
    
    &:nth-child(2) {
      top: 8px;
      left: 8px;
      animation-delay: 0.5s;
    }
  }
}

// 动画定义
@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-8px);
  }
}

@keyframes wave {
  0%, 100% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(-15deg);
  }
  75% {
    transform: rotate(15deg);
  }
}

@keyframes sparkle {
  0%, 100% {
    opacity: 1;
    transform: scale(1) rotate(0deg);
  }
  50% {
    opacity: 0.7;
    transform: scale(1.2) rotate(180deg);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) scale(1);
    opacity: 1;
  }
  50% {
    transform: translateY(-5px) scale(1.1);
    opacity: 0.8;
  }
}

@keyframes hairFloat {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-2px) rotate(1deg);
  }
}

@keyframes shimmer {
  0%, 100% {
    opacity: 0.6;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.05);
  }
}

@keyframes strandSway {
  0%, 100% {
    transform: rotate(-20deg) translateY(0);
  }
  50% {
    transform: rotate(-25deg) translateY(-1px);
  }
}

@keyframes accessoryTwinkle {
  0%, 100% {
    transform: scale(1) rotate(0deg);
    opacity: 1;
  }
  25% {
    transform: scale(1.1) rotate(90deg);
    opacity: 0.8;
  }
  50% {
    transform: scale(1.2) rotate(180deg);
    opacity: 1;
  }
  75% {
    transform: scale(1.1) rotate(270deg);
    opacity: 0.8;
  }
}

@keyframes happyShine {
  0%, 100% {
    transform: scale(1);
    opacity: 0.9;
  }
  50% {
    transform: scale(1.3);
    opacity: 1;
  }
}

@keyframes excitedPulse {
  0%, 100% {
    transform: translate(-50%, -50%) scale(1);
  }
  50% {
    transform: translate(-50%, -50%) scale(1.2);
  }
}

@keyframes excitedSparkle {
  0%, 100% {
    transform: scale(1) rotate(0deg);
    opacity: 0.9;
  }
  25% {
    transform: scale(1.5) rotate(90deg);
    opacity: 1;
  }
  50% {
    transform: scale(1.8) rotate(180deg);
    opacity: 0.8;
  }
  75% {
    transform: scale(1.5) rotate(270deg);
    opacity: 1;
  }
}

@keyframes cheekGlow {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.05);
  }
}

@keyframes armWave {
  0%, 100% {
    transform: rotate(-10deg);
  }
  25% {
    transform: rotate(-30deg);
  }
  75% {
    transform: rotate(10deg);
  }
}

@keyframes celebrate {
  0%, 100% {
    transform: rotate(-20deg) translateY(0);
  }
  50% {
    transform: rotate(-40deg) translateY(-8px);
  }
}

@keyframes patternTwinkle {
  0%, 100% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 1;
  }
  50% {
    transform: translate(-50%, -50%) scale(1.2);
    opacity: 0.8;
  }
}

@keyframes statusRipple {
  0% {
    transform: scale(0.8);
    opacity: 1;
  }
  100% {
    transform: scale(1.5);
    opacity: 0;
  }
}

// 现代状态指示器
.status-indicator {
  position: absolute;
  top: -10px;
  right: -10px;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 20;
  
  &::before {
    content: '';
    position: absolute;
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(255, 255, 255, 0.3) 0%, transparent 70%);
    animation: statusRipple 2s ease-in-out infinite;
  }
}

.status-dot {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  z-index: 2;
  border: 2px solid rgba(255, 255, 255, 0.8);
  
  &::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.9);
  }
  
  &.success {
    background: linear-gradient(135deg, #00b894 0%, #00a085 100%);
    box-shadow: 
      0 0 25px rgba(0, 184, 148, 0.6),
      0 6px 15px rgba(0, 184, 148, 0.3);
    animation: successPulse 1.5s ease-in-out infinite;
    
    &::before {
      content: '✓';
      color: white;
      font-size: 10px;
      font-weight: bold;
      width: auto;
      height: auto;
      background: none;
    }
  }
  
  &.error {
    background: linear-gradient(135deg, #e17055 0%, #d63031 100%);
    box-shadow: 
      0 0 25px rgba(225, 112, 85, 0.6),
      0 6px 15px rgba(225, 112, 85, 0.3);
    animation: errorShake 0.6s ease-in-out;
    
    &::before {
      content: '✗';
      color: white;
      font-size: 10px;
      font-weight: bold;
      width: auto;
      height: auto;
      background: none;
    }
  }
  
  &.hint {
    background: linear-gradient(135deg, #fdcb6e 0%, #e17055 100%);
    box-shadow: 
      0 0 25px rgba(253, 203, 110, 0.6),
      0 6px 15px rgba(253, 203, 110, 0.3);
    animation: hintGlow 2s ease-in-out infinite;
    
    &::before {
      content: '?';
      color: white;
      font-size: 12px;
      font-weight: bold;
      width: auto;
      height: auto;
      background: none;
    }
  }
  
  &.info {
    background: linear-gradient(135deg, #74b9ff 0%, #0984e3 100%);
    box-shadow: 
      0 0 25px rgba(116, 185, 255, 0.6),
      0 6px 15px rgba(116, 185, 255, 0.3);
    animation: infoPulse 2s ease-in-out infinite;
    
    &::before {
      content: 'i';
      color: white;
      font-size: 10px;
      font-weight: bold;
      width: auto;
      height: auto;
      background: none;
    }
  }
}

@keyframes successPulse {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.3);
    opacity: 0.8;
  }
}

@keyframes errorShake {
  0%, 100% {
    transform: translateX(0) scale(1);
  }
  10%, 30%, 50%, 70%, 90% {
    transform: translateX(-3px) scale(1.1);
  }
  20%, 40%, 60%, 80% {
    transform: translateX(3px) scale(1.1);
  }
}

@keyframes hintGlow {
  0%, 100% {
    transform: scale(1);
    box-shadow: 
      0 0 20px rgba(245, 158, 11, 0.6),
      0 4px 12px rgba(245, 158, 11, 0.3);
  }
  50% {
    transform: scale(1.2);
    box-shadow: 
      0 0 30px rgba(245, 158, 11, 0.8),
      0 6px 20px rgba(245, 158, 11, 0.5);
  }
}

@keyframes infoPulse {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.15);
    opacity: 0.9;
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
}

@keyframes shake {
  0%, 100% {
    transform: translateX(0);
  }
  25% {
    transform: translateX(-2px);
  }
  75% {
    transform: translateX(2px);
  }
}

@keyframes glow {
  0%, 100% {
    box-shadow: 0 0 5px $warning-color;
  }
  50% {
    box-shadow: 0 0 15px $warning-color, 0 0 25px $warning-color;
  }
}

@keyframes sharingan {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

// 现代对话框设计
.guide-dialog {
  position: absolute;
  bottom: 150px;
  left: 50%;
  transform: translateX(-50%);
  background: linear-gradient(145deg, rgba(255, 255, 255, 0.95) 0%, rgba(255, 255, 255, 0.9) 100%);
  border-radius: 20px;
  padding: 1.8rem;
  box-shadow: 
    0 25px 50px rgba(0, 0, 0, 0.15),
    0 12px 24px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(25px);
  min-width: 300px;
  max-width: 350px;
  animation: dialogSlideIn 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #74b9ff 0%, #0984e3 50%, #6c5ce7 100%);
    border-radius: 20px 20px 0 0;
  }
  
  &.welcome {
    &::before {
      background: linear-gradient(90deg, #74b9ff 0%, #0984e3 100%);
    }
  }
  
  &.success {
    &::before {
      background: linear-gradient(90deg, #00b894 0%, #00a085 100%);
    }
  }
  
  &.error {
    &::before {
      background: linear-gradient(90deg, #e17055 0%, #d63031 100%);
    }
  }
  
  &.hint {
    &::before {
      background: linear-gradient(90deg, #fdcb6e 0%, #e17055 100%);
    }
  }
  
  &.info {
    &::before {
      background: linear-gradient(90deg, #a29bfe 0%, #6c5ce7 100%);
    }
  }
}

// 现代对话框内容
.dialog-content {
  display: flex;
  align-items: flex-start;
  gap: 1.2rem;
  position: relative;
  z-index: 2;
}

.dialog-icon {
  font-size: 2.2rem;
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(116, 185, 255, 0.1) 0%, rgba(108, 92, 231, 0.1) 100%);
  border-radius: 12px;
  border: 1px solid rgba(116, 185, 255, 0.2);
}

.dialog-text {
  flex: 1;
  
  h4 {
    color: #2d3748;
    margin: 0 0 0.6rem 0;
    font-size: 1.15rem;
    font-weight: 700;
    line-height: 1.3;
  }
  
  p {
    color: #4a5568;
    margin: 0;
    font-size: 0.95rem;
    line-height: 1.6;
    font-weight: 500;
  }
}

.dialog-arrow {
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-top: 10px solid rgba(255, 255, 255, 0.95);
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.floating-tip {
  position: absolute;
  bottom: 140px;
  left: 50%;
  transform: translateX(-50%);
  background: linear-gradient(135deg, rgba($accent-color, 0.1) 0%, rgba($accent-color-light, 0.1) 100%);
  border: 1px solid rgba($accent-color, 0.2);
  border-radius: $btn-radius;
  padding: 0.8rem 1.2rem;
  animation: fadeInUp 0.3s ease-out;
  white-space: nowrap;
  
  &.progress {
    background: linear-gradient(135deg, rgba($info-color, 0.1) 0%, rgba($info-color-light, 0.1) 100%);
    border-color: rgba($info-color, 0.2);
  }
  
  &.answer {
    background: linear-gradient(135deg, rgba($info-color, 0.1) 0%, rgba($info-color-light, 0.1) 100%);
    border-color: rgba($info-color, 0.2);
  }
}

.tip-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.tip-icon {
  font-size: 1.2rem;
}

.tip-text {
  color: $text-color;
  font-size: 0.9rem;
  font-weight: 600;
}

@keyframes dialogSlideIn {
  0% {
    opacity: 0;
    transform: translateX(-50%) translateY(30px) scale(0.9);
  }
  60% {
    opacity: 0.8;
    transform: translateX(-50%) translateY(-5px) scale(1.02);
  }
  100% {
    opacity: 1;
    transform: translateX(-50%) translateY(0) scale(1);
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .quiz-guide {
    left: 1rem;
    bottom: 1rem;
  }
  
  .guide-character {
    width: 60px;
    height: 90px;
  }
  
  .character-face {
    width: 45px;
    height: 45px;
    
    .bow {
      width: 15px;
      height: 9px;
      top: -12px;
      right: -3px;
    }
    
    .blush {
      width: 6px;
      height: 3px;
      
      &.left {
        left: 6px;
      }
      
      &.right {
        right: 6px;
      }
    }
  }
  
  .guide-dialog {
    min-width: 240px;
    max-width: 280px;
    padding: 1rem;
  }
  
  .dialog-text h4 {
    font-size: 1rem;
  }
  
  .dialog-text p {
    font-size: 0.9rem;
  }
}
</style>