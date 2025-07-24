<template>
  <div class="python-basics-container">
    <div class="basics-header">
      <h2>
        <el-icon><Document /></el-icon>
        Python基础知识
      </h2>
      <p>掌握Python基础，为AI项目开发打下坚实基础</p>
    </div>

    <!-- 知识点导航 -->
    <div class="knowledge-nav">
      <el-steps :active="currentStep" finish-status="success" align-center>
        <el-step 
          v-for="(topic, index) in knowledgeTopics" 
          :key="index"
          :title="topic.title"
          :description="topic.description"
          @click="goToStep(index)"
        />
      </el-steps>
    </div>

    <!-- 知识点内容 -->
    <div class="knowledge-content">
      <el-card class="content-card">
        <template #header>
          <div class="card-header">
            <span>{{ currentTopic.title }}</span>
            <el-tag :type="getTopicStatus(currentStep)" size="small">
              {{ getTopicStatusText(currentStep) }}
            </el-tag>
          </div>
        </template>

        <!-- 理论讲解 -->
        <div class="theory-section">
          <h3>📚 理论讲解</h3>
          <div class="theory-content" v-html="currentTopic.theory"></div>
        </div>

        <!-- 代码示例 -->
        <div class="code-example-section">
          <h3>💻 代码示例</h3>
          <div class="code-examples">
            <div 
              v-for="(example, index) in currentTopic.examples" 
              :key="index"
              class="example-item"
            >
              <h4>{{ example.title }}</h4>
              <div class="code-block">
                <pre><code>{{ example.code }}</code></pre>
              </div>
              <div class="example-explanation">
                <p>{{ example.explanation }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 互动练习 -->
        <div class="practice-section">
          <h3>🎯 互动练习</h3>
          <div class="practice-content">
            <div class="question">
              <h4>{{ currentTopic.practice.question }}</h4>
              <div class="options">
                <el-radio-group v-model="selectedAnswer" @change="checkAnswer">
                  <el-radio 
                    v-for="(option, index) in currentTopic.practice.options" 
                    :key="index"
                    :label="index"
                    class="option-item"
                  >
                    {{ option }}
                  </el-radio>
                </el-radio-group>
              </div>
              
              <div v-if="showAnswer" class="answer-feedback">
                <el-alert
                  :type="isCorrect ? 'success' : 'error'"
                  :title="isCorrect ? '回答正确！' : '回答错误'"
                  :description="currentTopic.practice.explanation"
                  show-icon
                  :closable="false"
                />
              </div>
            </div>
          </div>
        </div>

        <!-- 导航按钮 -->
        <div class="navigation-buttons">
          <el-button 
            @click="previousStep" 
            :disabled="currentStep === 0"
            type="default"
          >
            <el-icon><ArrowLeft /></el-icon>
            上一个
          </el-button>
          
          <el-button 
            @click="nextStep" 
            :disabled="currentStep === knowledgeTopics.length - 1"
            type="primary"
          >
            下一个
            <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 学习进度 -->
    <div class="progress-section">
      <el-card class="progress-card">
        <div class="progress-content">
          <div class="progress-stats">
            <h3>学习统计</h3>
            <div class="stats-grid">
              <div class="stat-item">
                <div class="stat-number">{{ completedTopics }}</div>
                <div class="stat-label">已完成</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ knowledgeTopics.length }}</div>
                <div class="stat-label">总计</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ correctAnswers }}</div>
                <div class="stat-label">正确答案</div>
              </div>
            </div>
          </div>
          
          <div class="progress-bar">
            <h4>总体进度</h4>
            <el-progress 
              :percentage="overallProgress" 
              :stroke-width="12"
              :show-text="true"
              status="success"
            />
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { 
  Document, ArrowLeft, ArrowRight 
} from '@element-plus/icons-vue'

// 定义事件
const emit = defineEmits(['progress-update'])

// 响应式数据
const currentStep = ref(0)
const selectedAnswer = ref(null)
const showAnswer = ref(false)
const isCorrect = ref(false)
const completedTopics = ref(0)
const correctAnswers = ref(0)

// 知识点数据
const knowledgeTopics = ref([
  {
    title: 'Python简介',
    description: '了解Python语言特点',
    theory: `
      <p><strong>Python</strong> 是一种高级编程语言，具有以下特点：</p>
      <ul>
        <li><strong>简洁易读</strong>：语法接近自然语言，易于理解和学习</li>
        <li><strong>跨平台</strong>：可在Windows、Mac、Linux等系统运行</li>
        <li><strong>丰富的库</strong>：拥有庞大的第三方库生态系统</li>
        <li><strong>解释型语言</strong>：无需编译，直接运行</li>
        <li><strong>面向对象</strong>：支持面向对象编程范式</li>
      </ul>
      <p>Python在人工智能领域特别受欢迎，因为它提供了强大的数据处理和机器学习库。</p>
    `,
    examples: [
      {
        title: '第一个Python程序',
        code: `# 这是一个注释
print("Hello, Python!")
print("欢迎来到AI世界！")`,
        explanation: '使用print()函数输出文本，# 开头的是注释'
      }
    ],
    practice: {
      question: 'Python语言的主要特点是什么？',
      options: [
        '只能在Windows上运行',
        '语法复杂，难以学习',
        '简洁易读，跨平台',
        '只适用于网页开发'
      ],
      correct: 2,
      explanation: 'Python以其简洁易读的语法和跨平台特性而闻名，这使得它成为初学者的理想选择。'
    }
  },
  {
    title: '数据类型',
    description: '掌握基本数据类型',
    theory: `
      <p>Python中的基本数据类型包括：</p>
      <ul>
        <li><strong>整数 (int)</strong>：如 1, 2, 100, -5</li>
        <li><strong>浮点数 (float)</strong>：如 3.14, 2.0, -1.5</li>
        <li><strong>字符串 (str)</strong>：如 "hello", '世界'</li>
        <li><strong>布尔值 (bool)</strong>：True 或 False</li>
        <li><strong>列表 (list)</strong>：如 [1, 2, 3]</li>
        <li><strong>字典 (dict)</strong>：如 {"name": "张三", "age": 25}</li>
      </ul>
    `,
    examples: [
      {
        title: '数据类型示例',
        code: `# 不同的数据类型
number = 42          # 整数
pi = 3.14159        # 浮点数
name = "Python"     # 字符串
is_fun = True       # 布尔值
colors = ["红", "绿", "蓝"]  # 列表
person = {"姓名": "小明", "年龄": 20}  # 字典

print(f"数字: {number}")
print(f"圆周率: {pi}")
print(f"语言: {name}")`,
        explanation: '变量可以存储不同类型的数据，Python会自动识别数据类型'
      }
    ],
    practice: {
      question: '以下哪个是Python中的字典类型？',
      options: [
        '[1, 2, 3]',
        '{"name": "张三"}',
        '"hello world"',
        'True'
      ],
      correct: 1,
      explanation: '字典使用大括号{}，包含键值对，如{"name": "张三"}。'
    }
  },
  {
    title: '模块导入',
    description: '学习如何导入和使用模块',
    theory: `
      <p>Python的强大之处在于其丰富的模块库。模块是包含Python代码的文件，可以被其他程序导入和使用。</p>
      <h4>导入方式：</h4>
      <ul>
        <li><code>import 模块名</code> - 导入整个模块</li>
        <li><code>from 模块名 import 函数名</code> - 导入特定函数</li>
        <li><code>import 模块名 as 别名</code> - 给模块起别名</li>
      </ul>
      <p>常用的AI相关模块：</p>
      <ul>
        <li><strong>numpy</strong> - 数值计算</li>
        <li><strong>pandas</strong> - 数据分析</li>
        <li><strong>matplotlib</strong> - 数据可视化</li>
        <li><strong>torch</strong> - 深度学习</li>
      </ul>
    `,
    examples: [
      {
        title: '模块导入示例',
        code: `# 不同的导入方式
import math
from random import randint
import numpy as np

# 使用导入的模块
print(f"圆周率: {math.pi}")
print(f"随机数: {randint(1, 10)}")

# 使用numpy创建数组
arr = np.array([1, 2, 3, 4, 5])
print(f"数组平均值: {np.mean(arr)}")`,
        explanation: '通过导入模块，我们可以使用更多强大的功能'
      }
    ],
    practice: {
      question: '如果要给numpy模块起别名np，正确的导入语句是？',
      options: [
        'import numpy',
        'from numpy import np',
        'import numpy as np',
        'import np from numpy'
      ],
      correct: 2,
      explanation: '使用 "import numpy as np" 可以给模块起别名，这样使用时更简洁。'
    }
  }
])

// 计算属性
const currentTopic = computed(() => knowledgeTopics.value[currentStep.value])
const overallProgress = computed(() => {
  return Math.round((completedTopics.value / knowledgeTopics.value.length) * 100)
})

// 方法
const goToStep = (step) => {
  currentStep.value = step
  resetAnswer()
}

const nextStep = () => {
  if (currentStep.value < knowledgeTopics.value.length - 1) {
    currentStep.value++
    resetAnswer()
  }
}

const previousStep = () => {
  if (currentStep.value > 0) {
    currentStep.value--
    resetAnswer()
  }
}

const checkAnswer = () => {
  showAnswer.value = true
  isCorrect.value = selectedAnswer.value === currentTopic.value.practice.correct
  
  if (isCorrect.value) {
    correctAnswers.value++
    if (!isTopicCompleted(currentStep.value)) {
      completedTopics.value++
    }
  }
}

const resetAnswer = () => {
  selectedAnswer.value = null
  showAnswer.value = false
  isCorrect.value = false
}

const getTopicStatus = (step) => {
  return isTopicCompleted(step) ? 'success' : 'info'
}

const getTopicStatusText = (step) => {
  return isTopicCompleted(step) ? '已完成' : '学习中'
}

const isTopicCompleted = (step) => {
  // 简化的完成判断逻辑
  return step < completedTopics.value
}

// 监听进度变化
watch(overallProgress, (newProgress) => {
  emit('progress-update', 'python-basics', newProgress)
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.python-basics-container {
  padding: 2rem;
  background: $secondary-color;
  min-height: 100vh;
}

.basics-header {
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

.knowledge-nav {
  margin-bottom: 2rem;
  
  :deep(.el-steps) {
    .el-step__title {
      font-size: 14px;
      font-weight: 600;
    }
    
    .el-step__description {
      font-size: 12px;
    }
  }
}

.knowledge-content {
  margin-bottom: 2rem;

  .content-card {
    background: $primary-color;
    border: 1px solid $border-color;

    :deep(.el-card__header) {
      background: $secondary-color;
      border-bottom: 1px solid $border-color;
      color: $text-color;
    }

    :deep(.el-card__body) {
      background: $primary-color;
      color: $text-color;
    }

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-weight: 600;
      font-size: 1.1rem;
      color: $text-color;
    }

    .theory-section, .code-example-section, .practice-section {
      margin-bottom: 2rem;

      h3 {
        color: $text-color;
        margin-bottom: 1rem;
        font-size: 1.2rem;
      }
      
      .theory-content {
        :deep(ul) {
          padding-left: 1.5rem;

          li {
            margin-bottom: 0.5rem;
            line-height: 1.6;
            color: $text-secondary-color;
          }
        }

        :deep(p) {
          line-height: 1.6;
          margin-bottom: 1rem;
          color: $text-secondary-color;
        }
      }
    }

    .code-examples {
      .example-item {
        margin-bottom: 1.5rem;

        h4 {
          color: $accent-color;
          margin-bottom: 0.5rem;
        }

        .code-block {
          background: $secondary-color;
          border: 1px solid $border-color;
          border-radius: 4px;
          margin-bottom: 0.5rem;

          pre {
            margin: 0;
            padding: 1rem;
            font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
            font-size: 14px;
            line-height: 1.5;
            overflow-x: auto;
            color: $text-color;

            code {
              background: none;
              padding: 0;
              font-size: inherit;
              color: $text-color;
            }
          }
        }

        .example-explanation {
          color: $text-secondary-color;
          font-style: italic;
        }
      }
    }
    
    .practice-content {
      .question {
        h4 {
          color: $text-color;
          margin-bottom: 1rem;
        }

        .options {
          margin-bottom: 1rem;

          .option-item {
            display: block;
            margin-bottom: 0.5rem;
            padding: 0.5rem;
            border-radius: 4px;
            transition: background-color 0.3s;

            :deep(.el-radio__label) {
              color: $text-secondary-color;
            }

            &:hover {
              background-color: rgba(176, 179, 184, 0.1);
            }
          }
        }

        .answer-feedback {
          margin-top: 1rem;
        }
      }
    }
    
    .navigation-buttons {
      display: flex;
      justify-content: space-between;
      margin-top: 2rem;
      padding-top: 1rem;
      border-top: 1px solid #e9ecef;
    }
  }
}

.progress-section {
  .progress-card {
    background: $primary-color;
    border: 1px solid $border-color;

    :deep(.el-card__body) {
      background: $primary-color;
    }

    .progress-content {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 2rem;
      align-items: center;

      .progress-stats {
        h3 {
          color: $text-color;
          margin-bottom: 1rem;
        }

        .stats-grid {
          display: grid;
          grid-template-columns: repeat(3, 1fr);
          gap: 1rem;

          .stat-item {
            text-align: center;
            padding: 1rem;
            background: $secondary-color;
            border-radius: 8px;
            border: 1px solid $border-color;

            .stat-number {
              font-size: 2rem;
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

      .progress-bar {
        h4 {
          color: $text-color;
          margin-bottom: 1rem;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .progress-content {
    grid-template-columns: 1fr !important;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr) !important;
  }
  
  .navigation-buttons {
    flex-direction: column;
    gap: 1rem;
  }
}
</style>
