package com.raxn.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class CouponAppliedUser implements Serializable {

	private static final long serialVersionUID = -4882066856075508431L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Column(unique = true)
	private String username; //

	// all applied coupons(instant,recurring,percentage) info
	@Lob
	private String successallcouponsinfo; // [map object--{code:datetime,mode,orderid},....]

	// only recurring coupons applied info
	@Lob
	private String recurringcouponsinfo;// [{code:datetime,mode,orderid},...]

	@Lob
	private String recurringCbPendingCode;// only code

	// cashback completed recurring couppons info
	@Lob
	private String recurringCbcompletedCode;// [code1,code2,code3,...]

	@Lob
	private String recurringCbchainbreakCode;// [code1,code2,code3,...]

	@Lob
	private String remark;// [code1,code2,code3,...]

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSuccessallcouponsinfo() {
		return successallcouponsinfo;
	}

	public void setSuccessallcouponsinfo(String successallcouponsinfo) {
		this.successallcouponsinfo = successallcouponsinfo;
	}

	public String getRecurringcouponsinfo() {
		return recurringcouponsinfo;
	}

	public void setRecurringcouponsinfo(String recurringcouponsinfo) {
		this.recurringcouponsinfo = recurringcouponsinfo;
	}

	public String getRecurringCbPendingCode() {
		return recurringCbPendingCode;
	}

	public void setRecurringCbPendingCode(String recurringCbPendingCode) {
		this.recurringCbPendingCode = recurringCbPendingCode;
	}

	public String getRecurringCbcompletedCode() {
		return recurringCbcompletedCode;
	}

	public void setRecurringCbcompletedCode(String recurringCbcompletedCode) {
		this.recurringCbcompletedCode = recurringCbcompletedCode;
	}

	public String getRecurringCbchainbreakCode() {
		return recurringCbchainbreakCode;
	}

	public void setRecurringCbchainbreakCode(String recurringCbchainbreakCode) {
		this.recurringCbchainbreakCode = recurringCbchainbreakCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

}
