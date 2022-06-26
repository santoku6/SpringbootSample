package com.raxn.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DashboardService {

	ResponseEntity<String> getFooterSettingByName(String name) throws JsonProcessingException;

	ResponseEntity<String> getOffers() throws JsonProcessingException;

	// post suggestions
	ResponseEntity<String> postSuggestions(MultipartFile mpfile, String name, String email, String mobile,
			String messagetype, String query, String mode, String username);

	ResponseEntity<String> getRewardHistory(String username) throws JsonProcessingException;

	ResponseEntity<String> getFooterFaq() throws JsonProcessingException;

	ResponseEntity<String> getSliderInfo();

	ResponseEntity<String> getServiceIcons();

}
