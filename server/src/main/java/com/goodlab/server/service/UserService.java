package com.goodlab.server.service;


import com.goodlab.server.pojo.User;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

public interface UserService {


    // 通过用户名查询用户
    User findByUserName(String username);
    
    // 通过ID查询用户
    User findById(Integer id);
    
    //  注册
    void register(String username, String password) ;


    void update(User user);

    boolean isUsernameAvailable(String username, Integer excludeId);

    void updateUserPic(String userPic);
    
    void updateUserPicById(Integer userId, String userPic);

    void updatePwd(String newPwd);
}
