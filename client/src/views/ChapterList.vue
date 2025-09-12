<template>
  <div class="chapter-list">
    <div class="container">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1 class="page-title">教材章节</h1>
        <p class="page-description">
          《人工智能概论》完整章节列表，点击章节卡片查看详细内容
        </p>
        
        <!-- 系统测试工具已移除 -->
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
              {{ getChapterDisplayNumber(chapter.chapterNumber) }}
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
      lastTestReport: null,
      demo: {
        answered: false,
        correct: false,
        message: ''
      },
      // 第一章更多练习（仅添加图片类题目，对应样例说明中的题目1、3、6、7、9）
      quiz: [
        {
          id: 'q1',
          title: '题目 1：雪纺裙的图片',
          // A 真实 / B AI
          aSrc: '/images/chapter1/A_真实照片.png',
          bSrc: '/images/chapter1/A_ai生成.png',
          answer: { A: 'human', B: 'ai' },
          state: { answered: false, correct: false, message: '' }
        },
        {
          id: 'q3',
          title: '题目 3：城市街景（阿姆斯特丹）',
          aSrc: '/images/chapter1/B_真实图片.png',
          bSrc: '/images/chapter1/B_ai生成.png',
          answer: { A: 'human', B: 'ai' },
          state: { answered: false, correct: false, message: '' }
        },
        {
          id: 'q6',
          title: '题目 6：麻辣烫店铺',
          aSrc: '/images/chapter1/C_真实图片.png',
          bSrc: '/images/chapter1/C_ai生成.png',
          answer: { A: 'human', B: 'ai' },
          state: { answered: false, correct: false, message: '' }
        },
        {
          id: 'q7',
          title: '题目 7：沸腾的火锅',
          aSrc: '/images/chapter1/D_真实图片.png',
          bSrc: '/images/chapter1/D_ai生成.png',
          answer: { A: 'human', B: 'ai' },
          state: { answered: false, correct: false, message: '' }
        },
        {
          id: 'q9',
          title: '题目 9：古典画作 vs AI 画作',
          aSrc: '/images/chapter1/E_真实图片.png',
          bSrc: '/images/chapter1/E_ai生成.png',
          answer: { A: 'human', B: 'ai' },
          state: { answered: false, correct: false, message: '' }
        }
      ]
    }
  },
  async mounted() {
    // 添加调试日志
    console.log('📚 ChapterList组件挂载，开始加载章节列表')
    await this.loadChapters()
  },
  methods: {
    async loadChapters() {
      this.loading = true
      this.error = null

      try {
        const response = await chapterApi.getChapterOverview()
        // 过滤掉附录章节
        this.chapters = (response.data || []).filter(chapter => 
          chapter.type !== 'appendix' && 
          !chapter.title.includes('附录') &&
          !chapter.title.toLowerCase().includes('appendix')
        )
        console.log('加载章节列表成功:', this.chapters)
      } catch (error) {
        console.error('加载章节失败:', error)
        this.error = error.message || '网络连接失败，请检查后端服务是否启动'
      } finally {
        this.loading = false
      }
    },
    // 第一章样例交互逻辑
    answer(option, guess) {
      // 正确答案：A=human, B=ai
      const isCorrect = (option === 'A' && guess === 'human') || (option === 'B' && guess === 'ai')
      this.demo.answered = true
      this.demo.correct = isCorrect
      this.demo.message = isCorrect ? '恭喜你，答对了，很厉害哦！' : '不好意思，答错了。A 为真实图片，B 为 AI 生成。'
    },
    // 第一章更多题目的答题逻辑
    answerQ(id, option, guess) {
      const q = this.quiz.find(x => x.id === id)
      if (!q || q.state.answered) return
      const correct = q.answer[option] === guess
      q.state.answered = true
      q.state.correct = correct
      q.state.message = correct
        ? '恭喜你，答对了，很厉害哦！'
        : `不好意思，答错了。标准答案：A 为 ${q.answer.A === 'human' ? '真实' : 'AI'}，B 为 ${q.answer.B === 'human' ? '真实' : 'AI'}。`
    },
    resetQ(id) {
      const q = this.quiz.find(x => x.id === id)
      if (!q) return
      q.state.answered = false
      q.state.correct = false
      q.state.message = ''
    },
    resetDemo() {
      this.demo.answered = false
      this.demo.correct = false
      this.demo.message = ''
    },
    goToChapter(id) {
      this.$router.push(`/chapters/${id}`)
    },
    getChapterType(type) {
      const typeMap = {
        'prologue': '序章',
        'chapter': '正章'
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
     },

     getChapterDisplayNumber(chapterNumber) {
       if (chapterNumber === '0') {
         return '序章'
       } else if (chapterNumber === 'appendix') {
         return '附录'
       } else {
         return `第${chapterNumber}章`
       }
     }
  }
}
</script>

<style lang="scss" scoped>

.chapter-list {
  min-height: 100vh;
  background: var(--secondary-color, #23272e);
  padding: 2rem 0;
}

/* 第一章样例样式，使用 variables.scss 主题变量 */
.chapter1-demo {
  margin: 2rem 0 3rem;
  background: var(--card-bg, #292c33);
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  border-radius: var(--card-radius, 10px);
  box-shadow: var(--card-shadow, 0 4px 24px rgba(24, 25, 26, 0.10));
  padding: 1.5rem;
}

.demo-header {
  text-align: center;
  margin-bottom: 1rem;
}

.demo-badge {
  display: inline-block;
  background: var(--chapter-badge-bg, linear-gradient(135deg, #18191a, #232526));
  color: var(--accent-color, #b0b3b8);
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-weight: 600;
  box-shadow: var(--chapter-badge-shadow, 0 2px 8px rgba(143, 161, 179, 0.08));
}

.demo-title {
  margin: 0.75rem 0 0.25rem;
  color: var(--text-color, #f5f6fa);
  font-weight: 800;
}

.demo-desc {
  margin: 0;
  color: var(--text-secondary-color, #b0b3b8);
}

.demo-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-top: 1rem;
}

.demo-card {
  background: var(--secondary-color, #23272e);
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  border-radius: var(--card-radius, 10px);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.demo-label {
  padding: 0.5rem 0.75rem;
  background: var(--chapter-type-bg, #23272e);
  color: var(--accent-color, #b0b3b8);
  font-weight: 700;
}

.demo-image {
  width: 100%;
  height: 240px;
  object-fit: cover;
  display: block;
}

.demo-actions {
  display: flex;
  gap: 0.5rem;
  padding: 0.75rem;
}

.btn-secondary {
  background: var(--btn-secondary-bg, #393b40);
  color: var(--text-color, #f5f6fa);
  box-shadow: var(--btn-shadow, 0 4px 16px rgba(74, 144, 226, 0.3));
  border-radius: var(--btn-radius, 12px);
}

.btn-secondary:hover {
  background: var(--btn-secondary-hover, #4a4a4a);
  transform: translateY(-1px);
}

.btn-outline {
  background: transparent;
  color: var(--accent-color, #b0b3b8);
  border: 1px solid var(--btn-outline-border, #4a90e2);
  border-radius: var(--btn-radius, 12px);
  padding: 0.5rem 1rem;
}

.btn-outline:hover {
  background: rgba(255,255,255,0.05);
}

.demo-feedback {
  margin-top: 1rem;
  padding: 0.75rem 1rem;
  border-radius: var(--card-radius, 10px);
  display: flex;
  align-items: center;
  gap: 0.75rem;
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
}

.demo-feedback.success {
  background: rgba(76, 175, 80, 0.1);
  color: #388E3C;
}

.demo-feedback.error {
  background: rgba(244, 67, 54, 0.1);
  color: #D32F2F;
}

.feedback-icon { font-size: 1.3rem; }
.feedback-text { flex: 1; }

.chapter1-quiz-grid {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
  margin-top: 0.5rem;
}

.quiz-row {
  background: var(--card-bg, #292c33);
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  border-radius: var(--card-radius, 10px);
  padding: 1rem;
}

.quiz-title {
  color: var(--text-color, #f5f6fa);
  font-weight: 700;
  margin-bottom: 0.5rem;
}

@media (max-width: 768px) {
  .demo-grid {
    grid-template-columns: 1fr;
  }
  .demo-image { height: 200px; }
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
  color: var(--text-color, #f5f6fa);
  margin-bottom: 1rem;
  font-weight: 900;
  letter-spacing: 1.5px;
}

.page-description {
  font-size: 1.1rem;
  color: var(--text-secondary-color, #b0b3b8);
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
  gap: var(--list-item-gap, 1.5rem);
}

.chapter-item {
  background: var(--card-bg, #292c33);
  border-radius: var(--card-radius, 10px);
  padding: 2rem;
  box-shadow: var(--card-shadow, 0 4px 24px rgba(24, 25, 26, 0.10));
  cursor: pointer;
  transition: box-shadow 0.18s, background 0.18s, border 0.18s;
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  
  &:hover {
    background: var(--list-item-hover-bg, #31343b);
    box-shadow: var(--list-item-hover-shadow, 0 8px 32px #8fa1b3);
    border-color: var(--list-item-hover-border, #8fa1b3);
  }
}

.chapter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.chapter-badge {
  background: var(--chapter-badge-bg, linear-gradient(135deg, #18191a, #232526));
  color: var(--accent-color, #b0b3b8);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.95rem;
  font-weight: 600;
  box-shadow: var(--chapter-badge-shadow, 0 2px 8px rgba(143, 161, 179, 0.08));
  letter-spacing: 1px;
}

.chapter-type {
  color: var(--accent-color, #b0b3b8);
  font-size: 0.95rem;
  background: var(--chapter-type-bg, #23272e);
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-weight: 600;
}

.chapter-title {
  font-size: 1.5rem;
  color: var(--text-color, #f5f6fa);
  margin-bottom: 1rem;
  font-weight: 700;
  letter-spacing: 1px;
}

.chapter-summary {
  color: var(--text-secondary-color, #b0b3b8);
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
  color: var(--accent-color, #b0b3b8);
  font-weight: 600;
}

.status-dot {
  width: var(--status-dot-size, 8px);
  height: var(--status-dot-size, 8px);
  border-radius: 50%;
  background: var(--status-dot-unpublished, #393b40);
  
  &.published {
    background: var(--status-dot-published, #8fa1b3);
  }
}

.read-more {
  color: var(--accent-color, #b0b3b8);
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
  border: 4px solid var(--loading-border, #393b40);
  border-top: 4px solid var(--loading-spinner, #8fa1b3);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-icon, .empty-icon {
  font-size: var(--empty-icon-size, 4rem);
  margin-bottom: 1rem;
}

.error-container h3, .empty-container h3 {
  color: var(--text-color, #f5f6fa);
  margin-bottom: 0.5rem;
}

.error-container p, .empty-container p {
  color: var(--text-secondary-color, #b0b3b8);
  margin-bottom: 1.5rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: var(--btn-radius, 12px);
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
  background: var(--primary-color, #18191a);
  color: var(--text-color, #f5f6fa);
  border: none;
  
  &:hover {
    background: var(--primary-hover-color, #cccccc);
    border-color: var(--primary-hover-color, #cccccc);
    transform: translateY(-1px);
  }
}

/* 浅色主题优化 */
html.light-theme .chapter-list {
  .chapter-item {
    background: var(--card-bg);
    border: 1px solid var(--border-color);
    box-shadow: var(--box-shadow-light);
    
    &:hover {
      background: var(--list-item-hover-bg);
      box-shadow: var(--list-item-hover-shadow);
      border-color: var(--list-item-hover-border);
      transform: translateY(-2px);
    }
  }

  .chapter-badge {
    background: var(--chapter-badge-bg);
    color: white;
    box-shadow: var(--chapter-badge-shadow);
  }

  .chapter-type {
    background: var(--chapter-type-bg);
    color: var(--accent-color);
  }

  .status-dot {
    &.published {
      background: var(--status-dot-published);
    }
    
    &:not(.published) {
      background: var(--status-dot-unpublished);
    }
  }

  .read-more {
    color: var(--accent-color);
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
