package com.raxn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.LoginRequest;
import com.raxn.request.model.NextLoginRequest;
import com.raxn.request.model.OTPModel;
import com.raxn.request.model.ResetPWDRequest;
import com.raxn.request.model.UserRequest;
import com.raxn.request.model.VerifyOTPRequest;
import com.raxn.service.HomeService;
import com.raxn.util.service.AppConstant;

@CrossOrigin
@RestController
@RequestMapping()
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private HomeService homeservice;

	private static String errorStatus = "Error";

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest) {
		ResponseEntity<String> response = homeservice.registerUser(userRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/getotp")
	public ResponseEntity<String> generateOTP(@RequestBody OTPModel otpRequest) {
		ResponseEntity<String> response = homeservice.generateOTP(otpRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PutMapping("/verifyotp")
	public ResponseEntity<String> verifyOTP(@RequestBody VerifyOTPRequest verifyOTPRequest) {
		ResponseEntity<String> response = homeservice.verifyOTP(verifyOTPRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PutMapping("/resetpwd")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPWDRequest resetPWDRequest) {
		ResponseEntity<String> response = homeservice.resetPassword(resetPWDRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	/**
	 * @param loginRequest
	 * @return Step1: Verify login credentials, Step2: Send OTP for 2fa
	 */
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
		ResponseEntity<String> response = homeservice.loginUser(loginRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	/**
	 * @param verifyOTPRequest
	 * @return Step1: Verify OTP
	 */
	@PostMapping("/nextlogin")
	public ResponseEntity<String> nextLoginUser(@RequestBody NextLoginRequest nextloginRequest) {
		ResponseEntity<String> response = homeservice.nextLoginUser(nextloginRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/wbalance/{userid}")
	public ResponseEntity<String> getAdminByUserName(@PathVariable String userid) {
		ResponseEntity<String> response = homeservice.getUserWbalance(userid);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/delete/user/{identifier}")
	public ResponseEntity<String> deleteUserById(@PathVariable String identifier) {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = homeservice.deleteUserById(identifier);
		} catch (JsonProcessingException e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in deleteUserById(): " + e.getMessage());
			LOGGER.error("Error in deleteUserById(): " + e.getMessage());
			return new ResponseEntity<String>(appResponse.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/userinfo")
	public ResponseEntity<String> getUserInfo() {
		ResponseEntity<String> response = homeservice.getUserInfo();
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/byesite/{username}")
	public ResponseEntity<String> logoutSite(@PathVariable String username, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseEntity<String> responseReturn = homeservice.logoutSite(username, request, response);
		return new ResponseEntity<String>(responseReturn.getBody(), responseReturn.getStatusCode());
	}

	@GetMapping("/refreshtoken")
	public ResponseEntity<String> refreshtoken(HttpServletRequest request) throws Exception {
		ResponseEntity<String> responseReturn = homeservice.refreshtoken(request);
		return new ResponseEntity<String>(responseReturn.getBody(), responseReturn.getStatusCode());
	}

}
