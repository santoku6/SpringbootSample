package com.raxn.model;

import javax.validation.constraints.NotBlank;

public class ChangeMobileRequest {

	@NotBlank
	private String newmobile;

	@NotBlank
	private String mode;

	@NotBlank
	private String userid;

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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
