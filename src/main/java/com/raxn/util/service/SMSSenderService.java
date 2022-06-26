package com.raxn.util.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.raxn.entity.SmsTemplates;
import com.raxn.repository.SmsTemplatesRepository;

@Service
@EnableAsync
public class SMSSenderService {

	Logger LOGGER = LoggerFactory.getLogger(SMSSenderService.class);

	@Autowired
	SmsTemplatesRepository smstemplateRepo;

	@Value("${bulksms.key}")
	private String BULKSMS_KEY;

	@Value("${bulksms.url}")
	private String BULKSMS_URL;

	@Value("${nationalsms.url}")
	private String NATIONALSMS_URL;

	@Value("${nationalsms.key}")
	private String NATIONALSMS_KEY;

	@Value("${nationalsms.username}")
	private String NATIONALSMS_USERNAME;

	@Value("${sms.senderid}")
	private String SMS_SENDERID;

	@Value("${sms.validity.minutes}")
	private String SMS_VALIDITY_MINUTES;

	public void sendSMS_Suggestions(String username, String mobile, String purpose, String refno) {
		LOGGER.info("Entered sendSMS_Suggestions() -> Start");
		LOGGER.info("Parameter username -> " + username + " ,mobile -> " + mobile + " ,purpose -> " + purpose);

		String smsContent = null, templateid = null, vendor = null;

		SmsTemplates smstemplate = smstemplateRepo.findByPurpose(purpose);
		smsContent = smstemplate.getMsgContent();
		smsContent = smsContent.replace("{spref}", refno);
		smsContent = smsContent.replace("{sptime}", "24 hours");
		templateid = smstemplate.getTemplateId();
		vendor = smstemplate.getVendor();
		//LOGGER.info("smsContent=" + smsContent);
		
		if (vendor.equalsIgnoreCase("bulksmspublic")) {
			sendBulkSMSGateway_unirest(username, mobile, smsContent);
		}
		if (vendor.equalsIgnoreCase("nationalsms")) {
			sendNationalSMSGateway(username, mobile, smsContent, templateid);
		}
	}

	public void sendSMS_OTP(String username, String mobile, String purpose, String otp) {
		LOGGER.info("Entered sendSMS_OTP() -> Start");
		LOGGER.info("Parameter username -> " + username + " ,mobile -> " + mobile + " ,purpose -> " + purpose);

		String smsContent = null, templateid = null, vendor = null;
		if (purpose.equalsIgnoreCase("OTP_SMS")) {
			SmsTemplates smstemplate = smstemplateRepo.findByPurpose(purpose);
			smsContent = smstemplate.getMsgContent();
			smsContent = smsContent.replace("{otpnumber}", otp);
			smsContent = smsContent.replace("{validity}", SMS_VALIDITY_MINUTES);
			templateid = smstemplate.getTemplateId();
			vendor = smstemplate.getVendor();
		}

		if (vendor.equalsIgnoreCase("bulksmspublic")) {
			sendBulkSMSGateway_unirest(username, mobile, smsContent);
		}
		if (vendor.equalsIgnoreCase("nationalsms")) {
			sendNationalSMSGateway(username, mobile, smsContent, templateid);
		}
	}

	public void sendSMS_Cashback(String userId, String mobile, String purpose) {
		LOGGER.info("Entered sendSMS_Cashback() -> Start");
		LOGGER.info("Parameter userId -> " + userId + " ,mobile -> " + mobile + " ,purpose -> " + purpose);

		// Your cashback of Rs. {camount} is credited to your RechargeAXN Wallet.
		// Updated balance is Rs. {wamount}
		String smsContent = null, templateid = null, vendor = null;
		if (purpose.equalsIgnoreCase("Cashback_SMS")) {
			SmsTemplates smstemplate = smstemplateRepo.findByPurpose(purpose);
			smsContent = smstemplate.getMsgContent();
			//System.out.println("smsContent=" + smsContent);
			// TODO
			smsContent = smsContent.replace("{camount}", "1");
			smsContent = smsContent.replace("{wamount}", "2");
			templateid = smstemplate.getTemplateId();
			vendor = smstemplate.getVendor();
		}

		if (vendor.equalsIgnoreCase("bulksmspublic")) {
			sendBulkSMSGateway_unirest(userId, mobile, smsContent);
		}
		if (vendor.equalsIgnoreCase("nationalsms")) {
			sendNationalSMSGateway(userId, mobile, smsContent, templateid);
		}
	}

	public void sendSMS_Recharge(String userId, String mobile, String purpose) {
		LOGGER.info("Entered sendSMS_Recharge() -> Start");
		LOGGER.info("Parameter userId -> " + userId + " ,mobile -> " + mobile + " ,purpose -> " + purpose);

		// Recharge of {opname} number {usernum} for Rs. {ramount} is successful. Email
		// us care@rechargeaxn.com for any queries.
		String smsContent = null, templateid = null, vendor = null;
		if (purpose.equalsIgnoreCase("Recharge_SMS")) {
			SmsTemplates smstemplate = smstemplateRepo.findByPurpose(purpose);
			smsContent = smstemplate.getMsgContent();
			//System.out.println("smsContent=" + smsContent);
			// TODO
			// smsContent = smsContent.replace("{opname}", otp);
			// smsContent = smsContent.replace("{usernum}", SMS_VALIDITY_MINUTES);
			// smsContent = smsContent.replace("{ramount}", otp);
			templateid = smstemplate.getTemplateId();
			vendor = smstemplate.getVendor();
		}

		if (vendor.equalsIgnoreCase("bulksmspublic")) {
			sendBulkSMSGateway_unirest(userId, mobile, smsContent);
		}
		if (vendor.equalsIgnoreCase("nationalsms")) {
			sendNationalSMSGateway(userId, mobile, smsContent, templateid);
		}
	}

	public void sendSMS_AddWallet(String userId, String mobile, String purpose) {
		LOGGER.info("Entered sendSMS_AddWallet() -> Start");
		LOGGER.info("Parameter userId -> " + userId + " ,mobile -> " + mobile + " ,purpose -> " + purpose);

		// Rs. {damount} is added to your RechargeAXN Wallet. Current balance is Rs.
		// {wamount}. Email us care@rechargeaxn.com for any queries.
		String smsContent = null, templateid = null, vendor = null;
		if (purpose.equalsIgnoreCase("Wallet_Add_SMS")) {
			SmsTemplates smstemplate = smstemplateRepo.findByPurpose(purpose);
			smsContent = smstemplate.getMsgContent();
			//System.out.println("smsContent=" + smsContent);
			// TODO
			// smsContent = smsContent.replace("{damount}", otp);
			// smsContent = smsContent.replace("{wamount}", SMS_VALIDITY_MINUTES);
			templateid = smstemplate.getTemplateId();
			vendor = smstemplate.getVendor();
		}

		if (vendor.equalsIgnoreCase("bulksmspublic")) {
			sendBulkSMSGateway_unirest(userId, mobile, smsContent);
		}
		if (vendor.equalsIgnoreCase("nationalsms")) {
			sendNationalSMSGateway(userId, mobile, smsContent, templateid);
		}
	}

	public void sendSMS_Refund(String userId, String mobile, String purpose) {
		LOGGER.info("Entered sendSMS_Refund() -> Start");
		LOGGER.info("Parameter userId -> " + userId + " ,mobile -> " + mobile + " ,purpose -> " + purpose);

		// Dear User, Your transaction on RechargeAXN for order id {ordid} was
		// unsuccessful. Amount Rs {ramount} is now refunded in your wallet.
		String smsContent = null, templateid = null, vendor = null;
		if (purpose.equalsIgnoreCase("Refund_SMS")) {
			SmsTemplates smstemplate = smstemplateRepo.findByPurpose(purpose);
			smsContent = smstemplate.getMsgContent();
			//System.out.println("smsContent=" + smsContent);
			// TODO
			// smsContent = smsContent.replace("{ordid}", otp);
			// smsContent = smsContent.replace("{ramount}", SMS_VALIDITY_MINUTES);
			templateid = smstemplate.getTemplateId();
			vendor = smstemplate.getVendor();
		}

		if (vendor.equalsIgnoreCase("bulksmspublic")) {
			sendBulkSMSGateway_unirest(userId, mobile, smsContent);
		}
		if (vendor.equalsIgnoreCase("nationalsms")) {
			sendNationalSMSGateway(userId, mobile, smsContent, templateid);
		}
	}

	public void sendSMS_Promo(String userId, String mobile, String purpose) {
		LOGGER.info("Entered sendSMS_Promo() -> Start");
		LOGGER.info("Parameter userId -> " + userId + " ,mobile -> " + mobile + " ,purpose -> " + purpose);

		// {promo}

		// Get Rs. {camount} cashback on adding Rs. {damount} into your RechargeAXN
		// Wallet. Use code {pcode}. Valid till {timed}. Details on
		// https://www.rechargeaxn.com/home/offer
		String smsContent = null, templateid = null, vendor = null;
		if (purpose.equalsIgnoreCase("Promo_SMS")) {
			SmsTemplates smstemplate = smstemplateRepo.findByPurpose(purpose);
			smsContent = smstemplate.getMsgContent();
			//System.out.println("smsContent=" + smsContent);
			// TODO
			// smsContent = smsContent.replace("{promo}", otp);
			// smsContent = smsContent.replace("{camount}", otp);
			// smsContent = smsContent.replace("{damount}", SMS_VALIDITY_MINUTES);
			// smsContent = smsContent.replace("{pcode}", SMS_VALIDITY_MINUTES);
			// smsContent = smsContent.replace("{timed}", SMS_VALIDITY_MINUTES);
			templateid = smstemplate.getTemplateId();
			vendor = smstemplate.getVendor();
		}

		if (vendor.equalsIgnoreCase("bulksmspublic")) {
			sendBulkSMSGateway_unirest(userId, mobile, smsContent);
		}
		if (vendor.equalsIgnoreCase("nationalsms")) {
			sendNationalSMSGateway(userId, mobile, smsContent, templateid);
		}
	}

	public void sendSMS_ChangePassword(String username, String mobile, String purpose, String email) {
		LOGGER.info("Entered sendSMS_ChangePassword() -> Start");
		LOGGER.info("Parameter username -> " + username + " ,mobile -> " + mobile + " ,purpose -> " + purpose);
		LOGGER.info("email="+email);
		// Your RechargeAXN Account password changed successfully. Please check your
		// email {umail} for more information.
		String smsContent = null, templateid = null, vendor = null;
		if (purpose.equalsIgnoreCase("Password_Change_SMS")) {
			SmsTemplates smstemplate = smstemplateRepo.findByPurpose(purpose);
			smsContent = smstemplate.getMsgContent();
			//System.out.println("smsContent=" + smsContent);
			smsContent = smsContent.replace("{umail}", email);
			templateid = smstemplate.getTemplateId();
			vendor = smstemplate.getVendor();
		}

		if (vendor.equalsIgnoreCase("bulksmspublic")) {
			sendBulkSMSGateway_unirest(username, mobile, smsContent);
		}
		if (vendor.equalsIgnoreCase("nationalsms")) {
			sendNationalSMSGateway(username, mobile, smsContent, templateid);
		}
	}

	public void sendSMS_Registration(String email, String mobile, String purpose) {
		LOGGER.info("Entered sendSMS_Registration() -> Start");
		LOGGER.info("Parameter email -> " + email + " ,mobile -> " + mobile + " ,purpose -> " + purpose);

		// Your registration on RechargeAXN is successful. Please check your email
		// {umail} for more information.
		String smsContent = null, templateid = null, vendor = null;
		if (purpose.equalsIgnoreCase("Registration_SMS")) {
			SmsTemplates smstemplate = smstemplateRepo.findByPurpose(purpose);
			smsContent = smstemplate.getMsgContent();
			//LOGGER.info("smsContent=" + smsContent);
			smsContent = smsContent.replace("{umail}", email);
			templateid = smstemplate.getTemplateId();
			vendor = smstemplate.getVendor();
		}

		if (vendor.equalsIgnoreCase("bulksmspublic")) {
			sendBulkSMSGateway_unirest(email, mobile, smsContent);
		}
		if (vendor.equalsIgnoreCase("nationalsms")) {
			sendNationalSMSGateway(email, mobile, smsContent, templateid);
		}
	}

	public void sendSMS_ChangeMobile(String username, String mobile, String purpose, String email) {
		LOGGER.info("Entered sendSMS_ChangeMobile() -> Start");
		LOGGER.info("Parameter username -> " + username + " ,mobile -> " + mobile + " ,purpose -> " + purpose);
		LOGGER.info("email="+email);

		// Your RechargeAXN Account mobile change is successful. Please check your email
		// {umail} for more information.
		String smsContent = null, templateid = null, vendor = null;
		if (purpose.equalsIgnoreCase("Mobile_Change_SMS")) {
			SmsTemplates smstemplate = smstemplateRepo.findByPurpose(purpose);
			smsContent = smstemplate.getMsgContent();
			//System.out.println("smsContent=" + smsContent);
			smsContent = smsContent.replace("{umail}", email);
			templateid = smstemplate.getTemplateId();
			vendor = smstemplate.getVendor();
		}

		if (vendor.equalsIgnoreCase("bulksmspublic")) {
			sendBulkSMSGateway_unirest(username, mobile, smsContent);
		}
		if (vendor.equalsIgnoreCase("nationalsms")) {
			sendNationalSMSGateway(username, mobile, smsContent, templateid);
		}
	}

	public void sendSMS_BillPay(String userId, String mobile, String purpose) {
		LOGGER.info("Entered sendSMS_BillPay() -> Start");
		LOGGER.info("Parameter userId -> " + userId + " ,mobile -> " + mobile + " ,purpose -> " + purpose);

		// Bill payment of {opname} number {usernum} for Rs. {ramount} is received. Your
		// service provider will take two working days to consider bill paid in your
		// account. Email us care@rechargeaxn.com for any queries.
		String smsContent = null, templateid = null, vendor = null;
		if (purpose.equalsIgnoreCase("Billpay_SMS")) {
			SmsTemplates smstemplate = smstemplateRepo.findByPurpose(purpose);
			smsContent = smstemplate.getMsgContent();
			//System.out.println("smsContent=" + smsContent);
			// TODO
			// smsContent = smsContent.replace("{opname}", otp);
			// smsContent = smsContent.replace("{usernum}", SMS_VALIDITY_MINUTES);
			// smsContent = smsContent.replace("{ramount}", SMS_VALIDITY_MINUTES);
			templateid = smstemplate.getTemplateId();
			vendor = smstemplate.getVendor();
		}

		if (vendor.equalsIgnoreCase("bulksmspublic")) {
			sendBulkSMSGateway_unirest(userId, mobile, smsContent);
		}
		if (vendor.equalsIgnoreCase("nationalsms")) {
			sendNationalSMSGateway(userId, mobile, smsContent, templateid);
		}
	}

	@Async
	private void sendBulkSMSGateway_unirest(String username, String mobile, String smsContent) {
		LOGGER.info("Entered sendBulkSMSGateway_unirest() -> Start");
		LOGGER.info("Parameter username -> " + username + " ,mobile -> " + mobile + " ,smsContent -> " + smsContent);

		HttpResponse<String> response;
		try {
			smsContent = java.net.URLEncoder.encode(smsContent, "utf-8");
			response = Unirest
					.get(BULKSMS_URL + BULKSMS_KEY + "&message=" + smsContent + "&senderId=" + SMS_SENDERID
							+ "&routeId=1&mobileNos=" + mobile + "&smsContentType=english")
					.header("Cache-Control", "no-cache").asString();

			//LOGGER.info("response body = " + response.getBody());// ok
			LOGGER.info("response status=" + response.getStatus());// 200
			//LOGGER.info("response status text=" + response.getStatusText());// 200
			LOGGER.info("sending SMS completed for " + mobile);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Async
	private void sendNationalSMSGateway(String username, String mobile, String message, String templateid) {
		LOGGER.info("Entered sendNationalSMSGateway() -> Start");
		LOGGER.info("Parameter username -> " + username + " ,mobile -> " + mobile + " ,message -> " + message);
		LOGGER.info("templateid = " + templateid);

		try {
			String apirequest = "Text";
			String route = "TRANS";
			String requestUrl = NATIONALSMS_URL + "username=" + URLEncoder.encode(NATIONALSMS_USERNAME, "UTF-8")
					+ "&apikey=" + URLEncoder.encode(NATIONALSMS_KEY, "UTF-8") + "&apirequest="
					+ URLEncoder.encode(apirequest, "UTF-8") + "&route=" + URLEncoder.encode(route, "UTF-8")
					+ "&sender=" + URLEncoder.encode(SMS_SENDERID, "UTF-8") + "&mobile="
					+ URLEncoder.encode(mobile, "UTF-8") + "&TemplateID=" + URLEncoder.encode(templateid, "UTF-8")
					+ "&message=" + URLEncoder.encode(message, "UTF-8") + "&format=JSON";
			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			LOGGER.info("response msg = " + uc.getResponseMessage());// ok
			LOGGER.info("response code=" + uc.getResponseCode());// 200
			LOGGER.info("sending SMS completed for " + mobile);
			uc.disconnect();
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
	}

	/*
	 * @Async public void sendBulkSMSGateway_okhttp(String userId, String mobile,
	 * String purpose) { LOGGER.info("Entered sendSMSNewGateway() -> Start");
	 * LOGGER.info("Parameter userId -> " + userId + " ,mobile -> " + mobile);
	 * 
	 * String key = "b74c854fe8b2729f53daa0b3aab79f34"; String route = "1"; String
	 * senderid = "RCHAXN";
	 * 
	 * try { String smsContent =
	 * "Your registration on RechargeAXN is successful. Please check your\r\n" +
	 * "email santoku6@gmail.com for more information."; smsContent =
	 * java.net.URLEncoder.encode(smsContent, "utf-8"); OkHttpClient client = new
	 * OkHttpClient();
	 * 
	 * Request request = new Request.Builder() .url(
	 * "http://login.bulksmslaunch.com/rest/services/sendSMS/sendGroupSms?AUTH_KEY="
	 * + key + "&message=" + smsContent +
	 * "&senderId=RCHAXN&routeId=1&mobileNos=8073280884&smsContentType=english")
	 * .get().addHeader("Cache-Control", "no-cache").build();
	 * 
	 * Response response = client.newCall(request).execute();
	 * 
	 * LOGGER.info("smsResponse got =" + response.toString());
	 * LOGGER.info("response.message = " + response.message());
	 * LOGGER.info("response.code = " + response.code());
	 * LOGGER.info("response.message = " + response.body());
	 * 
	 * LOGGER.info("sending SMS completed for " + mobile); } catch (Exception e) {
	 * LOGGER.error(e.getMessage(), e); } }
	 */

	/*
	 * @Async public void sendBulkSMSGateway_httpURL(String userId, String mobile,
	 * String purpose) throws IOException {
	 * LOGGER.info("Entered sendSMSNewGateway() -> Start");
	 * LOGGER.info("Parameter userId -> " + userId + " ,mobile -> " + mobile);
	 * 
	 * String key = "b74c854fe8b2729f53daa0b3aab79f34"; String route = "1"; String
	 * senderid = "RCHAXN";
	 * 
	 * String smsContent =
	 * "Your registration on RechargeAXN is successful. Please check your\r\n" +
	 * "email santoku6@gmail.com for more information.";
	 * 
	 * String requestUrlBulkSMS =
	 * "http://login.bulksmslaunch.com/rest/services/sendSMS/sendGroupSms?" +
	 * "AUTH_KEY=" + URLEncoder.encode(key, "UTF-8") + "&message=" +
	 * URLEncoder.encode(smsContent, "UTF-8") + "&senderId=" +
	 * URLEncoder.encode(senderid, "UTF-8") + "&routeId=" + URLEncoder.encode(route,
	 * "UTF-8") + "&mobileNos=" + URLEncoder.encode(mobile, "UTF-8") +
	 * "&smsContentType=english"; System.out.println("requestUrlBulkSMS=" +
	 * requestUrlBulkSMS);
	 * 
	 * URL url = new URL(requestUrlBulkSMS); HttpURLConnection uc =
	 * (HttpURLConnection) url.openConnection(); LOGGER.info("response msg = " +
	 * uc.getResponseMessage());// ok LOGGER.info("response code=" +
	 * uc.getResponseCode());// 200 uc.disconnect(); }
	 */
}
