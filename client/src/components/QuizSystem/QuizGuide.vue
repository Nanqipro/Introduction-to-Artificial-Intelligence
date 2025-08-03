<template>
  <div class="quiz-guide" :class="{ 'guide-active': isActive, 'guide-hidden': isHidden }">
    <!-- 卡通人物容器 -->
    <div class="guide-character" @click="toggleGuide">
      <div class="character-avatar">
        <div class="character-face">
          <div class="bow"></div>
          <div class="blush left"></div>
          <div class="blush right"></div>
          <div class="eyes">
            <div class="eye left-eye" :class="{ 'blink': isBlinking }"></div>
            <div class="eye right-eye" :class="{ 'blink': isBlinking }"></div>
          </div>
          <div class="mouth" :class="mouthExpression"></div>
        </div>
        <div class="character-body">
          <div class="body-shape"></div>
          <div class="arms">
            <div class="arm left-arm" :class="{ 'wave': isWaving }"></div>
            <div class="arm right-arm" :class="{ 'point': isPointing }"></div>
          </div>
        </div>
      </div>
      
      <!-- 状态指示器 -->
      <div class="status-indicator" v-if="showStatus">
        <div class="status-dot" :class="statusType"></div>
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
      tipIcon: '💡',
      dialogIcon: '👩‍🎓',
      dialogTitle: '学习小助手',
      dialogMessage: '我是你的学习伙伴，有什么问题都可以问我哦！',
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
          this.tipIcon = '✏️'
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
      this.dialogIcon = '👋'
      this.dialogTitle = '欢迎来到答题系统！'
      this.dialogMessage = `我是你的学习小助手，我会陪伴你完成这${this.totalQuestions}道题目。准备好了吗？`
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
        this.tipIcon = '🏆'
        this.floatingTip = `优秀！进度${progress}%，正确率${(overallAccuracy * 100).toFixed(0)}%`
      } else if (progress >= 60 && overallAccuracy >= 0.6) {
        this.tipIcon = '🌟'
        this.floatingTip = `很好！进度${progress}%，正确率${(overallAccuracy * 100).toFixed(0)}%`
      } else if (progress >= 40) {
        this.tipIcon = '📊'
        this.floatingTip = `进度：${progress}% (${index + 1}/${this.totalQuestions})`
      } else {
        this.tipIcon = '🚀'
        this.floatingTip = `开始答题！进度${progress}%`
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
        this.dialogIcon = '🏆'
        this.dialogTitle = '太厉害了！'
        this.dialogMessage = `连续答对${this.consecutiveCorrect}题！你简直就是学习天才！继续保持这个状态！`
        this.mouthExpression = 'excited'
        this.userMood = 'excited'
        this.isWaving = true
        console.log('设置表情: excited')
      } else if (this.consecutiveCorrect >= 3) {
        // 连续答对3-4题
        this.dialogType = 'success'
        this.dialogIcon = '🌟'
        this.dialogTitle = '太棒了！'
        this.dialogMessage = `连续答对${this.consecutiveCorrect}题！你的学习状态很棒，继续保持！`
        this.mouthExpression = 'happy'
        this.userMood = 'happy'
        this.isWaving = true
        console.log('设置表情: happy')
      } else if (overallAccuracy >= 0.8) {
        // 整体正确率很高
        this.dialogType = 'success'
        this.dialogIcon = '🎯'
        this.dialogTitle = '回答正确！'
        this.dialogMessage = '答对了！你的正确率很高，说明你掌握得很好！'
        this.mouthExpression = 'happy'
        this.userMood = 'happy'
        console.log('设置表情: happy')
      } else {
        // 普通答对
        this.dialogType = 'success'
        this.dialogIcon = '✅'
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
        this.dialogIcon = '🤗'
        this.dialogTitle = '别灰心！'
        this.dialogMessage = `连续答错${this.consecutiveWrong}题了，没关系！让我来帮你分析一下，我们一起找出问题所在。`
        this.mouthExpression = 'sad'
        this.userMood = 'worried'
        this.isPointing = true
        console.log('设置表情: sad (连续答错)')
      } else if (overallAccuracy < 0.5) {
        // 整体正确率较低
        this.dialogType = 'error'
        this.dialogIcon = '💪'
        this.dialogTitle = '加油！'
        this.dialogMessage = '答错了，但不要紧！学习是一个过程，每个错误都是进步的机会。我们一起努力！'
        this.mouthExpression = 'sad'
        this.userMood = 'worried'
        this.isPointing = true
        console.log('设置表情: sad (正确率低)')
      } else {
        // 普通答错
        this.dialogType = 'error'
        this.dialogIcon = '💡'
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
        '答对了！你真棒！',
        '回答正确！继续保持！',
        '答对了！你的理解很到位！',
        '回答正确！学习进步中！',
        '答对了！你掌握得很好！',
        '回答正确！为你点赞！',
        '答对了！你的思路很清晰！',
        '回答正确！继续保持这个状态！'
      ]
      return messages[Math.floor(Math.random() * messages.length)]
    },
    
    getRandomWrongMessage() {
      const messages = [
        '答错了，但没关系！每个错误都是学习的机会。',
        '答错了，不要紧！我们一起找出正确答案。',
        '答错了，但你已经很努力了！继续加油！',
        '答错了，没关系！学习就是在错误中进步的。',
        '答错了，但不要灰心！你一定能行的！',
        '答错了，没关系！我们一起学习，一起进步！',
        '答错了，但你已经很棒了！继续努力！',
        '答错了，不要紧！错误是成功路上的垫脚石。'
      ]
      return messages[Math.floor(Math.random() * messages.length)]
    },
    
    showHintMessage() {
      const overallAccuracy = this.getOverallAccuracy()
      const consecutiveWrong = this.consecutiveWrong
      
      this.dialogType = 'hint'
      this.dialogIcon = '💡'
      this.dialogTitle = '学习提示'
      
      if (consecutiveWrong >= 3) {
        this.dialogMessage = '看起来你遇到了一些困难。让我们一起来分析：1. 仔细阅读题目 2. 找出关键词 3. 排除明显错误的选项 4. 相信你的直觉！'
        this.userMood = 'worried'
        this.mouthExpression = 'sad'
      } else if (overallAccuracy < 0.6) {
        this.dialogMessage = '你的正确率还有提升空间。建议：1. 放慢答题速度 2. 仔细思考每个选项 3. 回顾相关知识点 4. 不要着急，慢慢来！'
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
        '相信你的第一感觉，但也要仔细验证！',
        '题目中的每个词都很重要，不要忽略细节！',
        '把题目读两遍，确保理解完全正确！',
        '冷静思考，答案就在眼前！'
      ]
      return hints[Math.floor(Math.random() * hints.length)]
    },
    
        showGeneralTips() {
      const overallAccuracy = this.getOverallAccuracy()
      const totalAnswered = this.questionHistory.length
      
      this.dialogType = 'info'
      this.dialogIcon = '👩‍🎓'
      this.dialogTitle = '学习小贴士'
      
      if (totalAnswered === 0) {
        this.dialogMessage = '准备好开始答题了吗？记住要仔细阅读题目，相信自己一定能行！'
      } else if (overallAccuracy >= 0.9) {
        this.dialogMessage = '哇！你的表现太棒了！继续保持这个状态，你简直就是学习天才！'
      } else if (overallAccuracy >= 0.7) {
        this.dialogMessage = '你的学习状态很好！继续保持，相信你还能做得更好！'
      } else if (overallAccuracy >= 0.5) {
        this.dialogMessage = '你的表现不错！多练习一下，正确率会更高的！'
      } else if (this.consecutiveWrong >= 2) {
        this.dialogMessage = '看起来你遇到了一些困难。不要着急，慢慢来，我们一起找出问题所在！'
      } else {
        this.dialogMessage = this.getRandomEncouragement()
      }
      
      this.showDialog = true
    },
    
    getRandomEncouragement() {
      const encouragements = [
        '记住要仔细阅读题目哦！',
        '不确定的时候可以多想想',
        '相信自己，你一定能行！',
        '学习是一个渐进的过程',
        '每个错误都是学习的机会',
        '加油！你是最棒的！',
        '慢慢来，不要着急哦～',
        '你已经做得很好了！',
        '学习需要耐心，你已经很棒了！',
        '每一次尝试都是进步！',
        '不要害怕犯错，错误是成功的一部分！',
        '你的努力一定会得到回报的！'
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
      this.dialogIcon = '📊'
      this.dialogTitle = '答题总结'
      
      if (accuracy >= 90) {
        this.dialogMessage = `太棒了！你的正确率是${accuracy}%，表现非常优秀！你简直就是学习天才！`
        this.mouthExpression = 'excited'
      } else if (accuracy >= 80) {
        this.dialogMessage = `很好！你的正确率是${accuracy}%，表现很棒！继续保持这个状态！`
        this.mouthExpression = 'happy'
      } else if (accuracy >= 60) {
        this.dialogMessage = `不错！你的正确率是${accuracy}%，还有提升空间，继续加油！`
        this.mouthExpression = 'happy'
      } else {
        this.dialogMessage = `你的正确率是${accuracy}%，不要灰心！学习是一个过程，多练习一定会进步的！`
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
  transition: all 0.3s ease;
  
  &.guide-hidden {
    transform: translateX(-100%);
    opacity: 0;
  }
  
  &.guide-active {
    .guide-character {
      transform: scale(1.1);
    }
  }
}

.guide-character {
  width: 80px;
  height: 120px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  
  &:hover {
    transform: scale(1.05);
  }
}

.character-avatar {
  width: 100%;
  height: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.character-face {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #ffb3d9 0%, #ff8ac4 100%);
  border-radius: 50%;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 
    0 8px 16px rgba(0, 0, 0, 0.2),
    inset 0 2px 4px rgba(255, 255, 255, 0.3);
  border: 3px solid #fff;
  
  &::before {
    content: '';
    position: absolute;
    top: -8px;
    left: 50%;
    transform: translateX(-50%);
    width: 75px;
    height: 40px;
    background: linear-gradient(135deg, #ff69b4 0%, #ff1493 100%);
    border-radius: 40px 40px 0 0;
    z-index: -1;
  }
  
  &::after {
    content: '';
    position: absolute;
    top: -12px;
    left: 50%;
    transform: translateX(-50%);
    width: 85px;
    height: 45px;
    background: linear-gradient(135deg, #ffb3d9 0%, #ff8ac4 100%);
    border-radius: 45px 45px 0 0;
    z-index: -2;
  }
  
  // 蝴蝶结装饰
  .bow {
    position: absolute;
    top: -15px;
    right: -5px;
    width: 20px;
    height: 12px;
    background: linear-gradient(135deg, #ff69b4 0%, #ff1493 100%);
    border-radius: 6px;
    transform: rotate(15deg);
    animation: bowSway 3s ease-in-out infinite;
    
    &::before {
      content: '';
      position: absolute;
      top: 2px;
      left: 50%;
      transform: translateX(-50%);
      width: 4px;
      height: 8px;
      background: #fff;
      border-radius: 2px;
    }
  }
  
  // 腮红
  .blush {
    position: absolute;
    top: 15px;
    width: 8px;
    height: 4px;
    background: rgba(255, 105, 180, 0.4);
    border-radius: 50%;
    
    &.left {
      left: 8px;
    }
    
    &.right {
      right: 8px;
    }
  }
}

.eyes {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.eye {
  width: 14px;
  height: 14px;
  background: #333;
  border-radius: 50%;
  position: relative;
  
  &::after {
    content: '';
    position: absolute;
    top: 3px;
    left: 3px;
    width: 5px;
    height: 5px;
    background: #fff;
    border-radius: 50%;
  }
  
  &::before {
    content: '';
    position: absolute;
    top: 1px;
    left: 1px;
    width: 3px;
    height: 3px;
    background: #ff69b4;
    border-radius: 50%;
    opacity: 0.6;
  }
  
  &.blink {
    height: 3px;
    border-radius: 2px;
    
    &::after,
    &::before {
      display: none;
    }
  }
}

.mouth {
  width: 18px;
  height: 6px;
  border: 2px solid #333;
  border-top: none;
  border-radius: 0 0 18px 18px;
  transition: all 0.3s ease;
  position: relative;
  
  &::after {
    content: '';
    position: absolute;
    bottom: -2px;
    left: 50%;
    transform: translateX(-50%);
    width: 4px;
    height: 2px;
    background: #ff69b4;
    border-radius: 2px;
  }
  
  &.happy {
    border-radius: 20px 20px 0 0;
    border-bottom: none;
    border-top: 2px solid #333;
    height: 14px;
    width: 22px;
    transform: scale(1.1);
    
    &::after {
      display: none;
    }
  }
  
  &.encouraging {
    width: 14px;
    height: 5px;
    border-radius: 0 0 14px 14px;
  }
  
  &.excited {
    border-radius: 22px 22px 0 0;
    border-bottom: none;
    border-top: 2px solid #333;
    height: 16px;
    width: 24px;
    transform: scale(1.2);
    
    &::after {
      display: none;
    }
  }
  
  &.worried {
    width: 16px;
    height: 4px;
    border-radius: 0 0 16px 16px;
    border-top: none;
    border-bottom: 2px solid #333;
  }
  
  &.sad {
    border-radius: 0 0 18px 18px;
    border-top: none;
    border-bottom: 2px solid #333;
    height: 8px;
    width: 18px;
    transform: scale(0.9);
    
    &::after {
      display: none;
    }
  }
}

.character-body {
  width: 50px;
  height: 60px;
  position: relative;
  margin-top: -10px;
}

.body-shape {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #ff69b4 0%, #ff1493 100%);
  border-radius: 25px 25px 15px 15px;
  box-shadow: 
    0 4px 8px rgba(0, 0, 0, 0.2),
    inset 0 2px 4px rgba(255, 255, 255, 0.3);
  border: 2px solid #fff;
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    top: 15px;
    left: 50%;
    transform: translateX(-50%);
    width: 20px;
    height: 15px;
    background: linear-gradient(135deg, #ffb3d9 0%, #ff8ac4 100%);
    border-radius: 10px;
    border: 1px solid rgba(255, 255, 255, 0.5);
  }
}

.arms {
  position: absolute;
  top: 10px;
  left: -5px;
  right: -5px;
  height: 40px;
  display: flex;
  justify-content: space-between;
}

.arm {
  width: 8px;
  height: 30px;
  background: linear-gradient(135deg, #ffb3d9 0%, #ff8ac4 100%);
  border-radius: 4px;
  position: relative;
  transform-origin: top center;
  transition: all 0.3s ease;
  
  &::after {
    content: '';
    position: absolute;
    bottom: -2px;
    left: 50%;
    transform: translateX(-50%);
    width: 12px;
    height: 8px;
    background: linear-gradient(135deg, #ffb3d9 0%, #ff8ac4 100%);
    border-radius: 6px;
    border: 1px solid rgba(255, 255, 255, 0.5);
  }
  
  &.wave {
    animation: wave 0.5s ease-in-out infinite;
  }
  
  &.point {
    transform: rotate(-45deg);
  }
}

@keyframes wave {
  0%, 100% {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(-20deg);
  }
}

.status-indicator {
  position: absolute;
  top: -5px;
  right: -5px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.status-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  
  &.success {
    background: $success-color;
    animation: pulse 1s ease-in-out infinite;
  }
  
  &.error {
    background: $error-color;
    animation: shake 0.5s ease-in-out;
  }
  
  &.hint {
    background: $warning-color;
    animation: glow 2s ease-in-out infinite;
  }
  
  &.info {
    background: $info-color;
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

@keyframes bowSway {
  0%, 100% {
    transform: rotate(15deg);
  }
  50% {
    transform: rotate(20deg);
  }
}

.guide-dialog {
  position: absolute;
  bottom: 140px;
  left: 50%;
  transform: translateX(-50%);
  background: linear-gradient(145deg, $card-bg 0%, rgba($card-bg, 0.95) 100%);
  border-radius: $card-radius;
  padding: 1.5rem;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.3),
    0 8px 16px rgba($accent-color, 0.1);
  border: 1px solid $card-border;
  backdrop-filter: blur(20px);
  min-width: 280px;
  max-width: 320px;
  animation: slideInUp 0.3s ease-out;
  
  &.welcome {
    border-left: 4px solid $info-color;
  }
  
  &.success {
    border-left: 4px solid $success-color;
  }
  
  &.error {
    border-left: 4px solid $error-color;
  }
  
  &.hint {
    border-left: 4px solid $warning-color;
  }
  
  &.info {
    border-left: 4px solid $accent-color;
  }
}

.dialog-content {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
}

.dialog-icon {
  font-size: 2rem;
  flex-shrink: 0;
}

.dialog-text {
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
    font-size: 0.95rem;
    line-height: 1.5;
  }
}

.dialog-arrow {
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
  border-top: 8px solid $card-bg;
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