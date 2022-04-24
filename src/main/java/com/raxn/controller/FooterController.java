package com.raxn.controller;

import java.util.List;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raxn.entity.Gsetting;
import com.raxn.service.FooterService;
import com.raxn.util.service.AppConstant;

@RestController
@RequestMapping("/footer")
public class FooterController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(FooterController.class);

	@Autowired
	private FooterService footerService;
	
	private static String errorStatus = "Error";

	@GetMapping("/name/{name}")
	public ResponseEntity<String> getFooterSettingByName(@PathVariable String name) {
		ResponseEntity<String> response=null;
		JSONObject appResponse = new JSONObject();
		try {
			response = footerService.getFooterSettingByName(name);
		} catch (JsonProcessingException e) {
			appResponse.put(AppConstant.STATUS, errorStatus);
			appResponse.put(AppConstant.MESSAGE, "Error in getFooterSettingByName(): " + e.getMessage());
			return new ResponseEntity<String>("Error in getFooterSettingByName(): "+ e.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<String> getFooterSettingById(@PathVariable Integer id) throws JsonProcessingException {
		ResponseEntity<String> response = footerService.getFooterSettingById(id);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/all")
	public ResponseEntity<String> getAllFooterSettings() throws JsonProcessingException {
		ResponseEntity<String> response = footerService.getAllFooterSettings();
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/post")
	public ResponseEntity<String> saveFooterSetting(@Valid @RequestBody Gsetting gs) throws JsonProcessingException {
		ResponseEntity<String> response = footerService.saveFooterSetting(gs);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/postall")
	public ResponseEntity<String> saveAllFooterSetting(@Valid @RequestBody List<Gsetting> gs) throws JsonProcessingException  {
		ResponseEntity<String> response = footerService.saveAllFooterSetting(gs);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateFooterSetting(@Valid @RequestBody Gsetting gs) throws JsonProcessingException {
		ResponseEntity<String> response = footerService.updateFooterSetting(gs);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteFooterSetting(@PathVariable int id) {
		ResponseEntity<String> response = footerService.deleteFooterSetting(id);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

}
