package com.boot.security.service;

import com.boot.security.model.Dict;

import java.util.List;

public interface DictService {
    List<Dict> getDictsByType(String type);
}
