package com.raxn.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import net.bytebuddy.utility.RandomString;

@CrossOrigin
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
	public String abc() {
		return "this is abc page";
	}
	
	@GetMapping("/mypage")
	public String getMypage() {
		return "my page";
	}
	@GetMapping("/abc/def")
	public String def() {
		return "this is abc def page";
	}
	
	@GetMapping("/mypage/welcome")
	public String welcome() {
		return "welcome to my page";
	}
	
	@GetMapping("/index")
	public String index() {
		return "index";
	}
	
	@GetMapping("/admin/aabb")
	public String aabb() {
		return "aabb";
	}

}
