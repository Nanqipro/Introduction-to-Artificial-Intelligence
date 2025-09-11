<template>
  <div class="section medical">
    <div class="section-header">
      <h3 class="section-title">
        <span class="title-icon">🏥</span>
        <span class="title-text">精准医疗</span>
      </h3>
      <p class="section-subtitle">AI技术推动医疗诊断和治疗的精准化发展</p>
    </div>

    <div class="content-grid">
      <!-- 基因检测 -->
      <div class="content-card gene-testing-card">
        <div class="card-header">
          <h4>基因检测与分析</h4>
        </div>
        <div class="image-container" @mouseenter="handleGeneHover" @mouseleave="handleGeneLeave">
          <img 
            src="/images/chapter4/gene-analysis.jpg" 
            alt="基因检测技术展示"
            class="main-image"
            @click="onImageClick('gene-testing')"
          />
          <!-- 基因检测项目高亮提示 -->
          <div v-if="geneHovered" class="gene-overlays">
            <div 
              v-for="project in geneProjects" 
              :key="project.id"
              class="gene-overlay"
              :style="project.style"
              @click="handleGeneProjectClick(project)"
            >
              <div class="gene-info">
                <div class="gene-name">{{ project.name }}</div>
                <div class="gene-type">{{ project.type }}</div>
                <div class="gene-accuracy">准确率: {{ project.accuracy }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="card-content">
          <p class="card-description">
            以某知名基因检测公司为代表的基因检测服务，通过AI算法分析个人基因组数据，
            预测疾病风险、药物反应和遗传特征。深度学习技术能够识别复杂的基因变异模式，
            为个性化医疗提供科学依据。
          </p>
        </div>
      </div>

      <!-- 精准诊断 -->
      <div class="content-card precision-diagnosis-card">
        <div class="card-header">
          <h4>AI辅助诊断</h4>
        </div>
        <div class="image-container" @mouseenter="handleDiagnosisHover" @mouseleave="handleDiagnosisLeave">
          <img 
            src="/images/chapter4/medical-imaging.jpg" 
            alt="AI医疗诊断系统"
            class="main-image"
            @click="onImageClick('ai-diagnosis')"
          />
          <!-- 病变标记物高亮提示 -->
          <div v-if="diagnosisHovered" class="marker-overlays">
            <div 
              v-for="marker in diseaseMarkers" 
              :key="marker.id"
              class="marker-overlay"
              :style="marker.style"
              @click="handleMarkerClick(marker)"
            >
              <div class="marker-info">
                <div class="marker-name">{{ marker.name }}</div>
                <div class="marker-confidence">置信度: {{ marker.confidence }}</div>
                <div class="marker-risk" :class="marker.riskLevel">{{ getRiskText(marker.riskLevel) }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="card-content">
          <p class="card-description">
            AI医疗诊断系统通过深度学习分析医学影像、实验室数据和临床症状，
            辅助医生进行疾病诊断。系统能够识别早期病变、预测疾病进展，
            显著提高诊断准确率和效率。
          </p>
        </div>
      </div>

      <!-- 个性化治疗 -->
      <div class="content-card personalized-treatment-card">
        <div class="card-header">
          <h4>个性化治疗方案</h4>
        </div>
        <div class="image-container">
          <img 
            src="/images/chapter4/precision-medicine.jpg" 
            alt="个性化治疗方案"
            class="main-image"
            @click="showTreatmentDialog = true; onImageClick('personalized-treatment')"
          />
        </div>
        <div class="card-content">
          <p class="card-description">
            基于患者的基因型、表型和病史数据，AI系统能够制定个性化的治疗方案。
            通过分析大量临床数据和药物反应模式，为每位患者推荐最适合的治疗策略，
            提高治疗效果，减少副作用。
          </p>
        </div>
      </div>
    </div>

    <!-- 基因项目详情弹窗 -->
    <div v-if="showGeneDetail" class="gene-detail-modal" @click="closeGeneDetail">
      <div class="gene-detail-content" @click.stop>
        <div class="gene-detail-header">
          <h3>{{ selectedGeneProject?.name }}</h3>
          <button class="close-btn" @click="closeGeneDetail">×</button>
        </div>
        <div class="gene-detail-body">
          <div class="gene-overview">
            <div class="gene-type-badge">{{ selectedGeneProject?.type }}</div>
            <div class="gene-accuracy-display">
              <span class="accuracy-label">检测准确率:</span>
              <span class="accuracy-value">{{ selectedGeneProject?.accuracy }}</span>
            </div>
          </div>
          
          <div class="gene-description">
            <h4>检测说明</h4>
            <p>{{ selectedGeneProject?.description }}</p>
          </div>
          
          <div class="gene-applications">
            <h4>临床应用</h4>
            <ul class="application-list">
              <li v-for="app in selectedGeneProject?.applications" :key="app">
                {{ app }}
              </li>
            </ul>
          </div>
          
          <div class="gene-technologies">
            <h4>检测技术</h4>
            <div class="tech-tags">
              <span v-for="tech in selectedGeneProject?.technologies" :key="tech" class="tech-tag">
                {{ tech }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 病变标记物详情弹窗 -->
    <div v-if="showMarkerDetail" class="marker-detail-modal" @click="closeMarkerDetail">
      <div class="marker-detail-content" @click.stop>
        <div class="marker-detail-header">
          <h3>{{ selectedMarker?.name }}</h3>
          <button class="close-btn" @click="closeMarkerDetail">×</button>
        </div>
        <div class="marker-detail-body">
          <div class="marker-overview">
            <div class="marker-confidence-display">
              <span class="confidence-label">AI置信度:</span>
              <span class="confidence-value">{{ selectedMarker?.confidence }}</span>
            </div>
            <div class="marker-risk-display" :class="selectedMarker?.riskLevel">
              <span class="risk-label">风险等级:</span>
              <span class="risk-value">{{ getRiskText(selectedMarker?.riskLevel) }}</span>
            </div>
          </div>
          
          <div class="marker-description">
            <h4>标记物说明</h4>
            <p>{{ selectedMarker?.description }}</p>
          </div>
          
          <div class="marker-recommendations">
            <h4>医疗建议</h4>
            <ul class="recommendation-list">
              <li v-for="rec in selectedMarker?.recommendations" :key="rec">
                {{ rec }}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <!-- 个性化治疗方案弹窗 -->
    <div v-if="showTreatmentDialog" class="treatment-dialog-modal" @click="showTreatmentDialog = false">
      <div class="treatment-dialog-content" @click.stop>
        <div class="treatment-dialog-header">
          <h3>🎯 个性化治疗方案</h3>
          <button class="close-btn" @click="showTreatmentDialog = false">×</button>
        </div>
        <div class="treatment-dialog-body">
          <div class="treatment-example">
            <h4>案例：肺癌精准治疗</h4>
            <div class="patient-profile">
              <div class="profile-item">
                <span class="profile-label">患者信息:</span>
                <span class="profile-value">65岁男性，非小细胞肺癌</span>
              </div>
              <div class="profile-item">
                <span class="profile-label">基因检测:</span>
                <span class="profile-value">EGFR突变阳性</span>
              </div>
              <div class="profile-item">
                <span class="profile-label">AI推荐:</span>
                <span class="profile-value">靶向治疗 + 免疫治疗</span>
              </div>
            </div>
          </div>
          
          <div class="treatment-benefits">
            <h4>精准治疗优势</h4>
            <div class="benefit-grid">
              <div class="benefit-item">
                <div class="benefit-icon">🎯</div>
                <div class="benefit-text">提高治疗效果</div>
                <div class="benefit-desc">针对性治疗，疗效提升40%</div>
              </div>
              <div class="benefit-item">
                <div class="benefit-icon">⚡</div>
                <div class="benefit-text">减少副作用</div>
                <div class="benefit-desc">精准用药，副作用降低60%</div>
              </div>
              <div class="benefit-item">
                <div class="benefit-icon">💰</div>
                <div class="benefit-text">降低医疗成本</div>
                <div class="benefit-desc">避免无效治疗，节省30%费用</div>
              </div>
              <div class="benefit-item">
                <div class="benefit-icon">⏱️</div>
                <div class="benefit-text">缩短治疗周期</div>
                <div class="benefit-desc">快速响应，周期缩短25%</div>
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
const geneHovered = ref(false)
const diagnosisHovered = ref(false)
const showGeneDetail = ref(false)
const selectedGeneProject = ref(null)
const showMarkerDetail = ref(false)
const selectedMarker = ref(null)
const showTreatmentDialog = ref(false)

// 基因检测项目数据
const geneProjects = ref([
  {
    id: 'cancer-risk',
    name: '癌症风险评估',
    type: '疾病风险',
    accuracy: '95%',
    style: { top: '20%', left: '15%' },
    description: '通过分析与癌症相关的基因变异，评估个体患癌风险，包括乳腺癌、结直肠癌、前列腺癌等多种癌症类型。',
    applications: [
      '早期筛查和预防',
      '家族遗传咨询',
      '个性化监测方案',
      '生活方式指导'
    ],
    technologies: ['全基因组测序', '单核苷酸多态性分析', '拷贝数变异检测', '机器学习算法']
  },
  {
    id: 'drug-response',
    name: '药物反应预测',
    type: '药物基因组学',
    accuracy: '92%',
    style: { top: '15%', right: '20%' },
    description: '分析影响药物代谢和反应的基因变异，预测个体对特定药物的反应，指导个性化用药。',
    applications: [
      '个性化用药指导',
      '药物剂量调整',
      '不良反应预防',
      '治疗方案优化'
    ],
    technologies: ['药物代谢酶基因分析', '药物转运体检测', 'CYP450基因分型', 'AI预测模型']
  },
  {
    id: 'rare-disease',
    name: '罕见病诊断',
    type: '遗传病',
    accuracy: '88%',
    style: { bottom: '25%', left: '25%' },
    description: '通过全外显子或全基因组测序，识别罕见遗传病的致病基因变异，为罕见病患者提供明确诊断。',
    applications: [
      '罕见病确诊',
      '遗传咨询',
      '家系分析',
      '生育指导'
    ],
    technologies: ['全外显子测序', '全基因组测序', '变异注释', '致病性预测算法']
  },
  {
    id: 'nutrition',
    name: '营养代谢分析',
    type: '营养基因组学',
    accuracy: '85%',
    style: { bottom: '20%', right: '30%' },
    description: '分析与营养代谢相关的基因变异，提供个性化的营养建议和膳食指导。',
    applications: [
      '个性化营养方案',
      '体重管理指导',
      '运动建议',
      '慢性病预防'
    ],
    technologies: ['营养代谢基因检测', '肠道菌群分析', '代谢组学', '营养算法模型']
  }
])

// 病变标记物数据
const diseaseMarkers = ref([
  {
    id: 'tumor-marker',
    name: '肿瘤标记物',
    confidence: '94%',
    riskLevel: 'high',
    style: { top: '25%', left: '20%' },
    description: 'AI系统检测到影像中的异常组织结构，结合多种生物标记物数据，高度怀疑恶性肿瘤。',
    recommendations: [
      '立即进行活检确认',
      '完善肿瘤分期检查',
      '多学科会诊制定治疗方案',
      '基因检测指导靶向治疗'
    ]
  },
  {
    id: 'inflammation',
    name: '炎症指标',
    confidence: '87%',
    riskLevel: 'medium',
    style: { top: '30%', right: '25%' },
    description: '检测到炎症相关的生物标记物升高，可能提示慢性炎症状态或感染。',
    recommendations: [
      '进一步检查炎症原因',
      '监测炎症指标变化',
      '考虑抗炎治疗',
      '生活方式调整'
    ]
  },
  {
    id: 'metabolic',
    name: '代谢异常',
    confidence: '91%',
    riskLevel: 'medium',
    style: { bottom: '30%', left: '30%' },
    description: '代谢相关指标异常，可能提示糖尿病、脂质代谢紊乱等代谢性疾病风险。',
    recommendations: [
      '完善代谢功能检查',
      '饮食结构调整',
      '增加运动量',
      '定期监测血糖血脂'
    ]
  },
  {
    id: 'cardiovascular',
    name: '心血管风险',
    confidence: '89%',
    riskLevel: 'low',
    style: { bottom: '25%', right: '35%' },
    description: '心血管相关指标轻度异常，提示潜在的心血管疾病风险。',
    recommendations: [
      '心电图和超声心动检查',
      '控制血压和血脂',
      '戒烟限酒',
      '规律运动'
    ]
  }
])

// 方法
const handleGeneHover = () => {
  geneHovered.value = true
}

const handleGeneLeave = () => {
  geneHovered.value = false
}

const handleDiagnosisHover = () => {
  diagnosisHovered.value = true
}

const handleDiagnosisLeave = () => {
  diagnosisHovered.value = false
}

const handleGeneProjectClick = (project) => {
  console.log(`点击了基因检测项目: ${project.name}`)
  selectedGeneProject.value = project
  showGeneDetail.value = true
  emit('interaction')
}

const closeGeneDetail = () => {
  showGeneDetail.value = false
  selectedGeneProject.value = null
}

const handleMarkerClick = (marker) => {
  console.log(`点击了病变标记物: ${marker.name}`)
  selectedMarker.value = marker
  showMarkerDetail.value = true
  emit('interaction')
}

const closeMarkerDetail = () => {
  showMarkerDetail.value = false
  selectedMarker.value = null
}

const onImageClick = (imageType) => {
  emit('image-click', imageType)
  emit('interaction')
  console.log(`点击了${imageType}图片`)
}

const getRiskText = (riskLevel) => {
  const riskMap = {
    'low': '低风险',
    'medium': '中风险',
    'high': '高风险'
  }
  return riskMap[riskLevel] || '未知'
}
</script>

<style scoped>
/* 精准医疗板块样式 */
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
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 2rem;
  max-width: 1400px;
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

.gene-overlays,
.marker-overlays {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
}

.gene-overlay,
.marker-overlay {
  position: absolute;
  width: 50px;
  height: 50px;
  background: rgba(40, 167, 69, 0.9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all var(--transition-normal);
  animation: pulse 2s infinite;
}

.marker-overlay {
  background: rgba(220, 53, 69, 0.9);
}

.gene-overlay:hover,
.marker-overlay:hover {
  transform: scale(1.2);
  background: rgba(40, 167, 69, 1);
}

.marker-overlay:hover {
  background: rgba(220, 53, 69, 1);
}

.gene-overlay:hover .gene-info,
.marker-overlay:hover .marker-info {
  opacity: 1;
  visibility: visible;
}

.gene-info,
.marker-info {
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

.gene-name,
.marker-name {
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: 0.25rem;
}

.gene-type {
  color: var(--success-color);
  font-size: 0.9rem;
  margin-bottom: 0.25rem;
}

.gene-accuracy,
.marker-confidence {
  color: var(--accent-color);
  font-size: 0.9rem;
  font-weight: 600;
  margin-bottom: 0.25rem;
}

.marker-risk {
  font-size: 0.9rem;
  padding: 0.2rem 0.5rem;
  border-radius: 12px;
  color: white;
}

.marker-risk.low {
  background: var(--success-color);
}

.marker-risk.medium {
  background: var(--warning-color);
}

.marker-risk.high {
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
.gene-detail-modal,
.marker-detail-modal,
.treatment-dialog-modal {
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

.gene-detail-content,
.marker-detail-content,
.treatment-dialog-content {
  background: var(--card-bg);
  border-radius: 16px;
  max-width: 700px;
  width: 100%;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: var(--box-shadow);
}

.gene-detail-header,
.marker-detail-header,
.treatment-dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
}

.gene-detail-header h3,
.marker-detail-header h3,
.treatment-dialog-header h3 {
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

.gene-detail-body,
.marker-detail-body {
  padding: 1.5rem;
}

.gene-overview,
.marker-overview {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
  padding: 1rem;
  background: var(--secondary-color);
  border-radius: 8px;
}

.gene-type-badge {
  background: var(--success-color);
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 16px;
  font-size: 0.9rem;
}

.gene-accuracy-display,
.marker-confidence-display,
.marker-risk-display {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.accuracy-label,
.confidence-label,
.risk-label {
  color: var(--text-secondary-color);
  font-weight: 500;
}

.accuracy-value,
.confidence-value {
  color: var(--accent-color);
  font-weight: 700;
  font-size: 1.1rem;
}

.risk-value {
  padding: 0.3rem 0.8rem;
  border-radius: 16px;
  color: white;
  font-weight: 600;
}

.marker-risk-display.low .risk-value {
  background: var(--success-color);
}

.marker-risk-display.medium .risk-value {
  background: var(--warning-color);
}

.marker-risk-display.high .risk-value {
  background: var(--error-color);
}

.gene-description,
.marker-description,
.gene-applications,
.marker-recommendations,
.gene-technologies {
  margin-bottom: 1.5rem;
}

.gene-description h4,
.marker-description h4,
.gene-applications h4,
.marker-recommendations h4,
.gene-technologies h4 {
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.gene-description p,
.marker-description p {
  color: var(--text-secondary-color);
  line-height: 1.6;
  margin: 0;
}

.application-list,
.recommendation-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.application-list li,
.recommendation-list li {
  color: var(--text-secondary-color);
  padding: 0.5rem 0;
  border-bottom: 1px solid var(--border-color);
  position: relative;
  padding-left: 1.5rem;
}

.application-list li:before,
.recommendation-list li:before {
  content: '•';
  color: var(--accent-color);
  position: absolute;
  left: 0;
  font-weight: bold;
}

.application-list li:last-child,
.recommendation-list li:last-child {
  border-bottom: none;
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

/* 治疗方案弹窗样式 */
.treatment-dialog-body {
  padding: 1.5rem;
}

.treatment-example {
  margin-bottom: 2rem;
}

.treatment-example h4 {
  color: var(--text-color);
  margin-bottom: 1rem;
}

.patient-profile {
  background: var(--secondary-color);
  padding: 1rem;
  border-radius: 8px;
}

.profile-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
}

.profile-item:last-child {
  margin-bottom: 0;
}

.profile-label {
  color: var(--text-secondary-color);
  font-weight: 500;
}

.profile-value {
  color: var(--text-color);
  font-weight: 600;
}

.treatment-benefits h4 {
  color: var(--text-color);
  margin-bottom: 1rem;
}

.benefit-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.benefit-item {
  text-align: center;
  padding: 1rem;
  background: var(--secondary-color);
  border-radius: 12px;
}

.benefit-icon {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}

.benefit-text {
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.benefit-desc {
  color: var(--text-secondary-color);
  font-size: 0.9rem;
  line-height: 1.4;
}

/* 动画 */
@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(40, 167, 69, 0.7);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(40, 167, 69, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(40, 167, 69, 0);
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
  
  .gene-overview,
  .marker-overview {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .benefit-grid {
    grid-template-columns: repeat(2, 1fr);
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

.light-theme .gene-info,
.light-theme .marker-info {
  background: var(--card-bg);
}

.light-theme .gene-name,
.light-theme .marker-name {
  color: var(--text-color);
}

.light-theme .card-description {
  color: var(--text-secondary-color);
}

.light-theme .gene-detail-content,
.light-theme .marker-detail-content,
.light-theme .treatment-dialog-content {
  background: var(--card-bg);
}

.light-theme .gene-detail-header,
.light-theme .marker-detail-header,
.light-theme .treatment-dialog-header {
  border-bottom-color: var(--border-color);
}

.light-theme .gene-detail-header h3,
.light-theme .marker-detail-header h3,
.light-theme .treatment-dialog-header h3 {
  color: var(--text-color);
}

.light-theme .close-btn {
  color: var(--text-secondary-color);
}

.light-theme .close-btn:hover {
  background: var(--border-color);
  color: var(--text-color);
}

.light-theme .gene-overview,
.light-theme .marker-overview {
  background: var(--secondary-color);
}

.light-theme .accuracy-label,
.light-theme .confidence-label,
.light-theme .risk-label {
  color: var(--text-secondary-color);
}

.light-theme .gene-description h4,
.light-theme .marker-description h4,
.light-theme .gene-applications h4,
.light-theme .marker-recommendations h4,
.light-theme .gene-technologies h4 {
  color: var(--text-color);
}

.light-theme .gene-description p,
.light-theme .marker-description p {
  color: var(--text-secondary-color);
}

.light-theme .application-list li,
.light-theme .recommendation-list li {
  color: var(--text-secondary-color);
  border-bottom-color: var(--border-color);
}

.light-theme .patient-profile {
  background: var(--secondary-color);
}

.light-theme .profile-label {
  color: var(--text-secondary-color);
}

.light-theme .profile-value {
  color: var(--text-color);
}

.light-theme .treatment-benefits h4,
.light-theme .treatment-example h4 {
  color: var(--text-color);
}

.light-theme .benefit-item {
  background: var(--secondary-color);
}

.light-theme .benefit-text {
  color: var(--text-color);
}

.light-theme .benefit-desc {
  color: var(--text-secondary-color);
}
</style>