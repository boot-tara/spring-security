package com.boot.security.service;

import javax.mail.MessagingException;

public interface SendMailSevice {
	void sendMail(String u, String subject, String content) throws MessagingException;

	/**
	 * 
	 * @param toUser
	 * @param subject
	 *            标题
	 * @param text
	 *            内容（支持html格式）
	 */

}
