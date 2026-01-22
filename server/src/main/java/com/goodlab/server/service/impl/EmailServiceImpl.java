package com.goodlab.server.service.impl;

import com.goodlab.server.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailServiceImpl implements EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.frontend.url:http://localhost:5174}")
    private String frontendUrl;
    
    @Override
    public boolean sendPasswordResetEmail(String email, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("密码重置 - AI学习平台");
            
            String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
            String content = String.format(
                "您好！\n\n" +
                "您请求重置AI学习平台的密码。请点击以下链接重置您的密码：\n\n" +
                "%s\n\n" +
                "此链接将在30分钟后过期。如果您没有请求重置密码，请忽略此邮件。\n\n" +
                "祝您学习愉快！\n" +
                "AI学习平台团队",
                resetUrl
            );
            
            message.setText(content);
            mailSender.send(message);
            
            logger.info("密码重置邮件已发送到: {}", email);
            return true;
            
        } catch (Exception e) {
            logger.error("发送密码重置邮件失败: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean sendEmailVerification(String email, String verificationCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("邮箱验证 - AI学习平台");
            
            String content = String.format(
                "您好！\n\n" +
                "欢迎注册AI学习平台！您的邮箱验证码是：\n\n" +
                "%s\n\n" +
                "此验证码将在10分钟后过期。请及时完成验证。\n\n" +
                "祝您学习愉快！\n" +
                "AI学习平台团队",
                verificationCode
            );
            
            message.setText(content);
            mailSender.send(message);
            
            logger.info("邮箱验证邮件已发送到: {}", email);
            return true;
            
        } catch (Exception e) {
            logger.error("发送邮箱验证邮件失败: {}", e.getMessage());
            return false;
        }
    }
}