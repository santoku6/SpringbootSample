package com.raxn.service;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.entity.Admin;

public interface AdminService {

	ResponseEntity<String> getAdminByUserName(String username) throws JsonProcessingException;
	
	ResponseEntity<String>  addAdmin(Admin admin) throws JsonProcessingException;
	
}
