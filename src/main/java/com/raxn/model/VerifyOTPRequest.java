package com.raxn.model;

import javax.validation.constraints.NotBlank;

public class VerifyOTPRequest {
	
	@NotBlank
	private String servicename;

	@NotBlank
	private String identifier;
	
	@NotBlank
	private String mode;
	
	@NotBlank
	private String otp;

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
	
}
