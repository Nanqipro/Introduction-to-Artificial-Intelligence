package com.goodlab.server.config;

import com.goodlab.server.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 配置跨域访问等
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private LoginInterceptor loginInterceptor;

    // 配置跨域访问
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 允许所有路径的跨域访问
                .allowedOrigins("http://localhost:5173", "http://localhost:5174", "http://localhost:3000", "http://127.0.0.1:5173", "http://127.0.0.1:5174", "https://102qldp675617.vicp.fun")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
    // 放行登录接口 和注册接口
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 放行登录接口 和注册接口
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/user/login")
                .excludePathPatterns("/api/user/register")
                .excludePathPatterns("/api/chapters/**")
                .excludePathPatterns("/api/quiz/**")
                .excludePathPatterns("/api/admin/**")
                // 临时调试：放行level相关接口
                .excludePathPatterns("/api/level/**");
        
        System.out.println("拦截器配置完成 - 放行路径: /api/user/login, /api/user/register, /api/chapters/**, /api/quiz/**, /api/admin/**, /api/level/**");
    }
}
