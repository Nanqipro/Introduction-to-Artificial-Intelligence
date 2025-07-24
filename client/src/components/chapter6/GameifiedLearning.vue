<template>
  <div class="gamified-learning-container">
    <div class="game-header">
      <h2>
        <el-icon><Trophy /></el-icon>
        AI学习大冒险
      </h2>
      <p>通过游戏化的方式学习人工智能，收集徽章，提升等级！</p>
    </div>

    <!-- 玩家状态面板 -->
    <div class="player-status">
      <el-card class="status-card">
        <div class="player-info">
          <div class="avatar-section">
            <div class="player-avatar">
              <el-icon><User /></el-icon>
            </div>
            <div class="player-details">
              <h3>{{ playerName }}</h3>
              <div class="level-info">
                <el-tag type="warning" size="large">
                  等级 {{ playerLevel }}
                </el-tag>
                <span class="title">{{ playerTitle }}</span>
              </div>
            </div>
          </div>
          
          <div class="stats-section">
            <div class="stat-item">
              <div class="stat-value">{{ playerExp }}</div>
              <div class="stat-label">经验值</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ completedChallenges }}</div>
              <div class="stat-label">完成挑战</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ earnedBadges.length }}</div>
              <div class="stat-label">获得徽章</div>
            </div>
          </div>
          
          <div class="progress-section">
            <h4>升级进度</h4>
            <el-progress 
              :percentage="levelProgress" 
              :stroke-width="8"
              :show-text="true"
              status="success"
            />
            <p class="next-level">距离下一级还需 {{ expToNextLevel }} 经验值</p>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 挑战关卡 -->
    <div class="challenges-section">
      <el-card class="challenges-card">
        <template #header>
          <div class="challenges-header">
            <span>🎯 学习挑战</span>
            <el-tag :type="getCurrentChallengeType()" size="small">
              {{ getCurrentChallengeStatus() }}
            </el-tag>
          </div>
        </template>
        
        <div class="challenge-map">
          <div class="challenge-path">
            <div 
              v-for="(challenge, index) in challenges" 
              :key="challenge.id"
              class="challenge-node"
              :class="getChallengeNodeClass(challenge)"
              @click="selectChallenge(challenge)"
            >
              <div class="node-icon">
                <el-icon>
                  <component :is="challenge.icon" />
                </el-icon>
              </div>
              <div class="node-info">
                <h4>{{ challenge.title }}</h4>
                <p>{{ challenge.description }}</p>
                <div class="node-rewards">
                  <el-tag size="small" type="success">+{{ challenge.exp }}经验</el-tag>
                  <el-tag v-if="challenge.badge" size="small" type="warning">
                    {{ challenge.badge }}
                  </el-tag>
                </div>
              </div>
              
              <!-- 连接线 -->
              <div v-if="index < challenges.length - 1" class="connection-line"></div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 当前挑战详情 -->
    <div v-if="selectedChallenge" class="challenge-detail">
      <el-card class="detail-card">
        <template #header>
          <div class="detail-header">
            <span>{{ selectedChallenge.title }}</span>
            <el-button 
              v-if="!isChallengeCompleted(selectedChallenge.id)"
              type="primary" 
              @click="startChallenge"
              :disabled="!isChallengeUnlocked(selectedChallenge.id)"
            >
              开始挑战
            </el-button>
          </div>
        </template>
        
        <div class="challenge-content">
          <div class="challenge-description">
            <h3>挑战说明</h3>
            <p>{{ selectedChallenge.fullDescription }}</p>
          </div>
          
          <!-- 挑战任务 -->
          <div v-if="challengeStarted" class="challenge-tasks">
            <h3>任务列表</h3>
            <div class="task-list">
              <div 
                v-for="(task, index) in selectedChallenge.tasks" 
                :key="index"
                class="task-item"
                :class="{ completed: task.completed }"
              >
                <el-checkbox 
                  v-model="task.completed" 
                  @change="checkTaskCompletion"
                  :disabled="task.completed"
                >
                  {{ task.description }}
                </el-checkbox>
                <div v-if="task.completed" class="task-reward">
                  <el-tag size="small" type="success">+{{ task.points }}分</el-tag>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 挑战结果 -->
          <div v-if="challengeCompleted" class="challenge-result">
            <div class="success-animation">
              <el-icon class="success-icon"><Check /></el-icon>
              <h3>挑战完成！</h3>
              <div class="rewards">
                <div class="reward-item">
                  <span>获得经验：</span>
                  <el-tag type="success">+{{ selectedChallenge.exp }}</el-tag>
                </div>
                <div v-if="selectedChallenge.badge" class="reward-item">
                  <span>获得徽章：</span>
                  <el-tag type="warning">{{ selectedChallenge.badge }}</el-tag>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 徽章收藏 -->
    <div class="badges-section">
      <el-card class="badges-card">
        <template #header>
          <span>🏆 徽章收藏</span>
        </template>
        
        <div class="badges-grid">
          <div 
            v-for="badge in allBadges" 
            :key="badge.id"
            class="badge-item"
            :class="{ earned: earnedBadges.includes(badge.id) }"
          >
            <div class="badge-icon">
              <el-icon>
                <component :is="badge.icon" />
              </el-icon>
            </div>
            <div class="badge-info">
              <h4>{{ badge.name }}</h4>
              <p>{{ badge.description }}</p>
              <div v-if="earnedBadges.includes(badge.id)" class="earned-date">
                <el-tag size="small" type="success">已获得</el-tag>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 排行榜 -->
    <div class="leaderboard-section">
      <el-card class="leaderboard-card">
        <template #header>
          <span>📊 学习排行榜</span>
        </template>
        
        <div class="leaderboard-list">
          <div 
            v-for="(player, index) in leaderboard" 
            :key="player.id"
            class="leaderboard-item"
            :class="{ current: player.id === 'current' }"
          >
            <div class="rank">{{ index + 1 }}</div>
            <div class="player-info">
              <div class="name">{{ player.name }}</div>
              <div class="level">等级 {{ player.level }}</div>
            </div>
            <div class="score">{{ player.exp }} 经验</div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { 
  Trophy, User, Check 
} from '@element-plus/icons-vue'

// 定义事件
const emit = defineEmits(['progress-update'])

// 响应式数据
const playerName = ref('AI学习者')
const playerLevel = ref(1)
const playerExp = ref(0)
const completedChallenges = ref(0)
const earnedBadges = ref([])
const selectedChallenge = ref(null)
const challengeStarted = ref(false)
const challengeCompleted = ref(false)

// 挑战数据
const challenges = ref([
  {
    id: 'python-basics',
    title: 'Python基础',
    description: '掌握Python编程基础',
    fullDescription: '学习Python的基本语法、数据类型和控制结构，为AI开发打下基础。',
    icon: 'Document',
    exp: 100,
    badge: 'Python新手',
    unlocked: true,
    completed: false,
    tasks: [
      { description: '了解Python数据类型', completed: false, points: 20 },
      { description: '学习变量和运算符', completed: false, points: 20 },
      { description: '掌握条件语句和循环', completed: false, points: 30 },
      { description: '完成基础练习题', completed: false, points: 30 }
    ]
  },
  {
    id: 'data-processing',
    title: '数据处理',
    description: '学习数据预处理技术',
    fullDescription: '掌握使用Numpy和Pandas进行数据处理，理解数据清洗和特征工程的重要性。',
    icon: 'DataLine',
    exp: 150,
    badge: '数据处理师',
    unlocked: false,
    completed: false,
    tasks: [
      { description: '学习Numpy数组操作', completed: false, points: 30 },
      { description: '掌握Pandas数据框', completed: false, points: 30 },
      { description: '实践数据清洗', completed: false, points: 40 },
      { description: '完成数据可视化', completed: false, points: 50 }
    ]
  },
  {
    id: 'model-training',
    title: '模型训练',
    description: '训练你的第一个AI模型',
    fullDescription: '使用PyTorch构建和训练LeNet网络，理解深度学习的基本原理和训练过程。',
    icon: 'Cpu',
    exp: 200,
    badge: '模型训练师',
    unlocked: false,
    completed: false,
    tasks: [
      { description: '理解神经网络结构', completed: false, points: 40 },
      { description: '配置训练参数', completed: false, points: 30 },
      { description: '监控训练过程', completed: false, points: 50 },
      { description: '评估模型性能', completed: false, points: 80 }
    ]
  },
  {
    id: 'model-optimization',
    title: '模型优化',
    description: '优化模型性能',
    fullDescription: '学习各种模型优化技术，包括超参数调优、正则化和数据增强等方法。',
    icon: 'Setting',
    exp: 250,
    badge: '优化专家',
    unlocked: false,
    completed: false,
    tasks: [
      { description: '调整学习率', completed: false, points: 50 },
      { description: '应用数据增强', completed: false, points: 60 },
      { description: '使用正则化技术', completed: false, points: 70 },
      { description: '达到目标准确率', completed: false, points: 70 }
    ]
  }
])

// 徽章数据
const allBadges = ref([
  {
    id: 'python-newbie',
    name: 'Python新手',
    description: '完成Python基础学习',
    icon: 'Document'
  },
  {
    id: 'data-processor',
    name: '数据处理师',
    description: '掌握数据处理技能',
    icon: 'DataLine'
  },
  {
    id: 'model-trainer',
    name: '模型训练师',
    description: '成功训练第一个模型',
    icon: 'Cpu'
  },
  {
    id: 'optimization-expert',
    name: '优化专家',
    description: '掌握模型优化技术',
    icon: 'Setting'
  },
  {
    id: 'ai-master',
    name: 'AI大师',
    description: '完成所有挑战',
    icon: 'Trophy'
  }
])

// 排行榜数据
const leaderboard = ref([
  { id: 'player1', name: 'AI探索者', level: 5, exp: 1250 },
  { id: 'player2', name: '数据科学家', level: 4, exp: 980 },
  { id: 'current', name: playerName.value, level: playerLevel.value, exp: playerExp.value },
  { id: 'player3', name: '机器学习新手', level: 3, exp: 750 },
  { id: 'player4', name: '算法爱好者', level: 2, exp: 420 }
])

// 计算属性
const playerTitle = computed(() => {
  if (playerLevel.value >= 5) return 'AI大师'
  if (playerLevel.value >= 4) return '高级学习者'
  if (playerLevel.value >= 3) return '中级学习者'
  if (playerLevel.value >= 2) return '初级学习者'
  return '新手学习者'
})

const levelProgress = computed(() => {
  const currentLevelExp = (playerLevel.value - 1) * 200
  const nextLevelExp = playerLevel.value * 200
  const progress = ((playerExp.value - currentLevelExp) / (nextLevelExp - currentLevelExp)) * 100
  return Math.min(100, Math.max(0, progress))
})

const expToNextLevel = computed(() => {
  const nextLevelExp = playerLevel.value * 200
  return Math.max(0, nextLevelExp - playerExp.value)
})

// 方法
const selectChallenge = (challenge) => {
  selectedChallenge.value = challenge
  challengeStarted.value = false
  challengeCompleted.value = false
}

const startChallenge = () => {
  challengeStarted.value = true
  challengeCompleted.value = false
}

const checkTaskCompletion = () => {
  if (!selectedChallenge.value) return
  
  const allTasksCompleted = selectedChallenge.value.tasks.every(task => task.completed)
  
  if (allTasksCompleted && !challengeCompleted.value) {
    completeChallenge()
  }
}

const completeChallenge = () => {
  challengeCompleted.value = true
  selectedChallenge.value.completed = true
  completedChallenges.value++
  
  // 增加经验值
  playerExp.value += selectedChallenge.value.exp
  
  // 检查升级
  checkLevelUp()
  
  // 获得徽章
  if (selectedChallenge.value.badge) {
    const badgeId = selectedChallenge.value.badge.toLowerCase().replace(/\s+/g, '-')
    if (!earnedBadges.value.includes(badgeId)) {
      earnedBadges.value.push(badgeId)
    }
  }
  
  // 解锁下一个挑战
  unlockNextChallenge()
  
  // 更新进度
  updateProgress()
}

const checkLevelUp = () => {
  const newLevel = Math.floor(playerExp.value / 200) + 1
  if (newLevel > playerLevel.value) {
    playerLevel.value = newLevel
    // 显示升级动画
    showLevelUpAnimation()
  }
}

const showLevelUpAnimation = () => {
  // 升级动画逻辑
  console.log('恭喜升级！')
}

const unlockNextChallenge = () => {
  const currentIndex = challenges.value.findIndex(c => c.id === selectedChallenge.value.id)
  if (currentIndex < challenges.value.length - 1) {
    challenges.value[currentIndex + 1].unlocked = true
  }
}

const updateProgress = () => {
  const progress = Math.round((completedChallenges.value / challenges.value.length) * 100)
  emit('progress-update', 'gamified-learning', progress)
}

const getChallengeNodeClass = (challenge) => {
  return {
    'completed': challenge.completed,
    'unlocked': challenge.unlocked,
    'locked': !challenge.unlocked,
    'selected': selectedChallenge.value?.id === challenge.id
  }
}

const isChallengeUnlocked = (challengeId) => {
  const challenge = challenges.value.find(c => c.id === challengeId)
  return challenge?.unlocked || false
}

const isChallengeCompleted = (challengeId) => {
  const challenge = challenges.value.find(c => c.id === challengeId)
  return challenge?.completed || false
}

const getCurrentChallengeType = () => {
  if (!selectedChallenge.value) return 'info'
  if (selectedChallenge.value.completed) return 'success'
  if (selectedChallenge.value.unlocked) return 'warning'
  return 'info'
}

const getCurrentChallengeStatus = () => {
  if (!selectedChallenge.value) return '选择挑战'
  if (selectedChallenge.value.completed) return '已完成'
  if (selectedChallenge.value.unlocked) return '可挑战'
  return '未解锁'
}

// 监听器
watch(playerExp, (newExp) => {
  // 更新排行榜中的当前玩家信息
  const currentPlayerIndex = leaderboard.value.findIndex(p => p.id === 'current')
  if (currentPlayerIndex !== -1) {
    leaderboard.value[currentPlayerIndex].exp = newExp
    leaderboard.value[currentPlayerIndex].level = playerLevel.value
  }
  
  // 重新排序排行榜
  leaderboard.value.sort((a, b) => b.exp - a.exp)
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.gamified-learning-container {
  padding: 2rem;
  background: $secondary-color;
  min-height: 100vh;
}

.game-header {
  text-align: center;
  margin-bottom: 2rem;

  h2 {
    color: $text-color;
    margin-bottom: 0.5rem;

    .el-icon {
      margin-right: 0.5rem;
      color: $accent-color;
    }
  }

  p {
    color: $text-secondary-color;
    font-size: 1.1rem;
  }
}

.player-status {
  margin-bottom: 2rem;

  .status-card {
    background: $primary-color;
    border: 1px solid $border-color;

    :deep(.el-card__body) {
      background: $primary-color;
    }

    .player-info {
      display: grid;
      grid-template-columns: auto 1fr auto;
      gap: 2rem;
      align-items: center;

      .avatar-section {
        display: flex;
        align-items: center;
        gap: 1rem;

        .player-avatar {
          width: 60px;
          height: 60px;
          border-radius: 50%;
          background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
          display: flex;
          align-items: center;
          justify-content: center;
          color: $primary-color;
          font-size: 1.5rem;
        }

        .player-details {
          h3 {
            margin: 0 0 0.5rem 0;
            color: $text-color;
          }

          .level-info {
            display: flex;
            align-items: center;
            gap: 0.5rem;

            .title {
              color: $text-secondary-color;
              font-style: italic;
            }
          }
        }
      }
      
      .stats-section {
        display: flex;
        gap: 2rem;
        
        .stat-item {
          text-align: center;
          
          .stat-value {
            font-size: 1.5rem;
            font-weight: bold;
            color: #3498db;
          }
          
          .stat-label {
            color: #7f8c8d;
            font-size: 0.9rem;
          }
        }
      }
      
      .progress-section {
        min-width: 200px;
        
        h4 {
          margin: 0 0 0.5rem 0;
          color: #2c3e50;
          font-size: 1rem;
        }
        
        .next-level {
          margin: 0.5rem 0 0 0;
          color: #7f8c8d;
          font-size: 0.8rem;
        }
      }
    }
  }
}

.challenges-section {
  margin-bottom: 2rem;
  
  .challenges-card {
    .challenges-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-weight: 600;
    }
    
    .challenge-map {
      .challenge-path {
        display: flex;
        align-items: center;
        gap: 1rem;
        overflow-x: auto;
        padding: 1rem 0;
        
        .challenge-node {
          flex: 0 0 auto;
          min-width: 200px;
          padding: 1rem;
          border-radius: 12px;
          border: 2px solid #e9ecef;
          background: #f8f9fa;
          cursor: pointer;
          transition: all 0.3s ease;
          position: relative;
          
          &.unlocked {
            border-color: #3498db;
            background: #e3f2fd;
            
            &:hover {
              transform: translateY(-2px);
              box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
            }
          }
          
          &.completed {
            border-color: #27ae60;
            background: #e8f5e8;
          }
          
          &.locked {
            opacity: 0.5;
            cursor: not-allowed;
          }
          
          &.selected {
            border-color: #f39c12;
            background: #fef9e7;
          }
          
          .node-icon {
            text-align: center;
            margin-bottom: 0.5rem;
            
            .el-icon {
              font-size: 2rem;
              color: #3498db;
            }
          }
          
          .node-info {
            text-align: center;
            
            h4 {
              margin: 0 0 0.3rem 0;
              color: #2c3e50;
              font-size: 1rem;
            }
            
            p {
              margin: 0 0 0.5rem 0;
              color: #7f8c8d;
              font-size: 0.8rem;
            }
            
            .node-rewards {
              display: flex;
              justify-content: center;
              gap: 0.3rem;
              flex-wrap: wrap;
            }
          }
          
          .connection-line {
            position: absolute;
            right: -1rem;
            top: 50%;
            width: 2rem;
            height: 2px;
            background: #bdc3c7;
            transform: translateY(-50%);
          }
        }
      }
    }
  }
}

.challenge-detail {
  margin-bottom: 2rem;
  
  .detail-card {
    .detail-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-weight: 600;
    }
    
    .challenge-content {
      .challenge-description {
        margin-bottom: 2rem;
        
        h3 {
          color: #2c3e50;
          margin-bottom: 0.5rem;
        }
        
        p {
          color: #7f8c8d;
          line-height: 1.6;
        }
      }
      
      .challenge-tasks {
        margin-bottom: 2rem;
        
        h3 {
          color: #2c3e50;
          margin-bottom: 1rem;
        }
        
        .task-list {
          .task-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0.8rem;
            border-radius: 8px;
            margin-bottom: 0.5rem;
            background: #f8f9fa;
            transition: all 0.3s ease;
            
            &.completed {
              background: #e8f5e8;
            }
            
            .task-reward {
              margin-left: 1rem;
            }
          }
        }
      }
      
      .challenge-result {
        .success-animation {
          text-align: center;
          padding: 2rem;
          background: linear-gradient(135deg, #2ecc71, #27ae60);
          color: white;
          border-radius: 12px;
          
          .success-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
            animation: bounce 0.6s ease-in-out;
          }
          
          h3 {
            margin: 0 0 1rem 0;
          }
          
          .rewards {
            display: flex;
            justify-content: center;
            gap: 1rem;
            flex-wrap: wrap;
            
            .reward-item {
              display: flex;
              align-items: center;
              gap: 0.5rem;
            }
          }
        }
      }
    }
  }
}

.badges-section {
  margin-bottom: 2rem;
  
  .badges-card {
    .badges-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 1rem;
      
      .badge-item {
        display: flex;
        align-items: center;
        padding: 1rem;
        border-radius: 8px;
        border: 2px solid #e9ecef;
        background: #f8f9fa;
        transition: all 0.3s ease;
        
        &.earned {
          border-color: #f39c12;
          background: #fef9e7;
        }
        
        .badge-icon {
          margin-right: 1rem;
          font-size: 2rem;
          color: #f39c12;
        }
        
        .badge-info {
          flex: 1;
          
          h4 {
            margin: 0 0 0.3rem 0;
            color: #2c3e50;
          }
          
          p {
            margin: 0 0 0.5rem 0;
            color: #7f8c8d;
            font-size: 0.9rem;
          }
        }
      }
    }
  }
}

.leaderboard-section {
  .leaderboard-card {
    .leaderboard-list {
      .leaderboard-item {
        display: flex;
        align-items: center;
        padding: 1rem;
        border-radius: 8px;
        margin-bottom: 0.5rem;
        background: #f8f9fa;
        transition: all 0.3s ease;
        
        &.current {
          background: #e3f2fd;
          border: 2px solid #3498db;
        }
        
        .rank {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          background: #3498db;
          color: white;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: bold;
          margin-right: 1rem;
        }
        
        .player-info {
          flex: 1;
          
          .name {
            font-weight: 600;
            color: #2c3e50;
          }
          
          .level {
            color: #7f8c8d;
            font-size: 0.9rem;
          }
        }
        
        .score {
          font-weight: bold;
          color: #27ae60;
        }
      }
    }
  }
}

@keyframes bounce {
  0%, 20%, 53%, 80%, 100% {
    transform: translate3d(0, 0, 0);
  }
  40%, 43% {
    transform: translate3d(0, -30px, 0);
  }
  70% {
    transform: translate3d(0, -15px, 0);
  }
  90% {
    transform: translate3d(0, -4px, 0);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .player-info {
    grid-template-columns: 1fr !important;
    text-align: center;
  }
  
  .challenge-path {
    flex-direction: column !important;
    
    .connection-line {
      display: none;
    }
  }
  
  .badges-grid {
    grid-template-columns: 1fr !important;
  }
}
</style>
