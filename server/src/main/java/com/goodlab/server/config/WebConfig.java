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

        @Value("${app.cors.allowed-origins:http://localhost:5173,http://127.0.0.1:5173}")
        private String[] allowedOrigins;

        // 配置跨域访问
        // 安全原则：只允许明确信任的域名，避免使用通配符
        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许所有路径的跨域访问
                                .allowedOrigins(allowedOrigins)
                                // 仅允许必要的HTTP方法，禁用TRACE等危险方法
                                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
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
                                .excludePathPatterns(
                                                "/api/users/login", "/api/users/register",
                                                "/api/user/login", "/api/user/register",
                                                // 章节只读公开接口精确枚举，避免通配符意外暴露写接口
                                                "/api/chapters", "/api/chapters/all",
                                                "/api/chapters/health", "/api/chapters/*",
                                                // quiz: 仅放行只读公开接口，/results 含学生成绩需鉴权
                                                "/api/quiz/questions/**",
                                                "/api/quiz/leaderboard",
                                                "/api/quiz/db-questions/**",
                                                "/api/quiz/question-stats",
                                                "/api/quiz/health",
                                                "/error", "/uploads/**",
                                                "/api/chapter1-case-study/**");
                
                // 管理员权限拦截器 - 只拦截管理员相关接口
                registry.addInterceptor(adminInterceptor)
                                .addPathPatterns("/api/admin/**");

                // 移除控制台输出，避免泄露拦截器配置细节
        }
        
        // 配置静态资源访问
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // 配置上传文件的静态资源访问
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:" + uploadPath);
                        
                // 关闭控制台输出
        }
}
