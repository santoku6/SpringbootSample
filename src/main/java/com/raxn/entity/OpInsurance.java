package com.raxn.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OpInsurance implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Column(unique = true)
	private String opKey;

	@NotBlank
	private String opName;

	private String billFetchApi;
	private String billPayApi;
	private String opImage;
	private String codeAmbika;
	private String codePyatoz;
	private String codeUtilityhub;
	private String codeMplan;
	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpKey() {
		return opKey;
	}

	public void setOpKey(String opKey) {
		this.opKey = opKey;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getBillPayApi() {
		return billPayApi;
	}

	public void setBillPayApi(String billPayApi) {
		this.billPayApi = billPayApi;
	}

	public String getOpImage() {
		return opImage;
	}

	public void setOpImage(String opImage) {
		this.opImage = opImage;
	}

	public String getCodeAmbika() {
		return codeAmbika;
	}

	public void setCodeAmbika(String codeAmbika) {
		this.codeAmbika = codeAmbika;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBillFetchApi() {
		return billFetchApi;
	}

	public void setBillFetchApi(String billFetchApi) {
		this.billFetchApi = billFetchApi;
	}

	public String getCodePyatoz() {
		return codePyatoz;
	}

	public void setCodePyatoz(String codePyatoz) {
		this.codePyatoz = codePyatoz;
	}

	public String getCodeUtilityhub() {
		return codeUtilityhub;
	}

	public void setCodeUtilityhub(String codeUtilityhub) {
		this.codeUtilityhub = codeUtilityhub;
	}

	public String getCodeMplan() {
		return codeMplan;
	}

	public void setCodeMplan(String codeMplan) {
		this.codeMplan = codeMplan;
	}

}
