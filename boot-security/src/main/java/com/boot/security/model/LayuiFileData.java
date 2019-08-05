package com.boot.security.model;

import lombok.Data;

@Data
public class LayuiFileData {
    private String src;
    private String title;

    public LayuiFileData(String src, String title) {
        this.src = src;
        this.title = title;
    }

    public LayuiFileData() {
    }
}
