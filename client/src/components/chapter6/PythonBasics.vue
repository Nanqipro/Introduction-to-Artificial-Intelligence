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

    <!-- 学习模式选择 -->
    <div class="mode-selector">
      <el-card class="mode-card">
        <div class="mode-buttons">
          <el-button
            :type="learningMode === 'tutorial' ? 'primary' : 'default'"
            @click="setLearningMode('tutorial')"
            size="large"
          >
            <el-icon><Reading /></el-icon>
            教程模式
          </el-button>
          <el-button
            :type="learningMode === 'quiz' ? 'primary' : 'default'"
            @click="setLearningMode('quiz')"
            size="large"
          >
            <el-icon><EditPen /></el-icon>
            练习模式 (100题)
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 积分显示 -->
    <div class="score-display">
      <el-card class="score-card">
        <div class="score-content">
          <div class="score-item">
            <el-icon><Trophy /></el-icon>
            <span class="score-label">总积分</span>
            <span class="score-value">{{ totalScore }}</span>
          </div>
          <div class="score-item">
            <el-icon><Check /></el-icon>
            <span class="score-label">已完成</span>
            <span class="score-value">{{ completedQuestions }}/{{ totalQuestions }}</span>
          </div>
          <div class="score-item">
            <el-icon><Star /></el-icon>
            <span class="score-label">正确率</span>
            <span class="score-value">{{ accuracyRate }}%</span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 教程模式内容 -->
    <div v-if="learningMode === 'tutorial'" class="knowledge-content">
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

    <!-- 练习模式内容 -->
    <div v-if="learningMode === 'quiz'" class="quiz-content">
      <el-card class="quiz-card">
        <template #header>
          <div class="quiz-header">
            <span>Python基础练习 - 第{{ currentQuestionIndex + 1 }}题</span>
            <div class="quiz-progress">
              <el-progress
                :percentage="quizProgress"
                :stroke-width="8"
                :show-text="true"
              />
            </div>
          </div>
        </template>

        <div class="quiz-question">
          <div class="question-content">
            <h3>{{ currentQuestion.question }}</h3>
            <div v-if="currentQuestion.code" class="question-code">
              <pre><code>{{ currentQuestion.code }}</code></pre>
            </div>
          </div>

          <div class="question-options">
            <el-radio-group
              v-model="selectedAnswer"
              @change="handleAnswerSelect"
              :disabled="questionAnswered"
            >
              <el-radio
                v-for="(option, index) in currentQuestion.options"
                :key="index"
                :label="index"
                class="option-item"
              >
                {{ option }}
              </el-radio>
            </el-radio-group>
          </div>

          <div v-if="questionAnswered" class="answer-feedback">
            <el-alert
              :type="isCurrentAnswerCorrect ? 'success' : 'error'"
              :title="isCurrentAnswerCorrect ? '回答正确！' : '回答错误'"
              :description="currentQuestion.explanation"
              show-icon
              :closable="false"
            />
            <div class="score-gained" v-if="isCurrentAnswerCorrect">
              <el-tag type="success" size="large">
                +{{ currentQuestion.points }} 积分
              </el-tag>
            </div>
          </div>

          <div class="quiz-navigation">
            <el-button
              @click="previousQuestion"
              :disabled="currentQuestionIndex === 0"
              type="default"
            >
              <el-icon><ArrowLeft /></el-icon>
              上一题
            </el-button>

            <el-button
              v-if="!questionAnswered"
              @click="submitAnswer"
              :disabled="selectedAnswer === null"
              type="primary"
            >
              提交答案
            </el-button>

            <el-button
              v-if="questionAnswered"
              @click="nextQuestion"
              :disabled="currentQuestionIndex === pythonQuestions.length - 1"
              type="primary"
            >
              下一题
              <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
        </div>
      </el-card>

      <!-- 练习统计 -->
      <div class="quiz-stats">
        <el-card class="stats-card">
          <template #header>
            <span>练习统计</span>
          </template>
          <div class="stats-grid">
            <div class="stat-item">
              <div class="stat-number">{{ correctAnswers }}</div>
              <div class="stat-label">正确</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ wrongAnswers }}</div>
              <div class="stat-label">错误</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ totalScore }}</div>
              <div class="stat-label">总积分</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ averageScore }}</div>
              <div class="stat-label">平均分</div>
            </div>
          </div>
        </el-card>
      </div>
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
  Document, ArrowLeft, ArrowRight, Reading, EditPen,
  Trophy, Check, Star
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

// 新增：练习模式相关数据
const learningMode = ref('tutorial') // 'tutorial' 或 'quiz'
const currentQuestionIndex = ref(0)
const questionAnswered = ref(false)
const isCurrentAnswerCorrect = ref(false)
const totalScore = ref(0)
const wrongAnswers = ref(0)
const answeredQuestions = ref(new Set()) // 记录已回答的题目

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

// 100道Python选择题
const pythonQuestions = ref([
  // 基础语法题 (1-20)
  {
    id: 1,
    question: "Python中哪个关键字用于定义函数？",
    options: ["function", "def", "func", "define"],
    correct: 1,
    points: 5,
    explanation: "在Python中，使用'def'关键字来定义函数。"
  },
  {
    id: 2,
    question: "Python中的注释符号是什么？",
    options: ["//", "/**/", "#", "<!---->"],
    correct: 2,
    points: 5,
    explanation: "Python使用'#'符号来表示单行注释。"
  },
  {
    id: 3,
    question: "以下哪个是Python的正确变量名？",
    options: ["2variable", "variable-name", "variable_name", "variable name"],
    correct: 2,
    points: 5,
    explanation: "Python变量名可以包含字母、数字和下划线，但不能以数字开头，不能包含空格或连字符。"
  },
  {
    id: 4,
    question: "Python中字符串的索引从几开始？",
    options: ["1", "0", "-1", "任意数字"],
    correct: 1,
    points: 5,
    explanation: "Python中字符串和列表的索引都是从0开始的。"
  },
  {
    id: 5,
    question: "以下哪个运算符用于Python中的幂运算？",
    options: ["^", "**", "pow", "^2"],
    correct: 1,
    points: 5,
    explanation: "Python中使用'**'运算符进行幂运算，例如2**3表示2的3次方。"
  },
  {
    id: 6,
    question: "Python中如何获取字符串的长度？",
    options: ["length()", "len()", "size()", "count()"],
    correct: 1,
    points: 5,
    explanation: "使用len()函数可以获取字符串、列表等序列的长度。"
  },
  {
    id: 7,
    question: "Python中哪个关键字用于创建类？",
    options: ["class", "Class", "create", "new"],
    correct: 0,
    points: 5,
    explanation: "Python使用'class'关键字来定义类。"
  },
  {
    id: 8,
    question: "以下哪个是Python中的逻辑运算符？",
    options: ["&&", "||", "and", "!"],
    correct: 2,
    points: 5,
    explanation: "Python中的逻辑运算符是and、or、not，而不是&&、||、!。"
  },
  {
    id: 9,
    question: "Python中如何表示空值？",
    options: ["null", "NULL", "None", "empty"],
    correct: 2,
    points: 5,
    explanation: "Python中使用'None'来表示空值或无值。"
  },
  {
    id: 10,
    question: "以下哪个方法用于将字符串转换为大写？",
    options: ["toUpper()", "upper()", "uppercase()", "UPPER()"],
    correct: 1,
    points: 5,
    explanation: "Python字符串的upper()方法用于将字符串转换为大写。"
  },
  {
    id: 11,
    question: "Python中的缩进有什么作用？",
    options: ["仅为了美观", "表示代码块", "没有作用", "可选的"],
    correct: 1,
    points: 5,
    explanation: "Python使用缩进来表示代码块，这是语法的一部分，不仅仅是为了美观。"
  },
  {
    id: 12,
    question: "以下哪个是Python中的可变数据类型？",
    options: ["tuple", "string", "list", "int"],
    correct: 2,
    points: 5,
    explanation: "list是可变数据类型，可以修改其内容；而tuple、string、int都是不可变类型。"
  },
  {
    id: 13,
    question: "Python中如何创建一个空列表？",
    options: ["list()", "[]", "list[]", "以上都可以"],
    correct: 3,
    points: 5,
    explanation: "可以使用list()函数或[]来创建空列表，两种方法都是正确的。"
  },
  {
    id: 14,
    question: "以下哪个关键字用于异常处理？",
    options: ["catch", "try", "handle", "error"],
    correct: 1,
    points: 5,
    explanation: "Python使用try-except语句进行异常处理。"
  },
  {
    id: 15,
    question: "Python中的pass语句有什么作用？",
    options: ["跳过循环", "占位符", "传递参数", "退出程序"],
    correct: 1,
    points: 5,
    explanation: "pass是一个空操作语句，用作占位符，当语法需要语句但程序不需要执行任何操作时使用。"
  },
  {
    id: 16,
    question: "以下哪个是Python中的比较运算符？",
    options: ["=", "==", "===", "eq"],
    correct: 1,
    points: 5,
    explanation: "'=='用于比较两个值是否相等，'='用于赋值。"
  },
  {
    id: 17,
    question: "Python中如何表示多行字符串？",
    options: ["单引号", "双引号", "三引号", "反引号"],
    correct: 2,
    points: 5,
    explanation: "Python中使用三引号（'''或\"\"\"）来表示多行字符串。"
  },
  {
    id: 18,
    question: "以下哪个方法用于向列表末尾添加元素？",
    options: ["add()", "append()", "insert()", "push()"],
    correct: 1,
    points: 5,
    explanation: "append()方法用于向列表末尾添加一个元素。"
  },
  {
    id: 19,
    question: "Python中的字典使用什么符号？",
    options: ["[]", "{}", "()", "<>"],
    correct: 1,
    points: 5,
    explanation: "Python字典使用大括号{}，格式为{key: value}。"
  },
  {
    id: 20,
    question: "以下哪个是Python中的循环语句？",
    options: ["loop", "for", "repeat", "cycle"],
    correct: 1,
    points: 5,
    explanation: "Python中有for循环和while循环两种循环语句。"
  },
  // 数据类型题 (21-40)
  {
    id: 21,
    question: "Python中哪种数据类型是有序且可变的？",
    options: ["tuple", "set", "list", "dict"],
    correct: 2,
    points: 6,
    explanation: "list是有序且可变的数据类型，tuple有序但不可变，set无序但可变。"
  },
  {
    id: 22,
    question: "以下代码的输出是什么？\nprint(type(3.14))",
    code: "print(type(3.14))",
    options: ["<class 'int'>", "<class 'float'>", "<class 'double'>", "<class 'number'>"],
    correct: 1,
    points: 6,
    explanation: "3.14是浮点数，类型为float。"
  },
  {
    id: 23,
    question: "Python中如何创建一个集合(set)？",
    options: ["{1,2,3}", "set([1,2,3])", "set{1,2,3}", "以上都可以"],
    correct: 3,
    points: 6,
    explanation: "可以使用{1,2,3}或set([1,2,3])来创建集合。"
  },
  {
    id: 24,
    question: "以下哪个方法可以删除字典中的键值对？",
    options: ["remove()", "delete()", "pop()", "clear()"],
    correct: 2,
    points: 6,
    explanation: "pop()方法可以删除字典中指定的键值对并返回值。"
  },
  {
    id: 25,
    question: "Python中字符串是否可变？",
    options: ["可变", "不可变", "取决于内容", "取决于长度"],
    correct: 1,
    points: 6,
    explanation: "Python中字符串是不可变的，任何修改操作都会创建新的字符串对象。"
  },
  {
    id: 26,
    question: "以下代码的输出是什么？\nprint([1,2,3] + [4,5,6])",
    code: "print([1,2,3] + [4,5,6])",
    options: ["[1,2,3,4,5,6]", "[5,7,9]", "错误", "[1,2,3][4,5,6]"],
    correct: 0,
    points: 6,
    explanation: "列表的+操作符用于连接两个列表。"
  },
  {
    id: 27,
    question: "Python中如何获取字典的所有键？",
    options: ["dict.keys()", "dict.getKeys()", "keys(dict)", "dict.allKeys()"],
    correct: 0,
    points: 6,
    explanation: "使用dict.keys()方法可以获取字典的所有键。"
  },
  {
    id: 28,
    question: "以下哪个是Python中的不可变数据类型？",
    options: ["list", "dict", "set", "tuple"],
    correct: 3,
    points: 6,
    explanation: "tuple是不可变数据类型，一旦创建就不能修改。"
  },
  {
    id: 29,
    question: "Python中如何检查一个键是否在字典中？",
    options: ["key in dict", "dict.has(key)", "dict.contains(key)", "key.in(dict)"],
    correct: 0,
    points: 6,
    explanation: "使用'key in dict'语法来检查键是否存在于字典中。"
  },
  {
    id: 30,
    question: "以下代码的输出是什么？\nprint('Hello'[1])",
    code: "print('Hello'[1])",
    options: ["H", "e", "l", "错误"],
    correct: 1,
    points: 6,
    explanation: "字符串索引从0开始，'Hello'[1]返回第二个字符'e'。"
  },
  {
    id: 31,
    question: "Python中range(5)生成什么？",
    options: ["[1,2,3,4,5]", "[0,1,2,3,4]", "[0,1,2,3,4,5]", "1到5的数字"],
    correct: 1,
    points: 6,
    explanation: "range(5)生成从0到4的数字序列，不包括5。"
  },
  {
    id: 32,
    question: "以下哪个方法用于将列表中的元素连接成字符串？",
    options: ["join()", "connect()", "merge()", "combine()"],
    correct: 0,
    points: 6,
    explanation: "join()方法用于将列表中的字符串元素连接成一个字符串。"
  },
  {
    id: 33,
    question: "Python中如何创建一个空字典？",
    options: ["{}", "dict()", "dictionary()", "以上都可以"],
    correct: 3,
    points: 6,
    explanation: "可以使用{}或dict()来创建空字典。"
  },
  {
    id: 34,
    question: "以下代码的输出是什么？\nprint(len('Python'))",
    code: "print(len('Python'))",
    options: ["5", "6", "7", "错误"],
    correct: 1,
    points: 6,
    explanation: "'Python'有6个字符，所以len('Python')返回6。"
  },
  {
    id: 35,
    question: "Python中如何将字符串转换为列表？",
    options: ["str.toList()", "list(str)", "str.split()", "以上都可以"],
    correct: 3,
    points: 6,
    explanation: "可以使用list(str)将字符串转为字符列表，或使用split()分割字符串。"
  },
  {
    id: 36,
    question: "以下哪个操作会修改原列表？",
    options: ["list.sort()", "sorted(list)", "list + [1]", "list[:]"],
    correct: 0,
    points: 6,
    explanation: "list.sort()会就地排序，修改原列表；sorted()返回新列表。"
  },
  {
    id: 37,
    question: "Python中如何获取列表的最后一个元素？",
    options: ["list[end]", "list[-1]", "list[last]", "list.last()"],
    correct: 1,
    points: 6,
    explanation: "使用负索引list[-1]可以获取列表的最后一个元素。"
  },
  {
    id: 38,
    question: "以下代码的输出是什么？\nprint(bool(''))",
    code: "print(bool(''))",
    options: ["True", "False", "None", "错误"],
    correct: 1,
    points: 6,
    explanation: "空字符串在布尔上下文中被视为False。"
  },
  {
    id: 39,
    question: "Python中如何复制一个列表？",
    options: ["list.copy()", "list[:]", "list(original)", "以上都可以"],
    correct: 3,
    points: 6,
    explanation: "可以使用copy()方法、切片操作或list()构造函数来复制列表。"
  },
  {
    id: 40,
    question: "以下哪个方法用于移除列表中的重复元素？",
    options: ["list.unique()", "set(list)", "list.distinct()", "list.remove_duplicates()"],
    correct: 1,
    points: 6,
    explanation: "将列表转换为集合set(list)可以移除重复元素。"
  },
  // 控制流题 (41-60)
  {
    id: 41,
    question: "Python中if语句的正确语法是什么？",
    options: ["if condition:", "if (condition):", "if condition then:", "if condition {}"],
    correct: 0,
    points: 7,
    explanation: "Python中if语句的语法是'if condition:'，注意冒号。"
  },
  {
    id: 42,
    question: "以下代码的输出是什么？\nfor i in range(3):\n    print(i)",
    code: "for i in range(3):\n    print(i)",
    options: ["1 2 3", "0 1 2", "1 2", "0 1 2 3"],
    correct: 1,
    points: 7,
    explanation: "range(3)生成0, 1, 2，所以输出是0 1 2。"
  },
  {
    id: 43,
    question: "Python中break语句的作用是什么？",
    options: ["跳过当前迭代", "退出循环", "暂停程序", "重新开始循环"],
    correct: 1,
    points: 7,
    explanation: "break语句用于完全退出当前循环。"
  },
  {
    id: 44,
    question: "Python中continue语句的作用是什么？",
    options: ["退出循环", "跳过当前迭代", "重新开始循环", "暂停程序"],
    correct: 1,
    points: 7,
    explanation: "continue语句跳过当前迭代的剩余部分，继续下一次迭代。"
  },
  {
    id: 45,
    question: "以下哪个是Python中的条件表达式（三元运算符）？",
    options: ["condition ? true : false", "true if condition else false", "if condition true else false", "condition then true else false"],
    correct: 1,
    points: 7,
    explanation: "Python的条件表达式语法是'value_if_true if condition else value_if_false'。"
  },
  {
    id: 46,
    question: "Python中while循环的条件什么时候检查？",
    options: ["循环结束时", "循环开始前", "循环中间", "每次迭代前"],
    correct: 3,
    points: 7,
    explanation: "while循环在每次迭代前都会检查条件。"
  },
  {
    id: 47,
    question: "以下代码会输出什么？\nfor i in [1,2,3]:\n    if i == 2:\n        continue\n    print(i)",
    code: "for i in [1,2,3]:\n    if i == 2:\n        continue\n    print(i)",
    options: ["1 2 3", "1 3", "2", "1 2"],
    correct: 1,
    points: 7,
    explanation: "当i等于2时，continue跳过print语句，所以只输出1和3。"
  },
  {
    id: 48,
    question: "Python中elif关键字的作用是什么？",
    options: ["结束if语句", "else if的缩写", "定义函数", "循环控制"],
    correct: 1,
    points: 7,
    explanation: "elif是'else if'的缩写，用于多条件判断。"
  },
  {
    id: 49,
    question: "以下哪个循环结构在Python中不存在？",
    options: ["for", "while", "do-while", "foreach"],
    correct: 2,
    points: 7,
    explanation: "Python中没有do-while循环结构。"
  },
  {
    id: 50,
    question: "Python中如何遍历字典的键值对？",
    options: ["for k,v in dict:", "for k,v in dict.items():", "for k,v in dict.pairs():", "for k,v in dict.keyvalue():"],
    correct: 1,
    points: 7,
    explanation: "使用dict.items()方法可以遍历字典的键值对。"
  },
  // 函数题 (51-70)
  {
    id: 51,
    question: "Python中如何定义一个带默认参数的函数？",
    options: ["def func(a=1):", "def func(a:1):", "def func(a default 1):", "def func(a||1):"],
    correct: 0,
    points: 8,
    explanation: "使用'='为参数设置默认值，如def func(a=1):。"
  },
  {
    id: 52,
    question: "Python中*args的作用是什么？",
    options: ["接收关键字参数", "接收可变数量的位置参数", "解包列表", "定义全局变量"],
    correct: 1,
    points: 8,
    explanation: "*args用于接收可变数量的位置参数，以元组形式存储。"
  },
  {
    id: 53,
    question: "Python中**kwargs的作用是什么？",
    options: ["接收位置参数", "接收关键字参数", "解包字典", "定义类变量"],
    correct: 1,
    points: 8,
    explanation: "**kwargs用于接收可变数量的关键字参数，以字典形式存储。"
  },
  {
    id: 54,
    question: "Python中return语句的作用是什么？",
    options: ["结束函数并返回值", "打印结果", "定义变量", "导入模块"],
    correct: 0,
    points: 8,
    explanation: "return语句用于结束函数执行并返回值给调用者。"
  },
  {
    id: 55,
    question: "以下哪个是Python中的内置函数？",
    options: ["print()", "len()", "type()", "以上都是"],
    correct: 3,
    points: 8,
    explanation: "print()、len()、type()都是Python的内置函数。"
  },
  {
    id: 56,
    question: "Python中如何定义匿名函数？",
    options: ["lambda", "anonymous", "func", "def"],
    correct: 0,
    points: 8,
    explanation: "使用lambda关键字可以定义匿名函数。"
  },
  {
    id: 57,
    question: "以下代码的输出是什么？\ndef func(x=[]):\n    x.append(1)\n    return x\nprint(func())\nprint(func())",
    code: "def func(x=[]):\n    x.append(1)\n    return x\nprint(func())\nprint(func())",
    options: ["[1] [1]", "[1] [1,1]", "错误", "[1] []"],
    correct: 1,
    points: 8,
    explanation: "默认参数是可变对象时会保持状态，第二次调用时列表已包含第一次添加的元素。"
  },
  {
    id: 58,
    question: "Python中函数的作用域规则是什么？",
    options: ["LEGB", "BELG", "GLEB", "LEBG"],
    correct: 0,
    points: 8,
    explanation: "Python遵循LEGB规则：Local, Enclosing, Global, Built-in。"
  },
  {
    id: 59,
    question: "Python中global关键字的作用是什么？",
    options: ["定义全局变量", "访问全局变量", "修改全局变量", "删除全局变量"],
    correct: 2,
    points: 8,
    explanation: "global关键字用于在函数内部修改全局变量。"
  },
  {
    id: 60,
    question: "以下哪个是正确的lambda函数定义？",
    options: ["lambda x: x*2", "lambda(x): x*2", "lambda x => x*2", "lambda x { x*2 }"],
    correct: 0,
    points: 8,
    explanation: "lambda函数的语法是'lambda 参数: 表达式'。"
  },
  // 面向对象题 (61-80)
  {
    id: 61,
    question: "Python中如何创建类的实例？",
    options: ["new ClassName()", "ClassName.new()", "ClassName()", "create ClassName()"],
    correct: 2,
    points: 9,
    explanation: "直接调用类名ClassName()来创建实例。"
  },
  {
    id: 62,
    question: "Python中__init__方法的作用是什么？",
    options: ["初始化对象", "销毁对象", "复制对象", "比较对象"],
    correct: 0,
    points: 9,
    explanation: "__init__是构造方法，用于初始化新创建的对象。"
  },
  {
    id: 63,
    question: "Python中self参数代表什么？",
    options: ["类本身", "实例本身", "父类", "模块"],
    correct: 1,
    points: 9,
    explanation: "self参数代表类的实例本身。"
  },
  {
    id: 64,
    question: "Python中如何定义私有属性？",
    options: ["private attr", "_attr", "__attr", "attr_private"],
    correct: 2,
    points: 9,
    explanation: "使用双下划线前缀__attr定义私有属性。"
  },
  {
    id: 65,
    question: "Python中继承使用什么语法？",
    options: ["class Child extends Parent:", "class Child(Parent):", "class Child inherits Parent:", "class Child -> Parent:"],
    correct: 1,
    points: 9,
    explanation: "Python中使用class Child(Parent):语法实现继承。"
  },
  // 模块和包题 (66-80)
  {
    id: 66,
    question: "Python中如何导入整个模块？",
    options: ["import module", "from module import *", "include module", "using module"],
    correct: 0,
    points: 9,
    explanation: "使用import module语句导入整个模块。"
  },
  {
    id: 67,
    question: "Python中如何从模块导入特定函数？",
    options: ["import function from module", "from module import function", "include function from module", "using module.function"],
    correct: 1,
    points: 9,
    explanation: "使用from module import function语句导入特定函数。"
  },
  {
    id: 68,
    question: "Python中__name__变量的作用是什么？",
    options: ["存储模块名", "存储函数名", "存储变量名", "存储类名"],
    correct: 0,
    points: 9,
    explanation: "__name__变量存储当前模块的名称。"
  },
  {
    id: 69,
    question: "以下哪个是Python标准库模块？",
    options: ["os", "sys", "math", "以上都是"],
    correct: 3,
    points: 9,
    explanation: "os、sys、math都是Python的标准库模块。"
  },
  {
    id: 70,
    question: "Python中如何给导入的模块起别名？",
    options: ["import module as alias", "import module alias", "import module -> alias", "import module = alias"],
    correct: 0,
    points: 9,
    explanation: "使用import module as alias语法给模块起别名。"
  },
  // 异常处理题 (71-85)
  {
    id: 71,
    question: "Python中如何捕获异常？",
    options: ["try-catch", "try-except", "try-handle", "try-error"],
    correct: 1,
    points: 10,
    explanation: "Python使用try-except语句捕获异常。"
  },
  {
    id: 72,
    question: "Python中finally块的作用是什么？",
    options: ["处理异常", "无论是否有异常都执行", "只在有异常时执行", "只在无异常时执行"],
    correct: 1,
    points: 10,
    explanation: "finally块中的代码无论是否发生异常都会执行。"
  },
  {
    id: 73,
    question: "Python中如何主动抛出异常？",
    options: ["throw", "raise", "error", "exception"],
    correct: 1,
    points: 10,
    explanation: "使用raise语句主动抛出异常。"
  },
  {
    id: 74,
    question: "以下哪个是Python内置异常？",
    options: ["ValueError", "TypeError", "IndexError", "以上都是"],
    correct: 3,
    points: 10,
    explanation: "ValueError、TypeError、IndexError都是Python的内置异常。"
  },
  {
    id: 75,
    question: "Python中else子句在try-except中的作用是什么？",
    options: ["处理异常", "没有异常时执行", "总是执行", "有异常时执行"],
    correct: 1,
    points: 10,
    explanation: "else子句在没有异常发生时执行。"
  },
  // 文件操作题 (76-90)
  {
    id: 76,
    question: "Python中如何打开文件？",
    options: ["open()", "file()", "read()", "load()"],
    correct: 0,
    points: 10,
    explanation: "使用open()函数打开文件。"
  },
  {
    id: 77,
    question: "Python中文件的默认打开模式是什么？",
    options: ["写模式", "读模式", "追加模式", "二进制模式"],
    correct: 1,
    points: 10,
    explanation: "文件的默认打开模式是读模式('r')。"
  },
  {
    id: 78,
    question: "Python中with语句的作用是什么？",
    options: ["循环控制", "异常处理", "上下文管理", "函数定义"],
    correct: 2,
    points: 10,
    explanation: "with语句用于上下文管理，确保资源正确释放。"
  },
  {
    id: 79,
    question: "以下哪个方法用于读取文件的所有内容？",
    options: ["read()", "readline()", "readlines()", "readall()"],
    correct: 0,
    points: 10,
    explanation: "read()方法读取文件的所有内容。"
  },
  {
    id: 80,
    question: "Python中如何以追加模式打开文件？",
    options: ["open('file', 'a')", "open('file', 'append')", "open('file', 'add')", "open('file', 'w+')"],
    correct: 0,
    points: 10,
    explanation: "使用'a'模式以追加方式打开文件。"
  },
  // 高级特性题 (81-100)
  {
    id: 81,
    question: "Python中列表推导式的语法是什么？",
    options: ["[x for x in list]", "[x in list for x]", "[for x in list: x]", "[x | x in list]"],
    correct: 0,
    points: 12,
    explanation: "列表推导式的语法是[expression for item in iterable]。"
  },
  {
    id: 82,
    question: "Python中生成器的特点是什么？",
    options: ["一次性生成所有值", "惰性求值", "占用大量内存", "不能迭代"],
    correct: 1,
    points: 12,
    explanation: "生成器采用惰性求值，按需生成值，节省内存。"
  },
  {
    id: 83,
    question: "Python中yield关键字的作用是什么？",
    options: ["返回值", "创建生成器", "暂停函数", "以上都是"],
    correct: 3,
    points: 12,
    explanation: "yield用于创建生成器，暂停函数执行并返回值。"
  },
  {
    id: 84,
    question: "Python中装饰器的语法是什么？",
    options: ["@decorator", "#decorator", "&decorator", "%decorator"],
    correct: 0,
    points: 12,
    explanation: "使用@decorator语法应用装饰器。"
  },
  {
    id: 85,
    question: "Python中map()函数的作用是什么？",
    options: ["创建字典", "映射函数到序列", "过滤序列", "排序序列"],
    correct: 1,
    points: 12,
    explanation: "map()函数将函数应用到序列的每个元素。"
  },
  {
    id: 86,
    question: "Python中filter()函数的作用是什么？",
    options: ["映射函数", "过滤序列", "排序序列", "合并序列"],
    correct: 1,
    points: 12,
    explanation: "filter()函数根据条件过滤序列中的元素。"
  },
  {
    id: 87,
    question: "Python中zip()函数的作用是什么？",
    options: ["压缩文件", "合并序列", "分割序列", "排序序列"],
    correct: 1,
    points: 12,
    explanation: "zip()函数将多个序列合并成元组序列。"
  },
  {
    id: 88,
    question: "Python中enumerate()函数的作用是什么？",
    options: ["计数元素", "添加索引", "排序元素", "过滤元素"],
    correct: 1,
    points: 12,
    explanation: "enumerate()函数为序列添加索引，返回(索引, 值)对。"
  },
  {
    id: 89,
    question: "Python中*运算符用于解包时的作用是什么？",
    options: ["乘法运算", "解包序列", "重复序列", "连接序列"],
    correct: 1,
    points: 12,
    explanation: "*运算符可以解包序列，将序列元素作为单独参数传递。"
  },
  {
    id: 90,
    question: "Python中**运算符用于解包时的作用是什么？",
    options: ["幂运算", "解包字典", "重复字典", "合并字典"],
    correct: 1,
    points: 12,
    explanation: "**运算符可以解包字典，将键值对作为关键字参数传递。"
  },
  {
    id: 91,
    question: "Python中is和==的区别是什么？",
    options: ["没有区别", "is比较身份，==比较值", "is比较值，==比较身份", "is更快"],
    correct: 1,
    points: 12,
    explanation: "is比较对象身份(内存地址)，==比较对象的值。"
  },
  {
    id: 92,
    question: "Python中None的类型是什么？",
    options: ["NoneType", "null", "void", "empty"],
    correct: 0,
    points: 12,
    explanation: "None的类型是NoneType。"
  },
  {
    id: 93,
    question: "Python中如何检查对象的类型？",
    options: ["typeof()", "type()", "instanceof()", "class()"],
    correct: 1,
    points: 12,
    explanation: "使用type()函数检查对象的类型。"
  },
  {
    id: 94,
    question: "Python中如何检查对象是否是某个类的实例？",
    options: ["isinstance()", "istype()", "isclass()", "isinstance()"],
    correct: 0,
    points: 12,
    explanation: "使用isinstance()函数检查对象是否是某个类的实例。"
  },
  {
    id: 95,
    question: "Python中如何获取对象的所有属性？",
    options: ["attrs()", "dir()", "properties()", "getattrs()"],
    correct: 1,
    points: 12,
    explanation: "使用dir()函数获取对象的所有属性和方法。"
  },
  {
    id: 96,
    question: "Python中如何动态获取对象的属性值？",
    options: ["getattr()", "getprop()", "getvalue()", "getfield()"],
    correct: 0,
    points: 12,
    explanation: "使用getattr()函数动态获取对象的属性值。"
  },
  {
    id: 97,
    question: "Python中如何动态设置对象的属性值？",
    options: ["setattr()", "setprop()", "setvalue()", "setfield()"],
    correct: 0,
    points: 12,
    explanation: "使用setattr()函数动态设置对象的属性值。"
  },
  {
    id: 98,
    question: "Python中如何检查对象是否有某个属性？",
    options: ["hasattr()", "hasprop()", "hasfield()", "hasvalue()"],
    correct: 0,
    points: 12,
    explanation: "使用hasattr()函数检查对象是否有某个属性。"
  },
  {
    id: 99,
    question: "Python中如何删除对象的属性？",
    options: ["delattr()", "removeattr()", "deleteattr()", "dropattr()"],
    correct: 0,
    points: 12,
    explanation: "使用delattr()函数删除对象的属性。"
  },
  {
    id: 100,
    question: "Python中如何获取函数的文档字符串？",
    options: ["func.__doc__", "func.doc", "func.help", "func.description"],
    correct: 0,
    points: 15,
    explanation: "使用func.__doc__属性获取函数的文档字符串。"
  }
])

// 计算属性
const currentTopic = computed(() => knowledgeTopics.value[currentStep.value])
const overallProgress = computed(() => {
  return Math.round((completedTopics.value / knowledgeTopics.value.length) * 100)
})

// 练习模式计算属性
const currentQuestion = computed(() => pythonQuestions.value[currentQuestionIndex.value])
const totalQuestions = computed(() => pythonQuestions.value.length)
const completedQuestions = computed(() => answeredQuestions.value.size)
const quizProgress = computed(() => Math.round((completedQuestions.value / totalQuestions.value) * 100))
const accuracyRate = computed(() => {
  if (completedQuestions.value === 0) return 0
  return Math.round((correctAnswers.value / completedQuestions.value) * 100)
})
const averageScore = computed(() => {
  if (completedQuestions.value === 0) return 0
  return Math.round(totalScore.value / completedQuestions.value)
})

// 方法
const setLearningMode = (mode) => {
  learningMode.value = mode
  if (mode === 'quiz') {
    // 重置练习状态
    currentQuestionIndex.value = 0
    selectedAnswer.value = null
    questionAnswered.value = false
    isCurrentAnswerCorrect.value = false
  }
}

// 练习模式方法
const handleAnswerSelect = () => {
  // 选择答案时的处理
}

const submitAnswer = () => {
  if (selectedAnswer.value !== null && !questionAnswered.value) {
    questionAnswered.value = true
    isCurrentAnswerCorrect.value = selectedAnswer.value === currentQuestion.value.correct

    // 记录答题
    answeredQuestions.value.add(currentQuestionIndex.value)

    if (isCurrentAnswerCorrect.value) {
      correctAnswers.value++
      totalScore.value += currentQuestion.value.points

      // 显示积分获得动画
      showScoreAnimation(currentQuestion.value.points)
    } else {
      wrongAnswers.value++
    }

    // 更新进度
    updateProgress()
  }
}

const showScoreAnimation = (points) => {
  // 可以添加积分获得的动画效果
  console.log(`获得 ${points} 积分！`)
}

const nextQuestion = () => {
  if (currentQuestionIndex.value < pythonQuestions.value.length - 1) {
    currentQuestionIndex.value++
    selectedAnswer.value = null
    questionAnswered.value = false
    isCurrentAnswerCorrect.value = false
  }
}

const previousQuestion = () => {
  if (currentQuestionIndex.value > 0) {
    currentQuestionIndex.value--
    selectedAnswer.value = null
    questionAnswered.value = false
    isCurrentAnswerCorrect.value = false
  }
}

const updateProgress = () => {
  const progressValue = Math.max(progress.value, quizProgress.value)
  emit('progress-update', 'python-basics', progressValue)
}

// 教程模式方法
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
watch([progress, quizProgress], () => {
  updateProgress()
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.python-basics-container {
  padding: 2rem;
  background: $secondary-color;
  min-height: 100vh;

  // 全局选中样式
  ::selection {
    background: $accent-color;
    color: #ffffff;
  }

  ::-moz-selection {
    background: $accent-color;
    color: #ffffff;
  }
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

.mode-selector {
  margin-bottom: 2rem;

  .mode-card {
    background: $primary-color;
    border: 1px solid $border-color;

    :deep(.el-card__body) {
      background: $primary-color;
    }

    .mode-buttons {
      display: flex;
      justify-content: center;
      gap: 2rem;

      .el-button {
        padding: 1rem 2rem;
        font-size: 1.1rem;

        .el-icon {
          margin-right: 0.5rem;
        }
      }
    }
  }
}

.score-display {
  margin-bottom: 2rem;

  .score-card {
    background: $primary-color;
    border: 1px solid $border-color;

    :deep(.el-card__body) {
      background: $primary-color;
    }

    .score-content {
      display: flex;
      justify-content: space-around;
      align-items: center;

      .score-item {
        display: flex;
        align-items: center;
        gap: 0.5rem;

        .el-icon {
          font-size: 1.5rem;
          color: $accent-color;
        }

        .score-label {
          color: $text-secondary-color;
          font-size: 0.9rem;
        }

        .score-value {
          color: $text-color;
          font-size: 1.2rem;
          font-weight: bold;
        }
      }
    }
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

            // 代码块内的选中样式
            ::selection {
              background: $accent-color;
              color: #ffffff;
            }

            ::-moz-selection {
              background: $accent-color;
              color: #ffffff;
            }

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

.quiz-content {
  .quiz-card {
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

    .quiz-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: $text-color;
      font-weight: 600;

      .quiz-progress {
        min-width: 200px;
      }
    }

    .quiz-question {
      .question-content {
        margin-bottom: 2rem;

        h3 {
          color: $text-color;
          margin-bottom: 1rem;
          font-size: 1.3rem;
          line-height: 1.6;
        }

        .question-code {
          background: $secondary-color;
          border: 1px solid $border-color;
          border-radius: 4px;
          padding: 1rem;
          margin-top: 1rem;

          pre {
            margin: 0;
            color: $text-color;
            font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
            font-size: 14px;
            line-height: 1.5;
          }
        }
      }

      .question-options {
        margin-bottom: 2rem;

        .option-item {
          display: block;
          margin-bottom: 1rem;
          padding: 1rem;
          border-radius: 8px;
          background: $secondary-color;
          border: 1px solid $border-color;
          transition: all 0.3s ease;

          &:hover {
            background: rgba(176, 179, 184, 0.1);
          }

          :deep(.el-radio__label) {
            color: $text-color;
            font-size: 1rem;
            line-height: 1.5;
          }

          :deep(.el-radio__input.is-checked .el-radio__inner) {
            background-color: $accent-color;
            border-color: $accent-color;
          }
        }
      }

      .answer-feedback {
        margin-bottom: 2rem;

        .score-gained {
          margin-top: 1rem;
          text-align: center;

          .el-tag {
            font-size: 1.1rem;
            padding: 0.5rem 1rem;
          }
        }
      }

      .quiz-navigation {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-top: 1rem;
        border-top: 1px solid $border-color;
      }
    }
  }

  .quiz-stats {
    margin-top: 2rem;

    .stats-card {
      background: $primary-color;
      border: 1px solid $border-color;

      :deep(.el-card__header) {
        background: $secondary-color;
        border-bottom: 1px solid $border-color;
        color: $text-color;
        font-weight: 600;
      }

      :deep(.el-card__body) {
        background: $primary-color;
      }

      .stats-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
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
            margin-bottom: 0.5rem;
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

  .mode-buttons {
    flex-direction: column !important;
    gap: 1rem !important;
  }

  .score-content {
    flex-direction: column !important;
    gap: 1rem !important;
  }

  .quiz-header {
    flex-direction: column !important;
    gap: 1rem !important;

    .quiz-progress {
      min-width: auto !important;
      width: 100%;
    }
  }

  .quiz-navigation {
    flex-direction: column !important;
    gap: 1rem !important;
  }
}
</style>
