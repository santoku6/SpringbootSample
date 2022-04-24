package com.raxn.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raxn.entity.MobileEmailCheck;
import com.raxn.entity.Suggestion;
import com.raxn.entity.User;
import com.raxn.model.LoginRequest;
import com.raxn.model.OTPModel;
import com.raxn.model.ResetPWDRequest;
import com.raxn.model.UserRequest;
import com.raxn.model.VerifyOTPRequest;
import com.raxn.repository.MobileEmailCheckRepository;
import com.raxn.repository.SuggestionRepository;
import com.raxn.repository.UserRepository;
import com.raxn.service.HomeService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;
import com.raxn.util.service.EmailMobileValidator;
import com.raxn.util.service.SMSSenderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeServiceImpl implements HomeService {

	Logger LOGGER = LoggerFactory.getLogger(HomeServiceImpl.class);

	private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Autowired
	UserRepository userRepository;

	@Autowired
	SuggestionRepository suggestRepo;

	@Autowired
	MobileEmailCheckRepository mobileEmailRepo;

	@Autowired
	private AuthenticationManager authenticationManager;

	/*
	 * @Autowired private JWTUtil jwtUtil;
	 */

	@Autowired
	SMSSenderService smsservice;

	@Value("${send.sms.service}")
	private String SEND_SMS_SERVICE;

	@Value("${send.email.service}")
	private String SEND_EMAIL_SERVICE;

	@Value("${sms.validity.minutes}")
	private String SMS_VALIDITY_MINUTES;

	private static EmailMobileValidator emailMobileValidator = null;

	static {
		emailMobileValidator = EmailMobileValidator.getInstance();
	}

	@Override
	public ResponseEntity<String> registerUser(UserRequest userRequest) {
		LOGGER.info("Entered registerUser() -> Start");
		LOGGER.info("userRequest=" + ReflectionToStringBuilder.toString(userRequest));
		JSONObject response = new JSONObject();
		User userEntity = new User();
		String myipadress = null;

		if (null == userRequest.getName() || userRequest.getName().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Name is empty or null");
			LOGGER.error("Name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == userRequest.getEmail() || userRequest.getEmail().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Email is empty or null");
			LOGGER.error("Email is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != userRequest.getEmail().trim() && !userRequest.getEmail().isEmpty()) {
			if (!emailMobileValidator.emailValidator(userRequest.getEmail().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Email is not valid");
				LOGGER.error("Email is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}
		if (null == userRequest.getMobile() || userRequest.getMobile().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Mobile is empty or null");
			LOGGER.error("Mobile is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != userRequest.getMobile() && !userRequest.getMobile().isEmpty()) {
			if (!emailMobileValidator.mobileValidator(userRequest.getMobile().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mobile is not valid");
				LOGGER.error("Mobile is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}
		if (null == userRequest.getPassword() || userRequest.getPassword().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Password is empty or null");
			LOGGER.error("Messagetype is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (!(userRequest.getTnc().equalsIgnoreCase("true")) || null == userRequest.getTnc()
				|| userRequest.getTnc().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "TnC is either empty or incorrect");
			LOGGER.error("TnC is empty or incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == userRequest.getMode() || userRequest.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Mode is empty or null");
			LOGGER.error("Mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != userRequest.getMode() && !userRequest.getMode().isEmpty()) {
			if (!CommonServiceUtil.checkMode(userRequest.getMode().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mode is incorrect");
				LOGGER.error("Mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		if (null == userRequest.getServicename() || userRequest.getServicename().isEmpty()
				|| !userRequest.getServicename().trim().equalsIgnoreCase(AppConstant.SERVICE_SIGNUP_USER)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Service name is empty or incorrect");
			LOGGER.error("Service name is empty or incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null == userRequest.getUserip() || userRequest.getUserip().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "User IP is empty or null");
			LOGGER.error("User IP is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		ResponseEntity<String> responseStr = checkVerification(userRequest.getMobile(), userRequest.getEmail());
		if (responseStr.getStatusCodeValue() != 200) {
			return new ResponseEntity<String>(responseStr.getBody(), responseStr.getStatusCode());
		}

		if (responseStr.getStatusCodeValue() == 200) {
			try {
				myipadress = CommonServiceUtil.getIp();
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}

			LOGGER.info("ip address=" + myipadress);

			User userInfo = userRepository.findByMobile(userRequest.getMobile().trim());
			if (null != userInfo) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mobile is already registered");
				LOGGER.error("Mobile is already registered");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			userInfo = null;
			User userInfo1 = userRepository.findByEmail(userRequest.getEmail().trim());
			if (null != userInfo1) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Email is already registered");
				LOGGER.error("Email is already registered");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			userInfo1 = null;

			userEntity.setIpaddress(myipadress);
			userEntity.setName(userRequest.getName().trim());
			userEntity.setEmail(userRequest.getEmail().trim());
			userEntity.setMobile(userRequest.getMobile().trim());
			userEntity.setPassword(CommonServiceUtil.encodePassword(userRequest.getPassword().trim()));
			userEntity.setTnc(userRequest.getTnc().trim());
			userEntity.setRegMode(userRequest.getMode().trim());
			userEntity.setActivatedStatus(true);
			userEntity.setLockStatus(false);

			User userData = userRepository.save(userEntity);

			// TODO
			// SEND SMS AND EMAIL
			if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
				// Registration_SMS
				smsservice.sendSMS_Registration(userData.getEmail(), userData.getMobile(), "Registration_SMS");

			}
			if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
				// Registration_Email
			}

			clearEmailMobileData(userData.getMobile(), userData.getEmail());

			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "User registration completed");
			LOGGER.info("User registration completed for email " + userData.getEmail());
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
		} else {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Internal Server error, User registration incomplete");
			LOGGER.error("Internal Server error, User registration incomplete for " + userRequest.getEmail().trim());
			return new ResponseEntity<String>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ResponseEntity<String> checkVerification(String mobile, String email) {
		LOGGER.info("Entered checkVerification() -> Start");
		LOGGER.info("mobile=" + mobile + " ,email=" + email);
		boolean statusMobile = false, statusEmail = false;
		JSONObject response = new JSONObject();

		// checking email verification
		MobileEmailCheck emailData = mobileEmailRepo.findByEmail(email);
		if (null != emailData) {
			String emailStatus = emailData.getOtpStatus();
			if (null != emailStatus) {
				if (emailStatus.equalsIgnoreCase("verified")) {
					statusEmail = true;
				}
			}
		}
		emailData = null;
		if (!statusEmail) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Email is not verified");
			LOGGER.error("Email is not verified");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		// checking mobile verification
		MobileEmailCheck mobileData = mobileEmailRepo.findByMobile(mobile);

		if (null != mobileData) {
			String mobileStatus = mobileData.getOtpStatus();
			if (null != mobileStatus) {
				if (mobileStatus.equalsIgnoreCase("verified")) {
					statusMobile = true;
				}
			}
		}
		mobileData = null;
		if (!statusMobile) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Mobile is not verified");
			LOGGER.error("Mobile is not verified");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (statusMobile && statusEmail) {
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "Email & Mobile is verified");
			LOGGER.info("Email & Mobile is verified");
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
		} else {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Mobile/Email not verified");
			LOGGER.error("Mobile/Email not verified");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	private void clearEmailMobileData(String mobile, String email) {
		LOGGER.info("Entered clearEmailMobileData() -> Start");
		LOGGER.info("mobile=" + mobile + " ,email=" + email);

		int num = mobileEmailRepo.deleteByIdentifier(mobile);
		LOGGER.info("number of mobile records deleted = " + num);

		int num2 = mobileEmailRepo.deleteByIdentifier(email);
		LOGGER.info("number of email records deleted = " + num2);
	}

	@Override
	public ResponseEntity<String> generateOTP(OTPModel otpRequest) {
		LOGGER.info("Entered generateOTP() -> Start");
		LOGGER.info("otpRequest=" + ReflectionToStringBuilder.toString(otpRequest));
		JSONObject response = new JSONObject();
		// User userEntity = new User();

		if (null == otpRequest.getServicename() || otpRequest.getServicename().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Service name is empty or null");
			LOGGER.error("Service name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == otpRequest.getIdentifier() || otpRequest.getIdentifier().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Identifier is empty or null");
			LOGGER.error("Identifier is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == otpRequest.getMode() || otpRequest.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Mode is empty or null");
			LOGGER.error("Mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		String identifier = otpRequest.getIdentifier().trim();
		String mode = otpRequest.getMode().trim();
		String servicename = otpRequest.getServicename().trim();
		String otp = CommonServiceUtil.genOTP();

		if (identifier.contains("@")) {
			if (!emailMobileValidator.emailValidator(identifier)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Email is not valid");
				LOGGER.error("Email is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		} else {
			if (!emailMobileValidator.mobileValidator(identifier)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mobile is not valid");
				LOGGER.error("Mobile is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}
		if (null != otpRequest.getMode() && !otpRequest.getMode().isEmpty()) {
			if (!CommonServiceUtil.checkMode(otpRequest.getMode().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mode is incorrect");
				LOGGER.error("Mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		// SignUp User - Request OTP
		if (servicename.equalsIgnoreCase(AppConstant.SERVICE_SIGNUP_USER)) {
			// check if identifier is registered or not
			if (identifier.contains("@")) {
				User userEmailEntity = userRepository.findByEmail(identifier);
				if (null != userEmailEntity) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "Email is already registered");
					LOGGER.error("Email is already registered");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
			} else {
				User userMobileEntity = userRepository.findByMobile(identifier);
				if (null != userMobileEntity) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "Mobile is already registered");
					LOGGER.error("Mobile is already registered");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
			}
			// check if identifier record is existing in emailmobile table, then update
			// otp_datetime. If verified then different otp, else same otp
			MobileEmailCheck identifierData = mobileEmailRepo.findByIdentifier(identifier);
			if (null != identifierData) {
				if (identifierData.getOtpStatus().equalsIgnoreCase("verified")
						|| !identifierData.getOtpServicename().equalsIgnoreCase(AppConstant.SERVICE_SIGNUP_USER)) {
					identifierData.setOtpStatus("");
					identifierData.setOtp(otp);
				} else {
					identifierData.setOtpStatus("");
					if (null == identifierData.getOtp() || identifierData.getOtp().isEmpty()) {
						identifierData.setOtp(otp);
					} else {
						otp = identifierData.getOtp();
					}
				}
				identifierData.setOtpDatetime(Calendar.getInstance().getTime());
				identifierData.setOtpServicename(servicename);
				identifierData.setOtpMode(mode);
				mobileEmailRepo.save(identifierData);
			} else {
				MobileEmailCheck dataInfo = new MobileEmailCheck();
				dataInfo.setOtp(otp);
				dataInfo.setOtpStatus("");
				dataInfo.setOtpDatetime(Calendar.getInstance().getTime());
				dataInfo.setIdentifier(identifier);
				dataInfo.setOtpMode(mode);
				dataInfo.setOtpServicename(servicename);

				mobileEmailRepo.save(dataInfo);
			}
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "OTP generated");
			LOGGER.info("OTP to set:" + otp);

			if (identifier.contains("@")) {
				// send OTP in email

			} else {
				// send OTP in SMS
				if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
					smsservice.sendSMS_OTP(null, identifier, "OTP_SMS", otp);
				}
			}
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

			// Forget Password - Request OTP
		} else if (servicename.equalsIgnoreCase(AppConstant.SERVICE_FORGET_PASSWORD)) {
			// check if identifier is registered or not
			User userDBEntity = userRepository.findByEmail(identifier);
			if (null == userDBEntity) {
				userDBEntity = userRepository.findByMobile(identifier);
			}
			if (null == userDBEntity) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "User mobile/email is not registered");
				LOGGER.error("User mobile/email is not registered");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (userDBEntity != null) {
				// to know service name if in DB while otp raised last time
				// if service name different then get new otp
				// if status verified then get new otp
				String otpServicenameDB = userDBEntity.getOtpServicename();
				// to know if otp exists for resending same in re-send otp
				String otpDB = userDBEntity.getOtp();
				// to know otp status, if verified or not verified
				String otpStatusDB = userDBEntity.getOtpStatus();

				if (null == otpDB || otpDB.isEmpty()) {
					userDBEntity.setOtp(otp);
					userDBEntity.setOtpStatus("");
					userDBEntity.setOtpDatetime(Calendar.getInstance().getTime());
					userDBEntity.setOtpMode(mode);
					userDBEntity.setOtpServicename(servicename);

					userRepository.save(userDBEntity);
				}
				if (null != otpDB && !otpDB.isEmpty()) {
					if (otpStatusDB.equalsIgnoreCase("verified")
							|| !otpServicenameDB.equalsIgnoreCase(AppConstant.SERVICE_FORGET_PASSWORD)) {
						userDBEntity.setOtp(otp);
						userDBEntity.setOtpStatus("");
						userDBEntity.setOtpDatetime(Calendar.getInstance().getTime());
						userDBEntity.setOtpMode(mode);
						userDBEntity.setOtpServicename(servicename);

						userRepository.save(userDBEntity);
					} else {
						userDBEntity.setOtpStatus("");
						userDBEntity.setOtpDatetime(Calendar.getInstance().getTime());
						userDBEntity.setOtpMode(mode);
						userDBEntity.setOtpServicename(servicename);
						otp = otpDB;

						userRepository.save(userDBEntity);
					}
				}
				response.put(AppConstant.STATUS, successStatus);
				response.put(AppConstant.MESSAGE, "OTP generated");
				LOGGER.info("OTP to set:" + otp);

				if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
					smsservice.sendSMS_OTP(null, identifier, "OTP_SMS", otp);
				}
				if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
					// send email
				}
			}
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
		}
		response.put(AppConstant.STATUS, errorStatus);
		response.put(AppConstant.MESSAGE, "Service name is incorrect");
		LOGGER.info("Service name is incorrect");
		return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<String> verifyOTP(VerifyOTPRequest verifyOTPRequest) {
		LOGGER.info("Entered verifyOTP() -> Start");
		LOGGER.info("verifyOTPRequest=" + ReflectionToStringBuilder.toString(verifyOTPRequest));
		JSONObject response = new JSONObject();

		if (null == verifyOTPRequest.getServicename() || verifyOTPRequest.getServicename().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Service name is empty or null");
			LOGGER.error("Service name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == verifyOTPRequest.getIdentifier() || verifyOTPRequest.getIdentifier().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Identifier is empty or null");
			LOGGER.error("Identifier is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == verifyOTPRequest.getMode() || verifyOTPRequest.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Mode is empty or null");
			LOGGER.error("Mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == verifyOTPRequest.getOtp() || verifyOTPRequest.getOtp().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "OTP is empty or null");
			LOGGER.error("OTP is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (verifyOTPRequest.getOtp().length() != 6) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "OTP length is incorrect");
			LOGGER.error("OTP length is incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		String identifier = verifyOTPRequest.getIdentifier().trim();
		String mode = verifyOTPRequest.getMode().trim();
		String servicename = verifyOTPRequest.getServicename().trim();
		String otp = verifyOTPRequest.getOtp().trim();

		if (identifier.contains("@")) {
			if (!emailMobileValidator.emailValidator(identifier)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Email is not valid");
				LOGGER.error("Email is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		} else {
			if (!emailMobileValidator.mobileValidator(identifier)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mobile is not valid");
				LOGGER.error("Mobile is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}
		if (null != mode && !mode.isEmpty()) {
			if (!CommonServiceUtil.checkMode(mode)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mode is incorrect");
				LOGGER.error("Mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		// signup user otp matching
		if (servicename.equalsIgnoreCase(AppConstant.SERVICE_SIGNUP_USER)) {
			// check if identifier is registered or not
			if (identifier.contains("@")) {
				User userEmailEntity = userRepository.findByEmail(identifier);
				if (null != userEmailEntity) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "Email is already registered");
					LOGGER.error("Email is already registered");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
			} else {
				User userMobileEntity = userRepository.findByMobile(identifier);
				if (null != userMobileEntity) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "Mobile is already registered");
					LOGGER.error("Mobile is already registered");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
			}
			// check if identifier record is existing in emailmobile table
			MobileEmailCheck userData = mobileEmailRepo.findByMobile(identifier);
			if (null == userData) {
				userData = mobileEmailRepo.findByEmail(identifier);
			}
			if (null == userData) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Identifier verification failed");
				LOGGER.error("Identifier verification failed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			} else {
				String serviceNameDB = userData.getOtpServicename();
				String otpStatusDB = userData.getOtpServicename();
				String otpDB = userData.getOtp();
				long otpTS = userData.getOtpDatetime().getTime();
				long currentTS = System.currentTimeMillis();
				long differenceTS = currentTS - otpTS;
				LOGGER.info("differenceTS=" + differenceTS);

				long allowedMS = Long.parseLong(SMS_VALIDITY_MINUTES) * 60 * 1000;

				if (otpStatusDB.equalsIgnoreCase("verified") || differenceTS > allowedMS
						|| !servicename.equalsIgnoreCase(serviceNameDB)) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "OTP expired or used or changed");
					LOGGER.error("OTP expired or used or changed");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (differenceTS <= allowedMS && otpDB.equalsIgnoreCase(otp)) {
					// otp matching and success
					response.put(AppConstant.STATUS, successStatus);
					response.put(AppConstant.MESSAGE, "OTP matched & verified");
					LOGGER.info("OTP matched & verified for identifier " + identifier);
					// changing status to verified in DB

					userData.setOtpStatus("verified");
					mobileEmailRepo.save(userData);
					return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
				} else {
					// otp is not matching and error
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "OTP is wrong");
					LOGGER.error("OTP is wrong");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
			}
		}
		// reset pwd otp matching
		if (servicename.equalsIgnoreCase(AppConstant.SERVICE_FORGET_PASSWORD)) {
			// check if identifier is registered or not
			User userDBEntity = userRepository.findByEmail(identifier);
			if (null == userDBEntity) {
				userDBEntity = userRepository.findByMobile(identifier);
			}
			if (null == userDBEntity) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "User mobile/email is not registered");
				LOGGER.error("User mobile/email is not registered");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (userDBEntity != null) {
				String serviceNameDB = userDBEntity.getOtpServicename();
				String otpStatusDB = userDBEntity.getOtpStatus();
				String otpDB = userDBEntity.getOtp();
				long otpTS = userDBEntity.getOtpDatetime().getTime();
				long currentTS = System.currentTimeMillis();
				long differenceTS = currentTS - otpTS;
				LOGGER.info("differenceTS=" + differenceTS);

				long allowedMS = Long.parseLong(SMS_VALIDITY_MINUTES) * 60 * 1000;
				if (otpStatusDB.equalsIgnoreCase("verified") || differenceTS > allowedMS
						|| !servicename.equalsIgnoreCase(serviceNameDB)) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "OTP expired or used or changed");
					LOGGER.error("OTP expired or used or changed");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (differenceTS <= allowedMS && otpDB.equalsIgnoreCase(otp)) {
					// otp matching and success
					response.put(AppConstant.STATUS, successStatus);
					response.put(AppConstant.MESSAGE, "OTP matched & verified");
					LOGGER.info("OTP matched & verified for identifier " + identifier);
					// changing status to verified in DB

					userDBEntity.setOtpStatus("verified");
					userRepository.save(userDBEntity);
					return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
				} else {
					// otp is not matching and error
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "OTP is wrong");
					LOGGER.error("OTP is wrong");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
			}
		}
		if (servicename.equalsIgnoreCase(AppConstant.SERVICE_LOGIN_USER)) {
			// check if identifier is registered or not
			User userDBEntity = userRepository.findByEmail(identifier);
			if (null == userDBEntity) {
				userDBEntity = userRepository.findByMobile(identifier);
			}
			if (null == userDBEntity) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "User mobile/email is not registered");
				LOGGER.error("User mobile/email is not registered");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (userDBEntity != null) {
				String serviceNameDB = userDBEntity.getOtpServicename();
				String otpStatusDB = userDBEntity.getOtpStatus();
				String otpDB = userDBEntity.getOtp();
				long otpTS = userDBEntity.getOtpDatetime().getTime();
				long currentTS = System.currentTimeMillis();
				long differenceTS = currentTS - otpTS;
				LOGGER.info("differenceTS (in ms)=" + differenceTS);

				long allowedMS = Long.parseLong(SMS_VALIDITY_MINUTES) * 60 * 1000;
				if (otpStatusDB.equalsIgnoreCase("verified") || differenceTS > allowedMS
						|| !servicename.equalsIgnoreCase(serviceNameDB)) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "OTP expired or used or changed");
					LOGGER.error("OTP expired or used or changed");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (differenceTS <= allowedMS && otpDB.equalsIgnoreCase(otp)) {
					// otp matching and success
					response.put(AppConstant.STATUS, successStatus);
					response.put(AppConstant.MESSAGE, "OTP matched & verified");
					LOGGER.info("OTP matched & verified for identifier " + identifier);
					// changing status to verified in DB

					userDBEntity.setOtpStatus("verified");
					userRepository.save(userDBEntity);
					return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
				} else {
					// otp is not matching and error
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "OTP is wrong");
					LOGGER.error("OTP is wrong");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
			}
		}

		response.put(AppConstant.STATUS, errorStatus);
		response.put(AppConstant.MESSAGE, "Service name is incorrect");
		LOGGER.info("Service name is incorrect");
		return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<String> resetPassword(ResetPWDRequest resetPWDRequest) {
		LOGGER.info("Entered resetPassword() -> Start");
		LOGGER.info("resetPWDRequest=" + ReflectionToStringBuilder.toString(resetPWDRequest));
		JSONObject response = new JSONObject();

		if (null == resetPWDRequest.getIdentifier() || resetPWDRequest.getIdentifier().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Identifier is empty or null");
			LOGGER.error("Identifier is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == resetPWDRequest.getPassword() || resetPWDRequest.getPassword().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Password is empty or null");
			LOGGER.error("Password is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == resetPWDRequest.getMode() || resetPWDRequest.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Mode is empty or null");
			LOGGER.error("Mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		String mode = resetPWDRequest.getMode().trim();
		String identifier = resetPWDRequest.getIdentifier().trim();
		String password = CommonServiceUtil.encodePassword(resetPWDRequest.getPassword().trim());

		if (null != mode && !mode.isEmpty()) {
			if (!CommonServiceUtil.checkMode(mode)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mode is incorrect");
				LOGGER.error("Mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}
		if (identifier.contains("@")) {
			if (!emailMobileValidator.emailValidator(identifier)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Email is not valid");
				LOGGER.error("Email is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		} else {
			if (!emailMobileValidator.mobileValidator(identifier)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mobile is not valid");
				LOGGER.error("Mobile is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		User userDBEntity = userRepository.findByEmail(identifier);
		if (null == userDBEntity) {
			userDBEntity = userRepository.findByMobile(identifier);
		}
		if (null == userDBEntity) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "User mobile/email is not registered");
			LOGGER.error("User mobile/email is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (userDBEntity != null) {
			userDBEntity.setPassword(password);

			userRepository.save(userDBEntity);
		}
		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "Reset password completed");
		LOGGER.info("Reset password completed for user " + identifier);
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	// sending OTP during login
	private ResponseEntity<String> send2FAOTP(String identifier, String servicename, String mode) {
		LOGGER.info("Entered send2FAOTP() -> Start");
		LOGGER.info("identifier = " + identifier);
		JSONObject response = new JSONObject();
		String otp = CommonServiceUtil.genOTP();

		User userDBEntity = userRepository.findByEmail(identifier);
		if (null == userDBEntity) {
			userDBEntity = userRepository.findByMobile(identifier);
		}
		if (null == userDBEntity) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "User mobile/email is not registered");
			LOGGER.error("User mobile/email is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (userDBEntity != null) {
			String otpServicenameDB = userDBEntity.getOtpServicename();
			String otpDB = userDBEntity.getOtp();
			String otpStatusDB = userDBEntity.getOtpStatus();

			if (null == otpDB || otpDB.isEmpty()) {
				userDBEntity.setOtp(otp);
				userDBEntity.setOtpStatus("");
				userDBEntity.setOtpDatetime(Calendar.getInstance().getTime());
				userDBEntity.setOtpMode(mode);
				userDBEntity.setOtpServicename(servicename);

				userRepository.save(userDBEntity);
			}
			if (null != otpDB && !otpDB.isEmpty()) {
				if (otpStatusDB.equalsIgnoreCase("verified")
						|| !otpServicenameDB.equalsIgnoreCase(AppConstant.SERVICE_LOGIN_USER)) {
					userDBEntity.setOtp(otp);
					userDBEntity.setOtpStatus("");
					userDBEntity.setOtpDatetime(Calendar.getInstance().getTime());
					userDBEntity.setOtpMode(mode);
					userDBEntity.setOtpServicename(servicename);

					userRepository.save(userDBEntity);
				} else {
					userDBEntity.setOtpStatus("");
					userDBEntity.setOtpDatetime(Calendar.getInstance().getTime());
					userDBEntity.setOtpMode(mode);
					userDBEntity.setOtpServicename(servicename);
					otp = otpDB;

					userRepository.save(userDBEntity);
				}
			}
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "OTP generated");
			LOGGER.info("OTP to set:" + otp);

			if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
				smsservice.sendSMS_OTP(null, identifier, "OTP_SMS", otp);
			}
			if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
				// send email with same otp
			}
		}
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

	}

	@Override
	public ResponseEntity<String> loginUser(LoginRequest loginRequest) {
		LOGGER.info("Entered loginUser() -> Start");
		LOGGER.info("identifier = " + loginRequest.getIdentifier());
		JSONObject response = new JSONObject();

		if (null == loginRequest.getIdentifier() || loginRequest.getIdentifier().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Identifier is empty or null");
			LOGGER.error("Identifier is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == loginRequest.getPassword() || loginRequest.getPassword().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Password is empty or null");
			LOGGER.error("Password is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == loginRequest.getServicename() || loginRequest.getServicename().isEmpty()
				|| !loginRequest.getServicename().trim().equalsIgnoreCase(AppConstant.SERVICE_LOGIN_USER)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Service name is empty or incorrect");
			LOGGER.error("Service name is empty or incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == loginRequest.getMode() || loginRequest.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Mode is empty or null");
			LOGGER.error("Mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		String mode = loginRequest.getMode().trim();
		if (null != mode && !mode.isEmpty()) {
			if (!CommonServiceUtil.checkMode(mode)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mode is incorrect");
				LOGGER.error("Mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		String identifier = loginRequest.getIdentifier().trim();
		String password = loginRequest.getPassword().trim();
		String serviceName = loginRequest.getServicename().trim();

		if (identifier.contains("@")) {
			if (!emailMobileValidator.emailValidator(identifier)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Email is not valid");
				LOGGER.error("Email is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		} else {
			if (!emailMobileValidator.mobileValidator(identifier)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mobile is not valid");
				LOGGER.error("Mobile is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}
		// authenticate userid, pwd using spring security
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));

		// send OTP for 2FA
		send2FAOTP(identifier, serviceName, mode);

		return ResponseEntity.ok("user credential matched & OTP sent");
	}

	@Override
	public ResponseEntity<String> nextLoginUser(VerifyOTPRequest verifyOTPRequest) {
		LOGGER.info("Entered nextLoginUser() -> Start");
		ObjectMapper objMapper = new ObjectMapper();

		try {
			LOGGER.info("Parameter verifyOTPRequest = " + objMapper.writeValueAsString(verifyOTPRequest));
		} catch (JsonProcessingException e) {
			LOGGER.error("Error in nextLoginUser:" + e, e.getMessage());
		}
		JSONObject response = new JSONObject();
		ResponseEntity<String> responseVerification = verifyOTP(verifyOTPRequest);
		LOGGER.info("OTP Verification status code = " + responseVerification.getStatusCodeValue());
		if (responseVerification.getStatusCodeValue() != 200) {
			return new ResponseEntity<String>(responseVerification.getBody(), responseVerification.getStatusCode());
		}
		// LOGGER.info("otp verification completed");
		// String token = jwtUtil.createToken(verifyOTPRequest.getIdentifier());

		// response.put(AppConstant.TOKEN, token);
		// response.put(AppConstant.MESSAGE, "Token Generated By RechargeAXN Server");
		// LOGGER.info("JWT Token generated for
		// identifier:"+verifyOTPRequest.getIdentifier());
		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "OTP verified");
		LOGGER.info("OTP verified");
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> postSuggestions(MultipartFile mpfile, String name, String email, String mobile,
			String messagetype, String query, String mode) {
		LOGGER.info("Entered postSuggestions() -> Start");
		LOGGER.info("Name -> " + name + " ,Email = " + email + " ,mobile=" + mobile + " ,messagetype=" + messagetype
				+ " ,query=" + query + " ,mode=" + mode);
		JSONObject response = new JSONObject();
		String[] extnarray = { "pdf", "png", "jpg", "jpeg" };

		String referenceNumber = CommonServiceUtil.genReferenceNo();
		Suggestion suggestionEntity = new Suggestion();

		if (null == name || name.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Name is empty or null");
			LOGGER.error("Name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == email || email.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Email is empty or null");
			LOGGER.error("Email is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == mobile || mobile.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Mobile is empty or null");
			LOGGER.error("Mobile is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == messagetype || messagetype.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Messagetype is not selected");
			LOGGER.error("Messagetype is not selected");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == query || query.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Query is empty or null");
			LOGGER.error("Query is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == mode || mode.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Mode is empty or null");
			LOGGER.error("Mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		suggestionEntity.setName(name);
		suggestionEntity.setEmail(email);
		suggestionEntity.setMobile(mobile);
		suggestionEntity.setMessageType(messagetype);
		suggestionEntity.setMessage(query);
		suggestionEntity.setMode(mode);
		suggestionEntity.setRefno(referenceNumber);
		suggestionEntity.setStatus("open");

		if (null != mpfile && !mpfile.isEmpty()) {
			String filename = mpfile.getOriginalFilename();
			LOGGER.info("filename=" + filename);
			int extnPointer = filename.lastIndexOf(".");
			if (extnPointer == -1) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "File without extn is not allowed");
				LOGGER.error("File without extn is not allowed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			String extn = filename.substring(extnPointer + 1);
			boolean checkExtn = Arrays.asList(extnarray).contains(extn);
			if (!checkExtn) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, extn + " is not allowed");
				LOGGER.error(extn + " is not allowed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			long bytes = mpfile.getSize();
			double kilobytes = (bytes / 1024);
			double megabytes = (kilobytes / 1024);
			if (megabytes > 2.0) {
				LOGGER.error("File size is more than 2MB");
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "File size is more than 2MB");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			java.nio.file.Path pathTo = Paths.get("c:/santosh");
			String destFileName = mobile + "_" + filename;
			// java.nio.file.Path pathTo = Paths.get("/home/ec2-user/santosh/");
			java.nio.file.Path destination = Paths.get(pathTo.toString() + "\\" + destFileName);
			LOGGER.info("destination = " + destination);
			InputStream in;
			try {
				in = mpfile.getInputStream();
				Files.deleteIfExists(destination);
				Files.copy(in, destination);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			suggestionEntity.setAttachment(destFileName);
		}

		Suggestion savedInfo = suggestRepo.save(suggestionEntity);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.REFNO, savedInfo.getRefno());

		// sending sms and email

		if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
			// Registration_SMS
			// smsservice.sendSMS_Registration(userData.getEmail(), userData.getMobile(),
			// "Registration_SMS");

		}
		if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {

		}

		// smsservice.sendSMS_OTP("010", "8073280884", "OTP_SMS", "112233");

		// sendmail(userid, email, purpose);

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> getUserWbalance(String userid) {
		LOGGER.info("Entered getUserWbalance() -> Start");
		LOGGER.info("Parameter userid ->" + userid);
		JSONObject response = new JSONObject();
		int userInt = 0;

		if (null == userid || userid.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is empty or null");
			LOGGER.error("userid is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		userid = userid.trim();
		try {
			userInt = Integer.parseInt(userid);
			LOGGER.info("userid=" + userInt);
		} catch (NumberFormatException e) {
			LOGGER.error(e.getMessage(), e);
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is not correct");
			LOGGER.error("userid is not correct");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (userInt <= 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is invalid");
			LOGGER.error("userid is invalid");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		User userInfo = userRepository.findById(userInt);
		if (null == userInfo) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid not registered");
			LOGGER.error("userid not registered::" + userInt);
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.REWARDPOINT, userInfo.getWalletBalance());
		LOGGER.info("wallet balance of userid " + userInt + " is " + userInfo.getWalletBalance());
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	/*
	 * @Override public ResponseEntity<String> loginUser(LoginRequest loginRequest)
	 * { LOGGER.info("Entered loginUser() -> Start"); LOGGER.info("loginRequest=" +
	 * ReflectionToStringBuilder.toString(loginRequest)); JSONObject response = new
	 * JSONObject();
	 * 
	 * if (null == loginRequest.getIdentifier() ||
	 * loginRequest.getIdentifier().isEmpty()) { response.put(AppConstant.STATUS,
	 * errorStatus); response.put(AppConstant.MESSAGE,
	 * "Identifier is empty or null"); LOGGER.error("Identifier is empty or null");
	 * return new ResponseEntity<String>(response.toString(),
	 * HttpStatus.BAD_REQUEST); } if (null == loginRequest.getPassword() ||
	 * loginRequest.getPassword().isEmpty()) { response.put(AppConstant.STATUS,
	 * errorStatus); response.put(AppConstant.MESSAGE, "Password is empty or null");
	 * LOGGER.error("Password is empty or null"); return new
	 * ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST); } if
	 * (null == loginRequest.getMode() || loginRequest.getMode().isEmpty()) {
	 * response.put(AppConstant.STATUS, errorStatus);
	 * response.put(AppConstant.MESSAGE, "Mode is empty or null");
	 * LOGGER.error("Mode is empty or null"); return new
	 * ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST); } String
	 * mode = loginRequest.getMode().trim(); String identifier =
	 * loginRequest.getIdentifier().trim(); String password =
	 * CommonServiceUtil.encodePassword(loginRequest.getPassword().trim());
	 * 
	 * if (null != mode && !mode.isEmpty()) { if
	 * (!CommonServiceUtil.checkMode(mode)) { response.put(AppConstant.STATUS,
	 * errorStatus); response.put(AppConstant.MESSAGE, "Mode is incorrect");
	 * LOGGER.error("Mode is incorrect"); return new
	 * ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST); } } if
	 * (identifier.contains("@")) { if
	 * (!emailMobileValidator.emailValidator(identifier)) {
	 * response.put(AppConstant.STATUS, errorStatus);
	 * response.put(AppConstant.MESSAGE, "Email is not valid");
	 * LOGGER.error("Email is not valid"); return new
	 * ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST); } } else
	 * { if (!emailMobileValidator.mobileValidator(identifier)) {
	 * response.put(AppConstant.STATUS, errorStatus);
	 * response.put(AppConstant.MESSAGE, "Mobile is not valid");
	 * LOGGER.error("Mobile is not valid"); return new
	 * ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST); } }
	 * 
	 * User userDBEntity = userRepository.findByEmail(identifier); if (null ==
	 * userDBEntity) { userDBEntity = userRepository.findByMobile(identifier); } if
	 * (null == userDBEntity) { response.put(AppConstant.STATUS, errorStatus);
	 * response.put(AppConstant.MESSAGE, "User mobile/email is not registered");
	 * LOGGER.error("User mobile/email is not registered"); return new
	 * ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST); } return
	 * null; }
	 */

}
