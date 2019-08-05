package com.boot.security.service;


import com.boot.security.model.Mail;
import com.boot.security.model.MailDto;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MailService {


	PageInfo<Mail> list(Integer pno, Integer psize, MailDto mailDto);

	void save(Mail mail, List<String> toUser);
}
