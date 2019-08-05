package com.boot.security.service.impl;

import com.boot.security.dao.UserDao;
import com.boot.security.model.SysUser;
import com.boot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public SysUser getUser(String username) {
        return userDao.getUser(username);
    }
}
