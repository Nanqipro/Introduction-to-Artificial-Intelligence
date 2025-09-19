<template>
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
                 src="/案例2/卷积层.gif"
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
              <div class="math-formula">
                <h5>数学公式：</h5>
                <div class="formula-content">
                  <div class="math-expression">
                    <strong>Y<sub>i,j</sub> = Σ<sub>m=0</sub><sup>M-1</sup> Σ<sub>n=0</sub><sup>N-1</sup> X<sub>i+m,j+n</sub> · K<sub>m,n</sub> + b</strong>
                  </div>
                  <div class="detailed-explanation">
                    <h6>详细解释：</h6>
                    <ul class="formula-details">
                      <li><strong>Y_{i,j}：</strong>输出特征图在位置(i,j)的值，表示该位置检测到的特征强度</li>
                      <li><strong>X_{i+m,j+n}：</strong>输入图像在位置(i+m,j+n)的像素值，m和n是相对于当前位置的偏移</li>
                      <li><strong>K_{m,n}：</strong>卷积核(滤波器)在位置(m,n)的权重值，用于检测特定模式</li>
                      <li><strong>M×N：</strong>卷积核的尺寸，常见的有3×3、5×5等</li>
                      <li><strong>b：</strong>偏置项，为每个输出特征图添加的常数，增加模型表达能力</li>
                    </ul>
                    <div class="computation-steps">
                      <h6>计算步骤：</h6>
                      <ol>
                        <li>将卷积核放置在输入图像的(i,j)位置</li>
                        <li>对应位置的像素值与卷积核权重相乘</li>
                        <li>将所有乘积结果求和</li>
                        <li>加上偏置项b得到最终输出</li>
                      </ol>
                    </div>
                    <p class="practical-meaning">
                      <strong>实际意义：</strong>通过这个公式，卷积层能够检测图像中的边缘、角点、纹理等局部特征，不同的卷积核可以检测不同类型的特征模式。
                    </p>
                  </div>
                </div>
              </div>
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
                 src="/案例2/池化层.gif"
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
              <div class="math-formula">
                <h5>数学公式：</h5>
                <div class="formula-content">
                  <div class="formula-types">
                    <div class="max-pooling">
                      <strong>最大池化：</strong>
                      <div class="math-expression">
                        <strong>Y<sub>i,j</sub> = max<sub>(m,n) ∈ R<sub>i,j</sub></sub> X<sub>m,n</sub></strong>
                      </div>
                    </div>
                    <div class="avg-pooling">
                      <strong>平均池化：</strong>
                      <div class="math-expression">
                        <strong>Y<sub>i,j</sub> = (1/|R<sub>i,j</sub>|) × Σ<sub>(m,n) ∈ R<sub>i,j</sub></sub> X<sub>m,n</sub></strong>
                      </div>
                    </div>
                  </div>
                  <div class="detailed-explanation">
                    <h6>详细解释：</h6>
                    <ul class="formula-details">
                      <li><strong>Y_{i,j}：</strong>池化后输出特征图在位置(i,j)的值</li>
                      <li><strong>R_{i,j}：</strong>以(i,j)为中心的池化窗口区域，定义了参与池化计算的像素范围</li>
                      <li><strong>X_{m,n}：</strong>池化窗口内位置(m,n)处的输入值</li>
                      <li><strong>|R_{i,j}|：</strong>池化窗口的大小(像素数量)，如2×2窗口则|R_{i,j}|=4</li>
                    </ul>
                    <div class="pooling-comparison">
                      <h6>两种池化方式对比：</h6>
                      <div class="pooling-types">
                        <div class="max-pooling-detail">
                          <strong>最大池化：</strong>
                          <ul>
                            <li>选择窗口内的最大值作为输出</li>
                            <li>保留最显著的特征信息</li>
                            <li>对噪声具有一定的抗干扰能力</li>
                            <li>常用于特征检测任务</li>
                          </ul>
                        </div>
                        <div class="avg-pooling-detail">
                          <strong>平均池化：</strong>
                          <ul>
                            <li>计算窗口内所有值的平均数</li>
                            <li>保留整体的特征分布信息</li>
                            <li>具有平滑效果，减少特征的尖锐变化</li>
                            <li>常用于全局特征提取</li>
                          </ul>
                        </div>
                      </div>
                    </div>
                    <div class="computation-example">
                      <h6>计算示例(2×2池化)：</h6>
                      <p>输入矩阵: [1, 3; 2, 4] → 最大池化输出: 4, 平均池化输出: (1+3+2+4)/4 = 2.5</p>
                    </div>
                    <p class="practical-meaning">
                      <strong>实际意义：</strong>池化层通过降维减少计算量，同时保持重要特征信息，增强模型对输入变化的鲁棒性，是深度学习网络中不可缺少的组件。
                    </p>
                  </div>
                </div>
              </div>
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
                 src="/案例2/全连接层.png"
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
              <div class="math-formula">
                <h5>数学公式：</h5>
                <div class="formula-content">
                  <div class="math-expression">
                    <strong>Y = f(W · X + b)</strong>
                  </div>
                  <div class="activation-functions">
                    <div class="relu">
                      <strong>ReLU激活：</strong>
                      <div class="math-expression">
                        <strong>f(x) = max(0, x)</strong>
                      </div>
                    </div>
                    <div class="softmax">
                      <strong>Softmax输出：</strong>
                      <div class="math-expression">
                        <strong>σ(z<sub>i</sub>) = e<sup>z<sub>i</sub></sup> / Σ<sub>j=1</sub><sup>K</sup> e<sup>z<sub>j</sub></sup></strong>
                      </div>
                    </div>
                  </div>
                  <div class="detailed-explanation">
                    <h6>详细解释：</h6>
                    <ul class="formula-details">
                      <li><strong>Y：</strong>全连接层的输出向量，维度为输出神经元的数量</li>
                      <li><strong>W：</strong>权重矩阵，维度为[输出维度 × 输入维度]，每个元素表示连接强度</li>
                      <li><strong>X：</strong>输入向量，通常是前一层的输出或展平后的特征图</li>
                      <li><strong>b：</strong>偏置向量，为每个输出神经元添加的偏移量</li>
                      <li><strong>f：</strong>激活函数，引入非线性变换能力</li>
                    </ul>
                    <div class="activation-details">
                      <h6>激活函数详解：</h6>
                      <div class="activation-comparison">
                        <div class="relu-detail">
                          <strong>ReLU激活函数：</strong>
                          <ul>
                            <li>f(x) = max(0, x)：负值变为0，正值保持不变</li>
                            <li>优点：计算简单，缓解梯度消失问题</li>
                            <li>缺点：可能导致神经元"死亡"(输出恒为0)</li>
                            <li>适用：隐藏层，提供非线性变换</li>
                          </ul>
                        </div>
                        <div class="softmax-detail">
                          <strong>Softmax激活函数：</strong>
                          <ul>
                            <li>将输出转换为概率分布，所有输出和为1</li>
                            <li>z_i：第i个神经元的原始输出(logits)</li>
                            <li>K：输出类别总数</li>
                            <li>适用：多分类任务的输出层</li>
                          </ul>
                        </div>
                      </div>
                    </div>
                    <div class="computation-process">
                      <h6>计算过程：</h6>
                      <ol>
                        <li><strong>线性变换：</strong>计算 W·X + b，得到每个神经元的加权输入</li>
                        <li><strong>激活函数：</strong>对线性变换结果应用激活函数f</li>
                        <li><strong>输出生成：</strong>得到最终的输出向量Y</li>
                      </ol>
                    </div>
                    <div class="matrix-example">
                      <h6>矩阵运算示例：</h6>
                      <p>输入X=[x₁, x₂, x₃], 权重W=[[w₁₁, w₁₂, w₁₃], [w₂₁, w₂₂, w₂₃]], 偏置b=[b₁, b₂]</p>
                      <p>输出：Y₁ = f(w₁₁x₁ + w₁₂x₂ + w₁₃x₃ + b₁), Y₂ = f(w₂₁x₁ + w₂₂x₂ + w₂₃x₃ + b₂)</p>
                    </div>
                    <p class="practical-meaning">
                      <strong>实际意义：</strong>全连接层将前面层提取的局部特征进行全局整合，通过权重学习不同特征之间的关系，最终输出分类结果或回归值，是深度学习网络的"决策中心"。
                    </p>
                  </div>
                </div>
              </div>
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
</template>

<script>
export default {
  name: "DeepLearningShowcase",
  methods: {
    // 放大图片
    enlargeImage(event, type) {
      this.$emit('enlarge-image', event, type);
    }
  }
};
</script>

<style lang="scss" scoped>
.case-section {
  margin: 3rem 0;
  padding: 2rem;
  background: var(--card-bg);
  border-radius: 16px;
  border: 1px solid var(--border-color);
}

.section-header {
  text-align: center;
  margin-bottom: 3rem;
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin: 0 0 1rem 0;
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-color);
  position: relative;
}

.title-icon {
  font-size: 2.5rem;
  filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.3));
}

.title-text {
  background: var(--btn-primary-bg);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.title-decoration {
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: var(--btn-primary-bg);
  border-radius: 2px;
}

.section-subtitle {
  color: var(--text-secondary-color);
  font-size: 1.1rem;
  margin: 0;
  font-weight: 400;
}

.deep-learning-showcase {
  background: var(--card-bg);
  border-radius: 20px;
  border: 1px solid var(--border-color);
  backdrop-filter: blur(10px);
  padding: 2rem;
  box-shadow: var(--box-shadow);
}

.core-layers-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 2rem;
  margin-bottom: 3rem;
}

.layer-card {
  background: var(--card-bg);
  border-radius: 16px;
  border: 1px solid var(--border-color);
  overflow: hidden;
}

.layer-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.5rem;
  background: var(--card-header-bg);
  border-bottom: 1px solid var(--border-color);
}

.layer-icon {
  font-size: 1.8rem;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.layer-title {
  margin: 0;
  color: var(--text-color);
  font-size: 1.3rem;
  font-weight: 600;
}

.layer-content {
  padding: 1.5rem;
}

.layer-image {
  text-align: center;
  margin-bottom: 1.5rem;
}

.layer-demo {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  border: 2px solid rgba(255, 255, 255, 0.1);
}

.image-caption {
  margin-top: 0.5rem;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.85rem;
  font-style: italic;
}

.layer-description {
  color: rgba(255, 255, 255, 0.9);
  line-height: 1.6;
}

.layer-description > p {
  margin-bottom: 1rem;
  font-size: 1rem;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0 0 1.5rem 0;
}

.feature-list li {
  margin-bottom: 0.5rem;
  padding: 0.5rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 6px;
  border-left: 3px solid rgba(0, 191, 255, 0.6);
  font-size: 0.9rem;
}

.math-formula {
  background: rgba(255, 255, 255, 0.08);
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid rgba(255, 255, 255, 0.1);
  margin-top: 1rem;
}

.math-formula h5 {
  margin: 0 0 1rem 0;
  color: #ffffff;
  font-size: 1.1rem;
  font-weight: 600;
}

.formula-content {
  color: rgba(255, 255, 255, 0.9);
}

.math-expression {
  background: rgba(0, 0, 0, 0.3);
  padding: 1rem;
  border-radius: 8px;
  margin: 1rem 0;
  text-align: center;
  font-family: 'Times New Roman', serif;
  font-size: 1.1rem;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.detailed-explanation {
  margin-top: 1.5rem;
}

.detailed-explanation h6 {
  margin: 1rem 0 0.5rem 0;
  color: #ffffff;
  font-size: 1rem;
  font-weight: 600;
}

.formula-details {
  list-style: none;
  padding: 0;
  margin: 0.5rem 0;
}

.formula-details li {
  margin-bottom: 0.5rem;
  padding: 0.5rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 6px;
  font-size: 0.9rem;
  line-height: 1.4;
}

.computation-steps {
  margin: 1rem 0;
}

.computation-steps ol {
  padding-left: 1.5rem;
  margin: 0.5rem 0;
}

.computation-steps li {
  margin-bottom: 0.3rem;
  font-size: 0.9rem;
  line-height: 1.4;
}

.practical-meaning {
  margin-top: 1rem;
  padding: 1rem;
  background: rgba(0, 191, 255, 0.1);
  border-radius: 8px;
  border-left: 3px solid rgba(0, 191, 255, 0.6);
  font-size: 0.9rem;
  line-height: 1.5;
}

.formula-types {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin: 1rem 0;
}

.max-pooling, .avg-pooling {
  background: rgba(255, 255, 255, 0.05);
  padding: 1rem;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.pooling-comparison {
  margin: 1rem 0;
}

.pooling-types {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin: 0.5rem 0;
}

.max-pooling-detail, .avg-pooling-detail {
  background: rgba(255, 255, 255, 0.05);
  padding: 1rem;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.max-pooling-detail ul, .avg-pooling-detail ul {
  margin: 0.5rem 0;
  padding-left: 1.5rem;
}

.max-pooling-detail li, .avg-pooling-detail li {
  margin-bottom: 0.3rem;
  font-size: 0.85rem;
  line-height: 1.3;
}

.computation-example {
  margin: 1rem 0;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.computation-example h6 {
  margin: 0 0 0.5rem 0;
  color: #ffffff;
  font-size: 0.95rem;
  font-weight: 600;
}

.computation-example p {
  margin: 0;
  font-size: 0.9rem;
  font-family: 'Courier New', monospace;
  background: rgba(0, 0, 0, 0.3);
  padding: 0.5rem;
  border-radius: 4px;
}

.activation-functions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin: 1rem 0;
}

.relu, .softmax {
  background: rgba(255, 255, 255, 0.05);
  padding: 1rem;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.activation-details {
  margin: 1rem 0;
}

.activation-comparison {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin: 0.5rem 0;
}

.relu-detail, .softmax-detail {
  background: rgba(255, 255, 255, 0.05);
  padding: 1rem;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.relu-detail ul, .softmax-detail ul {
  margin: 0.5rem 0;
  padding-left: 1.5rem;
}

.relu-detail li, .softmax-detail li {
  margin-bottom: 0.3rem;
  font-size: 0.85rem;
  line-height: 1.3;
}

.computation-process {
  margin: 1rem 0;
}

.computation-process ol {
  padding-left: 1.5rem;
  margin: 0.5rem 0;
}

.computation-process li {
  margin-bottom: 0.3rem;
  font-size: 0.9rem;
  line-height: 1.4;
}

.matrix-example {
  margin: 1rem 0;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.matrix-example h6 {
  margin: 0 0 0.5rem 0;
  color: #ffffff;
  font-size: 0.95rem;
  font-weight: 600;
}

.matrix-example p {
  margin: 0.3rem 0;
  font-size: 0.85rem;
  font-family: 'Courier New', monospace;
  background: rgba(0, 0, 0, 0.3);
  padding: 0.5rem;
  border-radius: 4px;
  line-height: 1.4;
}

.learning-summary {
  background: linear-gradient(135deg, rgba(0, 191, 255, 0.1) 0%, rgba(0, 166, 230, 0.1) 100%);
  border-radius: 16px;
  padding: 2rem;
  border: 1px solid rgba(255, 255, 255, 0.1);
  text-align: center;
}

.learning-summary h4 {
  margin: 0 0 2rem 0;
  color: #ffffff;
  font-size: 1.4rem;
  font-weight: 600;
}

.workflow-steps {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.workflow-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  min-width: 150px;
}

.step-number {
  width: 30px;
  height: 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-weight: 600;
  font-size: 0.9rem;
}

.step-text {
  color: rgba(255, 255, 255, 0.9);
  font-size: 0.9rem;
  text-align: center;
  line-height: 1.3;
}

.workflow-arrow {
  color: rgba(255, 255, 255, 0.6);
  font-size: 1.5rem;
  font-weight: bold;
}

.learning-note {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.8rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  border-left: 3px solid rgba(0, 191, 255, 0.6);
}

.note-icon {
  font-size: 1.2rem;
  flex-shrink: 0;
}

.note-text {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9rem;
  line-height: 1.4;
}



/* 浅色主题适配 */
// 移除重复的.light-theme样式，使用统一的theme.css管理

.light-theme .layer-title {
  color: var(--chapter2-header-text);
}

.light-theme .layer-subtitle {
  color: var(--chapter2-header-secondary);
}

.light-theme .layer-description {
  color: var(--chapter2-header-secondary);
}

.light-theme .layer-description > p {
  color: var(--chapter2-header-secondary);
}

.light-theme .feature-list li {
  background: var(--chapter2-layer-demo-bg);
  border-left-color: var(--chapter2-layer-demo-border);
  color: var(--chapter2-header-secondary);
}

.light-theme .math-formula {
  background: var(--chapter2-chart-placeholder-bg);
  border-color: var(--chapter2-chart-placeholder-border);
}

.light-theme .math-formula h5 {
  color: var(--chapter2-header-text);
}

.light-theme .formula-content {
  color: var(--chapter2-header-secondary);
}

.light-theme .math-expression {
  background: var(--chapter2-network-bg);
  border-color: var(--chapter2-network-border);
  color: var(--chapter2-header-text);
}

.light-theme .detailed-explanation h6 {
  color: var(--chapter2-header-text);
}

.light-theme .formula-details li {
  background: var(--chapter2-layer-demo-bg);
  color: var(--chapter2-header-secondary);
}

.light-theme .computation-steps li {
  color: var(--chapter2-header-secondary);
}

.light-theme .practical-meaning {
  background: var(--chapter2-layer-demo-bg);
  border-left-color: var(--chapter2-layer-demo-border);
  color: var(--chapter2-header-secondary);
}

.light-theme .max-pooling,
.light-theme .avg-pooling,
.light-theme .max-pooling-detail,
.light-theme .avg-pooling-detail,
.light-theme .computation-example {
  background: var(--chapter2-chart-placeholder-bg);
  border-color: var(--chapter2-chart-placeholder-border);
  color: var(--chapter2-header-secondary);
}

.light-theme .computation-example h6 {
  color: var(--chapter2-header-text);
}

.light-theme .image-caption {
  color: var(--chapter2-chart-placeholder-text);
}

.light-theme .learning-summary {
  background: var(--chapter2-summary-bg);
  color: var(--chapter2-summary-text);
  border-color: rgba(59, 130, 246, 0.2);
  backdrop-filter: none;
}

.light-theme .learning-summary h4 {
  color: var(--chapter2-header-text);
}

.light-theme .summary-title {
  color: var(--chapter2-summary-text);
}

.light-theme .summary-content {
  color: var(--chapter2-summary-text);
}

.light-theme .workflow-step {
  background: var(--chapter2-layer-demo-bg);
  border-color: var(--chapter2-layer-demo-border);
  color: var(--chapter2-header-text);
}

.light-theme .step-text {
  color: var(--chapter2-header-text);
}

.light-theme .step-content h6 {
  color: var(--chapter2-header-text);
}

.light-theme .step-content p {
  color: var(--chapter2-header-secondary);
}

.light-theme .note-text {
  color: var(--chapter2-header-text) !important;
  font-weight: 500;
}

.light-theme .workflow-arrow {
  color: var(--chapter2-header-secondary);
}

.light-theme .relu-detail li,
.light-theme .softmax-detail li {
  color: var(--chapter2-header-secondary);
}

.light-theme .max-pooling-detail li,
.light-theme .avg-pooling-detail li {
  color: var(--chapter2-header-secondary);
}

.light-theme .relu,
.light-theme .softmax {
  background: var(--chapter2-chart-placeholder-bg);
  border-color: var(--chapter2-chart-placeholder-border);
  color: var(--chapter2-header-secondary);
}

.light-theme .relu-detail,
.light-theme .softmax-detail {
  background: var(--chapter2-chart-placeholder-bg);
  border-color: var(--chapter2-chart-placeholder-border);
  color: var(--chapter2-header-secondary);
}

.light-theme .matrix-example {
  background: var(--chapter2-chart-placeholder-bg);
  border-color: var(--chapter2-chart-placeholder-border);
  color: var(--chapter2-header-secondary);
}

.light-theme .matrix-example h6 {
  color: var(--chapter2-header-text);
}

.light-theme .matrix-example p {
  color: var(--chapter2-header-secondary);
}

.light-theme .image-caption,
.light-theme .layer-description,
.light-theme .formula-content,
.light-theme .computation-example h6,
.light-theme .layer-header h4,
.light-theme .layer-type,
.light-theme .pooling-type h5,
.light-theme .activation-function h5,
.light-theme .workflow-step h5,
.light-theme .learning-notes h5,
.light-theme .math-formula h5 {
  color: var(--chapter2-header-text) !important;
}

.light-theme .feature-list li,
.light-theme .note-text {
  color: var(--chapter2-text-secondary) !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .case-section {
    padding: 1.5rem;
  }
  
  .section-title {
    font-size: 1.5rem;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .title-icon {
    font-size: 2rem;
  }
  
  .deep-learning-showcase {
    padding: 1.5rem;
  }
  
  .core-layers-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  
  .layer-header {
    padding: 1rem;
  }
  
  .layer-content {
    padding: 1rem;
  }
  
  .formula-types {
    grid-template-columns: 1fr;
    gap: 0.5rem;
  }
  
  .pooling-types {
    grid-template-columns: 1fr;
    gap: 0.5rem;
  }
  
  .activation-functions {
    grid-template-columns: 1fr;
    gap: 0.5rem;
  }
  
  .activation-comparison {
    grid-template-columns: 1fr;
    gap: 0.5rem;
  }
  
  .workflow-steps {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .workflow-arrow {
    transform: rotate(90deg);
  }
  
  .learning-note {
    flex-direction: column;
    text-align: center;
    gap: 0.5rem;
  }
}

@media (max-width: 480px) {
  .case-section {
    padding: 1rem;
  }
  
  .deep-learning-showcase {
    padding: 1rem;
  }
  
  .layer-header {
    padding: 0.8rem;
    flex-direction: column;
    text-align: center;
    gap: 0.5rem;
  }
  
  .layer-content {
    padding: 0.8rem;
  }
  
  .math-formula {
    padding: 1rem;
  }
  
  .learning-summary {
    padding: 1.5rem;
  }
  
  .workflow-step {
    min-width: 120px;
    padding: 0.8rem;
  }
}
</style>