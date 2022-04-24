package com.raxn.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.entity.Admin;
import com.raxn.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;

	@GetMapping("/{username}")
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

	@PostMapping("/add")
	public ResponseEntity<String> postAdmin(@Valid @RequestBody Admin admin) throws JsonProcessingException {
		ResponseEntity<String> response = adminService.addAdmin(admin);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

}
