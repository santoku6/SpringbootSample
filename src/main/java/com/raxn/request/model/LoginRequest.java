package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank
	private String identifier;

	@NotBlank
	private String password;
	
	@NotBlank
	private String servicename;
	
	@NotBlank
	private String mode;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
