<template>
  <div class="section smart-driving">
    <div class="section-header">
      <h3 class="section-title">
        <span class="title-icon">🚗</span>
        <span class="title-text">智慧驾驶</span>
      </h3>
      <p class="section-subtitle">AI技术重新定义出行体验</p>
    </div>

    <div class="content-grid">
      <!-- 自动驾驶技术 -->
      <div class="content-card autonomous-driving-card">
        <div class="card-header">
          <h4>自动驾驶技术</h4>
        </div>
        <div class="image-container" @mouseenter="handleDrivingHover" @mouseleave="handleDrivingLeave">
          <img 
            src="/images/chapter4/autonomous-driving.jpg" 
            alt="自动驾驶汽车技术展示"
            class="main-image"
            @click="onImageClick('autonomous-driving')"
          />
          <!-- 驾驶指标高亮提示 -->
          <div v-if="drivingHovered" class="indicator-overlays">
            <div 
              v-for="indicator in drivingIndicators" 
              :key="indicator.id"
              class="indicator-overlay"
              :style="indicator.style"
              @click="handleIndicatorClick(indicator)"
            >
              <div class="indicator-info">
                <div class="indicator-name">{{ indicator.name }}</div>
                <div class="indicator-value">{{ indicator.value }}</div>
                <div class="indicator-status" :class="indicator.status">{{ getIndicatorStatusText(indicator.status) }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="card-content">
          <p class="card-description">
            以某知名汽车公司的自动驾驶系统为例，通过8个摄像头、12个超声波传感器和1个前向雷达，
            实现360度环境感知。AI神经网络实时处理路况信息，支持自动变道、自动泊车、
            召唤功能等，让驾驶变得更安全、更智能。
          </p>
        </div>
      </div>

      <!-- 智能交通系统 -->
      <div class="content-card traffic-system-card">
        <div class="card-header">
          <h4>智能交通系统</h4>
        </div>
        <div class="image-container">
          <img 
            src="/images/chapter4/connected-vehicles.jpg" 
            alt="智能交通管理系统"
            class="main-image"
            @click="showTrafficDialog = true; onImageClick('smart-traffic')"
          />
        </div>
        <div class="card-content">
          <p class="card-description">
            智能交通系统通过AI算法优化信号灯控制，实时监测交通流量，
            动态调整通行策略。结合车路协同技术，为车辆提供最优路径规划，
            有效缓解交通拥堵，提升道路通行效率。
          </p>
        </div>
      </div>
    </div>

    <!-- 驾驶指标详情弹窗 -->
    <div v-if="showIndicatorDetail" class="indicator-detail-modal" @click="closeIndicatorDetail">
      <div class="indicator-detail-content" @click.stop>
        <div class="indicator-detail-header">
          <h3>{{ selectedIndicator?.name }}</h3>
          <button class="close-btn" @click="closeIndicatorDetail">×</button>
        </div>
        <div class="indicator-detail-body">
          <div class="indicator-current-value">
            <span class="value-label">当前数值:</span>
            <span class="value-number">{{ selectedIndicator?.value }}</span>
            <span class="value-status" :class="selectedIndicator?.status">
              {{ getIndicatorStatusText(selectedIndicator?.status) }}
            </span>
          </div>
          
          <div class="indicator-description">
            <h4>指标说明</h4>
            <p>{{ selectedIndicator?.description }}</p>
          </div>
          
          <div class="indicator-technology">
            <h4>检测技术</h4>
            <div class="tech-tags">
              <span v-for="tech in selectedIndicator?.technologies" :key="tech" class="tech-tag">
                {{ tech }}
              </span>
            </div>
          </div>
          
          <div class="indicator-importance">
            <h4>安全重要性</h4>
            <p>{{ selectedIndicator?.importance }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 智能交通系统弹窗 -->
    <div v-if="showTrafficDialog" class="traffic-dialog-modal" @click="showTrafficDialog = false">
      <div class="traffic-dialog-content" @click.stop>
        <div class="traffic-dialog-header">
          <h3>🚦 智能交通管理系统</h3>
          <button class="close-btn" @click="showTrafficDialog = false">×</button>
        </div>
        <div class="traffic-dialog-body">
          <div class="traffic-stats">
            <div class="stat-item">
              <div class="stat-value">35%</div>
              <div class="stat-label">拥堵减少</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">28%</div>
              <div class="stat-label">通行效率提升</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">42%</div>
              <div class="stat-label">事故率降低</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">15%</div>
              <div class="stat-label">能耗减少</div>
            </div>
          </div>
          
          <div class="traffic-features">
            <h4>核心功能</h4>
            <div class="feature-list">
              <div class="feature-item">
                <span class="feature-icon">🚦</span>
                <span class="feature-text">智能信号灯控制</span>
              </div>
              <div class="feature-item">
                <span class="feature-icon">📊</span>
                <span class="feature-text">实时流量监测</span>
              </div>
              <div class="feature-item">
                <span class="feature-icon">🗺️</span>
                <span class="feature-text">动态路径规划</span>
              </div>
              <div class="feature-item">
                <span class="feature-icon">⚠️</span>
                <span class="feature-text">事故预警系统</span>
              </div>
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
const drivingHovered = ref(false)
const showIndicatorDetail = ref(false)
const selectedIndicator = ref(null)
const showTrafficDialog = ref(false)

// 驾驶指标数据
const drivingIndicators = ref([
  { 
    id: 'speed', 
    name: '车速', 
    value: '65 km/h', 
    status: 'normal',
    style: { top: '20%', left: '15%' },
    description: '当前车辆行驶速度，通过GPS和车轮转速传感器精确测量，是自动驾驶系统的基础参数之一。',
    technologies: ['GPS定位', '车轮转速传感器', '惯性测量单元'],
    importance: '车速控制是自动驾驶安全的核心，系统需要根据路况、限速标志和前方车辆距离动态调整速度。'
  },
  { 
    id: 'distance', 
    name: '跟车距离', 
    value: '45m', 
    status: 'safe',
    style: { top: '15%', right: '20%' },
    description: '与前方车辆的安全距离，通过雷达和摄像头实时监测，确保有足够的制动距离。',
    technologies: ['毫米波雷达', '激光雷达', '立体摄像头'],
    importance: '保持安全跟车距离是防止追尾事故的关键，系统会根据车速和路况自动调整跟车距离。'
  },
  { 
    id: 'lane', 
    name: '车道偏离', 
    value: '0.2m', 
    status: 'warning',
    style: { top: '50%', left: '10%' },
    description: '车辆相对于车道中心线的偏移距离，通过图像识别技术检测车道线位置。',
    technologies: ['计算机视觉', '车道线识别', '图像处理算法'],
    importance: '车道保持是自动驾驶的基本功能，防止车辆无意识偏离车道造成安全隐患。'
  },
  { 
    id: 'obstacles', 
    name: '障碍物检测', 
    value: '3个', 
    status: 'alert',
    style: { bottom: '30%', right: '15%' },
    description: '周围环境中检测到的障碍物数量，包括车辆、行人、自行车等动态和静态物体。',
    technologies: ['360度摄像头', '超声波传感器', '目标识别算法'],
    importance: '准确的障碍物检测是避免碰撞的前提，系统需要实时识别并预测障碍物的运动轨迹。'
  },
  { 
    id: 'weather', 
    name: '天气状况', 
    value: '晴朗', 
    status: 'good',
    style: { bottom: '20%', left: '25%' },
    description: '当前天气条件，影响传感器性能和驾驶策略，系统会根据天气调整驾驶模式。',
    technologies: ['环境传感器', '雨量传感器', '能见度检测'],
    importance: '恶劣天气会影响传感器精度和路面条件，系统需要相应调整驾驶策略以确保安全。'
  },
  { 
    id: 'battery', 
    name: '电池电量', 
    value: '78%', 
    status: 'normal',
    style: { bottom: '15%', right: '30%' },
    description: '电动车辆的剩余电量，影响续航里程和路径规划，系统会智能规划充电站路线。',
    technologies: ['电池管理系统', '能耗算法', '充电站导航'],
    importance: '电量管理关系到行程安全，系统需要预测续航里程并提前规划充电路线。'
  }
])

// 方法
const handleDrivingHover = () => {
  drivingHovered.value = true
}

const handleDrivingLeave = () => {
  drivingHovered.value = false
}

const handleIndicatorClick = (indicator) => {
  // 点击了驾驶指标
  selectedIndicator.value = indicator
  showIndicatorDetail.value = true
  emit('interaction')
}

const closeIndicatorDetail = () => {
  showIndicatorDetail.value = false
  selectedIndicator.value = null
}

const onImageClick = (imageType) => {
  emit('image-click', imageType)
  emit('interaction')
  // 点击了图片
}

const getIndicatorStatusText = (status) => {
  const statusMap = {
    'normal': '正常',
    'safe': '安全',
    'good': '良好',
    'warning': '警告',
    'alert': '注意'
  }
  return statusMap[status] || '未知'
}
</script>

<style scoped>
/* 智慧驾驶板块样式 */
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

.indicator-overlays {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
}

.indicator-overlay {
  position: absolute;
  width: 50px;
  height: 50px;
  background: rgba(255, 193, 7, 0.9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all var(--transition-normal);
  animation: pulse 2s infinite;
}

.indicator-overlay:hover {
  transform: scale(1.2);
  background: rgba(255, 193, 7, 1);
}

.indicator-overlay:hover .indicator-info {
  opacity: 1;
  visibility: visible;
}

.indicator-info {
  position: absolute;
  top: 60px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--card-bg);
  padding: 1rem;
  border-radius: 8px;
  box-shadow: var(--box-shadow);
  min-width: 180px;
  opacity: 0;
  visibility: hidden;
  transition: all var(--transition-normal);
  z-index: 10;
}

.indicator-name {
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: 0.25rem;
}

.indicator-value {
  color: var(--accent-color);
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 0.25rem;
}

.indicator-status {
  font-size: 0.9rem;
  padding: 0.2rem 0.5rem;
  border-radius: 12px;
  color: white;
}

.indicator-status.normal {
  background: var(--success-color);
}

.indicator-status.safe {
  background: var(--success-color);
}

.indicator-status.good {
  background: var(--success-color);
}

.indicator-status.warning {
  background: var(--warning-color);
}

.indicator-status.alert {
  background: var(--error-color);
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
.indicator-detail-modal,
.traffic-dialog-modal {
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

.indicator-detail-content,
.traffic-dialog-content {
  background: var(--card-bg);
  border-radius: 16px;
  max-width: 600px;
  width: 100%;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: var(--box-shadow);
}

.indicator-detail-header,
.traffic-dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
}

.indicator-detail-header h3,
.traffic-dialog-header h3 {
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

.indicator-detail-body {
  padding: 1.5rem;
}

.indicator-current-value {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
  padding: 1rem;
  background: var(--secondary-color);
  border-radius: 8px;
}

.value-label {
  color: var(--text-secondary-color);
  font-weight: 500;
}

.value-number {
  color: var(--accent-color);
  font-size: 1.5rem;
  font-weight: 700;
}

.value-status {
  padding: 0.3rem 0.8rem;
  border-radius: 16px;
  font-size: 0.9rem;
  color: white;
}

.indicator-description,
.indicator-technology,
.indicator-importance {
  margin-bottom: 1.5rem;
}

.indicator-description h4,
.indicator-technology h4,
.indicator-importance h4 {
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.indicator-description p,
.indicator-importance p {
  color: var(--text-secondary-color);
  line-height: 1.6;
  margin: 0;
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

/* 交通系统弹窗样式 */
.traffic-dialog-body {
  padding: 1.5rem;
}

.traffic-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 1rem;
  margin-bottom: 2rem;
}

.stat-item {
  text-align: center;
  padding: 1rem;
  background: var(--secondary-color);
  border-radius: 12px;
}

.stat-value {
  font-size: 2rem;
  font-weight: 700;
  color: var(--accent-color);
  margin-bottom: 0.5rem;
}

.stat-label {
  color: var(--text-secondary-color);
  font-size: 0.9rem;
}

.traffic-features h4 {
  color: var(--text-color);
  margin-bottom: 1rem;
}

.feature-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 0.5rem;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.8rem;
  background: var(--secondary-color);
  border-radius: 8px;
}

.feature-icon {
  font-size: 1.2rem;
}

.feature-text {
  color: var(--text-color);
  font-weight: 500;
}

/* 动画 */
@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 193, 7, 0.7);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(255, 193, 7, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(255, 193, 7, 0);
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
  
  .traffic-stats {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .feature-list {
    grid-template-columns: 1fr;
  }
}

/* 浅色主题样式已通过 theme.css 统一管理 */
</style>