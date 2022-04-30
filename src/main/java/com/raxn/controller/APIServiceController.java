package com.raxn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.ApiMobilePlanRequest;
import com.raxn.request.model.RechargeMobileRequest;
import com.raxn.service.ApiServiceService;

@RestController
@RequestMapping("/api")
public class APIServiceController {

	@Autowired
	ApiServiceService apiServiceService;

	// for prepaid mobile - plans finder
	@GetMapping("/mplans")
	public ResponseEntity<String> getMobilePlans(@RequestBody ApiMobilePlanRequest mplanRequest)
			throws JsonProcessingException {
		ResponseEntity<String> response = apiServiceService.getMobilePlans(mplanRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	// for prepaid mobile - Operator finder
	@GetMapping("/findop/{mobile}")
	public ResponseEntity<String> findOperator(@PathVariable String mobile) throws JsonProcessingException {
		ResponseEntity<String> response = apiServiceService.findOperator(mobile);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	// for prepaid mobile
	@GetMapping("/rmobile")
	public ResponseEntity<String> rechargeMobile(RechargeMobileRequest rmobilerequest) throws JsonProcessingException {
		ResponseEntity<String> response = apiServiceService.rechargeMobile(rmobilerequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

}
