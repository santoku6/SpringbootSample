package com.raxn.service;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.model.CouponCheckRequest;

public interface CouponsService {
	
	ResponseEntity<String> getAllCoupons() throws JsonProcessingException;
	
	ResponseEntity<String> getCouponsByCode(String code) throws JsonProcessingException;
	
	ResponseEntity<String> getAllCouponsOffers() throws JsonProcessingException;
	
	ResponseEntity<String> getCouponsOffersByCategory(String category) throws JsonProcessingException;
	
	ResponseEntity<String> checkCouponEligibility(CouponCheckRequest ccRequest) throws JsonProcessingException;

}
