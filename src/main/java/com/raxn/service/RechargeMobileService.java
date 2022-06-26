package com.raxn.service;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.RechargeMobileRequest;

public interface RechargeMobileService {

	ResponseEntity<String> rechargePrepaidMobile(RechargeMobileRequest rmobilerequest) throws JsonProcessingException;

}
