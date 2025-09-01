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
      }
    }
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    host: '0.0.0.0',
    allowedHosts: [
      '102qldp675617.vicp.fun',
      'localhost',
      '127.0.0.1'
    ],
    proxy: {
      '/api': 'http://localhost:8082',
      '/user': 'http://localhost:8082'
    }
  }
})
