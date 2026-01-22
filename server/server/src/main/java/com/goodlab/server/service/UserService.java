package com.goodlab.server.service;


import com.goodlab.server.pojo.User;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

public interface UserService {


    // 通过用户名查询用户
    User findByUserName(String username);
    
    // 通过ID查询用户
    User findById(Integer id);
    
    //  注册
    void register(String username, String password) ;


    void update(User user);

    boolean isUsernameAvailable(String username, Integer excludeId);

    void updateUserPic(String userPic);
    
    void updateUserPicById(Integer userId, String userPic);

    void updatePwd(String newPwd);
    
    // 通过邮箱查询用户
    User findByEmail(String email);
    
    // 生成密码重置令牌
    String generatePasswordResetToken(String email);
    
    // 验证密码重置令牌
    boolean validatePasswordResetToken(String token);
    
    // 通过重置令牌重置密码
    boolean resetPasswordByToken(String token, String newPassword);
    
    // 生成邮箱验证码
    String generateEmailVerificationCode(String email);
    
    // 验证邮箱验证码
    boolean verifyEmailCode(String email, String code);
    
    // 更新首次登录状态
    void updateFirstLogin(Integer userId);
}
