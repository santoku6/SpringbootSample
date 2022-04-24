package com.raxn.service.impl;

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
import com.raxn.entity.CouponApplied;
import com.raxn.entity.Coupons;
import com.raxn.entity.User;
import com.raxn.repository.CouponAppliedRepository;
import com.raxn.repository.CouponsRepository;
import com.raxn.repository.UserRepository;
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
	UserRepository userrepo;

	@Autowired
	CouponAppliedRepository couponappliedrepo;

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
			response.put(AppConstant.MESSAGE, "No active coupons found in database");
			LOGGER.error("No active coupons found in database");
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

		List<Coupons> couponObj = couponsrepo.findByCode(code);
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
	public ResponseEntity<String> getAllCouponsOffers() throws JsonProcessingException {
		LOGGER.info("Entered getAllCouponsOffers() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();

		Date dd = java.sql.Date.valueOf(java.time.LocalDate.now());
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

		List<CouponsDisplay> newList = parseCoupons(couponslist);
		LOGGER.info("size of new coupon list -> " + newList.size());
		LOGGER.info("coupon offers are -> " + objMapper.writeValueAsString(newList));
		return new ResponseEntity<String>(new Gson().toJson(newList), HttpStatus.OK);
	}

	private List<CouponsDisplay> parseCoupons(List<Coupons> couponslist) {
		LOGGER.info("Entered parseCoupons() -> Start");
		List<CouponsDisplay> newList = new ArrayList<CouponsDisplay>();
		for (Coupons c : couponslist) {
			String[] c_categories = c.getCouponCategory().split(",");
			for (int i = 0; i < c_categories.length; i++) {
				CouponsDisplay temp = new CouponsDisplay();
				BeanUtils.copyProperties(c, temp);
				temp.setCouponCategory(c_categories[i]);
				newList.add(temp);
			}
		}
		LOGGER.info("returning new coupons list of size:" + newList.size());
		return newList;
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

		Date dd = java.sql.Date.valueOf(java.time.LocalDate.now());
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
		for (Coupons c : couponslist) {
			String[] c_categories = c.getCouponCategory().split(",");
			for (int i = 0; i < c_categories.length; i++) {
				if (c_categories[i].equalsIgnoreCase(category)) {
					CouponsDisplay temp = new CouponsDisplay();
					BeanUtils.copyProperties(c, temp);
					temp.setCouponCategory(c_categories[i]);
					newList.add(temp);
				}
			}
		}
		LOGGER.info("returning new coupons list of size:" + newList.size());
		return newList;
	}

	@Override
	public ResponseEntity<String> checkCouponEligibility(CouponCheckRequest ccRequest) throws JsonProcessingException {
		LOGGER.info("Entered checkCouponEligibility() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();
		LOGGER.info("couponRequest=" + ReflectionToStringBuilder.toString(ccRequest));
		int userInt = 0;
		double amountDbl = 0.0;

		if (null == ccRequest.getUserid() || ccRequest.getUserid().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Userid is null or empty");
			LOGGER.error("Userid is null or incorrect");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == ccRequest.getAmount() || ccRequest.getAmount().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "amount is null or empty");
			LOGGER.error("amount is null or empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);

		}
		String userid = ccRequest.getUserid().trim();
		String amount = ccRequest.getAmount().trim();

		try {
			userInt = Integer.parseInt(userid);
			amountDbl = Double.parseDouble(amount);
		} catch (NumberFormatException e) {
			LOGGER.error(e.getMessage(), e);
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Userid or amount is not correct");
			LOGGER.error("Userid or amount is not correct");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (userInt <= 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "userid is invalid");
			LOGGER.error("userid is invalid");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);

		}
		if (amountDbl > 5000) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "max allowed amount is Rs 5000");
			LOGGER.error("max allowed amount is Rs 5000");
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
			response.put(AppConstant.MESSAGE, "category is empty or null");
			LOGGER.error("category is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != ccRequest.getCategory() && !ccRequest.getCategory().isEmpty()) {
			if (!CommonServiceUtil.checkCouponCategory(ccRequest.getCategory().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "category is incorrect");
				LOGGER.error("category is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
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

		Date dd = java.sql.Date.valueOf(java.time.LocalDate.now());
		LOGGER.info("Today date is " + dd);
		String code = ccRequest.getCode().trim();
		String category = ccRequest.getCategory().trim();
		String mode = ccRequest.getMode().trim();

		List<Coupons> couponslist = couponsrepo.findByCode(code);
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
		Coupons coupon = couponslist.get(0);
		LOGGER.info("Coupon info--" + objMapper.writeValueAsString(coupon));

		User userInfo = userrepo.findById(userInt);
		CouponApplied couponApplied = couponappliedrepo.findByUserid(userid);

		if (null == userInfo) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "user is not registered");
			LOGGER.error("user is not registered");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (dd.before(coupon.getStartDate()) || dd.after(coupon.getEndDate())) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon is expired");
			LOGGER.error("coupon is expired");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != coupon.getMode() && !coupon.getMode().isEmpty() && !mode.equalsIgnoreCase(coupon.getMode())) {
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

		String couponCategory = coupon.getCouponCategory();

		List<String> categorylist = ConvertUtil.convertCategoryToList(couponCategory);

		if (!categorylist.contains(category.toUpperCase())) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "this coupon not allowed for this category");
			LOGGER.error("this coupon not allowed for this category");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		String userAppliedSuccessCouponsList = couponApplied.getSuccessCouponInfo();

		List<String> keyCodeList = ConvertUtil.extractCodesFromAppliedCoupons(userAppliedSuccessCouponsList);
		int appliedCodeCounter = ConvertUtil.getAppliedCodeCount(keyCodeList, code);

		if (keyCodeList.contains(code.toUpperCase())) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon is already used");
			LOGGER.error("coupon is already used");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (appliedCodeCounter >= coupon.getCouponUseTime()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon already used max times");
			LOGGER.error("coupon already used max times");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "coupon can be applied");
		LOGGER.info("coupon can be applied");
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

}
