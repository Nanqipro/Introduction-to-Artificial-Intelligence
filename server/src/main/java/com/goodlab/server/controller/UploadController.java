package com.goodlab.server.controller;

import com.goodlab.server.model.ApiResponse;
import com.goodlab.server.service.FileUploadService;
import com.goodlab.server.service.UserService;
import com.goodlab.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174", "http://localhost:3000", "http://127.0.0.1:5173", "http://127.0.0.1:5174" })
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
    public ApiResponse<Map<String, Object>> uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            // 验证文件
            if (file.isEmpty()) {
                return ApiResponse.error("文件不能为空");
            }
            
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ApiResponse.error("只能上传图片文件");
            }
            
            // 验证文件大小 (5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ApiResponse.error("文件大小不能超过5MB");
            }
            
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
            
            // 上传文件
            String avatarUrl = fileUploadService.uploadAvatar(file);
            
            // 更新用户头像
            userService.updateUserPicById(userId, avatarUrl);
            
            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("avatarUrl", avatarUrl);
            return ApiResponse.success(result);
            
        } catch (Exception e) {
            return ApiResponse.error("头像上传失败: " + e.getMessage());
        }
    }
}