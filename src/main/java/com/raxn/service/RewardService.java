package com.raxn.service;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.model.DonateRequest;

public interface RewardService {

	// get reward history (last 20 transactions)
	ResponseEntity<String> getRewardHistory(String userid) throws JsonProcessingException;
	
	ResponseEntity<String> getRewardPoint(String userid) throws JsonProcessingException;
	
	ResponseEntity<String> donate(DonateRequest donateRequest) throws JsonProcessingException;

}
