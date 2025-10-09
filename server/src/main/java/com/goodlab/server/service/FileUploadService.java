package com.goodlab.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload.avatar-path:uploads/avatars/}")
    private String avatarUploadPath;
    
    @Value("${file.upload.base-url:http://localhost:8082/}")
    private String baseUrl;

    /**
     * 上传头像文件
     * @param file 头像文件
     * @return 头像访问URL
     * @throws IOException 文件操作异常
     */
    public String uploadAvatar(MultipartFile file) throws IOException {
        // 创建上传目录
        File uploadDir = new File(avatarUploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        // 使用时间戳和UUID生成唯一文件名
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String fileName = "avatar_" + timestamp + "_" + uniqueId + fileExtension;
        
        // 保存文件
        Path filePath = Paths.get(avatarUploadPath, fileName);
        Files.copy(file.getInputStream(), filePath);
        
        // 返回访问URL
        return baseUrl + avatarUploadPath + fileName;
    }
    
    /**
     * 删除文件
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            return file.exists() && file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}