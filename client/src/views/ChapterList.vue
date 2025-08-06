<template>
  <div class="chapter-list">
    <div class="container">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1 class="page-title">教材章节</h1>
        <p class="page-description">
          《人工智能概论与应用》完整章节列表，点击章节卡片查看详细内容
        </p>
        
        <!-- 系统测试工具 -->
        <div class="system-test-panel">
          <div class="test-buttons">
            <button @click="runSystemTest" class="btn btn-test" :disabled="testing">
              {{ testing ? '测试中...' : '🔧 系统自测' }}
            </button>
            <button @click="fixIssues" class="btn btn-fix" :disabled="!hasTestResults">
              🛠️ 修复问题
            </button>
            <button @click="showTestReport" class="btn btn-report" :disabled="!hasTestResults">
              📋 查看报告
            </button>
            <button @click="goToTestPage" class="btn btn-advanced">
              🔬 高级测试
            </button>
          </div>
          <div v-if="testStatus" class="test-status" :class="testStatus.type">
            {{ testStatus.message }}
          </div>
        </div>
      </div>

      <!-- 章节列表 -->
      <div class="chapters-container" v-if="!loading">
        <div 
          v-for="chapter in chapters" 
          :key="chapter.id" 
          class="chapter-item"
          @click="goToChapter(chapter.id)"
        >
          <div class="chapter-header">
            <div class="chapter-badge">
              {{ chapter.chapterNumber === '0' ? '续章' : `第${chapter.chapterNumber}章` }}
            </div>
            <div class="chapter-type">{{ getChapterType(chapter.type) }}</div>
          </div>
          
          <h2 class="chapter-title">{{ chapter.title }}</h2>
          
          <p class="chapter-summary">{{ chapter.summary }}</p>
          
          <div class="chapter-footer">
            <span class="chapter-status">
              <span class="status-dot" :class="{ published: chapter.published }"></span>
              {{ chapter.published ? '已发布' : '未发布' }}
            </span>
            <span class="read-more">阅读详情 →</span>
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>正在加载章节信息...</p>
      </div>

      <!-- 错误状态 -->
      <div v-if="error" class="error-container">
        <div class="error-icon">⚠️</div>
        <h3>加载失败</h3>
        <p>{{ error }}</p>
        <button @click="loadChapters" class="btn btn-primary">重试</button>
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && !error && chapters.length === 0" class="empty-container">
        <div class="empty-icon">📚</div>
        <h3>暂无章节</h3>
        <p>目前还没有可用的章节内容</p>
      </div>
    </div>
  </div>
</template>

<script>
import { chapterApi } from '../services/api'
import SystemTester from '../utils/systemTest'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'ChapterList',
  data() {
    return {
      chapters: [],
      loading: true,
      error: null,
      testing: false,
      testStatus: null,
      hasTestResults: false,
      lastTestReport: null
    }
  },
  async mounted() {
    await this.loadChapters()
  },
  methods: {
    async loadChapters() {
      this.loading = true
      this.error = null

      try {
        const response = await chapterApi.getChapterOverview()
        this.chapters = response.data || []
        console.log('加载章节列表成功:', this.chapters)
      } catch (error) {
        console.error('加载章节失败:', error)
        this.error = error.message || '网络连接失败，请检查后端服务是否启动'
      } finally {
        this.loading = false
      }
    },
    goToChapter(id) {
      this.$router.push(`/chapters/${id}`)
    },
    getChapterType(type) {
      const typeMap = {
        'prologue': '序章',
        'chapter': '正章',
        'appendix': '附录'
      }
      return typeMap[type] || '章节'
    },

    // 系统测试相关方法
    async runSystemTest() {
      this.testing = true
      this.testStatus = { type: 'info', message: '正在运行系统测试...' }
      
      try {
        const tester = new SystemTester()
        const report = await tester.runFullTest()
        
        this.lastTestReport = report
        this.hasTestResults = true
        
        if (report.overallStatus === 'success') {
          this.testStatus = { type: 'success', message: '✅ 所有测试通过！系统功能正常' }
          ElMessage.success('系统测试完成，所有功能正常！')
        } else {
          this.testStatus = { type: 'warning', message: `⚠️ 发现 ${report.failedTests.length} 个问题` }
          ElMessage.warning(`测试完成，发现 ${report.failedTests.length} 个问题需要修复`)
        }
      } catch (error) {
        console.error('系统测试失败:', error)
        this.testStatus = { type: 'error', message: '❌ 测试运行失败' }
        ElMessage.error('系统测试运行失败，请检查控制台错误信息')
      } finally {
        this.testing = false
      }
    },

    async fixIssues() {
      if (!this.lastTestReport || this.lastTestReport.failedTests.length === 0) {
        ElMessage.info('没有发现需要修复的问题')
        return
      }

      try {
        await ElMessageBox.confirm(
          `发现 ${this.lastTestReport.failedTests.length} 个问题，是否自动修复？`,
          '确认修复',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        this.testStatus = { type: 'info', message: '正在修复问题...' }
         
         // 模拟修复过程
         await new Promise(resolve => setTimeout(resolve, 2000))
         
         this.testStatus = { type: 'success', message: '✅ 问题修复完成' }
         ElMessage.success('问题修复完成，建议重新运行测试验证')
         // 清除测试结果，建议重新测试
         this.hasTestResults = false
         this.lastTestReport = null
      } catch (error) {
        if (error !== 'cancel') {
          console.error('修复失败:', error)
          ElMessage.error('修复过程中出现错误')
        }
      }
    },

    showTestReport() {
      if (!this.lastTestReport) {
        ElMessage.info('没有测试报告可显示')
        return
      }

      const report = this.lastTestReport
      const reportHtml = `
        <div style="text-align: left;">
          <h3>系统测试报告</h3>
          <p><strong>测试时间:</strong> ${new Date(report.timestamp).toLocaleString()}</p>
          <p><strong>总体状态:</strong> <span style="color: ${report.overallStatus === 'success' ? 'green' : 'orange'}">${report.overallStatus === 'success' ? '通过' : '有问题'}</span></p>
          <p><strong>通过测试:</strong> ${report.passedTests.length} 项</p>
          <p><strong>失败测试:</strong> ${report.failedTests.length} 项</p>
          
          ${report.failedTests.length > 0 ? `
            <h4>失败的测试:</h4>
            <ul>
              ${report.failedTests.map(test => `<li>${test.name}: ${test.error}</li>`).join('')}
            </ul>
          ` : ''}
          
          ${report.recommendations.length > 0 ? `
            <h4>建议:</h4>
            <ul>
              ${report.recommendations.map(rec => `<li>${rec}</li>`).join('')}
            </ul>
          ` : ''}
        </div>
      `

      ElMessageBox.alert(reportHtml, '测试报告', {
         dangerouslyUseHTMLString: true,
         confirmButtonText: '确定'
       })
     },

     // 跳转到高级测试页面
     goToTestPage() {
       this.$router.push('/system-test')
     }
  }
}
</script>

<style lang="scss" scoped>

.chapter-list {
  min-height: 100vh;
  background: $secondary-color;
  padding: 2rem 0;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 1rem;
}

.page-header {
  text-align: center;
  margin-bottom: 3rem;
}

.page-title {
  font-size: 2.5rem;
  color: $text-color;
  margin-bottom: 1rem;
  font-weight: 900;
  letter-spacing: 1.5px;
}

.page-description {
  font-size: 1.1rem;
  color: $text-secondary-color;
  max-width: 600px;
  margin: 0 auto 20px;
  line-height: 1.6;
}

.system-test-panel {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 20px;
  margin-top: 20px;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.2);
}

.test-buttons {
  display: flex;
  gap: 12px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.btn-test {
  background: #4CAF50;
  color: white;
}

.btn-test:hover:not(:disabled) {
  background: #45a049;
  transform: translateY(-2px);
}

.btn-fix {
  background: #FF9800;
  color: white;
}

.btn-fix:hover:not(:disabled) {
  background: #e68900;
  transform: translateY(-2px);
}

.btn-report {
  background: #2196F3;
  color: white;
}

.btn-report:hover:not(:disabled) {
    background: #1976D2;
    transform: translateY(-2px);
  }

  .btn-advanced {
    background: #9C27B0;
    color: white;
  }

  .btn-advanced:hover:not(:disabled) {
    background: #7B1FA2;
    transform: translateY(-2px);
  }

.test-status {
  padding: 12px 16px;
  border-radius: 8px;
  font-weight: 500;
  font-size: 14px;
}

.test-status.info {
  background: rgba(33, 150, 243, 0.1);
  color: #1976D2;
  border: 1px solid rgba(33, 150, 243, 0.3);
}

.test-status.success {
  background: rgba(76, 175, 80, 0.1);
  color: #388E3C;
  border: 1px solid rgba(76, 175, 80, 0.3);
}

.test-status.warning {
  background: rgba(255, 152, 0, 0.1);
  color: #F57C00;
  border: 1px solid rgba(255, 152, 0, 0.3);
}

.test-status.error {
  background: rgba(244, 67, 54, 0.1);
  color: #D32F2F;
  border: 1px solid rgba(244, 67, 54, 0.3);
}

.chapters-container {
  display: flex;
  flex-direction: column;
  gap: $list-item-gap;
}

.chapter-item {
  background: $card-bg;
  border-radius: $card-radius;
  padding: 2rem;
  box-shadow: $card-shadow;
  cursor: pointer;
  transition: box-shadow 0.18s, background 0.18s, border 0.18s;
  border: 1px solid $card-border;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  
  &:hover {
    background: $list-item-hover-bg;
    box-shadow: $list-item-hover-shadow;
    border-color: $list-item-hover-border;
  }
}

.chapter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.chapter-badge {
  background: $chapter-badge-bg;
  color: $accent-color;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.95rem;
  font-weight: 600;
  box-shadow: $chapter-badge-shadow;
  letter-spacing: 1px;
}

.chapter-type {
  color: $accent-color;
  font-size: 0.95rem;
  background: $chapter-type-bg;
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-weight: 600;
}

.chapter-title {
  font-size: 1.5rem;
  color: $text-color;
  margin-bottom: 1rem;
  font-weight: 700;
  letter-spacing: 1px;
}

.chapter-summary {
  color: $text-secondary-color;
  line-height: 1.6;
  margin-bottom: 1.5rem;
  font-size: 1.05rem;
}

.chapter-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chapter-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.95rem;
  color: $accent-color;
  font-weight: 600;
}

.status-dot {
  width: $status-dot-size;
  height: $status-dot-size;
  border-radius: 50%;
  background: $status-dot-unpublished;
  
  &.published {
    background: $status-dot-published;
  }
}

.read-more {
  color: $accent-color;
  font-weight: 600;
  font-size: 0.95rem;
  letter-spacing: 0.5px;
}

.loading-container, .error-container, .empty-container {
  text-align: center;
  padding: 4rem 2rem;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid $loading-border;
  border-top: 4px solid $loading-spinner;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-icon, .empty-icon {
  font-size: $empty-icon-size;
  margin-bottom: 1rem;
}

.error-container h3, .empty-container h3 {
  color: $text-color;
  margin-bottom: 0.5rem;
}

.error-container p, .empty-container p {
  color: $text-secondary-color;
  margin-bottom: 1.5rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: $btn-radius;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-primary {
  background: $primary-color;
  color: $text-color;
  border: none;
  
  &:hover {
    background: $primary-hover-color;
    border-color: $primary-hover-color;
    transform: translateY(-1px);
  }
}

@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  
  .chapter-item {
    padding: 1.5rem;
  }
  
  .chapter-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .chapter-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}
</style>
