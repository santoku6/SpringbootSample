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
import com.raxn.request.model.RechargeMobileRequest;
import com.raxn.service.RechargeMobileService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;
import com.raxn.util.service.PlansInfoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RechargeMobileServiceImpl implements RechargeMobileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RechargeMobileServiceImpl.class);

	@Autowired
	PlansInfoService plansinfoService;

	// private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Override
	public ResponseEntity<String> rechargePrepaidMobile(RechargeMobileRequest rmobilerequest)
			throws JsonProcessingException {
		LOGGER.info("Entered rechargeMobile() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		LOGGER.info("Parameter mplanRequest -> " + objMapper.writeValueAsString(rmobilerequest));

		JSONObject response = new JSONObject();
		String operator = null, circle = null, userid = null, mode = null, mobile = null, rechargeamount = null;
		String amountFromWallet = null, amountFromPGateway = null, code = null;
		double rechamount = 0.0, walletAmountForRecharge = 0.0, pgAmountForRecharge = 0.0;
		String rechRefNo = "RM";// prefix RM
		String servicecategory = "";

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
		if (null != rmobilerequest.getServicecategory() && !rmobilerequest.getServicecategory().isEmpty()) {
			servicecategory = rmobilerequest.getServicecategory().trim();
		}
		if (servicecategory.equalsIgnoreCase("recharge")) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "service category is not correct");
			LOGGER.error("service category is not correct");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		operator = rmobilerequest.getOperatorcode().trim();
		circle = rmobilerequest.getCirclecode().trim();
		userid = rmobilerequest.getUserid().trim();
		mode = rmobilerequest.getMode().trim();
		mobile = rmobilerequest.getMobile().trim();
		rechargeamount = rmobilerequest.getRechargeamount().trim();
		rechamount = Double.parseDouble(rechargeamount);// check if amount is valid against op & circle
		rechRefNo = rechRefNo + CommonServiceUtil.getOurRefNo();// AB,PA,MT

		if (null != rmobilerequest.getAmountfromwallet() && !rmobilerequest.getAmountfromwallet().isEmpty()) {
			amountFromWallet = rmobilerequest.getAmountfromwallet().trim();
			walletAmountForRecharge = Double.parseDouble(amountFromWallet);

		}
		if (null != rmobilerequest.getAmountfrompaygateway() && !rmobilerequest.getAmountfrompaygateway().isEmpty()) {
			amountFromPGateway = rmobilerequest.getAmountfrompaygateway().trim();
			pgAmountForRecharge = Double.parseDouble(amountFromPGateway);
		}

		// Step1: Recharge amount validity
		String resultPlans = plansinfoService.mobilePlans(operator, circle);
		boolean checkAmountInPlans = CommonServiceUtil.matchAmountInPlans(resultPlans,
				Integer.parseInt(rechargeamount));
		if (!checkAmountInPlans) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "recharge amount is invalid");
			LOGGER.error("recharge amount is invalid");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		// step
		if (null != rmobilerequest.getCode() && !rmobilerequest.getCode().isEmpty()) {
			code = rmobilerequest.getCode().trim();
		}

		if (null != code && !code.isEmpty() && walletAmountForRecharge > 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "wallet amount can not be used with code");
			LOGGER.error("wallet amount can not be used with code");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != code && !code.isEmpty() && pgAmountForRecharge < rechamount) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "partial amount of payment gateway not allowed with code");
			LOGGER.error("partial amount of payment gateway not allowed with code");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		// Step2: total of walletAmount plus pgamount equality with rechargeamount

		if ((null == code || code.isEmpty()) && (rechamount != walletAmountForRecharge + pgAmountForRecharge)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "total of wallet & PG amount not matching with recharge amount");
			LOGGER.error("total of wallet & PG amount not matching with recharge amount");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		// Step3: coupon code validity - against recharge category

		// TODO coupon code verification against category & paymentgateway
		// TODO user wallet amount verification
		// TODO call pg gateway if rechamount less than wallet amount
		// TODO pgresponse with responseid verification with pgamount
		// TODO insert into recharge table
		// TODO inset into wallet table
		// TODO insert into pgateway table
		// TODO insert into coupon used table
		// TODO inset into coupon benefit/cashback table
		// TODO insert into pgateway table

		// TODO pgresponse with responseid verification with pgamount
		// TODO send sms/email after recharge

		return null;
	}

}
