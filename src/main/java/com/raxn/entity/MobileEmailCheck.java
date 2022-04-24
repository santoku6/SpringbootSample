package com.raxn.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "otpDatetime" }, allowGetters = true)
public class MobileEmailCheck implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String identifier;
	private String otp;
	private int otpCount;
	private String otpStatus;// verified, not-verified or blank
	private String otpMode;
	private String otpServicename;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date otpDatetime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public int getOtpCount() {
		return otpCount;
	}

	public void setOtpCount(int otpCount) {
		this.otpCount = otpCount;
	}

	public String getOtpStatus() {
		return otpStatus;
	}

	public void setOtpStatus(String otpStatus) {
		this.otpStatus = otpStatus;
	}

	public String getOtpMode() {
		return otpMode;
	}

	public void setOtpMode(String otpMode) {
		this.otpMode = otpMode;
	}

	public String getOtpServicename() {
		return otpServicename;
	}

	public void setOtpServicename(String otpServicename) {
		this.otpServicename = otpServicename;
	}

	public Date getOtpDatetime() {
		return otpDatetime;
	}

	public void setOtpDatetime(Date otpDatetime) {
		this.otpDatetime = otpDatetime;
	}

}
