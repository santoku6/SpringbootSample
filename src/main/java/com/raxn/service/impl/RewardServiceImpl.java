package com.raxn.service.impl;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raxn.entity.RewardPoints;
import com.raxn.entity.User;
import com.raxn.repository.RewardPointsRepository;
import com.raxn.repository.UserRepository;
import com.raxn.request.model.DonateRequest;
import com.raxn.service.RewardService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RewardServiceImpl implements RewardService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RewardServiceImpl.class);

	@Autowired
	RewardPointsRepository rewardrepo;

	@Autowired
	UserRepository userRepository;

	private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Override
	public ResponseEntity<String> getRewardHistory(String userid) throws JsonProcessingException {
		LOGGER.info("Entered getRewardHistory() -> Start");
		LOGGER.info("Parameter userid ->" + userid);
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();

		if (null == userid || userid.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is empty or null");
			LOGGER.error("userid is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		List<RewardPoints> userRewards = rewardrepo.findFirst20ByUseridOrderByDateTimeDesc(userid);
		if (userRewards.isEmpty() || userRewards.size() == 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "No rewards available");
			LOGGER.error("No rewards available for userid:" + userid);
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("size of rewards available -> " + userRewards.size());

		return new ResponseEntity<String>(objMapper.writeValueAsString(userRewards), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> donate(DonateRequest donateRequest) throws JsonProcessingException {
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
		if (null == donateRequest.getUserid() || donateRequest.getUserid().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Userid is incorrect");
			LOGGER.error("Userid is incorrect");
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
		String userid = donateRequest.getUserid().trim();
		String amount = donateRequest.getAmount().trim();
		try {
			uid = Integer.parseInt(userid);
			donateamount = Double.parseDouble(amount);
			LOGGER.info("userid=" + uid);
			LOGGER.info("donateamount=" + donateamount);
		} catch (NumberFormatException e) {
			LOGGER.error(e.getMessage(), e);
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Userid or amount is not correct");
			LOGGER.error("Userid or amount is not correct");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (uid <= 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is invalid");
			LOGGER.error("userid is invalid");
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

	@Override
	public ResponseEntity<String> getRewardPoint(String userid) throws JsonProcessingException {
		LOGGER.info("Entered getRewardPoint() -> Start");
		LOGGER.info("userid ->" + userid);
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
			response.put(AppConstant.MESSAGE, "userid is not registered");
			LOGGER.error("userid is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.REWARDPOINT, userInfo.getRewardPoint());
		LOGGER.info("reward point of userid "+userInt+" is "+userInfo.getRewardPoint());
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);



	}

}
