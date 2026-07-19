package com.goodlab.server.service.impl;

import com.goodlab.server.mapper.UserMapper;
import com.goodlab.server.pojo.User;
import com.goodlab.server.service.UserService;
import com.goodlab.server.utils.PasswordUtil;
import com.goodlab.server.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    // 内存存储验证码（生产环境建议使用Redis）
    private final Map<String, String> emailVerificationCodes = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> codeExpiryTimes = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public User findByUserName(String username) {
        User user = userMapper.findByUserName(username);
        return user;
    }

    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public void register(String username, String password) {
        userMapper.add(username, PasswordUtil.encode(password));
    }

    // 更新用户信息
    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    // 检查用户名是否可用（排除指定ID的用户）
    @Override
    public boolean isUsernameAvailable(String username, Integer excludeId) {
        int count = userMapper.countByUsernameExcludingId(username, excludeId);
        return count == 0;
    }

    // 更新用户头像
    @Override
    public void updateUserPic(String userPic) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        userMapper.updateUserPic(userPic, id);

    }
    
    @Override
    public void updateUserPicById(Integer userId, String userPic) {
        userMapper.updateUserPic(userPic, userId);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        userMapper.updatePwd(PasswordUtil.encode(newPwd), id);
    }
    
    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }
    
    @Override
    public String generatePasswordResetToken(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            return null;
        }
        
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(30); // 30分钟过期
        
        userMapper.updateResetToken(email, resetToken, expiry);
        return resetToken;
    }
    
    @Override
    public boolean validatePasswordResetToken(String token) {
        User user = userMapper.findByValidResetToken(token);
        return user != null;
    }
    
    @Override
    public boolean resetPasswordByToken(String token, String newPassword) {
        User user = userMapper.findByValidResetToken(token);
        if (user == null) {
            return false;
        }
        
        String hashedPassword = PasswordUtil.encode(newPassword);
        userMapper.resetPasswordByToken(token, hashedPassword);
        return true;
    }
    
    @Override
    public String generateEmailVerificationCode(String email) {
        // 生成6位数字验证码
        String code = String.format("%06d", secureRandom.nextInt(1_000_000));
        
        // 存储验证码和过期时间
        emailVerificationCodes.put(email, code);
        codeExpiryTimes.put(email, LocalDateTime.now().plusMinutes(10)); // 10分钟过期
        
        return code;
    }
    
    @Override
    public boolean verifyEmailCode(String email, String code) {
        String storedCode = emailVerificationCodes.get(email);
        LocalDateTime expiryTime = codeExpiryTimes.get(email);
        
        if (storedCode == null || expiryTime == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(expiryTime)) {
            // 验证码已过期，清理
            emailVerificationCodes.remove(email);
            codeExpiryTimes.remove(email);
            return false;
        }
        
        if (storedCode.equals(code)) {
            // 验证成功，清理验证码并更新邮箱验证状态
            emailVerificationCodes.remove(email);
            codeExpiryTimes.remove(email);
            userMapper.updateEmailVerified(email, true);
            return true;
        }
        
        return false;
    }
    
    @Override
    public void updateFirstLogin(Integer userId) {
        userMapper.updateFirstLogin(userId);
    }

}
