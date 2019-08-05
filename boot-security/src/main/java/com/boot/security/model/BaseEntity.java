package com.boot.security.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class BaseEntity<T extends Serializable> implements  Serializable{

    private T id;
    private Date createTime=new Date();
    private Date updateTime=new Date();

}
