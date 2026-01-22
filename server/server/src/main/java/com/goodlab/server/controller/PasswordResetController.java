package com.goodlab.server.controller;

import com.goodlab.server.pojo.Result;
import com.goodlab.server.service.EmailService;
import com.goodlab.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * 发送密码重置邮件
     */
    @PostMapping("/send-email")
    public Result sendPasswordResetEmail(@RequestBody Map<String, String> params) {
        String email = params.get("email");
        
        if (email == null || email.trim().isEmpty()) {
            return Result.error("邮箱地址不能为空");
        }
        
        // 检查邮箱是否存在
        if (userService.findByEmail(email) == null) {
            return Result.error("该邮箱未注册");
        }
        
        // 生成重置令牌
        String resetToken = userService.generatePasswordResetToken(email);
        if (resetToken == null) {
            return Result.error("生成重置令牌失败");
        }
        
        // 发送邮件
        boolean emailSent = emailService.sendPasswordResetEmail(email, resetToken);
        if (!emailSent) {
            return Result.error("邮件发送失败，请稍后重试");
        }
        
        return Result.success("密码重置邮件已发送，请查收邮箱");
    }
    
    /**
     * 验证重置令牌
     */
    @GetMapping("/validate-token")
    public Result validateResetToken(@RequestParam String token) {
        if (token == null || token.trim().isEmpty()) {
            return Result.error("重置令牌不能为空");
        }
        
        boolean isValid = userService.validatePasswordResetToken(token);
        if (!isValid) {
            return Result.error("重置令牌无效或已过期");
        }
        
        return Result.success("重置令牌有效");
    }
    
    /**
     * 重置密码
     */
    @PostMapping("/reset")
    public Result resetPassword(@RequestBody Map<String, String> params) {
        String token = params.get("token");
        String newPassword = params.get("newPassword");
        
        if (token == null || token.trim().isEmpty()) {
            return Result.error("重置令牌不能为空");
        }
        
        if (newPassword == null || newPassword.length() < 6) {
            return Result.error("密码长度不能少于6位");
        }
        
        boolean resetSuccess = userService.resetPasswordByToken(token, newPassword);
        if (!resetSuccess) {
            return Result.error("密码重置失败，令牌无效或已过期");
        }
        
        return Result.success("密码重置成功");
    }
}