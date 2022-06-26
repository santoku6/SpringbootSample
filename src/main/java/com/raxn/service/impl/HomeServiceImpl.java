package com.raxn.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.raxn.entity.JwtTokentable;
import com.raxn.entity.LoginActivity;
import com.raxn.entity.MobileEmailCheck;
import com.raxn.entity.User;
import com.raxn.repository.LoginActivityRepository;
import com.raxn.repository.MobileEmailCheckRepository;
import com.raxn.repository.SuggestionRepository;
import com.raxn.repository.UserRepository;
import com.raxn.repository.jwtTokentableRepository;
import com.raxn.request.model.LoginRequest;
import com.raxn.request.model.NextLoginRequest;
import com.raxn.request.model.OTPModel;
import com.raxn.request.model.ResetPWDRequest;
import com.raxn.request.model.UserRequest;
import com.raxn.request.model.VerifyOTPRequest;
import com.raxn.response.model.UserResponse;
import com.raxn.security.CustomUserDetails;
import com.raxn.security.CustomUserDetailsService;
import com.raxn.service.HomeService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;
import com.raxn.util.service.EmailMobileValidator;
import com.raxn.util.service.EmailSenderService;
import com.raxn.util.service.JWTUtil;
import com.raxn.util.service.SMSSenderService;

import io.jsonwebtoken.impl.DefaultClaims;
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
	jwtTokentableRepository jwtTokentableRepo;

	@Autowired
	SuggestionRepository suggestRepo;

	@Autowired
	MobileEmailCheckRepository mobileEmailRepo;
	@Autowired
	LoginActivityRepository loginactivityRepo;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	SMSSenderService smsservice;

	@Autowired
	EmailSenderService emailservice;

	@Autowired
	CustomUserDetailsService userDetailsService;

	@Value("${send.sms.service}")
	private String SEND_SMS_SERVICE;

	@Value("${send.email.service}")
	private String SEND_EMAIL_SERVICE;

	@Value("${sms.validity.minutes}")
	private String SMS_VALIDITY_MINUTES;

	@Value("${ipaddress.url}")
	private String IPADDESS_URL;

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
		if (null != userRequest.getEmail() && !userRequest.getEmail().isEmpty()) {
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
			response.put(AppConstant.MESSAGE, "User IP Address is empty or null");
			LOGGER.error("User IP Address is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		// checking if email/mobile verified with otp or not
		ResponseEntity<String> responseStr = checkVerificationForRegistration(userRequest.getMobile(),
				userRequest.getEmail());
		if (responseStr.getStatusCodeValue() != 200) {
			return new ResponseEntity<String>(responseStr.getBody(), responseStr.getStatusCode());
		}

		if (responseStr.getStatusCodeValue() == 200) {
			/*
			 * try { myipadress = CommonServiceUtil.getIp(); } catch (Exception e) {
			 * LOGGER.error(e.getMessage(), e); }
			 * 
			 * LOGGER.info("ip address=" + myipadress);
			 */
			myipadress = userRequest.getUserip().trim();
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
			userEntity.setUsername(CommonServiceUtil.genUsername());
			userEntity.setRole("USER");

			User userData = userRepository.save(userEntity);
			userEntity = null;

			// SEND SMS AND EMAIL
			Thread threadSMS = new Thread(new Runnable() {
				public void run() {
					if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
						smsservice.sendSMS_Registration(userData.getEmail().trim(), userData.getMobile().trim(),
								"Registration_SMS");
					}
				}
			});
			threadSMS.start();

			Thread threadMail = new Thread(new Runnable() {
				public void run() {
					if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
						emailservice.formatRegistrationEmail(userData.getEmail().trim(), userData.getName(),
								userData.getMobile());
					}
				}
			});
			threadMail.start();

			// clearing email/mobile verification status after registration
			clearEmailMobileData(userData.getMobile(), userData.getEmail());

			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "User registration completed");
			LOGGER.info("User registration completed for email " + userData.getEmail());
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
		} else {
			LOGGER.error("Some error, User registration incomplete for " + userRequest.getEmail().trim());
			LOGGER.error(responseStr.getBody(), responseStr.getStatusCode());
			return new ResponseEntity<String>(responseStr.getBody(), responseStr.getStatusCode());
		}
	}

	private ResponseEntity<String> checkVerificationForRegistration(String mobile, String email) {
		LOGGER.info("Entered checkVerificationForRegistration() -> Start");
		LOGGER.info("mobile=" + mobile + " ,email=" + email);
		boolean statusMobile = false, statusEmail = false;
		JSONObject response = new JSONObject();

		// checking email verification
		MobileEmailCheck emailData = mobileEmailRepo.findByEmail(email);

		if (null == emailData) {
			statusEmail = false;
		}

		if (null != emailData) {
			String emailStatus = emailData.getOtpStatus();
			String serviceNameInDB = emailData.getOtpServicename();
			long otpTS = emailData.getOtpDatetime().getTime();
			long currentTS = System.currentTimeMillis();
			long differenceTS = currentTS - otpTS;
			LOGGER.info("Register: differenceTS (in ms)=" + differenceTS);

			long allowedMS = Long.parseLong(SMS_VALIDITY_MINUTES + 10) * 60 * 1000;
			if (serviceNameInDB.isEmpty() || null == serviceNameInDB || emailStatus.isEmpty() || null == emailStatus
					|| differenceTS > allowedMS || !serviceNameInDB.equalsIgnoreCase("signupusr")
					|| !emailStatus.equalsIgnoreCase("verified")) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Email OTP not validated");
				LOGGER.error("Email OTP not validated");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			if (null != emailStatus && !emailStatus.isEmpty()) {
				if (emailStatus.equalsIgnoreCase("verified")) {
					statusEmail = true;
				}
			}
		}
		if (!statusEmail) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Email is not verified");
			LOGGER.error("Email is not verified");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		// checking mobile verification
		MobileEmailCheck mobileData = mobileEmailRepo.findByMobile(mobile);

		if (null == mobileData) {
			statusMobile = false;
		}

		if (null != mobileData) {
			String mobileStatus = mobileData.getOtpStatus();
			String serviceNameInDB = mobileData.getOtpServicename();
			long otpTS = mobileData.getOtpDatetime().getTime();
			long currentTS = System.currentTimeMillis();
			long differenceTS = currentTS - otpTS;
			LOGGER.info("Register: differenceTS (in ms)=" + differenceTS);

			long allowedMS = Long.parseLong(SMS_VALIDITY_MINUTES + 10) * 60 * 1000;
			if (serviceNameInDB.isEmpty() || null == serviceNameInDB || mobileStatus.isEmpty() || null == mobileStatus
					|| differenceTS > allowedMS || !serviceNameInDB.equalsIgnoreCase("signupusr")
					|| !mobileStatus.equalsIgnoreCase("verified")) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mobile OTP not validated");
				LOGGER.error("Mobile OTP not validated");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			if (null != mobileStatus && !mobileStatus.isEmpty()) {
				if (mobileStatus.equalsIgnoreCase("verified")) {
					statusMobile = true;
				}
			}
		}
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
				final String smsotp = otp;
				// send OTP in email
				Thread threadMail = new Thread(new Runnable() {
					public void run() {
						if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
							emailservice.formatOTPEmail(identifier, "OTP_mail", smsotp);
						}
					}
				});
				threadMail.start();
			} else {
				final String mailotp = otp;
				// send OTP in SMS
				Thread threadSMS = new Thread(new Runnable() {
					public void run() {
						if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
							smsservice.sendSMS_OTP(null, identifier, "OTP_SMS", mailotp);
						}
					}
				});
				threadSMS.start();
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
				String userEmail = userDBEntity.getEmail();
				String userMobile = userDBEntity.getMobile();
				String userNameDB = userDBEntity.getUsername();

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

				final String finalotp = otp;
				// send OTP in email
				Thread threadMail = new Thread(new Runnable() {
					public void run() {
						if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
							emailservice.formatOTPEmail(userEmail, "OTP_mail", finalotp);
						}
					}
				});
				threadMail.start();

				// send OTP in SMS
				Thread threadSMS = new Thread(new Runnable() {
					public void run() {
						if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
							smsservice.sendSMS_OTP(userNameDB, userMobile, "OTP_SMS", finalotp);
						}
					}
				});
				threadSMS.start();

			}
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

			// User Login - Request OTP for 2fa
		} else if (servicename.equalsIgnoreCase(AppConstant.SERVICE_LOGIN_USER)) {
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
				String userEmail = userDBEntity.getEmail();
				String userMobile = userDBEntity.getMobile();
				String userNameDB = userDBEntity.getUsername();

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
				response.put(AppConstant.MESSAGE, "Login OTP generated");
				LOGGER.info("OTP to set:" + otp);

				final String finalotp = otp;
				// send OTP in email
				Thread threadMail = new Thread(new Runnable() {
					public void run() {
						if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
							emailservice.formatOTPEmail(userEmail, "OTP_mail", finalotp);
						}
					}
				});
				threadMail.start();

				// send OTP in SMS
				Thread threadSMS = new Thread(new Runnable() {
					public void run() {
						if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
							smsservice.sendSMS_OTP(userNameDB, userMobile, "OTP_SMS", finalotp);
						}
					}
				});
				threadSMS.start();

			}
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
			// mobile change scenario
		} else if (servicename.equalsIgnoreCase(AppConstant.SERVICE_CHANGE_MOBILE)) {
			// check if mobile is registered or not
			User userDBEntity = userRepository.findByMobile(identifier);

			if (null != userDBEntity) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "mobile is registered with other user");
				LOGGER.error("mobile is registered with other user");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (userDBEntity == null) {
				// check if identifier record is existing in emailmobile table, then update
				// otp_datetime. If verified then different otp, else same otp
				MobileEmailCheck identifierData = mobileEmailRepo.findByIdentifier(identifier);
				if (null != identifierData) {
					if (identifierData.getOtpStatus().equalsIgnoreCase("verified") || !identifierData
							.getOtpServicename().equalsIgnoreCase(AppConstant.SERVICE_CHANGE_MOBILE)) {
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

				final String mailotp = otp;
				// send OTP in SMS
				Thread threadSMS = new Thread(new Runnable() {
					public void run() {
						if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
							smsservice.sendSMS_OTP(null, identifier, "OTP_SMS", mailotp);
						}
					}
				});
				threadSMS.start();

				return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
			}

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
				userEmailEntity = null;
			} else {
				User userMobileEntity = userRepository.findByMobile(identifier);
				if (null != userMobileEntity) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "Mobile is already registered");
					LOGGER.error("Mobile is already registered");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				userMobileEntity = null;
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
				String otpStatusDB = userData.getOtpStatus();
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
		if (servicename.equalsIgnoreCase(AppConstant.SERVICE_CHANGE_MOBILE)) {
			// check if identifier is registered or not
			User userMobileEntity = userRepository.findByMobile(identifier);
			if (null != userMobileEntity) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "new mobile is already registered");
				LOGGER.error("new mobile is already registered");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			userMobileEntity = null;

			// check if identifier record is existing in emailmobile table
			MobileEmailCheck userData = mobileEmailRepo.findByMobile(identifier);

			if (null == userData) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "mobile verification failed");
				LOGGER.error("mobile verification failed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			} else {
				String serviceNameDB = userData.getOtpServicename();
				String otpStatusDB = userData.getOtpStatus();
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
		String serviceNameInDB = null, otpStatusInDB = null;
		//Date otpDateTimeInDB = null;

		if (null == resetPWDRequest.getServicename() || resetPWDRequest.getServicename().isEmpty()
				|| !resetPWDRequest.getServicename().equalsIgnoreCase(AppConstant.SERVICE_FORGET_PASSWORD)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Service name is empty or incorrect");
			LOGGER.error("Service name is empty or incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

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
		String servicename = resetPWDRequest.getServicename().trim();

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

			serviceNameInDB = userDBEntity.getOtpServicename();
			otpStatusInDB = userDBEntity.getOtpStatus();
			long otpTS = userDBEntity.getOtpDatetime().getTime();
			long currentTS = System.currentTimeMillis();
			long differenceTS = currentTS - otpTS;
			LOGGER.info("Reset Pwd:differenceTS (in ms)=" + differenceTS);

			// 10 minutes extra added, whole operation should finish within this time
			// else do again
			long allowedMS = Long.parseLong(SMS_VALIDITY_MINUTES + 10) * 60 * 1000;
			if (serviceNameInDB.isEmpty() || null == serviceNameInDB || otpStatusInDB.isEmpty() || null == otpStatusInDB
					|| differenceTS > allowedMS || !serviceNameInDB.equalsIgnoreCase(servicename)
					|| !otpStatusInDB.equalsIgnoreCase("verified")) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "OTP not validated");
				LOGGER.error("OTP not validated");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			userDBEntity.setPassword(password);

			userRepository.save(userDBEntity);
		}
		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "Reset password completed");
		LOGGER.info("Reset password completed for user " + identifier);
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}


	@Override
	public ResponseEntity<String> loginUser(LoginRequest loginRequest) {
		LOGGER.info("Entered loginUser() -> Start");
		LOGGER.info("identifier = " + loginRequest.getIdentifier());
		JSONObject response = new JSONObject();
		Authentication authentication = null;
		OTPModel otpModel = new OTPModel();

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
				|| !loginRequest.getServicename().equalsIgnoreCase(AppConstant.SERVICE_LOGIN_USER)) {
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
		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Bad Credential");
			
			if(e instanceof DisabledException) {
				response.put(AppConstant.MESSAGE, e.getMessage());
			}
			if(e instanceof LockedException) {
				response.put(AppConstant.MESSAGE, e.getMessage());
			}
			
			LOGGER.error("Login Error:: "+ e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.UNAUTHORIZED);
		}
		boolean isauthenticated = authentication.isAuthenticated();
		LOGGER.info("isauthenticated=" + isauthenticated);
		if (!isauthenticated) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Bad Credential");
			LOGGER.error("Bad Credential");
			return new ResponseEntity<String>(response.toString(), HttpStatus.UNAUTHORIZED);
		}
		// send OTP for 2FA
		otpModel.setIdentifier(identifier);
		otpModel.setMode(mode);
		otpModel.setServicename(serviceName);

		ResponseEntity<String> responseGenOTP = generateOTP(otpModel);

		return new ResponseEntity<String>(responseGenOTP.getBody(), responseGenOTP.getStatusCode());
	}

	@Override
	public ResponseEntity<String> nextLoginUser(NextLoginRequest nextloginRequest) {
		LOGGER.info("Entered nextLoginUser() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		VerifyOTPRequest verifyOTPRequest = new VerifyOTPRequest();
		JSONObject response = new JSONObject();

		try {
			LOGGER.info("Parameter verifyOTPRequest = " + objMapper.writeValueAsString(nextloginRequest));
		} catch (JsonProcessingException e) {
			LOGGER.error("Error ::" + e.getMessage());
		}

		if (null == nextloginRequest.getIpaddress() || nextloginRequest.getIpaddress().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "IP Address is empty or null");
			LOGGER.error("IP Address is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		BeanUtils.copyProperties(nextloginRequest, verifyOTPRequest);
		ResponseEntity<String> responseVerification = verifyOTP(verifyOTPRequest);
		LOGGER.info("OTP Verification status code = " + responseVerification.getStatusCodeValue());
		if (responseVerification.getStatusCodeValue() != 200) {
			return new ResponseEntity<String>(responseVerification.getBody(), responseVerification.getStatusCode());
		}
		LOGGER.info("login OTP verification completed");
		// create jwt token with username(12 chars)
		User userInfo = userRepository.findByEmailOrMobile(verifyOTPRequest.getIdentifier().trim());
		String username = userInfo.getUsername();
		String roleName = userInfo.getRole();
		userInfo = null;

		UserDetails userdetails = userDetailsService.loadUserByUsername(verifyOTPRequest.getIdentifier().trim());

		String token = jwtUtil.generateToken(userdetails);

		// store token in table
		JwtTokentable jwtTableData = jwtTokentableRepo.findByUsername(username);
		if (null == jwtTableData) {
			jwtTableData = new JwtTokentable();
		}
		jwtTableData.setUsername(username);
		jwtTableData.setToken(token);
		jwtTokentableRepo.save(jwtTableData);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.TOKEN, token);
		response.put(AppConstant.ROLE, roleName);
		response.put(AppConstant.USERNAME, username);
		response.put(AppConstant.MESSAGE, "full authenticated user");
		LOGGER.info("full authenticated user::" + verifyOTPRequest.getIdentifier());

		Thread threadStore = new Thread(new Runnable() {
			public void run() {
				storeUserLoginInfo(nextloginRequest);
			}
		});
		threadStore.start();

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	public void storeUserLoginInfo(NextLoginRequest nextloginRequest) {
		LOGGER.info("Entered storeUserLoginInfo() -> Start");
		StringBuffer response = new StringBuffer();
		LoginActivity loginactivity = new LoginActivity();
		String ipaddress = nextloginRequest.getIpaddress().trim();
		String requestUrl = IPADDESS_URL + ipaddress;

		try {
			URL url = new URL(requestUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			LOGGER.info("Response Code :: " + responseCode);
			if (responseCode == 200) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				con.disconnect();
			}

			// System.out.println(response.toString());
			JSONObject json = new JSONObject(response.toString());
			// System.out.println("--j--" + json.toString());

			loginactivity.setCity(json.getString("city"));
			loginactivity.setIpv4Address(ipaddress);
			loginactivity.setIspName(json.getString("asn_organization"));
			loginactivity.setLoginTime(new Date());
			loginactivity.setMode(nextloginRequest.getMode().trim());
			loginactivity.setState(json.getString("region"));
			loginactivity.setLatitude(json.getDouble("latitude") + "");
			loginactivity.setLongitude(json.getDouble("longitude") + "");
			loginactivity.setPostalcode(json.getString("postal_code"));
			loginactivity.setUsername(nextloginRequest.getIdentifier());

			if (null != nextloginRequest.getDeviceinfo() && !nextloginRequest.getDeviceinfo().isEmpty()) {
				loginactivity.setDeviceInfo(nextloginRequest.getDeviceinfo().trim());
			}
			if (null != nextloginRequest.getBrowserinfo() && !nextloginRequest.getBrowserinfo().isEmpty()) {
				loginactivity.setBrowserInfo(nextloginRequest.getBrowserinfo().trim());
			}

			loginactivityRepo.save(loginactivity);
		} catch (IOException e) {
			e.printStackTrace();
		}

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
			LOGGER.error(e.getMessage());
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

	@Override
	public ResponseEntity<String> deleteUserById(String identifier) throws JsonProcessingException {
		LOGGER.info("Entered deleteUserById() -> Start");
		LOGGER.info("Parameter identifier ->" + identifier);
		JSONObject response = new JSONObject();

		User userDBEntity = userRepository.findByEmail(identifier.trim());
		if (null == userDBEntity) {
			userDBEntity = userRepository.findByMobile(identifier.trim());
		}
		if (null == userDBEntity) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "User mobile/email is not registered");
			LOGGER.error("User mobile/email is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (userDBEntity != null) {
			userRepository.deleteByEmailOrMobile(identifier.trim());
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "User mobile/email record deleted");
			LOGGER.info("User mobile/email record deleted");
		}
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> getUserInfo() {
		LOGGER.info("Entered getUserInfo() -> Start");
		UserResponse userResponse = new UserResponse();

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		CustomUserDetails usrdetails = (CustomUserDetails) authentication.getPrincipal();

		CustomUserDetails usr = (CustomUserDetails) userDetailsService.loadUserByUsername(usrdetails.getUsername());

		BeanUtils.copyProperties(usr.getUser(), userResponse);

		return new ResponseEntity<String>(new Gson().toJson(userResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> logoutSite(String username, HttpServletRequest request,
			HttpServletResponse response) {
		LOGGER.info("Entered logoutSite() -> Start");
		LOGGER.info("username=" + username);
		JSONObject responseSite = new JSONObject();

		if (null == username || username.isEmpty()) {
			responseSite.put(AppConstant.STATUS, errorStatus);
			responseSite.put(AppConstant.MESSAGE, "username is empty");
			LOGGER.error("username is empty");
			return new ResponseEntity<String>(responseSite.toString(), HttpStatus.BAD_REQUEST);
		}
		String msg = "logout completed";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		// delete jwt token from table
		JwtTokentable jwtTableData = jwtTokentableRepo.findByUsername(username);
		if (null != jwtTableData) {
			int row = jwtTokentableRepo.deleteByUsername(username);
			LOGGER.info("jwt entry deleted::" + row);
		}

		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> refreshtoken(HttpServletRequest request) {
		LOGGER.info("Entered refreshtoken() -> Start");
		JSONObject response = new JSONObject();

		// From the HttpRequest get the claims
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

		if (null == claims) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Previous token not expired");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		String token = jwtUtil.generateRefreshToken(expectedMap, expectedMap.get("sub").toString());
		String username = jwtUtil.getUsername(token);

		JwtTokentable jwtTableData = jwtTokentableRepo.findByUsername(username);
		if (null == jwtTableData) {
			jwtTableData = new JwtTokentable();
		}
		jwtTableData.setUsername(username);
		jwtTableData.setToken(token);
		jwtTokentableRepo.save(jwtTableData);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.REFRESH_TOKEN, token);
		response.put(AppConstant.MESSAGE, "Refresh Token Available");

		LOGGER.info("Refresh Token generated");
		return ResponseEntity.ok(response.toString());
	}

	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}

}
