package com.goodlab.server.interceptor;

import com.goodlab.server.config.LoggingConfig;
import com.goodlab.server.utils.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 日志拦截器
 * 自动记录API访问日志
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    
    private static final String START_TIME_ATTRIBUTE = "startTime";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 记录请求开始时间
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 计算响应时间
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        long responseTime = startTime != null ? System.currentTimeMillis() - startTime : 0;
        
        // 获取请求信息
        String method = request.getMethod();
        String url = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null) {
            url += "?" + queryString;
        }
        
        // 获取客户端IP
        String ip = getClientIpAddress(request);
        
        // 获取用户ID（如果有JWT token）
        Long userId = null;
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Map<String, Object> claims = JwtUtil.parseToken(token);
                Integer id = (Integer) claims.get("id");
                userId = id != null ? id.longValue() : null;
            }
        } catch (Exception e) {
            // 忽略JWT解析错误
        }
        
        // 记录访问日志
        LoggingConfig.logApiAccess(method, url, userId, ip, responseTime, response.getStatus());
        
        // 如果有异常，记录错误日志
        if (ex != null) {
            LoggingConfig.logSystemError("API请求处理", "请求处理过程中发生异常: " + url, ex);
        }
    }
    
    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        String xForwardedProto = request.getHeader("X-Forwarded-Proto");
        if (xForwardedProto != null && !xForwardedProto.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedProto)) {
            return xForwardedProto;
        }
        
        return request.getRemoteAddr();
    }
}