<template>
  <div class="chapter-detail">
    <!-- 导航栏 -->
    <nav class="chapter-nav">
      <div class="nav-container">
        <button @click="goBack" class="nav-back">
          ← 返回章节列表
        </button>
        <div class="nav-title" v-if="chapter">
          {{ chapter.chapterNumber === '0' ? '续章' : `第${chapter.chapterNumber}章` }} - {{ chapter.title }}
        </div>
      </div>
    </nav>

    <div class="container">
      <!-- 章节内容 -->
      <article class="chapter-content" v-if="chapter && !loading">
        <!-- 章节头部 -->
        <header class="chapter-header">
          <div class="chapter-meta">
            <span class="chapter-badge">
              {{ chapter.chapterNumber === '0' ? '续章' : `第${chapter.chapterNumber}章` }}
            </span>
            <span class="chapter-type">{{ getChapterType(chapter.type) }}</span>
          </div>
          <h1 class="chapter-title">{{ chapter.title }}</h1>
          <p class="chapter-summary">{{ chapter.summary }}</p>
          <div class="chapter-info">
            <span class="info-item">
              <span class="info-label">更新时间:</span>
              <span class="info-value">{{ formatDate(chapter.updatedAt) }}</span>
            </span>
            <span class="info-item">
              <span class="info-label">状态:</span>
              <span class="info-value" :class="{ published: chapter.published }">
                {{ chapter.published ? '已发布' : '未发布' }}
              </span>
            </span>
          </div>
        </header>

        <!-- 章节正文 -->
        <div class="chapter-body">

          
          <!-- 第一章案例演示 -->
          <div v-if="isChapter1 && !showChapter1Quiz" class="case-study-section">
            <div class="case-study-header">
              <h2 class="case-study-title">🎯 第一章案例演示</h2>
              <p class="case-study-description">通过实际案例学习，掌握人工智能的基本概念和应用</p>
            </div>
            
            <!-- 第一题：雪纺裙图片 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">题目 1：雪纺裙的图片</h3>
                <p class="question-description">从两张图片中判断哪一张是真实拍摄，哪一张是 AI 生成。</p>
              </div>
              
              <div class="case-study-options">
                <!-- A选项 -->
                <div class="option-card option-a">
                  <h4 class="option-label">A（真实图片）</h4>
                  <div class="option-image">
                    <img src="/images/chapter1/A_真实照片.png" alt="A 真实图片" />
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(1, 'A', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(1, 'A', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
                
                <!-- B选项 -->
                <div class="option-card option-b">
                  <h4 class="option-label">B（AI 图片）</h4>
                  <div class="option-image">
                    <img src="/images/chapter1/A_ai生成.png" alt="B AI 图片" />
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(1, 'B', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(1, 'B', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 答题反馈 -->
              <div v-if="questionResults[1]" class="feedback-card" :class="{ 'correct': questionResults[1].correct, 'incorrect': !questionResults[1].correct }">
                <div class="feedback-icon">{{ questionResults[1].correct ? '🎉' : '💡' }}</div>
                <div class="feedback-message">{{ questionResults[1].message }}</div>
                <button @click="resetQuestion(1)" class="btn btn-outline">
                  再试一次
                </button>
              </div>
            </div>

            <!-- 第二题：音频对比 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">题目 2：人声女声翻唱和AI女声翻唱《告白气球》</h3>
                <p class="question-description">请听两段音频，判断哪段是人声翻唱，哪段是AI翻唱。</p>
              </div>
              
              <div class="case-study-options">
                <!-- A选项 -->
                <div class="option-card option-a">
                  <h4 class="option-label">A（人声翻唱）</h4>
                  <div class="option-content">
                    <div class="audio-placeholder">
                      <div class="placeholder-icon">🎵</div>
                      <div class="placeholder-title">人声翻唱《告白气球》</div>
                      <div class="placeholder-desc">请听音频判断</div>
                      <div class="placeholder-link">
                        <a href="https://www.bilibili.com/video/BV1ts41117qh/" target="_blank" class="link-btn">观看原视频</a>
                      </div>
                    </div>
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(2, 'A', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(2, 'A', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
                
                <!-- B选项 -->
                <div class="option-card option-b">
                  <h4 class="option-label">B（AI翻唱）</h4>
                  <div class="option-content">
                    <div class="audio-placeholder">
                      <div class="placeholder-icon">🎵</div>
                      <div class="placeholder-title">AI翻唱《告白气球》</div>
                      <div class="placeholder-desc">请听音频判断</div>
                      <div class="placeholder-link">
                        <a href="https://www.bilibili.com/video/BV17t421E7pz/" target="_blank" class="link-btn">观看原视频</a>
                      </div>
                    </div>
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(2, 'B', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(2, 'B', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 答题反馈 -->
              <div v-if="questionResults[2]" class="feedback-card" :class="{ 'correct': questionResults[2].correct, 'incorrect': !questionResults[2].correct }">
                <div class="feedback-icon">{{ questionResults[2].correct ? '🎉' : '💡' }}</div>
                <div class="feedback-message">{{ questionResults[2].message }}</div>
                <button @click="resetQuestion(2)" class="btn btn-outline">
                  再试一次
                </button>
              </div>
            </div>

            <!-- 第三题：城市街景 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">题目 3：城市街景（阿姆斯特丹）</h3>
                <p class="question-description">从两张图片中判断哪一张是真实拍摄，哪一张是 AI 生成。</p>
              </div>
              
              <div class="case-study-options">
                <!-- A选项 -->
                <div class="option-card option-a">
                  <h4 class="option-label">A（真实图片）</h4>
                  <div class="option-image">
                    <img src="/images/chapter1/B_真实图片.png" alt="A 真实图片" />
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(3, 'A', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(3, 'A', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
                
                <!-- B选项 -->
                <div class="option-card option-b">
                  <h4 class="option-label">B（AI 图片）</h4>
                  <div class="option-image">
                    <img src="/images/chapter1/B_ai生成.png" alt="B AI 图片" />
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(3, 'B', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(3, 'B', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 答题反馈 -->
              <div v-if="questionResults[3]" class="feedback-card" :class="{ 'correct': questionResults[3].correct, 'incorrect': !questionResults[3].correct }">
                <div class="feedback-icon">{{ questionResults[3].correct ? '🎉' : '💡' }}</div>
                <div class="feedback-message">{{ questionResults[3].message }}</div>
                <button @click="resetQuestion(3)" class="btn btn-outline">
                  再试一次
                </button>
              </div>
            </div>

            <!-- 第四题：论文语句 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">题目 4：论文语句（AI vs 优秀论文）</h3>
                <p class="question-description">请阅读两段文字，判断哪段是AI生成，哪段是人类优秀论文。</p>
              </div>
              
              <div class="case-study-options">
                <!-- A选项 -->
                <div class="option-card option-a">
                  <h4 class="option-label">A（AI 生成）</h4>
                  <div class="option-text">
                    <p>在现有的研究体系中，对于不确定性的探讨大多集中在宏观经济不确定性（Jurado et al., 2015）以及经济政策不确定性（Baker et al., 2016），而相较之下，金融市场层面的不确定性议题则明显被边缘化。这种现象在一定程度上反映了传统文献对于宏观经济变量影响力的过度强调，而对金融市场内生波动性的关注不足。</p>
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(4, 'A', 'ai')" class="btn btn-primary">
                      这是 AI
                    </button>
                    <button @click="answerQuestion(4, 'A', 'human')" class="btn btn-secondary">
                      这是真实
                    </button>
                  </div>
                </div>
                
                <!-- B选项 -->
                <div class="option-card option-b">
                  <h4 class="option-label">B（人类优秀论文）</h4>
                  <div class="option-text">
                    <p>目前，关于不确定性的研究主要集中在宏观经济不确定性(Jurado et al.，2015) 和经济政策不确定性(Baker et al., 2016) ，关于金融市场的不确定性关注较少。因为在传统文献中，金融市场主要受到宏观经济影响。</p>
                    <div class="text-source">引自：黄卓,邱晗,沈艳 & 童晨.(2018).测量中国的金融不确定性——基于大数据的方法.金融研究,(11),30-46.</div>
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(4, 'B', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(4, 'B', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 答题反馈 -->
              <div v-if="questionResults[4]" class="feedback-card" :class="{ 'correct': questionResults[4].correct, 'incorrect': !questionResults[4].correct }">
                <div class="feedback-icon">{{ questionResults[4].correct ? '🎉' : '💡' }}</div>
                <div class="feedback-message">{{ questionResults[4].message }}</div>
                <button @click="resetQuestion(4)" class="btn btn-outline">
                  再试一次
                </button>
              </div>
            </div>

            <!-- 第五题：猫咪视频 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">题目 5：小猫咪玩耍的视频</h3>
                <p class="question-description">请观看两段视频，判断哪段是真实拍摄，哪段是AI生成。</p>
              </div>
              
              <div class="case-study-options">
                <!-- A选项 -->
                <div class="option-card option-a">
                  <h4 class="option-label">A（真实视频）</h4>
                  <div class="option-content">
                    <div class="video-placeholder">
                      <div class="placeholder-icon">🎬</div>
                      <div class="placeholder-title">真实猫咪玩耍视频</div>
                      <div class="placeholder-desc">请观看视频判断</div>
                      <div class="placeholder-link">
                        <a href="https://www.bilibili.com/video/BV1n83tzdEsK" target="_blank" class="link-btn">观看原视频</a>
                      </div>
                    </div>
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(5, 'A', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(5, 'A', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
                
                <!-- B选项 -->
                <div class="option-card option-b">
                  <h4 class="option-label">B（AI视频）</h4>
                  <div class="option-content">
                    <div class="video-placeholder">
                      <div class="placeholder-icon">🎬</div>
                      <div class="placeholder-title">AI生成猫咪视频</div>
                      <div class="placeholder-desc">请观看视频判断</div>
                      <div class="placeholder-link">
                        <a href="https://jcni7655zeyq.feishu.cn/file/D1u0bKaTwoBRASxxJaicBsi9nid" target="_blank" class="link-btn">观看原视频</a>
                      </div>
                    </div>
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(5, 'B', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(5, 'B', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 答题反馈 -->
              <div v-if="questionResults[5]" class="feedback-card" :class="{ 'correct': questionResults[5].correct, 'incorrect': !questionResults[5].correct }">
                <div class="feedback-icon">{{ questionResults[5].correct ? '🎉' : '💡' }}</div>
                <div class="feedback-message">{{ questionResults[5].message }}</div>
                <button @click="resetQuestion(5)" class="btn btn-outline">
                  再试一次
                </button>
                          </div>

            <!-- 第六题：麻辣烫店铺 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">题目 6：麻辣烫店铺</h3>
                <p class="question-description">从两张图片中判断哪一张是真实拍摄，哪一张是 AI 生成。</p>
              </div>
              
              <div class="case-study-options">
                <!-- A选项 -->
                <div class="option-card option-a">
                  <h4 class="option-label">A（真实图片）</h4>
                  <div class="option-image">
                    <img src="/images/chapter1/C_真实图片.png" alt="A 真实图片" />
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(6, 'A', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(6, 'A', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
                
                <!-- B选项 -->
                <div class="option-card option-b">
                  <h4 class="option-label">B（AI 图片）</h4>
                  <div class="option-image">
                    <img src="/images/chapter1/C_ai生成.png" alt="B AI 图片" />
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(6, 'B', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(6, 'B', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 答题反馈 -->
              <div v-if="questionResults[6]" class="feedback-card" :class="{ 'correct': questionResults[6].correct, 'incorrect': !questionResults[6].correct }">
                <div class="feedback-icon">{{ questionResults[6].correct ? '🎉' : '💡' }}</div>
                <div class="feedback-message">{{ questionResults[6].message }}</div>
                <button @click="resetQuestion(6)" class="btn btn-outline">
                  再试一次
                </button>
              </div>
            </div>

            <!-- 第七题：沸腾火锅 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">题目 7：沸腾的火锅</h3>
                <p class="question-description">从两张图片中判断哪一张是真实拍摄，哪一张是 AI 生成。</p>
              </div>
              
              <div class="case-study-options">
                <!-- A选项 -->
                <div class="option-card option-a">
                  <h4 class="option-label">A（真实图片）</h4>
                  <div class="option-image">
                    <img src="/images/chapter1/D_真实图片.png" alt="A 真实图片" />
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(7, 'A', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(7, 'A', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
                
                <!-- B选项 -->
                <div class="option-card option-b">
                  <h4 class="option-label">B（AI 图片）</h4>
                  <div class="option-image">
                    <img src="/images/chapter1/D_ai生成.png" alt="B AI 图片" />
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(7, 'B', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(7, 'B', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 答题反馈 -->
              <div v-if="questionResults[7]" class="feedback-card" :class="{ 'correct': questionResults[7].correct, 'incorrect': !questionResults[7].correct }">
                <div class="feedback-icon">{{ questionResults[7].correct ? '🎉' : '💡' }}</div>
                <div class="feedback-message">{{ questionResults[7].message }}</div>
                <button @click="resetQuestion(7)" class="btn btn-outline">
                  再试一次
                </button>
              </div>
            </div>

            <!-- 第八题：哲学语句 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">题目 8：人类哲学家（黑格尔）vs AI 哲学家</h3>
                <p class="question-description">请阅读两段哲学语句，判断哪段是黑格尔原句，哪段是AI生成。</p>
              </div>
              
              <div class="case-study-options">
                <!-- A选项 -->
                <div class="option-card option-a">
                  <h4 class="option-label">A（黑格尔原句）</h4>
                  <div class="option-text">
                    <p>精神不是一个存在在外的东西，而是自我认识、自我实现的过程。</p>
                    <div class="text-source">黑格尔. (1979). 精神现象学 (贺麟 译). 商务印书馆. (原著发表于 1807)</div>
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(8, 'A', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(8, 'A', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
                
                <!-- B选项 -->
                <div class="option-card option-b">
                  <h4 class="option-label">B（AI 哲学语句）</h4>
                  <div class="option-text">
                    <p>意识不是孤立的旁观者，而是不断映照自身的镜子，通过反思获得存在的意义。</p>
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(8, 'B', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(8, 'B', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 答题反馈 -->
              <div v-if="questionResults[8]" class="feedback-card" :class="{ 'correct': questionResults[8].correct, 'incorrect': !questionResults[8].correct }">
                <div class="feedback-icon">{{ questionResults[8].correct ? '🎉' : '💡' }}</div>
                <div class="feedback-message">{{ questionResults[8].message }}</div>
                <button @click="resetQuestion(8)" class="btn btn-outline">
                  再试一次
                </button>
              </div>
            </div>

            <!-- 第九题：古典画作 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">题目 9：古典画作 vs AI 画作</h3>
                <p class="question-description">以1665年荷兰画家约翰内斯·维米尔《戴珍珠耳环的少女》为例。</p>
              </div>
              
              <div class="case-study-options">
                <!-- A选项 -->
                <div class="option-card option-a">
                  <h4 class="option-label">A（真实画作）</h4>
                  <div class="option-image">
                    <img src="/images/chapter1/E_真实图片.png" alt="A 真实画作" />
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(9, 'A', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(9, 'A', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
                
                <!-- B选项 -->
                <div class="option-card option-b">
                  <h4 class="option-label">B（AI 画作）</h4>
                  <div class="option-image">
                    <img src="/images/chapter1/E_ai生成.png" alt="B AI 画作" />
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(9, 'B', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(9, 'B', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 答题反馈 -->
              <div v-if="questionResults[9]" class="feedback-card" :class="{ 'correct': questionResults[9].correct, 'incorrect': !questionResults[9].correct }">
                <div class="feedback-icon">{{ questionResults[9].correct ? '🎉' : '💡' }}</div>
                <div class="feedback-message">{{ questionResults[9].message }}</div>
                <button @click="resetQuestion(9)" class="btn btn-outline">
                  再试一次
                </button>
              </div>
            </div>

            <!-- 第十题：广告视频 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">题目 10：真实广告 vs AI 广告</h3>
                <p class="question-description">以下为可口可乐公司发布的两则广告，一个为真实拍摄，一个为AI制作。</p>
              </div>
              
              <div class="case-study-options">
                <!-- A选项 -->
                <div class="option-card option-a">
                  <h4 class="option-label">A（真实拍摄）</h4>
                  <div class="option-content">
                    <div class="video-placeholder">
                      <div class="placeholder-icon">🎬</div>
                      <div class="placeholder-title">真实可口可乐广告</div>
                      <div class="placeholder-desc">请观看视频判断</div>
                      <div class="placeholder-link">
                        <a href="https://www.youtube.com/watch?v=6wtxogfPieA" target="_blank" class="link-btn">观看原视频</a>
                      </div>
                    </div>
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(10, 'A', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(10, 'A', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
                
                <!-- B选项 -->
                <div class="option-card option-b">
                  <h4 class="option-label">B（AI制作）</h4>
                  <div class="option-content">
                    <div class="video-placeholder">
                      <div class="placeholder-icon">🎬</div>
                      <div class="placeholder-title">AI制作可口可乐广告</div>
                      <div class="placeholder-desc">请观看视频判断</div>
                      <div class="placeholder-link">
                        <a href="https://www.youtube.com/watch?v=4RSTupbfGog" target="_blank" class="link-btn">观看原视频</a>
                      </div>
                    </div>
                  </div>
                  <div class="option-actions">
                    <button @click="answerQuestion(10, 'B', 'human')" class="btn btn-primary">
                      这是真实
                    </button>
                    <button @click="answerQuestion(10, 'B', 'ai')" class="btn btn-secondary">
                      这是 AI
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 答题反馈 -->
              <div v-if="questionResults[10]" class="feedback-card" :class="{ 'correct': questionResults[10].correct, 'incorrect': !questionResults[10].correct }">
                <div class="feedback-icon">{{ questionResults[10].correct ? '🎉' : '💡' }}</div>
                <div class="feedback-message">{{ questionResults[10].message }}</div>
                <button @click="resetQuestion(10)" class="btn btn-outline">
                  再试一次
                </button>
              </div>
            </div>
          </div>
          
          <!-- 题目概览 -->
          <div class="case-study-overview">
              <h3 class="overview-title">📚 第一章完整练习概览</h3>
              <p class="overview-description">总共 10 道题目，涵盖图片、音频、视频和文本等多种类型</p>
              
              <div class="overview-list">
                <h4>题目列表：</h4>
                <ul>
                  <li>题目 1：雪纺裙的图片</li>
                  <li>题目 2：音频对比</li>
                  <li>题目 3：城市街景</li>
                  <li>题目 4：智慧农业</li>
                  <li>题目 5：猫咪视频</li>
                  <li>题目 6：麻辣烫店铺</li>
                  <li>题目 7：沸腾火锅</li>
                  <li>题目 8：哲学语句</li>
                  <li>题目 9：古典画作</li>
                  <li>题目 10：广告视频</li>
                </ul>
              </div>
            </div>
          </div>
          
          <!-- 第一章测验模式 -->
          <div v-if="isChapter1 && showChapter1Quiz" class="case-study-section">
            <div class="case-study-header">
              <h2 class="case-study-title">🎯 第一章测验模式</h2>
              <p class="case-study-description">测验模式：所有题目同时显示，可以自由答题和查看结果</p>
              <button @click="showChapter1Quiz = false" class="btn btn-outline">
                ← 返回案例演示模式
              </button>
            </div>
            
            <!-- 测验模式下的所有题目 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">🎯 测验说明</h3>
                <p class="question-description">请仔细阅读每个题目，选择你认为正确的答案。答题后可以查看结果和解释。</p>
              </div>
            </div>
            
            <!-- 这里可以添加测验模式下的题目显示逻辑 -->
            <div class="case-study-card">
              <div class="case-study-question">
                <h3 class="question-title">📊 测验进度</h3>
                <p class="question-description">已完成题目：{{ Object.keys(questionResults).filter(id => questionResults[id]).length }}/10</p>
                <div class="progress-bar">
                  <div class="progress-fill" :style="{ width: (Object.keys(questionResults).filter(id => questionResults[id]).length / 10 * 100) + '%' }"></div>
                </div>
              </div>
            </div>
          </div>
          <!-- 第六章特殊处理：显示交互式内容 -->
          <div v-if="isChapter6" class="interactive-chapter">
            <Chapter6Interactive />
          </div>
          <!-- 其他章节显示普通内容 -->
          <div v-else class="content-text" v-html="formatContent(chapter.content)"></div>
        </div>

        <!-- 第二章案例学习 -->
        <div v-if="isChapter2" class="case-study-section">
          <Chapter2CaseStudy 
            :chapter-id="id" 
            @case-completed="onCaseCompleted"
            @all-cases-completed="onAllCasesCompleted"
          />
        </div>

        <!-- 第三章案例学习 -->
        <div v-if="isChapter3" class="case-study-section">
          <Chapter3CaseStudy 
            :chapter-id="id" 
            @case-completed="onCaseCompleted"
            @all-cases-completed="onAllCasesCompleted"
          />
        </div>

        <!-- 第四章案例学习 -->
        <div v-if="isChapter4" class="case-study-section">
          <Chapter4CaseStudy 
            :chapter-id="id" 
            @case-completed="onCaseCompleted"
            @all-cases-completed="onAllCasesCompleted"
          />
        </div>

        <!-- 第七章案例学习 -->
        <div v-if="isChapter7" class="case-study-section">
          <Chapter7CaseStudy 
            :chapter-id="id" 
            @case-completed="onCaseCompleted"
            @all-cases-completed="onAllCasesCompleted"
          />
        </div>

        <!-- 答题系统入口 -->
        <div class="quiz-section">
          <div class="quiz-header">
            <h3 class="quiz-title">📚 知识测验</h3>
            <p class="quiz-description">
              {{ (isChapter2 || isChapter3 || isChapter4 || isChapter7) ? '完成上述案例学习后，可以参加知识测验来检验学习成果' : '完成本章节的学习后，可以参加知识测验来检验学习成果' }}
            </p>
          </div>
          <div class="quiz-actions">
            <button 
              @click="startQuiz" 
              class="btn btn-quiz"
              :disabled="(isChapter2 || isChapter3 || isChapter4 || isChapter7) && !allCasesCompleted"
              :class="{ disabled: (isChapter2 || isChapter3 || isChapter4 || isChapter7) && !allCasesCompleted }"
            >
              <span class="btn-icon">🎯</span>
              <span class="btn-text">
                {{ (isChapter2 || isChapter3 || isChapter4 || isChapter7) && !allCasesCompleted ? '请先完成案例学习' : '开始测验' }}
              </span>
            </button>
            <div class="quiz-info">
              <span class="info-item">
                <span class="info-icon">⏱️</span>
                <span class="info-text">预计用时: 10-15分钟</span>
              </span>
              <span class="info-item">
                <span class="info-icon">🏆</span>
                <span class="info-text">可获得奖励和成就</span>
              </span>
              <span v-if="isChapter2" class="info-item">
                <span class="info-icon">✅</span>
                <span class="info-text">案例完成进度: {{ completedCasesCount }}/2</span>
              </span>
              <span v-if="isChapter3" class="info-item">
                <span class="info-icon">✅</span>
                <span class="info-text">案例完成进度: {{ allCasesCompleted ? '1/1' : '0/1' }}</span>
              </span>
              <span v-if="isChapter4" class="info-item">
                <span class="info-icon">✅</span>
                <span class="info-text">案例完成进度: {{ allCasesCompleted ? '1/1' : '0/1' }}</span>
              </span>
            </div>
          </div>
        </div>

        <!-- 章节导航 -->
        <nav class="chapter-pagination">
          <button 
            v-if="prevChapter" 
            @click="goToChapter(prevChapter.id)"
            class="pagination-btn prev"
          >
            <span class="btn-label">上一章</span>
            <span class="btn-title">{{ prevChapter.title }}</span>
          </button>
          <button 
            v-if="nextChapter" 
            @click="goToChapter(nextChapter.id)"
            class="pagination-btn next"
          >
            <span class="btn-label">下一章</span>
            <span class="btn-title">{{ nextChapter.title }}</span>
          </button>
        </nav>
      </article>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>正在加载章节内容...</p>
      </div>

      <!-- 错误状态 -->
      <div v-if="error" class="error-container">
        <div class="error-icon">⚠️</div>
        <h3>加载失败</h3>
        <p>{{ error }}</p>
        <button @click="loadChapter" class="btn btn-primary">重试</button>
      </div>
    </div>
  </div>
</template>

<script>
import { chapterApi } from '../services/api'
import Chapter1Demo from '../components/chapter1/Chapter1Demo.vue'
import Chapter6Interactive from '../components/chapter6/Chapter6Interactive.vue'
import Chapter2CaseStudy from '../components/chapter2/Chapter2CaseStudy.vue'
import Chapter3CaseStudy from '../components/chapter3/Chapter3CaseStudy.vue'
import Chapter4CaseStudy from '../components/chapter4/Chapter4CaseStudy.vue'
import Chapter7CaseStudy from '../components/chapter7/Chapter7CaseStudy.vue'

export default {
  name: 'ChapterDetail',
  components: {
    Chapter1Demo,
    Chapter6Interactive,
    Chapter2CaseStudy,
    Chapter3CaseStudy,
    Chapter4CaseStudy,
    Chapter7CaseStudy
  },
  props: {
    id: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      chapter: null,
      allChapters: [],
      loading: true,
      error: null,
      completedCasesCount: 0,
      allCasesCompleted: false,
      showChapter1Quiz: false, // 控制第一章测验显示状态，默认显示案例演示
      questionResults: {},
      questionAnswers: {
        1: { A: 'human', B: 'ai' },
        2: { A: 'human', B: 'ai' },
        3: { A: 'human', B: 'ai' },
        4: { A: 'ai', B: 'human' },
        5: { A: 'human', B: 'ai' },
        6: { A: 'human', B: 'ai' },
        7: { A: 'human', B: 'ai' },
        8: { A: 'human', B: 'ai' },
        9: { A: 'human', B: 'ai' },
        10: { A: 'human', B: 'ai' }
      }
    }
  },
  computed: {
    isChapter6() {
      return this.chapter && (
        this.chapter.chapterNumber === '6' ||
        this.chapter.title.includes('第一个人工智能项目') ||
        this.id === '6'
      )
    },
    isChapter2() {
      return this.chapter && (
        this.chapter.chapterNumber === '2' ||
        this.chapter.title.includes('机器学习基础') ||
        this.id === '2'
      )
    },
    isChapter1() {
      return this.chapter && (
        this.chapter.chapterNumber === '1' ||
        this.chapter.title.includes('人工智能概论') ||
        this.id === '1'
      )
    },
    isChapter3() {
      return this.chapter && (
        this.chapter.chapterNumber === '3' ||
        this.chapter.title.includes('图像识别') ||
        this.chapter.title.includes('计算机视觉') ||
        this.id === '3'
      )
    },
    isChapter4() {
      return this.chapter && (
        this.chapter.chapterNumber === '4' ||
        this.chapter.title.includes('人工智能应用') ||
        this.chapter.title.includes('智慧生活') ||
        this.chapter.title.includes('智慧驾驶') ||
        this.chapter.title.includes('智慧医疗') ||
        this.id === '4'
      )
    },
    isChapter7() {
      return this.chapter && (
        this.chapter.chapterNumber === '7' ||
        this.chapter.title.includes('人工智能的思考') ||
        this.chapter.title.includes('伦理') ||
        this.chapter.title.includes('算法歧视') ||
        this.id === '7'
      )
    },
    prevChapter() {
      if (!this.chapter || !this.allChapters.length) return null
      const currentIndex = this.allChapters.findIndex(ch => ch.id === this.chapter.id)
      return currentIndex > 0 ? this.allChapters[currentIndex - 1] : null
    },
    nextChapter() {
      if (!this.chapter || !this.allChapters.length) return null
      const currentIndex = this.allChapters.findIndex(ch => ch.id === this.chapter.id)
      return currentIndex < this.allChapters.length - 1 ? this.allChapters[currentIndex + 1] : null
    }
  },
  async mounted() {
    await this.loadChapter()
    await this.loadAllChapters()
    
    // 确保第一章默认显示案例演示模式
    if (this.isChapter1) {
      this.showChapter1Quiz = false
    }
  },
  watch: {
    id: {
      handler() {
        this.loadChapter()
        // 路由变化时重置第一章测验状态
        if (this.isChapter1) {
          this.showChapter1Quiz = false
        }
      }
    }
  },
  methods: {
    async loadChapter() {
      this.loading = true
      this.error = null

      try {
        const response = await chapterApi.getChapterById(this.id)
        this.chapter = response.data
        console.log('加载章节详情成功:', this.chapter)
      } catch (error) {
        console.error('加载章节详情失败:', error)
        if (error.message && error.message.includes('章节不存在')) {
          this.error = '章节不存在'
        } else {
          this.error = error.message || '网络连接失败，请检查后端服务是否启动'
        }
      } finally {
        this.loading = false
      }
    },
    async loadAllChapters() {
      try {
        const response = await chapterApi.getChapterOverview()
        this.allChapters = response.data || []
      } catch (error) {
        console.error('加载章节列表失败:', error)
      }
    },
    goBack() {
      this.$router.push('/chapters')
    },
    goToChapter(id) {
      this.$router.push(`/chapters/${id}`)
    },
    startQuiz() {
      // 第一章特殊处理：在当前页面显示测验，不跳转
      if (this.isChapter1) {
        this.showChapter1Quiz = true
        this.$message({
          message: '🎯 第一章测验已开始！请完成所有题目。',
          type: 'success',
          duration: 3000
        })
        return
      }
      
      // 其他章节：检查案例完成情况
      if ((this.isChapter2 || this.isChapter3 || this.isChapter4) && !this.allCasesCompleted) {
        this.$message({
          message: '请先完成所有案例学习后再开始测验',
          type: 'warning',
          duration: 3000
        })
        return
      }
      
      // 跳转到答题页面
      this.$router.push(`/quiz/${this.id}`)
    },
    
    // 第一章案例演示答题方法
          answerQuestion(questionId, option, guess) {
        const correctAnswer = this.questionAnswers[questionId][option]
        const isCorrect = guess === correctAnswer
        
        // Vue 3 兼容性：直接赋值即可，无需 $set
        this.questionResults[questionId] = {
          correct: isCorrect,
          message: isCorrect 
            ? '🎉 恭喜你，答对了，很厉害哦！' 
            : `💡 不好意思，答错了。标准答案：A 为 ${this.questionAnswers[questionId].A === 'human' ? '真实' : 'AI'}，B 为 ${this.questionAnswers[questionId].B === 'human' ? '真实' : 'AI'}。`
        }
      },
      
      resetQuestion(questionId) {
        // Vue 3 兼容性：直接赋值即可，无需 $set
        this.questionResults[questionId] = null
      },
    
    onCaseCompleted(caseId) {
      this.completedCasesCount++
      this.$message({
        message: `案例 ${caseId} 完成！`,
        type: 'success',
        duration: 2000
      })
    },
    
    onAllCasesCompleted() {
      this.allCasesCompleted = true
      this.$message({
        message: '🎉 恭喜！所有案例学习已完成，现在可以开始测验了！',
        type: 'success',
        duration: 3000
      })
    },
    getChapterType(type) {
      const typeMap = {
        'prologue': '序章',
        'chapter': '正章',
        'appendix': '附录'
      }
      return typeMap[type] || '章节'
    },
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleDateString('zh-CN')
    },
    formatContent(content) {
      if (!content) return ''
      // 简单的文本格式化，将换行转换为段落
      return content
        .split('\n\n')
        .map(paragraph => `<p>${paragraph.trim()}</p>`)
        .join('')
    },
    

  }
}
</script>

<style lang="scss" scoped>
@use '../styles/variables.scss' as *;

/* 第一章案例演示样式 */
.case-study-section {
  margin: $spacing-lg 0;
}

.case-study-header {
  text-align: center;
  margin-bottom: $spacing-lg;
  padding: $spacing-lg;
  background: linear-gradient(135deg, $primary-color 0%, $primary-gradient-end 100%);
  border-radius: $card-radius;
  color: $text-color;
  box-shadow: $card-shadow;
  border: 1px solid $card-border;
}

.case-study-title {
  font-size: $font-size-3xl;
  font-weight: 700;
  margin-bottom: $spacing-md;
  color: $text-color;
}

.case-study-description {
  font-size: $font-size-lg;
  opacity: 0.9;
  color: $text-secondary-color;
}

.case-study-card {
  background: $card-bg;
  border-radius: $card-radius;
  padding: $spacing-lg;
  margin-bottom: $spacing-lg;
  box-shadow: $card-shadow;
  border: 1px solid $card-border;
  transition: all $transition-normal ease;
}

.case-study-card:hover {
  transform: translateY(-2px);
  box-shadow: $shadow-lg;
}

.case-study-question {
  text-align: center;
  margin-bottom: $spacing-lg;
}

.question-title {
  font-size: $font-size-2xl;
  font-weight: 600;
  color: $text-color;
  margin-bottom: $spacing-md;
}

.question-description {
  color: $text-secondary-color;
  font-size: $font-size-lg;
}

.case-study-options {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: $spacing-lg;
  margin-bottom: $spacing-lg;
}

.option-card {
  background: $secondary-color;
  border-radius: $form-radius;
  padding: $spacing-md;
  border: 2px solid $card-border;
  transition: all $transition-normal ease;
}

.option-card:hover {
  border-color: $accent-color;
  transform: translateY(-2px);
  box-shadow: $shadow-md;
}

.option-a {
  border-left: 4px solid $success-color;
}

.option-b {
  border-left: 4px solid $error-color;
}

.option-label {
  font-size: $font-size-lg;
  font-weight: 600;
  color: $text-color;
  margin-bottom: $spacing-md;
  text-align: center;
}

.option-image {
  margin-bottom: $spacing-md;
  text-align: center;
}

.option-image img {
  max-width: 100%;
  height: auto;
  border-radius: $form-radius;
  box-shadow: $shadow-sm;
}

.option-content {
  margin-bottom: $spacing-md;
}

.option-text {
  background: $card-bg;
  padding: $spacing-md;
  border-radius: $form-radius;
  border: 1px solid $card-border;
  margin-bottom: $spacing-md;
}

.option-text p {
  color: $text-color;
  line-height: 1.6;
  margin: 0;
}

.text-source {
  font-size: $font-size-sm;
  color: $text-secondary-color;
  font-style: italic;
  margin-top: $spacing-sm;
  padding-top: $spacing-sm;
  border-top: 1px solid $card-border;
}

.audio-placeholder, .video-placeholder {
  background: $secondary-color;
  border: 2px dashed $accent-color;
  border-radius: $form-radius;
  padding: $spacing-lg $spacing-md;
  text-align: center;
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.placeholder-icon {
  font-size: $font-size-3xl;
  margin-bottom: $spacing-sm;
}

.placeholder-title {
  font-weight: 600;
  color: $text-color;
  margin-bottom: $spacing-sm;
}

.placeholder-desc {
  color: $text-secondary-color;
  font-size: $font-size-base;
  margin-bottom: $spacing-md;
}

.placeholder-link {
  margin-top: auto;
}

.link-btn {
  display: inline-block;
  background: $btn-primary-bg;
  color: $text-color;
  padding: $spacing-sm $spacing-md;
  border-radius: $btn-radius;
  text-decoration: none;
  font-size: $font-size-base;
  transition: all $transition-normal ease;
  box-shadow: $btn-shadow;
}

.link-btn:hover {
  background: $primary-hover-color;
  color: $text-color;
  text-decoration: none;
  transform: translateY(-1px);
}

.option-actions {
  display: flex;
  gap: $spacing-sm;
  justify-content: center;
}

.btn {
  padding: $spacing-sm $spacing-md;
  border: none;
  border-radius: $btn-radius;
  font-weight: 500;
  cursor: pointer;
  transition: all $transition-normal ease;
  font-size: $font-size-base;
  box-shadow: $btn-shadow;
}

.btn-primary {
  background: $btn-primary-bg;
  color: $text-color;
}

.btn-primary:hover {
  background: $primary-hover-color;
  transform: translateY(-1px);
  box-shadow: $shadow-md;
}

.btn-secondary {
  background: $btn-secondary-bg;
  color: $text-color;
}

.btn-secondary:hover {
  background: $btn-secondary-hover;
  transform: translateY(-1px);
  box-shadow: $shadow-md;
}

.btn-outline {
  background: transparent;
  color: $accent-color;
  border: 1px solid $accent-color;
}

.btn-outline:hover {
  background: $accent-color;
  color: $text-color;
  transform: translateY(-1px);
}

.feedback-card {
  text-align: center;
  padding: $spacing-md;
  border-radius: $form-radius;
  margin-top: $spacing-md;
  border: 1px solid $card-border;
}

.feedback-card.correct {
  background: rgba($success-color, 0.1);
  color: $success-color;
  border-color: $success-color;
}

.feedback-card.incorrect {
  background: rgba($error-color, 0.1);
  color: $error-color;
  border-color: $error-color;
}

.feedback-icon {
  font-size: $font-size-3xl;
  margin-bottom: $spacing-md;
}

.feedback-message {
  font-size: $font-size-lg;
  margin-bottom: $spacing-md;
}

.case-study-overview {
  background: $secondary-color;
  border-radius: $card-radius;
  padding: $spacing-lg;
  border: 1px solid $card-border;
  box-shadow: $card-shadow;
}

.overview-title {
  font-size: $font-size-2xl;
  font-weight: 600;
  color: $text-color;
  margin-bottom: $spacing-md;
  text-align: center;
}

.overview-description {
  color: $text-secondary-color;
  text-align: center;
  margin-bottom: $spacing-md;
}

.overview-list {
  background: $card-bg;
  padding: $spacing-md;
  border-radius: $form-radius;
  border: 1px solid $card-border;
}

.overview-list h4 {
  color: $text-color;
  margin-bottom: $spacing-md;
  font-weight: 600;
}

.overview-list ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.overview-list li {
  padding: $spacing-sm 0;
  border-bottom: 1px solid $card-border;
  color: $text-secondary-color;
  font-size: $font-size-base;
  transition: all $transition-normal ease;
}

.overview-list li:last-child {
  border-bottom: none;
}

.overview-list li:hover {
  color: $text-color;
  background: $list-item-hover-bg;
  padding-left: $spacing-sm;
  border-radius: $form-radius;
}

@media (max-width: 768px) {
  .case-study-options {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .case-study-header {
    padding: 1.5rem;
  }
  
  .case-study-title {
    font-size: 1.5rem;
  }
  
  .case-study-card {
    padding: 1.5rem;
  }
}

.chapter-detail {
  background: $secondary-color;
  min-height: 100vh;
  padding: 2rem 0;
}

.chapter-nav {
  background: $nav-bg;
  border-bottom: 1px solid $nav-border;
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: $nav-shadow;
  backdrop-filter: blur(4px);
}

.nav-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-back {
  background: none;
  border: none;
  color: $accent-color;
  cursor: pointer;
  font-size: 0.95rem;
  padding: 0.5rem 0;
  transition: color 0.2s;
  font-weight: 600;
  letter-spacing: 0.5px;
  
  &:hover {
    color: $nav-back-hover;
  }
}

.nav-title {
  font-weight: 700;
  color: $text-color;
  font-size: 1.05rem;
  letter-spacing: 1px;
}

.container {
  max-width: 900px;
  margin: 0 auto;
  background: $card-bg;
  border-radius: $card-radius;
  box-shadow: $card-shadow;
  padding: 2.5rem 2rem;
  border: 1px solid $card-border;
}

.chapter-content {
  color: $text-color;
  font-size: 1.08rem;
  line-height: 1.8;
  margin-bottom: 2.5rem;
}

.chapter-header {
  display: flex;
  align-items: center;
  gap: 1.2rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.chapter-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.chapter-badge {
  background: $chapter-badge-bg;
  color: $accent-color;
  padding: 0.5rem 1.2rem;
  border-radius: 20px;
  font-size: 1rem;
  font-weight: 600;
  box-shadow: $chapter-badge-shadow;
  letter-spacing: 1px;
}

.chapter-type {
  color: $accent-color;
  background: $chapter-type-bg;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.95rem;
  font-weight: 600;
}

.chapter-title {
  font-size: 2rem;
  color: $text-color;
  font-weight: 900;
  letter-spacing: 1px;
}

.chapter-summary {
  color: $text-secondary-color;
  font-size: 1.1rem;
  margin-bottom: 2rem;
  line-height: 1.7;
}

.chapter-info {
  display: flex;
  gap: 2rem;
  font-size: 0.95rem;
}

.info-item {
  display: flex;
  gap: 0.5rem;
}

.info-label {
  color: $accent-color;
}

.info-value {
  color: $text-color;
  font-weight: 600;
  
  &.published {
    color: $accent-color;
  }
}

.chapter-body {
  margin-bottom: 3rem;
}

.content-text {
  font-size: 1.1rem;
  line-height: 1.8;
  color: $text-color;
  
  :deep(p) {
    margin-bottom: 1.5rem;
  }
}

.chapter-pagination {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  padding-top: 2rem;
  border-top: 1px solid $card-header-border;
}

.quiz-section {
  background: $secondary-color;
  border-radius: $card-radius;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: $card-shadow;
  border: 1px solid $card-border;
}

.quiz-header {
  margin-bottom: 1.5rem;
}

.quiz-title {
  font-size: 1.4rem;
  color: $text-color;
  margin-bottom: 0.5rem;
  font-weight: 700;
}

.quiz-description {
  color: $text-secondary-color;
  font-size: 1rem;
  line-height: 1.6;
}

.quiz-actions {
  display: flex;
  align-items: center;
  gap: 2rem;
  flex-wrap: wrap;
}

.btn-quiz {
  background: $btn-primary-bg;
  color: $text-color;
  border: none;
  border-radius: $btn-radius;
  padding: 1rem 2rem;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  
  &:hover:not(.disabled) {
    transform: translateY(-2px);
    box-shadow: $btn-shadow;
  }
  
  &.disabled {
    background: #666;
    color: #999;
    cursor: not-allowed;
    opacity: 0.6;
  }
}

.btn-icon {
  font-size: 1.2rem;
}

.btn-text {
  font-weight: 600;
}

.quiz-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.quiz-info .info-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: $accent-color;
  font-size: 0.9rem;
}

.info-icon {
  font-size: 1rem;
}

.pagination-btn {
  background: $chapter-pagination-bg;
  border: 1px solid $card-border;
  border-radius: $btn-radius;
  padding: 1rem;
  cursor: pointer;
  transition: box-shadow 0.18s, background 0.18s, border 0.18s;
  flex: 1;
  max-width: 300px;
  color: $accent-color;
  font-weight: 600;
  font-size: 1rem;
  
  &:hover {
    background: $chapter-pagination-hover;
    border-color: $chapter-pagination-hover-border;
    box-shadow: $chapter-pagination-hover-shadow;
    color: $text-color;
  }
  
  &.prev {
    text-align: left;
  }
  
  &.next {
    text-align: right;
    margin-left: auto;
  }
}

.btn-label {
  display: block;
  font-size: 0.8rem;
  color: $accent-color;
  margin-bottom: 0.25rem;
}

.btn-title {
  display: block;
  font-weight: 700;
  color: $text-color;
}

.loading-container, .error-container {
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

.error-icon {
  font-size: $error-icon-size;
  margin-bottom: 1rem;
}

.error-container h3 {
  color: $text-color;
  margin-bottom: 0.5rem;
}

.error-container p {
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
}

.btn-primary {
  background: $primary-color;
  color: $text-color;
  border: none;
  
  &:hover {
    background: $primary-hover-color;
    color: $text-secondary-color;
    border-color: $primary-hover-color;
    transform: translateY(-1px);
  }
}

.back-link {
  color: $accent-color;
  font-size: 1rem;
  text-decoration: underline;
  cursor: pointer;
  margin-right: 1.5rem;
}

@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .chapter-title {
    font-size: 1.3rem;
  }
  
  .chapter-info {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .chapter-pagination {
    flex-direction: column;
  }
  
  .pagination-btn {
    max-width: none;
    text-align: center;
  }
  
  .container {
    padding: 1.2rem 0.5rem;
  }
  
  /* 第一章案例演示响应式样式 */
  .case-study-options {
    grid-template-columns: 1fr;
    gap: $spacing-md;
  }
  
  .case-study-header {
    padding: $spacing-md;
  }
  
  .case-study-title {
    font-size: $font-size-xl;
  }
  
  .case-study-card {
    padding: $spacing-md;
  }
  
  .question-title {
    font-size: $font-size-lg;
  }
  
  .option-actions {
    flex-direction: column;
    gap: $spacing-sm;
  }
  
  .btn {
    width: 100%;
    padding: $spacing-md;
  }
}

/* 进度条样式 */
.progress-bar {
  width: 100%;
  height: 12px;
  background: $secondary-color;
  border-radius: 6px;
  overflow: hidden;
  margin-top: $spacing-md;
  border: 1px solid $card-border;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, $success-color, $success-color-light);
  border-radius: 6px;
  transition: width $transition-normal ease;
  box-shadow: 0 2px 8px rgba($success-color, 0.3);
}
</style>
