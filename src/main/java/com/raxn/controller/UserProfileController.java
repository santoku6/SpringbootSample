package com.raxn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raxn.model.ChangeMobileRequest;
import com.raxn.model.ChangePwdRequest;
import com.raxn.model.TransHistoryRequest;
import com.raxn.model.UpdateUserRequest;
import com.raxn.service.UserProfileService;

@RestController
@RequestMapping("/usrprofile")
public class UserProfileController {

	@Autowired
	private UserProfileService userpfservice;

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
		ResponseEntity<String> response = userpfservice.transHistory(transHistoryReq);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
	
	

}
