<template>
  <div class="achievement-card">
    <div class="card-header">
      <div class="header-left">
        <div class="card-icon">
          <el-icon><Trophy /></el-icon>
        </div>
        <div class="header-text">
          <h3>我的成就</h3>
          <p>查看您获得的学习成就</p>
        </div>
      </div>
    </div>
    
    <div class="card-content">
      <div class="achievement-grid" v-if="userAchievements.length > 0">
        <div 
          v-for="achievement in userAchievements" 
          :key="achievement.id"
          class="achievement-item"
          :class="{ 'unlocked': achievement.unlocked }"
        >
          <div class="achievement-icon">
            <el-icon v-if="achievement.unlocked"><Trophy /></el-icon>
            <el-icon v-else><Lock /></el-icon>
          </div>
          <div class="achievement-info">
            <h4>{{ achievement.achievementName }}</h4>
            <p>{{ achievement.achievementDesc }}</p>
            <div class="achievement-progress" v-if="achievement.progress">
              <el-progress 
                :percentage="achievement.progress" 
                :show-text="false"
                :stroke-width="4"
              />
              <span class="progress-text">{{ achievement.progress }}%</span>
            </div>
          </div>
          <div class="achievement-date" v-if="achievement.unlocked && achievement.unlockedAt">
            {{ formatDate(achievement.unlockedAt) }}
          </div>
        </div>
      </div>
      
      <div class="empty-state" v-else>
        <el-icon class="empty-icon"><Trophy /></el-icon>
        <h3>暂无成就</h3>
        <p>继续学习，解锁更多成就吧！</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Trophy, Lock } from '@element-plus/icons-vue'

defineProps({
  userAchievements: {
    type: Array,
    default: () => []
  }
})

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}
</script>

<style scoped lang="scss">
.achievement-card {
  background: var(--card-bg);
  border-radius: 16px;
  padding: 0;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--border-color);
  overflow: hidden;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--box-shadow-light);
  }
  
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
        backdrop-filter: blur(10px);
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
    
    .achievement-grid {
      display: grid;
      gap: 1rem;
      
      .achievement-item {
        display: flex;
        align-items: center;
        gap: 1rem;
        padding: 1rem;
        background: var(--achievement-item-bg);
        border-radius: 12px;
        border: 1px solid var(--achievement-item-border);
        transition: all 0.3s ease;
        
        &:hover {
          background: var(--achievement-item-hover-bg);
        }
        
        &.unlocked {
          border-color: var(--success-color);
          background: rgba(0, 255, 127, 0.05);
        }
        
        .achievement-icon {
          width: 48px;
          height: 48px;
          background: var(--card-icon-bg);
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: var(--card-icon-color);
          font-size: 20px;
          flex-shrink: 0;
        }
        
        .achievement-info {
          flex: 1;
          
          h4 {
            font-size: 1rem;
            font-weight: 600;
            color: var(--text-color);
            margin: 0 0 0.25rem 0;
          }
          
          p {
            font-size: 0.9rem;
            color: var(--text-secondary-color);
            margin: 0 0 0.5rem 0;
          }
          
          .achievement-progress {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            
            .el-progress {
              flex: 1;
            }
            
            .progress-text {
              font-size: 0.8rem;
              color: var(--text-secondary-color);
              min-width: 40px;
            }
          }
        }
        
        .achievement-date {
          font-size: 0.8rem;
          color: var(--text-secondary-color);
          text-align: right;
          min-width: 80px;
        }
      }
    }
    
    .empty-state {
      text-align: center;
      padding: 3rem 1rem;
      
      .empty-icon {
        font-size: 4rem;
        color: var(--text-secondary-color);
        margin-bottom: 1rem;
      }
      
      h3 {
        font-size: 1.25rem;
        color: var(--text-color);
        margin: 0 0 0.5rem 0;
      }
      
      p {
        color: var(--text-secondary-color);
        margin: 0;
      }
    }
  }
}

/* 深色主题优化 */
html.dark-theme .profile-container {
  .achievement-card {
    background: rgba(26, 26, 46, 0.8);
    border: 1px solid rgba(0, 191, 255, 0.2);
    box-shadow: 0 8px 32px rgba(0, 191, 255, 0.1);
    backdrop-filter: blur(20px);
    
    &:hover {
      box-shadow: 0 12px 40px rgba(0, 191, 255, 0.15);
    }
    
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
      
      .achievement-item {
        background: rgba(0, 191, 255, 0.05);
        border: 1px solid rgba(0, 191, 255, 0.2);
        backdrop-filter: blur(10px);
        
        &:hover {
          background: rgba(0, 191, 255, 0.1);
          box-shadow: 0 8px 32px rgba(0, 191, 255, 0.2);
          transform: translateY(-2px);
        }
        
        &.unlocked {
          border-color: #00ff7f;
          background: rgba(0, 255, 127, 0.05);
          box-shadow: 0 4px 16px rgba(0, 255, 127, 0.2);
        }
        
        .achievement-icon {
          background: rgba(0, 191, 255, 0.15);
          color: #00bfff;
          box-shadow: 0 4px 16px rgba(0, 191, 255, 0.2);
        }
        
        .achievement-info h4 {
          color: #ffffff;
          text-shadow: 0 2px 8px rgba(0, 191, 255, 0.3);
        }
        
        .achievement-info p {
          color: rgba(255, 255, 255, 0.6);
        }
        
        .achievement-progress .progress-text {
          color: rgba(255, 255, 255, 0.6);
        }
        
        .achievement-date {
          color: rgba(255, 255, 255, 0.5);
        }
      }
      
      .empty-state {
        .empty-icon {
          color: rgba(255, 255, 255, 0.3);
        }
        
        h3 {
          color: #ffffff;
          text-shadow: 0 2px 8px rgba(0, 191, 255, 0.3);
        }
        
        p {
          color: rgba(255, 255, 255, 0.6);
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .achievement-card .card-content .achievement-grid .achievement-item {
    flex-direction: column;
    text-align: center;
    
    .achievement-date {
      text-align: center;
      min-width: auto;
    }
  }
}
</style>
