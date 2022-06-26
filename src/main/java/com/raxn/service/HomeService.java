package com.raxn.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.LoginRequest;
import com.raxn.request.model.NextLoginRequest;
import com.raxn.request.model.OTPModel;
import com.raxn.request.model.ResetPWDRequest;
import com.raxn.request.model.UserRequest;
import com.raxn.request.model.VerifyOTPRequest;

public interface HomeService {

	// Generate OTP
	ResponseEntity<String> generateOTP(OTPModel otpRequest);

	// User Registration
	ResponseEntity<String> registerUser(UserRequest userRequest);

	// Verify OTP
	ResponseEntity<String> verifyOTP(VerifyOTPRequest verifyOTPRequest);

	// Reset/Forget Password
	ResponseEntity<String> resetPassword(ResetPWDRequest resetPWDRequest);

	// login verification
	ResponseEntity<String> loginUser(LoginRequest loginRequest);

	// after login, otp verification & token generation
	ResponseEntity<String> nextLoginUser(NextLoginRequest nextloginRequest);

	ResponseEntity<String> getUserWbalance(String userid);

	ResponseEntity<String> deleteUserById(String identifier) throws JsonProcessingException;

	ResponseEntity<String> getUserInfo();

	ResponseEntity<String> logoutSite(String username, HttpServletRequest request,
			HttpServletResponse response);
	
	ResponseEntity<String> refreshtoken(HttpServletRequest request);

}
