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
        <div v-if="currentSection === 0" class="section smart-life" key="smart-life">
          <div class="section-header">
            <h3 class="section-title">
              <span class="title-icon">🏠</span>
              <span class="title-text">智慧生活</span>
            </h3>
            <p class="section-subtitle">人工智能让生活更便捷舒适</p>
          </div>

          <div class="content-grid">
            <!-- 智能家居 -->
            <div class="content-card smart-home-card">
              <div class="card-header">
                <h4>智能家居系统</h4>
              </div>
              <div class="image-container" @mouseenter="handleSmartHomeHover" @mouseleave="handleSmartHomeLeave">
                <img 
                  src="/images/chapter4/smart-home.jpg" 
                  alt="智能家居全景图"
                  class="main-image"
                  @click="onImageClick('smart-home')"
                />
                <!-- 智能设备高亮提示 -->
                <div v-if="smartHomeHovered" class="device-overlays">
                  <div 
                    v-for="device in smartDevices" 
                    :key="device.id"
                    class="device-overlay"
                    :style="device.style"
                  >
                    <div class="device-info">{{ device.name }}</div>
                  </div>
                </div>
              </div>
              <div class="card-content">
                <p class="card-description">
                  智能家居通过物联网和人工智能技术实现设备互联互通，智能灯光可根据光线强度自动调节亮度，
                  智能温控系统能学习用户习惯提前调整室内温度，为人们带来便捷、舒适的生活体验。
                </p>
              </div>
            </div>

            <!-- 智能语音助手 -->
            <div class="content-card voice-assistant-card">
              <div class="card-header">
                <h4>智能语音助手</h4>
              </div>
              <div class="image-container">
                <img 
                  src="/images/chapter4/voice-assistant.jpg" 
                  alt="智能语音助手交互图"
                  class="main-image"
                  @click="showVoiceDialog = true; onImageClick('voice-assistant')"
                />
              </div>
              <div class="card-content">
                <p class="card-description">
                  智能语音助手通过语音识别和自然语言处理技术，实现与用户的自然交流，
                  可完成信息查询、设备控制、日程安排等多种任务，大大提升了生活的便利性。
                </p>
              </div>
            </div>
          </div>
        </div>
      </transition>

      <!-- 智慧驾驶板块 -->
      <transition name="slide-center" mode="out-in">
        <div v-if="currentSection === 1" class="section smart-driving" key="smart-driving">
          <div class="section-header">
            <h3 class="section-title">
              <span class="title-icon">🚗</span>
              <span class="title-text">智慧驾驶</span>
            </h3>
            <p class="section-subtitle">自动驾驶技术引领出行革命</p>
          </div>

          <div class="content-grid">
            <!-- 自动驾驶 -->
            <div class="content-card autonomous-driving-card">
              <div class="card-header">
                <h4>自动驾驶技术</h4>
              </div>
              <div class="image-container driving-animation">
                <img 
                  src="/images/chapter4/autonomous-driving.jpg" 
                  alt="自动驾驶汽车示意图"
                  class="main-image"
                  @click="onImageClick('autonomous-driving')"
                />
                <!-- 传感器波纹动画 -->
                <div class="sensor-waves">
                  <div v-for="n in 3" :key="n" class="wave" :style="{ animationDelay: (n-1) * 0.5 + 's' }"></div>
                </div>
              </div>
              <div class="card-content">
                <p class="card-description">
                  自动驾驶技术依靠计算机视觉和深度学习，实现环境感知、障碍物识别、决策控制等功能，
                  能有效提高驾驶安全性与效率，推动交通系统智能化。
                </p>
              </div>
            </div>

            <!-- 车联网 -->
            <div class="content-card connected-vehicles-card">
              <div class="card-header">
                <h4>车联网通信</h4>
              </div>
              <div class="image-container">
                <img 
                  src="/images/chapter4/connected-vehicles.jpg" 
                  alt="车联网通信示意图"
                  class="main-image"
                  @click="onImageClick('connected-vehicles')"
                />
              </div>
              <div class="card-content">
                <p class="card-description">
                  车联网技术实现车辆与外部环境的实时通信，V2V、V2I、V2P等通信模式增强了车辆的感知和决策能力，
                  优化了交通流量，提升了交通安全性。
                </p>
              </div>
            </div>
          </div>
        </div>
      </transition>

      <!-- 智慧医疗板块 -->
      <transition name="fade" mode="out-in">
        <div v-if="currentSection === 2" class="section smart-medical" key="smart-medical">
          <div class="section-header">
            <h3 class="section-title">
              <span class="title-icon">⚕️</span>
              <span class="title-text">智慧医疗</span>
            </h3>
            <p class="section-subtitle">AI技术助力精准医疗</p>
          </div>

          <div class="content-grid">
            <!-- 医疗影像分析 -->
            <div class="content-card medical-imaging-card">
              <div class="card-header">
                <h4>医疗影像分析</h4>
              </div>
              <div class="image-container">
                <img 
                  src="/images/chapter4/medical-imaging.jpg" 
                  alt="医疗影像分析图"
                  class="main-image"
                  @click="handleMedicalImageClick"
                />
                <!-- 病灶标识 -->
                <div v-if="medicalImageClicked" class="lesion-markers">
                  <div 
                    v-for="lesion in lesionMarkers" 
                    :key="lesion.id"
                    class="lesion-marker"
                    :style="lesion.style"
                    @click="showLesionDetail(lesion)"
                  >
                    <div class="marker-pulse"></div>
                  </div>
                </div>
              </div>
              <div class="card-content">
                <p class="card-description">
                  AI医疗影像分析技术能够快速识别影像中的异常区域，提高诊断精度和效率，
                  为医生提供重要的辅助决策支持。
                </p>
              </div>
            </div>

            <!-- 远程医疗 -->
            <div class="content-card telemedicine-card">
              <div class="card-header">
                <h4>远程医疗服务</h4>
              </div>
              <div class="image-container">
                <img 
                  src="/images/chapter4/telemedicine.jpg" 
                  alt="远程医疗服务图"
                  class="main-image"
                  @click="onImageClick('telemedicine')"
                />
              </div>
              <div class="card-content">
                <p class="card-description">
                  智慧医疗借助人工智能技术，在医疗影像分析、个性化医疗、远程医疗等方面发挥重要作用，
                  提升了诊断的精准性和医疗资源的利用效率。
                </p>
              </div>
            </div>
          </div>
        </div>
      </transition>
    </div>

    <!-- 语音助手对话弹窗 -->
    <div v-if="showVoiceDialog" class="voice-dialog-overlay" @click="showVoiceDialog = false">
      <div class="voice-dialog" @click.stop>
        <div class="dialog-header">
          <h4>智能语音助手对话模拟</h4>
          <button class="close-btn" @click="showVoiceDialog = false">×</button>
        </div>
        <div class="dialog-content">
          <div class="conversation">
            <div 
              v-for="(message, index) in voiceConversation" 
              :key="index"
              :class="['message', message.type]"
              :style="{ animationDelay: index * 0.5 + 's' }"
            >
              <div class="message-content">
                <span class="speaker">{{ message.speaker }}:</span>
                <span class="text">{{ message.text }}</span>
              </div>
              <!-- 语音波形动画 -->
              <div v-if="message.type === 'assistant'" class="voice-wave">
                <div class="wave-bar" v-for="n in 5" :key="n"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 病灶详情弹窗 -->
    <div v-if="showLesionDetail" class="lesion-detail-overlay" @click="showLesionDetail = false">
      <div class="lesion-detail" @click.stop>
        <div class="detail-header">
          <h4>AI分析结果</h4>
          <button class="close-btn" @click="showLesionDetail = false">×</button>
        </div>
        <div class="detail-content">
          <div class="analysis-info">
            <h5>{{ selectedLesion?.name }}</h5>
            <p><strong>位置:</strong> {{ selectedLesion?.location }}</p>
            <p><strong>大小:</strong> {{ selectedLesion?.size }}</p>
            <p><strong>AI置信度:</strong> {{ selectedLesion?.confidence }}</p>
            <p><strong>建议:</strong> {{ selectedLesion?.recommendation }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 第四章案例学习组件 - AI应用场景展示
 * 
 * 功能特性:
 * - 三个主要板块: 智慧生活、智慧驾驶、智慧医疗
 * - 丰富的动画效果和交互体验
 * - 智能设备标识、语音对话模拟、医疗影像分析等功能
 * - 用户交互统计和学习进度跟踪
 */
import { ref, reactive, onMounted, computed, nextTick } from 'vue'

// 组件属性
const props = defineProps({
  chapterId: {
    type: [String, Number],
    default: 4
  }
})

// 事件定义
const emit = defineEmits(['case-completed', 'all-cases-completed'])

// 响应式数据
const titleVisible = ref(false)
const descriptionVisible = ref(false)
const showParticles = ref(false)
const currentSection = ref(0)
const totalInteractions = ref(0)
const requiredInteractions = ref(15) // 需要15次交互才算完成
const caseCompleted = ref(false)

// 智能家居相关
const smartHomeHovered = ref(false)
const smartDevices = ref([
  { id: 'light', name: '智能灯光', style: { top: '20%', left: '15%' } },
  { id: 'ac', name: '智能空调', style: { top: '15%', right: '20%' } },
  { id: 'camera', name: '安防摄像头', style: { top: '10%', right: '10%' } },
  { id: 'speaker', name: '智能音响', style: { bottom: '25%', left: '20%' } }
])

// 语音助手对话
const showVoiceDialog = ref(false)
const voiceConversation = ref([
  { type: 'user', speaker: '用户', text: '小智，帮我调节一下客厅的温度' },
  { type: 'assistant', speaker: '智能助手', text: '好的，我正在为您调节客厅温度到24度' },
  { type: 'user', speaker: '用户', text: '顺便把客厅的灯光调暗一些' },
  { type: 'assistant', speaker: '智能助手', text: '已为您调节客厅灯光亮度到40%，还有其他需要帮助的吗？' }
])

// 医疗影像相关
const medicalImageClicked = ref(false)
const showLesionDetail = ref(false)
const selectedLesion = ref(null)
const lesionMarkers = ref([
  { 
    id: 'lesion1', 
    name: '疑似结节',
    location: '右肺上叶',
    size: '8.5mm × 6.2mm',
    confidence: '92.3%',
    recommendation: '建议进一步CT增强扫描',
    style: { top: '35%', left: '45%' } 
  },
  { 
    id: 'lesion2', 
    name: '钙化灶',
    location: '左肺下叶',
    size: '3.2mm × 2.8mm',
    confidence: '98.7%',
    recommendation: '良性钙化，建议定期复查',
    style: { top: '60%', right: '35%' } 
  }
])

// 板块配置
const sections = ref([
  { id: 'smart-life', title: '智慧生活', icon: '🏠' },
  { id: 'smart-driving', title: '智慧驾驶', icon: '🚗' },
  { id: 'smart-medical', title: '智慧医疗', icon: '⚕️' }
])

// 方法定义
const handleSmartHomeHover = () => {
  smartHomeHovered.value = true
  incrementInteraction()
}

const handleSmartHomeLeave = () => {
  smartHomeHovered.value = false
}

const handleMedicalImageClick = () => {
  medicalImageClicked.value = !medicalImageClicked.value
  incrementInteraction()
}

const showLesionDetailFunc = (lesion) => {
  selectedLesion.value = lesion
  showLesionDetail.value = true
  incrementInteraction()
}

const onImageClick = (imageType) => {
  incrementInteraction()
  console.log(`点击了${imageType}图片`)
}

const switchSection = (index) => {
  if (currentSection.value !== index) {
    currentSection.value = index
    incrementInteraction()
  }
}

const incrementInteraction = () => {
  totalInteractions.value++
  checkCaseCompletion()
}

const checkCaseCompletion = () => {
  if (totalInteractions.value >= requiredInteractions.value && !caseCompleted.value) {
    caseCompleted.value = true
    emit('case-completed', {
      chapterId: props.chapterId,
      caseType: 'ai-applications',
      interactions: totalInteractions.value
    })
    emit('all-cases-completed', {
      chapterId: props.chapterId,
      totalCases: 1,
      completedCases: 1
    })
  }
}

// 粒子动画相关
const getParticleStyle = (index) => {
  const x = Math.random() * 100
  const y = Math.random() * 100
  const delay = Math.random() * 2
  const duration = 3 + Math.random() * 2
  
  return {
    left: x + '%',
    top: y + '%',
    animationDelay: delay + 's',
    animationDuration: duration + 's'
  }
}

const getParticleIcon = (index) => {
  const icons = ['🏠', '🚗', '⚕️', '🤖', '💡', '📱', '🔬', '💊']
  return icons[index % icons.length]
}

// 生命周期钩子
onMounted(async () => {
  // 页面加载动效序列
  showParticles.value = true
  
  setTimeout(() => {
    titleVisible.value = true
  }, 500)
  
  setTimeout(() => {
    descriptionVisible.value = true
  }, 1000)
  
  setTimeout(() => {
    showParticles.value = false
  }, 3000)
})
</script>

<style scoped>
/* 基础样式 */
.chapter4-case-study {
  position: relative;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  overflow-x: hidden;
}

/* 粒子背景动效 */
.particle-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
  opacity: 0;
  transition: opacity 1s ease;
}

.particle-background.active {
  opacity: 1;
}

.particle {
  position: absolute;
  font-size: 24px;
  animation: float 4s infinite ease-in-out;
}

@keyframes float {
  0%, 100% { 
    transform: translateY(0px) rotate(0deg);
    opacity: 0;
  }
  50% { 
    transform: translateY(-20px) rotate(180deg);
    opacity: 1;
  }
}

/* 头部样式 */
.case-header {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 40px 20px;
}

.case-title {
  font-size: 2.5rem;
  margin-bottom: 15px;
  opacity: 0;
  transform: translateY(30px) blur(10px);
  transition: all 1s ease;
}

.case-title.visible {
  opacity: 1;
  transform: translateY(0) blur(0);
}

.case-description {
  font-size: 1.2rem;
  margin-bottom: 30px;
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.8s ease;
}

.case-description.visible {
  opacity: 1;
  transform: translateY(0);
}

/* 进度指示器 */
.progress-indicator {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.progress-item {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 15px;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.progress-item.completed {
  background: rgba(76, 175, 80, 0.2);
  border-color: rgba(76, 175, 80, 0.5);
}

.progress-icon {
  font-size: 2rem;
  margin-right: 15px;
}

.progress-info h4 {
  margin: 0 0 5px 0;
  font-size: 1.1rem;
}

.progress-info p {
  margin: 0 0 8px 0;
  opacity: 0.8;
}

.status-completed {
  color: #4caf50;
  font-weight: bold;
}

.status-pending {
  color: #ff9800;
  font-weight: bold;
}

/* 板块导航 */
.section-navigation {
  position: relative;
  z-index: 2;
  display: flex;
  justify-content: center;
  margin-bottom: 40px;
}

.nav-buttons {
  display: flex;
  gap: 15px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 50px;
  padding: 10px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.nav-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  border: none;
  border-radius: 25px;
  background: transparent;
  color: #fff;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
}

.nav-button:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.nav-button.active {
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.nav-icon {
  font-size: 1.2rem;
}

/* 主要内容区域 */
.main-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.section {
  min-height: 600px;
}

.section-header {
  text-align: center;
  margin-bottom: 40px;
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 2rem;
  margin-bottom: 10px;
}

.title-icon {
  font-size: 2.5rem;
}

.section-subtitle {
  font-size: 1.1rem;
  opacity: 0.9;
}

/* 内容网格 */
.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 30px;
}

/* 内容卡片 */
.content-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 25px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.content-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.card-header h4 {
  font-size: 1.3rem;
  margin-bottom: 15px;
  text-align: center;
}

.image-container {
  position: relative;
  margin-bottom: 20px;
  border-radius: 15px;
  overflow: hidden;
  cursor: pointer;
}

.main-image {
  width: 100%;
  height: 250px;
  object-fit: cover;
  transition: all 0.3s ease;
}

.main-image:hover {
  transform: scale(1.05);
}

/* 智能设备高亮效果 */
.device-overlays {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.device-overlay {
  position: absolute;
  width: 60px;
  height: 60px;
  border: 3px solid #00ff88;
  border-radius: 50%;
  animation: pulse 2s infinite;
  cursor: pointer;
}

.device-info {
  position: absolute;
  top: -40px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: #fff;
  padding: 5px 10px;
  border-radius: 15px;
  font-size: 12px;
  white-space: nowrap;
  animation: slideUp 0.5s ease;
}

@keyframes pulse {
  0%, 100% { box-shadow: 0 0 0 0 rgba(0, 255, 136, 0.7); }
  50% { box-shadow: 0 0 0 20px rgba(0, 255, 136, 0); }
}

@keyframes slideUp {
  from { opacity: 0; transform: translateX(-50%) translateY(10px); }
  to { opacity: 1; transform: translateX(-50%) translateY(0); }
}

/* 自动驾驶动画 */
.driving-animation {
  position: relative;
  overflow: visible;
}

.sensor-waves {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  pointer-events: none;
}

.wave {
  position: absolute;
  width: 200px;
  height: 200px;
  border: 2px solid rgba(0, 255, 255, 0.6);
  border-radius: 50%;
  animation: expandWave 2s infinite ease-out;
}

@keyframes expandWave {
  0% {
    width: 50px;
    height: 50px;
    opacity: 1;
  }
  100% {
    width: 200px;
    height: 200px;
    opacity: 0;
  }
}

/* 医疗影像标记 */
.lesion-markers {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.lesion-marker {
  position: absolute;
  width: 30px;
  height: 30px;
  cursor: pointer;
}

.marker-pulse {
  width: 100%;
  height: 100%;
  background: rgba(255, 0, 0, 0.3);
  border: 2px solid #ff0000;
  border-radius: 50%;
  animation: markerPulse 1.5s infinite;
}

@keyframes markerPulse {
  0%, 100% { 
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(255, 0, 0, 0.7);
  }
  50% { 
    transform: scale(1.2);
    box-shadow: 0 0 0 15px rgba(255, 0, 0, 0);
  }
}

/* 卡片描述 */
.card-description {
  line-height: 1.6;
  opacity: 0.9;
}

/* 过渡动画 */
.slide-left-enter-active, .slide-left-leave-active {
  transition: all 0.6s ease;
}

.slide-left-enter-from {
  transform: translateX(100%);
  opacity: 0;
}

.slide-left-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}

.slide-center-enter-active, .slide-center-leave-active {
  transition: all 0.6s ease;
}

.slide-center-enter-from {
  transform: translateX(100%);
  opacity: 0;
}

.slide-center-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}

.fade-enter-active, .fade-leave-active {
  transition: all 0.6s ease;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
  transform: scale(0.95);
}

/* 弹窗样式 */
.voice-dialog-overlay, .lesion-detail-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(5px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.voice-dialog, .lesion-detail {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 30px;
  max-width: 500px;
  width: 90%;
  color: #333;
  position: relative;
}

.dialog-header, .detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  padding-bottom: 15px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #666;
  padding: 5px;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: rgba(0, 0, 0, 0.1);
  color: #333;
}

/* 对话样式 */
.conversation {
  max-height: 300px;
  overflow-y: auto;
}

.message {
  margin-bottom: 15px;
  animation: messageSlideIn 0.5s ease forwards;
  opacity: 0;
  transform: translateY(10px);
}

.message.user {
  text-align: right;
}

.message.assistant {
  text-align: left;
}

.message-content {
  display: inline-block;
  max-width: 80%;
  padding: 10px 15px;
  border-radius: 15px;
  margin-bottom: 5px;
}

.message.user .message-content {
  background: #007acc;
  color: white;
}

.message.assistant .message-content {
  background: #f0f0f0;
  color: #333;
}

.speaker {
  font-weight: bold;
  margin-right: 5px;
}

/* 语音波形动画 */
.voice-wave {
  display: flex;
  justify-content: center;
  gap: 3px;
  margin-top: 5px;
}

.wave-bar {
  width: 3px;
  height: 20px;
  background: #007acc;
  animation: waveAnimation 1s infinite ease-in-out;
}

.wave-bar:nth-child(2) { animation-delay: 0.1s; }
.wave-bar:nth-child(3) { animation-delay: 0.2s; }
.wave-bar:nth-child(4) { animation-delay: 0.3s; }
.wave-bar:nth-child(5) { animation-delay: 0.4s; }

@keyframes waveAnimation {
  0%, 100% { transform: scaleY(1); }
  50% { transform: scaleY(2); }
}

@keyframes messageSlideIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 病灶详情样式 */
.analysis-info h5 {
  color: #007acc;
  margin-bottom: 15px;
  font-size: 1.2rem;
}

.analysis-info p {
  margin-bottom: 10px;
  line-height: 1.5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .case-title {
    font-size: 2rem;
  }
  
  .content-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .nav-buttons {
    flex-direction: column;
    gap: 10px;
  }
  
  .section-title {
    font-size: 1.5rem;
  }
  
  .main-image {
    height: 200px;
  }
  
  .voice-dialog, .lesion-detail {
    margin: 20px;
    width: auto;
  }
}
</style>
