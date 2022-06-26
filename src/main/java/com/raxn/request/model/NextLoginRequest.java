package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class NextLoginRequest {
	
	@NotBlank
	private String servicename;

	@NotBlank
	private String identifier;
	
	@NotBlank
	private String mode;
	
	@NotBlank
	private String otp;
	
	@NotBlank
	private String ipaddress;
	
	private String deviceinfo;
	
	private String browserinfo;

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

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getDeviceinfo() {
		return deviceinfo;
	}

	public void setDeviceinfo(String deviceinfo) {
		this.deviceinfo = deviceinfo;
	}

	public String getBrowserinfo() {
		return browserinfo;
	}

	public void setBrowserinfo(String browserinfo) {
		this.browserinfo = browserinfo;
	}
	
}
