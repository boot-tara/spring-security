package com.boot.security.service;

import com.boot.security.model.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> listAll();

    List<Permission> listParents();

    void save(Permission permission);

    Permission getPermission(Long id);

    void update(Permission permission);

    void delete(Long id);

    List<Permission> getPermissionsByRoleId(Integer id);
}
