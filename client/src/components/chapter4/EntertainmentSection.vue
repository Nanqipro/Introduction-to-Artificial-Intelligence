<template>
  <div class="section entertainment">
    <div class="section-header">
      <h3 class="section-title">
        <span class="title-icon">🎬</span>
        <span class="title-text">智慧娱乐</span>
      </h3>
      <p class="section-subtitle">AI让娱乐体验更加个性化和智能</p>
    </div>

    <!-- 筛选控制 -->
    <div class="filter-controls">
      <div class="filter-group">
        <label>分类筛选:</label>
        <select v-model="selectedCategory" @change="filterCases">
          <option value="">全部</option>
          <option value="短视频">短视频</option>
          <option value="流媒体">流媒体</option>
          <option value="音乐">音乐</option>
          <option value="游戏">游戏</option>
          <option value="社交">社交</option>
        </select>
      </div>
      <div class="filter-group">
        <label>关键词搜索:</label>
        <input 
          v-model="searchKeyword" 
          @input="filterCases" 
          placeholder="输入关键词搜索..."
          type="text"
        />
      </div>
      <div class="filter-group">
        <label>排序方式:</label>
        <select v-model="sortBy" @change="sortCases">
          <option value="name">按名称</option>
          <option value="users">按用户数</option>
          <option value="accuracy">按推荐准确率</option>
        </select>
      </div>
    </div>

    <!-- 案例网格 -->
    <div class="cases-grid">
      <div 
        v-for="case_ in filteredCases" 
        :key="case_.id"
        class="case-card"
        @click="handleCaseClick(case_)"
      >
        <div class="case-header">
          <img :src="case_.logo" :alt="case_.name" class="case-logo" />
          <div class="case-info">
            <h4 class="case-name">{{ case_.name }}</h4>
            <span class="case-category">{{ case_.category }}</span>
          </div>
        </div>
        <div class="case-content">
          <p class="case-description">{{ case_.description }}</p>
          <div class="case-stats">
            <div class="stat-item">
              <span class="stat-label">用户数:</span>
              <span class="stat-value">{{ case_.users }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">推荐准确率:</span>
              <span class="stat-value">{{ case_.accuracy }}</span>
            </div>
          </div>
          <div class="case-technologies">
            <span 
              v-for="tech in case_.technologies" 
              :key="tech" 
              class="tech-badge"
            >
              {{ tech }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 案例详情弹窗 -->
    <div v-if="showCaseDetail" class="case-detail-modal" @click="closeCaseDetail">
      <div class="case-detail-content" @click.stop>
        <div class="case-detail-header">
          <div class="case-detail-title">
            <img :src="selectedCase?.logo" :alt="selectedCase?.name" class="detail-logo" />
            <div>
              <h3>{{ selectedCase?.name }}</h3>
              <span class="detail-category">{{ selectedCase?.category }}</span>
            </div>
          </div>
          <button class="close-btn" @click="closeCaseDetail">×</button>
        </div>
        <div class="case-detail-body">
          <div class="detail-section">
            <h4>平台简介</h4>
            <p>{{ selectedCase?.fullDescription }}</p>
          </div>
          
          <div class="detail-section">
            <h4>AI技术应用</h4>
            <div class="ai-features">
              <div 
                v-for="feature in selectedCase?.aiFeatures" 
                :key="feature.name"
                class="ai-feature-item"
              >
                <div class="feature-name">{{ feature.name }}</div>
                <div class="feature-description">{{ feature.description }}</div>
              </div>
            </div>
          </div>
          
          <div class="detail-section">
            <h4>核心数据</h4>
            <div class="core-stats">
              <div class="core-stat">
                <span class="core-stat-label">全球用户数</span>
                <span class="core-stat-value">{{ selectedCase?.users }}</span>
              </div>
              <div class="core-stat">
                <span class="core-stat-label">推荐准确率</span>
                <span class="core-stat-value">{{ selectedCase?.accuracy }}</span>
              </div>
              <div class="core-stat">
                <span class="core-stat-label">日活跃用户</span>
                <span class="core-stat-value">{{ selectedCase?.dailyActive }}</span>
              </div>
              <div class="core-stat">
                <span class="core-stat-label">内容库规模</span>
                <span class="core-stat-value">{{ selectedCase?.contentSize }}</span>
              </div>
            </div>
          </div>
          
          <div class="detail-section">
            <h4>技术架构</h4>
            <div class="tech-architecture">
              <div 
                v-for="tech in selectedCase?.technologies" 
                :key="tech"
                class="tech-item"
              >
                {{ tech }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, defineEmits } from 'vue'

// Props
const props = defineProps({
  chapterId: {
    type: String,
    default: '4'
  }
})

// Emits
const emit = defineEmits(['case-click', 'interaction'])

// 响应式数据
const selectedCategory = ref('')
const searchKeyword = ref('')
const sortBy = ref('name')
const showCaseDetail = ref(false)
const selectedCase = ref(null)

// 娱乐案例数据
const entertainmentCases = ref([
  {
    id: 'douyin',
    name: '抖音',
    category: '短视频',
    logo: '/images/chapter4/douyin-logo.svg',
    description: '基于AI算法的个性化短视频推荐平台，通过深度学习分析用户行为，实现精准内容分发。',
    fullDescription: '抖音是字节跳动旗下的短视频社交平台，通过先进的AI推荐算法，为用户提供个性化的内容体验。平台运用计算机视觉、自然语言处理等技术，实现内容理解和用户画像构建，让每个用户都能看到最感兴趣的内容。',
    users: '12亿+',
    accuracy: '85%',
    dailyActive: '6亿+',
    contentSize: '数十亿条视频',
    technologies: ['推荐算法', '计算机视觉', '自然语言处理', '用户画像'],
    aiFeatures: [
      { name: '智能推荐', description: '基于用户行为和兴趣的个性化内容推荐' },
      { name: '内容理解', description: '自动识别视频内容、音乐、文字等元素' },
      { name: '实时优化', description: '根据用户反馈实时调整推荐策略' },
      { name: '创作辅助', description: '为创作者提供热门话题和创作建议' }
    ]
  },
  {
    id: 'netflix',
    name: 'Netflix',
    category: '流媒体',
    logo: '/images/chapter4/netflix-logo.svg',
    description: '全球领先的流媒体平台，运用机器学习算法为用户推荐个性化影视内容。',
    fullDescription: 'Netflix是全球最大的流媒体娱乐服务公司，通过复杂的机器学习算法分析用户观看历史、评分、搜索行为等数据，为每个用户生成个性化的内容推荐。平台还利用AI技术优化内容制作和分发策略。',
    users: '2.3亿+',
    accuracy: '80%',
    dailyActive: '1.5亿+',
    contentSize: '15000+部影视作品',
    technologies: ['协同过滤', '深度学习', '内容分析', 'A/B测试'],
    aiFeatures: [
      { name: '个性化推荐', description: '基于观看历史和偏好的影视推荐' },
      { name: '缩略图优化', description: 'AI生成个性化的影片缩略图' },
      { name: '内容制作', description: '数据驱动的原创内容制作决策' },
      { name: '质量优化', description: '自适应流媒体质量调整' }
    ]
  },
  {
    id: 'spotify',
    name: 'Spotify',
    category: '音乐',
    logo: '/images/chapter4/spotify-logo.svg',
    description: '智能音乐流媒体平台，通过AI分析音乐特征和用户偏好，创建个性化播放列表。',
    fullDescription: 'Spotify是全球领先的音乐流媒体平台，运用先进的音频分析和机器学习技术，深度理解音乐特征和用户喜好。平台能够分析音乐的节拍、调性、情绪等特征，结合用户的听歌历史，创建高度个性化的音乐体验。',
    users: '4.8亿+',
    accuracy: '78%',
    dailyActive: '2.2亿+',
    contentSize: '1亿+首歌曲',
    technologies: ['音频分析', '协同过滤', '深度学习', '情感计算'],
    aiFeatures: [
      { name: 'Discover Weekly', description: '每周个性化音乐发现播放列表' },
      { name: '音乐基因', description: '深度分析音乐的音频特征和情绪' },
      { name: '智能电台', description: '基于种子歌曲生成相似音乐流' },
      { name: '播客推荐', description: '个性化播客内容推荐' }
    ]
  },
  {
    id: 'steam',
    name: 'Steam',
    category: '游戏',
    logo: '/images/chapter4/steam-logo.svg',
    description: '全球最大的PC游戏平台，运用AI算法为玩家推荐合适的游戏内容。',
    fullDescription: 'Steam是Valve公司开发的数字游戏分发平台，通过分析玩家的游戏历史、评价、愿望单等数据，运用机器学习算法为玩家推荐可能感兴趣的游戏。平台还利用AI技术进行反作弊检测和社区内容管理。',
    users: '1.3亿+',
    accuracy: '72%',
    dailyActive: '3000万+',
    contentSize: '50000+款游戏',
    technologies: ['推荐系统', '数据挖掘', '反作弊AI', '内容过滤'],
    aiFeatures: [
      { name: '游戏推荐', description: '基于游戏历史和偏好的个性化推荐' },
      { name: '价格预测', description: 'AI预测游戏折扣和最佳购买时机' },
      { name: '反作弊系统', description: '智能检测和防范游戏作弊行为' },
      { name: '社区管理', description: '自动化内容审核和社区管理' }
    ]
  },
  {
    id: 'tiktok',
    name: 'TikTok',
    category: '短视频',
    logo: '/images/chapter4/tiktok-logo.svg',
    description: '国际版抖音，通过先进的AI算法在全球范围内提供个性化短视频内容。',
    fullDescription: 'TikTok是字节跳动旗下的国际短视频平台，继承了抖音的核心AI技术，在全球范围内为用户提供个性化的短视频内容。平台运用多模态AI技术，理解视频、音频、文字等多种内容形式，实现跨文化的内容推荐。',
    users: '10亿+',
    accuracy: '83%',
    dailyActive: '5亿+',
    contentSize: '数十亿条视频',
    technologies: ['多模态AI', '跨语言理解', '实时推荐', '内容安全'],
    aiFeatures: [
      { name: '全球化推荐', description: '跨文化和语言的内容推荐算法' },
      { name: '创作工具', description: 'AI驱动的视频编辑和特效工具' },
      { name: '内容安全', description: '多语言内容审核和安全检测' },
      { name: '趋势预测', description: '预测和识别全球流行趋势' }
    ]
  },
  {
    id: 'youtube',
    name: 'YouTube',
    category: '视频',
    logo: '/images/chapter4/youtube-logo.svg',
    description: '全球最大的视频分享平台，运用AI技术进行内容推荐和创作者支持。',
    fullDescription: 'YouTube是Google旗下的视频分享平台，运用先进的机器学习技术分析用户行为和视频内容，提供个性化的视频推荐。平台还为创作者提供AI驱动的分析工具和优化建议，帮助提升内容质量和观众参与度。',
    users: '26亿+',
    accuracy: '76%',
    dailyActive: '20亿+',
    contentSize: '数十亿小时视频',
    technologies: ['深度学习', '计算机视觉', '语音识别', '自然语言处理'],
    aiFeatures: [
      { name: '智能推荐', description: '基于观看历史的个性化视频推荐' },
      { name: '自动字幕', description: 'AI生成多语言视频字幕' },
      { name: '内容分析', description: '自动识别视频内容和关键词' },
      { name: '创作者工具', description: '为创作者提供数据分析和优化建议' }
    ]
  }
])

// 计算属性
const filteredCases = computed(() => {
  let cases = [...entertainmentCases.value]
  
  // 分类筛选
  if (selectedCategory.value) {
    cases = cases.filter(case_ => case_.category === selectedCategory.value)
  }
  
  // 关键词搜索
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    cases = cases.filter(case_ => 
      case_.name.toLowerCase().includes(keyword) ||
      case_.description.toLowerCase().includes(keyword) ||
      case_.technologies.some(tech => tech.toLowerCase().includes(keyword))
    )
  }
  
  return cases
})

// 方法
const filterCases = () => {
  // 触发计算属性重新计算
  emit('interaction')
}

const sortCases = () => {
  const cases = filteredCases.value
  
  switch (sortBy.value) {
    case 'name':
      cases.sort((a, b) => a.name.localeCompare(b.name))
      break
    case 'users':
      cases.sort((a, b) => {
        const aUsers = parseFloat(a.users.replace(/[^0-9.]/g, ''))
        const bUsers = parseFloat(b.users.replace(/[^0-9.]/g, ''))
        return bUsers - aUsers
      })
      break
    case 'accuracy':
      cases.sort((a, b) => {
        const aAccuracy = parseFloat(a.accuracy.replace('%', ''))
        const bAccuracy = parseFloat(b.accuracy.replace('%', ''))
        return bAccuracy - aAccuracy
      })
      break
  }
  
  emit('interaction')
}

const handleCaseClick = (case_) => {
  console.log(`点击了娱乐案例: ${case_.name}`)
  selectedCase.value = case_
  showCaseDetail.value = true
  emit('case-click', case_)
  emit('interaction')
}

const closeCaseDetail = () => {
  showCaseDetail.value = false
  selectedCase.value = null
}
</script>

<style scoped>
/* 智慧娱乐板块样式 */
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

/* 筛选控制样式 */
.filter-controls {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: var(--card-bg);
  border-radius: 12px;
  box-shadow: var(--box-shadow);
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  min-width: 200px;
}

.filter-group label {
  font-weight: 600;
  color: var(--text-color);
  font-size: 0.9rem;
}

.filter-group select,
.filter-group input {
  padding: 0.8rem;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  background: var(--input-bg);
  color: var(--text-color);
  font-size: 0.9rem;
  transition: all var(--transition-fast);
}

.filter-group select:focus,
.filter-group input:focus {
  outline: none;
  border-color: var(--accent-color);
  box-shadow: 0 0 0 3px rgba(0, 191, 255, 0.1);
}

/* 案例网格样式 */
.cases-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
  max-width: 1400px;
  margin: 0 auto;
}

.case-card {
  background: var(--card-bg);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: var(--box-shadow);
  transition: all var(--transition-normal);
  border: 1px solid var(--border-color);
  cursor: pointer;
}

.case-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 191, 255, 0.2);
  border-color: var(--accent-color);
}

.case-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.5rem;
  background: linear-gradient(135deg, var(--accent-color), var(--accent-light-color));
  color: white;
}

.case-logo {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  object-fit: cover;
  background: white;
  padding: 0.5rem;
}

.case-info {
  flex: 1;
}

.case-name {
  margin: 0 0 0.25rem 0;
  font-size: 1.3rem;
  font-weight: 600;
}

.case-category {
  background: rgba(255, 255, 255, 0.2);
  padding: 0.3rem 0.8rem;
  border-radius: 16px;
  font-size: 0.8rem;
}

.case-content {
  padding: 1.5rem;
}

.case-description {
  color: var(--text-secondary-color);
  line-height: 1.6;
  margin-bottom: 1rem;
}

.case-stats {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem;
  background: var(--secondary-color);
  border-radius: 8px;
}

.stat-label {
  color: var(--text-secondary-color);
  font-size: 0.9rem;
}

.stat-value {
  color: var(--accent-color);
  font-weight: 600;
  font-size: 0.9rem;
}

.case-technologies {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tech-badge {
  background: var(--accent-color);
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 16px;
  font-size: 0.8rem;
  font-weight: 500;
}

/* 案例详情弹窗样式 */
.case-detail-modal {
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

.case-detail-content {
  background: var(--card-bg);
  border-radius: 16px;
  max-width: 800px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: var(--box-shadow);
}

.case-detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
  background: linear-gradient(135deg, var(--accent-color), var(--accent-light-color));
  color: white;
}

.case-detail-title {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.detail-logo {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  object-fit: cover;
  background: white;
  padding: 0.5rem;
}

.case-detail-title h3 {
  margin: 0 0 0.25rem 0;
  font-size: 1.5rem;
}

.detail-category {
  background: rgba(255, 255, 255, 0.2);
  padding: 0.3rem 0.8rem;
  border-radius: 16px;
  font-size: 0.9rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: white;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 50%;
  transition: all var(--transition-fast);
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.case-detail-body {
  padding: 2rem;
}

.detail-section {
  margin-bottom: 2rem;
}

.detail-section h4 {
  color: var(--text-color);
  margin-bottom: 1rem;
  font-size: 1.2rem;
}

.detail-section p {
  color: var(--text-secondary-color);
  line-height: 1.6;
  margin: 0;
}

.ai-features {
  display: grid;
  gap: 1rem;
}

.ai-feature-item {
  padding: 1rem;
  background: var(--secondary-color);
  border-radius: 12px;
  border-left: 4px solid var(--accent-color);
}

.feature-name {
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.feature-description {
  color: var(--text-secondary-color);
  font-size: 0.9rem;
  line-height: 1.5;
}

.core-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.core-stat {
  display: flex;
  flex-direction: column;
  padding: 1rem;
  background: var(--secondary-color);
  border-radius: 12px;
  text-align: center;
}

.core-stat-label {
  color: var(--text-secondary-color);
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
}

.core-stat-value {
  color: var(--accent-color);
  font-size: 1.5rem;
  font-weight: 700;
}

.tech-architecture {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tech-item {
  background: var(--accent-color);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .filter-controls {
    flex-direction: column;
    gap: 1rem;
  }
  
  .filter-group {
    min-width: auto;
  }
  
  .cases-grid {
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
  
  .case-stats {
    grid-template-columns: 1fr;
  }
  
  .core-stats {
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

.light-theme .filter-controls {
  background: var(--card-bg);
}

.light-theme .filter-group label {
  color: var(--text-color);
}

.light-theme .filter-group select,
.light-theme .filter-group input {
  border-color: var(--border-color);
  background: var(--input-bg);
  color: var(--text-color);
}

.light-theme .filter-group select:focus,
.light-theme .filter-group input:focus {
  border-color: var(--accent-color);
}

.light-theme .case-card {
  background: var(--card-bg);
  border-color: var(--border-color);
}

.light-theme .case-card:hover {
  box-shadow: 0 12px 40px rgba(0, 191, 255, 0.15);
  border-color: var(--accent-color);
}

.light-theme .case-description {
  color: var(--text-secondary-color);
}

.light-theme .stat-item {
  background: var(--secondary-color);
}

.light-theme .stat-label {
  color: var(--text-secondary-color);
}

.light-theme .case-detail-content {
  background: var(--card-bg);
}

.light-theme .case-detail-header {
  border-bottom-color: var(--border-color);
}

.light-theme .detail-section h4 {
  color: var(--text-color);
}

.light-theme .detail-section p {
  color: var(--text-secondary-color);
}

.light-theme .ai-feature-item {
  background: var(--secondary-color);
}

.light-theme .feature-name {
  color: var(--text-color);
}

.light-theme .feature-description {
  color: var(--text-secondary-color);
}

.light-theme .core-stat {
  background: var(--secondary-color);
}

.light-theme .core-stat-label {
  color: var(--text-secondary-color);
}
</style>