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

    @Insert("insert into user(username, password, nickname, email, phone, gender, birthday, location, bio, role, level, experience, total_score, completed_chapters, quiz_count, correct_answers, is_first_login, create_time, update_time) values(#{username}, #{md5String}, #{username}, CONCAT(#{username}, '@example.com'), '', '', null, '', '', '学生', 1, 0, 0, 0, 0, 0, true, now(), now())")
    void add(String username, String md5String);

    @Update("update user set nickname = #{nickname}, email = #{email}, phone = #{phone}, gender = #{gender}, birthday = #{birthday}, location = #{location}, bio = #{bio}, school = #{school}, major = #{major}, role = #{role}, level = #{level}, experience = #{experience}, total_score = #{totalScore}, completed_chapters = #{completedChapters}, quiz_count = #{quizCount}, correct_answers = #{correctAnswers}, study_time = #{studyTime}, update_time = now() where id = #{id}")
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
    
    @Select("select * from user where email = #{email}")
    User findByEmail(String email);
    
    @Update("update user set reset_token = #{resetToken}, reset_token_expiry = #{resetTokenExpiry}, update_time = now() where email = #{email}")
    void updateResetToken(String email, String resetToken, java.time.LocalDateTime resetTokenExpiry);
    
    @Select("select * from user where reset_token = #{resetToken} and reset_token_expiry > now()")
    User findByValidResetToken(String resetToken);
    
    @Update("update user set password = #{password}, reset_token = null, reset_token_expiry = null, update_time = now() where reset_token = #{resetToken}")
    void resetPasswordByToken(String resetToken, String password);
    
    @Update("update user set email_verified = #{emailVerified}, update_time = now() where email = #{email}")
    void updateEmailVerified(String email, boolean emailVerified);
    
    @Update("update user set is_first_login = false, last_login_time = now(), update_time = now() where id = #{id}")
    void updateFirstLogin(Integer id);
}