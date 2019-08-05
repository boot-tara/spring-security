package com.boot.security.service.impl;

import com.boot.security.dao.PermissionDao;
import com.boot.security.model.LoginUser;
import com.boot.security.model.Permission;
import com.boot.security.model.Status;
import com.boot.security.model.SysUser;
import com.boot.security.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    PermissionDao permissionDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //验证用户
        SysUser sysUser=userService.getUser(username);
        if(sysUser==null){
            throw new AuthenticationCredentialsNotFoundException("用户名不存在");
        }
        else if(sysUser.getStatus()== Status.DISABLED){
            throw new DisabledException("用户已作废");
        }
        else if(sysUser.getStatus()==Status.LOCKED){
            throw new DisabledException("用户被锁定");
        }
        //usernamepasswordfilter验证用户名密码是否一致
        LoginUser loginUser=new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        List<Permission> permissions=permissionDao.listByUserId(sysUser.getId());
        loginUser.setPermissions(permissions);
        return loginUser;
    }
}
