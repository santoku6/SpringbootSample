package com.raxn.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.raxn.entity.Coupons;
import com.raxn.entity.CouponsCashbackTimeline;
import com.raxn.entity.Faq;
import com.raxn.entity.Gsetting;
import com.raxn.entity.Slider;
import com.raxn.entity.SmsTemplates;
import com.raxn.entity.TempRecurringCode;
import com.raxn.repository.CouponCashbackTimelineRepository;
import com.raxn.repository.CouponsRepository;
import com.raxn.repository.FaqRepository;
import com.raxn.repository.GsettingRepository;
import com.raxn.repository.ServiceRepository;
import com.raxn.repository.SliderRepository;
import com.raxn.repository.SmsTemplatesRepository;
import com.raxn.repository.TempRecurringCodeRepository;
import com.raxn.request.model.AddCouponRequest;
import com.raxn.request.model.AddSMSTemplateRequest;
import com.raxn.request.model.EditServiceRequest;
import com.raxn.request.model.FaqRequest;
import com.raxn.service.AddUpdateService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;
import com.raxn.util.service.EmailMobileValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddUpdateServiceImpl implements AddUpdateService {

	Logger LOGGER = LoggerFactory.getLogger(AddUpdateServiceImpl.class);

	private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Value("${slider.path}")
	private String SLIDER_PATH;

	@Autowired
	CouponsRepository couponsRepo;
	@Autowired
	CouponCashbackTimelineRepository cctrRepo;
	@Autowired
	TempRecurringCodeRepository tempCodeRepo;
	@Autowired
	ServiceRepository serviceRepo;
	@Autowired
	SliderRepository sliderRepo;
	@Autowired
	SmsTemplatesRepository smsTempRepo;
	@Autowired
	FaqRepository faqRepo;
	@Autowired
	SmsTemplatesRepository smsTemplateRepo;
	@Autowired
	GsettingRepository gsetRepo;

	private static EmailMobileValidator emailMobileValidator = null;

	static {
		emailMobileValidator = EmailMobileValidator.getInstance();
	}

	@Override
	public ResponseEntity<String> addCoupon(AddCouponRequest addCouponRequest) {
		LOGGER.info("Entered addCoupon() -> Start");
		LOGGER.info("addCouponRequest=" + ReflectionToStringBuilder.toString(addCouponRequest));
		JSONObject response = new JSONObject();
		Coupons couponAdd = new Coupons();

		try {

			if (null == addCouponRequest.getCode() || addCouponRequest.getCode().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "coupon code is empty or null");
				LOGGER.error("coupon code is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getCategory() || addCouponRequest.getCategory().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "coupon category is empty or null");
				LOGGER.error("coupon category is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getDescription() || addCouponRequest.getDescription().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "description is empty or null");
				LOGGER.error("description is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getEligibleAmount() || addCouponRequest.getEligibleAmount().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Eligible amount is empty or null");
				LOGGER.error("Eligible amount is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);

			}
			if (null == addCouponRequest.getType() || addCouponRequest.getType().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "coupon type is empty or null");
				LOGGER.error("coupon type is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getTermsAndConditions()
					|| addCouponRequest.getTermsAndConditions().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Terms & condition is empty or null");
				LOGGER.error("Terms & condition is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getCouponmode() || addCouponRequest.getCouponmode().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mode is empty or null");
				LOGGER.error("Mode is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null != addCouponRequest.getCouponmode() && !addCouponRequest.getCouponmode().isEmpty()) {
				if (!CommonServiceUtil.checkCouponMode(addCouponRequest.getCouponmode().trim())) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "Mode is incorrect");
					LOGGER.error("Mode is incorrect");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
			}
			if (null == addCouponRequest.getUsetime() || addCouponRequest.getUsetime().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "coupon usetime is empty or null");
				LOGGER.error("coupon usetime is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getStartDate() || addCouponRequest.getStartDate().equals("")) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "start date is empty or null");
				LOGGER.error("start date is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getEndDate() || addCouponRequest.getEndDate().equals("")) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "end date is empty or null");
				LOGGER.error("end date is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getStatus() || addCouponRequest.getStatus().equals("")) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "status is empty or null");
				LOGGER.error("status is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			String code = addCouponRequest.getCode().trim().toUpperCase();
			String couponType = addCouponRequest.getType().trim();
			String couponCategory = addCouponRequest.getCategory().trim();
			String couponDescription = addCouponRequest.getDescription().trim();
			String couponUseTime = addCouponRequest.getUsetime().trim();
			String termsAndConditions = addCouponRequest.getTermsAndConditions().trim();
			String couponEligibleAmount = addCouponRequest.getEligibleAmount().trim();
			LocalDate startDate = addCouponRequest.getStartDate();
			LocalDate endDate = addCouponRequest.getEndDate();
			String status = addCouponRequest.getStatus().trim();
			String email = null;
			String mode = addCouponRequest.getCouponmode().trim();
			int instantCashbackWebMode = 0, instantCashbackAppMode = 0;
			int couponUseTimeInt = 0, couponEligibleAmountInt = 0, recurringDuration = 0;
			int minBalance = 0;
			int percentCashbackWebMode = 0, percentCashbackAppMode = 0;
			int maxCashbackAmountWebMode = 0, maxCashbackAmountAppMode = 0;

			String recurringCashbackWebMode = null, recurringCashbackAppMode = null;
			String recurringInterval = null;

			couponUseTimeInt = Integer.parseInt(couponUseTime);
			couponEligibleAmountInt = Integer.parseInt(couponEligibleAmount);

			ResponseEntity<String> responseCodeCheck = checkCodeInDB(code);
			if (responseCodeCheck.getStatusCode() != HttpStatus.OK) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "code already used, pls choose another");
				LOGGER.error("code already used, pls choose another");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			couponAdd.setCode(code);
			couponAdd.setType(couponType);
			couponAdd.setCategory(couponCategory);
			couponAdd.setDescription(couponDescription);
			couponAdd.setUsetime(couponUseTimeInt);
			couponAdd.setTnc(termsAndConditions);
			couponAdd.setEligibleAmount(couponEligibleAmountInt);
			couponAdd.setStartDate(startDate);
			couponAdd.setEndDate(endDate);
			couponAdd.setStatus(status);
			couponAdd.setCouponmode(mode);

			if (null != addCouponRequest.getUseremail() && !addCouponRequest.getUseremail().isEmpty()) {
				email = addCouponRequest.getUseremail().trim();
			}

			if (couponType.equals(AppConstant.INSTANT)) {
				if (0 == addCouponRequest.getInstantCashbackWebMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "instant cashback web should be greater than zero");
					LOGGER.error("instant cashback web should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (0 == addCouponRequest.getInstantCashbackAppMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "instant cashback app should be greater than zero");
					LOGGER.error("instant cashback app should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}

				instantCashbackWebMode = addCouponRequest.getInstantCashbackWebMode();
				instantCashbackAppMode = addCouponRequest.getInstantCashbackAppMode();

				couponAdd.setInstantCashbackWebMode(instantCashbackWebMode);
				couponAdd.setInstantCashbackAppMode(instantCashbackAppMode);

				if (null != email && !email.isEmpty()) {
					couponAdd.setEmail(email);
				}
			} else if (couponType.equals(AppConstant.RECURRING)) {
				if (null == addCouponRequest.getRecurringCashbackWebMode()
						|| addCouponRequest.getRecurringCashbackWebMode().isEmpty()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "recurring cashback web is empty or null");
					LOGGER.error("recurring cashback web is empty or null");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (null == addCouponRequest.getRecurringCashbackAppMode()
						|| addCouponRequest.getRecurringCashbackAppMode().isEmpty()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "recurring cashback app is empty or null");
					LOGGER.error("recurring cashback app is empty or null");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (null == addCouponRequest.getRecurringInterval()
						|| addCouponRequest.getRecurringInterval().isEmpty()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "recurring interval is empty or null");
					LOGGER.error("recurring interval is empty or null");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (null != addCouponRequest.getRecurringInterval()
						&& !addCouponRequest.getRecurringInterval().isEmpty()) {
					if (!CommonServiceUtil.checkCouponInterval(addCouponRequest.getRecurringInterval().trim())) {
						response.put(AppConstant.STATUS, errorStatus);
						response.put(AppConstant.MESSAGE, "recurring interval is incorrect");
						LOGGER.error("recurring interval is incorrect");
						return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
					}
				}

				if (0 == addCouponRequest.getRecurringDuration()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "recurring duration should be greater than zero");
					LOGGER.error("recurring duration should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (0 == addCouponRequest.getMinBalance()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "min balance should be greater than zero");
					LOGGER.error("min balance should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}

				recurringCashbackWebMode = addCouponRequest.getRecurringCashbackWebMode();
				recurringCashbackAppMode = addCouponRequest.getRecurringCashbackAppMode();
				recurringInterval = addCouponRequest.getRecurringInterval();
				recurringDuration = addCouponRequest.getRecurringDuration();
				minBalance = addCouponRequest.getMinBalance();
				LocalDate cbStartDate = null, cbEndDate = null;

				if (recurringInterval.equalsIgnoreCase(AppConstant.REC_MONTHLY) && recurringDuration > 0) {
					cbStartDate = startDate.plusMonths(1);
					cbEndDate = endDate.plusMonths(recurringDuration);
				}
				if (recurringInterval.equalsIgnoreCase(AppConstant.REC_WEEKLY) && recurringDuration > 0) {
					cbStartDate = startDate.plusWeeks(1);
					cbEndDate = endDate.plusWeeks(recurringDuration);
				}
				if (recurringInterval.equalsIgnoreCase(AppConstant.REC_DAILY) && recurringDuration > 0) {
					cbStartDate = startDate.plusDays(1);
					cbEndDate = endDate.plusDays(recurringDuration);
				}

				couponAdd.setCbStartdate(cbStartDate);
				couponAdd.setCbEnddate(cbEndDate);

				couponAdd.setRecurringCashbackWebMode(recurringCashbackWebMode);
				couponAdd.setRecurringCashbackAppMode(recurringCashbackAppMode);
				couponAdd.setRecurringInterval(recurringInterval);
				couponAdd.setRecurringDuration(recurringDuration);
				couponAdd.setMinBalance(minBalance);

			} else if (couponType.equals(AppConstant.PERCENT)) {

				if (0 == addCouponRequest.getPercentCashbackWebMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "percentCashbackWebMode should be greater than zero");
					LOGGER.error("percentCashbackWebMode should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (0 == addCouponRequest.getPercentCashbackAppMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "percentCashbackAppMode should be greater than zero");
					LOGGER.error("percentCashbackAppMode should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (0 == addCouponRequest.getMaxCashbackAmountWebMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "maxCashbackAmountWebMode should be greater than zero");
					LOGGER.error("maxCashbackAmountWebMode should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (0 == addCouponRequest.getMaxCashbackAmountAppMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "maxCashbackAmountAppMode should be greater than zero");
					LOGGER.error("maxCashbackAmountAppMode should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}

				percentCashbackWebMode = addCouponRequest.getPercentCashbackWebMode();
				percentCashbackAppMode = addCouponRequest.getPercentCashbackAppMode();
				maxCashbackAmountWebMode = addCouponRequest.getMaxCashbackAmountWebMode();
				maxCashbackAmountAppMode = addCouponRequest.getMaxCashbackAmountAppMode();

				couponAdd.setPercentCashbackWebMode(percentCashbackWebMode);
				couponAdd.setPercentCashbackAppMode(percentCashbackAppMode);
				couponAdd.setMaxCashbackAmountWebMode(maxCashbackAmountWebMode);
				couponAdd.setMaxCashbackAmountAppMode(maxCashbackAmountAppMode);
			}

			couponsRepo.save(couponAdd);
			LOGGER.info("coupon saved into DB");

		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, e.getMessage());
			LOGGER.error(e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "coupon added to repository");
		LOGGER.info("coupon added to repository");
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> editcoupon(AddCouponRequest addCouponRequest) {
		LOGGER.info("Entered updateCoupon() -> Start");
		LOGGER.info("updateCoupon=" + ReflectionToStringBuilder.toString(addCouponRequest));
		JSONObject response = new JSONObject();

		try {

			if (null == addCouponRequest.getCode() || addCouponRequest.getCode().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "coupon code is empty or null");
				LOGGER.error("coupon code is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getCategory() || addCouponRequest.getCategory().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "coupon category is empty or null");
				LOGGER.error("coupon category is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getDescription() || addCouponRequest.getDescription().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "description is empty or null");
				LOGGER.error("description is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getEligibleAmount() || addCouponRequest.getEligibleAmount().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Eligible amount is empty or null");
				LOGGER.error("Eligible amount is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);

			}
			if (null == addCouponRequest.getType() || addCouponRequest.getType().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "coupon type is empty or null");
				LOGGER.error("coupon type is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getTermsAndConditions()
					|| addCouponRequest.getTermsAndConditions().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Terms & condition is empty or null");
				LOGGER.error("Terms & condition is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getCouponmode() || addCouponRequest.getCouponmode().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mode is empty or null");
				LOGGER.error("Mode is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null != addCouponRequest.getCouponmode() && !addCouponRequest.getCouponmode().isEmpty()) {
				if (!CommonServiceUtil.checkCouponMode(addCouponRequest.getCouponmode().trim())) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "Mode is incorrect");
					LOGGER.error("Mode is incorrect");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
			}
			if (null == addCouponRequest.getUsetime() || addCouponRequest.getUsetime().isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "coupon usetime is empty or null");
				LOGGER.error("coupon usetime is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getStartDate() || addCouponRequest.getStartDate().equals("")) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "start date is empty or null");
				LOGGER.error("start date is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getEndDate() || addCouponRequest.getEndDate().equals("")) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "end date is empty or null");
				LOGGER.error("end date is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == addCouponRequest.getStatus() || addCouponRequest.getStatus().equals("")) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "status is empty or null");
				LOGGER.error("status is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			String code = addCouponRequest.getCode().trim().toUpperCase();
			String couponType = addCouponRequest.getType().trim();
			String couponCategory = addCouponRequest.getCategory().trim();
			String couponDescription = addCouponRequest.getDescription().trim();
			String couponUseTime = addCouponRequest.getUsetime().trim();
			String termsAndConditions = addCouponRequest.getTermsAndConditions().trim();
			String couponEligibleAmount = addCouponRequest.getEligibleAmount().trim();
			LocalDate startDate = addCouponRequest.getStartDate();
			LocalDate endDate = addCouponRequest.getEndDate();
			String status = addCouponRequest.getStatus().trim();
			String email = null;
			String mode = addCouponRequest.getCouponmode().trim();
			int instantCashbackWebMode = 0, instantCashbackAppMode = 0;
			int couponUseTimeInt = 0, couponEligibleAmountInt = 0, recurringDuration = 0;
			int minBalance = 0;
			int percentCashbackWebMode = 0, percentCashbackAppMode = 0;
			int maxCashbackAmountWebMode = 0, maxCashbackAmountAppMode = 0;

			String recurringCashbackWebMode = null, recurringCashbackAppMode = null;
			String recurringInterval = null;

			couponUseTimeInt = Integer.parseInt(couponUseTime);
			couponEligibleAmountInt = Integer.parseInt(couponEligibleAmount);

			List<Coupons> couponObj = couponsRepo.findByCode(code);
			Coupons couponUpdate = couponObj.get(0);

			if (null == couponUpdate) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "coupon does not exist in database");
				LOGGER.error("coupon does not exist in database");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			LocalDate today = LocalDate.now();
			if (today.equals(couponUpdate.getStartDate()) || today.isAfter(couponUpdate.getStartDate())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "change in live/running/expired coupon not allowed");
				LOGGER.error("change in live/running/expired coupon not allowed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			couponUpdate.setCode(code);
			couponUpdate.setType(couponType);
			couponUpdate.setCategory(couponCategory);
			couponUpdate.setDescription(couponDescription);
			couponUpdate.setUsetime(couponUseTimeInt);
			couponUpdate.setTnc(termsAndConditions);
			couponUpdate.setEligibleAmount(couponEligibleAmountInt);
			couponUpdate.setStartDate(startDate);
			couponUpdate.setEndDate(endDate);
			couponUpdate.setStatus(status);
			couponUpdate.setCouponmode(mode);

			if (null != addCouponRequest.getUseremail() && !addCouponRequest.getUseremail().isEmpty()) {
				email = addCouponRequest.getUseremail().trim();
			}

			if (couponType.equals(AppConstant.INSTANT)) {
				if (0 == addCouponRequest.getInstantCashbackWebMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "instant cashback web should be greater than zero");
					LOGGER.error("instant cashback web should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (0 == addCouponRequest.getInstantCashbackAppMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "instant cashback app should be greater than zero");
					LOGGER.error("instant cashback app should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}

				instantCashbackWebMode = addCouponRequest.getInstantCashbackWebMode();
				instantCashbackAppMode = addCouponRequest.getInstantCashbackAppMode();

				couponUpdate.setInstantCashbackWebMode(instantCashbackWebMode);
				couponUpdate.setInstantCashbackAppMode(instantCashbackAppMode);

				if (null != email && !email.isEmpty()) {
					couponUpdate.setEmail(email);
				}
			} else if (couponType.equals(AppConstant.RECURRING)) {
				if (null == addCouponRequest.getRecurringCashbackWebMode()
						|| addCouponRequest.getRecurringCashbackWebMode().isEmpty()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "recurring cashback web is empty or null");
					LOGGER.error("recurring cashback web is empty or null");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (null == addCouponRequest.getRecurringCashbackAppMode()
						|| addCouponRequest.getRecurringCashbackAppMode().isEmpty()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "recurring cashback app is empty or null");
					LOGGER.error("recurring cashback app is empty or null");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (null == addCouponRequest.getRecurringInterval()
						|| addCouponRequest.getRecurringInterval().isEmpty()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "recurring interval is empty or null");
					LOGGER.error("recurring interval is empty or null");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (null != addCouponRequest.getRecurringInterval()
						&& !addCouponRequest.getRecurringInterval().isEmpty()) {
					if (!CommonServiceUtil.checkCouponInterval(addCouponRequest.getRecurringInterval().trim())) {
						response.put(AppConstant.STATUS, errorStatus);
						response.put(AppConstant.MESSAGE, "recurring interval is incorrect");
						LOGGER.error("recurring interval is incorrect");
						return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
					}
				}
				if (0 == addCouponRequest.getRecurringDuration()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "recurring duration should be greater than zero");
					LOGGER.error("recurring duration should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (0 == addCouponRequest.getMinBalance()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "min balance should be greater than zero");
					LOGGER.error("min balance should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}

				recurringCashbackWebMode = addCouponRequest.getRecurringCashbackWebMode();
				recurringCashbackAppMode = addCouponRequest.getRecurringCashbackAppMode();
				recurringInterval = addCouponRequest.getRecurringInterval();
				recurringDuration = addCouponRequest.getRecurringDuration();
				minBalance = addCouponRequest.getMinBalance();
				LocalDate cbStartDate = null, cbEndDate = null;

				if (recurringInterval.equalsIgnoreCase(AppConstant.REC_MONTHLY) && recurringDuration > 0) {
					cbStartDate = startDate.plusMonths(1);
					cbEndDate = endDate.plusMonths(recurringDuration);
				}
				if (recurringInterval.equalsIgnoreCase(AppConstant.REC_WEEKLY) && recurringDuration > 0) {
					cbStartDate = startDate.plusWeeks(1);
					cbEndDate = endDate.plusWeeks(recurringDuration);
				}
				if (recurringInterval.equalsIgnoreCase(AppConstant.REC_DAILY) && recurringDuration > 0) {
					cbStartDate = startDate.plusDays(1);
					cbEndDate = endDate.plusDays(recurringDuration);
				}

				couponUpdate.setCbStartdate(cbStartDate);
				couponUpdate.setCbEnddate(cbEndDate);

				couponUpdate.setRecurringCashbackWebMode(recurringCashbackWebMode);
				couponUpdate.setRecurringCashbackAppMode(recurringCashbackAppMode);
				couponUpdate.setRecurringInterval(recurringInterval);
				couponUpdate.setRecurringDuration(recurringDuration);
				couponUpdate.setMinBalance(minBalance);

			} else if (couponType.equals(AppConstant.PERCENT)) {

				if (0 == addCouponRequest.getPercentCashbackWebMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "percentCashbackWebMode should be greater than zero");
					LOGGER.error("percentCashbackWebMode should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (0 == addCouponRequest.getPercentCashbackAppMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "percentCashbackAppMode should be greater than zero");
					LOGGER.error("percentCashbackAppMode should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (0 == addCouponRequest.getMaxCashbackAmountWebMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "maxCashbackAmountWebMode should be greater than zero");
					LOGGER.error("maxCashbackAmountWebMode should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}
				if (0 == addCouponRequest.getMaxCashbackAmountAppMode()) {
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "maxCashbackAmountAppMode should be greater than zero");
					LOGGER.error("maxCashbackAmountAppMode should be greater than zero");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}

				percentCashbackWebMode = addCouponRequest.getPercentCashbackWebMode();
				percentCashbackAppMode = addCouponRequest.getPercentCashbackAppMode();
				maxCashbackAmountWebMode = addCouponRequest.getMaxCashbackAmountWebMode();
				maxCashbackAmountAppMode = addCouponRequest.getMaxCashbackAmountAppMode();

				couponUpdate.setPercentCashbackWebMode(percentCashbackWebMode);
				couponUpdate.setPercentCashbackAppMode(percentCashbackAppMode);
				couponUpdate.setMaxCashbackAmountWebMode(maxCashbackAmountWebMode);
				couponUpdate.setMaxCashbackAmountAppMode(maxCashbackAmountAppMode);
			}

			couponUpdate.setId(couponUpdate.getId());
			couponsRepo.save(couponUpdate);
			LOGGER.info("coupon updated into DB");
		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, e.getMessage());
			LOGGER.error(e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "coupon updated to repository");
		LOGGER.info("coupon updated to repository");
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> editcouponstatus(String code) {
		LOGGER.info("Entered editcouponstatus() -> Start");
		LOGGER.info("code=" + code);
		JSONObject response = new JSONObject();
		Coupons coupon = null;
		if (null == code || code.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "code is empty or null");
			LOGGER.error("code is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		try {
			String codeReq = code.toUpperCase().trim();

			List<Coupons> couponList = couponsRepo.findByCode(codeReq);
			if (null != couponList && couponList.size() == 1) {
				coupon = couponList.get(0);
			}
			String couponstatus = coupon.getStatus();
			LocalDate startDate = coupon.getStartDate();
			LocalDate today = LocalDate.now();

			if (today.isAfter(startDate) || today.equals(startDate)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "coupon status change is denied for current/expired coupon");
				LOGGER.error("coupon status change is denied for current/expired coupon");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			if (couponstatus.equalsIgnoreCase(AppConstant.STATUS_ACTIVE)) {
				coupon.setStatus(AppConstant.STATUS_INACTIVE);
			}
			if (couponstatus.equalsIgnoreCase(AppConstant.STATUS_INACTIVE)) {
				coupon.setStatus(AppConstant.STATUS_ACTIVE);
			}

			coupon = couponsRepo.save(coupon);

			// response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.COUPON_CODE, coupon.getCode());
			response.put(AppConstant.COUPON_STATUS, coupon.getStatus());

			LOGGER.info("coupon status is changed");
		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "coupon record not found");
			LOGGER.error("coupon record not found, " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	/**
	 * Used to check new code if unique or not only to use when adding new coupon
	 * code in DB
	 */
	@Override
	public ResponseEntity<String> checkCodeInDB(String code) {
		LOGGER.info("Entered checkCodeInDB() -> Start");
		LOGGER.info("code=" + code);
		JSONObject response = new JSONObject();
		boolean isPresent = false;
		List<String> codeList = couponsRepo.findCodes();

		for (int i = 0; i < codeList.size(); i++) {
			if (codeList.get(i).toUpperCase().equalsIgnoreCase(code.toUpperCase())) {
				isPresent = true;
				break;
			}
		}

		if (isPresent) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "code is already used");
			LOGGER.error("code is already used");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		} else {
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "code can be applied ");
			LOGGER.info("Code is not used & can be applied ");
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
		}
	}

	/*
	 * @Scheduled(cron = "0 * 12 * * ?") public void scheduleTask() {
	 * SimpleDateFormat dateFormat = new
	 * SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS"); String strDate =
	 * dateFormat.format(new Date());
	 * System.out.println("Cron job Scheduler: Job running at - " + strDate); }
	 */

	@Scheduled(cron = "10 1 0 * * *")
	public void scheduleDailyActiveReCodeSystem() {
		LOGGER.info("Entered scheduleDailyActiveReCodeSystem() -> Start");

		CouponsCashbackTimeline cct = new CouponsCashbackTimeline();

		LocalDate dd = LocalDate.now();
		LOGGER.info("Today date is " + dd);
		List<String> codelist = couponsRepo.findAllRecurringCodes(dd);

		LOGGER.info("active recurring coupon for today -> " + codelist.size());

		List<CouponsCashbackTimeline> cctlist = cctrRepo.findAll();
		if (null != cctlist && cctlist.size() > 0) {
			cct = cctlist.get(0);
			cct.setId(cct.getId());
		}

		if (dd.getDayOfMonth() == 1) {
			LOGGER.info("today date is 1");
			cct.setDate01(getRecodeString(codelist, cct.getDate01()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate01());
		} else if (dd.getDayOfMonth() == 2) {
			LOGGER.info("today date is 2");
			cct.setDate02(getRecodeString(codelist, cct.getDate02()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate02());
		} else if (dd.getDayOfMonth() == 3) {
			LOGGER.info("today date is 3");
			cct.setDate03(getRecodeString(codelist, cct.getDate03()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate03());
		} else if (dd.getDayOfMonth() == 4) {
			LOGGER.info("today date is 4");
			cct.setDate04(getRecodeString(codelist, cct.getDate04()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate04());
		} else if (dd.getDayOfMonth() == 5) {
			LOGGER.info("today date is 5");
			cct.setDate05(getRecodeString(codelist, cct.getDate05()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate05());
		} else if (dd.getDayOfMonth() == 6) {
			LOGGER.info("today date is 6");
			cct.setDate06(getRecodeString(codelist, cct.getDate06()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate06());
		} else if (dd.getDayOfMonth() == 7) {
			LOGGER.info("today date is 7");
			cct.setDate07(getRecodeString(codelist, cct.getDate07()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate07());
		} else if (dd.getDayOfMonth() == 8) {
			LOGGER.info("today date is 8");
			cct.setDate08(getRecodeString(codelist, cct.getDate08()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate08());
		} else if (dd.getDayOfMonth() == 9) {
			LOGGER.info("today date is 9");
			cct.setDate09(getRecodeString(codelist, cct.getDate09()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate09());
		} else if (dd.getDayOfMonth() == 10) {
			LOGGER.info("today date is 10");
			cct.setDate10(getRecodeString(codelist, cct.getDate10()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate10());
		} else if (dd.getDayOfMonth() == 11) {
			LOGGER.info("today date is 11");
			cct.setDate11(getRecodeString(codelist, cct.getDate11()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate11());
		} else if (dd.getDayOfMonth() == 12) {
			LOGGER.info("today date is 12");
			cct.setDate12(getRecodeString(codelist, cct.getDate12()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate12());
		} else if (dd.getDayOfMonth() == 13) {
			LOGGER.info("today date is 13");
			cct.setDate13(getRecodeString(codelist, cct.getDate13()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate13());
		} else if (dd.getDayOfMonth() == 14) {
			LOGGER.info("today date is 14");
			cct.setDate14(getRecodeString(codelist, cct.getDate14()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate14());
		} else if (dd.getDayOfMonth() == 15) {
			LOGGER.info("today date is 15");
			cct.setDate15(getRecodeString(codelist, cct.getDate15()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate15());
		} else if (dd.getDayOfMonth() == 16) {
			LOGGER.info("today date is 16");
			cct.setDate16(getRecodeString(codelist, cct.getDate16()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate16());
		} else if (dd.getDayOfMonth() == 17) {
			LOGGER.info("today date is 17");
			cct.setDate17(getRecodeString(codelist, cct.getDate17()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate17());
		} else if (dd.getDayOfMonth() == 18) {
			LOGGER.info("today date is 18");
			cct.setDate18(getRecodeString(codelist, cct.getDate18()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate18());
		} else if (dd.getDayOfMonth() == 19) {
			LOGGER.info("today date is 19");
			cct.setDate19(getRecodeString(codelist, cct.getDate19()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate19());
		} else if (dd.getDayOfMonth() == 20) {
			LOGGER.info("today date is 20");
			cct.setDate20(getRecodeString(codelist, cct.getDate20()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate20());
		} else if (dd.getDayOfMonth() == 21) {
			LOGGER.info("today date is 21");
			cct.setDate21(getRecodeString(codelist, cct.getDate21()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate21());
		} else if (dd.getDayOfMonth() == 22) {
			LOGGER.info("today date is 22");
			cct.setDate22(getRecodeString(codelist, cct.getDate22()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate22());
		} else if (dd.getDayOfMonth() == 23) {
			LOGGER.info("today date is 23");
			cct.setDate23(getRecodeString(codelist, cct.getDate23()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate23());
		} else if (dd.getDayOfMonth() == 24) {
			LOGGER.info("today date is 24");
			cct.setDate24(getRecodeString(codelist, cct.getDate24()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate24());
		} else if (dd.getDayOfMonth() == 25) {
			LOGGER.info("today date is 25");
			cct.setDate25(getRecodeString(codelist, cct.getDate25()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate25());
		} else if (dd.getDayOfMonth() == 26) {
			LOGGER.info("today date is 26");
			cct.setDate26(getRecodeString(codelist, cct.getDate26()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate26());
		} else if (dd.getDayOfMonth() == 27) {
			LOGGER.info("today date is 27");
			cct.setDate27(getRecodeString(codelist, cct.getDate27()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate27());
		} else if (dd.getDayOfMonth() == 28) {
			LOGGER.info("today date is 28");
			cct.setDate28(getRecodeString(codelist, cct.getDate28()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate28());
		} else if (dd.getDayOfMonth() == 29) {
			LOGGER.info("today date is 29");
			cct.setDate29(getRecodeString(codelist, cct.getDate29()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate29());
		} else if (dd.getDayOfMonth() == 30) {
			LOGGER.info("today date is 30");
			cct.setDate30(getRecodeString(codelist, cct.getDate30()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate30());
		} else if (dd.getDayOfMonth() == 31) {
			LOGGER.info("today date is 31");
			cct.setDate31(getRecodeString(codelist, cct.getDate31()));
			cctrRepo.save(cct);
			storeDailyReCodeForCashback(dd, cct.getDate31());
		}

		// cctrRepo.save(cct);
		// storeDailyReCodeForCashback(dd, codelist);

	}

	private void storeDailyReCodeForCashback(LocalDate dd, String codeString) {
		LOGGER.info("Entered storeDailyReCodeForCashback() -> Start");
		LOGGER.info("dd=" + dd + ", codeString=" + codeString);
		String allowedCodes = "";

		String[] tempString = codeString.split(",");
		for (String str : tempString) {
			Coupons coupon = couponsRepo.findByCodeSingle(str);
			if (dd.isEqual(coupon.getCbStartdate()) || dd.isEqual(coupon.getCbEnddate())
					|| (dd.isAfter(coupon.getCbStartdate()) && dd.isBefore(coupon.getCbEnddate()))) {
				allowedCodes = allowedCodes + str + ",";
			}
		}
		if (null != allowedCodes && !allowedCodes.isEmpty()) {
			allowedCodes = allowedCodes.substring(0, allowedCodes.length() - 1);
			TempRecurringCode tempCode = new TempRecurringCode();
			tempCode.setCashbackDate(dd);
			tempCode.setCode(allowedCodes);
			tempCodeRepo.save(tempCode);
		}

	}

	private String getRecodeString(List<String> codelist, String codedata) {
		Set<String> setstring = new LinkedHashSet<String>();
		setstring.addAll(codelist);

		if (null != codedata && !codedata.isEmpty()) {
			String[] tempString = codedata.split(",");

			for (String str : tempString) {
				setstring.add(str.trim());
			}
		}

		String joined = String.join(",", setstring);

		return joined;
	}

	@Override
	public ResponseEntity<String> viewcoupons() {
		LOGGER.info("Entered viewcoupons() -> Start");
		LocalDate today = LocalDate.now();
		LOGGER.info("today=" + today);

		List<Coupons> oneMonthCoupons = couponsRepo.findAllCoupons4Display(today.minusMonths(1));

		LOGGER.info("size of all coupons to display -> " + oneMonthCoupons.size());

		Map<Integer, Coupons> result = oneMonthCoupons.stream()
				.collect(Collectors.toMap(Coupons::getId, Function.identity())); // Converts List items to Map

		JSONObject json = new JSONObject(result);

		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);

	}

	@Override
	public ResponseEntity<String> viewservices() {
		LOGGER.info("Entered viewservices() -> Start");
		List<com.raxn.entity.Service> allServices = serviceRepo.findAllByOrderByIdAsc();

		LOGGER.info("size of all services to display -> " + allServices.size());
		return new ResponseEntity<String>(new Gson().toJson(allServices), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> viewsliders() {
		LOGGER.info("Entered viewsliders() -> Start");
		List<Slider> allSliders = sliderRepo.findAllByOrderByIdAsc();

		LOGGER.info("size of all sliders to display -> " + allSliders.size());
		return new ResponseEntity<String>(new Gson().toJson(allSliders), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> viewsmsvendor() {
		LOGGER.info("Entered viewsmsvendor() -> Start");
		List<SmsTemplates> allTemplates = smsTempRepo.findAllByOrderByIdAsc();

		LOGGER.info("size of all sms templates to display -> " + allTemplates.size());
		return new ResponseEntity<String>(new Gson().toJson(allTemplates), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> viewfaq() {
		LOGGER.info("Entered viewfaq() -> Start");
		List<Faq> allFaq = faqRepo.findAllByOrderByIdAsc();

		LOGGER.info("size of all sms templates to display -> " + allFaq.size());
		return new ResponseEntity<String>(new Gson().toJson(allFaq), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> editfaq(FaqRequest faq) {
		LOGGER.info("Entered editfaq() -> Start");
		LOGGER.info("faq -> " + ReflectionToStringBuilder.toString(faq));
		JSONObject response = new JSONObject();
		Faq newObject = null;
		if (null == faq.getId() || faq.getId().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "FAQ id is empty or null");
			LOGGER.error("FAQ id is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == faq.getHeader() || faq.getHeader().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "FAQ header is empty or null");
			LOGGER.error("FAQ header is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		try {
			String id = faq.getId().trim();
			String header = faq.getHeader().trim();

			Faq faqObject = faqRepo.findById(Integer.parseInt(id));

			faqObject.setHeader(header);
			newObject = faqRepo.save(faqObject);
			LOGGER.info("FAQ record is updated");
		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "FAQ record not found");
			LOGGER.error("FAQ record not found, " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(new Gson().toJson(newObject), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> addfaq(FaqRequest faq) {
		LOGGER.info("Entered addfaq() -> Start");
		LOGGER.info("faq =" + ReflectionToStringBuilder.toString(faq));
		JSONObject response = new JSONObject();
		if (null == faq.getId() || faq.getId().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "FAQ id is empty or null");
			LOGGER.error("FAQ id is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == faq.getHeader() || faq.getHeader().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "FAQ header is empty or null");
			LOGGER.error("FAQ header is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		// String id = faq.getId().trim();
		String header = faq.getHeader().trim();

		Faq faqObject = new Faq();
		faqObject.setHeader(header);
		Faq newObject = faqRepo.save(faqObject);
		LOGGER.info("FAQ record is added");

		return new ResponseEntity<String>(new Gson().toJson(newObject), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> deletefaq(String id) {
		LOGGER.info("Entered deletefaq() -> Start");
		LOGGER.info("id=" + id);
		JSONObject response = new JSONObject();
		if (null == id || id.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "id is empty or null");
			LOGGER.error("id is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		try {
			String idReq = id.trim();

			faqRepo.deleteById(Integer.parseInt(idReq));

			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "FAQ record is deleted");
			LOGGER.info("FAQ record is deleted");
		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "FAQ record not found");
			LOGGER.error("FAQ record not found, " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> editsmsvendor(String id, String vendor) {
		LOGGER.info("Entered editsmsvendor() -> Start");
		LOGGER.info("id=" + id + " ,vendor=" + vendor);
		JSONObject response = new JSONObject();
		SmsTemplates smsTemplates = null;
		if (null == id || id.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "id is empty or null");
			LOGGER.error("id is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == vendor || vendor.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "vendor name is empty or null");
			LOGGER.error("vendor name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (!CommonServiceUtil.checkSMSVendor(vendor.trim())) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE,
					"vendor should be either " + AppConstant.VENDOR_BULKSMS + " or " + AppConstant.VENDOR_NATIONALSMS);
			LOGGER.error(
					"vendor should be either " + AppConstant.VENDOR_BULKSMS + " or " + AppConstant.VENDOR_NATIONALSMS);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		try {
			String idReq = id.trim();
			String vendorReq = vendor.trim();

			SmsTemplates smsTemplateObj = smsTemplateRepo.findById(Integer.parseInt(idReq));
			smsTemplateObj.setVendor(vendorReq);
			smsTemplates = smsTemplateRepo.save(smsTemplateObj);

			LOGGER.info("SMS template record is updated");

		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "SMS template record not found");
			LOGGER.error("SMS template record not found, " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(new Gson().toJson(smsTemplates), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> addsmsvendor(AddSMSTemplateRequest smsTemplateRequest) {
		LOGGER.info("Entered addsmsvendor() -> Start");
		LOGGER.info("smsTemplateRequest=" + ReflectionToStringBuilder.toString(smsTemplateRequest));
		JSONObject response = new JSONObject();
		SmsTemplates smsTemplate = new SmsTemplates();
		if (null == smsTemplateRequest.getTemplateId() || smsTemplateRequest.getTemplateId().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "template id is empty or null");
			LOGGER.error("template id is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == smsTemplateRequest.getTemplateName() || smsTemplateRequest.getTemplateName().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "template name is empty or null");
			LOGGER.error("template name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == smsTemplateRequest.getPurpose() || smsTemplateRequest.getPurpose().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "purpose is empty or null");
			LOGGER.error("purpose is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == smsTemplateRequest.getMsgContent() || smsTemplateRequest.getMsgContent().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "msg content is empty or null");
			LOGGER.error("msg content is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == smsTemplateRequest.getVendor() || smsTemplateRequest.getVendor().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "vendor is empty or null");
			LOGGER.error("vendor is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		String templateid = smsTemplateRequest.getTemplateId().trim();
		String templatename = smsTemplateRequest.getTemplateName().trim();
		String purpose = smsTemplateRequest.getPurpose().trim();
		String msgcontent = smsTemplateRequest.getMsgContent().trim();
		String vendor = smsTemplateRequest.getVendor().trim();
		String senderid = AppConstant.SMS_SENDERID;

		SmsTemplates obj1 = smsTemplateRepo.findByTemplateId(templateid);
		if (null != obj1) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "templateid should be unique");
			LOGGER.error("templateid should be unique");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		obj1 = null;
		SmsTemplates obj2 = smsTemplateRepo.findByTemplateName(templatename);
		if (null != obj2) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "templatename should be unique");
			LOGGER.error("templatename should be unique");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		obj2 = null;
		SmsTemplates obj3 = smsTemplateRepo.findByPurpose(purpose);
		if (null != obj3) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "purpose should be unique");
			LOGGER.error("purpose should be unique");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		obj3 = null;
		if (!CommonServiceUtil.checkSMSVendor(vendor)) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE,
					"vendor should be either " + AppConstant.VENDOR_BULKSMS + " or " + AppConstant.VENDOR_NATIONALSMS);
			LOGGER.error(
					"vendor should be either " + AppConstant.VENDOR_BULKSMS + " or " + AppConstant.VENDOR_NATIONALSMS);
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		smsTemplate.setMsgContent(msgcontent);
		smsTemplate.setPurpose(purpose);
		smsTemplate.setSenderId(senderid);
		smsTemplate.setTemplateId(templateid);
		smsTemplate.setTemplateName(templatename);
		smsTemplate.setVendor(vendor);

		smsTemplateRepo.save(smsTemplate);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "SMS template record is added");
		LOGGER.info("SMS template record is added");

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> delsmsvendor(String id) {
		LOGGER.info("Entered delsmsvendor() -> Start");
		LOGGER.info("id=" + id);
		JSONObject response = new JSONObject();
		if (null == id || id.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "id is empty or null");
			LOGGER.error("id is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		try {
			String idReq = id.trim();

			smsTemplateRepo.deleteById(Integer.parseInt(idReq));

			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "SMS Template record is deleted");
			LOGGER.info("SMS Template record is deleted");
		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "SMS Template record not found");
			LOGGER.error("SMS Template record not found, " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> editservice(EditServiceRequest editServiceRequest) {
		LOGGER.info("Entered editservice() -> Start");
		LOGGER.info("editServiceRequest=" + ReflectionToStringBuilder.toString(editServiceRequest));
		JSONObject response = new JSONObject();

		if (null == editServiceRequest.getId() || editServiceRequest.getId().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "id is empty or null");
			LOGGER.error("id is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == editServiceRequest.getFafaicon() || editServiceRequest.getFafaicon().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "fafaicon is empty or null");
			LOGGER.error("fafaicon is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == editServiceRequest.getName() || editServiceRequest.getName().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "service name is empty or null");
			LOGGER.error("service name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == editServiceRequest.getStatus() || editServiceRequest.getStatus().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "status is empty or null");
			LOGGER.error("status is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == editServiceRequest.getUrl() || editServiceRequest.getUrl().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "url is empty or null");
			LOGGER.error("url is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		String idreq = editServiceRequest.getId().trim();
		String fafaiconreq = editServiceRequest.getFafaicon().trim();
		String namereq = editServiceRequest.getName().trim();
		String statusreq = editServiceRequest.getStatus().trim();
		String urlreq = editServiceRequest.getUrl().trim();

		if (null != statusreq && !statusreq.isEmpty()) {
			if (!CommonServiceUtil.checkStatusOneZero(statusreq)) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "status should be either 1 or 0");
				LOGGER.error("status should be either 1 or 0");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		List<String> nameList = serviceRepo.findNames();
		com.raxn.entity.Service service = serviceRepo.findById(Integer.parseInt(idreq));

		if (!namereq.equalsIgnoreCase(service.getName())) {
			boolean isallowedName = checkServiceName(namereq, nameList);
			if (!isallowedName) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "service name should be unique");
				LOGGER.error("service name should be unique");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}
		service.setFafaicon(fafaiconreq);
		service.setName(namereq);
		service.setStatus(statusreq);
		service.setUrl(urlreq);

		service = serviceRepo.save(service);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "Service record is updated");
		LOGGER.info("Service record is updated");

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> editservicestatus(String id) {
		LOGGER.info("Entered editservicestatus() -> Start");
		LOGGER.info("id=" + id);
		JSONObject response = new JSONObject();
		if (null == id || id.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "id is empty or null");
			LOGGER.error("id is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		try {
			String idReq = id.trim();

			com.raxn.entity.Service service = serviceRepo.findById(Integer.parseInt(idReq));
			String serviceStatus = service.getStatus().trim();

			if (serviceStatus.equalsIgnoreCase(AppConstant.STATUS_ONE)) {
				service.setStatus(AppConstant.STATUS_ZERO);
			}
			if (serviceStatus.equalsIgnoreCase(AppConstant.STATUS_ZERO)) {
				service.setStatus(AppConstant.STATUS_ONE);
			}

			service = serviceRepo.save(service);

			response.put("ID", service.getId());
			response.put("Status", service.getStatus());
			LOGGER.info("service status is changed");
		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Service record not found");
			LOGGER.error("Service record not found, " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> addservice(EditServiceRequest editServiceRequest) {
		LOGGER.info("Entered addservice() -> Start");
		LOGGER.info("editServiceRequest =" + ReflectionToStringBuilder.toString(editServiceRequest));
		JSONObject response = new JSONObject();
		if (null == editServiceRequest.getFafaicon() || editServiceRequest.getFafaicon().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "fafaicon is empty or null");
			LOGGER.error("fafaicon is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == editServiceRequest.getName() || editServiceRequest.getName().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "name is empty or null");
			LOGGER.error("name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == editServiceRequest.getStatus() || editServiceRequest.getStatus().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "status is empty or null");
			LOGGER.error("status is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == editServiceRequest.getUrl() || editServiceRequest.getUrl().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "url is empty or null");
			LOGGER.error("url is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		String fafaicon = editServiceRequest.getFafaicon().trim();
		String name = editServiceRequest.getName().trim();
		String status = editServiceRequest.getStatus().trim();
		String url = editServiceRequest.getUrl().trim();

		if (null != status && !status.isEmpty()) {
			if (!CommonServiceUtil.checkStatusOneZero(status.trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "status should be either 1 or 0");
				LOGGER.error("status should be either 1 or 0");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		com.raxn.entity.Service service = new com.raxn.entity.Service();
		service.setFafaicon(fafaicon);
		service.setName(name);
		service.setStatus(status);
		service.setUrl(url);

		service = serviceRepo.save(service);
		LOGGER.info("Service record is added");

		return new ResponseEntity<String>(new Gson().toJson(service), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> delservice(String id) {
		LOGGER.info("Entered delservice() -> Start");
		LOGGER.info("id=" + id);
		JSONObject response = new JSONObject();
		if (null == id || id.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "id is empty or null");
			LOGGER.error("id is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		try {
			String idReq = id.trim();

			serviceRepo.deleteById(Integer.parseInt(idReq));

			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "Service record is deleted");
			LOGGER.info("Service record is deleted");
		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Service record not found");
			LOGGER.error("Service record not found, " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	private boolean checkServiceName(String namereq, List<String> nameList) {
		boolean isNameAllowed = true;
		for (String str : nameList) {
			if (str.trim().equalsIgnoreCase(namereq)) {
				isNameAllowed = false;
				break;
			}
		}
		return isNameAllowed;
	}

	@Override
	public ResponseEntity<String> editslider(MultipartFile mpfile, String imagename, String id, String status) {
		LOGGER.info("Entered addslider() -> Start");
		LOGGER.info("file name -> " + imagename + " ,status = " + status + " ,id=" + id);
		JSONObject response = new JSONObject();
		String[] extnarray = { "png", "jpg", "jpeg", "PNG", "gif" };

		Slider sliderEntity = new Slider();

		if (null == mpfile || mpfile.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "file is empty or null");
			LOGGER.error("file is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == id || id.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "id is empty or null");
			LOGGER.error("id is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == imagename || imagename.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "image name is empty or null");
			LOGGER.error("image name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == status || status.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Email is empty or null");
			LOGGER.error("Email is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != status && !status.isEmpty()) {
			if (!CommonServiceUtil.checkStatusOneZero(status.trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "status is incorrect");
				LOGGER.error("status is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		if (null != mpfile && !mpfile.isEmpty()) {
			String filename = mpfile.getOriginalFilename();
			// LOGGER.info("filename=" + filename);
			int extnPointer = filename.lastIndexOf(".");
			if (extnPointer == -1) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "File without extn is not allowed");
				LOGGER.error("File without extn is not allowed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			String extn = filename.substring(extnPointer + 1);
			// String filenameonly = filename.substring(0, extnPointer);
			boolean checkExtn = Arrays.asList(extnarray).contains(extn);
			if (!checkExtn) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, extn + " is not allowed");
				LOGGER.error(extn + " is not allowed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			try {

				BufferedImage bimg = ImageIO.read(mpfile.getInputStream());
				int width = bimg.getWidth();
				int height = bimg.getHeight();

				LOGGER.info("width=" + width);
				LOGGER.info("height=" + height);
				if (width > 550 || height > 165) {
					LOGGER.error("File dimension should be 550 by 165");
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "File dimension should be 550 by 165");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}

				long bytes = mpfile.getSize();
				double kilobytes = (bytes / 1024);
				double megabytes = (kilobytes / 1024);
				if (megabytes > 0.5) {
					LOGGER.error("File size is more than half MB");
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "File size is more than half MB");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}

				java.nio.file.Path pathTo = Paths.get(SLIDER_PATH);
				// Date dd = java.sql.Date.valueOf(java.time.LocalDate.now());
				String destFileName = imagename + "." + extn;
				java.nio.file.Path destination = Paths.get(pathTo.toString() + "/" + destFileName);
				LOGGER.info("destination = " + destination);
				InputStream in;

				in = mpfile.getInputStream();
				Files.deleteIfExists(destination);
				Files.copy(in, destination);

				sliderEntity = sliderRepo.findById(Integer.parseInt(id.trim()));
				sliderEntity.setImage(destFileName);
				sliderEntity.setStatus(status.trim());
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		}
		sliderEntity = sliderRepo.save(sliderEntity);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "slider added to repository");
		LOGGER.info("slider added to repository");

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> editsliderstatus(String id) {
		LOGGER.info("Entered editsliderstatus() -> Start");
		LOGGER.info("id=" + id);
		JSONObject response = new JSONObject();
		if (null == id || id.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "id is empty or null");
			LOGGER.error("id is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		try {
			String idReq = id.trim();

			Slider slider = sliderRepo.findById(Integer.parseInt(idReq));
			String sliderStatus = slider.getStatus().trim();

			if (sliderStatus.equalsIgnoreCase(AppConstant.STATUS_ONE)) {
				slider.setStatus(AppConstant.STATUS_ZERO);
			}
			if (sliderStatus.equalsIgnoreCase(AppConstant.STATUS_ZERO)) {
				slider.setStatus(AppConstant.STATUS_ONE);
			}

			slider = sliderRepo.save(slider);

			response.put("ID", slider.getId());
			response.put("Status", slider.getStatus());
			LOGGER.info("slider status is changed");
		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "slider record not found");
			LOGGER.error("slider record not found, " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> addslider(MultipartFile mpfile, String imagename, String status) {
		LOGGER.info("Entered addslider() -> Start");
		LOGGER.info("file name -> " + imagename + " ,status = " + status);
		JSONObject response = new JSONObject();
		String[] extnarray = { "png", "jpg", "jpeg", "PNG", "gif" };

		Slider sliderEntity = new Slider();

		if (null == mpfile || mpfile.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "file is empty or null");
			LOGGER.error("file is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == imagename || imagename.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "image name is empty or null");
			LOGGER.error("image name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == status || status.isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Email is empty or null");
			LOGGER.error("Email is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != status && !status.isEmpty()) {
			if (!CommonServiceUtil.checkStatusOneZero(status.trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "status be either 1 or 0");
				LOGGER.error("status be either 1 or 0");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		if (null != mpfile && !mpfile.isEmpty()) {
			String filename = mpfile.getOriginalFilename();
			// LOGGER.info("filename=" + filename);
			int extnPointer = filename.lastIndexOf(".");
			if (extnPointer == -1) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "File without extn is not allowed");
				LOGGER.error("File without extn is not allowed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			String extn = filename.substring(extnPointer + 1);
			// String filenameonly = filename.substring(0, extnPointer);
			boolean checkExtn = Arrays.asList(extnarray).contains(extn);
			if (!checkExtn) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, extn + " is not allowed");
				LOGGER.error(extn + " is not allowed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			try {
				BufferedImage bimg = ImageIO.read(mpfile.getInputStream());
				int width = bimg.getWidth();
				int height = bimg.getHeight();

				LOGGER.info("width=" + width);
				LOGGER.info("height=" + height);
				if (width != 550 || height != 165) {
					LOGGER.error("File dimension should be 550 by 165");
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "File dimension should be 550 by 165");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}

				long bytes = mpfile.getSize();
				double kilobytes = (bytes / 1024);
				double megabytes = (kilobytes / 1024);
				if (megabytes > 0.5) {
					LOGGER.error("File size is more than half MB");
					response.put(AppConstant.STATUS, errorStatus);
					response.put(AppConstant.MESSAGE, "File size is more than half MB");
					return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
				}

				java.nio.file.Path pathTo = Paths.get(SLIDER_PATH);
				// Date dd = java.sql.Date.valueOf(java.time.LocalDate.now());
				String destFileName = imagename + "." + extn;
				java.nio.file.Path destination = Paths.get(pathTo.toString() + "/" + destFileName);
				LOGGER.info("destination = " + destination);
				InputStream in;

				in = mpfile.getInputStream();
				Files.deleteIfExists(destination);
				Files.copy(in, destination);

				sliderEntity.setImage(destFileName);
				sliderEntity.setStatus(status.trim());
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		sliderEntity = sliderRepo.save(sliderEntity);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "slider updated to repository");
		LOGGER.info("slider updated to repository");

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> editfooter(String header, String aboutus, String chooseus, String mission,
			String vision, String address, String email, String mobile, String whatsapp, String noticebar,
			String ppolicy, String refpolicy, String tnc) {

		LOGGER.info("Entered editfooter() -> Start");
		LOGGER.info("Parameter header -> " + header);
		JSONObject response = new JSONObject();
		String headerText = null;

		if (null != header && !header.isEmpty()) {
			headerText = header.trim();
		}

		Gsetting gobj = gsetRepo.findById(1);

		if (null == gobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Footer setting is not found");
			LOGGER.error("Footer setting is not found");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}

		if (null != headerText && !headerText.isEmpty() && headerText.equalsIgnoreCase(AppConstant.FOOTER_ABOUTUS)) {
			if (null == aboutus || aboutus.isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "aboutus is empty or null");
				LOGGER.error("aboutus is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == chooseus || chooseus.isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "chooseus is empty or null");
				LOGGER.error("chooseus is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == mission || mission.isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "mission is empty or null");
				LOGGER.error("mission is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == vision || vision.isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "vision is empty or null");
				LOGGER.error("vision is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}
		if (null != headerText && !headerText.isEmpty() && headerText.equalsIgnoreCase(AppConstant.FOOTER_CONTACTUS)) {
			if (null == address || address.isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "address is empty or null");
				LOGGER.error("address is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == email || email.isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "email is empty or null");
				LOGGER.error("email is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == mobile || mobile.isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "mobile is empty or null");
				LOGGER.error("mobile is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			if (null == whatsapp || whatsapp.isEmpty()) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "whatsapp is empty or null");
				LOGGER.error("whatsapp is empty or null");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		if (null != aboutus && !aboutus.isEmpty()) {
			gobj.setAboutus(aboutus.trim());
		}
		if (null != chooseus && !chooseus.isEmpty()) {
			gobj.setChooseus(chooseus.trim());
		}
		if (null != mission && !mission.isEmpty()) {
			gobj.setOurmission(mission.trim());
		}
		if (null != vision && !vision.isEmpty()) {
			gobj.setOurvision(vision.trim());
		}
		if (null != address && !address.isEmpty()) {
			gobj.setAddress(address.trim());
		}
		if (null != email && !email.isEmpty()) {
			if (!emailMobileValidator.emailValidator(email.trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Email is not valid");
				LOGGER.error("Email is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			gobj.setEmail(email.trim());
		}
		if (null != mobile && !mobile.isEmpty()) {
			if (!emailMobileValidator.mobileValidator(mobile.trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mobile is not valid");
				LOGGER.error("Mobile is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			gobj.setMobile(mobile.trim());
		}
		if (null != whatsapp && !whatsapp.isEmpty()) {
			if (!emailMobileValidator.mobileValidator(whatsapp.trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Whatsapp is not valid");
				LOGGER.error("Whatsapp is not valid");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			gobj.setWhatsapp(whatsapp.trim());
		}
		if (null != noticebar && !noticebar.isEmpty()) {
			gobj.setNoticebar(noticebar.trim());
		}
		if (null != ppolicy && !ppolicy.isEmpty()) {
			gobj.setPrivacypolicy(ppolicy.trim());
		}
		if (null != refpolicy && !refpolicy.isEmpty()) {
			gobj.setRefundpolicy(refpolicy.trim());
		}
		if (null != tnc && !tnc.isEmpty()) {
			gobj.setTnc(tnc.trim());
		}

		try {
			gobj = gsetRepo.save(gobj);
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.MESSAGE, "Footer record is updated");
			LOGGER.info("Footer record is updated");
		} catch (Exception e) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Footer record not found");
			LOGGER.error("Footer record not found, " + e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

}
