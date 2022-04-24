package com.raxn.service;

import org.springframework.http.ResponseEntity;

import com.raxn.model.ChangeMobileRequest;
import com.raxn.model.ChangePwdRequest;
import com.raxn.model.TransHistoryRequest;
import com.raxn.model.UpdateUserRequest;

public interface UserProfileService {

	ResponseEntity<String> changePassword(ChangePwdRequest changePwdReq);

	ResponseEntity<String> changeMobile(ChangeMobileRequest changeMobileReq);

	ResponseEntity<String> updateProfile(UpdateUserRequest updateUserReq);

	ResponseEntity<String> transHistory(TransHistoryRequest transHistoryReq);

}
