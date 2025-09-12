<template>
  <div class="admin-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">管理员控制台</h1>
        <p class="page-description">题目管理和系统管理功能</p>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-section">
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon">📊</div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.totalQuestions || 0 }}</span>
            <span class="stat-label">总题目数</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">📚</div>
          <div class="stat-info">
            <span class="stat-value">{{ Object.keys(stats.chapterStats || {}).length }}</span>
            <span class="stat-label">章节数</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">🎯</div>
          <div class="stat-info">
            <span class="stat-value">{{ Object.keys(stats.typeStats || {}).length }}</span>
            <span class="stat-label">题型数</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">⭐</div>
          <div class="stat-info">
            <span class="stat-value">{{ Object.keys(stats.difficultyStats || {}).length }}</span>
            <span class="stat-label">难度等级</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 操作区域 -->
    <div class="actions-section">
      <div class="action-buttons">
        <button @click="showImportModal = true" class="btn btn-primary">
          <span class="btn-icon">📤</span>
          <span class="btn-text">导入Excel</span>
        </button>
        <button @click="showCreateModal = true" class="btn btn-secondary">
          <span class="btn-icon">➕</span>
          <span class="btn-text">添加题目</span>
        </button>
        <button @click="downloadTemplate" class="btn btn-outline">
          <span class="btn-icon">📥</span>
          <span class="btn-text">下载模板</span>
        </button>
        <button @click="exportQuestions" class="btn btn-outline">
          <span class="btn-icon">📋</span>
          <span class="btn-text">导出题目</span>
        </button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <div class="search-filters">
        <div class="search-box">
          <input 
            v-model="searchKeyword" 
            @input="searchQuestions"
            type="text" 
            placeholder="搜索题目..." 
            class="search-input"
          />
          <span class="search-icon">🔍</span>
        </div>
        <div class="filters">
          <select v-model="selectedChapter" @change="filterQuestions" class="filter-select">
            <option value="">所有章节</option>
            <option v-for="chapter in chapters" :key="chapter" :value="chapter">
              第{{ chapter }}章
            </option>
          </select>
          <select v-model="selectedType" @change="filterQuestions" class="filter-select">
            <option value="">所有题型</option>
            <option value="choice">选择题</option>
            <option value="tf">判断题</option>
            <option value="fill">填空题</option>
          </select>
          <select v-model="selectedDifficulty" @change="filterQuestions" class="filter-select">
            <option value="">所有难度</option>
            <option value="easy">简单</option>
            <option value="medium">中等</option>
            <option value="hard">困难</option>
          </select>
        </div>
      </div>
    </div>

    <!-- 题目列表 -->
    <div class="questions-section">
      <div class="section-header">
        <h2 class="section-title">题目管理</h2>
        <div class="list-controls">
          <span class="question-count">共 {{ totalQuestions }} 道题目</span>
          <div class="pagination-controls">
            <button @click="prevPage" :disabled="currentPage === 1" class="btn-page">上一页</button>
            <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
            <button @click="nextPage" :disabled="currentPage === totalPages" class="btn-page">下一页</button>
          </div>
        </div>
      </div>

      <div class="questions-list" v-if="filteredQuestions.length > 0">
        <div 
          v-for="question in filteredQuestions" 
          :key="question.id"
          class="question-item"
        >
          <div class="question-header">
            <div class="question-meta">
              <span class="question-id">#{{ question.id }}</span>
              <span class="question-chapter">第{{ question.chapterId }}章</span>
              <span class="question-type" :class="getTypeClass(question.type)">
                {{ getTypeText(question.type) }}
              </span>
              <span class="question-difficulty" :class="getDifficultyClass(question.difficulty)">
                {{ getDifficultyText(question.difficulty) }}
              </span>
              <span class="question-score">{{ question.score }}分</span>
            </div>
            <div class="question-actions">
              <button @click="editQuestion(question)" class="btn-icon-btn">
                ✏️
              </button>
              <button @click="deleteQuestion(question.id)" class="btn-icon-btn delete">
                🗑️
              </button>
            </div>
          </div>
          <div class="question-content">
            <h3 class="question-title">{{ question.title }}</h3>
            <p class="question-description" v-if="question.description">{{ question.description }}</p>
            
            <!-- 题目选项 -->
            <div v-if="question.type === 'choice' && question.options" class="question-options">
              <div v-for="(option, index) in question.options" :key="index" class="option-item">
                <span class="option-label">{{ String.fromCharCode(65 + index) }}.</span>
                <span class="option-text">{{ option }}</span>
              </div>
            </div>
            
            <!-- 题目媒体 -->
            <div v-if="question.imageUrl || question.audioUrl || question.videoUrl" class="question-media">
              <div v-if="question.imageUrl" class="media-item">
                <img :src="question.imageUrl" alt="题目图片" class="media-image" />
              </div>
              <div v-if="question.audioUrl" class="media-item">
                <audio controls class="media-audio">
                  <source :src="question.audioUrl" type="audio/mpeg">
                </audio>
              </div>
              <div v-if="question.videoUrl" class="media-item">
                <video controls class="media-video">
                  <source :src="question.videoUrl" type="video/mp4">
                </video>
              </div>
            </div>
            
            <div class="question-footer">
              <span class="correct-answer">正确答案: {{ getCorrectAnswerText(question) }}</span>
              <span class="explanation" v-if="question.explanation">{{ question.explanation }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="empty-state" v-else>
        <div class="empty-icon">📝</div>
        <h3>暂无题目</h3>
        <p>请先导入Excel文件或手动添加题目</p>
      </div>
    </div>

    <!-- Excel导入模态框 -->
    <div class="modal-overlay" v-if="showImportModal" @click="showImportModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>导入Excel文件</h3>
          <button @click="showImportModal = false" class="modal-close">×</button>
        </div>
        <div class="modal-body">
          <div class="upload-area" @click="triggerFileInput" @drop="handleFileDrop" @dragover.prevent>
            <input 
              ref="fileInput" 
              type="file" 
              accept=".xlsx,.xls" 
              @change="handleFileSelect" 
              style="display: none"
            />
            <div class="upload-content">
              <div class="upload-icon">📁</div>
              <p>点击选择文件或拖拽文件到此处</p>
              <p class="upload-hint">支持 .xlsx 和 .xls 格式</p>
            </div>
          </div>
          
          <div v-if="selectedFile" class="file-info">
            <div class="file-name">{{ selectedFile.name }}</div>
            <div class="file-size">{{ formatFileSize(selectedFile.size) }}</div>
          </div>

          <div class="import-tips">
            <h4>Excel文件格式说明：</h4>
            <ul>
              <li>第一行为标题行，从第二行开始为数据</li>
              <li>列顺序：章节ID | 题型 | 题目 | 描述 | 选项 | 正确答案 | 分值 | 解释 | 难度 | 图片URL | 音频URL | 视频URL</li>
              <li>题型：choice(选择题) | tf(判断题) | fill(填空题)</li>
              <li>难度：easy(简单) | medium(中等) | hard(困难)</li>
              <li>选项：多个选项用 | 分隔</li>
              <li>选择题答案：A=0, B=1, C=2, D=3</li>
              <li>判断题答案：true/正确 或 false/错误</li>
            </ul>
          </div>
        </div>
        <div class="modal-footer">
          <button @click="showImportModal = false" class="btn btn-secondary">取消</button>
          <button @click="importQuestions" class="btn btn-primary" :disabled="!selectedFile || importing">
            {{ importing ? '导入中...' : '开始导入' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 创建/编辑题目模态框 -->
    <div class="modal-overlay" v-if="showCreateModal" @click="showCreateModal = false">
      <div class="modal-content large" @click.stop>
        <div class="modal-header">
          <h3>{{ editingQuestion ? '编辑题目' : '添加题目' }}</h3>
          <button @click="showCreateModal = false" class="modal-close">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveQuestion" class="question-form">
            <div class="form-row">
              <div class="form-group">
                <label>章节ID *</label>
                <input v-model="questionForm.chapterId" type="number" min="1" max="7" required />
              </div>
              <div class="form-group">
                <label>题型 *</label>
                <select v-model="questionForm.type" required>
                  <option value="choice">选择题</option>
                  <option value="tf">判断题</option>
                  <option value="fill">填空题</option>
                </select>
              </div>
              <div class="form-group">
                <label>难度 *</label>
                <select v-model="questionForm.difficulty" required>
                  <option value="easy">简单</option>
                  <option value="medium">中等</option>
                  <option value="hard">困难</option>
                </select>
              </div>
              <div class="form-group">
                <label>分值 *</label>
                <input v-model="questionForm.score" type="number" min="1" max="100" required />
              </div>
            </div>
            
            <div class="form-group">
              <label>题目 *</label>
              <textarea v-model="questionForm.title" rows="3" required></textarea>
            </div>
            
            <div class="form-group">
              <label>描述</label>
              <textarea v-model="questionForm.description" rows="2"></textarea>
            </div>
            
            <div class="form-group" v-if="questionForm.type === 'choice'">
              <label>选项 *</label>
              <div class="options-list">
                <div v-for="(option, index) in questionForm.options" :key="index" class="option-item">
                  <input v-model="questionForm.options[index]" type="text" :placeholder="`选项 ${String.fromCharCode(65 + index)}`" required />
                  <button type="button" @click="removeOption(index)" class="remove-option" v-if="questionForm.options.length > 2">×</button>
                </div>
              </div>
              <button type="button" @click="addOption" class="btn btn-outline">添加选项</button>
            </div>
            
            <div class="form-group">
              <label>正确答案 *</label>
              <input v-model="questionForm.correctAnswer" type="text" required />
            </div>
            
            <div class="form-group">
              <label>解释</label>
              <textarea v-model="questionForm.explanation" rows="3"></textarea>
            </div>
            
            <div class="form-row">
              <div class="form-group">
                <label>图片URL</label>
                <input v-model="questionForm.imageUrl" type="url" placeholder="https://example.com/image.jpg" />
              </div>
              <div class="form-group">
                <label>音频URL</label>
                <input v-model="questionForm.audioUrl" type="url" placeholder="https://example.com/audio.mp3" />
              </div>
              <div class="form-group">
                <label>视频URL</label>
                <input v-model="questionForm.videoUrl" type="url" placeholder="https://example.com/video.mp4" />
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button @click="showCreateModal = false" class="btn btn-secondary">取消</button>
          <button @click="saveQuestion" class="btn btn-primary" :disabled="saving">
            {{ saving ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { adminApi } from '../services/api'

export default {
  name: 'AdminPage',
  data() {
    return {
      questions: [],
      filteredQuestions: [],
      stats: {},
      selectedChapter: '',
      selectedType: '',
      selectedDifficulty: '',
      searchKeyword: '',
      chapters: ['1', '2', '3', '4', '6', '7'], // 第五章暂未实现，已移除
      showImportModal: false,
      showCreateModal: false,
      selectedFile: null,
      importing: false,
      saving: false,
      editingQuestion: null,
      currentPage: 1,
      pageSize: 10,
      totalQuestions: 0,
      totalPages: 1,
      questionForm: {
        chapterId: '',
        type: 'choice',
        title: '',
        description: '',
        options: ['', '', '', ''],
        correctAnswer: '',
        score: 20,
        difficulty: 'medium',
        explanation: '',
        imageUrl: '',
        audioUrl: '',
        videoUrl: ''
      }
    }
  },
  async mounted() {
    await this.loadData()
  },
  methods: {
    async loadData() {
      try {
        console.log('开始加载数据...')
        
        const [questionsResponse, statsResponse] = await Promise.all([
          adminApi.getAllQuestions(),
          adminApi.getQuestionStats()
        ])
        
        console.log('API响应:', { questionsResponse, statsResponse })
        
        // 处理题目数据
        let questions = []
        if (questionsResponse && Array.isArray(questionsResponse)) {
          questions = questionsResponse
        } else if (questionsResponse && Array.isArray(questionsResponse.data)) {
          questions = questionsResponse.data
        } else if (questionsResponse && questionsResponse.data && Array.isArray(questionsResponse.data)) {
          questions = questionsResponse.data
        }
        
        this.questions = questions
        this.totalQuestions = this.questions.length
        this.totalPages = Math.ceil(this.totalQuestions / this.pageSize)
        this.filteredQuestions = [...this.questions]
        
        // 处理统计数据
        let stats = {}
        if (statsResponse && typeof statsResponse === 'object') {
          if (statsResponse.data) {
            stats = statsResponse.data
          } else {
            stats = statsResponse
          }
        }
        this.stats = stats
        
        console.log('数据加载完成:', {
          questionsCount: this.questions.length,
          totalQuestions: this.totalQuestions,
          totalPages: this.totalPages,
          stats: this.stats
        })
        
      } catch (error) {
        console.error('加载数据失败:', error)
        console.log('使用模拟数据作为备用方案')
        // 如果API调用失败，使用模拟数据
        this.loadMockData()
      }
    },
    
    loadMockData() {
      console.log('加载模拟数据...')
      // 模拟数据，用于演示
      this.questions = [
        {
          id: 1,
          chapterId: '1',
          type: 'choice',
          title: '人工智能的定义是什么？',
          description: '选择最准确的人工智能定义',
          options: ['能够执行特定任务的计算机程序', '模拟人类智能的计算机系统', '能够学习的算法', '自动化的机器'],
          correctAnswer: '1',
          score: 20,
          difficulty: 'medium',
          explanation: '人工智能是模拟人类智能的计算机系统，能够执行通常需要人类智能的任务。'
        },
        {
          id: 2,
          chapterId: '1',
          type: 'tf',
          title: '图灵测试是判断机器是否具有智能的标准方法。',
          correctAnswer: 'true',
          score: 15,
          difficulty: 'easy',
          explanation: '图灵测试由艾伦·图灵提出，通过判断机器能否在对话中让人类无法区分其是否为人类来测试智能。'
        },
        {
          id: 3,
          chapterId: '1',
          type: 'fill',
          title: '人工智能之父是谁？',
          description: '请输入人名',
          correctAnswer: '约翰·麦卡锡',
          score: 25,
          difficulty: 'medium',
          explanation: '约翰·麦卡锡在1956年的达特茅斯会议上首次提出"人工智能"这一术语。'
        }
      ]
      this.totalQuestions = this.questions.length
      this.totalPages = Math.ceil(this.totalQuestions / this.pageSize)
      this.filteredQuestions = [...this.questions]
      this.stats = {
        totalQuestions: 3,
        chapterStats: { '1': 3 },
        typeStats: { 'choice': 1, 'tf': 1, 'fill': 1 },
        difficultyStats: { 'easy': 1, 'medium': 2 }
      }
      
      console.log('模拟数据加载完成:', {
        questionsCount: this.questions.length,
        totalQuestions: this.totalQuestions,
        totalPages: this.totalPages,
        stats: this.stats
      })
    },
    
    async searchQuestions() {
      if (!this.searchKeyword.trim()) {
        await this.loadData()
        return
      }
      
      // 本地搜索，因为adminApi可能不支持搜索
      this.filteredQuestions = this.questions.filter(question => 
        question.title.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
        question.description?.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
        question.explanation?.toLowerCase().includes(this.searchKeyword.toLowerCase())
      )
      this.totalQuestions = this.filteredQuestions.length
      this.totalPages = Math.ceil(this.totalQuestions / this.pageSize)
      this.currentPage = 1
    },
    
    filterQuestions() {
      this.filteredQuestions = this.questions.filter(question => {
        const chapterMatch = !this.selectedChapter || question.chapterId == this.selectedChapter
        const typeMatch = !this.selectedType || question.type === this.selectedType
        const difficultyMatch = !this.selectedDifficulty || question.difficulty === this.selectedDifficulty
        return chapterMatch && typeMatch && difficultyMatch
      })
    },
    
    async prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--
        await this.loadData()
      }
    },
    
    async nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++
        await this.loadData()
      }
    },
    
    getTypeClass(type) {
      const classMap = {
        'choice': 'type-choice',
        'tf': 'type-tf',
        'fill': 'type-fill'
      }
      return classMap[type] || ''
    },
    
    getTypeText(type) {
      const textMap = {
        'choice': '选择题',
        'tf': '判断题',
        'fill': '填空题'
      }
      return textMap[type] || '题目'
    },
    
    getDifficultyClass(difficulty) {
      const classMap = {
        'easy': 'difficulty-easy',
        'medium': 'difficulty-medium',
        'hard': 'difficulty-hard'
      }
      return classMap[difficulty] || ''
    },
    
    getDifficultyText(difficulty) {
      const textMap = {
        'easy': '简单',
        'medium': '中等',
        'hard': '困难'
      }
      return textMap[difficulty] || '未知'
    },
    
    getCorrectAnswerText(question) {
      if (question.type === 'choice') {
        const options = ['A', 'B', 'C', 'D']
        return options[question.correctAnswer] || question.correctAnswer
      }
      return question.correctAnswer
    },
    
    triggerFileInput() {
      this.$refs.fileInput.click()
    },
    
    handleFileSelect(event) {
      const file = event.target.files[0]
      if (file) {
        this.selectedFile = file
      }
    },
    
    handleFileDrop(event) {
      event.preventDefault()
      const file = event.dataTransfer.files[0]
      if (file && (file.name.endsWith('.xlsx') || file.name.endsWith('.xls'))) {
        this.selectedFile = file
      }
    },
    
    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes'
      const k = 1024
      const sizes = ['Bytes', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    },
    
    async importQuestions() {
      if (!this.selectedFile) return
      
      this.importing = true
      try {
        const result = await adminApi.importQuestions(this.selectedFile)
        if (result.success) {
          alert(`导入成功！共导入 ${result.importedCount} 道题目`)
          this.showImportModal = false
          this.selectedFile = null
          await this.loadData()
        } else {
          alert(`导入失败：${result.message}`)
        }
      } catch (error) {
        console.error('导入失败:', error)
        alert('导入失败：' + error.message)
      } finally {
        this.importing = false
      }
    },
    
    async exportQuestions() {
      try {
        // 使用adminApi导出，或者本地生成CSV
        const csvContent = this.generateCSV()
        const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `题目导出_${new Date().toISOString().split('T')[0]}.csv`
        link.click()
        window.URL.revokeObjectURL(url)
      } catch (error) {
        console.error('导出失败:', error)
        alert('导出失败：' + error.message)
      }
    },
    
    generateCSV() {
      const headers = ['ID', '章节ID', '题型', '题目', '描述', '选项', '正确答案', '分值', '难度', '解释']
      const rows = this.questions.map(q => [
        q.id,
        q.chapterId,
        q.type,
        q.title,
        q.description || '',
        q.options ? q.options.join('|') : '',
        q.correctAnswer,
        q.score,
        q.difficulty || 'medium',
        q.explanation || ''
      ])
      
      return [headers, ...rows].map(row => row.map(cell => `"${cell}"`).join(',')).join('\n')
    },
    
    downloadTemplate() {
      // 创建Excel模板下载链接
      const templateData = [
        ['章节ID', '题型', '题目', '描述', '选项', '正确答案', '分值', '解释', '难度', '图片URL', '音频URL', '视频URL'],
        ['1', 'choice', '人工智能的定义是什么？', '选择最准确的人工智能定义', '能够执行特定任务的计算机程序|模拟人类智能的计算机系统|能够学习的算法|自动化的机器', '1', '20', '人工智能是模拟人类智能的计算机系统，能够执行通常需要人类智能的任务。', 'medium', '', '', ''],
        ['1', 'tf', '图灵测试是判断机器是否具有智能的标准方法。', '', '', 'true', '15', '图灵测试由艾伦·图灵提出，通过判断机器能否在对话中让人类无法区分其是否为人类来测试智能。', 'easy', '', '', ''],
        ['1', 'fill', '人工智能之父是谁？', '请输入人名', '', '约翰·麦卡锡', '25', '约翰·麦卡锡在1956年的达特茅斯会议上首次提出"人工智能"这一术语。', 'medium', '', '', '']
      ]
      
      let csvContent = "data:text/csv;charset=utf-8,\uFEFF"
      templateData.forEach(row => {
        csvContent += row.join(',') + '\n'
      })
      
      const link = document.createElement('a')
      link.href = encodeURI(csvContent)
      link.download = '题目导入模板.csv'
      link.click()
    },
    
    editQuestion(question) {
      this.editingQuestion = question
      this.questionForm = {
        chapterId: question.chapterId,
        type: question.type,
        title: question.title,
        description: question.description || '',
        options: question.options ? [...question.options] : ['', '', '', ''],
        correctAnswer: String(question.correctAnswer),
        score: question.score,
        difficulty: question.difficulty || 'medium',
        explanation: question.explanation || '',
        imageUrl: question.imageUrl || '',
        audioUrl: question.audioUrl || '',
        videoUrl: question.videoUrl || ''
      }
      this.showCreateModal = true
    },
    
    addOption() {
      this.questionForm.options.push('')
    },
    
    removeOption(index) {
      if (this.questionForm.options.length > 2) {
        this.questionForm.options.splice(index, 1)
      }
    },
    
    async saveQuestion() {
      this.saving = true
      try {
        // 验证章节ID（第5章暂未实现）
        if (this.questionForm.chapterId === 5 || this.questionForm.chapterId === '5') {
          alert('第5章暂未实现，请选择其他章节')
          this.saving = false
          return
        }
        
        const questionData = {
          chapterId: this.questionForm.chapterId,
          type: this.questionForm.type,
          title: this.questionForm.title,
          description: this.questionForm.description,
          options: this.questionForm.type === 'choice' ? this.questionForm.options.filter(opt => opt.trim()) : null,
          correctAnswer: this.questionForm.correctAnswer,
          score: this.questionForm.score,
          difficulty: this.questionForm.difficulty,
          explanation: this.questionForm.explanation,
          imageUrl: this.questionForm.imageUrl || null,
          audioUrl: this.questionForm.audioUrl || null,
          videoUrl: this.questionForm.videoUrl || null
        }
        
        if (this.editingQuestion) {
          // 更新现有题目
          const index = this.questions.findIndex(q => q.id === this.editingQuestion.id)
          if (index !== -1) {
            this.questions[index] = { ...this.editingQuestion, ...questionData }
          }
        } else {
          // 创建新题目
          const newQuestion = {
            id: Date.now(), // 临时ID
            ...questionData
          }
          this.questions.push(newQuestion)
        }
        
        this.showCreateModal = false
        this.editingQuestion = null
        this.resetQuestionForm()
        await this.loadData()
        alert(this.editingQuestion ? '题目更新成功' : '题目创建成功')
        // 更新统计数据
        this.updateStats()
      } catch (error) {
        console.error('保存题目失败:', error)
        alert('保存失败：' + error.message)
      } finally {
        this.saving = false
      }
    },
    
    resetQuestionForm() {
      this.questionForm = {
        chapterId: '',
        type: 'choice',
        title: '',
        description: '',
        options: ['', '', '', ''],
        correctAnswer: '',
        score: 20,
        difficulty: 'medium',
        explanation: '',
        imageUrl: '',
        audioUrl: '',
        videoUrl: ''
      }
    },
    
    async deleteQuestion(id) {
      if (!confirm('确定要删除这道题目吗？')) return
      
      try {
        // 本地删除
        this.questions = this.questions.filter(q => q.id !== id)
        this.filteredQuestions = this.filteredQuestions.filter(q => q.id !== id)
        this.totalQuestions = this.questions.length
        this.totalPages = Math.ceil(this.totalQuestions / this.pageSize)
        alert('题目删除成功')
        // 更新统计数据
        this.updateStats()
      } catch (error) {
        console.error('删除题目失败:', error)
        alert('删除失败：' + error.message)
      }
    },
    
    updateStats() {
      // 更新统计数据
      this.totalQuestions = this.questions.length
      this.totalPages = Math.ceil(this.totalQuestions / this.pageSize)
      
      // 统计章节分布
      const chapterStats = {}
      this.questions.forEach(q => {
        chapterStats[q.chapterId] = (chapterStats[q.chapterId] || 0) + 1
      })
      
      // 统计题型分布
      const typeStats = {}
      this.questions.forEach(q => {
        typeStats[q.type] = (typeStats[q.type] || 0) + 1
      })
      
      // 统计难度分布
      const difficultyStats = {}
      this.questions.forEach(q => {
        difficultyStats[q.difficulty] = (difficultyStats[q.difficulty] || 0) + 1
      })
      
      this.stats = {
        totalQuestions: this.totalQuestions,
        chapterStats,
        typeStats,
        difficultyStats
      }
    }
  }
}
</script>

<style lang="scss" scoped>

.admin-page {
  background: var(--secondary-color, #23272e);
  min-height: 100vh;
  padding: 2rem 0;
}

.page-header {
  background: linear-gradient(135deg, var(--card-bg, #292c33), var(--secondary-color, #23272e));
  padding: 3rem 0;
  margin-bottom: 2rem;
  border-bottom: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  text-align: center;
}

.page-title {
  font-size: 2.5rem;
  color: var(--text-color, #f5f6fa);
  margin-bottom: 1rem;
  font-weight: 700;
  letter-spacing: 1px;
}

.page-description {
  font-size: 1.1rem;
  color: var(--text-secondary-color, #b0b3b8);
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

.stats-section {
  max-width: 1200px;
  margin: 0 auto 2rem;
  padding: 0 2rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
}

.stat-card {
  background: var(--card-bg, #292c33);
  border-radius: var(--card-radius, 10px);
  padding: 2rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  box-shadow: var(--card-shadow, 0 4px 24px rgba(24, 25, 26, 0.10));
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
}

.stat-icon {
  font-size: 2.5rem;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 2rem;
  color: var(--text-color, #f5f6fa);
  font-weight: 700;
}

.stat-label {
  color: var(--accent-color, #b0b3b8);
  font-size: 0.9rem;
}

.actions-section {
  max-width: 1200px;
  margin: 0 auto 2rem;
  padding: 0 2rem;
}

.action-buttons {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem 2rem;
  border: none;
  border-radius: var(--btn-radius, 12px);
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: var(--btn-primary-bg, linear-gradient(135deg, #4a90e2, #357abd));
  color: var(--text-color, #f5f6fa);
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--btn-shadow, 0 4px 16px rgba(74, 144, 226, 0.3));
  }
}

.btn-secondary {
  background: var(--btn-secondary-bg, #393b40);
  color: var(--text-color, #f5f6fa);
  
  &:hover {
    background: var(--btn-secondary-hover, #4a4a4a);
    transform: translateY(-2px);
  }
}

.btn-outline {
  background: transparent;
  color: var(--btn-outline-color, #4a90e2);
  border: 2px solid var(--btn-outline-border, #4a90e2);
  
  &:hover {
    background: var(--btn-outline-color, #4a90e2);
    color: var(--text-color, #f5f6fa);
    transform: translateY(-2px);
  }
}

.btn-icon {
  font-size: 1.2rem;
}

.search-section {
  max-width: 1200px;
  margin: 0 auto 2rem;
  padding: 0 2rem;
}

.search-filters {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  align-items: center;
  background: var(--card-bg, #292c33);
  border-radius: var(--form-radius, 10px);
  padding: 0.5rem 1rem;
  box-shadow: var(--card-shadow, 0 4px 24px rgba(24, 25, 26, 0.10));
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
}

.search-box {
  display: flex;
  align-items: center;
  background: var(--form-bg, #23272e);
  border-radius: var(--form-radius, 10px);
  padding: 0.5rem 1rem;
  border: 1px solid var(--form-border, rgba(57, 59, 64, 0.18));
  flex: 1;
  max-width: 300px;
}

.search-input {
  background: none;
  border: none;
  color: var(--text-color, #f5f6fa);
  font-size: 1rem;
  width: 100%;
  
  &:focus {
    outline: none;
  }
}

.search-icon {
  font-size: 1.2rem;
  color: var(--accent-color, #b0b3b8);
}

.filters {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.filter-select {
  padding: 0.5rem 1rem;
  background: var(--card-bg, #292c33);
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  border-radius: var(--form-radius, 10px);
  color: var(--text-color, #f5f6fa);
  font-size: 0.9rem;
}

.questions-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.section-title {
  color: var(--text-color, #f5f6fa);
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
}

.list-controls {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex-wrap: wrap;
}

.question-count {
  color: var(--text-secondary-color, #b0b3b8);
  font-size: 0.9rem;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--text-color, #f5f6fa);
  font-size: 0.9rem;
}

.btn-page {
  background: var(--card-bg, #292c33);
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  border-radius: var(--form-radius, 10px);
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
  
  &:hover:not(:disabled) {
            background: var(--list-item-hover-bg, #31343b);
    border-color: var(--card-border, rgba(57, 59, 64, 0.18));
  }
  
  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
}

.questions-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.question-item {
  background: var(--card-bg, #292c33);
  border-radius: var(--card-radius, 10px);
  padding: 1.5rem;
  box-shadow: var(--card-shadow, 0 4px 24px rgba(24, 25, 26, 0.10));
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.question-meta {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.question-id {
  color: var(--accent-color, #b0b3b8);
  font-size: 0.9rem;
  font-weight: 600;
}

.question-chapter {
  color: var(--info-color, #4a90e2);
  font-weight: 600;
}

.question-type {
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
  
  &.type-choice {
    background: var(--type-choice-bg, linear-gradient(135deg, #667eea, #764ba2));
    color: var(--text-color, #f5f6fa);
  }
  
  &.type-tf {
    background: var(--type-tf-bg, linear-gradient(135deg, #f093fb, #f5576c));
    color: var(--text-color, #f5f6fa);
  }
  
  &.type-fill {
    background: var(--type-fill-bg, linear-gradient(135deg, #4facfe, #00f2fe));
    color: var(--text-color, #f5f6fa);
  }
}

.question-difficulty {
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
  
  &.difficulty-easy {
    background: var(--difficulty-easy-bg, linear-gradient(135deg, #4caf50, #66bb6a));
    color: var(--text-color, #f5f6fa);
  }
  
  &.difficulty-medium {
    background: var(--difficulty-medium-bg, linear-gradient(135deg, #ff9800, #ffb74d));
    color: var(--text-color, #f5f6fa);
  }
  
  &.difficulty-hard {
    background: var(--difficulty-hard-bg, linear-gradient(135deg, #f44336, #ef5350));
    color: var(--text-color, #f5f6fa);
  }
}

.question-score {
  color: var(--success-color, #4caf50);
  font-weight: 600;
}

.question-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-icon-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: var(--form-radius, 10px);
  transition: background 0.2s ease;
  
  &:hover {
    background: rgba(143, 161, 179, 0.1);
  }
  
  &.delete:hover {
    background: rgba(var(--error-color, #f44336), 0.1);
  }
}

.question-content {
  color: var(--text-color, #f5f6fa);
}

.question-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  line-height: 1.5;
}

.question-description {
  color: var(--text-secondary-color, #b0b3b8);
  font-size: 0.9rem;
  line-height: 1.6;
}

.question-options {
  margin-top: 0.5rem;
  padding-left: 1.5rem;
  color: var(--text-secondary-color, #b0b3b8);
  font-size: 0.9rem;
}

.option-item {
  display: flex;
  align-items: center;
  margin-bottom: 0.3rem;
}

.option-label {
  font-weight: 600;
  margin-right: 0.5rem;
}

.option-text {
  flex: 1;
}

.question-media {
  margin-top: 1rem;
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.media-item {
  background: var(--card-bg, #292c33);
  border-radius: var(--form-radius, 10px);
  padding: 0.5rem;
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  box-shadow: var(--card-shadow, 0 4px 24px rgba(24, 25, 26, 0.10));
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.media-image {
  max-width: 150px;
  max-height: 100px;
  object-fit: contain;
}

.media-audio,
.media-video {
  max-width: 200px;
  max-height: 50px;
  object-fit: contain;
}

.question-footer {
  margin-top: 1rem;
  color: var(--text-secondary-color, #b0b3b8);
  font-size: 0.9rem;
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.correct-answer {
  font-weight: 600;
  color: var(--success-color, #4caf50);
}

.explanation {
  font-style: italic;
  color: var(--text-secondary-color, #b0b3b8);
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: var(--accent-color, #b0b3b8);
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: var(--modal-overlay-bg, rgba(0, 0, 0, 0.8));
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 2rem;
}

.modal-content {
  background: var(--modal-bg, #292c33);
  border-radius: var(--modal-radius, 16px);
  max-width: 600px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
  
  &.large {
    max-width: 800px;
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 2rem;
  border-bottom: 1px solid var(--card-header-border, rgba(57, 59, 64, 0.18));
}

.modal-header h3 {
  color: var(--text-color, #f5f6fa);
  margin: 0;
  font-size: 1.3rem;
}

.modal-close {
  background: none;
  border: none;
  color: var(--accent-color, #b0b3b8);
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: var(--form-radius, 10px);
  transition: background 0.2s ease;
  
  &:hover {
    background: rgba(143, 161, 179, 0.1);
  }
}

.modal-body {
  padding: 2rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1.5rem 2rem;
  border-top: 1px solid var(--card-header-border, rgba(57, 59, 64, 0.18));
}

.upload-area {
  border: 2px dashed var(--upload-border, rgba(57, 59, 64, 0.18));
  border-radius: var(--upload-radius, 10px);
  padding: 3rem 2rem;
  text-align: center;
  cursor: pointer;
  transition: border-color 0.2s ease;
  
  &:hover {
    border-color: var(--upload-hover-border, #4a90e2);
  }
}

.upload-content {
  color: var(--accent-color, #b0b3b8);
}

.upload-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.upload-hint {
  font-size: 0.9rem;
  opacity: 0.7;
}

.file-info {
  margin-top: 1rem;
  padding: 1rem;
  background: var(--secondary-color, #23272e);
  border-radius: var(--form-radius, 10px);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.file-name {
  color: var(--text-color, #f5f6fa);
  font-weight: 600;
}

.file-size {
  color: var(--accent-color, #b0b3b8);
  font-size: 0.9rem;
}

.import-tips {
  margin-top: 2rem;
  padding: 1.5rem;
  background: var(--secondary-color, #23272e);
  border-radius: var(--upload-radius, 10px);
  border-left: 4px solid var(--info-color, #4a90e2);
}

.import-tips h4 {
  color: var(--text-color, #f5f6fa);
  margin-bottom: 1rem;
}

.import-tips ul {
  color: var(--text-secondary-color, #b0b3b8);
  line-height: 1.6;
  padding-left: 1.5rem;
}

.question-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  color: var(--text-color, #f5f6fa);
  font-weight: 600;
  font-size: 0.9rem;
}

.form-group input,
.form-group select,
.form-group textarea {
  padding: 0.8rem 1rem;
  background: var(--form-bg, #23272e);
  border: 1px solid var(--form-border, rgba(57, 59, 64, 0.18));
  border-radius: var(--form-radius, 10px);
  color: var(--text-color, #f5f6fa);
  font-size: 1rem;
  
  &:focus {
    outline: none;
    border-color: var(--form-focus-border, #4a90e2);
  }
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.options-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.option-item {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.option-item input {
  flex: 1;
}

.remove-option {
  background: var(--error-color, #f44336);
  color: var(--text-color, #f5f6fa);
  border: none;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  cursor: pointer;
  font-size: 1.2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  
  &:hover {
    background: var(--error-hover, #e53e3e);
  }
}

@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .search-filters {
    flex-direction: column;
    align-items: flex-start;
  }

  .search-box {
    width: 100%;
    max-width: none;
  }

  .filters {
    width: 100%;
    flex-direction: column;
  }
  
  .filter-select {
    flex: 1;
  }
  
  .question-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .question-meta {
    width: 100%;
  }
  
  .question-media {
    flex-direction: column;
    align-items: center;
  }

  .media-item {
    width: 100%;
    max-width: 250px;
  }

  .media-image {
    max-width: 100%;
    max-height: 200px;
  }

  .media-audio,
  .media-video {
    max-width: 100%;
    max-height: 100px;
  }

  .modal-content {
    margin: 1rem;
    max-height: calc(100vh - 2rem);
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>