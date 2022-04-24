package com.raxn.service.impl;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raxn.entity.Admin;
import com.raxn.repository.AdminRepository;
import com.raxn.service.AdminService;
import com.raxn.util.service.AppConstant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);
	
	@Autowired
	AdminRepository adminrepo;
	
	//private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Override
	public ResponseEntity<String> getAdminByUserName(String username) throws JsonProcessingException {
		LOGGER.info("Entered getAdminByUserName() -> Start");
		LOGGER.info("Parameter username -> " + username);
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();

		Admin adminobj = adminrepo.findByUserName(username);
		if (null == adminobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "No Admin found with username:" + username);
			LOGGER.error("No Admin found with username:" + username);
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("Admin found with username -> " + objMapper.writeValueAsString(adminobj));
		return new ResponseEntity<String>(objMapper.writeValueAsString(adminobj), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> addAdmin(Admin admin) throws JsonProcessingException {
		LOGGER.info("Entered postAdmin() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		LOGGER.info("Parameter admin -> " + objMapper.writeValueAsString(admin));
		
		JSONObject response = new JSONObject();
		
		Admin adminobj = adminrepo.save(admin);
		if (null == adminobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Admin not saved in database");
			LOGGER.error("Admin not saved in database");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("Admin saved in DB -> " + objMapper.writeValueAsString(adminobj));
		return new ResponseEntity<String>(objMapper.writeValueAsString(adminobj), HttpStatus.OK);
	}


}
