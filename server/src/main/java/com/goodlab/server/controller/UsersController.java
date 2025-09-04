package com.goodlab.server.controller;


import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.pojo.User;
import com.goodlab.server.service.UserService;
import com.goodlab.server.utils.JwtUtil;
import com.goodlab.server.utils.Md5Util;
import com.goodlab.server.utils.ThreadLocalUtil;
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

    // 注册
    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestParam @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "用户名格式错误") String username,
                           @RequestParam @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "密码格式错误") String password) {
        // 查询用户
        User user = userService.findByUserName(username);
        if (user != null) {
            return ApiResponse.error("用户名已存在");
        }
        // 没被占用
        // 创建用户
        userService.register(username, password);
        return ApiResponse.success(null);
    }

    // 登录
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestParam @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "用户名格式错误") String username,
                       @RequestParam @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "密码格式错误") String password) {
        // 获取用户
        User loginUser = userService.findByUserName(username);
        if (loginUser == null) {
            return ApiResponse.error("用户不存在");
        }
        // 验证密码
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            return ApiResponse.success(token);

         }else{
            return ApiResponse.error("密码错误");
        }
    }

    // 查询用户信息
    @GetMapping("/userInfo")
    public ApiResponse<User> userInfo() {

        // 根据用户名查询用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        User user = userService.findByUserName(username);
        return ApiResponse.success(user);
    }

    // 更新用户信息
    @PutMapping("/update")
    public ApiResponse<Void> update(@RequestBody @Validated User user) {  // @Validated用于验证参数
        // 获取当前登录用户的ID
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) claims.get("id");
        
        // 设置用户ID（确保只能更新当前登录用户的信息）
        user.setId(currentUserId);
        
        // 检查用户名是否已被其他用户使用
        if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
            if (!userService.isUsernameAvailable(user.getUsername(), currentUserId)) {
                return ApiResponse.error("用户名已存在");
            }
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

    // 更新密码
    @PatchMapping("/updatePwd")
    public ApiResponse<Void> updatePwd(@RequestBody Map<String, String> params) {

        // 参数校验
        String oldPwd = params.get("oldPwd");
        String newPwd = params.get("newPwd");
        String confirmPwd = params.get("confirmPwd");
        if(StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(confirmPwd)){
            return ApiResponse.error("参数不能为空");
        }
        // 验证密码是否正确
        // 调用 userService 根据用户名拿到原密码 再和old_pwd进行比较
        Map<String, Object> claims = ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        User user = userService.findByUserName(username);
        if(!Md5Util.getMD5String(oldPwd).equals(user.getPassword())){
            return ApiResponse.error("原密码错误");
        }

        // 校验 新密码和确认密码是否一致
        if(!newPwd.equals(confirmPwd)){
            return ApiResponse.error("新密码和确认密码不一致");
        }
        // 调用userService完成密码更新
        userService.updatePwd(newPwd);
        return ApiResponse.success(null);
    }






}
