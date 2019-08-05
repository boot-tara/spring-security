package com.boot.security.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class LayuiFile implements Serializable {

    private Integer code;
    private String msg;
    private Object data;

    public LayuiFile(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public LayuiFile() {
    }
}
