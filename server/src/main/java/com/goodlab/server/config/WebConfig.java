package com.goodlab.server.config;

import com.goodlab.server.interceptor.LoginInterceptor;
import com.goodlab.server.interceptor.AdminInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 配置跨域访问等
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Autowired
        private LoginInterceptor loginInterceptor;
        
        @Autowired
        private AdminInterceptor adminInterceptor;
        
        @Value("${file.upload.path:uploads/}")
        private String uploadPath;

        // 配置跨域访问
        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许所有路径的跨域访问
                                .allowedOrigins("http://localhost:5173", "http://localhost:5174",
                                                "http://localhost:3000",
                                                "http://127.0.0.1:5173", "http://127.0.0.1:5174",
                                                "https://102qldp675617.vicp.fun",
                                                "https://102qldp675617.vicp.fun:8082")
                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                                .allowedHeaders("*")
                                .allowCredentials(true);
        }

        // 配置拦截器
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                // 登录认证拦截器 - 放行登录接口、注册接口等公共接口
                registry.addInterceptor(loginInterceptor)
                                .addPathPatterns("/**")
                                .excludePathPatterns("/api/user/login", "/api/user/register", "/api/chapters/**",
                                                "/api/quiz/**", "/error", "/uploads/**", "/api/upload/**",
                                                "/api/chapter1-case-study/**");
                
                // 管理员权限拦截器 - 只拦截管理员相关接口
                registry.addInterceptor(adminInterceptor)
                                .addPathPatterns("/api/admin/**");

                System.out.println(
                                "拦截器配置完成 - 登录拦截器放行路径: /api/user/login, /api/user/register, /api/chapters/**, /api/quiz/**, /error, /uploads/**, /api/upload/**, /api/chapter1-case-study/**");
                System.out.println(
                                "管理员拦截器拦截路径: /api/admin/**");
                System.out.println(
                                "注意: /api/level/** 和其他需要认证的接口需要登录访问，/api/admin/** 需要管理员权限");
        }
        
        // 配置静态资源访问
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // 配置上传文件的静态资源访问
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:" + uploadPath);
                        
                System.out.println("静态资源配置完成 - 上传文件访问路径: /uploads/** -> file:" + uploadPath);
        }
}
