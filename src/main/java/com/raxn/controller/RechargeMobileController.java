package com.raxn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.RechargeMobileRequest;
import com.raxn.service.RechargeMobileService;

@RestController
@RequestMapping("/recharge")
public class RechargeMobileController {
	
	@Autowired
	RechargeMobileService rechargeMobileService;

	// for prepaid mobile
	@GetMapping("/mobile")
	public ResponseEntity<String> rechargePrepaidMobile(RechargeMobileRequest rmobilerequest)
			throws JsonProcessingException {
		ResponseEntity<String> response = rechargeMobileService.rechargePrepaidMobile(rmobilerequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

}
