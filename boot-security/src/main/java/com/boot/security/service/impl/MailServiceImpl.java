package com.boot.security.service.impl;

import com.boot.security.dao.MailDao;
import com.boot.security.model.Mail;
import com.boot.security.model.MailDto;
import com.boot.security.service.MailService;
import com.boot.security.service.SendMailSevice;
import com.boot.security.util.UserUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	MailDao mailDao;

	@Autowired
	private SendMailSevice sendMailSevice;

	@Override
	public PageInfo<Mail> list(Integer pno, Integer psize, MailDto mailDto) {
		PageHelper.startPage(pno,psize);
		List<Mail>lists=mailDao.list(mailDto);
		return new PageInfo<>(lists);
	}

	@Transactional
	@Override
	public void save(Mail mail, List<String> toUser) {
		//发送邮件
		toUser.stream().forEach(u->{
			int status=1;
			try {
				sendMailSevice.sendMail(u,mail.getSubject(),mail.getContent());
			} catch (MessagingException e) {
				e.printStackTrace();
				status=0;
			}
			//添加数据库
			mail.setUserId(UserUtil.getLoginUser().getId());
			mailDao.save(mail);
			mailDao.saveToUser(mail.getId(), u, status);

		});

	}
}
