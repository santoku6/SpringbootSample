package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class UpdateUserRequest {

	@NotBlank
	private String name;

	private String dob;

	private String city;

	@NotBlank
	private String mode;
	
	@NotBlank
	private String userid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
