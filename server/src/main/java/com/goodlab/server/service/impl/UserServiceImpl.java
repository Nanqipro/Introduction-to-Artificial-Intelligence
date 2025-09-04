package com.goodlab.server.service.impl;

import com.goodlab.server.mapper.UserMapper;
import com.goodlab.server.pojo.User;
import com.goodlab.server.service.UserService;
import com.goodlab.server.utils.Md5Util;
import com.goodlab.server.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username) {
        User user = userMapper.findByUserName(username);
        return user;
    }

    @Override
    public User findById(Integer id) {
        // 由于UserMapper没有findById方法，我们需要通过用户名来查找
        // 这里暂时返回null，或者可以通过其他方式实现
        return null;
    }

    @Override
    public void register(String username, String password) {
        // 加密 将明文改成密文
        String md5String = Md5Util.getMD5String(password);
        // 添加用户
        userMapper.add(username, md5String);
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
    public void updatePwd(String newPwd) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        String md5String = Md5Util.getMD5String(newPwd);
        userMapper.updatePwd(md5String, id);
    }

}
