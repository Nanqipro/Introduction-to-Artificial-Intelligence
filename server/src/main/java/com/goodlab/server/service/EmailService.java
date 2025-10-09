package com.goodlab.server.service;

public interface EmailService {
    /**
     * 发送密码重置邮件
     * @param email 收件人邮箱
     * @param resetToken 重置令牌
     * @return 是否发送成功
     */
    boolean sendPasswordResetEmail(String email, String resetToken);
    
    /**
     * 发送邮箱验证邮件
     * @param email 收件人邮箱
     * @param verificationCode 验证码
     * @return 是否发送成功
     */
    boolean sendEmailVerification(String email, String verificationCode);
}