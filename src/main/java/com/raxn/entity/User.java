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
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	private String name;

	@NotBlank
	@Column(unique = true)
	private String mobile;

	@NotBlank
	@Column(unique = true)
	private String email;

	@NotBlank
	private String tnc;// terms & conditions

	// user status active or inactive, inactive means user in sleeping state for
	// over a year, so need to remind user to use our service
	private boolean activatedStatus;

	// true(locked) or false(not-locked), locked cant login, we can lock a user if
	// found fraud,
	private boolean lockStatus;

	// to save in encryped way, others even admin cant see it
	private String password;
	private String city;
	private String dob; // date of birth
	private String otp; // otp send to login/forgot password/change password
	private int otpCount; // future purpose(restrict a user for no of sms sent)
	private Date otpDatetime;
	private String otpStatus;
	private double walletBalance;// always this be updated
	private double rewardPoint;
	private String regMode;// registration mode, not to change its value after user registration
	private String ipaddress;// ipaddress when user registered

	// will use to know for which service otp demanded, will verify otp for same
	// service
	private String otpServicename;
	private String otpMode;// web or app mode for demanding otp
	private String cashbackEarned;
	private String role;

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getTnc() {
		return tnc;
	}

	public void setTnc(String tnc) {
		this.tnc = tnc;
	}

	public boolean isActivatedStatus() {
		return activatedStatus;
	}

	public void setActivatedStatus(boolean activatedStatus) {
		this.activatedStatus = activatedStatus;
	}

	public boolean isLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(boolean lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
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

	public Date getOtpDatetime() {
		return otpDatetime;
	}

	public void setOtpDatetime(Date otpDatetime) {
		this.otpDatetime = otpDatetime;
	}

	public String getOtpStatus() {
		return otpStatus;
	}

	public void setOtpStatus(String otpStatus) {
		this.otpStatus = otpStatus;
	}

	public double getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(double walletBalance) {
		this.walletBalance = walletBalance;
	}

	public double getRewardPoint() {
		return rewardPoint;
	}

	public void setRewardPoint(double rewardPoint) {
		this.rewardPoint = rewardPoint;
	}

	public String getRegMode() {
		return regMode;
	}

	public void setRegMode(String regMode) {
		this.regMode = regMode;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getOtpServicename() {
		return otpServicename;
	}

	public void setOtpServicename(String otpServicename) {
		this.otpServicename = otpServicename;
	}

	public String getOtpMode() {
		return otpMode;
	}

	public void setOtpMode(String otpMode) {
		this.otpMode = otpMode;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCashbackEarned() {
		return cashbackEarned;
	}

	public void setCashbackEarned(String cashbackEarned) {
		this.cashbackEarned = cashbackEarned;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
