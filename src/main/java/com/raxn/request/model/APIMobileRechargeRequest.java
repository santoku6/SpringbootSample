package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class APIMobileRechargeRequest {

	@NotBlank
	private String operatorcode;

	@NotBlank
	private String requestid;

	@NotBlank
	private String userid;

	@NotBlank
	private String consumerno;

	@NotBlank
	private String amount;

	public String getOperatorcode() {
		return operatorcode;
	}

	public void setOperatorcode(String operatorcode) {
		this.operatorcode = operatorcode;
	}

	public String getRequestid() {
		return requestid;
	}

	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}

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

	public String getConsumerno() {
		return consumerno;
	}

	public void setConsumerno(String consumerno) {
		this.consumerno = consumerno;
	}

}
