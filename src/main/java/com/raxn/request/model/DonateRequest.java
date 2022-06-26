package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class DonateRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String amount;

	@NotBlank
	private String mode;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
