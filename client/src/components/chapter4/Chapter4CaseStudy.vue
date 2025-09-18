<template>
  <div class="chapter4-case-study">
    <!-- 页面加载动效背景 -->
    <div class="particle-background" :class="{ active: showParticles }">
      <div class="particle" v-for="n in 20" :key="n" 
           :style="getParticleStyle(n)">
        <i :class="getParticleIcon(n)"></i>
      </div>
    </div>

    <div class="case-header">
      <h2 class="case-title" :class="{ visible: titleVisible }">
        🤖 第四章案例学习
      </h2>
      <p class="case-description" :class="{ visible: descriptionVisible }">
        智慧生活、智慧驾驶与智慧医疗——人工智能技术赋能生活全场景
      </p>
      
      <!-- 学习进度指示器 -->
      <div class="progress-indicator">
        <div class="progress-item" :class="{ completed: caseCompleted }">
          <div class="progress-icon">🌟</div>
          <div class="progress-info">
            <h4>AI应用场景探索</h4>
            <p>交互次数: {{ totalInteractions }}/{{ requiredInteractions }} 次</p>
            <div class="progress-status">
              <span v-if="caseCompleted" class="status-completed">✅ 已完成</span>
              <span v-else class="status-pending">⏳ 进行中</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 板块导航 -->
    <div class="section-navigation">
      <div class="nav-buttons">
        <button 
          v-for="(section, index) in sections" 
          :key="section.id"
          :class="['nav-button', { active: currentSection === index }]"
          @click="switchSection(index)"
        >
          <span class="nav-icon">{{ section.icon }}</span>
          <span class="nav-text">{{ section.title }}</span>
        </button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 智慧生活板块 -->
      <transition name="slide-left" mode="out-in">
        <SmartLifeSection 
          v-if="currentSection === 0" 
          key="smart-life"
          :chapter-id="'4'"
          @image-click="onImageClick"
          @interaction="handleInteraction"
        />
      </transition>

      <!-- 智慧驾驶板块 -->
      <transition name="slide-center" mode="out-in">
        <SmartDrivingSection 
          v-if="currentSection === 1" 
          key="smart-driving"
          :chapter-id="'4'"
          @image-click="onImageClick"
          @interaction="handleInteraction"
        />
      </transition>

      <!-- 智慧娱乐板块 -->
      <transition name="slide-right" mode="out-in">
        <EntertainmentSection 
          v-if="currentSection === 2" 
          key="entertainment"
          :chapter-id="'4'"
          @image-click="onImageClick"
          @interaction="handleInteraction"
        />
      </transition>

      <!-- 精准医疗板块 -->
      <transition name="slide-up" mode="out-in">
        <MedicalSection 
          v-if="currentSection === 3" 
          key="medical"
          :chapter-id="'4'"
          @image-click="onImageClick"
          @interaction="handleInteraction"
        />
      </transition>

      <!-- 知识测验板块 -->
      <!-- <transition name="slide-down" mode="out-in">
        <QuizSection 
          v-if="currentSection === 4" 
          key="quiz"
          :chapter-id="'4'"
          @quiz-completed="handleQuizCompleted"
          @interaction="handleInteraction"
        />
      </transition> -->
    </div>

    <!-- 语音助手对话框 -->
    <div v-if="showVoiceDialog" class="voice-dialog-modal" @click="showVoiceDialog = false">
      <div class="voice-dialog-content" @click.stop>
        <div class="voice-dialog-header">
          <h3>🎤 智能语音助手体验</h3>
          <button class="close-btn" @click="showVoiceDialog = false">×</button>
        </div>
        <div class="voice-dialog-body">
          <div class="voice-demo">
            <div class="voice-wave" :class="{ active: isListening }">
              <div class="wave-bar" v-for="n in 5" :key="n"></div>
            </div>
            <p class="voice-status">{{ voiceStatus }}</p>
            <button class="voice-btn" @click="toggleVoiceDemo" :class="{ active: isListening }">
              {{ isListening ? '🛑 停止' : '🎤 开始体验' }}
            </button>
          </div>
          <div class="voice-examples">
            <h4>语音指令示例</h4>
            <div class="example-commands">
              <div class="command-item" v-for="cmd in voiceCommands" :key="cmd.id" @click="simulateVoiceCommand(cmd)">
                <span class="command-text">"{{ cmd.text }}"</span>
                <span class="command-response">{{ cmd.response }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import SmartLifeSection from './SmartLifeSection.vue'
import SmartDrivingSection from './SmartDrivingSection.vue'
import EntertainmentSection from './EntertainmentSection.vue'
import MedicalSection from './MedicalSection.vue'
// import QuizSection from './QuizSection.vue'

// 响应式数据
const showParticles = ref(false)
const titleVisible = ref(false)
const descriptionVisible = ref(false)
const currentSection = ref(0)
const totalInteractions = ref(0)
const requiredInteractions = ref(15)
const showVoiceDialog = ref(false)
const isListening = ref(false)
const voiceStatus = ref('点击开始体验语音助手')

// 板块配置
const sections = ref([
  { id: 'smart-life', title: '智慧生活', icon: '🏠' },
  { id: 'smart-driving', title: '智慧驾驶', icon: '🚗' },
  { id: 'entertainment', title: '智慧娱乐', icon: '🎮' },
  { id: 'medical', title: '精准医疗', icon: '🏥' }
  // { id: 'quiz', title: '知识测验', icon: '🧠' }
])

// 语音指令示例
const voiceCommands = ref([
  {
    id: 1,
    text: '小助手，打开客厅的灯',
    response: '好的，已为您打开客厅灯光'
  },
  {
    id: 2,
    text: '小助手，把空调温度调到26度',
    response: '已将空调温度调节至26度'
  },
  {
    id: 3,
    text: '小助手，播放轻音乐',
    response: '正在为您播放轻音乐'
  },
  {
    id: 4,
    text: '小助手，明天早上7点叫我起床',
    response: '好的，已设置明天早上7点的闹钟'
  }
])

// 计算属性
const caseCompleted = computed(() => {
  return totalInteractions.value >= requiredInteractions.value
})

// 方法
const switchSection = (index) => {
  currentSection.value = index
  handleInteraction()
  // 切换到板块
}

const handleInteraction = () => {
  totalInteractions.value++
  // 交互次数统计
}

const onImageClick = (imageType) => {
  handleInteraction()
  // 点击了图片
}

// const handleQuizCompleted = (result) => {
// 测验完成
//   handleInteraction()
// }

const toggleVoiceDemo = () => {
  isListening.value = !isListening.value
  if (isListening.value) {
    voiceStatus.value = '正在聆听您的指令...'
    setTimeout(() => {
      if (isListening.value) {
        voiceStatus.value = '请说出您的指令'
      }
    }, 1000)
  } else {
    voiceStatus.value = '点击开始体验语音助手'
  }
  handleInteraction()
}

const simulateVoiceCommand = (command) => {
  voiceStatus.value = `识别到: ${command.text}`
  setTimeout(() => {
    voiceStatus.value = command.response
  }, 1000)
  handleInteraction()
}

const getParticleStyle = (index) => {
  const colors = ['#00BFFF', '#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7']
  const size = Math.random() * 10 + 5
  return {
    left: Math.random() * 100 + '%',
    top: Math.random() * 100 + '%',
    width: size + 'px',
    height: size + 'px',
    backgroundColor: colors[index % colors.length],
    animationDelay: Math.random() * 3 + 's',
    animationDuration: (Math.random() * 3 + 2) + 's'
  }
}

const getParticleIcon = (index) => {
  const icons = ['fas fa-brain', 'fas fa-robot', 'fas fa-microchip', 'fas fa-cog', 'fas fa-lightbulb']
  return icons[index % icons.length]
}

// 生命周期
onMounted(() => {
  setTimeout(() => {
    showParticles.value = true
  }, 100)
  
  setTimeout(() => {
    titleVisible.value = true
  }, 500)
  
  setTimeout(() => {
    descriptionVisible.value = true
  }, 1000)
})
</script>

<style scoped>
/* 主容器样式 */
.chapter4-case-study {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--bg-color) 0%, var(--secondary-color) 100%);
  position: relative;
  overflow-x: hidden;
}

/* 粒子背景动画 */
.particle-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
  opacity: 0;
  transition: opacity 1s ease-in-out;
}

.particle-background.active {
  opacity: 1;
}

.particle {
  position: absolute;
  border-radius: 50%;
  animation: float 3s ease-in-out infinite;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 8px;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
    opacity: 0.7;
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
    opacity: 1;
  }
}

/* 案例头部样式 */
.case-header {
  text-align: center;
  padding: 4rem 2rem 2rem;
  position: relative;
  z-index: 2;
}

.case-title {
  font-size: 3rem;
  font-weight: 700;
  color: var(--text-color);
  margin-bottom: 1rem;
  opacity: 0;
  transform: translateY(30px);
  transition: all 0.8s ease-out;
}

.case-title.visible {
  opacity: 1;
  transform: translateY(0);
}

.case-description {
  font-size: 1.3rem;
  color: var(--text-secondary-color);
  margin-bottom: 2rem;
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.8s ease-out 0.3s;
}

.case-description.visible {
  opacity: 1;
  transform: translateY(0);
}

/* 进度指示器 */
.progress-indicator {
  max-width: 600px;
  margin: 0 auto;
}

.progress-item {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  background: var(--card-bg);
  padding: 1.5rem;
  border-radius: 16px;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--border-color);
  transition: all var(--transition-normal);
}

.progress-item.completed {
  background: linear-gradient(135deg, rgba(40, 167, 69, 0.1), rgba(40, 167, 69, 0.05));
  border-color: var(--success-color);
}

.progress-icon {
  font-size: 2.5rem;
  animation: pulse 2s infinite;
}

.progress-info h4 {
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.progress-info p {
  color: var(--text-secondary-color);
  margin-bottom: 0.5rem;
}

.status-completed {
  color: var(--success-color);
  font-weight: 600;
}

.status-pending {
  color: var(--warning-color);
  font-weight: 600;
}

/* 板块导航 */
.section-navigation {
  padding: 2rem;
  position: relative;
  z-index: 2;
}

.nav-buttons {
  display: flex;
  justify-content: center;
  gap: 1rem;
  flex-wrap: wrap;
  max-width: 1000px;
  margin: 0 auto;
}

.nav-button {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem 1.5rem;
  background: var(--card-bg);
  border: 2px solid var(--border-color);
  border-radius: 16px;
  cursor: pointer;
  transition: all var(--transition-normal);
  min-width: 120px;
}

.nav-button:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 191, 255, 0.2);
  border-color: var(--accent-color);
}

.nav-button.active {
  background: linear-gradient(135deg, var(--accent-color), var(--accent-light-color));
  color: white;
  border-color: var(--accent-color);
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 191, 255, 0.3);
}

.nav-icon {
  font-size: 2rem;
}

.nav-text {
  font-weight: 600;
  color: var(--text-color);
}

.nav-button.active .nav-text {
  color: white;
}

/* 主要内容区域 */
.main-content {
  position: relative;
  z-index: 2;
  min-height: 60vh;
}

/* 过渡动画 */
.slide-left-enter-active,
.slide-left-leave-active,
.slide-center-enter-active,
.slide-center-leave-active,
.slide-right-enter-active,
.slide-right-leave-active,
.slide-up-enter-active,
.slide-up-leave-active,
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.5s ease-in-out;
}

.slide-left-enter-from {
  opacity: 0;
  transform: translateX(-100px);
}

.slide-left-leave-to {
  opacity: 0;
  transform: translateX(100px);
}

.slide-center-enter-from {
  opacity: 0;
  transform: scale(0.9);
}

.slide-center-leave-to {
  opacity: 0;
  transform: scale(1.1);
}

.slide-right-enter-from {
  opacity: 0;
  transform: translateX(100px);
}

.slide-right-leave-to {
  opacity: 0;
  transform: translateX(-100px);
}

.slide-up-enter-from {
  opacity: 0;
  transform: translateY(50px);
}

.slide-up-leave-to {
  opacity: 0;
  transform: translateY(-50px);
}

.slide-down-enter-from {
  opacity: 0;
  transform: translateY(-50px);
}

.slide-down-leave-to {
  opacity: 0;
  transform: translateY(50px);
}

/* 语音助手对话框 */
.voice-dialog-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 2rem;
}

.voice-dialog-content {
  background: var(--card-bg);
  border-radius: 20px;
  max-width: 600px;
  width: 100%;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: var(--box-shadow);
}

.voice-dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
}

.voice-dialog-header h3 {
  margin: 0;
  color: var(--text-color);
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: var(--text-secondary-color);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 50%;
  transition: all var(--transition-fast);
}

.close-btn:hover {
  background: var(--border-color);
  color: var(--text-color);
}

.voice-dialog-body {
  padding: 2rem;
}

.voice-demo {
  text-align: center;
  margin-bottom: 2rem;
}

.voice-wave {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.3rem;
  height: 60px;
  margin-bottom: 1rem;
}

.wave-bar {
  width: 4px;
  height: 20px;
  background: var(--accent-color);
  border-radius: 2px;
  animation: wave 1.5s ease-in-out infinite;
}

.voice-wave.active .wave-bar {
  animation: wave-active 0.8s ease-in-out infinite;
}

.wave-bar:nth-child(2) {
  animation-delay: 0.1s;
}

.wave-bar:nth-child(3) {
  animation-delay: 0.2s;
}

.wave-bar:nth-child(4) {
  animation-delay: 0.3s;
}

.wave-bar:nth-child(5) {
  animation-delay: 0.4s;
}

@keyframes wave {
  0%, 100% {
    height: 20px;
    opacity: 0.5;
  }
  50% {
    height: 40px;
    opacity: 1;
  }
}

@keyframes wave-active {
  0%, 100% {
    height: 10px;
    opacity: 0.3;
  }
  50% {
    height: 50px;
    opacity: 1;
  }
}

.voice-status {
  color: var(--text-secondary-color);
  margin-bottom: 1rem;
  font-size: 1.1rem;
}

.voice-btn {
  background: var(--accent-color);
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 50px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-normal);
}

.voice-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 191, 255, 0.3);
}

.voice-btn.active {
  background: var(--error-color);
  animation: pulse 1s infinite;
}

.voice-examples h4 {
  color: var(--text-color);
  margin-bottom: 1rem;
}

.example-commands {
  display: grid;
  gap: 1rem;
}

.command-item {
  background: var(--secondary-color);
  padding: 1rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all var(--transition-fast);
  border: 1px solid var(--border-color);
}

.command-item:hover {
  background: var(--border-color);
  transform: translateX(5px);
}

.command-text {
  display: block;
  color: var(--text-color);
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.command-response {
  display: block;
  color: var(--text-secondary-color);
  font-style: italic;
}

/* 动画 */
@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(0, 191, 255, 0.7);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(0, 191, 255, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(0, 191, 255, 0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .case-title {
    font-size: 2.2rem;
  }
  
  .case-description {
    font-size: 1.1rem;
  }
  
  .nav-buttons {
    gap: 0.5rem;
  }
  
  .nav-button {
    min-width: 100px;
    padding: 0.8rem 1rem;
  }
  
  .nav-icon {
    font-size: 1.5rem;
  }
  
  .progress-item {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
  }
}

/* 浅色主题样式已通过 theme.css 统一管理 */
</style>
