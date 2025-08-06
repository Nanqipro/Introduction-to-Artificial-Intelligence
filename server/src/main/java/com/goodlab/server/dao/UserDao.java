package com.goodlab.server.dao;

import com.goodlab.server.pojo.User;
import com.goodlab.server.utils.Md5Util;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UserDao {
    private final Map<String, User> users = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public UserDao() {
        initializeUsers();
    }

    private void initializeUsers() {
        // 创建管理员用户
        User admin = new User();
        admin.setId(idGenerator.getAndIncrement());
        admin.setUsername("admin");
        admin.setPassword(Md5Util.getMD5String("admin"));
        admin.setNickname("Administrator");
        admin.setEmail("admin@example.com");
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        users.put("admin", admin);

        // 创建测试用户
        User test = new User();
        test.setId(idGenerator.getAndIncrement());
        test.setUsername("test");
        test.setPassword(Md5Util.getMD5String("test"));
        test.setNickname("Test User");
        test.setEmail("test@example.com");
        test.setCreateTime(LocalDateTime.now());
        test.setUpdateTime(LocalDateTime.now());
        users.put("test", test);
    }

    public User findByUserName(String username) {
        return users.get(username);
    }
    
    public User findById(Integer id) {
        return users.values().stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    public java.util.Collection<User> getAllUsers() {
        return users.values();
    }

    public void add(String username, String password) {
        User user = new User();
        user.setId(idGenerator.getAndIncrement());
        user.setUsername(username);
        user.setPassword(password);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        users.put(username, user);
    }

    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        users.put(user.getUsername(), user);
    }

    public void updateUserPic(String userPic, Integer id) {
        for (User user : users.values()) {
            if (user.getId().equals(id)) {
                user.setUserPic(userPic);
                user.setUpdateTime(LocalDateTime.now());
                break;
            }
        }
    }

    public void updatePwd(String password, Integer id) {
        for (User user : users.values()) {
            if (user.getId().equals(id)) {
                user.setPassword(password);
                user.setUpdateTime(LocalDateTime.now());
                break;
            }
        }
    }
}