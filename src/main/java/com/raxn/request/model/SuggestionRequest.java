package com.raxn.request.model;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuggestionRequest {
	
	@NotBlank
	@JsonProperty
	private String name;

	@NotBlank
	@JsonProperty
	private String mobile;
	
	@NotBlank
	@JsonProperty
	private String email;

	@NotBlank
	@JsonProperty
	private String messagetype;

	@NotBlank
	private String file;
	
	@JsonProperty
	private String querytext;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	public String getQuerytext() {
		return querytext;
	}

	public void setQuerytext(String querytext) {
		this.querytext = querytext;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
