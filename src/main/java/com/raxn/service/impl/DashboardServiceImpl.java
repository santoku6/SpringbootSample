package com.raxn.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.raxn.entity.Coupons;
import com.raxn.entity.Faq;
import com.raxn.entity.FaqContent;
import com.raxn.entity.Gsetting;
import com.raxn.entity.RewardPoints;
import com.raxn.entity.Slider;
import com.raxn.entity.Suggestion;
import com.raxn.repository.CouponsRepository;
import com.raxn.repository.FaqContentRepository;
import com.raxn.repository.FaqRepository;
import com.raxn.repository.GsettingRepository;
import com.raxn.repository.RewardPointsRepository;
import com.raxn.repository.ServiceRepository;
import com.raxn.repository.SliderRepository;
import com.raxn.repository.SuggestionRepository;
import com.raxn.request.model.CouponsDisplay;
import com.raxn.service.DashboardService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;
import com.raxn.util.service.EmailSenderService;
import com.raxn.util.service.SMSSenderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DashboardServiceImpl implements DashboardService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardServiceImpl.class);

	@Autowired
	GsettingRepository gsetrepo;

	@Autowired
	CouponsRepository couponsrepo;

	@Autowired
	SuggestionRepository suggestRepo;

	@Autowired
	RewardPointsRepository rewardrepo;

	@Autowired
	FaqContentRepository faqcontentrepo;

	@Autowired
	SliderRepository sliderrepo;

	@Autowired
	ServiceRepository servicerepo;

	@Autowired
	FaqRepository faqrepo;

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

	@Value("${suggestion.path}")
	private String SUGGESTION_PATH;

	@Value("${slider.path}")
	private String SLIDER_PATH;

	private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Override
	public ResponseEntity<String> getFooterSettingByName(String name) throws JsonProcessingException {
		LOGGER.info("Entered getFooterSettingByName() -> Start");
		LOGGER.info("Parameter name -> " + name);
		JSONObject response = new JSONObject();

		name = name.trim();

		Gsetting gobj = gsetrepo.findById(1);

		if (null == gobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Footer setting is not found");
			LOGGER.error("Footer setting is not found");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}

		if (name.equalsIgnoreCase(AppConstant.FOOTER_ABOUTUS)) {
			String value = "";
			value = gobj.getAboutus();
			if (null == value || value.isEmpty()) {
				response.put(AppConstant.MESSAGE, "footer is not set");
			}
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.FOOTER_ABOUTUS, value);
			response.put(AppConstant.FOOTER_CHOOSEUS, gobj.getChooseus());
			response.put(AppConstant.FOOTER_MISSION, gobj.getOurmission());
			response.put(AppConstant.FOOTER_VISION, gobj.getOurvision());
			// LOGGER.info("Footer setting:: " + name + " is found");
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

		} else if (name.equalsIgnoreCase(AppConstant.FOOTER_CONTACTUS)) {
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.FOOTER_CONTACTUS_ADDRESS, gobj.getAddress());
			response.put(AppConstant.FOOTER_CONTACTUS_EMAIL, gobj.getEmail());
			response.put(AppConstant.FOOTER_CONTACTUS_MOBILE, gobj.getMobile());
			response.put(AppConstant.FOOTER_CONTACTUS_WHATSAPP, gobj.getWhatsapp());
			// LOGGER.info("Footer setting:: " + name + " is found");
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

		} else if (name.equalsIgnoreCase(AppConstant.FOOTER_NOTICEBAR)) {
			String value = "";
			value = gobj.getNoticebar();
			if (null == value || value.isEmpty()) {
				response.put(AppConstant.MESSAGE, "footer is not set");
			}
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.FOOTER_NOTICEBAR, value);
			// LOGGER.info("Footer setting:: " + name + " is found");
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

		} else if (name.equalsIgnoreCase(AppConstant.FOOTER_PRVPOLICY)) {
			String value = "";
			value = gobj.getPrivacypolicy();
			if (null == value || value.isEmpty()) {
				response.put(AppConstant.MESSAGE, "footer is not set");
			}
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.FOOTER_PRVPOLICY, value);
			// LOGGER.info("Footer setting:: " + name + " is found");
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

		} else if (name.equalsIgnoreCase(AppConstant.FOOTER_RFNDPOLICY)) {
			String value = "";
			value = gobj.getRefundpolicy();
			if (null == value || value.isEmpty()) {
				response.put(AppConstant.MESSAGE, "footer is not set");
			}
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.FOOTER_RFNDPOLICY, value);
			// LOGGER.info("Footer setting:: " + name + " is found");
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

		} else if (name.equalsIgnoreCase(AppConstant.FOOTER_TNC)) {
			String value = "";
			value = gobj.getTnc();
			if (null == value || value.isEmpty()) {
				response.put(AppConstant.MESSAGE, "footer is not set");
			}
			response.put(AppConstant.STATUS, successStatus);
			response.put(AppConstant.FOOTER_TNC, value);
			// LOGGER.info("Footer setting:: " + name + " is found");
			return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

		}
		response.put(AppConstant.STATUS, errorStatus);
		response.put(AppConstant.MESSAGE, "Footer setting with name " + name + " is not found");
		LOGGER.error("Footer setting with name " + name + " is not found");
		return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<String> getOffers() throws JsonProcessingException {
		LOGGER.info("Entered getAllCouponsOffers() -> Start");
		JSONObject response = new JSONObject();
		JSONObject result = null;

		// Date dd = java.sql.Date.valueOf(java.time.LocalDate.now());
		LocalDate dd = LocalDate.now();
		LOGGER.info("Today date is " + dd);
		List<Coupons> couponslist = couponsrepo.findAllOffers(dd);
		if (couponslist.isEmpty() || couponslist.size() == 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "No coupon offers available");
			LOGGER.info("No coupon offers available");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}

		result = parseCoupons(couponslist);
		couponslist = null;
		// LOGGER.info("size of all coupon list -> " + newList.size());
		return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
	}

	private JSONObject parseCoupons(List<Coupons> couponslist) {
		LOGGER.info("Entered parseCoupons() -> Start");
		List<CouponsDisplay> listWallet = new ArrayList<CouponsDisplay>();
		List<CouponsDisplay> listRecharge = new ArrayList<CouponsDisplay>();
		List<CouponsDisplay> listUtility = new ArrayList<CouponsDisplay>();
		List<CouponsDisplay> listGiftcard = new ArrayList<CouponsDisplay>();
		Map<String, List<CouponsDisplay>> mapCoupons = new HashMap<String, List<CouponsDisplay>>();
		JSONObject json = null;
		try {
			for (Coupons coupon : couponslist) {
				String couponCategory = coupon.getCategory();

				if (!couponCategory.contains(",")) {
					CouponsDisplay temp = new CouponsDisplay();
					temp = setValuesInBean(coupon, temp);
					temp.setCategory(couponCategory);
					if (couponCategory.equalsIgnoreCase(AppConstant.WALLET)) {
						listWallet.add(temp);
					}
					if (couponCategory.equalsIgnoreCase(AppConstant.RECHARGE)) {
						listRecharge.add(temp);
					}
					if (couponCategory.equalsIgnoreCase(AppConstant.UTILITY)) {
						listUtility.add(temp);
					}
					if (couponCategory.equalsIgnoreCase(AppConstant.GIFTCARD)) {
						listGiftcard.add(temp);
					}
				}
				if (couponCategory.contains(",")) {
					couponCategory = couponCategory.substring(1, couponCategory.length() - 1);
					String[] c_categories = couponCategory.split(",");

					for (int i = 0; i < c_categories.length; i++) {
						CouponsDisplay temp1 = new CouponsDisplay();
						temp1 = setValuesInBean(coupon, temp1);
						temp1.setCategory(c_categories[i]);
						if (c_categories[i].equalsIgnoreCase(AppConstant.WALLET)) {
							listWallet.add(temp1);
						}
						if (c_categories[i].equalsIgnoreCase(AppConstant.RECHARGE)) {
							listRecharge.add(temp1);
						}
						if (c_categories[i].equalsIgnoreCase(AppConstant.UTILITY)) {
							listUtility.add(temp1);
						}
						if (c_categories[i].equalsIgnoreCase(AppConstant.GIFTCARD)) {
							listGiftcard.add(temp1);
						}
					}
				}

			}

			mapCoupons.put(AppConstant.WALLET, listWallet);
			mapCoupons.put(AppConstant.RECHARGE, listRecharge);
			mapCoupons.put(AppConstant.UTILITY, listUtility);
			mapCoupons.put(AppConstant.GIFTCARD, listGiftcard);

			json = new JSONObject(mapCoupons);

		} catch (Exception e) {
			LOGGER.error("Error is: " + e.getMessage(), e);
		}
		return json;
	}

	private CouponsDisplay setValuesInBean(Coupons coupon, CouponsDisplay temp) {
		if (coupon.getType().equalsIgnoreCase(AppConstant.INSTANT)) {
			temp.setCode(coupon.getCode());
			temp.setInstantCashbackWebMode(coupon.getInstantCashbackWebMode());
			temp.setInstantCashbackAppMode(coupon.getInstantCashbackAppMode());
			temp.setUsetime(coupon.getUsetime());
			temp.setDescription(coupon.getDescription());
			temp.setTnc(coupon.getTnc());
			temp.setEndDate(coupon.getEndDate());
		}
		if (coupon.getType().equalsIgnoreCase(AppConstant.RECURRING)) {
			temp.setCode(coupon.getCode());
			temp.setRecurringCashbackWebMode(coupon.getRecurringCashbackWebMode());
			temp.setRecurringCashbackAppMode(coupon.getRecurringCashbackAppMode());
			temp.setUsetime(coupon.getUsetime());
			temp.setDescription(coupon.getDescription());
			temp.setTnc(coupon.getTnc());
			temp.setEndDate(coupon.getEndDate());
		}
		if (coupon.getType().equalsIgnoreCase(AppConstant.PERCENT)) {
			temp.setCode(coupon.getCode());
			temp.setPercentCashbackWebMode(coupon.getPercentCashbackWebMode());
			temp.setPercentCashbackAppMode(coupon.getPercentCashbackAppMode());
			temp.setUsetime(coupon.getUsetime());
			temp.setDescription(coupon.getDescription());
			temp.setTnc(coupon.getTnc());
			temp.setMaxCashbackAmountWebMode(coupon.getMaxCashbackAmountWebMode());
			temp.setMaxCashbackAmountAppMode(coupon.getMaxCashbackAmountAppMode());
			temp.setEndDate(coupon.getEndDate());
		}
		return temp;
	}

	@Override
	public ResponseEntity<String> postSuggestions(MultipartFile mpfile, String name, String email, String mobile,
			String messagetype, String query, String mode, String username) {
		LOGGER.info("Entered postSuggestions() -> Start");
		LOGGER.info("Name -> " + name + " ,Email = " + email + " ,mobile=" + mobile + " ,messagetype=" + messagetype
				+ " ,query=" + query + " ,mode=" + mode + " ,username=" + username);
		JSONObject response = new JSONObject();
		String[] extnarray = { "pdf", "png", "jpg", "jpeg", "PNG" };

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
		if (null != mode && !mode.isEmpty()) {
			if (!CommonServiceUtil.checkMode(mode.trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "Mode is incorrect");
				LOGGER.error("Mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		if (null == username || username.isEmpty()) {
			suggestionEntity.setUsername("anonymous");
		} else {
			suggestionEntity.setUsername(username.trim());
		}

		suggestionEntity.setName(name.trim());
		suggestionEntity.setEmail(email.trim());
		suggestionEntity.setMobile(mobile.trim());
		suggestionEntity.setMessageType(messagetype.trim());
		suggestionEntity.setMessage(query.trim());
		suggestionEntity.setMode(mode.trim());
		suggestionEntity.setRefno(referenceNumber.trim());
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
			String filenameonly = filename.substring(0, extnPointer);
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

			java.nio.file.Path pathTo = Paths.get(SUGGESTION_PATH);
			Date dd = java.sql.Date.valueOf(java.time.LocalDate.now());
			String destFileName = mobile + "_" + filenameonly + "_" + dd + "." + extn;
			// java.nio.file.Path pathTo = Paths.get("/home/ec2-user/santosh/");
			java.nio.file.Path destination = Paths.get(pathTo.toString() + "/" + destFileName);
			LOGGER.info("destination = " + destination);
			InputStream in;
			try {
				in = mpfile.getInputStream();
				Files.deleteIfExists(destination);
				Files.copy(in, destination);
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
			suggestionEntity.setAttachment(destFileName);
		}

		Suggestion savedInfo = suggestRepo.save(suggestionEntity);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.REFNO, savedInfo.getRefno());

		// sending sms and email

		Thread threadSMS = new Thread(new Runnable() {
			public void run() {
				if (SEND_SMS_SERVICE.equalsIgnoreCase("true")) {
					smsservice.sendSMS_Suggestions(savedInfo.getUsername(), mobile, "Suggestion_SMS", referenceNumber);
				}
			}
		});
		threadSMS.start();

		Thread threadMail = new Thread(new Runnable() {
			public void run() {
				if (SEND_EMAIL_SERVICE.equalsIgnoreCase("true")) {
					emailservice.formatSuggestionEmail(savedInfo.getUsername(), email, referenceNumber);
				}
			}
		});
		threadMail.start();

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> getRewardHistory(String username) throws JsonProcessingException {
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

		List<RewardPoints> userRewards = rewardrepo.findFirst20ByUsernameOrderByDateTimeDesc(username);
		if (userRewards.isEmpty() || userRewards.size() == 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "No rewards available");
			LOGGER.error("No rewards available for username:" + username);
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("size of rewards available -> " + userRewards.size());

		return new ResponseEntity<String>(objMapper.writeValueAsString(userRewards), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> getFooterFaq() throws JsonProcessingException {
		LOGGER.info("Entered getFooterFaq() -> Start");

		JSONObject faqtop = new JSONObject();

		List<Faq> listfaq = faqrepo.findAll();
		List<FaqContent> listfaqcontent = faqcontentrepo.findAll();

		if (null != listfaq && listfaq.size() > 0) {
			for (int j = 0; j < listfaq.size(); j++) {
				int faqid = listfaq.get(j).getId();
				String faqheader = listfaq.get(j).getHeader();

				JSONObject faqmiddle = new JSONObject();

				for (int i = 0; i < listfaqcontent.size(); i++) {
					if (Integer.parseInt(listfaqcontent.get(i).getFaq()) == faqid) {
						faqmiddle.put(listfaqcontent.get(i).getQuestion(), listfaqcontent.get(i).getAnswer());
					}
					faqtop.put(faqheader, faqmiddle);
				}
			}
		}
		return new ResponseEntity<String>(faqtop.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> getSliderInfo() {
		LOGGER.info("Entered getSliderInfo() -> Start");
		JSONObject response = new JSONObject();

		List<Slider> sliders = sliderrepo.findAllActiveSliders();
		for (int i = 0; i < sliders.size(); i++) {
			response.put("" + sliders.get(i).getId(), sliders.get(i).getImage());
		}

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> getServiceIcons() {
		LOGGER.info("Entered getServiceIcons() -> Start");
		JSONObject topelement = new JSONObject();

		List<com.raxn.entity.Service> serviceIcons = servicerepo.findAllActiceService();

		for (int i = 0; i < serviceIcons.size(); i++) {

			JSONObject bodyelement = new JSONObject();

			bodyelement.put("name", serviceIcons.get(i).getName());
			bodyelement.put("fafaicon", serviceIcons.get(i).getFafaicon());
			bodyelement.put("url", serviceIcons.get(i).getUrl());

			topelement.put("icon" + (i + 1), bodyelement);
		}

		return new ResponseEntity<String>(topelement.toString(), HttpStatus.OK);
	}

}
