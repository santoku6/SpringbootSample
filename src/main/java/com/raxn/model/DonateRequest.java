package com.raxn.model;

import javax.validation.constraints.NotBlank;

public class DonateRequest {

	@NotBlank
	private String userid;

	@NotBlank
	private String amount;

	@NotBlank
	private String mode;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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
