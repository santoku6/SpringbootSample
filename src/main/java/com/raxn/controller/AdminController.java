package com.raxn.controller;

import javax.validation.Valid;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.entity.Admin;
import com.raxn.entity.Service;
import com.raxn.service.AdminService;
import com.raxn.util.service.AppConstant;

@RestController
@RequestMapping("/adminbb")
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	private static String errorStatus = "Error";

	@Autowired
	AdminService adminService;

	@GetMapping("/user/{username}")
	public ResponseEntity<String> getAdminByUserName(@PathVariable String username) throws JsonProcessingException {
		ResponseEntity<String> response = adminService.getAdminByUserName(username);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	/*
	 * @GetMapping("/all") public ResponseEntity<List<Admin>> getAllSettings() {
	 * List<Admin> list = adminrepo.findAll(); if (list == null) { return new
	 * ResponseEntity<List<Admin>>(list, HttpStatus.NOT_FOUND); } return new
	 * ResponseEntity<List<Admin>>(list, HttpStatus.OK); }
	 */

	@PostMapping("/addadmin")
	public ResponseEntity<String> postAdmin(@Valid @RequestBody Admin admin) throws JsonProcessingException {
		ResponseEntity<String> response = adminService.addAdmin(admin);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	// add banners
	@PostMapping("/addslider")
	public ResponseEntity<String> postSlider(@RequestParam(required = true, name = "file") MultipartFile mpfile,
			String status) {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = adminService.postSlider(mpfile, status);
		} catch (Exception e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in postSlider() : " + e.getMessage());
			LOGGER.error("Error in postSlider(): " + e.getMessage());
			return new ResponseEntity<String>(appResponse.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PutMapping("/updateslider")
	public ResponseEntity<String> updateSlider(@RequestParam(required = true, name = "file") MultipartFile mpfile,
			String status, String id) {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = adminService.updateSlider(mpfile, status, id);
		} catch (Exception e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in updateSlider() : " + e.getMessage());
			LOGGER.error("Error in updateSlider(): " + e.getMessage());
			return new ResponseEntity<String>(appResponse.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	// add serviceicon
	@PostMapping("/addservice")
	public ResponseEntity<String> postServiceIcon(Service service) {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = adminService.postServiceIcon(service);
		} catch (Exception e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in postServiceIcon() : " + e.getMessage());
			LOGGER.error("Error in postServiceIcon(): " + e.getMessage());
			return new ResponseEntity<String>(appResponse.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
	
	@PutMapping("/updateservice")
	public ResponseEntity<String> updateServiceIcon(Service service) {
		ResponseEntity<String> response = null;
		JSONObject appResponse = new JSONObject();
		try {
			response = adminService.updateServiceIcon(service);
		} catch (Exception e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in updateServiceIcon() : " + e.getMessage());
			LOGGER.error("Error in updateServiceIcon(): " + e.getMessage());
			return new ResponseEntity<String>(appResponse.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

}
