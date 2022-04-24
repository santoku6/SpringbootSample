package com.raxn.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.entity.Gsetting;

public interface FooterService {
	
	ResponseEntity<String> getFooterSettingByName(String name) throws JsonProcessingException;
	
	ResponseEntity<String> getFooterSettingById(Integer id) throws JsonProcessingException;;
	
	ResponseEntity<String> getAllFooterSettings() throws JsonProcessingException;
	
	ResponseEntity<String> saveFooterSetting(Gsetting gs) throws JsonProcessingException;
	
	ResponseEntity<String> saveAllFooterSetting(List<Gsetting> gs) throws JsonProcessingException;
	
	ResponseEntity<String> updateFooterSetting(Gsetting gs) throws JsonProcessingException;
	
	ResponseEntity<String> deleteFooterSetting(int id);
	
	

	

}
