package com.raxn.service.impl;

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
import com.raxn.request.model.ApiMobilePlanRequest;
import com.raxn.request.model.RechargeMobileRequest;
import com.raxn.service.ApiServiceService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;
import com.raxn.util.service.PlansInfoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiServiceServiceImpl implements ApiServiceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiServiceServiceImpl.class);

	@Autowired
	PlansInfoService plansinfoService;

	// private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Override
	public ResponseEntity<String> findOperator(String mobile) throws JsonProcessingException {
		LOGGER.info("Entered findOperator() -> Start");
		LOGGER.info("Parameter mobile -> " + mobile);

		JSONObject response = new JSONObject();

		if (!CommonServiceUtil.mobileChecker(mobile)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "mobile is not valid");
			LOGGER.error("mobile is not valid");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		String result = plansinfoService.findOperator(mobile);
		LOGGER.info("result -> " + result);
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> getMobilePlans(ApiMobilePlanRequest mplanRequest) throws JsonProcessingException {
		LOGGER.info("Entered getMobilePlans() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		LOGGER.info("Parameter mplanRequest -> " + objMapper.writeValueAsString(mplanRequest));

		JSONObject response = new JSONObject();
		String operator = null, circle = null;

		if (null == mplanRequest.getOperatorcode() || mplanRequest.getOperatorcode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "operator code is null or empty");
			LOGGER.error("operator code is null or empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == mplanRequest.getCirclecode() || mplanRequest.getCirclecode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "circle code is null or empty");
			LOGGER.error("circle code is null or empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		operator = mplanRequest.getOperatorcode().trim().toUpperCase();
		circle = mplanRequest.getCirclecode().trim().toUpperCase();

		String result = plansinfoService.mobilePlans(operator, circle);
		LOGGER.info("result -> " + result);
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> rechargeMobile(RechargeMobileRequest rmobilerequest) throws JsonProcessingException {
		LOGGER.info("Entered rechargeMobile() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		LOGGER.info("Parameter mplanRequest -> " + objMapper.writeValueAsString(rmobilerequest));

		JSONObject response = new JSONObject();
		String operator = null, circle = null, userid = null, mode = null, mobile = null, rechargeamount = null;
		String amountFromWallet = null, amountFromPGateway = null, code = null;
		double rechamount = 0.0, walletamount = 0.0, pgamount = 0.0;

		if (null == rmobilerequest.getOperatorcode() || rmobilerequest.getOperatorcode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "operator code is null or empty");
			LOGGER.error("operator code is null or empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == rmobilerequest.getCirclecode() || rmobilerequest.getCirclecode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "circle code is null or empty");
			LOGGER.error("circle code is null or empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == rmobilerequest.getUserid() || rmobilerequest.getUserid().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is null or empty");
			LOGGER.error("userid is null or empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == rmobilerequest.getMode() || rmobilerequest.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "mode is null or empty");
			LOGGER.error("mode is null or empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == rmobilerequest.getMobile() || rmobilerequest.getMobile().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "mobile number is null or empty");
			LOGGER.error("mobile number is null or empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == rmobilerequest.getRechargeamount() || rmobilerequest.getRechargeamount().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "recharge amount is null or empty");
			LOGGER.error("recharge amount is null or empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		operator = rmobilerequest.getOperatorcode().trim();
		circle = rmobilerequest.getCirclecode().trim();
		userid = rmobilerequest.getUserid().trim();
		mode = rmobilerequest.getMode().trim();
		mobile = rmobilerequest.getMobile().trim();
		rechargeamount = rmobilerequest.getRechargeamount().trim();
		rechamount = Double.parseDouble(rechargeamount);

		if (null != rmobilerequest.getAmountfromwallet() && !rmobilerequest.getAmountfromwallet().isEmpty()) {
			amountFromWallet = rmobilerequest.getAmountfromwallet().trim();
			walletamount = Double.parseDouble(amountFromWallet);

		}
		if (null != rmobilerequest.getAmountfrompaygateway() && !rmobilerequest.getAmountfrompaygateway().isEmpty()) {
			amountFromPGateway = rmobilerequest.getAmountfrompaygateway().trim();
			pgamount = Double.parseDouble(amountFromPGateway);
		}
		if (null != rmobilerequest.getCode() && !rmobilerequest.getCode().isEmpty()) {
			code = rmobilerequest.getCode().trim();
		}

		if (rechamount != walletamount + pgamount) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "total of wallet & PG amount not matching with recharge amount");
			LOGGER.error("total of wallet & PG amount not matching with recharge amount");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		//TODO code verification
		//TODO user wallet amount verification
		//TODO call pg gateway if rechamount less than wallet amount
		//TODO pgresponse with responseid verification with pgamount
		//TODO insert into recharge table
		//TODO inset into wallet table
		//TODO insert into pgateway table
		//TODO insert into coupon used table
		//TODO inset into coupon benefit/cashback table
		//TODO insert into pgateway table
		
		//TODO pgresponse with responseid verification with pgamount
		//TODO send sms/email after recharge

		return null;
	}

}
