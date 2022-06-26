package com.raxn.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "general_settings")
@EntityListeners(AuditingEntityListener.class)
public class Gsetting implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String mobile;
	private String email;
	private String whatsapp;
	private String address;

	private String aboutus;
	private String chooseus;
	private String ourmission;
	private String ourvision;

	@Lob
	private String privacypolicy;
	@Lob
	private String tnc;
	@Lob
	private String refundpolicy;

	private String noticebar;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getWhatsapp() {
		return whatsapp;
	}

	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAboutus() {
		return aboutus;
	}

	public void setAboutus(String aboutus) {
		this.aboutus = aboutus;
	}

	public String getChooseus() {
		return chooseus;
	}

	public void setChooseus(String chooseus) {
		this.chooseus = chooseus;
	}

	public String getOurmission() {
		return ourmission;
	}

	public void setOurmission(String ourmission) {
		this.ourmission = ourmission;
	}

	public String getPrivacypolicy() {
		return privacypolicy;
	}

	public void setPrivacypolicy(String privacypolicy) {
		this.privacypolicy = privacypolicy;
	}

	public String getTnc() {
		return tnc;
	}

	public void setTnc(String tnc) {
		this.tnc = tnc;
	}

	public String getRefundpolicy() {
		return refundpolicy;
	}

	public void setRefundpolicy(String refundpolicy) {
		this.refundpolicy = refundpolicy;
	}

	public String getNoticebar() {
		return noticebar;
	}

	public void setNoticebar(String noticebar) {
		this.noticebar = noticebar;
	}

	public String getOurvision() {
		return ourvision;
	}

	public void setOurvision(String ourvision) {
		this.ourvision = ourvision;
	}

}
