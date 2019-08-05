package com.boot.security.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity<Long>{

    private String username;
    private String password;
    private String nickname;
    private String headImgUrl;
    private String phone;
    private String telephone;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;
    private Integer sex;
    private Integer status;
    private String intro;

    public SysUser(String username, String password, String nickname, String headImgUrl, String phone, String telephone, String email, Date birthday, Integer sex, Integer status, String intro) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.headImgUrl = headImgUrl;
        this.phone = phone;
        this.telephone = telephone;
        this.email = email;
        this.birthday = birthday;
        this.sex = sex;
        this.status = status;
        this.intro = intro;
    }

    public SysUser() {
    }





}
