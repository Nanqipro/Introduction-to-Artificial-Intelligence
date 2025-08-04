package com.goodlab.server.service;


import com.goodlab.server.pojo.User;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

public interface UserService {


    // 通过用户名查询用户
    User findByUserName(String username);
    //  注册
    void register(String username, String password) ;


    void update(User user);

    void updateUserPic(String userPic);

    void updatePwd(String newPwd);
}
