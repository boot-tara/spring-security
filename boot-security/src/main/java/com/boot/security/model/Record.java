package com.boot.security.model;

import lombok.Data;

import java.util.List;
@Data
    public class Record {
    private Long id;
    private String name;

    private String description;
    private List<Integer>ids;
}
