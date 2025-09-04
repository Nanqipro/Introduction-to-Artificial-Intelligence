package com.goodlab.server.mapper;

import com.goodlab.server.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username}")
    User findByUserName(String username);

    @Insert("insert into user(username, password, nickname, email, phone, gender, birthday, location, bio, role, level, experience, total_score, completed_chapters, quiz_count, correct_answers, create_time, update_time) values(#{username}, #{md5String}, #{username}, CONCAT(#{username}, '@example.com'), '', '', null, '', '', '学生', 1, 0, 0, 0, 0, 0, now(), now())")
    void add(String username, String md5String);

    @Update("update user set username = #{username}, nickname = #{nickname}, email = #{email}, phone = #{phone}, gender = #{gender}, birthday = #{birthday}, location = #{location}, bio = #{bio}, role = #{role}, update_time = now() where id = #{id}")
    void update(User user);

    @Select("select count(*) from user where username = #{username} and id != #{id}")
    int countByUsernameExcludingId(String username, Integer id);

    @Update("update user set user_pic = #{userPic}, update_time = now() where id = #{id}")
    void updateUserPic(String userPic, Integer id);

    @Update("update user set password = #{md5String}, update_time = now() where id = #{id}")
    void updatePwd(String md5String, Integer id);

    @Select("select * from user where id = #{id}")
    User findById(Integer id);

    @Select("select * from user order by level desc, experience desc")
    java.util.List<User> findAllUsers();
}