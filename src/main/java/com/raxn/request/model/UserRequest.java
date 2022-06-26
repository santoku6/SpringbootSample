package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class UserRequest {
	
	@NotBlank
	private String name;

	@NotBlank
	private String mobile;
	
	@NotBlank
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String tnc;
	
	@NotBlank
	private String mode;
	
	@NotBlank
	private String servicename;//signupusr
	
	@NotBlank
	private String userip;//ip address
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTnc() {
		return tnc;
	}

	public void setTnc(String tnc) {
		this.tnc = tnc;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getUserip() {
		return userip;
	}

	public void setUserip(String userip) {
		this.userip = userip;
	}

}
