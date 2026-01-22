package com.goodlab.server.controller;


import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.pojo.User;
import com.goodlab.server.service.UserService;
import com.goodlab.server.mapper.UserMapper;
import com.goodlab.server.utils.ExcelStudentReader;
import com.goodlab.server.utils.JwtUtil;
import com.goodlab.server.utils.Md5Util;
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
    
    @Autowired
    private ExcelStudentReader excelStudentReader;

    // 注册功能已禁用 - 仅支持学号登录
    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestParam String username,
                           @RequestParam String password) {
        return ApiResponse.error("注册功能已禁用，请使用学号登录");
    }

    // 登录 - 支持学号+密码验证（初始密码和修改后的密码）、教师账号登录以及管理员账号登录
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestParam String username,
                       @RequestParam String password) {
        
        // 检查是否为管理员账号或教师账号
        boolean isAdminAccount = "goodlabAdmin".equals(username);
        boolean isTeacherAccount = username != null && username.startsWith("TCH_");
        
        // 对于非管理员和非教师账号，验证学号格式和是否存在于Excel文件中
        if (!isAdminAccount && !isTeacherAccount && !excelStudentReader.isValidStudentId(username)) {
            LoggingConfig.logLogin(null, username, false, "学号不存在或格式错误");
            // 清除缓存并重试一次
            excelStudentReader.clearCache();
            if (!excelStudentReader.isValidStudentId(username)) {
                return ApiResponse.error("学号不存在或格式错误，请输入10-12位数字学号");
            }
        }
        
        // 查找用户
        User loginUser = userService.findByUserName(username);
        if (loginUser == null) {
            // 管理员账号或教师账号不存在时直接返回错误
            if (isAdminAccount) {
                LoggingConfig.logLogin(null, username, false, "管理员账号不存在");
                return ApiResponse.error("管理员账号不存在");
            }
            
            if (isTeacherAccount) {
                LoggingConfig.logLogin(null, username, false, "教师账号不存在");
                return ApiResponse.error("教师账号不存在");
            }
            
            // 如果用户不存在，检查是否为初始密码格式，如果是则自动创建用户（仅对学生账号）
            if (excelStudentReader.isValidPasswordFormat(username, password)) {
                try {
                    userService.register(username, password);
                    loginUser = userService.findByUserName(username);
                    LoggingConfig.logLogin(loginUser.getId().intValue(), username, true, "首次登录，自动创建用户");
                } catch (Exception e) {
                    LoggingConfig.logLogin(null, username, false, "用户创建失败: " + e.getMessage());
                    return ApiResponse.error("用户创建失败");
                }
            } else {
                LoggingConfig.logLogin(null, username, false, "用户不存在，密码格式不正确");
                return ApiResponse.error("用户不存在，请使用初始密码格式：学号@ncu2025");
            }
        }
        
        // 验证密码（支持初始密码和修改后的密码）
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            
            // 仅依据数据库中的 is_first_login 字段控制首次登录改密
            boolean shouldShowFirstLogin = Boolean.TRUE.equals(loginUser.getIsFirstLogin());
            claims.put("isFirstLogin", shouldShowFirstLogin);
            
            // 已移除调试日志，避免控制台输出敏感信息
            
            String token = JwtUtil.genToken(claims);
            LoggingConfig.logLogin(loginUser.getId().intValue(), username, true, "登录成功");
            return ApiResponse.success(token);
        } else {
            LoggingConfig.logLogin(loginUser.getId().intValue(), username, false, "密码错误");
            return ApiResponse.error("密码错误");
        }
    }

    // 查询用户信息
    @GetMapping("/userInfo")
    public ApiResponse<User> userInfo() {

        // 根据用户ID查询用户信息（避免用户名修改后JWT token中的用户名过期问题）
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        User user = userService.findById(userId);
        
        // 如果用户名是学号格式，尝试获取学生真实姓名
        // 对于教师账号（TCH_开头）和管理员账号，不需要从Excel获取信息
        if (user != null && user.getUsername() != null 
            && !user.getUsername().equals("goodlabAdmin") 
            && !user.getUsername().startsWith("TCH_")) {
            try {
                com.goodlab.server.pojo.StudentInfo studentInfo = excelStudentReader.getStudentInfo(user.getUsername());
                if (studentInfo != null && studentInfo.getName() != null && !studentInfo.getName().trim().isEmpty()) {
                    // 仅在昵称为空或未设置时回填学生姓名，避免覆盖用户自定义昵称
                    if (user.getNickname() == null || user.getNickname().trim().isEmpty()) {
                        user.setNickname(studentInfo.getName());
                    }
                }
            } catch (Exception e) {
                // 如果获取学生信息失败，继续使用原有信息（不输出控制台）
            }
        }
        
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
        
        // 检查是否为管理员或教师
        if ("goodlabAdmin".equals(username)) {
            return ApiResponse.error("管理员账号无需修改密码");
        }
        
        if (username != null && username.startsWith("TCH_")) {
            return ApiResponse.error("教师账号无需修改密码");
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
            String md5Password = Md5Util.getMD5String(newPassword);
            userMapper.updatePwd(md5Password, userId);
            
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
        if(StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(confirmPwd)){
            LoggingConfig.logPasswordChange(userId, username, false, "新密码或确认密码不能为空");
            return ApiResponse.error("新密码或确认密码不能为空");
        }
        
        // 如果提供了旧密码，则进行验证；否则跳过（按需求允许仅输入新密码修改）
        if (!StringUtils.isEmpty(oldPwd)) {
            User user = userService.findByUserName(username);
            if(!Md5Util.getMD5String(oldPwd).equals(user.getPassword())){
                LoggingConfig.logPasswordChange(userId, username, false, "原密码错误");
                return ApiResponse.error("原密码错误");
            }
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
