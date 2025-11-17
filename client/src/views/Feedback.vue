<template>
  <div class="container" style="max-width: 720px; margin: 0 auto; padding: 16px;">
    <h2>问题反馈</h2>
    <p style="color:#666;">请描述你在使用过程中的问题或建议，管理员会查看并处理。</p>

    <div style="margin: 16px 0;">
      <label style="display:block; margin-bottom:8px;">反馈类别（可选）</label>
      <select v-model="form.category" style="width:100%; padding:8px;">
        <option value="">未选择</option>
        <option value="bug">功能异常</option>
        <option value="suggestion">功能建议</option>
        <option value="content">教材内容问题</option>
        <option value="other">其他</option>
      </select>
    </div>

    <div style="margin: 16px 0;">
      <label style="display:block; margin-bottom:8px;">反馈内容</label>
      <textarea v-model="form.content" rows="6" style="width:100%; padding:8px;" placeholder="请尽量详细描述问题或建议"></textarea>
    </div>

    <div style="display:flex; gap:12px;">
      <button @click="submit" style="padding:8px 16px;">提交反馈</button>
      <button @click="reset" style="padding:8px 16px;">清空</button>
    </div>

    <div v-if="message" style="margin-top:12px; color:#2a7;">{{ message }}</div>
    <div v-if="error" style="margin-top:12px; color:#c33;">{{ error }}</div>

    <hr style="margin:24px 0;" />
    <h3>全部反馈</h3>
    <div v-if="loadingAll">正在加载全部反馈...</div>
    <div v-else>
      <div v-if="allList.length === 0" style="color:#666;">暂无反馈</div>
      <ul>
        <li v-for="item in allList" :key="item.id" style="margin-bottom:12px; padding:8px; border:1px solid #eee; border-radius:6px;">
          <div style="font-size:14px; color:#888;">{{ formatDate(item.createdAt) }} · 状态：{{ item.status }} · 用户：{{ item.nickname || '未知' }}</div>
          <div style="margin-top:6px; white-space:pre-wrap;">{{ item.content }}</div>
          <div v-if="item.adminReply" style="margin-top:6px; background:#f7f7f7; padding:8px; border-radius:4px;">
            管理员回复：{{ item.adminReply }}
          </div>
        </li>
      </ul>
    </div>

    <h3 style="margin-top:20px;">我的反馈记录</h3>
    <div v-if="loadingList">正在加载我的反馈...</div>
    <div v-else>
      <div v-if="list.length === 0" style="color:#666;">你还没有提交过反馈</div>
      <ul>
        <li v-for="item in list" :key="item.id" style="margin-bottom:12px; padding:8px; border:1px solid #eee; border-radius:6px;">
          <div style="font-size:14px; color:#888;">{{ formatDate(item.createdAt) }} · 状态：{{ item.status }}</div>
          <div style="margin-top:6px; white-space:pre-wrap;">{{ item.content }}</div>
          <div v-if="item.adminReply" style="margin-top:6px; background:#f7f7f7; padding:8px; border-radius:4px;">
            管理员回复：{{ item.adminReply }}
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { feedbackApi } from '@/services/api'

export default {
  name: 'Feedback',
  data() {
    return {
      form: { category: '', content: '' },
      message: '',
      error: '',
      list: [],
      allList: [],
      loadingList: false,
      loadingAll: false
    }
  },
  methods: {
    async submit() {
      this.message = ''
      this.error = ''
      try {
        if (!this.form.content || this.form.content.trim() === '') {
          this.error = '反馈内容不能为空'
          return
        }
        const res = await feedbackApi.submit({
          category: this.form.category,
          content: this.form.content.trim()
        })
        this.message = res?.data || '反馈已提交'
        this.form.content = ''
        await this.fetchList()
      } catch (e) {
        this.error = e.message || '提交失败'
      }
    },
    reset() {
      this.form = { category: '', content: '' }
      this.message = ''
      this.error = ''
    },
    async fetchList() {
      this.loadingList = true
      try {
        const res = await feedbackApi.myList()
        this.list = res?.data || []
      } catch (e) {
        this.error = e.message || '加载失败'
      } finally {
        this.loadingList = false
      }
    },
    async fetchAll() {
      this.loadingAll = true
      try {
        const res = await feedbackApi.allList()
        this.allList = res?.data || []
      } catch (e) {
        // 不阻断页面，仅提示
      } finally {
        this.loadingAll = false
      }
    },
    formatDate(dt) {
      if (!dt) return ''
      try {
        return new Date(dt).toLocaleString()
      } catch (_) {
        return dt
      }
    }
  },
  async mounted() {
    await Promise.all([this.fetchList(), this.fetchAll()])
  }
}
</script>

<style scoped>
h2 { margin: 8px 0; }
h3 { margin: 8px 0; }
button { background: #1976d2; color: #fff; border: none; border-radius: 4px; cursor: pointer; }
button:hover { background: #115ea3; }
</style>