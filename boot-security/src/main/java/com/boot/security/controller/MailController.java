package com.boot.security.controller;

import com.boot.security.dao.MailDao;
import com.boot.security.model.Mail;
import com.boot.security.model.MailDto;
import com.boot.security.model.MailTo;
import com.boot.security.service.MailService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mails")
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private MailDao mailDao;

    @GetMapping("/{pno}/{psize}")
    @PreAuthorize("hasAuthority('mail:all:query')")
    public PageInfo<Mail> list(@PathVariable Integer pno, @PathVariable Integer psize, MailDto mailDto) {
        return mailService.list(pno, psize, mailDto);
    }


    //获取mail信息
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('mail:all:query')")
    public Mail get(@PathVariable Long id) {
        return mailDao.getById(id);
    }

    //获取邮见的用户信息，状态
    @GetMapping("/{id}/to")
    @PreAuthorize("hasAuthority('mail:all:query')")
    public List<MailTo> getMailTo(@PathVariable Long id) {
        return mailDao.getToUsers(id);
    }

    //发送邮件
    @PostMapping
    @PreAuthorize("hasAuthority('mail:send')")
    public Mail save( @RequestBody  Mail mail) {
        String toUsers = mail.getToUsers().trim();
        if (StringUtils.isBlank(toUsers)) {
            throw new IllegalArgumentException("收件人不能为空");
        }
        toUsers.replace(" ","");    //取所有的空格  2 2 -》  22
         toUsers.replace("；",";");
        String[] split = toUsers.split(";");
        List<String> toUser = Arrays.asList(split);
        //发送邮件
        toUser= toUser.stream().filter(u -> StringUtils.isNotBlank(u)).map(u -> u.trim()).collect(Collectors.toList());
        mailService.save(mail, toUser);

        return mail;
    }
}
