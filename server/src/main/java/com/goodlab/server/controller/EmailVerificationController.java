package com.goodlab.server.controller;

import com.goodlab.server.pojo.Result;
import com.goodlab.server.service.EmailService;
import com.goodlab.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email-verification")
public class EmailVerificationController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * 发送邮箱验证码
     */
    @PostMapping("/send-code")
    public Result sendVerificationCode(@RequestBody Map<String, String> params) {
        String email = params.get("email");
        
        if (email == null || email.trim().isEmpty()) {
            return Result.error("邮箱地址不能为空");
        }
        
        // 检查邮箱格式
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return Result.error("邮箱格式不正确");
        }
        
        // 生成验证码
        String verificationCode = userService.generateEmailVerificationCode(email);
        
        // 发送邮件
        boolean emailSent = emailService.sendEmailVerification(email, verificationCode);
        if (!emailSent) {
            return Result.error("邮件发送失败，请稍后重试");
        }
        
        return Result.success("验证码已发送，请查收邮箱");
    }
    
    /**
     * 验证邮箱验证码
     */
    @PostMapping("/verify-code")
    public Result verifyCode(@RequestBody Map<String, String> params) {
        String email = params.get("email");
        String code = params.get("code");
        
        if (email == null || email.trim().isEmpty()) {
            return Result.error("邮箱地址不能为空");
        }
        
        if (code == null || code.trim().isEmpty()) {
            return Result.error("验证码不能为空");
        }
        
        boolean isValid = userService.verifyEmailCode(email, code);
        if (!isValid) {
            return Result.error("验证码错误或已过期");
        }
        
        return Result.success("邮箱验证成功");
    }
}