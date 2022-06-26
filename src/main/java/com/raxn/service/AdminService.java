package com.raxn.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.entity.Admin;
import com.raxn.entity.Service;

public interface AdminService {

	ResponseEntity<String> getAdminByUserName(String username) throws JsonProcessingException;

	ResponseEntity<String> addAdmin(Admin admin) throws JsonProcessingException;

	ResponseEntity<String> postSlider(MultipartFile mpfile, String status) throws IOException;
	
	ResponseEntity<String> updateSlider(MultipartFile mpfile, String status, String id) throws IOException;
	
	ResponseEntity<String> postServiceIcon(Service service) throws IOException;
	
	ResponseEntity<String> updateServiceIcon(Service service) throws IOException;

}
