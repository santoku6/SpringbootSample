package com.raxn.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.ChangeMobileRequest;
import com.raxn.request.model.ChangePwdRequest;
import com.raxn.request.model.TransHistoryRequest;
import com.raxn.request.model.UpdateUserRequest;
import com.raxn.service.UserProfileService;
import com.raxn.util.service.AppConstant;

@RestController
@RequestMapping("/usrprofile")
public class UserProfileController {

	@Autowired
	private UserProfileService userpfservice;
	
	private static String errorStatus = "Error";

	@PostMapping("/changepwd")
	public ResponseEntity<String> changePassword(@RequestBody ChangePwdRequest changePwdReq) {
		ResponseEntity<String> response = userpfservice.changePassword(changePwdReq);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/changemobile")
	public ResponseEntity<String> changeMobile(@RequestBody ChangeMobileRequest changeMobileReq) {
		ResponseEntity<String> response = userpfservice.changeMobile(changeMobileReq);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/updateprofile")
	public ResponseEntity<String> updateProfile(@RequestBody UpdateUserRequest updateUserReq) {
		ResponseEntity<String> response = userpfservice.updateProfile(updateUserReq);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/transhistory")
	public ResponseEntity<String> transHistory(@RequestBody TransHistoryRequest transHistoryReq) {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = userpfservice.transHistory(transHistoryReq);
		} catch (JsonProcessingException e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in transHistory(): " + e.getMessage());
			return new ResponseEntity<String>("Error in transHistory(): "+ e.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

}
