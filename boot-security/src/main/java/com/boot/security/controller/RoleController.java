package com.boot.security.controller;

import com.boot.security.model.Record;
import com.boot.security.model.Role;
import com.boot.security.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/{pno}/{psize}")
    public PageInfo<Role> getRolesByPage(@PathVariable  Integer pno, @PathVariable Integer psize, String content){
        PageHelper.startPage(pno,psize);
       return roleService.getRolesByPage(content);
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Integer id){
        return roleService.getRoleById(id);
    }

    @PostMapping
    public void update( Record record){
         roleService.update(record);
    }
}
