package com.boot.security.service.impl;

import com.boot.security.dao.RoleDao;
import com.boot.security.model.Record;
import com.boot.security.model.Role;
import com.boot.security.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    @Override
    public PageInfo<Role> getRolesByPage(String content) {
        List<Role>list=roleDao.getRolesByPage(content);
        return new PageInfo<Role>(list);
    }

    @Override
    public Role getRoleById(Integer id) {

        return roleDao.getRoleById(id);
    }

    @Override
    @Transactional
    public void update(Record record) {
        //清空所有
        roleDao.deleteAll(record.getId());
        //批量添加
        roleDao.insertBatch(record.getId(),record.getIds());
    }
}
