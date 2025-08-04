package com.goodlab.server.pojo;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NonNull;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;


@Data
public class User {
    @NonNull
    private Integer id;//主键ID
    private String username;//用户名
    @JsonIgnore
    private String password;//密码
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "用户名格式错误")
    private String nickname;//昵称
    @NotEmpty
    @Email
    private String email;//邮箱
    private String userPic;//用户头像地址
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
