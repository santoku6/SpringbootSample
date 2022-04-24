package com.raxn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.DonateRequest;
import com.raxn.service.RewardService;

@RestController
@RequestMapping("/reward")
public class RewardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RewardController.class);

	@Autowired
	private RewardService rewardService;

	@GetMapping("/history/{userid}")
	public ResponseEntity<String> getRewardHistory(@PathVariable String userid) {
		ResponseEntity<String> response = null;
		try {
			response = rewardService.getRewardHistory(userid);
		} catch (JsonProcessingException e) {
			LOGGER.info("JsonProcessingException :: -> " + e.getMessage(), e);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
	
	@GetMapping("/point/{userid}")
	public ResponseEntity<String> getRewardPoint(@PathVariable String userid) {
		ResponseEntity<String> response = null;
		try {
			response = rewardService.getRewardPoint(userid);
		} catch (JsonProcessingException e) {
			LOGGER.info("JsonProcessingException :: -> " + e.getMessage(), e);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/donate")
	public ResponseEntity<String> donate(DonateRequest donateRequest) {
		ResponseEntity<String> response = null;
		try {
			response = rewardService.donate(donateRequest);
		} catch (JsonProcessingException e) {
			LOGGER.info("JsonProcessingException :: -> " + e.getMessage(), e);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

}
