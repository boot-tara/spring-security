package com.boot.security.dao;

import com.boot.security.model.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleDao {

    List<Role> getRolesByPage(@Param("content") String content);

    @Select("select * from sys_role where id=#{id}")
    Role getRoleById(Integer id);

    @Delete("delete from sys_role_permission where roleId=#{id}")
    void deleteAll(Long id);

    void insertBatch(@Param("roleId")Long roleId, @Param("ids")List<Integer>ids);
}
