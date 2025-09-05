package com.goodlab.server.config;

import com.goodlab.server.interceptor.LoginInterceptor;
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
        
        @Value("${file.upload.path:uploads/}")
        private String uploadPath;

        // 配置跨域访问
        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许所有路径的跨域访问
                                .allowedOrigins("http://localhost:5173", "http://localhost:5174",
                                                "http://localhost:3000",
                                                "http://127.0.0.1:5173", "http://127.0.0.1:5174",
                                                "https://102qldp675617.vicp.fun")
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
                                .excludePathPatterns("/user/login", "/user/register", "/api/chapters/**",
                                                "/api/quiz/**", "/api/admin/**", "/error", "/uploads/**", "/api/upload/**");

                System.out.println(
                                "拦截器配置完成 - 放行路径: /user/login, /user/register, /api/chapters/**, /api/quiz/**, /api/admin/**, /error, /uploads/**, /api/upload/**");
                System.out.println(
                                "注意: /api/level/** 需要认证访问");
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
