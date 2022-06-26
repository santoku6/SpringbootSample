package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class TransHistoryRequest {

	@NotBlank
	private String mode;

	@NotBlank
	private String username;

	@NotBlank
	private String category;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
