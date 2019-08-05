package com.boot.security.dao;

import com.boot.security.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from sys_user where username=#{username}")
    SysUser getUser(@Param("username") String username);
}
