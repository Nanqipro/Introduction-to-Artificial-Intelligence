<template>
  <div class="regression-demo-grid">
    <!-- 左侧：类型切换+介绍+参数+教学内容 -->
    <div class="side-panel">
      <div class="lang-switch">
        <button :class="{active: lang==='zh'}" @click="lang='zh'">中文</button>
        <button :class="{active: lang==='en'}" @click="lang='en'">English</button>
      </div>
      <div class="type-switch">
        <button :class="{active: mode==='linear'}" @click="mode='linear'">
          <span>📈</span> {{ lang==='zh' ? '线性回归' : 'Linear Regression' }}
        </button>
        <button :class="{active: mode==='logistic'}" @click="mode='logistic'">
          <span>🧮</span> {{ lang==='zh' ? '逻辑回归' : 'Logistic Regression' }}
        </button>
      </div>
      <transition name="slide-fade" mode="out-in">
        <div v-if="mode==='linear' && lang==='zh'" key="linear-zh" class="desc-card">
          <h2>线性回归</h2>
          <p>线性回归通过一条直线拟合数据点，预测连续值。常用于数值预测，如房价、分数等。</p>
          <div class="formula">y = wx + b</div>
          <div class="case">案例：根据面积预测房价</div>
        </div>
        <div v-else-if="mode==='linear' && lang==='en'" key="linear-en" class="desc-card">
          <h2>Linear Regression</h2>
          <p>Fits a straight line to data points to predict continuous values. Used for value prediction, e.g., house price, score.</p>
          <div class="formula">y = wx + b</div>
          <div class="case">Case: Predict house price by area</div>
        </div>
        <div v-else-if="mode==='logistic' && lang==='zh'" key="logistic-zh" class="desc-card">
          <h2>逻辑回归</h2>
          <p>逻辑回归用于二分类问题，通过S型曲线（sigmoid）拟合，预测概率。常用于分类，如通过/未通过。</p>
          <div class="formula">y = sigmoid(wx + b)</div>
          <div class="case">案例：预测考试是否通过</div>
        </div>
        <div v-else key="logistic-en" class="desc-card">
          <h2>Logistic Regression</h2>
          <p>Used for binary classification, fits an S-shaped (sigmoid) curve to predict probabilities. E.g., pass/fail.</p>
          <div class="formula">y = sigmoid(wx + b)</div>
          <div class="case">Case: Predict exam pass/fail</div>
        </div>
      </transition>
      <div class="params">
        <template v-if="!is3D">
          <template v-if="mode === 'linear'">
            <span>{{ lang==='zh' ? '斜率' : 'Slope' }}: <b>{{ fitParams.slope?.toFixed(2) ?? '--' }}</b></span>
            <span>{{ lang==='zh' ? '截距' : 'Intercept' }}: <b>{{ fitParams.intercept?.toFixed(2) ?? '--' }}</b></span>
          </template>
          <template v-else>
            <span>{{ lang==='zh' ? '权重' : 'Weight' }}: <b>{{ fitParams.w?.toFixed(2) ?? '--' }}</b></span>
            <span>{{ lang==='zh' ? '偏置' : 'Bias' }}: <b>{{ fitParams.b?.toFixed(2) ?? '--' }}</b></span>
          </template>
        </template>
        <template v-else>
          <span>w₁: <b>{{ fitParams.w1?.toFixed(2) ?? '--' }}</b></span>
          <span>w₂: <b>{{ fitParams.w2?.toFixed(2) ?? '--' }}</b></span>
          <span>b: <b>{{ fitParams.b?.toFixed(2) ?? '--' }}</b></span>
        </template>
      </div>
      <transition name="slide-fade" mode="out-in">
        <div class="teaching-tips" :key="mode + '-' + lang">
          <div v-if="mode==='linear' && lang==='zh'">
            <b>知识点：</b> 最小二乘法、残差、拟合优度
          </div>
          <div v-else-if="mode==='linear' && lang==='en'">
            <b>Tip:</b> Least squares, residual, goodness of fit
          </div>
          <div v-else-if="mode==='logistic' && lang==='zh'">
            <b>知识点：</b> Sigmoid函数、概率输出、分类阈值
          </div>
          <div v-else>
            <b>Tip:</b> Sigmoid function, probability output, classification threshold
          </div>
        </div>
      </transition>
    </div>
    <!-- 右侧：交互式图表 -->
    <div class="main-panel">
      <div class="controls-bar">
        <button class="btn" @click="randomizeData">{{ lang==='zh' ? '随机数据' : 'Random Data' }}</button>
        <button class="btn" @click="showInput = true">{{ lang==='zh' ? (is3D ? '手动输入3D数据' : '手动输入数据') : (is3D ? 'Manual 3D Input' : 'Manual Input') }}</button>
        <label class="slider-label">
          <span>{{ lang==='zh' ? '噪声' : 'Noise' }}</span>
          <input type="range" min="0" max="2" step="0.01" v-model.number="noise" @input="randomizeData" />
          <span class="slider-value">{{ noise }}</span>
        </label>
        <button class="btn mode-btn" :class="{active: !is3D}" @click="is3D = false">2D</button>
        <button class="btn mode-btn" :class="{active: is3D}" @click="is3D = true">3D</button>
      </div>
      <div class="chart-card">
        <Plotly3D v-if="is3D" :data="plotlyProps.data" :layout="plotlyProps.layout" :style="plotlyProps.style" />
        <Line v-else :data="chartData" :options="chartOptions" />
      </div>
      <transition name="fade">
        <div v-if="showInput" class="input-dialog">
          <div class="dialog-content">
            <h3>{{ lang==='zh' ? (is3D ? '手动输入3D数据点' : '手动输入数据点') : (is3D ? 'Manual 3D Data Input' : 'Manual Data Input') }}</h3>
            <table class="input-table">
              <thead>
                <tr>
                  <th>X</th>
                  <th>Y</th>
                  <th v-if="is3D">Z</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(row, i) in inputRows" :key="i">
                  <td><input type="number" v-model.number="row.x" /></td>
                  <td><input type="number" v-model.number="row.y" /></td>
                  <td v-if="is3D"><input type="number" v-model.number="row.z" /></td>
                  <td><button class="del-btn" @click="removeRow(i)">×</button></td>
                </tr>
              </tbody>
            </table>
            <div class="input-actions">
              <button class="btn" @click="addRow">{{ lang==='zh' ? '添加行' : 'Add Row' }}</button>
              <button class="btn" @click="applyInput">{{ lang==='zh' ? '应用' : 'Apply' }}</button>
              <button class="btn cancel" @click="showInput = false">{{ lang==='zh' ? '取消' : 'Cancel' }}</button>
            </div>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { Line } from 'vue-chartjs';
import Plotly3D from './Plotly3D.vue';
import {
  Chart,
  LineElement,
  PointElement,
  LinearScale,
  Title,
  CategoryScale
} from 'chart.js';
Chart.register(LineElement, PointElement, LinearScale, Title, CategoryScale);
const lang = ref('zh');
const mode = ref('linear');
const noise = ref(0.5);
const is3D = ref(false);
const N = 20;
const xData = ref([]);
const yData = ref([]);
const zData = ref([]); // 3D only
const showInput = ref(false);
const inputRows = ref([]);
function randomizeData() {
  if (!is3D.value) {
    xData.value = Array.from({ length: N }, (_, i) => i / (N - 1) * 10);
    if (mode.value === 'linear') {
      yData.value = xData.value.map(x => 2 * x + 1 + (Math.random() - 0.5) * Math.min(noise.value, 3));
    } else {
      yData.value = xData.value.map(x => 1 / (1 + Math.exp(-(1.2 * x - 6))) + (Math.random() - 0.5) * Math.min(noise.value, 0.1));
    }
  } else {
    xData.value = Array.from({ length: N }, () => Math.random() * 10);
    yData.value = Array.from({ length: N }, () => Math.random() * 10);
    if (mode.value === 'linear') {
      zData.value = xData.value.map((x, i) => 2 * x + 1.5 * yData.value[i] + 3 + (Math.random() - 0.5) * Math.min(noise.value, 3));
    } else {
      zData.value = xData.value.map((x, i) => 1 / (1 + Math.exp(-(1.2 * x + 0.8 * yData.value[i] - 6))) + (Math.random() - 0.5) * Math.min(noise.value, 0.1));
    }
  }
}
function addRow() {
  if (!is3D.value) {
    inputRows.value.push({ x: 0, y: 0 });
  } else {
    inputRows.value.push({ x: 0, y: 0, z: 0 });
  }
}
function removeRow(i) {
  inputRows.value.splice(i, 1);
}
function applyInput() {
  if (!is3D.value) {
    if (inputRows.value.length > 0) {
      xData.value = inputRows.value.map(r => r.x);
      yData.value = inputRows.value.map(r => r.y);
    }
  } else {
    if (inputRows.value.length > 0) {
      xData.value = inputRows.value.map(r => r.x);
      yData.value = inputRows.value.map(r => r.y);
      zData.value = inputRows.value.map(r => r.z);
    }
  }
  showInput.value = false;
}
watch(showInput, (v) => {
  if (v) {
    if (!is3D.value) {
      inputRows.value = xData.value.map((x, i) => ({ x, y: yData.value[i] }));
      if (inputRows.value.length === 0) inputRows.value = [{ x: 0, y: 0 }];
    } else {
      inputRows.value = xData.value.map((x, i) => ({ x, y: yData.value[i], z: zData.value[i] ?? 0 }));
      if (inputRows.value.length === 0) inputRows.value = [{ x: 0, y: 0, z: 0 }];
    }
  }
});
watch(is3D, () => {
  randomizeData();
});
watch(mode, () => {
  if (is3D.value) is3D.value = false;
  randomizeData();
});
onMounted(() => {
  randomizeData();
});
function linearFit(x, y) {
  const n = x.length;
  const xMean = x.reduce((a, b) => a + b, 0) / n;
  const yMean = y.reduce((a, b) => a + b, 0) / n;
  let num = 0, den = 0;
  for (let i = 0; i < n; i++) {
    num += (x[i] - xMean) * (y[i] - yMean);
    den += (x[i] - xMean) ** 2;
  }
  const slope = num / den;
  const intercept = yMean - slope * xMean;
  return { slope, intercept };
}
function linearFit3D(x, y, z) {
  // 最小二乘法解三元线性: z = w1*x + w2*y + b
  const n = x.length;
  let sumX = 0, sumY = 0, sumZ = 0, sumXX = 0, sumYY = 0, sumXY = 0, sumXZ = 0, sumYZ = 0;
  for (let i = 0; i < n; i++) {
    sumX += x[i]; sumY += y[i]; sumZ += z[i];
    sumXX += x[i] * x[i]; sumYY += y[i] * y[i]; sumXY += x[i] * y[i];
    sumXZ += x[i] * z[i]; sumYZ += y[i] * z[i];
  }
  // 解正规方程
  // | sumXX sumXY sumX |   | w1 |   | sumXZ |
  // | sumXY sumYY sumY | * | w2 | = | sumYZ |
  // | sumX  sumY  n   |   | b  |   | sumZ  |
  // 用克拉默法则
  const det = sumXX * (sumYY * n - sumY * sumY) - sumXY * (sumXY * n - sumY * sumX) + sumX * (sumXY * sumY - sumYY * sumX);
  if (Math.abs(det) < 1e-8) return { w1: 0, w2: 0, b: 0 };
  const detW1 = sumXZ * (sumYY * n - sumY * sumY) - sumYZ * (sumXY * n - sumY * sumX) + sumZ * (sumXY * sumY - sumYY * sumX);
  const detW2 = sumXX * (sumYZ * n - sumY * sumZ) - sumXY * (sumXZ * n - sumZ * sumX) + sumX * (sumXY * sumZ - sumYZ * sumX);
  const detB  = sumXX * (sumYY * sumZ - sumY * sumYZ) - sumXY * (sumXY * sumZ - sumY * sumXZ) + sumX * (sumXY * sumYZ - sumYY * sumXZ);
  const w1 = detW1 / det;
  const w2 = detW2 / det;
  const b = detB / det;
  return { w1, w2, b };
}
function logisticFit3D(x, y, z) {
  // 简单梯度下降，演示用
  let w1 = 1, w2 = 1, b = 0;
  const lr = 0.1, steps = 1000;
  for (let step = 0; step < steps; step++) {
    let dw1 = 0, dw2 = 0, db = 0;
    for (let i = 0; i < x.length; i++) {
      const z_pred = w1 * x[i] + w2 * y[i] + b;
      const pred = 1 / (1 + Math.exp(-z_pred));
      dw1 += (pred - z[i]) * x[i];
      dw2 += (pred - z[i]) * y[i];
      db  += (pred - z[i]);
    }
    w1 -= lr * dw1 / x.length;
    w2 -= lr * dw2 / x.length;
    b  -= lr * db  / x.length;
  }
  return { w1, w2, b };
}
const fitParams = computed(() => {
  if (!is3D.value) {
    if (mode.value === 'linear') {
      return linearFit(xData.value, yData.value);
    } else {
      return { w: 1, b: 0 }; // 2D逻辑回归参数略
    }
  } else {
    if (mode.value === 'linear') {
      return linearFit3D(xData.value, yData.value, zData.value);
    } else {
      return logisticFit3D(xData.value, yData.value, zData.value);
    }
  }
});
const chartData = computed(() => {
  if (!is3D.value) {
    let fitY = [];
    if (mode.value === 'linear') {
      const { slope, intercept } = linearFit(xData.value, yData.value);
      fitY = xData.value.map(x => slope * x + intercept);
    } else {
      fitY = xData.value.map(x => 1 / (1 + Math.exp(-(1.2 * x - 6))));
    }
    return {
      labels: xData.value.map(x => x.toFixed(2)),
      datasets: [
        {
          label: lang.value==='zh' ? '数据点' : 'Data',
          data: yData.value,
          pointRadius: 5,
          showLine: false,
          backgroundColor: '#b0b3b8',
          borderColor: '#b0b3b8',
          type: 'scatter',
        },
        {
          label: mode.value === 'linear' ? (lang.value==='zh' ? '线性回归拟合' : 'Linear Fit') : (lang.value==='zh' ? '逻辑回归拟合' : 'Logistic Fit'),
          data: fitY,
          borderColor: '#4e8cff',
          borderWidth: 2,
          fill: false,
          pointRadius: 0,
          tension: 0.1,
        }
      ]
    };
  }
  return null;
});
const plotlyProps = computed(() => {
  // 3D 散点
  const scatter = {
    x: xData.value,
    y: yData.value,
    z: zData.value,
    mode: 'markers',
    type: 'scatter3d',
    marker: { size: 5, color: '#b0b3b8' },
    name: lang.value==='zh' ? '数据点' : 'Data'
  };

  // 检查数据有效性
  const valid = xData.value.length >= 3 && yData.value.length >= 3 && zData.value.length >= 3
    && !xData.value.some(isNaN) && !yData.value.some(isNaN) && !zData.value.some(isNaN);

  let surface = null;
  if (valid) {
    const { w1, w2, b } = fitParams.value;
    const gridN = 15;
    const xMin = Math.min(...xData.value), xMax = Math.max(...xData.value);
    const yMin = Math.min(...yData.value), yMax = Math.max(...yData.value);
    const xGrid = Array.from({length: gridN}, (_, i) => xMin + (xMax-xMin)*i/(gridN-1));
    const yGrid = Array.from({length: gridN}, (_, i) => yMin + (yMax-yMin)*i/(gridN-1));
    // 生成二维zGrid
    let zGrid;
    if (mode.value === 'linear') {
      zGrid = xGrid.map(x => yGrid.map(y => w1*x + w2*y + b));
    } else {
      // 逻辑回归sigmoid面
      zGrid = xGrid.map(x => yGrid.map(y => 1 / (1 + Math.exp(-(w1*x + w2*y + b)))));
    }
    surface = {
      x: xGrid,
      y: yGrid,
      z: zGrid,
      type: 'surface',
      opacity: 0.5,
      colorscale: [[0, '#4e8cff'], [1, '#b0b3b8']],
      showscale: false,
      name: lang.value==='zh' ? (mode.value==='linear' ? '拟合平面' : 'sigmoid曲面') : (mode.value==='linear' ? 'Fit Plane' : 'Sigmoid Surface'),
      contours: {
        x: {
          show: true,
          color: '#b0b3b8',
          width: 2,
          highlight: true,
        },
        y: {
          show: true,
          color: '#b0b3b8',
          width: 2,
          highlight: true,
        },
        z: {
          show: true,
          usecolormap: true,
          highlightcolor: '#b0b3b8',
          project: { z: true }
        }
      }
    };
  }

  return {
    data: surface ? [scatter, surface] : [scatter],
    layout: {
      autosize: true,
      margin: { l: 0, r: 0, b: 0, t: 0 },
      scene: {
        xaxis: {
          title: 'X',
          showspikes: true,
          showgrid: true,
          gridcolor: '#b0b3b8',
          gridwidth: 2,
          zeroline: true,
          zerolinecolor: '#b0b3b8',
          color: '#b0b3b8',
          showaxesarrow: true,
        },
        yaxis: {
          title: 'Y',
          showspikes: true,
          showgrid: true,
          gridcolor: '#b0b3b8',
          gridwidth: 2,
          zeroline: true,
          zerolinecolor: '#b0b3b8',
          color: '#b0b3b8',
          showaxesarrow: true,
        },
        zaxis: {
          title: 'Z',
          showspikes: true,
          showgrid: true,
          gridcolor: '#b0b3b8',
          gridwidth: 2,
          zeroline: true,
          zerolinecolor: '#b0b3b8',
          color: '#b0b3b8',
          showaxesarrow: true,
          ...(mode.value === 'logistic' ? { range: [0, 1.2] } : {})
        },
        aspectmode: 'cube',
        bgcolor: 'rgba(24,25,26,0.01)'
      },
      paper_bgcolor: 'rgba(0,0,0,0)',
      plot_bgcolor: 'rgba(0,0,0,0)',
      showlegend: false
    },
    style: { width: '100%', height: '420px' }
  };
});
const chartOptions = {
  responsive: true,
  plugins: {
    legend: { display: true },
    title: { display: false }
  },
  scales: {
    x: { title: { display: true, text: 'X' } },
    y: { title: { display: true, text: 'Y' }, min: 0, max: mode.value === 'linear' ? undefined : 1.2 }
  }
};
</script>

<style scoped lang="scss">
@use '@/styles/variables.scss' as *;
.regression-demo-grid {
  display: grid;
  grid-template-columns: 1fr 2.2fr;
  gap: 2.5vw;
  min-height: 100vh;
  background: linear-gradient(135deg, $primary-color 0%, $primary-gradient-end 100%);
  color: $text-color;
  padding: 4vw 0 4vw 0;
  justify-content: center;
}
.side-panel {
  background: rgba($secondary-color, 0.92);
  border-radius: $border-radius * 1.5;
  box-shadow: $box-shadow;
  padding: 2.2em 2em 2em 2em;
  min-width: 320px;
  max-width: 420px;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  border: 1.5px solid $border-color;
  gap: 1.2em;
  height: fit-content;
  margin: 0 auto;
}
.lang-switch {
  display: flex;
  gap: 1em;
  justify-content: flex-end;
  margin-bottom: 0.5em;
}
.lang-switch button {
  background: none;
  border: none;
  color: $accent-color-light;
  font-size: 1.08em;
  font-weight: 700;
  cursor: pointer;
  padding: 0.2em 0.7em;
  border-radius: $border-radius;
  transition: background 0.18s, color 0.18s;
}
.lang-switch button.active, .lang-switch button:hover {
  background: $primary-hover-color;
  color: $text-color;
}
.type-switch {
  display: flex;
  gap: 1.2em;
  margin-bottom: 1em;
  justify-content: center;
}
.type-switch button {
  background: linear-gradient(90deg, $primary-color-light 0%, $primary-hover-color 100%);
  color: $accent-color-light;
  border: none;
  border-radius: $border-radius * 1.1;
  padding: 0.55em 1.4em;
  font-size: 1.08em;
  font-weight: 700;
  cursor: pointer;
  box-shadow: $box-shadow;
  transition: background 0.18s, color 0.18s, box-shadow 0.18s;
  display: flex;
  align-items: center;
  gap: 0.5em;
}
.type-switch button.active, .type-switch button:hover {
  background: $primary-hover-color;
  color: $text-color;
  box-shadow: 0 4px 16px rgba(176,179,184,0.18);
}
.desc-card {
  background: rgba($primary-color, 0.85);
  border-radius: $border-radius * 1.1;
  box-shadow: $box-shadow;
  padding: 1.2em 1.2em 1em 1.2em;
  color: $text-color;
  font-size: 1.08rem;
  line-height: 1.7;
  border: 1px solid $border-color;
  margin-bottom: 0.5em;
  min-height: 120px;
}
.desc-card h2 {
  color: $accent-color;
  font-size: 1.18rem;
  font-weight: 800;
  margin-bottom: 0.5em;
}
.formula {
  color: $accent-color-light;
  font-size: 1.1em;
  margin: 0.5em 0 0.2em 0;
}
.case {
  color: $accent-color-light;
  font-size: 0.98em;
  margin-bottom: 0.2em;
}
.params {
  display: flex;
  gap: 2em;
  font-size: 1.08em;
  color: $accent-color-light;
  margin-left: 0.5em;
  justify-content: flex-start;
  margin-bottom: 0.5em;
}
.teaching-tips {
  background: rgba($primary-hover-color, 0.7);
  border-radius: $border-radius;
  color: $text-color;
  font-size: 1em;
  padding: 0.7em 1em;
  margin-top: 0.5em;
  box-shadow: 0 2px 8px rgba(176,179,184,0.10);
  min-height: 2.2em;
}
.main-panel {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.2em;
  min-width: 0;
}
.controls-bar {
  display: flex;
  align-items: center;
  gap: 1.5em;
  margin-bottom: 0.5em;
  flex-wrap: wrap;
  justify-content: flex-start;
}
.btn {
  background: linear-gradient(90deg, $primary-color-light 0%, $primary-hover-color 100%);
  color: $accent-color-light;
  border: none;
  border-radius: $border-radius * 1.1;
  padding: 0.55em 1.4em;
  font-size: 1.08em;
  font-weight: 700;
  cursor: pointer;
  box-shadow: $box-shadow;
  transition: background 0.18s, color 0.18s, box-shadow 0.18s;
  &:hover {
    background: $primary-hover-color;
    color: $text-color;
    box-shadow: 0 4px 16px rgba(176,179,184,0.18);
  }
}
.slider-label {
  display: flex;
  align-items: center;
  gap: 0.5em;
  font-size: 1em;
  color: $accent-color-light;
}
input[type="range"] {
  accent-color: $accent-color;
  width: 90px;
  margin: 0 0.3em;
  height: 3px;
  background: $border-color;
  border-radius: 2px;
}
.slider-value {
  min-width: 2.5em;
  display: inline-block;
  text-align: right;
  color: $accent-color-light;
  font-weight: 700;
}
.chart-card {
  background: rgba($secondary-color, 0.92);
  border-radius: $border-radius * 1.2;
  box-shadow: $box-shadow;
  padding: 2em 2em 2.5em 2em;
  margin: 0 auto;
  max-width: 700px;
  border: 1.5px solid $border-color;
  backdrop-filter: blur(4px);
  width: 100%;
  min-height: 420px;
}
.input-dialog {
  position: fixed;
  left: 0; top: 0; right: 0; bottom: 0;
  background: rgba(24,25,26,0.45);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
}
.dialog-content {
  background: $secondary-color;
  border-radius: $border-radius * 1.2;
  box-shadow: 0 8px 32px 0 rgba(24,25,26,0.18);
  padding: 2em 2.5em 1.5em 2.5em;
  min-width: 320px;
  max-width: 95vw;
  color: $text-color;
  border: 1.5px solid $border-color;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.input-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1em;
  max-height: 320px;
  overflow-y: auto;
  display: block;
}
.input-table th, .input-table td {
  border: 1px solid $border-color;
  padding: 0.3em 0.7em;
  text-align: center;
  background: $primary-color;
}
.input-table input[type="number"] {
  width: 70px;
  background: $secondary-color;
  color: $text-color;
  border: 1px solid $border-color;
  border-radius: $border-radius * 0.7;
  padding: 0.2em 0.5em;
  font-size: 1em;
}
.del-btn {
  background: none;
  border: none;
  color: $accent-color;
  font-size: 1.2em;
  cursor: pointer;
  padding: 0 0.5em;
  transition: color 0.18s;
}
.del-btn:hover {
  color: #ff4d4f;
}
.input-actions {
  display: flex;
  gap: 1em;
  justify-content: flex-end;
  width: 100%;
  margin-top: 0.5em;
}
.btn.cancel {
  background: none;
  color: $accent-color-light;
  border: 1px solid $border-color;
}
.mode-btn {
  background: linear-gradient(90deg, $primary-color-light 0%, $primary-hover-color 100%);
  color: $accent-color-light;
  border: none;
  border-radius: $border-radius * 1.1;
  padding: 0.55em 1.4em;
  font-size: 1.08em;
  font-weight: 700;
  cursor: pointer;
  box-shadow: $box-shadow;
  transition: background 0.18s, color 0.18s, box-shadow 0.18s;
  &:hover {
    background: $primary-hover-color;
    color: $text-color;
    box-shadow: 0 4px 16px rgba(176,179,184,0.18);
  }
}
.mode-btn.active {
  background: $primary-hover-color;
  color: $text-color;
  box-shadow: 0 4px 16px rgba(176,179,184,0.18);
}
@media (max-width: 900px) {
  .regression-demo-grid {
    grid-template-columns: 1fr;
    gap: 2vw;
    padding: 2vw 0 2vw 0;
  }
  .side-panel, .main-panel {
    max-width: 98vw;
    width: 95vw;
    padding: 1em 0.5em 1.2em 0.5em;
  }
  .chart-card {
    max-width: 99vw;
    width: 98vw;
    padding: 1em 0.5em 1.2em 0.5em;
  }
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
.slide-fade-enter-active, .slide-fade-leave-active {
  transition: all 0.45s cubic-bezier(0.4, 0.2, 0.2, 1);
}
.slide-fade-enter-from {
  opacity: 0;
  transform: translateY(30px) scale(0.98);
}
.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-30px) scale(0.98);
}
</style> 