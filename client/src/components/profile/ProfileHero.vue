<template>
  <div class="profile-hero">
    <div class="hero-content">
      <div class="avatar-container">
        <div class="avatar-wrapper">
          <el-avatar :size="120" :src="userInfo.avatar" class="user-avatar">
            <el-icon><User /></el-icon>
          </el-avatar>
          <div class="avatar-overlay" @click="$emit('showAvatarDialog')">
            <el-icon class="camera-icon"><Camera /></el-icon>
          </div>
        </div>
        <div class="status-indicator"></div>
      </div>
      
      <div class="user-details">
        <h1 class="username">{{ userInfo.username || '用户名' }}</h1>
        <p class="user-role">{{ userInfo.role || '学生' }}</p>
        
        <div class="user-stats">
          <div class="stat-item level-stat">
            <span class="stat-number">{{ userStats.level || 1 }}</span>
            <span class="stat-label">当前等级</span>
            <div class="level-title">{{ getLevelTitle(userStats.level || 1) }}</div>
          </div>
          <div class="stat-item">
            <span class="stat-number">{{ userStats.experience || 0 }}</span>
            <span class="stat-label">经验值</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">{{ userStats.completedChapters || 0 }}</span>
            <span class="stat-label">已完成章节</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">{{ userAchievements.length || 0 }}</span>
            <span class="stat-label">获得成就</span>
          </div>
        </div>
        
        <!-- 等级进度条 -->
        <div class="level-progress">
          <div class="progress-header">
            <span>等级进度</span>
            <span>{{ getExpToNextLevel() }} 经验值升级</span>
          </div>
          <el-progress 
            :percentage="getLevelProgress()" 
            :stroke-width="8"
            :show-text="true"
            status="success"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { User, Camera } from '@element-plus/icons-vue'

const props = defineProps({
  userInfo: {
    type: Object,
    default: () => ({})
  },
  userStats: {
    type: Object,
    default: () => ({})
  },
  userAchievements: {
    type: Array,
    default: () => []
  }
})

defineEmits(['showAvatarDialog'])

// 获取等级标题
const getLevelTitle = (level) => {
  const titles = {
    1: '初学者',
    2: '入门者',
    3: '进阶者',
    4: '熟练者',
    5: '专家',
    6: '大师',
    7: '传奇',
    8: '神话',
    9: '传说',
    10: '至尊'
  }
  return titles[level] || '初学者'
}

// 计算升级所需经验值
const getExpToNextLevel = () => {
  const currentLevel = props.userStats.level || 1
  const currentExp = props.userStats.experience || 0
  const nextLevelExp = currentLevel * 1000
  return Math.max(0, nextLevelExp - currentExp)
}

// 计算等级进度
const getLevelProgress = () => {
  const currentLevel = props.userStats.level || 1
  const currentExp = props.userStats.experience || 0
  const levelExp = currentLevel * 1000
  const prevLevelExp = (currentLevel - 1) * 1000
  const expInLevel = currentExp - prevLevelExp
  const expNeeded = levelExp - prevLevelExp
  return Math.min(100, Math.round((expInLevel / expNeeded) * 100))
}
</script>

<style scoped lang="scss">
.profile-hero {
  background: var(--profile-hero-bg);
  border-radius: 20px;
  padding: 3rem;
  margin-bottom: 2rem;
  box-shadow: var(--box-shadow);
  border: 1px solid var(--profile-hero-border);
  
  .hero-content {
    display: flex;
    align-items: center;
    gap: 3rem;
  }
  
  .avatar-container {
    position: relative;
    
    .avatar-wrapper {
      position: relative;
      display: inline-block;
      
      .user-avatar {
        border: 4px solid var(--avatar-wrapper-border);
        box-shadow: var(--box-shadow);
      }
      
      .avatar-overlay {
        position: absolute;
        bottom: 0;
        right: 0;
        width: 36px;
        height: 36px;
        background: var(--avatar-overlay-bg);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        border: 3px solid var(--avatar-wrapper-border);
        
        .camera-icon {
          color: white;
          font-size: 16px;
        }
      }
    }
    
    .status-indicator {
      position: absolute;
      top: 10px;
      right: 10px;
      width: 16px;
      height: 16px;
      background: var(--status-indicator-bg);
      border-radius: 50%;
      border: 3px solid var(--avatar-wrapper-border);
    }
  }
  
  .user-details {
    flex: 1;
    
    .username {
      font-size: 2.5rem;
      font-weight: 700;
      color: var(--username-color);
      margin-bottom: 0.5rem;
    }
    
    .user-role {
      font-size: 1.2rem;
      color: var(--user-role-color);
      margin-bottom: 2rem;
    }
    
    .user-stats {
      display: flex;
      gap: 2rem;
      
      .stat-item {
        text-align: center;
        
        .stat-number {
          display: block;
          font-size: 2rem;
          font-weight: 700;
          color: var(--stat-number-color);
          margin-bottom: 0.25rem;
        }
        
        .stat-label {
          font-size: 0.9rem;
          color: var(--stat-label-color);
          text-transform: uppercase;
          letter-spacing: 0.5px;
        }
        
        &.level-stat {
          .level-title {
            font-size: 0.8rem;
            color: var(--level-title-color);
            font-weight: bold;
            margin-top: 0.25rem;
            text-transform: none;
            letter-spacing: normal;
          }
        }
      }
    }
    
    .level-progress {
      margin-top: 1.5rem;
      padding: 1rem;
      background: var(--stat-item-bg);
      border-radius: 8px;
      border: 1px solid var(--stat-item-border);
      
      .progress-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 0.75rem;
        font-size: 0.9rem;
        color: var(--progress-header-color);
        
        span:last-child {
          color: var(--success-color);
          font-weight: bold;
        }
      }
    }
  }
}

/* 浅色主题优化 */
html.light-theme .profile-container {
  .profile-hero {
    background: #ffffff;
    border: 1px solid #e9ecef;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  }
}

@media (max-width: 768px) {
  .profile-hero {
    padding: 2rem;
    
    .hero-content {
      flex-direction: column;
      text-align: center;
      gap: 2rem;
    }
    
    .user-details {
      .username {
        font-size: 2rem;
      }
      
      .user-stats {
        justify-content: center;
      }
    }
  }
}

@media (max-width: 480px) {
  .profile-hero {
    padding: 1.5rem;
    
    .user-details {
      .username {
        font-size: 1.75rem;
      }
      
      .user-stats {
        flex-direction: column;
        gap: 1rem;
        
        .stat-item {
          .stat-number {
            font-size: 1.5rem;
          }
        }
      }
    }
  }
}
</style>
