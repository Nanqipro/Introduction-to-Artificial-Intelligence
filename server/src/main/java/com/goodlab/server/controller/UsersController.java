package com.goodlab.server.controller;


import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.pojo.User;
import com.goodlab.server.service.UserService;
import com.goodlab.server.mapper.UserMapper;
import com.goodlab.server.utils.JwtUtil;
import com.goodlab.server.utils.PasswordUtil;
import com.goodlab.server.utils.ThreadLocalUtil;
import com.goodlab.server.config.LoggingConfig;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Validated
public class UsersController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;
    
    // 注册功能已禁用 - 仅支持学号登录
    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestParam String username,
                           @RequestParam String password) {
        return ApiResponse.error("注册功能已禁用，请使用学号登录");
    }

    // 登录账号由授权学校预先创建；公开版本不包含名单导入或默认密码逻辑。
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestParam String username,
                       @RequestParam String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return ApiResponse.error("账号或密码错误");
        }

        User loginUser = userService.findByUserName(username);
        if (loginUser == null || !PasswordUtil.matches(password, loginUser.getPassword())) {
            LoggingConfig.logLogin(loginUser == null ? null : loginUser.getId().intValue(), username, false, "登录凭据无效");
            return ApiResponse.error("账号或密码错误");
        }

        // 兼容历史 MD5 摘要：首次验证成功后立即升级为带盐的 PBKDF2。
        if (PasswordUtil.needsRehash(loginUser.getPassword())) {
            userMapper.updatePwd(PasswordUtil.encode(password), loginUser.getId().intValue());
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginUser.getId());
        claims.put("username", loginUser.getUsername());
        claims.put("isFirstLogin", Boolean.TRUE.equals(loginUser.getIsFirstLogin()));

        String token = JwtUtil.genToken(claims);
        LoggingConfig.logLogin(loginUser.getId().intValue(), username, true, "登录成功");
        return ApiResponse.success(token);
    }

    // 查询用户信息
    @GetMapping("/userInfo")
    public ApiResponse<User> userInfo() {

        // 根据用户ID查询用户信息（避免用户名修改后JWT token中的用户名过期问题）
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        User user = userService.findById(userId);
        
        return ApiResponse.success(user);
    }

    // 更新用户信息
    @PutMapping("/update")
    public ApiResponse<Void> update(@RequestBody @Validated User user) {  // @Validated用于验证参数
        // 获取当前登录用户的ID
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) claims.get("id");
        
        // 设置用户ID（确保只能更新当前登录用户的信息）
        user.setId(currentUserId.longValue());
        
        // 禁止更新用户名 - 用户名是唯一标识，不允许修改
        if (user.getUsername() != null) {
            return ApiResponse.error("用户名是唯一标识，不允许修改");
        }
        
        userService.update(user);
        return ApiResponse.success(null);
    }


    // 更新用户头像
    @PatchMapping("/updateAvatar")
    public ApiResponse<Void> updateAvatar(@RequestParam String avatarUrl) {
        userService.updateUserPic(avatarUrl);
        return ApiResponse.success(null);
    }

    // 首次登录密码修改
    @PostMapping("/first-login-password-change")
    public ApiResponse<String> firstLoginPasswordChange(@RequestParam String newPassword) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        String username = (String) claims.get("username");
        Boolean isFirstLogin = (Boolean) claims.get("isFirstLogin");
        
        // 检查是否为首次登录
        if (isFirstLogin == null || !isFirstLogin) {
            return ApiResponse.error("非首次登录用户无法使用此接口");
        }
        
        // 验证新密码格式
        if (newPassword == null || newPassword.length() < 8) {
            return ApiResponse.error("密码长度不能少于8位");
        }
        
        // 强密码验证：至少包含数字、字母和特殊字符中的两种
        boolean hasNumber = newPassword.matches(".*\\d.*");
        boolean hasLetter = newPassword.matches(".*[a-zA-Z].*");
        boolean hasSpecial = newPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?@].*");
        
        int typeCount = 0;
        if (hasNumber) typeCount++;
        if (hasLetter) typeCount++;
        if (hasSpecial) typeCount++;
        
        if (typeCount < 2) {
            return ApiResponse.error("密码必须包含数字、字母、特殊字符中的至少两种");
        }
        
        try {
            // 更新密码
            userMapper.updatePwd(PasswordUtil.encode(newPassword), userId);
            
            // 更新首次登录状态
            userService.updateFirstLogin(userId);
            
            LoggingConfig.logPasswordChange(userId, username, true, "首次登录密码修改成功");
            return ApiResponse.success("密码修改成功，请重新登录");
        } catch (Exception e) {
            LoggingConfig.logPasswordChange(userId, username, false, "首次登录密码修改失败: " + e.getMessage());
            return ApiResponse.error("密码修改失败");
        }
    }

    // 更新密码
    @PatchMapping("/updatePwd")
    public ApiResponse<Void> updatePwd(@RequestBody Map<String, String> params) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        Integer userId = (Integer) claims.get("id");

        // 参数校验
        String oldPwd = params.get("oldPwd");
        String newPwd = params.get("newPwd");
        String confirmPwd = params.get("confirmPwd");
        if(!StringUtils.hasText(oldPwd)){
            LoggingConfig.logPasswordChange(userId, username, false, "原密码不能为空");
            return ApiResponse.error("原密码不能为空");
        }
        if(!StringUtils.hasText(newPwd) || !StringUtils.hasText(confirmPwd)){
            LoggingConfig.logPasswordChange(userId, username, false, "新密码或确认密码不能为空");
            return ApiResponse.error("新密码或确认密码不能为空");
        }

        // 强制验证旧密码，防止持有 token 的攻击者直接改密劫持账号
        User user = userService.findByUserName(username);
        if(user == null || !PasswordUtil.matches(oldPwd, user.getPassword())){
            LoggingConfig.logPasswordChange(userId, username, false, "原密码错误");
            return ApiResponse.error("原密码错误");
        }

        // 校验 新密码和确认密码是否一致
        if(!newPwd.equals(confirmPwd)){
            LoggingConfig.logPasswordChange(userId, username, false, "新密码和确认密码不一致");
            return ApiResponse.error("新密码和确认密码不一致");
        }
        
        // 验证新密码格式（强密码验证）
        if (newPwd.length() < 8) {
            LoggingConfig.logPasswordChange(userId, username, false, "密码长度不能少于8位");
            return ApiResponse.error("密码长度不能少于8位");
        }
        
        // 强密码验证：至少包含数字、字母和特殊字符中的两种
        boolean hasNumber = newPwd.matches(".*\\d.*");
        boolean hasLetter = newPwd.matches(".*[a-zA-Z].*");
        boolean hasSpecial = newPwd.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?@].*");
        
        int typeCount = 0;
        if (hasNumber) typeCount++;
        if (hasLetter) typeCount++;
        if (hasSpecial) typeCount++;
        
        if (typeCount < 2) {
            LoggingConfig.logPasswordChange(userId, username, false, "密码必须包含数字、字母、特殊字符中的至少两种");
            return ApiResponse.error("密码必须包含数字、字母、特殊字符中的至少两种");
        }
        
        try {
            // 调用userService完成密码更新
            userService.updatePwd(newPwd);
            // 若用户修改了密码，视为已完成首次登录流程，清除首次登录标记
            // 这样后续登录不会再触发首次登录强制改密
            userService.updateFirstLogin(userId);
            LoggingConfig.logPasswordChange(userId, username, true, "密码修改成功");
            return ApiResponse.success(null);
        } catch (Exception e) {
            LoggingConfig.logPasswordChange(userId, username, false, "密码修改失败: " + e.getMessage());
            return ApiResponse.error("密码修改失败");
        }
    }






}
