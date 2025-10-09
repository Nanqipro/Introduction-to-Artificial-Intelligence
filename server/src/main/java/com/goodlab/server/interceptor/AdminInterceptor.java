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
        System.out.println("AdminInterceptor: 检查管理员权限 - " + requestURI);
        
        // 获取当前用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims == null) {
            System.out.println("AdminInterceptor: 用户未登录");
            response.setStatus(401);
            return false;
        }
        
        Integer userId = (Integer) claims.get("id");
        String username = (String) claims.get("username");
        
        // 查询用户详细信息
        User user = userService.findById(userId);
        if (user == null) {
            System.out.println("AdminInterceptor: 用户不存在 - ID: " + userId);
            response.setStatus(401);
            return false;
        }
        
        // 检查用户角色
        if (!"admin".equals(user.getRole())) {
            System.out.println("AdminInterceptor: 用户无管理员权限 - 用户名: " + username + ", 角色: " + user.getRole());
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":1,\"message\":\"访问被拒绝：只有管理员才能访问此功能\"}");
            return false;
        }
        
        System.out.println("AdminInterceptor: 管理员权限验证通过 - 用户名: " + username);
        return true;
    }
}