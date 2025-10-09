<template>
  <div class="stats-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">学习统计</h1>
        <p class="page-description">查看你的学习进度、成绩统计和排行榜</p>
      </div>
    </div>

    <!-- 统计内容 -->
    <UserStats />
  </div>
</template>

<script>
import UserStats from '../components/QuizSystem/UserStats.vue'

export default {
  name: 'StatsPage',
  components: {
    UserStats
  }
}
</script>

<style lang="scss" scoped>

.stats-page {
  background: var(--secondary-color, #23272e);
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, var(--card-bg, #292c33), var(--secondary-color, #23272e));
  padding: 3rem 0;
  margin-bottom: 2rem;
  border-bottom: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
}

.header-content {
  max-width: var(--page-header-max-width, 1200px);
  margin: 0 auto;
  padding: 0 var(--page-header-padding, 2rem);
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

@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  
  .header-content {
    padding: 0 var(--page-padding, 1rem);
  }
}
</style> 