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
      <!-- 智能娱乐板块 -->    
    <div class="case-section">
      <h2 class="section-title">🎬 智能娱乐与个性化推荐</h2>
      <p class="section-description">探索AI如何革新娱乐体验，通过深度学习和大数据分析实现精准的个性化内容推荐</p>
      
      <div class="entertainment-grid">
        <div 
          v-for="entertainment in entertainmentCases" 
          :key="entertainment.id"
          class="entertainment-card interactive-element float-animation"
          @click="handleEntertainmentClick(entertainment)"
        >
          <div class="card-header">
            <div class="platform-icon">
              <i :class="getEntertainmentIcon(entertainment.id)"></i>
            </div>
            <div class="card-title">
              <h3>{{ entertainment.name }}</h3>
              <span class="platform-name">{{ entertainment.platform }}</span>
            </div>
          </div>
          
          <p class="card-description">{{ entertainment.description }}</p>
          
          <div class="tech-tags">
            <span 
              v-for="tech in entertainment.technologies" 
              :key="tech"
              class="tech-tag"
            >
              {{ tech }}
            </span>
          </div>
          
          <div class="metrics-preview">
            <div class="metric-item">
              <span class="metric-label">用户规模</span>
              <span class="metric-value">{{ entertainment.metrics.dailyActiveUsers || entertainment.metrics.globalSubscribers || entertainment.metrics.monthlyActiveUsers }}</span>
            </div>
            <div class="metric-item">
              <span class="metric-label">推荐准确率</span>
              <span class="metric-value">{{ entertainment.metrics.clickThroughRate || entertainment.metrics.recommendationAccuracy || entertainment.metrics.discoveryRate }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 娱乐案例详情弹窗 -->
      <div v-if="selectedEntertainment" class="entertainment-modal" @click="selectedEntertainment = null">
        <div class="modal-content" @click.stop>
          <div class="modal-header">
            <h3>{{ selectedEntertainment.name }}</h3>
            <button class="close-btn" @click="selectedEntertainment = null">×</button>
          </div>
          
          <div class="modal-body">
            <div class="platform-info">
              <span class="platform-badge">{{ selectedEntertainment.platform }}</span>
              <p class="description">{{ selectedEntertainment.description }}</p>
            </div>
            
            <div class="features-section">
              <h4>核心技术特性</h4>
              <ul class="features-list">
                <li v-for="feature in selectedEntertainment.features" :key="feature">
                  {{ feature }}
                </li>
              </ul>
            </div>
            
            <div class="metrics-section">
              <h4>关键指标</h4>
              <div class="metrics-grid">
                <div v-for="(value, key) in selectedEntertainment.metrics" :key="key" class="metric-card">
                  <span class="metric-name">{{ getMetricName(key) }}</span>
                  <span class="metric-number">{{ value }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

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
                  以特斯拉Autopilot为代表的自动驾驶技术，通过8个摄像头、12个超声波传感器和前向雷达，
                  结合神经网络算法实现L2级自动驾驶。系统能识别车道线、交通标志、行人车辆，自动变道超车，大幅提升驾驶安全性。
                </p>
                <!-- 自动驾驶场景详细展示 -->
                <div class="driving-scenarios-grid">
                  <div 
                    v-for="scenario in drivingScenarios" 
                    :key="scenario.id"
                    class="scenario-card" 
                    :class="{ active: scenario.active }"
                    @click="handleDrivingScenarioClick(scenario)"
                  >
                    <div class="scenario-header">
                      <h5>{{ scenario.name }}</h5>
                      <span class="company-tag">{{ scenario.company }}</span>
                    </div>
                    <p class="scenario-description">{{ scenario.description }}</p>
                  </div>
                </div>
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
                  百度萝卜快跑作为全球最大的自动驾驶出行服务平台，在北京、上海、广州等城市提供无人驾驶出租车服务。
                  通过V2X车路协同技术，实现车辆与交通设施的实时通信，优化路径规划，已累计服务超过600万人次。
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
                  DeepMind开发的眼底疾病AI诊断系统，能在30秒内分析眼底照片，识别糖尿病视网膜病变、青光眼等50多种眼疾，
                  准确率达94%以上，已在英国NHS医院部署，为偏远地区提供专家级诊断服务。
                </p>
              </div>
            </div>

            <!-- 智慧医疗案例展示 -->
            <div class="content-card medical-cases-card">
              <div class="card-header">
                <h4>智慧医疗应用案例</h4>
              </div>
              <div class="card-content">
                <div class="medical-cases-grid">
                  <div 
                    v-for="medicalCase in medicalCases" 
                    :key="medicalCase.id"
                    class="medical-case-item interactive-element glow-animation"
                    @click="handleMedicalCaseClick(medicalCase)"
                  >
                    <h5>{{ medicalCase.title }}</h5>
                    <p class="case-description">{{ medicalCase.description }}</p>
                    <div class="case-tech">
                      <span class="tech-tag">{{ medicalCase.technology }}</span>
                    </div>
                    <div class="case-impact">
                      <small>{{ medicalCase.impact }}</small>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </transition>

      <!-- 个性化医疗板块 -->
      <transition name="slide-up" mode="out-in">
        <div v-if="currentSection === 3" class="section personalized-medicine" key="personalized-medicine">
          <div class="section-header">
            <h3 class="section-title">
              <span class="title-icon">🧬</span>
              <span class="title-text">个性化医疗</span>
            </h3>
            <p class="section-subtitle">基因科技开启精准医疗新时代</p>
          </div>

          <div class="content-grid">
            <!-- 基因检测与分析 -->
            <div class="content-card gene-analysis-card">
              <div class="card-header">
                <h4>基因检测与疾病预测</h4>
              </div>
              <div class="image-container">
                <img 
                  src="/images/chapter4/gene-analysis.jpg" 
                  alt="基因检测分析图"
                  class="main-image"
                  @click="handleGeneAnalysisClick"
                />
                <!-- DNA螺旋动画 -->
                <div class="dna-helix">
                  <div v-for="n in 8" :key="n" class="dna-strand" :style="{ animationDelay: n * 0.2 + 's' }"></div>
                </div>
              </div>
              <div class="card-content">
                <p class="card-description">
                  23andMe通过分析个人基因组数据，可预测阿尔茨海默病、帕金森病等遗传性疾病风险，
                  分析药物代谢能力，提供个性化健康建议。已为全球1200万+用户提供基因检测服务。
                </p>
                <!-- 基因检测项目展示 -->
                <div class="gene-tests-grid">
                  <div 
                    v-for="test in geneTests" 
                    :key="test.id"
                    class="gene-test-item interactive-element pulse-animation" 
                    @click="handleGeneTestClick(test)"
                  >
                    <div class="test-icon">{{ test.icon }}</div>
                    <h5>{{ test.name }}</h5>
                    <p class="test-description">{{ test.description }}</p>
                    <div class="test-accuracy">
                      <span class="accuracy-label">准确率:</span>
                      <span class="accuracy-value">{{ test.accuracy }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 精准治疗 -->
            <div class="content-card precision-treatment-card">
              <div class="card-header">
                <h4>精准治疗方案</h4>
              </div>
              <div class="image-container">
                <img 
                  src="/images/chapter4/precision-medicine.jpg" 
                  alt="精准医疗示意图"
                  class="main-image"
                  @click="handlePrecisionMedicineClick"
                />
              </div>
              <div class="card-content">
                <p class="card-description">
                  基于患者基因突变数据，AI系统可为肿瘤患者推荐最适合的靶向药物，
                  预测治疗效果，制定个性化治疗方案，显著提高治疗成功率和患者生存期。
                </p>
                <!-- 治疗方案统计 -->
                <div class="treatment-stats">
                  <div class="stat-item">
                    <div class="stat-number">85%</div>
                    <div class="stat-label">药物匹配准确率</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-number">30%</div>
                    <div class="stat-label">生存期延长</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-number">40%</div>
                    <div class="stat-label">治疗响应率提升</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </transition>

      <!-- 远程医疗板块 -->
      <transition name="slide-down" mode="out-in">
        <div v-if="currentSection === 4" class="section telemedicine" key="telemedicine">
          <div class="section-header">
            <h3 class="section-title">
              <span class="title-icon">📱</span>
              <span class="title-text">远程医疗</span>
            </h3>
            <p class="section-subtitle">智能设备守护健康每一刻</p>
          </div>

          <div class="content-grid">
            <!-- 智能问诊 -->
            <div class="content-card smart-consultation-card">
              <div class="card-header">
                <h4>AI智能问诊</h4>
              </div>
              <div class="image-container">
                <img 
                  src="/images/chapter4/smart-consultation.jpg" 
                  alt="智能问诊界面"
                  class="main-image"
                  @click="handleSmartConsultationClick"
                />
                <!-- 对话气泡动画 -->
                <div class="chat-bubbles">
                  <div v-for="n in 3" :key="n" class="chat-bubble" :style="{ animationDelay: n * 0.8 + 's' }"></div>
                </div>
              </div>
              <div class="card-content">
                <p class="card-description">
                  Babylon Health的AI医生助手提供24/7在线问诊服务，通过自然语言处理技术理解患者症状，
                  提供初步诊断建议，必要时安排专科医生咨询，已服务超过2400万用户。
                </p>
              </div>
            </div>

            <!-- 健康监测 -->
            <div class="content-card health-monitoring-card">
              <div class="card-header">
                <h4>智能健康监测</h4>
              </div>
              <div class="image-container">
                <img 
                  src="/images/chapter4/health-monitoring.jpg" 
                  alt="健康监测设备"
                  class="main-image"
                  @click="handleHealthMonitoringClick"
                />
                <!-- 心率波形动画 -->
                <div class="heartbeat-wave">
                  <div class="wave-line"></div>
                </div>
              </div>
              <div class="card-content">
                <p class="card-description">
                  Apple Watch通过光学心率传感器实时监测心率变化，智能识别房颤等心律不齐，
                  自动检测跌倒并发送紧急求助，已获FDA医疗器械认证，挽救了数千生命。
                </p>
                <!-- 健康监测指标 -->
                <div class="health-metrics">
                  <div class="metric-item">
                    <div class="metric-icon">❤️</div>
                    <div class="metric-info">
                      <div class="metric-value">98%</div>
                      <div class="metric-label">房颤检测准确率</div>
                    </div>
                  </div>
                  <div class="metric-item">
                    <div class="metric-icon">🚨</div>
                    <div class="metric-info">
                      <div class="metric-value">数千</div>
                      <div class="metric-label">挽救生命数量</div>
                    </div>
                  </div>
                </div>
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

// 智能家居相关 - 基于小米全屋智能系统
const smartHomeHovered = ref(false)
const selectedDevice = ref(null)
const selectedScenario = ref(null)
const selectedMarker = ref(null)
const selectedEntertainment = ref(null)
const entertainmentCases = ref([
  {
    id: 'douyin',
    name: '抖音个性化视频流',
    platform: '字节跳动',
    description: '基于用户行为数据和视频内容特征的智能推荐系统',
    technologies: ['深度学习', '协同过滤', '内容理解', '实时计算'],
    features: [
      '用户行为数据解析：点赞、评论、分享、完播率等多维度数据收集',
      '视频内容特征提取：音频识别、图像理解、文本分析、场景检测',
      '混合推荐算法：协同过滤 + 内容推荐 + 深度学习模型融合',
      '实时反馈优化：毫秒级响应，动态调整推荐策略'
    ],
    metrics: {
      dailyActiveUsers: '6亿+',
      avgWatchTime: '52分钟',
      clickThroughRate: '8.2%',
      userSatisfaction: '89%'
    }
  },
  {
    id: 'netflix',
    name: 'Netflix影视推荐',
    platform: 'Netflix',
    description: '全球领先的个性化影视内容推荐引擎',
    technologies: ['机器学习', '协同过滤', '内容分析', 'A/B测试'],
    features: [
      '协同过滤算法：基于用户相似性和物品相似性的双重过滤',
      '内容推荐系统：影片类型、演员、导演、情节等多维度匹配',
      '混合推荐模型：多种算法融合，提升推荐准确性',
      '个性化排序：根据用户偏好动态调整内容展示顺序'
    ],
    metrics: {
      globalSubscribers: '2.3亿+',
      recommendationAccuracy: '75%',
      contentEngagement: '80%',
      churnReduction: '减少15%'
    }
  },
  {
    id: 'spotify',
    name: 'Spotify音乐发现',
    platform: 'Spotify',
    description: '基于音频特征和用户偏好的智能音乐推荐',
    technologies: ['音频分析', '自然语言处理', '深度学习', '图神经网络'],
    features: [
      '音频特征分析：节拍、调性、能量、舞蹈性等音乐特征提取',
      '歌词情感分析：自然语言处理技术分析歌词情感和主题',
      '用户画像构建：听歌历史、跳过行为、播放列表分析',
      '社交推荐：好友音乐偏好和社交网络影响因素'
    ],
    metrics: {
      monthlyActiveUsers: '4.8亿+',
      discoveryRate: '31%',
      playlistCompletion: '67%',
      userRetention: '提升23%'
    }
  }
])
const smartDevices = ref([
  { id: 'light', name: 'Yeelight智能灯', brand: '小米生态链', features: ['色温调节', '亮度调节', '场景模式'], style: { top: '20%', left: '15%' } },
  { id: 'ac', name: '米家空调', brand: '小米', features: ['远程控制', '定时开关', '节能模式'], style: { top: '15%', right: '20%' } },
  { id: 'camera', name: '小白摄像头', brand: '小米', features: ['1080P高清', '夜视功能', '移动侦测'], style: { top: '10%', right: '10%' } },
  { id: 'speaker', name: '小爱音箱Pro', brand: '小米', features: ['语音控制', '智能联动', '音乐播放'], style: { bottom: '25%', left: '20%' } },
  { id: 'robot', name: '米家扫地机器人', brand: '小米', features: ['激光导航', '智能规划', '自动回充'], style: { bottom: '30%', right: '25%' } },
  { id: 'purifier', name: '小米净化器', brand: '小米', features: ['PM2.5检测', '智能净化', '静音模式'], style: { top: '60%', left: '25%' } }
])

// 语音助手对话
const showVoiceDialog = ref(false)
const voiceConversation = ref([
  { type: 'user', speaker: '用户', text: '小爱同学，帮我调节一下客厅的温度' },
  { type: 'assistant', speaker: '小爱同学', text: '好的，我正在为您调节客厅空调温度到24度，制冷模式' },
  { type: 'user', speaker: '用户', text: '顺便把客厅的灯光调暗一些，播放轻音乐' },
  { type: 'assistant', speaker: '小爱同学', text: '已为您调节Yeelight灯光亮度到40%，正在播放轻音乐，还有其他需要帮助的吗？' },
  { type: 'user', speaker: '用户', text: '启动扫地机器人清扫' },
  { type: 'assistant', speaker: '小爱同学', text: '米家扫地机器人已开始清扫，预计30分钟完成，清扫完成后会自动回充' }
])

// 自动驾驶相关 - 特斯拉Autopilot和百度萝卜快跑
const drivingAnimationActive = ref(false)
const drivingScenarios = ref([
  {
    id: 'tesla-autopilot',
    name: '特斯拉Autopilot',
    company: 'Tesla',
    description: '基于视觉感知的完全自动驾驶技术，通过8个摄像头、12个超声波传感器实现L2+级自动驾驶',
    active: false,
    technologies: ['计算机视觉', '神经网络', '传感器融合', '实时决策'],
    features: [
      '视觉感知系统：8个摄像头提供360度视野覆盖',
      'FSD芯片：每秒处理2300帧图像，算力达144TOPS',
      '神经网络决策：基于大量真实驾驶数据训练的深度学习模型',
      '持续学习：通过影子模式收集数据，不断优化算法性能'
    ],
    achievements: {
      deployedVehicles: '500万+',
      milesAutopilot: '60亿英里',
      safetyImprovement: '事故率降低10倍',
      marketShare: '全球第一'
    }
  },
  {
    id: 'baidu-apollo',
    name: '百度萝卜快跑',
    company: '百度',
    description: 'L4级自动驾驶出行服务，已在北京、上海等多个城市开展商业化运营',
    active: false,
    technologies: ['激光雷达', '高精地图', 'Apollo平台', '5G通信'],
    features: [
      'Apollo平台：开放式自动驾驶软件平台，支持多种传感器配置',
      'L4级自动驾驶：在特定区域实现完全无人驾驶',
      '商业化运营：在北京、上海、广州等城市提供Robotaxi服务',
      '安全冗余设计：多重安全保障机制，确保乘客安全'
    ],
    achievements: {
      operatingCities: '30+城市',
      totalOrders: '600万+',
      fleetSize: '500+车辆',
      testMiles: '5000万公里'
    }
  },
  {
    id: 'zhongguancun-lights',
    name: '中关村智能信号灯',
    company: '北京交通委',
    description: 'AI动态调整配时技术，通过实时车流分析优化信号灯配时，显著提升通行效率',
    active: false,
    technologies: ['AI算法', '车流检测', '动态配时', '协同优化'],
    features: [
      '实时车流监测：地磁传感器和高清摄像头收集交通数据',
      'AI动态配时：根据车流量自动调整红绿灯时长',
      '区域协同：多路口联动优化，形成绿波带',
      '特殊人群关怀：识别老人儿童，智能延长通行时间'
    ],
    achievements: {
      efficiencyImprovement: '通行效率提升30%',
      waitTimeReduction: '等待时间从135秒降至49秒',
      violationReduction: '违规停靠减少72%',
      maintenanceImprovement: '维修响应速度提升60%'
    }
  },
  {
    id: 'c-v2x-system',
    name: 'C-V2X车路协同',
    company: '华为/大唐',
    description: '基于蜂窝网络的车用无线通信技术，实现车车、车路、车人全方位信息交互',
    active: false,
    technologies: ['5G通信', 'V2V/V2I/V2P', '边缘计算', '路侧感知'],
    features: [
      '低时延通信：毫秒级信息传输，确保实时性',
      '路口盲区预警：提前发现盲区车辆和行人，避免碰撞',
      '协同感知：车辆与路侧设备共享感知信息',
      '交通优化：实时路况信息支持智能路径规划'
    ],
    achievements: {
      coverageArea: '全国重点城市',
      connectedVehicles: '100万+',
      accidentReduction: '交通事故减少25%',
      standardization: '国际标准制定参与者'
    }
  }
])

// 医疗影像相关 - DeepMind眼底识别和肺结节AI检测
const medicalImageClicked = ref(false)
const showLesionDetail = ref(false)
const selectedLesion = ref(null)
 
 // 智慧医疗扩展案例
 const medicalCases = ref([
   {
     id: 'deepmind_eye',
     title: 'DeepMind眼底疾病AI诊断',
     description: '通过深度学习分析眼底照片，准确识别糖尿病视网膜病变等50多种眼部疾病',
     technology: '深度学习 + 医学影像分析',
     impact: '已在英国NHS医院部署，服务偏远地区',
     technologies: ['深度学习', '图像识别', '医学影像', '卷积神经网络'],
     features: [
       '多疾病检测：可识别糖尿病视网膜病变、青光眼、黄斑变性等50+种眼疾',
       '高精度诊断：在糖尿病视网膜病变检测中达到94.5%的准确率',
       '实时分析：几秒内完成眼底照片分析，大幅提升筛查效率',
       '临床部署：已在英国NHS系统和印度Aravind眼科医院投入使用'
     ],
     achievements: {
       accuracy: '诊断准确率94.5%',
       deployment: '英国NHS系统部署',
       screening: '每年筛查数百万患者',
       prevention: '预防失明风险降低23%'
     }
   },
   {
     id: 'lung_nodule_ai',
     title: '肺结节AI检测系统',
     description: 'AI辅助医生快速准确检测肺部结节，提高早期肺癌筛查效率',
     technology: '卷积神经网络 + 3D图像处理',
     impact: '提高早期肺癌检出率30%以上',
     technologies: ['卷积神经网络', '3D图像分析', '医学影像', '深度学习'],
     features: [
       '3D影像分析：处理CT扫描的三维数据，精确定位结节位置',
       '恶性风险评估：不仅检测结节，还评估恶性概率',
       '多尺度检测：可检测2mm以上的微小结节',
       '辅助诊断：为医生提供详细的影像标注和诊断建议'
     ],
     achievements: {
       sensitivity: '检测敏感性95%',
       falsePositive: '假阳性率降低50%',
       earlyDetection: '早期肺癌检出率提升20%',
       efficiency: '诊断时间缩短60%'
     }
   },
   {
     id: 'watson_oncology',
     title: '肿瘤靶向药推荐系统',
     description: '基于基因突变数据分析，为肿瘤患者推荐最适合的靶向治疗方案',
     technology: '自然语言处理 + 知识图谱',
     impact: '已为全球数万癌症患者提供治疗建议',
     technologies: ['基因组学', '机器学习', '精准医疗', '大数据分析'],
     features: [
       '基因突变分析：检测肿瘤组织中的关键基因突变',
       '药物匹配：根据突变类型推荐对应的靶向药物',
       '疗效预测：预测不同治疗方案的可能效果',
       '个性化方案：为每位患者制定个性化治疗策略'
     ],
     achievements: {
       matchingAccuracy: '药物匹配准确率85%',
       survivalImprovement: '患者生存期延长30%',
       treatmentResponse: '治疗响应率提升40%',
       costReduction: '治疗成本降低25%'
     }
   },
   {
     id: 'gene_analysis',
     title: '23andMe基因检测',
     description: '通过基因分析预测疾病风险，提供个性化健康建议',
     technology: '基因组学 + 机器学习',
     impact: '已为1200万+用户提供基因检测服务',
     technologies: ['基因组学', '生物信息学', '统计学', '机器学习'],
     features: [
       '疾病风险评估：预测阿尔茨海默病、帕金森病等遗传性疾病风险',
       '药物反应预测：分析个体对特定药物的代谢能力',
       '祖源分析：追溯用户的遗传祖先和地理起源',
       '特征预测：预测身高、体重、运动能力等遗传特征'
     ],
     achievements: {
       userBase: '1200万+用户',
       geneticVariants: '检测70万+遗传变异',
       healthReports: '提供200+健康报告',
       research: '参与1000+科学研究项目'
     }
   },
   {
     id: 'babylon_health',
     title: 'Babylon Health智能问诊',
     description: 'AI医生助手提供24/7在线问诊和健康咨询',
     technology: '自然语言处理 + 医学知识库',
     impact: '服务超过2400万用户，减少医院就诊压力',
     technologies: ['自然语言处理', '医学知识图谱', '机器学习', '语音识别'],
     features: [
       '智能问诊：通过对话了解症状，提供初步诊断建议',
       '健康监测：持续跟踪用户健康状态，提供个性化建议',
       '药物提醒：智能用药提醒和药物相互作用检查',
       '专家转诊：必要时安排专科医生在线咨询'
     ],
     achievements: {
       userBase: '2400万+用户',
       consultations: '每月100万+次咨询',
       accuracy: '诊断准确率92%',
       satisfaction: '用户满意度95%'
     }
   },
   {
     id: 'apple_watch_health',
     title: 'Apple Watch心率监测',
     description: '实时监测心率异常，及时预警房颤等心脏疾病',
     technology: '光学心率传感器 + 机器学习算法',
     impact: '已挽救数千生命，获FDA医疗器械认证',
     technologies: ['光学传感器', '机器学习', '信号处理', '移动健康'],
     features: [
       '连续心率监测：24小时实时监测心率变化',
       '房颤检测：智能识别心房颤动等心律不齐',
       '跌倒检测：自动检测意外跌倒并发送紧急求助',
       '健康数据整合：与iPhone健康应用无缝集成'
     ],
     achievements: {
       userBase: '1亿+用户',
       liveSaved: '挽救数千生命',
       fdaApproval: 'FDA医疗器械认证',
       accuracy: '房颤检测准确率98%'
     }
   }
 ])

 // 基因检测项目数据
 const geneTests = ref([
   {
     id: 'disease-risk',
     name: '疾病风险评估',
     description: '预测阿尔茨海默病、帕金森病等遗传性疾病风险',
     icon: '🧠',
     accuracy: '85-95%'
   },
   {
     id: 'drug-response',
     name: '药物反应预测',
     description: '分析个体对特定药物的代谢能力和副作用风险',
     icon: '💊',
     accuracy: '90-98%'
   },
   {
     id: 'ancestry-analysis',
     name: '祖源分析',
     description: '追溯用户的遗传祖先和地理起源',
     icon: '🌍',
     accuracy: '99%'
   },
   {
     id: 'trait-prediction',
     name: '特征预测',
     description: '预测身高、体重、运动能力等遗传特征',
     icon: '🏃',
     accuracy: '70-85%'
   }
 ])

const lesionMarkers = ref([
  { 
    id: 'lesion1', 
    name: '糖尿病视网膜病变',
    location: '视网膜黄斑区',
    size: '微血管瘤群',
    confidence: '94.2%',
    recommendation: 'DeepMind AI建议立即转诊眼科专家',
    system: 'DeepMind眼底AI诊断系统',
    style: { top: '35%', left: '45%' } 
  },
  { 
    id: 'lesion2', 
    name: '疑似肺结节',
    location: '右肺上叶',
    size: '8.5mm × 6.2mm',
    confidence: '92.3%',
    recommendation: 'IBM Watson建议进一步CT增强扫描',
    system: 'IBM Watson肺结节检测',
    style: { top: '60%', right: '35%' } 
  },
  { 
    id: 'lesion3', 
    name: '青光眼早期征象',
    location: '视神经盘',
    size: '杯盘比0.7',
    confidence: '89.6%',
    recommendation: 'DeepMind AI建议眼压检查和视野检测',
    system: 'DeepMind眼底AI诊断系统',
    style: { top: '25%', right: '25%' } 
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

const handleMedicalCaseClick = (medicalCase) => {
  console.log(`点击了医疗案例: ${medicalCase.title}`)
  incrementInteraction()
}

const handleDrivingScenarioClick = (scenario) => {
  scenario.active = !scenario.active
  console.log(`点击了自动驾驶场景: ${scenario.name} - ${scenario.company}`)
  incrementInteraction()
}

// 智能娱乐相关方法
const handleEntertainmentClick = (entertainment) => {
  selectedEntertainment.value = entertainment
  incrementInteraction()
}

const getEntertainmentIcon = (id) => {
  const icons = {
    douyin: 'fas fa-video',
    netflix: 'fas fa-film', 
    spotify: 'fas fa-music'
  }
  return icons[id] || 'fas fa-play'
}

const getMetricName = (key) => {
  const names = {
    dailyActiveUsers: '日活用户',
    avgWatchTime: '平均观看时长',
    clickThroughRate: '点击率',
    userSatisfaction: '用户满意度',
    globalSubscribers: '全球订阅用户',
    recommendationAccuracy: '推荐准确率',
    contentEngagement: '内容参与度',
    churnReduction: '流失率降低',
    monthlyActiveUsers: '月活用户',
    discoveryRate: '发现率',
    playlistCompletion: '播放列表完成率',
    userRetention: '用户留存提升'
  }
  return names[key] || key
}

// 个性化医疗相关处理函数
const handleGeneAnalysisClick = () => {
  incrementInteraction()
  console.log('基因分析图片点击')
}

const handleGeneTestClick = (test) => {
  incrementInteraction()
  console.log('基因检测项目点击:', test.name)
}

const handlePrecisionMedicineClick = () => {
  incrementInteraction()
  console.log('精准医疗图片点击')
}

// 远程医疗相关处理函数
const handleSmartConsultationClick = () => {
  incrementInteraction()
  console.log('智能问诊图片点击')
}

const handleHealthMonitoringClick = () => {
  incrementInteraction()
  console.log('健康监测图片点击')
}

const handleSmartDeviceClick = (device) => {
  console.log(`点击了智能设备: ${device.name} - ${device.brand}`)
  incrementInteraction()
}

const getDeviceIcon = (deviceId) => {
  const iconMap = {
    'light': '💡',
    'ac': '❄️',
    'camera': '📹',
    'speaker': '🔊',
    'robot': '🤖',
    'purifier': '🌪️'
  }
  return iconMap[deviceId] || '📱'
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
  background: rgba(255, 255, 255, 0.1);
  border: 2px solid rgba(0, 122, 204, 0.5);
  border-radius: 12px;
  padding: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  transform: translate(-50%, -50%);
  min-width: 120px;
  backdrop-filter: blur(5px);
}

.device-overlay:hover {
  background: rgba(0, 122, 204, 0.2);
  border-color: #007acc;
  box-shadow: 0 4px 20px rgba(0, 122, 204, 0.4);
  transform: translate(-50%, -50%) scale(1.05);
}

.device-info {
  text-align: center;
  color: #fff;
}

.device-name {
  font-size: 0.9rem;
  font-weight: bold;
  margin-bottom: 2px;
}

.device-brand {
  font-size: 0.7rem;
  opacity: 0.8;
  margin-bottom: 4px;
}

.device-features {
  display: flex;
  flex-wrap: wrap;
  gap: 2px;
  justify-content: center;
}

.feature-tag {
  background: rgba(0, 122, 204, 0.3);
  color: #fff;
  padding: 1px 4px;
  border-radius: 6px;
  font-size: 0.6rem;
  border: 1px solid rgba(0, 122, 204, 0.5);
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
  transition: all 0.3s ease;
}

.lesion-marker:hover {
  transform: scale(1.2);
}

.marker-pulse {
  width: 100%;
  height: 100%;
  background: rgba(255, 0, 0, 0.3);
  border: 2px solid #ff0000;
  border-radius: 50%;
  animation: markerPulse 1.5s infinite;
}

.marker-tooltip {
  position: absolute;
  bottom: 120%;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.9);
  color: #fff;
  padding: 12px 16px;
  border-radius: 8px;
  font-size: 0.85rem;
  white-space: nowrap;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.3s ease;
  z-index: 10;
  min-width: 200px;
  text-align: left;
}

.lesion-marker:hover .marker-tooltip {
  opacity: 1;
}

.marker-tooltip::after {
  content: '';
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  border: 6px solid transparent;
  border-top-color: rgba(0, 0, 0, 0.9);
}

.tooltip-title {
  font-weight: bold;
  margin-bottom: 4px;
  color: #ff6b6b;
}

.tooltip-info {
  font-size: 0.8rem;
  line-height: 1.3;
  margin-bottom: 2px;
}

.tooltip-confidence {
  color: #4caf50;
  font-weight: bold;
}

.tooltip-system {
  color: #2196f3;
  font-style: italic;
  font-size: 0.75rem;
  margin-top: 4px;
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

/* 医疗案例网格样式 */
.medical-cases-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 15px;
  max-height: 400px;
  overflow-y: auto;
}

/* 自动驾驶场景网格样式 */
.driving-scenarios-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
  margin-top: 20px;
}

.scenario-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.scenario-card:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.scenario-card.active {
  background: rgba(0, 122, 204, 0.3);
  border-color: rgba(0, 122, 204, 0.5);
  box-shadow: 0 4px 15px rgba(0, 122, 204, 0.3);
}

.scenario-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.scenario-header h5 {
  margin: 0;
  font-size: 0.9rem;
  color: #fff;
  line-height: 1.3;
}

.company-tag {
  background: rgba(0, 122, 204, 0.4);
  color: #fff;
  padding: 2px 6px;
  border-radius: 8px;
  font-size: 0.7rem;
  border: 1px solid rgba(0, 122, 204, 0.6);
  white-space: nowrap;
}

.scenario-description {
  font-size: 0.8rem;
  line-height: 1.3;
  margin: 0;
  opacity: 0.9;
}

.medical-case-item {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.medical-case-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.medical-case-item h5 {
  margin: 0 0 8px 0;
  font-size: 1rem;
  color: #fff;
}

.medical-case-item .case-description {
  font-size: 0.9rem;
  line-height: 1.4;
  margin-bottom: 10px;
  opacity: 0.9;
}

.case-tech {
  margin-bottom: 8px;
}

.tech-tag {
  background: rgba(0, 122, 204, 0.3);
  color: #fff;
  padding: 3px 8px;
  border-radius: 12px;
  font-size: 0.8rem;
  border: 1px solid rgba(0, 122, 204, 0.5);
}

.case-impact {
  opacity: 0.8;
  font-style: italic;
}

.case-impact small {
  font-size: 0.8rem;
}

/* 智能娱乐板块样式 */
.entertainment-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 24px;
  margin-top: 24px;
}

.entertainment-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 24px;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.entertainment-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.entertainment-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0,0,0,0.2);
}

.entertainment-card:hover::before {
  opacity: 1;
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.platform-icon {
  width: 48px;
  height: 48px;
  background: rgba(255,255,255,0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.platform-icon i {
  font-size: 24px;
  color: white;
}

.card-title h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.platform-name {
  font-size: 14px;
  opacity: 0.8;
  display: block;
  margin-top: 4px;
}

.card-description {
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 16px;
  opacity: 0.9;
}

.tech-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

.tech-tag {
  background: rgba(255,255,255,0.2);
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.metrics-preview {
  display: flex;
  justify-content: space-between;
  gap: 16px;
}

.metric-item {
  flex: 1;
  text-align: center;
}

.metric-label {
  display: block;
  font-size: 12px;
  opacity: 0.8;
  margin-bottom: 4px;
}

.metric-value {
  display: block;
  font-size: 16px;
  font-weight: 600;
}

/* 娱乐案例弹窗样式 */
.entertainment-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.modal-content {
  background: white;
  border-radius: 16px;
  max-width: 600px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
  animation: slideUp 0.3s ease;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  color: #333;
  font-size: 20px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s ease;
}

.close-btn:hover {
  background: #f5f5f5;
  color: #333;
}

.modal-body {
  padding: 24px;
}

.platform-info {
  margin-bottom: 24px;
}

.platform-badge {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  display: inline-block;
  margin-bottom: 12px;
}

.description {
  color: #666;
  line-height: 1.6;
  margin: 0;
}

.features-section, .metrics-section {
  margin-bottom: 24px;
}

.features-section h4, .metrics-section h4 {
  color: #333;
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
}

.features-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.features-list li {
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
  color: #555;
  line-height: 1.5;
}

.features-list li:last-child {
  border-bottom: none;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 16px;
}

.metric-card {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 12px;
  text-align: center;
}

.metric-name {
  display: block;
  font-size: 12px;
  color: #666;
  margin-bottom: 8px;
}

.metric-number {
  display: block;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 个性化医疗板块样式 */
.personalized-medicine {
  margin-bottom: 60px;
}

.gene-analysis {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
  margin-bottom: 40px;
}

.gene-image {
  position: relative;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
}

.gene-image:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 40px rgba(0,0,0,0.2);
}

.gene-image img {
  width: 100%;
  height: 250px;
  object-fit: cover;
}

.gene-tests {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.gene-test-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.gene-test-card:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-3px);
}

.test-icon {
  font-size: 2rem;
  margin-bottom: 10px;
}

.test-name {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 8px;
}

.test-description {
  font-size: 0.9rem;
  opacity: 0.8;
  margin-bottom: 10px;
}

.test-accuracy {
  font-size: 0.8rem;
  color: #4caf50;
  font-weight: 600;
}

/* 远程医疗板块样式 */
.telemedicine {
  margin-bottom: 60px;
}

.telemedicine-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
}

.telemedicine-item {
  position: relative;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
}

.telemedicine-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 40px rgba(0,0,0,0.2);
}

.telemedicine-item img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.telemedicine-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0,0,0,0.8));
  padding: 20px;
  color: white;
}

.telemedicine-title {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 5px;
}

.telemedicine-description {
  font-size: 0.9rem;
  opacity: 0.9;
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
  
  .entertainment-grid {
    grid-template-columns: 1fr;
  }
  
  .entertainment-card {
    min-width: auto;
  }
  
  .gene-analysis {
    grid-template-columns: 1fr;
  }
  
  .telemedicine-grid {
    grid-template-columns: 1fr;
  }
  
  .gene-tests {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  }
}

/* 增强动效 */
@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

@keyframes glow {
  0%, 100% { box-shadow: 0 0 20px rgba(76, 175, 80, 0.3); }
  50% { box-shadow: 0 0 30px rgba(76, 175, 80, 0.6); }
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

.pulse-animation {
  animation: pulse 2s infinite;
}

.glow-animation {
  animation: glow 3s infinite;
}

.float-animation {
  animation: float 4s ease-in-out infinite;
}

/* 数据可视化增强 */
.progress-bar {
  width: 100%;
  height: 8px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
  overflow: hidden;
  margin-top: 10px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #4caf50, #81c784);
  border-radius: 4px;
  transition: width 1s ease;
  animation: shimmer 2s infinite;
}

@keyframes shimmer {
  0% { background-position: -200px 0; }
  100% { background-position: 200px 0; }
}

.metric-chart {
  position: relative;
  width: 60px;
  height: 60px;
  margin: 0 auto 10px;
}

.chart-circle {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: conic-gradient(#4caf50 0deg, #4caf50 var(--percentage), rgba(255,255,255,0.2) var(--percentage));
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 12px;
  animation: rotate 2s ease-in-out;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 交互反馈增强 */
.interactive-element {
  position: relative;
  overflow: hidden;
}

.interactive-element::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  transition: width 0.6s, height 0.6s;
}

.interactive-element:active::before {
  width: 300px;
  height: 300px;
}
</style>
