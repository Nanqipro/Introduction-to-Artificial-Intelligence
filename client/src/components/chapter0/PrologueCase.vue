<template>
  <section class="prologue-case">
    <header class="case-header">
      <h2 class="case-title">🎬 序章案例演示</h2>
      <p class="case-desc">观看序章预热视频，快速了解本课程的学习内容与形式</p>
      <div v-if="videoCompleted" class="completion-indicator">
        <span class="completion-badge">✅ 视频观看完成</span>
      </div>
    </header>

    <div class="video-card">
      <video
        ref="videoPlayer"
        class="video-player"
        controls
        controlsList="nodownload"
        preload="metadata"
        :poster="posterUrl"
        @ended="onVideoEnded"
        @timeupdate="onTimeUpdate"
      >
        <source src="/videos/prologue/preVideo.mp4" type="video/mp4" />
        您的浏览器不支持视频播放，请下载观看。
      </video>
      <div class="video-meta">
        <div class="video-buttons">
          <button class="btn btn-primary" @click="restart">⏮ 重新播放</button>
          <a class="btn btn-outline" href="/videos/prologue/preVideo.mp4" download>⬇️ 下载视频</a>
        </div>
        <div class="watch-progress">
          <span class="progress-text">观看进度: {{ Math.round(watchProgress) }}%</span>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'PrologueCase',
  props: {
    posterUrl: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      videoCompleted: false,
      watchProgress: 0,
      hasWatchedMostOfVideo: false // 观看超过80%即可认为完成
    }
  },
  methods: {
    restart() {
      const video = this.$refs.videoPlayer
      if (video) {
        video.currentTime = 0
        video.play()
      }
    },
    onTimeUpdate() {
      const video = this.$refs.videoPlayer
      if (video && video.duration) {
        this.watchProgress = (video.currentTime / video.duration) * 100
        
        // 当观看进度超过80%时，认为已完成观看
        if (this.watchProgress >= 80 && !this.hasWatchedMostOfVideo) {
          this.hasWatchedMostOfVideo = true
          this.markVideoCompleted()
        }
      }
    },
    onVideoEnded() {
      // 视频播放完成
      if (!this.videoCompleted) {
        this.markVideoCompleted()
      }
    },
    markVideoCompleted() {
      this.videoCompleted = true
      this.watchProgress = 100
      
      // 显示完成提示
      this.$message.success('🎉 序章视频观看完成！')
      
      // 触发序章完成事件
      this.$emit('video-completed')
      
      // 调用章节完成逻辑
      this.addChapterExperience()
    },
    async addChapterExperience() {
      // 检查用户是否登录
      const token = localStorage.getItem('token')
      if (!token) {
        // 用户未登录，跳过经验值添加
        return
      }
      
      try {
        const { levelApi } = await import('../../services/api')
        
        // 调用章节完成接口（序章通过视频完成）
        const response = await levelApi.completeChapter({
          chapterId: 0, // 序章ID为0
          completionType: 'video',
          score: 100
        })
        
        if (response && response.code === 200) {
          const result = response.data
          // 序章完成标记成功
          
          // 显示完成提示
          this.$message.success('🎉 序章完成！获得经验值奖励！')
          
          // 发送全局事件通知经验值更新
          window.dispatchEvent(new CustomEvent('experienceUpdated', {
            detail: {
              experienceGained: result.experienceGained || 50,
              newExperience: result.experience,
              newLevel: result.newLevel,
              leveledUp: result.levelUp,
              activityType: 'chapter',
              chapterId: 0
            }
          }))
          
          // 检查是否升级
          if (result.levelUp) {
            this.$notify({
              title: '🎉 恭喜升级！',
              message: result.levelUpMessage || `恭喜升级到 ${result.newLevel} 级！`,
              type: 'success',
              duration: 5000
            })
          }
        }
      } catch (error) {
        // 序章完成标记失败
        this.$message.error('序章完成标记失败，请稍后重试')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.prologue-case {
  background: var(--secondary-color, #23272e);
  border-radius: 16px;
  padding: 1.5rem;
  border: 1px solid var(--border-color, rgba(57, 59, 64, 0.18));
}

.case-header {
  text-align: center;
  margin-bottom: 1rem;
  background: linear-gradient(135deg, rgba(var(--accent-color-rgb, 59, 130, 246), 0.15), rgba(var(--accent-color-rgb, 59, 130, 246), 0.05));
  border: 1px solid rgba(var(--accent-color-rgb, 59, 130, 246), 0.25);
  border-radius: 12px;
  padding: 1rem;
}

.case-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-color, #f5f6fa);
}

.case-desc {
  color: var(--text-secondary-color, #b0b3b8);
}

.completion-indicator {
  margin-top: 1rem;
  text-align: center;
}

.completion-badge {
  display: inline-block;
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
}

.video-card {
  background: var(--card-bg, #292c33);
  border: 1px solid var(--card-border, rgba(57, 59, 64, 0.18));
  border-radius: 12px;
  padding: 1rem;
}

.video-player {
  width: 100%;
  aspect-ratio: 16 / 9;
  height: auto;
  max-height: 85vh;
  border-radius: 12px;
  outline: none;
  background: #000;
}

.video-meta {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  align-items: center;
  margin-top: 0.75rem;
}

.video-buttons {
  display: flex;
  gap: 0.75rem;
  justify-content: center;
}

.watch-progress {
  margin-top: 0.5rem;
}

.progress-text {
  color: var(--text-secondary-color, #b0b3b8);
  font-size: 0.9rem;
  font-weight: 500;
}

.btn {
  padding: 0.6rem 1rem;
  border-radius: var(--btn-radius, 12px);
  font-weight: 600;
  cursor: pointer;
  text-decoration: none;
}

.btn-primary {
  background: var(--btn-primary-bg, linear-gradient(135deg, #3b82f6, #2563eb));
  color: #fff;
  border: none;
}

.btn-outline {
  background: transparent;
  color: var(--accent-color, #3b82f6);
  border: 1px solid var(--accent-color, #3b82f6);
}

/* 浅色主题优化 */
:root.light-theme .prologue-case {
  background: #ffffff;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
}

:root.light-theme .prologue-case .case-header {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

:root.light-theme .prologue-case .case-title {
  color: #212529;
}

:root.light-theme .prologue-case .case-desc {
  color: #495057;
}

:root.light-theme .prologue-case .video-card {
  background: #ffffff;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

:root.light-theme .prologue-case .btn-primary {
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  color: #ffffff;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);
}

:root.light-theme .prologue-case .btn-primary:hover {
  background: linear-gradient(135deg, #2563eb, #1d4ed8);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

:root.light-theme .prologue-case .btn-outline {
  color: #3b82f6;
  border: 1px solid #3b82f6;
  background: transparent;
}

:root.light-theme .prologue-case .btn-outline:hover {
  background: #3b82f6;
  color: #ffffff;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}
</style>

