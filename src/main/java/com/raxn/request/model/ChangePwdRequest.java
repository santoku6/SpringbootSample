package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class ChangePwdRequest {

	@NotBlank
	private String oldpassword;

	@NotBlank
	private String newpassword;

	@NotBlank
	private String mode;

	@NotBlank
	private String userid;

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
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
