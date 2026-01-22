package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.service.FileUploadService;
import com.goodlab.server.service.UserService;
import com.goodlab.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
// 使用全局CORS配置，无需在Controller级别重复配置
// 如需特殊配置，可在此处指定
public class UploadController {

    @Autowired
    private FileUploadService fileUploadService;
    
    @Autowired
    private UserService userService;

    /**
     * 上传用户头像
     * @param file 头像文件
     * @return 上传结果
     */
    @PostMapping("/avatar")
    public ApiResponse<Map<String, Object>> uploadAvatar(
            @RequestParam("file") MultipartFile file, 
            HttpServletRequest request) {
        try {
            // 从请求头获取token并解析用户ID
            String token = request.getHeader("Authorization");
            if (token == null || token.trim().isEmpty()) {
                return ApiResponse.error("未提供认证token");
            }
            
            // 处理Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            Map<String, Object> claims = JwtUtil.parseToken(token);
            Integer userId = (Integer) claims.get("id");
            
            // 上传文件（FileUploadService内部会进行全面的安全验证）
            String avatarUrl = fileUploadService.uploadAvatar(file);
            
            // 更新用户头像
            userService.updateUserPicById(userId, avatarUrl);
            
            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("avatarUrl", avatarUrl);
            return ApiResponse.success(result);
            
        } catch (IllegalArgumentException e) {
            // 文件验证失败
            return ApiResponse.error("文件验证失败: " + e.getMessage());
        } catch (IOException e) {
            // 文件操作失败
            return ApiResponse.error("文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            // 其他异常
            return ApiResponse.error("头像上传失败: " + e.getMessage());
        }
    }
}