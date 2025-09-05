<template>
  <div class="section smart-life">
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
              @click="handleSmartDeviceClick(device)"
            >
              <div class="device-info">
                <div class="device-name">{{ device.name }}</div>
                <div class="device-brand">{{ device.brand }}</div>
                <div class="device-features">
                  <span v-for="feature in device.features" :key="feature" class="feature-tag">
                    {{ feature }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="card-content">
          <p class="card-description">
            以小米全屋智能为例，通过米家App统一控制，实现设备间的智能联动。Yeelight智能灯可根据环境光线自动调节，
            米家空调能学习用户作息习惯智能调温，小爱音箱作为控制中枢，让语音控制成为可能，打造真正的智慧生活体验。
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
            以小爱同学为代表的智能语音助手，集成了先进的语音识别和自然语言处理技术，支持方言识别，
            可控制全屋智能设备、查询天气信息、播放音乐、设置提醒等，真正实现了"动口不动手"的智能生活体验。
          </p>
        </div>
      </div>
    </div>

    <!-- 设备详情弹窗 -->
    <div v-if="showDeviceDetail" class="device-detail-modal" @click="closeDeviceDetail">
      <div class="device-detail-content" @click.stop>
        <div class="device-detail-header">
          <h3>{{ selectedDeviceDetail?.name }}</h3>
          <button class="close-btn" @click="closeDeviceDetail">×</button>
        </div>
        <div class="device-detail-body">
          <div class="device-brand">{{ selectedDeviceDetail?.brand }}</div>
          <p class="device-description">{{ selectedDeviceDetail?.description }}</p>
          
          <div class="device-technologies">
            <h4>核心技术</h4>
            <div class="tech-tags">
              <span v-for="tech in selectedDeviceDetail?.technologies" :key="tech" class="tech-tag">
                {{ tech }}
              </span>
            </div>
          </div>
          
          <div class="device-scenarios">
            <h4>应用场景</h4>
            <ul class="scenario-list">
              <li v-for="scenario in selectedDeviceDetail?.scenarios" :key="scenario">
                {{ scenario }}
              </li>
            </ul>
          </div>
          
          <div class="device-specifications">
            <h4>技术规格</h4>
            <div class="spec-grid">
              <div v-for="(value, key) in selectedDeviceDetail?.specifications" :key="key" class="spec-item">
                <span class="spec-label">{{ key }}:</span>
                <span class="spec-value">{{ value }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 语音助手对话弹窗 -->
    <div v-if="showVoiceDialog" class="voice-dialog-modal" @click="showVoiceDialog = false">
      <div class="voice-dialog-content" @click.stop>
        <div class="voice-dialog-header">
          <h3>🎤 小爱同学智能对话</h3>
          <button class="close-btn" @click="showVoiceDialog = false">×</button>
        </div>
        <div class="voice-dialog-body">
          <div class="conversation">
            <div class="message user-message">
              <div class="message-content">小爱同学，帮我打开客厅的灯</div>
              <div class="message-time">19:30</div>
            </div>
            <div class="message ai-message">
              <div class="message-content">好的，已为您打开客厅的Yeelight智能灯，当前亮度70%，色温3000K</div>
              <div class="message-time">19:30</div>
            </div>
            <div class="message user-message">
              <div class="message-content">调暗一点，我要看电影</div>
              <div class="message-time">19:31</div>
            </div>
            <div class="message ai-message">
              <div class="message-content">已切换到观影模式，亮度调至30%，色温调至2700K，为您营造舒适的观影环境</div>
              <div class="message-time">19:31</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue'

// Props
const props = defineProps({
  chapterId: {
    type: String,
    default: '4'
  }
})

// Emits
const emit = defineEmits(['image-click', 'interaction'])

// 响应式数据
const smartHomeHovered = ref(false)
const showDeviceDetail = ref(false)
const selectedDeviceDetail = ref(null)
const showVoiceDialog = ref(false)

// 智能设备数据
const smartDevices = ref([
  { 
    id: 'light', 
    name: 'Yeelight智能灯', 
    brand: '小米生态链', 
    features: ['色温调节', '亮度调节', '场景模式'], 
    style: { top: '20%', left: '15%' },
    description: 'Yeelight智能灯采用先进的LED技术，支持1600万种颜色调节，可根据时间自动调整色温，营造舒适的光照环境。',
    technologies: ['WiFi连接', 'LED调光技术', '色温控制', '场景联动'],
    scenarios: ['阅读模式：暖白光护眼', '观影模式：柔和昏暗', '起床模式：渐亮唤醒', '睡眠模式：自动关闭'],
    specifications: {
      power: '9W',
      brightness: '800流明',
      colorTemp: '1700K-6500K',
      lifespan: '25000小时'
    }
  },
  { 
    id: 'ac', 
    name: '米家空调', 
    brand: '小米', 
    features: ['远程控制', '定时开关', '节能模式'], 
    style: { top: '15%', right: '20%' },
    description: '米家空调搭载变频技术和AI学习算法，能够智能学习用户习惯，自动调节温度，实现舒适节能的室内环境。',
    technologies: ['变频技术', 'AI温控算法', '远程控制', '节能优化'],
    scenarios: ['回家模式：提前制冷', '睡眠模式：静音运行', '外出模式：自动关闭', '节能模式：智能调温'],
    specifications: {
      cooling: '1.5匹',
      energyLevel: '一级能效',
      noise: '≤18dB',
      coverage: '16-25㎡'
    }
  },
  { 
    id: 'camera', 
    name: '小白摄像头', 
    brand: '小米', 
    features: ['1080P高清', '夜视功能', '移动侦测'], 
    style: { top: '10%', right: '10%' },
    description: '小白摄像头配备高清镜头和红外夜视功能，支持AI人形检测和异常行为识别，为家庭安全提供24小时守护。',
    technologies: ['1080P高清录制', '红外夜视', 'AI人形检测', '云端存储'],
    scenarios: ['安防监控：24小时守护', '远程查看：手机实时观看', '异常报警：智能识别入侵', '双向通话：远程沟通'],
    specifications: {
      resolution: '1920×1080',
      nightVision: '10米红外',
      storage: '本地+云端',
      connectivity: 'WiFi 2.4GHz'
    }
  },
  { 
    id: 'speaker', 
    name: '小爱音箱', 
    brand: '小米', 
    features: ['语音控制', '音乐播放', '智能问答'], 
    style: { bottom: '20%', left: '25%' },
    description: '小爱音箱作为智能家居的控制中枢，集成了小爱同学语音助手，支持语音控制全屋设备，提供丰富的内容服务。',
    technologies: ['语音识别', '自然语言处理', '远场拾音', '智能联动'],
    scenarios: ['语音控制：动口不动手', '音乐播放：海量曲库', '信息查询：天气新闻', '设备联动：一句话控制'],
    specifications: {
      microphones: '6麦克风阵列',
      speaker: '2.25英寸全频',
      connectivity: 'WiFi + 蓝牙',
      power: '12W'
    }
  },
  { 
    id: 'robot', 
    name: '米家扫地机器人', 
    brand: '小米', 
    features: ['激光导航', '智能清扫', '自动回充'], 
    style: { bottom: '15%', right: '30%' },
    description: '米家扫地机器人采用激光雷达导航技术，能够精确建图规划路径，智能识别房间类型，提供个性化清扫方案。',
    technologies: ['激光雷达', 'SLAM算法', '路径规划', '智能识别'],
    scenarios: ['定时清扫：按时自动工作', '分区清扫：指定区域清理', '禁区设置：避开敏感区域', '远程控制：手机操控'],
    specifications: {
      navigation: 'LDS激光雷达',
      suction: '2100Pa大吸力',
      battery: '5200mAh',
      runtime: '150分钟'
    }
  },
  { 
    id: 'purifier', 
    name: '米家空气净化器', 
    brand: '小米', 
    features: ['PM2.5检测', '智能净化', '静音运行'], 
    style: { bottom: '25%', left: '45%' },
    description: '米家空气净化器配备高精度传感器，实时监测空气质量，智能调节净化模式，为家庭提供清新健康的空气环境。',
    technologies: ['激光传感器', '三层过滤', '智能算法', '静音技术'],
    scenarios: ['自动模式：智能调节风速', '睡眠模式：超静音运行', '手动模式：自定义设置', '儿童锁：防误操作'],
    specifications: {
      cadr: '330m³/h',
      coverage: '23-40㎡',
      noise: '≤34dB',
      filter: 'H13级HEPA'
    }
  }
])

// 方法
const handleSmartHomeHover = () => {
  smartHomeHovered.value = true
}

const handleSmartHomeLeave = () => {
  smartHomeHovered.value = false
}

const handleSmartDeviceClick = (device) => {
  console.log(`点击了智能设备: ${device.name} - ${device.brand}`)
  selectedDeviceDetail.value = device
  showDeviceDetail.value = true
  emit('interaction')
}

const closeDeviceDetail = () => {
  showDeviceDetail.value = false
  selectedDeviceDetail.value = null
}

const onImageClick = (imageType) => {
  emit('image-click', imageType)
  emit('interaction')
  console.log(`点击了${imageType}图片`)
}
</script>

<style scoped>
/* 智慧生活板块样式 */
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

.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.content-card {
  background: var(--card-bg);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: var(--box-shadow);
  transition: all var(--transition-normal);
  border: 1px solid var(--border-color);
}

.content-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 191, 255, 0.2);
  border-color: var(--accent-color);
}

.card-header {
  padding: 1.5rem;
  background: linear-gradient(135deg, var(--accent-color), var(--accent-light-color));
  color: white;
}

.card-header h4 {
  margin: 0;
  font-size: 1.4rem;
  font-weight: 600;
}

.image-container {
  position: relative;
  overflow: hidden;
  height: 300px;
}

.main-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-normal);
  cursor: pointer;
}

.main-image:hover {
  transform: scale(1.05);
}

.device-overlays {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
}

.device-overlay {
  position: absolute;
  width: 60px;
  height: 60px;
  background: rgba(0, 191, 255, 0.9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all var(--transition-normal);
  animation: pulse 2s infinite;
}

.device-overlay:hover {
  transform: scale(1.2);
  background: rgba(0, 191, 255, 1);
}

.device-overlay:hover .device-info {
  opacity: 1;
  visibility: visible;
}

.device-info {
  position: absolute;
  top: 70px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--card-bg);
  padding: 1rem;
  border-radius: 8px;
  box-shadow: var(--box-shadow);
  min-width: 200px;
  opacity: 0;
  visibility: hidden;
  transition: all var(--transition-normal);
  z-index: 10;
}

.device-name {
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.device-brand {
  color: var(--text-secondary-color);
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
}

.device-features {
  display: flex;
  flex-wrap: wrap;
  gap: 0.25rem;
}

.feature-tag {
  background: var(--accent-color);
  color: white;
  padding: 0.2rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
}

.card-content {
  padding: 1.5rem;
}

.card-description {
  color: var(--text-secondary-color);
  line-height: 1.6;
  margin: 0;
}

/* 弹窗样式 */
.device-detail-modal,
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

.device-detail-content,
.voice-dialog-content {
  background: var(--card-bg);
  border-radius: 16px;
  max-width: 600px;
  width: 100%;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: var(--box-shadow);
}

.device-detail-header,
.voice-dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
}

.device-detail-header h3,
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

.device-detail-body {
  padding: 1.5rem;
}

.device-description {
  color: var(--text-secondary-color);
  line-height: 1.6;
  margin-bottom: 1.5rem;
}

.device-technologies,
.device-scenarios,
.device-specifications {
  margin-bottom: 1.5rem;
}

.device-technologies h4,
.device-scenarios h4,
.device-specifications h4 {
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.tech-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tech-tag {
  background: var(--accent-color);
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 16px;
  font-size: 0.9rem;
}

.scenario-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.scenario-list li {
  color: var(--text-secondary-color);
  padding: 0.5rem 0;
  border-bottom: 1px solid var(--border-color);
}

.scenario-list li:last-child {
  border-bottom: none;
}

.spec-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 0.5rem;
}

.spec-item {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem;
  background: var(--secondary-color);
  border-radius: 8px;
}

.spec-label {
  color: var(--text-secondary-color);
  font-weight: 500;
}

.spec-value {
  color: var(--text-color);
  font-weight: 600;
}

/* 语音对话样式 */
.voice-dialog-body {
  padding: 1.5rem;
  max-height: 400px;
  overflow-y: auto;
}

.conversation {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.message {
  display: flex;
  flex-direction: column;
  max-width: 80%;
}

.user-message {
  align-self: flex-end;
}

.user-message .message-content {
  background: var(--accent-color);
  color: white;
  padding: 0.8rem 1rem;
  border-radius: 18px 18px 4px 18px;
}

.ai-message .message-content {
  background: var(--secondary-color);
  color: var(--text-color);
  padding: 0.8rem 1rem;
  border-radius: 18px 18px 18px 4px;
}

.message-time {
  font-size: 0.8rem;
  color: var(--text-muted-color);
  margin-top: 0.25rem;
  text-align: right;
}

.ai-message .message-time {
  text-align: left;
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
  .content-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .section {
    padding: 1rem;
  }
  
  .section-title {
    font-size: 2rem;
  }
  
  .title-icon {
    font-size: 2.5rem;
  }
}

/* 浅色主题样式 */
.light-theme .section-title {
  color: var(--text-color);
}

.light-theme .section-subtitle {
  color: var(--text-secondary-color);
}

.light-theme .content-card {
  background: var(--card-bg);
  border-color: var(--border-color);
}

.light-theme .content-card:hover {
  box-shadow: 0 12px 40px rgba(0, 191, 255, 0.15);
  border-color: var(--accent-color);
}

.light-theme .device-info {
  background: var(--card-bg);
}

.light-theme .device-name {
  color: var(--text-color);
}

.light-theme .device-brand {
  color: var(--text-secondary-color);
}

.light-theme .card-description {
  color: var(--text-secondary-color);
}

.light-theme .device-detail-content,
.light-theme .voice-dialog-content {
  background: var(--card-bg);
}

.light-theme .device-detail-header,
.light-theme .voice-dialog-header {
  border-bottom-color: var(--border-color);
}

.light-theme .device-detail-header h3,
.light-theme .voice-dialog-header h3 {
  color: var(--text-color);
}

.light-theme .close-btn {
  color: var(--text-secondary-color);
}

.light-theme .close-btn:hover {
  background: var(--border-color);
  color: var(--text-color);
}

.light-theme .device-description {
  color: var(--text-secondary-color);
}

.light-theme .device-technologies h4,
.light-theme .device-scenarios h4,
.light-theme .device-specifications h4 {
  color: var(--text-color);
}

.light-theme .scenario-list li {
  color: var(--text-secondary-color);
  border-bottom-color: var(--border-color);
}

.light-theme .spec-item {
  background: var(--secondary-color);
}

.light-theme .spec-label {
  color: var(--text-secondary-color);
}

.light-theme .spec-value {
  color: var(--text-color);
}

.light-theme .ai-message .message-content {
  background: var(--secondary-color);
  color: var(--text-color);
}

.light-theme .message-time {
  color: var(--text-muted-color);
}
</style>