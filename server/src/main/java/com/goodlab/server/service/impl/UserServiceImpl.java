package com.goodlab.server.service.impl;


import com.goodlab.server.dao.UserDao;
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
    private UserDao userDao;

    @Override
    public User findByUserName(String username) {
        User user = userDao.findByUserName(username);
        return user;
    }
    
    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public void register(String username, String password) {

        // 加密 将明文改成密文
        String md5String = Md5Util.getMD5String(password);
        // 添加用户
        userDao.add(username, md5String);
    }

    // 更新用户信息
    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userDao.update(user);
    }

    // 更新用户头像
    @Override
    public void updateUserPic(String userPic) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        userDao.updateUserPic(userPic,id);

    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        String md5String = Md5Util.getMD5String(newPwd);
        userDao.updatePwd(md5String,id);
    }


}
