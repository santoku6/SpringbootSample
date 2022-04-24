package com.raxn.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.raxn.request.model.LoginRequest;
import com.raxn.request.model.OTPModel;
import com.raxn.request.model.ResetPWDRequest;
import com.raxn.request.model.UserRequest;
import com.raxn.request.model.VerifyOTPRequest;

public interface HomeService {
	
	// post suggestions
	ResponseEntity<String> postSuggestions(MultipartFile mpfile, String name, String email, String mobile,
			String messagetype, String query, String mode);

	// Generate OTP
	ResponseEntity<String> generateOTP(OTPModel otpRequest);

	// User Registration
	ResponseEntity<String> registerUser(UserRequest userRequest);
	
	// Verify OTP
	ResponseEntity<String> verifyOTP(VerifyOTPRequest verifyOTPRequest);
	
	// Reset/Forget Password
	ResponseEntity<String> resetPassword(ResetPWDRequest resetPWDRequest);
	
	//login verification
	ResponseEntity<String> loginUser(LoginRequest loginRequest);
	
	//after login, otp verification & token generation
	ResponseEntity<String> nextLoginUser(VerifyOTPRequest verifyOTPRequest);
	
	ResponseEntity<String> getUserWbalance(String userid);
	
	

	
	
	

}
