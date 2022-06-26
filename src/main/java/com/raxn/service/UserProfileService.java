package com.raxn.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.ChangeMobileRequest;
import com.raxn.request.model.ChangePwdRequest;
import com.raxn.request.model.DonateRequest;
import com.raxn.request.model.TransHistoryRequest;
import com.raxn.request.model.UpdateUserRequest;

public interface UserProfileService {

	ResponseEntity<String> changePassword(HttpServletRequest request, ChangePwdRequest changePwdReq);

	ResponseEntity<String> changeMobile(HttpServletRequest request, ChangeMobileRequest changeMobileReq);

	ResponseEntity<String> updateProfile(HttpServletRequest request, UpdateUserRequest updateUserReq);

	ResponseEntity<String> transHistory(HttpServletRequest request, TransHistoryRequest transHistoryReq)
			throws JsonProcessingException;

	ResponseEntity<String> getRewardHistory(HttpServletRequest request, String username) throws JsonProcessingException;
	
	ResponseEntity<String> donate(HttpServletRequest request, DonateRequest donateRequest) throws JsonProcessingException;

}
