package com.boot.security.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boot.security.dao.PermissionDao;
import com.boot.security.model.LoginUser;
import com.boot.security.model.Permission;
import com.boot.security.service.PermissionService;
import com.boot.security.util.UserUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/permissions")
public class PermissionController {


    @Autowired
    PermissionService permissionService;

    @Autowired
    PermissionDao permissionDao;

    //左侧菜单栏 parallelStream快，结果乱的，   stream慢，结果一制
    @GetMapping("/current")
    public List<Permission> permissionsCurrent(){
        //进入这里了，代表一定登陆成功了，取出登陆用户
        LoginUser loginUser = UserUtil.getLoginUser();  //这边如果是null，有异常，异常处理器
        List<Permission> list = loginUser.getPermissions();
        List<Permission> permissions = list.stream().filter(l -> l.getType().equals(1)).collect(Collectors.toList());
        //递归造就多级菜单
        //选出所有的以及菜单
        List<Permission> firstLevel = permissions.stream().filter(p -> p.getParentId().equals(0L)).collect(Collectors.toList());
        //便利递归
        firstLevel.parallelStream().forEach(x->{
            setChild(x,permissions);
        });
        return firstLevel;
    }

    //递归除了一级菜单一下的菜单
    private void setChild(Permission x, List<Permission> permissions) {
        List<Permission> child = permissions.stream().filter(a -> a.getParentId().equals(x.getId())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(child)){
            x.setChild(child);
            child.parallelStream().forEach(c->
            {
                setChild(c,permissions);
            });
        }
    }


    //treetable
    @GetMapping
    @PreAuthorize("hasAuthority('sys:menu:query')")
    public List<Permission>  permissionList(){
        List<Permission>permissionAll=permissionService.listAll();
        List<Permission>list=new ArrayList<>();
        //讲list做成标准treetable形式
        setPermissionsList(0L,permissionAll,list);
        return list;
    }

    //讲list做成标准treetable形式
    private void setPermissionsList(long l, List<Permission> permissionAll, List<Permission> list) {
       for(Permission p:permissionAll){
           if(p.getParentId().equals(l)){
               list.add(p);
               //讲该id下的孩子跟着放入集合中
               setPermissionsList(p.getId(),permissionAll,list);
           }
       }
    }


    //自己的权限集合 (字符串)
    @GetMapping("/owns")
    public List<String> ownsPermissions(){
        List<Permission> permissions = UserUtil.getLoginUser().getPermissions();
        return permissions.stream().filter(p-> StringUtils.isNotBlank(p.getPermission())).map(x->x.getPermission()).collect(Collectors.toList());
    }

    //添加菜单  一级菜单
    @PreAuthorize("hasAuthority('sys:menu:query')")
    @GetMapping("/parents")
    public List<Permission> parentMenu(){
        return permissionService.listParents();
    }


    //点击添加
    @PostMapping
    @PreAuthorize("hasAuthority('sys:menu:add')")
    public void save(@RequestBody Permission permission){
        permissionService.save(permission);
    }


    //回显
    @PreAuthorize("hasAuthority('sys:menu:add')")
    @GetMapping("/{id}")
    public Permission get(@PathVariable Long id){
        return permissionService.getPermission(id);
    }

    //修改
    @PreAuthorize("hasAuthority('sys:menu:add')")
    @PutMapping
    public void update(@RequestBody Permission permission){
        permissionService.update(permission);
    }

    //删除
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:menu:del')")
    public void delete(@PathVariable Long id) {
        permissionService.delete(id);
    }

    //角色中获取角色所有的全选集合
    @PreAuthorize("hasAuthority('sys:role:add')")
    @GetMapping("/role/{id}")
    public List<Permission>getPermissionsByRoleId(@PathVariable Integer id){
        return permissionService.getPermissionsByRoleId(id);
    }

    //所有的权限集合
    @PreAuthorize("hasAuthority('sys:role:add')")
    @GetMapping("/all")
    public List<Permission>permissions(){
        List<Permission> permissions = permissionService.listAll();
        List<Permission> firstLevel = permissions.stream().filter(p -> p.getParentId().equals(0L)).collect(Collectors.toList());
        firstLevel.stream().forEach(x->{
            setChild(x,permissions);
        });
        return firstLevel;
    }

}
