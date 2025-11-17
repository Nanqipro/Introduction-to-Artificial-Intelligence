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
            return true;
        }
        
        // 已移除详细控制台输出，避免泄露信息
        
        // 令牌验证
        String token = request.getHeader("Authorization");
        
        // 验证token
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new Exception("Token为空");
            }
            
            // 处理Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
                // 关闭控制台输出
            }
            
            Map<String, Object> claims = JwtUtil.parseToken(token);
            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            // http 401
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
