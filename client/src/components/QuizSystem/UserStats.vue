<template>
  <div class="user-stats">
    <!-- 用户统计卡片 -->
    <div class="stats-card">
      <div class="stats-header">
        <h3 class="stats-title">📊 学习统计</h3>
        <div class="stats-refresh" @click="loadStats">
          <span class="refresh-icon">🔄</span>
        </div>
      </div>
      
      <div class="stats-grid" v-if="stats">
        <div class="stat-item">
          <div class="stat-icon">📚</div>
          <div class="stat-content">
            <span class="stat-value">{{ stats.totalQuizzes }}</span>
            <span class="stat-label">完成测验</span>
          </div>
        </div>
        
        <div class="stat-item">
          <div class="stat-icon">🏆</div>
          <div class="stat-content">
            <span class="stat-value">{{ stats.totalScore }}</span>
            <span class="stat-label">总分数</span>
          </div>
        </div>
        
        <div class="stat-item">
          <div class="stat-icon">📈</div>
          <div class="stat-content">
            <span class="stat-value">{{ stats.averageScore }}</span>
            <span class="stat-label">平均分</span>
          </div>
        </div>
        
        <div class="stat-item">
          <div class="stat-icon">⭐</div>
          <div class="stat-content">
            <span class="stat-value">{{ stats.bestScore }}</span>
            <span class="stat-label">最高分</span>
          </div>
        </div>
      </div>
      
      <div class="stats-loading" v-else>
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>
    </div>

    <!-- 排行榜 -->
    <div class="leaderboard-card">
      <div class="leaderboard-header">
        <h3 class="leaderboard-title">🏅 排行榜</h3>
        <div class="leaderboard-refresh" @click="loadLeaderboard">
          <span class="refresh-icon">🔄</span>
        </div>
      </div>
      
      <div class="leaderboard-list" v-if="leaderboard.length > 0">
        <div 
          v-for="(result, index) in leaderboard" 
          :key="result.id"
          class="leaderboard-item"
          :class="{ 'top-three': index < 3 }"
        >
          <div class="rank-badge" :class="getRankClass(index)">
            {{ index + 1 }}
          </div>
          <div class="result-info">
            <div class="result-chapter">第{{ result.chapterId }}章</div>
            <div class="result-score">{{ result.score }}分</div>
          </div>
          <div class="result-accuracy">{{ result.accuracy }}%</div>
          <div class="result-date">{{ formatDate(result.completedAt) }}</div>
        </div>
      </div>
      
      <div class="leaderboard-empty" v-else>
        <div class="empty-icon">📊</div>
        <span>暂无排行榜数据</span>
      </div>
    </div>

    <!-- 答题历史 -->
    <div class="history-card">
      <div class="history-header">
        <h3 class="history-title">📝 答题历史</h3>
      </div>
      
      <div class="history-list" v-if="history.length > 0">
        <div 
          v-for="result in history" 
          :key="result.id"
          class="history-item"
        >
          <div class="history-info">
            <div class="history-chapter">第{{ result.chapterId }}章</div>
            <div class="history-score">{{ result.score }}分</div>
          </div>
          <div class="history-details">
            <span class="detail-item">正确率: {{ result.accuracy }}%</span>
            <span class="detail-item">正确题数: {{ result.correctCount }}/{{ result.totalQuestions }}</span>
          </div>
          <div class="history-date">{{ formatDate(result.completedAt) }}</div>
        </div>
      </div>
      
      <div class="history-empty" v-else>
        <div class="empty-icon">📝</div>
        <span>暂无答题历史</span>
      </div>
    </div>
  </div>
</template>

<script>
import { quizApi } from '../../services/api'
import { useAuth } from '../../composables/useAuth'

export default {
  name: 'UserStats',
  data() {
    return {
      stats: null,
      leaderboard: [],
      history: [],
      loading: false
    }
  },
  async mounted() {
    const { isLoggedIn } = useAuth()
    if (isLoggedIn.value) {
      await this.loadAllData()
    }
  },
  methods: {
    async loadAllData() {
      await Promise.all([
        this.loadStats(),
        this.loadLeaderboard(),
        this.loadHistory()
      ])
    },
    async loadStats() {
      try {
        this.stats = await quizApi.getUserStats()
      } catch (error) {
        console.error('加载统计信息失败:', error)
      }
    },
    async loadLeaderboard() {
      try {
        this.leaderboard = await quizApi.getLeaderboard()
      } catch (error) {
        console.error('加载排行榜失败:', error)
      }
    },
    async loadHistory() {
      try {
        this.history = await quizApi.getUserQuizHistory()
      } catch (error) {
        console.error('加载答题历史失败:', error)
      }
    },
    getRankClass(index) {
      if (index === 0) return 'rank-gold'
      if (index === 1) return 'rank-silver'
      if (index === 2) return 'rank-bronze'
      return 'rank-normal'
    },
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN', {
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.user-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.stats-card, .leaderboard-card, .history-card {
  background: #292c33;
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 4px 24px rgba(0,0,0,0.12);
  border: 1px solid rgba(57,59,64,0.18);
}

.stats-header, .leaderboard-header, .history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.stats-title, .leaderboard-title, .history-title {
  color: #f5f6fa;
  font-size: 1.2rem;
  font-weight: 600;
  margin: 0;
}

.stats-refresh, .leaderboard-refresh {
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 8px;
  transition: background 0.2s ease;
  
  &:hover {
    background: rgba(143, 161, 179, 0.1);
  }
}

.refresh-icon {
  font-size: 1rem;
  color: #8fa1b3;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #23272e;
  border-radius: 12px;
  border: 1px solid rgba(57,59,64,0.18);
}

.stat-icon {
  font-size: 1.5rem;
}

.stat-content {
  display: flex;
  flex-direction: column;
}

.stat-value {
  color: #f5f6fa;
  font-size: 1.3rem;
  font-weight: 700;
}

.stat-label {
  color: #8fa1b3;
  font-size: 0.9rem;
}

.stats-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  padding: 2rem;
  color: #8fa1b3;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid #393b40;
  border-top: 2px solid #8fa1b3;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.leaderboard-list, .history-list {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}

.leaderboard-item, .history-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #23272e;
  border-radius: 12px;
  border: 1px solid rgba(57,59,64,0.18);
  transition: transform 0.2s ease;
  
  &:hover {
    transform: translateY(-2px);
  }
  
  &.top-three {
    border-color: #4a90e2;
    background: rgba(74, 144, 226, 0.05);
  }
}

.rank-badge {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 0.9rem;
  
  &.rank-gold {
    background: linear-gradient(135deg, #ffd700, #ffed4e);
    color: #000;
  }
  
  &.rank-silver {
    background: linear-gradient(135deg, #c0c0c0, #e5e5e5);
    color: #000;
  }
  
  &.rank-bronze {
    background: linear-gradient(135deg, #cd7f32, #daa520);
    color: #fff;
  }
  
  &.rank-normal {
    background: #393b40;
    color: #8fa1b3;
  }
}

.result-info, .history-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.result-chapter, .history-chapter {
  color: #f5f6fa;
  font-weight: 600;
  font-size: 1rem;
}

.result-score, .history-score {
  color: #4a90e2;
  font-weight: 700;
  font-size: 1.1rem;
}

.result-accuracy {
  color: #4caf50;
  font-weight: 600;
  font-size: 0.9rem;
}

.result-date, .history-date {
  color: #8fa1b3;
  font-size: 0.8rem;
}

.history-details {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.detail-item {
  color: #b0b3b8;
  font-size: 0.8rem;
}

.leaderboard-empty, .history-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 1rem;
  color: #8fa1b3;
  gap: 1rem;
}

.empty-icon {
  font-size: 3rem;
  opacity: 0.5;
}

@media (max-width: 768px) {
  .user-stats {
    grid-template-columns: 1fr;
    padding: 1rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .leaderboard-item, .history-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .result-info, .history-info {
    width: 100%;
  }
}
</style>