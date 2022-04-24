package com.raxn.model;

import javax.validation.constraints.NotBlank;

public class ResetPWDRequest {
	
	@NotBlank
	private String identifier;
	
	@NotBlank
	private String mode;
	
	@NotBlank
	private String password;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
