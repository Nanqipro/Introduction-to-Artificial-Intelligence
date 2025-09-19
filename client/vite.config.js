import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path';

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "@/styles/variables.scss" as *;`,
        api: 'modern-compiler' // 使用现代编译器API
      }
    }
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 5173,
    host: '0.0.0.0',
    allowedHosts: [
      '102qldp675617.vicp.fun',
      'localhost',
      '127.0.0.1',
      '222.204.4.108'
    ],
    proxy: {
      '/api': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        secure: false
      }
    }
  },
  build: {
    // 生产环境构建配置
    outDir: 'dist',
    assetsDir: 'assets',
    sourcemap: false,
    // 修复变量初始化问题
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true,
      },
    },
    // 分包策略
    rollupOptions: {
      output: {
        manualChunks(id) {
          // 更安全的分包策略
          if (id.includes('node_modules')) {
            if (id.includes('element-plus')) {
              return 'element-plus';
            }
            if (id.includes('vue') || id.includes('@vue')) {
              return 'vue-vendor';
            }
            if (id.includes('echarts') || id.includes('chart')) {
              return 'charts';
            }
            return 'vendor';
          }
        }
      }
    }
  }
})
