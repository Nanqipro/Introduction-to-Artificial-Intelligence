<template>
  <div>
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
  name: "CaseSummary"
};
</script>

<style lang="scss" scoped>
.case-summary {
  margin: 3rem 0;
  padding: 2rem;
  background: linear-gradient(135deg, rgba(76, 175, 80, 0.1) 0%, rgba(139, 195, 74, 0.1) 100%);
  border-radius: 16px;
  border: 1px solid rgba(76, 175, 80, 0.3);
  backdrop-filter: blur(10px);
}

.summary-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.8rem;
  margin: 0 0 2rem 0;
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--text-color);
  text-align: center;
}

.summary-content {
  display: grid;
  gap: 2rem;
}

.summary-item {
  background: var(--card-bg);
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid var(--border-color);
}

.summary-item h4 {
  margin: 0 0 1rem 0;
  color: var(--text-color);
  font-size: 1.3rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.summary-item ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.summary-item li {
  margin-bottom: 0.8rem;
  padding: 0.8rem;
  background: var(--card-bg);
  border-radius: 8px;
  border-left: 3px solid var(--success-color);
  color: var(--text-secondary-color);
  font-size: 0.95rem;
  line-height: 1.5;
}

.summary-item li:last-child {
  margin-bottom: 0;
}

.next-step {
  margin: 3rem 0;
  padding: 0;
}

.next-step-content {
  background: var(--card-bg);
  border-radius: 20px;
  border: 1px solid var(--border-color);
  backdrop-filter: blur(15px);
  overflow: hidden;
  position: relative;
}

.next-step-content::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.05) 50%, transparent 70%);
  background-size: 200% 200%;
  animation: shimmer 3s ease-in-out infinite;
  pointer-events: none;
}

@keyframes shimmer {
  0% { background-position: -200% -200%; }
  50% { background-position: 200% 200%; }
  100% { background-position: -200% -200%; }
}

.completion-celebration {
  padding: 3rem 2rem;
  text-align: center;
  position: relative;
  z-index: 2;
}

.completion-celebration h3 {
  margin: 0 0 1.5rem 0;
  font-size: 2.2rem;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 2px 10px rgba(0, 191, 255, 0.3);
  animation: celebration-glow 2s ease-in-out infinite alternate;
}

@keyframes celebration-glow {
  from {
    filter: drop-shadow(0 0 5px rgba(0, 191, 255, 0.5));
  }
  to {
    filter: drop-shadow(0 0 20px rgba(0, 191, 255, 0.8));
  }
}

.completion-celebration p {
  margin: 0;
  color: rgba(255, 255, 255, 0.9);
  font-size: 1.2rem;
  line-height: 1.6;
  max-width: 600px;
  margin: 0 auto;
  font-weight: 400;
}

/* 浅色主题样式已移至统一的theme.css管理 */

/* 响应式设计 */
@media (max-width: 768px) {
  .case-summary {
    padding: 1.5rem;
  }
  
  .summary-title {
    font-size: 1.5rem;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .summary-item {
    padding: 1rem;
  }
  
  .summary-item h4 {
    font-size: 1.1rem;
  }
  
  .summary-item li {
    font-size: 0.9rem;
    padding: 0.6rem;
  }
  
  .completion-celebration {
    padding: 2rem 1.5rem;
  }
  
  .completion-celebration h3 {
    font-size: 1.8rem;
  }
  
  .completion-celebration p {
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .case-summary {
    padding: 1rem;
  }
  
  .summary-item {
    padding: 0.8rem;
  }
  
  .completion-celebration {
    padding: 1.5rem 1rem;
  }
  
  .completion-celebration h3 {
    font-size: 1.5rem;
  }
  
  .completion-celebration p {
    font-size: 0.9rem;
  }
}
</style>