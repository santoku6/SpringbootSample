package com.raxn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raxn.request.model.AddMoneyRequest;
import com.raxn.service.UserService;

@CrossOrigin
@RestController
@RequestMapping()
public class UserServiceController {
	
	@Autowired
	private UserService userservice;
	
	@PostMapping("/addmoney")
	public ResponseEntity<String> addmoney(@RequestBody AddMoneyRequest addmoneyRequest) {
		ResponseEntity<String> response = userservice.addmoney(addmoneyRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

}
