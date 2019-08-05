package com.boot.security.util;

import lombok.Data;

import java.io.Serializable;
@Data
public class ResponseInfo implements Serializable {

    private String message;
    private String code;
    public ResponseInfo(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ResponseInfo() {
    }
}
