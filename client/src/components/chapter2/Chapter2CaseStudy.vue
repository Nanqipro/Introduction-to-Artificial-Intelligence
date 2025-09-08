<template>
  <div class="chapter2-case-study">
    <div class="case-header">
      <h2 class="case-title">🎯 第二章案例学习</h2>
      <p class="case-description">
        通过交互式案例深入理解机器学习基础和深度学习核心概念
      </p>

      <!-- 学习进度指示器 -->
      <div class="progress-indicator">
        <div
          class="progress-item"
          :class="{ completed: regressionCaseCompleted }"
        >
          <div class="progress-icon">📊</div>
          <div class="progress-info">
            <h4>回归分析案例</h4>
            <p>需要生成数据 {{ interactionCounts.regressionGenerate }}/3 次</p>
            <div class="progress-status">
              <span v-if="regressionCaseCompleted" class="status-completed"
                >✅ 已完成</span
              >
              <span v-else class="status-pending">⏳ 进行中</span>
            </div>
       </div>
       
       <!-- 图片放大遮罩层 -->
       <div class="image-overlay" @click="closeEnlargedImage"></div>
     </div>

        <div
          class="progress-item"
          :class="{ completed: deepLearningCaseCompleted }"
        >
          <div class="progress-icon">🧠</div>
          <div class="progress-info">
            <h4>深度学习核心层</h4>
            <p>观看动画演示，理解核心概念</p>
            <div class="progress-status">
              <span v-if="deepLearningCaseCompleted" class="status-completed"
                >✅ 已完成</span
              >
              <span v-else class="status-pending">⏳ 进行中</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 回归演示案例 - 重新设计的现代化平台 -->
    <div class="case-section">
      <div class="section-header">
        <h3 class="section-title">
          <span class="title-icon">📊</span>
          <span class="title-text">回归演示学习平台</span>
          <span class="title-decoration"></span>
        </h3>
        <p class="section-subtitle">探索机器学习中最基础且重要的回归算法</p>
      </div>

      <!-- 现代化回归演示平台 -->
      <div class="modern-platform-card">
        <!-- 平台头部 -->
        <div class="platform-header">
          <div class="header-content">
            <div class="platform-logo">
              <div class="logo-circle">
                <span class="logo-icon">🎯</span>
              </div>
              <div class="logo-text">
                <h4>AI回归实验室</h4>
                <span class="logo-subtitle">Interactive Regression Lab</span>
              </div>
            </div>
            <div class="platform-status">
              <div class="status-indicator active">
                <span class="status-dot"></span>
                <span class="status-text">平台在线</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 算法展示区 -->
        <div class="algorithms-showcase">
          <div class="showcase-header">
            <h5>🧮 核心算法模块</h5>
            <p>两大经典回归算法，从理论到实践的完整学习体验</p>
          </div>

          <div class="algorithm-grid">
            <!-- 线性回归卡片 -->
            <div class="algorithm-card linear-regression">
              <div class="card-background">
                <div class="bg-pattern"></div>
              </div>
              <div class="card-content">
                <div class="algorithm-icon">
                  <span class="icon-symbol">📈</span>
                  <div class="icon-glow"></div>
                </div>
                <div class="algorithm-info">
                  <h6 class="algorithm-title">线性回归</h6>
                  <p class="algorithm-desc">Linear Regression</p>
                  <div class="algorithm-tags">
                    <span class="tag">连续预测</span>
                    <span class="tag">最小二乘</span>
                  </div>
                </div>
              </div>
              <div class="card-features">
                <div class="feature-list">
                  <div class="feature-item">
                    <span class="feature-dot"></span>
                    <span>梯度下降优化</span>
                  </div>
                  <div class="feature-item">
                    <span class="feature-dot"></span>
                    <span>R²性能评估</span>
                  </div>
                  <div class="feature-item">
                    <span class="feature-dot"></span>
                    <span>正则化技术</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- 逻辑回归卡片 -->
            <div class="algorithm-card logistic-regression">
              <div class="card-background">
                <div class="bg-pattern"></div>
              </div>
              <div class="card-content">
                <div class="algorithm-icon">
                  <span class="icon-symbol">📊</span>
                  <div class="icon-glow"></div>
                </div>
                <div class="algorithm-info">
                  <h6 class="algorithm-title">逻辑回归</h6>
                  <p class="algorithm-desc">Logistic Regression</p>
                  <div class="algorithm-tags">
                    <span class="tag">分类问题</span>
                    <span class="tag">Sigmoid</span>
                  </div>
                </div>
              </div>
              <div class="card-features">
                <div class="feature-list">
                  <div class="feature-item">
                    <span class="feature-dot"></span>
                    <span>最大似然估计</span>
                  </div>
                  <div class="feature-item">
                    <span class="feature-dot"></span>
                    <span>ROC曲线分析</span>
                  </div>
                  <div class="feature-item">
                    <span class="feature-dot"></span>
                    <span>决策边界可视化</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 平台特性展示 -->
        <div class="platform-highlights">
          <div class="highlights-grid">
            <div class="highlight-item">
              <div class="highlight-icon">
                <span>🎮</span>
              </div>
              <div class="highlight-content">
                <h6>交互式学习</h6>
                <p>实时调参，即时反馈</p>
              </div>
            </div>
            <div class="highlight-item">
              <div class="highlight-icon">
                <span>📊</span>
              </div>
              <div class="highlight-content">
                <h6>数据可视化</h6>
                <p>直观图表，深度理解</p>
              </div>
            </div>
            <div class="highlight-item">
              <div class="highlight-icon">
                <span>🧪</span>
              </div>
              <div class="highlight-content">
                <h6>算法实验</h6>
                <p>参数对比，效果分析</p>
              </div>
            </div>
            <div class="highlight-item">
              <div class="highlight-icon">
                <span>⚡</span>
              </div>
              <div class="highlight-content">
                <h6>实时计算</h6>
                <p>毫秒级响应，流畅体验</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 启动按钮区域 -->
        <div class="platform-launch">
          <div class="launch-content">
            <div class="launch-info">
              <h5>🚀 开始您的回归学习之旅</h5>
              <p>线性回归与逻辑回归交互式演示实验室已准备就绪，包含数据生成、模型训练和可视化</p>
            </div>
            <button @click="redirectToRegressionPlatform" class="launch-button">
              <div class="button-background">
                <div class="bg-gradient"></div>
                <div class="bg-shine"></div>
              </div>
              <div class="button-content">
                <span class="button-icon">🎯</span>
                <span class="button-text">进入回归实验室</span>
                <span class="button-arrow">→</span>
              </div>
            </button>
          </div>
          <div class="launch-note">
            <div class="note-icon">💡</div>
            <p>在回归实验室中，您可以生成数据、训练模型、观察拟合效果，为后续测验做准备</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 深度学习核心层展示 -->
    <div class="case-section">
      <div class="section-header">
        <h3 class="section-title">
          <span class="title-icon">🧠</span>
          <span class="title-text">深度学习核心层</span>
          <span class="title-decoration"></span>
        </h3>
        <p class="section-subtitle">通过动画演示理解深度学习网络的核心组件</p>
      </div>

      <!-- 深度学习三大核心层展示平台 -->
      <div class="deep-learning-showcase">
        <div class="core-layers-grid">
          <!-- 卷积层 -->
          <div class="layer-card">
            <div class="layer-header">
              <span class="layer-icon">🔍</span>
              <h4 class="layer-title">卷积层 (Convolution Layer)</h4>
            </div>
            <div class="layer-content">
              <div class="layer-image">
                 <img
                   src="/案例(2)/卷积层.gif"
                   alt="卷积层动画演示"
                   class="layer-demo"
                   @click="enlargeImage($event, 'convolution')"
                 />
                 <div class="image-caption">点击放大查看卷积操作过程</div>
               </div>
              <div class="layer-description">
                <p><strong>核心功能：</strong>特征提取与模式识别</p>
                <ul class="feature-list">
                  <li>🎯 <strong>参数共享：</strong>同一滤波器扫描整个输入</li>
                  <li>🔗 <strong>局部连接：</strong>只关注局部区域特征</li>
                  <li>📊 <strong>特征映射：</strong>提取边缘、纹理等特征</li>
                </ul>
              </div>
            </div>
          </div>

          <!-- 池化层 -->
          <div class="layer-card">
            <div class="layer-header">
              <span class="layer-icon">📉</span>
              <h4 class="layer-title">池化层 (Pooling Layer)</h4>
            </div>
            <div class="layer-content">
              <div class="layer-image">
                 <img
                   src="/案例(2)/池化层.gif"
                   alt="池化层动画演示"
                   class="layer-demo"
                   @click="enlargeImage($event, 'pooling')"
                 />
                 <div class="image-caption">点击放大查看池化降维过程</div>
               </div>
              <div class="layer-description">
                <p><strong>核心功能：</strong>降维压缩与特征选择</p>
                <ul class="feature-list">
                  <li>📏 <strong>尺寸缩减：</strong>减少特征图大小</li>
                  <li>🎯 <strong>特征保留：</strong>保持重要信息不丢失</li>
                  <li>🔄 <strong>平移不变：</strong>增强模型鲁棒性</li>
                </ul>
              </div>
            </div>
          </div>

          <!-- 全连接层 -->
          <div class="layer-card">
            <div class="layer-header">
              <span class="layer-icon">🧠</span>
              <h4 class="layer-title">全连接层 (Fully Connected Layer)</h4>
            </div>
            <div class="layer-content">
              <div class="layer-image">
                 <img
                   src="/案例(2)/全连接层.png"
                   alt="全连接层结构图"
                   class="layer-demo"
                   @click="enlargeImage($event, 'fc')"
                 />
                 <div class="image-caption">点击放大查看全连接结构</div>
               </div>
              <div class="layer-description">
                <p><strong>核心功能：</strong>特征整合与最终决策</p>
                <ul class="feature-list">
                  <li>🔗 <strong>全局连接：</strong>整合所有特征信息</li>
                  <li>🎲 <strong>非线性变换：</strong>复杂模式识别</li>
                  <li>📊 <strong>分类输出：</strong>生成最终预测结果</li>
                </ul>
              </div>
            </div>
          </div>
        </div>

        <!-- 学习要点总结 -->
        <div class="learning-summary">
          <h4>🎯 三大核心层协同工作流程</h4>
          <div class="workflow-steps">
            <div class="workflow-step">
              <span class="step-number">1</span>
              <span class="step-text"><strong>卷积层</strong>提取局部特征</span>
            </div>
            <div class="workflow-arrow">→</div>
            <div class="workflow-step">
              <span class="step-number">2</span>
              <span class="step-text"><strong>池化层</strong>压缩重要信息</span>
            </div>
            <div class="workflow-arrow">→</div>
            <div class="workflow-step">
              <span class="step-number">3</span>
              <span class="step-text"><strong>全连接层</strong>整合并决策</span>
            </div>
          </div>
          <div class="learning-note">
            <span class="note-icon">💡</span>
            <span class="note-text">点击各层图片可增加交互次数，完成深度学习案例学习</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 案例总结 -->
    <div class="case-summary">
      <h3 class="summary-title">💡 案例总结</h3>
      <div class="summary-content">
        <div class="summary-item">
          <h4>线性回归 vs 逻辑回归</h4>
          <ul>
            <li><strong>线性回归:</strong> 用于连续值预测，输出无界实数</li>
            <li>
              <strong>逻辑回归:</strong> 用于分类问题，输出0-1之间的概率值
            </li>
            <li><strong>共同点:</strong> 都使用线性函数作为基础模型</li>
          </ul>
        </div>
      </div>
    </div>

    <!-- 下一步提示 -->
    <div class="next-step">
      <div class="next-step-content">
        <div class="completion-celebration">
          <h3>🎉 恭喜完成第二章案例学习！</h3>
          <p>
            你已经了解了第二章的核心概念，包括线性回归、逻辑回归和深度学习核心层。现在可以滚动到"知识测试"部分参加测验来检验学习成果！
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Chapter2CaseStudy",
  props: {
    chapterId: {
      type: String,
      default: "2",
    },
  },
  emits: ["case-completed", "all-cases-completed"],
  data() {
    return {
      dataPoints: 50,
      noiseLevel: 0.5,
      linearR2: 0.85,
      linearMSE: 0.12,
      logisticAccuracy: 0.92,
      logisticAUC: 0.89,

      // 卷积层参数
      kernelSize: 3,
      stride: 1,
      padding: 0,
      inputMatrixSize: 6,
      isAnimating: false,
      showComputationDetails: false,
      kernelMatrix: [],
      inputMatrix: [],

      // 池化层参数
      poolingType: "max",
      poolingSize: 2,
      poolingStride: 2,
      poolingInputSize: 6,
      isPoolingAnimating: false,
      showPoolingOperation: false,
      showPoolingComparison: false,
      poolingComparison: {},

      // 全连接层参数
      inputNeurons: 5,
      hiddenNeurons: 4,
      outputNeurons: 3,
      activationFunction: "relu",
      learningRate: 0.01,
      weightInit: "random",
      showWeights: false,
      currentLoss: 0.5,
      trainingSteps: 0,
      weights: {
        inputHidden: [],
        hiddenOutput: [],
      },
      neuronValues: {
        input: [],
        hidden: [],
        output: [],
      },

      // 案例完成状态
      caseStatus: {
        regression: false,
        deepLearning: false,
      },
      // 交互次数跟踪
      interactionCounts: {
        regressionGenerate: 0,
        layerVisualize: 0,
        convolutionDemo: 0,
        poolingDemo: 0,
        fcDemo: 0,
      },
      // 图片放大功能
      enlargedImage: {
        src: '',
        type: '',
        visible: false
      },
    };
  },
  mounted() {
    this.initConvolutionDemo();
    this.initPoolingDemo();
    this.initFullyConnectedDemo();
  },
  computed: {
    regressionCaseCompleted() {
      return this.interactionCounts.regressionGenerate >= 3;
    },
    deepLearningCaseCompleted() {
      // 需要用户进行一定的交互才能完成深度学习案例
      return this.interactionCounts.convolutionDemo >= 1 || 
             this.interactionCounts.poolingDemo >= 1 || 
             this.interactionCounts.fcDemo >= 1;
    },
    allCasesCompleted() {
      // 需要完成回归案例和深度学习案例
      return this.regressionCaseCompleted && this.deepLearningCaseCompleted;
    },

    // 卷积层计算属性
    outputMatrixSize() {
      return (
        Math.floor(
          (this.inputMatrixSize + 2 * this.padding - this.kernelSize) /
            this.stride
        ) + 1
      );
    },

    // 池化层计算属性
    poolingOutputSize() {
      return (
        Math.floor(
          (this.poolingInputSize - this.poolingSize) / this.poolingStride
        ) + 1
      );
    },
    poolingReductionRatio() {
      const inputSize = this.poolingInputSize * this.poolingInputSize;
      const outputSize = this.poolingOutputSize * this.poolingOutputSize;
      return Math.round((1 - outputSize / inputSize) * 100);
    },

    // 全连接层计算属性
    totalParameters() {
      return (
        this.inputNeurons * this.hiddenNeurons +
        this.hiddenNeurons +
        (this.hiddenNeurons * this.outputNeurons + this.outputNeurons)
      );
    },
  },
  methods: {
    generateData() {
      this.linearR2 = 0.8 + Math.random() * 0.15;
      this.linearMSE = 0.1 + Math.random() * 0.2;
      this.logisticAccuracy = 0.85 + Math.random() * 0.1;
      this.logisticAUC = 0.8 + Math.random() * 0.15;

      // 增加交互次数
      this.interactionCounts.regressionGenerate++;

      // 检查回归案例是否完成
      if (this.regressionCaseCompleted && !this.caseStatus.regression) {
        this.caseStatus.regression = true;
        this.$emit("case-completed", "regression");
        this.checkAllCasesCompleted();
      }
    },

    initConvolutionDemo() {
      this.updateKernel();
      this.renderMatrices();
    },

    updateKernel() {
      const size = parseInt(this.kernelSize);
      this.kernelMatrix = [];
      for (let i = 0; i < size; i++) {
        const row = [];
        for (let j = 0; j < size; j++) {
          row.push(Math.round((Math.random() - 0.5) * 2));
        }
        this.kernelMatrix.push(row);
      }
      this.renderMatrices();

      // 增加卷积演示交互次数
      this.interactionCounts.convolutionDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    randomizeKernel() {
      this.updateKernel();
    },

    // 新增卷积层交互方法
    animateConvolution() {
      this.isAnimating = true;
      this.showComputationDetails = true;
      // 模拟卷积动画
      let step = 0;
      const totalSteps = this.outputMatrixSize * this.outputMatrixSize;

      const animate = () => {
        if (step < totalSteps) {
          // 更新动画状态
          step++;
          setTimeout(animate, 500);
        } else {
          this.isAnimating = false;
        }
      };
      animate();

      this.interactionCounts.convolutionDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    resetConvolution() {
      this.kernelSize = 3;
      this.stride = 1;
      this.padding = 0;
      this.isAnimating = false;
      this.showComputationDetails = false;
      this.updateKernel();
    },

    renderMatrices() {
      this.renderMatrix(this.$refs.inputMatrix, this.generateInputMatrix());
      this.renderMatrix(this.$refs.kernelMatrix, this.kernelMatrix);
      this.renderMatrix(this.$refs.outputMatrix, this.computeConvolution());
    },

    generateInputMatrix() {
      const size = parseInt(this.kernelSize) + 2;
      const matrix = [];
      for (let i = 0; i < size; i++) {
        const row = [];
        for (let j = 0; j < size; j++) {
          row.push(Math.round(Math.random() * 9));
        }
        matrix.push(row);
      }
      return matrix;
    },

    computeConvolution() {
      const input = this.generateInputMatrix();
      const kernel = this.kernelMatrix;
      const outputSize = input.length - kernel.length + 1;
      const output = [];

      for (let i = 0; i < outputSize; i++) {
        const row = [];
        for (let j = 0; j < outputSize; j++) {
          let sum = 0;
          for (let ki = 0; ki < kernel.length; ki++) {
            for (let kj = 0; kj < kernel.length; kj++) {
              sum += input[i + ki][j + kj] * kernel[ki][kj];
            }
          }
          row.push(sum);
        }
        output.push(row);
      }
      return output;
    },

    renderMatrix(container, matrix) {
      if (!container) return;

      container.innerHTML = "";
      matrix.forEach((row) => {
        const rowDiv = document.createElement("div");
        rowDiv.className = "matrix-row";
        row.forEach((cell) => {
          const cellDiv = document.createElement("div");
          cellDiv.className = "matrix-cell";
          cellDiv.textContent = cell;
          rowDiv.appendChild(cellDiv);
        });
        container.appendChild(rowDiv);
      });
    },

    initPoolingDemo() {
      this.updatePooling();
    },

    updatePooling() {
      this.renderPoolingMatrices();

      // 增加池化演示交互次数
      this.interactionCounts.poolingDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    // 新增池化层交互方法
    generateNewPoolingData() {
      this.poolingInputSize = 4 + Math.floor(Math.random() * 4); // 4-7的随机大小
      this.updatePooling();
    },

    animatePooling() {
      this.isPoolingAnimating = true;
      this.showPoolingOperation = true;

      // 模拟池化动画
      setTimeout(() => {
        this.isPoolingAnimating = false;
        this.showPoolingOperation = false;
      }, 3000);

      this.interactionCounts.poolingDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    comparePoolingTypes() {
      this.showPoolingComparison = !this.showPoolingComparison;

      if (this.showPoolingComparison) {
        const inputMatrix = this.generatePoolingInput();
        this.poolingComparison = {
          max: this.computePoolingByType(inputMatrix, "max"),
          avg: this.computePoolingByType(inputMatrix, "avg"),
          min: this.computePoolingByType(inputMatrix, "min"),
        };

        // 渲染对比结果
        this.$nextTick(() => {
          Object.keys(this.poolingComparison).forEach((type) => {
            const ref = this.$refs[`comparison-${type}`];
            if (ref && ref[0]) {
              this.renderMatrix(ref[0], this.poolingComparison[type]);
            }
          });
        });
      }

      this.interactionCounts.poolingDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    computePoolingByType(matrix, type) {
      const originalType = this.poolingType;
      this.poolingType = type;
      const result = this.computePooling(matrix);
      this.poolingType = originalType;
      return result;
    },

    getPoolingDescription(type) {
      const descriptions = {
        max: "保留最大值，突出显著特征",
        avg: "计算平均值，平滑特征表示",
        min: "保留最小值，关注细节信息",
      };
      return descriptions[type] || "";
    },

    renderPoolingMatrices() {
      const beforeMatrix = this.generatePoolingInput();
      this.renderMatrix(this.$refs.beforePooling, beforeMatrix);

      const afterMatrix = this.computePooling(beforeMatrix);
      this.renderMatrix(this.$refs.afterPooling, afterMatrix);
    },

    generatePoolingInput() {
      const size = parseInt(this.poolingSize) * 2;
      const matrix = [];
      for (let i = 0; i < size; i++) {
        const row = [];
        for (let j = 0; j < size; j++) {
          row.push(Math.round(Math.random() * 9));
        }
        matrix.push(row);
      }
      return matrix;
    },

    computePooling(matrix) {
      const size = parseInt(this.poolingSize);
      const output = [];

      for (let i = 0; i < matrix.length; i += size) {
        const row = [];
        for (let j = 0; j < matrix[i].length; j += size) {
          let values = [];
          for (let ki = 0; ki < size; ki++) {
            for (let kj = 0; kj < size; kj++) {
              if (i + ki < matrix.length && j + kj < matrix[i].length) {
                values.push(matrix[i + ki][j + kj]);
              }
            }
          }

          if (this.poolingType === "max") {
            row.push(Math.max(...values));
          } else {
            row.push(
              Math.round(values.reduce((a, b) => a + b, 0) / values.length)
            );
          }
        }
        output.push(row);
      }
      return output;
    },

    initFullyConnectedDemo() {
      this.renderNeurons();
    },

    renderNeurons() {
      this.renderNeuronLayer(
        this.$refs.inputNeurons,
        this.inputNeurons,
        "input"
      );
      this.renderNeuronLayer(
        this.$refs.hiddenNeurons,
        this.hiddenNeurons,
        "hidden"
      );
      this.renderNeuronLayer(
        this.$refs.outputNeurons,
        this.outputNeurons,
        "output"
      );
      this.drawConnections();

      // 增加全连接层演示交互次数
      this.interactionCounts.fcDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    // 新增全连接层交互方法
    updateActivation() {
      this.renderActivationPreview();
      this.interactionCounts.fcDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    initializeWeights() {
      // 初始化权重矩阵
      this.weights.inputHidden = this.createWeightMatrix(
        this.inputNeurons,
        this.hiddenNeurons
      );
      this.weights.hiddenOutput = this.createWeightMatrix(
        this.hiddenNeurons,
        this.outputNeurons
      );

      if (this.showWeights) {
        this.renderWeightHeatmaps();
      }

      this.interactionCounts.fcDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    createWeightMatrix(rows, cols) {
      const matrix = [];
      for (let i = 0; i < rows; i++) {
        const row = [];
        for (let j = 0; j < cols; j++) {
          let weight = 0;
          switch (this.weightInit) {
            case "random":
              weight = (Math.random() - 0.5) * 2;
              break;
            case "xavier":
              weight = (Math.random() - 0.5) * 2 * Math.sqrt(6 / (rows + cols));
              break;
            case "he":
              weight = (Math.random() - 0.5) * 2 * Math.sqrt(2 / rows);
              break;
          }
          row.push(weight);
        }
        matrix.push(row);
      }
      return matrix;
    },

    forwardPass() {
      // 生成随机输入
      this.neuronValues.input = Array.from({ length: this.inputNeurons }, () =>
        Math.random()
      );

      // 计算隐藏层
      this.neuronValues.hidden = this.computeLayerOutput(
        this.neuronValues.input,
        this.weights.inputHidden
      );

      // 计算输出层
      this.neuronValues.output = this.computeLayerOutput(
        this.neuronValues.hidden,
        this.weights.hiddenOutput
      );

      this.updateNeuronDisplay();
      this.interactionCounts.fcDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    backwardPass() {
      // 模拟反向传播
      this.currentLoss = Math.max(
        0.01,
        this.currentLoss - 0.05 + Math.random() * 0.1
      );
      this.trainingSteps++;

      this.interactionCounts.fcDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    trainStep() {
      this.forwardPass();
      setTimeout(() => {
        this.backwardPass();
      }, 1000);
    },

    resetNetwork() {
      this.currentLoss = 0.5;
      this.trainingSteps = 0;
      this.initializeWeights();
      this.neuronValues = {
        input: [],
        hidden: [],
        output: [],
      };
      this.updateNeuronDisplay();
    },

    randomizeInputs() {
      this.neuronValues.input = Array.from({ length: this.inputNeurons }, () =>
        Math.random()
      );
      this.updateNeuronDisplay();

      this.interactionCounts.fcDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    toggleWeights() {
      this.showWeights = !this.showWeights;

      // 增加全连接层演示交互次数
      this.interactionCounts.fcDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    computeLayerOutput(inputs, weights) {
      const outputs = [];
      for (let j = 0; j < weights[0].length; j++) {
        let sum = 0;
        for (let i = 0; i < inputs.length; i++) {
          sum += inputs[i] * weights[i][j];
        }
        outputs.push(this.applyActivation(sum));
      }
      return outputs;
    },

    applyActivation(x) {
      switch (this.activationFunction) {
        case "relu":
          return Math.max(0, x);
        case "sigmoid":
          return 1 / (1 + Math.exp(-x));
        case "tanh":
          return Math.tanh(x);
        case "leaky_relu":
          return x > 0 ? x : 0.01 * x;
        default:
          return x;
      }
    },

    getActivationDescription(func) {
      const descriptions = {
        relu: "ReLU: f(x) = max(0, x)，解决梯度消失问题",
        sigmoid: "Sigmoid: f(x) = 1/(1+e^(-x))，输出0-1之间",
        tanh: "Tanh: f(x) = tanh(x)，输出-1到1之间",
        leaky_relu: "Leaky ReLU: f(x) = max(0.01x, x)，避免神经元死亡",
      };
      return descriptions[func] || "";
    },

    drawConnections() {
      // 绘制神经元之间的连接线
      // 这里可以添加SVG连接线的绘制逻辑
    },

    renderActivationPreview() {
      // 渲染激活函数预览图
      // 这里可以添加激活函数图形的绘制逻辑
    },

    renderWeightHeatmaps() {
      // 渲染权重热力图
      // 这里可以添加权重可视化的逻辑
    },

    updateNeuronDisplay() {
      // 更新神经元显示状态
      // 这里可以添加神经元颜色和数值显示的逻辑
    },

    renderNeuronLayer(container, count, type) {
      if (!container) return;

      container.innerHTML = "";
      for (let i = 0; i < count; i++) {
        const neuron = document.createElement("div");
        neuron.className = `neuron neuron-${type}`;
        neuron.textContent = i + 1;
        container.appendChild(neuron);
      }
    },

    checkDeepLearningCaseCompletion() {
      if (this.deepLearningCaseCompleted && !this.caseStatus.deepLearning) {
        this.caseStatus.deepLearning = true;
        this.$emit("case-completed", "deepLearning");
        this.checkAllCasesCompleted();
      }
    },

    checkAllCasesCompleted() {
      if (this.allCasesCompleted) {
        this.$emit("all-cases-completed");
      }
    },

    startQuiz() {
      this.$router.push("/quiz");
    },

    scrollToTop() {
      window.scrollTo({ top: 0, behavior: "smooth" });
    },

    // 跳转到回归演示实验室
    redirectToRegressionPlatform() {
      this.$router.push("/regression-demo");
    },

    updateFullyConnected() {
      this.updateWeights();
      this.updateNeuronValues();

      // 增加全连接层演示交互次数
      this.interactionCounts.fcDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    startTraining() {
      if (this.trainingSteps > 0) return;

      this.trainingSteps = 0;
      this.currentLoss = 0.5;

      const train = () => {
        if (this.trainingSteps < 100) {
          this.trainingSteps++;
          this.currentLoss = Math.max(0.01, this.currentLoss * 0.95);
          setTimeout(train, 100);
        }
      };

      train();

      // 增加全连接层演示交互次数
      this.interactionCounts.fcDemo++;
      this.checkDeepLearningCaseCompletion();
    },

    updateWeights() {
      // 生成输入到隐藏层的权重
      this.weights.inputHidden = [];
      for (let i = 0; i < this.inputNeurons; i++) {
        const row = [];
        for (let j = 0; j < this.hiddenNeurons; j++) {
          row.push((Math.random() - 0.5) * 2);
        }
        this.weights.inputHidden.push(row);
      }

      // 生成隐藏层到输出层的权重
      this.weights.hiddenOutput = [];
      for (let i = 0; i < this.hiddenNeurons; i++) {
        const row = [];
        for (let j = 0; j < this.outputNeurons; j++) {
          row.push((Math.random() - 0.5) * 2);
        }
        this.weights.hiddenOutput.push(row);
      }
    },

    updateNeuronValues() {
      // 生成输入层神经元值
      this.neuronValues.input = [];
      for (let i = 0; i < this.inputNeurons; i++) {
        this.neuronValues.input.push(Math.random());
      }

      // 生成隐藏层神经元值
      this.neuronValues.hidden = [];
      for (let i = 0; i < this.hiddenNeurons; i++) {
        this.neuronValues.hidden.push(Math.random());
      }

      // 生成输出层神经元值
      this.neuronValues.output = [];
      for (let i = 0; i < this.outputNeurons; i++) {
        this.neuronValues.output.push(Math.random());
      }
    },

    // 图片放大功能方法
    enlargeImage(imageSrc, layerType) {
      this.enlargedImage = {
        src: imageSrc,
        type: layerType,
        visible: true
      };
      
      // 增加对应层的交互次数
      if (layerType === 'convolution') {
        this.interactionCounts.convolutionDemo++;
      } else if (layerType === 'pooling') {
        this.interactionCounts.poolingDemo++;
      } else if (layerType === 'fullyConnected') {
        this.interactionCounts.fcDemo++;
      }
      
      this.checkDeepLearningCaseCompletion();
    },

    closeEnlargedImage() {
      this.enlargedImage.visible = false;
      setTimeout(() => {
        this.enlargedImage = {
          src: '',
          type: '',
          visible: false
        };
      }, 300);
    },
  },

  watch: {
    inputNeurons() {
      this.renderNeurons();
    },
    hiddenNeurons() {
      this.renderNeurons();
    },
    outputNeurons() {
      this.renderNeurons();
    },
  },
};
</script>

<style lang="scss" scoped>
@use "sass:color";


.chapter2-case-study {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.case-header {
  text-align: center;
  margin-bottom: 3rem;
  padding: 2rem;
  background: var(--chapter-badge-bg, linear-gradient(135deg, #18191a, #232526));
  border-radius: 16px;
  color: white;
  
  /* 浅色主题样式 */
  .light-theme & {
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
    color: var(--text-color, #1e293b);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(203, 213, 225, 0.3);
  }
}

.case-title {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  font-weight: 900;
  
  /* 浅色主题样式 */
  .light-theme & {
    color: var(--text-color, #1e293b);
    background: linear-gradient(135deg, var(--accent-color, #0d6efd) 0%, #0dcaf0 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
}

.case-description {
  font-size: 1.2rem;
  opacity: 0.9;
  line-height: 1.6;
  margin-bottom: 2rem;
  
  /* 浅色主题样式 */
  .light-theme & {
    color: var(--text-secondary-color, #475569);
    opacity: 0.8;
  }
}

.progress-indicator {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
  margin-top: 2rem;
}

.progress-item {
  background: rgba(255, 255, 255, 0.1);
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: all 0.3s ease;

  &.completed {
    background: rgba(76, 175, 80, 0.2);
    border-color: rgba(76, 175, 80, 0.5);
    transform: scale(1.02);
  }
  
  /* 浅色主题样式 */
  .light-theme & {
    background: rgba(255, 255, 255, 0.8);
    border: 2px solid rgba(203, 213, 225, 0.4);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    
    &.completed {
      background: rgba(13, 110, 253, 0.1);
      border-color: rgba(13, 110, 253, 0.3);
      box-shadow: 0 6px 20px rgba(13, 110, 253, 0.15);
    }
  }
}

.progress-icon {
  font-size: 2rem;
  min-width: 3rem;
  text-align: center;
}

.progress-info {
  flex: 1;

  h4 {
    color: white;
    margin: 0 0 0.5rem 0;
    font-size: 1.1rem;
    font-weight: 600;
    
    /* 浅色主题样式 */
    .light-theme & {
      color: var(--text-color, #1e293b);
    }
  }

  p {
    color: rgba(255, 255, 255, 0.8);
    margin: 0 0 0.5rem 0;
    font-size: 0.9rem;
    
    /* 浅色主题样式 */
    .light-theme & {
      color: var(--text-secondary-color, #64748b);
    }
  }
}

.progress-status {
  .status-completed {
    color: #4caf50;
    font-weight: 600;
    
    /* 浅色主题样式 */
    .light-theme & {
      color: var(--accent-color, #0d6efd);
    }
  }

  .status-pending {
    color: #ffc107;
    font-weight: 600;
    
    /* 浅色主题样式 */
    .light-theme & {
      color: #f59e0b;
    }
  }
}

.case-section {
  background: var(--card-bg, #292c33);
  border-radius: var(--card-radius, 10px);
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: var(--card-shadow, 0 4px 24px rgba(24, 25, 26, 0.10));
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
}

.section-title {
  font-size: 1.8rem;
  color: var(--text-color, #f5f6fa);
  margin-bottom: 1.5rem;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.regression-demo {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.demo-controls {
  display: flex;
  gap: 2rem;
  flex-wrap: wrap;
  align-items: center;
  padding: 1rem;
  background: rgba(74, 144, 226, 0.1); // 使用主题信息色系（蓝）
  border-radius: 12px;
}

.control-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  font-weight: 600;
  color: var(--text-color, #f5f6fa);
}

.slider {
  width: 150px;
  height: 6px;
  border-radius: 3px;
  background: #ddd;
  outline: none;
  -webkit-appearance: none;
  appearance: none;

  &::-webkit-slider-thumb {
    -webkit-appearance: none;
    appearance: none;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background: var(--accent-color, #b0b3b8);
    cursor: pointer;
  }
}

.btn-generate {
  background: var(--accent-color, #b0b3b8);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    background: var(--accent-color, #b0b3b8);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(74, 144, 226, 0.3);
  }
  // removed extra closing brace that caused unmatched '}' error

  // 新增按钮样式
  .btn-small {
    padding: 0.5rem 1rem;
    font-size: 0.9rem;
    border-radius: 6px;
    border: none;
    cursor: pointer;
    transition: all 0.3s ease;
    margin: 0.25rem;

    &.btn-animate {
      background: linear-gradient(135deg, var(--info-color, #4a90e2) 0%, #64b5f6 100%);
      color: white;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(74, 144, 226, 0.3);
      }
    }

    &.btn-reset {
      background: #f44336;
      color: white;

      &:hover {
        background: #d32f2f;
        transform: translateY(-2px);
      }
    }

    &.btn-compare {
      background: #ff9800;
      color: white;

      &:hover {
        background: #f57c00;
        transform: translateY(-2px);
      }
    }

    &.btn-forward {
      background: #4caf50;
      color: white;

      &:hover {
        background: #388e3c;
        transform: translateY(-2px);
      }
    }

    &.btn-backward {
      background: #2196f3;
      color: white;

      &:hover {
        background: #1976d2;
        transform: translateY(-2px);
      }
    }

    &.btn-train {
      background: var(--info-color, #4a90e2);
      color: white;

      &:hover {
        background: #64b5f6;
        transform: translateY(-2px);
      }
    }

    &.btn-tiny {
      padding: 0.25rem 0.5rem;
      font-size: 0.8rem;
    }
  }

  .control-row {
    display: flex;
    gap: 1.5rem;
    flex-wrap: wrap;
    align-items: center;
    margin-bottom: 1rem;
  }

  .control-item {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;

    label {
      font-weight: 600;
      color: var(--text-color, #f5f6fa);
      font-size: 0.9rem;
    }

    select {
      padding: 0.5rem;
      border: 2px solid #e0e0e0;
      border-radius: 6px;
      background: white;
      font-size: 0.9rem;

      &:focus {
        outline: none;
        border-color: var(--accent-color, #b0b3b8);
      }
    }
  }

  .action-buttons {
    display: flex;
    gap: 0.5rem;
    flex-wrap: wrap;
    margin-top: 1rem;
    padding-top: 1rem;
    border-top: 1px solid #e0e0e0;
  }

  .chart-container {
    height: 300px;
    position: relative;
  }

  .chart-placeholder {
    height: 100%;
    background: #f8f9fa;
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: 2px dashed #dee2e6;
    text-align: center;

    h4 {
      color: var(--accent-color, #b0b3b8);
      margin-bottom: 0.5rem;
    }

    p {
      color: var(--text-secondary-color, #b0b3b8);
      margin-bottom: 1rem;
    }
  }

  .chart-metrics {
    display: flex;
    gap: 1rem;

    .metric {
      background: var(--accent-color, #b0b3b8);
      color: white;
      padding: 0.3rem 0.8rem;
      border-radius: 20px;
      font-size: 0.9rem;
      font-weight: 600;
    }
  }

  .demo-info {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1.5rem;
  }

  .info-card {
    background: rgba(74, 144, 226, 0.05);
    border: 1px solid rgba(74, 144, 226, 0.2);
    border-radius: 12px;
    padding: 1.5rem;

    h4 {
      color: var(--info-color, #4a90e2);
      margin-bottom: 0.5rem;
      font-weight: 700;
    }

    p {
      color: var(--text-secondary-color, #b0b3b8);
      margin-bottom: 1rem;
      line-height: 1.6;
    }
  }

  .layer-demo {
    margin-bottom: 3rem;
    padding: 1.5rem;
    background: rgba(255, 152, 0, 0.05);
    border: 1px solid rgba(255, 152, 0, 0.2);
    border-radius: 12px;
  }

  .layer-title {
    color: #ff9800;
    margin-bottom: 1rem;
    font-weight: 700;
    display: flex;
    align-items: center;
    gap: 0.5rem;
  }

  .convolution-demo {
    display: grid;
    grid-template-columns: 1fr auto 1fr;
    gap: 2rem;
    align-items: start;
    margin-bottom: 1rem;
  }

  .kernel-controls {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    margin-bottom: 1rem;

    select {
      padding: 0.5rem;
      border: 1px solid #ddd;
      border-radius: 6px;
      background: white;
    }
  }

  // 移除重复的 .btn-small 样式，已在上面定义

  .matrix {
    border: 2px solid #ddd;
    border-radius: 8px;
    overflow: hidden;
    background: white;

    .matrix-row {
      display: flex;

      .matrix-cell {
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-right: 1px solid #eee;
        border-bottom: 1px solid #eee;
        font-weight: 600;
        font-size: 0.9rem;

        &:last-child {
          border-right: none;
        }
      }

      &:last-child .matrix-cell {
        border-bottom: none;
      }
    }
  }

  .pooling-demo {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }

  .pooling-controls {
    display: flex;
    gap: 1rem;
    align-items: center;
    flex-wrap: wrap;

    select {
      padding: 0.5rem;
      border: 1px solid #ddd;
      border-radius: 6px;
      background: white;
    }
  }

  .pooling-visualization {
    display: grid;
    grid-template-columns: 1fr auto 1fr;
    gap: 2rem;
    align-items: center;
  }

  .pooling-arrow {
    font-size: 2rem;
    color: #ff9800;
    font-weight: bold;
  }

  .fc-demo {
    display: grid;
    grid-template-columns: 2fr 1fr;
    gap: 2rem;
    align-items: start;
  }

  .network-visualization {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 2rem;
    background: rgba(74, 144, 226, 0.08);
    border-radius: 12px;
    border: 1px solid rgba(74, 144, 226, 0.2);
  }

  .neurons {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    align-items: center;

    .neuron {
      width: 50px;
      height: 50px;
      border-radius: 50%;
      background: var(--accent-color, #b0b3b8);
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 700;
      font-size: 1.2rem;
      box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);

      &.neuron-hidden {
        background: #9c27b0;
        box-shadow: 0 2px 8px rgba(156, 39, 176, 0.3);
      }

      &.neuron-output {
        background: #4caf50;
        box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
      }
    }
  }

  .fc-controls {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }

  .case-summary {
    background: linear-gradient(135deg, #4caf50 0%, #45a049 100%);
    border-radius: 16px;
    padding: 2rem;
    color: white;
    margin-bottom: 2rem;
  }

  .summary-title {
    font-size: 1.8rem;
    margin-bottom: 1.5rem;
    font-weight: 700;
    text-align: center;
  }

  .summary-content {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 2rem;
  }

  .summary-item {
    h4 {
      margin-bottom: 1rem;
      font-weight: 700;
      color: rgba(255, 255, 255, 0.9);
    }

    ul {
      list-style: none;
      padding: 0;

      li {
        margin-bottom: 0.5rem;
        padding-left: 1.5rem;
        position: relative;

        &:before {
          content: "✓";
          position: absolute;
          left: 0;
          color: #ffd700;
          font-weight: bold;
        }
      }
    }
  }

  .next-step {
    border-radius: 16px;
    padding: 2rem;
    text-align: center;
    color: white;
    background: linear-gradient(135deg, #4caf50 0%, #45a049 100%);
    box-shadow: 0 8px 32px rgba(76, 175, 80, 0.3);
    transition: all 0.3s ease;
    margin-top: 3rem;
  }

  .next-step-content {
    max-width: 800px;
    margin: 0 auto;
  }

  .completion-celebration {
    h3 {
      font-size: 2.2rem;
      margin-bottom: 1.5rem;
      animation: bounce 1s ease-in-out;
      color: white;
      font-weight: 700;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
      letter-spacing: 1px;
    }

    p {
      font-size: 1.2rem;
      margin-bottom: 2.5rem;
      line-height: 1.7;
      color: white;
      opacity: 0.95;
      font-weight: 500;
      text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
      max-width: 600px;
      margin-left: auto;
      margin-right: auto;
    }
  }

  @keyframes bounce {
    0%,
    20%,
    50%,
    80%,
    100% {
      transform: translateY(0);
    }
    40% {
      transform: translateY(-10px);
    }
    60% {
      transform: translateY(-5px);
    }
  }

  // 新增可视化区域样式
  .visualization-area {
    background: #f8f9fa;
    border-radius: 12px;
    padding: 1.5rem;
    margin: 1rem 0;
    border: 2px solid #e9ecef;

    .viz-title {
      font-size: 1.1rem;
      font-weight: 700;
      color: var(--primary-color, #18191a);
      margin-bottom: 1rem;
      text-align: center;
    }

    .viz-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1.5rem;
      margin-bottom: 1rem;
    }

    .viz-item {
      background: white;
      border-radius: 8px;
      padding: 1rem;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

      .viz-label {
        font-weight: 600;
        color: var(--text-color, #f5f6fa);
        margin-bottom: 0.5rem;
        font-size: 0.9rem;
      }

      .viz-content {
        min-height: 100px;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #f8f9fa;
        border-radius: 6px;
        border: 1px dashed #dee2e6;
      }
    }
  }

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 1rem;
    margin: 1rem 0;

    .stat-item {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 1rem;
      border-radius: 8px;
      text-align: center;

      .stat-value {
        font-size: 1.5rem;
        font-weight: 700;
        margin-bottom: 0.25rem;
      }

      .stat-label {
        font-size: 0.8rem;
        opacity: 0.9;
      }
    }
  }

  .network-canvas {
    border: 2px solid #e9ecef;
    border-radius: 8px;
    background: white;
    margin: 1rem 0;
  }

  .weight-heatmap {
    .heatmap-row {
      display: flex;

      .heatmap-cell {
        width: 20px;
        height: 20px;
        margin: 1px;
        border-radius: 2px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 0.7rem;
        color: white;
        text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.5);
      }
    }
  }

  .activation-preview {
    width: 100%;
    height: 150px;
    border: 1px solid #dee2e6;
    border-radius: 6px;
    background: white;
  }

  .comparison-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
    margin: 1rem 0;

    .comparison-item {
      background: white;
      border: 2px solid #e9ecef;
      border-radius: 8px;
      padding: 1rem;
      text-align: center;

      .comparison-title {
        font-weight: 600;
        color: var(--primary-color, #18191a);
        margin-bottom: 0.5rem;
      }

      .comparison-result {
        font-size: 1.2rem;
        font-weight: 700;
        color: var(--accent-color, #b0b3b8);
        margin: 0.5rem 0;
      }

      .comparison-desc {
        font-size: 0.8rem;
        color: #666;
      }
    }
  }

  .training-status {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    color: white;
    padding: 1rem;
    border-radius: 8px;
    margin: 1rem 0;

    .status-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
      gap: 1rem;

      .status-item {
        text-align: center;

        .status-value {
          font-size: 1.2rem;
          font-weight: 700;
          margin-bottom: 0.25rem;
        }

        .status-label {
          font-size: 0.8rem;
          opacity: 0.9;
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

  .animating {
    animation: pulse 1s ease-in-out infinite;
  }

  .fade-in {
    animation: fadeIn 0.5s ease-in;
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(10px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  // 平台跳转卡片样式
  .platform-redirect-card {
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    border: 2px solid #dee2e6;
    border-radius: 16px;
    padding: 2rem;
    margin-bottom: 2rem;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
      border-color: var(--accent-color, #b0b3b8);
    }
  }

  .redirect-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.5rem;

    h4 {
      font-size: 1.5rem;
      font-weight: 700;
      color: var(--primary-color, #18191a);
      margin: 0;
    }
  }

  .redirect-badge {
    background: linear-gradient(135deg, var(--primary-color, #18191a) 0%, var(--accent-color, #b0b3b8) 100%);
    color: white;
    padding: 0.5rem 1rem;
    border-radius: 20px;
    font-size: 0.8rem;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }

  .redirect-content {
    display: grid;
    grid-template-columns: 2fr 1fr;
    gap: 2rem;
    align-items: start;
  }

  .redirect-info {
    .redirect-desc {
      font-size: 1.1rem;
      color: var(--text-color, #f5f6fa);
      line-height: 1.6;
      margin-bottom: 1.5rem;
    }
  }

  .learning-objectives {
    margin-bottom: 1.5rem;

    h5 {
      font-size: 1.1rem;
      font-weight: 600;
      color: var(--primary-color, #18191a);
      margin-bottom: 0.75rem;
    }

    ul {
      list-style: none;
      padding: 0;

      li {
        position: relative;
        padding-left: 1.5rem;
        margin-bottom: 0.5rem;
        color: var(--text-color, #f5f6fa);
        line-height: 1.5;

        &:before {
          content: "✓";
          position: absolute;
          left: 0;
          color: #4caf50;
          font-weight: bold;
        }
      }
    }
  }

  .platform-features {
    display: flex;
    gap: 1rem;
    flex-wrap: wrap;

    .feature-item {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      background: var(--card-bg, #292c33);
      padding: 0.75rem 1.25rem;
      border-radius: var(--form-radius, 10px);
      border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 15px rgba(74, 144, 226, 0.1);
        border-color: var(--primary-color, #18191a);
      }

      .feature-icon {
        font-size: 1.3rem;
        background: linear-gradient(
          135deg,
          var(--primary-color, #18191a) 0%,
          var(--accent-color, #b0b3b8) 100%
        );
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
      }

      .feature-text {
        font-size: 0.9rem;
        font-weight: 500;
        color: var(--text-color, #f5f6fa);
      }
    }
  }

  .algorithm-cards {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1rem;
    margin: 1.5rem 0;

    .algorithm-card {
      background: var(--card-bg, #292c33);
      border: 2px solid var(--card-border, rgba(57, 59, 64, 0.18));
      border-radius: var(--card-radius, 10px);
      padding: 1.25rem;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      position: relative;
      overflow: hidden;

      &:before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 4px;
        background: linear-gradient(
          90deg,
          var(--primary-color, #18191a) 0%,
          var(--accent-color, #b0b3b8) 50%,
          var(--info-color, #4a90e2) 100%
        );
        opacity: 0;
        transition: opacity 0.3s ease;
      }

      &:hover {
        border-color: var(--primary-color, #18191a);
        transform: translateY(-3px);
        box-shadow: 0 8px 30px rgba(74, 144, 226, 0.15);

        &:before {
          opacity: 1;
        }
      }

      .algorithm-header {
        display: flex;
        align-items: center;
        gap: 0.75rem;
        margin-bottom: 0.75rem;

        .algorithm-icon {
          font-size: 1.8rem;
          background: linear-gradient(
            135deg,
            var(--primary-color, #18191a) 0%,
            var(--accent-color, #b0b3b8) 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
        }

        .algorithm-name {
          font-size: 1.1rem;
          font-weight: 600;
          color: var(--text-color, #f5f6fa);
          background: linear-gradient(
            135deg,
            var(--text-color, #f5f6fa) 0%,
            var(--text-secondary-color, #b0b3b8) 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
        }
      }

      .algorithm-desc {
        font-size: 0.9rem;
        color: var(--text-secondary-color, #b0b3b8);
        line-height: 1.5;
        font-weight: 400;
      }
    }
  }

  .redirect-action {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;

    .redirect-note {
      font-size: 0.9rem;
      color: #666;
      margin-top: 1rem;
      line-height: 1.4;
    }
  }

  .btn-platform {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    padding: 1rem 2rem;
    border: none;
    border-radius: 12px;
    font-size: 1.1rem;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
    text-decoration: none;
    color: white;
    position: relative;
    overflow: hidden;

    &.linear {
      background: linear-gradient(
        135deg,
        var(--success-color, #4caf50) 0%,
        #66bb6a 100%
      );
      box-shadow: 0 4px 20px rgba(82, 196, 26, 0.25);

      &:hover {
        background: linear-gradient(
          135deg,
          #66bb6a 0%,
          #4caf50 100%
        );
        transform: translateY(-3px);
        box-shadow: 0 12px 35px rgba(82, 196, 26, 0.4);
      }
    }

    &.logistic {
      background: linear-gradient(135deg, var(--primary-color, #18191a) 0%, var(--info-color, #4a90e2) 100%);
      box-shadow: 0 4px 20px rgba(74, 144, 226, 0.25);

      &:hover {
        background: linear-gradient(
          135deg,
          var(--info-color, #4a90e2) 0%,
          #357abd 100%
        );
        transform: translateY(-3px);
        box-shadow: 0 12px 35px rgba(74, 144, 226, 0.4);
      }
    }

    &.unified {
      background: linear-gradient(
        135deg,
        var(--primary-color, #18191a) 0%,
        var(--accent-color, #b0b3b8) 100%
      );
      box-shadow: 0 4px 20px rgba(74, 144, 226, 0.25);

      &:hover {
        background: linear-gradient(
          135deg,
          var(--info-color, #4a90e2) 0%,
          #8fa1b3 100%
        );
        transform: translateY(-3px);
        box-shadow: 0 12px 35px rgba(74, 144, 226, 0.4);
      }
    }

    .btn-icon {
      font-size: 1.2rem;
    }

    .btn-text {
      flex: 1;
    }

    .btn-arrow {
      font-size: 1.2rem;
      transition: transform 0.3s ease;
    }

    &:hover .btn-arrow {
      transform: translateX(4px);
    }

    &:before {
      content: "";
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(
        90deg,
        transparent,
        rgba(255, 255, 255, 0.2),
        transparent
      );
      transition: left 0.5s;
    }

    &:hover:before {
      left: 100%;
    }
  }

  .next-step-content {
    h3 {
      font-size: 1.8rem;
      margin-bottom: 1rem;
      font-weight: 700;
    }

    p {
      font-size: 1.1rem;
      margin-bottom: 1.5rem;
      opacity: 0.9;
      line-height: 1.6;
    }
  }

  .btn {
    border: none;
    padding: 1rem 2rem;
    border-radius: 12px;
    font-size: 1.1rem;
    font-weight: 700;
    cursor: pointer;
    transition: all 0.3s ease;
    text-decoration: none;
    display: inline-block;
    text-align: center;
  }

  .btn-quiz {
    border: none;
    padding: 1.2rem 2.5rem;
    border-radius: 16px;
    font-size: 1.2rem;
    font-weight: 700;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    text-decoration: none;
    display: inline-block;
    text-align: center;
    background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
    color: white;
    box-shadow: 0 6px 25px rgba(255, 149, 0, 0.4);
    position: relative;
    overflow: hidden;
    min-width: 200px;
    border: 2px solid rgba(255, 255, 255, 0.2);

    &:hover {
      background: linear-gradient(135deg, #f57c00 0%, #e65100 100%);
      transform: translateY(-4px);
      box-shadow: 0 15px 40px rgba(255, 149, 0, 0.5);
      border-color: rgba(255, 255, 255, 0.4);
    }

    &:active {
      transform: translateY(-2px);
      box-shadow: 0 8px 30px rgba(255, 149, 0, 0.4);
    }

    &:before {
      content: "";
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(
        90deg,
        transparent,
        rgba(255, 255, 255, 0.3),
        transparent
      );
      transition: left 0.6s ease;
    }

    &:hover:before {
      left: 100%;
    }

    /* 确保按钮文字清晰可见 */
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
      sans-serif;
    letter-spacing: 0.5px;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  }

  @media (max-width: 768px) {
    .chapter2-case-study {
      padding: 1rem;
    }

    .case-title {
      font-size: 2rem;
    }

    .demo-controls {
      flex-direction: column;
      align-items: stretch;
    }

    .convolution-demo {
      grid-template-columns: 1fr;
      gap: 1rem;
    }

    .pooling-visualization {
      grid-template-columns: 1fr;
      gap: 1rem;
    }

    .fc-demo {
      grid-template-columns: 1fr;
      gap: 1rem;
    }

    .summary-content {
      grid-template-columns: 1fr;
      gap: 1rem;
    }

    .network-visualization {
      flex-direction: column;
      gap: 1rem;
    }

    .platform-redirect-card {
      padding: 1.5rem;
    }

    .redirect-content {
      grid-template-columns: 1fr;
      gap: 1.5rem;
    }

    .platform-features {
      grid-template-columns: 1fr;
      gap: 1rem;
    }

    /* 答题模块移动端样式 */
    .next-step {
      padding: 1.5rem;
      margin-top: 2rem;
    }

    .completion-celebration h3 {
      font-size: 1.8rem;
      margin-bottom: 1rem;
    }

    .completion-celebration p {
      font-size: 1.1rem;
      margin-bottom: 2rem;
    }

    .btn-quiz {
      padding: 1rem 2rem;
      font-size: 1.1rem;
      min-width: 180px;
    }
  }

  .btn-platform {
    padding: 0.75rem 1.5rem;
    font-size: 1rem;

    .btn-text {
      font-size: 0.9rem;
    }
  }

  .learning-objectives ul li {
    font-size: 0.9rem;
  }

  .algorithm-cards {
    grid-template-columns: 1fr;
    gap: 1rem;
    margin: 1rem 0;

    .algorithm-card {
      padding: 1rem;
      border-radius: 12px;

      .algorithm-header {
        gap: 0.5rem;
        margin-bottom: 0.5rem;

        .algorithm-icon {
          font-size: 1.5rem;
        }

        .algorithm-name {
          font-size: 1rem;
        }
      }

      .algorithm-desc {
        font-size: 0.85rem;
        line-height: 1.4;
      }
    }
  }
}

// 现代化平台卡片样式
.modern-platform-card {
  background: linear-gradient(145deg, #ffffff 0%, #f8fafc 50%, #ffffff 100%);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04), inset 0 1px 0 rgba(255, 255, 255, 0.9);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  margin: 24px 0;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 32px 64px -12px rgba(0, 0, 0, 0.15),
      0 20px 25px -5px rgba(0, 0, 0, 0.1),
      inset 0 1px 0 rgba(255, 255, 255, 0.9);
  }

  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(
      90deg,
      var(--primary-color, #18191a),
      var(--accent-color, #b0b3b8),
      var(--info-color, #4a90e2)
    );
    background-size: 200% 100%;
    animation: shimmer 3s ease-in-out infinite;
  }

  @at-root {
    @keyframes shimmer {
      0%,
      100% {
        background-position: 200% 0;
      }
      50% {
        background-position: -200% 0;
      }
    }
  }
}

// 节标题样式
.section-header {
  text-align: center;
  margin-bottom: 32px;

  .section-title {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    margin-bottom: 12px;
    position: relative;

    .title-icon {
      font-size: 2rem;
      filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
    }

    .title-text {
      background: linear-gradient(135deg, var(--primary-color, #18191a), var(--accent-color, #b0b3b8));
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      font-weight: 800;
      font-size: 2rem;
    }

    .title-decoration {
      position: absolute;
      bottom: -8px;
      left: 50%;
      transform: translateX(-50%);
      width: 60px;
      height: 3px;
      background: linear-gradient(90deg, var(--primary-color, #18191a), var(--accent-color, #b0b3b8));
      border-radius: 2px;
    }
  }

  .section-subtitle {
    color: var(--text-secondary-color, #b0b3b8);
    font-size: 1.1rem;
    font-weight: 500;
    margin: 0;
    opacity: 0.8;
  }
}

// 平台头部样式
.platform-header {
  padding: 32px 32px 24px;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid rgba(226, 232, 240, 0.6);

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;

    @media (max-width: 768px) {
      flex-direction: column;
      gap: 16px;
      text-align: center;
    }
  }

  .platform-logo {
    display: flex;
    align-items: center;
    gap: 16px;

    .logo-circle {
      width: 64px;
      height: 64px;
      background: linear-gradient(135deg, var(--primary-color, #18191a), var(--accent-color, #b0b3b8));
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
      position: relative;
      overflow: hidden;

      &::before {
        content: "";
        position: absolute;
        top: -50%;
        left: -50%;
        width: 200%;
        height: 200%;
        background: linear-gradient(
          45deg,
          transparent,
          rgba(255, 255, 255, 0.3),
          transparent
        );
        animation: rotate 3s linear infinite;
      }

      .logo-icon {
        font-size: 1.8rem;
        z-index: 1;
        filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
      }
    }

    .logo-text {
      h4 {
        margin: 0 0 4px 0;
        font-size: 1.5rem;
        font-weight: 700;
        color: var(--text-color, #f5f6fa);
      }

      .logo-subtitle {
        color: var(--text-secondary-color, #b0b3b8);
        font-size: 0.9rem;
        font-weight: 500;
        opacity: 0.8;
      }
    }
  }

  .platform-status {
    .status-indicator {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 16px;
      background: rgba(34, 197, 94, 0.1);
      border: 1px solid rgba(34, 197, 94, 0.2);
      border-radius: 20px;
      transition: all 0.3s ease;

      &:hover {
        background: rgba(34, 197, 94, 0.15);
        transform: scale(1.05);
      }

      .status-dot {
        width: 8px;
        height: 8px;
        background: #22c55e;
        border-radius: 50%;
        animation: pulseOpacity 2s infinite;
      }

      .status-text {
        color: #16a34a;
        font-weight: 600;
        font-size: 0.875rem;
      }
    }
  }

  @at-root {
    @keyframes rotate {
      from {
        transform: rotate(0deg);
      }
      to {
        transform: rotate(360deg);
      }
    }

    @keyframes pulseOpacity {
      0%,
      100% {
        opacity: 1;
      }
      50% {
        opacity: 0.5;
      }
    }
  }
}

// 算法展示区样式
.algorithms-showcase {
  padding: 32px;
  background: linear-gradient(135deg, #fafbfc 0%, #f1f5f9 100%);

  .showcase-header {
    text-align: center;
    margin-bottom: 32px;

    h5 {
      font-size: 1.4rem;
      font-weight: 700;
      color: var(--text-color, #f5f6fa);
      margin-bottom: 8px;
    }

    p {
      color: var(--text-secondary-color, #b0b3b8);
      font-size: 1rem;
      margin: 0;
      opacity: 0.8;
    }
  }

  .algorithm-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 24px;

    @media (max-width: 768px) {
      grid-template-columns: 1fr;
      gap: 16px;
    }
  }

  .algorithm-card {
    background: white;
    overflow: hidden;
    box-shadow: 0 8px 25px -5px rgba(0, 0, 0, 0.1);
    position: relative;

    &.linear-regression {
      .card-background {
        background: linear-gradient(
          135deg,
          rgba(34, 197, 94, 0.1) 0%,
          rgba(34, 197, 94, 0.05) 100%
        );
      }

      .algorithm-icon .icon-glow {
        background: radial-gradient(
          circle,
          rgba(34, 197, 94, 0.3) 0%,
          transparent 70%
        );
      }

      .tag {
        background: rgba(34, 197, 94, 0.1);
        color: #16a34a;
      }

      .feature-dot {
        background: #22c55e;
      }
    }

    &.logistic-regression {
      .card-background {
        background: linear-gradient(
          135deg,
          rgba(59, 130, 246, 0.1) 0%,
          rgba(59, 130, 246, 0.05) 100%
        );
      }

      .algorithm-icon .icon-glow {
        background: radial-gradient(
          circle,
          rgba(59, 130, 246, 0.3) 0%,
          transparent 70%
        );
      }

      .tag {
        background: rgba(59, 130, 246, 0.1);
        color: #2563eb;
      }

      .feature-dot {
        background: #3b82f6;
      }
    }

    .card-background {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      opacity: 0;
      transition: opacity 0.3s ease;

      .bg-pattern {
        width: 100%;
        height: 100%;
        background-image: radial-gradient(
            circle at 20% 80%,
            rgba(120, 119, 198, 0.1) 0%,
            transparent 50%
          ),
          radial-gradient(
            circle at 80% 20%,
            rgba(255, 119, 198, 0.1) 0%,
            transparent 50%
          );
      }
    }

    .card-content {
      padding: 24px;
      position: relative;
      z-index: 1;

      .algorithm-icon {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 60px;
        height: 60px;
        margin: 0 auto 16px;
        position: relative;

        .icon-symbol {
          font-size: 2rem;
          z-index: 2;
        }

        .icon-glow {
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          width: 80px;
          height: 80px;
          border-radius: 50%;
          opacity: 0;
        }
      }

      .algorithm-info {
        text-align: center;

        .algorithm-title {
          font-size: 1.3rem;
          font-weight: 700;
          color: var(--text-color, #f5f6fa);
          margin: 0 0 4px 0;
        }

        .algorithm-desc {
          color: var(--text-secondary-color, #b0b3b8);
          font-size: 0.9rem;
          margin: 0 0 16px 0;
          opacity: 0.8;
        }

        .algorithm-tags {
          display: flex;
          gap: 8px;
          justify-content: center;
          flex-wrap: wrap;

          .tag {
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 0.75rem;
            font-weight: 600;
            border: 1px solid rgba(0, 0, 0, 0.1);
          }
        }
      }
    }

    .card-features {
      padding: 0 24px 24px;
      position: relative;
      z-index: 1;

      .feature-list {
        .feature-item {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 8px 0;
          font-size: 0.9rem;
          color: var(--text-secondary-color, #b0b3b8);

          .feature-dot {
            width: 6px;
            height: 6px;
            border-radius: 50%;
            flex-shrink: 0;
          }
        }
      }
    }
  }
}

// 平台特性展示样式
.platform-highlights {
  padding: 32px;
  background: white;

  .highlights-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 24px;

    @media (max-width: 768px) {
      grid-template-columns: repeat(2, 1fr);
      gap: 16px;
    }
  }

  .highlight-item {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 20px;
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border-radius: 16px;
    border: 1px solid rgba(226, 232, 240, 0.6);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px -5px rgba(0, 0, 0, 0.1);
      border-color: var(--primary-color, #18191a);
    }

    .highlight-icon {
      width: 48px;
      height: 48px;
      background: linear-gradient(135deg, var(--primary-color, #18191a), var(--accent-color, #b0b3b8));
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 1.5rem;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      flex-shrink: 0;
    }

    .highlight-content {
      h6 {
        font-size: 1rem;
        font-weight: 600;
        color: var(--text-color, #f5f6fa);
        margin: 0 0 4px 0;
      }

      p {
        font-size: 0.875rem;
        color: var(--text-secondary-color, #b0b3b8);
        margin: 0;
        opacity: 0.8;
      }
    }

    @media (max-width: 768px) {
      flex-direction: column;
      text-align: center;
      gap: 12px;

      .highlight-icon {
        width: 40px;
        height: 40px;
        font-size: 1.2rem;
      }
    }
  }
}

// 启动按钮区域样式
.platform-launch {
  padding: 32px;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-top: 1px solid rgba(226, 232, 240, 0.6);

  .launch-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 32px;
    margin-bottom: 24px;

    @media (max-width: 768px) {
      flex-direction: column;
      text-align: center;
      gap: 24px;
    }

    .launch-info {
      flex: 1;

      h5 {
        font-size: 1.4rem;
        font-weight: 700;
        color: var(--text-color, #f5f6fa);
        margin: 0 0 8px 0;
      }

      p {
        color: var(--text-secondary-color, #b0b3b8);
        font-size: 1rem;
        margin: 0;
        opacity: 0.8;
        line-height: 1.5;
      }
    }

    .launch-button {
      position: relative;
      padding: 0;
      border: none;
      border-radius: 16px;
      cursor: pointer;
      overflow: hidden;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      box-shadow: 0 8px 25px -5px rgba(0, 0, 0, 0.2);

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 20px 40px -5px rgba(0, 0, 0, 0.25);
      }

      .button-background {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;

        .bg-gradient {
          width: 100%;
          height: 100%;
          background: linear-gradient(
            135deg,
            var(--primary-color, #18191a) 0%,
            var(--accent-color, #b0b3b8) 50%,
            var(--info-color, #4a90e2) 100%
          );
          background-size: 200% 200%;
          animation: gradientShift 4s ease-in-out infinite;
        }

        .bg-shine {
          position: absolute;
          top: 0;
          left: -100%;
          width: 100%;
          height: 100%;
          background: linear-gradient(
            90deg,
            transparent,
            rgba(255, 255, 255, 0.3),
            transparent
          );
          transition: left 0.6s ease;
        }
      }

      &:hover .bg-shine {
        left: 100%;
      }

      .button-content {
        position: relative;
        z-index: 1;
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 16px 32px;
        color: white;
        font-size: 1.1rem;
        font-weight: 600;

        .button-icon {
          font-size: 1.3rem;
        }

        .button-arrow {
          font-size: 1.2rem;
          transition: transform 0.3s ease;
        }
      }

      &:hover .button-arrow {
        transform: translateX(4px);
      }

      @keyframes gradientShift {
        0%,
        100% {
          background-position: 0% 50%;
        }
        50% {
          background-position: 100% 50%;
        }
      }
    }
  }

  .launch-note {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    padding: 16px;
    background: rgba(255, 255, 255, 0.7);
    border-radius: 12px;
    border: 1px solid rgba(226, 232, 240, 0.8);

    .note-icon {
      font-size: 1.2rem;
      color: var(--warning-color, #ff9800);
    }

    p {
      color: var(--text-secondary-color, #b0b3b8);
      font-size: 0.9rem;
      margin: 0;
      text-align: center;
      line-height: 1.4;
    }
  }
}

/* 深度学习三大核心层展示样式 */
.deep-learning-showcase {
  background: linear-gradient(135deg, var(--primary-color, #18191a) 0%, #232526 100%);
  border-radius: 16px;
  padding: 2rem;
  margin-top: 2rem;
  box-shadow: 0 8px 32px rgba(74, 144, 226, 0.18);
}

/* 核心层网格布局 */
.core-layers-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 2.5rem;
  margin-bottom: 2rem;
}

/* 层级卡片样式 */
.layer-card {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  overflow: hidden;
  backdrop-filter: blur(10px);
}

/* 层级头部 */
.layer-header {
  padding: 1.2rem 1.5rem 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  gap: 0.8rem;
}

.layer-icon {
  font-size: 1.5rem;
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.layer-title {
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0;
  line-height: 1.3;
}

/* 层级内容 */
.layer-content {
  padding: 2rem;
}

/* 层级图片区域 */
.layer-image {
  text-align: center;
  margin-bottom: 1.5rem;
  position: relative;
}

.layer-demo {
  width: 100%;
  max-width: 500px;
  height: 350px;
  border-radius: 12px;
  object-fit: contain;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
  border: 2px solid rgba(255, 255, 255, 0.2);
  cursor: pointer;
  background: rgba(255, 255, 255, 0.1);
}

.layer-demo.enlarged {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  max-width: 80vw;
  max-height: 80vh;
  width: auto;
  height: auto;
  z-index: 9999;
  border: 3px solid rgba(255, 255, 255, 0.6);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
}

.image-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.8);
  z-index: 9998;
  display: none;
  cursor: pointer;
}

.image-overlay.active {
  display: block;
}

.image-caption {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.8rem;
  margin-top: 0.5rem;
  font-style: italic;
}

/* 层级描述 */
.layer-description p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 0.95rem;
  margin-bottom: 1rem;
  line-height: 1.5;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.feature-list li {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9rem;
  margin-bottom: 0.6rem;
  line-height: 1.4;
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
}

/* 学习总结区域 */
.learning-summary {
  background: rgba(255, 255, 255, 0.08);
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.learning-summary h4 {
  color: white;
  font-size: 1.2rem;
  margin-bottom: 1.2rem;
  text-align: center;
}

/* 工作流程步骤 */
.workflow-steps {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}

.workflow-step {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.1);
  padding: 0.8rem 1.2rem;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.step-number {
  width: 24px;
  height: 24px;
  background: rgba(74, 144, 226, 0.8);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  font-weight: 600;
  flex-shrink: 0;
}

.step-text {
  color: rgba(255, 255, 255, 0.9);
  font-size: 0.9rem;
  white-space: nowrap;
}

.workflow-arrow {
  color: rgba(255, 255, 255, 0.6);
  font-size: 1.2rem;
  font-weight: bold;
}

/* 学习提示 */
.learning-note {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 1rem;
  border: 1px solid rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: flex-start;
  gap: 0.8rem;
}

.note-icon {
  font-size: 1.2rem;
  flex-shrink: 0;
  margin-top: 0.1rem;
}

.note-text {
  color: rgba(255, 255, 255, 0.9);
  font-size: 0.9rem;
  line-height: 1.5;
  font-style: italic;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .core-layers-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .layer-header {
    padding: 1rem 1.2rem 0.8rem;
  }
  
  .layer-content {
    padding: 1.2rem;
  }
  
  .layer-demo {
    max-width: 180px;
    height: 130px;
  }
  
  .workflow-steps {
    flex-direction: column;
    gap: 0.8rem;
  }
  
  .workflow-arrow {
    transform: rotate(90deg);
  }
  
  .step-text {
    white-space: normal;
    text-align: center;
  }
}

@media (max-width: 480px) {
  .deep-learning-showcase {
    padding: 1.5rem;
  }
  
  .layer-header {
    flex-direction: column;
    text-align: center;
    gap: 0.5rem;
  }
  
  .layer-title {
    font-size: 1rem;
  }
  
  .layer-demo {
    max-width: 160px;
    height: 120px;
  }
}
</style>