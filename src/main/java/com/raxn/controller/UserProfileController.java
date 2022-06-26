package com.raxn.controller;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.ChangeMobileRequest;
import com.raxn.request.model.ChangePwdRequest;
import com.raxn.request.model.DonateRequest;
import com.raxn.request.model.TransHistoryRequest;
import com.raxn.request.model.UpdateUserRequest;
import com.raxn.service.UserProfileService;
import com.raxn.util.service.AppConstant;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserProfileController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);

	@Autowired
	private UserProfileService userpfservice;

	private static String errorStatus = "Error";

	@PostMapping("/changepwd")
	public ResponseEntity<String> changePassword(HttpServletRequest request,
			@RequestBody ChangePwdRequest changePwdReq) {
		ResponseEntity<String> response = userpfservice.changePassword(request, changePwdReq);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/changemobile")
	public ResponseEntity<String> changeMobile(HttpServletRequest request,
			@RequestBody ChangeMobileRequest changeMobileReq) {
		ResponseEntity<String> response = userpfservice.changeMobile(request, changeMobileReq);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/updateprofile")
	public ResponseEntity<String> updateProfile(HttpServletRequest request,
			@RequestBody UpdateUserRequest updateUserReq) {
		ResponseEntity<String> response = userpfservice.updateProfile(request, updateUserReq);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/transhistory")
	public ResponseEntity<String> transHistory(HttpServletRequest request,
			@RequestBody TransHistoryRequest transHistoryReq) {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = userpfservice.transHistory(request, transHistoryReq);
		} catch (JsonProcessingException e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in transHistory(): " + e.getMessage());
			LOGGER.error("JsonProcessingException :: -> " + e.getMessage());
			return new ResponseEntity<String>("Error in transHistory(): " + e.getMessage(),
					HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
	
	@GetMapping("/rewardlist/{username}")
	public ResponseEntity<String> getRewardHistory(HttpServletRequest request, @PathVariable String username) {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = userpfservice.getRewardHistory(request, username);
		} catch (JsonProcessingException e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in getRewardHistory(): " + e.getMessage());
			LOGGER.error("JsonProcessingException :: -> " + e.getMessage());
			return new ResponseEntity<String>("Error in getRewardHistory(): " + e.getMessage(),
					HttpStatus.SERVICE_UNAVAILABLE);			
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
	
	@PostMapping("/donate")
	public ResponseEntity<String> donate(HttpServletRequest request, DonateRequest donateRequest) {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = userpfservice.donate(request, donateRequest);
		} catch (JsonProcessingException e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in donate(): " + e.getMessage());
			LOGGER.error("JsonProcessingException :: -> " + e.getMessage());
			return new ResponseEntity<String>("Error in donate(): " + e.getMessage(),
					HttpStatus.SERVICE_UNAVAILABLE);			
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

}
