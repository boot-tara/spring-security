package com.boot.security.controller;

import com.boot.security.model.Dict;
import com.boot.security.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dicts")
public class DictController {

    @Autowired
    DictService dictService;

    @GetMapping("/{type}")
    public List<Dict>getDictsByType(@PathVariable String type){
        return dictService.getDictsByType(type);
    }
}
