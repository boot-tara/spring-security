package com.boot.security.dao;

import com.boot.security.model.Permission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
public interface PermissionDao {
    @Select("select distinct p.* from sys_permission p inner join sys_role_permission rp on p.id = rp.permissionId inner join sys_role_user ru on ru.roleId = rp.roleId where ru.userId = #{userId} order by p.sort")
    List<Permission> listByUserId(Long id);

    @Select("select * from sys_permission order by sort")
    List<Permission> listAll();

    @Select("select * from sys_permission where type=1 order by sort")
    List<Permission> listParents();

    @Insert("insert into sys_permission(parentId, name, css, href, type, permission, sort) values(#{parentId}, #{name}, #{css}, #{href}, #{type}, #{permission}, #{sort})")
    void save(Permission permission);

    @Select("select * from sys_permission where id=#{id}")
    Permission getPermission(Long id);

    @Update("update sys_permission t set parentId = #{parentId}, name = #{name}, css = #{css}, href = #{href}, type = #{type}, permission = #{permission}, sort = #{sort} where t.id = #{id}")
    void update(Permission permission);


    void delete(Long id);
    
    @Select("select * from sys_permission where parentId=#{id}")
    List<Permission> selectChildIds(Long id);

    void deleteChild(List<Long> ids);

    void deleteRoleandPermission(List<Long> ids);

    @Select("select sp.* from sys_role_permission  srp  inner join sys_permission sp on srp.permissionId = sp.id where srp.roleId = #{id} order by sp.sort ")
    List<Permission> getPermissionsByRoleId(Integer id);
}
