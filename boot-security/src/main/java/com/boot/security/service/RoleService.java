package com.boot.security.service;

import com.boot.security.model.Record;
import com.boot.security.model.Role;
import com.github.pagehelper.PageInfo;

public interface RoleService {
    PageInfo<Role> getRolesByPage(String content);

    Role getRoleById(Integer id);

    void update(Record record);
}
