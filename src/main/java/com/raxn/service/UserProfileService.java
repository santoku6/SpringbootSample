package com.raxn.service;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.ChangeMobileRequest;
import com.raxn.request.model.ChangePwdRequest;
import com.raxn.request.model.TransHistoryRequest;
import com.raxn.request.model.UpdateUserRequest;

public interface UserProfileService {

	ResponseEntity<String> changePassword(ChangePwdRequest changePwdReq);

	ResponseEntity<String> changeMobile(ChangeMobileRequest changeMobileReq);

	ResponseEntity<String> updateProfile(UpdateUserRequest updateUserReq);

	ResponseEntity<String> transHistory(TransHistoryRequest transHistoryReq) throws JsonProcessingException;

}
