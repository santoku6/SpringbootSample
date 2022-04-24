package com.raxn.util.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


public class EmailSenderService {

	Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

	@Autowired
	private JavaMailSender mailsender;

	public void sendEmail(String userId, String tomail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		// message.setFrom("help@rechargeaxn.com");
		logger.info("Entered into EmailSenderService.sendEmail()");
		logger.info("tomail = " + tomail + " ,subject=" + subject + " ,body=" + body);
		message.setTo(tomail);
		message.setText(body);
		message.setSubject(subject);

		mailsender.send(message);

		logger.info("mail send done");
		logger.info("Exiting sendEmail()");
	}
}
