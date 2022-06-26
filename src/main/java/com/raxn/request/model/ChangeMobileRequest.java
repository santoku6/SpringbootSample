package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class ChangeMobileRequest {

	@NotBlank
	private String newmobile;

	@NotBlank
	private String mode;

	@NotBlank
	private String username;
	
	@NotBlank
	private String servicename;

	public String getNewmobile() {
		return newmobile;
	}

	public void setNewmobile(String newmobile) {
		this.newmobile = newmobile;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

}
