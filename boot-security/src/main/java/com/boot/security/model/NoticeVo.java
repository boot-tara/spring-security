package com.boot.security.model;

import lombok.Data;

import java.util.List;

@Data
public class NoticeVo extends Notice {
    private List<SysUser> users;
}
