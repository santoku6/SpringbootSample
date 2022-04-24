package com.raxn.util.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


public class EmailSenderService {

	Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);

	@Autowired
	private JavaMailSender mailsender;

	public void sendEmail(String userId, String tomail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		// message.setFrom("help@rechargeaxn.com");
		LOGGER.info("Entered into EmailSenderService.sendEmail()");
		LOGGER.info("tomail = " + tomail + " ,subject=" + subject + " ,body=" + body);
		message.setTo(tomail);
		message.setText(body);
		message.setSubject(subject);

		mailsender.send(message);

		LOGGER.info("mail send done");
		LOGGER.info("Exiting sendEmail()");
	}
}
