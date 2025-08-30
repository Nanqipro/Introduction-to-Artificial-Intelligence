<template>
  <div class="chapter7-case-study">
    <!-- 动态背景效果 -->
    <div class="ethics-background" :class="{ active: showBackground }">
      <div class="ethics-particle" v-for="n in 15" :key="n" 
           :style="getParticleStyle(n)">
        <i :class="getEthicsIcon(n)"></i>
      </div>
    </div>

    <div class="case-header">
      <h2 class="case-title" :class="{ visible: titleVisible }">
        ⚖️ 第七章案例学习
      </h2>
      <p class="case-description" :class="{ visible: descriptionVisible }">
        人工智能伦理治理：探索算法歧视、隐私保护与责任监管的挑战与对策
      </p>
      
      <!-- 学习进度指示器 -->
      <div class="progress-indicator">
        <div class="progress-item" :class="{ completed: caseCompleted }">
          <div class="progress-icon">🎯</div>
          <div class="progress-info">
            <h4>AI伦理案例探索</h4>
            <p>交互次数: {{ totalInteractions }}/{{ requiredInteractions }} 次</p>
            <div class="progress-status">
              <span v-if="caseCompleted" class="status-completed">✅ 已完成</span>
              <span v-else class="status-pending">⏳ 进行中</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 主题导航 -->
    <div class="ethics-navigation">
      <div class="nav-buttons">
        <button 
          v-for="(topic, index) in ethicsTopics" 
          :key="topic.id"
          @click="switchTopic(index)"
          class="nav-button"
          :class="{ active: currentTopic === index }"
        >
          <span class="nav-icon">{{ topic.icon }}</span>
          <span class="nav-text">{{ topic.title }}</span>
        </button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 算法歧视案例 -->
      <div v-if="currentTopic === 0" class="topic-section algorithm-bias">
        <div class="section-header">
          <h3 class="section-title">
            <span class="title-icon">🤖</span>
            <span class="title-text">算法歧视案例分析</span>
          </h3>
          <p class="section-subtitle">体验算法偏见如何在现实场景中产生不公平结果</p>
        </div>
        
        <!-- 招聘系统案例 -->
        <div class="case-card recruitment-case">
          <div class="card-header">
            <h4>💼 AI招聘系统偏见演示</h4>
            <span class="case-badge">真实案例改编</span>
          </div>
          
          <div class="case-content">
            <div class="scenario-description">
              <p>某大型科技公司使用AI系统筛选简历，但系统在历史数据训练中产生了性别偏见...</p>
            </div>
            
            <!-- 交互式简历筛选 -->
            <div class="resume-filter-demo">
              <div class="demo-controls">
                <h5>📋 简历筛选演示</h5>
                <button @click="runRecruitmentDemo" class="demo-button" :disabled="recruitmentRunning">
                  {{ recruitmentRunning ? '筛选中...' : '开始筛选' }}
                </button>
              </div>
              
              <div class="resumes-grid" v-if="showResumes">
                <div v-for="(resume, index) in demoResumes" :key="index" 
                     class="resume-card" 
                     :class="{ 
                       selected: resume.selected, 
                       rejected: resume.rejected,
                       biased: resume.biased 
                     }">
                  <div class="resume-header">
                    <span class="resume-name">{{ resume.name }}</span>
                    <span class="resume-gender" :class="resume.gender">{{ resume.gender === 'male' ? '👨' : '👩' }}</span>
                  </div>
                  <div class="resume-skills">
                    <span v-for="skill in resume.skills" :key="skill" class="skill-tag">{{ skill }}</span>
                  </div>
                  <div class="resume-score">
                    <span class="score-label">AI评分:</span>
                    <span class="score-value" :class="{ biased: resume.biased }">{{ resume.score }}</span>
                  </div>
                  <div class="resume-result" v-if="resume.selected || resume.rejected">
                    <span v-if="resume.selected" class="result-selected">✅ 通过筛选</span>
                    <span v-if="resume.rejected" class="result-rejected">❌ 未通过筛选</span>
                    <span v-if="resume.biased" class="bias-indicator">⚠️ 存在偏见</span>
                  </div>
                </div>
              </div>
              
              <div class="bias-analysis" v-if="showBiasAnalysis">
                <h6>🔍 偏见分析结果</h6>
                <div class="analysis-stats">
                  <div class="stat-item">
                    <span class="stat-label">男性通过率:</span>
                    <span class="stat-value male">{{ malePassRate }}%</span>
                  </div>
                  <div class="stat-item">
                    <span class="stat-label">女性通过率:</span>
                    <span class="stat-value female">{{ femalePassRate }}%</span>
                  </div>
                </div>
                <div class="bias-explanation">
                  <p>💡 <strong>偏见原因:</strong> 训练数据中历史招聘记录存在性别偏见，导致AI系统学习并放大了这种偏见。</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 解决方案展示 -->
        <div class="solution-card">
          <h4>🛠️ 算法公平性解决方案</h4>
          <div class="solutions-grid">
            <div class="solution-item" @click="showSolutionDetail('data-diversity')" 
                 :class="{ active: activeSolution === 'data-diversity' }">
              <div class="solution-icon">📊</div>
              <div class="solution-title">数据多样性</div>
              <div class="solution-desc">确保训练数据的代表性和平衡性</div>
            </div>
            <div class="solution-item" @click="showSolutionDetail('fairness-algorithm')" 
                 :class="{ active: activeSolution === 'fairness-algorithm' }">
              <div class="solution-icon">⚖️</div>
              <div class="solution-title">公平性算法</div>
              <div class="solution-desc">使用专门的公平性约束和偏差缓解技术</div>
            </div>
            <div class="solution-item" @click="showSolutionDetail('transparency')" 
                 :class="{ active: activeSolution === 'transparency' }">
              <div class="solution-icon">🔍</div>
              <div class="solution-title">算法透明度</div>
              <div class="solution-desc">提高模型的可解释性和决策透明度</div>
            </div>
          </div>
          
          <div class="solution-detail" v-if="activeSolution">
            <div class="detail-content">
              <h5>{{ getSolutionTitle(activeSolution) }}</h5>
              <p>{{ getSolutionDescription(activeSolution) }}</p>
              <div class="detail-examples">
                <h6>实施方法:</h6>
                <ul>
                  <li v-for="method in getSolutionMethods(activeSolution)" :key="method">{{ method }}</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 隐私保护案例 -->
      <div v-if="currentTopic === 1" class="topic-section privacy-protection">
        <div class="section-header">
          <h3 class="section-title">
            <span class="title-icon">🔒</span>
            <span class="title-text">数据隐私保护案例</span>
          </h3>
          <p class="section-subtitle">了解AI系统中的隐私风险和保护技术</p>
        </div>
        
        <!-- 数据泄露风险演示 -->
        <div class="case-card privacy-risk-case">
          <div class="card-header">
            <h4>🚨 数据隐私风险演示</h4>
            <span class="case-badge">交互式体验</span>
          </div>
          
          <div class="case-content">
            <div class="privacy-scenario">
              <div class="scenario-tabs">
                <button v-for="scenario in privacyScenarios" :key="scenario.id"
                        @click="selectPrivacyScenario(scenario.id)"
                        class="scenario-tab"
                        :class="{ active: selectedPrivacyScenario === scenario.id }">
                  {{ scenario.icon }} {{ scenario.title }}
                </button>
              </div>
              
              <div class="scenario-content" v-if="currentPrivacyScenario">
                <div class="scenario-description">
                  <h5>{{ currentPrivacyScenario.title }}</h5>
                  <p>{{ currentPrivacyScenario.description }}</p>
                </div>
                
                <!-- 数据流动可视化 -->
                <div class="data-flow-visualization">
                  <div class="data-flow-header">
                    <h6>📊 数据流动过程</h6>
                    <button @click="startDataFlow" class="flow-button" :disabled="dataFlowRunning">
                      {{ dataFlowRunning ? '数据传输中...' : '开始演示' }}
                    </button>
                  </div>
                  
                  <div class="flow-diagram">
                    <div class="flow-step" v-for="(step, index) in dataFlowSteps" :key="index"
                         :class="{ active: currentFlowStep >= index, risk: step.risk }">
                      <div class="step-icon">{{ step.icon }}</div>
                      <div class="step-content">
                        <div class="step-title">{{ step.title }}</div>
                        <div class="step-desc">{{ step.description }}</div>
                        <div v-if="step.risk" class="risk-indicator">
                          ⚠️ {{ step.riskDescription }}
                        </div>
                      </div>
                      <div v-if="index < dataFlowSteps.length - 1" class="flow-arrow">→</div>
                    </div>
                  </div>
                </div>
                
                <!-- 隐私保护技术展示 -->
                <div class="protection-techniques">
                  <h6>🛡️ 隐私保护技术</h6>
                  <div class="techniques-grid">
                    <div v-for="technique in privacyTechniques" :key="technique.id"
                         class="technique-card"
                         @click="showTechniqueDemo(technique.id)"
                         :class="{ active: activeTechnique === technique.id }">
                      <div class="technique-icon">{{ technique.icon }}</div>
                      <div class="technique-title">{{ technique.title }}</div>
                      <div class="technique-desc">{{ technique.description }}</div>
                    </div>
                  </div>
                  
                  <div class="technique-demo" v-if="activeTechnique">
                    <div class="demo-content">
                      <h6>{{ getCurrentTechnique().title }} 演示</h6>
                      <div class="demo-visualization">
                        <!-- 差分隐私演示 -->
                        <div v-if="activeTechnique === 'differential-privacy'" class="differential-privacy-demo">
                          <div class="privacy-controls">
                            <label>隐私预算 (ε): </label>
                            <input type="range" v-model="privacyBudget" min="0.1" max="2" step="0.1">
                            <span class="budget-value">{{ privacyBudget }}</span>
                          </div>
                          <div class="privacy-comparison">
                            <div class="data-column">
                              <h6>原始数据</h6>
                              <div class="data-points">
                                <div v-for="point in originalData" :key="point.id" class="data-point">
                                  {{ point.value }}
                                </div>
                              </div>
                            </div>
                            <div class="data-column">
                              <h6>添加噪声后</h6>
                              <div class="data-points">
                                <div v-for="point in noisyData" :key="point.id" class="data-point noisy">
                                  {{ point.value }}
                                </div>
                              </div>
                            </div>
                          </div>
                          <div class="privacy-explanation">
                            <p>💡 通过添加数学噪声，保护个体隐私的同时保持统计特性</p>
                          </div>
                        </div>
                        
                        <!-- 联邦学习演示 -->
                        <div v-if="activeTechnique === 'federated-learning'" class="federated-learning-demo">
                          <div class="federation-network">
                            <div class="central-server">
                              <div class="server-icon">🏢</div>
                              <div class="server-label">中央服务器</div>
                              <div class="model-updates">模型聚合</div>
                            </div>
                            <div class="client-devices">
                              <div v-for="device in federatedDevices" :key="device.id" 
                                   class="device-node" 
                                   :class="{ training: device.training }">
                                <div class="device-icon">{{ device.icon }}</div>
                                <div class="device-label">{{ device.label }}</div>
                                <div class="local-data">本地数据</div>
                              </div>
                            </div>
                          </div>
                          <button @click="startFederatedTraining" class="training-button" :disabled="federatedTraining">
                            {{ federatedTraining ? '训练中...' : '开始联邦学习' }}
                          </button>
                          <div class="federated-explanation">
                            <p>💡 数据不离开本地设备，只共享模型参数，保护数据隐私</p>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 责任与监管案例 -->
      <div v-if="currentTopic === 2" class="topic-section responsibility-governance">
        <div class="section-header">
          <h3 class="section-title">
            <span class="title-icon">⚖️</span>
            <span class="title-text">责任与监管案例</span>
          </h3>
          <p class="section-subtitle">探索AI决策责任归属和监管框架</p>
        </div>
        
        <!-- 责任归属案例 -->
        <div class="case-card responsibility-case">
          <div class="card-header">
            <h4>🚗 自动驾驶责任归属案例</h4>
            <span class="case-badge">真实案例分析</span>
          </div>
          
          <div class="case-content">
            <div class="case-scenario">
              <div class="scenario-setup">
                <h5>案例背景</h5>
                <p>2018年，Uber自动驾驶测试车辆在亚利桑那州发生致命事故，这是首起自动驾驶汽车致死案例...</p>
              </div>
              
              <!-- 责任链分析 -->
              <div class="responsibility-chain">
                <h6>🔗 责任链分析</h6>
                <div class="chain-visualization">
                  <div v-for="(entity, index) in responsibilityEntities" :key="entity.id"
                       class="responsibility-entity"
                       @click="selectResponsibilityEntity(entity.id)"
                       :class="{ active: selectedEntity === entity.id, liable: entity.liable }">
                    <div class="entity-icon">{{ entity.icon }}</div>
                    <div class="entity-name">{{ entity.name }}</div>
                    <div class="entity-role">{{ entity.role }}</div>
                    <div v-if="entity.liable" class="liability-indicator">⚠️ 承担责任</div>
                  </div>
                </div>
                
                <div class="entity-detail" v-if="selectedEntity">
                  <div class="detail-content">
                    <h6>{{ getCurrentEntity().name }}</h6>
                    <p><strong>角色:</strong> {{ getCurrentEntity().role }}</p>
                    <p><strong>责任分析:</strong> {{ getCurrentEntity().responsibility }}</p>
                    <div v-if="getCurrentEntity().liable" class="liability-detail">
                      <p><strong>法律后果:</strong> {{ getCurrentEntity().legalConsequence }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 监管框架展示 -->
        <div class="governance-framework">
          <h4>🏛️ AI监管框架</h4>
          <div class="framework-tabs">
            <button v-for="framework in governanceFrameworks" :key="framework.id"
                    @click="selectFramework(framework.id)"
                    class="framework-tab"
                    :class="{ active: selectedFramework === framework.id }">
              {{ framework.icon }} {{ framework.name }}
            </button>
          </div>
          
          <div class="framework-content" v-if="currentFramework">
            <div class="framework-overview">
              <h5>{{ currentFramework.name }}</h5>
              <p>{{ currentFramework.description }}</p>
            </div>
            
            <div class="framework-principles">
              <h6>核心原则</h6>
              <div class="principles-grid">
                <div v-for="principle in currentFramework.principles" :key="principle.id"
                     class="principle-card">
                  <div class="principle-icon">{{ principle.icon }}</div>
                  <div class="principle-title">{{ principle.title }}</div>
                  <div class="principle-desc">{{ principle.description }}</div>
                </div>
              </div>
            </div>
            
            <div class="framework-implementation">
              <h6>实施措施</h6>
              <div class="implementation-timeline">
                <div v-for="(measure, index) in currentFramework.measures" :key="index"
                     class="timeline-item">
                  <div class="timeline-marker">{{ index + 1 }}</div>
                  <div class="timeline-content">
                    <div class="measure-title">{{ measure.title }}</div>
                    <div class="measure-desc">{{ measure.description }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 学习总结 -->
    <div class="learning-summary" v-if="caseCompleted">
      <div class="summary-header">
        <h3>🎓 学习总结</h3>
        <p>恭喜完成AI伦理治理案例学习！</p>
      </div>
      
      <div class="summary-content">
        <div class="key-insights">
          <h4>🔑 关键洞察</h4>
          <ul>
            <li>算法歧视是AI系统中的重要伦理问题，需要通过数据多样性、公平性算法等手段解决</li>
            <li>隐私保护技术如差分隐私、联邦学习等为AI应用提供了重要保障</li>
            <li>AI责任归属需要明确的法律框架和监管机制来规范</li>
            <li>国际合作和多方协作是推进AI伦理治理的关键</li>
          </ul>
        </div>
        
        <div class="next-steps">
          <h4>📚 延伸学习</h4>
          <div class="learning-resources">
            <div class="resource-item">
              <span class="resource-icon">📖</span>
              <span class="resource-text">深入了解GDPR等隐私保护法规</span>
            </div>
            <div class="resource-item">
              <span class="resource-icon">🔬</span>
              <span class="resource-text">研究最新的AI公平性算法</span>
            </div>
            <div class="resource-item">
              <span class="resource-icon">🌍</span>
              <span class="resource-text">关注国际AI治理框架发展</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, nextTick } from 'vue'

// 组件属性
const props = defineProps({
  chapterId: {
    type: [String, Number],
    default: 7
  }
})

// 事件定义
const emit = defineEmits(['case-completed', 'all-cases-completed'])

// 响应式数据
const titleVisible = ref(false)
const descriptionVisible = ref(false)
const showBackground = ref(false)
const currentTopic = ref(0)
const totalInteractions = ref(0)
const requiredInteractions = ref(12) // 需要12次交互才算完成
const caseCompleted = ref(false)

// 伦理主题
const ethicsTopics = ref([
  {
    id: 'algorithm-bias',
    title: '算法歧视',
    icon: '🤖',
    description: '探索AI系统中的偏见和歧视问题'
  },
  {
    id: 'privacy-protection',
    title: '隐私保护',
    icon: '🔒',
    description: '了解数据隐私风险和保护技术'
  },
  {
    id: 'responsibility-governance',
    title: '责任监管',
    icon: '⚖️',
    description: '分析AI决策责任归属和监管框架'
  }
])

// 算法歧视相关数据
const recruitmentRunning = ref(false)
const showResumes = ref(false)
const showBiasAnalysis = ref(false)
const activeSolution = ref(null)

const demoResumes = ref([
  {
    name: '张伟',
    gender: 'male',
    skills: ['Python', 'Machine Learning', '5年经验'],
    score: 85,
    selected: false,
    rejected: false,
    biased: false
  },
  {
    name: '李娜',
    gender: 'female',
    skills: ['Python', 'Deep Learning', '5年经验'],
    score: 72,
    selected: false,
    rejected: false,
    biased: true
  },
  {
    name: '王强',
    gender: 'male',
    skills: ['Java', 'Data Science', '4年经验'],
    score: 78,
    selected: false,
    rejected: false,
    biased: false
  },
  {
    name: '刘芳',
    gender: 'female',
    skills: ['Python', 'AI Research', '6年经验'],
    score: 69,
    selected: false,
    rejected: false,
    biased: true
  }
])

// 隐私保护相关数据
const selectedPrivacyScenario = ref('healthcare')
const dataFlowRunning = ref(false)
const currentFlowStep = ref(0)
const activeTechnique = ref(null)
const privacyBudget = ref(1.0)
const federatedTraining = ref(false)

const privacyScenarios = ref([
  {
    id: 'healthcare',
    title: '医疗数据',
    icon: '🏥',
    description: '医疗AI系统处理患者敏感健康数据时的隐私风险'
  },
  {
    id: 'finance',
    title: '金融数据',
    icon: '💳',
    description: 'AI金融服务中个人财务信息的隐私保护挑战'
  },
  {
    id: 'social',
    title: '社交数据',
    icon: '📱',
    description: '社交平台AI推荐系统中的用户行为数据隐私'
  }
])

const privacyTechniques = ref([
  {
    id: 'differential-privacy',
    title: '差分隐私',
    icon: '🔢',
    description: '通过添加数学噪声保护个体隐私'
  },
  {
    id: 'federated-learning',
    title: '联邦学习',
    icon: '🌐',
    description: '数据不离开本地的分布式机器学习'
  },
  {
    id: 'homomorphic-encryption',
    title: '同态加密',
    icon: '🔐',
    description: '在加密状态下进行计算的密码学技术'
  }
])

// 责任监管相关数据
const selectedEntity = ref(null)
const selectedFramework = ref('eu-ai-act')

const responsibilityEntities = ref([
  {
    id: 'uber',
    name: 'Uber公司',
    icon: '🚗',
    role: '自动驾驶技术开发商',
    responsibility: '负责自动驾驶系统的设计、测试和部署',
    liable: true,
    legalConsequence: '面临刑事调查，最终达成和解协议'
  },
  {
    id: 'safety-driver',
    name: '安全驾驶员',
    icon: '👨‍💼',
    role: '车辆监督员',
    responsibility: '监控自动驾驶系统并在必要时接管车辆',
    liable: true,
    legalConsequence: '被判过失杀人罪'
  },
  {
    id: 'government',
    name: '监管部门',
    icon: '🏛️',
    role: '政策制定和监管',
    responsibility: '制定自动驾驶测试规范和安全标准',
    liable: false,
    legalConsequence: '加强了自动驾驶测试监管'
  }
])

const governanceFrameworks = ref([
  {
    id: 'eu-ai-act',
    name: '欧盟AI法案',
    icon: '🇪🇺',
    description: '全球首个全面的AI监管法律框架',
    principles: [
      {
        id: 'risk-based',
        title: '风险分级',
        icon: '⚠️',
        description: '根据AI系统风险等级实施不同监管要求'
      },
      {
        id: 'transparency',
        title: '透明度',
        icon: '🔍',
        description: '要求高风险AI系统具备可解释性'
      },
      {
        id: 'human-oversight',
        title: '人类监督',
        icon: '👥',
        description: '确保人类对AI决策的最终控制权'
      }
    ],
    measures: [
      {
        title: '禁止特定AI应用',
        description: '禁用对人类造成明显伤害的AI系统'
      },
      {
        title: '高风险系统认证',
        description: '建立CE标识认证体系'
      },
      {
        title: '算法审计制度',
        description: '定期评估AI系统的公平性和安全性'
      }
    ]
  },
  {
    id: 'china-ai-governance',
    name: '中国AI治理',
    icon: '🇨🇳',
    description: '以人为本的AI治理原则和实践',
    principles: [
      {
        id: 'people-centered',
        title: '以人为本',
        icon: '👤',
        description: '坚持AI发展服务于人类福祉'
      },
      {
        id: 'ethical-development',
        title: '伦理先行',
        icon: '⚖️',
        description: '将伦理道德贯穿AI全生命周期'
      },
      {
        id: 'security-controllable',
        title: '安全可控',
        icon: '🛡️',
        description: '确保AI系统安全可靠可控'
      }
    ],
    measures: [
      {
        title: '算法推荐管理规定',
        description: '规范算法推荐服务行为'
      },
      {
        title: 'AI伦理委员会',
        description: '建立跨部门AI伦理治理机制'
      },
      {
        title: '数据安全法',
        description: '保障数据处理活动安全'
      }
    ]
  }
])

// 计算属性
const malePassRate = computed(() => {
  const maleResumes = demoResumes.value.filter(r => r.gender === 'male')
  const maleSelected = maleResumes.filter(r => r.selected)
  return maleResumes.length > 0 ? Math.round((maleSelected.length / maleResumes.length) * 100) : 0
})

const femalePassRate = computed(() => {
  const femaleResumes = demoResumes.value.filter(r => r.gender === 'female')
  const femaleSelected = femaleResumes.filter(r => r.selected)
  return femaleResumes.length > 0 ? Math.round((femaleSelected.length / femaleResumes.length) * 100) : 0
})

const currentPrivacyScenario = computed(() => {
  return privacyScenarios.value.find(s => s.id === selectedPrivacyScenario.value)
})

const dataFlowSteps = computed(() => {
  const scenarios = {
    healthcare: [
      {
        icon: '🏥',
        title: '数据收集',
        description: '医院收集患者诊疗数据',
        risk: false
      },
      {
        icon: '📤',
        title: '数据传输',
        description: '数据上传至AI分析平台',
        risk: true,
        riskDescription: '传输过程可能被截获'
      },
      {
        icon: '🧠',
        title: 'AI分析',
        description: 'AI系统分析诊断',
        risk: true,
        riskDescription: '分析过程可能泄露患者信息'
      },
      {
        icon: '📊',
        title: '结果输出',
        description: '生成诊断建议',
        risk: false
      }
    ],
    finance: [
      {
        icon: '💳',
        title: '交易数据',
        description: '收集用户金融交易记录',
        risk: false
      },
      {
        icon: '🔄',
        title: '数据处理',
        description: '分析用户消费模式',
        risk: true,
        riskDescription: '可能推断出敏感个人信息'
      },
      {
        icon: '🎯',
        title: '风险评估',
        description: 'AI进行信用评估',
        risk: true,
        riskDescription: '评估结果可能存在偏见'
      },
      {
        icon: '✅',
        title: '决策输出',
        description: '生成贷款审批结果',
        risk: false
      }
    ],
    social: [
      {
        icon: '📱',
        title: '行为收集',
        description: '收集用户社交行为数据',
        risk: false
      },
      {
        icon: '🔍',
        title: '模式分析',
        description: '分析用户兴趣偏好',
        risk: true,
        riskDescription: '可能侵犯用户隐私'
      },
      {
        icon: '🎯',
        title: '内容推荐',
        description: 'AI生成个性化推荐',
        risk: true,
        riskDescription: '可能形成信息茧房'
      },
      {
        icon: '📺',
        title: '内容展示',
        description: '向用户展示推荐内容',
        risk: false
      }
    ]
  }
  return scenarios[selectedPrivacyScenario.value] || scenarios.healthcare
})

const originalData = computed(() => {
  return Array.from({ length: 5 }, (_, i) => ({
    id: i,
    value: Math.round(Math.random() * 100)
  }))
})

const noisyData = computed(() => {
  return originalData.value.map(point => ({
    id: point.id,
    value: Math.round(point.value + (Math.random() - 0.5) * privacyBudget.value * 20)
  }))
})

const federatedDevices = ref([
  { id: 1, icon: '📱', label: '手机A', training: false },
  { id: 2, icon: '💻', label: '电脑B', training: false },
  { id: 3, icon: '⌚', label: '手表C', training: false },
  { id: 4, icon: '🏥', label: '医院D', training: false }
])

const currentFramework = computed(() => {
  return governanceFrameworks.value.find(f => f.id === selectedFramework.value)
})

// 方法
const getParticleStyle = (n) => {
  return {
    left: `${Math.random() * 100}%`,
    top: `${Math.random() * 100}%`,
    animationDelay: `${Math.random() * 3}s`,
    animationDuration: `${3 + Math.random() * 2}s`
  }
}

const getEthicsIcon = (n) => {
  const icons = ['⚖️', '🔒', '🤖', '🛡️', '🔍', '👥', '📊', '🌐']
  return `ethics-icon ${icons[n % icons.length]}`
}

const switchTopic = (index) => {
  currentTopic.value = index
  incrementInteraction()
}

const runRecruitmentDemo = async () => {
  recruitmentRunning.value = true
  showResumes.value = true
  
  // 模拟筛选过程
  for (let i = 0; i < demoResumes.value.length; i++) {
    await new Promise(resolve => setTimeout(resolve, 800))
    const resume = demoResumes.value[i]
    
    // 模拟偏见：女性候选人分数被人为降低
    if (resume.gender === 'female') {
      resume.score = Math.max(60, resume.score - 10)
      resume.biased = true
    }
    
    // 根据分数决定是否通过
    if (resume.score >= 75) {
      resume.selected = true
    } else {
      resume.rejected = true
    }
  }
  
  recruitmentRunning.value = false
  showBiasAnalysis.value = true
  incrementInteraction()
}

const showSolutionDetail = (solutionId) => {
  activeSolution.value = activeSolution.value === solutionId ? null : solutionId
  incrementInteraction()
}

const getSolutionTitle = (solutionId) => {
  const titles = {
    'data-diversity': '数据多样性与平衡',
    'fairness-algorithm': '公平性算法技术',
    'transparency': '算法透明度提升'
  }
  return titles[solutionId]
}

const getSolutionDescription = (solutionId) => {
  const descriptions = {
    'data-diversity': '通过确保训练数据的代表性和多样性，减少数据中的历史偏见和系统性歧视。',
    'fairness-algorithm': '使用专门的公平性约束和偏差缓解技术，如对抗性去偏、公平性正则化等方法。',
    'transparency': '提高模型的可解释性，使决策过程透明化，便于识别和纠正潜在的偏见。'
  }
  return descriptions[solutionId]
}

const getSolutionMethods = (solutionId) => {
  const methods = {
    'data-diversity': [
      '平衡不同群体的样本数量',
      '使用数据增强技术扩充少数群体样本',
      '建立多元化的数据标注团队',
      '定期审查数据收集过程'
    ],
    'fairness-algorithm': [
      '实施公平性约束优化',
      '使用对抗性训练去除偏见',
      '应用后处理校准技术',
      '采用多目标优化平衡准确性和公平性'
    ],
    'transparency': [
      '使用可解释AI技术(如LIME、SHAP)',
      '提供决策路径可视化',
      '建立算法审计机制',
      '发布算法影响评估报告'
    ]
  }
  return methods[solutionId]
}

const selectPrivacyScenario = (scenarioId) => {
  selectedPrivacyScenario.value = scenarioId
  currentFlowStep.value = 0
  incrementInteraction()
}

const startDataFlow = async () => {
  dataFlowRunning.value = true
  currentFlowStep.value = 0
  
  for (let i = 0; i < dataFlowSteps.value.length; i++) {
    await new Promise(resolve => setTimeout(resolve, 1000))
    currentFlowStep.value = i + 1
  }
  
  dataFlowRunning.value = false
  incrementInteraction()
}

const showTechniqueDemo = (techniqueId) => {
  activeTechnique.value = activeTechnique.value === techniqueId ? null : techniqueId
  incrementInteraction()
}

const getCurrentTechnique = () => {
  return privacyTechniques.value.find(t => t.id === activeTechnique.value)
}

const startFederatedTraining = async () => {
  federatedTraining.value = true
  
  // 模拟联邦学习过程
  for (let round = 0; round < 3; round++) {
    // 各设备开始训练
    federatedDevices.value.forEach(device => {
      device.training = true
    })
    
    await new Promise(resolve => setTimeout(resolve, 1500))
    
    // 训练完成
    federatedDevices.value.forEach(device => {
      device.training = false
    })
    
    await new Promise(resolve => setTimeout(resolve, 500))
  }
  
  federatedTraining.value = false
  incrementInteraction()
}

const selectResponsibilityEntity = (entityId) => {
  selectedEntity.value = selectedEntity.value === entityId ? null : entityId
  incrementInteraction()
}

const getCurrentEntity = () => {
  return responsibilityEntities.value.find(e => e.id === selectedEntity.value)
}

const selectFramework = (frameworkId) => {
  selectedFramework.value = frameworkId
  incrementInteraction()
}

const incrementInteraction = () => {
  totalInteractions.value++
  
  if (totalInteractions.value >= requiredInteractions && !caseCompleted.value) {
    caseCompleted.value = true
    emit('case-completed', {
      chapterId: props.chapterId,
      caseType: 'ethics-governance',
      interactions: totalInteractions.value
    })
    emit('all-cases-completed', {
      chapterId: props.chapterId,
      totalCases: 1,
      completedCases: 1
    })
  }
}

// 生命周期
onMounted(async () => {
  await nextTick()
  
  // 启动动画效果
  setTimeout(() => {
    titleVisible.value = true
  }, 300)
  
  setTimeout(() => {
    descriptionVisible.value = true
  }, 600)
  
  setTimeout(() => {
    showBackground.value = true
  }, 900)
})
</script>

<style lang="scss" scoped>
@use "../../styles/variables.scss" as *;
/* 基础样式 */
.chapter7-case-study {
  position: relative;
  min-height: 100vh;
  background: $secondary-color; // 使用主题深色背景，移除紫色
  color: $text-color;
  overflow-x: hidden;
}

/* 动态背景 */
.ethics-background {
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

.ethics-background.active {
  opacity: 1;
}

.ethics-particle {
  position: absolute;
  font-size: 1.5rem;
  opacity: 0.3;
  animation: float 5s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

/* 头部样式 */
.case-header {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 60px 20px 40px;
}

.case-title {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 20px;
  opacity: 0;
  transform: translateY(30px);
  transition: all 0.8s ease;
}

.case-title.visible {
  opacity: 1;
  transform: translateY(0);
}

.case-description {
  font-size: 1.2rem;
  margin-bottom: 40px;
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.8s ease 0.3s;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
  line-height: 1.6;
}

.case-description.visible {
  opacity: 1;
  transform: translateY(0);
}

/* 进度指示器 */
.progress-indicator {
  display: flex;
  justify-content: center;
  margin-bottom: 40px;
}

.progress-item {
  display: flex;
  align-items: center;
  gap: 15px;
  background: $card-bg;
  border-radius: 20px;
  padding: 20px 30px;
  border: 1px solid $card-border;
  box-shadow: $card-shadow;
  transition: all 0.3s ease;
}

.progress-item.completed {
  background: rgba(76, 175, 80, 0.2);
  border-color: rgba(76, 175, 80, 0.5);
}

.progress-icon {
  font-size: 2rem;
}

.progress-info h4 {
  margin: 0 0 5px 0;
  font-size: 1.1rem;
}

.progress-info p {
  margin: 0 0 8px 0;
  opacity: 0.8;
  font-size: 0.9rem;
}

.status-completed {
  color: #4caf50;
  font-weight: bold;
}

.status-pending {
  color: #ff9800;
  font-weight: bold;
}

/* 主题导航 */
.ethics-navigation {
  position: relative;
  z-index: 2;
  display: flex;
  justify-content: center;
  margin-bottom: 40px;
}

.nav-buttons {
  display: flex;
  gap: 15px;
  background: $card-bg;
  border-radius: 50px;
  padding: 10px;
  border: 1px solid $card-border;
  box-shadow: $card-shadow;
}

.nav-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  border: none;
  border-radius: 25px;
  background: transparent;
  color: $text-color;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
}

.nav-button:hover {
  background: $chapter-type-bg;
  transform: translateY(-2px);
}

.nav-button.active {
  background: $chapter-badge-bg;
  box-shadow: $card-shadow;
}

.nav-icon {
  font-size: 1.2rem;
}

/* 主要内容 */
.main-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.topic-section {
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

/* 案例卡片 */
.case-card {
  background: $card-bg;
  border-radius: 20px;
  padding: 30px;
  margin-bottom: 30px;
  border: 1px solid $card-border;
  box-shadow: $card-shadow;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.card-header h4 {
  margin: 0;
  font-size: 1.3rem;
}

.case-badge {
  background: rgba(255, 193, 7, 0.2);
  color: #ffc107;
  padding: 5px 12px;
  border-radius: 15px;
  font-size: 0.8rem;
  border: 1px solid rgba(255, 193, 7, 0.3);
}

/* 招聘演示 */
.resume-filter-demo {
  margin-top: 20px;
}

.demo-controls {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.demo-button {
  background: linear-gradient(45deg, #4caf50, #45a049);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.demo-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
}

.demo-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.resumes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.resume-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  border: 2px solid transparent;
  transition: all 0.3s ease;
}

.resume-card.selected {
  border-color: #4caf50;
  background: rgba(76, 175, 80, 0.1);
}

.resume-card.rejected {
  border-color: #f44336;
  background: rgba(244, 67, 54, 0.1);
}

.resume-card.biased {
  border-color: #ff9800;
  background: rgba(255, 152, 0, 0.1);
}

.resume-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.resume-name {
  font-weight: bold;
  font-size: 1.1rem;
}

.resume-gender {
  font-size: 1.5rem;
}

.resume-skills {
  margin-bottom: 10px;
}

.skill-tag {
  display: inline-block;
  background: rgba(255, 255, 255, 0.2);
  padding: 3px 8px;
  border-radius: 10px;
  font-size: 0.8rem;
  margin-right: 5px;
  margin-bottom: 5px;
}

.resume-score {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.score-value {
  font-weight: bold;
  font-size: 1.2rem;
}

.score-value.biased {
  color: #ff9800;
}

.resume-result {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.result-selected {
  color: #4caf50;
  font-weight: bold;
}

.result-rejected {
  color: #f44336;
  font-weight: bold;
}

.bias-indicator {
  color: #ff9800;
  font-weight: bold;
  font-size: 0.9rem;
}

/* 偏见分析 */
.bias-analysis {
  background: rgba(255, 152, 0, 0.1);
  border: 1px solid rgba(255, 152, 0, 0.3);
  border-radius: 15px;
  padding: 20px;
  margin-top: 20px;
}

.analysis-stats {
  display: flex;
  gap: 30px;
  margin-bottom: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.stat-value.male {
  color: #2196f3;
  font-weight: bold;
}

.stat-value.female {
  color: #e91e63;
  font-weight: bold;
}

.bias-explanation {
  background: rgba(255, 255, 255, 0.1);
  padding: 15px;
  border-radius: 10px;
}

/* 解决方案 */
.solution-card {
  background: rgba(76, 175, 80, 0.1);
  border: 1px solid rgba(76, 175, 80, 0.3);
}

.solutions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.solution-item {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.solution-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-5px);
}

.solution-item.active {
  border-color: #4caf50;
  background: rgba(76, 175, 80, 0.2);
}

.solution-icon {
  font-size: 2.5rem;
  margin-bottom: 10px;
}

.solution-title {
  font-size: 1.1rem;
  font-weight: bold;
  margin-bottom: 8px;
}

.solution-desc {
  font-size: 0.9rem;
  opacity: 0.8;
}

.solution-detail {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  margin-top: 20px;
}

.detail-examples ul {
  margin: 10px 0;
  padding-left: 20px;
}

.detail-examples li {
  margin-bottom: 5px;
}

/* 隐私保护样式 */
.scenario-tabs {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  justify-content: center;
}

.scenario-tab {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: #fff;
  padding: 10px 20px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.scenario-tab:hover {
  background: rgba(255, 255, 255, 0.2);
}

.scenario-tab.active {
  background: rgba(33, 150, 243, 0.3);
  border: 1px solid rgba(33, 150, 243, 0.5);
}

/* 数据流动可视化 */
.data-flow-visualization {
  margin: 20px 0;
}

.data-flow-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.flow-button {
  background: linear-gradient(45deg, #2196f3, #1976d2);
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.flow-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(33, 150, 243, 0.3);
}

.flow-diagram {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  gap: 10px;
}

.flow-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  min-width: 120px;
  opacity: 0.5;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.flow-step.active {
  opacity: 1;
  background: rgba(33, 150, 243, 0.2);
  border-color: rgba(33, 150, 243, 0.5);
}

.flow-step.risk {
  border-color: rgba(255, 152, 0, 0.5);
  background: rgba(255, 152, 0, 0.1);
}

.step-icon {
  font-size: 2rem;
  margin-bottom: 8px;
}

.step-title {
  font-weight: bold;
  margin-bottom: 5px;
  text-align: center;
}

.step-desc {
  font-size: 0.8rem;
  text-align: center;
  opacity: 0.8;
}

.risk-indicator {
  color: #ff9800;
  font-size: 0.7rem;
  text-align: center;
  margin-top: 5px;
  font-weight: bold;
}

.flow-arrow {
  font-size: 1.5rem;
  color: #2196f3;
  margin: 0 10px;
}

/* 隐私保护技术 */
.protection-techniques {
  margin-top: 30px;
}

.techniques-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
}

.technique-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.technique-card:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-3px);
}

.technique-card.active {
  border-color: #4caf50;
  background: rgba(76, 175, 80, 0.2);
}

.technique-icon {
  font-size: 2rem;
  margin-bottom: 10px;
}

.technique-title {
  font-weight: bold;
  margin-bottom: 8px;
}

.technique-desc {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* 技术演示 */
.technique-demo {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  margin-top: 20px;
}

.differential-privacy-demo {
  text-align: center;
}

.privacy-controls {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
}

.privacy-controls input[type="range"] {
  width: 200px;
}

.budget-value {
  font-weight: bold;
  color: #4caf50;
}

.privacy-comparison {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 15px;
}

.data-column {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  padding: 15px;
}

.data-points {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: center;
  margin-top: 10px;
}

.data-point {
  background: rgba(33, 150, 243, 0.3);
  color: #fff;
  padding: 5px 10px;
  border-radius: 15px;
  font-weight: bold;
}

.data-point.noisy {
  background: rgba(255, 152, 0, 0.3);
}

.privacy-explanation {
  background: rgba(76, 175, 80, 0.1);
  border: 1px solid rgba(76, 175, 80, 0.3);
  border-radius: 10px;
  padding: 15px;
  text-align: center;
}

/* 联邦学习演示 */
.federated-learning-demo {
  text-align: center;
}

.federation-network {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
  margin-bottom: 20px;
}

.central-server {
  background: rgba(76, 175, 80, 0.2);
  border: 2px solid rgba(76, 175, 80, 0.5);
  border-radius: 20px;
  padding: 20px;
  text-align: center;
}

.server-icon {
  font-size: 3rem;
  margin-bottom: 10px;
}

.server-label {
  font-weight: bold;
  margin-bottom: 5px;
}

.model-updates {
  font-size: 0.9rem;
  opacity: 0.8;
}

.client-devices {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 20px;
  width: 100%;
  max-width: 600px;
}

.device-node {
  background: rgba(33, 150, 243, 0.2);
  border: 2px solid rgba(33, 150, 243, 0.5);
  border-radius: 15px;
  padding: 15px;
  text-align: center;
  transition: all 0.3s ease;
}

.device-node.training {
  background: rgba(255, 152, 0, 0.2);
  border-color: rgba(255, 152, 0, 0.5);
  animation: pulse 1s infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.device-icon {
  font-size: 2rem;
  margin-bottom: 8px;
}

.device-label {
  font-weight: bold;
  margin-bottom: 5px;
}

.local-data {
  font-size: 0.8rem;
  opacity: 0.8;
}

.training-button {
  background: linear-gradient(45deg, #ff9800, #f57c00);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 15px;
}

.training-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(255, 152, 0, 0.3);
}

.federated-explanation {
  background: rgba(76, 175, 80, 0.1);
  border: 1px solid rgba(76, 175, 80, 0.3);
  border-radius: 10px;
  padding: 15px;
}

/* 责任监管样式 */
.responsibility-chain {
  margin: 20px 0;
}

.chain-visualization {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.responsibility-entity {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.responsibility-entity:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-3px);
}

.responsibility-entity.active {
  border-color: #2196f3;
  background: rgba(33, 150, 243, 0.2);
}

.responsibility-entity.liable {
  border-color: #f44336;
  background: rgba(244, 67, 54, 0.1);
}

.entity-icon {
  font-size: 2.5rem;
  margin-bottom: 10px;
}

.entity-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.entity-role {
  font-size: 0.9rem;
  opacity: 0.8;
  margin-bottom: 8px;
}

.liability-indicator {
  color: #f44336;
  font-weight: bold;
  font-size: 0.8rem;
}

.entity-detail {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  margin-top: 20px;
}

.liability-detail {
  background: rgba(244, 67, 54, 0.1);
  border: 1px solid rgba(244, 67, 54, 0.3);
  border-radius: 10px;
  padding: 15px;
  margin-top: 10px;
}

/* 监管框架 */
.governance-framework {
  background: $card-bg;
  border: 1px solid $card-border;
  border-radius: 20px;
  padding: 30px;
  margin-top: 30px;
  box-shadow: $card-shadow;
}

.framework-tabs {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  justify-content: center;
}

.framework-tab {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: #fff;
  padding: 12px 24px;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
}

.framework-tab:hover {
  background: rgba(255, 255, 255, 0.2);
}

.framework-tab.active {
  background: $chapter-type-bg;
  border: 1px solid $card-border;
}

.framework-overview {
  text-align: center;
  margin-bottom: 30px;
}

.framework-overview h5 {
  font-size: 1.5rem;
  margin-bottom: 10px;
}

.framework-principles {
  margin-bottom: 30px;
}

.principles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 15px;
}

.principle-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  text-align: center;
  transition: all 0.3s ease;
}

.principle-card:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-3px);
}

.principle-icon {
  font-size: 2rem;
  margin-bottom: 10px;
}

.principle-title {
  font-weight: bold;
  margin-bottom: 8px;
}

.principle-desc {
  font-size: 0.9rem;
  opacity: 0.8;
}

.implementation-timeline {
  margin-top: 15px;
}

.timeline-item {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  margin-bottom: 20px;
}

.timeline-marker {
  background: $accent-color;
  color: $text-color;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  flex-shrink: 0;
}

.timeline-content {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  padding: 15px;
  flex: 1;
}

.measure-title {
  font-weight: bold;
  margin-bottom: 5px;
}

.measure-desc {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* 学习总结 */
.learning-summary {
  position: relative;
  z-index: 2;
  max-width: 800px;
  margin: 40px auto;
  background: rgba(76, 175, 80, 0.1);
  border: 1px solid rgba(76, 175, 80, 0.3);
  border-radius: 20px;
  padding: 30px;
  text-align: center;
}

.summary-header {
  margin-bottom: 30px;
}

.summary-header h3 {
  font-size: 2rem;
  margin-bottom: 10px;
}

.key-insights {
  text-align: left;
  margin-bottom: 30px;
}

.key-insights h4 {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.key-insights ul {
  list-style: none;
  padding: 0;
}

.key-insights li {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 10px;
  border-left: 4px solid #4caf50;
}

.next-steps {
  text-align: left;
}

.next-steps h4 {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.learning-resources {
  display: grid;
  gap: 10px;
}

.resource-item {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  padding: 15px;
  transition: all 0.3s ease;
}

.resource-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateX(5px);
}

.resource-icon {
  font-size: 1.5rem;
}

.resource-text {
  font-size: 1rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .case-title {
    font-size: 2rem;
  }
  
  .case-description {
    font-size: 1rem;
  }
  
  .nav-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .nav-button {
    width: 100%;
    max-width: 200px;
  }
  
  .resumes-grid {
    grid-template-columns: 1fr;
  }
  
  .solutions-grid {
    grid-template-columns: 1fr;
  }
  
  .techniques-grid {
    grid-template-columns: 1fr;
  }
  
  .privacy-comparison {
    grid-template-columns: 1fr;
  }
  
  .client-devices {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .chain-visualization {
    grid-template-columns: 1fr;
  }
  
  .principles-grid {
    grid-template-columns: 1fr;
  }
  
  .framework-tabs {
    flex-direction: column;
    align-items: center;
  }
  
  .framework-tab {
    width: 100%;
    max-width: 200px;
  }
}

@media (max-width: 480px) {
  .case-header {
    padding: 40px 15px 30px;
  }
  
  .main-content {
    padding: 0 15px;
  }
  
  .case-card {
    padding: 20px;
  }
  
  .flow-diagram {
    flex-direction: column;
  }
  
  .flow-arrow {
    transform: rotate(90deg);
  }
  
  .analysis-stats {
    flex-direction: column;
    gap: 15px;
  }
}
</style>