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
        // 添加调试日志
        String requestURI = request.getRequestURI();
        System.out.println("LoginInterceptor: 拦截请求 " + requestURI);
        
        // 令牌验证
        String token = request.getHeader("Authorization");
        System.out.println("LoginInterceptor: 收到的token = " + (token != null ? token.substring(0, Math.min(token.length(), 20)) + "..." : "null"));
        
        // 验证token
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new Exception("Token为空");
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
