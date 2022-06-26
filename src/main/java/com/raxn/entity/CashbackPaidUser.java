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
public class CashbackPaidUser implements Serializable {

	private static final long serialVersionUID = -4882066856075508431L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Column(unique = true)
	private String username; //

	// all applied coupons(instant,recurring,percentage) info
	@Lob
	private String cbInstantInfo; // [map object--{code:datetime,mode,orderid},....]

	// only recurring coupons applied info
	@Lob
	private String cbRecurringInfo;// [{code:datetime,mode,orderid}...]

	// cashback completed recurring couppons info
	@Lob
	private String cbPercentInfo;// [{code:datetime,mode,orderid},...]

	private String cbtotalamount;

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

	public String getCbInstantInfo() {
		return cbInstantInfo;
	}

	public void setCbInstantInfo(String cbInstantInfo) {
		this.cbInstantInfo = cbInstantInfo;
	}

	public String getCbRecurringInfo() {
		return cbRecurringInfo;
	}

	public void setCbRecurringInfo(String cbRecurringInfo) {
		this.cbRecurringInfo = cbRecurringInfo;
	}

	public String getCbPercentInfo() {
		return cbPercentInfo;
	}

	public void setCbPercentInfo(String cbPercentInfo) {
		this.cbPercentInfo = cbPercentInfo;
	}

	public String getCbtotalamount() {
		return cbtotalamount;
	}

	public void setCbtotalamount(String cbtotalamount) {
		this.cbtotalamount = cbtotalamount;
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
