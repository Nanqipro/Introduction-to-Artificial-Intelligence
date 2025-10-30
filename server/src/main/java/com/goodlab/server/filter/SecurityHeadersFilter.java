package com.goodlab.server.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 安全响应头过滤器
 * 为所有HTTP响应添加安全相关的响应头，防御常见的Web安全攻击
 */
@Component
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 防止点击劫持攻击（Clickjacking）
        // SAMEORIGIN: 只允许同源的页面嵌入
        httpResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
        
        // 防止MIME类型嗅探攻击
        // 强制浏览器遵循Content-Type的声明
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        
        // XSS保护（虽然现代浏览器默认启用，但显式设置更安全）
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        
        // 内容安全策略（CSP）
        // 限制资源加载来源，防御XSS和数据注入攻击
        // default-src 'self': 默认只允许同源内容
        // script-src: 允许的脚本来源（包括内联脚本和CDN）
        // style-src: 允许的样式来源
        // img-src: 允许的图片来源
        // font-src: 允许的字体来源
        // connect-src: 允许的Ajax/WebSocket连接
        httpResponse.setHeader("Content-Security-Policy", 
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdn.jsdelivr.net; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self' data:; " +
            "connect-src 'self' http://localhost:* http://127.0.0.1:* http://222.204.4.108:*; " +
            "frame-ancestors 'self';");
        
        // Referrer策略
        // 控制Referer头的发送，保护用户隐私
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // 权限策略（Permissions Policy）
        // 控制浏览器特性和API的访问权限
        httpResponse.setHeader("Permissions-Policy", 
            "geolocation=(), microphone=(), camera=(), payment=()");
        
        // 控制下载选项
        httpResponse.setHeader("X-Download-Options", "noopen");
        
        // 跨域策略
        httpResponse.setHeader("X-Permitted-Cross-Domain-Policies", "none");
        
        // 注意：HSTS (Strict-Transport-Security) 仅在HTTPS环境下启用
        // 如果站点启用了HTTPS，取消下面的注释
        // httpResponse.setHeader("Strict-Transport-Security", 
        //     "max-age=31536000; includeSubDomains; preload");
        
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 过滤器初始化
    }

    @Override
    public void destroy() {
        // 过滤器销毁时的清理工作
    }
}

