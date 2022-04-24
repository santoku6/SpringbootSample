package com.raxn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/user")
	public String user() {
		return "this is user page";
	}
	
	@GetMapping("/user/suggestion")
	public String userSuggestion() {
		return "this is user/suggestion page";
	}
	
	@GetMapping("/abc")
	public String user11() {
		return "this is abc page";
	}

}
