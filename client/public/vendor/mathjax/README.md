# MathJax 本地文件

这个目录包含 MathJax 的本地副本，用作 CDN 失败时的备用方案。

## 文件说明

- `tex-mml-chtml.js` - MathJax 核心文件，支持 TeX 和 MathML 输入，HTML+CSS 输出

## 更新方法

当需要更新 MathJax 版本时，运行以下命令：

```bash
# 下载最新版本
curl -L -o tex-mml-chtml.js https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js

# 或使用 PowerShell
Invoke-WebRequest -Uri "https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js" -OutFile "tex-mml-chtml.js"
```

## 工作原理

在 `index.html` 中，我们首先尝试从 CDN 加载 MathJax：
1. 优先使用 `https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js`
2. 如果 CDN 失败或超时（3秒），自动回退到本地文件 `/vendor/mathjax/tex-mml-chtml.js`

这确保了即使在网络环境受限的情况下，数学公式仍能正常渲染。