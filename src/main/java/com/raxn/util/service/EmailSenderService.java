package com.raxn.util.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class EmailSenderService {

	Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);

	@Autowired
	private JavaMailSender mailsender;

	@Value("${sms.validity.minutes}")
	private String SMS_VALIDITY_MINUTES;

	@Value("${mailtemplate.path}")
	private String MAIL_TEMPLATE_PATH;

	@Async
	public void sendEmail(String tomail, String subject, String body) {
		MimeMessage mailMessage = mailsender.createMimeMessage();
		LOGGER.info("Entered into sendEmail()");
		LOGGER.info("tomail = " + tomail + " ,subject=" + subject);
		InternetAddress toAddress = null;
		try {

			toAddress = new InternetAddress(tomail);

			mailMessage.setSubject(subject, "utf-8");
			mailMessage.setText(body, "utf-8", "html");
			mailMessage.setRecipient(RecipientType.TO, toAddress);

			mailsender.send(mailMessage);
		} catch (Exception e) {
			LOGGER.error("Exception while sending email to " + tomail + " :: -> " + e.getMessage(), e);
		}

		LOGGER.info("mail send done");
		LOGGER.info("Exiting sendEmail()");

	}

	public void formatSuggestionEmail(String username, String email, String refNumber) {
		LOGGER.info("Entered into formatSuggestionEmail()");
		LOGGER.info("username = " + username + " ,email=" + email);
		LOGGER.info("refNumber = " + refNumber);
		StringBuilder mailcontent = new StringBuilder();
		mailcontent.setLength(0);
		String destFileName = "Enquirymail.html";
		int startIndex = 0, endIndex = 0;

		try {

			java.nio.file.Path pathTo = Paths.get(MAIL_TEMPLATE_PATH);
			java.nio.file.Path destination = Paths.get(pathTo.toString() + "/" + destFileName);
			FileReader file = new FileReader(destination.toFile());
			BufferedReader in = new BufferedReader(file);
			String str;

			while ((str = in.readLine()) != null) {
				mailcontent.append(str);
			}
		} catch (IOException e) {
			LOGGER.error("Error while preparing mailcontent-> " + e.getMessage());
		}

		startIndex = mailcontent.indexOf("{refnumber}");
		endIndex = startIndex + 11;
		mailcontent.replace(startIndex, endIndex, refNumber);

		sendEmail(email, "Received query on RechargeAXN", mailcontent.toString());

	}

	public void formatRegistrationEmail(String email, String fullname, String mobile) {
		LOGGER.info("Entered into formatRegistrationEmail()");
		LOGGER.info("email=" + email);

		StringBuilder mailcontent = new StringBuilder();
		mailcontent.setLength(0);
		String destFileName = "Registermail.html";
		int startIndex = 0, endIndex = 0;

		try {

			java.nio.file.Path pathTo = Paths.get(MAIL_TEMPLATE_PATH);
			java.nio.file.Path destination = Paths.get(pathTo.toString() + "/" + destFileName);
			FileReader file = new FileReader(destination.toFile());
			BufferedReader in = new BufferedReader(file);
			String str;

			while ((str = in.readLine()) != null) {
				mailcontent.append(str);
			}
		} catch (IOException e) {
			LOGGER.error("Error while preparing mailcontent-> " + e.getMessage());
		}

		String firstLetStr = fullname.substring(0, 1);
        String remLetStr = fullname.substring(1);
        firstLetStr = firstLetStr.toUpperCase();
        String firstLetterCapitalizedName = firstLetStr + remLetStr;
		
		startIndex = mailcontent.indexOf("{user.name}");
		endIndex = startIndex + 11;
		mailcontent.replace(startIndex, endIndex, firstLetterCapitalizedName);

		startIndex = mailcontent.indexOf("{user.mobile}");
		endIndex = startIndex + 13;
		mailcontent.replace(startIndex, endIndex, mobile);

		startIndex = mailcontent.indexOf("{user.mail}");
		endIndex = startIndex + 11;
		mailcontent.replace(startIndex, endIndex, email);

		sendEmail(email, "Registration completed on RechargeAXN", mailcontent.toString());
	}

	public void formatOTPEmail(String email, String purpose, String otp) {
		LOGGER.info("Entered into formatOTPEmail()");
		LOGGER.info("email=" + email + " ,purpose=" + purpose);

		StringBuilder mailcontent = new StringBuilder();
		mailcontent.setLength(0);
		String destFileName = "OTPmail.html";
		int startIndex = 0, endIndex = 0;

		try {

			java.nio.file.Path pathTo = Paths.get(MAIL_TEMPLATE_PATH);
			java.nio.file.Path destination = Paths.get(pathTo.toString() + "/" + destFileName);
			FileReader file = new FileReader(destination.toFile());
			BufferedReader in = new BufferedReader(file);
			String str;

			while ((str = in.readLine()) != null) {
				mailcontent.append(str);
			}
		} catch (IOException e) {
			LOGGER.error("Error while preparing mailcontent-> " + e.getMessage());
		}

		startIndex = mailcontent.indexOf("{otpnumber}");
		endIndex = startIndex + 11;
		mailcontent.replace(startIndex, endIndex, otp);

		startIndex = mailcontent.indexOf("{validity}");
		endIndex = startIndex + 10;
		mailcontent.replace(startIndex, endIndex, SMS_VALIDITY_MINUTES);

		sendEmail(email, "OTP mail from RechargeAXN", mailcontent.toString());
	}
	
	public void formatChangePasswordEmail(String email, String fullname) {
		LOGGER.info("Entered into formatChangePasswordEmail()");
		LOGGER.info("email=" + email);

		StringBuilder mailcontent = new StringBuilder();
		mailcontent.setLength(0);
		String destFileName = "PasswordChange.html";
		int startIndex = 0, endIndex = 0;

		try {

			java.nio.file.Path pathTo = Paths.get(MAIL_TEMPLATE_PATH);
			java.nio.file.Path destination = Paths.get(pathTo.toString() + "/" + destFileName);
			FileReader file = new FileReader(destination.toFile());
			BufferedReader in = new BufferedReader(file);
			String str;

			while ((str = in.readLine()) != null) {
				mailcontent.append(str);
			}
		} catch (IOException e) {
			LOGGER.error("Error while preparing mailcontent-> " + e.getMessage());
		}

		String firstLetStr = fullname.substring(0, 1);
        String remLetStr = fullname.substring(1);
        firstLetStr = firstLetStr.toUpperCase();
        String firstLetterCapitalizedName = firstLetStr + remLetStr;
		
		startIndex = mailcontent.indexOf("{User.Name}");
		endIndex = startIndex + 11;
		mailcontent.replace(startIndex, endIndex, firstLetterCapitalizedName);

		sendEmail(email, "Account Password Change", mailcontent.toString());
	}
	
	public void formatChangeMobileEmail(String email, String fullname, String mobile) {
		LOGGER.info("Entered into formatChangeMobileEmail()");
		LOGGER.info("email=" + email+" ,mobile="+mobile);

		StringBuilder mailcontent = new StringBuilder();
		mailcontent.setLength(0);
		String destFileName = "MobileChange.html";
		int startIndex = 0, endIndex = 0;

		try {

			java.nio.file.Path pathTo = Paths.get(MAIL_TEMPLATE_PATH);
			java.nio.file.Path destination = Paths.get(pathTo.toString() + "/" + destFileName);
			FileReader file = new FileReader(destination.toFile());
			BufferedReader in = new BufferedReader(file);
			String str;

			while ((str = in.readLine()) != null) {
				mailcontent.append(str);
			}
		} catch (IOException e) {
			LOGGER.error("Error while preparing mailcontent-> " + e.getMessage());
		}

		String firstLetStr = fullname.substring(0, 1);
        String remLetStr = fullname.substring(1);
        firstLetStr = firstLetStr.toUpperCase();
        String firstLetterCapitalizedName = firstLetStr + remLetStr;
		
		startIndex = mailcontent.indexOf("{User.Name}");
		endIndex = startIndex + 11;
		mailcontent.replace(startIndex, endIndex, firstLetterCapitalizedName);
		
		startIndex = mailcontent.indexOf("{user.mobile}");
		endIndex = startIndex + 13;
		mailcontent.replace(startIndex, endIndex, mobile);

		sendEmail(email, "Change Mobile on RechargeAXN", mailcontent.toString());
	}
}
