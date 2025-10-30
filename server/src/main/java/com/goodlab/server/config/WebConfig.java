package com.goodlab.server.config;

import com.goodlab.server.interceptor.LoginInterceptor;
import com.goodlab.server.interceptor.AdminInterceptor;
import com.goodlab.server.interceptor.LoggingInterceptor;
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
        
        @Autowired
        private LoggingInterceptor loggingInterceptor;
        
        @Value("${file.upload.path:uploads/}")
        private String uploadPath;

        // 配置跨域访问
        // 安全原则：只允许明确信任的域名，避免使用通配符
        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许所有路径的跨域访问
                                // 仅允许已知的可信源
                                // 开发环境：localhost
                                // 生产环境：实际部署的域名和IP
                                .allowedOrigins(
                                                // 本地开发环境
                                                "http://localhost:5173", 
                                                "http://localhost:5174",
                                                "http://127.0.0.1:5173", 
                                                "http://127.0.0.1:5174",
                                                // 生产环境 - 请根据实际部署情况调整
                                                "http://222.204.4.108",
                                                "https://222.204.4.108"
                                                // 注意：部署时请移除不需要的域名
                                )
                                // 仅允许必要的HTTP方法，禁用TRACE等危险方法
                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                                // 限制允许的请求头，避免使用通配符"*"
                                .allowedHeaders(
                                                "Content-Type", 
                                                "Authorization", 
                                                "X-Requested-With",
                                                "Accept",
                                                "Origin"
                                )
                                // 允许携带凭证（cookies）
                                .allowCredentials(true)
                                // 设置预检请求的缓存时间（秒）
                                .maxAge(3600);
        }

        // 配置拦截器
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                // 日志拦截器 - 记录所有API访问日志
                registry.addInterceptor(loggingInterceptor)
                                .addPathPatterns("/**");
                
                // 登录认证拦截器 - 放行登录接口、注册接口等公共接口
                registry.addInterceptor(loginInterceptor)
                                .addPathPatterns("/**")
                                .excludePathPatterns("/api/users/login", "/api/users/register", "/api/user/login", "/api/user/register", "/api/chapters/**",
                                                "/api/quiz/**", "/error", "/uploads/**", "/api/upload/**",
                                                "/api/chapter1-case-study/**");
                
                // 管理员权限拦截器 - 只拦截管理员相关接口
                registry.addInterceptor(adminInterceptor)
                                .addPathPatterns("/api/admin/**");

                System.out.println(
                                "拦截器配置完成 - 登录拦截器放行路径: /api/users/login, /api/users/register, /api/user/login, /api/user/register, /api/chapters/**, /api/quiz/**, /error, /uploads/**, /api/upload/**, /api/chapter1-case-study/**");
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
