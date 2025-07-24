<template>
  <div class="ai-assistant-container">
    <div class="assistant-header">
      <h2>
        <el-icon><ChatDotRound /></el-icon>
        AI智能助手
      </h2>
      <p>你的专属AI学习伙伴，随时为你答疑解惑</p>
    </div>

    <!-- 助手状态面板 -->
    <div class="assistant-status">
      <el-card class="status-card">
        <div class="assistant-info">
          <div class="avatar-section">
            <div class="assistant-avatar" :class="{ talking: isTalking }">
              <div class="avatar-face">
                <div class="eyes">
                  <div class="eye left" :class="{ blink: isBlinking }"></div>
                  <div class="eye right" :class="{ blink: isBlinking }"></div>
                </div>
                <div class="mouth" :class="{ talking: isTalking }"></div>
              </div>
            </div>
            <div class="assistant-details">
              <h3>小AI助手</h3>
              <div class="status-info">
                <el-tag :type="assistantStatus.type" size="small">
                  {{ assistantStatus.text }}
                </el-tag>
                <span class="mood">{{ currentMood }}</span>
              </div>
            </div>
          </div>
          
          <div class="stats-section">
            <div class="stat-item">
              <div class="stat-value">{{ answeredQuestions }}</div>
              <div class="stat-label">已回答问题</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ helpfulRating }}%</div>
              <div class="stat-label">满意度</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ responseTime }}ms</div>
              <div class="stat-label">响应时间</div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 聊天界面 -->
    <div class="chat-interface">
      <el-card class="chat-card">
        <template #header>
          <div class="chat-header">
            <span>💬 智能对话</span>
            <div class="chat-controls">
              <el-button @click="clearChat" size="small" type="warning">
                <el-icon><Delete /></el-icon>
                清空对话
              </el-button>
              <el-button @click="exportChat" size="small" type="info">
                <el-icon><Download /></el-icon>
                导出对话
              </el-button>
            </div>
          </div>
        </template>
        
        <div class="chat-messages" ref="chatMessages">
          <div 
            v-for="message in chatHistory" 
            :key="message.id"
            class="message-item"
            :class="{ user: message.type === 'user', assistant: message.type === 'assistant' }"
          >
            <div class="message-avatar">
              <el-icon v-if="message.type === 'user'"><User /></el-icon>
              <div v-else class="ai-avatar">🤖</div>
            </div>
            <div class="message-content">
              <div class="message-bubble">
                <div v-if="message.type === 'assistant' && message.isTyping" class="typing-indicator">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
                <div v-else class="message-text" v-html="message.content"></div>
              </div>
              <div class="message-time">{{ formatTime(message.timestamp) }}</div>
              <div v-if="message.type === 'assistant' && !message.isTyping" class="message-actions">
                <el-button 
                  @click="rateMessage(message.id, 'helpful')" 
                  size="small" 
                  type="success"
                  :disabled="message.rated"
                >
                  <el-icon><Check /></el-icon>
                  有用
                </el-button>
                <el-button 
                  @click="rateMessage(message.id, 'not-helpful')" 
                  size="small" 
                  type="danger"
                  :disabled="message.rated"
                >
                  <el-icon><Close /></el-icon>
                  无用
                </el-button>
              </div>
            </div>
          </div>
          
          <!-- 空状态 -->
          <div v-if="chatHistory.length === 0" class="empty-chat">
            <div class="empty-icon">💭</div>
            <h3>开始与AI助手对话吧！</h3>
            <p>你可以问我关于Python、机器学习、深度学习的任何问题</p>
          </div>
        </div>
        
        <!-- 输入区域 -->
        <div class="chat-input">
          <div class="input-container">
            <el-input
              v-model="currentMessage"
              placeholder="输入你的问题..."
              @keyup.enter="sendMessage"
              :disabled="isProcessing"
              size="large"
              class="message-input"
            >
              <template #append>
                <el-button 
                  @click="sendMessage" 
                  type="primary"
                  :disabled="!currentMessage.trim() || isProcessing"
                  :loading="isProcessing"
                >
                  <el-icon><Promotion /></el-icon>
                </el-button>
              </template>
            </el-input>
          </div>
          
          <!-- 快速问题 -->
          <div class="quick-questions">
            <span class="quick-label">快速提问：</span>
            <el-tag 
              v-for="question in quickQuestions" 
              :key="question"
              @click="askQuickQuestion(question)"
              class="quick-question"
              type="info"
              size="small"
            >
              {{ question }}
            </el-tag>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 知识库搜索 -->
    <div class="knowledge-base">
      <el-card class="knowledge-card">
        <template #header>
          <span>📚 知识库搜索</span>
        </template>
        
        <div class="search-section">
          <el-input
            v-model="searchQuery"
            placeholder="搜索相关知识点..."
            @input="searchKnowledge"
            size="large"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          
          <div v-if="searchResults.length > 0" class="search-results">
            <div 
              v-for="result in searchResults" 
              :key="result.id"
              class="result-item"
              @click="selectKnowledgeItem(result)"
            >
              <div class="result-title">{{ result.title }}</div>
              <div class="result-summary">{{ result.summary }}</div>
              <div class="result-tags">
                <el-tag 
                  v-for="tag in result.tags" 
                  :key="tag"
                  size="small"
                  type="info"
                >
                  {{ tag }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 学习建议 -->
    <div class="learning-suggestions">
      <el-card class="suggestions-card">
        <template #header>
          <span>💡 个性化学习建议</span>
        </template>
        
        <div class="suggestions-content">
          <div 
            v-for="suggestion in learningSuggestions" 
            :key="suggestion.id"
            class="suggestion-item"
          >
            <div class="suggestion-icon">
              <el-icon>
                <component :is="suggestion.icon" />
              </el-icon>
            </div>
            <div class="suggestion-content">
              <h4>{{ suggestion.title }}</h4>
              <p>{{ suggestion.description }}</p>
              <el-button 
                @click="applySuggestion(suggestion)"
                size="small" 
                type="primary"
              >
                采纳建议
              </el-button>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick, onMounted } from 'vue'
import { 
  ChatDotRound, Delete, Download, User, Check, Close, 
  Promotion, Search 
} from '@element-plus/icons-vue'

// 定义事件
const emit = defineEmits(['progress-update'])

// 响应式数据
const currentMessage = ref('')
const chatHistory = ref([])
const isProcessing = ref(false)
const isTalking = ref(false)
const isBlinking = ref(false)
const answeredQuestions = ref(0)
const helpfulRating = ref(95)
const responseTime = ref(150)
const searchQuery = ref('')
const searchResults = ref([])
const chatMessages = ref(null)

// 助手状态
const assistantStatus = computed(() => {
  if (isProcessing.value) return { type: 'warning', text: '思考中...' }
  if (isTalking.value) return { type: 'success', text: '回答中' }
  return { type: 'success', text: '在线' }
})

const currentMood = computed(() => {
  const moods = ['😊 开心', '🤔 思考', '😎 自信', '🎯 专注']
  return moods[Math.floor(Math.random() * moods.length)]
})

// 快速问题
const quickQuestions = ref([
  'Python如何导入模块？',
  'LeNet网络结构是什么？',
  '什么是卷积神经网络？',
  '如何提高模型准确率？'
])

// 知识库数据
const knowledgeBase = ref([
  {
    id: 1,
    title: 'Python模块导入',
    summary: '学习Python中import语句的使用方法',
    content: 'Python中可以使用import、from...import、import...as等方式导入模块...',
    tags: ['Python', '基础语法', '模块']
  },
  {
    id: 2,
    title: 'LeNet网络架构',
    summary: '了解LeNet卷积神经网络的结构和原理',
    content: 'LeNet是最早的卷积神经网络之一，包含卷积层、池化层和全连接层...',
    tags: ['深度学习', 'CNN', 'LeNet']
  },
  {
    id: 3,
    title: '数据预处理技术',
    summary: '掌握图像数据预处理的常用方法',
    content: '数据预处理包括尺寸调整、归一化、数据增强等步骤...',
    tags: ['数据处理', '图像处理', '预处理']
  }
])

// 学习建议
const learningSuggestions = ref([
  {
    id: 1,
    title: '加强Python基础',
    description: '建议多练习Python语法和数据结构操作',
    icon: 'Document',
    action: 'python-practice'
  },
  {
    id: 2,
    title: '深入理解CNN',
    description: '可以通过可视化工具更好地理解卷积神经网络',
    icon: 'View',
    action: 'cnn-visualization'
  },
  {
    id: 3,
    title: '实践项目经验',
    description: '尝试完成更多实际的机器学习项目',
    icon: 'Trophy',
    action: 'project-practice'
  }
])

// 方法
const sendMessage = async () => {
  if (!currentMessage.value.trim() || isProcessing.value) return
  
  const userMessage = {
    id: Date.now(),
    type: 'user',
    content: currentMessage.value,
    timestamp: new Date()
  }
  
  chatHistory.value.push(userMessage)
  const question = currentMessage.value
  currentMessage.value = ''
  
  // 滚动到底部
  await nextTick()
  scrollToBottom()
  
  // 开始处理
  isProcessing.value = true
  isTalking.value = true
  
  // 添加打字指示器
  const typingMessage = {
    id: Date.now() + 1,
    type: 'assistant',
    content: '',
    timestamp: new Date(),
    isTyping: true
  }
  chatHistory.value.push(typingMessage)
  
  // 模拟AI响应
  setTimeout(async () => {
    const response = await generateAIResponse(question)
    
    // 移除打字指示器
    chatHistory.value.pop()
    
    // 添加实际回复
    const assistantMessage = {
      id: Date.now() + 2,
      type: 'assistant',
      content: response,
      timestamp: new Date(),
      isTyping: false,
      rated: false
    }
    
    chatHistory.value.push(assistantMessage)
    answeredQuestions.value++
    
    isProcessing.value = false
    isTalking.value = false
    
    await nextTick()
    scrollToBottom()
    
    // 更新进度
    updateProgress()
  }, 1500)
}

const generateAIResponse = async (question) => {
  // 模拟AI响应生成
  const responses = {
    'python': `
      <p>关于Python模块导入，有以下几种方式：</p>
      <ul>
        <li><code>import module_name</code> - 导入整个模块</li>
        <li><code>from module_name import function_name</code> - 导入特定函数</li>
        <li><code>import module_name as alias</code> - 给模块起别名</li>
      </ul>
      <p>例如：<code>import numpy as np</code></p>
    `,
    'lenet': `
      <p>LeNet是经典的卷积神经网络，结构如下：</p>
      <ol>
        <li>输入层：32×32像素图像</li>
        <li>卷积层C1：6个5×5卷积核</li>
        <li>池化层S2：2×2平均池化</li>
        <li>卷积层C3：16个5×5卷积核</li>
        <li>池化层S4：2×2平均池化</li>
        <li>全连接层：120个神经元</li>
        <li>全连接层：84个神经元</li>
        <li>输出层：10个神经元（分类）</li>
      </ol>
    `,
    'cnn': `
      <p>卷积神经网络（CNN）是一种深度学习模型，特别适合处理图像数据：</p>
      <ul>
        <li><strong>卷积层</strong>：提取图像特征</li>
        <li><strong>池化层</strong>：降低维度，减少计算量</li>
        <li><strong>全连接层</strong>：进行最终分类</li>
      </ul>
      <p>CNN的优势在于参数共享和局部连接，能够有效处理图像的空间结构。</p>
    `,
    'accuracy': `
      <p>提高模型准确率的方法：</p>
      <ul>
        <li>🔧 <strong>数据增强</strong>：旋转、缩放、翻转图像</li>
        <li>📊 <strong>调整超参数</strong>：学习率、批次大小等</li>
        <li>🏗️ <strong>改进网络结构</strong>：增加层数或改变架构</li>
        <li>🎯 <strong>正则化</strong>：Dropout、权重衰减</li>
        <li>📈 <strong>更多训练数据</strong>：扩大数据集规模</li>
      </ul>
    `
  }
  
  const lowerQuestion = question.toLowerCase()
  
  if (lowerQuestion.includes('python') || lowerQuestion.includes('模块') || lowerQuestion.includes('导入')) {
    return responses.python
  } else if (lowerQuestion.includes('lenet') || lowerQuestion.includes('网络结构')) {
    return responses.lenet
  } else if (lowerQuestion.includes('卷积') || lowerQuestion.includes('cnn')) {
    return responses.cnn
  } else if (lowerQuestion.includes('准确率') || lowerQuestion.includes('提高') || lowerQuestion.includes('优化')) {
    return responses.accuracy
  } else {
    return `
      <p>这是一个很好的问题！让我来为你解答：</p>
      <p>根据你的问题，我建议你可以：</p>
      <ul>
        <li>查看相关的代码示例</li>
        <li>尝试动手实践</li>
        <li>参考官方文档</li>
      </ul>
      <p>如果你需要更具体的帮助，请告诉我更多细节。</p>
    `
  }
}

const askQuickQuestion = (question) => {
  currentMessage.value = question
  sendMessage()
}

const rateMessage = (messageId, rating) => {
  const message = chatHistory.value.find(m => m.id === messageId)
  if (message) {
    message.rated = true
    message.rating = rating
    
    if (rating === 'helpful') {
      helpfulRating.value = Math.min(100, helpfulRating.value + 1)
    }
  }
}

const clearChat = () => {
  chatHistory.value = []
}

const exportChat = () => {
  const chatContent = chatHistory.value
    .filter(m => !m.isTyping)
    .map(m => `${m.type === 'user' ? '用户' : 'AI助手'}: ${m.content.replace(/<[^>]*>/g, '')}`)
    .join('\n\n')
  
  const blob = new Blob([chatContent], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = 'ai-chat-history.txt'
  a.click()
  URL.revokeObjectURL(url)
}

const searchKnowledge = () => {
  if (!searchQuery.value.trim()) {
    searchResults.value = []
    return
  }
  
  const query = searchQuery.value.toLowerCase()
  searchResults.value = knowledgeBase.value.filter(item => 
    item.title.toLowerCase().includes(query) ||
    item.summary.toLowerCase().includes(query) ||
    item.tags.some(tag => tag.toLowerCase().includes(query))
  )
}

const selectKnowledgeItem = (item) => {
  currentMessage.value = `请详细解释：${item.title}`
  sendMessage()
}

const applySuggestion = (suggestion) => {
  // 根据建议类型执行相应操作
  console.log('应用建议:', suggestion.action)
  
  // 可以触发相应的学习模块
  switch (suggestion.action) {
    case 'python-practice':
      // 跳转到Python练习
      break
    case 'cnn-visualization':
      // 跳转到CNN可视化
      break
    case 'project-practice':
      // 跳转到项目实践
      break
  }
}

const formatTime = (timestamp) => {
  return timestamp.toLocaleTimeString('zh-CN', { 
    hour: '2-digit', 
    minute: '2-digit' 
  })
}

const scrollToBottom = () => {
  if (chatMessages.value) {
    chatMessages.value.scrollTop = chatMessages.value.scrollHeight
  }
}

const updateProgress = () => {
  const progress = Math.min(100, answeredQuestions.value * 10)
  emit('progress-update', 'ai-assistant', progress)
}

// 生命周期
onMounted(() => {
  // 眨眼动画
  setInterval(() => {
    isBlinking.value = true
    setTimeout(() => {
      isBlinking.value = false
    }, 150)
  }, 3000)
  
  // 欢迎消息
  setTimeout(() => {
    chatHistory.value.push({
      id: Date.now(),
      type: 'assistant',
      content: `
        <p>你好！我是你的AI学习助手 🤖</p>
        <p>我可以帮助你：</p>
        <ul>
          <li>解答Python编程问题</li>
          <li>解释机器学习概念</li>
          <li>指导项目实践</li>
          <li>提供学习建议</li>
        </ul>
        <p>有什么问题尽管问我吧！</p>
      `,
      timestamp: new Date(),
      isTyping: false,
      rated: false
    })
  }, 1000)
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.ai-assistant-container {
  padding: 2rem;
  background: $secondary-color;
  min-height: 100vh;
}

.assistant-header {
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

.assistant-status {
  margin-bottom: 2rem;

  .status-card {
    background: $primary-color;
    border: 1px solid $border-color;

    :deep(.el-card__body) {
      background: $primary-color;
    }

    .assistant-info {
      display: grid;
      grid-template-columns: auto 1fr;
      gap: 2rem;
      align-items: center;

      .avatar-section {
        display: flex;
        align-items: center;
        gap: 1rem;

        .assistant-avatar {
          width: 80px;
          height: 80px;
          border-radius: 50%;
          background: linear-gradient(135deg, $accent-color 0%, $accent-color-light 100%);
          display: flex;
          align-items: center;
          justify-content: center;
          position: relative;
          transition: all 0.3s ease;

          &.talking {
            animation: pulse 1s infinite;
          }
          
          .avatar-face {
            .eyes {
              display: flex;
              gap: 8px;
              margin-bottom: 8px;
              
              .eye {
                width: 8px;
                height: 8px;
                background: white;
                border-radius: 50%;
                transition: all 0.15s ease;
                
                &.blink {
                  height: 2px;
                }
              }
            }
            
            .mouth {
              width: 12px;
              height: 6px;
              border: 2px solid white;
              border-top: none;
              border-radius: 0 0 12px 12px;
              transition: all 0.3s ease;
              
              &.talking {
                animation: talk 0.5s infinite alternate;
              }
            }
          }
        }
        
        .assistant-details {
          h3 {
            margin: 0 0 0.5rem 0;
            color: $text-color;
          }

          .status-info {
            display: flex;
            align-items: center;
            gap: 0.5rem;

            .mood {
              color: $text-secondary-color;
              font-size: 0.9rem;
            }
          }
        }
      }

      .stats-section {
        display: flex;
        gap: 2rem;
        justify-content: flex-end;

        .stat-item {
          text-align: center;

          .stat-value {
            font-size: 1.5rem;
            font-weight: bold;
            color: $accent-color;
          }

          .stat-label {
            color: $text-secondary-color;
            font-size: 0.9rem;
          }
        }
      }
    }
  }
}

.chat-interface {
  margin-bottom: 2rem;

  .chat-card {
    background: $primary-color;
    border: 1px solid $border-color;

    :deep(.el-card__header) {
      background: $secondary-color;
      border-bottom: 1px solid $border-color;
      color: $text-color;
    }

    :deep(.el-card__body) {
      background: $primary-color;
    }

    .chat-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-weight: 600;
      color: $text-color;

      .chat-controls {
        display: flex;
        gap: 0.5rem;
      }
    }

    .chat-messages {
      height: 400px;
      overflow-y: auto;
      padding: 1rem 0;
      background: $primary-color;
      
      .message-item {
        display: flex;
        margin-bottom: 1rem;
        
        &.user {
          justify-content: flex-end;

          .message-content {
            .message-bubble {
              background: $accent-color;
              color: $primary-color;
            }
          }
        }

        &.assistant {
          justify-content: flex-start;
        }

        .message-avatar {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin: 0 0.5rem;
          background: $secondary-color;

          .el-icon {
            font-size: 1.2rem;
            color: $accent-color;
          }

          .ai-avatar {
            font-size: 1.5rem;
          }
        }
        
        .message-content {
          max-width: 70%;

          .message-bubble {
            background: $secondary-color;
            padding: 0.8rem 1rem;
            border-radius: 12px;
            margin-bottom: 0.3rem;
            border: 1px solid $border-color;
            
            .typing-indicator {
              display: flex;
              gap: 0.3rem;

              span {
                width: 8px;
                height: 8px;
                background: $text-secondary-color;
                border-radius: 50%;
                animation: typing 1.4s infinite ease-in-out;

                &:nth-child(2) {
                  animation-delay: 0.2s;
                }

                &:nth-child(3) {
                  animation-delay: 0.4s;
                }
              }
            }

            .message-text {
              color: $text-color;

              :deep(p) {
                margin: 0 0 0.5rem 0;
                color: $text-color;

                &:last-child {
                  margin-bottom: 0;
                }
              }

              :deep(ul), :deep(ol) {
                margin: 0.5rem 0;
                padding-left: 1.5rem;
                color: $text-color;
              }

              :deep(code) {
                background: $primary-color;
                color: $accent-color;
                padding: 0.2rem 0.4rem;
                border-radius: 4px;
                font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
                border: 1px solid $border-color;
              }
            }
          }
          
          .message-time {
            font-size: 0.8rem;
            color: #7f8c8d;
            text-align: right;
          }
          
          .message-actions {
            margin-top: 0.5rem;
            display: flex;
            gap: 0.5rem;
          }
        }
      }
      
      .empty-chat {
        text-align: center;
        padding: 3rem 1rem;
        color: #7f8c8d;
        
        .empty-icon {
          font-size: 3rem;
          margin-bottom: 1rem;
        }
        
        h3 {
          margin: 0 0 0.5rem 0;
          color: #2c3e50;
        }
      }
    }
    
    .chat-input {
      border-top: 1px solid #e9ecef;
      padding-top: 1rem;
      
      .input-container {
        margin-bottom: 1rem;
        
        .message-input {
          :deep(.el-input-group__append) {
            padding: 0;
            
            .el-button {
              border-radius: 0 4px 4px 0;
            }
          }
        }
      }
      
      .quick-questions {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        flex-wrap: wrap;
        
        .quick-label {
          color: #7f8c8d;
          font-size: 0.9rem;
        }
        
        .quick-question {
          cursor: pointer;
          transition: all 0.3s ease;
          
          &:hover {
            background: #3498db;
            color: white;
          }
        }
      }
    }
  }
}

.knowledge-base, .learning-suggestions {
  margin-bottom: 2rem;
  
  .search-section {
    .search-results {
      margin-top: 1rem;
      max-height: 300px;
      overflow-y: auto;
      
      .result-item {
        padding: 1rem;
        border: 1px solid #e9ecef;
        border-radius: 8px;
        margin-bottom: 0.5rem;
        cursor: pointer;
        transition: all 0.3s ease;
        
        &:hover {
          background: #f8f9fa;
          border-color: #3498db;
        }
        
        .result-title {
          font-weight: 600;
          color: #2c3e50;
          margin-bottom: 0.3rem;
        }
        
        .result-summary {
          color: #7f8c8d;
          margin-bottom: 0.5rem;
          font-size: 0.9rem;
        }
        
        .result-tags {
          display: flex;
          gap: 0.3rem;
          flex-wrap: wrap;
        }
      }
    }
  }
  
  .suggestions-content {
    .suggestion-item {
      display: flex;
      align-items: center;
      padding: 1rem;
      border: 1px solid #e9ecef;
      border-radius: 8px;
      margin-bottom: 1rem;
      
      .suggestion-icon {
        margin-right: 1rem;
        font-size: 1.5rem;
        color: #f39c12;
      }
      
      .suggestion-content {
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

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes talk {
  0% {
    height: 6px;
  }
  100% {
    height: 2px;
  }
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .assistant-info {
    grid-template-columns: 1fr !important;
    text-align: center;
  }
  
  .stats-section {
    justify-content: center !important;
  }
  
  .message-content {
    max-width: 85% !important;
  }
  
  .quick-questions {
    flex-direction: column;
    align-items: flex-start !important;
  }
}
</style>
