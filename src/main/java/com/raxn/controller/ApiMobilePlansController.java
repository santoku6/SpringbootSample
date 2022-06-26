package com.raxn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.request.model.ApiMobilePlanRequest;
import com.raxn.service.ApiMobilePlansService;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ApiMobilePlansController {

	@Autowired
	ApiMobilePlansService apiMobilePlansService;

	// for prepaid mobile - plans finder
	@GetMapping("/mplans")
	public ResponseEntity<String> getMobilePlans(@RequestBody ApiMobilePlanRequest mplanRequest)
			throws JsonProcessingException {
		ResponseEntity<String> response = apiMobilePlansService.getMobilePlans(mplanRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
	
	@GetMapping("/mplansparam/{operator}/{circle}")
	public ResponseEntity<String> getMobilePlansquery(@PathVariable String operator, @PathVariable String circle)
			throws JsonProcessingException {
		ResponseEntity<String> response = apiMobilePlansService.getMobilePlansquery(operator, circle);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	// for prepaid mobile - Operator finder
	@GetMapping("/findop/{mobile}")
	public ResponseEntity<String> findOperator(@PathVariable String mobile) throws JsonProcessingException {
		ResponseEntity<String> response = apiMobilePlansService.findOperator(mobile);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	

}
