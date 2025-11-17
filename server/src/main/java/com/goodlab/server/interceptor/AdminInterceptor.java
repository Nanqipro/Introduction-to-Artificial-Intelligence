package com.goodlab.server.interceptor;

import com.goodlab.server.pojo.User;
import com.goodlab.server.service.UserService;
import com.goodlab.server.utils.JwtUtil;
import com.goodlab.server.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    
    @Autowired
    private UserService userService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 跳过OPTIONS预检请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        String requestURI = request.getRequestURI();
        
        // 获取当前用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims == null) {
            response.setStatus(401);
            return false;
        }
        
        Integer userId = (Integer) claims.get("id");
        String username = (String) claims.get("username");
        
        // 查询用户详细信息
        User user = userService.findById(userId);
        if (user == null) {
            response.setStatus(401);
            return false;
        }
        
        // 检查用户角色
        if (!"admin".equals(user.getRole())) {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":1,\"message\":\"访问被拒绝：只有管理员才能访问此功能\"}");
            return false;
        }
        return true;
    }
}