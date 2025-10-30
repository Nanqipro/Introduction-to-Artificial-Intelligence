package com.goodlab.server.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * HTTP方法过滤器
 * 禁用危险的HTTP方法（TRACE, TRACK等），只允许必要的HTTP方法
 * 防御跨站追踪（Cross-Site Tracing）攻击
 */
@Component
public class HttpMethodFilter implements Filter {

    // 允许的HTTP方法白名单
    private static final Set<String> ALLOWED_METHODS = new HashSet<>(
        Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
    );
    
    // 危险的HTTP方法（需要明确禁用）
    private static final Set<String> DANGEROUS_METHODS = new HashSet<>(
        Arrays.asList("TRACE", "TRACK", "CONNECT")
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String method = httpRequest.getMethod().toUpperCase();
        
        // 检查是否为危险方法
        if (DANGEROUS_METHODS.contains(method)) {
            // 返回405 Method Not Allowed
            httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write(
                "{\"code\": 405, \"message\": \"HTTP方法不允许: " + method + "\"}"
            );
            return;
        }
        
        // 检查是否在白名单中
        if (!ALLOWED_METHODS.contains(method)) {
            // 返回405 Method Not Allowed
            httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write(
                "{\"code\": 405, \"message\": \"HTTP方法不支持: " + method + "\"}"
            );
            return;
        }
        
        // 允许的方法，继续处理
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

