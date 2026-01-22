package com.goodlab.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 日志配置类
 * 提供统一的日志记录工具和配置
 */
@Configuration
public class LoggingConfig {
    
    // 业务日志记录器
    public static final Logger BUSINESS_LOGGER = LoggerFactory.getLogger("com.goodlab.business");
    
    // 访问日志记录器
    public static final Logger ACCESS_LOGGER = LoggerFactory.getLogger("com.goodlab.access");
    
    // 系统日志记录器
    public static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("com.goodlab.system");
    
    /**
     * 记录用户操作日志
     * @param userId 用户ID
     * @param operation 操作类型
     * @param details 操作详情
     */
    public static void logUserOperation(Long userId, String operation, String details) {
        BUSINESS_LOGGER.info("用户操作 - 用户ID: {}, 操作: {}, 详情: {}", userId, operation, details);
    }
    
    /**
     * 记录API访问日志
     * @param method HTTP方法
     * @param url 请求URL
     * @param userId 用户ID
     * @param ip 客户端IP
     * @param responseTime 响应时间(ms)
     * @param status 响应状态码
     */
    public static void logApiAccess(String method, String url, Long userId, String ip, long responseTime, int status) {
        ACCESS_LOGGER.info("API访问 - 方法: {}, URL: {}, 用户ID: {}, IP: {}, 响应时间: {}ms, 状态: {}", 
                          method, url, userId, ip, responseTime, status);
    }
    
    /**
     * 记录系统错误日志
     * @param operation 操作描述
     * @param error 错误信息
     * @param exception 异常对象
     */
    public static void logSystemError(String operation, String error, Exception exception) {
        SYSTEM_LOGGER.error("系统错误 - 操作: {}, 错误: {}", operation, error, exception);
    }
    
    /**
     * 记录密码修改日志
     * @param userId 用户ID
     * @param username 用户名
     * @param success 是否成功
     * @param message 操作消息
     */
    public static void logPasswordChange(Integer userId, String username, boolean success, String message) {
        String result = success ? "成功" : "失败";
        BUSINESS_LOGGER.info("密码修改 - 用户ID: {}, 用户名: {}, 结果: {}, 消息: {}", userId, username, result, message);
    }
    
    /**
     * 记录登录日志
     * @param userId 用户ID
     * @param username 用户名
     * @param success 是否成功
     * @param message 操作消息
     */
    public static void logLogin(Integer userId, String username, boolean success, String message) {
        String result = success ? "成功" : "失败";
        BUSINESS_LOGGER.info("用户登录 - 用户ID: {}, 用户名: {}, 结果: {}, 消息: {}", userId, username, result, message);
    }
    
    /**
     * 记录数据导入日志
     * @param operation 导入操作
     * @param totalCount 总数量
     * @param successCount 成功数量
     * @param failCount 失败数量
     * @param userId 操作用户ID
     */
    public static void logDataImport(String operation, int totalCount, int successCount, int failCount, Long userId) {
        BUSINESS_LOGGER.info("数据导入 - 操作: {}, 总数: {}, 成功: {}, 失败: {}, 操作用户: {}", 
                           operation, totalCount, successCount, failCount, userId);
    }
}