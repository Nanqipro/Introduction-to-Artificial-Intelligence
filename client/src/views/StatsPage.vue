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
  background: $secondary-color;
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, $card-bg, $secondary-color);
  padding: 3rem 0;
  margin-bottom: 2rem;
  border-bottom: 1px solid $card-border;
}

.header-content {
  max-width: $page-header-max-width;
  margin: 0 auto;
  padding: 0 $page-header-padding;
  text-align: center;
}

.page-title {
  font-size: 2.5rem;
  color: $text-color;
  margin-bottom: 1rem;
  font-weight: 700;
  letter-spacing: 1px;
}

.page-description {
  font-size: 1.1rem;
  color: $text-secondary-color;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  
  .header-content {
    padding: 0 $page-padding;
  }
}
</style> 