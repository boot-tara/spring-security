package com.boot.security.dao;

import com.boot.security.model.FileInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface FileInfoDao {

    @Select("select * from file_info order by updateTime desc")
    List<FileInfo> getFileInfosByPage(Integer pno, Integer psize);

    @Select("select * from file_info where id=#{md5}")
    FileInfo getFile(@Param("md5") String md5);

    @Update("update file_info set updateTime=now() where id=#{md5}")
    void updateTime(@Param("md5") String md5);

    @Insert("insert into file_info(id, contentType, size, path, url, type, createTime, updateTime) values(#{id}, #{contentType}, #{size}, #{path}, #{url}, #{type}, now(), now())")
    int save(FileInfo fileInfo);

    @Delete("delete from file_info where id = #{id}")
    void delete(@Param("id") String id);
}
