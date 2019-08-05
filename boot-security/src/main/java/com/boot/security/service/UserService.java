package com.boot.security.service;

import com.boot.security.model.SysUser;

public interface UserService {

    SysUser getUser(String username);
}
