package com.raxn.service;

import org.springframework.http.ResponseEntity;

import com.raxn.request.model.AddMoneyRequest;

public interface UserService {
	ResponseEntity<String> addmoney(AddMoneyRequest addmoneyRequest);
}
