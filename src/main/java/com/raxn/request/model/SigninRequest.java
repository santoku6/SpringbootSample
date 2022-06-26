package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class SigninRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String servicename;

	@NotBlank
	private String mode;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
