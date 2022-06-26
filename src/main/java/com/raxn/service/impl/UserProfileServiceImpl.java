package com.raxn.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raxn.entity.MobileEmailCheck;
import com.raxn.entity.RchBroadbandLandline;
import com.raxn.entity.RchDth;
import com.raxn.entity.RchElectricity;
import com.raxn.entity.RchFastag;
import com.raxn.entity.RchGasAndCylinder;
import com.raxn.entity.RchGiftcards;
import com.raxn.entity.RchInsurance;
import com.raxn.entity.RchMobile;
import com.raxn.entity.RchPostpaid;
import com.raxn.entity.RchWater;
import com.raxn.entity.RewardPoints;
import com.raxn.entity.User;
import com.raxn.entity.Wallet;
import com.raxn.repository.MobileEmailCheckRepository;
import com.raxn.repository.RchBroadbandLandlineRepository;
import com.raxn.repository.RchDthRepository;
import com.raxn.repository.RchElectricityRepository;
import com.raxn.repository.RchFastagRepository;
import com.raxn.repository.RchGasAndCylinderRepository;
import com.raxn.repository.RchGiftcardsRepository;
import com.raxn.repository.RchInsuranceRepository;
import com.raxn.repository.RchMobileRepository;
import com.raxn.repository.RchPostpaidRepository;
import com.raxn.repository.RchWalletRepository;
import com.raxn.repository.RchWaterRepository;
import com.raxn.repository.RewardPointsRepository;
import com.raxn.repository.SuggestionRepository;
import com.raxn.repository.UserRepository;
import com.raxn.request.model.ChangeMobileRequest;
import com.raxn.request.model.ChangePwdRequest;
import com.raxn.request.model.DonateRequest;
import com.raxn.request.model.TransHistoryRequest;
import com.raxn.request.model.UpdateUserRequest;
import com.raxn.response.model.TransHistoryResponse;
import com.raxn.service.UserProfileService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;
import com.raxn.util.service.EmailMobileValidator;
import com.raxn.util.service.EmailSenderService;
import com.raxn.util.service.GatherTransactionHistory;
import com.raxn.util.service.SMSSenderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

	Logger LOGGER = LoggerFactory.getLogger(UserProfileServiceImpl.class);

	private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Autowired
	UserRepository userRepository;

	@Autowired
	SuggestionRepository suggestRepo;

	@Autowired
	MobileEmailCheckRepository mobileEmailRepo;
	
	@Autowired
	RewardPointsRepository rewardrepo;

	@Autowired
	RchMobileRepository rchMobileRepo;

	@Autowired
	RchGiftcardsRepository rchGiftcardRepo;

	@Autowired
	RchWalletRepository rchWalletRepo;

	@Autowired
	RchDthRepository rchDthRepo;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	RchBroadbandLandlineRepository rchBBLLRepo;
	@Autowired
	RchElectricityRepository rchElectricityRepo;
	@Autowired
	RchFastagRepository rchFastagRepo;
	@Autowired
	RchGasAndCylinderRepository rchGasAndCylinderRepo;
	@Autowired
	RchInsuranceRepository rchInsuranceRepo;
	@Autowired
	RchPostpaidRepository rchPostpaidRepo;
	@Autowired
	RchWaterRepository rchWaterRepo;

	@Autowired
	SMSSenderService smsservice;

	@Autowired
	EmailSenderService emailservice;

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
	public ResponseEntity<String> changePassword(HttpServletRequest request, ChangePwdRequest changePwdReq) {
		LOGGER.info("Entered changePassword() -> Start");
		LOGGER.info("changePwdReq request=" + ReflectionToStringBuilder.toString(changePwdReq));
		// LOGGER.info("Authorization"+auth);
		JSONObject response = new JSONObject();
		Authentication authentication = null;
		boolean isauthenticated = false;

		String usernameToken = (String) request.getAttribute("usernametoken");
		LOGGER.info("usernameToken=" + usernameToken);
		// User userEntity = new User();

		if (null == changePwdReq.getOldpassword().trim() || changePwdReq.getOldpassword().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "old password is empty or null");
			LOGGER.error("old password is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null == changePwdReq.getNewpassword().trim() || changePwdReq.getNewpassword().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "new password is empty or null");
			LOGGER.error("new password is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (changePwdReq.getNewpassword().trim().equalsIgnoreCase(changePwdReq.getOldpassword().trim())) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "new password can not be same as old password");
			LOGGER.error("new password is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == changePwdReq.getUsername() || changePwdReq.getUsername().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "username is empty or null");
			LOGGER.error("username is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == changePwdReq.getMode() || changePwdReq.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "mode is empty or null");
			LOGGER.error("mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != changePwdReq.getMode() && !changePwdReq.getMode().isEmpty()) {
			if (!CommonServiceUtil.checkMode(changePwdReq.getMode().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "mode is incorrect");
				LOGGER.error("mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		String oldpassword = changePwdReq.getOldpassword().trim();
		String newpassword = changePwdReq.getNewpassword().trim();
		String username = changePwdReq.getUsername().trim();

		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, oldpassword));
			isauthenticated = authentication.isAuthenticated();
			LOGGER.info("auth status=" + isauthenticated);
		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Old password is incorrect");
			LOGGER.error("Old password is incorrect", e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (!isauthenticated) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Old password is incorrect");
			LOGGER.error("Old password is incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (!username.equals(usernameToken)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "request is suspicious");
			LOGGER.error("request is suspicious");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		User userInfo = userRepository.findByUsername(username);
		if (null == userInfo) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "user is not registered");
			LOGGER.error("user is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		userInfo.setPassword(CommonServiceUtil.encodePassword(newpassword));
		userInfo.setId(userInfo.getId());

		User userData = userRepository.save(userInfo);

		// send SMS/Email
		Thread threadSMS = new Thread(new Runnable() {
			public void run() {
				if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
					smsservice.sendSMS_ChangePassword(userData.getUsername(), userData.getMobile(),
							"Password_Change_SMS", userData.getEmail());
				}
			}
		});
		threadSMS.start();

		Thread threadMail = new Thread(new Runnable() {
			public void run() {
				if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
					emailservice.formatChangePasswordEmail(userData.getEmail().trim(), userData.getName());
				}
			}
		});
		threadMail.start();

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, AppConstant.PASSWORD_CHANGE);
		LOGGER.info(AppConstant.PASSWORD_CHANGE);
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changeMobile(HttpServletRequest request, ChangeMobileRequest changeMobileReq) {
		LOGGER.info("Entered changeMobile() -> Start");
		LOGGER.info("changeMobileReq request=" + ReflectionToStringBuilder.toString(changeMobileReq));
		JSONObject response = new JSONObject();

		if (null == changeMobileReq.getNewmobile() || changeMobileReq.getNewmobile().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "new mobile is empty or null");
			LOGGER.error("new mobile is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == changeMobileReq.getUsername() || changeMobileReq.getUsername().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "username is empty or null");
			LOGGER.error("username is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == changeMobileReq.getServicename() || changeMobileReq.getServicename().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "service name is empty or null");
			LOGGER.error("service name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null == changeMobileReq.getMode() || changeMobileReq.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "mode is empty or null");
			LOGGER.error("mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != changeMobileReq.getMode() && !changeMobileReq.getMode().isEmpty()) {
			if (!CommonServiceUtil.checkMode(changeMobileReq.getMode().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "mode is incorrect");
				LOGGER.error("mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		String newmobile = changeMobileReq.getNewmobile().trim();
		String username = changeMobileReq.getUsername().trim();
		String servicename = changeMobileReq.getServicename().trim();
		String usernameToken = (String) request.getAttribute("usernametoken");
		LOGGER.info("usernameToken=" + usernameToken);

		String remoteaddress = (String) request.getRemoteAddr();
		LOGGER.info("remoteaddress=" + remoteaddress);
		String localaddress = (String) request.getLocalAddr();
		LOGGER.info("localaddress=" + localaddress);
		String remoteahost = (String) request.getRemoteHost();
		LOGGER.info("remoteahost=" + remoteahost);

		if (!servicename.equalsIgnoreCase(AppConstant.SERVICE_CHANGE_MOBILE)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Service name is incorrect");
			LOGGER.error("Service name is incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (!username.equals(usernameToken)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "request is suspicious");
			LOGGER.error("request is suspicious");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		User userInfo = userRepository.findByUsername(username);
		if (null == userInfo) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "username is not registered");
			LOGGER.error("username is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		User userInfoMobile = userRepository.findByEmailOrMobile(newmobile);
		if (null != userInfoMobile) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "new mobile is already registered");
			LOGGER.error("new mobile is already registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		// checking mobile verification
		MobileEmailCheck mobileData = mobileEmailRepo.findByMobile(newmobile);
		
		if(null == mobileData) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "new mobile is not verified with OTP");
			LOGGER.error("new mobile is not verified with OTP");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null != mobileData) {
			String otpStatus = mobileData.getOtpStatus().trim();
			String otpService = mobileData.getOtpServicename().trim();
			if (null == otpStatus || otpStatus.isEmpty() || otpService.isEmpty() || null == otpService
					|| !otpStatus.equalsIgnoreCase("verified") || !otpService.equalsIgnoreCase(servicename)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "new mobile is not verified with OTP");
				LOGGER.error("new mobile is not verified with OTP");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}
		userInfoMobile = null;

		userInfo.setMobile(newmobile);
		User userData = userRepository.save(userInfo);

		// send SMS/Email
		Thread threadSMS = new Thread(new Runnable() {
			public void run() {
				if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
					smsservice.sendSMS_ChangeMobile(userData.getUsername(), userData.getMobile(), "Mobile_Change_SMS",
							userData.getEmail());
				}
			}
		});
		threadSMS.start();

		Thread threadMail = new Thread(new Runnable() {
			public void run() {
				if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
					emailservice.formatChangeMobileEmail(userData.getEmail().trim(), userData.getName(),
							userData.getMobile());
				}
			}
		});
		threadMail.start();

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, AppConstant.MOBILE_CHANGE);
		LOGGER.info(AppConstant.MOBILE_CHANGE);
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> updateProfile(HttpServletRequest request, UpdateUserRequest updateUserReq) {
		LOGGER.info("Entered updateProfile() -> Start");
		LOGGER.info("updateUserReq request=" + ReflectionToStringBuilder.toString(updateUserReq));
		JSONObject response = new JSONObject();

		if (null == updateUserReq.getName() || updateUserReq.getName().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "name is empty or null");
			LOGGER.error("name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == updateUserReq.getUsername() || updateUserReq.getUsername().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "username is empty or null");
			LOGGER.error("username is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null == updateUserReq.getMode() || updateUserReq.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "mode is empty or null");
			LOGGER.error("mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != updateUserReq.getMode() && !updateUserReq.getMode().isEmpty()) {
			if (!CommonServiceUtil.checkMode(updateUserReq.getMode().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "mode is incorrect");
				LOGGER.error("mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		String name = updateUserReq.getName().trim();
		String username = updateUserReq.getUsername().trim();
		String usernameToken = (String) request.getAttribute("usernametoken");
		LOGGER.info("usernameToken=" + usernameToken);

		if (!username.equals(usernameToken)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "request is suspicious");
			LOGGER.error("request is suspicious");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		User userInfo = userRepository.findByUsername(username);
		if (null == userInfo) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "username is not registered");
			LOGGER.error("username is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		userInfo.setName(name);
		if (null != updateUserReq.getDob() && !updateUserReq.getDob().isEmpty()) {
			userInfo.setDob(updateUserReq.getDob().trim());
		}
		if (null != updateUserReq.getCity() && !updateUserReq.getCity().isEmpty()) {
			userInfo.setCity(updateUserReq.getCity().trim());
		}

		userRepository.save(userInfo);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, AppConstant.PROFILE_CHANGE);
		LOGGER.info(AppConstant.PROFILE_CHANGE);
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> transHistory(HttpServletRequest request, TransHistoryRequest transHistoryReq) throws JsonProcessingException {
		LOGGER.info("Entered transHistory() -> Start");
		LOGGER.info("transHistoryReq request=" + ReflectionToStringBuilder.toString(transHistoryReq));
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();
		TransHistoryResponse transResponse = new TransHistoryResponse();
		List<RchMobile> rechMobileHistory = new ArrayList<RchMobile>();
		List<RchDth> rechDthHistory = new ArrayList<RchDth>();
		List<Wallet> rechWalletHistory = new ArrayList<Wallet>();
		List<RchGiftcards> rechGiftcardHistory = new ArrayList<RchGiftcards>();

		List<RchBroadbandLandline> rechBBLLHistory = new ArrayList<RchBroadbandLandline>();
		List<RchElectricity> rechElectricityHistory = new ArrayList<RchElectricity>();
		List<RchFastag> rechFastagHistory = new ArrayList<RchFastag>();
		List<RchGasAndCylinder> rechgasAndCylinderHistory = new ArrayList<RchGasAndCylinder>();
		List<RchInsurance> rechInsuranceHistory = new ArrayList<RchInsurance>();
		List<RchPostpaid> rechPostpaidHistory = new ArrayList<RchPostpaid>();
		List<RchWater> rechWaterHistory = new ArrayList<RchWater>();

		List<TransHistoryResponse> displayHistory = new ArrayList<TransHistoryResponse>();

		if (null == transHistoryReq.getCategory() || transHistoryReq.getCategory().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "category is empty or null");
			LOGGER.error("category is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == transHistoryReq.getUsername() || transHistoryReq.getUsername().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "username is empty or null");
			LOGGER.error("username is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null == transHistoryReq.getMode() || transHistoryReq.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "mode is empty or null");
			LOGGER.error("mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != transHistoryReq.getMode() && !transHistoryReq.getMode().isEmpty()) {
			if (!CommonServiceUtil.checkMode(transHistoryReq.getMode().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "mode is incorrect");
				LOGGER.error("mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		String category = transHistoryReq.getCategory().trim();
		String username = transHistoryReq.getUsername().trim();
		String usernameToken = (String) request.getAttribute("usernametoken");
		LOGGER.info("usernameToken=" + usernameToken);

		if (!username.equals(usernameToken)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "request is suspicious");
			LOGGER.error("request is suspicious");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		User userInfo = userRepository.findByUsername(username);
		if (null == userInfo) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "username is not registered");
			LOGGER.error("username is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		Date todayDate = java.sql.Date.valueOf(java.time.LocalDate.now());
		LOGGER.info("Today date is " + todayDate);
		Date dateBefore30Days = java.sql.Date.valueOf(java.time.LocalDate.now().minusDays(30));
		LOGGER.info("dateBefore30Days=" + dateBefore30Days);

		if (category.equalsIgnoreCase("recharge")) {
			rechMobileHistory = rchMobileRepo.findByDateTime(username, dateBefore30Days, todayDate);
			rechDthHistory = rchDthRepo.findByDateTime(username, dateBefore30Days, todayDate);

			displayHistory = GatherTransactionHistory.listRechargeHistory(rechMobileHistory, rechDthHistory);
		}
		if (category.equalsIgnoreCase("bills")) {
			rechBBLLHistory = rchBBLLRepo.findByDateTime(username, dateBefore30Days, todayDate);
			rechElectricityHistory = rchElectricityRepo.findByDateTime(username, dateBefore30Days, todayDate);
			rechFastagHistory = rchFastagRepo.findByDateTime(username, dateBefore30Days, todayDate);
			rechgasAndCylinderHistory = rchGasAndCylinderRepo.findByDateTime(username, dateBefore30Days, todayDate);
			rechInsuranceHistory = rchInsuranceRepo.findByDateTime(username, dateBefore30Days, todayDate);
			rechPostpaidHistory = rchPostpaidRepo.findByDateTime(username, dateBefore30Days, todayDate);
			rechWaterHistory = rchWaterRepo.findByDateTime(username, dateBefore30Days, todayDate);

			displayHistory = GatherTransactionHistory.listBillsHistory(rechBBLLHistory, rechElectricityHistory,
					rechFastagHistory, rechgasAndCylinderHistory, rechInsuranceHistory, rechPostpaidHistory,
					rechWaterHistory);
		}
		if (category.equalsIgnoreCase("wallet")) {
			rechWalletHistory = rchWalletRepo.findByDateTime(username, dateBefore30Days, todayDate);
			displayHistory = GatherTransactionHistory.listWalletHistory(rechWalletHistory);
		}
		if (category.equalsIgnoreCase("giftcards")) {
			rechGiftcardHistory = rchGiftcardRepo.findByDateTime(username, dateBefore30Days, todayDate);
			displayHistory = GatherTransactionHistory.listGiftcardHistory(rechGiftcardHistory);
		}

		LOGGER.info("displayHistory size = " + displayHistory.size());

		if (displayHistory.size() == 0) {
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "no " + category + " history found");
			LOGGER.error("no " + category + " history found");
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
		}

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, category + " history found");
		LOGGER.error(category + " history found");
		return new ResponseEntity<String>(objMapper.writeValueAsString(displayHistory), HttpStatus.OK);

	}

	@Override
	public ResponseEntity<String> getRewardHistory(HttpServletRequest request, String username)
			throws JsonProcessingException {
		LOGGER.info("Entered getRewardHistory() -> Start");
		LOGGER.info("Parameter username ->" + username);
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();

		if (null == username || username.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "username is empty or null");
			LOGGER.error("username is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		String usernameToken = (String) request.getAttribute("usernametoken");
		LOGGER.info("usernameToken=" + usernameToken);

		if (!username.trim().equals(usernameToken)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "request is suspicious");
			LOGGER.error("request is suspicious");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		List<RewardPoints> userRewards = rewardrepo.findFirst20ByUsernameOrderByDateTimeDesc(username.trim());
		if (userRewards.isEmpty() || userRewards.size() == 0) {
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "No rewards available");
			LOGGER.error("No rewards available for username:" + username);
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
		}
		LOGGER.info("size of rewards available -> " + userRewards.size());
		return new ResponseEntity<String>(objMapper.writeValueAsString(userRewards), HttpStatus.OK);
	}
	
	/**
	 * 1st=Userid, 2nd=mode, 3rd=action, 4th=amount
	 * @param params
	 */
	public void calculateAndSaveRewardPoint(String... params) {
		LOGGER.info("Entered calculateAndSaveRewardPoint() -> Start");
		LOGGER.info("params length ->" + params.length);
		String action = null, activity = null;
		double amount = 0.0, userRewardPoint=0.0;
		String mode = null, username = null;
		int paramsize = 0;
		RewardPoints rp = new RewardPoints();
		String depostText = "Deposited Rs<AMOUNT> to wallet";
		String donateText = "Donated Rs<AMOUNT> to social care fund";
		User user = new User();
		double userCreditPoint = 0.0;

		paramsize = params.length;

		if (paramsize == 3) {
			username = params[0];
			mode = params[1];
			action = params[2];
		}
		if (paramsize == 4) {
			username = params[0];
			mode = params[1];
			action = params[2];
			amount = Double.parseDouble(params[3]);
		}
		
		if(null != username && !username.isEmpty()) {
			user = userRepository.findByUsername(username);
		}
		if(null != user) {
			userRewardPoint = user.getRewardPoint();
		}

		if (action.equalsIgnoreCase("deposit")) {
			activity = depostText.replace("<AMOUNT>", amount + "");
			userCreditPoint = amount/100;
			userRewardPoint += userCreditPoint;
		}
		if (action.equalsIgnoreCase("donate")) {
			activity = donateText.replace("<AMOUNT>", amount + "");
			userCreditPoint = amount*3/100;
			userRewardPoint += userCreditPoint;
		}

		rp.setUsername(username);
		rp.setMode(mode);
		if (null != activity && !activity.isEmpty()) {
			rp.setActivity(activity);
		}
		rp.setCredit(userCreditPoint);
		rp.setTotalPoint(userRewardPoint);
		
		user.setRewardPoint(userRewardPoint);
		
		userRepository.save(user);		
		rewardrepo.save(rp);
	}

	@Override
	public ResponseEntity<String> donate(HttpServletRequest request, DonateRequest donateRequest) throws JsonProcessingException {
		LOGGER.info("Entered donate() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		LOGGER.info("Parameter donateRequest ->" + objMapper.writeValueAsString(donateRequest));
		JSONObject response = new JSONObject();
		int uid = 0;
		double donateamount = 0.0;

		if (null == donateRequest.getAmount() || donateRequest.getAmount().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Amount is empty or zero");
			LOGGER.error("Amount is empty or zero");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == donateRequest.getUsername() || donateRequest.getUsername().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Username is incorrect");
			LOGGER.error("Username is incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == donateRequest.getMode() || donateRequest.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Mode is empty or null");
			LOGGER.error("Mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != donateRequest.getMode() && !donateRequest.getMode().isEmpty()) {
			if (!CommonServiceUtil.checkMode(donateRequest.getMode().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mode is incorrect");
				LOGGER.error("Mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}
		String amount = donateRequest.getAmount().trim();
		String username = donateRequest.getUsername().trim();
		String usernameToken = (String) request.getAttribute("usernametoken");
		LOGGER.info("usernameToken=" + usernameToken);

		if (!username.trim().equals(usernameToken)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "request is suspicious");
			LOGGER.error("request is suspicious");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		
		
		User userInfo = userRepository.findById(uid);
		if (null == userInfo) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is not registered");
			LOGGER.error("userid is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		double walletmoney = userInfo.getWalletBalance();
		if (donateamount > walletmoney) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "donate amount should be less than wallet balance");
			LOGGER.error("donate amount should be less than wallet balance");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		String orderid = "DF";
		orderid += CommonServiceUtil.genOrderId();

		return null;
	}

}
