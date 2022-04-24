package com.raxn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.model.CouponCheckRequest;
import com.raxn.service.CouponsService;

@RestController
@RequestMapping("/coupons")
public class CouponsController {
	
	@Autowired
	private CouponsService couponsService;
	
	@GetMapping("/all")
	public ResponseEntity<String> getAllCoupons() throws JsonProcessingException {
		ResponseEntity<String> response = couponsService.getAllCoupons();
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
		
	}
	
	@GetMapping("/{code}")
	public ResponseEntity<String> getCouponsByCode(@PathVariable String code) throws JsonProcessingException {
		ResponseEntity<String> response = couponsService.getCouponsByCode(code);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
	
	@GetMapping("/offers")
	public ResponseEntity<String> getAllCouponsOffers() throws JsonProcessingException {
		ResponseEntity<String> response = couponsService.getAllCouponsOffers();
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
	
	@GetMapping("/offers/{category}")
	public ResponseEntity<String> getCouponsOffersByCategory(@PathVariable String category) throws JsonProcessingException {
		ResponseEntity<String> response = couponsService.getCouponsOffersByCategory(category);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
	
	@GetMapping("/check")
	public ResponseEntity<String> checkCouponEligibility(@RequestBody CouponCheckRequest ccRequest) throws JsonProcessingException {
		ResponseEntity<String> response = couponsService.checkCouponEligibility(ccRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
	

}
