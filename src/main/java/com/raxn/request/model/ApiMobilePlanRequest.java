package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

public class ApiMobilePlanRequest {

	@NotBlank
	private String operatorcode;

	@NotBlank
	private String circlecode;

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

}
