package com.boot.security.dao;

import com.boot.security.model.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DictDao {

    @Select("select * from t_dict where type=#{type}")
    List<Dict> getDictsByType(@Param("type") String type);
}
