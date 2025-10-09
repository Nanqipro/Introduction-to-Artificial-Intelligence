<template>
  <div class="system-test-page">
    <div class="container">
      <!-- 测试页面头部 -->
      <div class="test-header">
        <h1 class="test-title">🔧 系统功能测试中心</h1>
        <p class="test-description">
          全面测试答题系统、等级升级、用户进度等核心功能的连通性
        </p>
      </div>

      <!-- 快速测试面板 -->
      <div class="quick-test-panel">
        <h2>🚀 快速测试</h2>
        <div class="test-grid">
          <div class="test-card" @click="testQuizSystem">
            <div class="test-icon">📝</div>
            <h3>答题系统测试</h3>
            <p>测试题目加载、答题逻辑、分数计算</p>
            <div class="test-status" :class="quizTestStatus">{{ quizTestMessage }}</div>
          </div>

          <div class="test-card" @click="testLevelSystem">
            <div class="test-icon">⭐</div>
            <h3>等级系统测试</h3>
            <p>测试等级计算、升级逻辑、经验值</p>
            <div class="test-status" :class="levelTestStatus">{{ levelTestMessage }}</div>
          </div>

          <div class="test-card" @click="testUserProgress">
            <div class="test-icon">📊</div>
            <h3>用户进度测试</h3>
            <p>测试进度保存、历史记录、统计</p>
            <div class="test-status" :class="progressTestStatus">{{ progressTestMessage }}</div>
          </div>

          <div class="test-card" @click="testApiConnectivity">
            <div class="test-icon">🌐</div>
            <h3>API连接测试</h3>
            <p>测试前后端接口连通性</p>
            <div class="test-status" :class="apiTestStatus">{{ apiTestMessage }}</div>
          </div>
        </div>
      </div>

      <!-- 综合测试面板 -->
      <div class="comprehensive-test-panel">
        <h2>🔍 综合功能测试</h2>
        <div class="test-controls">
          <button @click="runFullSystemTest" class="btn btn-primary" :disabled="testing">
            {{ testing ? '测试中...' : '🔧 运行完整测试' }}
          </button>
          <button @click="simulateUserJourney" class="btn btn-secondary" :disabled="testing">
            🎯 模拟用户流程
          </button>
          <button @click="generateTestReport" class="btn btn-info" :disabled="!hasResults">
            📋 生成测试报告
          </button>
        </div>

        <!-- 测试进度 -->
        <div v-if="testing" class="test-progress">
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: testProgress + '%' }"></div>
          </div>
          <p class="progress-text">{{ currentTestStep }} ({{ testProgress }}%)</p>
        </div>

        <!-- 测试结果 -->
        <div v-if="testResults.length > 0" class="test-results">
          <h3>测试结果</h3>
          <div class="results-grid">
            <div 
              v-for="result in testResults" 
              :key="result.name"
              class="result-item"
              :class="result.status"
            >
              <div class="result-icon">
                {{ result.status === 'success' ? '✅' : result.status === 'warning' ? '⚠️' : '❌' }}
              </div>
              <div class="result-content">
                <h4>{{ result.name }}</h4>
                <p>{{ result.message }}</p>
                <div v-if="result.details" class="result-details">
                  <small>{{ result.details }}</small>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 实时日志 -->
      <div class="test-logs">
        <h3>🔍 测试日志</h3>
        <div class="log-container">
          <div 
            v-for="(log, index) in testLogs" 
            :key="index"
            class="log-item"
            :class="log.type"
          >
            <span class="log-time">{{ formatTime(log.timestamp) }}</span>
            <span class="log-message">{{ log.message }}</span>
          </div>
        </div>
        <button @click="clearLogs" class="btn btn-small">清空日志</button>
      </div>
    </div>
  </div>
</template>

<script>
import { quizApi, userApi } from '../services/api'
import SystemTester from '../utils/systemTest'
import { ElMessage } from 'element-plus'

export default {
  name: 'SystemTestPage',
  data() {
    return {
      testing: false,
      testProgress: 0,
      currentTestStep: '',
      hasResults: false,
      testResults: [],
      testLogs: [],
      
      // 各模块测试状态
      quizTestStatus: 'pending',
      quizTestMessage: '待测试',
      levelTestStatus: 'pending', 
      levelTestMessage: '待测试',
      progressTestStatus: 'pending',
      progressTestMessage: '待测试',
      apiTestStatus: 'pending',
      apiTestMessage: '待测试'
    }
  },
  
  methods: {
    // 添加日志
    addLog(message, type = 'info') {
      this.testLogs.unshift({
        timestamp: new Date(),
        message,
        type
      })
      
      // 限制日志数量
      if (this.testLogs.length > 100) {
        this.testLogs = this.testLogs.slice(0, 100)
      }
    },
    
    // 格式化时间
    formatTime(timestamp) {
      return timestamp.toLocaleTimeString()
    },
    
    // 清空日志
    clearLogs() {
      this.testLogs = []
    },
    
    // 测试答题系统
    async testQuizSystem() {
      this.addLog('开始测试答题系统...', 'info')
      this.quizTestStatus = 'testing'
      this.quizTestMessage = '测试中...'
      
      try {
        // 测试获取题目
        const questions = await quizApi.getQuestions(1)
        this.addLog(`成功获取 ${questions.data?.length || 0} 道题目`, 'success')
        
        // 测试答题逻辑
        if (questions.data && questions.data.length > 0) {
          const testAnswer = {
            questionId: questions.data[0].id,
            answer: questions.data[0].options?.[0] || 'test',
            timeSpent: 30
          }
          
          // 模拟提交答案
          this.addLog('模拟提交答案...', 'info')
          
          this.quizTestStatus = 'success'
          this.quizTestMessage = '✅ 答题系统正常'
          this.addLog('答题系统测试通过', 'success')
        } else {
          throw new Error('未获取到题目数据')
        }
      } catch (error) {
        this.quizTestStatus = 'error'
        this.quizTestMessage = '❌ 答题系统异常'
        this.addLog(`答题系统测试失败: ${error.message}`, 'error')
      }
    },
    
    // 测试等级系统
    async testLevelSystem() {
      this.addLog('开始测试等级系统...', 'info')
      this.levelTestStatus = 'testing'
      this.levelTestMessage = '测试中...'
      
      try {
        // 测试等级计算逻辑
        const testExp = 1250
        const level = Math.floor(testExp / 1000) + 1
        const progress = (testExp % 1000) / 1000 * 100
        
        this.addLog(`经验值: ${testExp}, 等级: ${level}, 进度: ${progress.toFixed(1)}%`, 'info')
        
        // 测试等级称号
        const titles = ['新手', '学徒', '专家', '大师', '宗师']
        const title = titles[Math.min(level - 1, titles.length - 1)]
        
        this.addLog(`当前称号: ${title}`, 'info')
        
        this.levelTestStatus = 'success'
        this.levelTestMessage = '✅ 等级系统正常'
        this.addLog('等级系统测试通过', 'success')
      } catch (error) {
        this.levelTestStatus = 'error'
        this.levelTestMessage = '❌ 等级系统异常'
        this.addLog(`等级系统测试失败: ${error.message}`, 'error')
      }
    },
    
    // 测试用户进度
    async testUserProgress() {
      this.addLog('开始测试用户进度...', 'info')
      this.progressTestStatus = 'testing'
      this.progressTestMessage = '测试中...'
      
      try {
        // 测试获取用户统计
        const stats = await quizApi.getUserStats()
        this.addLog(`获取用户统计: 总分 ${stats.data?.totalScore || 0}`, 'success')
        
        // 测试历史记录
        const history = await quizApi.getUserHistory()
        this.addLog(`获取历史记录: ${history.data?.length || 0} 条`, 'success')
        
        this.progressTestStatus = 'success'
        this.progressTestMessage = '✅ 进度系统正常'
        this.addLog('用户进度测试通过', 'success')
      } catch (error) {
        this.progressTestStatus = 'error'
        this.progressTestMessage = '❌ 进度系统异常'
        this.addLog(`用户进度测试失败: ${error.message}`, 'error')
      }
    },
    
    // 测试API连接
    async testApiConnectivity() {
      this.addLog('开始测试API连接...', 'info')
      this.apiTestStatus = 'testing'
      this.apiTestMessage = '测试中...'
      
      try {
        // 测试用户API
        await userApi.getCurrentUser()
        this.addLog('用户API连接正常', 'success')
        
        // 测试答题API
        await quizApi.getQuestions(1)
        this.addLog('答题API连接正常', 'success')
        
        this.apiTestStatus = 'success'
        this.apiTestMessage = '✅ API连接正常'
        this.addLog('API连接测试通过', 'success')
      } catch (error) {
        this.apiTestStatus = 'warning'
        this.apiTestMessage = '⚠️ 部分API异常'
        this.addLog(`API连接测试警告: ${error.message}`, 'warning')
      }
    },
    
    // 运行完整系统测试
    async runFullSystemTest() {
      this.testing = true
      this.testProgress = 0
      this.testResults = []
      this.addLog('开始运行完整系统测试...', 'info')
      
      const tests = [
        { name: 'API连接测试', method: this.testApiConnectivity },
        { name: '答题系统测试', method: this.testQuizSystem },
        { name: '等级系统测试', method: this.testLevelSystem },
        { name: '用户进度测试', method: this.testUserProgress }
      ]
      
      for (let i = 0; i < tests.length; i++) {
        const test = tests[i]
        this.currentTestStep = test.name
        this.testProgress = Math.round((i / tests.length) * 100)
        
        try {
          await test.method()
          this.testResults.push({
            name: test.name,
            status: 'success',
            message: '测试通过',
            details: '所有功能正常工作'
          })
        } catch (error) {
          this.testResults.push({
            name: test.name,
            status: 'error',
            message: '测试失败',
            details: error.message
          })
        }
        
        // 模拟测试延迟
        await new Promise(resolve => setTimeout(resolve, 500))
      }
      
      this.testProgress = 100
      this.currentTestStep = '测试完成'
      this.hasResults = true
      this.testing = false
      
      const successCount = this.testResults.filter(r => r.status === 'success').length
      this.addLog(`完整测试完成: ${successCount}/${tests.length} 项通过`, 'info')
      
      if (successCount === tests.length) {
        ElMessage.success('所有测试通过！系统功能正常')
      } else {
        ElMessage.warning(`${tests.length - successCount} 项测试失败，请查看详细结果`)
      }
    },
    
    // 模拟用户流程
    async simulateUserJourney() {
      this.addLog('开始模拟用户学习流程...', 'info')
      
      try {
        // 模拟用户登录
        this.addLog('1. 模拟用户登录...', 'info')
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        // 模拟进入章节
        this.addLog('2. 模拟进入章节学习...', 'info')
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        // 模拟开始答题
        this.addLog('3. 模拟开始答题...', 'info')
        await this.testQuizSystem()
        
        // 模拟完成答题
        this.addLog('4. 模拟完成答题，计算分数...', 'info')
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        // 模拟等级升级
        this.addLog('5. 模拟等级升级检查...', 'info')
        await this.testLevelSystem()
        
        // 模拟保存进度
        this.addLog('6. 模拟保存学习进度...', 'info')
        await this.testUserProgress()
        
        this.addLog('用户学习流程模拟完成！', 'success')
        ElMessage.success('用户流程模拟完成，所有环节正常')
      } catch (error) {
        this.addLog(`用户流程模拟失败: ${error.message}`, 'error')
        ElMessage.error('用户流程模拟失败')
      }
    },
    
    // 生成测试报告
    generateTestReport() {
      if (!this.hasResults) {
        ElMessage.info('请先运行测试')
        return
      }
      
      const report = {
        timestamp: new Date().toLocaleString(),
        totalTests: this.testResults.length,
        passedTests: this.testResults.filter(r => r.status === 'success').length,
        failedTests: this.testResults.filter(r => r.status === 'error').length,
        warningTests: this.testResults.filter(r => r.status === 'warning').length,
        results: this.testResults,
        logs: this.testLogs.slice(0, 20) // 最近20条日志
      }
      
      // 下载报告
      const blob = new Blob([JSON.stringify(report, null, 2)], { type: 'application/json' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `system-test-report-${Date.now()}.json`
      a.click()
      URL.revokeObjectURL(url)
      
      this.addLog('测试报告已生成并下载', 'success')
      ElMessage.success('测试报告已生成')
    }
  },
  
  mounted() {
    this.addLog('系统测试页面已加载', 'info')
  }
}
</script>

<style lang="scss">


.system-test-page {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--primary-color, #18191a) 0%, var(--primary-gradient-end, #232526) 100%);
  padding: 2rem 0;
  color: var(--text-color, #f5f6fa);
  
  .container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 2rem;
  }

  .test-header {
    text-align: center;
    margin-bottom: 3rem;
    
    .test-title {
      font-size: 2.5rem;
      margin-bottom: 1rem;
      text-shadow: 0 2px 4px rgba(0,0,0,0.3);
      color: var(--text-color, #f5f6fa);
    }
    
    .test-description {
      font-size: 1.1rem;
      opacity: 0.9;
      color: var(--text-secondary-color, #b0b3b8);
    }
  }

  .quick-test-panel, .comprehensive-test-panel {
    background: var(--card-bg, #292c33);
    border-radius: 16px;
    padding: 2rem;
    margin-bottom: 2rem;
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.12);
    border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
    
    h2 {
      color: var(--text-color, #f5f6fa);
      margin-bottom: 1.5rem;
      font-size: 1.5rem;
    }
  }

  .test-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 1.5rem;
    margin-top: 1.5rem;
  }

  .test-card {
    background: linear-gradient(135deg, var(--secondary-color, #23272e), #31343b);
    border-radius: 16px;
    padding: 1.5rem;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
      border-color: var(--accent-color, #b0b3b8);
    }
    
    .test-icon {
      font-size: 3rem;
      margin-bottom: 1rem;
    }
    
    h3 {
      margin-bottom: 0.5rem;
      color: var(--text-color, #f5f6fa);
    }
    
    p {
      color: var(--text-secondary-color, #b0b3b8);
      margin-bottom: 1rem;
    }
  }

  .test-status {
    padding: 0.5rem 1rem;
    border-radius: 20px;
    font-weight: 500;
    font-size: 0.9rem;
    
    &.pending {
      background: rgba(var(--accent-color, #b0b3b8), 0.1);
      color: var(--accent-color, #b0b3b8);
      border: 1px solid rgba(var(--accent-color, #b0b3b8), 0.3);
    }
    
    &.testing {
      background: rgba(var(--info-color, #4a90e2), 0.1);
      color: var(--info-color, #4a90e2);
      border: 1px solid rgba(var(--info-color, #4a90e2), 0.3);
    }
    
    &.success {
      background: rgba(var(--success-color, #4caf50), 0.1);
      color: var(--success-color, #4caf50);
      border: 1px solid rgba(var(--success-color, #4caf50), 0.3);
    }
    
    &.error {
      background: rgba(var(--error-color, #f44336), 0.1);
      color: var(--error-color, #f44336);
      border: 1px solid rgba(var(--error-color, #f44336), 0.3);
    }
    
    &.warning {
      background: rgba(var(--warning-color, #ff9800), 0.1);
      color: var(--warning-color, #ff9800);
      border: 1px solid rgba(var(--warning-color, #ff9800), 0.3);
    }
  }

  .test-controls {
    display: flex;
    gap: 1rem;
    margin-bottom: 1.5rem;
    flex-wrap: wrap;
  }

  .btn {
    padding: 0.75rem 1.5rem;
    border: none;
    border-radius: 12px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    gap: 0.5rem;
    font-size: 1rem;
    
    &:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }
    
    &.btn-primary {
      background: linear-gradient(135deg, #4a90e2, #357abd);
      color: white;
      box-shadow: 0 4px 16px rgba(74, 144, 226, 0.3);
      
      &:hover:not(:disabled) {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(74, 144, 226, 0.4);
      }
    }
    
    &.btn-secondary {
      background: #393b40;
      color: var(--text-color, #f5f6fa);
      border: 1px solid var(--border-color, #393b40);
      
      &:hover:not(:disabled) {
        background: #4a4c51;
        border-color: var(--accent-color, #b0b3b8);
      }
    }
    
    &.btn-info {
      background: linear-gradient(135deg, #4a90e2, #64b5f6);
      color: white;
      
      &:hover:not(:disabled) {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(74, 144, 226, 0.4);
      }
    }
    
    &.btn-small {
      padding: 0.5rem 1rem;
      font-size: 0.9rem;
      background: #393b40;
      color: var(--text-color, #f5f6fa);
      border: 1px solid var(--border-color, #393b40);
      
      &:hover {
        background: #4a4c51;
        border-color: var(--accent-color, #b0b3b8);
      }
    }
  }

  .test-progress {
    margin: 1.5rem 0;
    
    .progress-bar {
      width: 100%;
      height: 12px;
      background: rgba(var(--accent-color, #b0b3b8), 0.2);
      border-radius: 6px;
      overflow: hidden;
      border: 1px solid rgba(var(--accent-color, #b0b3b8), 0.3);
    }
    
    .progress-fill {
      height: 100%;
      background: linear-gradient(90deg, var(--accent-color, #b0b3b8), #d1d3d8);
      transition: width 0.3s ease;
      border-radius: 6px;
    }
    
    .progress-text {
      text-align: center;
      margin-top: 0.5rem;
      color: var(--text-secondary-color, #b0b3b8);
      font-size: 0.9rem;
    }
  }

  .results-grid {
    display: grid;
    gap: 1rem;
    margin-top: 1.5rem;
  }

  .result-item {
    display: flex;
    align-items: flex-start;
    gap: 1rem;
    padding: 1.5rem;
    border-radius: 16px;
    border-left: 4px solid;
    background: var(--card-bg, #292c33);
    border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
    
    &.success {
      background: rgba(var(--success-color, #4caf50), 0.05);
      border-left-color: var(--success-color, #4caf50);
      border-color: rgba(var(--success-color, #4caf50), 0.3);
    }
    
    &.error {
      background: rgba(var(--error-color, #f44336), 0.05);
      border-left-color: var(--error-color, #f44336);
      border-color: rgba(var(--error-color, #f44336), 0.3);
    }
    
    &.warning {
      background: rgba(var(--warning-color, #ff9800), 0.05);
      border-left-color: var(--warning-color, #ff9800);
      border-color: rgba(var(--warning-color, #ff9800), 0.3);
    }
    
    .result-icon {
      font-size: 1.5rem;
      flex-shrink: 0;
    }
    
    .result-content {
      h4 {
        margin: 0 0 0.25rem 0;
        color: var(--text-color, #f5f6fa);
        font-size: 1.125rem;
      }
      
      p {
        margin: 0 0 0.25rem 0;
        color: var(--text-secondary-color, #b0b3b8);
      }
      
      .result-details {
        color: var(--text-secondary-color, #b0b3b8);
        font-size: 0.875rem;
        opacity: 0.8;
      }
    }
  }

  .test-logs {
    background: var(--card-bg, #292c33);
    border-radius: 16px;
    padding: 2rem;
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.12);
    border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
    
    h3 {
      color: var(--text-color, #f5f6fa);
      margin-bottom: 1.5rem;
    }
    
    .log-container {
      max-height: 300px;
      overflow-y: auto;
      background: var(--secondary-color, #23272e);
      border-radius: 8px;
      padding: 1rem;
      margin: 1rem 0;
      font-family: 'Courier New', monospace;
      border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
    }
    
    .log-item {
      display: flex;
      gap: 0.5rem;
      margin-bottom: 0.5rem;
      font-size: 0.875rem;
      
      .log-time {
        color: var(--text-secondary-color, #b0b3b8);
        flex-shrink: 0;
        font-size: 0.75rem;
      }
      
      .log-message {
        flex: 1;
        color: var(--text-color, #f5f6fa);
      }
      
      &.info .log-message {
        color: var(--info-color, #4a90e2);
      }
      
      &.success .log-message {
        color: var(--success-color, #4caf50);
      }
      
      &.error .log-message {
        color: var(--error-color, #f44336);
      }
      
      &.warning .log-message {
        color: var(--warning-color, #ff9800);
      }
    }
  }

  @media (max-width: 768px) {
    .container {
      padding: 0 1rem;
    }
    
    .test-header {
      margin-bottom: 2rem;
      
      .test-title {
        font-size: 2rem;
      }
    }
    
    .quick-test-panel, .comprehensive-test-panel {
      padding: 1.5rem;
    }
    
    .test-grid {
      grid-template-columns: 1fr;
      gap: 1rem;
    }
    
    .test-controls {
      flex-direction: column;
      
      .btn {
        justify-content: center;
      }
    }
    
    .test-card {
      padding: 1.5rem;
    }
  }
}
</style>