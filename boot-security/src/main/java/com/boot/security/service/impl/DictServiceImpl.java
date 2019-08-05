package com.boot.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.boot.security.dao.DictDao;
import com.boot.security.model.Dict;
import com.boot.security.service.DictService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    DictDao dictDao;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public List<Dict> getDictsByType(String type) {
        String s = redisTemplate.opsForValue().get("dict:" + type);
        List<Dict> dicts = JSON.parseArray(s, Dict.class);
        if(CollectionUtils.isEmpty(dicts)){
            dicts = dictDao.getDictsByType(type);
            redisTemplate.opsForValue().set("dict:"+type, JSON.toJSONString(dicts));
        }
        return dicts;
    }
}
