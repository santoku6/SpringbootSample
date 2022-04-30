package com.raxn.service;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.ApiMobilePlanRequest;
import com.raxn.request.model.RechargeMobileRequest;

public interface ApiServiceService {

	ResponseEntity<String> getMobilePlans(ApiMobilePlanRequest mplanRequest) throws JsonProcessingException;

	ResponseEntity<String> findOperator(String mobile) throws JsonProcessingException;
	
	ResponseEntity<String> rechargeMobile(RechargeMobileRequest rmobilerequest) throws JsonProcessingException;

}
