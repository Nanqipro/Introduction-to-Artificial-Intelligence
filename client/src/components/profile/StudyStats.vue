<template>
  <div class="stats-card">
    <div class="card-header">
      <div class="header-left">
        <div class="card-icon stats-icon">
          <el-icon><TrendCharts /></el-icon>
        </div>
        <div class="header-text">
          <h3>学习统计</h3>
          <p>查看您的学习进度和成就</p>
        </div>
      </div>
    </div>
    
    <div class="card-content">
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-info">
            <h4>{{ userStats.completedChapters }}</h4>
            <p>已完成章节</p>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-icon">
            <el-icon><Trophy /></el-icon>
          </div>
          <div class="stat-info">
            <h4>{{ userStats.totalScore }}</h4>
            <p>总得分</p>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-icon">
            <el-icon><Timer /></el-icon>
          </div>
          <div class="stat-info">
            <h4>{{ userStats.studyTime }}h</h4>
            <p>学习时长</p>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-icon">
            <el-icon><Medal /></el-icon>
          </div>
          <div class="stat-info">
            <h4>{{ userStats.achievements }}</h4>
            <p>获得成就</p>
          </div>
        </div>
      </div>
      
      <div class="progress-section">
        <h4>学习进度</h4>
        <div class="progress-item">
          <div class="progress-label">
            <span>网络基础</span>
            <span>{{ userStats.networkProgress }}%</span>
          </div>
          <el-progress :percentage="userStats.networkProgress" :show-text="false" />
        </div>
        
        <div class="progress-item">
          <div class="progress-label">
            <span>协议学习</span>
            <span>{{ userStats.protocolProgress }}%</span>
          </div>
          <el-progress :percentage="userStats.protocolProgress" :show-text="false" />
        </div>
        
        <div class="progress-item">
          <div class="progress-label">
            <span>实践训练</span>
            <span>{{ userStats.practiceProgress }}%</span>
          </div>
          <el-progress :percentage="userStats.practiceProgress" :show-text="false" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { TrendCharts, Document, Trophy, Timer, Medal } from '@element-plus/icons-vue'

defineProps({
  userStats: {
    type: Object,
    default: () => ({})
  }
})
</script>

<style scoped lang="scss">
.stats-card {
  grid-column: 1 / -1;
  background: var(--card-bg);
  border-radius: 16px;
  padding: 0;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--card-border);
  overflow: hidden;
  
  .card-header {
    background: var(--card-header-bg);
    padding: 1.5rem 2rem;
    border-bottom: 1px solid var(--card-header-border);
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 1rem;
      
      .card-icon {
        width: 48px;
        height: 48px;
        background: var(--card-icon-bg);
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: var(--card-icon-color);
        font-size: 20px;
      }
      
      .header-text {
        h3 {
          font-size: 1.25rem;
          font-weight: 600;
          color: var(--text-color);
          margin: 0 0 0.25rem 0;
        }
        
        p {
          font-size: 0.9rem;
          color: var(--text-secondary-color);
          margin: 0;
        }
      }
    }
  }
  
  .card-content {
    padding: 2rem;
    background: var(--card-content-bg);
    
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1.5rem;
      margin-bottom: 2rem;
      
      .stat-card {
        background: var(--stat-card-bg);
        border-radius: 12px;
        padding: 1.5rem;
        display: flex;
        align-items: center;
        gap: 1rem;
        border: 1px solid var(--stat-card-border);
        
        .stat-icon {
          width: 48px;
          height: 48px;
          background: var(--card-icon-bg);
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: var(--card-icon-color);
          font-size: 20px;
        }
        
        .stat-info {
          h4 {
            font-size: 1.75rem;
            font-weight: 700;
            color: var(--stat-number-color);
            margin: 0 0 0.25rem 0;
          }
          
          p {
            font-size: 0.9rem;
            color: var(--stat-label-color);
            margin: 0;
          }
        }
      }
    }
    
    .progress-section {
      h4 {
        font-size: 1.1rem;
        font-weight: 600;
        color: var(--text-color);
        margin: 0 0 1.5rem 0;
      }
      
      .progress-item {
        margin-bottom: 1.5rem;
        
        &:last-child {
          margin-bottom: 0;
        }
        
        .progress-label {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 0.5rem;
          
          span {
            font-size: 0.9rem;
            color: var(--text-color);
            font-weight: 500;
            
            &:last-child {
              color: var(--success-color);
              font-weight: 600;
            }
          }
        }
      }
    }
  }
}

/* 深色主题优化 */
html.dark-theme .profile-container {
  .stats-card {
    background: rgba(26, 26, 46, 0.8);
    border: 1px solid rgba(0, 191, 255, 0.2);
    box-shadow: 0 8px 32px rgba(0, 191, 255, 0.1);
    backdrop-filter: blur(20px);
    
    .card-header {
      background: linear-gradient(135deg, rgba(0, 191, 255, 0.1), rgba(0, 255, 127, 0.05));
      border-bottom: 1px solid rgba(0, 191, 255, 0.2);
      
      .header-text h3 {
        color: #ffffff;
        text-shadow: 0 2px 8px rgba(0, 191, 255, 0.3);
      }
      
      .header-text p {
        color: rgba(255, 255, 255, 0.7);
      }
      
      .card-icon {
        background: rgba(0, 191, 255, 0.15);
        color: #00bfff;
        box-shadow: 0 4px 16px rgba(0, 191, 255, 0.2);
      }
    }
    
    .card-content {
      background: rgba(26, 26, 46, 0.8);
      
      .stat-card {
        background: rgba(0, 191, 255, 0.05);
        border: 1px solid rgba(0, 191, 255, 0.2);
        backdrop-filter: blur(10px);
        
        &:hover {
          background: rgba(0, 191, 255, 0.1);
          box-shadow: 0 8px 32px rgba(0, 191, 255, 0.2);
          transform: translateY(-2px);
        }
        
        .stat-icon {
          background: rgba(0, 191, 255, 0.15);
          color: #00bfff;
          box-shadow: 0 4px 16px rgba(0, 191, 255, 0.2);
        }
        
        .stat-info h4 {
          color: #00bfff;
          text-shadow: 0 2px 8px rgba(0, 191, 255, 0.3);
        }
        
        .stat-info p {
          color: rgba(255, 255, 255, 0.6);
        }
      }
      
      .progress-section h4 {
        color: #ffffff;
        text-shadow: 0 2px 8px rgba(0, 191, 255, 0.3);
      }
      
      .progress-item .progress-label span {
        color: rgba(255, 255, 255, 0.8);
        
        &:last-child {
          color: #00ff7f;
          text-shadow: 0 2px 8px rgba(0, 255, 127, 0.3);
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .stats-card .card-content .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
