package com.raxn.service;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.ApiMobilePlanRequest;

public interface ApiMobilePlansService {

	ResponseEntity<String> getMobilePlans(ApiMobilePlanRequest mplanRequest) throws JsonProcessingException;
	
	ResponseEntity<String> getMobilePlansquery(String operator, String circle) throws JsonProcessingException;

	ResponseEntity<String> findOperator(String mobile) throws JsonProcessingException;

}
