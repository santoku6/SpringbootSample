package com.raxn.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.raxn.entity.CouponAppliedUser;
import com.raxn.entity.Coupons;
import com.raxn.entity.User;
import com.raxn.repository.CouponAppliedUserRepository;
import com.raxn.repository.CouponsRepository;
import com.raxn.repository.UserRepository;
import com.raxn.request.model.AddMoneyRequest;
import com.raxn.request.model.CouponCheckRequest;
import com.raxn.request.model.CouponsDisplay;
import com.raxn.service.CouponsService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;
import com.raxn.util.service.ConvertUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponsServiceImpl implements CouponsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CouponsServiceImpl.class);

	@Autowired
	CouponsRepository couponsrepo;

	@Autowired
	CouponAppliedUserRepository couponAppliedRepo;

	@Autowired
	UserRepository userrepo;

	@Autowired
	CouponAppliedUserRepository couponappliedrepo;

	private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Override
	public ResponseEntity<String> getAllCoupons() throws JsonProcessingException {
		LOGGER.info("Entered getAllCoupons() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();

		List<Coupons> couponsObj = couponsrepo.findAll();
		if (couponsObj.isEmpty() || couponsObj.size() == 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "No active coupons found");
			LOGGER.error("No active coupons found");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("All active coupons are -> " + objMapper.writeValueAsString(couponsObj));
		return new ResponseEntity<String>(objMapper.writeValueAsString(couponsObj), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> getCouponsByCode(String code) throws JsonProcessingException {
		LOGGER.info("Entered getCouponsByCode() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();

		List<Coupons> couponObj = couponsrepo.findByCode(code.toUpperCase());
		if (couponObj.isEmpty() || couponObj.size() == 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "No coupon found with code " + code + " in database");
			LOGGER.error("No coupon found with code " + code + " in database");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		if (null != couponObj && couponObj.size() > 1) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "ERROR: Multiple coupons found with code " + code + " in database");
			LOGGER.error("Multiple coupons should not found with code " + code + " in database");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}

		Coupons coupon = couponObj.get(0);

		LOGGER.info("Coupon with " + code + " is -> " + objMapper.writeValueAsString(coupon));
		return new ResponseEntity<String>(objMapper.writeValueAsString(coupon), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> getCouponsOffersByCategory(String category) throws JsonProcessingException {
		LOGGER.info("Entered getCouponsOffersByCategory() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();

		if (null == category || category.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Category is empty or null");
			LOGGER.error("Category is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != category && !category.isEmpty()) {
			if (!CommonServiceUtil.checkCouponCategory(category.trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "category is incorrect");
				LOGGER.error("category is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		LocalDate dd = LocalDate.now();
		LOGGER.info("Today date is " + dd);
		List<Coupons> couponslist = couponsrepo.findAllOffers(dd);
		if (couponslist.isEmpty() || couponslist.size() == 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "No coupon offers available");
			LOGGER.error("No coupon offers available");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("size of All coupon offers -> " + couponslist.size());
		LOGGER.info("All coupon offers are -> " + objMapper.writeValueAsString(couponslist));

		List<CouponsDisplay> newList = parseCouponsByCategory(couponslist, category);
		LOGGER.info("size of new coupon list -> " + newList.size());
		LOGGER.info("coupon offers are -> " + objMapper.writeValueAsString(newList));
		return new ResponseEntity<String>(new Gson().toJson(newList), HttpStatus.OK);
	}

	private List<CouponsDisplay> parseCouponsByCategory(List<Coupons> couponslist, String category) {
		LOGGER.info("Entered parseCouponsByCategory() -> Start");
		LOGGER.info("Parameter category=" + category);
		List<CouponsDisplay> newList = new ArrayList<CouponsDisplay>();
		for (Coupons coupon : couponslist) {
			String couponCategory = coupon.getCategory();
			couponCategory = couponCategory.substring(1, couponCategory.length() - 1);	
			String[] c_categories = coupon.getCategory().split(",");
			for (int i = 0; i < c_categories.length; i++) {
				if (c_categories[i].equalsIgnoreCase(category)) {
					CouponsDisplay temp = new CouponsDisplay();
					BeanUtils.copyProperties(coupon, temp);
					newList.add(temp);
				}
			}
		}
		LOGGER.info("returning new coupons list of size:" + newList.size());
		return newList;
	}

	/**
	 * check coupon eligibility for a user
	 */
	@Override
	public ResponseEntity<String> checkCouponEligibility(CouponCheckRequest ccRequest) throws JsonProcessingException {
		LOGGER.info("Entered checkCouponEligibility() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();
		LOGGER.info("couponRequest=" + ReflectionToStringBuilder.toString(ccRequest));
		double amountDbl = 0.0;

		if (null == ccRequest.getUsername() || ccRequest.getUsername().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Username is null or empty");
			LOGGER.error("Username is null or incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == ccRequest.getAmount() || ccRequest.getAmount().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "amount is null or empty");
			LOGGER.error("amount is null or empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);

		}
		String username = ccRequest.getUsername().trim();
		String amount = ccRequest.getAmount().trim();

		try {
			amountDbl = Double.parseDouble(amount);
		} catch (NumberFormatException e) {
			LOGGER.error(e.getMessage());
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "amount is not correct");
			LOGGER.error("amount is not correct");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null == ccRequest.getCode() || ccRequest.getCode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "code is empty or null");
			LOGGER.error("code is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == ccRequest.getCategory() || ccRequest.getCategory().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon category is empty or null");
			LOGGER.error("coupon category is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null != ccRequest.getCategory() && !ccRequest.getCategory().isEmpty()
				&& !(ccRequest.getCategory().equalsIgnoreCase(AppConstant.UTILITY)
						|| ccRequest.getCategory().equalsIgnoreCase(AppConstant.RECHARGE)
						|| ccRequest.getCategory().equalsIgnoreCase(AppConstant.GIFTCARD)
						|| ccRequest.getCategory().equalsIgnoreCase(AppConstant.WALLET))) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon category is incorrect");
			LOGGER.error("coupon category is incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null == ccRequest.getMode() || ccRequest.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "mode is empty or null");
			LOGGER.error("mode is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != ccRequest.getMode() && !ccRequest.getMode().isEmpty()) {
			if (!CommonServiceUtil.checkMode(ccRequest.getMode().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "mode is incorrect");
				LOGGER.error("mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		// Date dd = java.sql.Date.valueOf(java.time.LocalDate.now());
		LocalDate dd = LocalDate.now();
		LOGGER.info("Today date is " + dd);
		String code = ccRequest.getCode().trim();
		String category = ccRequest.getCategory().trim();
		String mode = ccRequest.getMode().trim();

		if (category.equalsIgnoreCase(AppConstant.WALLET)) {
			if (amountDbl > 5000) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "max allowed amount is Rs 5000");
				LOGGER.error("max allowed amount is Rs 5000");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);

			}
		}

		List<Coupons> couponslist = couponsrepo.findByCode(code.toUpperCase());
		if (couponslist.isEmpty() || couponslist.size() == 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon is invalid");
			LOGGER.error("coupon is invalid");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		if (null != couponslist && couponslist.size() > 1) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "multiple coupons found with code");
			LOGGER.error("multiple coupons found with code");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		// get coupon info from DB
		Coupons coupon = couponslist.get(0);
		LOGGER.info("Coupon info--" + objMapper.writeValueAsString(coupon));

		// get userinfo from DB
		User userInfo = userrepo.findByUsername(username);
		// get all codes used by user from DB
		CouponAppliedUser couponApplied = couponappliedrepo.findByUsername(username);

		if (null == userInfo) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "user is not registered");
			LOGGER.error("user is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (coupon.getStatus().equalsIgnoreCase("inactive")) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon is not active");
			LOGGER.error("coupon is not active");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (dd.isBefore(coupon.getStartDate()) || dd.isAfter(coupon.getEndDate())) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon is expired");
			LOGGER.error("coupon is expired");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != coupon.getCouponmode() && !coupon.getCouponmode().isEmpty()
				&& !(mode.equalsIgnoreCase(coupon.getCouponmode())
						|| coupon.getCouponmode().equalsIgnoreCase("both"))) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon is not applicable on this platform");
			LOGGER.error("coupon is not applicable on this platform");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (amountDbl < coupon.getEligibleAmount()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon is not applicable for this amount");
			LOGGER.error("coupon is not applicable for this amount");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		// user specific coupon
		if (null != coupon.getEmail() && !coupon.getEmail().isEmpty()
				&& !userInfo.getEmail().equalsIgnoreCase(coupon.getEmail())) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon is not linked with your account");
			LOGGER.error("coupon is not linked with your account");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		userInfo = null;
		String couponCategory = coupon.getCategory();

		List<String> categorylist = ConvertUtil.convertCategoryToList(couponCategory);

		if (!categorylist.contains(category.toUpperCase())) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "this coupon not allowed for this category");
			LOGGER.error("this coupon not allowed for this category");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		String userAppliedSuccessCoupons = couponApplied.getSuccessallcouponsinfo();

		List<String> keyCodeList = ConvertUtil.extractCodesFromUserAppliedCoupons(userAppliedSuccessCoupons);
		int appliedCodeCounter = ConvertUtil.getAppliedCodeCount(keyCodeList, code);

		if (keyCodeList.contains(code.toUpperCase())) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon is already used by user");
			LOGGER.error("coupon is already used by user");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (appliedCodeCounter >= coupon.getUsetime()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon already used max times by user");
			LOGGER.error("coupon already used max times by user");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "this coupon can be applied");
		LOGGER.info("this coupon can be applied");
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	// call below method from a thread
	private boolean applyAndSaveCouponInfoForUser(AddMoneyRequest addMoneyRequest) {
		LOGGER.info("Entered applyAndSaveCouponInfoForUser() -> Start");
		boolean codeRemark = false;

		try {
			// code, username, amount, category:wallet, web/app,
			// Step1: call coupon check eligibility and save status in a variable

			String code = addMoneyRequest.getCode();
			String username = addMoneyRequest.getUsername();
			//int amount = addMoneyRequest.getAmount();
			String category = addMoneyRequest.getCategory();
			String mode = addMoneyRequest.getMode();
			//String orderid = addMoneyRequest.getOrderid();

			// code1=datetime1,web,orderid1#code2=datetime2,web,orderid2#...etc
			//String successCouponText = code + "=" + new Date() + "," + mode + "," + orderid;
			String successCouponText = code + "=" + new Date() + "," + mode;

			CouponAppliedUser cpApplied = couponAppliedRepo.findByUsername(username);
			if (null == cpApplied) {
				cpApplied = new CouponAppliedUser();
				cpApplied.setUsername(username);
				cpApplied.setSuccessallcouponsinfo(successCouponText);
			} else {
				String successCodeInfo = cpApplied.getSuccessallcouponsinfo();
				successCouponText = successCodeInfo + "#" + successCouponText;
				cpApplied.setSuccessallcouponsinfo(successCouponText);
				cpApplied.setId(cpApplied.getId());
			}
			couponAppliedRepo.save(cpApplied);
		} catch (Exception e) {
			LOGGER.error("Error is->" + e.getMessage());
		}
		return codeRemark;
	}

	// after above method
	// set cashback
	private void setOrProcessCashback(String username, String code) {
		LOGGER.info("Entered setOrProcessCashback() -> Start");

		couponsrepo.findByCode(code.toUpperCase());

		// deposit instant cashback to user wallet
	}

}
