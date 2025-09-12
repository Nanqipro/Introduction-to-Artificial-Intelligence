<template>
  <div class="chapter2-case-study">
    <!-- 学习进度指示器 -->
    <ProgressIndicator 
      :regression-case-completed="regressionCaseCompleted"
      :deep-learning-case-completed="deepLearningCaseCompleted"
      :interaction-counts="interactionCounts"
    />

    <!-- 回归演示案例 -->
    <RegressionPlatform @redirect-to-regression="redirectToRegressionPlatform" />

    <!-- 深度学习核心层案例 -->
    <DeepLearningShowcase 
      :convolution-params="convolutionParams"
      :pooling-params="poolingParams"
      :fully-connected-params="fullyConnectedParams"
      :enlarged-image="enlargedImage"
      @enlarge-image="enlargeImage"
      @close-enlarged-image="closeEnlargedImage"
    />

    <!-- 案例总结和下一步提示 -->
    <CaseSummary 
      :overall-progress="overallProgress"
      @start-quiz="startQuiz"
    />
  </div>
</template>

<script>
// 导入子组件
import ProgressIndicator from './ProgressIndicator.vue';
import RegressionPlatform from './RegressionPlatform.vue';
import DeepLearningShowcase from './DeepLearningShowcase.vue';
import CaseSummary from './CaseSummary.vue';

export default {
  name: "Chapter2CaseStudy",
  components: {
    ProgressIndicator,
    RegressionPlatform,
    DeepLearningShowcase,
    CaseSummary
  },
  props: {
    theme: {
      type: String,
      default: "dark"
    }
  },
  emits: ["start-quiz"],
  data() {
    return {
      // 交互计数
      interactionCounts: {
        regressionGenerate: 0,
        convolutionDemo: 0,
        poolingDemo: 0,
        fullyConnectedDemo: 0
      },
      
      // 图片放大相关
      enlargedImage: {
        show: false,
        src: '',
        alt: ''
      }
    };
  },
  computed: {
    // 回归案例完成状态
    regressionCaseCompleted() {
      return this.interactionCounts.regressionGenerate >= 3;
    },
    
    // 深度学习案例完成状态
    deepLearningCaseCompleted() {
      return (
        this.interactionCounts.convolutionDemo >= 1 &&
        this.interactionCounts.poolingDemo >= 1 &&
        this.interactionCounts.fullyConnectedDemo >= 1
      );
    },
    
    // 总体进度
    overallProgress() {
      const completed = this.completedCases;
      return (completed / 2) * 100;
    },
    
    completedCases() {
      let count = 0;
      if (this.regressionCaseCompleted) count++;
      if (this.deepLearningCaseCompleted) count++;
      return count;
    }
  },
  methods: {
    // 跳转到回归演示实验室
    redirectToRegressionPlatform() {
      this.$router.push("/regression-demo");
    },
    
    // 放大图片
    enlargeImage(event, type) {
      const img = event.target;
      this.enlargedImage = {
        show: true,
        src: img.src,
        alt: img.alt
      };
      
      // 增加交互次数
      if (type === 'convolution') {
        this.interactionCounts.convolutionDemo++;
      } else if (type === 'pooling') {
        this.interactionCounts.poolingDemo++;
      } else if (type === 'fullyConnected') {
        this.interactionCounts.fullyConnectedDemo++;
      }
    },
    
    // 关闭放大图片
    closeEnlargedImage() {
      this.enlargedImage.show = false;
    },
    
    // 开始测验
    startQuiz() {
      this.$emit("start-quiz");
    }
  }
};
</script>

<style lang="scss" scoped>
.chapter2-case-study {
  padding: 2rem;
  background: var(--bg-color);
  min-height: 100vh;
  color: var(--text-color);
}



/* 响应式设计 */
@media (max-width: 768px) {
  .chapter2-case-study {
    padding: 1rem;
  }
}

@media (max-width: 480px) {
  .chapter2-case-study {
    padding: 0.5rem;
  }
}
</style>