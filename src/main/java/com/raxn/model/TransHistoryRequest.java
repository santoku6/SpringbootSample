package com.raxn.model;

import javax.validation.constraints.NotBlank;

public class TransHistoryRequest {

	@NotBlank
	private String mode;

	@NotBlank
	private String userid;

	@NotBlank
	private String category;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
