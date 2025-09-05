package com.goodlab.server.interceptor;


import com.goodlab.server.utils.JwtUtil;
import com.goodlab.server.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;


// 拦截器
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 添加详细调试日志
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        // 跳过OPTIONS预检请求的认证检查
        if ("OPTIONS".equals(method)) {
            System.out.println("LoginInterceptor: 跳过OPTIONS预检请求 - " + requestURI);
            return true;
        }
        
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");
        String origin = request.getHeader("Origin");
        String remoteAddr = request.getRemoteAddr();
        
        System.out.println("=== LoginInterceptor 详细请求信息 ===");
        System.out.println("请求URI: " + requestURI);
        System.out.println("请求方法: " + method);
        System.out.println("User-Agent: " + (userAgent != null ? userAgent : "null"));
        System.out.println("Referer: " + (referer != null ? referer : "null"));
        System.out.println("Origin: " + (origin != null ? origin : "null"));
        System.out.println("Remote Address: " + remoteAddr);
        
        // 令牌验证
        String token = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + (token != null ? token.substring(0, Math.min(token.length(), 30)) + "..." : "null"));
        
        // 验证token
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new Exception("Token为空");
            }
            
            // 处理Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
                System.out.println("LoginInterceptor: 移除Bearer前缀后的token = " + token.substring(0, Math.min(token.length(), 20)) + "...");
            }
            
            Map<String, Object> claims = JwtUtil.parseToken(token);
            ThreadLocalUtil.set(claims);
            System.out.println("LoginInterceptor: Token验证成功，用户ID = " + claims.get("id"));
            return true;
        } catch (Exception e) {
            // http 401
            System.out.println("LoginInterceptor: Token验证失败 - " + e.getMessage());
            response.setStatus(401);
            return false;
        }
    }

    // 请求处理完毕后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }





}
