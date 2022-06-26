package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class RechargeMobileRequest {

	@NotBlank
	private String operatorcode;

	@NotBlank
	private String circlecode;

	@NotBlank
	private String userid;

	@NotBlank
	private String mobile;

	@NotBlank
	private String rechargeamount;

	private String amountfromwallet;

	private String amountfrompaygateway;

	private String code;
	
	private String servicecategory;

	@NotBlank
	private String mode;

	public String getOperatorcode() {
		return operatorcode;
	}

	public void setOperatorcode(String operatorcode) {
		this.operatorcode = operatorcode;
	}

	public String getCirclecode() {
		return circlecode;
	}

	public void setCirclecode(String circlecode) {
		this.circlecode = circlecode;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRechargeamount() {
		return rechargeamount;
	}

	public void setRechargeamount(String rechargeamount) {
		this.rechargeamount = rechargeamount;
	}

	public String getAmountfromwallet() {
		return amountfromwallet;
	}

	public void setAmountfromwallet(String amountfromwallet) {
		this.amountfromwallet = amountfromwallet;
	}

	public String getAmountfrompaygateway() {
		return amountfrompaygateway;
	}

	public void setAmountfrompaygateway(String amountfrompaygateway) {
		this.amountfrompaygateway = amountfrompaygateway;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getServicecategory() {
		return servicecategory;
	}

	public void setServicecategory(String servicecategory) {
		this.servicecategory = servicecategory;
	}

}
