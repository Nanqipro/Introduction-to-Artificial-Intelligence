package com.goodlab.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload.avatar-path:uploads/avatars/}")
    private String avatarUploadPath;
    
    @Value("${file.upload.base-url:http://localhost:8082/}")
    private String baseUrl;
    
    // 允许的图片文件扩展名白名单（小写）
    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = new HashSet<>(
        Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp")
    );
    
    // 允许的MIME类型白名单
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(
        Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", 
            "image/gif", "image/bmp", "image/webp"
        )
    );
    
    // 最大文件大小：5MB
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 验证文件是否为合法的图片文件
     * @param file 上传的文件
     * @throws IllegalArgumentException 如果文件不合法
     */
    private void validateImageFile(MultipartFile file) throws IllegalArgumentException {
        // 1. 验证文件是否为空
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        // 2. 验证文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小不能超过5MB");
        }
        
        // 3. 验证文件名（防止路径遍历攻击）
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..") || 
            originalFilename.contains("/") || originalFilename.contains("\\")) {
            throw new IllegalArgumentException("非法的文件名");
        }
        
        // 4. 验证文件扩展名
        String fileExtension = "";
        if (originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(fileExtension)) {
            throw new IllegalArgumentException(
                "不支持的文件类型，仅允许: " + String.join(", ", ALLOWED_IMAGE_EXTENSIONS)
            );
        }
        
        // 5. 验证MIME类型
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException(
                "不支持的MIME类型，仅允许图片文件"
            );
        }
        
        // 6. 验证文件头（Magic Number）- 额外的安全检查
        try {
            validateFileHeader(file.getInputStream(), fileExtension);
        } catch (IOException e) {
            throw new IllegalArgumentException("无法读取文件内容");
        }
    }
    
    /**
     * 验证文件头（Magic Number）确保文件类型真实性
     * @param inputStream 文件输入流
     * @param extension 文件扩展名
     * @throws IllegalArgumentException 如果文件头不匹配
     */
    private void validateFileHeader(InputStream inputStream, String extension) 
            throws IllegalArgumentException, IOException {
        byte[] header = new byte[8];
        int bytesRead = inputStream.read(header);
        
        if (bytesRead < 2) {
            throw new IllegalArgumentException("文件内容无效");
        }
        
        // 检查常见图片格式的文件头
        // JPEG: FF D8 FF
        // PNG: 89 50 4E 47
        // GIF: 47 49 46 38
        // BMP: 42 4D
        // WEBP: 52 49 46 46 (RIFF)
        
        boolean validHeader = false;
        
        if (extension.equals(".jpg") || extension.equals(".jpeg")) {
            validHeader = (header[0] == (byte) 0xFF && header[1] == (byte) 0xD8 && header[2] == (byte) 0xFF);
        } else if (extension.equals(".png")) {
            validHeader = (header[0] == (byte) 0x89 && header[1] == 0x50 && 
                          header[2] == 0x4E && header[3] == 0x47);
        } else if (extension.equals(".gif")) {
            validHeader = (header[0] == 0x47 && header[1] == 0x49 && 
                          header[2] == 0x46 && header[3] == 0x38);
        } else if (extension.equals(".bmp")) {
            validHeader = (header[0] == 0x42 && header[1] == 0x4D);
        } else if (extension.equals(".webp")) {
            validHeader = (header[0] == 0x52 && header[1] == 0x49 && 
                          header[2] == 0x46 && header[3] == 0x46);
        }
        
        if (!validHeader) {
            throw new IllegalArgumentException("文件内容与扩展名不匹配，可能是伪造的图片文件");
        }
    }
    
    /**
     * 安全地生成文件名（防止任何特殊字符）
     * @param extension 文件扩展名
     * @return 安全的文件名
     */
    private String generateSafeFileName(String extension) {
        // 仅使用时间戳和UUID，不包含任何用户输入
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uniqueId = UUID.randomUUID().toString().replaceAll("-", "");
        
        // 确保扩展名是安全的（仅包含字母和点）
        String safeExtension = extension.replaceAll("[^a-zA-Z.]", "");
        
        return "avatar_" + timestamp + "_" + uniqueId + safeExtension;
    }
    
    /**
     * 上传头像文件（增强安全版本）
     * @param file 头像文件
     * @return 头像访问URL
     * @throws IOException 文件操作异常
     * @throws IllegalArgumentException 文件验证失败
     */
    public String uploadAvatar(MultipartFile file) throws IOException, IllegalArgumentException {
        // 安全验证
        validateImageFile(file);
        
        // 创建上传目录
        File uploadDir = new File(avatarUploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new IOException("无法创建上传目录");
            }
        }
        
        // 获取安全的文件扩展名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }
        
        // 生成安全的文件名
        String fileName = generateSafeFileName(fileExtension);
        
        // 保存文件
        Path filePath = Paths.get(avatarUploadPath, fileName);
        
        // 使用StandardCopyOption.REPLACE_EXISTING可能不安全，改为检查文件是否已存在
        if (Files.exists(filePath)) {
            // 理论上UUID应该不会重复，但为了安全起见
            fileName = generateSafeFileName(fileExtension);
            filePath = Paths.get(avatarUploadPath, fileName);
        }
        
        Files.copy(file.getInputStream(), filePath);
        
        // 设置文件权限（仅所有者可读写）
        try {
            File savedFile = filePath.toFile();
            savedFile.setReadable(true, false); // 所有人可读
            savedFile.setWritable(true, true);  // 仅所有者可写
            savedFile.setExecutable(false);     // 不可执行
        } catch (Exception e) {
            // 权限设置失败不影响上传
            System.err.println("警告: 无法设置文件权限 - " + e.getMessage());
        }
        
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