<template>
  <div class="notification-manager">
    <transition-group name="toast-list" tag="div" class="toast-container">
      <NotificationToast
        v-for="notification in notifications"
        :key="notification.id"
        :type="notification.type"
        :title="notification.title"
        :message="notification.message"
        :duration="notification.duration"
        :auto-close="notification.autoClose"
        :closable="notification.closable"
        :persistent="notification.persistent"
        @close="removeNotification(notification.id)"
        @show="handleNotificationShow(notification)"
      />
    </transition-group>
  </div>
</template>

<script>
import NotificationToast from './NotificationToast.vue'

export default {
  name: 'NotificationManager',
  components: {
    NotificationToast
  },
  data() {
    return {
      notifications: [],
      nextId: 1
    }
  },
  mounted() {
    // 组件已挂载，provide已通过provide()选项提供
  },
  provide() {
    return {
      notification: {
        showNotification: this.showNotification,
        showSuccess: this.showSuccess,
        showWarning: this.showWarning,
        showError: this.showError,
        showInfo: this.showInfo
      }
    }
  },
  methods: {
    showNotification(options) {
      const notification = {
        id: this.nextId++,
        type: options.type || 'info',
        title: options.title || '',
        message: options.message || '',
        duration: options.duration || 5000,
        autoClose: options.autoClose !== false,
        closable: options.closable !== false,
        persistent: options.persistent || false,
        timestamp: Date.now()
      }
      
      this.notifications.push(notification)
      
      // 限制最大通知数量
      if (this.notifications.length > 5) {
        this.notifications.shift()
      }
      
      return notification.id
    },
    
    showSuccess(message, options = {}) {
      return this.showNotification({
        type: 'success',
        message,
        ...options
      })
    },
    
    showWarning(message, options = {}) {
      return this.showNotification({
        type: 'warning',
        message,
        ...options
      })
    },
    
    showError(message, options = {}) {
      return this.showNotification({
        type: 'error',
        message,
        duration: 8000, // 错误通知显示更长时间
        ...options
      })
    },
    
    showInfo(message, options = {}) {
      return this.showNotification({
        type: 'info',
        message,
        ...options
      })
    },
    
    removeNotification(id) {
      const index = this.notifications.findIndex(n => n.id === id)
      if (index > -1) {
        this.notifications.splice(index, 1)
      }
    },
    
    removeAll() {
      this.notifications = []
    },
    
    handleNotificationShow(notification) {
      // 可以在这里添加通知显示的逻辑，比如记录到日志
      // 通知已显示
    }
  }
}
</script>

<style lang="scss" scoped>

.notification-manager {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 10000;
  pointer-events: none;
}

.toast-container {
  position: relative;
  pointer-events: auto;
}

// 通知列表动画
.toast-list-enter-active,
.toast-list-leave-active {
  transition: all var(--transition-normal, 0.3s) ease;
}

.toast-list-enter-from {
  opacity: 0;
  transform: translateX(100%) translateY(-20px);
}

.toast-list-leave-to {
  opacity: 0;
  transform: translateX(100%) translateY(-20px);
}

.toast-list-move {
  transition: transform var(--transition-normal, 0.3s) ease;
}

// 响应式设计
@media (max-width: var(--breakpoint-md, 768px)) {
  .notification-manager {
    left: 0;
    right: 0;
  }
}
</style>