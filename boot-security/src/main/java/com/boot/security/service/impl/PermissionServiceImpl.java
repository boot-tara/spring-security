package com.boot.security.service.impl;

import com.boot.security.dao.PermissionDao;
import com.boot.security.model.Permission;
import com.boot.security.service.PermissionService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionDao permissionDao;
    @Override
    public List<Permission> listAll() {
        return permissionDao.listAll();
    }

    @Override
    public List<Permission> listParents() {
        return permissionDao.listParents();
    }

    @Override
    public void save(Permission permission) {
         permissionDao.save(permission);
    }

    @Override
    public Permission getPermission(Long id) {
        return permissionDao.getPermission(id);
    }

    @Override
    public void update(Permission permission) {
        permissionDao.update(permission);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        //该id和他的下面的孩子的ids
        List<Long>ids=new ArrayList<>();
        List<Permission>list=permissionDao.selectChildIds(id);
        if(CollectionUtils.isNotEmpty(list)){
            ids=list.stream().map(x->x.getId()).collect(Collectors.toList());
            System.out.println(ids);
        }
        //删除自己
        permissionDao.delete(id);
        //删除儿子
        permissionDao.deleteChild(ids);
        //删除关联表role-permission
        ids.add(id);
        System.out.println(ids);
        permissionDao.deleteRoleandPermission(ids);
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Integer id) {
        return permissionDao.getPermissionsByRoleId(id);
    }
}
