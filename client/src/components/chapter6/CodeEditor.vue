<template>
  <div class="code-editor-container">
    <div class="editor-header">
      <h2>
        <el-icon><Edit /></el-icon>
        交互式Python代码编辑器
      </h2>
      <p>在这里编写和运行Python代码，体验人工智能项目开发</p>
    </div>

    <!-- 代码示例选择器 -->
    <div class="example-selector">
      <el-card class="selector-card">
        <div class="selector-header">
          <h3>选择代码示例</h3>
          <el-button-group>
            <el-button 
              v-for="example in codeExamples" 
              :key="example.id"
              :type="selectedExample === example.id ? 'primary' : 'default'"
              @click="loadExample(example.id)"
              size="small"
            >
              {{ example.title }}
            </el-button>
          </el-button-group>
        </div>
      </el-card>
    </div>

    <!-- 代码编辑区域 -->
    <div class="editor-workspace">
      <el-row :gutter="20">
        <!-- 左侧：代码编辑器 -->
        <el-col :span="12">
          <el-card class="editor-card">
            <template #header>
              <div class="card-header">
                <span>代码编辑器</span>
                <div class="header-actions">
                  <el-button 
                    type="success" 
                    @click="runCode" 
                    :loading="isRunning"
                    size="small"
                  >
                    <el-icon><CaretRight /></el-icon>
                    运行代码
                  </el-button>
                  <el-button 
                    type="warning" 
                    @click="clearCode" 
                    size="small"
                  >
                    <el-icon><Delete /></el-icon>
                    清空
                  </el-button>
                </div>
              </div>
            </template>
            
            <div class="code-editor-wrapper">
              <textarea
                ref="codeTextarea"
                v-model="code"
                class="code-textarea"
                placeholder="在这里输入Python代码..."
                @input="onCodeChange"
              ></textarea>
            </div>
          </el-card>
        </el-col>

        <!-- 右侧：输出结果 -->
        <el-col :span="12">
          <el-card class="output-card">
            <template #header>
              <div class="card-header">
                <span>运行结果</span>
                <el-tag :type="outputType" size="small">
                  {{ outputStatus }}
                </el-tag>
              </div>
            </template>
            
            <div class="output-content">
              <div v-if="!hasOutput" class="no-output">
                <el-icon class="output-icon"><Monitor /></el-icon>
                <p>运行代码后，结果将在这里显示</p>
              </div>
              
              <div v-else class="output-result">
                <pre v-if="output" class="output-text">{{ output }}</pre>
                <div v-if="error" class="error-text">
                  <el-icon><WarningFilled /></el-icon>
                  {{ error }}
                </div>
                
                <!-- 图表输出区域 -->
                <div v-if="chartData" class="chart-output">
                  <div ref="chartContainer" class="chart-container"></div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 学习进度和提示 -->
    <div class="learning-progress">
      <el-card class="progress-card">
        <div class="progress-content">
          <div class="progress-info">
            <h3>学习进度</h3>
            <el-progress 
              :percentage="progress" 
              :stroke-width="8"
              :show-text="true"
            />
            <p class="progress-text">
              已完成 {{ completedExamples.length }} / {{ codeExamples.length }} 个示例
            </p>
          </div>
          
          <div class="hints">
            <h4>💡 编程提示</h4>
            <ul>
              <li>尝试修改代码参数，观察结果变化</li>
              <li>使用 print() 函数输出中间结果</li>
              <li>遇到错误时，仔细阅读错误信息</li>
              <li>完成所有示例可获得"代码大师"徽章</li>
            </ul>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 成功动画 -->
    <div v-if="showSuccessAnimation" class="success-animation">
      <div class="success-content">
        <el-icon class="success-icon"><Check /></el-icon>
        <h3>代码运行成功！</h3>
        <p>+10 经验值</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { 
  Edit, CaretRight, Delete, Monitor, 
  WarningFilled, Check 
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'

// 定义事件
const emit = defineEmits(['progress-update'])

// 响应式数据
const code = ref('')
const output = ref('')
const error = ref('')
const isRunning = ref(false)
const selectedExample = ref(1)
const completedExamples = ref([])
const showSuccessAnimation = ref(false)
const chartData = ref(null)
const codeTextarea = ref(null)
const chartContainer = ref(null)

// 代码示例
const codeExamples = ref([
  {
    id: 1,
    title: 'Python基础',
    code: `# Python基础语法示例
import numpy as np
import matplotlib.pyplot as plt

# 创建数据
x = np.linspace(0, 10, 100)
y = np.sin(x)

# 输出基本信息
print("数组长度:", len(x))
print("最大值:", np.max(y))
print("最小值:", np.min(y))

# 简单的数学运算
result = np.mean(y)
print(f"平均值: {result:.4f}")
`
  },
  {
    id: 2,
    title: 'Numpy操作',
    code: `# Numpy数组操作示例
import numpy as np

# 创建多维数组
matrix = np.array([[1, 2, 3], [4, 5, 6]])
print("原始矩阵:")
print(matrix)

# 矩阵运算
transposed = matrix.T
print("\\n转置矩阵:")
print(transposed)

# 统计运算
print(f"\\n矩阵形状: {matrix.shape}")
print(f"矩阵平均值: {np.mean(matrix):.2f}")
print(f"矩阵标准差: {np.std(matrix):.2f}")
`
  },
  {
    id: 3,
    title: 'Pandas数据处理',
    code: `# Pandas数据处理示例
import pandas as pd
import numpy as np

# 创建示例数据
data = {
    '姓名': ['张三', '李四', '王五', '赵六'],
    '年龄': [25, 30, 35, 28],
    '城市': ['北京', '上海', '广州', '深圳'],
    '薪资': [8000, 12000, 15000, 10000]
}

df = pd.DataFrame(data)
print("原始数据:")
print(df)

# 数据分析
print(f"\\n平均年龄: {df['年龄'].mean():.1f}")
print(f"平均薪资: {df['薪资'].mean():.0f}")

# 数据筛选
high_salary = df[df['薪资'] > 10000]
print("\\n高薪人员:")
print(high_salary)
`
  }
])

// 计算属性
const hasOutput = computed(() => output.value || error.value || chartData.value)
const outputType = computed(() => error.value ? 'danger' : 'success')
const outputStatus = computed(() => error.value ? '错误' : '成功')
const progress = computed(() => {
  return Math.round((completedExamples.value.length / codeExamples.value.length) * 100)
})

// 方法
const loadExample = (exampleId) => {
  selectedExample.value = exampleId
  const example = codeExamples.value.find(ex => ex.id === exampleId)
  if (example) {
    code.value = example.code
    clearOutput()
  }
}

const runCode = async () => {
  if (!code.value.trim()) {
    error.value = '请输入代码后再运行'
    return
  }

  isRunning.value = true
  clearOutput()
  
  try {
    // 模拟代码执行（实际项目中可以集成Pyodide或后端API）
    await simulateCodeExecution()
    
    // 标记示例为已完成
    if (!completedExamples.value.includes(selectedExample.value)) {
      completedExamples.value.push(selectedExample.value)
      showSuccessAnimation.value = true
      setTimeout(() => {
        showSuccessAnimation.value = false
      }, 2000)
    }
    
    // 更新进度
    emit('progress-update', 'code-practice', progress.value)
    
  } catch (err) {
    error.value = err.message
  } finally {
    isRunning.value = false
  }
}

const simulateCodeExecution = async () => {
  // 模拟执行延迟
  await new Promise(resolve => setTimeout(resolve, 1000))
  
  // 根据代码内容生成模拟输出
  if (code.value.includes('numpy') || code.value.includes('np.')) {
    output.value = `数组长度: 100
最大值: 1.0
最小值: -1.0
平均值: 0.0000

矩阵形状: (2, 3)
矩阵平均值: 3.50
矩阵标准差: 1.71`
  } else if (code.value.includes('pandas') || code.value.includes('pd.')) {
    output.value = `原始数据:
   姓名  年龄  城市    薪资
0  张三  25  北京   8000
1  李四  30  上海  12000
2  王五  35  广州  15000
3  赵六  28  深圳  10000

平均年龄: 29.5
平均薪资: 11250

高薪人员:
   姓名  年龄  城市    薪资
1  李四  30  上海  12000
2  王五  35  广州  15000`
  } else {
    output.value = '代码执行成功！\n输出结果已显示。'
  }
}

const clearCode = () => {
  code.value = ''
  clearOutput()
}

const clearOutput = () => {
  output.value = ''
  error.value = ''
  chartData.value = null
}

const onCodeChange = () => {
  // 代码变化时的处理逻辑
}

// 生命周期
onMounted(() => {
  // 加载第一个示例
  loadExample(1)
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.code-editor-container {
  padding: 2rem;
  background: $secondary-color;
  min-height: 100vh;
}

.editor-header {
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

.example-selector {
  margin-bottom: 2rem;

  .selector-card {
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

    .selector-header {
      h3 {
        margin-bottom: 1rem;
        color: $text-color;
      }
    }
  }
}

.editor-workspace {
  margin-bottom: 2rem;

  .editor-card, .output-card {
    height: 500px;
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

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: $text-color;

      .header-actions {
        display: flex;
        gap: 0.5rem;
      }
    }
  }
  
  .code-editor-wrapper {
    height: 400px;

    .code-textarea {
      width: 100%;
      height: 100%;
      border: none;
      outline: none;
      font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
      font-size: 14px;
      line-height: 1.5;
      padding: 1rem;
      background: $secondary-color;
      color: $text-color;
      border-radius: 4px;
      resize: none;
      border: 1px solid $border-color;

      &::placeholder {
        color: $text-secondary-color;
      }

      &:focus {
        outline: 2px solid $accent-color;
        border-color: $accent-color;
      }
    }
  }
  
  .output-content {
    height: 400px;
    overflow-y: auto;
    
    .no-output {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 100%;
      color: $text-secondary-color;

      .output-icon {
        font-size: 3rem;
        margin-bottom: 1rem;
        color: $accent-color;
      }

      p {
        color: $text-secondary-color;
      }
    }
    
    .output-result {
      .output-text {
        background: $secondary-color;
        color: $text-color;
        padding: 1rem;
        border-radius: 4px;
        font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
        font-size: 14px;
        line-height: 1.5;
        margin: 0;
        white-space: pre-wrap;
        border: 1px solid $border-color;
      }

      .error-text {
        background: rgba(231, 76, 60, 0.1);
        color: #e74c3c;
        padding: 1rem;
        border-radius: 4px;
        border-left: 4px solid #e74c3c;
        border: 1px solid rgba(231, 76, 60, 0.3);

        .el-icon {
          margin-right: 0.5rem;
        }
      }
      
      .chart-container {
        height: 300px;
        margin-top: 1rem;
      }
    }
  }
}

.learning-progress {
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

      .progress-info {
        h3 {
          margin-bottom: 1rem;
          color: $text-color;
        }

        .progress-text {
          margin-top: 1rem;
          color: $text-secondary-color;
        }
      }

      .hints {
        h4 {
          margin-bottom: 1rem;
          color: $accent-color;
        }

        ul {
          list-style: none;
          padding: 0;

          li {
            padding: 0.5rem 0;
            color: $text-secondary-color;
            border-bottom: 1px solid $border-color;

            &:last-child {
              border-bottom: none;
            }
          }
        }
      }
    }
  }
}

.success-animation {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  background: linear-gradient(135deg, $accent-color, $accent-color-light);
  color: $primary-color;
  padding: 2rem;
  border-radius: 12px;
  text-align: center;
  animation: bounceIn 0.6s ease-out;
  box-shadow: 0 8px 32px rgba(176, 179, 184, 0.3);
  border: 1px solid $accent-color-light;

  .success-icon {
    font-size: 3rem;
    margin-bottom: 1rem;
    color: $primary-color;
  }

  h3 {
    margin: 0 0 0.5rem 0;
    color: $primary-color;
  }

  p {
    margin: 0;
    font-size: 1.2rem;
    font-weight: bold;
    color: $primary-color;
  }
}

@keyframes bounceIn {
  0% {
    transform: translate(-50%, -50%) scale(0.3);
    opacity: 0;
  }
  50% {
    transform: translate(-50%, -50%) scale(1.05);
  }
  70% {
    transform: translate(-50%, -50%) scale(0.9);
  }
  100% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 1;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .editor-workspace {
    .el-row {
      flex-direction: column;
    }
    
    .el-col {
      width: 100% !important;
      margin-bottom: 1rem;
    }
  }
  
  .learning-progress .progress-content {
    grid-template-columns: 1fr;
  }
}
</style>
