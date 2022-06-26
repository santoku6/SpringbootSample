package com.raxn.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.service.DashboardService;
import com.raxn.util.service.AppConstant;

@CrossOrigin
@RestController
@RequestMapping
public class DashboardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private DashboardService dashboardService;

	private static String errorStatus = "Error";

	@GetMapping("/footer/name/{name}")
	public ResponseEntity<String> getFooterSettingByName(@PathVariable String name) {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = dashboardService.getFooterSettingByName(name);
		} catch (JsonProcessingException e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in getFooterSettingByName(): " + e.getMessage());
			LOGGER.error("Error in getFooterSettingByName(): " + e.getMessage());
			return new ResponseEntity<String>(appResponse.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/offers")
	public ResponseEntity<String> getOffers() {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = dashboardService.getOffers();
		} catch (JsonProcessingException e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in getAllCouponsOffers() : " + e.getMessage());
			LOGGER.error("Error in getAllCouponsOffers(): " + e.getMessage());
			return new ResponseEntity<String>(appResponse.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/postsuggestion")
	public ResponseEntity<String> postSuggestions(@RequestParam(required = false, name = "file") MultipartFile mpfile,
			@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("mobile") String mobile, @RequestParam("messagetype") String messagetype,
			@RequestParam("query") String query, @RequestParam("mode") String mode,
			@RequestParam(value = "username", required = false) String username) {
		ResponseEntity<String> response = dashboardService.postSuggestions(mpfile, name, email, mobile, messagetype,
				query, mode, username);

		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/rewardhistory/{username}")
	public ResponseEntity<String> getRewardHistory(@PathVariable String username) {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = dashboardService.getRewardHistory(username);
		} catch (JsonProcessingException e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in getRewardHistory() : " + e.getMessage());
			LOGGER.error("Error in getRewardHistory(): " + e.getMessage());
			return new ResponseEntity<String>(appResponse.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/faq")
	public ResponseEntity<String> getFooterFaq() {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = dashboardService.getFooterFaq();
		} catch (JsonProcessingException e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in getFooterFaq() : " + e.getMessage());
			LOGGER.error("Error in getFooterFaq(): " + e.getMessage());
			return new ResponseEntity<String>(appResponse.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/slider")
	public ResponseEntity<String> getSliderInfo() {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = dashboardService.getSliderInfo();
		} catch (Exception e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in getSliderInfo() : " + e.getMessage());
			LOGGER.error("Error in getSliderInfo(): " + e.getMessage());
			return new ResponseEntity<String>(appResponse.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/serviceicons")
	public ResponseEntity<String> getServiceIcons() {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = dashboardService.getServiceIcons();
		} catch (Exception e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in getServiceIcons() : " + e.getMessage());
			LOGGER.error("Error in getServiceIcons(): " + e.getMessage());
			return new ResponseEntity<String>(appResponse.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

}
