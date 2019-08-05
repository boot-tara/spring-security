package com.boot.security.dao;

import com.boot.security.model.Notice;
import com.boot.security.model.NoticeVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeDao {
    List<Notice> getNoticeByPage(Notice notice);

    NoticeVo getNoticeVoByNoticeId(Integer noticeId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_notice(title, content, status, createTime, updateTime) values(#{title}, #{content}, #{status}, now(), now())")
    void save(Notice notice);

    @Select("select * from t_notice t where t.id = #{id}")
    Notice getById(Integer id);

    @Update("update t_notice t set title = #{title}, content = #{content}, status = #{status}, updateTime =now() where t.id = #{id}")
    void update(Notice notice);
}
