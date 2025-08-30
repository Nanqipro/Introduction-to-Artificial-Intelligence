<template>
  <div>
    <!-- 极简测试版本 -->
    <div style="background: #ff0000; color: white; padding: 20px; margin: 20px; font-size: 24px; text-align: center; border: 5px solid #000;">
      🎉 Chapter1Demo 组件已加载！🎉
      <br>
      当前时间: {{ currentTime }}
      <br>
      组件状态: 正常
      <br>
      <strong>如果你能看到这个红色框，说明组件正在工作！</strong>
    </div>
    
    <!-- 简单内容测试 -->
    <div style="background: #00ff00; color: black; padding: 20px; margin: 20px; border: 3px solid #000;">
      <h2 style="color: #000;">第一章案例演示</h2>
      <p style="color: #000;">这是一个测试页面，用来确认组件是否正常显示。</p>
      
      <!-- 图片测试 -->
      <div style="background: #ffff00; padding: 15px; margin: 15px; border: 2px solid #000;">
        <h3 style="color: #000;">图片测试</h3>
        <img src="/images/chapter1/A_真实照片.png" alt="测试图片" style="max-width: 300px; border: 2px solid #000;" />
        <p style="color: #000;">如果能看到上面的图片，说明图片路径正常</p>
      </div>
      
      <!-- 按钮测试 -->
      <div style="background: #ff00ff; padding: 15px; margin: 15px; border: 2px solid #000;">
        <h3 style="color: #000;">按钮测试</h3>
        <button @click="testClick" style="background: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px;">
          点击我测试交互
        </button>
        <p style="color: #000;">点击次数: {{ clickCount }}</p>
      </div>
      
      <!-- 数据测试 -->
      <div style="background: #00ffff; padding: 15px; margin: 15px; border: 2px solid #000;">
        <h3 style="color: #000;">数据测试</h3>
        <p style="color: #000;">题目总数: {{ quiz.length }}</p>
        <ul style="color: #000;">
          <li v-for="(q, index) in quiz.slice(0, 3)" :key="q.id">
            {{ index + 1 }}. {{ q.title }}
          </li>
        </ul>
      </div>
    </div>
    
    <!-- 额外测试 -->
    <div style="background: #ff8800; color: white; padding: 20px; margin: 20px; font-size: 18px; text-align: center; border: 3px solid #000;">
      🎯 额外测试：如果你能看到这个橙色框，说明组件完全正常！
      <br>
      <button @click="testClick" style="background: #000; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; margin: 10px;">
        再次测试按钮
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Chapter1Demo',
  data() {
    return {
      clickCount: 0,
      quiz: [
        { id: 'q1', title: '题目 1：雪纺裙的图片' },
        { id: 'q2', title: '题目 2：音频对比' },
        { id: 'q3', title: '题目 3：城市街景' },
        { id: 'q4', title: '题目 4：智慧农业' },
        { id: 'q5', title: '题目 5：猫咪视频' }
      ]
    }
  },
  computed: {
    currentTime() {
      return new Date().toLocaleTimeString();
    }
  },
  methods: {
    testClick() {
      this.clickCount++;
      console.log('按钮被点击了！', this.clickCount);
      alert(`按钮被点击了 ${this.clickCount} 次！`);
    }
  },
  mounted() {
    console.log('Chapter1Demo 组件已挂载！');
    alert('Chapter1Demo 组件已成功加载！');
  }
}
</script>

<style scoped>
/* 移除所有样式，使用内联样式 */
</style>
