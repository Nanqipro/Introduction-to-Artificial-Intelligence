<template>
  <div class="container" style="max-width: 960px; margin: 0 auto; padding: 16px;">
    <h2>反馈管理</h2>
    <p style="color:#666;">查看学生反馈并更新处理状态。</p>

    <div style="display:flex; gap:8px; align-items:center; margin: 12px 0;">
      <label>筛选状态：</label>
      <select v-model="filterStatus" style="padding:6px;">
        <option value="">全部</option>
        <option value="NEW">NEW</option>
        <option value="IN_PROGRESS">IN_PROGRESS</option>
        <option value="RESOLVED">RESOLVED</option>
      </select>
      <button @click="fetchList" style="padding:6px 12px;">刷新</button>
    </div>

    <div v-if="loading">正在加载...</div>
    <table v-else style="width:100%; border-collapse:collapse;">
      <thead>
        <tr>
          <th style="text-align:left; border-bottom:1px solid #ddd; padding:8px;">ID</th>
          <th style="text-align:left; border-bottom:1px solid #ddd; padding:8px;">用户</th>
          <th style="text-align:left; border-bottom:1px solid #ddd; padding:8px;">类别</th>
          <th style="text-align:left; border-bottom:1px solid #ddd; padding:8px;">状态</th>
          <th style="text-align:left; border-bottom:1px solid #ddd; padding:8px;">内容</th>
          <th style="text-align:left; border-bottom:1px solid #ddd; padding:8px;">管理员回复</th>
          <th style="text-align:left; border-bottom:1px solid #ddd; padding:8px;">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in filtered" :key="item.id">
          <td style="padding:8px;">{{ item.id }}</td>
          <td style="padding:8px;">{{ item.nickname || '未知' }}</td>
          <td style="padding:8px;">{{ item.category || '-' }}</td>
          <td style="padding:8px;">{{ item.status }}</td>
          <td style="padding:8px; white-space:pre-wrap;">{{ item.content }}</td>
          <td style="padding:8px;">
            <textarea v-model="item._reply" rows="2" style="width:100%;"></textarea>
          </td>
          <td style="padding:8px;">
            <select v-model="item._status" style="padding:6px; margin-right:8px;">
              <option value="NEW">NEW</option>
              <option value="IN_PROGRESS">IN_PROGRESS</option>
              <option value="RESOLVED">RESOLVED</option>
            </select>
            <button @click="save(item)" style="padding:6px 12px;">保存</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="error" style="margin-top:12px; color:#c33;">{{ error }}</div>
    <div v-if="message" style="margin-top:12px; color:#2a7;">{{ message }}</div>
  </div>
</template>

<script>
import { feedbackApi } from '@/services/api'

export default {
  name: 'AdminFeedback',
  data() {
    return {
      list: [],
      loading: false,
      filterStatus: '',
      error: '',
      message: ''
    }
  },
  computed: {
    filtered() {
      if (!this.filterStatus) return this.list
      return this.list.filter(i => i.status === this.filterStatus)
    }
  },
  methods: {
    async fetchList() {
      this.loading = true
      this.error = ''
      try {
        const res = await feedbackApi.adminList()
        const data = res?.data || []
        // 初始化可编辑字段
        this.list = data.map(d => ({ ...d, _status: d.status || 'NEW', _reply: d.adminReply || '' }))
      } catch (e) {
        this.error = e.message || '加载失败'
      } finally {
        this.loading = false
      }
    },
    async save(item) {
      this.error = ''
      this.message = ''
      try {
        await feedbackApi.adminUpdate(item.id, { status: item._status, adminReply: item._reply })
        this.message = '保存成功'
        await this.fetchList()
      } catch (e) {
        this.error = e.message || '保存失败'
      }
    }
  },
  async mounted() {
    await this.fetchList()
  }
}
</script>

<style scoped>
button { background: #1976d2; color: #fff; border: none; border-radius: 4px; cursor: pointer; }
button:hover { background: #115ea3; }
th, td { border-bottom: 1px solid #eee; }
</style>