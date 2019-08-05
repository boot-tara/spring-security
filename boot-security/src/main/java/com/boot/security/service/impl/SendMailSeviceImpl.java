package com.boot.security.service.impl;

import com.boot.security.service.SendMailSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class SendMailSeviceImpl implements SendMailSevice {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String serverMail;

    @Override
    public void sendMail(String toUser, String subject, String text) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(serverMail);
        helper.setTo(toUser);
        helper.setSubject(subject);
        helper.setText(text, true);//支持html标签

        javaMailSender.send(message);
    }
}
