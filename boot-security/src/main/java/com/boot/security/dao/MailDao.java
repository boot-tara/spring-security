package com.boot.security.dao;

import com.boot.security.model.Mail;
import com.boot.security.model.MailDto;
import com.boot.security.model.MailTo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MailDao {


	List<Mail> list(MailDto mailDto);

	@Select("select * from t_mail t where t.id = #{id}")
	Mail getById(Long id);

	@Select("select t.* from t_mail_to t where t.mailId = #{mailId}")
	List<MailTo> getToUsers(Long id);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into t_mail(userId, subject, content, createTime, updateTime) values(#{userId}, #{subject}, #{content}, now(), now())")
	int save(Mail mail);

	@Insert("insert into t_mail_to(mailId, toUser, status) values(#{mailId}, #{toUser}, #{status})")
	int saveToUser(@Param("mailId") Long mailId, @Param("toUser") String toUser, @Param("status") int status);
}
