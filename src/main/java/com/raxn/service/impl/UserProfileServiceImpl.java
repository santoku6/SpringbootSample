package com.raxn.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raxn.entity.MobileEmailCheck;
import com.raxn.entity.RchDth;
import com.raxn.entity.RchMobile;
import com.raxn.entity.User;
import com.raxn.repository.MobileEmailCheckRepository;
import com.raxn.repository.RchDthRepository;
import com.raxn.repository.RchMobileRepository;
import com.raxn.repository.SuggestionRepository;
import com.raxn.repository.UserRepository;
import com.raxn.request.model.ChangeMobileRequest;
import com.raxn.request.model.ChangePwdRequest;
import com.raxn.request.model.TransHistoryRequest;
import com.raxn.request.model.UpdateUserRequest;
import com.raxn.response.model.TransHistoryResponse;
import com.raxn.service.UserProfileService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;
import com.raxn.util.service.EmailMobileValidator;
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
	RchMobileRepository rchMobileRepo;

	@Autowired
	RchDthRepository rchDthRepo;

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
	public ResponseEntity<String> changePassword(ChangePwdRequest changePwdReq) {
		LOGGER.info("Entered changePassword() -> Start");
		LOGGER.info("changePwdReq request=" + ReflectionToStringBuilder.toString(changePwdReq));
		JSONObject response = new JSONObject();
		User userEntity = new User();

		if (null == changePwdReq.getOldpassword() || changePwdReq.getOldpassword().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "old password is empty or null");
			LOGGER.error("old password is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null == changePwdReq.getNewpassword() || changePwdReq.getNewpassword().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "new password is empty or null");
			LOGGER.error("new password is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == changePwdReq.getUserid() || changePwdReq.getUserid().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is empty or null");
			LOGGER.error("userid is empty or null");
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
		String userid = changePwdReq.getUserid().trim();
		int userInt = 0;

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
			response.put(AppConstant.MESSAGE, "userid is not registered");
			LOGGER.error("userid is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (!CommonServiceUtil.encodePassword(oldpassword).equals(userInfo.getPassword())) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "existing password is incorrect");
			LOGGER.error("existing password is incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		userEntity.setPassword(CommonServiceUtil.encodePassword(newpassword));

		User userData = userRepository.save(userEntity);

		// TODO
		// SEND SMS AND EMAIL
		if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
			// password change sms
			smsservice.sendSMS_ChangePassword(userData.getEmail(), userData.getMobile(), "Password_Change_SMS");

		}
		if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
			// password change email
		}
		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, AppConstant.PASSWORD_CHANGE);
		LOGGER.info(AppConstant.PASSWORD_CHANGE);
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

	}

	@Override
	public ResponseEntity<String> changeMobile(ChangeMobileRequest changeMobileReq) {
		LOGGER.info("Entered changeMobile() -> Start");
		LOGGER.info("changeMobileReq request=" + ReflectionToStringBuilder.toString(changeMobileReq));
		JSONObject response = new JSONObject();
		User userEntity = new User();

		if (null == changeMobileReq.getNewmobile() || changeMobileReq.getNewmobile().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "new mobile is empty or null");
			LOGGER.error("new mobile is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == changeMobileReq.getUserid() || changeMobileReq.getUserid().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is empty or null");
			LOGGER.error("userid is empty or null");
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
		String userid = changeMobileReq.getUserid().trim();
		int userInt = 0;

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
			response.put(AppConstant.MESSAGE, "userid is not registered");
			LOGGER.error("userid is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		// checking mobile verification
		MobileEmailCheck mobileData = mobileEmailRepo.findByMobile(newmobile);

		if (null != mobileData) {
			String mobileStatus = mobileData.getOtpStatus();
			if (null != mobileStatus) {
				if (!mobileStatus.equalsIgnoreCase("verified")) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "new mobile is not verified with OTP");
					LOGGER.error("new mobile is not verified with OTP");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
			}
		}

		userEntity.setMobile(newmobile);
		User userData = userRepository.save(userEntity);

		// TODO
		// SEND SMS AND EMAIL
		if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
			// password change sms
			smsservice.sendSMS_ChangeMobile(userData.getEmail(), userData.getMobile(), "Mobile_Change_SMS");

		}
		if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
			// password change email
		}
		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, AppConstant.MOBILE_CHANGE);
		LOGGER.info(AppConstant.MOBILE_CHANGE);
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> updateProfile(UpdateUserRequest updateUserReq) {
		LOGGER.info("Entered updateProfile() -> Start");
		LOGGER.info("updateUserReq request=" + ReflectionToStringBuilder.toString(updateUserReq));
		JSONObject response = new JSONObject();
		User userEntity = new User();

		if (null == updateUserReq.getName() || updateUserReq.getName().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "name is empty or null");
			LOGGER.error("name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == updateUserReq.getUserid() || updateUserReq.getUserid().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is empty or null");
			LOGGER.error("userid is empty or null");
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
		String userid = updateUserReq.getUserid().trim();

		int userInt = 0;

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
			response.put(AppConstant.MESSAGE, "userid is not registered");
			LOGGER.error("userid is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		userEntity.setName(name);
		if (null != updateUserReq.getDob() && !updateUserReq.getDob().isEmpty()) {
			userEntity.setDob(updateUserReq.getDob().trim());
		}
		if (null != updateUserReq.getCity() && !updateUserReq.getCity().isEmpty()) {
			userEntity.setCity(updateUserReq.getCity().trim());
		}

		User userData = userRepository.save(userEntity);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, AppConstant.PROFILE_CHANGE);
		LOGGER.info(AppConstant.PROFILE_CHANGE);
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> transHistory(TransHistoryRequest transHistoryReq) throws JsonProcessingException {
		LOGGER.info("Entered transHistory() -> Start");
		LOGGER.info("transHistoryReq request=" + ReflectionToStringBuilder.toString(transHistoryReq));
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();
		TransHistoryResponse transResponse = new TransHistoryResponse();
		List<RchMobile> rechMobileHistory = new ArrayList<RchMobile>();
		List<RchDth> rechDthHistory = new ArrayList<RchDth>();

		if (null == transHistoryReq.getCategory() || transHistoryReq.getCategory().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "category is empty or null");
			LOGGER.error("category is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == transHistoryReq.getUserid() || transHistoryReq.getUserid().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is empty or null");
			LOGGER.error("userid is empty or null");
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
		String userid = transHistoryReq.getUserid().trim();

		int userInt = 0;

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
			response.put(AppConstant.MESSAGE, "userid is not registered");
			LOGGER.error("userid is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		Date todayDate = java.sql.Date.valueOf(java.time.LocalDate.now());
		LOGGER.info("Today date is " + todayDate);
		Date dateBefore30Days = java.sql.Date.valueOf(java.time.LocalDate.now().minusDays(30));
		LOGGER.info("dateBefore30Days=" + dateBefore30Days);

		if (category.equalsIgnoreCase("recharge")) {
			rechMobileHistory = rchMobileRepo.findByDateTime(userid, dateBefore30Days, todayDate);
			rechDthHistory = rchDthRepo.findByDateTime(userid, dateBefore30Days, todayDate);
			// TODO for fastag

		}

		List<TransHistoryResponse> displayHistory = GatherTransactionHistory.listRechargeHistory(rechMobileHistory, rechDthHistory);
		/*
		 * for (RchMobile indRech : rechMobileHistory) {
		 * transResponse.setAmount(indRech.getRchAmount());
		 * transResponse.setCategory(indRech.getCategory());
		 * transResponse.setDatetime(indRech.getDateTime());
		 * transResponse.setMobile(indRech.getMobile());
		 * transResponse.setOperator(indRech.getOperator());
		 * transResponse.setOrderid(indRech.getOrderid());
		 * transResponse.setUserid(indRech.getUserid());
		 * 
		 * displayHistory.add(transResponse); }
		 */
		LOGGER.info("displayHistory size = " + displayHistory.size());

		if (displayHistory.size() == 0) {
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "no recharge history found");
			LOGGER.error("no recharge history found");
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
		}

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "recharge history found");
		LOGGER.error("recharge history found");
		return new ResponseEntity<String>(objMapper.writeValueAsString(displayHistory), HttpStatus.OK);

	}

}
