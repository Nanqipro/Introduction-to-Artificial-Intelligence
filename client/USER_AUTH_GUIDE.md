# 用户认证系统使用指南

## 功能概述

本项目已实现完整的用户认证系统，包括：

- ✅ 用户注册
- ✅ 用户登录
- ✅ 获取用户信息
- ✅ 更新用户基本信息
- ✅ 更新用户头像
- ✅ 更新用户密码
- ✅ 用户登出
- ✅ 路由守卫（需要登录的页面自动跳转）

## 页面路由

| 路由 | 页面 | 说明 | 是否需要登录 |
|------|------|------|-------------|
| `/login` | 登录注册页 | 用户登录和注册 | ❌ |
| `/profile` | 个人中心 | 用户信息管理 | ✅ |
| `/admin` | 管理员页面 | 管理员功能 | ✅ |
| `/stats` | 统计页面 | 学习统计 | ✅ |

## 使用方法

### 1. 用户注册

1. 访问 http://localhost:5173/login
2. 点击"没有账号？立即注册"
3. 输入用户名（5-16位）和密码（5-16位）
4. 点击"注册"按钮
5. 注册成功后会自动切换到登录模式

### 2. 用户登录

1. 在登录页面输入用户名和密码
2. 点击"登录"按钮
3. 登录成功后会跳转到首页
4. 导航栏右侧会显示用户头像和用户名

### 3. 个人中心

1. 登录后点击导航栏右侧的用户头像
2. 选择"个人中心"
3. 可以进行以下操作：
   - 编辑基本信息（昵称、邮箱）
   - 更换头像（输入图片URL）
   - 修改密码

### 4. 用户登出

1. 点击导航栏右侧的用户头像
2. 选择"退出登录"
3. 系统会清除登录状态并跳转到首页

## 技术实现

### 前端技术栈

- **Vue 3** - 前端框架
- **Vue Router** - 路由管理
- **Element Plus** - UI组件库
- **Axios** - HTTP请求库
- **Composables** - 状态管理

### 核心文件

```
client/src/
├── composables/
│   └── useAuth.js          # 认证状态管理
├── views/
│   ├── LoginPage.vue       # 登录注册页面
│   └── UserProfile.vue     # 个人中心页面
├── services/
│   └── api.js              # API接口定义
├── router/
│   └── index.js            # 路由配置和守卫
└── components/
    └── Navigation.vue      # 导航组件（包含用户菜单）
```

### API接口

所有用户相关的API都在 `userApi` 中定义：

```javascript
import { userApi } from '@/services/api'

// 注册
await userApi.register({ username, password })

// 登录
await userApi.login({ username, password })

// 获取用户信息
await userApi.getUserInfo()

// 更新用户信息
await userApi.updateUserInfo({ id, nickname, email })

// 更新头像
await userApi.updateAvatar(avatarUrl)

// 更新密码
await userApi.updatePassword({ old_pwd, new_pwd, re_pwd })
```

### 状态管理

使用 `useAuth` composable 管理认证状态：

```javascript
import { useAuth } from '@/composables/useAuth'

const {
  isLoggedIn,    // 是否已登录
  currentUser,   // 当前用户信息
  login,         // 登录方法
  register,      // 注册方法
  logout,        // 登出方法
  updateUserInfo // 更新用户信息
} = useAuth()
```

## 安全特性

1. **JWT Token认证** - 使用JWT token进行身份验证
2. **自动token刷新** - token存储在localStorage中，页面刷新后自动恢复登录状态
3. **路由守卫** - 未登录用户访问需要认证的页面会自动跳转到登录页
4. **401处理** - token过期时自动清除登录状态并跳转到登录页
5. **密码加密** - 后端对密码进行MD5加密存储

## 测试账号

已创建测试账号供测试使用：
- 用户名：`testuser`
- 密码：`123456`

## 开发说明

### 添加需要认证的页面

在路由配置中添加 `meta.requiresAuth: true`：

```javascript
{
  path: '/new-page',
  name: 'NewPage',
  component: NewPage,
  meta: {
    requiresAuth: true  // 需要登录才能访问
  }
}
```

### 在组件中使用认证状态

```vue
<template>
  <div v-if="isLoggedIn">
    欢迎，{{ currentUser?.nickname || currentUser?.username }}！
  </div>
  <div v-else>
    请先登录
  </div>
</template>

<script setup>
import { useAuth } from '@/composables/useAuth'

const { isLoggedIn, currentUser } = useAuth()
</script>
```

## 故障排除

### 常见问题

1. **登录后页面没有更新** - 检查是否正确使用了 `useAuth` composable
2. **token过期** - 系统会自动处理，用户需要重新登录
3. **跨域问题** - 后端已配置CORS，支持前端域名访问
4. **API请求失败** - 检查后端服务是否正常运行在8082端口

### 调试技巧

1. 打开浏览器开发者工具查看Network标签页的API请求
2. 检查localStorage中是否存储了token和userInfo
3. 查看控制台是否有错误信息
4. 确认后端服务正常运行：`curl http://localhost:8082/user/userInfo`

## 下一步计划

- [ ] 添加邮箱验证功能
- [ ] 实现忘记密码功能
- [ ] 添加用户角色权限管理
- [ ] 实现第三方登录（如GitHub、Google等）
- [ ] 添加用户活动日志
