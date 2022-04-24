package com.raxn.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class CouponApplied implements Serializable {

	private static final long serialVersionUID = -4882066856075508431L;

	@Id
	@GeneratedValue
	private int id;

	@NotBlank
	@Column(unique = true)
	private String userid; //

	private String successCouponInfo; // [map object--{code:datetime,mode,orderid},....]
	private String pendingCouponInfo;// [{code:datetime,mode},...]
	private String pendingCashbackInfo;// [{code:cashbackamount},...]

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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSuccessCouponInfo() {
		return successCouponInfo;
	}

	public void setSuccessCouponInfo(String successCouponInfo) {
		this.successCouponInfo = successCouponInfo;
	}

	public String getPendingCouponInfo() {
		return pendingCouponInfo;
	}

	public void setPendingCouponInfo(String pendingCouponInfo) {
		this.pendingCouponInfo = pendingCouponInfo;
	}

	public String getPendingCashbackInfo() {
		return pendingCashbackInfo;
	}

	public void setPendingCashbackInfo(String pendingCashbackInfo) {
		this.pendingCashbackInfo = pendingCashbackInfo;
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
