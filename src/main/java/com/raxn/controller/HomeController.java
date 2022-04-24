package com.raxn.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.raxn.model.LoginRequest;
import com.raxn.model.OTPModel;
import com.raxn.model.ResetPWDRequest;
import com.raxn.model.UserRequest;
import com.raxn.model.VerifyOTPRequest;
import com.raxn.service.HomeService;

@RestController
@RequestMapping("/home")
public class HomeController {

	@Autowired
	private HomeService homeservice;
	
	@PostMapping("/postsuggestion")
	public ResponseEntity<String> postSuggestions(@RequestParam(required = false, name = "file") MultipartFile mpfile,
			@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("mobile") String mobile, @RequestParam("messagetype") String messagetype,
			@RequestParam("query") String query, @RequestParam("mode") String mode) {
		ResponseEntity<String> response = homeservice.postSuggestions(mpfile, name, email, mobile, messagetype, query, mode);
		
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequest userRequest) {
		ResponseEntity<String> response = homeservice.registerUser(userRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/getotp")
	public ResponseEntity<String> generateOTP(@Valid @RequestBody OTPModel otpRequest) {
		ResponseEntity<String> response = homeservice.generateOTP(otpRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PutMapping("/verifyotp")
	public ResponseEntity<String> verifyOTP(@Valid @RequestBody VerifyOTPRequest verifyOTPRequest) {
		ResponseEntity<String> response = homeservice.verifyOTP(verifyOTPRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PutMapping("/resetpwd")
	public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPWDRequest resetPWDRequest) {
		ResponseEntity<String> response = homeservice.resetPassword(resetPWDRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	/**
	 * 
	 * @param loginRequest
	 * @return
	 * Step1: Verify login credentials, 
	 * Step2: Send OTP for 2fa
	 */
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {		
		ResponseEntity<String> response = homeservice.loginUser(loginRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	/**
	 * 
	 * @param verifyOTPRequest
	 * @return
	 * Step1: Verify OTP
	 * 
	 */
	@PostMapping("/nextlogin")
	public ResponseEntity<String> nextLoginUser(@RequestBody VerifyOTPRequest verifyOTPRequest) {
		ResponseEntity<String> response = homeservice.nextLoginUser(verifyOTPRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
	
	@GetMapping("/wbalance/{userid}")
	public ResponseEntity<String> getAdminByUserName(@PathVariable String userid) {
		ResponseEntity<String> response = homeservice.getUserWbalance(userid);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

}
