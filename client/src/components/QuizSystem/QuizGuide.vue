<template>
  <div class="quiz-guide" :class="{ 'guide-active': isActive, 'guide-hidden': isHidden }">
    <!-- 简化的卡通人物容器 -->
    <div class="guide-character" @click="toggleGuide">
      <div class="character-container">
        <!-- 简化的卡通角色 -->
        <div class="character-main">
          <!-- 头部区域 -->
          <div class="character-head">
            <!-- 简化的头发 -->
            <div class="hair"></div>
            
            <!-- 简化的脸部 -->
            <div class="face">
              <!-- 简化的眼睛 -->
              <div class="eyes">
                <div class="eye left-eye" :class="{ 'blink': isBlinking }"></div>
                <div class="eye right-eye" :class="{ 'blink': isBlinking }"></div>
              </div>
              
              <!-- 简化的嘴巴 -->
              <div class="mouth" :class="mouthExpression"></div>
            </div>
          </div>
          
          <!-- 简化的身体 -->
          <div class="character-body">
            <!-- 简化的手臂 -->
            <div class="arms">
              <div class="arm left-arm" :class="{ 'wave': isWaving }"></div>
              <div class="arm right-arm" :class="{ 'point': isPointing }"></div>
            </div>
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
      floatingTip: '',
      showStatus: false,
      statusType: 'info',
      mouthExpression: 'normal',
      dialogIcon: '💡',
      dialogTitle: '学习提示',
      dialogMessage: '点击我可以获得帮助哦！'
    }
  },
  watch: {
    isCorrect(newValue) {
      if (newValue !== null) {
        this.updateExpression(newValue ? 'happy' : 'sad')
        this.showFeedback(newValue)
      }
    },
    showHint(newValue) {
      if (newValue) {
        this.showTip('💡', '提示', '试试看这个方向！', 'hint')
      }
    },
    showAnswer(newValue) {
      if (newValue) {
        this.showTip('🔍', '答案', '这是正确答案！', 'answer')
      }
    }
  },
  mounted() {
    this.startBlinking()
    this.updateStatus()
  },
  methods: {
    toggleGuide() {
      this.isActive = !this.isActive
      if (this.isActive) {
        this.showGuideDialog()
      }
    },
    
    startBlinking() {
      setInterval(() => {
        this.isBlinking = true
        setTimeout(() => {
          this.isBlinking = false
        }, 150)
      }, 3000)
    },
    
    updateExpression(expression) {
      this.mouthExpression = expression
      
      // 根据表情触发相应的动作
      switch (expression) {
        case 'happy':
          this.isWaving = true
          setTimeout(() => this.isWaving = false, 1000)
          break
        case 'excited':
          this.isPointing = true
          setTimeout(() => this.isPointing = false, 800)
          break
        case 'sad':
          // 悲伤时不做特殊动作
          break
      }
    },
    
    showFeedback(isCorrect) {
      const message = isCorrect ? '太棒了！答对了！' : '没关系，继续加油！'
      const icon = isCorrect ? '🎉' : '💪'
      const type = isCorrect ? 'success' : 'encouragement'
      
      this.showTip(icon, isCorrect ? '恭喜' : '鼓励', message, type)
    },
    
    showTip(icon, title, message, type) {
      this.tipIcon = icon
      this.dialogTitle = title
      this.dialogMessage = message
      this.tipType = type
      this.showFloatingTip = true
      
      setTimeout(() => {
        this.showFloatingTip = false
      }, 3000)
    },
    
    showGuideDialog() {
      this.dialogType = 'guide'
      this.dialogIcon = '🎯'
      this.dialogTitle = '学习助手'
      this.dialogMessage = `当前进度：${this.questionIndex + 1}/${this.totalQuestions}`
      this.showDialog = true
      
      setTimeout(() => {
        this.showDialog = false
      }, 4000)
    },
    
    updateStatus() {
      if (this.currentQuestion) {
        this.showStatus = true
        this.statusType = 'active'
      }
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
    transform: translateX(-120%);
    opacity: 0;
  }
  
  &.guide-active {
    .guide-character {
      transform: scale(1.1);
    }
  }
}

.guide-character {
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    transform: scale(1.05);
  }
}

.character-container {
  width: 80px;
  height: 100px;
  position: relative;
}

.character-main {
  width: 100%;
  height: 100%;
  position: relative;
}

// 简化的头部设计
.character-head {
  width: 60px;
  height: 60px;
  position: relative;
  margin: 0 auto;
}

// 简化的头发
.hair {
  width: 70px;
  height: 50px;
  background: linear-gradient(135deg, #ff9ff3, #f368e0);
  border-radius: 50px 50px 20px 20px;
  position: absolute;
  top: -5px;
  left: -5px;
  box-shadow: 0 4px 12px rgba(255, 159, 243, 0.3);
}

// 简化的脸部
.face {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #ffe4e1, #ffb3ba);
  border-radius: 50%;
  position: absolute;
  top: 15px;
  left: 5px;
  border: 3px solid #fff;
  box-shadow: 0 4px 16px rgba(255, 179, 186, 0.3);
}

// 简化的眼睛
.eyes {
  position: absolute;
  top: 15px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 12px;
}

.eye {
  width: 16px;
  height: 16px;
  background: #fff;
  border-radius: 50%;
  border: 2px solid #ff9ff3;
  position: relative;
  transition: all 0.2s ease;
  
  &.blink {
    height: 4px;
    border-radius: 8px;
  }
  
  &::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 8px;
    height: 8px;
    background: #ff9ff3;
    border-radius: 50%;
  }
}

// 简化的嘴巴
.mouth {
  position: absolute;
  bottom: 12px;
  left: 50%;
  transform: translateX(-50%);
  width: 12px;
  height: 6px;
  border-radius: 0 0 12px 12px;
  background: #ff6b9d;
  transition: all 0.3s ease;
  
  &.happy {
    border-radius: 12px 12px 0 0;
    height: 8px;
  }
  
  &.sad {
    border-radius: 12px 12px 0 0;
    transform: translateX(-50%) rotate(180deg);
  }
  
  &.excited {
    width: 16px;
    height: 10px;
    border-radius: 50%;
  }
}

// 简化的身体
.character-body {
  width: 40px;
  height: 35px;
  background: linear-gradient(135deg, #ff9ff3, #f368e0);
  border-radius: 20px 20px 25px 25px;
  position: absolute;
  top: 55px;
  left: 20px;
  box-shadow: 0 4px 12px rgba(255, 159, 243, 0.3);
}

// 简化的手臂
.arms {
  position: absolute;
  top: 60px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 50px;
}

.arm {
  width: 8px;
  height: 25px;
  background: linear-gradient(135deg, #ffe4e1, #ffb3ba);
  border-radius: 4px;
  position: relative;
  transition: all 0.3s ease;
  
  &.wave {
    animation: wave 0.5s ease-in-out infinite;
  }
  
  &.point {
    transform: rotate(-45deg);
  }
}

@keyframes wave {
  0%, 100% { transform: rotate(0deg); }
  50% { transform: rotate(20deg); }
}

// 状态指示器
.status-indicator {
  position: absolute;
  top: -5px;
  right: -5px;
  width: 16px;
  height: 16px;
}

.status-dot {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: #4CAF50;
  box-shadow: 0 0 8px rgba(76, 175, 80, 0.6);
  
  &.info { background: #2196F3; }
  &.success { background: #4CAF50; }
  &.warning { background: #FF9800; }
  &.error { background: #F44336; }
}

// 指导对话框
.guide-dialog {
  position: absolute;
  bottom: 120px;
  left: 50%;
  transform: translateX(-50%);
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  max-width: 280px;
  z-index: 1001;
  
  .dialog-content {
    display: flex;
    align-items: center;
    gap: 12px;
  }
  
  .dialog-icon {
    font-size: 24px;
    flex-shrink: 0;
  }
  
  .dialog-text {
    h4 {
      margin: 0 0 4px 0;
      font-size: 14px;
      font-weight: 600;
      color: #333;
    }
    
    p {
      margin: 0;
      font-size: 12px;
      color: #666;
      line-height: 1.4;
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
    border-top: 8px solid #fff;
  }
}

// 浮动提示
.floating-tip {
  position: absolute;
  top: -60px;
  left: 50%;
  transform: translateX(-50%);
  background: #fff;
  border-radius: 8px;
  padding: 8px 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  white-space: nowrap;
  z-index: 1001;
  
  .tip-content {
    display: flex;
    align-items: center;
    gap: 6px;
  }
  
  .tip-icon {
    font-size: 16px;
  }
  
  .tip-text {
    font-size: 12px;
    color: #333;
  }
}
</style>